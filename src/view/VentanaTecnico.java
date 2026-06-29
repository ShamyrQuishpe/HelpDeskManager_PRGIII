package view;

import dao.SeguimientoDAO;
import dao.TicketDAO;
import dao.UsuarioDAO;
import model.Seguimiento;
import model.Ticket;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class VentanaTecnico {

    public JPanel panelTecnico;
    private JTabbedPane tabbedPane1;
    private JLabel lblNombre;
    private JTextField txtNombreTecnico;
    private JTextField txtCorreoTecnico;
    private JPasswordField txtContrasenaTecnico;
    private JButton btnActualizarTecnico;
    private JPanel panelSolventarTickets;
    private JList lstTicketsAsignados;
    private JTextField txtTituloTicketTecnico;
    private JTextField txtDescripcionTicketTecnico;
    private JTextField txtCategoriaTicketTecnico;
    private JTextField txtPrioridadTicketTecnico;
    private JTextField txtEstadoTicketTecnico;
    private JTextArea txtComentarioTecnico;
    private JButton btnActualizarTicketsTecnico;
    private JButton btnCerrarTicket;
    private JPanel panelSeguimientoTecnico;
    private JList lstSeguimientoTecnico;
    private JTextField txtIdTicketSeguimientoTecnico;
    private JButton btnBuscarSeguimientoTecnico;
    private JButton btnListarSeguimientoTecnico;
    private Usuario usuario;
    private TicketDAO ticketDAO;
    private SeguimientoDAO seguimientoDAO;
    private Ticket ticketSeleccionado;
    private boolean editando = false;

    // CONSTRUCTOR
    public VentanaTecnico(Usuario usuario) {

        this.usuario = usuario;

        // Cargar datos
        cargarDatos();

        // Bloquear campos inicialmente
        bloquearCampos();

        ticketDAO = new TicketDAO();
        seguimientoDAO = new SeguimientoDAO();
        bloquearCamposTicket();
        cargarTicketsAsignados();
        cargarSeguimientosTecnico();

        // BOTÓN ACTUALIZAR
        btnActualizarTecnico.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        actualizarDatos();
                    }
                });

        btnActualizarTicketsTecnico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarTicketsAsignados();
            }
        });

        btnCerrarTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarTicket();
            }
        });

        lstTicketsAsignados.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && lstTicketsAsignados.getSelectedValue() != null) {
                    ticketSeleccionado = (Ticket) lstTicketsAsignados.getSelectedValue();
                    mostrarTicketSeleccionado();
                }
            }
        });

        btnListarSeguimientoTecnico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarSeguimientosTecnico();
            }
        });

        btnBuscarSeguimientoTecnico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarSeguimientoTecnicoPorTicket();
            }
        });
    }

    // MOSTRAR DATOS
    private void cargarDatos() {

        lblNombre.setText("Bienvenid@ " +
                usuario.getNombre());

        txtNombreTecnico.setText(usuario.getNombre());

        txtCorreoTecnico.setText(usuario.getCorreo());

        txtContrasenaTecnico.setText(usuario.getPassword());
    }

    // BLOQUEAR CAMPOS
    private void bloquearCampos() {

        txtNombreTecnico.setEditable(false);

        txtCorreoTecnico.setEditable(false);

        txtContrasenaTecnico.setEditable(false);
    }

    // DESBLOQUEAR CAMPOS
    private void desbloquearCampos() {

        txtNombreTecnico.setEditable(true);

        txtCorreoTecnico.setEditable(true);

        txtContrasenaTecnico.setEditable(true);
    }

    // ACTUALIZAR DATOS
    private void actualizarDatos() {

        // PRIMER CLICK
        if (!editando) {

            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro deseas modificar tus datos?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {

                desbloquearCampos();

                editando = true;

                btnActualizarTecnico.setText("Guardar Cambios");
            }
        }

        // SEGUNDO CLICK
        else {
            usuario.setNombre(txtNombreTecnico.getText());

            usuario.setCorreo(txtCorreoTecnico.getText());

            usuario.setPassword(String.valueOf(txtContrasenaTecnico.getPassword()));

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            usuarioDAO.actualizar(usuario);

            lblNombre.setText("Bienvenid@ " + usuario.getNombre());

            bloquearCampos();

            editando = false;

            btnActualizarTecnico.setText("Actualizar Datos");

            JOptionPane.showMessageDialog(null, "Datos actualizados correctamente");
        }
    }

    private void bloquearCamposTicket() {
        txtTituloTicketTecnico.setEditable(false);
        txtDescripcionTicketTecnico.setEditable(false);
        txtCategoriaTicketTecnico.setEditable(false);
        txtPrioridadTicketTecnico.setEditable(false);
        txtEstadoTicketTecnico.setEditable(false);
    }

    private void cargarTicketsAsignados() {
        ArrayList<Ticket> listaTickets = ticketDAO.obtenerTicketsAsignadosTecnico(usuario.getIdUsuario());
        DefaultListModel modeloLista = new DefaultListModel();

        for (Ticket t : listaTickets) {
            modeloLista.addElement(t);
        }

        lstTicketsAsignados.setModel(modeloLista);
        limpiarDetalleTicket();
    }

    private void mostrarTicketSeleccionado() {
        txtTituloTicketTecnico.setText(ticketSeleccionado.getTitulo());
        txtDescripcionTicketTecnico.setText(ticketSeleccionado.getDescripcion());
        txtCategoriaTicketTecnico.setText(ticketSeleccionado.getCategoria());
        txtPrioridadTicketTecnico.setText(ticketSeleccionado.getPrioridad());
        txtEstadoTicketTecnico.setText(ticketSeleccionado.getEstado());
        txtComentarioTecnico.setText("");
    }

    private void limpiarDetalleTicket() {
        ticketSeleccionado = null;
        txtTituloTicketTecnico.setText("");
        txtDescripcionTicketTecnico.setText("");
        txtCategoriaTicketTecnico.setText("");
        txtPrioridadTicketTecnico.setText("");
        txtEstadoTicketTecnico.setText("");
        txtComentarioTecnico.setText("");
    }

    private void cerrarTicket() {
        if (ticketSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "Selecciona un ticket asignado.");
            return;
        }

        if ("CERRADO".equals(ticketSeleccionado.getEstado())) {
            JOptionPane.showMessageDialog(null, "Este ticket ya está¡ cerrado.");
            return;
        }

        String comentario = txtComentarioTecnico.getText().trim();

        if (comentario.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingresa un comentario para cerrar el ticket.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro deseas cerrar este ticket?", "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean seguimientoGuardado = seguimientoDAO.registrarSeguimiento(comentario, LocalDateTime.now(), ticketSeleccionado.getIdTicket(), usuario.getIdUsuario());

            if (!seguimientoGuardado) {
                JOptionPane.showMessageDialog(null, "No se pudo guardar el seguimiento. El ticket no fue cerrado.");
                return;
            }

            ticketDAO.cambiarEstado(ticketSeleccionado.getIdTicket(), "CERRADO");

            JOptionPane.showMessageDialog(null, "Ticket cerrado correctamente.");
            cargarTicketsAsignados();
            cargarSeguimientosTecnico();
        }
    }

    private void cargarSeguimientosTecnico() {
        ArrayList<Seguimiento> listaSeguimiento = seguimientoDAO.obtenerPorTecnico(usuario.getIdUsuario());
        DefaultListModel modeloLista = new DefaultListModel();

        for (Seguimiento s : listaSeguimiento) {
            modeloLista.addElement(s);
        }

        lstSeguimientoTecnico.setModel(modeloLista);
    }

    private void buscarSeguimientoTecnicoPorTicket() {
        try {
            if (txtIdTicketSeguimientoTecnico.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingresa un ID de ticket.");
                return;
            }

            int idTicket = Integer.parseInt(txtIdTicketSeguimientoTecnico.getText().trim());
            ArrayList<Ticket> ticketsTecnico = ticketDAO.obtenerTicketsAsignadosTecnico(usuario.getIdUsuario());
            boolean ticketAsignadoTecnico = false;

            for (Ticket t : ticketsTecnico) {
                if (t.getIdTicket() == idTicket) {
                    ticketAsignadoTecnico = true;
                    break;
                }
            }

            if (!ticketAsignadoTecnico) {
                JOptionPane.showMessageDialog(null, "Ese ticket no estÃ¡ asignado a tu usuario.");
                return;
            }

            ArrayList<Seguimiento> listaSeguimiento = seguimientoDAO.obtenerPorTicket(idTicket);
            DefaultListModel modeloLista = new DefaultListModel();

            for (Seguimiento s : listaSeguimiento) {
                modeloLista.addElement(s);
            }

            lstSeguimientoTecnico.setModel(modeloLista);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Ingresa un ID de ticket vÃ¡lido.");
        }
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
        panelTecnico = new JPanel();
        panelTecnico.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        panelTecnico.add(tabbedPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Mi perfil", panel1);
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Estos son tu datos: ");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Nombre: ");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Correo: ");
        panel1.add(label3, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Contraseña: ");
        panel1.add(label4, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNombreTecnico = new JTextField();
        panel1.add(txtNombreTecnico, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtCorreoTecnico = new JTextField();
        panel1.add(txtCorreoTecnico, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtContrasenaTecnico = new JPasswordField();
        panel1.add(txtContrasenaTecnico, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnActualizarTecnico = new JButton();
        btnActualizarTecnico.setText("Actualizar Datos");
        panel1.add(btnActualizarTecnico, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblNombre = new JLabel();
        lblNombre.setText("Label");
        panel1.add(lblNombre, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelSolventarTickets = new JPanel();
        panelSolventarTickets.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 3, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1.addTab("Solventar tickets", panelSolventarTickets);
        final JScrollPane scrollPane1 = new JScrollPane();
        panelSolventarTickets.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 7, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(220, 150), null, 0, false));
        lstTicketsAsignados = new JList();
        scrollPane1.setViewportView(lstTicketsAsignados);
        final JLabel label5 = new JLabel();
        label5.setText("Título:");
        panelSolventarTickets.add(label5, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtTituloTicketTecnico = new JTextField();
        panelSolventarTickets.add(txtTituloTicketTecnico, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Descripción:");
        panelSolventarTickets.add(label6, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtDescripcionTicketTecnico = new JTextField();
        panelSolventarTickets.add(txtDescripcionTicketTecnico, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Categoría:");
        panelSolventarTickets.add(label7, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtCategoriaTicketTecnico = new JTextField();
        panelSolventarTickets.add(txtCategoriaTicketTecnico, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Prioridad:");
        panelSolventarTickets.add(label8, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtPrioridadTicketTecnico = new JTextField();
        panelSolventarTickets.add(txtPrioridadTicketTecnico, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Estado:");
        panelSolventarTickets.add(label9, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtEstadoTicketTecnico = new JTextField();
        panelSolventarTickets.add(txtEstadoTicketTecnico, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Comentario:");
        panelSolventarTickets.add(label10, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panelSolventarTickets.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 80), null, 0, false));
        txtComentarioTecnico = new JTextArea();
        scrollPane2.setViewportView(txtComentarioTecnico);
        btnActualizarTicketsTecnico = new JButton();
        btnActualizarTicketsTecnico.setText("Actualizar Lista");
        panelSolventarTickets.add(btnActualizarTicketsTecnico, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCerrarTicket = new JButton();
        btnCerrarTicket.setText("Cerrar Ticket");
        panelSolventarTickets.add(btnCerrarTicket, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelSeguimientoTecnico = new JPanel();
        panelSeguimientoTecnico.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1.addTab("Seguimiento", panelSeguimientoTecnico);
        final JScrollPane scrollPane3 = new JScrollPane();
        panelSeguimientoTecnico.add(scrollPane3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(260, 150), null, 0, false));
        lstSeguimientoTecnico = new JList();
        scrollPane3.setViewportView(lstSeguimientoTecnico);
        final JLabel label11 = new JLabel();
        label11.setText("ID Ticket:");
        panelSeguimientoTecnico.add(label11, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtIdTicketSeguimientoTecnico = new JTextField();
        panelSeguimientoTecnico.add(txtIdTicketSeguimientoTecnico, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnBuscarSeguimientoTecnico = new JButton();
        btnBuscarSeguimientoTecnico.setText("Buscar por Ticket");
        panelSeguimientoTecnico.add(btnBuscarSeguimientoTecnico, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnListarSeguimientoTecnico = new JButton();
        btnListarSeguimientoTecnico.setText("Listar Seguimiento");
        panelSeguimientoTecnico.add(btnListarSeguimientoTecnico, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelTecnico;
    }

}
