package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.SQLException;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;

public class Menu {
    /**
     * @wbp.parser.entryPoint
     */
    public static void mostrarMenu(Connection connection, int usuarioId) {
        JFrame menuFrame = new JFrame("Menú Principal");
        menuFrame.getContentPane().setBackground(new Color(255, 255, 208));
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(400, 300);
        menuFrame.getContentPane().setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Calcular las coordenadas para centrar el frame
        int x = (screenSize.width - menuFrame.getWidth()) / 2;
        int y = (screenSize.height - menuFrame.getHeight()) / 2;
        
        // Establecer las coordenadas para el frame
        menuFrame.setLocation(x, y);

        JLabel opcionesLabel = new JLabel(
                "<html><pre>" +
                        "\nMenú Principal:\n" +
                        "1. Consultar saldo.\n" +
                        "2. Realizar un depósito.\n" +
                        "3. Realizar un retiro.\n" +
                        "4. Cambiar PIN.\n" +
                        "5. Salir.\n" +
                        "Seleccione una opción: " +
                        "</pre></html>"
        );
        opcionesLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        opcionesLabel.setBounds(10, 0, 366, 199);
        menuFrame.getContentPane().add(opcionesLabel);

        JTextField opcionTextField = new JTextField();
        opcionTextField.setBounds(10, 232, 126, 20);
        menuFrame.getContentPane().add(opcionTextField);

        JButton seleccionarButton = new JButton("Seleccionar");
        seleccionarButton.setBackground(new Color(145, 200, 255));
        seleccionarButton.setBounds(250, 232, 126, 20);
        menuFrame.getContentPane().add(seleccionarButton);

        seleccionarButton.addActionListener(e -> {
            int opcionSeleccionada = Integer.parseInt(opcionTextField.getText());
            switch (opcionSeleccionada) {
                case 1:
                    try {
                        String saldoMessage = Cajero.consultarSaldo(connection, usuarioId);
                        Mensaje.mostrarMensaje("Consulta de Saldo", saldoMessage);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        Deposito.realizarDeposito(connection, usuarioId);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        Retiro.realizarRetiro(connection, usuarioId);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        CambioPin.cambiarPIN(connection, usuarioId);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 5:
                    JOptionPane.showMessageDialog(null, "Gracias por usar el cajero. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida. Intente nuevamente.");
            }
        });

        menuFrame.setVisible(true);
    }

    /**
     * @wbp.parser.entryPoint
     */
}
