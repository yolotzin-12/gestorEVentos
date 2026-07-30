package com.example.events.model.dao;

import com.example.events.model.models.Administrador;
import com.example.events.DB.OracleConnectApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDao implements Dao<Administrador, Integer> {

    // Obtener id_admin a partir del id_usuario
    public int getIdAdminByUsuario(int idUsuario) {
        String sql = "SELECT id_admin FROM ADMINISTRADOR WHERE id_usuario = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_admin");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    // Crear administrador
    @Override
    public boolean create(Administrador a) {
        String sql = "INSERT INTO ADMINISTRADOR(id_usuario, nivel_acceso) VALUES(?, ?)";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_ADMIN"})) {
            ps.setInt(1, a.getIdUsuario());
            ps.setString(2, a.getNivelAcceso() != null ? a.getNivelAcceso() : "total");
            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) a.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }


    @Override
    public List<Administrador> getAll() {
        List<Administrador> lista = new ArrayList<>();
        String sql = "SELECT a.id_admin, a.id_usuario, a.nivel_acceso, " +
                "u.nombre, u.apellido_paterno, u.apellido_materno, u.correo_electronico " +
                "FROM ADMINISTRADOR a " +
                "JOIN USUARIO u ON a.id_usuario = u.id_usuario " +
                "ORDER BY u.nombre";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }


    @Override
    public Administrador getById(Integer id) {
        String sql = "SELECT a.id_admin, a.id_usuario, a.nivel_acceso, " +
                "u.nombre, u.apellido_paterno, u.apellido_materno, u.correo_electronico " +
                "FROM ADMINISTRADOR a " +
                "JOIN USUARIO u ON a.id_usuario = u.id_usuario " +
                "WHERE a.id_admin = ?";
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
    public boolean update(Administrador a) {
        String sql = "UPDATE ADMINISTRADOR SET nivel_acceso = ? WHERE id_admin = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNivelAcceso());
            ps.setInt(2, a.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }


    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM ADMINISTRADOR WHERE id_admin = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Administrador mapear(ResultSet rs) throws SQLException {
        Administrador a = new Administrador();
        a.setId(rs.getInt("id_admin"));
        a.setIdUsuario(rs.getInt("id_usuario"));
        a.setNivelAcceso(rs.getString("nivel_acceso"));
        a.setNombre(rs.getString("nombre"));
        a.setApellidoPaterno(rs.getString("apellido_paterno"));
        a.setApellidoMaterno(rs.getString("apellido_materno"));
        a.setCorreoElectronico(rs.getString("correo_electronico"));
        return a;
    }
}