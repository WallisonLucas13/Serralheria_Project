package com.example.cursosserver.repositories;

import com.example.cursosserver.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByNome(String nome);

}
