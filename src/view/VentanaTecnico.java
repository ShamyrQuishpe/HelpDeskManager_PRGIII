package view;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaTecnico {
    private JPanel panelTecnico;
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane2;
    private JLabel lblNombre;
    private JTextField txtNombreTecnico;
    private JTextField txtCorreoTecnico;
    private JPasswordField txtContrasenaTecnico;
    private JButton btnActualizarTecnico;
    private Usuario usuario;
    private boolean editando = false;

    public VentanaTecnico() {
        this.usuario = usuario;

        // Cargar datos usuario
        cargarDatos();

        // Bloquear campos inicialmente
        bloquearCampos();
        btnActualizarTecnico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    // Mostrar datos usuario

    private void cargarDatos() {

        lblNombre.setText("Bienvenid@ " + usuario.getNombre());

        txtNombreTecnico.setText(usuario.getNombre());

        txtCorreoTecnico.setText(usuario.getCorreo());

        txtContrasenaTecnico.setText(usuario.getPassword());
    }
    // Bloquear campos
    private void bloquearCampos() {

        txtNombreTecnico.setEditable(false);

        txtCorreoTecnico.setEditable(false);

        txtContrasenaTecnico.setEditable(false);
    }

    // Desbloquear campos
    private void desbloquearCampos() {

        txtNombreTecnico.setEditable(true);

        txtCorreoTecnico.setEditable(true);

        txtContrasenaTecnico.setEditable(true);
    }

    // Actualizar datos usuario
    private void actualizarDatos() {

        // PRIMER CLICK
        if(!editando) {

            int confirmacion =
                    JOptionPane.showConfirmDialog(null, "¿Seguro deseas modificar tus datos?", "Confirmación", JOptionPane.YES_NO_OPTION);

            if(confirmacion == JOptionPane.YES_OPTION) {

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

            lblNombre.setText(
                    "Bienvenid@ " +
                            usuario.getNombre()
            );

            bloquearCampos();

            editando = false;

            btnActualizarTecnico.setText("Actualizar Datos");

            JOptionPane.showMessageDialog(null, "Datos actualizados correctamente");
        }
    }
}
