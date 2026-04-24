package es.sdweb.application.model.dal.facade;

import java.util.List;

import es.sdweb.application.model.dto.PerfilDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaReferenciada;
import es.sdweb.application.util.ExceptionFaltaParametro;


/**
 * @author Antonio Carro Mariño
 *
 * DAO que opera sobre la tabla perfil.
 */

public interface IPerfilDAO {

	/**
	 * Crea un perfil en la B.D. y le asocia unos determinados elementos.
	 * @param usr
	 * @param perfilDTO
	 * @param elementoDTOs lista con los elementos a asociar al nuevo perfil.
	 * @return
	 * @throws ExceptionAccesoDatos
	 * @throws ExceptionInstanciaDuplicada
	 * @throws ExceptionFaltaParametro
	 * @throws ExceptionInstanciaNoHallada
	 */
	public PerfilDTO createPerfilElementos(UsrDTO usr,PerfilDTO perfilDTO,List elementoDTOs)
	throws ExceptionAccesoDatos, ExceptionInstanciaDuplicada, ExceptionFaltaParametro,ExceptionInstanciaNoHallada;

	/**
	 * Actualiza los valores de un determinado perfil en la B.D. as� como sus asociaciones con los elementos.
	 * @param usr
	 * @param codPerfil
	 * @param perfilDTO
	 * @param elementoDTOs
	 * @return
	 * @throws ExceptionEjecucionSQL
	 * @throws ExceptionAccesoDatos
	 * @throws ExceptionInstanciaDuplicada
	 * @throws ExceptionFaltaParametro
	 * @throws ExceptionInstanciaNoHallada
	 */

	public PerfilDTO updatePerfilElementos(UsrDTO usr,String codPerfil,PerfilDTO perfilDTO,List elementoDTOs)
	throws ExceptionAccesoDatos, ExceptionInstanciaDuplicada,ExceptionFaltaParametro,ExceptionInstanciaNoHallada;

	/**
	 * Busca todos los perfiles que se encuentren en la B.D.
	 * @param usr
	 * @param startIndex comienzo de la b�squeda.
	 * @param count n�mero de perfiles a buscar.
	 * @return
	 * @throws ExceptionAccesoDatos
	 */

	public List findAll(UsrDTO usr) throws ExceptionAccesoDatos;

	public PerfilDTO findPerfilProfesor(UsrDTO usr,String nombrePerfil) throws ExceptionAccesoDatos;

	/**
	 * Elimina un perfil de la B.D.
	 * @param usr
	 * @param codPerfil
	 * @throws ExceptionInstanciaNoHallada
	 * @throws ExceptionAccesoDatos
	 * @throws ExceptionExisteUsuarioConPerfil
	 */
	public void remove(UsrDTO usr, String codPerfil) throws  ExceptionInstanciaNoHallada,ExceptionAccesoDatos, ExceptionInstanciaReferenciada ;

}//Interface
