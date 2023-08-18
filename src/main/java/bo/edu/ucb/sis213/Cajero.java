package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Cajero {
    private static int usuarioId;
    private static String usuarioAlias;
    static int usuarioPIN;
    private static double saldo;
    
    public static boolean validarPIN(Connection connection, int pin) {
        String query = "SELECT id, saldo FROM usuarios WHERE pin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                usuarioId = resultSet.getInt("id");
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
                return "DEPOSITO realizado con exito. Su nuevo saldo es: $" + (saldoActual + cantidad);
            }
        } else {
            return "No se encontro el usuario.";
        }
    }
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
                return "RETIRO realizado con exito. Su nuevo saldo es: $" + (saldoActual - cantidad);
            }
        } else {
            return "No se encontro el usuario.";
        }
    }
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

public static int getUsuarioPIN() {
    return usuarioPIN;
}

public static void setUsuarioPIN(int newUsuarioPIN) {
    usuarioId = newUsuarioPIN;
}

    // Resto de métodos de la clase Cajero (consultarSaldo, realizarDeposito, realizarRetiro, cambiarPIN, registrarOperacion)
}