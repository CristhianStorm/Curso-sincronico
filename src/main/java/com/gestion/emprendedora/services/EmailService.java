package com.gestion.emprendedora.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String tutorEmail, String studentName, String verificationLink) {
        // ... (tu método existente para la verificación)
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@mathlan.com");
            helper.setTo(tutorEmail);
            helper.setSubject("Acción Requerida: Confirma la inscripción de " + studentName);

            String htmlContent = "<html><body>"
                + "<h2>Hola,</h2>"
                + "<p>Recibes este correo para autorizar la creación de una cuenta en nuestra plataforma para <strong>" + studentName + "</strong>.</p>"
                + "<p>Para confirmar tu consentimiento como tutor y activar la cuenta, por favor, haz clic en el siguiente botón:</p>"
                + "<a href=\"" + verificationLink + "\" style=\"background-color:#0077b6; color:white; padding:10px 20px; text-decoration:none; border-radius:5px;\">Activar Cuenta</a>"
                + "<p>Si no has solicitado esta inscripción, puedes ignorar este correo.</p>"
                + "<br><p>Gracias,<br>El equipo de Mathlan</p>"
                + "</body></html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo de verificación", e);
        }
    }

    public void sendSchedulingConfirmationEmail(String studentEmail, String tutorEmail, String studentName, String teacherName, Date scheduleDate, String consultaTitle) {
        // ... (tu método existente para agendar llamadas)
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy 'a las' HH:mm 'hs.'", new Locale("es", "ES"));
            String fechaFormateada = formatter.format(scheduleDate);

            helper.setFrom("no-reply@mathlan.com");
            helper.setSubject("Videollamada Agendada: " + consultaTitle);

            String htmlContent = "<html><body>"
                + "<h2>Hola " + studentName + ",</h2>"
                + "<p>Tu videollamada de tutoría para la consulta '<strong>" + consultaTitle + "</strong>' ha sido agendada con el docente " + teacherName + ".</p>"
                + "<p><strong>Fecha y Hora:</strong> " + fechaFormateada + "</p>"
                + "<p>Por favor, ingresa a tu dashboard en la plataforma a la hora indicada para unirte a la sesión.</p>"
                + "<br><p>Gracias,<br>El equipo de Mathlan</p>"
                + "</body></html>";

            helper.setText(htmlContent, true);

            if (tutorEmail != null && !tutorEmail.trim().isEmpty()) {
                helper.setTo(new String[]{studentEmail, tutorEmail});
            } else {
                helper.setTo(studentEmail);
            }
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo de agendamiento", e);
        }
    }

    /**
     * ¡NUEVO MÉTODO!
     * Envía una notificación con el informe de progreso en PDF adjunto.
     * @param studentEmail Email del estudiante.
     * @param tutorEmail Email del tutor (puede ser nulo).
     * @param studentName Nombre del estudiante.
     * @param pdfContent El contenido del PDF como un array de bytes.
     */
    public void sendInformeNotificationEmail(String studentEmail, String tutorEmail, String studentName, byte[] pdfContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // El 'true' en MimeMessageHelper indica que será un mensaje multipart (para adjuntos)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@mathlan.com");
            helper.setSubject("Nuevo Informe de Progreso para " + studentName);

            String htmlContent = "<html><body>"
                + "<h2>Hola,</h2>"
                + "<p>Se ha generado un nuevo informe de progreso para <strong>" + studentName + "</strong>.</p>"
                + "<p>Puedes encontrar el resumen detallado en el archivo PDF adjunto a este correo.</p>"
                + "<p>También puedes verlo en cualquier momento desde el dashboard de estudiante en la plataforma.</p>"
                + "<br><p>Atentamente,<br>El equipo de Mathlan</p>"
                + "</body></html>";
            
            helper.setText(htmlContent, true);

            // Adjuntamos el PDF
            helper.addAttachment("Informe_Progreso.pdf", new ByteArrayResource(pdfContent));

            // Decidimos a quién enviar el correo
            if (tutorEmail != null && !tutorEmail.trim().isEmpty()) {
                helper.setTo(new String[]{studentEmail, tutorEmail});
            } else {
                helper.setTo(studentEmail);
            }

            mailSender.send(message);

        } catch (Exception e) {
            // En un entorno real, aquí se debería loguear el error.
            throw new RuntimeException("Error al enviar el correo con el informe.", e);
        }
    }
}