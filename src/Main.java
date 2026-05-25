package view;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // Crear formulario login
        Login login = new Login();

        // Crear ventana principal
        JFrame frame = new JFrame("HelpDesk Manager - Login");

        // Cargar panel del GUI Form
        frame.setContentPane(login.panel1);

        // Configuración ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}