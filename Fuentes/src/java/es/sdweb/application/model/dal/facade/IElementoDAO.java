package es.sdweb.application.model.dal.facade;

import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import java.util.List;


/**
 * @author Antonio Carro Mariño
 *
 * Fachada al DAO que opera sobre la tabla elemento.
 */

public interface IElementoDAO {


	/**
	 * Devuelve en estructura de lista todos aquellos elementos a los que tiene acceso el usuario identificado por 
	 * un determinado login.
	 * @param usr
	 * @param login
	 * @return
	 * @throws ExceptionInstanciaNoHallada
	 * @throws ExceptionAccesoDatos
	 */
	public List permisos(UsrDTO usr ,String login )throws ExceptionInstanciaNoHallada,ExceptionAccesoDatos;
	
	
	/**
	 * Devuelve la estructura completa de los elementos que estan asociados a los perfiles del usuario
	 * con ese login y que son del tipo especificado. Si el tipo es una cadena vacia entonces devuelve
	 * todo el arbol de elementos.  
	 * @param usrDTO
	 * @param login
	 * @param tipo
	 * @return
	 * @throws ExceptionAccesoDatos
	 * @throws ExceptionInstanciaNoHallada
	 * @throws ExceptionEjecucionSQL
	 */
	public List findArbol(UsrDTO usrDTO,String login,String tipo)throws ExceptionAccesoDatos,ExceptionInstanciaNoHallada;
		
	
	/**
	 * Devuelve la estructura completa de los elementos que hay en la B.D. con la informacion sobre
	 * si dicho elemento esta o no relacionado con el perfil que recibe como parametro.
	 * @param usrDTO
	 * @param codPerfil
	 * @return
	 * @throws ExceptionAccesoDatos
	 * @throws ExceptionInstanciaNoHallada
	 * @throws ExceptionEjecucionSQL
	 */
	public List findArbolCheckPerfil(UsrDTO usrDTO,String codPerfil)throws ExceptionAccesoDatos,ExceptionInstanciaNoHallada;
	
	

}//Interface
