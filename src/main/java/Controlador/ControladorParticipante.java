package Controlador;

import Modelo.Clases.Participante;
import Modelo.Persistencia.ConexionDB;
import Modelo.Persistencia.Operaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControladorParticipante {

    public String inscribir(String nombre, String correo, String empresa) {
        if (nombre == null || nombre.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()
                || empresa == null || empresa.trim().isEmpty()) {
            return "Nombre, correo y empresa son obligatorios.";
        }

        Connection con;
        try {
            con = ConexionDB.MysConnection();
        } catch (SQLException ex) {
            return "No se pudo conectar a la base de datos: " + ex.getMessage();
        }
        if (con == null) {
            return "No se pudo conectar a la base de datos.";
        }

        Operaciones.setConnection(con);
        Operaciones.setAutoCommitBD(false);

        try {
            String sqlBusca = "SELECT idparticipante FROM participantes WHERE correo = ?";
            PreparedStatement psBusca = con.prepareStatement(sqlBusca);
            psBusca.setString(1, correo.trim());
            ResultSet rs = Operaciones.consultar_BD(psBusca);

            if (rs != null && rs.next()) {
                Operaciones.rollbackBD();
                return "Ya existe una inscripción con ese correo.";
            }

            String sqlInsert = "INSERT INTO participantes (nombre, correo, empresa) VALUES (?, ?, ?)";
            PreparedStatement psInsert = con.prepareStatement(sqlInsert);
            psInsert.setString(1, nombre.trim());
            psInsert.setString(2, correo.trim());
            psInsert.setString(3, empresa.trim());

            int filas = Operaciones.insertar_actualizar_borrar_BD(psInsert);

            if (filas > 0) {
                Operaciones.commitBD();
                return "Participante inscrito correctamente.";
            } else {
                Operaciones.rollbackBD();
                return "No se pudo inscribir al participante.";
            }
        } catch (SQLException ex) {
            Operaciones.rollbackBD();
            return "Error en la transacción: " + ex.getMessage();
        } finally {
            Operaciones.cerrarConexion();
        }
    }

    public List<Participante> listar() {
        List<Participante> lista = new ArrayList<>();
        Connection con;
        try {
            con = ConexionDB.MysConnection();
        } catch (SQLException ex) {
            System.out.println("Error de conexión: " + ex.getMessage());
            return lista;
        }
        if (con == null) {
            return lista;
        }

        Operaciones.setConnection(con);

        try {
            String sql = "SELECT idparticipante, nombre, correo, empresa FROM participantes";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = Operaciones.consultar_BD(ps);
            if (rs != null) {
                while (rs.next()) {
                    lista.add(new Participante(
                            rs.getInt("idparticipante"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getString("empresa")
                    ));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar: " + ex.getMessage());
        } finally {
            Operaciones.cerrarConexion();
        }
        return lista;
    }

    public List<Participante> buscarPorEmpresa(String empresa) {
        List<Participante> lista = new ArrayList<>();
        Connection con;
        try {
            con = ConexionDB.MysConnection();
        } catch (SQLException ex) {
            System.out.println("Error de conexión: " + ex.getMessage());
            return lista;
        }
        if (con == null) {
            return lista;
        }

        Operaciones.setConnection(con);

        try {
            String sql = "SELECT idparticipante, nombre, correo, empresa FROM participantes WHERE empresa = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, empresa == null ? "" : empresa.trim());
            ResultSet rs = Operaciones.consultar_BD(ps);
            if (rs != null) {
                while (rs.next()) {
                    lista.add(new Participante(
                            rs.getInt("idparticipante"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getString("empresa")
                    ));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar: " + ex.getMessage());
        } finally {
            Operaciones.cerrarConexion();
        }
        return lista;
    }

    public int contar() {
        Connection con;
        try {
            con = ConexionDB.MysConnection();
        } catch (SQLException ex) {
            System.out.println("Error de conexión: " + ex.getMessage());
            return 0;
        }
        if (con == null) {
            return 0;
        }

        Operaciones.setConnection(con);
        int total = 0;
        try {
            String sql = "SELECT COUNT(*) AS total FROM participantes";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = Operaciones.consultar_BD(ps);
            if (rs != null && rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException ex) {
            System.out.println("Error al contar: " + ex.getMessage());
        } finally {
            Operaciones.cerrarConexion();
        }
        return total;
    }

    public String eliminar(int id) {
        Connection con;
        try {
            con = ConexionDB.MysConnection();
        } catch (SQLException ex) {
            return "No se pudo conectar a la base de datos: " + ex.getMessage();
        }
        if (con == null) {
            return "No se pudo conectar a la base de datos.";
        }

        Operaciones.setConnection(con);
        try {
            String sql = "DELETE FROM participantes WHERE idparticipante = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int filas = Operaciones.insertar_actualizar_borrar_BD(ps);
            if (filas > 0) {
                return "Participante eliminado correctamente.";
            } else {
                return "No se encontró ningún participante con ese id.";
            }
        } finally {
            Operaciones.cerrarConexion();
        }
    }
}