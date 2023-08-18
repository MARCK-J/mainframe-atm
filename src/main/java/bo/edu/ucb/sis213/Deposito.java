package bo.edu.ucb.sis213;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;

public class Deposito {
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void realizarDeposito(Connection connection, int usuarioId) throws SQLException {
	    JFrame depositoFrame = new JFrame("Realizar Deposito");
	    depositoFrame.getContentPane().setBackground(new Color(255, 255, 208));
	    depositoFrame.setSize(300, 220);
	    depositoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    depositoFrame.getContentPane().setLayout(null);

	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        
	        // Calcular las coordenadas para centrar el frame
	        int x = (screenSize.width - depositoFrame.getWidth()) / 2;
	        int y = (screenSize.height - depositoFrame.getHeight()) / 2;
	        
	        // Establecer las coordenadas para el frame
	        depositoFrame.setLocation(x, y);

	    JLabel cantidadLabel = new JLabel("Ingrese la cantidad a depositar:");
	    cantidadLabel.setBounds(10, 10, 200, 20);
	    depositoFrame.getContentPane().add(cantidadLabel);

	    JTextField cantidadTextField = new JTextField();
	    cantidadTextField.setBounds(10, 35, 120, 20);
	    depositoFrame.getContentPane().add(cantidadTextField);

	    JButton depositarButton = new JButton("Depositar");
	    depositarButton.setBackground(new Color(145, 200, 255));
	    depositarButton.setBounds(138, 155, 142, 25);
	    depositarButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            double cantidad = Double.parseDouble(cantidadTextField.getText());

	            if (cantidad <= 0) {
	                Mensaje.mostrarMensaje("Deposito", "Cantidad no válida.");
	            } else {
	                try {
	                    String depositoMessage = Cajero.realizarDepositoOperacion(connection, usuarioId, cantidad);
	                    Mensaje.mostrarMensaje("Deposito", depositoMessage);
	                    depositoFrame.dispose();
	                } catch (SQLException ex) {
	                    Mensaje.mostrarMensaje("Error", "Error al realizar el deposito: " + ex.getMessage());
	                }
	            }
	        }
	    });
	    depositoFrame.getContentPane().add(depositarButton);

	    depositoFrame.setVisible(true);
	}
}
