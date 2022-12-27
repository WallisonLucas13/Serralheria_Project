package com.example.cursosserver.services;

import com.example.cursosserver.models.Curso;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Service
public class CreateAttachmentFile {

    public String create(Curso curso) throws DocumentException, FileNotFoundException {

        Document document = new Document();
        String documentName = "Relatorio.pdf";

        PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Wallison\\Documents\\Projetos Spring-Boot\\Cursos-Server\\src\\main\\java\\com\\example\\cursosserver\\files\\" + documentName));
        document.open();

        Font fontTitle = new Font();
        fontTitle.setColor(BaseColor.RED);
        fontTitle.setSize(20);
        fontTitle.setStyle("italic");

        document.addSubject("Relatório!");
        System.out.println(document.getPageSize().getBorder());

        document.addAuthor("Wallison");
        document.add(new Paragraph(limitPageBorder(74)));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Curso Criado:\n", fontTitle));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Curso: " + curso.getNome()));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Categoria: " + curso.getCategoria()));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph(limitPageBorder(74)));
        document.close();
        return documentName;
    }

    private String limitPageBorder(int size){
        String limit = "";
        System.out.println(size);

        for(int i=0; i<size; i++){
            limit += "=";
        }

        System.out.println(limit);
        return limit;
    }
}
