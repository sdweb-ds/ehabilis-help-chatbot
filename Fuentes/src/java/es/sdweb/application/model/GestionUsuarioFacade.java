package es.sdweb.application.model;

import java.util.ArrayList;
import java.util.List;

import es.sdweb.application.componentes.util.CifradorRijndael;
import es.sdweb.application.model.dal.facade.FabricaDAOs;
import es.sdweb.application.model.dal.facade.IElementoDAO;
import es.sdweb.application.model.dal.facade.IPerfilDAO;
import es.sdweb.application.model.dal.facade.IPerfilUsuarioDAO;
import es.sdweb.application.model.dal.facade.IUsuarioDAO;
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
import es.sdweb.application.model.facade.IGestionUsuarioFacade;
import es.sdweb.application.util.ExceptionFaltaParametro;

/**
 *
 * @author Antonio Carro Mariño
 *
 */
public class GestionUsuarioFacade implements IGestionUsuarioFacade {

    /**************************************DAOs SOBRE ELEMENTO ********************************/

    @Override
    public List permisos(UsrDTO usrDTO, String login) throws ExceptionInstanciaNoHallada, ExceptionErrorInterno, ExceptionFaltaParametro {
        IElementoDAO iElementoDAO = FabricaDAOs.createElementoDAO();
        return iElementoDAO.permisos(usrDTO, login);
    }

    @Override
    public List accesoBotones(UsrDTO usrDTO, String login) throws ExceptionFaltaParametro, ExceptionErrorInterno, ExceptionInstanciaNoHallada {

        List arbolBD = new ArrayList();
        IElementoDAO iElementoDAO = FabricaDAOs.createElementoDAO();
        arbolBD = iElementoDAO.findArbol(usrDTO, login, "B");
        return arbolBD;

    }

    @Override
    public List accesoMenu(UsrDTO usrDTO, String login) throws ExceptionErrorInterno, ExceptionInstanciaNoHallada {

        List arbolBD = new ArrayList();
        IElementoDAO iElementoDAO = FabricaDAOs.createElementoDAO();
        arbolBD = iElementoDAO.findArbol(usrDTO, login, "M");
        return arbolBD;

    }

    @Override
    public List arbolElementos(UsrDTO usrDTO, String codPerfil) throws ExceptionErrorInterno, ExceptionInstanciaNoHallada {

        List arbolBD = new ArrayList();
        IElementoDAO iElementoDAO = FabricaDAOs.createElementoDAO();
        arbolBD = iElementoDAO.findArbolCheckPerfil(usrDTO, codPerfil);
        return arbolBD;

    }

    /**************************************DAOs SOBRE PERFILES ********************************/

    @Override
    public List buscarPerfiles(UsrDTO usr) throws ExceptionErrorInterno, ExceptionFaltaParametro {

        try {
            IPerfilDAO iPerfilDAO = FabricaDAOs.createPerfilDAO();
            List resultado;

            resultado = iPerfilDAO.findAll(usr);
            return resultado;

        } catch (ExceptionErrorInterno e) {
            throw e;
        }
    }

    @Override
    public void eliminarPerfil(UsrDTO usrDTO, String codPerfil) throws ExceptionFaltaParametro, ExceptionInstanciaNoHallada, ExceptionErrorInterno,
            ExceptionInstanciaReferenciada {

        IPerfilDAO iPerfilDAO = FabricaDAOs.createPerfilDAO();
        iPerfilDAO.remove(usrDTO, codPerfil);
    }

    @Override
    public PerfilDTO crearPerfilElementos(UsrDTO usrDTO, PerfilDTO perfilDTO, List elementoArbol) throws ExceptionErrorInterno,
            ExceptionInstanciaDuplicada, ExceptionFaltaParametro, ExceptionInstanciaNoHallada {

        IPerfilDAO iPerfilDAO = FabricaDAOs.createPerfilDAO();
        PerfilDTO resultado = iPerfilDAO.createPerfilElementos(usrDTO, perfilDTO, elementoArbol);
        return resultado;
    }

    @Override
    public PerfilDTO updatePerfilElementos(UsrDTO usrDTO, String codPerfil, PerfilDTO perfilDTO, List elementoArbol) throws ExceptionErrorInterno,
            ExceptionInstanciaDuplicada, ExceptionFaltaParametro, ExceptionInstanciaNoHallada {

        IPerfilDAO iPerfilDAO = FabricaDAOs.createPerfilDAO();
        PerfilDTO resultado = iPerfilDAO.updatePerfilElementos(usrDTO, codPerfil, perfilDTO, elementoArbol);
        return resultado;
    }

    /************************* DAOs SOBRE LA ASOCIACIÓN ENTRE PERFILES Y USUARIOS ********************/

    @Override
    public List PerfilesNoAsociadosAUsuario(UsrDTO usr, String codUsuario) throws ExceptionErrorInterno, ExceptionFaltaParametro {

        try {
            IPerfilUsuarioDAO iPerfilUsuarioDAO = FabricaDAOs.createPerfilUsuarioDAO();
            List resultado = new ArrayList();

            resultado = iPerfilUsuarioDAO.perfilesNoAsociadosAUsuario(usr, codUsuario);
            return resultado;

        } catch (ExceptionErrorInterno e) {
            throw e;
        }
    }

    @Override
    public List PerfilesAsociadosAUsuario(UsrDTO usr, String codUsuario) throws ExceptionErrorInterno, ExceptionFaltaParametro {

        try {
            IPerfilUsuarioDAO iPerfilUsuarioDAO = FabricaDAOs.createPerfilUsuarioDAO();
            List resultado = new ArrayList();

            resultado = iPerfilUsuarioDAO.perfilesAsociadosAUsuario(usr, codUsuario);
            return resultado;

        } catch (ExceptionErrorInterno e) {
            throw e;
        }
    }

    /**************************************DAOs SOBRE USUARIOS ********************************/

    @Override
    public UsuarioDTO buscarUsuario(UsrDTO usrDTO, String login) throws ExceptionInstanciaNoHallada, ExceptionFaltaParametro, ExceptionErrorInterno,
            ExceptionFormatoDeDatos {
        IUsuarioDAO iUsuarioDAO = FabricaDAOs.createUsuarioDAO();
        UsuarioDTO usuarioDTO = iUsuarioDAO.findByLogin(usrDTO, login);
        usuarioDTO.setPassword(CifradorRijndael.descifrar(usuarioDTO.getPassword()));
        return usuarioDTO;
    }

    /**
     * Busca un usuario en la BD con el login y password que se le pasa en el formulario de entrada.
     * Devuelve true si el usuario existe.
     *
     * @param usrDTO Datos del usuario que esta realizando la autenticacion.
     * @param loginDTO Datos del usuario que esta realizando la autenticacion.
     * @return True si el usuario se ha autenticado, false en caso contrario.
     * @throws ExceptionFaltaParametro
     * @throws ExceptionErrorInterno
     * @throws ExceptionInstanciaNoHallada
     */
    @Override
    public boolean registro(UsrDTO usrDTO, LoginDTO loginDTO) throws ExceptionFaltaParametro, ExceptionErrorInterno, ExceptionInstanciaNoHallada {

        IUsuarioDAO iUsuarioDAO = FabricaDAOs.createUsuarioDAO();
        UsuarioDTO usuarioDTO = iUsuarioDAO.findByLogin(usrDTO, loginDTO.getLogin());

        CifradorRijndael cifradorRijndael = new CifradorRijndael();
        String passwordEnClaro = cifradorRijndael.descifrar(usuarioDTO.getPassword());

        if (passwordEnClaro.trim().compareTo(loginDTO.getPassword()) == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List buscarUsuarios(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, boolean busquedaEstricta) throws ExceptionFaltaParametro, ExceptionErrorInterno, ExceptionFormatoDeDatos,
            ExceptionInstanciaNoHallada {
        return buscarUsuarios(usrDTO, codUsuario, login, apellido1, apellido2, nombre, nif, codPerfil, null, null, busquedaEstricta);

    }

    @Override
	public List buscarUsuarios(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, String order, String orderType, boolean busquedaEstricta) throws ExceptionFaltaParametro, ExceptionErrorInterno, ExceptionFormatoDeDatos,
            ExceptionInstanciaNoHallada {

        List usuarioDTOs = new ArrayList();
        List resultado = new ArrayList();
        int startIndex = 1;
        int count = 2;

        IUsuarioDAO iUsuarioDAO = FabricaDAOs.createUsuarioDAO();
        do {
            resultado = iUsuarioDAO.findByCampos(usrDTO, codUsuario, login, apellido1, apellido2, nombre, nif, codPerfil, order, orderType, startIndex, count,
                    busquedaEstricta);
            usuarioDTOs.addAll(resultado);
            startIndex = startIndex + count;
        } while (!resultado.isEmpty());
        return usuarioDTOs;

    }


	@Override
	public List buscarTodos(UsrDTO usrDTO) throws ExceptionErrorInterno {
		List usuarioDTOs = new ArrayList();
        List resultado = new ArrayList();

        IUsuarioDAO iUsuarioDAO = FabricaDAOs.createUsuarioDAO();
        resultado = iUsuarioDAO.findAll(usrDTO);
        usuarioDTOs.addAll(resultado);
        return usuarioDTOs;
	}

    @Override
    public void eliminarUsuarioporLogin(UsrDTO usrDTO, String login) throws ExceptionFaltaParametro, ExceptionInstanciaNoHallada,
            ExceptionErrorInterno {

        IUsuarioDAO iUsuarioDAO = FabricaDAOs.createUsuarioDAO();
        iUsuarioDAO.removeByLogin(usrDTO, login);
    }

    @Override
    public UsuarioDTOList buscarUsuariosPorPerfil(UsrDTO usrDTO, String codPerfil, int startIndex, int count, boolean busquedaEstricta)
            throws ExceptionErrorInterno, ExceptionFaltaParametro, ExceptionFormatoDeDatos, ExceptionInstanciaNoHallada {

        try {
            IUsuarioDAO iUsuarioDAO = FabricaDAOs.createUsuarioDAO();
            List usuarioDTOs = new ArrayList();
            UsuarioDTOList usuarioDTOList;

            if (count == 0) {
                List resultado;
                int cont = 500;
                int start = startIndex;

                do {
                    resultado = iUsuarioDAO.findByCampos(usrDTO, null, null, null, null, null, null, codPerfil, start, cont, busquedaEstricta);
                    usuarioDTOs.addAll(resultado);
                    start = start + cont;
                } while (!resultado.isEmpty());

                usuarioDTOList = new UsuarioDTOList(usuarioDTOs, false);

            } else {
                usuarioDTOs = iUsuarioDAO
                        .findByCampos(usrDTO, null, null, null, null, null, null, codPerfil, startIndex, count + 1, busquedaEstricta);

                if (usuarioDTOs.size() == count + 1) {
                    usuarioDTOs.remove(count);
                    usuarioDTOList = new UsuarioDTOList(usuarioDTOs, true);
                } else {
                    usuarioDTOList = new UsuarioDTOList(usuarioDTOs, false);
                }

            }

            return usuarioDTOList;

        } catch (ExceptionErrorInterno e) {
            throw e;
        }
    }

    @Override
    public UsuarioDTO crearUsuarioPerfiles(UsrDTO usrDTO, UsuarioDTO usuarioDTO, List perfilesDTOs) throws ExceptionFaltaParametro,
            ExceptionErrorInterno, ExceptionInstanciaDuplicada, ExceptionInstanciaNoHallada, ExceptionUniqueLogin {

        IUsuarioDAO iUsuarioDAO = FabricaDAOs.createUsuarioDAO();
        String claveCif = new CifradorRijndael().cifrar(usuarioDTO.getPassword());
        usuarioDTO.setPassword(claveCif);
        UsuarioDTO resultado = iUsuarioDAO.createUsuarioPerfiles(usrDTO, usuarioDTO, perfilesDTOs);

        return resultado;
    }

    @Override
    public void modificarUsuario(UsrDTO usrDTO, UsuarioDTO usuarioDTO, List perfilesDTOs) throws ExceptionFaltaParametro, ExceptionErrorInterno,
            ExceptionInstanciaNoHallada, ExceptionUniqueLogin, ExceptionInstanciaDuplicada {

        IUsuarioDAO iUsuarioDAO = FabricaDAOs.createUsuarioDAO();
        String claveCif = new CifradorRijndael().cifrar(usuarioDTO.getPassword());
        usuarioDTO.setPassword(claveCif);
        iUsuarioDAO.modificarUsuario(usrDTO, usuarioDTO, perfilesDTOs);
    }

    @Override
    public void modificarClaveUsuario(UsrDTO usrDTO, String login, String password) throws ExceptionFaltaParametro, ExceptionInstanciaNoHallada,
            ExceptionErrorInterno {

        IUsuarioDAO iUsuarioDAO = FabricaDAOs.createUsuarioDAO();

        iUsuarioDAO.modificarClaveUsuario(usrDTO, login, password);
    }

}//class