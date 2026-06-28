package model;

import java.time.LocalDateTime;

public class Asignacion {

    private int idAsignacion;
    private int idTicket;
    private int idTecnico;
    private LocalDateTime fechaAsignacion;

    public Asignacion() {
    }

    public Asignacion(int idTicket, int idTecnico, LocalDateTime fechaAsignacion) {
        this.idTicket = idTicket;
        this.idTecnico = idTecnico;
        this.fechaAsignacion = fechaAsignacion;
    }

    public Asignacion(int idAsignacion, int idTicket, int idTecnico, LocalDateTime fechaAsignacion) {
        this.idAsignacion = idAsignacion;
        this.idTicket = idTicket;
        this.idTecnico = idTecnico;
        this.fechaAsignacion = fechaAsignacion;
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    @Override
    public String toString() {
        return "Asignacion{" +
                "idAsignacion=" + idAsignacion +
                ", idTicket=" + idTicket +
                ", idTecnico=" + idTecnico +
                ", fechaAsignacion=" + fechaAsignacion +
                '}';
    }
}
