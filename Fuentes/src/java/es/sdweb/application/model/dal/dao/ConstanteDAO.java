package es.sdweb.application.model.dal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.exceptions.ExceptionEjecucionSQL;
import es.sdweb.application.model.dal.util.SQLUtil;
import es.sdweb.application.model.dto.ConstanteDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaReferenciada;
import es.sdweb.application.util.GestorParametrosConfiguracion;

/**
 * @author Antonio Carro Mariño
 *
 * DAO que opera sobre la tabla de constantes.
 */
public class ConstanteDAO {


	/**
	  * Inserta una Constante en la base de datos.
	  * @param user Usuario que realiza el acceso.
	  * @param connection Una conexion con la base de datos.
	  * @param constante Constante que se desea insertar en la base de datos.
	  * @return ConstanteDTO devuelve la Constantes insertadas con el idConstante asignado.
	  * @throws ExceptionAccesoDatos Cualquier Error de acceso a Base de datos.
	  * @throws ExceptionInstanciaDuplicada Si el Nivel ya existe en la Base de datos.
	  */

	public ConstanteDTO create(UsrDTO usrDTO,Connection connection,ConstanteDTO constante) throws ExceptionAccesoDatos,ExceptionInstanciaDuplicada{


	Log.logRT(usrDTO.getUsr(),"BEGIN ConstanteDAO.create()");

		int insertedRows;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatementID = null;
		ResultSet resultSetID = null;
		String CODIGO_ERROR_PKV = "primary.key.violation";

		try{

			String queryStringID = "SELECT MAX(id_constante)  FROM constante ";
			preparedStatementID = connection.prepareStatement(queryStringID);
			resultSetID = preparedStatementID.executeQuery();
			resultSetID.next();
			int max = resultSetID.getInt(1);

			String queryString="INSERT INTO constante (id_constante,nombre,valor,descripcion) VALUES (?,?,?,?)";

			Log.logRT(usrDTO.getUsr(),StringUtil.cat("SQL: ",queryString));
			preparedStatement = connection.prepareStatement(queryString);

			/* Rellenar "PreparedStatement" */
			int i = 1;
			SQLUtil.setInteger(preparedStatement,i++,max+1);
			SQLUtil.setString(preparedStatement,i++,constante.getNombre().trim());
			SQLUtil.setString(preparedStatement,i++,constante.getValor().trim());
			SQLUtil.setString(preparedStatement,i++,constante.getDescripcion().trim());
			insertedRows = preparedStatement.executeUpdate();
			Log.logRT(usrDTO.getUsr(),"Ejecutando la SQL...");

			if (insertedRows == 0) {
				Log.logRT(usrDTO.getUsr(),"ERROR: insertedRows = 0");
				Log.logRT(usrDTO.getUsr(),"END ConstanteDAO.create()");
				throw new ExceptionEjecucionSQL(null,queryString,ConstanteDAO.class.getName(),"create",1);
			}

			constante.setIdConstante(String.valueOf(max+1));

			resultSetID.close();

			Log.logRT(usrDTO.getUsr(),"END ConstanteDAO.create()");


			return constante;

		}catch (SQLException e){
			int codigoError = Integer.valueOf(GestorParametrosConfiguracion.getParametro(CODIGO_ERROR_PKV)).intValue();
			if (e.getErrorCode() == codigoError){
				Log.logRE(usrDTO.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_NORMAL,e.getErrorCode(),
						 "Error en la insercion de constante:"+e.getMessage());
				throw new ExceptionInstanciaDuplicada(constante.getIdConstante(),ConstanteDAO.class.getName());
			}else{
				Log.logRE(usrDTO.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_NORMAL,e.getErrorCode(),
						 "Error en la insercion de constante:"+e.getMessage());
				throw new ExceptionEjecucionSQL( e,ConstanteDAO.class.getName());
			}
		}finally{
			SQLUtil.cierraPreparedStatement(preparedStatement);
			SQLUtil.cierraPreparedStatement(preparedStatementID);
		}
	}

	/**
	  * Modifica una Constante en la base de datos.
	  * @param user Usuario que realiza el acceso.
	  * @param constante Constante que se desea modificar en la base de datos.
	  * @throws ExceptionAccesoDatos Cualquier Error de acceso a Base de datos.
	  * @throws ExceptionInstanciaDuplicada Si la constante ya existe en la Base de datos.
	  */
	public void update(UsrDTO usrDTO,Connection connection,ConstanteDTO constante)
	                                  throws ExceptionAccesoDatos,ExceptionInstanciaNoHallada,ExceptionInstanciaDuplicada {

	Log.logRT(usrDTO.getUsr(),"BEGIN ConstanteDAO.update()");

		int updatedRows;
		PreparedStatement preparedStatement = null;
		String queryString = "";

		try{

			queryString = "UPDATE constante " +
			"SET nombre=?,valor=?,descripcion=? " +
			" WHERE id_constante=?";

			Log.logRT(usrDTO.getUsr(),StringUtil.cat("SQL: ",queryString));

			preparedStatement = connection.prepareStatement(queryString);

			/* Rellenar "PreparedStatement" */
			int i = 1;

			SQLUtil.setString(preparedStatement,i++,constante.getNombre().trim());
			SQLUtil.setString(preparedStatement,i++,constante.getValor().trim());
			SQLUtil.setString(preparedStatement,i++,constante.getDescripcion().trim());
			SQLUtil.setString(preparedStatement,i++,constante.getIdConstante().trim());
			Log.logRT(usrDTO.getUsr(),"Ejecutando la SQL...");
			updatedRows = preparedStatement.executeUpdate();


			if (updatedRows == 0) {
				throw new ExceptionInstanciaNoHallada(String.valueOf(constante.getIdConstante()),ConstanteDAO.class.getName());
			}

			Log.logRT(usrDTO.getUsr(),"END ConstanteDAO.update()");

		}catch (SQLException e){
			int codigoError = Integer.valueOf(GestorParametrosConfiguracion.getParametro("constraint.key.violation")).intValue();
			Log.logRE(usrDTO.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_NORMAL,e.getErrorCode(),
						  "Error en la modificacion de constante:"+e.getMessage());
			throw new ExceptionEjecucionSQL(e,queryString,ConstanteDAO.class.getName(),"update()",e.getErrorCode());
		}finally{
		  SQLUtil.cierraPreparedStatement(preparedStatement);
		}
	}


	/**
	  * Comprueba la existencia de una Constante en la base de datos con el mismo nombre
	  * que el objeto ConstanteDTO que recibe como parametro.
	  * @param user Usuario que realiza el acceso.
	  * @param connection Una conexion con la base de datos.*
	  * @param constante Objeto a comprobar.
	  * @return boolean true si existe y false en caso contrario.
	  * @throws ExceptionAccesoDatos Cualquier Error de acceso a Base de datos.
	  */

	public boolean exists(UsrDTO usr,Connection connection,ConstanteDTO constante) throws ExceptionAccesoDatos{

		Log.logRT(usr.getUsr(),"BEGIN ConstanteDAO.exists()");

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		/* Create "preparedStatement". */
		String queryString = "SELECT nombre FROM constante WHERE nombre = ?";

		try {

			Log.logRT(usr.getUsr(),StringUtil.cat("SQL: ",queryString,"; @nombre=",constante.getNombre()));
			preparedStatement = connection.prepareStatement(queryString);
			int i = 1;
			preparedStatement.setString(i++,constante.getNombre().trim().toUpperCase());

			Log.logRT(usr.getUsr(),"Ejecutando la SQL...");
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				 Log.logRT(usr.getUsr(),StringUtil.cat("La constante ",constante.getNombre()," existe."));
				 Log.logRT(usr.getUsr(),"END ConstanteDAO.exists()");
				return true;
			}
			else {
				 Log.logRT(usr.getUsr(),StringUtil.cat("La constante ",constante.getNombre()," NO existe."));
				 Log.logRT(usr.getUsr(),"END ConstanteDAO.exists()");
				return false;
			}


		} catch (SQLException e) {
			throw new ExceptionEjecucionSQL(e,queryString,ConstanteDAO.class.getName(),"exists",e.getErrorCode());
		} finally {
		  SQLUtil.cierraPreparedStatement(preparedStatement);
		}
	}


	/**
	  * Devuelve todas las constantes existentes en la base de datos ordenadas por nombre.
	  * @param user Usuario que realiza el acceso.
	  * @param connection La conexion.
	  * @return List lista conteniendo todas las constantes o lista vacia en caso de no haber ninguna.
	  * @throws ExceptionAccesoDatos Cualquier Error de acceso a base de datos.
	  */

	public List getAllConstantes(UsrDTO usr,Connection connection) throws  ExceptionEjecucionSQL {

		Log.logRT(usr.getUsr(),"BEGIN ConstanteDAO.getAllConstantes()");

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List resultDTOs = new ArrayList();

		String queryString = "SELECT id_constante,nombre,valor,descripcion FROM constante ORDER BY nombre";

		try {

			preparedStatement = connection.prepareStatement(queryString,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			/* Execute query. */
			Log.logRT(usr.getUsr(),"Ejecutando la SQL...");
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()){
				int i = 1;
				String idConstante =StringUtil.trim(resultSet.getString(i++)); //Obtenemos el entero como String
				String nombre = StringUtil.trim(resultSet.getString(i++));
				String valor = StringUtil.trim((resultSet.getString(i++)));
				String descripcion = StringUtil.trim(resultSet.getString(i++));
				ConstanteDTO dto = new ConstanteDTO(idConstante,nombre,valor,descripcion);
				resultDTOs.add(dto);
			}

		    Log.logRT(usr.getUsr(),"END ConstanteDAO.getAllConstantes()");
			return resultDTOs;

		} catch (SQLException e) {
			throw new ExceptionEjecucionSQL(e,queryString,ConstanteDAO.class.getName(),"getAllConstantes()",e.getErrorCode());
		} finally {
		  SQLUtil.cierraPreparedStatement(preparedStatement);
		}
	}


	/**
	  * Devuelve una constante a partir de su nombre.
	  * @param user Usuario que realiza el acceso.
	  * @param connection La conexion.
	  * @param nombreConstante Nombre de la constante buscada.
	  * @return ConstanteDTO Constante buscada, o null si no se encuentra.
	  * @throws ExceptionAccesoDatos Cualquier Error de acceso a base de datos.
	  */

	public ConstanteDTO getConstante(UsrDTO usr,Connection connection,String nombreConstante) throws  ExceptionEjecucionSQL {

		Log.logRT("BEGIN ConstanteDAO.getConstante()");

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ConstanteDTO resultDTO = null;

		String queryString = "SELECT id_constante,nombre,valor,descripcion FROM constante WHERE nombre='"+nombreConstante+"' ORDER BY nombre";

		try {

			preparedStatement = connection.prepareStatement(queryString,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			/* Execute query. */
			Log.logRT("Ejecutando la SQL...");
			resultSet = preparedStatement.executeQuery();

			while(resultSet.next()){
				int i = 1;
				String idConstante =StringUtil.trim(resultSet.getString(i++)); //Obtenemos el entero como String
				String nombre = StringUtil.trim(resultSet.getString(i++));
				String valor = StringUtil.trim((resultSet.getString(i++)));
				String descripcion = StringUtil.trim(resultSet.getString(i++));
				ConstanteDTO dto = new ConstanteDTO(idConstante,nombre,valor,descripcion);
				resultDTO=dto;
			}

                        Log.logRT("END ConstanteDAO.getConstante()");
			return resultDTO;

		} catch (SQLException e) {
			Log.logRT("ERROR: "+e.getMessage());
			throw new ExceptionEjecucionSQL(e,queryString,ConstanteDAO.class.getName(),"getConstante()",e.getErrorCode());
		} finally {
		  SQLUtil.cierraPreparedStatement(preparedStatement);
		}
	}


	/**
	  * Elimina una constante de la base de datos.
	  * @param user Usuario que realiza el acceso.
	  * @param idConstante Borra esta constante de la base de datos.
	  * @throws ExceptionInstanciaNoHallada No exite la constante en la base de datos.
	  * @throws ExceptionAccesoDatos Cualquier Error de acceso a base de datos.
	  * @throws ExceptionInstanciaReferenciada La instancia se encuentra referenciada por otros datos de la BBDD, por tanto no puede ser eliminada.
	  */

	public void remove(UsrDTO usr,Connection connection, String idConstante)
		throws  ExceptionEjecucionSQL, ExceptionInstanciaNoHallada,ExceptionInstanciaReferenciada {

		Log.logRT(usr.getUsr(),"BEGIN ConstanteDAO.remove()");


		PreparedStatement preparedStatement = null;
		String queryString = "DELETE FROM constante WHERE id_constante = ?";
		try {

			Log.logRT(usr.getUsr(),StringUtil.cat("SQL: ",queryString,"; @id_constante=",idConstante));
			preparedStatement = connection.prepareStatement(queryString);

			int i = 1;
                        SQLUtil.setInteger(preparedStatement, i++, Integer.parseInt(idConstante));

			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0)  {
				throw new ExceptionInstanciaNoHallada(idConstante,ConstanteDAO.class.getName());
			}

			Log.logRT(usr.getUsr(),"END ConstanteDAO.remove()");
		}
		catch (SQLException e) {
			int codigoError = Integer.valueOf(GestorParametrosConfiguracion.getParametro("foreign.key.violation.referenced")).intValue();
			if (e.getErrorCode() == codigoError){
				Log.logRE(usr.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_MINOR,e.getErrorCode(),
						 "No se pudo eliminar este registro porque esta siendo referenciado por otra tabla."+e.getMessage());
				throw new ExceptionInstanciaReferenciada(idConstante,ConstanteDAO.class.getName());
			}else{
				Log.logRE(usr.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_MAYOR,e.getErrorCode(),
						 "No se pudo eliminar este registro porque esta siendo referenciado por otra tabla."+e.getMessage());
				throw new ExceptionEjecucionSQL(e,ConstanteDAO.class.getName());
			}
		}
		finally {
		  SQLUtil.cierraPreparedStatement(preparedStatement);
		}
	}


}//class
