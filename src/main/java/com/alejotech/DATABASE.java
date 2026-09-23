package com.alejotech;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DATABASE {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/my_db";
        String usuario = "root";
        String contrasena = "supapaxd12A";
        try {
            Connection connection = DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("CONEXION EXITOSA");
            System.out.println("Controlador: " + connection.getMetaData().getDriverName()
                    + " " + connection.getMetaData().getDriverVersion());
            System.out.println("Base de datos: " + connection.getCatalog());
            connection.close();
        } catch (SQLException e) {
            System.out.println("NO SE PUDO CONECTAR: " + e.getMessage());
        }
    }
}


