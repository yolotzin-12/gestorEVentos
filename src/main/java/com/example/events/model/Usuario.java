package com.example.events.model;

public class Usuario {

    // Atributos actuales del usuario
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String password;
    private String telefono;

    // 1. Constructor vacío
    public Usuario() {
    }

    // 2. Constructor con todos los parámetros
    public Usuario(String nombre, String apellidoPaterno, String apellidoMaterno, String email, String password, String telefono) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
    }

    // --- MÉTODOS GETTERS Y SETTERS ---

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // --- MÉTODO TOSTRING ---
    @Override
    public String toString() {
        return nombre + "," + apellidoPaterno + "," + apellidoMaterno + "," + email + "," + password + "," + telefono;
    }
}