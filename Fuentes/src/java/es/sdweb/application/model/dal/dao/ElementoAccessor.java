package es.sdweb.application.model.dal.dao;

import java.sql.Connection;
import java.util.List;

import es.sdweb.application.model.dal.facade.IElementoDAO;
import es.sdweb.application.model.dal.FabricaGestorConexiones;
import es.sdweb.application.model.dal.util.SQLUtil;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.dal.dao.ElementoDAO;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.exceptions.ExceptionEjecucionSQL;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;

/**
 * @author Antonio Carro Mariño
 *
 * DAO que opera sobre la tabla Elemento.
 */

public class ElementoAccessor implements IElementoDAO{
	
	
	public List permisos(UsrDTO usr ,String login )throws ExceptionInstanciaNoHallada,ExceptionAccesoDatos{

		Log.logRT(usr.getUsr(),"BEGIN ElementoAccessor.findHijos()");
		
		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();	
		Connection connection = null;
		List result;
		
		try{
			Log.logRT(usr.getUsr(),"Obteniendo una conexi�n...");
		  	connection = gestorConexiones.getConexion();
			Log.logRT(usr.getUsr(),"Llamando al DAO...");
			result=new ElementoDAO().permisos(usr,login,connection);		
		  }finally{
		  	
			  SQLUtil.cierraConexion(connection);
		  }; 
		
		 Log.logRT(usr.getUsr(),"END ElementoAccessor.findHijos()");
		 return result;
		
	}	
	
	public List findArbol(UsrDTO usrDTO,String login,String tipo)
		throws ExceptionInstanciaNoHallada, ExceptionAccesoDatos{
	
		Log.logRT(usrDTO.getUsr(),"BEGIN ElementoAccessor.findArbol()");

		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();	
		Connection connection = null;
		List result;

		try{
			Log.logRT(usrDTO.getUsr(),"Obteniendo una conexi�n...");
		  	connection = gestorConexiones.getConexion();
			Log.logRT(usrDTO.getUsr(),"Llamando al DAO...");
			result=new ElementoDAO().findArbol(usrDTO,login,tipo,connection);		
		  }finally{
		  	
			SQLUtil.cierraConexion(connection);
		  }; 

		 Log.logRT(usrDTO.getUsr(),"END ElementoAccessor.findArbol()");
		 
		 return result;

		}
	
	
	public List findArbolCheckPerfil(UsrDTO usrDTO,String codPerfil)
		throws ExceptionInstanciaNoHallada,ExceptionEjecucionSQL, ExceptionAccesoDatos{
		Log.logRT(usrDTO.getUsr(),"BEGIN ElementoAccessor.findArbolCheckPerfil()");

		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();	
		Connection connection = null;
		List result;

		try{
			Log.logRT(usrDTO.getUsr(),"Obteniendo una conexi�n...");
		  	connection = gestorConexiones.getConexion();
			Log.logRT(usrDTO.getUsr(),"Llamando al DAO...");
			result=new ElementoDAO().findArbolCheckPerfil(usrDTO,codPerfil,connection);		
		  }finally{
		  	
			  SQLUtil.cierraConexion(connection);
		  }; 

		 Log.logRT(usrDTO.getUsr(),"END ElementoAccessor.findArbolCheckPerfil()");
		 
		 return result;

		}
	

}//class
