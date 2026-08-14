package com.example.events.model.dao;

import com.example.events.DB.OracleConnectApp;
import com.example.events.model.models.Espacio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class EspacioDao {

    public boolean insertEspacio(Espacio espacio) {
        String sql = "INSERT INTO Espacio (nombre_espacio, capacidad, ubicacion, horario, imagen_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, espacio.getNombreEspacio());

            if (espacio.getCapacidad() != null) ps.setInt(2, espacio.getCapacidad());
            else ps.setNull(2, Types.NUMERIC);

            ps.setString(3, espacio.getUbicacion());
            ps.setString(4, espacio.getHorario());
            ps.setString(5, espacio.getImagenUrl());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Espacio> getAllEspacios() {
        List<Espacio> lista = new ArrayList<>();
        String sql = "SELECT id_espacio, nombre_espacio, capacidad, ubicacion, horario, imagen_url " +
                "FROM Espacio ORDER BY nombre_espacio";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Espacio getEspacioById(int idEspacio) {
        String sql = "SELECT id_espacio, nombre_espacio, capacidad, ubicacion, horario, imagen_url " +
                "FROM Espacio WHERE id_espacio = ?";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEspacio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizarEspacio(Espacio espacio) {
        String sql = "UPDATE Espacio SET nombre_espacio = ?, capacidad = ?, ubicacion = ?, " +
                "horario = ?, imagen_url = ? WHERE id_espacio = ?";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, espacio.getNombreEspacio());

            if (espacio.getCapacidad() != null) ps.setInt(2, espacio.getCapacidad());
            else ps.setNull(2, Types.NUMERIC);

            ps.setString(3, espacio.getUbicacion());
            ps.setString(4, espacio.getHorario());
            ps.setString(5, espacio.getImagenUrl());
            ps.setInt(6, espacio.getIdEspacio());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String eliminarEspacio(int idEspacio) {
        String sql = "DELETE FROM Espacio WHERE id_espacio = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEspacio);
            int filasAffected = ps.executeUpdate();
            return filasAffected > 0 ? "success" : "not_found";

        } catch (SQLException e) {
            if (e.getErrorCode() == 2292) {
                return "in_use";
            }
            e.printStackTrace();
            return "error";
        }
    }

    private Espacio mapear(ResultSet rs) throws SQLException {
        Espacio esp = new Espacio();
        esp.setIdEspacio(rs.getInt("id_espacio"));
        esp.setNombreEspacio(rs.getString("nombre_espacio"));

        int cap = rs.getInt("capacidad");
        esp.setCapacidad(rs.wasNull() ? null : cap);

        esp.setUbicacion(rs.getString("ubicacion"));
        esp.setHorario(rs.getString("horario"));
        esp.setImagenUrl(rs.getString("imagen_url"));
        return esp;
    }
}