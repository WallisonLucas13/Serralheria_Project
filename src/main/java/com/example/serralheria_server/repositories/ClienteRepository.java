package com.example.serralheria_server.repositories;

import com.example.serralheria_server.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByNome(String nome);

}
