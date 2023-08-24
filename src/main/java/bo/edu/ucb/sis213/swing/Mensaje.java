package bo.edu.ucb.sis213.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Mensaje {
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void mostrarMensaje(String titulo, String mensaje) {
        JFrame mensajeFrame = new JFrame(titulo);
        mensajeFrame.setSize(500, 300);
        mensajeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mensajeFrame.getContentPane().setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Calcular las coordenadas para centrar el frame
        int x = (screenSize.width - mensajeFrame.getWidth()) / 2;
        int y = (screenSize.height - mensajeFrame.getHeight()) / 2;
        
        // Establecer las coordenadas para el frame
        mensajeFrame.setLocation(x, y);
        
        JButton cerrarButton = new JButton("Cerrar");
        cerrarButton.setBackground(new Color(145, 200, 255));
        cerrarButton.setBounds(362, 212, 100, 25);
        cerrarButton.addActionListener(e -> mensajeFrame.dispose());
        mensajeFrame.getContentPane().add(cerrarButton);

        JTextArea mensajeArea = new JTextArea(mensaje);
        mensajeArea.setBackground(new Color(255, 255, 196));
        mensajeArea.setBounds(10, 11, 466, 241);
        mensajeArea.setEditable(false);
        mensajeFrame.getContentPane().add(mensajeArea);

        mensajeFrame.setVisible(true);
    }
}
