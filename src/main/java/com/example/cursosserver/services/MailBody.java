package com.example.cursosserver.services;

import com.example.cursosserver.models.Cliente;
import org.springframework.stereotype.Service;

@Service
public class MailBody {

    public String bodyMail(Cliente cliente) {
        return "";
    }

    public String attachmentBody(){
        return "Segue abaixo o orçamento com todos os detalhes do serviço!\n";
    }

    public String titleMail(String adress){
        return "Orçamento";
    }
}
