package com.example.cursosserver.services;

import com.example.cursosserver.dtos.OrcamentoAdressTo;
import com.example.cursosserver.models.Cliente;
import com.example.cursosserver.models.Material;
import com.example.cursosserver.models.Servico;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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

    public String create(Cliente cliente, Servico servico, OrcamentoAdressTo orcamentoAdressTo, String mailCompany) throws DocumentException, IOException {

        Document document = new Document();

        String documentName = "Orcamento.pdf";

        String empresaName = "Serralheria Qualidade e Pontualidade";
        String CNPJ = "41.221.179/0001-21";
        String email = mailCompany;
        String telefone = "(34) 98848-3279";
        List<Material> materiais = servico.getMateriais();

        boolean ocultarMateriais = orcamentoAdressTo.isOcultarMateriais();
        boolean ocultarMaoDeObra = orcamentoAdressTo.isOcultarMaoDeObra();
        boolean ocultarDesconto = orcamentoAdressTo.isOcultarDesconto();

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        //PdfWriter.getInstance(document, new FileOutputStream(path));
        Resource resource = resourceLoader.getResource("file:files\\" + documentName);
        PdfWriter.getInstance(document, new FileOutputStream(resource.getFile()));

        Rectangle rectangle = new Rectangle(PageSize.A4);

        if(ocultarMateriais) {
            rectangle = new Rectangle(595, 550);
        }

        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorder(Rectangle.BOX);
        rectangle.setBorderWidth(3);
        rectangle.setBorderColor(BaseColor.BLACK);

        document.setPageSize(rectangle);
        document.open();
        document.add(rectangle);

        //FONTS
        Font fontTrs = new Font(Font.FontFamily.HELVETICA);
        fontTrs.setStyle(Font.BOLD);

        Font fontBody = new Font();
        fontBody.setSize(11);
        Font fontValues = new Font();
        fontValues.setColor(new BaseColor(0, 128, 0));
        Font fontImportant = new Font();
        fontImportant.setColor(new BaseColor(128, 0, 0));
        Font fontTitle = new Font();
        fontTitle.setStyle(Font.BOLD);
        fontTitle.setSize(25);

        //TABLES
        PdfPTable tablePrestador = new PdfPTable(new float[]{5f});
        tablePrestador.setWidthPercentage(100f);

        PdfPTable tableHeader = new PdfPTable(new float[]{5f});
        tableHeader.setWidthPercentage(100f);

        PdfPTable table = new PdfPTable(new float[]{1f, 3f, 1f, 2f});
        table.setWidthPercentage(100f);

        PdfPTable empresaTable = new PdfPTable(new float[]{1f,3f,1f,2f});
        empresaTable.setWidthPercentage(100f);

        PdfPTable tableAdress = new PdfPTable(new float[]{1f,3f,1f,2f});
        tableAdress.setWidthPercentage(100f);

        PdfPTable tableServicoHeader = new PdfPTable(new float[]{5f});
        tableServicoHeader.setWidthPercentage(100f);

        PdfPTable tableServicoBody = new PdfPTable(new float[]{1f,4f});
        tableServicoBody.setWidthPercentage(100f);

        PdfPTable tableMateriaisHeader = new PdfPTable(new float[]{5f});
        tableMateriaisHeader.setWidthPercentage(100f);

        PdfPTable tableMateriaisBody = new PdfPTable(new float[]{1f, 1f, 1f, 1f});
        tableMateriaisBody.setWidthPercentage(100f);

        PdfPTable tableMateriaisValorFinal = new PdfPTable(new float[]{1f, 1f});
        tableMateriaisValorFinal.setWidthPercentage(50f);

        PdfPTable tableMaoDeObra = new PdfPTable(new float[]{4f, 2f});
        tableMaoDeObra.setWidthPercentage(50f);

        PdfPTable tableCustoFinalServico = new PdfPTable(new float[]{1f, 1f});
        tableCustoFinalServico.setWidthPercentage(100f);

        PdfPTable tableDescontoAplicado = new PdfPTable(new float[]{2f, 1f, 3f, 2f});
        tableDescontoAplicado.setWidthPercentage(100f);

        PdfPTable tableTime = new PdfPTable(new float[]{2f, 1f});
        tableTime.setWidthPercentage(40f);

        //CELLS
        tablePrestador.addCell(cellBackgroundGray("Prestador Do Serviço", fontTrs));

        empresaTable.addCell(cellBackgroundGray("Prestador", fontTrs));
        PdfPCell cellEmpresa = cellConteudo(empresaName, fontBody);
        empresaTable.addCell(cellEmpresa);
        empresaTable.addCell(cellBackgroundGray("CNPJ", fontTrs));
        PdfPCell cellCNPJ = cellConteudo(CNPJ, fontBody);
        empresaTable.addCell(cellCNPJ);
        empresaTable.completeRow();

        empresaTable.addCell(cellBackgroundGray("Email", fontTrs));
        PdfPCell cellEmail = cellConteudo(email, fontBody);
        empresaTable.addCell(cellEmail);
        empresaTable.addCell(cellBackgroundGray("Telefone", fontTrs));
        PdfPCell cellTelefone = cellConteudo(telefone, fontBody);
        empresaTable.addCell(cellTelefone);
        empresaTable.completeRow();

        PdfPCell titleCellCliente = cellBackgroundGray("Cliente", fontTrs);
        tableHeader.addCell(titleCellCliente);

        PdfPCell cellName = cellConteudo(cliente.getNome(), fontBody);
        PdfPCell cellTel = cellConteudo(cliente.getTel(), fontBody);
        PdfPCell cellBairro = cellConteudo(cliente.getBairro(), fontBody);
        PdfPCell cellAdress = cellConteudo(cliente.getEndereco(), fontBody);

        table.addCell(cellBackgroundGray("Nome", fontTrs));
        table.addCell(cellName);
        table.addCell(cellBackgroundGray("Telefone", fontTrs));
        table.addCell(cellTel);
        table.completeRow();

        tableAdress.addCell(cellBackgroundGray("Endereço", fontTrs));
        tableAdress.addCell(cellAdress);
        tableAdress.addCell(cellBackgroundGray("Bairro", fontTrs));
        tableAdress.addCell(cellBairro);
        tableAdress.completeRow();

        tableServicoHeader.addCell(cellBackgroundGray("Serviço Realizado", fontTrs));
        tableServicoBody.addCell(cellBackgroundGray("Serviço", fontTrs));
        tableServicoBody.addCell(cellConteudo(servico.getNome(), fontBody));
        tableServicoBody.completeRow();
        tableServicoBody.addCell(cellBackgroundGray("Descrição", fontTrs));
        PdfPCell c = cellConteudo(servico.getDesc().replace("\n", " "), fontBody);
        c.setMinimumHeight(100f);
        c.setVerticalAlignment(Element.ALIGN_CENTER);
        tableServicoBody.addCell(c);
        tableServicoBody.completeRow();

        tableMateriaisHeader.addCell(cellBackgroundGray("Lista De Materiais",fontTrs));
        tableMateriaisBody.addCell(cellBackgroundGray("Material", fontTrs));
        tableMateriaisBody.addCell(cellBackgroundGray("Valor Unitário", fontTrs));
        tableMateriaisBody.addCell(cellBackgroundGray("Quantidade", fontTrs));
        tableMateriaisBody.addCell(cellBackgroundGray("Total", fontTrs));
        tableMateriaisBody.completeRow();

        for(int i=0; i<materiais.size(); i++){

            tableMateriaisBody.addCell(cellConteudo(materiais.get(i).getNome(), fontBody));
            tableMateriaisBody.addCell(cellConteudo("R$ " + String.valueOf(materiais.get(i).getValor()) + ",00", fontValues));
            tableMateriaisBody.addCell(cellConteudo(String.valueOf(materiais.get(i).getQuant()), fontBody));
            tableMateriaisBody
                    .addCell(cellConteudo("R$ " + String.valueOf(materiais.get(i).getQuant()
                            * materiais.get(i).getValor() +",00"), fontValues));
            tableMateriaisBody.completeRow();

        }

        for(int i=0; i<2; i++){
            tableMateriaisBody.addCell(cellEmpty());
            tableMateriaisBody.addCell(cellEmpty());
            tableMateriaisBody.addCell(cellEmpty());
            tableMateriaisBody.addCell(cellEmpty());
            tableMateriaisBody.completeRow();
        }

        tableMateriaisValorFinal.addCell(cellBackgroundGray("Total", fontTrs));
        tableMateriaisValorFinal.addCell(cellConteudo("R$ " +String.valueOf(servico.getValorTotalMateriais()) + ",00", fontValues));
        tableMateriaisValorFinal.completeRow();

        tableMaoDeObra.addCell(cellBackgroundGray("Mão De Obra Definida", fontTrs));
        tableMaoDeObra.addCell(cellConteudo("R$ " + String.valueOf(servico.getMaoDeObra()) + ",00", fontValues));
        tableMaoDeObra.completeRow();

        tableCustoFinalServico.addCell(cellBackgroundGray("Total Do Serviço", fontTrs));
        tableCustoFinalServico.addCell(cellConteudo("R$ " + String.valueOf(servico.getValorTotalMateriais() + servico.getMaoDeObra()) + ",00", fontValues));
        tableCustoFinalServico.completeRow();

        tableDescontoAplicado.addCell(cellBackgroundGray("Desconto Aplicado", fontTrs));
        tableDescontoAplicado.addCell(cellConteudo(String.valueOf(servico.getDesconto()) + "%", fontImportant));
        tableDescontoAplicado.addCell(cellBackgroundGray("Total Com Desconto", fontTrs));
        tableDescontoAplicado.addCell(cellConteudo("R$ " + String.valueOf(servico.getValorFinal()) + ",00", fontValues));


        Paragraph title = new Paragraph(new Phrase(20f,"Serralheria Qualidade e Pontualidade", FontFactory.getFont(FontFactory.HELVETICA, 18F)));
        title.setAlignment(Element.ALIGN_CENTER);
        Font fontDeLink = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
        Paragraph subtitle = new Paragraph(new Phrase(14F, "Esquadrias Metálicas", fontDeLink));
        subtitle.setAlignment(Element.ALIGN_CENTER);

        document.addTitle("Orçamento");

        document.add(title);
        document.add(subtitle);
        document.add(paragraphEmpty());

        document.add(paragraphEmpty());
        document.add(tablePrestador);
        document.add(empresaTable);
        document.add(paragraphEmpty());

        document.add(tableHeader);
        document.add(table);
        document.add(tableAdress);
        document.add(paragraphEmpty());

        document.add(tableServicoHeader);
        document.add(tableServicoBody);
        document.add(paragraphEmpty());

        if(!ocultarMateriais) {
            document.add(tableMateriaisHeader);
            document.add(paragraphEmpty());
            document.add(tableMateriaisBody);
            document.add(tableMateriaisValorFinal);
        }
        if(!ocultarMaoDeObra){
            document.add(paragraphEmpty());
            document.add(tableMaoDeObra);
            document.add(paragraphEmpty());
        }
        document.add(tableCustoFinalServico);

        if(!ocultarDesconto){
            document.add(paragraphEmpty());
            document.add(tableDescontoAplicado);
        }

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Paragraph time = new Paragraph(new Phrase(12F,"Data de Emissão: " + format.format(date), FontFactory.getFont(FontFactory.HELVETICA, 11)));
        time.setAlignment(Element.ALIGN_RIGHT);
        time.setSpacingBefore(9f);
        document.add(time);


        Paragraph msg = new Paragraph(new Phrase(14F, "O Senhor é o meu pastor: nada me faltará.\nSalmos 23:1", FontFactory.getFont(FontFactory.TIMES_ITALIC)));
        msg.setAlignment(Element.ALIGN_LEFT);
        msg.setSpacingBefore(-14f);
        document.add(msg);

        document.close();

        return documentName;
    }

    private PdfPCell cellBackgroundGray(String body, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(body, font));
        cell.setFixedHeight(20f);
        cell.setBackgroundColor(new BaseColor(148, 183, 213));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private PdfPCell cellConteudo(String body, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(body, font));
        cell.setFixedHeight(20f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private Paragraph paragraphEmpty(){
        Phrase phrase = new Phrase("\n");
        Paragraph paragraph = new Paragraph();
        paragraph.add(phrase);
        return paragraph;
    }

    private PdfPCell cellEmpty(){
        PdfPCell cell = new PdfPCell(new Phrase(" "));
        cell.setFixedHeight(20f);
        return cell;
    }
}
