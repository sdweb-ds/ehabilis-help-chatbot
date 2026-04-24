package es.sdweb.application.model.dal;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import es.sdweb.application.util.GestorParametrosConfiguracion;
import es.sdweb.application.model.dal.FabricaGestorConexiones;
import es.sdweb.application.model.dal.GestorConexionesDataSource;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.exceptions.ExceptionSQL;


/* Crea conexiones a Base de Datos utilizando o DataSource que se lle pase no ficheiro de configuracion. */

public class GestorConexionesDataSource extends FabricaGestorConexiones {

    private static String NOMBRE_DATA_SOURCE=null;
    private final static String PARAMETRO_CONFIGURACION_NOMBRE_DATASOURCE="nombre.datasource";

    static 
    { // Cargamos el nombre del DataSource de manera estatica desde el fichero de configuracion.
    	NOMBRE_DATA_SOURCE = GestorParametrosConfiguracion.getParametro(PARAMETRO_CONFIGURACION_NOMBRE_DATASOURCE);
    	if (NOMBRE_DATA_SOURCE == null) {
    		System.err.println(PARAMETRO_CONFIGURACION_NOMBRE_DATASOURCE + " no especificado.");
    	};
    }

    
    public Connection getConexion() throws ExceptionAccesoDatos{
        Connection c =null;
        try{
        	Context context = new InitialContext();
        	DataSource ds = (DataSource)context.lookup(GestorConexionesDataSource.NOMBRE_DATA_SOURCE);
        	c = ds.getConnection();
        
        }catch (SQLException sqle){
  	  throw new ExceptionSQL(sqle);
  	}catch (NamingException ne){
          System.err.println("GESCUL ERROR: "+ne.toString());
  	  throw new ExceptionAccesoDatos(GestorConexionesDataSource.class.toString(),"No se puede establecer una conexion con la base de datos. Revise la configuracion del DataSource.");
  	  	}
  	  	return c;
    }
  

}//class





