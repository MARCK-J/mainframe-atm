package bo.edu.ucb.sis213;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cajero Automático");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(460, 250);
        frame.getContentPane().setLayout(null);

        JLabel pinLabel = new JLabel("Ingrese su PIN de 4 dígitos:");
        pinLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pinLabel.setBounds(10, 128, 200, 20);
        frame.getContentPane().add(pinLabel);

        JTextField pinTextField = new JTextField();
        pinTextField.setBounds(216, 128, 220, 20);
        frame.getContentPane().add(pinTextField);

        JButton loginButton = new JButton("Ingresar");
        loginButton.setBounds(329, 170, 107, 20);
        frame.getContentPane().add(loginButton);
        
        JLabel lblNewLabel = new JLabel("Bienvenido");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(176, 28, 70, 14);
        frame.getContentPane().add(lblNewLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int intentos = 3;
                try {
                    Connection connection = Conexion.getConnection();
                    int pinIngresado = Integer.parseInt(pinTextField.getText());
                    int usuarioId = Cajero.obtenerUsuarioIdPorPin(connection, pinIngresado);
                    if (usuarioId != -1) {
                        Cajero.setUsuarioId(usuarioId);
                        frame.dispose();
                        Menu.mostrarMenu(connection, usuarioId);
                    } else {
                        intentos--;
                        if (intentos > 0) {
                            JOptionPane.showMessageDialog(null, "PIN incorrecto. Le quedan " + intentos + " intentos.");
                        } else {
                            JOptionPane.showMessageDialog(null, "PIN incorrecto. Ha excedido el número de intentos.");
                            System.exit(0);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + ex.getMessage());
                }
            }
        });

        frame.setVisible(true);
    }
}



