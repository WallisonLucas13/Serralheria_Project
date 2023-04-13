package com.example.cursosserver.services;

import com.example.cursosserver.exceptions.ObjetoInexistenteException;
import com.example.cursosserver.models.Material;
import com.example.cursosserver.repositories.MaterialRepository;
import jakarta.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    @Autowired
    private ServicoService servicoService;

    @Transactional
    public List<Material> listarTodos(Long idServico) throws RuntimeException{
        return servicoService.listarTodosMateriais(idServico);
    }

    @Transactional
    public void salvarMaterial(Material material, Long idServico) throws RuntimeException{

        this.servicoService.addMaterialInServico(material, idServico);
        //sendMailService.createMailAndSendWithAttachments(adressMail, cliente);
    }

    @Transactional
    public void atualizarMaterial(Material material, Long id) throws ObjetoInexistenteException {

        boolean exist = repository.existsById(id);

        if(!exist){throw new ObjetoInexistenteException("Material Inexistente!");}

        material.setId(id);
        repository.save(material);
    }

    @Transactional
    public void apagarMaterial(Long id) throws ObjetoInexistenteException{

        if(!repository.existsById(id)){
            throw new ObjetoInexistenteException("Cliente Inexistente!");
        }

        servicoService.apagarMaterialInServico(repository.findById(id).get());
        repository.deleteById(id);
    }
}
