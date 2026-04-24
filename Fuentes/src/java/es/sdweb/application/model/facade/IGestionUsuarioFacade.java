package es.sdweb.application.model.facade;

import java.util.List;

import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.exceptions.ExceptionEjecucionSQL;
import es.sdweb.application.model.dto.LoginDTO;
import es.sdweb.application.model.dto.PerfilDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.dto.UsuarioDTO;
import es.sdweb.application.model.dto.UsuarioDTOList;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.model.exceptions.ExceptionFormatoDeDatos;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaReferenciada;
import es.sdweb.application.model.exceptions.ExceptionUniqueLogin;
import es.sdweb.application.util.ExceptionFaltaParametro;

/**
 * @author Antonio Carro Mariño
 *
 * Expone una fachada a la capa superior (Controlador). Implementacion del patron Facade.
 */

public interface IGestionUsuarioFacade {

    /**************************************DAOs SOBRE ELEMENTO ********************************/

    /**
     * Devuelve en estructura de lista todos aquellos elementos a los que tiene acceso el usuario identificado por un determinado login.
     *
     * @param usrDTO
     * @param login
     * @return
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionAccesoDatos
     * @throws ExceptionFaltaParametro
     */
    public List permisos(UsrDTO usrDTO, String login) throws ExceptionInstanciaNoHallada, ExceptionErrorInterno, ExceptionFaltaParametro;

    /**
     * Devuelve la estructura completa de los elementos que son botones que estan asociados a los perfiles del usuario con ese login.
     *
     * @param usrDTO
     * @param login
     * @return
     * @throws ExceptionFaltaParametro
     * @throws ExceptionAccesoDatos
     * @throws ExceptionInstanciaNoHallada
     */
    public List accesoBotones(UsrDTO usrDTO, String login) throws ExceptionFaltaParametro, ExceptionErrorInterno, ExceptionInstanciaNoHallada;

    /**
     * Devuelve la estructura completa de los elementos que conforman el menu que estan asociados a los perfiles del usuario con ese login.
     *
     * @param usrDTO
     * @param login
     * @return
     * @throws ExceptionFaltaParametro
     * @throws ExceptionErrorInterno
     * @throws ExceptionInstanciaNoHallada
     */
    public List accesoMenu(UsrDTO usrDTO, String login) throws ExceptionFaltaParametro, ExceptionErrorInterno, ExceptionInstanciaNoHallada;

    /**
     * Devuelve la estructura completa de los elementos que hay en la B.D. en forma de árbol que están asociados con un perfil determinado.
     *
     * @param usrDTO
     * @param codPerfil
     * @return
     * @throws ExceptionFaltaParametro
     * @throws ExceptionAccesoDatos
     * @throws ExceptionInstanciaNoHallada
     */
    public List arbolElementos(UsrDTO usrDTO, String codPerfil) throws ExceptionFaltaParametro, ExceptionErrorInterno, ExceptionInstanciaNoHallada;


    /**************************************DAOs SOBRE PERFILES ********************************/

    /**
     * Recupera de la B.D todos los perfiles.
     *
     * @param usr
     * @param startIndex
     * @param count
     * @return
     * @throws ExceptionErrorInterno
     * @throws ExceptionFaltaParametro
     */
    public List buscarPerfiles(UsrDTO usr) throws ExceptionErrorInterno, ExceptionFaltaParametro;

    /**
     * Elimina un Perfil de la B.D.
     *
     * @param usrDTO
     * @param codPefil
     * @throws ExceptionFaltaParametro
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionAccesoDatos
     * @throws ExceptionExisteUsuarioConPerfil
     */
    public void eliminarPerfil(UsrDTO usrDTO, String codPefil) throws ExceptionFaltaParametro, ExceptionInstanciaNoHallada, ExceptionErrorInterno,
            ExceptionInstanciaReferenciada;

    /**
     * Crea un perfil en la B.D. y le asocia unos determinados elementos.
     *
     * @param usrDTO
     * @param perfilDTO
     * @param elementoDTOs
     * @return
     * @throws ExceptionAccesoDatos
     * @throws ExceptionInstanciaDuplicada
     * @throws ExceptionFaltaParametro
     * @throws ExceptionInstanciaNoHallada
     */
    public PerfilDTO crearPerfilElementos(UsrDTO usrDTO, PerfilDTO perfilDTO, List elementoDTOs) throws ExceptionErrorInterno,
            ExceptionInstanciaDuplicada, ExceptionFaltaParametro, ExceptionInstanciaNoHallada;

    /**
     * Actualiza los valores de un determinado perfil en la B.D. así como sus asociaciones con los elementos.
     *
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
    public PerfilDTO updatePerfilElementos(UsrDTO usr, String codPerfil, PerfilDTO perfilDTO, List elementoDTOs) throws ExceptionErrorInterno,
            ExceptionInstanciaDuplicada, ExceptionFaltaParametro, ExceptionInstanciaNoHallada;


    /************************* DAOs SOBRE LA ASOCIACIÓN ENTRE PERFILES Y USUARIOS ********************/

    /**
     * Devuelve una lista con los perfiles asociados a un determinado usuario.
     *
     * @param usr
     * @param codUsuario
     * @param startIndex
     * @param count
     * @return
     * @throws ExceptionErrorInterno
     * @throws ExceptionFaltaParametro
     */
    public List PerfilesAsociadosAUsuario(UsrDTO usr, String codUsuario) throws ExceptionErrorInterno, ExceptionFaltaParametro;

    /**
     * Devuelve una lista con los perfiles que no están asociados a un determinado usuario.
     *
     * @param usr
     * @param codUsuario
     * @param startIndex
     * @param count
     * @return
     * @throws ExceptionErrorInterno
     * @throws ExceptionFaltaParametro
     */
    public List PerfilesNoAsociadosAUsuario(UsrDTO usr, String codUsuario) throws ExceptionErrorInterno, ExceptionFaltaParametro;


    /**************************************DAOs SOBRE USUARIOS ********************************/

    /**
     * Crea un nuevo usuario en la B.D. y las asociaciones pertinentes con los perfiles.
     *
     * @param usrDTO
     * @param usuarioDTO
     * @param perfilesDTOs
     * @return
     * @throws ExceptionFaltaParametro
     * @throws ExceptionAccesoDatos
     * @throws ExceptionInstanciaDuplicada
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionUniqueLogin
     * @throws ExceptionFormatoDeDatos
     */
    public UsuarioDTO crearUsuarioPerfiles(UsrDTO usrDTO, UsuarioDTO usuarioDTO, List perfilesDTOs) throws ExceptionFaltaParametro,
            ExceptionErrorInterno, ExceptionInstanciaDuplicada, ExceptionInstanciaNoHallada, ExceptionUniqueLogin;

    /**
     *  Modifica los datos de un ususario en la B.D. y sus asociaciones con los perfiles.
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
    public void modificarUsuario(UsrDTO usrDTO, UsuarioDTO usuarioDTO, List perfilesDTOs) throws ExceptionFaltaParametro, ExceptionErrorInterno,
            ExceptionInstanciaNoHallada, ExceptionInstanciaDuplicada, ExceptionUniqueLogin;

    /**
     * Modifica el password de validación de un usuario en la aplicación
     *
     * @param usrDTO
     * @param login
     * @param password
     * @throws ExceptionFaltaParametro
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionAccesoDatos
     */
    public void modificarClaveUsuario(UsrDTO usrDTO, String login, String password) throws ExceptionFaltaParametro, ExceptionInstanciaNoHallada,
            ExceptionErrorInterno;

    /**
     * Comprueba las credenciales de un usuario.
     *
     * @param usrDTO
     * @param loginDTO
     * @return
     * @throws ExceptionFaltaParametro
     * @throws ExceptionAccesoDatos
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionFormatoDeDatos
     */
    public boolean registro(UsrDTO usrDTO, LoginDTO loginDTO) throws ExceptionFaltaParametro, ExceptionErrorInterno, ExceptionInstanciaNoHallada;

    /**
     *
     * @param usrDTO
     * @param codUsuario
     * @param login
     * @param apellido1
     * @param apellido2
     * @param nombre
     * @param nif
     * @param codPerfil
     * @param busquedaEstricta: booleano que indica si la busqueda se hace con igual (true) o con LIKE (false)
     * @return
     * @throws ExceptionFaltaParametro
     * @throws ExceptionAccesoDatos
     * @throws ExceptionFormatoDeDatos
     */
    public List buscarUsuarios(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, boolean busquedaEstricta) throws ExceptionFaltaParametro, ExceptionErrorInterno, ExceptionFormatoDeDatos, ExceptionInstanciaNoHallada;

    /**
     *
     * @param usrDTO
     * @param codUsuario
     * @param login
     * @param apellido1
     * @param apellido2
     * @param nombre
     * @param nif
     * @param codPerfil
     * @param order
     * @param orderType
     * @param busquedaEstricta
     * @return
     * @throws ExceptionFaltaParametro
     * @throws ExceptionErrorInterno
     * @throws ExceptionFormatoDeDatos
     * @throws ExceptionInstanciaNoHallada
     */
    public List buscarUsuarios(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, String order, String orderType, boolean busquedaEstricta) throws ExceptionFaltaParametro, ExceptionErrorInterno, ExceptionFormatoDeDatos, ExceptionInstanciaNoHallada;

   /**
    *
    * @param usrDTO
    * @return
    * @throws ExceptionFaltaParametro
    * @throws ExceptionAccesoDatos
    * @throws ExceptionFormatoDeDatos
    */
   public List buscarTodos(UsrDTO usrDTO) throws ExceptionErrorInterno;


    /**
     * Devuelve el usuario en la B.D. identificado por ese login.
     *
     * @param usrDTO
     * @param login
     * @return
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionFaltaParametro
     * @throws ExceptionAccesoDatos
     * @throws ExceptionFormatoDeDatos
     */
    public UsuarioDTO buscarUsuario(UsrDTO usrDTO, String login) throws ExceptionInstanciaNoHallada, ExceptionFaltaParametro, ExceptionErrorInterno,
            ExceptionFormatoDeDatos;

    /**
     * Elimina el usuario identificado por ese login.
     *
     * @param usrDTO
     * @param login
     * @throws ExceptionFaltaParametro
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionErrorInterno
     */
    public void eliminarUsuarioporLogin(UsrDTO usrDTO, String login) throws ExceptionFaltaParametro, ExceptionInstanciaNoHallada,
            ExceptionErrorInterno;

    /**
     * Busca usuarios en la B.D. por distintos filtros de búsqueda.
     *
     * @param usr
     * @param codPerfil
     * @param startIndex
     * @param count
     * @param busquedaEstricta: booleano que indica si la busqueda se hace con igual (true) o con LIKE (false)
     * @return
     * @throws ExceptionErrorInterno
     * @throws ExceptionFaltaParametro
     * @throws ExceptionFormatoDeDatos
     */
    public UsuarioDTOList buscarUsuariosPorPerfil(UsrDTO usr, String codPerfil, int startIndex, int count, boolean busquedaEstricta) throws ExceptionErrorInterno,
            ExceptionFaltaParametro, ExceptionFormatoDeDatos, ExceptionInstanciaNoHallada;

}//interface
