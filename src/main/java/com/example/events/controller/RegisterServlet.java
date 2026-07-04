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
        String email = request.getParameter("email");
        String contra = request.getParameter("contra");

        if (nombre == null || nombre.trim().isEmpty() || email == null || email.trim().isEmpty() || contra == null || contra.trim().isEmpty()) {
            request.setAttribute("error", "Por favor, completa todos los campos obligatorios.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(contra);

        boolean creado = dao.create(nuevoUsuario);

        if (creado) {
            request.setAttribute("mensaje", "¡Cuenta creada con éxito! Ahora puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Ese correo ya está registrado.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}