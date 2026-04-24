package es.sdweb.application.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.controller.util.SessionContainer;

/**
 * Clase Helper para el Filtro de control de acceso
 * @author sdweb
 *
 */
public class LoginHelper {
	/**
	 * Comprueba si el usuario esta validado (existe un usuario en el SessionContainer)
	 * @param request
	 * @return True/False dependiendo de si el usuario está validado o no
	 */
	protected static boolean isLoggedIn(HttpServletRequest request) {
		SessionContainer container = getSessionContainer(request);

		if (container.getUserContainer() != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Devuelve un objeto SessionContainer.
	 * @param request
	 * @return El objeto SessionContainer de la request enviada
	 */
	protected static SessionContainer getSessionContainer(HttpServletRequest request) {
		SessionContainer sessionContainer = null;
		sessionContainer = (SessionContainer) getSessionObject(request, IConstantes.SES_SESSION_CONTAINER);

		// Crea un SessionContainer para a sesion se ainda non existe
		if (sessionContainer == null) {
			sessionContainer = new SessionContainer();

			HttpSession session = request.getSession(true);
			session.setAttribute(IConstantes.SES_SESSION_CONTAINER, sessionContainer);

		}
		return sessionContainer;
	}

	/**
	 * Obtener un objeto de la sesión
	 * @param req
	 * @param attrName
	 * @return El objeto requerido
	 */
	protected static Object getSessionObject(HttpServletRequest req, String attrName) {
		Object sessionObj = null;
		HttpSession session = req.getSession(false);
		if (session != null) {
			sessionObj = session.getAttribute(attrName);
		}
		return sessionObj;
	}
}
