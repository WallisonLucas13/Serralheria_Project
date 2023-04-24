package com.example.serralheria_server.dtos;

import com.example.serralheria_server.models.Servico;
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
