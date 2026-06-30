package view;

import javax.swing.*;
import java.awt.*;

public final class NavegacionSesion {

    private NavegacionSesion() {
    }

    public static void cerrarSesion(Component componenteActual) {
        Login login = new Login();

        JFrame frame = new JFrame("HelpDesk Manager - Login");
        frame.setContentPane(login.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Window ventanaActual = SwingUtilities.getWindowAncestor(componenteActual);
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }
    }
}
