package model;

import java.util.PriorityQueue;

public class PriorityManager {

    private PriorityQueue<Ticket> colaPrioridad;

    public PriorityManager(){

        colaPrioridad =
                new PriorityQueue<>();
    }

    public void agregarTicket(Ticket ticket){

        colaPrioridad.add(ticket);
    }

    public Ticket obtenerSiguienteTicket(){

        return colaPrioridad.poll();
    }

    public boolean estaVacia(){

        return colaPrioridad.isEmpty();
    }
}