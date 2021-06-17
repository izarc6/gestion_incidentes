/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package output;

import bd.Query;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.UnitValue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import ticketsystem.Task;
import ticketsystem.Ticket;
import ticketsystem.User;

/**
 *
 * @author Izar
 */
public class PDFOut {

    public void WriteTicketTable(String[][] tickets) throws FileNotFoundException, IOException {
        String outpath = "output/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")) + ".pdf";

        File file = new File(outpath);
        file.getParentFile().mkdirs();
        System.out.println("Created file at " + file.getAbsolutePath());

        PdfWriter writer = new PdfWriter(outpath);
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf, PageSize.A4);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Indica el número de columnas y su tamaño relativo
        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 6, 3, 16}))
                .useAllAvailableWidth();

        // Crea celdas de encabezado
        for (int i = 0; i < 4; i++) {
            table.addHeaderCell(new Cell().add(new Paragraph(tickets[0][i])).setFont(bold));
        }
        // Añade datos
        for (int i = 1; i < tickets.length; i++) {
            for (int j = 0; j < tickets[i].length; j++) {
                table.addCell(new Cell().add(new Paragraph(tickets[i][j]).setFont(font)));
            }
        }

        Paragraph title = new Paragraph("Exportación Tickets de la BBDD\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).setFont(bold).setFontSize(18f);
        title.getAccessibilityProperties().setRole(StandardRoles.TITLE);

        document.add(title);
        document.add(table);

        //Close document
        document.close();

    }

    public void WriteTicketDetails(Ticket t) throws FileNotFoundException, IOException, SQLException {
        String outpath = "output/Ticket " + t.getId() + " - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")) + ".pdf";

        File file = new File(outpath);
        file.getParentFile().mkdirs();
        System.out.println("Created file at " + file.getAbsolutePath());

        PdfWriter writer = new PdfWriter(outpath);
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf, PageSize.A4);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        Paragraph title = new Paragraph("Detalles del ticket n." + t.getId() + "\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + "\n").setFont(bold).setFontSize(18f);
        title.getAccessibilityProperties().setRole(StandardRoles.TITLE);

        Paragraph id_header = new Paragraph("ID del ticket").setFont(bold).setFontSize(16f);
        Paragraph id = new Paragraph(t.getId() + "\n").setFont(font).setFontSize(14f);

        Paragraph datetime_header = new Paragraph("Fecha y hora de creación").setFont(bold).setFontSize(16f);
        Paragraph datetime = new Paragraph(t.getDatetime().toString().replaceAll("T", " ") + "\n").setFont(font).setFontSize(14f);

        Paragraph creator_header = new Paragraph("Creador").setFont(bold).setFontSize(16f);
        Query q = new Query();
        User creator_user = q.getUser(t.getCreator());

        Paragraph creator = new Paragraph(creator_user.getName() + " " + creator_user.getSurname() + " (ID: " + t.getCreator() + ")\n").setFont(font).setFontSize(14f);

        Paragraph tech_header = new Paragraph("Técnico asignado").setFont(bold).setFontSize(16f);
        String tech_choice = "";
        if (t.getTechnician() == 0) {
            tech_choice = "Ningún técnico asignado\n";
        } else {
            User tech_user = q.getUser(t.getTechnician());
            tech_choice = tech_user.getName() + " " + tech_user.getSurname() + " (ID: " + t.getTechnician() + ")\n";
        }
        Paragraph tech = new Paragraph(tech_choice).setFont(font).setFontSize(14f);

        String priority_str = "";

        switch (t.getPriorityInt()) {
            case 0:
                priority_str = "Baja";
                break;
            case 1:
                priority_str = "Media";
                break;
            case 2:
                priority_str = "Alta";
                break;
            case 3:
                priority_str = "Grave";
                break;
            case 4:
                priority_str = "Crítica";
                break;
            default:
                priority_str = "Desconocida";
                break;
        }

        Paragraph priority_header = new Paragraph("Prioridad").setFont(bold).setFontSize(16f);
        Paragraph priority = new Paragraph(priority_str + "\n").setFont(font).setFontSize(14f);

        String status_str = "";

        switch (t.getStatusInt()) {
            case 0:
                status_str = "Inicio";
                break;
            case 1:
                status_str = "Asignado";
                break;
            case 2:
                status_str = "En proceso";
                break;
            case 3:
                status_str = "Finalizado";
                break;
            default:
                status_str = "Desconocido";
                break;
        }

        Paragraph status_header = new Paragraph("Estado").setFont(bold).setFontSize(16f);
        Paragraph status = new Paragraph(status_str + "\n").setFont(font).setFontSize(14f);

        String scalability_str = "";

        switch (t.getStatusInt()) {
            case 0:
                scalability_str = "Básico";
                break;
            case 1:
                scalability_str = "Técnico";
                break;
            case 2:
                scalability_str = "Externo";
                break;
            default:
                scalability_str = "Desconocido";
                break;
        }

        Paragraph scalab_header = new Paragraph("Nivel de escalabilidad").setFont(bold).setFontSize(16f);
        Paragraph scalab = new Paragraph(scalability_str + "\n").setFont(font).setFontSize(14f);

        Paragraph desc_header = new Paragraph("Descripción").setFont(bold).setFontSize(16f);
        Paragraph desc = new Paragraph(t.getDescription() + "\n\n").setFont(font).setFontSize(14f);

        

        // Indica el número de columnas y su tamaño relativo
        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 6, 4, 16}))
                .useAllAvailableWidth();

        ArrayList<Task> tasks = q.readTasksByID(t.getId());
        
        q.close();
        
        Paragraph table_header = new Paragraph("Lista de tareas realizadas").setFont(bold).setFontSize(16f);

        // Crea celdas de encabezado
        table.addHeaderCell(new Cell().add(new Paragraph("ID Tarea")).setFont(bold));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha y hora")).setFont(bold));
        table.addHeaderCell(new Cell().add(new Paragraph("ID Creador")).setFont(bold));
        table.addHeaderCell(new Cell().add(new Paragraph("Descripción")).setFont(bold));

        // Añade datos
        for (int i = 0; i < tasks.size(); i++) {
            table.addCell(new Cell().add(new Paragraph(tasks.get(i).getTaskID() + "").setFont(font)));
            table.addCell(new Cell().add(new Paragraph(tasks.get(i).getDateTimeString()+ "").setFont(font)));
            table.addCell(new Cell().add(new Paragraph(tasks.get(i).getUserID()+ "").setFont(font)));
            table.addCell(new Cell().add(new Paragraph(tasks.get(i).getDescription()+ "").setFont(font)));
        }

        document.add(title);

        document.add(id_header);
        document.add(id);
        document.add(datetime_header);
        document.add(datetime);
        document.add(creator_header);
        document.add(creator);
        document.add(tech_header);
        document.add(tech);
        document.add(priority_header);
        document.add(priority);
        document.add(status_header);
        document.add(status);
        document.add(scalab_header);
        document.add(scalab);
        document.add(desc_header);
        document.add(desc);
        
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        document.add(table_header);
        document.add(table);

        //Close document
        document.close();
    }

}
