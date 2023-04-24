package com.example.serralheria_server.dtos;

import com.example.serralheria_server.models.Material;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MaterialDto {

    private String nome;

    private int quant;

    private int valor;

    public Material transform(){
        return new Material(nome, quant, valor);
    }

}
