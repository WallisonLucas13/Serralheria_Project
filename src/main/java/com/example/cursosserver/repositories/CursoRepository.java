package com.example.cursosserver.repositories;

import com.example.cursosserver.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Curso findByNome(String nome);
    boolean existsByNome(String nome);
    void deleteByNome(String nome);
    List<Curso> findAllByNome(String nome);
}
