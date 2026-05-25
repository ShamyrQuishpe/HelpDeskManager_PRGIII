package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dao.UsuarioDAO;
import model.Usuario;

public class Login {
    private JTextField txtCorreo;
    private JButton btnLogin;
    private JPasswordField txtContrasena;
    public JPanel panel1;

    public Login() {

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                login();
            }
        });
    }

    // MÉTODO LOGIN
    private void login() {

        String correo = txtCorreo.getText();

        String password = String.valueOf(txtContrasena.getPassword());

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        Usuario usuario = usuarioDAO.login(correo, password);

        if(usuario != null) {

            JOptionPane.showMessageDialog(null, "Bienvenido " + usuario.getNombre());

            // ABRIR SEGÚN ROL
            abrirVentanaSegunRol(usuario);

        } else {

            JOptionPane.showMessageDialog(null, "Correo o contraseña incorrectos");
        }
    }

    // ESTE MÉTODO ES EL QUE TE FALTABA
    private void abrirVentanaSegunRol(Usuario usuario) {

        JFrame frame = new JFrame();

        // CLIENTE
        if(usuario.getRol().equals("ADMIN")) {

            VentanaUsuario ventana = new VentanaUsuario(usuario);

            System.out.println(ventana.panel1);

            frame.setTitle("Panel Cliente");

            frame.setContentPane(ventana.panel1);
        }

        // ADMIN
        /*else if(usuario.getRol().equals("ADMIN")) {

            VentanaAdmin ventana =
                    new VentanaAdmin(usuario);

            frame.setTitle(
                    "Panel Administrador"
            );

            frame.setContentPane(
                    ventana.panel1
            );
        }

        // TECNICO
        else if(usuario.getRol().equals("TECNICO")) {

            VentanaTecnico ventana =
                    new VentanaTecnico(usuario);

            frame.setTitle(
                    "Panel Técnico"
            );

            frame.setContentPane(
                    ventana.panel1
            );
        }*/

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        // CERRAR LOGIN
        SwingUtilities.getWindowAncestor(btnLogin).dispose();
    }
}
