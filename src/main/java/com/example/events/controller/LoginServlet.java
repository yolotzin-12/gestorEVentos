package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.events.model.Usuario;
import com.example.events.model.dao.UsuarioDao;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDao dao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String contra = request.getParameter("contra");

        if (email == null || email.trim().isEmpty() || contra == null || contra.trim().isEmpty()) {
            request.setAttribute("error", "Por favor completa todos los campos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        Usuario usuario = dao.login(email.trim(), contra);

        if (usuario != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuario);
            session.setAttribute("idRol", usuario.getIdRol());

            switch (usuario.getIdRol()) {
                case 1 -> response.sendRedirect(request.getContextPath() + "/dashboard-admin.jsp");
                case 2 -> response.sendRedirect(request.getContextPath() + "/dashboard-organizador.jsp");
                default -> response.sendRedirect(request.getContextPath() + "/eventos.jsp");
            }
        } else {
            request.setAttribute("error", "Correo o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}