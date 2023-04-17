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
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        return "Nós da Serralheria Qualidade e Pontualidade Agradecemos por utilizar nossos serviços! Estamos focados em fornecer o melhor para nossos clientes!\n\n\n" +
                "Data de Emissão: " + format.format(date);
    }

    public String bodyKeyMail(String code){
        return "Olá ADM, aqui está o seu código temporário!\\" + code;
    }

    public String titleMail(String adress){
        return "Orçamento";
    }
}
