package bo.edu.ucb.sis213;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;

public class CambioPin {
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void cambiarPIN(Connection connection, int usuarioId) throws SQLException {
	    String query1 = "SELECT pin FROM usuarios WHERE id = ?";
	    
	    try {
	            PreparedStatement preparedStatement = connection.prepareStatement(query1);
	            preparedStatement.setInt(1, usuarioId);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                Cajero.usuarioPIN = resultSet.getInt("pin");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    
	    JFrame cambiarPINFrame = new JFrame("Cambiar PIN");
	    cambiarPINFrame.getContentPane().setBackground(new Color(255, 255, 208));
	    cambiarPINFrame.setSize(350, 260);
	    cambiarPINFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    cambiarPINFrame.getContentPane().setLayout(null);

	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        
	        // Calcular las coordenadas para centrar el frame
	        int x = (screenSize.width - cambiarPINFrame.getWidth()) / 2;
	        int y = (screenSize.height - cambiarPINFrame.getHeight()) / 2;
	        
	        // Establecer las coordenadas para el frame
	        cambiarPINFrame.setLocation(x, y);

	    JLabel actualPinLabel = new JLabel("Ingrese su PIN actual:");
	    actualPinLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
	    actualPinLabel.setBounds(10, 10, 237, 20);
	    cambiarPINFrame.getContentPane().add(actualPinLabel);

	    JTextField actualPinTextField = new JTextField();
	    actualPinTextField.setBounds(10, 35, 100, 20);
	    cambiarPINFrame.getContentPane().add(actualPinTextField);

	    JLabel nuevoPinLabel = new JLabel("Ingrese su nuevo PIN:");
	    nuevoPinLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
	    nuevoPinLabel.setBounds(10, 70, 237, 20);
	    cambiarPINFrame.getContentPane().add(nuevoPinLabel);

	    JTextField nuevoPinTextField = new JTextField();
	    nuevoPinTextField.setBounds(10, 95, 100, 20);
	    cambiarPINFrame.getContentPane().add(nuevoPinTextField);

	    JLabel confirmarPinLabel = new JLabel("Confirme su nuevo PIN:");
	    confirmarPinLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
	    confirmarPinLabel.setBounds(10, 130, 237, 20);
	    cambiarPINFrame.getContentPane().add(confirmarPinLabel);

	    JTextField confirmarPinTextField = new JTextField();
	    confirmarPinTextField.setBounds(10, 155, 100, 20);
	    cambiarPINFrame.getContentPane().add(confirmarPinTextField);

	    JButton cambiarButton = new JButton("Cambiar PIN");
	    cambiarButton.setBackground(new Color(145, 200, 255));
	    cambiarButton.setBounds(190, 187, 136, 25);
	    cambiarButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            
	            int actualPin = Integer.parseInt(actualPinTextField.getText());
	            int nuevoPin = Integer.parseInt(nuevoPinTextField.getText());
	            int confirmarPin = Integer.parseInt(confirmarPinTextField.getText());

	            if (actualPin == Cajero.usuarioPIN) {
	                if (nuevoPin == confirmarPin) {
	                    String updateQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
	                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
	                        updateStatement.setInt(1, nuevoPin);
	                        updateStatement.setInt(2, usuarioId);
	                        int rowsAffected = updateStatement.executeUpdate();
	                        if (rowsAffected > 0) {
	                            Mensaje.mostrarMensaje("Cambio de PIN", "PIN actualizado con éxito.");
	                            cambiarPINFrame.dispose();
	                        } else {
	                            Mensaje.mostrarMensaje("Cambio de PIN", "No se pudo actualizar el PIN.");
	                        }
	                    } catch (SQLException ex) {
	                        Mensaje.mostrarMensaje("Error", "Error al cambiar el PIN: " + ex.getMessage());
	                    }
	                } else {
	                    Mensaje.mostrarMensaje("Cambio de PIN", "Los PINs no coinciden.");
	                }
	            } else {
	                Mensaje.mostrarMensaje("Cambio de PIN", "PIN actual incorrecto.");
	            }
	        }
	    });
	    cambiarPINFrame.getContentPane().add(cambiarButton);

	    cambiarPINFrame.setVisible(true);
	}
}
