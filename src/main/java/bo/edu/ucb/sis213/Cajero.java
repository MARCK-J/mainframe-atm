package bo.edu.ucb.sis213;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Cajero {
    private static int usuarioId;
    private static String usuarioAlias;
    private static double saldo;
    
    public static boolean validarPIN(Connection connection, int pin) {
        String query = "SELECT id, saldo FROM usuarios WHERE pin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                usuarioAlias = resultSet.getString("id");
                saldo = resultSet.getDouble("saldo");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int obtenerUsuarioIdPorPinYAlias(Connection connection, String aliasIngresado, int pinIngresado) throws SQLException {
        String query = "SELECT id FROM usuarios WHERE pin = ? AND alias = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pinIngresado);
            preparedStatement.setString(2, aliasIngresado);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return -1;  // No se encontró un usuario con el PIN y alias ingresados
            }
        }
    }

    public static int obtenerUsuarioIdPorPin(Connection connection, String aliasIngresado, int pinIngresado) throws SQLException {
    String query = "SELECT id FROM usuarios WHERE pin = ? AND alias = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pinIngresado);
            preparedStatement.setString(2, aliasIngresado);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return -1;  // No se encontró un usuario con el PIN y alias ingresados
            }
        }
}

public static String consultarSaldo(Connection connection, int usuarioId) throws SQLException {
    String query = "SELECT nombre, saldo FROM usuarios WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, usuarioId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String nombreUsuario = resultSet.getString("nombre");
            double saldo = resultSet.getDouble("saldo");
            return "Usuario: " + nombreUsuario + "\nSaldo actual: $" + saldo;
        } else {
            return "No se encontro el usuario.";
        }
    }
}

public static void realizarDeposito(Connection connection, int usuarioId) throws SQLException {
    JFrame depositoFrame = new JFrame("Realizar Depósito");
    depositoFrame.setSize(300, 220);
    depositoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    depositoFrame.getContentPane().setLayout(null);

    JLabel cantidadLabel = new JLabel("Ingrese la cantidad a depositar:");
    cantidadLabel.setBounds(10, 10, 200, 20);
    depositoFrame.getContentPane().add(cantidadLabel);

    JTextField cantidadTextField = new JTextField();
    cantidadTextField.setBounds(10, 35, 100, 20);
    depositoFrame.getContentPane().add(cantidadTextField);

    JButton depositarButton = new JButton("Depositar");
    depositarButton.setBounds(160, 155, 120, 25);
    depositarButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            double cantidad = Double.parseDouble(cantidadTextField.getText());

            if (cantidad <= 0) {
                Menu.mostrarMensaje("Depósito", "Cantidad no válida.");
            } else {
                try {
                    String depositoMessage = realizarDepositoOperacion(connection, usuarioId, cantidad);
                    Menu.mostrarMensaje("Depósito", depositoMessage);
                    depositoFrame.dispose();
                } catch (SQLException ex) {
                    Menu.mostrarMensaje("Error", "Error al realizar el depósito: " + ex.getMessage());
                }
            }
        }
    });
    depositoFrame.getContentPane().add(depositarButton);

    depositoFrame.setVisible(true);
}

public static String realizarDepositoOperacion(Connection connection, int usuarioId, double cantidad) throws SQLException {
    // Verificar saldo suficiente
    String saldoQuery = "SELECT saldo FROM usuarios WHERE id = ?";
    try (PreparedStatement saldoStatement = connection.prepareStatement(saldoQuery)) {
        saldoStatement.setInt(1, usuarioId);
        ResultSet saldoResultSet = saldoStatement.executeQuery();
        if (saldoResultSet.next()) {
            double saldoActual = saldoResultSet.getDouble("saldo");

            // Actualizar el saldo en la base de datos
            String updateQuery = "UPDATE usuarios SET saldo = saldo + ? WHERE id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setDouble(1, cantidad);
                updateStatement.setInt(2, usuarioId);
                updateStatement.executeUpdate();
                registrarOperacion(connection, usuarioId, "DEPOSITO", cantidad);
                return "DEPOSITO realizado con éxito. Su nuevo saldo es: $" + (saldoActual + cantidad);
            }
        } else {
            return "No se encontró el usuario.";
        }
    }
}

public static void realizarRetiro(Connection connection, int usuarioId) throws SQLException {
    JFrame depositoFrame = new JFrame("Realizar Retiro");
    depositoFrame.setSize(300, 220);
    depositoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    depositoFrame.getContentPane().setLayout(null);

    JLabel cantidadLabel = new JLabel("Ingrese la cantidad a depositar:");
    cantidadLabel.setBounds(10, 10, 200, 20);
    depositoFrame.getContentPane().add(cantidadLabel);

    JTextField cantidadTextField = new JTextField();
    cantidadTextField.setBounds(10, 35, 100, 20);
    depositoFrame.getContentPane().add(cantidadTextField);

    JButton depositarButton = new JButton("retiro");
    depositarButton.setBounds(160, 155, 120, 25);
    depositarButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            double cantidad = Double.parseDouble(cantidadTextField.getText());

            if (cantidad <= 0) {
                Menu.mostrarMensaje("Retiro", "Cantidad no válida.");
            } else {
                try {
                    String depositoMessage = realizarRetiroOperacion(connection, usuarioId, cantidad);
                    Menu.mostrarMensaje("Retiro", depositoMessage);
                    depositoFrame.dispose();
                } catch (SQLException ex) {
                    Menu.mostrarMensaje("Error", "Error al realizar el retiro: " + ex.getMessage());
                }
            }
        }
    });
    depositoFrame.getContentPane().add(depositarButton);

    depositoFrame.setVisible(true);
}

public static String realizarRetiroOperacion(Connection connection, int usuarioId, double cantidad) throws SQLException {
    // Verificar saldo suficiente
    String saldoQuery = "SELECT saldo FROM usuarios WHERE id = ?";
    try (PreparedStatement saldoStatement = connection.prepareStatement(saldoQuery)) {
        saldoStatement.setInt(1, usuarioId);
        ResultSet saldoResultSet = saldoStatement.executeQuery();
        if (saldoResultSet.next()) {
            double saldoActual = saldoResultSet.getDouble("saldo");

            // Actualizar el saldo en la base de datos
            String updateQuery = "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setDouble(1, cantidad);
                updateStatement.setInt(2, usuarioId);
                updateStatement.executeUpdate();
                registrarOperacion(connection, usuarioId, "RETIRO", cantidad);
                return "RETIRO realizado con éxito. Su nuevo saldo es: $" + (saldoActual - cantidad);
            }
        } else {
            return "No se encontró el usuario.";
        }
    }
}

public static void cambiarPIN(Connection connection, int usuarioId) throws SQLException {
    JFrame cambiarPINFrame = new JFrame("Cambiar PIN");
    cambiarPINFrame.setSize(300, 220);
    cambiarPINFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    cambiarPINFrame.getContentPane().setLayout(null);

    JLabel actualPinLabel = new JLabel("Ingrese su PIN actual:");
    actualPinLabel.setBounds(10, 10, 180, 20);
    cambiarPINFrame.getContentPane().add(actualPinLabel);

    JTextField actualPinTextField = new JTextField();
    actualPinTextField.setBounds(10, 35, 100, 20);
    cambiarPINFrame.getContentPane().add(actualPinTextField);

    JLabel nuevoPinLabel = new JLabel("Ingrese su nuevo PIN:");
    nuevoPinLabel.setBounds(10, 70, 180, 20);
    cambiarPINFrame.getContentPane().add(nuevoPinLabel);

    JTextField nuevoPinTextField = new JTextField();
    nuevoPinTextField.setBounds(10, 95, 100, 20);
    cambiarPINFrame.getContentPane().add(nuevoPinTextField);

    JLabel confirmarPinLabel = new JLabel("Confirme su nuevo PIN:");
    confirmarPinLabel.setBounds(10, 130, 180, 20);
    cambiarPINFrame.getContentPane().add(confirmarPinLabel);

    JTextField confirmarPinTextField = new JTextField();
    confirmarPinTextField.setBounds(10, 155, 100, 20);
    cambiarPINFrame.getContentPane().add(confirmarPinTextField);

    JButton cambiarButton = new JButton("Cambiar PIN");
    cambiarButton.setBounds(160, 155, 120, 25);
    cambiarButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int actualPin = Integer.parseInt(actualPinTextField.getText());
            int nuevoPin = Integer.parseInt(nuevoPinTextField.getText());
            int confirmarPin = Integer.parseInt(confirmarPinTextField.getText());

            if (actualPin == usuarioId) {
                if (nuevoPin == confirmarPin) {
                    String updateQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setInt(1, nuevoPin);
                        updateStatement.setInt(2, usuarioId);
                        int rowsAffected = updateStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            Menu.mostrarMensaje("Cambio de PIN", "PIN actualizado con éxito.");
                            cambiarPINFrame.dispose();
                        } else {
                            Menu.mostrarMensaje("Cambio de PIN", "No se pudo actualizar el PIN.");
                        }
                    } catch (SQLException ex) {
                        Menu.mostrarMensaje("Error", "Error al cambiar el PIN: " + ex.getMessage());
                    }
                } else {
                    Menu.mostrarMensaje("Cambio de PIN", "Los PINs no coinciden.");
                }
            } else {
                Menu.mostrarMensaje("Cambio de PIN", "PIN actual incorrecto.");
            }
        }
    });
    cambiarPINFrame.getContentPane().add(cambiarButton);

    cambiarPINFrame.setVisible(true);
}

public static void registrarOperacion(Connection connection, int usuarioId, String tipoOperacion, double cantidad) throws SQLException {
    String insertQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
        insertStatement.setInt(1, usuarioId);
        insertStatement.setString(2, tipoOperacion);
        insertStatement.setDouble(3, cantidad);
        insertStatement.executeUpdate();
    }
}

public static int getUsuarioId() {
    return usuarioId;
}

public static void setUsuarioId(int newUsuarioId) {
    usuarioId = newUsuarioId;
}

public static String getUsuarioAlias() {
    return usuarioAlias;
}

public static void setUsuarioAlias(int newUsuarioAlias) {
    usuarioId = newUsuarioAlias;
}

    // Resto de métodos de la clase Cajero (consultarSaldo, realizarDeposito, realizarRetiro, cambiarPIN, registrarOperacion)
}