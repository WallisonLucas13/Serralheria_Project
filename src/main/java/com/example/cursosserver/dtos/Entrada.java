package com.example.cursosserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Entrada {

    private String porcentagem;

    public Entrada(String porcentagem, String valor, String formaPagamento) {
        this.porcentagem = porcentagem;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
    }

    private String valor;

    private String formaPagamento;
}
