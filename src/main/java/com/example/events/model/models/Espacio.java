package com.example.events.model.models;

public class Espacio {
    private int id;
    private String nombre;
    private Integer capacidad;
    private String ubicacion;
    private String horario;

    public Espacio() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
}