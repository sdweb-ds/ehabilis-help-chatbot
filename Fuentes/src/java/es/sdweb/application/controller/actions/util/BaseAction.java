package es.sdweb.application.controller.actions.util;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.controller.util.ApplicationContainer;
import es.sdweb.application.controller.util.SessionContainer;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.memorycorp.core.LearningEngine;
import es.sdweb.memorycorp.dialogue.DialogueEngine;
import es.sdweb.memorycorp.nlpengine.SpanishDictionary;

/**
 *
 * @author Antonio Carro Mariño
 *
 *         Clase Action que se debe tomar de base para todas las Actions de la aplicacion.
 *
 */
abstract public class BaseAction extends Action {

	protected static LearningEngine learningEngine = null;

	protected static DialogueEngine dialogueEngine = null;

	// Inicializar learningEngine y dialogueEngine
	static {
		LogUtil.setLogTrazas(true); //Activamos el log de trazas
        LogUtil.setNivelTraza(8); //Establecemos el nivel de la visibilidad de trazas
        LogUtil.logTraza("[INICIALIZACION FASE_0] Logs activados.",0);

        LogUtil.logTraza("[INICIALIZACION FASE_1] Inicializando diccionario de Español.",5);
        SpanishDictionary.initializeDictionary(); //Cargamos el diccionario de Castellano
        LogUtil.logTraza("[INICIALIZACION FASE_1] Inicializando LearningEngine.",5);

        learningEngine = new LearningEngine(SpanishDictionary.getDictionary()); //Creamos el Engine de IA

        // Inicializar DialogueEngine
        dialogueEngine = new DialogueEngine(learningEngine);
	}


	@Override
	public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) {
		ActionForward fwd = null;

		try {
			fwd = executeAction(map, frm, req, res);
		} catch (Exception e) {
			e.printStackTrace();
			fwd = map.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return fwd;
	}

	/**
	 * Metodo abstracto en el que delega metodo Execute() en las subclases de
	 * BaseAction. Ello obliga al desarrollador a tener que extender esta clase
	 * ya que no puede ser instanciada.
	 *
	 * @param map
	 * @param frm
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	abstract protected ActionForward executeAction(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws Exception;

	/**
	 * Retorna un objeto de sesion basandose en el request y en el nombre del
	 * atributo.
	 *
	 * @param req
	 * @param attrName
	 * @return
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
	 * Devuelve un objeto SessionContainer.
	 */
	protected SessionContainer getSessionContainer(HttpServletRequest request) {
		SessionContainer sessionContainer = null;
		sessionContainer = (SessionContainer) getSessionObject(request, IConstantes.SES_SESSION_CONTAINER);

		// Crea un SessionContainer para a sesion se ainda non existe
		if (sessionContainer == null) {
			sessionContainer = new SessionContainer();

			HttpSession session = request.getSession(true);
			session.setAttribute(IConstantes.SES_SESSION_CONTAINER, sessionContainer);

			// request.getSession().setAttribute(Globals.LOCALE_KEY,
			// Locale.ENGLISH);
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
	 *         Comprueba si el usuario esta validado (existe un UsuarioVO en el
	 *         SessionContainer)
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
	 *
	 */
	public boolean acceso(HttpServletRequest request, String key) {

		SessionContainer container = getSessionContainer(request);
		HashMap acceso = container.getUserContainer().getAcceso();

		if (acceso.containsKey(key)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param req
	 * @param locale
	 *
	 *            Establece en sesion las variables locale para traduccion jstl
	 *            y struts a partir de un locale pasado en formato "idioma_PAIS"
	 *            (gl_ES, es_ES)
	 *
	 */
	protected void setSessionLocale(HttpServletRequest req, String locale) {
		String[] locale_array = locale.split("_");

		if ((locale_array != null) && (locale_array[0] != null) && (locale_array[1] != null)) {
			Locale objetoLocale = new Locale(locale_array[0], locale_array[1]);

			setSessionObject(req, Globals.LOCALE_KEY, objetoLocale);
			setSessionObject(req, "javax.servlet.jsp.jstl.fmt.locale.session", objetoLocale);

			// Guardar el locale en UsrDTO
			SessionContainer sessionContainer = getSessionContainer(req);

			if (sessionContainer.getUserContainer() != null) {
				UsrDTO usrDTO = sessionContainer.getUserContainer().getUsuario();

				if (usrDTO != null) {
					usrDTO.setLocale(objetoLocale);

					sessionContainer.getUserContainer().setUsuario(usrDTO);
				}
			}
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