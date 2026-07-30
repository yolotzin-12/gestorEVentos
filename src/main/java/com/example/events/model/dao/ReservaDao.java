package com.example.events.model.dao;

import com.example.events.model.models.Reserva;
import com.example.events.DB.OracleConnectApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservaDao implements Dao<Reserva, Integer> {

    private final EventoDao eventoDao = new EventoDao();

    // Genera código único
    private String generarCodigo() {
        return "SRAE-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    @Override
    public boolean create(Reserva r) {
        String sqlReserva = "INSERT INTO RESERVA(id_evento, id_asistente, codigo_reserva, estado) " +
                "VALUES(?, ?, ?, 'Reservado')";

        try (Connection con = OracleConnectApp.getConnection()) {
            con.setAutoCommit(false);

            // 1. Verificar y decrementar disponibilidad
            if (!eventoDao.decrementarDisponibilidad(r.getIdEvento(), con)) {
                con.rollback();
                return false; // sin disponibilidad
            }

            // 2. Insertar reserva
            r.setCodigoReserva(generarCodigo());
            try (PreparedStatement ps = con.prepareStatement(sqlReserva,
                    new String[]{"ID_RESERVA"})) {
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

    // Cancelar reserva (cambia estado + incrementa disponibilidad)
    public boolean cancelar(int idReserva) {
        String sqlCancelar = "UPDATE RESERVA SET estado = 'Cancelado' WHERE id_reserva = ? AND estado = 'Reservado'";

        try (Connection con = OracleConnectApp.getConnection()) {
            con.setAutoCommit(false);

            // Obtener idEvento antes de cancelar
            Reserva r = getById(idReserva);
            if (r == null || !"Reservado".equals(r.getEstado())) {
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sqlCancelar)) {
                ps.setInt(1, idReserva);
                int filas = ps.executeUpdate();
                if (filas == 0) { con.rollback(); return false; }
            }

            eventoDao.incrementarDisponibilidad(r.getIdEvento(), con);
            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Historial de reservas por asistente
    public List<Reserva> getByAsistente(int idAsistente) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.id_reserva, r.id_evento, r.id_asistente, " +
                "r.codigo_reserva, r.estado, r.fecha_hora_reserva " +
                "FROM RESERVA r WHERE r.id_asistente = ? ORDER BY r.fecha_hora_reserva DESC";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAsistente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Reserva> getAll() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT id_reserva, id_evento, id_asistente, codigo_reserva, " +
                "estado, fecha_hora_reserva FROM RESERVA ORDER BY fecha_hora_reserva DESC";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public Reserva getById(Integer id) {
        String sql = "SELECT id_reserva, id_evento, id_asistente, codigo_reserva, " +
                "estado, fecha_hora_reserva FROM RESERVA WHERE id_reserva = ?";
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
        r.setFechaHoraReserva(rs.getString("fecha_hora_reserva"));
        return r;
    }
}