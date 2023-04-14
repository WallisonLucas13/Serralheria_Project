package com.example.cursosserver.services;

import com.example.cursosserver.models.Cliente;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MailBody {

    public String bodyMail(Cliente cliente) {
        return "";
    }

    public String attachmentBody(){
        return "Boa tarde! Aqui está o orçamento com todos os detalhes do serviço!\n";
    }

    public String attachmentBodyEnd(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        return "Nós da Serralheria Qualidade e Pontualidade Agradecemos por consumir nossos serviços! Estamos focados em fornecer o melhor para nossos clientes!\n\n" +
                "O Senhor é meu pastor:nada me faltará\nSalmos 23:1\n\n" +
                "#Serralheria Qualidade e Pontualidade\n\n" +
                "Data de Emissão: " + format.format(date);
    }

    public String titleMail(String adress){
        return "Orçamento";
    }
}
