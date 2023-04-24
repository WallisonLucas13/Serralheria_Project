package com.example.serralheria_server.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValoresServico {

    private int valor;

    private int valorTotalMateriais;

    private int valorFinal;

    private int desconto;

    private com.example.serralheria_server.dtos.Entrada entrada;

    private com.example.serralheria_server.dtos.PagamentoFinal pagamentoFinal;
}
