package com.example.cursosserver.services;

import com.example.cursosserver.dtos.OrcamentoAdressTo;
import com.example.cursosserver.models.Cliente;
import com.example.cursosserver.models.Servico;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendMailService{

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailBody mailBody;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CreateAttachmentFile createAttachmentFile;

    public void createMailAndSend(String adress, Cliente cliente){

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(adress);
            mail.setSubject(mailBody.titleMail(adress));
            mail.setText(mailBody.bodyMail(cliente));
            javaMailSender.send(mail);
        }
        catch (Exception e){
            System.out.println("Encontramos problemas para enviar o Email! Tente Novamente!");
            System.out.println(e.getMessage());
        }
    }

    public void createMailAndSendWithAttachments(OrcamentoAdressTo adress, Cliente cliente, Servico servico) {

        try {
            String orcamento = createAttachmentFile.create(cliente, servico, adress);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setSubject(mailBody.titleMail(adress.getAdress()));
            helper.setText(mailBody.attachmentBody());
            helper.setFrom(createAttachmentFile.email);
            helper.setTo(adress.getAdress());

            Resource file = resourceLoader.getResource("file:files\\" + orcamento);
            helper.addAttachment(file.getFilename(), file);
            helper.setText(mailBody.attachmentBodyEnd());
            javaMailSender.send(mimeMessage);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

}
