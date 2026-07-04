package com.example.events.model.models;

public class EventoEliel {

    private Integer ID;
    private String NOMBRE;
    private Integer CAPACIDAD;
    private String IMG;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public Integer getCAPACIDAD() {
        return CAPACIDAD;
    }

    public void setCAPACIDAD(Integer CAPACIDAD) {
        this.CAPACIDAD = CAPACIDAD;
    }

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    @Override
    public String toString() {
        return "EventoEliel{" +
                "ID=" + ID +
                ", NOMBRE='" + NOMBRE + '\'' +
                ", CAPACIDAD=" + CAPACIDAD +
                ", IMG='" + IMG + '\'' +
                '}';
    }
}
