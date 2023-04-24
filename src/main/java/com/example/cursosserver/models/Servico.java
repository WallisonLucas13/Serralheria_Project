package com.example.cursosserver.models;

import com.example.cursosserver.enums.FormaPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Servico {

    public Servico(String nome, String desc){
        this.desc = desc;
        this.nome = nome;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nome;
    @Column(name = "sobre")
    private String desc;
    @Column
    private int valorFinal;

    @Column
    private int valorTotalMateriais;

    @Column
    private int maoDeObra;

    @Column
    private int desconto = 0;

    @Column
    private String porcentagemEntrada = "0";

    @Column
    private String valorEntrada = "0";

    @Column
    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamentoEntrada = FormaPagamento.NENHUMA;

    @Column
    private String valorPagamentoFinal = "0";

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamentoFinal = FormaPagamento.NENHUMA;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Material> materiais;

}
