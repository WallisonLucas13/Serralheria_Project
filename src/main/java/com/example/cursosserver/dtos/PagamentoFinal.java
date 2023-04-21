package com.example.cursosserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoFinal {

    public PagamentoFinal(String valor,String formaPagamento) {
        this.valor = valor;
        this.formaPagamento = formaPagamento;
    }

    private String valor;

    private String formaPagamento;
}
