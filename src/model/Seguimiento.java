package model;

import java.time.LocalDateTime;

public class Seguimiento {

    private int idSeguimiento;
    private String comentario;
    private LocalDateTime fecha;
    private int idTicket;
    private int idUsuario;

    public Seguimiento(int idSeguimiento, String comentario, LocalDateTime fecha, int idTicket, int idUsuario) {
        this.idSeguimiento = idSeguimiento;
        this.comentario = comentario;
        this.fecha = fecha;
        this.idTicket = idTicket;
        this.idUsuario = idUsuario;
    }

    public int getIdSeguimiento() {
        return idSeguimiento;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "ID: " + idSeguimiento +
                " | Ticket: " + idTicket +
                " | Usuario: " + idUsuario +
                " | Fecha: " + fecha +
                " | " + comentario;
    }
}
