package view;

import dao.ReporteDAO;
import dao.SeguimientoDAO;
import dao.UsuarioDAO;
import model.Seguimiento;
import model.Usuario;
import dao.TicketDAO;
import model.Ticket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class VentanaAdmin {
    private JTabbedPane tabbedPane1;
    public JPanel panelAdmin;
    private JTextField textField1;
    private JTextField textField2;
    private JPasswordField passwordField1;
    private JButton btnRegistrarUsuario;
    private JButton btnListarUsuario;
    private JButton btnActualizarUsuario;
    private JButton btnEliminarUsuario;
    private JTabbedPane tabbedPane2;
    private JList lstUsuarios;
    private JTextField txtIdModificar;
    private JTextField txtNombreModificar;
    private JTextField txtCorreoModificar;
    private JTextField txtContrasenaUsuario;
    private JButton buscarPorIDButton;
    private JComboBox cmbRolUsuario;
    private JComboBox cmbRolUsuarioMod;
    private JPanel panelTicketsAdmin;
    private JList lstTicketsAdmin;
    private JTextField txtIdTicketAdmin;
    private JButton btnBuscarTicketAdmin;
    private JComboBox cmbEstadoTicketAdmin;
    private JComboBox cmbPrioridadTicketAdmin;
    private JButton btnActualizarTicketAdmin;
    private JButton btnListarTicketsAdmin;
    private JButton btnEliminarTicketAdmin;
    private JPanel panelSeguimientoAdmin;
    private JList lstSeguimientoAdmin;
    private JTextField txtIdSeguimientoAdmin;
    private JTextArea txtComentarioSeguimientoAdmin;
    private JTextField txtIdTicketSeguimientoAdmin;
    private JTextField txtIdUsuarioSeguimientoAdmin;
    private JButton btnListarSeguimientoAdmin;
    private JButton btnBuscarSeguimientoAdmin;
    private JButton btnCrearSeguimientoAdmin;
    private JButton btnActualizarSeguimientoAdmin;
    private JButton btnEliminarSeguimientoAdmin;
    private JPanel panelReportesAdmin;
    private JTextField txtFechaInicioReporte;
    private JTextField txtFechaFinReporte;
    private JButton btnGenerarReporte;
    private JLabel lblTotalTicketsReporte;
    private JLabel lblAbiertosReporte;
    private JLabel lblCerradosReporte;
    private JLabel lblSeguimientosReporte;
    private JLabel lblPromedioResolucionReporte;
    private JTable tblReporteTecnicos;
    private JTable tblReporteResumen;
    private Ticket ticketActual;
    private boolean editandoTicket = false;
    private Seguimiento seguimientoActual;

    private Usuario usuarioActual;

    private boolean editando = false;

    public VentanaAdmin(Usuario admin) {
        // COMBOBOX ROLES
        cmbRolUsuario.addItem("ADMIN");
        cmbRolUsuario.addItem("TECNICO");
        cmbRolUsuario.addItem("CLIENTE");

        cmbRolUsuarioMod.addItem("ADMIN");
        cmbRolUsuarioMod.addItem("TECNICO");
        cmbRolUsuarioMod.addItem("CLIENTE");

        bloquearCamposModificar();

        // Agregar pestaña de Asignaciones
        VentanaAsignacion ventanaAsignacion = new VentanaAsignacion();
        tabbedPane1.addTab("Asignaciones", ventanaAsignacion.panelAsignacion);

        btnRegistrarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
        btnListarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarUsuarios();
            }
        });
        buscarPorIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarUsuario();
            }
        });
        btnActualizarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarUsuario();
            }
        });
        btnEliminarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarUsuario();
            }
        });
        // INICIALIZACIÓN TICKETS
        inicializarComboBoxesTickets();
        bloquearCamposTicket();

        // EVENTOS TICKETS
        btnListarTicketsAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarTicketsAdmin();
            }
        });

        btnBuscarTicketAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarTicketAdmin();
            }
        });

        btnActualizarTicketAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTicketAdmin();
            }
        });

        btnEliminarTicketAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarTicketAdmin();
            }
        });

        // EVENTO: AL HACER CLIC EN UN TICKET DE LA LISTA
        lstTicketsAdmin.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Verificar que la selección haya terminado y no sea nula
                if (!e.getValueIsAdjusting() && lstTicketsAdmin.getSelectedValue() != null) {
                    String ticketSeleccionado = lstTicketsAdmin.getSelectedValue().toString();

                    try {
                        // El texto tiene el formato "ID: 5 | ID Usuario: 2 | ..."
                        // Separamos por el símbolo "|" y tomamos la primera parte ("ID: 5 ")
                        String primeraParte = ticketSeleccionado.split("\\|")[0];

                        // Quitamos la palabra "ID:" y los espacios para quedarnos solo con el número
                        String idExtraido = primeraParte.replace("ID:", "").trim();

                        // Lo ponemos en el campo de texto
                        txtIdTicketAdmin.setText(idExtraido);

                    } catch (Exception ex) {
                        System.out.println("No se pudo extraer el ID del texto seleccionado.");
                    }
                }
            }
        });

        cargarSeguimientosAdmin();

        btnListarSeguimientoAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarSeguimientosAdmin();
            }
        });

        btnBuscarSeguimientoAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarSeguimientoAdmin();
            }
        });

        btnCrearSeguimientoAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearSeguimientoAdmin();
            }
        });

        btnActualizarSeguimientoAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarSeguimientoAdmin();
            }
        });

        btnEliminarSeguimientoAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarSeguimientoAdmin();
            }
        });

        lstSeguimientoAdmin.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && lstSeguimientoAdmin.getSelectedValue() != null) {
                    seguimientoActual = (Seguimiento) lstSeguimientoAdmin.getSelectedValue();
                    mostrarSeguimientoAdmin();
                }
            }
        });

        btnGenerarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporteAdmin();
            }
        });
    }

    // REGISTRAR
    private void registrarUsuario() {

        Usuario usuario = new Usuario(

                0,

                textField1.getText(),

                textField2.getText(),

                String.valueOf(passwordField1.getPassword()),

                cmbRolUsuario.getSelectedItem().toString()
        );

        UsuarioDAO dao = new UsuarioDAO();

        dao.insertar(usuario);

        JOptionPane.showMessageDialog(null, "Usuario registrado correctamente");
    }

    // LISTAR
    private void listarUsuarios() {

        UsuarioDAO dao = new UsuarioDAO();

        ArrayList<Usuario> lista = dao.obtenerTodos();

        DefaultListModel modelo = new DefaultListModel();

        for (Usuario u : lista) {

            modelo.addElement(u.toString());
        }

        lstUsuarios.setModel(modelo);
    }

    // BUSCAR
    private void buscarUsuario() {

        int id = Integer.parseInt(txtIdModificar.getText());

        UsuarioDAO dao = new UsuarioDAO();

        usuarioActual = dao.buscarPorId(id);

        if (usuarioActual != null) {

            txtNombreModificar.setText(usuarioActual.getNombre());

            txtCorreoModificar.setText(usuarioActual.getCorreo());

            txtContrasenaUsuario.setText(usuarioActual.getPassword());

            cmbRolUsuarioMod.setSelectedItem(usuarioActual.getRol());

            bloquearCamposModificar();

            JOptionPane.showMessageDialog(null, "Usuario encontrado");
        } else {

            JOptionPane.showMessageDialog(null, "Usuario no encontrado");
        }
    }

    // ACTUALIZAR
    private void actualizarUsuario() {

        // PRIMER CLICK
        if (!editando) {

            int opcion = JOptionPane.showConfirmDialog(null, "¿Deseas modificar este usuario?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {

                desbloquearCamposModificar();

                editando = true;

                btnActualizarUsuario.setText("Guardar Cambios");
            }
        }

        // SEGUNDO CLICK
        else {

            usuarioActual.setNombre(txtNombreModificar.getText());

            usuarioActual.setCorreo(txtCorreoModificar.getText());

            usuarioActual.setPassword(txtContrasenaUsuario.getText());

            usuarioActual.setRol(cmbRolUsuarioMod.getSelectedItem().toString());

            UsuarioDAO dao = new UsuarioDAO();

            dao.actualizarAdmin(usuarioActual);

            bloquearCamposModificar();

            editando = false;

            btnActualizarUsuario.setText("Actualizar Usuario");

            JOptionPane.showMessageDialog(null, "Usuario actualizado");
        }
    }

    // ELIMINAR
    private void eliminarUsuario() {

        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro deseas eliminar este usuario?", "Confirmación", JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {

            int id = Integer.parseInt(txtIdModificar.getText());

            UsuarioDAO dao = new UsuarioDAO();

            dao.eliminar(id);

            JOptionPane.showMessageDialog(null, "Usuario eliminado");
        }
    }

    // BLOQUEAR
    private void bloquearCamposModificar() {

        txtNombreModificar.setEditable(false);

        txtCorreoModificar.setEditable(false);

        txtContrasenaUsuario.setEditable(false);

        cmbRolUsuarioMod.setEnabled(false);
    }

    // DESBLOQUEAR
    private void desbloquearCamposModificar() {

        txtNombreModificar.setEditable(true);

        txtCorreoModificar.setEditable(true);

        txtContrasenaUsuario.setEditable(true);

        cmbRolUsuarioMod.setEnabled(true);
    }
    // ------------------------------------------
    // LÓGICA DE TICKETS
    // ------------------------------------------

    // INICIALIZAR COMBOBOXES DE TICKET
    private void inicializarComboBoxesTickets() {
        cmbEstadoTicketAdmin.addItem("ABIERTO");
        cmbEstadoTicketAdmin.addItem("CERRADO");

        cmbPrioridadTicketAdmin.addItem("BAJA");
        cmbPrioridadTicketAdmin.addItem("MEDIA");
        cmbPrioridadTicketAdmin.addItem("ALTA");
        cmbPrioridadTicketAdmin.addItem("CRITICA");
    }

    // BLOQUEAR/DESBLOQUEAR
    private void bloquearCamposTicket() {
        cmbEstadoTicketAdmin.setEnabled(false);
        cmbPrioridadTicketAdmin.setEnabled(false);
    }


    // LISTAR TICKETS
    private void listarTicketsAdmin() {
        TicketDAO dao = new TicketDAO();
        ArrayList<Ticket> lista = dao.obtenerTickets();
        DefaultListModel modelo = new DefaultListModel();

        for (Ticket t : lista) {
            // Personalizamos el texto para que incluya el ID del Usuario (t.getIdUsuario())
            String infoTicket = "ID: " + t.getIdTicket() +
                    " | ID Usuario: " + t.getIdUsuario() +
                    " | " + t.getTitulo() +
                    " | Prioridad: " + t.getPrioridad() +
                    " | Estado: " + t.getEstado();

            modelo.addElement(infoTicket);
        }

        lstTicketsAdmin.setModel(modelo);
    }

    // BUSCAR TICKET
    private void buscarTicketAdmin() {
        try {
            int id = Integer.parseInt(txtIdTicketAdmin.getText());
            TicketDAO dao = new TicketDAO();

            // Como TicketDAO no tiene buscarPorId, filtramos la lista completa
            ArrayList<Ticket> todos = dao.obtenerTickets();
            ticketActual = null;

            for (Ticket t : todos) {
                if (t.getIdTicket() == id) {
                    ticketActual = t;
                    break;
                }
            }

            if (ticketActual != null) {
                cmbEstadoTicketAdmin.setSelectedItem(ticketActual.getEstado());
                cmbPrioridadTicketAdmin.setSelectedItem(ticketActual.getPrioridad());

                bloquearCamposTicket();
                JOptionPane.showMessageDialog(null, "Ticket encontrado");
            } else {
                JOptionPane.showMessageDialog(null, "Ticket no encontrado");
                ticketActual = null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido (número).");
        }
    }

    // DESBLOQUEAR
    private void desbloquearCamposTicket() {
        cmbEstadoTicketAdmin.setEnabled(true);
        cmbPrioridadTicketAdmin.setEnabled(true); // <-- Ahora también se habilita la prioridad
    }

    // ACTUALIZAR TICKET (ESTADO Y PRIORIDAD)
    private void actualizarTicketAdmin() {
        if (ticketActual == null) {
            JOptionPane.showMessageDialog(null, "Primero busque un ticket por ID.");
            return;
        }

        // PRIMER CLICK
        if (!editandoTicket) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Deseas modificar el estado y la prioridad de este ticket?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                desbloquearCamposTicket();
                editandoTicket = true;
                btnActualizarTicketAdmin.setText("Guardar Cambios");
            }
        }
        // SEGUNDO CLICK
        else {
            String nuevoEstado = cmbEstadoTicketAdmin.getSelectedItem().toString();
            String nuevaPrioridad = cmbPrioridadTicketAdmin.getSelectedItem().toString(); // <-- Obtenemos la prioridad
            TicketDAO dao = new TicketDAO();

            // Usamos el nuevo método que creamos en TicketDAO
            dao.cambiarEstadoYPrioridad(ticketActual.getIdTicket(), nuevoEstado, nuevaPrioridad);

            bloquearCamposTicket();
            editandoTicket = false;
            btnActualizarTicketAdmin.setText("Actualizar Ticket");

            JOptionPane.showMessageDialog(null, "Ticket actualizado exitosamente");
            listarTicketsAdmin(); // Refresca la lista
        }
    }

    // ELIMINAR TICKET
    private void eliminarTicketAdmin() {
        try {
            int id = Integer.parseInt(txtIdTicketAdmin.getText());
            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro deseas eliminar este ticket permanentemente?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                TicketDAO dao = new TicketDAO();
                dao.eliminarTicket(id);

                JOptionPane.showMessageDialog(null, "Ticket eliminado");
                listarTicketsAdmin(); // Refrescar lista
                txtIdTicketAdmin.setText("");
                ticketActual = null;
                bloquearCamposTicket();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido (número).");
        }
    }

    private void cargarSeguimientosAdmin() {
        SeguimientoDAO dao = new SeguimientoDAO();
        ArrayList<Seguimiento> lista = dao.obtenerTodos();
        DefaultListModel modelo = new DefaultListModel();

        for (Seguimiento s : lista) {
            modelo.addElement(s);
        }

        lstSeguimientoAdmin.setModel(modelo);
    }

    private void mostrarSeguimientoAdmin() {
        txtIdSeguimientoAdmin.setText(String.valueOf(seguimientoActual.getIdSeguimiento()));
        txtComentarioSeguimientoAdmin.setText(seguimientoActual.getComentario());
        txtIdTicketSeguimientoAdmin.setText(String.valueOf(seguimientoActual.getIdTicket()));
        txtIdUsuarioSeguimientoAdmin.setText(String.valueOf(seguimientoActual.getIdUsuario()));
    }

    private void buscarSeguimientoAdmin() {
        try {
            int idSeguimiento = Integer.parseInt(txtIdSeguimientoAdmin.getText().trim());
            SeguimientoDAO dao = new SeguimientoDAO();
            seguimientoActual = dao.buscarPorId(idSeguimiento);

            if (seguimientoActual != null) {
                mostrarSeguimientoAdmin();
                JOptionPane.showMessageDialog(null, "Seguimiento encontrado");
            } else {
                JOptionPane.showMessageDialog(null, "Seguimiento no encontrado");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ingresa un ID de seguimiento válido.");
        }
    }

    private void crearSeguimientoAdmin() {
        try {
            String comentario = txtComentarioSeguimientoAdmin.getText().trim();

            if (comentario.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingresa un comentario.");
                return;
            }

            int idTicket = Integer.parseInt(txtIdTicketSeguimientoAdmin.getText().trim());
            int idUsuario = Integer.parseInt(txtIdUsuarioSeguimientoAdmin.getText().trim());

            SeguimientoDAO dao = new SeguimientoDAO();
            boolean guardado = dao.registrarSeguimiento(comentario, LocalDateTime.now(), idTicket, idUsuario);

            if (guardado) {
                JOptionPane.showMessageDialog(null, "Seguimiento creado correctamente.");
                limpiarSeguimientoAdmin();
                cargarSeguimientosAdmin();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo crear el seguimiento.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "ID Ticket e ID Usuario deben ser numeros.");
        }
    }

    private void actualizarSeguimientoAdmin() {
        try {
            if (txtIdSeguimientoAdmin.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Busca o selecciona un seguimiento primero.");
                return;
            }

            int idSeguimiento = Integer.parseInt(txtIdSeguimientoAdmin.getText().trim());
            String comentario = txtComentarioSeguimientoAdmin.getText().trim();

            if (comentario.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingresa un comentario.");
                return;
            }

            int idTicket = Integer.parseInt(txtIdTicketSeguimientoAdmin.getText().trim());
            int idUsuario = Integer.parseInt(txtIdUsuarioSeguimientoAdmin.getText().trim());

            Seguimiento seguimiento = new Seguimiento(idSeguimiento, comentario, LocalDateTime.now(), idTicket, idUsuario);
            SeguimientoDAO dao = new SeguimientoDAO();
            dao.actualizarSeguimiento(seguimiento);

            JOptionPane.showMessageDialog(null, "Seguimiento actualizado correctamente.");
            cargarSeguimientosAdmin();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Revisa los IDs numericos.");
        }
    }

    private void eliminarSeguimientoAdmin() {
        try {
            int idSeguimiento = Integer.parseInt(txtIdSeguimientoAdmin.getText().trim());
            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro deseas eliminar este seguimiento?", "ConfirmaciÃ³n", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                SeguimientoDAO dao = new SeguimientoDAO();
                dao.eliminarSeguimiento(idSeguimiento);

                JOptionPane.showMessageDialog(null, "Seguimiento eliminado.");
                limpiarSeguimientoAdmin();
                cargarSeguimientosAdmin();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ingresa un ID de seguimiento válido.");
        }
    }

    private void limpiarSeguimientoAdmin() {
        seguimientoActual = null;
        txtIdSeguimientoAdmin.setText("");
        txtComentarioSeguimientoAdmin.setText("");
        txtIdTicketSeguimientoAdmin.setText("");
        txtIdUsuarioSeguimientoAdmin.setText("");
    }

    private void generarReporteAdmin() {
        try {
            LocalDate fechaInicio = LocalDate.parse(txtFechaInicioReporte.getText().trim());
            LocalDate fechaFin = LocalDate.parse(txtFechaFinReporte.getText().trim());

            if (fechaFin.isBefore(fechaInicio)) {
                JOptionPane.showMessageDialog(null, "La fecha fin no puede ser anterior a la fecha inicio.");
                return;
            }

            ReporteDAO reporteDAO = new ReporteDAO();

            int totalTickets = reporteDAO.contarTicketsPorRango(fechaInicio, fechaFin);
            int abiertos = reporteDAO.contarTicketsPorEstado("ABIERTO", fechaInicio, fechaFin);
            int cerrados = reporteDAO.contarTicketsPorEstado("CERRADO", fechaInicio, fechaFin);
            int seguimientos = reporteDAO.contarSeguimientosPorRango(fechaInicio, fechaFin);
            double promedioHoras = reporteDAO.obtenerTiempoPromedioResolucionHoras(fechaInicio, fechaFin);

            lblTotalTicketsReporte.setText("Total tickets: " + totalTickets);
            lblAbiertosReporte.setText("Abiertos: " + abiertos);
            lblCerradosReporte.setText("Cerrados: " + cerrados);
            lblSeguimientosReporte.setText("Seguimientos: " + seguimientos);
            lblPromedioResolucionReporte.setText("Promedio resolución: " + String.format("%.2f", promedioHoras) + " horas");

            cargarTablaReporteTecnicos(reporteDAO, fechaInicio, fechaFin);
            cargarTablaReporteResumen(reporteDAO, fechaInicio, fechaFin);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Usa fechas vÃ¡lidas con formato yyyy-MM-dd.");
        }
    }

    private void cargarTablaReporteTecnicos(ReporteDAO reporteDAO, LocalDate fechaInicio, LocalDate fechaFin) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Técnico");
        modelo.addColumn("Asignados");
        modelo.addColumn("Cerrados");
        modelo.addColumn("Promedio horas");

        Map<String, Integer> asignados = reporteDAO.contarTicketsPorTecnico(fechaInicio, fechaFin);
        Map<String, Integer> cerrados = reporteDAO.contarTicketsCerradosPorTecnico(fechaInicio, fechaFin);
        Map<String, Double> promedio = reporteDAO.obtenerTiempoPromedioResolucionPorTecnico(fechaInicio, fechaFin);

        for (String tecnico : asignados.keySet()) {
            Object[] fila = new Object[]{
                    tecnico,
                    asignados.get(tecnico),
                    cerrados.getOrDefault(tecnico, 0),
                    String.format("%.2f", promedio.getOrDefault(tecnico, 0.0))
            };
            modelo.addRow(fila);
        }

        tblReporteTecnicos.setModel(modelo);
    }

    private void cargarTablaReporteResumen(ReporteDAO reporteDAO, LocalDate fechaInicio, LocalDate fechaFin) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Tipo");
        modelo.addColumn("Valor");
        modelo.addColumn("Cantidad");

        Map<String, Integer> porPrioridad = reporteDAO.contarTicketsPorPrioridad(fechaInicio, fechaFin);
        for (String prioridad : porPrioridad.keySet()) {
            modelo.addRow(new Object[]{"Prioridad", prioridad, porPrioridad.get(prioridad)});
        }

        Map<String, Integer> porCategoria = reporteDAO.contarTicketsPorCategoria(fechaInicio, fechaFin);
        for (String categoria : porCategoria.keySet()) {
            modelo.addRow(new Object[]{"Categoría", categoria, porCategoria.get(categoria)});
        }

        tblReporteResumen.setModel(modelo);
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelAdmin = new JPanel();
        panelAdmin.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        panelAdmin.add(tabbedPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Usuarios", panel1);
        final JLabel label1 = new JLabel();
        label1.setText("Nombre Usuario");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        panel1.add(textField1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Correo Usuario");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField2 = new JTextField();
        panel1.add(textField2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Contraseña Usuario");
        panel1.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField1 = new JPasswordField();
        panel1.add(passwordField1, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnRegistrarUsuario = new JButton();
        btnRegistrarUsuario.setText("Registrar Usuario");
        panel1.add(btnRegistrarUsuario, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnListarUsuario = new JButton();
        btnListarUsuario.setText("Listar Usuarios");
        panel1.add(btnListarUsuario, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lstUsuarios = new JList();
        panel1.add(lstUsuarios, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Rol Usuario");
        panel1.add(label4, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbRolUsuario = new JComboBox();
        panel1.add(cmbRolUsuario, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tabbedPane2 = new JTabbedPane();
        tabbedPane1.addTab("Modificar Usuarios", tabbedPane2);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("Modificar Usuarios", panel2);
        btnActualizarUsuario = new JButton();
        btnActualizarUsuario.setText("Actualizar Usuario");
        panel2.add(btnActualizarUsuario, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEliminarUsuario = new JButton();
        btnEliminarUsuario.setText("EliminarUsuario");
        panel2.add(btnEliminarUsuario, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Ingrese el ID");
        panel2.add(label5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtIdModificar = new JTextField();
        txtIdModificar.setText("");
        panel2.add(txtIdModificar, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Nombre Usuario");
        panel2.add(label6, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Correo Usuario");
        panel2.add(label7, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Rol Usuario");
        panel2.add(label8, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Contraseña Usuario");
        panel2.add(label9, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNombreModificar = new JTextField();
        panel2.add(txtNombreModificar, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtCorreoModificar = new JTextField();
        panel2.add(txtCorreoModificar, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtContrasenaUsuario = new JTextField();
        panel2.add(txtContrasenaUsuario, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buscarPorIDButton = new JButton();
        buscarPorIDButton.setText("Buscar por ID");
        panel2.add(buscarPorIDButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbRolUsuarioMod = new JComboBox();
        panel2.add(cmbRolUsuarioMod, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelTicketsAdmin = new JPanel();
        panelTicketsAdmin.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 3, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1.addTab("Tickets", panelTicketsAdmin);
        final JScrollPane scrollPane1 = new JScrollPane();
        panelTicketsAdmin.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 5, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(220, 150), null, 0, false));
        lstTicketsAdmin = new JList();
        scrollPane1.setViewportView(lstTicketsAdmin);
        final JLabel label10 = new JLabel();
        label10.setText("ID Ticket:");
        panelTicketsAdmin.add(label10, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtIdTicketAdmin = new JTextField();
        panelTicketsAdmin.add(txtIdTicketAdmin, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnBuscarTicketAdmin = new JButton();
        btnBuscarTicketAdmin.setText("Buscar por ID");
        panelTicketsAdmin.add(btnBuscarTicketAdmin, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Estado:");
        panelTicketsAdmin.add(label11, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbEstadoTicketAdmin = new JComboBox();
        panelTicketsAdmin.add(cmbEstadoTicketAdmin, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Prioridad:");
        panelTicketsAdmin.add(label12, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbPrioridadTicketAdmin = new JComboBox();
        panelTicketsAdmin.add(cmbPrioridadTicketAdmin, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnActualizarTicketAdmin = new JButton();
        btnActualizarTicketAdmin.setText("Actualizar Ticket");
        panelTicketsAdmin.add(btnActualizarTicketAdmin, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnListarTicketsAdmin = new JButton();
        btnListarTicketsAdmin.setText("Listar Todos");
        panelTicketsAdmin.add(btnListarTicketsAdmin, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEliminarTicketAdmin = new JButton();
        btnEliminarTicketAdmin.setText("Eliminar Ticket");
        panelTicketsAdmin.add(btnEliminarTicketAdmin, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelSeguimientoAdmin = new JPanel();
        panelSeguimientoAdmin.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 3, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1.addTab("Seguimiento", panelSeguimientoAdmin);
        final JScrollPane scrollPane2 = new JScrollPane();
        panelSeguimientoAdmin.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 7, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(260, 150), null, 0, false));
        lstSeguimientoAdmin = new JList();
        scrollPane2.setViewportView(lstSeguimientoAdmin);
        final JLabel label13 = new JLabel();
        label13.setText("ID Seguimiento:");
        panelSeguimientoAdmin.add(label13, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtIdSeguimientoAdmin = new JTextField();
        panelSeguimientoAdmin.add(txtIdSeguimientoAdmin, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("ID Ticket:");
        panelSeguimientoAdmin.add(label14, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtIdTicketSeguimientoAdmin = new JTextField();
        panelSeguimientoAdmin.add(txtIdTicketSeguimientoAdmin, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("ID Usuario:");
        panelSeguimientoAdmin.add(label15, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtIdUsuarioSeguimientoAdmin = new JTextField();
        panelSeguimientoAdmin.add(txtIdUsuarioSeguimientoAdmin, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("Comentario:");
        panelSeguimientoAdmin.add(label16, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane3 = new JScrollPane();
        panelSeguimientoAdmin.add(scrollPane3, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 80), null, 0, false));
        txtComentarioSeguimientoAdmin = new JTextArea();
        scrollPane3.setViewportView(txtComentarioSeguimientoAdmin);
        btnBuscarSeguimientoAdmin = new JButton();
        btnBuscarSeguimientoAdmin.setText("Buscar por ID");
        panelSeguimientoAdmin.add(btnBuscarSeguimientoAdmin, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCrearSeguimientoAdmin = new JButton();
        btnCrearSeguimientoAdmin.setText("Crear");
        panelSeguimientoAdmin.add(btnCrearSeguimientoAdmin, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnActualizarSeguimientoAdmin = new JButton();
        btnActualizarSeguimientoAdmin.setText("Actualizar");
        panelSeguimientoAdmin.add(btnActualizarSeguimientoAdmin, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnListarSeguimientoAdmin = new JButton();
        btnListarSeguimientoAdmin.setText("Listar Seguimiento");
        panelSeguimientoAdmin.add(btnListarSeguimientoAdmin, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnEliminarSeguimientoAdmin = new JButton();
        btnEliminarSeguimientoAdmin.setText("Eliminar");
        panelSeguimientoAdmin.add(btnEliminarSeguimientoAdmin, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelReportesAdmin = new JPanel();
        panelReportesAdmin.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 4, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1.addTab("Reportes", panelReportesAdmin);
        final JLabel label17 = new JLabel();
        label17.setText("Fecha inicio:");
        panelReportesAdmin.add(label17, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFechaInicioReporte = new JTextField();
        txtFechaInicioReporte.setText("2026-01-01");
        panelReportesAdmin.add(txtFechaInicioReporte, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(120, -1), null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("Fecha fin:");
        panelReportesAdmin.add(label18, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtFechaFinReporte = new JTextField();
        txtFechaFinReporte.setText("2026-12-31");
        panelReportesAdmin.add(txtFechaFinReporte, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(120, -1), null, 0, false));
        btnGenerarReporte = new JButton();
        btnGenerarReporte.setText("Generar Reporte");
        panelReportesAdmin.add(btnGenerarReporte, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTotalTicketsReporte = new JLabel();
        lblTotalTicketsReporte.setText("Total tickets: 0");
        panelReportesAdmin.add(lblTotalTicketsReporte, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblAbiertosReporte = new JLabel();
        lblAbiertosReporte.setText("Abiertos: 0");
        panelReportesAdmin.add(lblAbiertosReporte, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblCerradosReporte = new JLabel();
        lblCerradosReporte.setText("Cerrados: 0");
        panelReportesAdmin.add(lblCerradosReporte, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblSeguimientosReporte = new JLabel();
        lblSeguimientosReporte.setText("Seguimientos: 0");
        panelReportesAdmin.add(lblSeguimientosReporte, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblPromedioResolucionReporte = new JLabel();
        lblPromedioResolucionReporte.setText("Promedio resolución: 0 horas");
        panelReportesAdmin.add(lblPromedioResolucionReporte, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        label19.setText("Reporte por técnico");
        panelReportesAdmin.add(label19, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane4 = new JScrollPane();
        panelReportesAdmin.add(scrollPane4, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(400, 120), null, 0, false));
        tblReporteTecnicos = new JTable();
        scrollPane4.setViewportView(tblReporteTecnicos);
        final JLabel label20 = new JLabel();
        label20.setText("Resumen por prioridad y categorÃ\u00ADa");
        panelReportesAdmin.add(label20, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane5 = new JScrollPane();
        panelReportesAdmin.add(scrollPane5, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(400, 120), null, 0, false));
        tblReporteResumen = new JTable();
        scrollPane5.setViewportView(tblReporteResumen);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelAdmin;
    }

}
