package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class   ReporteDAO {

    private Connection conexion;

    public ReporteDAO() {
        conexion = ConexionBD.conectar();
    }

    public int contarTicketsPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        int total = 0;

        try {
            String sql = "SELECT COUNT(*) AS total FROM tickets " +
                    "WHERE fecha_creacion BETWEEN ? AND ?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setTimestamp(1, inicioDelDia(fechaInicio));
            ps.setTimestamp(2, finDelDia(fechaFin));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public int contarTicketsPorEstado(String estado, LocalDate fechaInicio, LocalDate fechaFin) {
        int total = 0;

        try {
            String sql = "SELECT COUNT(*) AS total FROM tickets " +
                    "WHERE estado = ? AND fecha_creacion BETWEEN ? AND ?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setTimestamp(2, inicioDelDia(fechaInicio));
            ps.setTimestamp(3, finDelDia(fechaFin));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public Map<String, Integer> contarTicketsPorPrioridad(LocalDate fechaInicio, LocalDate fechaFin) {
        Map<String, Integer> datos = new LinkedHashMap<>();

        datos.put("BAJA", 0);
        datos.put("MEDIA", 0);
        datos.put("ALTA", 0);
        datos.put("CRITICA", 0);

        try {
            String sql = "SELECT prioridad, COUNT(*) AS total FROM tickets " +
                    "WHERE fecha_creacion BETWEEN ? AND ? " +
                    "GROUP BY prioridad";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setTimestamp(1, inicioDelDia(fechaInicio));
            ps.setTimestamp(2, finDelDia(fechaFin));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                datos.put(rs.getString("prioridad"), rs.getInt("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datos;
    }

    public Map<String, Integer> contarTicketsPorCategoria(LocalDate fechaInicio, LocalDate fechaFin) {
        Map<String, Integer> datos = new LinkedHashMap<>();

        datos.put("Hardware", 0);
        datos.put("Software", 0);
        datos.put("Red", 0);
        datos.put("Otro", 0);

        try {
            String sql = "SELECT categoria, COUNT(*) AS total FROM tickets " +
                    "WHERE fecha_creacion BETWEEN ? AND ? " +
                    "GROUP BY categoria";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setTimestamp(1, inicioDelDia(fechaInicio));
            ps.setTimestamp(2, finDelDia(fechaFin));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                datos.put(rs.getString("categoria"), rs.getInt("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datos;
    }

    public Map<String, Integer> contarTicketsPorTecnico(LocalDate fechaInicio, LocalDate fechaFin) {
        Map<String, Integer> datos = new LinkedHashMap<>();

        try {
            String sql = "SELECT u.nombre AS tecnico, COUNT(t.id_ticket) AS total " +
                    "FROM usuarios u " +
                    "INNER JOIN asignaciones a ON u.id_usuario = a.id_tecnico " +
                    "INNER JOIN tickets t ON a.id_ticket = t.id_ticket " +
                    "WHERE t.fecha_creacion BETWEEN ? AND ? " +
                    "GROUP BY u.id_usuario, u.nombre " +
                    "ORDER BY total DESC";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setTimestamp(1, inicioDelDia(fechaInicio));
            ps.setTimestamp(2, finDelDia(fechaFin));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                datos.put(rs.getString("tecnico"), rs.getInt("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datos;
    }

    public Map<String, Integer> contarTicketsCerradosPorTecnico(LocalDate fechaInicio, LocalDate fechaFin) {
        Map<String, Integer> datos = new LinkedHashMap<>();

        try {
            String sql = "SELECT u.nombre AS tecnico, COUNT(t.id_ticket) AS total " +
                    "FROM usuarios u " +
                    "INNER JOIN asignaciones a ON u.id_usuario = a.id_tecnico " +
                    "INNER JOIN tickets t ON a.id_ticket = t.id_ticket " +
                    "WHERE t.estado = 'CERRADO' " +
                    "AND t.fecha_creacion BETWEEN ? AND ? " +
                    "GROUP BY u.id_usuario, u.nombre " +
                    "ORDER BY total DESC";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setTimestamp(1, inicioDelDia(fechaInicio));
            ps.setTimestamp(2, finDelDia(fechaFin));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                datos.put(rs.getString("tecnico"), rs.getInt("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datos;
    }

    public double obtenerTiempoPromedioResolucionHoras(LocalDate fechaInicio, LocalDate fechaFin) {
        long totalHoras = 0;
        int totalTickets = 0;

        try {
            String sql = "SELECT t.fecha_creacion, MAX(s.fecha) AS fecha_resolucion " +
                    "FROM tickets t " +
                    "INNER JOIN seguimiento s ON t.id_ticket = s.id_ticket " +
                    "WHERE t.estado = 'CERRADO' " +
                    "AND t.fecha_creacion BETWEEN ? AND ? " +
                    "GROUP BY t.id_ticket, t.fecha_creacion";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setTimestamp(1, inicioDelDia(fechaInicio));
            ps.setTimestamp(2, finDelDia(fechaFin));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDateTime fechaCreacion =
                        rs.getTimestamp("fecha_creacion").toLocalDateTime();

                LocalDateTime fechaResolucion =
                        rs.getTimestamp("fecha_resolucion").toLocalDateTime();

                totalHoras += Duration.between(fechaCreacion, fechaResolucion).toHours();
                totalTickets++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (totalTickets == 0) {
            return 0;
        }

        return (double) totalHoras / totalTickets;
    }

    public Map<String, Double> obtenerTiempoPromedioResolucionPorTecnico(LocalDate fechaInicio, LocalDate fechaFin) {
        Map<String, Long> sumaHoras = new LinkedHashMap<>();
        Map<String, Integer> conteoTickets = new LinkedHashMap<>();
        Map<String, Double> resultado = new LinkedHashMap<>();

        try {
            String sql = "SELECT u.nombre AS tecnico, t.fecha_creacion, MAX(s.fecha) AS fecha_resolucion " +
                    "FROM usuarios u " +
                    "INNER JOIN asignaciones a ON u.id_usuario = a.id_tecnico " +
                    "INNER JOIN tickets t ON a.id_ticket = t.id_ticket " +
                    "INNER JOIN seguimiento s ON t.id_ticket = s.id_ticket " +
                    "WHERE t.estado = 'CERRADO' " +
                    "AND t.fecha_creacion BETWEEN ? AND ? " +
                    "GROUP BY u.id_usuario, u.nombre, t.id_ticket, t.fecha_creacion";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setTimestamp(1, inicioDelDia(fechaInicio));
            ps.setTimestamp(2, finDelDia(fechaFin));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String tecnico = rs.getString("tecnico");

                LocalDateTime fechaCreacion =
                        rs.getTimestamp("fecha_creacion").toLocalDateTime();

                LocalDateTime fechaResolucion =
                        rs.getTimestamp("fecha_resolucion").toLocalDateTime();

                long horas = Duration.between(fechaCreacion, fechaResolucion).toHours();

                sumaHoras.put(tecnico, sumaHoras.getOrDefault(tecnico, 0L) + horas);
                conteoTickets.put(tecnico, conteoTickets.getOrDefault(tecnico, 0) + 1);
            }

            for (String tecnico : sumaHoras.keySet()) {
                resultado.put(
                        tecnico,
                        (double) sumaHoras.get(tecnico) / conteoTickets.get(tecnico)
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public int contarSeguimientosPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        int total = 0;

        try {
            String sql = "SELECT COUNT(*) AS total FROM seguimiento " +
                    "WHERE fecha BETWEEN ? AND ?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setTimestamp(1, inicioDelDia(fechaInicio));
            ps.setTimestamp(2, finDelDia(fechaFin));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    private Timestamp inicioDelDia(LocalDate fecha) {
        return Timestamp.valueOf(fecha.atStartOfDay());
    }

    private Timestamp finDelDia(LocalDate fecha) {
        return Timestamp.valueOf(fecha.atTime(23, 59, 59));
    }
}