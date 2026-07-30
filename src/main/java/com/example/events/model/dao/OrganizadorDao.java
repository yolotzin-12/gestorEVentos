package com.example.events.model.dao;

import com.example.events.model.models.Organizador;
import com.example.events.DB.OracleConnectApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizadorDao implements Dao<Organizador, Integer> {

    // Obtener id_organizador a partir del id_usuario (lo usa EventoServlet)
    public int getIdOrganizadorByUsuario(int idUsuario) {
        String sql = "SELECT id_organizador FROM ORGANIZADOR WHERE id_usuario = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_organizador");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    @Override
    public boolean create(Organizador o) {
        String sql = "INSERT INTO ORGANIZADOR(id_usuario, organizacion) VALUES(?, ?)";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, o.getIdUsuario());
            ps.setString(2, o.getOrganizacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override public List<Organizador> getAll() { return new ArrayList<>(); }
    @Override public Organizador getById(Integer id) { return null; }
    @Override public boolean update(Organizador o) { return false; }
    @Override public boolean delete(Integer id) { return false; }
}