package com.example.events.model.dao;

import com.example.events.model.models.Reserva;
import com.example.events.DB.OracleConnectApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservaDao implements Dao<Reserva, Integer> {

    private final EventoDao eventoDao = new EventoDao();

    private String generarCodigo() {
        return "SRAE-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    @Override
    public boolean create(Reserva r) {
        String sqlReserva = "INSERT INTO RESERVA(id_evento, id_asistente, codigo_reserva, estado) VALUES(?, ?, ?, 'Reservado')";

        try (Connection con = OracleConnectApp.getConnection()) {
            con.setAutoCommit(false);

            if (!eventoDao.decrementarDisponibilidad(r.getIdEvento(), con)) {
                con.rollback();
                return false;
            }

            r.setCodigoReserva(generarCodigo());
            try (PreparedStatement ps = con.prepareStatement(sqlReserva, new String[]{"ID_RESERVA"})) {
                ps.setInt(1, r.getIdEvento());
                ps.setInt(2, r.getIdAsistente());
                ps.setString(3, r.getCodigoReserva());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) r.setId(rs.getInt(1));
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelar(int idReserva) {
        // Solo se puede cancelar si sigue "Reservado" y el evento todavía no ocurre.
        String sqlCheck = "SELECT r.id_evento FROM RESERVA r JOIN EVENTO e ON r.id_evento = e.id_evento " +
                "WHERE r.id_reserva = ? AND r.estado = 'Reservado' AND e.fecha_hora >= SYSTIMESTAMP";
        String sqlCancelar = "UPDATE RESERVA SET estado = 'Cancelado' WHERE id_reserva = ? AND estado = 'Reservado'";

        try (Connection con = OracleConnectApp.getConnection()) {
            con.setAutoCommit(false);

            int idEvento = -1;
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, idReserva);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) idEvento = rs.getInt("id_evento");
                }
            }

            if (idEvento == -1) {
                // Ya estaba cancelada/utilizada o el evento ya finalizó
                con.rollback();
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sqlCancelar)) {
                ps.setInt(1, idReserva);
                int filas = ps.executeUpdate();
                if (filas == 0) { con.rollback(); return false; }
            }

            eventoDao.incrementarDisponibilidad(idEvento, con);
            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reserva> getByAsistenteConFiltro(int idAsistente, String estado, String fecha) {
        List<Reserva> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT r.id_reserva, r.id_evento, r.id_asistente, r.codigo_reserva, r.estado, " +
                        "TO_CHAR(r.fecha_hora_reserva, 'DD/MM/YYYY HH24:MI') as fecha_reserva_fmt, " +
                        "e.nombre AS nombre_evento, " +
                        "TO_CHAR(e.fecha_hora, 'DD/MM/YYYY HH24:MI') as fecha_evento_fmt, " +
                        "esp.nombre_espacio, " +
                        "CASE WHEN e.fecha_hora < SYSTIMESTAMP THEN 1 ELSE 0 END AS finalizado " +
                        "FROM RESERVA r " +
                        "JOIN EVENTO e ON r.id_evento = e.id_evento " +
                        "JOIN ESPACIO esp ON e.id_espacio = esp.id_espacio " +
                        "WHERE r.id_asistente = ? "
        );

        // "Finalizado" no es un estado real en la tabla RESERVA: es una reserva
        // que sigue "Reservado" pero cuyo evento ya pasó. "Reservado" en el
        // filtro significa lo opuesto: reservas activas de eventos que aún no ocurren.
        boolean esFinalizado = estado != null && estado.trim().equalsIgnoreCase("Finalizado");
        boolean esReservado = estado != null && estado.trim().equalsIgnoreCase("Reservado");
        boolean esOtroEstado = estado != null && !estado.trim().isEmpty() && !esFinalizado && !esReservado;

        if (esFinalizado) {
            sql.append("AND LOWER(r.estado) = 'reservado' AND e.fecha_hora < SYSTIMESTAMP ");
        } else if (esReservado) {
            sql.append("AND LOWER(r.estado) = 'reservado' AND e.fecha_hora >= SYSTIMESTAMP ");
        } else if (esOtroEstado) {
            sql.append("AND LOWER(r.estado) = LOWER(?) ");
        }

        if (fecha != null && !fecha.trim().isEmpty()) {
            sql.append("AND TO_CHAR(e.fecha_hora, 'YYYY-MM-DD') = ? ");
        }

        sql.append("ORDER BY r.fecha_hora_reserva DESC");

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            ps.setInt(paramIndex++, idAsistente);

            if (esOtroEstado) {
                ps.setString(paramIndex++, estado.trim());
            }

            if (fecha != null && !fecha.trim().isEmpty()) {
                ps.setString(paramIndex++, fecha.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCompleto(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Reserva getDetalleById(int idReserva) {
        String sql = "SELECT r.id_reserva, r.id_evento, r.id_asistente, r.codigo_reserva, r.estado, " +
                "TO_CHAR(r.fecha_hora_reserva, 'DD/MM/YYYY HH24:MI') as fecha_reserva_fmt, " +
                "e.nombre AS nombre_evento, e.descripcion, " +
                "TO_CHAR(e.fecha_hora, 'DD/MM/YYYY HH24:MI') as fecha_evento_fmt, " +
                "esp.nombre_espacio, esp.ubicacion, " +
                "CASE WHEN e.fecha_hora < SYSTIMESTAMP THEN 1 ELSE 0 END AS finalizado " +
                "FROM RESERVA r " +
                "JOIN EVENTO e ON r.id_evento = e.id_evento " +
                "JOIN ESPACIO esp ON e.id_espacio = esp.id_espacio " +
                "WHERE r.id_reserva = ?";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reserva r = mapearCompleto(rs);
                    r.setDescripcionEvento(rs.getString("descripcion"));
                    r.setUbicacionEspacio(rs.getString("ubicacion"));
                    return r;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reserva> getAll() {
        return new ArrayList<>();
    }

    @Override
    public Reserva getById(Integer id) {
        String sql = "SELECT id_reserva, id_evento, id_asistente, codigo_reserva, estado, fecha_hora_reserva FROM RESERVA WHERE id_reserva = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public boolean update(Reserva r) {
        String sql = "UPDATE RESERVA SET estado = ? WHERE id_reserva = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getEstado());
            ps.setInt(2, r.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM RESERVA WHERE id_reserva = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Reserva mapear(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setId(rs.getInt("id_reserva"));
        r.setIdEvento(rs.getInt("id_evento"));
        r.setIdAsistente(rs.getInt("id_asistente"));
        r.setCodigoReserva(rs.getString("codigo_reserva"));
        r.setEstado(rs.getString("estado"));
        // "fecha_hora_reserva" solo existe en el SELECT de getById(); en las
        // consultas de historial esa columna viene formateada con TO_CHAR
        // bajo el alias "fecha_reserva_fmt" (ver mapearCompleto). Por eso
        // se intenta leerla y, si no está presente, simplemente se ignora
        // aquí para que mapearCompleto la complete después.
        try {
            r.setFechaHoraReserva(rs.getString("fecha_hora_reserva"));
        } catch (SQLException ex) {
            // columna no presente en este resultset, no es un error real
        }
        return r;
    }

    private Reserva mapearCompleto(ResultSet rs) throws SQLException {
        Reserva r = mapear(rs);
        r.setNombreEvento(rs.getString("nombre_evento"));
        r.setFechaHoraReserva(rs.getString("fecha_reserva_fmt"));
        r.setFechaEvento(rs.getString("fecha_evento_fmt"));
        r.setNombreEspacio(rs.getString("nombre_espacio"));
        r.setEventoFinalizado(rs.getInt("finalizado") == 1);
        return r;
    }
}