package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.events.model.Usuario;
import com.example.events.model.dao.UsuarioDao;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    UsuarioDao dao = new UsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String apellidoPaterno = request.getParameter("apellidoPaterno");
        String apellidoMaterno = request.getParameter("apellidoMaterno");
        String email = request.getParameter("email");
        String emailConfirmacion = request.getParameter("emailConfirmacion");
        String contra = request.getParameter("contra");

        if (nombre == null || nombre.trim().isEmpty() ||
                apellidoPaterno == null || apellidoPaterno.trim().isEmpty() ||
                apellidoMaterno == null || apellidoMaterno.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                emailConfirmacion == null || emailConfirmacion.trim().isEmpty() ||
                contra == null || contra.trim().isEmpty()) {

            request.setAttribute("error", "Por favor, completa todos los campos obligatorios.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        String regexNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";
        if (!nombre.trim().matches(regexNombre)) {
            request.setAttribute("error", "El nombre solo puede contener letras y espacios.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        String regexApellido = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$";
        if (!apellidoPaterno.trim().matches(regexApellido) || !apellidoMaterno.trim().matches(regexApellido)) {
            request.setAttribute("error", "No se permiten espacios ni caracteres especiales.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        if (!email.equals(emailConfirmacion)) {
            request.setAttribute("error", "Los correos electrónicos no coinciden.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(formatearTexto(nombre));
        nuevoUsuario.setApellidoPaterno(formatearTexto(apellidoPaterno));
        nuevoUsuario.setApellidoMaterno(formatearTexto(apellidoMaterno));
        nuevoUsuario.setEmail(email.trim().toLowerCase());
        nuevoUsuario.setPassword(contra);
        nuevoUsuario.setTelefono("");

        boolean creado = dao.create(nuevoUsuario);

        if (creado) {
            request.setAttribute("mensaje", "¡Cuenta creada con éxito! Ahora puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Ese correo ya está registrado.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }


    private String formatearTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }

        texto = texto.trim().toLowerCase();

        String[] palabras = texto.split("\\s+");
        StringBuilder resultado = new StringBuilder();

        for (String palabra : palabras) {
            if (palabra.length() > 0) {
                resultado.append(Character.toUpperCase(palabra.charAt(0)))
                        .append(palabra.substring(1))
                        .append(" ");
            }
        }

        return resultado.toString().trim();
    }
}