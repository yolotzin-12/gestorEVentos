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
import com.example.events.utils.EmailSender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "UsuarioServlet", value = "/usuarios")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 5,      // 5 MB
        maxRequestSize = 1024 * 1024 * 10   // 10 MB
)
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

        HttpSession session = request.getSession();
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

        // ACTUALIZAR DATOS PERSONALES ---
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

            // Foto de perfil
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

        // CAMBIAR LA CONTRASEÑA
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
                // Notificación por correo de que la contraseña se cambió,
                // por seguridad: si el usuario no fue quien la cambió, se entera.
                try {
                    String html = construirCorreoCambioPassword(usuarioSesion.getNombre());
                    EmailSender.sendMail(usuarioSesion.getEmail(), "Contraseña actualizada - SRAE", html);
                } catch (Exception ex) {
                    System.err.println("Correo de cambio de contraseña no enviado: " + ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/crearPerfil.jsp?success=pass_updated");
            } else {
                response.sendRedirect(request.getContextPath() + "/crearPerfil.jsp?error=pass_invalid");
            }
            return;
        }

        // GESTIÓN DE USUARIOS (DESHABILITAR / ESTADO / ROL)
        if (action != null && request.getParameter("idUsuario") != null) {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

            if ("deshabilitar".equals(action)) {
                dao.deshabilitar(idUsuario);
                response.sendRedirect(request.getContextPath() + "/usuarios?success=estado_actualizado");
            } else if ("cambiarEstado".equals(action)) {
                boolean nuevoEstado = Boolean.parseBoolean(request.getParameter("estado"));
                dao.cambiarEstado(idUsuario, nuevoEstado);
                response.sendRedirect(request.getContextPath() + "/usuarios?success=estado_actualizado");
            } else if ("asignarRol".equals(action)) {
                int idRol = Integer.parseInt(request.getParameter("idRol"));
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

    private static String construirCorreoCambioPassword(String nombre) {
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
                          <h2 style="color:#0d8a5f; margin:0 0 12px;">Contraseña actualizada</h2>
                          <p style="color:#495057; font-size:14px; line-height:1.6; margin:0 0 20px;">
                            Hola {0}, te confirmamos que la contraseña de tu cuenta se actualizó correctamente.
                          </p>
                          <p style="color:#adb5bd; font-size:12px; margin-top:24px;">
                            Si tú no realizaste este cambio, contacta al administrador del sistema lo antes posible.
                          </p>
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
            """.replace("{0}", nombre);
    }
}