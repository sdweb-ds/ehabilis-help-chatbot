package es.sdweb.application.controller.actions.gestionUsuariosPerfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles.LoginForm;
import es.sdweb.application.controller.actions.util.BaseAction;
import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.controller.util.UserContainer;
import es.sdweb.application.model.dto.ElementoDetallesDTO;
import es.sdweb.application.model.dto.LoginDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.dto.UsuarioDTO;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.facade.FabricaFacades;
import es.sdweb.application.model.facade.IGestionUsuarioFacade;
import es.sdweb.application.util.ExceptionFaltaParametro;
import es.sdweb.application.vista.util.ElementosMenu;

/**
 * @author Antonio Carro Mariño
 *
 * Esta clase tiene como mision realizar la autenticacion y autorizacion del usuario. En caso de que el usuario no cumpla
 * los requisitos exigidos se le denegara el acceso a la aplicacion.
 */
public class AutenticacionAction extends BaseAction {

    @Override
    public ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        UsrDTO usrDTO = new UsrDTO(); // Usuario para los registros de logs
        ActionForward result = null;
        LoginForm loginForm = (LoginForm) actionForm;
        usrDTO.setUsr(loginForm.getLogin().trim().toUpperCase());
        HashMap acceso = new HashMap();
        UserContainer user = new UserContainer();

        Log.logRT(usrDTO.getUsr(), "---------------------------------------------------------------------------------------------------------------------");
        Log.logRT(usrDTO.getUsr(), "BEGIN AutenticacionAction.execute()");

        try {

            LoginDTO loginDTO = new LoginDTO(loginForm.getLogin(), loginForm.getPassword());
            IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();

            if (loginDTO.getPassword().length() < 2) {
                loginForm.setAdvertencia("advertencia.password.longitud.incorrecta");
                return result = actionMapping.findForward("Login");
            }

            if (iGestionUsuarioFacade.registro(usrDTO, loginDTO)) {
                List elementosArbol = iGestionUsuarioFacade.accesoMenu(usrDTO, loginDTO.getLogin());
                List permisos = iGestionUsuarioFacade.permisos(usrDTO, loginDTO.getLogin()); // Obtiene la lista de permisos del usuario.

                // Se guarda en la sesion el login del usuario y los elementos a los que tiene acceso
                accesoPermisos(permisos, acceso); // Guarda la lista de accesos en el HashMap vacio.
                user.setUsuario(usrDTO);
                user.setAcceso(acceso);

                this.getSessionContainer(httpServletRequest).setUserContainer(user);

                // Obtener el Locale del usuario y guardarlo en sesion
                UsuarioDTO usuarioDTO = iGestionUsuarioFacade.buscarUsuario(usrDTO, loginDTO.getLogin());
                setSessionLocale(httpServletRequest, usuarioDTO.getIdioma());
                
                //Creamos el menú con el idioma que tiene seleccionado el usuario
                Locale locale = new Locale(usuarioDTO.getIdioma());
                
                // En esta seccion de codigo se genera el HTML del menu a partir del fichero de traducciones y la ubicacion de los permisos de tipo menu
                String menu = (ElementosMenu.getInstance()).getArbolMenu(httpServletRequest.getContextPath(), elementosArbol, locale);

                this.getSessionContainer(httpServletRequest).setMenu(menu);

                result = actionMapping.findForward("Principal");
            } else {
                loginForm.setAdvertencia("advertencia.password.incorrecto");
                result = actionMapping.findForward("Login");
            }

        } catch (ExceptionErrorInterno e) {
            httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "AutenticacionAction.execute()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201, "Ha fallado el establecimiento de la conexion a la BD.");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);

        } catch (ExceptionInstanciaNoHallada e) {
            loginForm.setAdvertencia("advertencia.login.incorrecto");
            result = actionMapping.findForward("Login");

        } catch (ExceptionFaltaParametro e) {
            httpServletRequest.setAttribute("mensajeError", "AutenticacionAction: " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "AutenticacionAction.execute()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201, "Falta un parametro de configuracion necesario.");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);

        } catch (Exception e) {
            httpServletRequest.setAttribute("mensajeError", "AutenticacionAction: " + e.getMessage());
            Log.logRT(usrDTO.getUsr(), "AutenticacionAction.execute(): [ERROR]: Se ha producido un error en la Autenticacion: "+ e.getMessage());
            Log.logRT(usrDTO.getUsr(), "AutenticacionAction.execute(): [ERROR]: "+ ExceptionUtils.getFullStackTrace(e));
            Log.logRE(usrDTO.getUsr(), "AutenticacionAction.execute()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201, "Ha fallado la autenticacion del usuario.");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
        }

        return result;
    }

    private void accesoPermisos(List nivel, HashMap acceso) {

        for (int i = 0; i < nivel.size(); i++) {
            ElementoDetallesDTO elemento = ((ElementoDetallesDTO) nivel.get(i));
            acceso.put(elemento.getCodElemento().trim(), "true");
        }
    }

}// class
