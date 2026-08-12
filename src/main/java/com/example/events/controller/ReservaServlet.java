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
import java.text.MessageFormat;
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

        String action = request.getParameter("action");

        if ("detalle".equals(action)) {
            int idReserva = Integer.parseInt(request.getParameter("idReserva"));
            Reserva r = reservaDao.getDetalleById(idReserva);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (r != null) {
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

        // --- Evita que el navegador muestre una versión en caché de esta página ---

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Historial de Reservas
        int idAsistente = asisDao.getIdAsistenteByUsuario(u.getId());
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

            Reserva r = new Reserva();
            r.setIdEvento(idEvento);
            r.setIdAsistente(idAsistente);

            boolean ok = reservaDao.create(r);

            if (ok) {
                try {
                    String html = """
                        <div style="background-color: #f5f5f5; padding: 40px 20px; font-family: Arial, sans-serif; text-align: center;">
                            <div style="background-color: #ffffff; max-width: 500px; margin: 0 auto; padding: 40px; border-radius: 12px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
                                
                                <!-- NOTA: Para que el logo se vea en Gmail, la URL debe ser pública -->
                                <img src="https://i.imgur.com/K3C4Xn9.png" alt="SRAE Logo" style="height: 80px; margin-bottom: 10px;">
                                
                                <h1 style="color: #003b71; font-size: 26px; margin-bottom: 15px; font-weight: 800;">¡Reserva confirmada!</h1>
                                
                                <p style="color: #000000; font-size: 15px; margin-bottom: 30px; line-height: 1.6; font-weight: bold;">
                                    ¡Gracias! Tu lugar para el evento ha sido<br>reservado con éxito. Por favor, presenta<br>este correo al momento del ingreso.
                                </p>
                    
                                <div style="background-color: #e8ecef; border-radius: 15px; padding: 25px; margin-bottom: 35px; text-align: left;">
                                    <h3 style="margin-top: 0; margin-bottom: 15px; font-size: 18px; color: #003b71; text-align: center;">¡Detalles de la reserva!</h3>
                                    
                                    <p style="margin: 5px 0; font-size: 15px; color: #000000;">
                                        <strong>Evento:</strong> {0}
                                    </p>
                                    <p style="margin: 5px 0; font-size: 15px; color: #000000;">
                                        <strong>Fecha:</strong> {1}
                                    </p>
                                    <p style="margin: 5px 0; font-size: 15px; color: #000000;">
                                        <strong>Hora:</strong> {2}
                                    </p>
                                    <p style="margin: 5px 0; font-size: 15px; color: #000000;">
                                        <strong>Ubicación:</strong> {3}
                                    </p>
                                    <p style="margin: 5px 0; font-size: 15px; color: #000000;">
                                        <strong>Código:</strong> {4}
                                    </p>
                                </div>
                    
                                <!-- El enlace apunta al Servlet de reservas de tu proyecto local -->
                                <a href="http://localhost:8080/Events_war_exploded/reserva" style="display: inline-block; background-color: #0d8a5f; color: #ffffff; text-decoration: none; padding: 14px 28px; border-radius: 8px; font-weight: bold; font-size: 14px;">
                                    &#128197; VER EN MIS RESERVACIONES
                                </a>
                                
                            </div>
                        </div>
                        """;

                    String nombreEvento = (evento != null) ? evento.getNombre() : "—";
                    String fechaEvento = (evento != null) ? evento.getFechaHora() : "—";
                    String horaEvento = "Por definir";
                    String ubicacion = "Auditorio Principal";
                    String codigo = r.getCodigoReserva();

                    EmailSender.sendMail(u.getEmail(), "Confirmación de reserva - SRAE",
                            MessageFormat.format(html, nombreEvento, fechaEvento, horaEvento, ubicacion, codigo));

                } catch (Exception ex) {
                    System.err.println("Correo de reserva no enviado: " + ex.getMessage());
                }
            }
            response.sendRedirect(request.getContextPath() + "/reserva");

        } else if ("cancelar".equals(action)) {
            int idReserva = Integer.parseInt(request.getParameter("idReserva"));

            Reserva detalle = reservaDao.getDetalleById(idReserva);
            boolean ok = reservaDao.cancelar(idReserva);

            if (ok && detalle != null) {
                try {
                    String html = """
                        <html><body style="font-family:Arial,sans-serif">
                          <h2 style="color:#cc0000">Reserva cancelada</h2>
                          <p><strong>Evento:</strong> {0}</p>
                          <p><strong>Fecha:</strong> {1}</p>
                          <p><strong>Código de reserva:</strong> {2}</p>
                          <p>Tu lugar ha sido liberado.</p>
                        </body></html>
                        """;
                    EmailSender.sendMail(u.getEmail(), "Cancelación de reserva - SRAE",
                            MessageFormat.format(html,
                                    detalle.getNombreEvento(),
                                    detalle.getFechaEvento(),
                                    detalle.getCodigoReserva()));
                } catch (Exception ex) {
                    System.err.println("Correo de cancelación no enviado: " + ex.getMessage());
                }
            }

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
    }
}