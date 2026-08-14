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
import com.example.events.utils.SessionRegistry;

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
                try {
                    String html = construirCorreoCambioPassword(usuarioSesion.getNombre());
                    EmailSender.sendMail(usuarioSesion.getEmail(), "Contraseña actualizada - SRAE", html);
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
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
                enviarCorreoDeshabilitado(idUsuario);

                // <-- NUEVA LÍNEA: Destruye la sesión instantáneamente
                SessionRegistry.expulsarUsuario(idUsuario);

                response.sendRedirect(request.getContextPath() + "/usuarios?success=estado_actualizado_correo");

            } else if ("cambiarEstado".equals(action)) {
                boolean nuevoEstado = Boolean.parseBoolean(request.getParameter("estado"));
                boolean ok = dao.cambiarEstado(idUsuario, nuevoEstado);

                if (ok) {
                    if (!nuevoEstado) {
                        enviarCorreoDeshabilitado(idUsuario);

                        SessionRegistry.expulsarUsuario(idUsuario);
                    } else {
                        enviarCorreoReactivado(idUsuario);
                    }
                }
                response.sendRedirect(request.getContextPath() + "/usuarios?success=estado_actualizado_correo");

            } else if ("asignarRol".equals(action)) {
                int idRol = Integer.parseInt(request.getParameter("idRol"));
                int resultado = dao.asignarRol(idUsuario, idRol);

                if (resultado == -1) {
                    response.sendRedirect(request.getContextPath() + "/usuarios?error=reservas_activas");
                } else if (resultado == 1) {
                    enviarCorreoRol(idUsuario, idRol);


                    response.sendRedirect(request.getContextPath() + "/usuarios?success=rol_actualizado_correo");
                } else {
                    response.sendRedirect(request.getContextPath() + "/usuarios?error=general");
                }
            }
        }
    }

    private void enviarCorreoDeshabilitado(int idUsuario) {
        Usuario u = dao.getById(idUsuario);
        if (u != null && u.getEmail() != null) {
            try {
                String html = construirCorreoDeshabilitado(u.getNombre());
                EmailSender.sendMail(u.getEmail(), "Aviso de cuenta - SRAE", html);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void enviarCorreoReactivado(int idUsuario) {
        Usuario u = dao.getById(idUsuario);
        if (u != null && u.getEmail() != null) {
            try {
                String html = construirCorreoReactivado(u.getNombre());
                EmailSender.sendMail(u.getEmail(), "Cuenta reactivada - SRAE", html);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void enviarCorreoRol(int idUsuario, int idRol) {
        Usuario u = dao.getById(idUsuario);
        if (u != null && u.getEmail() != null) {
            try {
                String nombreRol = "";
                if (idRol == 1) nombreRol = "Administrador";
                else if (idRol == 2) nombreRol = "Organizador";
                else if (idRol == 3) nombreRol = "Asistente";

                String html = construirCorreoRolActualizado(u.getNombre(), nombreRol);
                EmailSender.sendMail(u.getEmail(), "Actualización de privilegios - SRAE", html);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static String construirCorreoCambioPassword(String nombre) {
        return """
            <html>
            <body style="margin:0; padding:0; background-color:#f5f6f8; font-family:Arial,sans-serif;">
              <table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="background-color:#f5f6f8; padding:30px 0;">
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
                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.replace("{0}", nombre);
    }

    private static String construirCorreoDeshabilitado(String nombre) {
        return """
            <html>
            <body style="margin:0; padding:0; background-color:#f5f6f8; font-family:Arial,sans-serif;">
              <table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="background-color:#f5f6f8; padding:30px 0;">
                <tr>
                  <td align="center">
                    <table role="presentation" width="480" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:14px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.06);">
                      <tr>
                        <td style="background-color:#cc0000; padding:24px; text-align:center;">
                          <div style="color:#ffffff; font-weight:bold; font-size:26px; letter-spacing:2px; margin-bottom:6px;">
                            SRAE
                          </div>
                          <div style="color:#ffffff; font-weight:bold; font-size:12px; letter-spacing:1px; opacity:0.85;">
                            AVISO IMPORTANTE DE CUENTA
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:32px 30px;">
                          <h2 style="color:#cc0000; margin:0 0 12px;">Cuenta deshabilitada</h2>
                          <p style="color:#495057; font-size:14px; line-height:1.6; margin:0 0 20px;">
                            Hola {0}, te informamos que tu cuenta en el sistema ha sido deshabilitada por un administrador.
                          </p>
                          <p style="color:#495057; font-size:14px; line-height:1.6; margin:0 0 20px;">
                            Ya no podrás acceder al sistema ni realizar reservas.
                          </p>
                          <p style="color:#adb5bd; font-size:12px; margin-top:24px;">
                            Si crees que esto es un error, por favor ponte en contacto con soporte técnico.
                          </p>
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

    private static String construirCorreoReactivado(String nombre) {
        return """
            <html>
            <body style="margin:0; padding:0; background-color:#f5f6f8; font-family:Arial,sans-serif;">
              <table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="background-color:#f5f6f8; padding:30px 0;">
                <tr>
                  <td align="center">
                    <table role="presentation" width="480" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:14px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.06);">
                      <tr>
                        <td style="background-color:#0d8a5f; padding:24px; text-align:center;">
                          <div style="color:#ffffff; font-weight:bold; font-size:26px; letter-spacing:2px; margin-bottom:6px;">
                            SRAE
                          </div>
                          <div style="color:#ffffff; font-weight:bold; font-size:12px; letter-spacing:1px; opacity:0.85;">
                            AVISO IMPORTANTE DE CUENTA
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:32px 30px;">
                          <h2 style="color:#0d8a5f; margin:0 0 12px;">Cuenta reactivada</h2>
                          <p style="color:#495057; font-size:14px; line-height:1.6; margin:0 0 20px;">
                            Hola {0}, te informamos que tu cuenta en el sistema ha sido reactivada por un administrador.
                          </p>
                          <p style="color:#495057; font-size:14px; line-height:1.6; margin:0 0 20px;">
                            Ya puedes acceder al sistema y realizar reservas nuevamente.
                          </p>
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

    private static String construirCorreoRolActualizado(String nombre, String rol) {
        return """
            <html>
            <body style="margin:0; padding:0; background-color:#f5f6f8; font-family:Arial,sans-serif;">
              <table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="background-color:#f5f6f8; padding:30px 0;">
                <tr>
                  <td align="center">
                    <table role="presentation" width="480" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:14px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.06);">
                      <tr>
                        <td style="background-color:#162e54; padding:24px; text-align:center;">
                          <div style="color:#ffffff; font-weight:bold; font-size:26px; letter-spacing:2px; margin-bottom:6px;">
                            SRAE
                          </div>
                          <div style="color:#ffffff; font-weight:bold; font-size:12px; letter-spacing:1px; opacity:0.85;">
                            ACTUALIZACIÓN DE PRIVILEGIOS
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:32px 30px;">
                          <h2 style="color:#0d8a5f; margin:0 0 12px;">Rol actualizado</h2>
                          <p style="color:#495057; font-size:14px; line-height:1.6; margin:0 0 20px;">
                            Hola {0}, te informamos que tus privilegios en el sistema han sido modificados por un administrador.
                          </p>
                          <p style="color:#495057; font-size:14px; line-height:1.6; margin:0 0 20px;">
                            Tu nuevo rol asignado es: <strong>{1}</strong>.
                          </p>
                          <p style="color:#adb5bd; font-size:12px; margin-top:24px;">
                            Inicia sesión nuevamente en tu cuenta para ver los cambios aplicados en tu perfil.
                          </p>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.replace("{0}", nombre).replace("{1}", rol);
    }
}