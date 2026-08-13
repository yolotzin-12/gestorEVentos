package com.example.events.model.dao;

import com.example.events.model.models.Organizador;
import com.example.events.DB.OracleConnectApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizadorDao implements Dao<Organizador, Integer> {

    public List<Organizador> getAllOrganizadores() {
        List<Organizador> lista = new ArrayList<>();
        String sql = "SELECT o.id_organizador, o.organizacion, u.nombre, u.apellido_paterno " +
                "FROM Organizador o JOIN Usuario u ON o.id_usuario = u.id_usuario";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Organizador org = new Organizador();
                org.setId(rs.getInt("id_organizador"));
                org.setNombre(rs.getString("nombre"));
                org.setApellidoPaterno(rs.getString("apellido_paterno"));
                org.setOrganizacion(rs.getString("organizacion"));

                lista.add(org);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

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

    public boolean tieneEventosActivos(int idUsuario) {
        String sql = "SELECT COUNT(*) FROM EVENTO e JOIN ORGANIZADOR o ON e.id_organizador = o.id_organizador WHERE o.id_usuario = ? AND e.fecha_hora >= SYSTIMESTAMP AND LOWER(e.estado) != 'cancelado'";
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
    public boolean create(Organizador o) {
        String sql = "INSERT INTO ORGANIZADOR(id_usuario, organizacion) VALUES(?, ?)";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, o.getIdUsuario());
            ps.setString(2, o.getOrganizacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean actualizarOrganizacion(int idOrganizador, String nuevaOrganizacion) {
        String sql = "UPDATE ORGANIZADOR SET organizacion = ? WHERE id_organizador = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevaOrganizacion);
            ps.setInt(2, idOrganizador);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override public List<Organizador> getAll() { return new ArrayList<>(); }
    @Override public Organizador getById(Integer id) { return null; }
    @Override public boolean update(Organizador o) { return false; }
    @Override public boolean delete(Integer id) { return false; }
}