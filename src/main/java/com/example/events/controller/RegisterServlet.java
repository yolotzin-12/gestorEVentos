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
import java.text.MessageFormat;

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

        if (!email.equals(emailConf)) {
            request.setAttribute("error", "Los correos no coinciden.");
            request.setAttribute("errorEmail", true); // Bandera para pintar inputs de correo
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        if (contra.length() < 8) {
            request.setAttribute("error", "La contraseña debe tener al menos 8 caracteres.");
            request.setAttribute("errorContra", true); // Bandera para pintar input de contraseña
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
                String html = """
                    <html><body style="font-family:Arial,sans-serif">
                      <h2 style="color:#003b71">¡Bienvenido a SRAE, {0}!</h2>
                      <p>Tu cuenta ha sido creada exitosamente.</p>
                      <p>Ya puedes <a href="{1}/login.jsp">iniciar sesión</a>.</p>
                    </body></html>
                    """;
                String baseUrl = request.getScheme() + "://" + request.getServerName()
                        + ":" + request.getServerPort() + request.getContextPath();
                EmailSender.sendMail(u.getEmail(), "Bienvenido a SRAE",
                        MessageFormat.format(html, u.getNombre(), baseUrl));
            } catch (Exception ex) {
                System.err.println("Correo de bienvenida no enviado: " + ex.getMessage());
            }

            request.setAttribute("mensaje", "¡Cuenta creada! Ahora puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Ese correo ya está registrado.");
            request.setAttribute("errorEmail", true); // Bandera para pintar inputs de correo
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
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