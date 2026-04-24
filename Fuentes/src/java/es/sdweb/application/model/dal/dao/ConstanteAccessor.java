package es.sdweb.application.model.dal.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.model.dal.FabricaGestorConexiones;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.exceptions.ExceptionSQL;
import es.sdweb.application.model.dal.facade.IConstanteDAO;
import es.sdweb.application.model.dal.util.SQLUtil;
import es.sdweb.application.model.dto.ConstanteDTO;
import es.sdweb.application.model.dto.ConstanteDTOList;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaReferenciada;


/**
 * @author Antonio Carro Mariño
 *
 * Clase que gestiona las conexiones y transacciones de la tabla de constantes.
 */
public class ConstanteAccessor implements IConstanteDAO {



	@Override
    public ConstanteDTO create(UsrDTO usrDTO,ConstanteDTO constante) throws ExceptionAccesoDatos,ExceptionInstanciaDuplicada{

		Log.logRT(usrDTO.getUsr(),"BEGIN ConstanteAccessor.create()");

		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();

		boolean rollback=false;
		Connection connection = null;
		ConstanteDTO result=null;

		try{
			Log.logRT(usrDTO.getUsr(),"Obteniendo una conexion...");
			connection = gestorConexiones.getConexion();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false); //Abrimos una transaccion
			Log.logRT(usrDTO.getUsr(),"Llamando al DAO...");
			result = new ConstanteDAO().create(usrDTO, connection, constante);

		}catch (ExceptionInstanciaDuplicada e) {
			rollback = true;
			throw e;
		}catch (ExceptionAccesoDatos e) {
			rollback = true;
			throw e;
		}catch (SQLException e) {
			rollback = true;
			throw new ExceptionSQL(e);
		}catch(RuntimeException e) {
			 rollback = true;
			 throw e;
		}catch(Error e) {
			rollback = true;
			throw e;
		}finally{
			try {
                          SQLUtil.rollbackOrCommitAndCloseConnection(connection, rollback);
			}catch (SQLException e) {
				throw new ExceptionAccesoDatos(UsuarioAccessor.class.getName(),"Error al cerrar la conexion");
			}

		}//finally

		Log.logRT(usrDTO.getUsr(),"END ConstanteAccessor.create()");
		return result;
	}



	@Override
    public void update(UsrDTO usrDTO,ConstanteDTO constante) throws ExceptionAccesoDatos,ExceptionInstanciaNoHallada,ExceptionInstanciaDuplicada{

		Log.logRT(usrDTO.getUsr(),"BEGIN ConstanteAccessor.update()");

		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();

		boolean rollback=false;
		Connection connection = null;

		try{

			Log.logRT(usrDTO.getUsr(),"Obteniendo una conexion...");
			connection = gestorConexiones.getConexion();
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			Log.logRT(usrDTO.getUsr(),"Llamando al DAO...");
			new ConstanteDAO().update(usrDTO,connection,constante);

		}catch (ExceptionInstanciaNoHallada e) {
			rollback = true;
			throw e;
		}catch (ExceptionInstanciaDuplicada e) {
			rollback = true;
			throw e;
		}catch(ExceptionAccesoDatos e) {
                        rollback = true;
                        throw e;
		}catch (SQLException e) {
			rollback = true;
			throw new ExceptionSQL(e);

		}catch(RuntimeException e) {
			 rollback = true;
			 throw e;
		}catch(Error e) {
			rollback = true;
			throw e;

		}finally{
			try {
                          SQLUtil.rollbackOrCommitAndCloseConnection(connection, rollback);
			}catch (SQLException e) {
				throw new ExceptionAccesoDatos(UsuarioAccessor.class.getName(),"Error al cerrar la conexion");
			}
		}
		Log.logRT(usrDTO.getUsr(),"END ConstanteAccessor.update()");
	}


	@Override
    public void updateAll(UsrDTO usrDTO,ConstanteDTOList constantes) throws ExceptionAccesoDatos,ExceptionInstanciaDuplicada, ExceptionInstanciaReferenciada{

		Log.logRT(usrDTO.getUsr(),"BEGIN ConstanteAccessor.updateAll()");

		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();

		boolean rollback=false;
		Connection connection = null;

		try{

			Log.logRT(usrDTO.getUsr(),"Obteniendo una conexion...");
			connection = gestorConexiones.getConexion();
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false); // Iniciamos una transaccion
			Log.logRT(usrDTO.getUsr(),"Llamando al DAO...");
                        ConstanteDAO dao=new ConstanteDAO();
                        for (int i=0;i<constantes.getLista().size();i++){
                          ConstanteDTO cte=(ConstanteDTO)constantes.getLista().get(i);
                          if (cte.getEstado().equals(IConstantes.CFG_JAVASCRIPT_NEW_ITEM_KEY)){
                              dao.create(usrDTO, connection, cte);
                          }else{
                              if (cte.getEstado().equals(IConstantes.CFG_JAVASCRIPT_UPDATE_ITEM_KEY)){
                                  dao.update(usrDTO, connection, cte);
                              }else{
                                  if (cte.getEstado().equals(IConstantes.CFG_JAVASCRIPT_DELETE_ITEM_KEY)){
                                    dao.remove(usrDTO, connection, cte.getIdConstante());
                                  }
                              }
                          }
                        }//for

		}catch (ExceptionInstanciaNoHallada e) { // Si no se halla una instancia durante un update, no hacemos nada
		}catch (ExceptionInstanciaDuplicada e) {
			rollback = true;
			throw e;
		}catch(ExceptionAccesoDatos e) {
                        rollback = true;
                        throw e;
		}catch (SQLException e) {
			rollback = true;
			throw new ExceptionSQL(e);

		}catch(RuntimeException e) {
			 rollback = true;
			 throw e;
		}catch(Error e) {
			rollback = true;
			throw e;

		}finally{
			try {
                          SQLUtil.rollbackOrCommitAndCloseConnection(connection, rollback);
			}catch (SQLException e) {
				throw new ExceptionAccesoDatos(UsuarioAccessor.class.getName(),"Error al cerrar la conexion.");
			}
		}
		Log.logRT(usrDTO.getUsr(),"END ConstanteAccessor.updateAll()");
	}



	@Override
    public List getAllConstantes(UsrDTO usrDTO) throws ExceptionAccesoDatos{

		Log.logRT(usrDTO.getUsr(),"BEGIN ConstanteAccessor.getAllConstantes()");
		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();

		Connection connection = null;
		List result;

		try{
			Log.logRT(usrDTO.getUsr(),"Obteniendo una conexion...");
			connection = gestorConexiones.getConexion();

			Log.logRT(usrDTO.getUsr(),"Llamando al DAO...");
			result = new ConstanteDAO().getAllConstantes(usrDTO,connection);

		}finally{
			SQLUtil.cierraConexion(connection);
		}

		Log.logRT(usrDTO.getUsr(),"END ConstanteAccessor.getAllConstantes()");
		return result;
	}


	@Override
    public ConstanteDTO getConstante(UsrDTO usrDTO, String nombreConstante) throws ExceptionAccesoDatos{
		Log.logRT("BEGIN ConstanteAccessor.getConstante()");
		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();

		Connection connection = null;
		ConstanteDTO result;

		try{
			Log.logRT("Obteniendo una conexion...");
			connection = gestorConexiones.getConexion();

			Log.logRT("Llamando al DAO...");
			result = new ConstanteDAO().getConstante(usrDTO,connection,nombreConstante);

		}finally{
			SQLUtil.cierraConexion(connection);
		}

		Log.logRT("END ConstanteAccessor.getConstante()");
		return result;
	}


	@Override
    public void remove(UsrDTO usrDTO,String idConstante)throws ExceptionAccesoDatos,ExceptionInstanciaNoHallada, ExceptionInstanciaReferenciada{

		Log.logRT(usrDTO.getUsr(),"BEGIN ConstanteAccessor.remove()");
		FabricaGestorConexiones gestorConexiones=FabricaGestorConexiones.getInstance();
		Connection connection = null;

		try{
			Log.logRT(usrDTO.getUsr(),"Obteniendo una conexi�n...");
			connection = gestorConexiones.getConexion();

			Log.logRT(usrDTO.getUsr(),"Llamando al DAO...");
			new ConstanteDAO().remove(usrDTO,connection, idConstante);

		}catch(ExceptionAccesoDatos e) {
			throw e;

		}finally{
			SQLUtil.cierraConexion(connection);
		}

		Log.logRT(usrDTO.getUsr(),"END ConstanteAccessor.remove()");
	}


} //class
