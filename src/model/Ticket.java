package model;

public class Ticket implements Comparable<Ticket>{
    private int idTicket;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String prioridad;
    private String estado;

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
}
