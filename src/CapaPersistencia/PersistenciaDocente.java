/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaPersistencia;

import CapaException.BDException;
import CapaException.DocenteException;
import CapaLogica.Docente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersistenciaDocente {
    
    private static final String SQL_INSERT = "INSERT INTO docente(id, nombre, asignatura) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT id, nombre, asignatura FROM docente WHERE id = ?";

    public void guardarDocente(Docente docente) throws BDException, SQLException, DocenteException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(SQL_INSERT);
            
            ps.setString(1, docente.getId());
            ps.setString(2, docente.getNombre());
            ps.setString(3, docente.getAsignatura());
            
            int resultado = ps.executeUpdate();
            
            if (resultado == 0) {
                 throw new DocenteException("No se pudo registrar el docente.");
            }
            
        } finally {
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
    }
    

    public Docente buscarDocente(String id) throws BDException, SQLException, DocenteException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Docente docente = null;
        
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(SQL_SELECT_BY_ID);
            ps.setString(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                docente = new Docente(
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("asignatura")
                );
            } else {
                 throw new DocenteException("El ID de docente " + id + " no fue encontrado.");
            }
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        
        return docente; 
    }
}