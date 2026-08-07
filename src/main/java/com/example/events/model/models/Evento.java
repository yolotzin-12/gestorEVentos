package com.example.events.model.models;

public class Evento {
    private int id;
    private int idOrganizador;
    private int idEspacio;
    private int idCategoria;
    private String nombre;
    private String categoria;
    private String descripcion;
    private int capacidadMaxima;
    private int capacidadDisponible;
    private String ubicacion;
    private String fechaHora;
    private String estado; // "Borrador", "Disponible", "Cancelado"

    public Evento() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdOrganizador() { return idOrganizador; }
    public void setIdOrganizador(int idOrganizador) { this.idOrganizador = idOrganizador; }

    public int getIdEspacio() { return idEspacio; }
    public void setIdEspacio(int idEspacio) { this.idEspacio = idEspacio; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }

    public int getCapacidadDisponible() { return capacidadDisponible; }
    public void setCapacidadDisponible(int capacidadDisponible) { this.capacidadDisponible = capacidadDisponible; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}