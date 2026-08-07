package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.events.model.Usuario;
import com.example.events.model.dao.UsuarioDao;
import com.example.events.model.dao.TokenRecuperacionDao;
import com.example.events.utils.EmailSender;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "RecuperarContraServlet", value = "/recuperar")
public class RecuperarContraServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final TokenRecuperacionDao tokenDao = new TokenRecuperacionDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email == null || email.isBlank()) {
            request.setAttribute("error", "Ingresa tu correo.");
            request.getRequestDispatcher("recuperarContra.jsp").forward(request, response);
            return;
        }

        Usuario u = usuarioDao.getByEmail(email.trim().toLowerCase());

        request.setAttribute("mensaje",
                "Si ese correo está registrado, recibirás un enlace en los próximos minutos.");

        if (u != null) {
            String token = UUID.randomUUID().toString();
            boolean guardado = tokenDao.crear(u.getId(), token);

            if (guardado) {
                String html = getString(request, token);

                try {
                    EmailSender.sendMail(u.getEmail(), "Recuperar contraseña - SRAE", html);
                } catch (Exception ex) {
                    System.err.println("Error enviando correo de recuperación: " + ex.getMessage());
                }
            }
        }

        request.getRequestDispatcher("recuperarContra.jsp").forward(request, response);
    }

    private static String getString(HttpServletRequest request, String token) {
        String baseUrl = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath();

        // Enlace correcto enviando la petición al Servlet de restablecer
        String enlace = baseUrl + "/restablecer?token=" + token;

        String html = """
    <html><body style="font-family:Arial,sans-serif; text-align: center; padding: 20px;">
      <h2 style="color:#003b71;">Recuperar contraseña - SRAE</h2>
      <p style="color:#333;">Hemos recibido una solicitud para restablecer tu contraseña.</p>
      <p style="margin: 30px 0;">
        <a href="{0}" style="background-color: #003b71; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold;">
          Restablecer Contraseña
        </a>
      </p>
      <p style="color:#888; font-size:12px;">Este enlace expira en 30 minutos. Si no solicitaste este cambio, ignora este correo.</p>
    </body></html>
    """.replace("{0}", enlace);
        return html;
    }
}