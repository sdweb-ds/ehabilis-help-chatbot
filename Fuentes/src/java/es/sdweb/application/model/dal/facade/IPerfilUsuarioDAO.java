package es.sdweb.application.model.dal.facade;

import java.util.List;

import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;

/**
 * @author Antonio Carro Mariño
 *
 * DAO que opera sobre la tabla usuario_perfil.
 */

public interface IPerfilUsuarioDAO {


	/**
	 * Devuelve una lista con los perfiles asociados a un determinado usuario.
	 * @param usr
	 * @param codUsuario
	 * @param startIndex
	 * @param count
	 * @return
	 * @throws ExceptionAccesoDatos
	 */
	public List perfilesAsociadosAUsuario(UsrDTO usr, String codUsuario) throws ExceptionAccesoDatos;

	/**
	 * Devuelve una lista con los perfiles que no se encuentran asociados con un determinado usuario.
	 * @param usr
	 * @param codUsuario
	 * @param startIndex
	 * @param count
	 * @return
	 * @throws ExceptionAccesoDatos
	 */
	public List perfilesNoAsociadosAUsuario(UsrDTO usr, String codUsuario) throws  ExceptionAccesoDatos;


}//class
