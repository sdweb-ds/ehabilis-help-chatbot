package es.sdweb.application.model.dal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.dal.exceptions.ExceptionEjecucionSQL;
import es.sdweb.application.model.dto.ElementoPerfilDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.util.GestorParametrosConfiguracion;

/**
 *  @author Antonio Carro Mariño
 *
 * DAO que opera sobre la tabla perfilElemento.
 */
public class ElementoPerfilDAO {



	/**
	 * Crea una nueva asociaci�n entre elemento y perfil en la B.D.
	 * @param usr
	 * @param connection
	 * @param elementoPerfilDTO
	 * @return
	 * @throws ExceptionEjecucionSQL
	 * @throws ExceptionInstanciaDuplicada
	 */
	public ElementoPerfilDTO create(UsrDTO usr,Connection connection,ElementoPerfilDTO elementoPerfilDTO) throws ExceptionEjecucionSQL,ExceptionInstanciaDuplicada{

		String CODIGO_ERROR_PKV = "primary.key.violation";
		Log.logRT(usr.getUsr(),"BEGIN ElementoPerfilDAO.create()");

		PreparedStatement preparedStatement = null;
		String queryString = "INSERT INTO elemento_perfil (codelemento,codperfil) "+" VALUES (?, ?)";
		int insertedRows=0;

		try {
			preparedStatement = connection.prepareStatement(queryString);
			int i=1;
			preparedStatement.setString(i++,elementoPerfilDTO.getCodElemento());
			preparedStatement.setString(i++,elementoPerfilDTO.getCodPerfil().trim().toUpperCase());


			Log.logRT(usr.getUsr(),"Ejecutando la SQL...");
			insertedRows = preparedStatement.executeUpdate();

			if (insertedRows == 0) {
				Log.logRT(usr.getUsr(),"error insertedRows == 0");
				Log.logRT(usr.getUsr(),"END ElementoPerfilDAO.create()");
				throw new	ExceptionEjecucionSQL(null,queryString,ElementoPerfilDAO.class.getName(),"create",1);
			}


			Log.logRT(usr.getUsr(),"END ElementoPerfilDAO.create()");

		return elementoPerfilDTO;

		} catch (SQLException e) {
			int codigoError = Integer.valueOf(GestorParametrosConfiguracion.getParametro(CODIGO_ERROR_PKV)).intValue();
			if (e.getErrorCode() == codigoError){
				Log.logRE(usr.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_NORMAL,e.getErrorCode(),
						 "Error en la inserci�n de ElementoPerfil:"+e.getMessage());
				throw new ExceptionInstanciaDuplicada(elementoPerfilDTO.getCodElemento()+"/"
						+elementoPerfilDTO.getCodPerfil(),ElementoPerfilDTO.class.getName());
			}else{
				Log.logRE(usr.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_NORMAL,e.getErrorCode(),
						 "Error en la inserci�n de ElementoPerfil:"+e.getMessage());

			}
			throw new ExceptionEjecucionSQL(e,queryString,ElementoPerfilDAO.class.getName(),"create",e.getErrorCode());
		} finally {
			try {
				if (preparedStatement != null) {
				preparedStatement.close();
				}
				} catch (SQLException e) {
					throw new ExceptionEjecucionSQL(e,queryString,ElementoPerfilDAO.class.getName(),"create",e.getErrorCode());
				}

		}

	}

	/**
	 * Borra los elementos asociados a un perfil determinado.
	 * @param usrDTO
	 * @param codPerfil
	 * @param connection
	 * @throws ExceptionEjecucionSQL
	 * @throws ExceptionInstanciaNoHallada
	 */
	public void removePerfil(UsrDTO usrDTO,String codPerfil,Connection connection)throws ExceptionEjecucionSQL{

		Log.logRT(usrDTO.getUsr(),"BEGIN ElementoPerfilDAO.removePerfil()");

		int removedRows = 0;
		PreparedStatement preparedStatement = null;
		String queryString = "";

		try{

			queryString = "DELETE FROM elemento_perfil WHERE (codperfil= '"+codPerfil+"')";

			Log.logRT(usrDTO.getUsr(),StringUtil.cat("SQL: ",queryString));
			preparedStatement = connection.prepareStatement(queryString);
			Log.logRT(usrDTO.getUsr(),"Ejecutando la SQL...");
			removedRows = preparedStatement.executeUpdate();
			Log.logRT(usrDTO.getUsr(),"END ElementoPerfilDAO.removePerfil()");

		}catch (SQLException e){
			Log.logRE(usrDTO.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_NORMAL,e.getErrorCode(),
					"Error en la eliminaci�n de los elementos asignados a un perfil:"+e.getMessage());
			throw new ExceptionEjecucionSQL(e,queryString,ElementoPerfilDAO.class.getName(),"existsCodPerfil()",e.getErrorCode());
		}finally{
			try {
				if (preparedStatement != null)
					preparedStatement.close();

			} catch (SQLException e) {
				throw new ExceptionEjecucionSQL(e,queryString,ElementoPerfilDAO.class.getName(),"removePerfil",e.getErrorCode());
			}
		}
	}


}//class
