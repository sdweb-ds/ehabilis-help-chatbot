package es.sdweb.application.model.dal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.sdweb.application.componentes.util.CifradorRijndael;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.exceptions.ExceptionEjecucionSQL;
import es.sdweb.application.model.dal.util.SQLUtil;
import es.sdweb.application.model.dto.PerfilDTO;
import es.sdweb.application.model.dto.PerfilUsuarioDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.dto.UsuarioDTO;
import es.sdweb.application.model.exceptions.ExceptionFormatoDeDatos;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionUniqueLogin;
import es.sdweb.application.util.ExceptionFaltaParametro;
import es.sdweb.application.util.GestorParametrosConfiguracion;

/**
 * @author Antonio Carro Mariño
 *
 * DAO que opera sobre la tabla usuario.
 */

public class UsuarioDAO {

    public UsuarioDTO create(UsrDTO usrDTO, UsuarioDTO usuarioDTO, Connection connection) throws ExceptionEjecucionSQL {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.create()");

        int insertedRows;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatementID = null;
        ResultSet resultSetID = null;

        try {
            String queryStringID = "SELECT MAX(codusuario) FROM usuario ";
            preparedStatementID = connection.prepareStatement(queryStringID);
            resultSetID = preparedStatementID.executeQuery();
            resultSetID.next();
            Integer codigo = resultSetID.getInt(1) + 1;
            String sCodigo = StringUtil.fillWithZero(codigo.toString(),10); //El código de usuario como string

            String queryString = "INSERT INTO usuario (codusuario,idioma,login,password,apellido1,apellido2,nombre,telefono,correo_elec,nif)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)";

            Log.logRT(usrDTO.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString);

            int i = 1;
            SQLUtil.setString(preparedStatement, i++, sCodigo);
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getIdioma());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getLogin().toUpperCase());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getPassword());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getApellido1().toUpperCase());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getApellido2().toUpperCase());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getNombre().toUpperCase());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getTelefono().toUpperCase());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getCorreo().trim());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getNif().toUpperCase());

            insertedRows = preparedStatement.executeUpdate();
            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");

            if (insertedRows == 0) {
                Log.logRT(usrDTO.getUsr(), "error insertedRows == 0");
                Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.create()");
                throw new ExceptionEjecucionSQL(null, queryString, PerfilDAO.class.getName(), "create", 1);
            }

            usuarioDTO.setCodUsuario(sCodigo);

            resultSetID.close();

            Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.create()");

            return usuarioDTO;

        } catch (SQLException e) {
            Log.logRE(usrDTO.getUsr(), ".create()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(), "Error en la inserción de usuario:" + e.getMessage());
            throw new ExceptionEjecucionSQL(e, UsuarioDAO.class.getName());

        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
            SQLUtil.cierraPreparedStatement(preparedStatementID);
        }
    }

    /*****************************************************************************************************************************/

    //Si el parametro perfilesDTO es null no crea la asociacion de usuario con perfiles
    public UsuarioDTO createUsuarioPerfiles(UsrDTO usrDTO, UsuarioDTO usuarioDTO, List perfilesDTOs, Connection connection)
            throws ExceptionAccesoDatos, ExceptionInstanciaDuplicada, ExceptionFaltaParametro, ExceptionInstanciaNoHallada, ExceptionUniqueLogin {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.createUsuarioPerfiles()");

        UsuarioDTO resultadoU;

        if (perfilesDTOs != null) {
            PerfilDAO perfilDAO = new PerfilDAO();

            boolean correcto = true;
            Iterator ite = perfilesDTOs.iterator();

            while (ite.hasNext()) {
                String codPerfil = ((PerfilDTO) ite.next()).getCodPerfil();
                correcto = correcto && perfilDAO.exists(usrDTO, connection, codPerfil);
                if (!correcto) {
                    throw new ExceptionInstanciaNoHallada(codPerfil, PerfilDTO.class.getName());
                }
            }

            //La unicidad del login se comprueba a nivel de B.D. y se encapsula en la ExceptionUnique
            correcto = correcto && (!existeLogin(usrDTO, usuarioDTO.getLogin(), connection));
            if (!correcto) {
                throw new ExceptionUniqueLogin(UsuarioDAO.class.getName(), "login duplicado", usuarioDTO.getLogin());
            }

            resultadoU = create(usrDTO, usuarioDTO, connection);

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            ite = perfilesDTOs.iterator();

            while (ite.hasNext()) {
                PerfilDTO perfilDTO = (PerfilDTO) ite.next();
                PerfilUsuarioDTO perfilUsuarioDTO = new PerfilUsuarioDTO(perfilDTO.getCodPerfil(), usuarioDTO.getCodUsuario());
                perfilUsuarioDTO = new PerfilUsuarioDAO().create(usrDTO, connection, perfilUsuarioDTO);
            }

        } else {
            boolean correcto = true;
            correcto = correcto && (!existeLogin(usrDTO, usuarioDTO.getLogin(), connection));

            if (!correcto) {
                throw new ExceptionUniqueLogin(UsuarioDAO.class.getName(), "login duplicado", usuarioDTO.getLogin());
            }

            resultadoU = create(usrDTO, usuarioDTO, connection);

        }

        Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.createUsuarioPerfiles()");
        return resultadoU;
    }

    /*****************************************************************************************************************************/

    public void modificarUsuario(UsrDTO usrDTO, UsuarioDTO usuarioDTO, List perfilesDTOs, Connection connection) throws ExceptionInstanciaNoHallada,
            ExceptionAccesoDatos, ExceptionFaltaParametro, ExceptionInstanciaDuplicada, ExceptionUniqueLogin {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.modificarUsuario()");

        if (perfilesDTOs != null) {

            boolean correcto = exists(usrDTO, usuarioDTO.getCodUsuario(), connection);

            if (!correcto) {
                throw new ExceptionInstanciaNoHallada(usuarioDTO.getCodUsuario(), UsuarioDTO.class.getName());
            }

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            update(usrDTO, usuarioDTO, connection);

            boolean existeUsuario = new PerfilUsuarioDAO().existsCodUsuario(usrDTO, usuarioDTO.getCodUsuario(), connection);

            if (existeUsuario) {
                new PerfilUsuarioDAO().removeUsuario(usrDTO, usuarioDTO.getCodUsuario(), connection);
            }

            Iterator ite = perfilesDTOs.iterator();
            while (ite.hasNext()) {
                PerfilDTO perfilDTO = (PerfilDTO) ite.next();
                PerfilUsuarioDTO perfilUsuarioDTO = new PerfilUsuarioDTO(perfilDTO.getCodPerfil(), usuarioDTO.getCodUsuario());
                new PerfilUsuarioDAO().create(usrDTO, connection, perfilUsuarioDTO);
            }

            Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.modificarUsuario()");
        } else {
            boolean correcto = exists(usrDTO, usuarioDTO.getCodUsuario(), connection);

            if (!correcto) {
                throw new ExceptionInstanciaNoHallada(usuarioDTO.getCodUsuario(), UsuarioDTO.class.getName());
            }
            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            update(usrDTO, usuarioDTO, connection);

        }
    }

    /*****************************************************************************************************************************/

    public void modificarClaveUsuario(UsrDTO usrDTO, String login, String password, Connection connection) throws ExceptionInstanciaNoHallada,
            ExceptionEjecucionSQL {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.modificarClaveUsuario()");

        int updatedRows;
        PreparedStatement preparedStatement = null;
        String queryString = "";

        String claveCifrada = CifradorRijndael.cifrar(password);

        try {
            queryString = "UPDATE usuario SET password=? WHERE login=?";

            Log.logRT(usrDTO.getUsr(), StringUtil.cat("SQL: ", queryString, " login=", login, "passwordNueva=password=", password));

            preparedStatement = connection.prepareStatement(queryString);

            //Rellenar PreparedStatement
            int i = 1;
            SQLUtil.setString(preparedStatement, i++, claveCifrada);
            SQLUtil.setString(preparedStatement, i++, login.trim().toUpperCase());

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new ExceptionInstanciaNoHallada(login, UsuarioDTO.class.getName());
            }

            Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.modificarClaveUsuario()");

        } catch (SQLException e) {
            Log.logRE(usrDTO.getUsr(), "modificarClaveUsuario()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                    "Error en la modificación de la clave de usuario:" + e.getMessage());
            throw new ExceptionEjecucionSQL(e, queryString, UsuarioDTO.class.getName(), "modificarClaveUsuario()", e.getErrorCode());
        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

    /*****************************************************************************************************************************/

    public UsuarioDTO findByLogin(UsrDTO usrDTO, String login, Connection connection) throws ExceptionInstanciaNoHallada, ExceptionEjecucionSQL {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.findByLogin()");
        UsuarioDTO result = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String queryString = "";

        try {
            queryString = "SELECT codusuario,idioma,password,apellido1,apellido2,nombre,telefono,correo_elec,nif FROM usuario WHERE login=?";

            Log.logRT(usrDTO.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString);

            int i = 1;
            SQLUtil.setString(preparedStatement, i++, login.trim().toUpperCase());

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new ExceptionInstanciaNoHallada(String.valueOf(login), UsuarioDTO.class.getName());
            }

            //Obtener valores
            i = 1;

            String codUsuarioAux = resultSet.getString(i++);
            if (codUsuarioAux != null)
                codUsuarioAux.trim().toUpperCase();
            String idioma = resultSet.getString(i++);
            String password = resultSet.getString(i++);
            String apellido1 = resultSet.getString(i++);
            if (apellido1 != null)
                apellido1.trim().toUpperCase();
            String apellido2 = resultSet.getString(i++);
            if (apellido2 != null)
                apellido2.trim().toUpperCase();
            String nombre = resultSet.getString(i++);
            if (nombre != null)
                nombre.trim().toUpperCase();
            String telefono = resultSet.getString(i++);
            if (telefono != null)
                telefono.trim().toUpperCase();
            String correo = resultSet.getString(i++);
            String nif = resultSet.getString(i++);
            if (nif != null)
                nif.trim().toUpperCase();

            result = new UsuarioDTO(codUsuarioAux, idioma, login, password, apellido1, apellido2, nombre, telefono, correo, nif);
            resultSet.close();

            Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.findByLogin()");
            return result;

        } catch (SQLException e) {
            Log.logRE(usrDTO.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(), "Error en la busqueda de usuario:"
                    + e.getMessage());
            throw new ExceptionEjecucionSQL(e, queryString, UsuarioDAO.class.getName(), "findByLogin()", e.getErrorCode());

        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

    /*****************************************************************************************************************************/

    public void update(UsrDTO usrDTO, UsuarioDTO usuarioDTO, Connection connection) throws ExceptionInstanciaNoHallada, ExceptionEjecucionSQL,
            ExceptionUniqueLogin {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.update()");
        int updatedRows;
        PreparedStatement preparedStatement = null;
        String queryString = "";

        try {

            queryString = "UPDATE usuario "
                    + "SET idioma=?,login=?,password=?,apellido1=?,apellido2=?,nombre=?,telefono=?,correo_elec=?,nif=? WHERE codUsuario=?";

            Log.logRT(usrDTO.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString);

            int i = 1;
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getIdioma());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getLogin().trim().toUpperCase());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getPassword());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getApellido1().trim().toUpperCase());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getApellido2().trim().toUpperCase());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getNombre().trim().toUpperCase());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getTelefono().trim().toUpperCase());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getCorreo().trim());
            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getNif().trim().toUpperCase());

            SQLUtil.setString(preparedStatement, i++, usuarioDTO.getCodUsuario().trim().toUpperCase());

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new ExceptionInstanciaNoHallada(String.valueOf(usuarioDTO.getCodUsuario()), UsuarioDTO.class.getName());
            }

            Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.update()");

        } catch (SQLException e) {
            int codigoError = Integer.valueOf(GestorParametrosConfiguracion.getParametro("primary.key.violation")).intValue();
            if (e.getErrorCode() == codigoError) {
                Log.logRE(usrDTO.getUsr(), ".update()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                        "Error en la inserción de usuario:" + e.getMessage());
                throw new ExceptionUniqueLogin(UsuarioDAO.class.getName(), "login duplicado", usuarioDTO.getLogin());
            } else {
                Log.logRE(usrDTO.getUsr(), ".update()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                        "Error en la inserción de usuario:" + e.getMessage());
                throw new ExceptionEjecucionSQL(e, UsuarioDAO.class.getName());
            }
        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

    /*****************************************************************************************************************************/

    public List findUsuarios(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, int startIndex, int count, Connection connection) throws ExceptionEjecucionSQL, ExceptionFormatoDeDatos,
            ExceptionInstanciaNoHallada {

        return findUsuarios(usrDTO, codUsuario, login, apellido1, apellido2, nombre, nif, codPerfil, null, null, startIndex, count, connection);
    }

    public List findUsuarios(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, String order, String orderType, int startIndex, int count, Connection connection) throws ExceptionEjecucionSQL, ExceptionFormatoDeDatos,
            ExceptionInstanciaNoHallada {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.findUsuarios()");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String queryString = "";
        String stringAux = "";
        List result = new ArrayList();
        UsuarioDTO usrAux = null;

        try {
            String stringSelect;
            String stringFrom;

            if (StringUtil.isEmpty(codPerfil)) {
                stringSelect = "SELECT u.codusuario,u.idioma,u.login,u.password,u.apellido1,u.apellido2,u.nombre,u.telefono,u.nif ";
                stringFrom = " FROM usuario u";
            } else {
                stringSelect = "SELECT u.codusuario,u.idioma,u.login,u.password,u.apellido1,u.apellido2,u.nombre,u.telefono,u.nif ";
                stringFrom = " FROM usuario u,perfil_usuario pu";
            }

            String stringWhere = "";
            boolean soloWhere = true;
            if (!StringUtil.isEmpty(codUsuario)) {
                if (soloWhere == true) {
                    stringAux = " codusuario=? ";
                    soloWhere = false;
                } else {
                    stringAux = " AND codusuario=? ";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(login)) {
                if (soloWhere == true) {
                    stringAux = " login=? ";
                    soloWhere = false;
                } else {
                    stringAux = " AND login=? ";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(apellido1)) {
                if (soloWhere == true) {
                    stringAux = " apellido1=? ";
                    soloWhere = false;
                } else {
                    stringAux = " AND apellido1=? ";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(apellido2)) {
                if (soloWhere == true) {
                    stringAux = " apellido2=? ";
                    soloWhere = false;
                } else {
                    stringAux = " AND apellido2=? ";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(nombre)) {
                if (soloWhere == true) {
                    stringAux = " nombre=? ";
                    soloWhere = false;
                } else {
                    stringAux = " AND nombre=? ";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(codPerfil)) {
                if (soloWhere == true) {
                    stringAux = " u.codUsuario = pu.codUsuario AND pu.codPerfil = ? ";
                    soloWhere = false;
                } else {
                    stringAux = " AND u.codUsuario = pu.codUsuario AND pu.codPerfil = ? ";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(nif)) {
                if (soloWhere == true) {
                    stringAux = " nif=? ";
                    soloWhere = false;
                } else {
                    stringAux = " AND nif=? ";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }

            if(!soloWhere) {
            	stringWhere = StringUtil.cat(" WHERE ", stringWhere);
            }

            String stringOrderBy = "";
            if(order != null && orderType != null) {
            	stringOrderBy = StringUtil.cat(" ORDER BY ", order, " ", orderType);
            }

            queryString = StringUtil.cat(stringSelect, stringFrom, stringWhere, stringOrderBy);

            Log.logRT(usrDTO.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            int i = 1;
            if (!StringUtil.isEmpty(codUsuario)) {
                SQLUtil.setString(preparedStatement, i++, codUsuario.trim().toUpperCase());
            }
            if (!StringUtil.isEmpty(login)) {
                SQLUtil.setString(preparedStatement, i++, login.trim().toUpperCase());
            }
            if (!StringUtil.isEmpty(apellido1)) {
                SQLUtil.setString(preparedStatement, i++, apellido1.trim().toUpperCase());
            }
            if (!StringUtil.isEmpty(apellido2)) {
                SQLUtil.setString(preparedStatement, i++, apellido2.trim().toUpperCase());
            }
            if (!StringUtil.isEmpty(nombre)) {
                SQLUtil.setString(preparedStatement, i++, nombre.trim().toUpperCase());
            }
            if (!StringUtil.isEmpty(codPerfil)) {
                SQLUtil.setString(preparedStatement, i++, codPerfil.trim().toUpperCase());
            }
            if (!StringUtil.isEmpty(nif)) {
                SQLUtil.setString(preparedStatement, i++, nif.trim().toUpperCase());
            }

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();

            int contador = 0;
            if (!resultSet.next()) {
                throw new ExceptionInstanciaNoHallada(String.valueOf(codUsuario), UsuarioDAO.class.getName());
            }
            if ((startIndex >= 1) && resultSet.absolute(startIndex)) {
                do {

                    i = 1;
                    String codUsuarioAux = resultSet.getString(i++);
                    if (codUsuarioAux != null)
                        codUsuarioAux = codUsuarioAux.trim().toUpperCase();
                    String idIdiomaAux = resultSet.getString(i++);
                    String loginAux = resultSet.getString(i++);
                    if (loginAux != null)
                        loginAux = loginAux.trim().toUpperCase();
                    String passwordAux = resultSet.getString(i++);
                    String apellido1Aux = resultSet.getString(i++);
                    if (apellido1Aux != null)
                        apellido1Aux = apellido1Aux.trim().toUpperCase();
                    String apellido2Aux = resultSet.getString(i++);
                    if (apellido2Aux != null)
                        apellido2Aux = apellido2Aux.trim().toUpperCase();
                    String nombreAux = resultSet.getString(i++);
                    if (nombreAux != null)
                        nombreAux = nombreAux.trim().toUpperCase();
                    String telefonoAux = resultSet.getString(i++);
                    if (telefonoAux != null)
                        telefonoAux = telefonoAux.trim().toUpperCase();
                    String nifAux = resultSet.getString(i++);
                    if (nifAux != null)
                        nifAux = nifAux.trim().toUpperCase();

                    usrAux = new UsuarioDTO(codUsuarioAux, idIdiomaAux, loginAux, passwordAux, apellido1Aux, apellido2Aux, nombreAux, telefonoAux,
                            "", nifAux);
                    result.add(usrAux);
                    contador++;

                } while ((resultSet.next()) && (contador < count));
            }

            resultSet.close();

            Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.findUsuarios()");
            return result;

        } catch (SQLException e) {
            Log.logRE(usrDTO.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                    "Error en la búsqueda de usuarios:" + e.getMessage());
            throw new ExceptionEjecucionSQL(e, queryString, UsuarioDTO.class.getName(), "findUsuarios()", e.getErrorCode());
        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

    /*****************************************************************************************************************************/

    public List findAll(UsrDTO usrDTO, Connection connection) throws ExceptionEjecucionSQL {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.findAll()");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String queryString = "";
        String stringAux = "";
        List result = new ArrayList();
        UsuarioDTO usrAux = null;

        try {
            String stringSelect;
            String stringFrom;

            //if (StringUtil.isEmpty(codPerfil)) {
                stringSelect = "SELECT u.codusuario,u.idioma,u.login,u.apellido1,u.apellido2,u.nombre,u.telefono,u.nif ";
                stringFrom = " FROM usuario u";
            /*} else {
                stringSelect = "SELECT u.codusuario,u.idioma,u.login,u.password,u.apellido1,u.apellido2,u.nombre,u.telefono,u.nif ";
                stringFrom = " FROM usuario u,perfil_usuario pu";
            }*/

            queryString = StringUtil.cat(stringSelect, stringFrom);

            Log.logRT(usrDTO.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            int i = 1;

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();

            	while (resultSet.next()) {

                   i = 1;
                    String codUsuarioAux = resultSet.getString(i++);
                    /*if (codUsuarioAux != null)
                        codUsuarioAux = codUsuarioAux.trim().toUpperCase();*/
                    String idIdiomaAux = resultSet.getString(i++);
                    String loginAux = resultSet.getString(i++);
                    /*if (loginAux != null)
                        loginAux = loginAux.trim().toUpperCase();*/
                    String apellido1Aux = resultSet.getString(i++);
                    /*if (apellido1Aux != null)
                        apellido1Aux = apellido1Aux.trim().toUpperCase();*/
                    String apellido2Aux = resultSet.getString(i++);
                    /*if (apellido2Aux != null)
                        apellido2Aux = apellido2Aux.trim().toUpperCase();*/
                    String nombreAux = resultSet.getString(i++);
                    /*if (nombreAux != null)
                        nombreAux = nombreAux.trim().toUpperCase();*/
                    String telefonoAux = resultSet.getString(i++);
                    /*if (telefonoAux != null)
                        telefonoAux = telefonoAux.trim().toUpperCase();*/
                    String nifAux = resultSet.getString(i++);
                    /*if (nifAux != null)
                        nifAux = nifAux.trim().toUpperCase();*/

                    usrAux = new UsuarioDTO("", idIdiomaAux, loginAux, "", apellido1Aux, apellido2Aux, nombreAux, telefonoAux,
                            "", nifAux);
                    result.add(usrAux);

                }


            resultSet.close();

            Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.findAll()");
            return result;

        } catch (SQLException e) {
            Log.logRE(usrDTO.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                    "Error en la búsqueda de usuarios:" + e.getMessage());
            throw new ExceptionEjecucionSQL(e, queryString, UsuarioDTO.class.getName(), "findAll()", e.getErrorCode());
        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

    /*****************************************************************************************************************************/

    public List<UsuarioDTO> findUsuariosLike(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, int startIndex, int count, Connection connection) throws ExceptionEjecucionSQL, ExceptionFormatoDeDatos,
            ExceptionInstanciaNoHallada {

        return findUsuariosLike(usrDTO, codUsuario, login, apellido1, apellido2, nombre, nif, codPerfil, null, null, startIndex, count, connection);
    }

    public List<UsuarioDTO> findUsuariosLike(UsrDTO usrDTO, String codUsuario, String login, String apellido1, String apellido2, String nombre, String nif,
            String codPerfil, String order, String orderType, int startIndex, int count, Connection connection) throws ExceptionEjecucionSQL, ExceptionFormatoDeDatos,
            ExceptionInstanciaNoHallada {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.findUsuariosLike()");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String queryString = "";
        String stringAux = "";
        List<UsuarioDTO> result = new ArrayList<UsuarioDTO>();
        UsuarioDTO usrAux = null;

        try {
            String stringSelect;
            String stringFrom;

            if (StringUtil.isEmpty(codPerfil)) {
                stringSelect = "SELECT u.codusuario,u.idioma,u.login,u.password,u.apellido1,u.apellido2,u.nombre,u.telefono,u.nif";
                stringFrom = " FROM usuario u";
            } else {
                stringSelect = "SELECT u.codusuario,u.idioma,u.login,u.password,u.apellido1,u.apellido2,u.nombre,u.telefono,u.nif";
                stringFrom = " FROM usuario u, perfil_usuario pu";
            }

            String stringWhere = "";
            boolean soloWhere = true;
            if (!StringUtil.isEmpty(codUsuario)) {
                if (soloWhere == true) {
                    stringAux = " codusuario = ?";
                    soloWhere = false;
                } else {
                    stringAux = " AND codusuario = ?";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(login)) {
                if (soloWhere == true) {
                    stringAux = " login LIKE ?";
                    soloWhere = false;
                } else {
                    stringAux = " AND login LIKE ?";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(apellido1)) {
                if (soloWhere == true) {
                    stringAux = " apellido1 LIKE ?";
                    soloWhere = false;
                } else {
                    stringAux = " AND apellido1 LIKE ?";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(apellido2)) {
                if (soloWhere == true) {
                    stringAux = " apellido2 LIKE ?";
                    soloWhere = false;
                } else {
                    stringAux = " AND apellido2 LIKE ?";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(nombre)) {
                if (soloWhere == true) {
                    stringAux = " nombre LIKE ?";
                    soloWhere = false;
                } else {
                    stringAux = " AND nombre LIKE ?";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(codPerfil)) {
                if (soloWhere == true) {
                    stringAux = " u.codUsuario = pu.codUsuario AND pu.codPerfil = ?";
                    soloWhere = false;
                } else {
                    stringAux = " AND u.codUsuario = pu.codUsuario AND pu.codPerfil = ?";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }
            if (!StringUtil.isEmpty(nif)) {
                if (soloWhere == true) {
                    stringAux = " nif = ?";
                    soloWhere = false;
                } else {
                    stringAux = " AND nif = ?";
                }
                stringWhere = StringUtil.cat(stringWhere, stringAux);
            }

            if(!soloWhere) {
            	stringWhere = StringUtil.cat(" WHERE", stringWhere);
            }

            String stringOrderBy = "";
            if(order != null && orderType != null) {
            	stringOrderBy = StringUtil.cat(" ORDER BY ", order, " ", orderType);
            }

            queryString = StringUtil.cat(stringSelect, stringFrom, stringWhere, stringOrderBy);

            Log.logRT(usrDTO.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            int i = 1;
            if (!StringUtil.isEmpty(codUsuario)) {
                SQLUtil.setString(preparedStatement, i++, codUsuario.trim().toUpperCase());
            }
            if (!StringUtil.isEmpty(login)) {
                SQLUtil.setString(preparedStatement, i++, "%" + login.trim().toUpperCase() + "%");
            }
            if (!StringUtil.isEmpty(apellido1)) {
                SQLUtil.setString(preparedStatement, i++, "%" + apellido1.trim().toUpperCase() + "%");
            }
            if (!StringUtil.isEmpty(apellido2)) {
                SQLUtil.setString(preparedStatement, i++, "%" + apellido2.trim().toUpperCase() + "%");
            }
            if (!StringUtil.isEmpty(nombre)) {
                SQLUtil.setString(preparedStatement, i++, "%" + nombre.trim().toUpperCase() + "%");
            }
            if (!StringUtil.isEmpty(codPerfil)) {
                SQLUtil.setString(preparedStatement, i++, codPerfil.trim().toUpperCase());
            }
            if (!StringUtil.isEmpty(nif)) {
                SQLUtil.setString(preparedStatement, i++, nif.trim().toUpperCase());
            }

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();

            int contador = 0;
            if (!resultSet.next()) {
                throw new ExceptionInstanciaNoHallada(String.valueOf(codUsuario), UsuarioDAO.class.getName());
            }

            if ((startIndex >= 1) && resultSet.absolute(startIndex)) {
                do {
                    i = 1;

                    String codUsuarioAux = resultSet.getString(i++);
                    if (codUsuarioAux != null) {
                        codUsuarioAux = codUsuarioAux.trim().toUpperCase();
                    }

                    String idIdiomaAux = resultSet.getString(i++);
                    String loginAux = resultSet.getString(i++);
                    if (loginAux != null) {
                        loginAux = loginAux.trim().toUpperCase();
                    }

                    String passwordAux = resultSet.getString(i++);
                    String apellido1Aux = resultSet.getString(i++);
                    if (apellido1Aux != null) {
                        apellido1Aux = apellido1Aux.trim().toUpperCase();
                    }

                    String apellido2Aux = resultSet.getString(i++);
                    if (apellido2Aux != null) {
                        apellido2Aux = apellido2Aux.trim().toUpperCase();
                    }

                    String nombreAux = resultSet.getString(i++);
                    if (nombreAux != null) {
                        nombreAux = nombreAux.trim().toUpperCase();
                    }

                    String telefonoAux = resultSet.getString(i++);
                    if (telefonoAux != null) {
                        telefonoAux = telefonoAux.trim().toUpperCase();
                    }

                    String nifAux = resultSet.getString(i++);
                    if (nifAux != null) {
                        nifAux = nifAux.trim().toUpperCase();
                    }

                    usrAux = new UsuarioDTO(codUsuarioAux, idIdiomaAux, loginAux, passwordAux, apellido1Aux, apellido2Aux, nombreAux, telefonoAux, "", nifAux);
                    result.add(usrAux);
                    contador++;

                } while ((resultSet.next()) && (contador < count));
            }

            resultSet.close();

            Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.findUsuariosLike()");
            return result;

        } catch (SQLException e) {
            Log.logRE(usrDTO.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                    "Error en la búsqueda de usuarios:" + e.getMessage());
            throw new ExceptionEjecucionSQL(e, queryString, UsuarioDTO.class.getName(), "findUsuariosLike()", e.getErrorCode());
        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

    /*****************************************************************************************************************************/

    public boolean exists(UsrDTO usrDTO, String codUsuario, Connection connect) throws ExceptionEjecucionSQL {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.exists()");

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        String queryString = "";

        try {
            queryString = "SELECT codusuario FROM usuario WHERE codUsuario = ?";

            Log.logRT(usrDTO.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connect.prepareStatement(queryString);

            int i = 1;
            SQLUtil.setString(preparedStatement, i++, codUsuario.trim().toUpperCase());

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Log.logRT(usrDTO.getUsr(), "Búsqueda satisfactoria.");
                Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.exists()");
                resultSet.close();
                return true;
            } else {
                Log.logRT(usrDTO.getUsr(), "Búsqueda infructuosa.");
                Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.exists()");
                resultSet.close();
                return false;
            }

        } catch (SQLException e) {
            Log.logRE(usrDTO.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                    "Error al comprobar si existe usuario:" + e.getMessage());
            throw new ExceptionEjecucionSQL(e, queryString, UsuarioDTO.class.getName(), "exists()", e.getErrorCode());
        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

    /*****************************************************************************************************************************/

    public boolean existeLogin(UsrDTO usrDTO, String login, Connection connection) throws ExceptionEjecucionSQL {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.existeLogin()");

        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        String queryString = "";

        try {
            queryString = "SELECT codusuario FROM usuario WHERE login = ?";

            Log.logRT(usrDTO.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString);

            int i = 1;
            SQLUtil.setString(preparedStatement, i++, login.trim().toUpperCase());

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Log.logRT(usrDTO.getUsr(), "Búsqueda satisfactoria.");
                Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.existeLogin()");
                resultSet.close();
                return true;
            } else {
                Log.logRT(usrDTO.getUsr(), "Búsqueda infructuosa.");
                Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.existeLogin()");
                resultSet.close();
                return false;
            }

        } catch (SQLException e) {
            Log.logRE(usrDTO.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                    "Error al comprobar si existe usuario con el mismo Login:" + e.getMessage());
            throw new ExceptionEjecucionSQL(e, queryString, UsuarioDTO.class.getName(), "existeLogin()", e.getErrorCode());
        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

    /*****************************************************************************************************************************/

    public void removeByLogin(UsrDTO usrDTO, String login, Connection connection) throws ExceptionInstanciaNoHallada, ExceptionEjecucionSQL {

        Log.logRT(usrDTO.getUsr(), "BEGIN UsuarioDAO.removeByLogin()");

        int removedRows;
        PreparedStatement preparedStatement = null;
        String queryString = "";

        try {
            queryString = "DELETE FROM usuario WHERE login=?";

            Log.logRT(usrDTO.getUsr(), StringUtil.cat("SQL: ", queryString));
            preparedStatement = connection.prepareStatement(queryString);

            int i = 1;
            SQLUtil.setString(preparedStatement, i++, login.trim().toUpperCase());

            Log.logRT(usrDTO.getUsr(), "Ejecutando la SQL...");
            removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new ExceptionInstanciaNoHallada(String.valueOf(login), UsuarioDTO.class.getName());
            }

            Log.logRT(usrDTO.getUsr(), "END UsuarioDAO.removeByLogin()");

        } catch (SQLException e) {
            Log.logRE(usrDTO.getUsr(), ".execute()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, e.getErrorCode(),
                    "Error en la eliminación de usuario:" + e.getMessage());
            throw new ExceptionEjecucionSQL(e, queryString, UsuarioDTO.class.getName(), "removeByLogin()", e.getErrorCode());
        } finally {
            SQLUtil.cierraPreparedStatement(preparedStatement);
        }
    }

}
