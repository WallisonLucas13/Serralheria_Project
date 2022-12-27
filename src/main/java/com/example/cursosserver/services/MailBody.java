package com.example.cursosserver.services;

import com.example.cursosserver.models.Curso;
import org.springframework.stereotype.Service;

@Service
public class MailBody {

    public String bodyMail(Curso curso) {
        return "Olá, um novo curso foi cadastrado no nosso Sistema segue os detalhes abaixo:\n" +
                "Nome: " + curso.getNome() + "\n" +
                "Categoria: " + curso.getCategoria();
    }

    public String attachmentBody(){
        return "Segue abaixo o relatório com todos os detalhes!\n";
    }

    public String titleMail(String adress){
        return "Relatório";
    }
}
