package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.events.model.Usuario;
import com.example.events.model.models.Evento;
import com.example.events.model.models.Reserva;
import com.example.events.model.dao.AsistenteDao;
import com.example.events.model.dao.EventoDao;
import com.example.events.model.dao.ReservaDao;
import com.example.events.utils.EmailSender;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(name = "ReservaServlet", value = "/reserva")
public class ReservaServlet extends HttpServlet {

    private final ReservaDao reservaDao = new ReservaDao();
    private final EventoDao eventoDao   = new EventoDao();
    private final AsistenteDao asisDao  = new AsistenteDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario u = (Usuario) request.getSession(false).getAttribute("usuario");
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        String action = request.getParameter("action");
        int idAsistente = asisDao.getIdAsistenteByUsuario(u.getId());

        if ("detalle".equals(action)) {
            int idReserva = Integer.parseInt(request.getParameter("idReserva"));
            Reserva r = reservaDao.getDetalleById(idReserva);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (r != null && r.getIdAsistente() == idAsistente) {
                String json = String.format(
                        "{\"codigo\":\"%s\", \"evento\":\"%s\", \"descripcion\":\"%s\", \"fechaEvento\":\"%s\", \"lugar\":\"%s\", \"ubicacion\":\"%s\", \"estado\":\"%s\", \"fechaReserva\":\"%s\"}",
                        r.getCodigoReserva(),
                        r.getNombreEvento().replace("\"", "\\\""),
                        r.getDescripcionEvento() != null ? r.getDescripcionEvento().replace("\"", "\\\"").replace("\n", " ") : "",
                        r.getFechaEvento(),
                        r.getNombreEspacio().replace("\"", "\\\""),
                        r.getUbicacionEspacio() != null ? r.getUbicacionEspacio().replace("\"", "\\\"") : "",
                        r.getEstado(),
                        r.getFechaHoraReserva()
                );
                response.getWriter().write(json);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }

        String estado = request.getParameter("estado");
        String fecha = request.getParameter("fecha");

        List<Reserva> misReservas = reservaDao.getByAsistenteConFiltro(idAsistente, estado, fecha);

        request.setAttribute("misReservas", misReservas);
        request.setAttribute("filtroEstado", estado);
        request.setAttribute("filtroFecha", fecha);

        request.getRequestDispatcher("historialReservas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        Usuario u = (Usuario) request.getSession(false).getAttribute("usuario");
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int idAsistente = asisDao.getIdAsistenteByUsuario(u.getId());

        if ("reservar".equals(action)) {
            int idEvento = Integer.parseInt(request.getParameter("idEvento"));
            Evento evento = eventoDao.getById(idEvento);

            // No se permite una segunda reserva activa del mismo usuario para
            // el mismo evento (por ejemplo, si reenvía el formulario o manipula
            // el POST). Se le regresa a la pantalla de reserva, que mostrará
            // el aviso de "ya reservaste" junto con la opción de cancelar.
            if (reservaDao.getReservaActivaDeUsuario(idEvento, idAsistente) != null) {
                response.sendRedirect(request.getContextPath() + "/evento?action=reservar&id=" + idEvento);
                return;
            }

            Reserva r = new Reserva();
            r.setIdEvento(idEvento);
            r.setIdAsistente(idAsistente);

            boolean ok = reservaDao.create(r);

            if (ok) {
                try {
                    String codReserva = (r.getCodigoReserva() != null && !r.getCodigoReserva().isBlank())
                            ? r.getCodigoReserva() : "SRAE-PENDIENTE";

                    String nomEvt = (evento != null && evento.getNombre() != null) ? evento.getNombre() : "Evento SRAE";
                    String fecEvt = (evento != null && evento.getFechaHora() != null) ? evento.getFechaHora() : "Por confirmar";

                    String html = construirCorreoReserva(nomEvt, fecEvt, codReserva);

                    System.out.println(">>> Intentando enviar correo de reserva a: " + u.getEmail());
                    EmailSender.sendMail(u.getEmail(), "Confirmación de reserva - SRAE", html);

                } catch (Exception ex) {
                    System.err.println("❌ ERROR AL CONSTRUIR O ENVIAR CORREO:");
                    ex.printStackTrace();
                }
            } else {
                System.err.println("❌ La reserva NO se guardó en la base de datos (reservaDao.create devolvió false).");
            }
            response.sendRedirect(request.getContextPath() + "/reserva");

        } else if ("cancelar".equals(action)) {
            int idReserva = Integer.parseInt(request.getParameter("idReserva"));

            Reserva detalle = reservaDao.getDetalleById(idReserva);
            boolean esDelUsuario = detalle != null && detalle.getIdAsistente() == idAsistente;

            boolean ok = esDelUsuario && reservaDao.cancelar(idReserva);

            if (ok) {
                try {
                    String nomEvt = (detalle.getNombreEvento() != null) ? detalle.getNombreEvento() : "Evento SRAE";
                    String fecEvt = (detalle.getFechaEvento() != null) ? detalle.getFechaEvento() : "Por confirmar";
                    String codReserva = (detalle.getCodigoReserva() != null) ? detalle.getCodigoReserva() : "SRAE-CANCELADO";

                    String html = construirCorreoCancelacion(nomEvt, fecEvt, codReserva);

                    System.out.println(">>> Intentando enviar correo de cancelación a: " + u.getEmail());
                    EmailSender.sendMail(u.getEmail(), "Cancelación de reserva - SRAE", html);

                } catch (Exception ex) {
                    System.err.println("❌ ERROR AL ENVIAR CORREO DE CANCELACIÓN:");
                    ex.printStackTrace();
                }
            }

            // Si la cancelación se hizo desde la pantalla de reservar (en vez
            // del historial), se regresa ahí para que el usuario vea el
            // formulario de reserva disponible de nuevo.
            String origen = request.getParameter("origen");
            if ("reservar".equals(origen) && detalle != null) {
                response.sendRedirect(request.getContextPath() + "/evento?action=reservar&id=" + detalle.getIdEvento());
                return;
            }

            redirigirConFiltros(request, response);

        } else if ("limpiarHistorial".equals(action)) {
            reservaDao.limpiarHistorial(idAsistente);
            redirigirConFiltros(request, response);
        }
    }

    private void redirigirConFiltros(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String estadoFiltro = request.getParameter("filtroEstado");
        String fechaFiltro = request.getParameter("filtroFecha");
        StringBuilder redirect = new StringBuilder(request.getContextPath()).append("/reserva");
        String sep = "?";
        if (estadoFiltro != null && !estadoFiltro.isBlank()) {
            redirect.append(sep).append("estado=").append(URLEncoder.encode(estadoFiltro, StandardCharsets.UTF_8));
            sep = "&";
        }
        if (fechaFiltro != null && !fechaFiltro.isBlank()) {
            redirect.append(sep).append("fecha=").append(URLEncoder.encode(fechaFiltro, StandardCharsets.UTF_8));
        }
        response.sendRedirect(redirect.toString());
    }

    private static String construirCorreoReserva(String nombreEvento, String fechaEvento, String codigo) {
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
                          <h2 style="color:#0d8a5f; margin:0 0 12px;">¡Reserva confirmada!</h2>
                          <p style="color:#495057; font-size:14px; line-height:1.6; margin:0 0 20px;">
                            Tu lugar ha quedado reservado. Aquí están los detalles:
                          </p>

                          <table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="background-color:#f5f6f8; border-radius:10px; padding:16px;">
                            <tr>
                              <td style="padding:6px 0; color:#162e54; font-size:14px;"><strong>Evento:</strong></td>
                              <td style="padding:6px 0; color:#495057; font-size:14px; text-align:right;">{0}</td>
                            </tr>
                            <tr>
                              <td style="padding:6px 0; color:#162e54; font-size:14px;"><strong>Fecha:</strong></td>
                              <td style="padding:6px 0; color:#495057; font-size:14px; text-align:right;">{1}</td>
                            </tr>
                            <tr>
                              <td style="padding:6px 0; color:#162e54; font-size:14px;"><strong>Código de reserva:</strong></td>
                              <td style="padding:6px 0; color:#0d8a5f; font-size:14px; font-weight:bold; text-align:right;">{2}</td>
                            </tr>
                          </table>

                          <p style="color:#adb5bd; font-size:12px; margin-top:24px;">
                            Guarda este código, te será útil para consultar o gestionar tu reserva.
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
            """.replace("{0}", nombreEvento).replace("{1}", fechaEvento).replace("{2}", codigo);
    }

    private static String construirCorreoCancelacion(String nombreEvento, String fechaEvento, String codigo) {
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
                          <h2 style="color:#cc0000; margin:0 0 12px;">Reserva cancelada</h2>
                          <p style="color:#495057; font-size:14px; line-height:1.6; margin:0 0 20px;">
                            Tu reserva ha sido cancelada y tu lugar ha sido liberado. Aquí están los detalles:
                          </p>

                          <table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="background-color:#f5f6f8; border-radius:10px; padding:16px;">
                            <tr>
                              <td style="padding:6px 0; color:#162e54; font-size:14px;"><strong>Evento:</strong></td>
                              <td style="padding:6px 0; color:#495057; font-size:14px; text-align:right;">{0}</td>
                            </tr>
                            <tr>
                              <td style="padding:6px 0; color:#162e54; font-size:14px;"><strong>Fecha:</strong></td>
                              <td style="padding:6px 0; color:#495057; font-size:14px; text-align:right;">{1}</td>
                            </tr>
                            <tr>
                              <td style="padding:6px 0; color:#162e54; font-size:14px;"><strong>Código de reserva:</strong></td>
                              <td style="padding:6px 0; color:#cc0000; font-size:14px; font-weight:bold; text-align:right;">{2}</td>
                            </tr>
                          </table>

                          <p style="color:#adb5bd; font-size:12px; margin-top:24px;">
                            Si esto fue un error o cambias de opinión, puedes hacer una nueva reserva desde el catálogo de eventos.
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
            """.replace("{0}", nombreEvento).replace("{1}", fechaEvento).replace("{2}", codigo);
    }
}