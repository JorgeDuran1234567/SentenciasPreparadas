
package com.jorge.conexionbd;

import static com.jorge.conexionbd.ConexionBD.DB_URL;
import static com.jorge.conexionbd.ConexionBD.PASS;
import static com.jorge.conexionbd.ConexionBD.QUERY;
import static com.jorge.conexionbd.ConexionBD.USER;
import static com.jorge.conexionbd.ConexionBD.eliminarRegistro;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 *
 * @author jorge
 */
public class ConexionBD2 {
   static final String DB_URL = "jdbc:mysql://localhost:3306/jcvd";
   static final String USER = "dam";
   static final String PASS = "dam";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        
            //buscaNombre
        System.out.println("Introduce el nombre de un juego: ");
        String n=sc.nextLine();
        boolean result= buscaNombre(n);
        System.out.println(result);
        
            //lanzaConsulta
        /*String consulta="SELECT FechaLanzamiento FROM videojuegos";
        lanzaConsulta(consulta);*/
        
            //nuevoRegistro
        /*Date fecha = Date.valueOf("2017-03-03");
        nuevoRegistro("The Legend of Zelda: Breath of the Wild", "Aventura", fecha, "Nintendo", 70);
        */
            
            //nuevoRegistro
        //nuevoRegistro();
        
            //eliminarRegistro
        /*String juegoAEliminar = "The Legend of Zelda: Breath of the Wild";
        boolean eliminar = eliminarRegistro(juegoAEliminar);

        if (eliminar) {
            System.out.println("Videojuego eliminado exitosamente.");
        } else {
            System.out.println("No se pudo eliminar el videojuego o no existe en la base de datos.");
        }*/
    }
    
    public static boolean buscaNombre(String nombre){
        boolean nom = false;
        String consulta = "SELECT Nombre FROM Videojuegos WHERE nombre = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement pstmt = conn.prepareStatement(consulta)) {

            pstmt.setString(1, nombre);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Extract data from result set
                while (rs.next()) {
                    // Retrieve by column name
                    String nombreJuego = rs.getString("nombre");
                    if (nombreJuego.equals(nombre)) {
                        nom = true;
                    }
                }
            }
        } catch (SQLException e) {
        e.printStackTrace();
        }

        System.out.println(nom);
        return nom;
    }
    
    public static void lanzaConsulta(String consulta){
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consulta);) {

            while (rs.next()) {
                System.out.println("Resultado: " + rs.getString("FechaLanzamiento"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void nuevoRegistro(String nombre, String genero, Date fechaLanzamiento, String compañia, int precio) {
        String consulta = "INSERT INTO videojuegos (Nombre, Genero, FechaLanzamiento, Compañia, Precio) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = conn.prepareStatement(consulta)) {

            // Establecer los valores de los parámetros
            statement.setString(1, nombre);
            statement.setString(2, genero);
            statement.setDate(3, fechaLanzamiento);
            statement.setString(4, compañia);
            statement.setInt(5, precio);

            // Ejecutar la consulta
            int filasAfectadas = statement.executeUpdate();

            // Verificar si se ha insertado correctamente
            if (filasAfectadas > 0) {
                System.out.println("Nuevo videojuego creado exitosamente.");
            } else {
                System.out.println("No se pudo insertar el nuevo videojuego.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void nuevoRegistro() {
        String consulta = "INSERT INTO videojuegos VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = conn.prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Establecer los valores de los parámetros
            statement.setInt(1, 22);
            statement.setString(2, "Juan");
            statement.setString(3, "Perez");

            ResultSet rs=statement.getGeneratedKeys();
            
            while(rs.next()){
                int claveGenerada=rs.getInt(1);
                System.out.println("Clave generada= "+claveGenerada);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
    
    public static boolean eliminarRegistro(String nombreVideojuego) {
        String consulta = "DELETE FROM videojuegos WHERE Nombre = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = conn.prepareStatement(consulta)) {

            // Establecer el valor del parámetro
            statement.setString(1, nombreVideojuego);

            // Ejecutar la consulta
            int filasAfectadas = statement.executeUpdate();

            // Verificar si se ha eliminado correctamente
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si ocurre una excepción, asumimos que la eliminación no fue exitosa
        }
    }
    
}
