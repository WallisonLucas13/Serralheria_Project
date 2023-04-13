package com.example.cursosserver.services;

import com.example.cursosserver.models.Cliente;
import org.springframework.stereotype.Service;

@Service
public class MailBody {

    public String bodyMail(Cliente cliente) {
        //return "Olá, um novo curso foi cadastrado no nosso Sistema segue os detalhes abaixo:\n" +
                //"Nome: " + cliente.getNome() + "\n" +
                //"Categoria: " + cliente.getCategoria();
        return "";
    }

    public String attachmentBody(){
        return "Segue abaixo o relatório com todos os detalhes!\n";
    }

    public String titleMail(String adress){
        return "Relatório";
    }
}
