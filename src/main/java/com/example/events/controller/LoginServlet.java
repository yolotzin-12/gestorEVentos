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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String contra = request.getParameter("contra");

        Usuario usuario = dao.login(email, contra);

        if (usuario != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuario);     // objeto completo
            session.setAttribute("idRol", usuario.getIdRol());

            // SEGUN SU rol: 1=Admin, 2=Organizador, 3=Asistente
            switch (usuario.getIdRol()) {
                case 1 -> response.sendRedirect(request.getContextPath() + "/dashboard-admin.jsp");
                case 2 -> response.sendRedirect(request.getContextPath() + "/dashboard-organizador.jsp");
                default -> response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } else {
            request.setAttribute("error", "Correo o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}