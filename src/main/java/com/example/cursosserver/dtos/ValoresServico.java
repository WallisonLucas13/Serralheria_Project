package com.example.cursosserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValoresServico {

    private int valor;

    private int valorTotalMateriais;

    private int valorFinal;

    private int desconto;

    private Entrada entrada;

    private PagamentoFinal pagamentoFinal;
}
