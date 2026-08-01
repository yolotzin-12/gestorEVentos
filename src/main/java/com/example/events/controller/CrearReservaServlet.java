package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.events.DB.OracleConnectApp;
import com.example.events.model.dao.EventoDao;
import com.example.events.model.models.Evento;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "CrearReservaServlet", value = "/crearReserva")
public class CrearReservaServlet extends HttpServlet {

    private final EventoDao eventoDao = new EventoDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idEventoStr = request.getParameter("idEvento");
        String motivo = request.getParameter("asistencia");

        HttpSession session = request.getSession(false);

        if (idEventoStr == null || idEventoStr.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/evento");
            return;
        }

        int idEvento = Integer.parseInt(idEventoStr);

        // Obtener el ID del usuario en sesión
        int idUsuario = 1;
        if (session != null) {
            if (session.getAttribute("idUsuario") != null) {
                idUsuario = (int) session.getAttribute("idUsuario");
            } else if (session.getAttribute("usuario") != null) {
                Object userObj = session.getAttribute("usuario");
                try {
                    idUsuario = (int) userObj.getClass().getMethod("getId").invoke(userObj);
                } catch (Exception ignored) {}
            }
        }

        // 1. Verificar disponibilidad de aforo en el evento
        Evento evento = eventoDao.getById(idEvento);
        if (evento == null || evento.getCapacidadDisponible() <= 0) {
            request.setAttribute("error", "No hay aforo disponible para este evento.");
            request.setAttribute("evento", evento);
            request.getRequestDispatcher("reservar.jsp").forward(request, response);
            return;
        }

        // 2. Registrar la reserva con estado "Confirmado" y decrementar el aforo disponible
        try (Connection con = OracleConnectApp.getConnection()) {
            con.setAutoCommit(false);

            String sqlReserva = "INSERT INTO RESERVA (id_usuario, id_evento, fecha_reserva, estado) " +
                    "VALUES (?, ?, CURRENT_TIMESTAMP, 'Confirmado')";

            try (PreparedStatement ps = con.prepareStatement(sqlReserva)) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, idEvento);
                ps.executeUpdate();
            }

            // Decrementar capacidad disponible en la tabla EVENTO
            boolean decrementoOk = eventoDao.decrementarDisponibilidad(idEvento, con);

            if (decrementoOk) {
                con.commit();
                // 3. Redirigir a misReservas.jsp al completar con éxito
                response.sendRedirect(request.getContextPath() + "/misReservas.jsp");
            } else {
                con.rollback();
                request.setAttribute("error", "No se pudo procesar la reserva. Intenta de nuevo.");
                request.setAttribute("evento", evento);
                request.getRequestDispatcher("reservar.jsp").forward(request, response);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Error en la base de datos al realizar la reserva.");
            request.setAttribute("evento", evento);
            request.getRequestDispatcher("reservar.jsp").forward(request, response);
        }
    }
}