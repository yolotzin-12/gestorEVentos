package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.events.model.Usuario;
import com.example.events.model.dao.UsuarioDao;
import com.example.events.utils.EmailSender;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    private final UsuarioDao dao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre          = request.getParameter("nombre");
        String apellidoPaterno = request.getParameter("apellidoPaterno");
        String apellidoMaterno = request.getParameter("apellidoMaterno");
        String email           = request.getParameter("email");
        String emailConf       = request.getParameter("emailConfirmacion");
        String contra          = request.getParameter("contra");

        if (nombre == null || nombre.isBlank() || apellidoPaterno == null ||
                email == null || contra == null) {
            request.setAttribute("error", "Completa todos los campos obligatorios.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        String regexNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";
        String regexEmail = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com|yahoo\\.com|outlook\\.com|utez\\.edu\\.mx)$";

        if (!nombre.matches(regexNombre) || !apellidoPaterno.matches(regexNombre) ||
                (apellidoMaterno != null && !apellidoMaterno.isBlank() && !apellidoMaterno.matches(regexNombre))) {
            request.setAttribute("error", "Los nombres y apellidos solo deben contener letras.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        if (!email.toLowerCase().matches(regexEmail)) {
            request.setAttribute("error", "Dominio de correo no permitido. Usa gmail, hotmail, yahoo, outlook o utez.edu.mx.");
            request.setAttribute("errorEmail", true);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        if (!email.equals(emailConf)) {
            request.setAttribute("error", "Los correos no coinciden.");
            request.setAttribute("errorEmail", true);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        if (contra.length() < 8) {
            request.setAttribute("error", "La contraseña debe tener al menos 8 caracteres.");
            request.setAttribute("errorContra", true);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        Usuario u = new Usuario();
        u.setNombre(formatear(nombre));
        u.setApellidoPaterno(formatear(apellidoPaterno));
        u.setApellidoMaterno(apellidoMaterno != null ? formatear(apellidoMaterno) : "");
        u.setEmail(email.trim().toLowerCase());
        u.setPassword(contra);
        u.setTelefono("");

        boolean creado = dao.create(u);

        if (creado) {
            try {
                String html = construirCorreoBienvenida(request, u.getNombre());
                EmailSender.sendMail(u.getEmail(), "Bienvenido a SRAE", html);
            } catch (Exception ex) {
                System.err.println("Correo de bienvenida no enviado: " + ex.getMessage());
            }

            request.setAttribute("mensaje", "¡Cuenta creada! Ahora puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Ese correo ya está registrado.");
            request.setAttribute("errorEmail", true);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }

    private static String construirCorreoBienvenida(HttpServletRequest request, String nombre) {
        String baseUrl = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath();

        String enlaceLogin = baseUrl + "/login.jsp";

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
                          <h2 style="color:#162e54; margin:0 0 12px;">¡Bienvenido a SRAE, {1}!</h2>
                          <p style="color:#495057; font-size:14px; line-height:1.6; margin:0 0 24px;">
                            Tu cuenta ha sido creada exitosamente. Ya puedes iniciar sesión y comenzar a reservar tus eventos favoritos.
                          </p>
                          <table role="presentation" cellpadding="0" cellspacing="0">
                            <tr>
                              <td style="background-color:#0d8a5f; border-radius:10px;">
                                <a href="{0}" style="display:inline-block; padding:12px 28px; color:#ffffff; text-decoration:none; font-weight:bold; font-size:14px;">
                                  Iniciar sesión
                                </a>
                              </td>
                            </tr>
                          </table>
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
            """.replace("{0}", enlaceLogin).replace("{1}", nombre);
    }

    private String formatear(String texto) {
        if (texto == null || texto.isBlank()) return texto;
        texto = texto.trim().toLowerCase();
        String[] palabras = texto.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String p : palabras)
            if (!p.isEmpty()) sb.append(Character.toUpperCase(p.charAt(0)))
                    .append(p.substring(1)).append(" ");
        return sb.toString().trim();
    }
}