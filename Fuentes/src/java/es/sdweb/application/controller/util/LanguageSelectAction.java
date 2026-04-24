package es.sdweb.application.controller.util;

import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actions.util.BaseAction;
import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.dto.UsuarioDTO;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.model.exceptions.ExceptionFormatoDeDatos;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionUniqueLogin;
import es.sdweb.application.model.facade.FabricaFacades;
import es.sdweb.application.model.facade.IGestionUsuarioFacade;
import es.sdweb.application.util.ExceptionFaltaParametro;
import es.sdweb.application.vista.util.ElementosMenu;
import es.sdweb.application.vista.util.GestorInformacionWeb;

public class LanguageSelectAction extends BaseAction {

	@Override
	public ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		String locale = GestorInformacionWeb.getAttribute(httpServletRequest, "idioma");

		if (locale != null) {
			setSessionLocale(httpServletRequest, locale);

			UsrDTO usrDTO = this.getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

			try {
				IGestionUsuarioFacade iGestionUsuariosFacade = FabricaFacades.createGestionUsuarioFacade();

				// Actualizar el idioma del usuario en BBDD
				UsuarioDTO usuarioDTO = iGestionUsuariosFacade.buscarUsuario(usrDTO, usrDTO.getUsr());
				usuarioDTO.setIdioma(locale);
				iGestionUsuariosFacade.modificarUsuario(usrDTO, usuarioDTO, null);

				// Regenerar el menú usando el locale seleccionado
				List elementosArbol = iGestionUsuariosFacade.accesoMenu(usrDTO, usrDTO.getUsr());

				String menu = (ElementosMenu.getInstance()).getArbolMenu(httpServletRequest.getContextPath(), elementosArbol, new Locale(locale.split("_")[0],
						locale.split("_")[1]));

				this.getSessionContainer(httpServletRequest).setMenu(menu);

			} catch (ExceptionUniqueLogin e) {
				Log.logRE(usrDTO.getUsr(), "LanguageSelectAction.executeAction()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
						"El login que ha introducido ya está siendo utilizado por otro usuario");
			} catch (ExceptionInstanciaDuplicada e) {
				Log.logRE(usrDTO.getUsr(), "LanguageSelectAction.executeAction()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
						"Instancia duplicada.");
			} catch (ExceptionFaltaParametro e) {
				Log.logRE(usrDTO.getUsr(), "LanguageSelectAction.executeAction()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4203,
						"Falta alguno de los parámetros necesarios.");
			} catch (ExceptionInstanciaNoHallada e) {
				Log.logRE(usrDTO.getUsr(), "LanguageSelectAction.executeAction()", Log.TIPO_APLICACION, Log.CRITICIDAD_NORMAL, 4202,
						"No se ha encontrado el login buscado en la BD.");
			} catch (ExceptionFormatoDeDatos e) {
				Log.logRE(usrDTO.getUsr(), "LanguageSelectAction.executeAction()", Log.TIPO_APLICACION, Log.CRITICIDAD_NORMAL, 4202,
						"Error de formato de datos.");
			} catch (ExceptionErrorInterno e) {
				httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
				Log.logRE(usrDTO.getUsr(), "LanguageSelectAction.executeAction()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4204,
						"Ha fallado el establecimiento de la conexión a la BD. Revise la configuración de los Data Sources requeridos por GESCUL.");
			}
		}

		return actionMapping.findForward("Principal");
	}
}