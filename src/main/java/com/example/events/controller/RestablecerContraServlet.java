package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.events.model.models.TokenRecuperacion;
import com.example.events.model.dao.TokenRecuperacionDao;
import com.example.events.model.dao.UsuarioDao;

import java.io.IOException;

@WebServlet(name = "RestablecerContraServlet", value = "/restablecer")
public class RestablecerContraServlet extends HttpServlet {

    private final TokenRecuperacionDao tokenDao = new TokenRecuperacionDao();
    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        TokenRecuperacion t = tokenDao.validar(token);

        if (t == null) {
            request.setAttribute("error",
                    "El enlace ha expirado o ya fue utilizado. Solicita uno nuevo.");
            request.getRequestDispatcher("recuperarContra.jsp").forward(request, response);
            return;
        }

        request.setAttribute("token", token);
        request.getRequestDispatcher("nuevaContra.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        String nueva = request.getParameter("nuevaContra");
        String confirma = request.getParameter("confirmarContra");

        if (nueva == null || !nueva.equals(confirma) || nueva.length() < 8) {
            request.setAttribute("error", "Las contraseñas no coinciden o deben tener al menos 8 caracteres.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("nuevaContra.jsp").forward(request, response);
            return;
        }

        TokenRecuperacion t = tokenDao.validar(token);
        if (t == null) {
            request.setAttribute("error", "El enlace ha expirado. Solicita uno nuevo.");
            request.getRequestDispatcher("recuperarContra.jsp").forward(request, response);
            return;
        }

        usuarioDao.actualizarContrasena(t.getIdUsuario(), nueva);
        tokenDao.marcarUsado(t.getId());

        response.sendRedirect("contraActualizada.jsp");
    }
}