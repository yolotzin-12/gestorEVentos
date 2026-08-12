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

@WebServlet(name = "UsuarioServlet", value = "/usuarios")
public class UsuarioServlet extends HttpServlet {

    private final UsuarioDao dao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("listaUsuarios", dao.getAll());
        request.getRequestDispatcher("administrarUsu.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // petición  para actualizar la contraseña desde el perfil
        if ("cambiarPassword".equals(action)) {
            HttpSession session = request.getSession();
            Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

            if (usuarioSesion == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            String contraActual = request.getParameter("contraActual");
            String contraNew = request.getParameter("contraNew");
            String confirmarContra = request.getParameter("confirmarContra");

            if (contraNew == null || !contraNew.equals(confirmarContra)) {
                response.sendRedirect(request.getContextPath() + "/crearPerfil.jsp?error=pass_mismatch");
                return;
            }

            boolean exito = dao.cambiarContrasenaPerfil(usuarioSesion.getId(), contraActual, contraNew);

            if (exito) {
                session.invalidate();

                response.sendRedirect(request.getContextPath() + "/login.jsp?msg=pass_updated");
            } else {
                response.sendRedirect(request.getContextPath() + "/crearPerfil.jsp?error=pass_invalid");
            }
            return;
        }

        // ── Lógica administrativa existente (requiere idUsuario en el request) ──
        if (action != null && request.getParameter("idUsuario") != null) {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

            if ("deshabilitar".equals(action)) {
                dao.deshabilitar(idUsuario);
            } else if ("cambiarEstado".equals(action)) { // Alternar estado mediante el switch
                boolean nuevoEstado = Boolean.parseBoolean(request.getParameter("estado"));
                dao.cambiarEstado(idUsuario, nuevoEstado);
            } else if ("asignarRol".equals(action)) {
                int idRol = Integer.parseInt(request.getParameter("idRol"));
                dao.asignarRol(idUsuario, idRol);
            }
        }

        // Redirige de vuelta a la lista para ver los cambios
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }
}