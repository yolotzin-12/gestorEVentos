package com.example.events.model.dao;

import com.example.events.model.Usuario;
import com.example.events.DB.OracleConnectApp;

import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    // Encripta una cadena de texto usando el algoritmo SHA-256
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

    // Registra un nuevo usuario junto con su contraseña y datos de asistente
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
            System.err.println("❌ Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    // Valida las credenciales de un usuario para permitirle el acceso
    public Usuario login(String email, String password) {
        if (email == null || password == null) return null;

        String sql = "SELECT u.id_usuario, u.id_rol, u.nombre, u.apellido_paterno, " +
                "u.apellido_materno, u.correo_electronico, u.activo " +
                "FROM USUARIO u " +
                "JOIN CONTRASENA c ON c.id_usuario = u.id_usuario AND c.activa = 1 " +
                "WHERE LOWER(u.correo_electronico) = ? AND c.hash_contrasena = ? AND u.activo = 1";

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
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en login: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Obtiene una lista con todos los usuarios registrados en el sistema
    public List<Usuario> getAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT ID_USUARIO, ID_ROL, NOMBRE, APELLIDO_PATERNO, APELLIDO_MATERNO, CORREO_ELECTRONICO, ACTIVO FROM USUARIO ORDER BY ID_USUARIO ASC";

        System.out.println("=== 🔍 INTENTANDO OBTENER USUARIOS DE LA BD ===");

        try (Connection con = OracleConnectApp.getConnection()) {

            if (con == null) {
                System.err.println("❌ ERROR: OracleConnectApp.getConnection() devolvió NULL.");
                return lista;
            }

            System.out.println("✅ Conexión establecida con Oracle. Ejecutando SQL...");

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                int contador = 0;
                while (rs.next()) {
                    contador++;
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("ID_USUARIO"));
                    u.setIdRol(rs.getInt("ID_ROL"));
                    u.setNombre(rs.getString("NOMBRE"));
                    u.setApellidoPaterno(rs.getString("APELLIDO_PATERNO"));
                    u.setApellidoMaterno(rs.getString("APELLIDO_MATERNO"));
                    u.setEmail(rs.getString("CORREO_ELECTRONICO"));
                    u.setActivo(rs.getInt("ACTIVO") == 1);
                    lista.add(u);
                }
                System.out.println(" TOTAL DE USUARIOS EXTRAÍDOS DE LA BD: " + contador);
            }
        } catch (SQLException e) {
            System.err.println(" ERROR AL EJECUTAR LA CONSULTA SQL EN getAll():");
            System.err.println("Detalle del error: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Marca a un usuario como inactivo utilizando su ID
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

    // Modifica el estado activo o inactivo de un usuario específico
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

    // Actualiza el rol asignado a un usuario
    public boolean asignarRol(int idUsuario, int idRol) {
        String sql = "UPDATE USUARIO SET id_rol = ? WHERE id_usuario = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRol);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Busca y devuelve los datos de un usuario mediante su correo electrónico
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

    // Reemplaza la contraseña de un usuario por una nueva
    public boolean actualizarContrasena(int idUsuario, String nuevaContrasena) {
        String sqlDesact = "UPDATE CONTRASENA SET activa = 0 WHERE id_usuario = ?";
        String sqlNueva = "INSERT INTO CONTRASENA(id_usuario, hash_contrasena, activa) VALUES(?, ?, 1)";

        Connection con = null;
        try {
            con = OracleConnectApp.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(sqlDesact)) {
                ps1.setInt(1, idUsuario);
                ps1.executeUpdate();
            }

            con.commit();

            try (PreparedStatement ps2 = con.prepareStatement(sqlNueva)) {
                ps2.setInt(1, idUsuario);
                ps2.setString(2, hashSHA256(nuevaContrasena));
                ps2.executeUpdate();
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

    // Permite al usuario cambiar su contraseña validando primero la contraseña actual

    public boolean cambiarContrasenaPerfil(int idUsuario, String contraActual, String nuevaContrasena) {
        String sqlVerificar = "SELECT hash_contrasena FROM CONTRASENA WHERE id_usuario = ? AND activa = 1";
        String sqlDesact = "UPDATE CONTRASENA SET activa = 0 WHERE id_usuario = ?";
        String sqlNueva = "INSERT INTO CONTRASENA(id_usuario, hash_contrasena, activa) VALUES(?, ?, 1)";

        Connection con = null;
        try {
            con = OracleConnectApp.getConnection();

            try (PreparedStatement ps = con.prepareStatement(sqlVerificar)) {
                ps.setInt(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next() || !rs.getString("hash_contrasena").equals(hashSHA256(contraActual))) {
                        return false;
                    }
                }
            }

            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(sqlDesact)) {
                ps1.setInt(1, idUsuario);
                ps1.executeUpdate();
            }

            con.commit();

            try (PreparedStatement ps2 = con.prepareStatement(sqlNueva)) {
                ps2.setInt(1, idUsuario);
                ps2.setString(2, hashSHA256(nuevaContrasena));
                ps2.executeUpdate();
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
}