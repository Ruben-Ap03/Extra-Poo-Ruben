package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.SQLException;
/**
 *
 * @author CC_Laboratorio
 */
public class conexion {
    private final String BD = "biblioteca";
    private final String USER = "root";
    private final String PASSWORD = "1234567890";
    private final String URL = "jdbc:mysql://localhost:3306/" + BD;
    
    private Connection con;
    
    public Connection getConexion(){
        try{
            this.con = DriverManager.getConnection(this.URL, this.USER, this.PASSWORD);
            System.out.println(con.getCatalog());
        }catch(SQLException e){
            System.err.println(e);
        }
        return con;
    }
    
}

