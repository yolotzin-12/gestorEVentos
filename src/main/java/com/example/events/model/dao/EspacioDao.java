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
                esp.setCapacidad(rs.getInt("capacidad"));
                esp.setUbicacion(rs.getString("ubicacion"));
                esp.setHorario(rs.getString("horario"));
                lista.add(esp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}
