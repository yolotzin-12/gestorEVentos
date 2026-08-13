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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("recuperarContra.jsp").forward(request, response);
    }

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

        // Si el correo no está registrado, se avisa explícitamente y se
        // marca "correoNoExistente" para que el JSP muestre los botones
        // de Registrarse / Cancelar en vez del mensaje de éxito.
        if (u == null) {
            request.setAttribute("error", "Este correo no está registrado en el sistema.");
            request.setAttribute("correoNoExistente", true);
            request.getRequestDispatcher("recuperarContra.jsp").forward(request, response);
            return;
        }

        String token = UUID.randomUUID().toString();
        boolean guardado = tokenDao.crear(u.getId(), token);

        if (guardado) {
            String html = construirCorreo(request, token);

            try {
                EmailSender.sendMail(u.getEmail(), "Recuperar contraseña - SRAE", html);
            } catch (Exception ex) {
                System.err.println("Error enviando correo de recuperación: " + ex.getMessage());
            }
        }

        request.setAttribute("mensaje", "Se ha enviado un enlace de recuperación a tu correo.");
        request.getRequestDispatcher("recuperarContra.jsp").forward(request, response);
    }

    private static String construirCorreo(HttpServletRequest request, String token) {
        String baseUrl = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath();

        String enlace = baseUrl + "/restablecer?token=" + token;

        return """
            <html>
            <body style="margin:0; padding:0; background-color:#f5f6f8; font-family:Arial,sans-serif;">
              <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f5f6f8; padding:30px 0;">
                <tr>
                  <td align="center">
                    <table role="presentation" width="480" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:14px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.06);">

                      <tr>
                        <td style="background-color:#162e54; padding:24px; text-align:center;">
                          <div style="color:#ffffff; font-weight:bold; font-size:26px; letter-spacing:2px; margin-bottom:6px;">
                            SRAE
                          </div>
                          <div style="color:#ffffff; font-weight:bold; font-size:12px; letter-spacing:1px; opacity:0.85;">
                            SISTEMA DE RESERVACIÓN Y ADMINISTRACIÓN DE EVENTOS
                          </div>
                        </td>
                      </tr>

                      <tr>
                        <td style="padding:32px 30px;">
                          <h2 style="color:#162e54; margin:0 0 12px;">Recuperar contraseña</h2>
                          <p style="color:#495057; font-size:14px; line-height:1.6; margin:0 0 24px;">
                            Recibimos una solicitud para restablecer tu contraseña. Haz clic en el siguiente botón para continuar:
                          </p>
                          <table role="presentation" cellpadding="0" cellspacing="0">
                            <tr>
                              <td style="background-color:#0d8a5f; border-radius:10px;">
                                <a href="{0}" style="display:inline-block; padding:12px 28px; color:#ffffff; text-decoration:none; font-weight:bold; font-size:14px;">
                                  Restablecer contraseña
                                </a>
                              </td>
                            </tr>
                          </table>
                          <p style="color:#adb5bd; font-size:12px; margin-top:24px;">
                            Este enlace expira en 30 minutos. Si tú no solicitaste este cambio, puedes ignorar este correo.
                          </p>
                        </td>
                      </tr>

                      <tr>
                        <td style="background-color:#f5f6f8; padding:16px; text-align:center; color:#adb5bd; font-size:11px;">
                          SRAE &middot; Sistema de Reservación y Administración de Eventos
                        </td>
                      </tr>

                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.replace("{0}", enlace);
    }
}