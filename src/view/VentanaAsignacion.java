package view;

import dao.AsignacionDAO;
import dao.TicketDAO;
import dao.UsuarioDAO;
import model.Asignacion;
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

public class VentanaAsignacion {

    public JPanel panelAsignacion;
    private JTabbedPane tabbedPane1;
    private JPanel panelGestion;
    private JList lstAsignaciones;
    private JTextField txtIdTicket;
    private JComboBox cmbTecnico;
    private JButton btnAsignar;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnListar;
    private JButton btnDesasignar;
    private JButton btnFiltrarTecnico;

    private boolean editando = false;

    public VentanaAsignacion() {

        // CARGAR TÉCNICOS EN COMBOBOX
        cargarTecnicosCmb();

        // LISTAR ASIGNACIONES AL INICIO
        listarTicketsParaAsignar();

        // EVENTOS
        btnListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarTicketsParaAsignar();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarAsignacion();
            }
        });

        btnAsignar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                asignarTecnico();
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarAsignacion();
            }
        });

        btnDesasignar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desasignarTecnico();
            }
        });

        btnFiltrarTecnico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarPorTecnico();
            }
        });

        // EVENTO: AL HACER CLIC EN UNA ASIGNACIÓN DE LA LISTA
        lstAsignaciones.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && lstAsignaciones.getSelectedValue() != null) {
                    String seleccion = lstAsignaciones.getSelectedValue().toString();
                    try {
                        // Formato: "Ticket ID: 5 (Titulo) | Técnico ID: 2 - Juan Perez | Fecha: ..."
                        String ticketPart = seleccion.split("\\|")[0].trim();
                        String idTicketStr = ticketPart.substring(ticketPart.indexOf("Ticket ID:") + 10, ticketPart.indexOf("(")).trim();
                        txtIdTicket.setText(idTicketStr);

                        if (!seleccion.contains("T") || !seleccion.contains("ID:") || seleccion.split("\\|").length < 2) {
                            return;
                        }

                        String tecnicoPart = seleccion.split("\\|")[1].trim();
                        if (!tecnicoPart.contains("ID:")) {
                            return;
                        }
                        String tecnicoInfo = tecnicoPart.replace("Técnico ID:", "").trim();

                        for (int i = 0; i < cmbTecnico.getItemCount(); i++) {
                            if (cmbTecnico.getItemAt(i).toString().startsWith(tecnicoInfo.split(" - ")[0])) {
                                cmbTecnico.setSelectedIndex(i);
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("Error al extraer info de la selección de asignaciones: " + ex.getMessage());
                    }
                }
            }
        });
    }

    // ------------------------------------------
    // LÓGICA DE ASIGNACIÓN DE TÉCNICOS
    // ------------------------------------------

    private void cargarTecnicosCmb() {
        cmbTecnico.removeAllItems();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ArrayList<Usuario> tecnicos = usuarioDAO.obtenerTecnicos();
        for (Usuario t : tecnicos) {
            cmbTecnico.addItem(t.getIdUsuario() + " - " + t.getNombre());
        }
    }

    private void asignarTecnico() {
        try {
            if (txtIdTicket.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de ticket.");
                return;
            }
            int idTicket = Integer.parseInt(txtIdTicket.getText().trim());
            if (cmbTecnico.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un técnico.");
                return;
            }
            String selectedTecnico = cmbTecnico.getSelectedItem().toString();
            int idTecnico = Integer.parseInt(selectedTecnico.split(" - ")[0]);

            // Verificar si el ticket existe
            TicketDAO ticketDAO = new TicketDAO();
            ArrayList<Ticket> todosTickets = ticketDAO.obtenerTickets();
            boolean ticketExiste = false;
            for (Ticket t : todosTickets) {
                if (t.getIdTicket() == idTicket) {
                    ticketExiste = true;
                    break;
                }
            }

            if (!ticketExiste) {
                JOptionPane.showMessageDialog(null, "El ticket con ID " + idTicket + " no existe.");
                return;
            }

            AsignacionDAO asignacionDAO = new AsignacionDAO();
            Asignacion asignacionExistente = asignacionDAO.obtenerAsignacionPorTicket(idTicket);

            if (asignacionExistente != null) {
                int opcion = JOptionPane.showConfirmDialog(null, "El ticket ya está asignado al técnico ID " + asignacionExistente.getIdTecnico() + ". ¿Deseas reasignarlo?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    Asignacion nuevaAsignacion = new Asignacion(0, idTicket, idTecnico, LocalDateTime.now());
                    asignacionDAO.actualizarAsignacion(nuevaAsignacion);
                    JOptionPane.showMessageDialog(null, "Asignación actualizada (ticket reasignado).");
                    listarTicketsParaAsignar();
                }
            } else {
                Asignacion nuevaAsignacion = new Asignacion(0, idTicket, idTecnico, LocalDateTime.now());
                asignacionDAO.crearAsignacion(nuevaAsignacion);
                JOptionPane.showMessageDialog(null, "Técnico asignado correctamente.");
                listarTicketsParaAsignar();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de ticket válido (número).");
        }
    }

    private void actualizarAsignacion() {
        try {
            if (txtIdTicket.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de ticket.");
                return;
            }
            int idTicket = Integer.parseInt(txtIdTicket.getText().trim());
            if (cmbTecnico.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un técnico.");
                return;
            }

            AsignacionDAO asignacionDAO = new AsignacionDAO();
            Asignacion a = asignacionDAO.obtenerAsignacionPorTicket(idTicket);

            if (a == null) {
                JOptionPane.showMessageDialog(null, "No existe una asignación para el ticket ID " + idTicket + ". Use 'Asignar Técnico' primero.");
                return;
            }

            String selectedTecnico = cmbTecnico.getSelectedItem().toString();
            int idTecnico = Integer.parseInt(selectedTecnico.split(" - ")[0]);

            int opcion = JOptionPane.showConfirmDialog(null, "¿Deseas actualizar la asignación del ticket ID " + idTicket + " al técnico seleccionado?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                Asignacion nuevaAsignacion = new Asignacion(0, idTicket, idTecnico, LocalDateTime.now());
                asignacionDAO.actualizarAsignacion(nuevaAsignacion);
                JOptionPane.showMessageDialog(null, "Asignación actualizada correctamente.");
                listarTicketsParaAsignar();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de ticket válido (número).");
        }
    }

    private void desasignarTecnico() {
        try {
            if (txtIdTicket.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de ticket.");
                return;
            }
            int idTicket = Integer.parseInt(txtIdTicket.getText().trim());

            AsignacionDAO asignacionDAO = new AsignacionDAO();
            Asignacion a = asignacionDAO.obtenerAsignacionPorTicket(idTicket);
            if (a == null) {
                JOptionPane.showMessageDialog(null, "No existe una asignación para el ticket ID " + idTicket);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro deseas quitar la asignación de este ticket?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                asignacionDAO.eliminarAsignacion(idTicket);
                JOptionPane.showMessageDialog(null, "Asignación eliminada correctamente.");
                listarTicketsParaAsignar();
                txtIdTicket.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de ticket válido (número).");
        }
    }

    private void listarTicketsParaAsignar() {
        TicketDAO ticketDAO = new TicketDAO();
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        ArrayList<Ticket> todosTickets = ticketDAO.obtenerTickets();
        DefaultListModel modelo = new DefaultListModel();

        for (Ticket t : todosTickets) {
            Asignacion asignacion = asignacionDAO.obtenerAsignacionPorTicket(t.getIdTicket());
            String info = "Ticket ID: " + t.getIdTicket() + " (" + t.getTitulo() + ")";

            if (asignacion != null) {
                String nombreTecnico = "Desconocido";
                Usuario tecnico = usuarioDAO.buscarPorId(asignacion.getIdTecnico());
                if (tecnico != null) {
                    nombreTecnico = tecnico.getNombre();
                }

                info += " | Técnico ID: " + asignacion.getIdTecnico() + " - " + nombreTecnico;
            } else {
                info += " Sin asignar";
            }

            info += " | Prioridad: " + t.getPrioridad() +
                    " | Estado: " + t.getEstado();

            modelo.addElement(info);
        }

        lstAsignaciones.setModel(modelo);
    }

    private void listarAsignaciones() {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        ArrayList<Asignacion> lista = asignacionDAO.obtenerAsignaciones();
        DefaultListModel modelo = new DefaultListModel();

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        TicketDAO ticketDAO = new TicketDAO();
        ArrayList<Ticket> todosTickets = ticketDAO.obtenerTickets();

        for (Asignacion a : lista) {
            String nombreTecnico = "Desconocido";
            Usuario tecnico = usuarioDAO.buscarPorId(a.getIdTecnico());
            if (tecnico != null) {
                nombreTecnico = tecnico.getNombre();
            }

            String tituloTicket = "Desconocido";
            for (Ticket t : todosTickets) {
                if (t.getIdTicket() == a.getIdTicket()) {
                    tituloTicket = t.getTitulo();
                    break;
                }
            }

            String info = "Ticket ID: " + a.getIdTicket() + " (" + tituloTicket + ")" +
                    " | Técnico ID: " + a.getIdTecnico() + " - " + nombreTecnico +
                    " | Fecha: " + a.getFechaAsignacion().toString();
            modelo.addElement(info);
        }

        lstAsignaciones.setModel(modelo);
    }

    private void buscarAsignacion() {
        try {
            if (txtIdTicket.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de ticket.");
                return;
            }
            int idTicket = Integer.parseInt(txtIdTicket.getText().trim());
            AsignacionDAO asignacionDAO = new AsignacionDAO();
            Asignacion a = asignacionDAO.obtenerAsignacionPorTicket(idTicket);

            if (a != null) {
                for (int i = 0; i < cmbTecnico.getItemCount(); i++) {
                    if (cmbTecnico.getItemAt(i).toString().startsWith(String.valueOf(a.getIdTecnico()))) {
                        cmbTecnico.setSelectedIndex(i);
                        break;
                    }
                }
                JOptionPane.showMessageDialog(null, "Asignación encontrada. Técnico asignado ID: " + a.getIdTecnico());
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró asignación para el ticket con ID " + idTicket);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de ticket válido (número).");
        }
    }

    private void filtrarPorTecnico() {
        if (cmbTecnico.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un técnico.");
            return;
        }
        try {
            String selectedTecnico = cmbTecnico.getSelectedItem().toString();
            int idTecnico = Integer.parseInt(selectedTecnico.split(" - ")[0]);

            AsignacionDAO asignacionDAO = new AsignacionDAO();
            ArrayList<Asignacion> lista = asignacionDAO.obtenerAsignaciones();
            DefaultListModel modelo = new DefaultListModel();

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            TicketDAO ticketDAO = new TicketDAO();
            ArrayList<Ticket> todosTickets = ticketDAO.obtenerTickets();

            int count = 0;
            for (Asignacion a : lista) {
                if (a.getIdTecnico() == idTecnico) {
                    String nombreTecnico = "Desconocido";
                    Usuario tecnico = usuarioDAO.buscarPorId(a.getIdTecnico());
                    if (tecnico != null) {
                        nombreTecnico = tecnico.getNombre();
                    }

                    String tituloTicket = "Desconocido";
                    for (Ticket t : todosTickets) {
                        if (t.getIdTicket() == a.getIdTicket()) {
                            tituloTicket = t.getTitulo();
                            break;
                        }
                    }

                    String info = "Ticket ID: " + a.getIdTicket() + " (" + tituloTicket + ")" +
                            " | Técnico ID: " + a.getIdTecnico() + " - " + nombreTecnico +
                            " | Fecha: " + a.getFechaAsignacion().toString();
                    modelo.addElement(info);
                    count++;
                }
            }

            lstAsignaciones.setModel(modelo);
            if (count == 0) {
                JOptionPane.showMessageDialog(null, "El técnico seleccionado no tiene tickets asignados.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al filtrar por técnico: " + ex.getMessage());
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
        panelAsignacion = new JPanel();
        panelAsignacion.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        panelAsignacion.add(tabbedPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        panelGestion = new JPanel();
        panelGestion.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 3, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1.addTab("Gestión de Asignaciones", panelGestion);
        final JScrollPane scrollPane1 = new JScrollPane();
        panelGestion.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 6, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(220, 150), null, 0, false));
        lstAsignaciones = new JList();
        scrollPane1.setViewportView(lstAsignaciones);
        final JLabel label1 = new JLabel();
        label1.setText("ID Ticket:");
        panelGestion.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtIdTicket = new JTextField();
        panelGestion.add(txtIdTicket, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Técnico:");
        panelGestion.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbTecnico = new JComboBox();
        panelGestion.add(cmbTecnico, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnAsignar = new JButton();
        btnAsignar.setText("Asignar Técnico");
        panelGestion.add(btnAsignar, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnBuscar = new JButton();
        btnBuscar.setText("Buscar por ID Ticket");
        panelGestion.add(btnBuscar, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnActualizar = new JButton();
        btnActualizar.setText("Actualizar Asignación");
        panelGestion.add(btnActualizar, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnListar = new JButton();
        btnListar.setText("Listar Tickets");
        panelGestion.add(btnListar, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDesasignar = new JButton();
        btnDesasignar.setText("Quitar Asignación");
        panelGestion.add(btnDesasignar, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnFiltrarTecnico = new JButton();
        btnFiltrarTecnico.setText("Filtrar por Técnico");
        panelGestion.add(btnFiltrarTecnico, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelAsignacion;
    }


}
