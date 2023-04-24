package com.example.serralheria_server.services;

import com.example.serralheria_server.dtos.Entrada;
import com.example.serralheria_server.dtos.OrcamentoAdressTo;
import com.example.serralheria_server.dtos.PagamentoFinal;
import com.example.serralheria_server.dtos.ValoresServico;
import com.example.serralheria_server.enums.FormaPagamento;
import com.example.serralheria_server.exceptions.ObjetoInexistenteException;
import com.example.serralheria_server.models.Material;
import com.example.serralheria_server.models.Servico;
import com.example.serralheria_server.repositories.ServicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository repository;

    @Autowired
    private com.example.serralheria_server.services.ClienteService clienteService;

    @Autowired
    private com.example.serralheria_server.services.SendMailService sendMailService;

    @Transactional
    public List<Servico> listarTodos(Long idCliente) throws RuntimeException{
        return clienteService.listarTodosServicos(idCliente);
    }

    @Transactional
    public void salvarServico(Servico servico, Long idCliente) throws RuntimeException{

        this.clienteService.addServicoInClient(servico, idCliente);
    }

    @Transactional
    public void atualizarServico(Servico servico, Long id) throws ObjetoInexistenteException{

        boolean exist = repository.existsById(id);

        if(!exist){throw new ObjetoInexistenteException("Servico Inexistente!");}

        servico.setId(id);
        repository.save(servico);
    }
    @Transactional
    public void apagarServico(Long id) throws ObjetoInexistenteException{

        if(!repository.existsById(id)){
            throw new ObjetoInexistenteException("Serviço Inexistente!");
        }

        repository.deleteById(id);
    }

    @Transactional
    public void addMaterialInServico(Material material, Long idServico){

        Servico servico = repository.findById(idServico)
                .orElseThrow(() -> new ObjetoInexistenteException("Inexistente"));;

        List<Material> novaLista = servico.getMateriais();
        novaLista.add(material);
        servico.setMateriais(novaLista);
        servico.setValorTotalMateriais(calcularValorTotalMateriais(servico.getMateriais()));
        servico.setValorFinal(servico.getMaoDeObra() + servico.getValorTotalMateriais());
        servico.setValorEntrada(String.valueOf((servico.getValorFinal()*Integer.parseInt(servico.getPorcentagemEntrada()))/100));
        servico.setValorPagamentoFinal(String.valueOf(servico.getValorFinal() - Integer.parseInt(servico.getValorEntrada())));
        repository.save(servico);

    }

    @Transactional
    public void apagarMaterialInServico(Material material){

        List<Servico> todos = repository.findAll();

        for(int i=0; i<todos.size(); i++){

            if(todos.get(i).getMateriais().contains(material)){

                List<Material> materiais = todos.get(i).getMateriais();
                materiais.remove(material);
                todos.get(i).setValorTotalMateriais(calcularValorTotalMateriais(materiais));
                todos.get(i).setValorFinal(todos.get(i).getValorTotalMateriais() + todos.get(i).getMaoDeObra());
                todos.get(i).setValorEntrada(String.valueOf((todos.get(i).getValorFinal()*Integer.parseInt(todos.get(i).getPorcentagemEntrada()))/100));
                todos.get(i).setValorPagamentoFinal(String.valueOf(todos.get(i).getValorFinal() - Integer.parseInt(todos.get(i).getValorEntrada())));
                repository.save(todos.get(i));
            }
        }
    }

    public List<Material> listarTodosMateriais(Long id){
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ObjetoInexistenteException("Inexistente"));

        return servico.getMateriais();
    }

    public int calcularValorTotalMateriais(List<Material> materiais){

        int valor = 0;

        for (int i = 0; i < materiais.size(); i++) {
            valor += materiais.get(i).getValor() * materiais.get(i).getQuant();
        }

        return valor;
    }

    @Transactional
    public void setMaoDeObra(int maoDeObra, Long id){
        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ObjetoInexistenteException("Inexistente"));

        servico.setMaoDeObra(maoDeObra);
        servico.setValorFinal(servico.getMaoDeObra() + servico.getValorTotalMateriais());
        servico.setValorEntrada(String.valueOf((servico.getValorFinal()*Integer.parseInt(servico.getPorcentagemEntrada()))/100));
        servico.setValorPagamentoFinal(String.valueOf(servico.getValorFinal() - Integer.parseInt(servico.getValorEntrada())));
        repository.save(servico);
    }

    @Transactional
    public ValoresServico getValores(Long id){

        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ObjetoInexistenteException("Inexistente"));


        ValoresServico valoresServico = new ValoresServico();
        valoresServico.setValor(servico.getMaoDeObra());
        valoresServico.setValorTotalMateriais(servico.getValorTotalMateriais());
        aplicarDesconto(servico.getId(), servico.getDesconto());
        valoresServico.setValorFinal(servico.getValorFinal());
        valoresServico.setDesconto(servico.getDesconto());
        valoresServico.setEntrada(new Entrada(servico.getPorcentagemEntrada(), servico.getValorEntrada(), servico.getFormaPagamentoEntrada().name()));
        valoresServico.setPagamentoFinal(new PagamentoFinal(servico.getValorPagamentoFinal(), servico.getFormaPagamentoFinal().name()));
        return valoresServico;
    }

    @Transactional
    public void aplicarDesconto(Long id, int desconto){

        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ObjetoInexistenteException("Inexistente"));

        servico.setValorFinal(servico.getMaoDeObra() + servico.getValorTotalMateriais());

        double desc = ((double) desconto / 100) * servico.getValorFinal();

        servico.setValorFinal((int) (servico.getValorFinal() - desc));

        servico.setDesconto(desconto);
        servico.setValorEntrada(String.valueOf((servico.getValorFinal()*Integer.parseInt(servico.getPorcentagemEntrada()))/100));
        servico.setValorPagamentoFinal(String.valueOf(servico.getValorFinal() - Integer.parseInt(servico.getValorEntrada())));
        repository.save(servico);
    }

    @Transactional
    public void sendOrcamento(Long id, OrcamentoAdressTo adress){

        Servico servico = repository.findById(id)
                .orElseThrow(() -> new ObjetoInexistenteException("Inexistente"));;

        sendMailService.createMailAndSendWithAttachments(adress, servico);
    }

    @Transactional
    public void sendEntrada(Entrada entrada, Long idServico){

        Servico servico = repository.findById(idServico)
                .orElseThrow(() -> new ObjetoInexistenteException("Inexistente"));

        servico.setValorFinal(servico.getMaoDeObra() + servico.getValorTotalMateriais());

        servico.setPorcentagemEntrada(String.valueOf(entrada.getPorcentagem()));
        servico.setValorEntrada(String.valueOf((servico.getValorFinal()*Integer.parseInt(entrada.getPorcentagem()))/100));
        servico.setFormaPagamentoEntrada(formatarFormaPagamento(entrada.getFormaPagamento()));
        servico.setValorPagamentoFinal(String.valueOf(servico.getValorFinal() - Integer.parseInt(servico.getValorEntrada())));
        repository.save(servico);
    }

    @Transactional
    public void sendFormaPagamentoFinal(PagamentoFinal pagamentoFinal, Long idServico){

        Servico servico = repository.findById(idServico)
                .orElseThrow(() -> new ObjetoInexistenteException("Inexistente"));

        servico.setFormaPagamentoFinal(formatarFormaPagamento(pagamentoFinal.getFormaPagamento()));
        repository.save(servico);
    }

    private FormaPagamento formatarFormaPagamento(String forma){

        switch (forma){
            case "PIX": return FormaPagamento.PIX;
            case "DEBITO": return FormaPagamento.DEBITO;
            case "CREDITO": return FormaPagamento.CREDITO;
            case "DINHEIRO": return FormaPagamento.DINHEIRO;

            default: return FormaPagamento.NENHUMA;
        }
    }
}
