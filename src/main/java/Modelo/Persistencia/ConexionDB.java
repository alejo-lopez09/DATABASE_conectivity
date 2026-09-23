package Modelo.Persistencia;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class ConexionDB {

    private static String url = "";
    private static String user = "";
    private static String password = "";
    public static Connection con = null;

    public static Connection MysConnection() throws SQLException {
        url = "jdbc:mysql://localhost:3306/my_db";
        user = "root";
        password = "supapaxd12A";
        return getConnection(url, user, password);
    }

    private static Connection getConnection(String url, String user, String password) {
        try {
            con = DriverManager.getConnection(url, user, password);
            if (con != null) {
                DatabaseMetaData meta = con.getMetaData();
                System.out.println("Base de datos conectada " + meta.getDriverName());
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return con;
    }

}