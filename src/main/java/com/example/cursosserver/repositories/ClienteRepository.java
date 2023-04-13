package com.example.cursosserver.repositories;

import com.example.cursosserver.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByNome(String nome);
    boolean existsByNome(String nome);
    void deleteByNome(String nome);
    List<Cliente> findAllByNome(String nome);
}
