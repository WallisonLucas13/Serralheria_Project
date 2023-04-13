package com.example.cursosserver.repositories;

import com.example.cursosserver.models.Cliente;
import com.example.cursosserver.models.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    public List<Servico> findAllByCliente(Cliente cliente);
}
