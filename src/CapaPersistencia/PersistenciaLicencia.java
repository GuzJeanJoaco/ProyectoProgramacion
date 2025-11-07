/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaPersistencia;

import CapaException.BDException;
import CapaException.LicenciaException;
import CapaLogica.Licencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaLicencia {
    
    // SQL para INSERTAR una Licencia
    // NOTA: Asumo que las fechas vienen en formato String y usamos STR_TO_DATE para MySQL.
    private static final String SQL_INSERT = "INSERT INTO licencia(id, docenteId, fechaInicio, fechaFin, turno, motivo) VALUES (?, ?, STR_TO_DATE(?, '%d/%m/%Y'), STR_TO_DATE(?, '%d/%m/%Y'), ?, ?)";
    
    // SQL para BUSCAR Licencias por ID de Docente
    private static final String SQL_SELECT_BY_DOCENTE = "SELECT id, docenteId, DATE_FORMAT(fechaInicio, '%d/%m/%Y') as fechaInicio, DATE_FORMAT(fechaFin, '%d/%m/%Y') as fechaFin, turno, motivo FROM licencia WHERE docenteId = ?";
    
    /**
     * Guarda una Licencia en la base de datos.
     */
    public void guardarLicencia(Licencia licencia) throws BDException, SQLException, LicenciaException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(SQL_INSERT);
            
            // Mapeo de atributos de la Licencia a la consulta SQL
            ps.setString(1, licencia.getId());
            ps.setString(2, licencia.getDocenteId());
            ps.setString(3, licencia.getFechaInicio()); // La fecha debe ser un String ej: "25/11/2025"
            ps.setString(4, licencia.getFechaFin());
            ps.setString(5, licencia.getTurno());
            ps.setString(6, licencia.getMotivo());
            
            int resultado = ps.executeUpdate();
            
            if (resultado == 0) {
                 throw new LicenciaException("No se pudo registrar la licencia.");
            }
            
        } finally {
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
    }
    
    /**
     * Busca todas las Licencias asociadas a un Docente.
     */
    public List<Licencia> buscarLicenciasPorDocente(String docenteId) throws BDException, SQLException, LicenciaException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Licencia> listaLicencias = new ArrayList<>();
        
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(SQL_SELECT_BY_DOCENTE);
            ps.setString(1, docenteId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Licencia lic = new Licencia();
                lic.setId(rs.getString("id"));
                lic.setDocenteId(rs.getString("docenteId"));
                lic.setFechaInicio(rs.getString("fechaInicio")); 
                lic.setFechaFin(rs.getString("fechaFin"));
                lic.setTurno(rs.getString("turno"));
                lic.setMotivo(rs.getString("motivo"));
                
                listaLicencias.add(lic);
            } 
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        
        if (listaLicencias.isEmpty()) {
            throw new LicenciaException("No se encontraron licencias para el docente ID: " + docenteId);
        }

        return listaLicencias; 
    }
}