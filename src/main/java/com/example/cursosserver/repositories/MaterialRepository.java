package com.example.cursosserver.repositories;

import com.example.cursosserver.models.Material;
import com.example.cursosserver.models.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    public List<Material> findAllByServico(Servico servico);
}
