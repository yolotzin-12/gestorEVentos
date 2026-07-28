package com.example.events.model.dao;

import com.example.events.model.Usuario;
import com.example.events.DB.OracleConnectApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    // Método para crear (registrar) un usuario en la base de datos
    public boolean create(Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        // Ajusta los nombres de las columnas (nombre, apellido_paterno, etc.) a como estén exactamente en tu tabla de Oracle
        String sql = "INSERT INTO USUARIOS(nombre, apellido_paterno, apellido_materno, email, password, telefono) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidoPaterno());
            ps.setString(3, usuario.getApellidoMaterno());
            ps.setString(4, usuario.getEmail());
            ps.setString(5, usuario.getPassword());
            ps.setString(6, usuario.getTelefono());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al intentar registrar al usuario.");
            e.printStackTrace();
            return false;
        }
    }

    // Método para iniciar sesión
    public boolean login(String email, String password) {
        if (email == null || password == null) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM USUARIOS WHERE email = ? AND password = ?";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim());
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Si el conteo es mayor a 0, las credenciales son correctas
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar realizar el login.");
            e.printStackTrace();
        }
        return false;
    }

    // Método para obtener todos los usuarios (opcional, basado en tu ejemplo)
    public List<Usuario> getAll() {
        List<Usuario> listaUsuarios = new ArrayList<>();
        String sql = "SELECT * FROM USUARIOS";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setNombre(rs.getString("nombre"));
                u.setApellidoPaterno(rs.getString("apellido_paterno"));
                u.setApellidoMaterno(rs.getString("apellido_materno"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setTelefono(rs.getString("telefono"));

                listaUsuarios.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar obtener los usuarios.");
            e.printStackTrace();
        }
        return listaUsuarios;
    }
}