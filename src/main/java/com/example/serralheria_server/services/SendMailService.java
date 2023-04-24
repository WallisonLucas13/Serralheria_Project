package com.example.serralheria_server.services;

import com.example.serralheria_server.dtos.OrcamentoAdressTo;
import com.example.serralheria_server.models.Servico;
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

    @Value("${EMPRESA}")
    private String empresa;

    @Value("${CNPJ}")
    private String cnpj;

    @Value("${TELEFONE}")
    private String telefone;

    @Value("${EMPRESA_SUBTITLE}")
    private String empresaSubTitle;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private com.example.serralheria_server.services.MailBody mailBody;

    private final com.example.serralheria_server.services.CreateAttachmentFile createAttachmentFile = new com.example.serralheria_server.services.CreateAttachmentFile();
    private final com.example.serralheria_server.services.CodeKeyGenerator codeKeyGenerator = new com.example.serralheria_server.services.CodeKeyGenerator();

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
            e.printStackTrace();
            return "";
        }
    }

    public void createMailAndSendWithAttachments(OrcamentoAdressTo adress, Servico servico) {

        try {
            String orcamento = createAttachmentFile.create(servico,
                    adress, mailCompany, empresa, empresaSubTitle, cnpj, telefone);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            ResourceLoader resourceLoader = new DefaultResourceLoader();

            helper.setSubject("Orçamento");
            helper.setFrom("Serralheria");
            helper.setTo(adress.getAdress());


            Resource file = resourceLoader.getResource("file:files\\" + orcamento);
            helper.addAttachment(file.getFilename(), file);
            helper.setText(mailBody.attachmentBody());
            javaMailSender.send(mimeMessage);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
