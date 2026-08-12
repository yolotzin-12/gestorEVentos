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

        // Anti-caché aplicado a TODA respuesta de este servlet
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

            // Se valida que la reserva pertenezca al usuario logueado antes
            // de devolver cualquier dato. Sin esto, cualquiera podía cambiar
            // idReserva en la URL y ver reservas de otras personas.
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

        // Historial de Reservas — cada usuario ve únicamente lo que le
        // corresponde, filtrado por su propio idAsistente.
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
                // Enviar correo de confirmación (HU-14)
                try {
                    String html = """
                        <html><body style="font-family:Arial,sans-serif">
                          <h2 style="color:#003b71">¡Reserva confirmada!</h2>
                          <p><strong>Evento:</strong> {0}</p>
                          <p><strong>Fecha:</strong> {1}</p>
                          <p><strong>Código de reserva:</strong> {2}</p>
                        </body></html>
                        """;
                    EmailSender.sendMail(u.getEmail(), "Confirmación de reserva - SRAE",
                            MessageFormat.format(html,
                                    evento != null ? evento.getNombre() : "—",
                                    evento != null ? evento.getFechaHora() : "—",
                                    r.getCodigoReserva()));
                } catch (Exception ex) {
                    System.err.println("Correo de reserva no enviado: " + ex.getMessage());
                }
            }
            response.sendRedirect(request.getContextPath() + "/reserva");

        } else if ("cancelar".equals(action)) {
            int idReserva = Integer.parseInt(request.getParameter("idReserva"));

            // Se toma el detalle ANTES de cancelar, para armar el correo
            // y, sobre todo, para comprobar que la reserva es del usuario
            // que la está intentando cancelar (evita que alguien cancele
            // reservas ajenas manipulando el idReserva del formulario).
            Reserva detalle = reservaDao.getDetalleById(idReserva);
            boolean esDelUsuario = detalle != null && detalle.getIdAsistente() == idAsistente;

            boolean ok = esDelUsuario && reservaDao.cancelar(idReserva);

            if (ok) {
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

            redirigirConFiltros(request, response);

        } else if ("limpiarHistorial".equals(action)) {
            // Elimina del historial las reservas Canceladas y las Finalizadas
            // (Utilizado, o Reservado con evento ya pasado). Las reservas
            // activas (próximas) nunca se tocan.
            reservaDao.limpiarHistorial(idAsistente);
            redirigirConFiltros(request, response);
        }
    }

    private void redirigirConFiltros(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Se conservan los filtros que el usuario tenía aplicados en el historial
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