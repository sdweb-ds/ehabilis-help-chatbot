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
import es.sdweb.application.model.dto.PerfilDTO;
import es.sdweb.application.model.dto.PerfilUsuarioDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.util.GestorParametrosConfiguracion;

/**
 * @author Antonio Carro Mariño
 *
 * DAO que opera sobre la tabla usuario_perfil.
 */

public class PerfilUsuarioDAO {

    /**
     * Crea una asociacion en la B.D. entre usuario y perfil.
     *
     * @param usr
     * @param connection
     * @param perfilUsuarioDTO
     * @return
     * @throws ExceptionEjecucionSQL
     * @throws ExceptionInstanciaDuplicada
     */
    public PerfilUsuarioDTO create(UsrDTO usr, Connection connection, PerfilUsuarioDTO perfilUsuarioDTO) throws ExceptionEjecucionSQL,
            ExceptionInstanciaDuplicada {

        int insertedRows;
        String CODIGO_ERROR_PKV = "primary.key.violation";

        Log.logRT(usr.getUsr(), "BEGIN PerfilUsuarioDAO.create()");

        PreparedStatement preparedStatement = null;
        String queryString = "INSERT INTO perfil_usuario (codperfil, codusuario) VALUES (?, ?)";

        try {
            Log.logRT(
                    usr.getUsr(),
                    StringUtil.cat("SQL: ", "; @codPerfil=", perfilUsuarioDTO.getCodPerfil(), queryString, "; @codUsuario=",
                            perfilUsuarioDTO.getCodUsuario()));
            preparedStatement = connection.prepareStatement(queryString);
            int i = 1;
            preparedStatement.setString(i++, perfilUsuarioDTO.getCodPerfil());
            preparedStatement.setString(i++, perfilUsuarioDTO.getCodUsuario());

            Log.logRT(usr.getUsr(), "Ejecutando la SQL...");
            insertedRows = preparedStatement.executeUpdate();

            if (insertedRows == 0) {
                Log.logRT(usr.getUsr(), "error insertedRows == 0");
                Log.logRT(usr.getUsr(), "END UsuarioPerfilDAO.create()");
                throw new ExceptionEjecucionSQL(null, queryString, PerfilUsuarioDAO.class.getName(), "create", 1);
            }

            Log.logRT(usr.getUsr(), "END PerfilUsuarioDAO.create()");
            return perfilUsuarioDTO;

        } catch (SQLException e) {
            int codigoError = Integer.valueOf(GestorParametrosConfiguracion.getParametro(CODIGO_ERROR_PKV)).intValue();

            if (e.getErrorCode() == codigoError) {
                Log.logRE(usr.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                        "Error en la inserción de perfilElemento:" + e.getMessage());
                throw new ExceptionInstanciaDuplicada(String.valueOf(perfilUsuarioDTO.getCodUsuario()) + " / " + perfilUsuarioDTO.getCodPerfil(),
                        PerfilUsuarioDAO.class.getName());

            } else {
                Log.logRE(usr.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                        "Error en la inserción de PerfilUsuarioDTO:" + e.getMessage());

            }
            throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDAO.class.getName(), "create", e.getErrorCode());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDAO.class.getName(), "create", e.getErrorCode());
            }
        }
    }

    /**
     * Actualiza la asociacion entre un usuario y un perfil ante cambios en el identificador del registro de elemento.
     *
     * @param usr
     * @param connection
     * @param codPerfilViejo
     * @param codPerfil
     * @throws ExceptionEjecucionSQL
     * @throws ExceptionInstanciaDuplicada
     * @throws ExceptionInstanciaNoHallada
     */
    public void updateCodPerfil(UsrDTO usr, Connection connection, String codPerfilViejo, String codPerfil) throws ExceptionEjecucionSQL,
            ExceptionInstanciaDuplicada, ExceptionInstanciaNoHallada {

        int insertedRows;
        Log.logRT(usr.getUsr(), "BEGIN PerfilUsuarioDAO.updateCodPerfil()");

        PreparedStatement preparedStatement = null;
        String queryString = "UPDATE perfil_usuario " + " SET codPerfil = ? " + " WHERE codPerfil = ?";

        try {
            int i = 1;
            preparedStatement = connection.prepareStatement(queryString);
            SQLUtil.setStringUpper(preparedStatement, i++, codPerfil);
            SQLUtil.setStringUpper(preparedStatement, i++, codPerfilViejo);

            Log.logRT(usr.getUsr(), "Ejecutando la SQL...");
            insertedRows = preparedStatement.executeUpdate();

            if (insertedRows == 0) {
                throw new ExceptionInstanciaNoHallada(codPerfil, PerfilDTO.class.getName());
            }

            Log.logRT(usr.getUsr(), "END PerfilUsuarioDAO.updateCodPerfil()");

        } catch (SQLException e) {
            throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDAO.class.getName(), "updateCodPerfil", e.getErrorCode());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDAO.class.getName(), "updateCodPerfil", e.getErrorCode());
            }
        }
    }

    /**
     * Elimina una asociacion entre usuario y perfil.
     *
     * @param usr
     * @param connection
     * @param perfilUsuarioDTO
     * @throws ExceptionEjecucionSQL
     * @throws ExceptionInstanciaNoHallada
     */
    public void remove(UsrDTO usr, Connection connection, PerfilUsuarioDTO perfilUsuarioDTO) throws ExceptionEjecucionSQL,
            ExceptionInstanciaNoHallada {

        Log.logRT(usr.getUsr(), "BEGIN PerfilUsuarioDAO.remove()");

        PreparedStatement preparedStatement = null;
        String queryString = "DELETE FROM perfil_usuario WHERE codPerfil = ? AND codUsuario = ?";

        try {
            Log.logRT(
                    usr.getUsr(),
                    StringUtil.cat("SQL: ", "; @codPerfil=", perfilUsuarioDTO.getCodPerfil(), queryString, "; @codUsuario=",
                            perfilUsuarioDTO.getCodUsuario()));
            preparedStatement = connection.prepareStatement(queryString);

            int i = 1;
            preparedStatement.setString(i++, perfilUsuarioDTO.getCodPerfil());
            preparedStatement.setString(i++, perfilUsuarioDTO.getCodUsuario());

            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new ExceptionInstanciaNoHallada(perfilUsuarioDTO.getCodPerfil() + "/" + perfilUsuarioDTO.getCodUsuario(), PerfilUsuarioDAO.class.getName());
            }

        } catch (SQLException e) {
            throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDAO.class.getName(), "remove", e.getErrorCode());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDAO.class.getName(), "remove", e.getErrorCode());
            }
        }
    }

    /**
     * Busca la lista de perfiles asociados a un usuario y devuelve el resultado.
     *
     * @param usr
     * @param connection
     * @param codUsuario
     * @param startIndex
     * @param count
     * @return
     * @throws ExceptionEjecucionSQL
     */
    public List perfilesAsociadosAUsuario(UsrDTO usr, Connection connection, String codUsuario) throws ExceptionEjecucionSQL {

        Log.logRT(usr.getUsr(), "BEGIN PerfilUsuarioDAO.perfilesAsociadosAUsuario()");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String queryString = "SELECT p.codPerfil, p.nombre, p.descripcion FROM perfil_usuario pu, perfil p WHERE"
                + " pu.codUsuario = ? AND pu.codPerfil = p.codPerfil ORDER BY codPerfil";

        try {
            List resultDTOs = new ArrayList();
            preparedStatement = connection.prepareStatement(queryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            int i = 1;
            preparedStatement.setString(i++, codUsuario);

            Log.logRT(usr.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                i = 1;
                String codPerfil = resultSet.getString(i++);
                String nomPerfil = resultSet.getString(i++);
                String desPerfil = resultSet.getString(i++);
                resultDTOs.add(new PerfilDTO(codPerfil, nomPerfil, desPerfil));
            }

            if (resultDTOs.isEmpty()) {
                Log.logRT(usr.getUsr(), "No se encontró ningún perfil que esté asociado al usuario con código: " + codUsuario);
            }

            Log.logRT(usr.getUsr(), "END PerfilUsuarioDAO.perfilesAsociadosAUsuario()");

            /* Return the value object. */
            return resultDTOs;

        } catch (SQLException e) {
            throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDAO.class.getName(), "perfilesAsociadosAUsuario", e.getErrorCode());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDAO.class.getName(), "perfilesAsociadosAUsuario", e.getErrorCode());
            }
        }
    }

    /**
     * Busca la lista de perfiles no asociados a un usuario y devuelve el resultado indexado.
     *
     * @param usr
     * @param connection
     * @param codUsuario
     * @param startIndex
     * @param count
     * @return
     * @throws ExceptionEjecucionSQL
     */
    public List perfilesNoAsociadosAUsuario(UsrDTO usr, Connection connection, String codUsuario) throws ExceptionEjecucionSQL {

        Log.logRT(usr.getUsr(), "BEGIN PerfilUsuarioDAO.perfilesNoAsociadosAUsuario()");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // Create "preparedStatement".
        String queryString = "SELECT DISTINCT p.codPerfil, p.nombre, p.descripcion FROM perfil_usuario pu, perfil p WHERE"
                + " p.codPerfil NOT IN (SELECT p2.codPerfil FROM perfil_usuario pu2, perfil p2 WHERE pu2.codUsuario = ?"
                + " AND pu2.codPerfil = p2.codPerfil)" + " ORDER BY p.codPerfil";

        try {
            List resultDTOs = new ArrayList();
            preparedStatement = connection.prepareStatement(queryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            int i = 1;
            preparedStatement.setString(i++, codUsuario);

            Log.logRT(usr.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                i = 1;
                String codPerfil = resultSet.getString(i++);
                String nomPerfil = resultSet.getString(i++);
                String desPerfil = resultSet.getString(i++);
                resultDTOs.add(new PerfilDTO(codPerfil, nomPerfil, desPerfil));
            }

            if (resultDTOs.isEmpty()) {
                Log.logRT(usr.getUsr(), "No se encontró ningún perfil que no esté asociado al usuario con código: " + codUsuario);
            }

            Log.logRT(usr.getUsr(), "END PerfilUsuarioDAO.perfilesNoAsociadosAUsuario()");

            /* Return the value object. */
            return resultDTOs;

        } catch (SQLException e) {
            throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDAO.class.getName(), "perfilesNoAsociadosAUsuario", e.getErrorCode());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDAO.class.getName(), "perfilesNoAsociadosAUsuario", e.getErrorCode());
            }
        }
    }

    /**
     * Comprueba si existe alguna asociación de algún perfil con un usuario determinado.
     *
     * @param usr
     * @param codUsuario
     * @param connection
     * @return
     * @throws ExceptionEjecucionSQL
     */
    public boolean existsCodUsuario(UsrDTO usr, String codUsuario, Connection connection) throws ExceptionEjecucionSQL {

        Log.logRT(usr.getUsr(), "BEGIN PerfilUsuarioDAO.existsCodUsuario()");

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        String queryString = "";

        try {
            queryString = "SELECT codperfil FROM perfil_usuario WHERE codusuario = ?";

            Log.logRT(usr.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString);

            int i = 1;
            SQLUtil.setString(preparedStatement, i++, codUsuario);

            Log.logRT(usr.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Log.logRT(usr.getUsr(), "Búsqueda satisfactoria.");
                Log.logRT(usr.getUsr(), "END PerfilUsuarioDAO.existsCodUsuario()");
                resultSet.close();
                return true;
            } else {
                Log.logRT(usr.getUsr(), "Búsqueda infructuosa.");
                Log.logRT(usr.getUsr(), "END PerfilUsuarioDAO.existsCodUsuario()");
                resultSet.close();
                return false;
            }

        } catch (SQLException e) {
            Log.logRE(usr.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                    "usr al comprobar si existe un usuario:" + e.getMessage());
            throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDTO.class.getName(), "existsCodUsuario()", e.getErrorCode());
        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

    /**
     * Comprueba si un determinado perfil tiene usuarios asociados
     *
     * @param usr
     * @param connection
     * @param codPerfil
     * @return
     * @throws ExceptionEjecucionSQL
     */
    public boolean perfilTieneUsuarios(UsrDTO usr, Connection connection, String codPerfil) throws ExceptionEjecucionSQL {

        Log.logRT(usr.getUsr(), "BEGIN PerfilUsuarioDAO.perfilTieneUsuarios()");

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        String queryString = "";
        boolean result = false;

        try {
            queryString = "SELECT codperfil FROM perfil_usuario WHERE codperfil = ?";

            Log.logRT(usr.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString);

            int i = 1;
            SQLUtil.setString(preparedStatement, i++, codPerfil);

            Log.logRT(usr.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = true;
            } else {
                result = false;
            }

            resultSet.close();

            Log.logRT(usr.getUsr(), "END PerfilUsuarioDAO.perfilTieneUsuarios()");

            return result;

        } catch (SQLException e) {
            Log.logRE(usr.getUsr(), ".perfilTieneUsuarios()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                    "Error al comprobar si un determinado perfil tiene usuarios asociados:" + e.getMessage());
            throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDTO.class.getName(), "perfilTieneUsuarios()", e.getErrorCode());
        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

    /**
     * Elimina todos los asociaciones de perfiles con un usuario determinado.
     *
     * @param usrDTO
     * @param codUsuario
     * @param connection
     * @throws ExceptionAccesoDatos
     * @throws ExceptionInstanciaNoHallada
     */

    public void removeUsuario(UsrDTO usrDTO, String codUsuario, Connection connection) throws ExceptionAccesoDatos, ExceptionInstanciaNoHallada {

        Log.logRT(usrDTO.getUsr(), "BEGIN PerfilUsuarioDAO.removeUsuario()");

        int removedRows = 0;
        PreparedStatement preparedStatement = null;
        String queryString = "";

        try {
            queryString = "DELETE FROM perfil_usuario WHERE codusuario = ?";

            Log.logRT(usrDTO.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString);

            int i = 1;
            SQLUtil.setString(preparedStatement, i++, codUsuario);

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            removedRows = preparedStatement.executeUpdate();
            if (removedRows == 0) {
                throw new ExceptionInstanciaNoHallada(codUsuario, PerfilUsuarioDAO.class.getName());
            }

            Log.logRT(usrDTO.getUsr(), "END PerfilUsuarioDAO.removeUsuario()");

        } catch (SQLException e) {
            Log.logRE(usrDTO.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                    "Error en la eliminación de los perfiles asignados a un usuario:" + e.getMessage());
            throw new ExceptionEjecucionSQL(e, queryString, PerfilUsuarioDTO.class.getName(), "eliminarUsuario()", e.getErrorCode());
        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

}
