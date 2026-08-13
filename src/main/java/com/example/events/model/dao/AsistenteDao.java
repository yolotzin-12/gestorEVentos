package com.example.events.model.dao;

import com.example.events.model.models.Asistente;
import com.example.events.DB.OracleConnectApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsistenteDao implements Dao<Asistente, Integer> {

    public int getIdAsistenteByUsuario(int idUsuario) {
        String sql = "SELECT id_asistente FROM ASISTENTE WHERE id_usuario = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_asistente");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    public boolean tieneReservasActivas(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM RESERVA r JOIN ASISTENTE a ON r.id_asistente = a.id_asistente JOIN EVENTO e ON r.id_evento = e.id_evento WHERE a.id_usuario = ? AND LOWER(r.estado) = 'reservado' AND e.fecha_hora >= SYSTIMESTAMP";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean create(Asistente a) {
        String sql = "INSERT INTO ASISTENTE(id_usuario, telefono) VALUES(?, ?)";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getIdUsuario());
            ps.setString(2, a.getTelefono());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public List<Asistente> getAll() { return new ArrayList<>(); }
    @Override public Asistente getById(Integer id) { return null; }
    @Override public boolean update(Asistente a) { return false; }
    @Override public boolean delete(Integer id) { return false; }
}