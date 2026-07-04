package com.example.events.model.dao;



import com.example.events.DB.OracleConnectApp;
import com.example.events.model.models.Evento;
import com.example.events.model.models.EventoEliel;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDaoEliel implements Dao<EventoEliel, Integer> {

    public static void main(String[] args) {
        EventoEliel newEvent=new EventoEliel();
        newEvent.setNOMBRE("Test1");
        newEvent.setCAPACIDAD(2);
        newEvent.setIMG("/test/img.png");
        EventoDaoEliel dao= new EventoDaoEliel();
        dao.create(newEvent);
    }

    // Consultas SQL básicas
    private static final String INSERT_QUERY = "INSERT INTO EVENTO (NOMBRE, IMG, CAPACIDAD) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT ID, NOMBRE, IMG, CAPACIDAD FROM EVENTO";
    private static final String SELECT_BY_ID_QUERY = "SELECT ID, NOMBRE, IMG, CAPACIDAD FROM EVENTO WHERE ID = ?";
    private static final String UPDATE_QUERY = "UPDATE EVENTO SET NOMBRE = ?, IMG = ?, CAPACIDAD = ? WHERE ID = ?";
    private static final String DELETE_QUERY = "DELETE FROM EVENTO WHERE ID = ?";

    @Override
    public boolean create(EventoEliel entidad) {
        // Usamos try-with-resources para asegurar el cierre de conexiones y statements
        try (Connection conn = OracleConnectApp.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_QUERY)) {

            ps.setString(1, entidad.getNOMBRE());
            ps.setString(2, entidad.getIMG()); // Mapeado al campo IMG de tu tabla
            ps.setInt(3, entidad.getCAPACIDAD());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar evento: " + e.getMessage());
            return false;
        }
    }



    @Override
    public List<EventoEliel> getAll() {
        List<EventoEliel> lista = new ArrayList<>();

        try (Connection conn = OracleConnectApp.getConnection();
             //PreparedStatement ps = conn.prepareStatement(SELECT_ALL_QUERY);
             PreparedStatement ps = conn.prepareStatement("SELECT ID, NOMBRE, IMG, CAPACIDAD FROM EVENTO");

             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EventoEliel e = new EventoEliel();
                e.setID(rs.getInt("ID"));
                e.setNOMBRE(rs.getString("NOMBRE"));
                e.setIMG(rs.getString("IMG")); // Asegúrate de tener este setter en tu clase Evento
                e.setCAPACIDAD(rs.getInt("CAPACIDAD"));
                lista.add(e);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener eventos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public EventoEliel getById(Integer id) {
        try (Connection conn = OracleConnectApp.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_QUERY)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EventoEliel e = new EventoEliel();
                    e.setID(rs.getInt("ID"));
                    e.setNOMBRE(rs.getString("NOMBRE"));
                    //e.setImg(rs.getString("IMG"));
                    //e.setCapacidad(rs.getInt("CAPACIDAD"));
                    return e;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar evento por ID: " + e.getMessage());
        }
        return null;
    }



    @Override
    public boolean update(EventoEliel entidad) {
        try (Connection conn = OracleConnectApp.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_QUERY)) {

            //ps.setString(1, entidad.getNombre());
            //ps.setString(2, entidad.getImg());
            //ps.setInt(3, entidad.getCapacidad());
           // ps.setInt(4, entidad.getId());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar evento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection conn = OracleConnectApp.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_QUERY)) {

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar evento: " + e.getMessage());
            return false;
        }
    }
}
