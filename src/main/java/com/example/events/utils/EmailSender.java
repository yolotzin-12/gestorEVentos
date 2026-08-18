package com.example.events.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EmailSender {

    public static void sendMail(String to, String subject, String body) {
        Properties props = new Properties();

        // 1. Configuración usando SSL nativo (Puerto 465) en lugar de TLS (587)
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");

        // Ajustes clave de SSL para traspasar restricciones de red institucional:
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Timeouts para evitar cuelgues
        props.put("mail.smtp.connectiontimeout", "8000");
        props.put("mail.smtp.timeout", "8000");

        // 2. Obtener credenciales
        String usuario = System.getenv("SMTP_USER");
        String contrasena = System.getenv("SMTP_PASS");

        if (usuario == null || contrasena == null) {
            Properties creds = new Properties();
            try (InputStream is = EmailSender.class.getClassLoader().getResourceAsStream("credentials.properties")) {
                if (is != null) {
                    try (java.io.InputStreamReader reader = new java.io.InputStreamReader(is, StandardCharsets.ISO_8859_1)) {
                        creds.load(reader);
                    }
                    usuario = creds.getProperty("smtp.user");
                    contrasena = creds.getProperty("smtp.pass");
                }
            } catch (Exception e) {
                System.err.println("Error al cargar credenciales: " + e.getMessage());
            }
        }

        final String userFinal = usuario;
        final String passFinal = contrasena;

        // 3. Crear la sesión
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userFinal, passFinal);
            }
        });

        try {
            // 4. Crear el correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userFinal));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            // 5. Enviar
            Transport.send(message);
            System.out.println("¡Correo enviado con éxito a: " + to + " desde la red UTEZ!");

        } catch (MessagingException e) {
            System.err.println("❌ ERROR: La red bloqueó la conexión SSL en el puerto 465.");
            e.printStackTrace();
            throw new RuntimeException("Error al enviar correo: " + e.getMessage(), e);
        }
    }
}