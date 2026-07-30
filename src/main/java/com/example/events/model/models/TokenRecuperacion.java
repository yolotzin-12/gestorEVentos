package com.example.events.model.models;

import java.sql.Timestamp;

public class TokenRecuperacion {
    private int id;
    private int idUsuario;
    private String tokenHash;
    private Timestamp expiracion;
    private boolean usado;

    public TokenRecuperacion() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }

    public Timestamp getExpiracion() { return expiracion; }
    public void setExpiracion(Timestamp expiracion) { this.expiracion = expiracion; }

    public boolean isUsado() { return usado; }
    public void setUsado(boolean usado) { this.usado = usado; }
}