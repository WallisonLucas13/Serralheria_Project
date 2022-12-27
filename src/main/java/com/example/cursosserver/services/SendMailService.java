package com.example.cursosserver.services;

import com.example.cursosserver.models.Curso;
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

    public void createMailAndSend(String adress, Curso curso){

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(adress);
            mail.setSubject(mailBody.titleMail(adress));
            mail.setText(mailBody.bodyMail(curso));
            javaMailSender.send(mail);
        }
        catch (Exception e){
            System.out.println("Encontramos problemas para enviar o Email! Tente Novamente!");
            System.out.println(e.getMessage());
        }
    }

    public void createMailAndSendWithAttachments(String adress, Curso curso){

        try {
            String relatorio = createAttachmentFile.create(curso);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setSubject(mailBody.titleMail(adress));
            helper.setText(mailBody.attachmentBody());
            helper.setTo(adress);

            Resource file = resourceLoader.getResource("file:src\\main\\java\\com\\example\\cursosserver\\files\\" + relatorio);
            System.out.println(file.contentLength());
            helper.addAttachment(file.getFilename(), file);
            javaMailSender.send(mimeMessage);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
