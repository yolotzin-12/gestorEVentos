package com.example.events.model.models;

public class Reserva {
    private int id;
    private int idEvento;
    private int idAsistente;
    private String codigoReserva;
    private String estado;
    private String fechaHoraReserva;


    private String nombreEvento;
    private String descripcionEvento;
    private String fechaEvento;
    private String nombreEspacio;
    private String ubicacionEspacio;
    private boolean eventoFinalizado;

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

    public String getDescripcionEvento() { return descripcionEvento; }
    public void setDescripcionEvento(String descripcionEvento) { this.descripcionEvento = descripcionEvento; }

    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }

    public String getNombreEspacio() { return nombreEspacio; }
    public void setNombreEspacio(String nombreEspacio) { this.nombreEspacio = nombreEspacio; }

    public String getUbicacionEspacio() { return ubicacionEspacio; }
    public void setUbicacionEspacio(String ubicacionEspacio) { this.ubicacionEspacio = ubicacionEspacio; }

    // true cuando la fecha/hora del evento ya pasó (se usa para pintar
    // el botón de la fila en historialReservas.jsp como "Finalizado")
    public boolean isEventoFinalizado() { return eventoFinalizado; }
    public void setEventoFinalizado(boolean eventoFinalizado) { this.eventoFinalizado = eventoFinalizado; }
}