package es.sdweb.application.controller.actions.util;

import es.sdweb.application.componentes.util.StringUtil;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.actions.DispatchAction;

import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.controller.util.ApplicationContainer;
import es.sdweb.application.controller.util.SessionContainer;

public abstract class BaseDispatchAction extends DispatchAction {

	/**
	 * Retorna un objeto de sesion basandose en el request y en el nombre del
	 * atributo.
	 */
	protected Object getSessionObject(HttpServletRequest req, String attrName) {
		Object sessionObj = null;
		HttpSession session = req.getSession(false);
		if (session != null) {
			sessionObj = session.getAttribute(attrName);
		}
		return sessionObj;
	}

	protected void setSessionObject(HttpServletRequest req, String attrName, Object sessionObj) {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.setAttribute(attrName, sessionObj);
		}
	}

	/**
	 * Devuelve el objeto ApplicationContainer.
	 */
	protected ApplicationContainer getApplicationContainer() {
		return (ApplicationContainer) getApplicationObject(IConstantes.APP_APPLICATION_CONTAINER);
	}

	/**
	 * Devuelve el objeto SessionContainer. Lo busca en la sesion y si lo
	 * encuentra lo devuelve. Si no lo encuentra en la sesion, es que no hay
	 * sesion, en cuyo caso se crea y se guarda en ella el objeto
	 * SessionContainer. El objeto SessionContainer contendra todos aquellos
	 * datos que deban ser almacenados en sesion. El objeto SessionContainer se
	 * guarda con la clave definida por IConstantes.SES_SESSION_CONTAINER
	 */
	protected SessionContainer getSessionContainer(HttpServletRequest request) {
		SessionContainer sessionContainer = (SessionContainer) getSessionObject(request, IConstantes.SES_SESSION_CONTAINER);

		// Crea un SessionContainer para a sesion se ainda non existe
		if (sessionContainer == null) {
			sessionContainer = new SessionContainer();

			HttpSession session = request.getSession(true);
			session.setAttribute(IConstantes.SES_SESSION_CONTAINER, sessionContainer);
		}

		return sessionContainer;
	}

	/**
	 * Devuelve un objeto del ambito de aplicacion por su nombre.
	 */
	protected Object getApplicationObject(String attrName) {
		return servlet.getServletContext().getAttribute(attrName);
	}

	/**
	 *
	 * @param request
	 * @return
	 *
	 *		   Comprueba si el usuario esta validado (existe un UsuarioVO en el SessionContainer)
	 *
	 */
	protected boolean isLoggedIn(HttpServletRequest request) {
		SessionContainer container = getSessionContainer(request);
		if (container.getUserContainer() != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *
	 * @param request
	 * @param key
	 * @return
	 *
	 *         Comprueba si el usuario tiene acceso al elemento referenciado por
	 *         key
	 */
	public boolean acceso(HttpServletRequest request, String key) {

		SessionContainer container = getSessionContainer(request);
		HashMap acceso = container.getUserContainer().getAcceso();
		key = StringUtil.trim(key);
		String flag = (String) acceso.get(key); // Comprobamos si en esa entrada hay el string "true" que se almacena cuando se cargan los permisos

		if (!StringUtil.isEmpty(flag)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param req
	 *
	 *            Recupera de sesion las variables locale
	 *
	 */
	protected Locale getSessionLocale(HttpServletRequest req) {
		return (Locale) getSessionObject(req, Globals.LOCALE_KEY);
	}
} // class
