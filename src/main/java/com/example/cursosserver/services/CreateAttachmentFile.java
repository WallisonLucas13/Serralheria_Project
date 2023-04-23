package com.example.cursosserver.services;

import com.example.cursosserver.dtos.OrcamentoAdressTo;
import com.example.cursosserver.models.Cliente;
import com.example.cursosserver.models.Material;
import com.example.cursosserver.models.Servico;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CreateAttachmentFile {

    @Value("${EMPRESA}")
    private String empresa;

    @Value("${CNPJ}")
    private String cnpj;

    @Value("${EMAIL}")
    private String email;

    @Value("${TELEFONE}")
    private String telefone;

    public String create(Cliente cliente, Servico servico, OrcamentoAdressTo orcamentoAdressTo, String mailCompany) throws DocumentException, IOException {

        Document document = new Document();

        String documentName = "Orcamento.pdf";

        List<Material> materiais = servico.getMateriais();

        boolean ocultarMateriais = orcamentoAdressTo.isOcultarMateriais();
        boolean ocultarMaoDeObra = orcamentoAdressTo.isOcultarMaoDeObra();
        boolean ocultarDesconto = orcamentoAdressTo.isOcultarDesconto();

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("file:files\\" + documentName);
        PdfWriter.getInstance(document, new FileOutputStream(resource.getFile()));

        Rectangle rectangle = new Rectangle(PageSize.A4);

        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorder(Rectangle.BOX);
        rectangle.setBorderWidth(3);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBackgroundColor(new BaseColor(34, 35, 35));

        document.setPageSize(rectangle);
        document.open();
        document.add(rectangle);

        //FONTS
        Font fontTrs = new Font(Font.FontFamily.HELVETICA);
        fontTrs.setStyle(Font.BOLD);
        fontTrs.setColor(new BaseColor(255, 187, 51));

        Font fontBody = new Font();
        fontBody.setSize(11);
        fontBody.setColor(new BaseColor(220, 221, 216));

        Font fontValues = new Font();
        fontValues.setColor(new BaseColor(0, 128, 0));

        Font fontImportant = new Font();
        fontImportant.setColor(new BaseColor(174, 6, 3));

        Font fontTitle = new Font();
        fontTitle.setStyle(Font.BOLD);
        fontTitle.setSize(25);
        fontTitle.setColor(new BaseColor(255, 187, 51));

        Font fontEndQuestions = new Font();
        fontEndQuestions.setColor(new BaseColor(244, 244, 244, 1));
        fontEndQuestions.setSize(12);

        Font fontEndQuestionsStyled = new Font();
        fontEndQuestionsStyled.setColor(new BaseColor(255, 187, 51));
        fontEndQuestionsStyled.setSize(12);

        //TITLE
        Paragraph title = new Paragraph(new Phrase(20f,"Serralheria Qualidade e Pontualidade", FontFactory.getFont(FontFactory.HELVETICA, 18F, new BaseColor(255, 187, 51))));
        title.setAlignment(Element.ALIGN_CENTER);
        Font fontDeLink = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(255, 187, 51));
        Paragraph subtitle = new Paragraph(new Phrase(14F, "Esquadrias Metálicas", fontDeLink));
        subtitle.setAlignment(Element.ALIGN_CENTER);

        document.addTitle("Orçamento");

        document.add(title);
        document.add(subtitle);
        //-------------------------------------------------------------------------------------------------

        //PRESTADOR HEADER
        Phrase headerPrestador = new Phrase("Prestador", fontEndQuestionsStyled);

        Paragraph prestador = new Paragraph(headerPrestador);
        prestador.setAlignment(Element.ALIGN_LEFT);
        document.add(prestador);
        //----------------------------------------------------------------------------------------------

        //EMPRESA
        Phrase headerEmpresa = new Phrase("Empresa: ", fontEndQuestions);
        Phrase bodyEmpresa = new Phrase(this.empresa, fontEndQuestions);
        headerEmpresa.add(bodyEmpresa);

        Paragraph empresa = new Paragraph(headerEmpresa);
        empresa.setAlignment(Element.ALIGN_LEFT);
        document.add(empresa);
        //----------------------------------------------------------------------------------------------

        //CNPJ
        Phrase headerCNPJ = new Phrase("Cnpj: ", fontEndQuestions);
        Phrase bodyCNPJ = new Phrase(this.cnpj, fontEndQuestions);
        headerCNPJ.add(bodyCNPJ);

        Paragraph cnpj = new Paragraph(headerCNPJ);
        cnpj.setAlignment(Element.ALIGN_LEFT);
        document.add(cnpj);
        //----------------------------------------------------------------------------------------------

        //EMAIL
        Phrase headerEmail = new Phrase("Email: ", fontEndQuestions);
        Phrase bodyEmail = new Phrase(this.email, fontEndQuestions);
        headerEmail.add(bodyEmail);

        Paragraph email = new Paragraph(headerEmail);
        email.setAlignment(Element.ALIGN_LEFT);
        document.add(email);
        //----------------------------------------------------------------------------------------------

        //TELEFONE
        Phrase headerTel = new Phrase("Cnpj: ", fontEndQuestions);
        Phrase bodyTel = new Phrase(this.telefone, fontEndQuestions);
        headerTel.add(bodyTel);

        Paragraph tel = new Paragraph(headerTel);
        tel.setAlignment(Element.ALIGN_LEFT);
        document.add(tel);
        //----------------------------------------------------------------------------------------------

        //SERVICO HEADER
        Phrase headerServico = new Phrase("Serviço", fontEndQuestionsStyled);

        Paragraph servicoP = new Paragraph(headerServico);
        servicoP.setAlignment(Element.ALIGN_LEFT);
        document.add(servicoP);
        //----------------------------------------------------------------------------------------------

        //NOME
        Phrase headerNome = new Phrase("Nome: ", fontEndQuestions);
        Phrase bodyNome = new Phrase(servico.getNome(), fontEndQuestions);
        headerNome.add(bodyNome);

        Paragraph nome = new Paragraph(headerNome);
        nome.setAlignment(Element.ALIGN_LEFT);
        document.add(nome);
        //----------------------------------------------------------------------------------------------

        //DESCRICAO
        Phrase headerDesc = new Phrase("Descrição: ", fontEndQuestions);
        Phrase bodyDesc = new Phrase(servico.getDesc(), fontEndQuestions);
        headerTel.add(bodyDesc);

        Paragraph desc = new Paragraph(headerDesc);
        desc.setAlignment(Element.ALIGN_LEFT);
        document.add(desc);
        //----------------------------------------------------------------------------------------------

        //MATERIAIS HEADER
        Phrase headerMateriais = new Phrase("Materiais", fontEndQuestionsStyled);

        Paragraph materiaisP = new Paragraph(headerMateriais);
        materiaisP.setAlignment(Element.ALIGN_LEFT);
        document.add(materiaisP);
        //----------------------------------------------------------------------------------------------

        //MATERIAL
        materiais.stream().forEach((material) -> {

            Phrase headerMaterial = new Phrase("Material: ", fontEndQuestions);
            Phrase bodyMaterial = new Phrase(formatarMaterial(material), fontEndQuestions);
            headerMaterial.add(bodyMaterial);

            Paragraph m = new Paragraph(headerMaterial);
            m.setAlignment(Element.ALIGN_LEFT);
            try {
                document.add(m);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }

        });
        //----------------------------------------------------------------------------------------------

        //MATERIAL TOTAL
        Phrase headerTotalM = new Phrase("Total: ", fontEndQuestionsStyled);
        Phrase bodyTotalM = new Phrase("R$ " + servico.getValorTotalMateriais() + ",00", fontEndQuestions);
        headerTotalM.add(bodyTotalM);

        Paragraph totalM = new Paragraph(headerTotalM);
        totalM.setAlignment(Element.ALIGN_LEFT);
        document.add(totalM);
        //----------------------------------------------------------------------------------------------
        
        //ENTRADA
        Phrase headerEntrada = new Phrase("Entrada: ", fontEndQuestionsStyled);
        Phrase bodyEntrada = new Phrase(servico.getPorcentagemEntrada() + "% / " + "R$ " + servico.getValorEntrada() + ",00", fontImportant);
        headerEntrada.add(bodyEntrada);

        Paragraph entrada = new Paragraph(headerEntrada);
        entrada.setAlignment(Element.ALIGN_LEFT);
        document.add(entrada);
        //----------------------------------------------------------------------------------------------

        //Total a pagar
        Phrase value = new Phrase("R$ " + servico.getValorPagamentoFinal() + ",00", fontValues);
        Phrase text = new Phrase("Total a pagar na conclusão do serviço: ", fontEndQuestions);
        text.add(value);

        Paragraph pagamentoFinal = new Paragraph(text);
        pagamentoFinal.setAlignment(Element.ALIGN_LEFT);
        document.add(pagamentoFinal);
        //-----------------------------------------------------------------------------------------------

        //Formas Pagamento
        Phrase headerPagamentoForm = new Phrase("Formas de Pagamento: ", fontEndQuestions);
        Phrase bodyPagamentoForm = new Phrase("DÉBITO | CRÉDITO | PIX | DINHEIRO", FontFactory.getFont(FontFactory.HELVETICA, 12, new BaseColor(255, 187, 51)));
        headerPagamentoForm.add(bodyPagamentoForm);

        Paragraph formaPagamentoFinal = new Paragraph(headerPagamentoForm);
        pagamentoFinal.setAlignment(Element.ALIGN_LEFT);
        document.add(formaPagamentoFinal);
        //------------------------------------------------------------------------------------------------

        //Data Emissão
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Paragraph time = new Paragraph(new Phrase(12F,"Data de Emissão: " + format.format(date), FontFactory.getFont(FontFactory.HELVETICA, 11, new BaseColor(220, 221, 216))));
        time.setAlignment(Element.ALIGN_RIGHT);
        time.setSpacingBefore(9f);
        document.add(time);
        //------------------------------------------------------------------------------------------------

        //Mensagem Biblica
        Paragraph msg = new Paragraph(new Phrase(14F, "O Senhor é o meu pastor: nada me faltará.\nSalmos 23:1", FontFactory.getFont(FontFactory.TIMES_ITALIC, 11, new BaseColor(220, 221, 216))));
        msg.setAlignment(Element.ALIGN_LEFT);
        msg.setSpacingBefore(-14f);
        document.add(msg);
        //------------------------------------------------------------------------------------------------

        //Footer
        Paragraph footer = new Paragraph(new Phrase("By Serralheria Qualidade e Pontualidade", FontFactory.getFont(FontFactory.TIMES_ITALIC, 11, new BaseColor(255, 187, 51))));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setExtraParagraphSpace(2L);
        document.add(footer);
        //-------------------------------------------------------------------------------------------------

        document.close();

        return documentName;
    }

    private String formatarMaterial(Material material){
        return material.getNome() + " | " + "R$ " + material.getValor() + ",00" + " |" + material.getQuant() + " = R$ " + material.getValor()*material.getQuant() + ",00";
    }
}
