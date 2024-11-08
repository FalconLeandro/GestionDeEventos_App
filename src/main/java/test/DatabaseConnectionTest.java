//package test;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DatabaseConnectionTest {
//
//    // Configura tus parámetros de conexión
//    private static final String URL = "jdbc:mysql://localhost:3306/bd_gestiondeeventosapp";
//    private static final String USER = "root";
//    private static final String PASSWORD = "mysql1034";
//
//    public static void main(String[] args) {
//        try {
//            // Carga el driver JDBC
//            Class.forName("com.mysql.cj.jdbc.Driver");
//
//            // Establece la conexión
//            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//
//            if (connection != null) {
//                System.out.println("Conexión exitosa a la base de datos!");
//            } else {
//                System.out.println("No se pudo conectar a la base de datos.");
//            }
//
//            // Cierra la conexión
//            connection.close();
//        } catch (ClassNotFoundException e) {
//            System.out.println("Driver JDBC no encontrado.");
//            e.printStackTrace();
//        } catch (SQLException e) {
//            System.out.println("Error al conectar con la base de datos.");
//            e.printStackTrace();
//        }
//    }
//}
