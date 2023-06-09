package com.example.serralheria_server.services;

import com.example.serralheria_server.exceptions.ObjetoInexistenteException;
import com.example.serralheria_server.models.Material;
import com.example.serralheria_server.repositories.MaterialRepository;
import jakarta.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository repository;

    @Autowired
    private com.example.serralheria_server.services.ServicoService servicoService;

    @Transactional
    public List<Material> listarTodos(Long idServico) throws RuntimeException{
        return servicoService.listarTodosMateriais(idServico);
    }

    @Transactional
    public void salvarMaterial(Material material, Long idServico) throws RuntimeException{

        this.servicoService.addMaterialInServico(material, idServico);
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

        servicoService.apagarMaterialInServico(repository.findById(id)
                .orElseThrow(() -> new ObjetoInexistenteException("Inexistente")));

        repository.deleteById(id);
    }
}
