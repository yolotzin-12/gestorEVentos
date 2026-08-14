package com.example.events.model.dao;

import com.example.events.model.models.Evento;
import com.example.events.DB.OracleConnectApp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDao implements Dao<Evento, Integer> {

    @Override
    public boolean create(Evento e) {
        String sql = "INSERT INTO EVENTO(id_organizador, id_espacio, id_categoria, nombre, descripcion, " +
                "capacidad_maxima, capacidad_disponible, fecha_hora, estado, imagen_url) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?)";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_EVENTO"})) {

            ps.setInt(1, e.getIdOrganizador());
            ps.setInt(2, e.getIdEspacio() > 0 ? e.getIdEspacio() : 1);
            ps.setInt(3, e.getIdCategoria());
            ps.setString(4, e.getNombre());
            ps.setString(5, e.getDescripcion());
            ps.setInt(6, e.getCapacidadMaxima());
            ps.setInt(7, e.getCapacidadMaxima());
            ps.setString(8, e.getFechaHora());
            ps.setString(9, e.getEstado() != null ? e.getEstado() : "Borrador");
            ps.setString(10, e.getImagenUrl());

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
                "e.fecha_hora, e.estado, e.imagen_url, esp.ubicacion, c.nombre AS nombre_categoria, " +
                "CASE WHEN e.fecha_hora < SYSTIMESTAMP THEN 1 ELSE 0 END AS finalizado " +
                "FROM EVENTO e " +
                "JOIN ESPACIO esp ON e.id_espacio = esp.id_espacio " +
                "JOIN CATEGORIA c ON e.id_categoria = c.id_categoria " +
                "WHERE e.estado = 'Disponible' " +
                "ORDER BY CASE WHEN e.fecha_hora >= SYSTIMESTAMP THEN 0 ELSE 1 END, " +
                "CASE WHEN e.fecha_hora >= SYSTIMESTAMP THEN e.fecha_hora END ASC, " +
                "CASE WHEN e.fecha_hora < SYSTIMESTAMP THEN e.fecha_hora END DESC";

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

    // Método para Administrador (Rol 1): Obtiene TODOS los eventos sin filtrar por estado
    public List<Evento> getAllAdmin() {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT e.id_evento, e.id_organizador, e.id_espacio, e.nombre, " +
                "e.descripcion, e.capacidad_maxima, e.capacidad_disponible, " +
                "e.fecha_hora, e.estado, e.imagen_url, esp.ubicacion, c.nombre AS nombre_categoria, " +
                "(SELECT COUNT(*) FROM RESERVA r WHERE r.id_evento = e.id_evento AND r.estado = 'Reservado') AS total_reservas, " +
                "CASE WHEN e.fecha_hora < SYSTIMESTAMP THEN 1 ELSE 0 END AS finalizado " +
                "FROM EVENTO e " +
                "JOIN ESPACIO esp ON e.id_espacio = esp.id_espacio " +
                "JOIN CATEGORIA c ON e.id_categoria = c.id_categoria " +
                "ORDER BY e.fecha_hora DESC";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Evento e = mapear(rs);
                e.setTotalReservas(rs.getInt("total_reservas"));
                lista.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public List<Evento> getByOrganizador(int idOrganizador) {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT e.id_evento, e.id_organizador, e.id_espacio, e.nombre, " +
                "e.descripcion, e.capacidad_maxima, e.capacidad_disponible, " +
                "e.fecha_hora, e.estado, e.imagen_url, esp.ubicacion, c.nombre AS nombre_categoria, " +
                "CASE WHEN e.fecha_hora < SYSTIMESTAMP THEN 1 ELSE 0 END AS finalizado " +
                "FROM EVENTO e " +
                "JOIN ESPACIO esp ON e.id_espacio = esp.id_espacio " +
                "JOIN CATEGORIA c ON e.id_categoria = c.id_categoria " +
                "WHERE e.id_organizador = ? " +
                "ORDER BY CASE WHEN e.fecha_hora >= SYSTIMESTAMP THEN 0 ELSE 1 END, " +
                "CASE WHEN e.fecha_hora >= SYSTIMESTAMP THEN e.fecha_hora END ASC, " +
                "CASE WHEN e.fecha_hora < SYSTIMESTAMP THEN e.fecha_hora END DESC";

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

    // Panel "Mis eventos" del organizador -> incluye conteo de reservas
    public List<Evento> getByOrganizadorConReservas(int idOrganizador) {
        List<Evento> lista = new ArrayList<>();
        String sql = "SELECT e.id_evento, e.id_organizador, e.id_espacio, e.nombre, " +
                "e.descripcion, e.capacidad_maxima, e.capacidad_disponible, " +
                "e.fecha_hora, e.estado, e.imagen_url, esp.ubicacion, c.nombre AS nombre_categoria, " +
                "(SELECT COUNT(*) FROM RESERVA r WHERE r.id_evento = e.id_evento " +
                " AND r.estado = 'Reservado') AS total_reservas " +
                "FROM EVENTO e " +
                "JOIN ESPACIO esp ON e.id_espacio = esp.id_espacio " +
                "JOIN CATEGORIA c ON e.id_categoria = c.id_categoria " +
                "WHERE e.id_organizador = ? ORDER BY e.fecha_hora DESC";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrganizador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Evento e = mapear(rs);
                    e.setTotalReservas(rs.getInt("total_reservas"));
                    lista.add(e);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    @Override
    public Evento getById(Integer id) {
        String sql = "SELECT e.id_evento, e.id_organizador, e.id_espacio, e.id_categoria, e.nombre, " +
                "e.descripcion, e.capacidad_maxima, e.capacidad_disponible, " +
                "e.fecha_hora, e.estado, e.imagen_url, esp.ubicacion, c.nombre AS nombre_categoria, " +
                "CASE WHEN e.fecha_hora < SYSTIMESTAMP THEN 1 ELSE 0 END AS finalizado " +
                "FROM EVENTO e JOIN ESPACIO esp ON e.id_espacio = esp.id_espacio " +
                "JOIN CATEGORIA c ON e.id_categoria = c.id_categoria " +
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
        String sql = "UPDATE EVENTO SET nombre=?, descripcion=?, id_categoria=?, id_espacio=?, " +
                "capacidad_maxima=?, capacidad_disponible=?, " +
                "fecha_hora=TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'), estado=?, imagen_url=? WHERE id_evento=?";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.setInt(3, e.getIdCategoria());
            ps.setInt(4, e.getIdEspacio());
            ps.setInt(5, e.getCapacidadMaxima());
            ps.setInt(6, e.getCapacidadMaxima());
            ps.setString(7, e.getFechaHora());
            ps.setString(8, e.getEstado());
            ps.setString(9, e.getImagenUrl());
            ps.setInt(10, e.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Cambiar estado del evento (ej: 'Cancelado', 'Disponible', 'Borrador')
    public boolean cambiarEstado(int idEvento, String nuevoEstado) {
        String sql = "UPDATE EVENTO SET estado = ? WHERE id_evento = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idEvento);
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

    // Elimina eventos pasados o cancelados pertenecientes a un organizador específico
    public boolean limpiarHistorialOrganizador(int idOrganizador) {
        String sql = "DELETE FROM EVENTO WHERE id_organizador = ? AND (estado = 'Cancelado' OR fecha_hora < SYSTIMESTAMP)";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrganizador);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean decrementarDisponibilidad(int idEvento, Connection con) throws SQLException {
        String sql = "UPDATE EVENTO SET capacidad_disponible = capacidad_disponible - 1 " +
                "WHERE id_evento = ? AND capacidad_disponible > 0";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            return ps.executeUpdate() > 0;
        }
    }

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

        try {
            e.setIdCategoria(rs.getInt("id_categoria"));
        } catch (SQLException ex) {
        }

        try {
            e.setNombreCategoria(rs.getString("nombre_categoria"));
        } catch (SQLException ex) {
        }

        try {
            e.setImagenUrl(rs.getString("imagen_url"));
        } catch (SQLException ex) {
        }

        try {
            e.setEventoFinalizado(rs.getInt("finalizado") == 1);
        } catch (SQLException ex) {
        }

        return e;
    }
}