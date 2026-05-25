package view;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

        for(Usuario u : lista){

            modelo.addElement(u.toString());
        }

        lstUsuarios.setModel(modelo);
    }

    // BUSCAR
    private void buscarUsuario() {

        int id = Integer.parseInt(txtIdModificar.getText());

        UsuarioDAO dao = new UsuarioDAO();

        usuarioActual = dao.buscarPorId(id);

        if(usuarioActual != null){

            txtNombreModificar.setText(usuarioActual.getNombre());

            txtCorreoModificar.setText(usuarioActual.getCorreo());

            txtContrasenaUsuario.setText(usuarioActual.getPassword());

            cmbRolUsuarioMod.setSelectedItem(usuarioActual.getRol());

            bloquearCamposModificar();

            JOptionPane.showMessageDialog(null, "Usuario encontrado");
        }
        else{

            JOptionPane.showMessageDialog(null, "Usuario no encontrado");
        }
    }

    // ACTUALIZAR
    private void actualizarUsuario() {

        // PRIMER CLICK
        if(!editando){

            int opcion = JOptionPane.showConfirmDialog(null, "¿Deseas modificar este usuario?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if(opcion == JOptionPane.YES_OPTION){

                desbloquearCamposModificar();

                editando = true;

                btnActualizarUsuario.setText("Guardar Cambios");
            }
        }

        // SEGUNDO CLICK
        else{

            usuarioActual.setNombre(txtNombreModificar.getText());

            usuarioActual.setCorreo(txtCorreoModificar.getText());

            usuarioActual.setPassword(txtContrasenaUsuario.getText());

            usuarioActual.setRol(cmbRolUsuarioMod.getSelectedItem().toString());

            UsuarioDAO dao = new UsuarioDAO();

            dao.actualizar(usuarioActual);

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

        if(confirmacion == JOptionPane.YES_OPTION){

            int id = Integer.parseInt(txtIdModificar.getText());

            UsuarioDAO dao = new UsuarioDAO();

            dao.eliminar(id);

            JOptionPane.showMessageDialog(null, "Usuario eliminado");
        }
    }

    // BLOQUEAR
    private void bloquearCamposModificar(){

        txtNombreModificar.setEditable(false);

        txtCorreoModificar.setEditable(false);

        txtContrasenaUsuario.setEditable(false);

        cmbRolUsuarioMod.setEnabled(false);
    }

    // DESBLOQUEAR
    private void desbloquearCamposModificar(){

        txtNombreModificar.setEditable(true);

        txtCorreoModificar.setEditable(true);

        txtContrasenaUsuario.setEditable(true);

        cmbRolUsuarioMod.setEnabled(true);
    }
}
