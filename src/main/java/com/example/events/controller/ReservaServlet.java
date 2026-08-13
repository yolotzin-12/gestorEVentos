package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.events.model.Usuario;
import com.example.events.model.models.Reserva;
import com.example.events.model.dao.AsistenteDao;
import com.example.events.model.dao.ReservaDao;
import com.example.events.utils.EmailSender;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@WebServlet(name = "ReservaServlet", value = "/reserva")
public class ReservaServlet extends HttpServlet {

    private final ReservaDao reservaDao = new ReservaDao();
    private final AsistenteDao asisDao  = new AsistenteDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario u = (Usuario) request.getSession(false).getAttribute("usuario");
        if (u == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int idAsistente = asisDao.getIdAsistenteByUsuario(u.getId());

        // Recibimos los filtros que manda tu nuevo formFiltros del JSP
        String filtroEstado = request.getParameter("estado");
        String filtroFecha = request.getParameter("fecha");

        // Usamos tu nuevo método del DAO con filtros y JOINs
        List<Reserva> misReservas = reservaDao.getByAsistenteConFiltro(idAsistente, filtroEstado, filtroFecha);

        // Mantenemos los valores en la vista para que los inputs no se borren
        request.setAttribute("misReservas", misReservas);
        request.setAttribute("filtroEstado", filtroEstado);
        request.setAttribute("filtroFecha", filtroFecha);

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

            Reserva r = new Reserva();
            r.setIdEvento(idEvento);
            r.setIdAsistente(idAsistente);

            boolean ok = reservaDao.create(r);

            if (ok) {
                try {
                    // Usamos tu método getDetalleById para traer los datos cruzados
                    Reserva reservaCompleta = reservaDao.getDetalleById(r.getId());

                    // Diseño del correo
                    String html = """
                        <div style="background-color: #f5f5f5; padding: 40px 20px; font-family: Arial, sans-serif; text-align: center;">
                            <div style="background-color: #ffffff; max-width: 500px; margin: 0 auto; padding: 40px; border-radius: 12px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
                                <img src="https://i.postimg.cc/YSrdyH4Z/LOGOOO.png" alt="SRAE Logo" style="height: 80px; margin-bottom: 10px;">
                                <h1 style="color: #000000; font-size: 26px; margin-bottom: 15px; font-weight: 800;">¡Reserva confirmada!</h1>
                                <p style="color: #000000; font-size: 15px; margin-bottom: 30px; line-height: 1.6; font-weight: bold;">
                                    ¡Gracias! Tu lugar para el evento ha sido<br>reservado con éxito. Por favor, presenta<br>este correo al momento del ingreso.
                                </p>
                                <div style="background-color: #e8ecef; border-radius: 15px; padding: 25px; margin-bottom: 35px; text-align: center;">
                                    <h3 style="margin-top: 0; margin-bottom: 15px; font-size: 18px; color: #000000;">¡Detalles de la reserva!</h3>
                                    <p style="margin: 5px 0; font-size: 15px; color: #000000;"><strong>Evento:</strong> {0}</p>
                                    <p style="margin: 5px 0; font-size: 15px; color: #000000;"><strong>Fecha:</strong> {1}</p>
                                    <p style="margin: 5px 0; font-size: 15px; color: #000000;"><strong>Ubicación:</strong> {2}</p>
                                    <p style="margin: 5px 0; font-size: 15px; color: #000000;"><strong>Código:</strong> {3}</p>
                                </div>
                                <a href="http://localhost:8080/Events_war_exploded/reserva" style="display: inline-block; background-color: #0d8a5f; color: #ffffff; text-decoration: none; padding: 14px 28px; border-radius: 8px; font-weight: bold; font-size: 14px;">
                                    &#128197; VER EN MIS RESERVACIONES
                                </a>
                            </div>
                        </div>
                        """;

                    String nombreEv = reservaCompleta.getNombreEvento();
                    String fechaEv = reservaCompleta.getFechaEvento();
                    String lugar = reservaCompleta.getNombreEspacio() + " - " + reservaCompleta.getUbicacionEspacio();
                    String cod = reservaCompleta.getCodigoReserva();

                    EmailSender.sendMail(u.getEmail(), "Confirmación de reserva - SRAE",
                            MessageFormat.format(html, nombreEv, fechaEv, lugar, cod));

                } catch (Exception ex) {
                    System.err.println("Correo de reserva no enviado: " + ex.getMessage());
                }
            }

            // Redirigimos al historial de reservas
            response.sendRedirect(request.getContextPath() + "/reserva");

        } else if ("cancelar".equals(action)) {
            int idReserva = Integer.parseInt(request.getParameter("idReserva"));
            reservaDao.cancelar(idReserva);

            // Recuperamos los filtros ocultos del formulario modal para no perderlos
            String fEstado = request.getParameter("filtroEstado");
            String fFecha = request.getParameter("filtroFecha");
            response.sendRedirect(request.getContextPath() + "/reserva?estado=" + (fEstado!=null?fEstado:"") + "&fecha=" + (fFecha!=null?fFecha:""));

        } else if ("limpiarHistorial".equals(action)) {
            // Se ejecuta la nueva función que agregaste
            reservaDao.limpiarHistorial(idAsistente);

            String fEstado = request.getParameter("filtroEstado");
            String fFecha = request.getParameter("filtroFecha");
            response.sendRedirect(request.getContextPath() + "/reserva?estado=" + (fEstado!=null?fEstado:"") + "&fecha=" + (fFecha!=null?fFecha:""));
        }
    }
}