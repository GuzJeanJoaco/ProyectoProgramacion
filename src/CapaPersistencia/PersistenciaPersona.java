/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CapaPersistencia;

import CapaException.BDException;
import CapaException.LoginException; 
import CapaLogica.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersistenciaPersona {

    private static final String SQL_INSERT = "INSERT INTO usuario(usuario, contrasena) VALUES (?, ?)";
    private static final String SQL_SELECT = "SELECT usuario FROM usuario WHERE usuario = ? AND contrasena = ?";


    public void registrarUsuario(Usuario user) throws BDException, SQLException, LoginException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(SQL_INSERT);

            ps.setString(1, user.getNombreUsuario());
            ps.setString(2, user.getContrasena());

            int resultado = ps.executeUpdate();

            if (resultado == 0) {
                throw new LoginException("No se pudo registrar el usuario. El nombre de usuario ya existe.");
            }

        } finally {
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
    }


    public boolean iniciarSesion(String usuario, String contrasena) throws BDException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConnection();
            ps = con.prepareStatement(SQL_SELECT);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            rs = ps.executeQuery();

            return rs.next(); 

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
    }
}