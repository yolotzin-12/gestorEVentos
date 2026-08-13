package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import com.example.events.model.Usuario;
import com.example.events.model.dao.UsuarioDao;
import com.example.events.model.dao.AsistenteDao;
import com.example.events.model.dao.OrganizadorDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "UsuarioServlet", value = "/usuarios")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class UsuarioServlet extends HttpServlet {

    private final UsuarioDao dao = new UsuarioDao();
    private final AsistenteDao asisDao = new AsistenteDao();
    private final OrganizadorDao orgDao = new OrganizadorDao();

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
        HttpSession session = request.getSession();
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        if ("actualizarDatos".equals(action)) {
            if (usuarioSesion == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            String nombre = request.getParameter("nombre");
            String apeP = request.getParameter("apeP");
            String apeM = request.getParameter("apeM");
            String telefono = request.getParameter("telefono");
            String correo = request.getParameter("correo");

            usuarioSesion.setNombre(nombre);
            usuarioSesion.setApellidoPaterno(apeP);
            usuarioSesion.setApellidoMaterno(apeM);
            usuarioSesion.setTelefono(telefono);
            usuarioSesion.setEmail(correo);

            Part filePart = request.getPart("fotoPerfil");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String uploadPath = getServletContext().getRealPath("") + File.separator + "img" + File.separator + "perfiles";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String filePath = uploadPath + File.separator + fileName;
                filePart.write(filePath);
                usuarioSesion.setFotoUrl("img/perfiles/" + fileName);
            }

            boolean exito = dao.actualizarPerfil(usuarioSesion);

            if (exito) {
                session.setAttribute("usuario", usuarioSesion);
                response.sendRedirect(request.getContextPath() + "/crearPerfil.jsp?update=success");
            } else {
                response.sendRedirect(request.getContextPath() + "/crearPerfil.jsp?update=error");
            }
            return;
        }

        if ("cambiarPassword".equals(action)) {
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
                response.sendRedirect(request.getContextPath() + "/crearPerfil.jsp?success=pass_updated");
            } else {
                response.sendRedirect(request.getContextPath() + "/crearPerfil.jsp?error=pass_invalid");
            }
            return;
        }

        if (action != null && request.getParameter("idUsuario") != null) {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

            if ("deshabilitar".equals(action)) {
                dao.deshabilitar(idUsuario);
                response.sendRedirect(request.getContextPath() + "/usuarios?success=estado_actualizado");
            } else if ("cambiarEstado".equals(action)) {
                boolean nuevoEstado = Boolean.parseBoolean(request.getParameter("estado"));

                if (!nuevoEstado) {
                    if (asisDao.tieneReservasActivas(idUsuario)) {
                        response.sendRedirect(request.getContextPath() + "/usuarios?error=reservas_activas");
                        return;
                    }
                    if (orgDao.tieneEventosActivos(idUsuario)) {
                        response.sendRedirect(request.getContextPath() + "/usuarios?error=eventos_activos");
                        return;
                    }
                }

                dao.cambiarEstado(idUsuario, nuevoEstado);
                response.sendRedirect(request.getContextPath() + "/usuarios?success=estado_actualizado");
            } else if ("asignarRol".equals(action)) {
                int idRol = Integer.parseInt(request.getParameter("idRol"));

                if (asisDao.tieneReservasActivas(idUsuario)) {
                    response.sendRedirect(request.getContextPath() + "/usuarios?error=reservas_activas");
                    return;
                }
                if (orgDao.tieneEventosActivos(idUsuario)) {
                    response.sendRedirect(request.getContextPath() + "/usuarios?error=eventos_activos");
                    return;
                }

                int resultado = dao.asignarRol(idUsuario, idRol);

                if (resultado == -1) {
                    response.sendRedirect(request.getContextPath() + "/usuarios?error=reservas_activas");
                } else if (resultado == 1) {
                    response.sendRedirect(request.getContextPath() + "/usuarios?success=rol_actualizado");
                } else {
                    response.sendRedirect(request.getContextPath() + "/usuarios?error=general");
                }
            }
        }
    }
}