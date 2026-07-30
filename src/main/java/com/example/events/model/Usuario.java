package com.example.events.model;

public class Usuario {
    private int id;
    private int idRol;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String password;
    private String telefono;
    private boolean activo;

    public Usuario() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String ap) { this.apellidoPaterno = ap; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String am) { this.apellidoMaterno = am; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}