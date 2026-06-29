package dao;

import model.Ticket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TicketDAO {

    private Connection conexion;

    public TicketDAO(){
        conexion = ConexionBD.conectar();
    }

    // CREAR TICKET
    public void crearTicket(Ticket ticket){
        try{

            String sql = "INSERT INTO tickets(" + "titulo,descripcion,categoria," + "prioridad,estado,fecha_creacion,id_usuario" + ") VALUES(?,?,?,?,?,?,?)";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, ticket.getTitulo());

            ps.setString(2, ticket.getDescripcion());

            ps.setString(3, ticket.getCategoria());

            ps.setString(4, ticket.getPrioridad());

            ps.setString(5, ticket.getEstado());

            ps.setTimestamp(6, Timestamp.valueOf(ticket.getFechaCreacion())
            );

            ps.setInt(7, ticket.getIdUsuario());

            ps.executeUpdate();

            System.out.println("Ticket creado");

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // LISTAR TODOS
    public ArrayList<Ticket> obtenerTickets(){
        ArrayList<Ticket> lista = new ArrayList<>();
        try{
            String sql = "SELECT * FROM tickets";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                Ticket t = new Ticket(

                                rs.getInt("id_ticket"),

                                rs.getString("titulo"),

                                rs.getString("descripcion"),

                                rs.getString("categoria"),

                                rs.getString("prioridad"),

                                rs.getString("estado"),

                                rs.getTimestamp("fecha_creacion").toLocalDateTime(),

                                rs.getInt("id_usuario")
                        );

                lista.add(t);
            }

        }
        catch(Exception e){

            e.printStackTrace();
        }

        return lista;
    }

    // TICKETS CLIENTE
    public ArrayList<Ticket> obtenerTicketsPorUsuario(int idUsuario){
        ArrayList<Ticket> lista = new ArrayList<>();

        try{
            String sql = "SELECT * FROM tickets " + "WHERE id_usuario = ?";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, idUsuario);

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()){

                Ticket t =
                        new Ticket(
                                rs.getInt("id_ticket"),

                                rs.getString("titulo"),

                                rs.getString("descripcion"),

                                rs.getString("categoria"),

                                rs.getString("prioridad"),

                                rs.getString("estado"),

                                rs.getTimestamp(
                                        "fecha_creacion"
                                ).toLocalDateTime(),

                                rs.getInt("id_usuario")
                        );
                lista.add(t);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return lista;
    }

    // TICKETS ASIGNADOS A TECNICO
    public ArrayList<Ticket> obtenerTicketsAsignadosTecnico(int idTecnico) {
        ArrayList<Ticket> lista = new ArrayList<>();

        try {
            String sql = "SELECT t.* FROM tickets t " +
                    "INNER JOIN asignaciones a ON t.id_ticket = a.id_ticket " +
                    "WHERE a.id_tecnico = ?";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, idTecnico);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ticket t = new Ticket(
                        rs.getInt("id_ticket"),

                        rs.getString("titulo"),

                        rs.getString("descripcion"),

                        rs.getString("categoria"),

                        rs.getString("prioridad"),

                        rs.getString("estado"),

                        rs.getTimestamp("fecha_creacion").toLocalDateTime(),

                        rs.getInt("id_usuario")
                );

                lista.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // CAMBIAR ESTADO
    public void cambiarEstado(
            int idTicket,
            String estado
    ){
        try{
            String sql = "UPDATE tickets " + "SET estado = ? " + "WHERE id_ticket = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, idTicket);
            ps.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    // CAMBIAR ESTADO Y PRIORIDAD
    public void cambiarEstadoYPrioridad(int idTicket, String estado, String prioridad){
        try{
            String sql = "UPDATE tickets SET estado = ?, prioridad = ? WHERE id_ticket = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setString(2, prioridad);
            ps.setInt(3, idTicket);
            ps.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminarTicket(int idTicket){
        try{
            String sql = "DELETE FROM tickets " + "WHERE id_ticket = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idTicket);
            ps.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
