package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Menu {
    public static void mostrarMenu(Connection connection, int usuarioId) {
        JFrame menuFrame = new JFrame("Menú Principal");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(400, 300);
        menuFrame.getContentPane().setLayout(null);

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
        opcionesLabel.setBounds(20, 26, 300, 144);
        menuFrame.getContentPane().add(opcionesLabel);

        JTextField opcionTextField = new JTextField();
        opcionTextField.setBounds(20, 180, 100, 20);
        menuFrame.getContentPane().add(opcionTextField);

        JButton seleccionarButton = new JButton("Seleccionar");
        seleccionarButton.setBounds(276, 181, 100, 20);
        menuFrame.getContentPane().add(seleccionarButton);

        seleccionarButton.addActionListener(e -> {
            int opcionSeleccionada = Integer.parseInt(opcionTextField.getText());
            switch (opcionSeleccionada) {
                case 1:
                    try {
                        String saldoMessage = Cajero.consultarSaldo(connection, usuarioId);
                        mostrarMensaje("Consulta de Saldo", saldoMessage);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        Cajero.realizarDeposito(connection, usuarioId);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        Cajero.realizarRetiro(connection, usuarioId);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        Cajero.cambiarPIN(connection, usuarioId);
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
    public static void mostrarMensaje(String titulo, String mensaje) {
        JFrame mensajeFrame = new JFrame(titulo);
        mensajeFrame.setSize(500, 300);
        mensajeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mensajeFrame.getContentPane().setLayout(null);
        
        JButton cerrarButton = new JButton("Cerrar");
        cerrarButton.setBounds(362, 212, 100, 25);
        cerrarButton.addActionListener(e -> mensajeFrame.dispose());
        mensajeFrame.getContentPane().add(cerrarButton);

        JTextArea mensajeArea = new JTextArea(mensaje);
        mensajeArea.setBounds(10, 11, 466, 241);
        mensajeArea.setEditable(false);
        mensajeFrame.getContentPane().add(mensajeArea);

        mensajeFrame.setVisible(true);
    }
}
