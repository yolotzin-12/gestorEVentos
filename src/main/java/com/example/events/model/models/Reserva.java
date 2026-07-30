package com.example.events.model.models;

public class Reserva {
    private int id;
    private int idEvento;
    private int idAsistente;
    private String codigoReserva;
    private String estado;
    private String fechaHoraReserva;

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
}