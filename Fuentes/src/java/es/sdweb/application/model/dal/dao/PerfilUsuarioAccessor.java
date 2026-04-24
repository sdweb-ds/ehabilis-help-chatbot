package es.sdweb.application.model.dal.dao;

import java.sql.Connection;
import java.util.List;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.dal.FabricaGestorConexiones;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.facade.IPerfilUsuarioDAO;
import es.sdweb.application.model.dal.util.SQLUtil;
import es.sdweb.application.model.dto.UsrDTO;


/**
 * @author Antonio Carro Mariño
 *
 * DAO que opera sobre la tabla usuario_perfil.
 */

public class PerfilUsuarioAccessor implements IPerfilUsuarioDAO {


	public List perfilesNoAsociadosAUsuario(UsrDTO usr, String codUsuario) throws ExceptionAccesoDatos{

		Log.logRT(usr.getUsr(),"BEGIN PerfilUsuarioAccessor.PerfilesAsociadosAUsuario()");

		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();
		Connection connection = null;
		List result;

		try{
			Log.logRT(usr.getUsr(),"Obteniendo una conexi�n...");
		  	connection = gestorConexiones.getConexion();
			Log.logRT(usr.getUsr(),"Llamando al DAO...");

		  	result=new PerfilUsuarioDAO().perfilesNoAsociadosAUsuario(usr,connection,codUsuario);

		  }finally{

			  SQLUtil.cierraConexion(connection);
		  };

		 Log.logRT(usr.getUsr(),"END PerfilUsuarioAccessor.PerfilesAsociadosAUsuario");
		 return result;
	}



	public List perfilesAsociadosAUsuario(UsrDTO usr, String codUsuario) throws ExceptionAccesoDatos{

		Log.logRT(usr.getUsr(),"BEGIN PerfilUsuarioAccessor.PerfilesAsociadosAUsuario()");

		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();
		Connection connection = null;
		List result;

		try{
			Log.logRT(usr.getUsr(),"Obteniendo una conexi�n...");
		  	connection = gestorConexiones.getConexion();
			Log.logRT(usr.getUsr(),"Llamando al DAO...");

		  	result=new PerfilUsuarioDAO().perfilesAsociadosAUsuario(usr,connection,codUsuario);

		}finally{

			  SQLUtil.cierraConexion(connection);
		  };

		 Log.logRT(usr.getUsr(),"END PerfilUsuarioAccessor.PerfilesAsociadosAUsuario");
		 return result;
	}


} //class
