package com.gestion.emprendedora.services;

import com.gestion.emprendedora.entities.Consulta;
import com.gestion.emprendedora.entities.InformeSesion;
import com.gestion.emprendedora.entities.Usuario;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.awt.Color;

@Service
public class PdfService {

    public byte[] generarInformePdf(InformeSesion informe, Usuario estudiante, Usuario docente, Consulta consulta) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // --- Estilos de Fuente ---
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Font subtituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.DARK_GRAY);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

            // --- Encabezado ---
            document.add(new Paragraph("INFORME DE PROGRESO ACADÉMICO", tituloFont));
            document.add(new Paragraph("Proyecto MathLan", subtituloFont));
            document.add(new Paragraph(" ")); 

            // --- Datos del Estudiante ---
            document.add(new Paragraph("Nombre y Apellido: " + estudiante.getNombreCompleto(), normalFont));
            document.add(new Paragraph("Materia de Consulta: " + consulta.getMateria(), normalFont));
            document.add(new Paragraph("Docente responsable: " + docente.getNombreCompleto(), normalFont));
            document.add(new Paragraph(" "));

            // --- Resumen del Progreso ---
            document.add(new Paragraph("Resumen del Progreso", subtituloFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Participación general: " + informe.getParticipacionGeneral(), normalFont));
            document.add(new Paragraph("Cumplimiento de tareas: " + informe.getCumplimientoTareas(), normalFont));
            document.add(new Paragraph("Nivel de avance en contenidos: " + informe.getNivelAvance(), normalFont));
            document.add(new Paragraph("Desempeño promedio: " + informe.getDesempenoPromedio(), normalFont));
            document.add(new Paragraph(" "));

            // --- Tabla de Evaluación ---
            document.add(new Paragraph("Evaluación de Desempeño", subtituloFont));
            document.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 2});

            // Encabezados de la tabla
            PdfPCell header1 = new PdfPCell(new Phrase("Área de Aprendizaje", boldFont));
            header1.setBackgroundColor(Color.LIGHT_GRAY);
            PdfPCell header2 = new PdfPCell(new Phrase("Nivel de Logro", boldFont));
            header2.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(header1);
            table.addCell(header2);

            // Filas de la tabla
            table.addCell(new Phrase("Resolución de problemas", normalFont));
            table.addCell(new Phrase(informe.getResolucionProblemas(), normalFont));
            table.addCell(new Phrase("Razonamiento lógico", normalFont));
            table.addCell(new Phrase(informe.getRazonamientoLogico(), normalFont));
            table.addCell(new Phrase("Cálculo y operaciones", normalFont));
            table.addCell(new Phrase(informe.getCalculoOperaciones(), normalFont));
            table.addCell(new Phrase("Participación en plataforma", normalFont));
            table.addCell(new Phrase(informe.getParticipacionPlataforma(), normalFont));

            document.add(table);
            document.add(new Paragraph(" "));

            // --- Observaciones ---
            document.add(new Paragraph("Observaciones del Docente", subtituloFont));
            document.add(new Paragraph(informe.getPuntosFuertes(), normalFont)); // Reutilizamos "puntosFuertes"

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}