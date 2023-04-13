package com.example.cursosserver.services;

import com.example.cursosserver.dtos.OrcamentoAdressTo;
import com.example.cursosserver.dtos.ValoresServico;
import com.example.cursosserver.exceptions.ObjetoInexistenteException;
import com.example.cursosserver.models.Cliente;
import com.example.cursosserver.models.Material;
import com.example.cursosserver.models.Servico;
import com.example.cursosserver.repositories.ServicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository repository;

    @Autowired
    private ClienteService clienteService;


    @Autowired
    private SendMailService sendMailService;

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

        System.out.println(exist);

        System.out.println(id);
        servico.setId(id);
        repository.save(servico);
    }
    @Transactional
    public void apagarServico(Long id) throws ObjetoInexistenteException{

        if(!repository.existsById(id)){
            throw new ObjetoInexistenteException("Cliente Inexistente!");
        }

        System.out.println(id);
        repository.deleteById(id);
    }

    @Transactional
    public Servico getById(Long id) throws ObjetoInexistenteException{
        return repository.findById(id).or(() -> {
            throw new ObjetoInexistenteException("");
        }).get();
    }

    @Transactional
    public void addMaterialInServico(Material material, Long idServico){

        Servico servico = repository.findById(idServico).get();

        List<Material> novaLista = servico.getMateriais();
        novaLista.add(material);
        servico.setMateriais(novaLista);
        servico.setValorTotalMateriais(calcularValorTotalMateriais(servico.getMateriais()));
        servico.setValorFinal(servico.getMaoDeObra() + servico.getValorTotalMateriais());
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
                repository.save(todos.get(i));
            }
        }
    }

    public List<Material> listarTodosMateriais(Long id){
        Servico servico = repository.findById(id).get();
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
        Servico servico = repository.findById(id).get();
        servico.setMaoDeObra(maoDeObra);
        servico.setValorFinal(servico.getMaoDeObra() + servico.getValorTotalMateriais());
        repository.save(servico);
    }

    @Transactional
    public ValoresServico getValores(Long id){

        Servico servico = repository.findById(id).get();

        ValoresServico valoresServico = new ValoresServico();
        valoresServico.setValor(servico.getMaoDeObra());
        valoresServico.setValorTotalMateriais(servico.getValorTotalMateriais());
        aplicarDesconto(servico.getId(), servico.getDesconto());
        valoresServico.setValorFinal(servico.getValorFinal());
        valoresServico.setDesconto(servico.getDesconto());
        return valoresServico;
    }

    @Transactional
    public void aplicarDesconto(Long id, int desconto){

        Servico servico = repository.findById(id).get();

        servico.setValorFinal(servico.getMaoDeObra() + servico.getValorTotalMateriais());
        repository.save(servico);

        double desc = ((double) desconto / 100) * servico.getValorFinal();

        servico.setValorFinal((int) (servico.getValorFinal() - desc));

        servico.setDesconto(desconto);
        repository.save(servico);
    }

    @Transactional
    public void sendOrcamento(Long id, OrcamentoAdressTo adress){

        Cliente cliente = clienteService.getById(adress.getIdCliente());
        Servico servico = repository.findById(id).get();

        System.out.println("Nome Cliente: " + cliente.getNome());
        System.out.println("Nome Serviço: " + servico.getNome());

        sendMailService.createMailAndSendWithAttachments(adress, cliente, servico);
    }
}
