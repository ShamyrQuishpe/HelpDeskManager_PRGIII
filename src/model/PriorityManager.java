package model;

import java.util.PriorityQueue;

public class PriorityManager {
    private PriorityQueue<Ticket> cola;

    public PriorityManager(){

        cola = new PriorityQueue<>();
    }

    public void agregar(Ticket ticket){

        cola.add(ticket);

    }

    public Ticket siguienteTicket(){

        return cola.poll();

    }

}
