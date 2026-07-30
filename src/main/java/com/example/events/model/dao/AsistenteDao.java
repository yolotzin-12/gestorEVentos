package com.example.events.model.dao;

import com.example.events.model.models.Asistente;
import com.example.events.DB.OracleConnectApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsistenteDao implements Dao<Asistente, Integer> {

    // Obtener id_asistente a partir del id_usuario (lo usa ReservaServlet)
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