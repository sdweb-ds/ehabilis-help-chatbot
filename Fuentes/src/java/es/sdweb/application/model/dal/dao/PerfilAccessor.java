package es.sdweb.application.model.dal.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.dal.FabricaGestorConexiones;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.exceptions.ExceptionEjecucionSQL;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaReferenciada;
import es.sdweb.application.model.dal.exceptions.ExceptionSQL;
import es.sdweb.application.model.dal.facade.IPerfilDAO;
import es.sdweb.application.model.dal.util.SQLUtil;
import es.sdweb.application.model.dto.PerfilDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.util.ExceptionFaltaParametro;

/**
 * @author Antonio Carro Mariño
 *
 * DAO que opera sobre la tabla perfil.
 */
public class PerfilAccessor implements IPerfilDAO{


	public PerfilDTO updatePerfilElementos(UsrDTO usr,String codPerfil,PerfilDTO perfilDTO,List elementoDTOs)
		throws ExceptionEjecucionSQL,ExceptionAccesoDatos, ExceptionInstanciaDuplicada,ExceptionFaltaParametro,ExceptionInstanciaNoHallada {

		boolean rollback=false;
		Log.logRT(usr.getUsr(),"BEGIN PerfilAccesor.updatePerfilElementos()");
		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();
		Connection connection = null;
		PerfilDTO result;

		try{

			Log.logRT(usr.getUsr(),"Obteniendo una conexi�n...");
			connection = gestorConexiones.getConexion();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			Log.logRT(usr.getUsr(),"Llamando al DAO...");
			result = new PerfilDAO().updatePerfilElementos(usr,connection, codPerfil, perfilDTO,elementoDTOs);

		}catch (SQLException e) {
			rollback = true;
			throw new ExceptionSQL(e);
		}catch(ExceptionAccesoDatos e) {
			 rollback = true;
			throw e;
		}catch(RuntimeException e) {
			 rollback = true;
			 throw e;
		}catch(Error e) {
			rollback = true;
			throw e;
		}catch(ExceptionInstanciaDuplicada e) {
			rollback = true;
			throw e;
		}catch(ExceptionInstanciaNoHallada e) {
			rollback = true;
			throw e;
		}finally{
			try {
				if (connection != null) {
					if (rollback) {
						connection.rollback();
					} else {
						connection.commit();
					}
					SQLUtil.cierraConexion(connection);
				}
			} catch (SQLException e) {
				throw new ExceptionAccesoDatos(PerfilAccessor.class.getName(),"error al cerrar la conexion");
			}
		};

		Log.logRT(usr.getUsr(),"END PerfilAccesor.createPerfilElementos()");
		return result;
	}



	public PerfilDTO createPerfilElementos(UsrDTO usr,PerfilDTO perfilDTO,List elementoDTOs)
		throws ExceptionAccesoDatos, ExceptionInstanciaDuplicada, ExceptionFaltaParametro,ExceptionInstanciaNoHallada {

		boolean rollback=false;
		Log.logRT(usr.getUsr(),"BEGIN PerfilAccesor.createPerfilElementos()");
		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();
		Connection connection = null;
		PerfilDTO result;

		try{

			Log.logRT(usr.getUsr(),"Obteniendo una conexi�n...");
			connection = gestorConexiones.getConexion();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			Log.logRT(usr.getUsr(),"Llamando al DAO...");
			result = new PerfilDAO().createPerfilElementos(usr,connection,perfilDTO,elementoDTOs);

		}catch(ExceptionInstanciaDuplicada e) {
			rollback = true;
			throw e;
		}catch(ExceptionFaltaParametro e) {
			rollback = true;
			throw e;
		}catch(ExceptionInstanciaNoHallada e) {
			rollback = true;
			throw e;
		}catch (SQLException e) {
			rollback = true;
			throw new ExceptionSQL(e);
		}catch(ExceptionAccesoDatos e) {
			rollback = true;
			throw e;
		}catch(RuntimeException e) {
			 rollback = true;
			 throw e;
		} catch(Error e) {
			rollback = true;
			throw e;
		} finally{
			try {
				if (connection != null) {
					if (rollback) {
						connection.rollback();
					} else {
						connection.commit();
					}
					SQLUtil.cierraConexion(connection);
				}
			} catch (SQLException e) {
				throw new ExceptionAccesoDatos(PerfilAccessor.class.getName(),"error al cerrar la conexion");
			}
		};

		Log.logRT(usr.getUsr(),"END PerfilAccesor.createPerfilElementos()");
		return result;
	}


	@Override
	public List findAll(UsrDTO usr) throws ExceptionAccesoDatos {

		Log.logRT(usr.getUsr(),"BEGIN PerfilAccesor.findAll()");
		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();
		Connection connection = null;
		List result;

		try{
			Log.logRT(usr.getUsr(),"Obteniendo una conexi�n...");
		  	connection = gestorConexiones.getConexion();
			Log.logRT(usr.getUsr(),"Llamando al DAO...");
			result = new PerfilDAO().findAll(usr,connection);
		  }finally{

			  SQLUtil.cierraConexion(connection);
		  };

		 Log.logRT(usr.getUsr(),"END PerfilAccesor.findAll");
		 return result;
	}

	@Override
	public PerfilDTO findPerfilProfesor(UsrDTO usr,String nombrePerfil) throws  ExceptionAccesoDatos{
		Log.logRT(usr.getUsr(),"BEGIN PerfilAccesor.findPerfilProfesor()");
		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();
		Connection connection = null;
		PerfilDTO result;

		try{
			Log.logRT(usr.getUsr(),"Obteniendo una conexi�n...");
		  	connection = gestorConexiones.getConexion();
			Log.logRT(usr.getUsr(),"Llamando al DAO...");
			result = new PerfilDAO().findPerfilProfesor(usr,connection,nombrePerfil);
		  }finally{

			  SQLUtil.cierraConexion(connection);
		  };

		 Log.logRT(usr.getUsr(),"END PerfilAccesor.findPerfilProfesor");
		 return result;
	}


	@Override
	public void remove(UsrDTO usr, String codPerfil) throws  ExceptionAccesoDatos,ExceptionInstanciaReferenciada {

		Log.logRT(usr.getUsr(),"BEGIN ElementoAccessor.remove()");
		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();
		Connection connection = null;
		boolean perfilTieneUsuarios = false;

		try{
			Log.logRT(usr.getUsr(),"Obteniendo una conexi�n...");
		  	connection = gestorConexiones.getConexion();
			Log.logRT(usr.getUsr(),"Llamando al DAO...");
			perfilTieneUsuarios = new PerfilUsuarioDAO().perfilTieneUsuarios(usr,connection,codPerfil);
			//Si el perfil tiene todav�a usuarios asociados no podr� borrarse hasta que no se eliminen dichos usuarios
			if (!perfilTieneUsuarios){
				new PerfilDAO().remove(usr,connection,codPerfil);
			}
			else{
				throw new ExceptionInstanciaReferenciada(codPerfil,PerfilAccessor.class.getName());
			}

		}finally{
			SQLUtil.cierraConexion(connection);
		};

		Log.logRT(usr.getUsr(),"END ElementoAccessor.remove()");
	}


}//class
