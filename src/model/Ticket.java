package model;

import java.time.LocalDateTime;

public class Ticket implements Comparable<Ticket>{

    private int idTicket;

    private String titulo;

    private String descripcion;

    private String categoria;

    private String prioridad;

    private String estado;

    private LocalDateTime fechaCreacion;

    private int idUsuario;

    public Ticket(
            int idTicket,
            String titulo,
            String descripcion,
            String categoria,
            String prioridad,
            String estado,
            LocalDateTime fechaCreacion,
            int idUsuario
    ) {

        this.idTicket = idTicket;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.idUsuario = idUsuario;
    }

    @Override
    public int compareTo(Ticket t){

        return obtenerValor(t.prioridad)
                - obtenerValor(this.prioridad);
    }

    private int obtenerValor(String p){

        switch(p){

            case "CRITICA":
                return 4;

            case "ALTA":
                return 3;

            case "MEDIA":
                return 2;

            default:
                return 1;
        }
    }

    public int getIdTicket() {
        return idTicket;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {

        return
                "ID: " + idTicket +
                        " | " + titulo +
                        " | Prioridad: " + prioridad +
                        " | Estado: " + estado;
    }
}