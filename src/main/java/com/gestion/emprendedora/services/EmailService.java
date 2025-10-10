package com.gestion.emprendedora.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String tutorEmail, String studentName, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@mathlan.com"); // Cambiado a un email más genérico
            helper.setTo(tutorEmail);
            helper.setSubject("Acción Requerida: Confirma la inscripción de " + studentName);

            String htmlContent = "<html>"
                + "<body>"
                + "<h2>Hola,</h2>"
                + "<p>Recibes este correo para autorizar la creación de una cuenta en nuestra plataforma para <strong>" + studentName + "</strong>.</p>"
                + "<p>Para confirmar tu consentimiento como tutor y activar la cuenta, por favor, haz clic en el siguiente botón:</p>"
                + "<a href=\"" + verificationLink + "\" style=\"background-color:#0077b6; color:white; padding:10px 20px; text-decoration:none; border-radius:5px;\">Activar Cuenta</a>"
                + "<p>Si no has solicitado esta inscripción, puedes ignorar este correo.</p>"
                + "<br>"
                + "<p>Gracias,<br>El equipo de Mathlan</p>"
                + "</body>"
                + "</html>";

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo de verificación", e);
        }
    }

    /**
     * ¡NUEVO MÉTODO!
     * Envía un correo de confirmación tanto al estudiante como a su tutor (si existe)
     * sobre una videollamada agendada.
     * @param studentEmail El email del estudiante.
     * @param tutorEmail El email del tutor (puede ser nulo).
     * @param studentName El nombre del estudiante.
     * @param teacherName El nombre del docente.
     * @param scheduleDate La fecha y hora de la llamada.
     * @param consultaTitle El título de la consulta.
     */
    public void sendSchedulingConfirmationEmail(String studentEmail, String tutorEmail, String studentName, String teacherName, Date scheduleDate, String consultaTitle) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Formateamos la fecha para que sea legible en español
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

            // Decidimos a quién enviar el correo
            if (tutorEmail != null && !tutorEmail.trim().isEmpty()) {
                // Si hay un email de tutor, lo enviamos a ambos
                helper.setTo(new String[]{studentEmail, tutorEmail});
            } else {
                // Si no, solo al estudiante
                helper.setTo(studentEmail);
            }

            mailSender.send(message);

        } catch (Exception e) {
            // En una aplicación real, se usaría un logger para registrar el error
            throw new RuntimeException("Error al enviar el correo de agendamiento", e);
        }
    }
}