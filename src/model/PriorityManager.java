package model;

import dao.TicketDAO;

import java.util.ArrayList;
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

    public Usuario seleccionarTecnicoDisponible(ArrayList<Usuario> tecnicos, TicketDAO ticketDAO) {
        PriorityQueue<TecnicoCarga> colaTecnicos = new PriorityQueue<>();

        for (Usuario tecnico : tecnicos) {
            ArrayList<Ticket> ticketsAsignados = ticketDAO.obtenerTicketsAsignadosTecnico(tecnico.getIdUsuario());
            int cargaPonderada = 0;
            int ticketsAbiertos = 0;

            for (Ticket ticket : ticketsAsignados) {
                if (!"CERRADO".equals(ticket.getEstado())) {
                    cargaPonderada += obtenerPesoPrioridad(ticket.getPrioridad());
                    ticketsAbiertos++;
                }
            }

            colaTecnicos.add(new TecnicoCarga(tecnico, cargaPonderada, ticketsAbiertos));
        }

        if (colaTecnicos.isEmpty()) {
            return null;
        }

        return colaTecnicos.poll().getTecnico();
    }

    private int obtenerPesoPrioridad(String prioridad) {
        switch (prioridad) {
            case "CRITICA":
                return 5;
            case "ALTA":
                return 3;
            case "MEDIA":
                return 2;
            default:
                return 1;
        }
    }

    private static class TecnicoCarga implements Comparable<TecnicoCarga> {

        private Usuario tecnico;
        private int cargaPonderada;
        private int ticketsAbiertos;

        public TecnicoCarga(Usuario tecnico, int cargaPonderada, int ticketsAbiertos) {
            this.tecnico = tecnico;
            this.cargaPonderada = cargaPonderada;
            this.ticketsAbiertos = ticketsAbiertos;
        }

        public Usuario getTecnico() {
            return tecnico;
        }

        @Override
        public int compareTo(TecnicoCarga otro) {
            if (this.cargaPonderada != otro.cargaPonderada) {
                return this.cargaPonderada - otro.cargaPonderada;
            }

            if (this.ticketsAbiertos != otro.ticketsAbiertos) {
                return this.ticketsAbiertos - otro.ticketsAbiertos;
            }

            return this.tecnico.getIdUsuario() - otro.tecnico.getIdUsuario();
        }
    }
}
