package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.events.model.dao.UsuarioDao;

import java.io.IOException;

@WebServlet(name = "UsuarioServlet", value = "/usuarios")
public class UsuarioServlet extends HttpServlet {

    private final UsuarioDao dao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("listaUsuarios", dao.getAll());
        request.getRequestDispatcher("dashboard-admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

        if ("deshabilitar".equals(action)) {
            dao.deshabilitar(idUsuario);
        } else if ("asignarRol".equals(action)) {
            int idRol = Integer.parseInt(request.getParameter("idRol"));
            dao.asignarRol(idUsuario, idRol);
        }

        response.sendRedirect(request.getContextPath() + "/usuarios");
    }
}