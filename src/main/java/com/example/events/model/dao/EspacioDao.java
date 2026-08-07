package com.example.events.model.dao;

import com.example.events.model.models.Espacio;
import com.example.events.DB.OracleConnectApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspacioDao {

    public List<Espacio> getAll() {
        List<Espacio> lista = new ArrayList<>();
        String sql = "SELECT id_espacio, nombre_espacio, capacidad, ubicacion, horario FROM ESPACIO ORDER BY nombre_espacio";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Espacio esp = new Espacio();
                esp.setId(rs.getInt("id_espacio"));
                esp.setNombre(rs.getString("nombre_espacio"));
                esp.setCapacidad(rs.getObject("capacidad") != null ? rs.getInt("capacidad") : null);
                esp.setUbicacion(rs.getString("ubicacion"));
                esp.setHorario(rs.getString("horario"));
                lista.add(esp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public boolean insertEspacio(Espacio espacio) {
        String sql = "INSERT INTO ESPACIO (nombre_espacio, capacidad, ubicacion, horario) VALUES (?, ?, ?, ?)";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, espacio.getNombre());

            if (espacio.getCapacidad() != null) ps.setInt(2, espacio.getCapacidad());
            else ps.setNull(2, java.sql.Types.NUMERIC);

            ps.setString(3, espacio.getUbicacion());
            ps.setString(4, espacio.getHorario());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}