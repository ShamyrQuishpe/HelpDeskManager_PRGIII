package view;

import javax.swing.*;
import dao.UsuarioDAO;
import model.Usuario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaUsuario {
    public JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane2;
    private JTabbedPane tabbedPane3;
    private JTextField txtNombreUsuario;
    private JTextField txtCorreoUsuario;
    private JLabel lblNombre;
    private JPasswordField txtContrasenaUsuario;
    private JButton btnActualizarUsuario;
    private Usuario usuario;
    private boolean editando = false;

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
        if(!editando) {

            int confirmacion =
                    JOptionPane.showConfirmDialog(
                            null,
                            "¿Seguro deseas modificar tus datos?",
                            "Confirmación",
                            JOptionPane.YES_NO_OPTION
                    );

            if(confirmacion == JOptionPane.YES_OPTION) {

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
}
