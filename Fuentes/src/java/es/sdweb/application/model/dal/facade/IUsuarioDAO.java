package es.sdweb.application.model.dal.facade;

import java.util.List;

import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.exceptions.ExceptionErrorConexion;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.dto.UsuarioDTO;
import es.sdweb.application.model.exceptions.ExceptionFormatoDeDatos;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionUniqueLogin;
import es.sdweb.application.util.ExceptionFaltaParametro;

/**
 * DAO que opera sobre la tabla usuario.
 *
 */

public interface IUsuarioDAO {

    /**
     * Crea un nuevo usuario en la B.D. y las asociaciones pertinentes con los perfiles.
     *
     * @param usrDTO
     * @param usuarioDTO
     * @param perfilesDTOs
     * @return
     * @throws ExceptionAccesoDatos
     * @throws ExceptionInstanciaDuplicada
     * @throws ExceptionFaltaParametro
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionUniqueLogin
     * @throws ExceptionFormatoDeDatos
     */
    public UsuarioDTO createUsuarioPerfiles(UsrDTO usrDTO, UsuarioDTO usuarioDTO, List perfilesDTOs) throws ExceptionAccesoDatos,
            ExceptionInstanciaDuplicada, ExceptionFaltaParametro, ExceptionInstanciaNoHallada, ExceptionUniqueLogin;

    /**
     *  Modifica los datos de un usuario en la B.D. y sus asociaciones con los perfiles.
     *
     * @param usrDTO
     * @param usuarioDTO
     * @param perfilesDTOs
     * @throws ExceptionFaltaParametro
     * @throws ExceptionAccesoDatos
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionInstanciaDuplicada
     * @throws ExceptionUniqueLogin
     * @throws ExceptionFormatoDeDatos
     */
    public void modificarUsuario(UsrDTO usrDTO, UsuarioDTO usuarioDTO, List perfilesDTOs) throws ExceptionFaltaParametro, ExceptionAccesoDatos,
            ExceptionInstanciaNoHallada, ExceptionInstanciaDuplicada, ExceptionUniqueLogin;

    /**
     * Modifica
     *
     * @param usrDTO
     * @param login
     * @param password
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionErrorConexion
     * @throws ExceptionAccesoDatos
     */
    public void modificarClaveUsuario(UsrDTO usrDTO, String login, String password) throws ExceptionInstanciaNoHallada, ExceptionAccesoDatos;

    /**
     * Comprueba la existencia de un usario en la B.D. con un login determinado.
     *
     * @param usrDTO
     * @param login
     * @return
     * @throws ExceptionAccesoDatos
     */
    public boolean existeLogin(UsrDTO usrDTO, String login) throws ExceptionAccesoDatos;

    /**
     * Devuelve el usuario en la B.D. identificado por ese login.
     *
     * @param usrDTO
     * @param codUsuario
     * @return
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionAccesoDatos
     * @throws ExceptionFormatoDeDatos
     */
    public UsuarioDTO findByLogin(UsrDTO usrDTO, String codUsuario) throws ExceptionInstanciaNoHallada, ExceptionAccesoDatos;

    /**
     * Busca usuarios en la B.D. por distintos filtros de busqueda.
     *
     * @param usrDTO
     * @param codUsuario
     * @param login
     * @param apellido1
     * @param apellido2
     * @param nombre
     * @param nif
     * @param codPerfil
     * @param startIndex
     * @param cont
     * @param busquedaEstricta: booleano que indica si la busqueda se hace con igual (true) o con LIKE (false)
     * @return
     * @throws ExceptionAccesoDatos
     * @throws ExceptionFormatoDeDatos
     * @throws ExceptionInstanciaNoHallada
     */
    public List findByCampos(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, int startIndex, int cont, boolean busquedaEstricta) throws ExceptionAccesoDatos, ExceptionFormatoDeDatos, ExceptionInstanciaNoHallada;

    public List findByCampos(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, String order, String orderType, int startIndex, int cont, boolean busquedaEstricta) throws ExceptionAccesoDatos, ExceptionFormatoDeDatos, ExceptionInstanciaNoHallada;

    /**
     * Recupera todos los usuarios de la BD.
     *
     * @param usrDTO
     * @return
     * @throws ExceptionAccesoDatos
     * @throws ExceptionFormatoDeDatos
     * @throws ExceptionInstanciaNoHallada
     */
    public List findAll(UsrDTO usrDTO) throws ExceptionAccesoDatos;

    /**
     * Elimina el usuario identificado por ese login.
     *
     * @param usrDTO
     * @param login
     * @throws ExceptionAccesoDatos
     * @throws ExceptionInstanciaNoHallada
     */
    public void removeByLogin(UsrDTO usrDTO, String login) throws ExceptionAccesoDatos, ExceptionInstanciaNoHallada;

}//class
