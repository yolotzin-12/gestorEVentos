package com.example.events.model.dao;

import com.example.events.model.models.Evento;
import com.example.events.DB.OracleConnectApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDao implements Dao<Evento, Integer> {

    @Override
    public boolean create(Evento e) {
        String sql = "INSERT INTO EVENTO(id_organizador, id_espacio, nombre, descripcion, " +
                "capacidad_maxima, capacidad_disponible, fecha_hora, estado) " +
                "VALUES(?, ?, ?, ?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD'), ?)";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_EVENTO"})) {

            ps.setInt(1, e.getIdOrganizador());
            ps.setInt(2, e.getIdEspacio() > 0 ? e.getIdEspacio() : 1); // espacio por defecto
            ps.setString(3, e.getNombre());
            ps.setString(4, e.getDescripcion());
            ps.setInt(5, e.getCapacidadMaxima());
            ps.setInt(6, e.getCapacidadMaxima()); // disponible = máxima al crear
            ps.setString(7, e.getFechaHora());
            ps.setString(8, e.getEstado() != null ? e.getEstado() : "Borrador");

            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) e.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Evento> getAll() {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT e.id_evento, e.id_organizador, e.id_espacio, e.nombre, " +
                "e.descripcion, e.capacidad_maxima, e.capacidad_disponible, " +
                "e.fecha_hora, e.estado, esp.ubicacion " +
                "FROM EVENTO e JOIN ESPACIO esp ON e.id_espacio = esp.id_espacio " +
                "WHERE e.estado = 'Disponible' ORDER BY e.fecha_hora";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // Obtener todos los eventos de un organizador (panel del organizador)
    public List<Evento> getByOrganizador(int idOrganizador) {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT e.id_evento, e.id_organizador, e.id_espacio, e.nombre, " +
                "e.descripcion, e.capacidad_maxima, e.capacidad_disponible, " +
                "e.fecha_hora, e.estado, esp.ubicacion " +
                "FROM EVENTO e JOIN ESPACIO esp ON e.id_espacio = esp.id_espacio " +
                "WHERE e.id_organizador = ? ORDER BY e.fecha_hora DESC";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrganizador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    @Override
    public Evento getById(Integer id) {
        String sql = "SELECT e.id_evento, e.id_organizador, e.id_espacio, e.nombre, " +
                "e.descripcion, e.capacidad_maxima, e.capacidad_disponible, " +
                "e.fecha_hora, e.estado, esp.ubicacion " +
                "FROM EVENTO e JOIN ESPACIO esp ON e.id_espacio = esp.id_espacio " +
                "WHERE e.id_evento = ?";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Evento e) {
        String sql = "UPDATE EVENTO SET nombre=?, descripcion=?, capacidad_maxima=?, " +
                "fecha_hora=TO_TIMESTAMP(?, 'YYYY-MM-DD'), estado=? WHERE id_evento=?";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.setInt(3, e.getCapacidadMaxima());
            ps.setString(4, e.getFechaHora());
            ps.setString(5, e.getEstado());
            ps.setInt(6, e.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM EVENTO WHERE id_evento = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Decrementar disponibilidad al reservar
    public boolean decrementarDisponibilidad(int idEvento, Connection con) throws SQLException {
        String sql = "UPDATE EVENTO SET capacidad_disponible = capacidad_disponible - 1 " +
                "WHERE id_evento = ? AND capacidad_disponible > 0";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            return ps.executeUpdate() > 0;
        }
    }

    // Incrementar disponibilidad al cancelar
    public boolean incrementarDisponibilidad(int idEvento, Connection con) throws SQLException {
        String sql = "UPDATE EVENTO SET capacidad_disponible = capacidad_disponible + 1 " +
                "WHERE id_evento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            return ps.executeUpdate() > 0;
        }
    }

    private Evento mapear(ResultSet rs) throws SQLException {
        Evento e = new Evento();
        e.setId(rs.getInt("id_evento"));
        e.setIdOrganizador(rs.getInt("id_organizador"));
        e.setIdEspacio(rs.getInt("id_espacio"));
        e.setNombre(rs.getString("nombre"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setCapacidadMaxima(rs.getInt("capacidad_maxima"));
        e.setCapacidadDisponible(rs.getInt("capacidad_disponible"));
        e.setFechaHora(rs.getString("fecha_hora"));
        e.setEstado(rs.getString("estado"));
        e.setUbicacion(rs.getString("ubicacion"));
        return e;
    }
}