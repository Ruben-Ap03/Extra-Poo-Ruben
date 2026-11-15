package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

public class consultaLibro extends conexion {
    

    public boolean insertar(libro li) {
        Connection con = getConexion();
        String query = "INSERT INTO libro (titulo, idAutor, idEditorial, idGenero, isbn, foto, estatus) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, li.getTitulo());
            ps.setInt(2, li.getIdAutor());
            ps.setInt(3, li.getIdEditorial());
            ps.setInt(4, li.getIdGenero());
            ps.setString(5, li.getIsbn());
            ps.setString(6, li.getFoto());
            ps.setInt(7, li.getEstatus());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar: " + e);
            return false;
        } finally {
            try { con.close(); } catch (SQLException e) { System.err.println(e); }
        }
    }

    public boolean Modificar(libro li) {
    Connection con = getConexion();
    String query = "UPDATE libro SET titulo = ?, idAutor = ?, idEditorial = ?, idGenero = ?, "
            + "isbn = ?, foto = ?, estatus = ? WHERE idLibro = ?";
    try {
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, li.getTitulo());
        ps.setInt(2, li.getIdAutor());
        ps.setInt(3, li.getIdEditorial());
        ps.setInt(4, li.getIdGenero());
        ps.setString(5, li.getIsbn());
        ps.setString(6, li.getFoto());
        ps.setInt(7, li.getEstatus());
        ps.setInt(8, li.getIdLibro());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al modificar: " + e);
        return false;
    } finally {
        try { con.close(); } catch (SQLException e) { System.err.println(e); }
    }
}

    public boolean Buscar(libro li) {
        Connection con = getConexion();
        String query = "SELECT * FROM libro WHERE isbn = ? AND estatus <> 0";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, li.getIsbn());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                li.setIdLibro(rs.getInt("idLibro"));
                li.setTitulo(rs.getString("titulo"));
                li.setIdAutor(rs.getInt("idAutor"));
                li.setIdEditorial(rs.getInt("idEditorial"));
                li.setIdGenero(rs.getInt("idGenero"));
                li.setIsbn(rs.getString("isbn"));
                li.setFoto(rs.getString("foto"));
                li.setEstatus(rs.getInt("estatus"));
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al buscar: " + e);
            return false;
        } finally {
            try { con.close(); } catch (SQLException e) { System.err.println(e); }
        }
    }

    // Eliminación lógica: solo cambia el estatus a 0
    public boolean eliminar(libro li) {
        Connection con = getConexion();
        String query = "UPDATE libro SET estatus = 0 WHERE idLibro = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, li.getIdLibro());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e);
            return false;
        } finally {
            try { con.close(); } catch (SQLException e) { System.err.println(e); }
        }
    }
    
    // true si ya existe OTRO libro activo con ese ISBN
    public boolean existeIsbn(String isbn, int idLibro) {
        Connection con = getConexion();
        String query = "SELECT idLibro FROM libro WHERE isbn = ? AND estatus <> 0 AND idLibro <> ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, isbn);
            ps.setInt(2, idLibro);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error al validar ISBN: " + e);
            return false;
        } finally {
            try { con.close(); } catch (SQLException e) { System.err.println(e); }
        }
    }

    public boolean listar(DefaultTableModel modelo) {
        Connection con = getConexion();
        String query = "SELECT l.idLibro, l.titulo, a.nombre, e.nombre, g.nombre, l.isbn, "
                + "CASE l.estatus WHEN 1 THEN 'Disponible' ELSE 'No hay' END "
                + "FROM libro l "
                + "JOIN autor a ON l.idAutor = a.idAutor "
                + "JOIN editorial e ON l.idEditorial = e.idEditorial "
                + "JOIN genero g ON l.idGenero = g.idGenero "
                + "WHERE l.estatus <> 0";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            modelo.setColumnCount(0);
            modelo.setRowCount(0);
            modelo.addColumn("ID");
            modelo.addColumn("Titulo");
            modelo.addColumn("Autor");
            modelo.addColumn("Editorial");
            modelo.addColumn("Género");
            modelo.addColumn("ISBN");
            modelo.addColumn("Estatus");

            int filas = 0;
            while (rs.next()) {
                Object[] fila = new Object[7];
                    for (int i = 0; i < 7; i++) {
                        fila[i] = rs.getObject(i + 1);
                }
                modelo.addRow(fila);
                filas++;
            }
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al listar: " + e);
            return false;
        } finally {
            try { con.close(); } catch (SQLException e) { System.err.println(e); }
        }
    }

    // Llena un combo con los datos de autor / editorial / genero
    public void llenarCombo(JComboBox<item> combo, String tabla, String colId) {
        Connection con = getConexion();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT " + colId + ", nombre FROM " + tabla);
            ResultSet rs = ps.executeQuery();
            combo.removeAllItems();
            while (rs.next()) {
                combo.addItem(new item(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            System.err.println("Error al llenar combo: " + e);
        } finally {
            try { con.close(); } catch (SQLException e) { System.err.println(e); }
        }
    }
}
