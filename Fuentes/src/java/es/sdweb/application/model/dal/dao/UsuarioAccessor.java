package es.sdweb.application.model.dal.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.dal.FabricaGestorConexiones;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.exceptions.ExceptionErrorConexion;
import es.sdweb.application.model.dal.exceptions.ExceptionSQL;
import es.sdweb.application.model.dal.facade.IUsuarioDAO;
import es.sdweb.application.model.dal.util.SQLUtil;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.dto.UsuarioDTO;
import es.sdweb.application.model.exceptions.ExceptionFormatoDeDatos;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionUniqueLogin;
import es.sdweb.application.util.ExceptionFaltaParametro;

/**
 * @author Antonio Carro Mariño
 *
 * Clase que gestiona las conexiones y transacciones de las tablas usuario.
 */
public class UsuarioAccessor implements IUsuarioDAO {

    @Override
    public UsuarioDTO createUsuarioPerfiles(UsrDTO usrDTO, UsuarioDTO usuarioDTO, List perfilesDTOs) throws ExceptionAccesoDatos,
            ExceptionInstanciaDuplicada, ExceptionFaltaParametro, ExceptionInstanciaNoHallada, ExceptionUniqueLogin {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioAccesor.createUsuarioPerfiles()");

        FabricaGestorConexiones gestorConexiones = FabricaGestorConexiones.getInstance();

        boolean rollback = false;
        Connection connection = null;
        UsuarioDTO result;

        try {

            Log.logRT(usrDTO.getUsr(), "Obteniendo una conexión...");
            connection = gestorConexiones.getConexion();
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);
            Log.logRT(usrDTO.getUsr(), "Llamando al DAO...");
            result = new UsuarioDAO().createUsuarioPerfiles(usrDTO, usuarioDTO, perfilesDTOs, connection);

        } catch (ExceptionInstanciaDuplicada e) {
            rollback = true;
            throw e;
        } catch (ExceptionFaltaParametro e) {
            rollback = true;
            throw e;
        } catch (ExceptionInstanciaNoHallada e) {
            rollback = true;
            throw e;
        } catch (ExceptionUniqueLogin e) {
            rollback = true;
            throw e;
        } catch (SQLException e) {
            rollback = true;
            throw new ExceptionSQL(e);

        } catch (ExceptionAccesoDatos e) {
            rollback = true;
            throw e;

        } catch (RuntimeException e) {
            rollback = true;
            throw e;

        } catch (Error e) {
            rollback = true;
            throw e;

        } finally {
            try {
                if (connection != null) {
                    if (rollback) {
                        connection.rollback();
                    } else {
                        connection.commit();
                    }
                    SQLUtil.cierraConexion(connection);
                }
            } catch (SQLException e) {
                throw new ExceptionAccesoDatos(UsuarioAccessor.class.getName(), "error al cerrar la conexion");
            }

        }

        Log.logRT(usrDTO.getUsr(), "END UsuarioAccesor.createUsuarioPerfiles()");
        return result;
    }

    @Override
    public void modificarUsuario(UsrDTO usrDTO, UsuarioDTO usuarioDTO, List perfilesDTOs) throws ExceptionFaltaParametro, ExceptionAccesoDatos,
            ExceptionInstanciaNoHallada, ExceptionInstanciaDuplicada, ExceptionUniqueLogin {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioAccesor.modificarUsuario()");

        FabricaGestorConexiones gestorConexiones = FabricaGestorConexiones.getInstance();

        boolean rollback = false;
        Connection connection = null;

        try {

            Log.logRT(usrDTO.getUsr(), "Obteniendo una conexión...");
            connection = gestorConexiones.getConexion();
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);
            Log.logRT(usrDTO.getUsr(), "Llamando al DAO...");
            new UsuarioDAO().modificarUsuario(usrDTO, usuarioDTO, perfilesDTOs, connection);

        } catch (ExceptionUniqueLogin e) {
            rollback = true;
            throw e;
        } catch (ExceptionFaltaParametro e) {
            rollback = true;
            throw e;
        } catch (ExceptionInstanciaNoHallada e) {
            rollback = true;
            throw e;
        } catch (ExceptionInstanciaDuplicada e) {
            rollback = true;
            throw e;
        } catch (SQLException e) {
            rollback = true;
            throw new ExceptionSQL(e);

        } catch (ExceptionAccesoDatos e) {
            rollback = true;
            throw e;

        } catch (RuntimeException e) {
            rollback = true;
            throw e;

        } catch (Error e) {
            rollback = true;
            throw e;

        } finally {
            try {
                if (connection != null) {
                    if (rollback) {
                        connection.rollback();
                    } else {
                        connection.commit();
                    }
                    SQLUtil.cierraConexion(connection);
                }
            } catch (SQLException e) {
                throw new ExceptionAccesoDatos(UsuarioAccessor.class.getName(), "error al cerrar la conexion");
            }
        }

        Log.logRT(usrDTO.getUsr(), "END UsuarioAccesor.modificarUsuario()");
    }

    @Override
    public void modificarClaveUsuario(UsrDTO usrDTO, String login, String password) throws ExceptionInstanciaNoHallada, ExceptionErrorConexion,
            ExceptionAccesoDatos {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioAccesor.modificarClaveUsuario()");
        FabricaGestorConexiones gestorConexiones = FabricaGestorConexiones.getInstance();
        Connection connection = null;

        try {
            Log.logRT(usrDTO.getUsr(), "Obteniendo una conexión...");
            connection = gestorConexiones.getConexion();

            Log.logRT(usrDTO.getUsr(), "Llamando al DAO...");
            new UsuarioDAO().modificarClaveUsuario(usrDTO, login, password, connection);

        } finally {
            SQLUtil.cierraConexion(connection);
        }

        Log.logRT(usrDTO.getUsr(), "END UsuarioAccesor.modificarClaveUsuario()");
    }

    @Override
    public boolean existeLogin(UsrDTO usrDTO, String login) throws ExceptionAccesoDatos {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioAccesor.existeLogin()");
        FabricaGestorConexiones gestorConexiones = FabricaGestorConexiones.getInstance();

        Connection connection = null;
        boolean result;

        try {
            Log.logRT(usrDTO.getUsr(), "Obteniendo una conexión...");

            connection = gestorConexiones.getConexion();

            Log.logRT(usrDTO.getUsr(), "Llamando al DAO...");
            result = new UsuarioDAO().existeLogin(usrDTO, login, connection);

        } finally {
            SQLUtil.cierraConexion(connection);
        }

        Log.logRT(usrDTO.getUsr(), "END UsuarioAccesor.existeLogin()");
        return result;
    }

    @Override
    public UsuarioDTO findByLogin(UsrDTO usrDTO, String login) throws ExceptionInstanciaNoHallada, ExceptionAccesoDatos {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioAccesor.findByLogin()");
        FabricaGestorConexiones gestorConexiones = FabricaGestorConexiones.getInstance();

        Connection connection = null;
        UsuarioDTO result = null;

        try {
            Log.logRT(usrDTO.getUsr(), "Obteniendo una conexión...");
            connection = gestorConexiones.getConexion();

            Log.logRT(usrDTO.getUsr(), "Llamando al DAO...");
            result = new UsuarioDAO().findByLogin(usrDTO, login, connection);

        } finally {
            SQLUtil.cierraConexion(connection);
        }

        Log.logRT(usrDTO.getUsr(), "END UsuarioAccesor.findByLogin()");
        return result;
    }

    @Override
    public List findByCampos(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, int startIndex, int cont, boolean busquedaEstricta) throws ExceptionAccesoDatos, ExceptionFormatoDeDatos, ExceptionInstanciaNoHallada {

        return findByCampos(usrDTO, codUsuario, login, apellido1, apellido2, nombre, nif, codPerfil, null, null, startIndex, cont, busquedaEstricta);
    }

    @Override
	public List findByCampos(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, String order, String orderType, int startIndex, int cont, boolean busquedaEstricta) throws ExceptionAccesoDatos, ExceptionFormatoDeDatos, ExceptionInstanciaNoHallada {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioAccesor.findByCampos()");
        FabricaGestorConexiones gestorConexiones = FabricaGestorConexiones.getInstance();

        Connection connection = null;
        List result;

        try {
            Log.logRT(usrDTO.getUsr(), "Obteniendo una conexión...");
            connection = gestorConexiones.getConexion();

            Log.logRT(usrDTO.getUsr(), "Llamando al DAO...");
            if (!busquedaEstricta) {
                result = new UsuarioDAO().findUsuariosLike(usrDTO, codUsuario, login, apellido1, apellido2, nombre, nif, codPerfil, order, orderType, startIndex, cont, connection);
            } else {
                result = new UsuarioDAO().findUsuarios(usrDTO, codUsuario, login, apellido1, apellido2, nombre, nif, codPerfil, order, orderType, startIndex, cont, connection);
            }

        } finally {
            SQLUtil.cierraConexion(connection);
        }

        Log.logRT(usrDTO.getUsr(), "END UsuarioAccesor.findByCampos()");
        return result;
    }

    @Override
	public List findAll(UsrDTO usrDTO) throws ExceptionAccesoDatos {
    	Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioAccesor.findAll()");
        FabricaGestorConexiones gestorConexiones = FabricaGestorConexiones.getInstance();

        Connection connection = null;
        List result;

        try {
            Log.logRT(usrDTO.getUsr(), "Obteniendo una conexión...");
            connection = gestorConexiones.getConexion();

            Log.logRT(usrDTO.getUsr(), "Llamando al DAO...");
            result = new UsuarioDAO().findAll(usrDTO, connection);

        } finally {
            SQLUtil.cierraConexion(connection);
        }

        Log.logRT(usrDTO.getUsr(), "END UsuarioAccesor.findAll()");
        return result;
	}

    @Override
    public void removeByLogin(UsrDTO usrDTO, String login) throws ExceptionAccesoDatos, ExceptionInstanciaNoHallada {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioAccesor.removeByLogin()");
        FabricaGestorConexiones gestorConexiones = FabricaGestorConexiones.getInstance();
        Connection connection = null;

        try {
            Log.logRT(usrDTO.getUsr(), "Obteniendo una conexión...");
            connection = gestorConexiones.getConexion();

            Log.logRT(usrDTO.getUsr(), "Llamando al DAO...");
            new UsuarioDAO().removeByLogin(usrDTO, login, connection);

        } catch (ExceptionAccesoDatos e) {
            throw e;

        } finally {
            SQLUtil.cierraConexion(connection);
        }

        Log.logRT(usrDTO.getUsr(), "END UsuarioAccesor.removeByLogin()");
    }

} //class
