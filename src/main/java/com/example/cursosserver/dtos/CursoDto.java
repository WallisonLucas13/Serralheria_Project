package com.example.cursosserver.dtos;

import com.example.cursosserver.models.Curso;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursoDto {
    @NotBlank
    private String nome;
    @NotBlank
    private String categoria;

    public Curso tranform(){
        return new Curso(this.nome, this.categoria);
    }
}
