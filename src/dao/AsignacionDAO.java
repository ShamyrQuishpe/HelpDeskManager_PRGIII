package dao;

import model.Asignacion;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AsignacionDAO {

    private Connection conexion;

    public AsignacionDAO() {
        conexion = ConexionBD.conectar();
        crearTablaSiNoExiste();
    }

    private void crearTablaSiNoExiste() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS asignaciones (" +
                         "id_asignacion INT AUTO_INCREMENT PRIMARY KEY, " +
                         "id_ticket INT NOT NULL UNIQUE, " +
                         "id_tecnico INT NOT NULL, " +
                         "fecha_asignacion DATETIME, " +
                         "FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket) ON DELETE CASCADE, " +
                         "FOREIGN KEY (id_tecnico) REFERENCES usuarios(id_usuario) ON DELETE CASCADE" +
                         ")";
            Statement stmt = conexion.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Error al crear la tabla asignaciones: " + e.getMessage());
        }
    }

    // CREATE
    public void crearAsignacion(Asignacion asignacion) {
        try {
            String sql = "INSERT INTO asignaciones (id_ticket, id_tecnico, fecha_asignacion) VALUES (?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, asignacion.getIdTicket());
            ps.setInt(2, asignacion.getIdTecnico());
            ps.setTimestamp(3, Timestamp.valueOf(asignacion.getFechaAsignacion()));
            ps.executeUpdate();
            System.out.println("Asignación creada");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ ALL
    public ArrayList<Asignacion> obtenerAsignaciones() {
        ArrayList<Asignacion> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM asignaciones";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("fecha_asignacion");
                LocalDateTime fecha = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();
                Asignacion a = new Asignacion(
                        rs.getInt("id_asignacion"),
                        rs.getInt("id_ticket"),
                        rs.getInt("id_tecnico"),
                        fecha
                );
                lista.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // READ BY TICKET ID
    public Asignacion obtenerAsignacionPorTicket(int idTicket) {
        Asignacion a = null;
        try {
            String sql = "SELECT * FROM asignaciones WHERE id_ticket = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idTicket);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp ts = rs.getTimestamp("fecha_asignacion");
                LocalDateTime fecha = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();
                a = new Asignacion(
                        rs.getInt("id_asignacion"),
                        rs.getInt("id_ticket"),
                        rs.getInt("id_tecnico"),
                        fecha
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }

    // UPDATE
    public void actualizarAsignacion(Asignacion asignacion) {
        try {
            String sql = "UPDATE asignaciones SET id_tecnico = ?, fecha_asignacion = ? WHERE id_ticket = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, asignacion.getIdTecnico());
            ps.setTimestamp(2, Timestamp.valueOf(asignacion.getFechaAsignacion()));
            ps.setInt(3, asignacion.getIdTicket());
            ps.executeUpdate();
            System.out.println("Asignación actualizada");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void eliminarAsignacion(int idTicket) {
        try {
            String sql = "DELETE FROM asignaciones WHERE id_ticket = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idTicket);
            ps.executeUpdate();
            System.out.println("Asignación eliminada");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
