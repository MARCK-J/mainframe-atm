package bo.edu.ucb.sis213.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bo.edu.ucb.sis213.BDD.Conexion;
import bo.edu.ucb.sis213.Funciones.Cajero;

public class App {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cajero Automatico");
        frame.setBackground(new Color(0, 0, 0));
        frame.getContentPane().setBackground(new Color(255, 255, 196));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(460, 250);
        frame.getContentPane().setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Calcular las coordenadas para centrar el frame
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        
        // Establecer las coordenadas para el frame
        frame.setLocation(x, y);

        JLabel pinLabel = new JLabel("Ingrese su Password de 6 digitos:");
        pinLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        pinLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pinLabel.setBounds(6, 128, 204, 20);
        frame.getContentPane().add(pinLabel);

        JTextField aliasTextField = new JTextField();
        aliasTextField.setBounds(216, 73, 220, 20);
        frame.getContentPane().add(aliasTextField);

        JTextField pinTextField = new JTextField();
        pinTextField.setBounds(216, 128, 220, 20);
        frame.getContentPane().add(pinTextField);

        JButton loginButton = new JButton("Ingresar");
        loginButton.setBackground(new Color(145, 200, 255));
        loginButton.setBounds(329, 170, 107, 20);
        frame.getContentPane().add(loginButton);
        
        JLabel lblNewLabel = new JLabel("Bienvenido");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(176, 28, 70, 14);
        frame.getContentPane().add(lblNewLabel);
        
        JLabel lblIngreseSuAlias = new JLabel("Ingrese su Alias");
        lblIngreseSuAlias.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblIngreseSuAlias.setHorizontalAlignment(SwingConstants.CENTER);
        lblIngreseSuAlias.setBounds(6, 73, 200, 20);
        frame.getContentPane().add(lblIngreseSuAlias);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int intentos = 3;
                try {
                    Connection connection = Conexion.getConnection();
                    int pinIngresado = Integer.parseInt(pinTextField.getText());
                    String aliasIngresado = aliasTextField.getText();
                    int usuarioId = Cajero.obtenerUsuarioIdPorPin(connection, aliasIngresado, pinIngresado);
                    if (usuarioId != -1) {
                        Cajero.setUsuarioId(usuarioId);
                        frame.dispose();
                        Menu.mostrarMenu(connection, usuarioId);
                    } else {
                        intentos--;
                        if (intentos > 0) {
                            JOptionPane.showMessageDialog(null, "Password incorrecto. Le quedan " + intentos + " intentos.");
                        } else {
                            JOptionPane.showMessageDialog(null, "PIN incorrecto. Ha excedido el numero de intentos.");
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


