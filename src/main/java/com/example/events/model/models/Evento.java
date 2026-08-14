package com.example.events.model.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento {
    private int id;
    private int idOrganizador;
    private int idEspacio;
    private int idCategoria;
    private String nombre;
    private String nombreCategoria;
    private String imagenUrl;
    private String descripcion;
    private int capacidadMaxima;
    private int capacidadDisponible;
    private String ubicacion;
    private String fechaHora;
    private String estado;
    private int totalReservas;
    private boolean eventoFinalizado;

    public Evento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOrganizador() {
        return idOrganizador;
    }

    public void setIdOrganizador(int idOrganizador) {
        this.idOrganizador = idOrganizador;
    }

    public int getIdEspacio() {
        return idEspacio;
    }

    public void setIdEspacio(int idEspacio) {
        this.idEspacio = idEspacio;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public int getCapacidadDisponible() {
        return capacidadDisponible;
    }

    public void setCapacidadDisponible(int capacidadDisponible) {
        this.capacidadDisponible = capacidadDisponible;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    // CALCULA DINÁMICAMENTE LAS RESERVAS
    public int getTotalReservas() {
        if (totalReservas <= 0 && capacidadMaxima > 0 && capacidadDisponible < capacidadMaxima) {
            return capacidadMaxima - capacidadDisponible;
        }
        return totalReservas;
    }

    public void setTotalReservas(int totalReservas) {
        this.totalReservas = totalReservas;
    }

    // EVALÚA DINÁMICAMENTE SI EL EVENTO YA SUCEDIÓ
    public boolean isEventoFinalizado() {
        if (this.fechaHora != null && !this.fechaHora.trim().isEmpty()) {
            try {
                String f = this.fechaHora.replace("T", " ");
                if (f.length() == 16) f += ":00";

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime fechaEvento = LocalDateTime.parse(f, formatter);

                return fechaEvento.isBefore(LocalDateTime.now());
            } catch (Exception e) {
                // Si la fecha falla en parsearse por formato, usa el atributo directo
            }
        }
        return this.eventoFinalizado;
    }

    public void setEventoFinalizado(boolean eventoFinalizado) {
        this.eventoFinalizado = eventoFinalizado;
    }
}