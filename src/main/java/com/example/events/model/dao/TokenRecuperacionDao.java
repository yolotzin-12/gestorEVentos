package com.example.events.model.dao;

import com.example.events.model.models.TokenRecuperacion;
import com.example.events.DB.OracleConnectApp;

import java.sql.*;

public class TokenRecuperacionDao {

    // Crear token (vigencia 30 min)
    public boolean crear(int idUsuario, String tokenHash) {
        String sql = "INSERT INTO TOKEN_RECUPERACION(id_usuario, token_hash, expiracion, usado) " +
                "VALUES(?, ?, SYSTIMESTAMP + INTERVAL '30' MINUTE, 0)";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, tokenHash);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validar token (no expirado y no usado)
    public TokenRecuperacion validar(String tokenHash) {
        String sql = "SELECT id_token, id_usuario, token_hash, expiracion, usado " +
                "FROM TOKEN_RECUPERACION " +
                "WHERE token_hash = ? AND usado = 0 AND expiracion > SYSTIMESTAMP";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tokenHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TokenRecuperacion t = new TokenRecuperacion();
                    t.setId(rs.getInt("id_token"));
                    t.setIdUsuario(rs.getInt("id_usuario"));
                    t.setTokenHash(rs.getString("token_hash"));
                    t.setExpiracion(rs.getTimestamp("expiracion"));
                    t.setUsado(rs.getInt("usado") == 1);
                    return t;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // null = token inválido o expirado
    }

    // Marcar como usado después de restablecer contraseña
    public boolean marcarUsado(int idToken) {
        String sql = "UPDATE TOKEN_RECUPERACION SET usado = 1 WHERE id_token = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idToken);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}