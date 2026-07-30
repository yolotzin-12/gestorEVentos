package com.example.events.model.models;

public class Asistente {
    private int id;
    private int idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correoElectronico;
    private String telefono;

    public Asistente() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String ap) { this.apellidoPaterno = ap; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String am) { this.apellidoMaterno = am; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correo) { this.correoElectronico = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}