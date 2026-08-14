package com.example.events.model.dao;

import com.example.events.model.Usuario;
import com.example.events.DB.OracleConnectApp;

import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    public static String hashSHA256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear contraseña", e);
        }
    }

    public boolean create(Usuario usuario) {
        if (usuario == null || usuario.getEmail() == null) return false;

        String sqlUsuario = "INSERT INTO USUARIO(id_rol, nombre, apellido_paterno, " +
                "apellido_materno, correo_electronico, activo) " +
                "VALUES(3, ?, ?, ?, ?, 1)";

        String sqlContra = "INSERT INTO CONTRASENA(id_usuario, hash_contrasena, activa) VALUES(?, ?, 1)";
        String sqlAsis = "INSERT INTO ASISTENTE(id_usuario, telefono) VALUES(?, ?)";

        Connection con = null;
        try {
            con = OracleConnectApp.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlUsuario, new String[]{"ID_USUARIO"})) {
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getApellidoPaterno());
                ps.setString(3, usuario.getApellidoMaterno());
                ps.setString(4, usuario.getEmail().trim().toLowerCase());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }

            try (PreparedStatement ps = con.prepareStatement(sqlContra)) {
                ps.setInt(1, usuario.getId());
                ps.setString(2, hashSHA256(usuario.getPassword()));
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(sqlAsis)) {
                ps.setInt(1, usuario.getId());
                ps.setString(2, usuario.getTelefono() != null ? usuario.getTelefono() : "");
                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    public Usuario login(String email, String password) {
        if (email == null || password == null) return null;

        String sql = "SELECT u.id_usuario, u.id_rol, u.nombre, u.apellido_paterno, " +
                "u.apellido_materno, u.correo_electronico, u.activo, u.foto_url, " +
                "COALESCE(a.telefono, o.telefono, ad.telefono) AS telefono " +
                "FROM USUARIO u " +
                "JOIN CONTRASENA c ON c.id_usuario = u.id_usuario AND c.activa = 1 " +
                "LEFT JOIN ASISTENTE a ON u.id_usuario = a.id_usuario " +
                "LEFT JOIN ORGANIZADOR o ON u.id_usuario = o.id_usuario " +
                "LEFT JOIN ADMINISTRADOR ad ON u.id_usuario = ad.id_usuario " +
                "WHERE LOWER(u.correo_electronico) = ? AND c.hash_contrasena = ?";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());
            ps.setString(2, hashSHA256(password));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    u.setIdRol(rs.getInt("id_rol"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellidoPaterno(rs.getString("apellido_paterno"));
                    u.setApellidoMaterno(rs.getString("apellido_materno"));
                    u.setEmail(rs.getString("correo_electronico"));
                    u.setActivo(rs.getInt("activo") == 1);
                    u.setFotoUrl(rs.getString("foto_url"));
                    u.setTelefono(rs.getString("telefono"));
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Usuario> getAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT ID_USUARIO, ID_ROL, NOMBRE, APELLIDO_PATERNO, APELLIDO_MATERNO, CORREO_ELECTRONICO, ACTIVO, FOTO_URL FROM USUARIO ORDER BY ID_USUARIO ASC";

        try (Connection con = OracleConnectApp.getConnection()) {
            if (con == null) return lista;

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("ID_USUARIO"));
                    u.setIdRol(rs.getInt("ID_ROL"));
                    u.setNombre(rs.getString("NOMBRE"));
                    u.setApellidoPaterno(rs.getString("APELLIDO_PATERNO"));
                    u.setApellidoMaterno(rs.getString("APELLIDO_MATERNO"));
                    u.setEmail(rs.getString("CORREO_ELECTRONICO"));
                    u.setActivo(rs.getInt("ACTIVO") == 1);
                    u.setFotoUrl(rs.getString("FOTO_URL"));
                    lista.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean deshabilitar(int idUsuario) {
        String sql = "UPDATE USUARIO SET activo = 0 WHERE id_usuario = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarEstado(int idUsuario, boolean estado) {
        String sql = "UPDATE USUARIO SET activo = ? WHERE id_usuario = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado ? 1 : 0);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int asignarRol(int idUsuario, int idRolNuevo) {
        int resultado = 0;
        Connection con = null;
        PreparedStatement psCheckReservas = null;
        PreparedStatement psActualizarRol = null;
        PreparedStatement psEliminarAsistente = null;

        try {
            con = OracleConnectApp.getConnection();
            con.setAutoCommit(false);

            if (idRolNuevo != 3) {
                String sqlCheck = "SELECT COUNT(*) FROM RESERVA r INNER JOIN ASISTENTE a ON r.id_asistente = a.id_asistente WHERE a.id_usuario = ?";
                psCheckReservas = con.prepareStatement(sqlCheck);
                psCheckReservas.setInt(1, idUsuario);
                try (ResultSet rsCheck = psCheckReservas.executeQuery()) {
                    if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                        return -1;
                    }
                }
            }

            String sqlRol = "UPDATE USUARIO SET id_rol = ? WHERE id_usuario = ?";
            psActualizarRol = con.prepareStatement(sqlRol);
            psActualizarRol.setInt(1, idRolNuevo);
            psActualizarRol.setInt(2, idUsuario);
            psActualizarRol.executeUpdate();

            if (idRolNuevo == 2) {
                String sqlCheckOrg = "SELECT COUNT(*) FROM ORGANIZADOR WHERE id_usuario = ?";
                try (PreparedStatement psCheckOrg = con.prepareStatement(sqlCheckOrg)) {
                    psCheckOrg.setInt(1, idUsuario);
                    try (ResultSet rsCheckOrg = psCheckOrg.executeQuery()) {
                        if (rsCheckOrg.next() && rsCheckOrg.getInt(1) == 0) {
                            String sqlInsertOrg = "INSERT INTO ORGANIZADOR (id_usuario, organizacion) VALUES (?, 'Organización Pendiente')";
                            try (PreparedStatement psInsertOrg = con.prepareStatement(sqlInsertOrg)) {
                                psInsertOrg.setInt(1, idUsuario);
                                psInsertOrg.executeUpdate();
                            }
                        }
                    }
                }

                String sqlDelAsis = "DELETE FROM ASISTENTE WHERE id_usuario = ?";
                psEliminarAsistente = con.prepareStatement(sqlDelAsis);
                psEliminarAsistente.setInt(1, idUsuario);
                psEliminarAsistente.executeUpdate();
            }

            con.commit();
            resultado = 1;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            try {
                if (psCheckReservas != null) psCheckReservas.close();
                if (psActualizarRol != null) psActualizarRol.close();
                if (psEliminarAsistente != null) psEliminarAsistente.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultado;
    }

    public Usuario getByEmail(String email) {
        if (email == null) return null;

        String sql = "SELECT id_usuario, id_rol, nombre, correo_electronico, activo " +
                "FROM USUARIO WHERE LOWER(correo_electronico) = ?";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    u.setIdRol(rs.getInt("id_rol"));
                    u.setNombre(rs.getString("nombre"));
                    u.setEmail(rs.getString("correo_electronico"));
                    u.setActivo(rs.getInt("activo") == 1);
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario getById(int idUsuario) {
        String sql = "SELECT id_usuario, nombre, correo_electronico FROM USUARIO WHERE id_usuario = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setEmail(rs.getString("correo_electronico"));
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizarContrasena(int idUsuario, String nuevaContrasena) {
        String sqlActualizar = "UPDATE CONTRASENA SET hash_contrasena = ? WHERE id_usuario = ? AND activa = 1";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlActualizar)) {

            ps.setString(1, hashSHA256(nuevaContrasena));
            ps.setInt(2, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarContrasenaPerfil(int idUsuario, String contraActual, String nuevaContrasena) {
        String sqlVerificar = "SELECT id_usuario FROM CONTRASENA WHERE id_usuario = ? AND hash_contrasena = ? AND activa = 1";
        String sqlActualizar = "UPDATE CONTRASENA SET hash_contrasena = ? WHERE id_usuario = ? AND activa = 1";

        try (Connection con = OracleConnectApp.getConnection()) {

            try (PreparedStatement ps = con.prepareStatement(sqlVerificar)) {
                ps.setInt(1, idUsuario);
                ps.setString(2, hashSHA256(contraActual));
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return false;
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(sqlActualizar)) {
                ps.setString(1, hashSHA256(nuevaContrasena));
                ps.setInt(2, idUsuario);
                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPerfil(Usuario u) {
        String sqlUsuario = "UPDATE USUARIO SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, correo_electronico = ?, foto_url = ? WHERE id_usuario = ?";
        String sqlTelefono = "";
        String sqlInsert = "";

        if (u.getIdRol() == 1) {
            sqlTelefono = "UPDATE ADMINISTRADOR SET telefono = ? WHERE id_usuario = ?";
            sqlInsert = "INSERT INTO ADMINISTRADOR (id_usuario, telefono, nivel_acceso) VALUES (?, ?, 'total')";
        } else if (u.getIdRol() == 2) {
            sqlTelefono = "UPDATE ORGANIZADOR SET telefono = ? WHERE id_usuario = ?";
            sqlInsert = "INSERT INTO ORGANIZADOR (id_usuario, telefono) VALUES (?, ?)";
        } else if (u.getIdRol() == 3) {
            sqlTelefono = "UPDATE ASISTENTE SET telefono = ? WHERE id_usuario = ?";
            sqlInsert = "INSERT INTO ASISTENTE (id_usuario, telefono) VALUES (?, ?)";
        }

        Connection con = null;
        try {
            con = OracleConnectApp.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement psU = con.prepareStatement(sqlUsuario)) {
                psU.setString(1, u.getNombre());
                psU.setString(2, u.getApellidoPaterno());
                psU.setString(3, u.getApellidoMaterno());
                psU.setString(4, u.getEmail().trim().toLowerCase());
                psU.setString(5, u.getFotoUrl());
                psU.setInt(6, u.getId());
                psU.executeUpdate();
            }

            if (!sqlTelefono.isEmpty()) {
                try (PreparedStatement psT = con.prepareStatement(sqlTelefono)) {
                    psT.setString(1, u.getTelefono());
                    psT.setInt(2, u.getId());
                    int filasActualizadas = psT.executeUpdate();

                    if (filasActualizadas == 0 && !sqlInsert.isEmpty()) {
                        try (PreparedStatement psI = con.prepareStatement(sqlInsert)) {
                            psI.setInt(1, u.getId());
                            psI.setString(2, u.getTelefono());
                            psI.executeUpdate();
                        }
                    }
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
}