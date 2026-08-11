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
        int idAsistente = asisDao.getIdAsistenteByUsuario(u.getId());

        List<Reserva> misReservas = reservaDao.getByAsistente(idAsistente);
        request.setAttribute("misReservas", misReservas);
        request.getRequestDispatcher("historialReservas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        Usuario u = (Usuario) request.getSession(false).getAttribute("usuario");
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
                request.setAttribute("mensaje", "Reserva exitosa. Código: " + r.getCodigoReserva());
            } else {
                request.setAttribute("error", "No hay disponibilidad para este evento.");
            }
            response.sendRedirect(request.getContextPath() + "/reserva");

        } else if ("cancelar".equals(action)) {
            int idReserva = Integer.parseInt(request.getParameter("idReserva"));
            reservaDao.cancelar(idReserva);
            response.sendRedirect(request.getContextPath() + "/reserva");
        }
    }
}