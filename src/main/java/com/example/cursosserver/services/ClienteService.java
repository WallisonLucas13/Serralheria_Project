package com.example.cursosserver.services;

import com.example.cursosserver.exceptions.ObjetoInexistenteException;
import com.example.cursosserver.models.Cliente;
import com.example.cursosserver.models.Servico;
import com.example.cursosserver.repositories.ClienteRepository;
import jakarta.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;
    @Autowired
    private SendMailService sendMailService;

    private final String adressMail = "lucaswalison136@gmail.com";

    @Transactional
    public List<Cliente> listarTodos() throws RuntimeException{
        return this.repository.findAll();
    }

    @Transactional
    public Cliente getById(Long id) throws ObjetoInexistenteException {

        boolean exist = repository.existsById(id);

        if(!exist){
            throw new ObjetoInexistenteException("Cliente Inexistente!");
        }
        return repository.findById(id).get();
    }

    @Transactional
    public void salvarCliente(Cliente cliente) throws RuntimeException{

        boolean exist = repository.existsByNome(cliente.getNome());
        if(exist){throw new RuntimeException();}

        System.out.println("Curso: [ "+ cliente.getNome() + " ] Salvo!");
        this.repository.save(cliente);
        //sendMailService.createMailAndSendWithAttachments(adressMail, cliente);
    }

    @Transactional
    public void atualizarCliente(Cliente cliente, Long id) throws ObjetoInexistenteException{

        boolean exist = repository.existsById(id);

        if(!exist){throw new ObjetoInexistenteException("Cliente Inexistente!");}

        cliente.setId(id);
        repository.save(cliente);
    }
    @Transactional
    public void apagarCliente(Long id) throws ObjetoInexistenteException{

        if(!repository.existsById(id)){
            throw new ObjetoInexistenteException("Cliente Inexistente!");
        }

        repository.deleteById(id);
    }

    @Transactional
    public void addServicoInClient(Servico servico, Long idCliente){

        Cliente cliente = repository.findById(idCliente).get();

        List<Servico> novaLista = cliente.getServicos();
        novaLista.add(servico);
        cliente.setServicos(novaLista);

        repository.save(cliente);
    }

    public List<Servico> listarTodosServicos(Long id){
        Cliente cliente = repository.findById(id).get();
        return cliente.getServicos();
    }
}
