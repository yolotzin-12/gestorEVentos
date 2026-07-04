package com.example.events.model.models;

public class Evento {
    private int id;
    private String nombre;
    private String categoria;
    private int capacidad;
    private String ubicacion;
    private String fecha;
    private boolean estado;

    public Evento() {
    }

    public Evento(int id, String nombre, String categoria, int capacidad, String ubicacion, String fecha, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return id + ',' + nombre + ',' + categoria + ',' + capacidad
                + ',' + ubicacion + ',' + fecha + ',' + estado;
    }
}