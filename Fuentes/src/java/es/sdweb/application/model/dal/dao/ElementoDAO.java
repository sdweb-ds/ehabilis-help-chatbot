package es.sdweb.application.model.dal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.dal.exceptions.ExceptionEjecucionSQL;
import es.sdweb.application.model.dto.ElementoDetallesDTO;
import es.sdweb.application.model.dto.ElementoDetallesDTOArbol;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;

/**
 * @author Antonio Carro Mariño
 *
 * DAO que opera sobre la tabla elemento.
 * No contiene metodos create,update y remove, ya que los Elementos que contengan la B.D. configuran la aplicacion y
 * deberan ser considerados como una inicializacion de la B.D., es decir insertados a mano con SQLs. Estos elementos
 * estan presentes en el codigo fuente de la aplicacion para controlar si el usuario tiene asignado un permiso.
 */

public class ElementoDAO {




	/**
	 * Comprueba si existe en la B.D un elemento identificado por un determinado codElemento.
	 * @param usr
	 * @param connection
	 * @param codElemento
	 * @return
	 * @throws ExceptionEjecucionSQL
	 */
	public boolean exists(UsrDTO usr,Connection connection,String codElemento) throws ExceptionEjecucionSQL{

		Log.logRT(usr.getUsr(),"BEGIN ElementoDAO.exists()");

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String queryString =
		"SELECT codElemento FROM elemento WHERE codElemento = ?";

		try {

			Log.logRT(usr.getUsr(),StringUtil.cat("SQL: ",queryString,"; @codElemento=",codElemento));

			preparedStatement =
			connection.prepareStatement(queryString);
			int i = 1;
			preparedStatement.setString(i++,codElemento);
			Log.logRT(usr.getUsr(),"Ejecutando la SQL...");
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				 Log.logRT(usr.getUsr(),"B�squeda satisfactoria.");
				 Log.logRT(usr.getUsr(),"END ElementoDAO.exists()");
				return true;
			}
			else {
				 Log.logRT(usr.getUsr(),"Busqueda infructuosa.");
				 Log.logRT(usr.getUsr(),"END ElementoDAO.exists()");
				return false;
			}
		} catch (SQLException e) {
			throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"exists",e.getErrorCode());
		} finally {
			try {
				if (preparedStatement != null) {
				preparedStatement.close();
				}
				} catch (SQLException e) {
					throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"exists",e.getErrorCode());
				}
		}//finally
	}



	/**
	 * Devuelve en estructura de lista todos aquellos elementos a los que tiene acceso el usuario identificado por
	 * un determinado login.
	 * @param usrDTO
	 * @param login identifica al usuario.
	 * @param connection
	 * @return
	 * @throws ExceptionInstanciaNoHallada
	 * @throws ExceptionEjecucionSQL
	 */
	public List permisos(UsrDTO usrDTO,String login ,Connection connection)throws ExceptionInstanciaNoHallada,ExceptionEjecucionSQL{

		Log.logRT(usrDTO.getUsr(),"BEGIN ElementoDAO: public List permisos(UsrDTO usrDTO,String login ,Connection connection)throws ExceptionInstanciaNoHallada,ExceptionEjecucionSQL");

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List resultDTOs = new ArrayList();
		String queryString = "";
		if (!StringUtil.isEmpty(login)) {
			login= login.toUpperCase().trim();
			}

		try {

			queryString =
				" SELECT DISTINCT e.codelemento,da.url,da.ubicacion,da.nombre,da.tipo,u.login "+
				" FROM usuario u, perfil_usuario pu, elemento_perfil ep, elemento e, dato_elemento da "+
					" WHERE (u.login = ? AND u.codUsuario = pu.CodUsuario AND pu.codPerfil = ep.codPerfil "+
						" AND ep.codElemento = e.codelemento AND  e.codElemento = da.codElemento "+
						" ) ORDER BY codElemento";

			preparedStatement = connection.prepareStatement(queryString);

			int i = 1;
			preparedStatement.setString(i++,login);

			/* Execute query. */
			Log.logRT(usrDTO.getUsr(),"Ejecutando la SQL...");
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()){
				i = 1;
				String codElemento = resultSet.getString(i++);
				String url = resultSet.getString(i++);
				String ubicacion = resultSet.getString(i++);
				String nombre = resultSet.getString(i++);
				String tipo = resultSet.getString(i++);

				resultDTOs.add(new ElementoDetallesDTO(codElemento,url,tipo,ubicacion,nombre,tipo));
			}

			/* Return the value object. */
		        Log.logRT(usrDTO.getUsr(),"END ElementoDAO: public List permisos(UsrDTO usrDTO,String login ,Connection connection)throws ExceptionInstanciaNoHallada,ExceptionEjecucionSQL");


			return resultDTOs;

			} catch (SQLException e) {
				Log.logRE(usrDTO.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_NORMAL,e.getErrorCode(),
						 "Error en la busqueda de elementos a los que tiene acceso el usuario de login:"+login);
				throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"findAllElementosByLoginByTipo()",e.getErrorCode());
			} finally {
				try {
					if (preparedStatement != null)
						preparedStatement.close();

				} catch (SQLException e) {
					throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"public List permisos(UsrDTO usrDTO,String login ,Connection connection)throws ExceptionInstanciaNoHallada,ExceptionEjecucionSQL",e.getErrorCode());
				}
			}
	}


	/**
	 * Devuelve la estructura completa de los elementos que est�n asociados a los perfiles del usuario
	 * con ese login y que son del tipo especificado. Si el tipo es una cadena vac�a entonces devuelve
	 * todo el �rbol de elementos.
	 * @param usrDTO
	 * @param login
	 * @param tipo
	 * @param connection
	 * @return
	 * @throws ExceptionInstanciaNoHallada
	 * @throws ExceptionEjecucionSQL
	 */
	public List findArbol(UsrDTO usrDTO,String login,String tipo,Connection connection)throws ExceptionInstanciaNoHallada,ExceptionEjecucionSQL{

		Log.logRT(usrDTO.getUsr(),"BEGIN ElementoDAO.findArbol()");


		Log.logRT(usrDTO.getUsr(),"Ejecutando la SQL...");

		List elementosPadreDTOs = new ArrayList();
		elementosPadreDTOs = findAllElementosByLoginByTipo(usrDTO,login.trim().toUpperCase(),tipo,connection);


		Iterator iter = elementosPadreDTOs.iterator();
		List resultado = new ArrayList();
		List listaVacia = new ArrayList();
		ElementoDetallesDTO elementoDetallesDTO;
		ElementoDetallesDTOArbol elementoDetallesDTOArbol;
		while (iter.hasNext()){
			elementoDetallesDTO = (ElementoDetallesDTO)iter.next();
			elementoDetallesDTOArbol = new ElementoDetallesDTOArbol(elementoDetallesDTO,listaVacia);
			findHijos(usrDTO,elementoDetallesDTOArbol,login,connection);
			resultado.add(elementoDetallesDTOArbol);
		}

		Log.logRT(usrDTO.getUsr(),"END ElementoDAO.findArbol()");

		return resultado;

	}



	/**
	 * Devuelve una lista con todos los elementos raiz (de un tipo determinado)que estan asociados con
	 * los perfiles que a su vez estan asociados con un determinado usuario con dicho login.
	 * @param usrDTO
	 * @param login que identifica al usuario.
	 * @param tipo identifica a los elementos.
	 * @param connection
	 * @return
	 * @throws ExceptionInstanciaNoHallada
	 * @throws ExceptionEjecucionSQL
	 */
	private List findAllElementosByLoginByTipo(UsrDTO usrDTO,String login,String tipo,Connection connection)throws ExceptionInstanciaNoHallada,ExceptionEjecucionSQL{

		Log.logRT(usrDTO.getUsr(),"BEGIN ElementoDAO.findAllElementosByLoginByTipo()");

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List resultDTOs = new ArrayList();
		String queryString = "";


		try {

			queryString =
				"SELECT DISTINCT e.codelemento,da.url,da.ubicacion,da.nombre "+
					" FROM usuario u, perfil_usuario pu, elemento_perfil ep, elemento e, dato_elemento da " +
						" WHERE (u.login = ? AND u.codUsuario = pu.CodUsuario AND pu.codPerfil = ep.codPerfil " +
							" AND ep.codElemento = e.codelemento AND  e.codElemento = da.codElemento " +
								"AND e.codelepadre is null AND  da.tipo = ?) ORDER BY order_num ASC, codElemento";

			preparedStatement = connection.prepareStatement(queryString);

			int i = 1;
			preparedStatement.setString(i++,login);
			preparedStatement.setString(i++,tipo);

			Log.logRT(usrDTO.getUsr(),"Ejecutando la SQL...");
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()){
				i = 1;
				String codElemento = resultSet.getString(i++);
				String url = resultSet.getString(i++);
				String ubicacion = resultSet.getString(i++);
				String nombre = resultSet.getString(i++);

				resultDTOs.add(new ElementoDetallesDTO(codElemento,url,tipo,ubicacion,nombre,"-1"));
			}

		    Log.logRT(usrDTO.getUsr(),"END ElementoDAO.findAllElementosByLoginByTipo()");
			return resultDTOs;

			} catch (SQLException e) {
				Log.logRE(usrDTO.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_NORMAL,e.getErrorCode(),
						 "Error en la busqueda de elementos a los que tiene acceso el usuario de login:"+login);
				throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"findAllElementosByLoginByTipo()",e.getErrorCode());
			} finally {
				try {
					if (preparedStatement != null)
						preparedStatement.close();

				} catch (SQLException e) {
					throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"findAllElementosByLoginByTipo",e.getErrorCode());
				}
			}
	}


	/**
	 * Devuelve la estrutura de arbol que cuelga de un determinado elemento y que esta asociado a los permisos
	 * de un determinado usuario.
	 * @param usrDTO
	 * @param elementoDetallesDTOArbol elemento padre sobre el que se hace la b�squeda.
	 * @param login identifica al usario.
	 * @param connection
	 * @throws ExceptionEjecucionSQL
	 */
	private void findHijos(UsrDTO usrDTO,ElementoDetallesDTOArbol elementoDetallesDTOArbol,String login,Connection connection)
	throws ExceptionEjecucionSQL{


	Log.logRT(usrDTO.getUsr(),"BEGIN ElementoDAO.buscarTodos()");

	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	String queryString = "";

	try {
		if (!StringUtil.isEmpty(login)) {
		login= login.toUpperCase().trim();
		}
 		queryString = "SELECT DISTINCT  e.codelemento,da.url, da.ubicacion,da.nombre,dapadre.ubicacion "+
		 " FROM usuario u "+
						" INNER JOIN  perfil_usuario pu  ON (u.codUsuario = pu.CodUsuario) "+
						" INNER JOIN  elemento_perfil ep  ON (pu.codPerfil = ep.codPerfil) "+
						" INNER JOIN  elemento e  ON (ep.codElemento = e.codelemento) "+
                        " INNER JOIN  dato_elemento da  ON (e.codElemento = da.codElemento) "+
						" INNER JOIN  dato_elemento dapadre  ON (e.codelepadre = dapadre.codElemento) "+
						" WHERE ( e.codelepadre =? AND u.login = ? and da.tipo = ? ) "+
 						" ORDER BY e.order_num";

		preparedStatement = connection.prepareStatement(queryString);
		int i = 1;
		preparedStatement.setString(i++,elementoDetallesDTOArbol.getElementoDetallesDTO().getCodElemento());
		preparedStatement.setString(i++,login);
		preparedStatement.setString(i++,elementoDetallesDTOArbol.getElementoDetallesDTO().getTipo());


		Log.logRT(usrDTO.getUsr(),"Ejecutando la SQL...");
		resultSet = preparedStatement.executeQuery();

		ElementoDetallesDTO elementoDetallesDTOAux;
		ElementoDetallesDTOArbol elementoDetallesDTOArbolAux;
		boolean hasElementos = false;

		List listaVacia = new ArrayList();
		List listaAuxiliar = new ArrayList();
		while (resultSet.next()){
			hasElementos = true;
			i=1;
			String codElemento = resultSet.getString(i++);
			String url = resultSet.getString(i++);
			String ubicacion = resultSet.getString(i++);
			String nombre = resultSet.getString(i++);
			String ubicacionPadre = resultSet.getString(i++);


			elementoDetallesDTOAux = new ElementoDetallesDTO(codElemento,url,
			elementoDetallesDTOArbol.getElementoDetallesDTO().getTipo(),ubicacion,nombre,ubicacionPadre);
			elementoDetallesDTOArbolAux = new ElementoDetallesDTOArbol(elementoDetallesDTOAux,listaVacia);
			listaAuxiliar.add(elementoDetallesDTOArbolAux);

		}
		elementoDetallesDTOArbol.setElementoDetallesDTO(elementoDetallesDTOArbol.getElementoDetallesDTO());
		elementoDetallesDTOArbol.setSubArbol(listaAuxiliar);

		if (hasElementos){
			List hijos = elementoDetallesDTOArbol.getSubArbol();
			Iterator iter = hijos.iterator();
			while (iter.hasNext()){
				findHijos(usrDTO,(ElementoDetallesDTOArbol)iter.next(),login,connection);

			}
		}

	    Log.logRT(usrDTO.getUsr(),"END ElementoDAO.buscarTodos()");

		} catch (SQLException e) {
			Log.logRE(usrDTO.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_NORMAL,e.getErrorCode(),
					 "Error en la busqueda de elementos:"+e.getMessage());
			throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"buscarTodos()",e.getErrorCode());
		} finally {
			try {
				if (preparedStatement != null) {
				preparedStatement.close();
				}
				} catch (SQLException e) {
					throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"find",e.getErrorCode());
				}}
}


	/**
	 * Devuelve la estructura completa de los elementos que hay en la B.D. con la informaci�n sobre
	 * si dicho elemento est� o no relacionado con el perfil que recibe como parametro.
	 * @param usrDTO
	 * @param codPerfil
	 * @param connection
	 * @return
	 * @throws ExceptionInstanciaNoHallada
	 * @throws ExceptionEjecucionSQL
	 */
	public List findArbolCheckPerfil(UsrDTO usrDTO,String codPerfil,Connection connection)throws ExceptionInstanciaNoHallada,ExceptionEjecucionSQL{

		Log.logRT(usrDTO.getUsr(),"BEGIN ElementoDAO.findArbolCheckPerfil()");


		Log.logRT(usrDTO.getUsr(),"Ejecutando la SQL...");

		List elementosPadreDTOs = new ArrayList();
		elementosPadreDTOs = findAllElementosCheckPerfil(usrDTO,codPerfil.trim().toUpperCase(),connection);


		Iterator iter = elementosPadreDTOs.iterator();
		List resultado = new ArrayList();
		ElementoDetallesDTOArbol elementoDetallesDTOArbol;
		while (iter.hasNext()){

			elementoDetallesDTOArbol = (ElementoDetallesDTOArbol)iter.next();
			findHijosCheckPerfil(usrDTO,elementoDetallesDTOArbol,codPerfil,connection);
			resultado.add(elementoDetallesDTOArbol);
		}

		Log.logRT(usrDTO.getUsr(),"END ElementoDAO.findArbolCheckPerfil()");

		return resultado;

	}


	/**
	 * Devuelve una lista con todos los elementos ra�z (de un tipo determinado)que est�n asociados con
	 * un perfil determinado.
	 * @param usrDTO
	 * @param codPerfil
	 * @param connection
	 * @return
	 * @throws ExceptionInstanciaNoHallada
	 * @throws ExceptionEjecucionSQL
	 */
	private List findAllElementosCheckPerfil(UsrDTO usrDTO,String codPerfil,Connection connection)throws ExceptionInstanciaNoHallada,ExceptionEjecucionSQL{

		Log.logRT(usrDTO.getUsr(),"BEGIN ElementoDAO.findAllElementosCheckPerfil()");

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List resultDTOs = new ArrayList();
		String queryString = "";


		try {

		queryString = "SELECT DISTINCT e.codelemento,da.url,da.ubicacion,da.nombre,da.tipo , ep.codPerfil "+
					" FROM elemento_perfil ep, elemento e, dato_elemento da "+
							" WHERE ( ep.codPerfil = ? AND ep.codElemento = e.codelemento AND"+
							" e.codElemento = da.codElemento AND e.codelepadre is null ) "+
					" UNION "+
					" SELECT DISTINCT e.codelemento,da.url,da.ubicacion,da.nombre,da.tipo ,e.codelemento "+
					"FROM elemento_perfil ep, elemento e, dato_elemento da "+
							" WHERE ( e.codElemento = da.codElemento AND e.codelepadre is null AND "+
							" e.codElemento NOT IN " +
								"(select e.codelemento  FROM elemento_perfil ep, elemento e "+
									" where e.codElemento=ep.codElemento AND ep.codPerfil = ?)) "+
					"ORDER BY 1";

			preparedStatement = connection.prepareStatement(queryString);

			int i = 1;
			preparedStatement.setString(i++,codPerfil);
			preparedStatement.setString(i++,codPerfil);

			Log.logRT(usrDTO.getUsr(),"Ejecutando la SQL...");
			resultSet = preparedStatement.executeQuery();
			String relacionado="false";
			while (resultSet.next()){
				relacionado="false";
				i = 1;
				String codElemento = resultSet.getString(i++).trim();
				String url = resultSet.getString(i++).trim();
				String ubicacion = resultSet.getString(i++).trim();
				String nombre = resultSet.getString(i++).trim();
				String tipo = resultSet.getString(i++).trim();
				String codPerfil1 = resultSet.getString(i++).trim();
				ElementoDetallesDTO elementoDetallesDTO = new ElementoDetallesDTO(codElemento,url,tipo,ubicacion,nombre,"-1");
				if (codPerfil1.trim().toUpperCase().compareTo(codPerfil)==0) {
					relacionado = "true";
				}
				List listaVacia = new ArrayList();
				ElementoDetallesDTOArbol elementoDetallesDTOArbol = new ElementoDetallesDTOArbol(elementoDetallesDTO,relacionado,listaVacia);

				resultDTOs.add(elementoDetallesDTOArbol);

			}

		    Log.logRT(usrDTO.getUsr(),"END ElementoDAO.findAllElementosCheckPerfil()");


			return resultDTOs;

			} catch (SQLException e) {
				Log.logRE(usrDTO.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_NORMAL,e.getErrorCode(),
						 "Error en la busqueda de todos los elementos chequeando que est�n relacionados con:"+codPerfil);
				throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"findAllElementosCheckPerfil()",e.getErrorCode());
			} finally {
				try {
					if (preparedStatement != null)
						preparedStatement.close();

				} catch (SQLException e) {
					throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"findAllElementosCheckPerfil",e.getErrorCode());
				}
			}
	}


	/**
	 * Devuelve la estrutura de �rbol que cuelga de un determinado elemento indicando que elementos hijos est�n
	 *  asociados a un determinado perfil.
	 * @param usrDTO
	 * @param elementoDetallesDTOArbol
	 * @param codPerfil
	 * @param connection
	 * @throws ExceptionEjecucionSQL
	 */
	private void findHijosCheckPerfil(UsrDTO usrDTO,ElementoDetallesDTOArbol elementoDetallesDTOArbol,String codPerfil,Connection connection)
	throws ExceptionEjecucionSQL{


	Log.logRT(usrDTO.getUsr(),"BEGIN ElementoDAO.buscarTodos()");

	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	String queryString = "";

	try {

		queryString = "SELECT DISTINCT e.codelemento,da.url,da.ubicacion,da.nombre,da.tipo ,dapadre.ubicacion, ep.codPerfil "+
		" FROM elemento_perfil ep, elemento e, dato_elemento da, dato_elemento dapadre "+
				" WHERE ( ep.codPerfil = ? AND ep.codElemento = e.codelemento AND"+
				" e.codElemento = da.codElemento AND e.codelepadre = dapadre.codElemento AND e.codelepadre = ? ) "+
		" UNION "+
		" SELECT DISTINCT e.codelemento,da.url,da.ubicacion,da.nombre,da.tipo,dapadre.ubicacion ,e.codelemento "+
		"FROM elemento_perfil ep, elemento e, dato_elemento da, dato_elemento dapadre "+
				" WHERE ( e.codElemento = da.codElemento AND e.codelepadre = dapadre.codElemento AND e.codelepadre = ? AND "+
				" e.codElemento NOT IN " +
					"(select e.codelemento FROM elemento_perfil ep, elemento e "+
						" where e.codElemento=ep.codElemento AND ep.codPerfil = ?)) "+
		"ORDER BY 1";

		preparedStatement = connection.prepareStatement(queryString);
		int i = 1;
		preparedStatement.setString(i++,codPerfil);
		preparedStatement.setString(i++,elementoDetallesDTOArbol.getElementoDetallesDTO().getCodElemento());
		preparedStatement.setString(i++,elementoDetallesDTOArbol.getElementoDetallesDTO().getCodElemento());
		preparedStatement.setString(i++,codPerfil);

		Log.logRT(usrDTO.getUsr(),"Ejecutando la SQL...");
		resultSet = preparedStatement.executeQuery();

		ElementoDetallesDTO elementoDetallesDTOAux;
		ElementoDetallesDTOArbol elementoDetallesDTOArbolAux;
		boolean hasElementos = false;


		String relacionado = "false";
		List listaVacia = new ArrayList();
		List listaAuxiliar = new ArrayList();
		while (resultSet.next()){
			hasElementos = true;
			i=1;
			relacionado = "false";
			String codElemento = resultSet.getString(i++).trim();
			String url = resultSet.getString(i++).trim();
			String ubicacion = resultSet.getString(i++).trim();
			String nombre = resultSet.getString(i++).trim();
			String tipo = resultSet.getString(i++).trim();
			String ubicacionPadre = resultSet.getString(i++).trim();
			String codPerfil1 = resultSet.getString(i++).trim();
			elementoDetallesDTOAux = new ElementoDetallesDTO(codElemento,url,tipo,ubicacion,nombre,ubicacionPadre);
			if (codPerfil1.trim().toUpperCase().compareTo(codPerfil)==0) {
				relacionado = "true";
			}
			elementoDetallesDTOArbolAux =new ElementoDetallesDTOArbol(elementoDetallesDTOAux,relacionado,listaVacia);



			listaAuxiliar.add(elementoDetallesDTOArbolAux);

		}
		elementoDetallesDTOArbol.setElementoDetallesDTO(elementoDetallesDTOArbol.getElementoDetallesDTO());
		elementoDetallesDTOArbol.setSubArbol(listaAuxiliar);

		if (hasElementos){
			List hijos = elementoDetallesDTOArbol.getSubArbol();
			Iterator iter = hijos.iterator();
			while (iter.hasNext()){
				findHijosCheckPerfil(usrDTO,(ElementoDetallesDTOArbol)iter.next(),codPerfil,connection);

			}
		}

	    Log.logRT(usrDTO.getUsr(),"END ElementoDAO.buscarTodos()");

		} catch (SQLException e) {
			Log.logRE(usrDTO.getUsr(),".execute()",Log.TIPO_BASE_DATOS,Log.CRITICIDAD_NORMAL,e.getErrorCode(),
					 "Error en la busqueda de elementos:"+e.getMessage());
			throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"buscarTodos()",e.getErrorCode());
		} finally {
			try {
				if (preparedStatement != null) {
				preparedStatement.close();
				}
				} catch (SQLException e) {
					throw new ExceptionEjecucionSQL(e,queryString,ElementoDAO.class.getName(),"find",e.getErrorCode());
				}}
}


}//class
