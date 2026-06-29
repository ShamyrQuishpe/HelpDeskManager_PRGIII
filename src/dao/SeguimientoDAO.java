package dao;

import model.Seguimiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SeguimientoDAO {

    private Connection conexion;

    public SeguimientoDAO() {
        conexion = ConexionBD.conectar();
    }

    public boolean registrarSeguimiento(String comentario, LocalDateTime fecha, int idTicket, int idUsuario) {
        try {
            String sql = "INSERT INTO seguimiento(comentario, fecha, id_ticket, id_usuario) VALUES(?,?,?,?)";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, comentario);
            ps.setTimestamp(2, Timestamp.valueOf(fecha));
            ps.setInt(3, idTicket);
            ps.setInt(4, idUsuario);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Seguimiento> obtenerTodos() {
        ArrayList<Seguimiento> lista = new ArrayList<>();

        try {
            String sql = "SELECT * FROM seguimiento";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(crearSeguimientoDesdeResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public ArrayList<Seguimiento> obtenerPorTicket(int idTicket) {
        ArrayList<Seguimiento> lista = new ArrayList<>();

        try {
            String sql = "SELECT * FROM seguimiento WHERE id_ticket = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idTicket);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(crearSeguimientoDesdeResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public ArrayList<Seguimiento> obtenerPorUsuario(int idUsuario) {
        ArrayList<Seguimiento> lista = new ArrayList<>();

        try {
            String sql = "SELECT s.* FROM seguimiento s " +
                    "INNER JOIN tickets t ON s.id_ticket = t.id_ticket " +
                    "WHERE t.id_usuario = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(crearSeguimientoDesdeResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public ArrayList<Seguimiento> obtenerPorTecnico(int idTecnico) {
        ArrayList<Seguimiento> lista = new ArrayList<>();

        try {
            String sql = "SELECT s.* FROM seguimiento s " +
                    "INNER JOIN asignaciones a ON s.id_ticket = a.id_ticket " +
                    "WHERE a.id_tecnico = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idTecnico);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(crearSeguimientoDesdeResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Seguimiento buscarPorId(int idSeguimiento) {
        Seguimiento seguimiento = null;

        try {
            String sql = "SELECT * FROM seguimiento WHERE id_seguimiento = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idSeguimiento);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                seguimiento = crearSeguimientoDesdeResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return seguimiento;
    }

    public void actualizarSeguimiento(Seguimiento seguimiento) {
        try {
            String sql = "UPDATE seguimiento SET comentario = ?, fecha = ?, id_ticket = ?, id_usuario = ? WHERE id_seguimiento = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, seguimiento.getComentario());
            ps.setTimestamp(2, Timestamp.valueOf(seguimiento.getFecha()));
            ps.setInt(3, seguimiento.getIdTicket());
            ps.setInt(4, seguimiento.getIdUsuario());
            ps.setInt(5, seguimiento.getIdSeguimiento());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarSeguimiento(int idSeguimiento) {
        try {
            String sql = "DELETE FROM seguimiento WHERE id_seguimiento = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idSeguimiento);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Seguimiento crearSeguimientoDesdeResultSet(ResultSet rs) throws Exception {
        return new Seguimiento(
                rs.getInt("id_seguimiento"),
                rs.getString("comentario"),
                rs.getTimestamp("fecha").toLocalDateTime(),
                rs.getInt("id_ticket"),
                rs.getInt("id_usuario")
        );
    }
}
