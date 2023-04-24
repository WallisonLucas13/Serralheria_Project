package com.example.serralheria_server.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrcamentoAdressTo{

    private String adress;

    private Long idCliente;

    private boolean ocultarMateriais;

    private boolean ocultarMaoDeObra;

    private boolean ocultarDesconto;
}
