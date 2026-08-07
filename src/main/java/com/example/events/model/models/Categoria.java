package com.example.events.model.models;

public class Categoria {
    private int idCategoria;
    private String nombre;


    public Categoria(int idCategoria, String nombre) {
    }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}