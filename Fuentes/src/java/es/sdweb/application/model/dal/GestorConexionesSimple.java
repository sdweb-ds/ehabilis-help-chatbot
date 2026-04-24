package es.sdweb.application.model.dal;

import es.sdweb.application.componentes.util.CifradorRijndael;
import es.sdweb.application.util.GestorParametrosConfiguracion;
import es.sdweb.application.model.dal.FabricaGestorConexiones;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.exceptions.ExceptionErrorConexion;

import java.sql.*;

/**
 * @author Antonio Carro Mariño
 *
 * Utiliza conexiones simples a la Base de Datos.
 */

  public class GestorConexionesSimple extends FabricaGestorConexiones{

    GestorConexionesSimple(){}
        
    public Connection getConexion() throws ExceptionAccesoDatos { 
  	  Connection c =null;
        try{    	      	      							
        	String driverClassName = GestorParametrosConfiguracion.getParametro("driverClassName");
  			String driverUrl = GestorParametrosConfiguracion.getParametro("driverUrl");
  			String user = GestorParametrosConfiguracion.getParametro("user");
  			String passwordCifrado = GestorParametrosConfiguracion.getParametro("password");
  			String password = CifradorRijndael.descifrar(passwordCifrado);
  			Class.forName(driverClassName);
  			c = DriverManager.getConnection(driverUrl, user,password); 

  			
        }catch (SQLException sqle){
  		  throw new ExceptionErrorConexion("Error al crear una conexion simple a la BD.", sqle, "GestorConexionesSimple");
  	}catch (ClassNotFoundException e) {
  		  throw new ExceptionAccesoDatos("GestorConexionesSimple","Error al crear una conexion simple a la BD.");
  	}  	 
  	return c;
    }

}//class
