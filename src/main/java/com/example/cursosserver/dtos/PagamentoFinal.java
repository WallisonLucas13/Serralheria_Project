package com.example.cursosserver.dtos;

import com.example.cursosserver.enums.FormaPagamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoFinal {

    public PagamentoFinal(String valor, FormaPagamento formaPagamento) {
        this.valor = valor;
        this.formaPagamento = formaPagamento;
    }

    private String valor;

    private FormaPagamento formaPagamento;
}
