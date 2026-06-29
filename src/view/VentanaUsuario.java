package view;

import javax.swing.*;
import dao.AsignacionDAO;
import dao.SeguimientoDAO;
import dao.UsuarioDAO;
import model.Asignacion;
import model.PriorityManager;
import model.Seguimiento;
import model.Usuario;
import dao.TicketDAO;
import model.Ticket;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaUsuario {
    public JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextField txtNombreUsuario;
    private JTextField txtCorreoUsuario;
    private JLabel lblNombre;
    private JPasswordField txtContrasenaUsuario;
    private JButton btnActualizarUsuario;
    private JPanel panelTickets;
    private JList lstTickets;
    private JTextField txtTitulo;
    private JTextField txtDescripcion;
    private JComboBox cmbCategoria;
    private JComboBox cmbPrioridad;
    private JComboBox cmbEstado;
    private JButton btnListarTickets;
    private JButton btnCrearTicket;
    private JPanel panelSeguimientoUsuario;
    private JList lstSeguimientoUsuario;
    private JTextField txtIdTicketSeguimientoUsuario;
    private JButton btnBuscarSeguimientoUsuario;
    private JButton btnListarSeguimientoUsuario;
    private Usuario usuario;
    private boolean editando = false;
    private TicketDAO ticketDAO;
    private SeguimientoDAO seguimientoDAO;

    public VentanaUsuario(Usuario usuario) {
        this.usuario = usuario;

        // Cargar datos usuario
        cargarDatos();

        // Bloquear campos inicialmente
        bloquearCampos();

        // Botón actualizar
        btnActualizarUsuario.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        actualizarDatos();
                    }
                });

        // Inicializar el DAO de tickets
        this.ticketDAO = new TicketDAO();
        this.seguimientoDAO = new SeguimientoDAO();

        // Inicializar componentes de Tickets
        inicializarComboBoxes();
        cargarTickets();

        // Evento: Botón crear ticket
        btnCrearTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearTicket();
            }
        });

        // Evento: Botón listar/actualizar tickets
        btnListarTickets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarTickets();
            }
        });

        cargarSeguimientosUsuario();

        btnListarSeguimientoUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarSeguimientosUsuario();
            }
        });

        btnBuscarSeguimientoUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarSeguimientoUsuarioPorTicket();
            }
        });

    }

    // Mostrar datos usuario
    private void cargarDatos() {

        lblNombre.setText("Bienvenid@ " + usuario.getNombre());

        txtNombreUsuario.setText(usuario.getNombre());

        txtCorreoUsuario.setText(usuario.getCorreo());

        txtContrasenaUsuario.setText(usuario.getPassword());
    }

    // Bloquear campos
    private void bloquearCampos() {

        txtNombreUsuario.setEditable(false);

        txtCorreoUsuario.setEditable(false);

        txtContrasenaUsuario.setEditable(false);
    }

    // Desbloquear campos
    private void desbloquearCampos() {

        txtNombreUsuario.setEditable(true);

        txtCorreoUsuario.setEditable(true);

        txtContrasenaUsuario.setEditable(true);
    }

    // Actualizar datos usuario
    private void actualizarDatos() {
        // PRIMER CLICK
        if (!editando) {
            int confirmacion =
                    JOptionPane.showConfirmDialog(
                            null,
                            "¿Seguro deseas modificar tus datos?",
                            "Confirmación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirmacion == JOptionPane.YES_OPTION) {

                desbloquearCampos();

                editando = true;

                btnActualizarUsuario.setText(
                        "Guardar Cambios"
                );
            }

        }

        // SEGUNDO CLICK
        else {

            usuario.setNombre(
                    txtNombreUsuario.getText()
            );

            usuario.setCorreo(
                    txtCorreoUsuario.getText()
            );

            usuario.setPassword(
                    String.valueOf(
                            txtContrasenaUsuario.getPassword()
                    )
            );

            UsuarioDAO usuarioDAO =
                    new UsuarioDAO();

            usuarioDAO.actualizar(usuario);

            lblNombre.setText(
                    "Bienvenid@ " +
                            usuario.getNombre()
            );

            bloquearCampos();

            editando = false;

            btnActualizarUsuario.setText(
                    "Actualizar Datos"
            );

            JOptionPane.showMessageDialog(
                    null,
                    "Datos actualizados correctamente"
            );
        }
    }


    // Inicializar los ComboBox con opciones
    private void inicializarComboBoxes() {
        cmbCategoria.setModel(new DefaultComboBoxModel(new String[]{"Hardware", "Software", "Red", "Otro"}));
        cmbPrioridad.setModel(new DefaultComboBoxModel(new String[]{"BAJA", "MEDIA", "ALTA", "CRITICA"}));

        cmbEstado.setModel(new DefaultComboBoxModel(new String[]{"ABIERTO"}));
        cmbEstado.setEnabled(false); // El usuario no cambia el estado al crear
    }

    // Cargar y mostrar los tickets del usuario
    private void cargarTickets() {
        ArrayList<Ticket> listaTickets = ticketDAO.obtenerTicketsPorUsuario(usuario.getIdUsuario());
        DefaultListModel modeloLista = new DefaultListModel();

        for (Ticket t : listaTickets) {
            modeloLista.addElement(t);
        }

        lstTickets.setModel(modeloLista);
    }

    // Lógica para crear un nuevo ticket
    private void crearTicket() {
        String titulo = txtTitulo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (titulo.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa un título y una descripción.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String categoria = cmbCategoria.getSelectedItem().toString();
        String prioridad = cmbPrioridad.getSelectedItem().toString();
        String estado = "ABIERTO";

        // Se usa 0 como ID porque la BD lo genera automáticamente
        Ticket nuevoTicket = new Ticket(
                0,
                titulo,
                descripcion,
                categoria,
                prioridad,
                estado,
                LocalDateTime.now(),
                usuario.getIdUsuario()
        );

        int idTicketCreado = ticketDAO.crearTicket(nuevoTicket);
        Usuario tecnicoAsignado = asignarTicketAutomaticamente(idTicketCreado);

        if (tecnicoAsignado != null) {
            JOptionPane.showMessageDialog(null, "Ticket enviado correctamente. Asignado al tÃ©cnico: " + tecnicoAsignado.getNombre());
        } else {
            JOptionPane.showMessageDialog(null, "Ticket enviado correctamente. No hay tÃ©cnicos disponibles para asignarlo automÃ¡ticamente.");
        }

        // Limpiar formulario
        txtTitulo.setText("");
        txtDescripcion.setText("");
        cmbCategoria.setSelectedIndex(0);
        cmbPrioridad.setSelectedIndex(0);

        // Actualizar la lista
        cargarTickets();
    }

    private Usuario asignarTicketAutomaticamente(int idTicket) {
        if (idTicket <= 0) {
            return null;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ArrayList<Usuario> tecnicos = usuarioDAO.obtenerTecnicos();

        if (tecnicos.isEmpty()) {
            return null;
        }

        PriorityManager priorityManager = new PriorityManager();
        Usuario tecnicoSeleccionado = priorityManager.seleccionarTecnicoDisponible(tecnicos, ticketDAO);

        if (tecnicoSeleccionado == null) {
            return null;
        }

        AsignacionDAO asignacionDAO = new AsignacionDAO();
        Asignacion asignacion = new Asignacion(0, idTicket, tecnicoSeleccionado.getIdUsuario(), LocalDateTime.now());
        asignacionDAO.crearAsignacion(asignacion);

        return tecnicoSeleccionado;
    }

    private void cargarSeguimientosUsuario() {
        ArrayList<Seguimiento> listaSeguimiento = seguimientoDAO.obtenerPorUsuario(usuario.getIdUsuario());
        DefaultListModel modeloLista = new DefaultListModel();

        for (Seguimiento s : listaSeguimiento) {
            modeloLista.addElement(s);
        }

        lstSeguimientoUsuario.setModel(modeloLista);
    }

    private void buscarSeguimientoUsuarioPorTicket() {
        try {
            if (txtIdTicketSeguimientoUsuario.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingresa un ID de ticket.");
                return;
            }

            int idTicket = Integer.parseInt(txtIdTicketSeguimientoUsuario.getText().trim());
            ArrayList<Ticket> ticketsUsuario = ticketDAO.obtenerTicketsPorUsuario(usuario.getIdUsuario());
            boolean ticketPerteneceUsuario = false;

            for (Ticket t : ticketsUsuario) {
                if (t.getIdTicket() == idTicket) {
                    ticketPerteneceUsuario = true;
                    break;
                }
            }

            if (!ticketPerteneceUsuario) {
                JOptionPane.showMessageDialog(null, "Ese ticket no pertenece a tu usuario.");
                return;
            }

            ArrayList<Seguimiento> listaSeguimiento = seguimientoDAO.obtenerPorTicket(idTicket);
            DefaultListModel modeloLista = new DefaultListModel();

            for (Seguimiento s : listaSeguimiento) {
                modeloLista.addElement(s);
            }

            lstSeguimientoUsuario.setModel(modeloLista);
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
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        panel1.add(tabbedPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Mi Perfil", panel2);
        final JLabel label1 = new JLabel();
        label1.setText("Nombre");
        panel2.add(label1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNombreUsuario = new JTextField();
        panel2.add(txtNombreUsuario, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Correo");
        panel2.add(label2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtCorreoUsuario = new JTextField();
        panel2.add(txtCorreoUsuario, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lblNombre = new JLabel();
        lblNombre.setText("Label");
        panel2.add(lblNombre, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Contraseaña");
        panel2.add(label3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Estos son tus datos:");
        panel2.add(label4, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtContrasenaUsuario = new JPasswordField();
        panel2.add(txtContrasenaUsuario, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnActualizarUsuario = new JButton();
        btnActualizarUsuario.setText("Actualizar Datos");
        panel2.add(btnActualizarUsuario, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelTickets = new JPanel();
        panelTickets.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 3, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1.addTab("Mis tickets", panelTickets);
        final JScrollPane scrollPane1 = new JScrollPane();
        panelTickets.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 5, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(220, 150), null, 0, false));
        lstTickets = new JList();
        scrollPane1.setViewportView(lstTickets);
        final JLabel label5 = new JLabel();
        label5.setText("Título:");
        panelTickets.add(label5, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtTitulo = new JTextField();
        panelTickets.add(txtTitulo, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Descripción:");
        panelTickets.add(label6, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtDescripcion = new JTextField();
        panelTickets.add(txtDescripcion, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Categoría:");
        panelTickets.add(label7, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbCategoria = new JComboBox();
        panelTickets.add(cmbCategoria, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Prioridad:");
        panelTickets.add(label8, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbPrioridad = new JComboBox();
        panelTickets.add(cmbPrioridad, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Estado:");
        panelTickets.add(label9, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbEstado = new JComboBox();
        panelTickets.add(cmbEstado, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnListarTickets = new JButton();
        btnListarTickets.setText("Actualizar Lista");
        panelTickets.add(btnListarTickets, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCrearTicket = new JButton();
        btnCrearTicket.setText("Enviar Ticket");
        panelTickets.add(btnCrearTicket, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelSeguimientoUsuario = new JPanel();
        panelSeguimientoUsuario.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1.addTab("Seguimiento", panelSeguimientoUsuario);
        final JScrollPane scrollPane2 = new JScrollPane();
        panelSeguimientoUsuario.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(260, 150), null, 0, false));
        lstSeguimientoUsuario = new JList();
        scrollPane2.setViewportView(lstSeguimientoUsuario);
        final JLabel label10 = new JLabel();
        label10.setText("ID Ticket:");
        panelSeguimientoUsuario.add(label10, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtIdTicketSeguimientoUsuario = new JTextField();
        panelSeguimientoUsuario.add(txtIdTicketSeguimientoUsuario, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnBuscarSeguimientoUsuario = new JButton();
        btnBuscarSeguimientoUsuario.setText("Buscar por Ticket");
        panelSeguimientoUsuario.add(btnBuscarSeguimientoUsuario, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnListarSeguimientoUsuario = new JButton();
        btnListarSeguimientoUsuario.setText("Listar Seguimiento");
        panelSeguimientoUsuario.add(btnListarSeguimientoUsuario, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
