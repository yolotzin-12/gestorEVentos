package com.example.events.model.dao;

import com.example.events.DB.OracleConnectApp;
import com.example.events.model.models.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDao {

    public boolean insertCategoria(String nombre) {
        String sql = "INSERT INTO Categoria (nombre) VALUES (?)";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Categoria> getCategoriasActivas() {
        List<Categoria> lista = new ArrayList<>();
        // Consultamos solo las categorías activas según tu script de BD
        String sql = "SELECT id_categoria, nombre FROM Categoria WHERE activa = 1";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombre(rs.getString("nombre"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public String eliminarCategoria(int idCategoria) {
        String sql = "DELETE FROM Categoria WHERE id_categoria = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            int filasAffected = ps.executeUpdate();
            return filasAffected > 0 ? "success" : "not_found";

        } catch (SQLException e) {
            // Error 2292 en Oracle: "integrity constraint violated - child record found"
            if (e.getErrorCode() == 2292) {
                return "in_use";
            }
            e.printStackTrace();
            return "error";
        }
    }
}