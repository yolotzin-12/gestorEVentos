package com.example.events.model.models;

public class Reserva {
    private int id;
    private int idEvento;
    private int idAsistente;
    private String codigoReserva;
    private String estado;
    private String fechaHoraReserva;

    // Campos auxiliares para la vista de Historial (obtenidos mediante JOIN)
    private String nombreEvento;
    private String fechaEvento;
    private String lugar;

    public Reserva() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdEvento() { return idEvento; }
    public void setIdEvento(int idEvento) { this.idEvento = idEvento; }

    public int getIdAsistente() { return idAsistente; }
    public void setIdAsistente(int idAsistente) { this.idAsistente = idAsistente; }

    public String getCodigoReserva() { return codigoReserva; }
    public void setCodigoReserva(String codigoReserva) { this.codigoReserva = codigoReserva; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaHoraReserva() { return fechaHoraReserva; }
    public void setFechaHoraReserva(String fechaHoraReserva) { this.fechaHoraReserva = fechaHoraReserva; }

    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }

    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }
}