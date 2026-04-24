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
import es.sdweb.application.model.dto.ElementoDetallesDTO;
import es.sdweb.application.model.dto.ElementoDetallesDTOArbol;
import es.sdweb.application.model.dto.ElementoPerfilDTO;
import es.sdweb.application.model.dto.PerfilDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.util.ExceptionFaltaParametro;
import es.sdweb.application.util.GestorParametrosConfiguracion;

/**
 * @author Antonio Carro Mariño
 *
 * DAO que opera sobre la tabla perfil.
 */
public class PerfilDAO {

    /**
     * Crea un perfil en la B.D. y le asocia unos determinados elementos.
     *
     * @param usr
     * @param connection
     * @param perfilDTO
     * @param elementoArbols
     * @return
     * @throws ExceptionAccesoDatos
     * @throws ExceptionInstanciaDuplicada
     * @throws ExceptionFaltaParametro
     * @throws ExceptionInstanciaNoHallada
     */
    public PerfilDTO createPerfilElementos(UsrDTO usr, Connection connection, PerfilDTO perfilDTO, List elementoArbols) throws ExceptionAccesoDatos,
            ExceptionInstanciaDuplicada, ExceptionFaltaParametro, ExceptionInstanciaNoHallada {

        Log.logRT(usr.getUsr(), "BEGIN PerfilDAO.createPerfilElementos()");
        PerfilDAO perfilDAO = new PerfilDAO();

        if (exists(usr, connection, perfilDTO.getCodPerfil())) {
            throw new ExceptionInstanciaDuplicada(String.valueOf(perfilDTO.getCodPerfil()), PerfilDTO.class.getName());

        }
        Log.logRT(usr.getUsr(), "Ejecutando la SQL...");
        PerfilDTO resultado = perfilDAO.create(usr, connection, perfilDTO);
        crearAsociacion(usr, elementoArbols, perfilDTO.getCodPerfil(), connection);

        Log.logRT(usr.getUsr(), "END PerfilDAO.createPerfilElementos()");
        return resultado;
    }

    /**
     * Inserta en la B.D. un nuevo perfil.
     *
     * @param usr
     * @param connection
     * @param perfilDTO
     * @return
     * @throws ExceptionEjecucionSQL
     * @throws ExceptionInstanciaDuplicada
     */
    private PerfilDTO create(UsrDTO usr, Connection connection, PerfilDTO perfilDTO) throws ExceptionEjecucionSQL, ExceptionInstanciaDuplicada {
        String CODIGO_ERROR_PKV = "primary.key.violation";

        Log.logRT(usr.getUsr(), "BEGIN PerfilDAO.create()");

        PreparedStatement preparedStatement = null;
        String queryString = "INSERT INTO perfil (codperfil, nombre, descripcion) " + " VALUES (?, ?, ?)";
        int insertedRows = 0;

        try {
            Log.logRT(usr.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString);

            int i = 1;
            preparedStatement.setString(i++, perfilDTO.getCodPerfil().trim().toUpperCase());
            preparedStatement.setString(i++, perfilDTO.getNomPerfil().trim().toUpperCase());
            preparedStatement.setString(i++, perfilDTO.getDesPerfil().trim().toUpperCase());

            Log.logRT(usr.getUsr(), "Ejecutando la SQL...");
            insertedRows = preparedStatement.executeUpdate();

            if (insertedRows == 0) {
                Log.logRT(usr.getUsr(), "error insertedRows == 0");
                Log.logRT(usr.getUsr(), "END PerfilDAO.create()");
                throw new ExceptionEjecucionSQL(null, queryString, PerfilDAO.class.getName(), "create", 1);
            }

            Log.logRT(usr.getUsr(), "END PerfilDAO.create()");
            return perfilDTO;

        } catch (SQLException e) {
            int codigoError = Integer.valueOf(GestorParametrosConfiguracion.getParametro(CODIGO_ERROR_PKV)).intValue();

            if (e.getErrorCode() == codigoError) {
                Log.logRE(usr.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                        "Error en la inserción de perfil:" + e.getMessage());
                throw new ExceptionInstanciaDuplicada(String.valueOf(perfilDTO.getCodPerfil()), PerfilDTO.class.getName());
            } else {
                Log.logRE(usr.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                        "Error en la inserción de perfil:" + e.getMessage());
                throw new ExceptionEjecucionSQL(e, queryString, PerfilDAO.class.getName(), "create", 0);
            }

        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new ExceptionEjecucionSQL(e, queryString, PerfilDAO.class.getName(), "create", 0);
            }
        }
    }

    /**
     * Crea la asociacion entre el perfil y aquellos elementos que tienen el atributo seleccionado a true.
     *
     * @param usr
     * @param nivel
     * @param codPerfil
     * @param connection
     * @throws ExceptionInstanciaNoHallada
     * @throws ExceptionEjecucionSQL
     * @throws ExceptionInstanciaDuplicada
     */
    private void crearAsociacion(UsrDTO usr, List nivel, String codPerfil, Connection connection) throws ExceptionInstanciaNoHallada,
            ExceptionEjecucionSQL, ExceptionInstanciaDuplicada {
        boolean correcto = true;

        ElementoDAO elementoDAO = new ElementoDAO();
        ElementoPerfilDAO elementoPerfilDAO = new ElementoPerfilDAO();

        for (int i = 0; i < nivel.size(); i++) {
            ElementoDetallesDTOArbol nodo = ((ElementoDetallesDTOArbol) nivel.get(i));

            if (nodo.getSeleccionado().compareTo("true") == 0) {
                ElementoDetallesDTO elemento = nodo.getElementoDetallesDTO();
                correcto = correcto && elementoDAO.exists(usr, connection, elemento.getCodElemento());

                if (!correcto) {
                    throw new ExceptionInstanciaNoHallada(elemento.getCodElemento(), PerfilDAO.class.getName());
                }
                elementoPerfilDAO.create(usr, connection, new ElementoPerfilDTO(elemento.getCodElemento(), codPerfil));

                List siguienteNivel = nodo.getSubArbol();
                if (!siguienteNivel.isEmpty()) {
                    crearAsociacion(usr, siguienteNivel, codPerfil, connection);
                }
            }
        }
    }

    /**
     * Actualiza los valores de un determinado perfil en la B.D. asi como sus asociaciones con los elementos.
     *
     * @param usr
     * @param connection
     * @param codPerfil
     * @param perfilDTO
     * @param elementoArbols
     * @return
     * @throws ExceptionEjecucionSQL
     * @throws ExceptionAccesoDatos
     * @throws ExceptionInstanciaDuplicada
     * @throws ExceptionFaltaParametro
     * @throws ExceptionInstanciaNoHallada
     */
    public PerfilDTO updatePerfilElementos(UsrDTO usr, Connection connection, String codPerfil, PerfilDTO perfilDTO, List elementoArbols)
            throws ExceptionEjecucionSQL, ExceptionAccesoDatos, ExceptionInstanciaDuplicada, ExceptionFaltaParametro, ExceptionInstanciaNoHallada {

        Log.logRT(usr.getUsr(), "BEGIN PerfilDAO.updatePerfilElementos()");

        if (!exists(usr, connection, codPerfil)) {
            throw new ExceptionInstanciaNoHallada(String.valueOf(codPerfil), PerfilDTO.class.getName());
        }

//        PerfilDTO resultado;
        Log.logRT(usr.getUsr(), "Ejecutando la SQL...");

        ElementoPerfilDAO elementoPerfilDAO = new ElementoPerfilDAO();
        elementoPerfilDAO.removePerfil(usr, codPerfil, connection);

        PerfilDTO resultado = perfilDTO;
        update(usr, connection, perfilDTO);

/* BORRAR (el codigo de perfil no se va a poder modificar)
        if (perfilDTO.getCodPerfil().compareTo(codPerfil) == 0) {
            //solo se actualiza la descripcion
            resultado = perfilDTO;
            update(usr, connection, perfilDTO);

        } else {
            resultado = new PerfilDAO().create(usr, connection, perfilDTO);

            try {
                new PerfilUsuarioDAO().updateCodPerfil(usr, connection, codPerfil, perfilDTO.getCodPerfil());
            } catch (ExceptionInstanciaNoHallada e) {
                //No tiene asociado ningun usuario, no supone un problema
            }

            new PerfilDAO().remove(usr, connection, codPerfil);
        }
*/

        crearAsociacion(usr, elementoArbols, perfilDTO.getCodPerfil(), connection);

        Log.logRT(usr.getUsr(), "END PerfilDAO.updatePerfilElementos()");

        return resultado;
    }

    /**
     * Actualiza un perfil en la B.D.
     *
     * @param usr
     * @param connection
     * @param perfilDTO
     * @throws ExceptionEjecucionSQL
     * @throws ExceptionInstanciaNoHallada
     */
    private void update(UsrDTO usr, Connection connection, PerfilDTO perfilDTO) throws ExceptionEjecucionSQL, ExceptionInstanciaNoHallada {

        Log.logRT(usr.getUsr(), "BEGIN PerfilDAO.update()");

        PreparedStatement preparedStatement = null;
        String queryString = "UPDATE perfil SET nombre = ?, descripcion = ? WHERE codperfil = ?";
        int insertedRows = 0;

        try {
            Log.logRT(usr.getUsr(), StringUtil.cat("SQL: ", queryString, "; @codPerfil=", perfilDTO.getCodPerfil()));

            preparedStatement = connection.prepareStatement(queryString);
            int i = 1;
            preparedStatement.setString(i++, perfilDTO.getNomPerfil().trim().toUpperCase());
            preparedStatement.setString(i++, perfilDTO.getDesPerfil().trim().toUpperCase());
            preparedStatement.setString(i++, perfilDTO.getCodPerfil().trim().toUpperCase());

            Log.logRT(usr.getUsr(), "Ejecutando la SQL...");

            insertedRows = preparedStatement.executeUpdate();

            if (insertedRows == 0) {
                throw new ExceptionInstanciaNoHallada(perfilDTO.getCodPerfil(), PerfilDTO.class.getName());
            }

            if (insertedRows > 1) {
                throw new ExceptionEjecucionSQL(null, queryString, PerfilDAO.class.getName(), "update", 2);
            }

            Log.logRT(usr.getUsr(), "END PerfilDAO.update()");

        } catch (SQLException e) {
            throw new ExceptionEjecucionSQL(e, queryString, PerfilDAO.class.getName(), "update", e.getErrorCode());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new ExceptionEjecucionSQL(e, queryString, PerfilDAO.class.getName(), "update", e.getErrorCode());
            }
        }
    }

    /**
     * Comprueba la existencia de un determinado perfil en la B.D.
     *
     * @param usr
     * @param connection
     * @param codPerfil
     * @return
     * @throws ExceptionEjecucionSQL
     */
    public boolean exists(UsrDTO usr, Connection connection, String codPerfil) throws ExceptionEjecucionSQL {

        Log.logRT(usr.getUsr(), "BEGIN PerfilDAO.exists()");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String queryString = "SELECT codPerfil FROM perfil WHERE codPerfil = ?";

        try {
            Log.logRT(usr.getUsr(), StringUtil.cat("SQL: ", queryString, "; @codPerfil=", codPerfil));

            preparedStatement = connection.prepareStatement(queryString);
            int i = 1;
            preparedStatement.setString(i++, codPerfil.trim().toUpperCase());

            Log.logRT(usr.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Log.logRT(usr.getUsr(), "Búsqueda satisfactoria.");
                Log.logRT(usr.getUsr(), "END PerfilDAO.exists()");
                return true;
            } else {
                Log.logRT(usr.getUsr(), "Búsqueda infructuosa.");
                Log.logRT(usr.getUsr(), "END PerfilDAO.exists()");
                return false;
            }

        } catch (SQLException e) {
            throw new ExceptionEjecucionSQL(e, queryString, PerfilDTO.class.getName(), "exists", e.getErrorCode());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new ExceptionEjecucionSQL(e, queryString, PerfilDTO.class.getName(), "exists", e.getErrorCode());
            }

        }
    }

    /**
     * Busca todos los perfiles que se encuentren en la B.D.
     *
     * @param usr
     * @param connection
     * @param startIndex
     * @param count
     * @return
     * @throws ExceptionEjecucionSQL
     */
    public List findAll(UsrDTO usr, Connection connection) throws ExceptionEjecucionSQL {

        Log.logRT(usr.getUsr(), "BEGIN PerfilDAO.findAll()");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List resultDTOs = new ArrayList();

        String queryString = "SELECT codperfil, nombre, descripcion FROM perfil ORDER BY codPerfil";

        try {
            preparedStatement = connection.prepareStatement(queryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            Log.logRT(usr.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();

            int i;
            while (resultSet.next()) {
                i = 1;
                String codPerfil = resultSet.getString(i++).trim().toUpperCase();
                String nomPerfil = resultSet.getString(i++).trim().toUpperCase();
                String desPerfil = resultSet.getString(i++).trim().toUpperCase();
                resultDTOs.add(new PerfilDTO(codPerfil, nomPerfil, desPerfil));
            }

            if (resultDTOs.isEmpty()) {
                Log.logRT(usr.getUsr(), "No se encontró ningún perfil en la base de datos.");
            }

            Log.logRT(usr.getUsr(), "END PerfilDAO.findAll()");

            return resultDTOs;

        } catch (SQLException e) {
            throw new ExceptionEjecucionSQL(e, queryString, PerfilDAO.class.getName(), "findAll", e.getErrorCode());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new ExceptionEjecucionSQL(e, queryString, PerfilDAO.class.getName(), "findAll", e.getErrorCode());
            }
        }
    }

    /**
     * Devuelve los datos (codperfil,descripcion) del perfil profesor
     *
     * @param usr
     * @param connection
     * @return
     * @throws ExceptionEjecucionSQL
     */
    public PerfilDTO findPerfilProfesor(UsrDTO usr, Connection connection, String nombrePerfil) throws ExceptionEjecucionSQL {

        Log.logRT(usr.getUsr(), "BEGIN PerfilDAO.findPerfilProfesor()");
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PerfilDTO result = null;
        String queryString = "SELECT codperfil, nombre, descripcion FROM perfil WHERE descripcion= ? ORDER BY codPerfil";

        try {
            int i = 1;
            preparedStatement = connection.prepareStatement(queryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setString(i++, nombrePerfil.trim().toUpperCase());

            Log.logRT(usr.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                i = 1;
                String codPerfil = resultSet.getString(i++).trim().toUpperCase();
                String nomPerfil = resultSet.getString(i++).trim().toUpperCase();
                String desPerfil = resultSet.getString(i++).trim().toUpperCase();
                result = new PerfilDTO(codPerfil, nomPerfil, desPerfil);
            } else {
                Log.logRT(usr.getUsr(), "No se encontró ningún perfil con el nombre: " + nombrePerfil);
            }

            Log.logRT(usr.getUsr(), "END PerfilDAO.findPerfilProfesor()");

            return result;

        } catch (SQLException e) {
            throw new ExceptionEjecucionSQL(e, queryString, PerfilDAO.class.getName(), "findByAll", e.getErrorCode());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new ExceptionEjecucionSQL(e, queryString, PerfilDAO.class.getName(), "findByAll", e.getErrorCode());
            }
        }
    }

    /**
     * Elimina un perfil de la B.D.
     *
     * @param usr
     * @param connection
     * @param codPerfil
     * @throws ExceptionEjecucionSQL
     * @throws ExceptionInstanciaNoHallada
     */
    public void remove(UsrDTO usr, Connection connection, String codPerfil) throws ExceptionEjecucionSQL {

        Log.logRT(usr.getUsr(), "BEGIN PerfilDAO.remove()");

        PreparedStatement preparedStatement = null;
        int removedRows = 0;
        String queryString = "DELETE FROM perfil WHERE codPerfil = ?";

        try {
            Log.logRT(usr.getUsr(), StringUtil.cat("SQL: ", queryString, "; @codPerfil=", codPerfil));

            preparedStatement = connection.prepareStatement(queryString);

            int i = 1;
            preparedStatement.setString(i++, codPerfil.trim().toUpperCase());

            removedRows = preparedStatement.executeUpdate();

            Log.logRT(usr.getUsr(), "END PerfilDAO.remove()");

        } catch (SQLException e) {
            throw new ExceptionEjecucionSQL(e, queryString, PerfilDTO.class.getName(), "remove", e.getErrorCode());

        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new ExceptionEjecucionSQL(e, queryString, PerfilDTO.class.getName(), "remove", e.getErrorCode());
            }
        }
    }

}//class
