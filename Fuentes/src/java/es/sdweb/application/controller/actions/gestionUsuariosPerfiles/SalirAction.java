package es.sdweb.application.controller.actions.gestionUsuariosPerfiles;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actions.util.BaseAction;
import es.sdweb.application.model.dto.UsrDTO;

/**
 * Sale de la aplicacion. Invalida la sesion.
 */
public class SalirAction extends BaseAction {

	@Override
	public ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward("Login");
		}

		ActionForward result = null;
		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "---------------------------------------------------------------------------------------------------------------------");
		Log.logRT(usrDTO.getUsr(), "BEGIN Salir.execute()");

		// Recuperar el locale del usuario para mantenerlo despues de eliminar la sesion
		Locale localeUsuario = getSessionLocale(httpServletRequest);

		// Elimina la sesion.
		httpServletRequest.getSession().invalidate();

		// Iniciar la sesion de nuevo y establecer de nuevo el locale del usuario
		httpServletRequest.getSession(true);
		setSessionLocale(httpServletRequest, localeUsuario.getLanguage() + "_" + localeUsuario.getCountry());

		result = actionMapping.findForward("Login");
		Log.logRT(usrDTO.getUsr(), "END Salir.execute()");

		return result;
	}
} // class
