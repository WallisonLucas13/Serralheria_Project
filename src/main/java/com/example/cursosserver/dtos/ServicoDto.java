package com.example.cursosserver.dtos;

import com.example.cursosserver.models.Servico;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoDto {

    private String nome;
    private String desc;

    public Servico transform(){
        return new Servico(nome, desc);
    }
}
