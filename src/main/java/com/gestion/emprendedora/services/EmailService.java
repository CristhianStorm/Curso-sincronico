package com.gestion.emprendedora.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String tutorEmail, String studentName, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@tutoria.com");
            helper.setTo(tutorEmail);
            helper.setSubject("Acción Requerida: Confirma la inscripción de " + studentName);

            // Creamos el cuerpo del correo en formato HTML para que se vea bien
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

            helper.setText(htmlContent, true); // El 'true' indica que el contenido es HTML

            mailSender.send(message);

        } catch (Exception e) {
            // En una aplicación real, aquí manejaríamos el error de forma más robusta (ej: log)
            throw new RuntimeException("Error al enviar el correo de verificación", e);
        }
    }
}
