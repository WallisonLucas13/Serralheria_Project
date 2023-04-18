package com.example.cursosserver.services;

import com.example.cursosserver.dtos.OrcamentoAdressTo;
import com.example.cursosserver.models.Cliente;
import com.example.cursosserver.models.Servico;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendMailService{

    @Value("${MAIL_ADRESS_KEY}")
    private String mailAdressKey;

    @Value("${MAIL_COMPANY}")
    private String mailCompany;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailBody mailBody;

    private CreateAttachmentFile createAttachmentFile = new CreateAttachmentFile();
    private CodeKeyGenerator codeKeyGenerator = new CodeKeyGenerator();

    public String createMailAndSend(){

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(mailAdressKey);
            mail.setSubject("Código de Acesso");
            String code = codeKeyGenerator.gerarKey();
            mail.setText(mailBody.bodyKeyMail(code));
            javaMailSender.send(mail);
            return code;
        }
        catch (Exception e){
            System.out.println("Encontramos problemas para enviar o Email! Tente Novamente!");
            return "";
        }
    }

    public void createMailAndSendWithAttachments(OrcamentoAdressTo adress, Cliente cliente, Servico servico) {

        try {
            String orcamento = createAttachmentFile.create(cliente, servico, adress, mailCompany);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            ResourceLoader resourceLoader = new DefaultResourceLoader();

            helper.setSubject(mailBody.titleMail(adress.getAdress()));
            helper.setFrom("Serralheria");
            helper.setTo(adress.getAdress());


            Resource file = resourceLoader.getResource("file:files\\" + orcamento);
            helper.addAttachment(file.getFilename(), file);
            helper.setText(mailBody.attachmentBody());
            javaMailSender.send(mimeMessage);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
