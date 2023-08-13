package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {

    private static int usuarioId;
    private static double saldo;
    private static int pinActual;

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

    public static int obtenerUsuarioIdPorPin(Connection connection, int pinIngresado) throws SQLException {
    String query = "SELECT id FROM usuarios WHERE pin = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, pinIngresado);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id");
        } else {
            return -1;  // No se encontró un usuario con el PIN ingresado
        }
    }
}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int intentos = 3;

        System.out.println("Bienvenido al Cajero Automático.");
        while (intentos > 0) {
            System.out.print("Ingrese su PIN de 4 dígitos: ");
            int pinIngresado = scanner.nextInt();
            try {
                Connection connection = Conexion.getConnection();
                usuarioId = obtenerUsuarioIdPorPin(connection, pinIngresado);
                if (usuarioId != -1) {
                    mostrarMenu(connection, usuarioId);
                    break;
                } else {
                    intentos--;
                    if (intentos > 0) {
                        System.out.println("PIN incorrecto. Le quedan " + intentos + " intentos.");
                    } else {
                        System.out.println("PIN incorrecto. Ha excedido el número de intentos.");
                        System.exit(0);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            }
        }
    }

    



    public static void mostrarMenu(Connection connection, int usuarioId) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Consultar saldo.");  
            System.out.println("2. Realizar un depósito.");
            System.out.println("3. Realizar un retiro.");
            System.out.println("4. Cambiar PIN.");
            System.out.println("5. Salir.");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    consultarSaldo(connection,usuarioId);
                    break;
                case 2:
                    realizarDeposito(connection,usuarioId);
                    break;
                case 3:
                    realizarRetiro(connection,usuarioId);
                    break;
                case 4:
                    cambiarPIN(connection,usuarioId);
                    break;
                case 5:
                    System.out.println("Gracias por usar el cajero. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    public static void consultarSaldo(Connection connection, int usuarioId) throws SQLException {
    String query = "SELECT nombre, saldo FROM usuarios WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, usuarioId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String nombreUsuario = resultSet.getString("nombre");
            double saldo = resultSet.getDouble("saldo");
            System.out.println("Usuario: " + nombreUsuario);
            System.out.println("Saldo actual: $" + saldo);
        } else {
            System.out.println("No se encontró el usuario.");
        }
    }
}


public static void realizarDeposito(Connection connection, int usuarioId) throws SQLException {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Ingrese la cantidad a retirar: $");
    double cantidad = scanner.nextDouble();

    if (cantidad <= 0) {
        System.out.println("Cantidad no válida.");
        return;
    }

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
                System.out.println("DEPOSITO realizado con éxito. Su nuevo saldo es: $" + (saldoActual + cantidad));
            }
        } else {
            System.out.println("No se encontró el usuario.");
        }
    }
}

public static void realizarRetiro(Connection connection, int usuarioId) throws SQLException {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Ingrese la cantidad a retirar: $");
    double cantidad = scanner.nextDouble();

    if (cantidad <= 0) {
        System.out.println("Cantidad no válida.");
        return;
    }

    // Verificar saldo suficiente
    String saldoQuery = "SELECT saldo FROM usuarios WHERE id = ?";
    try (PreparedStatement saldoStatement = connection.prepareStatement(saldoQuery)) {
        saldoStatement.setInt(1, usuarioId);
        ResultSet saldoResultSet = saldoStatement.executeQuery();
        if (saldoResultSet.next()) {
            double saldoActual = saldoResultSet.getDouble("saldo");
            if (cantidad > saldoActual) {
                System.out.println("Saldo insuficiente.");
                return;
            }

            // Actualizar el saldo en la base de datos
            String updateQuery = "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setDouble(1, cantidad);
                updateStatement.setInt(2, usuarioId);
                updateStatement.executeUpdate();
                registrarOperacion(connection, usuarioId, "RETIRO", cantidad);
                System.out.println("Retiro realizado con éxito. Su nuevo saldo es: $" + (saldoActual - cantidad));
            }
        } else {
            System.out.println("No se encontró el usuario.");
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



    public static void cambiarPIN(Connection connection, int usuarioId) throws SQLException {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Ingrese su PIN actual: ");
    int pinIngresado = scanner.nextInt();

    // Verificar si el PIN ingresado es correcto y obtener el ID del usuario
    String query = "SELECT id FROM usuarios WHERE id = ? AND pin = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, usuarioId);
        preparedStatement.setInt(2, pinIngresado);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            // El PIN es correcto, proceder a cambiarlo
            System.out.print("Ingrese su nuevo PIN: ");
            int nuevoPin = scanner.nextInt();
            System.out.print("Confirme su nuevo PIN: ");
            int confirmacionPin = scanner.nextInt();

            if (nuevoPin == confirmacionPin) {
                // Actualizar el PIN en la base de datos
                String updateQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, nuevoPin);
                    updateStatement.setInt(2, usuarioId);
                    int rowsAffected = updateStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("PIN actualizado con éxito.");
                    } else {
                        System.out.println("No se pudo actualizar el PIN.");
                    }
                }
            } else {
                System.out.println("Los PINs no coinciden.");
            }
        } else {
            System.out.println("PIN incorrecto.");
        }
    }
}
}

