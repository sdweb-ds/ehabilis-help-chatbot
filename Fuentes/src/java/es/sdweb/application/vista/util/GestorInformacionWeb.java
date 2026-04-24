package es.sdweb.application.vista.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.controller.util.SessionContainer;
import es.sdweb.application.controller.util.Translator;
import es.sdweb.application.model.dal.facade.FabricaDAOs;
import es.sdweb.application.model.dal.facade.IUsuarioDAO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.dto.UsuarioDTO;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.model.exceptions.ExceptionFormatoDeDatos;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.facade.FabricaFacades;
import es.sdweb.application.model.facade.IGestionUsuarioFacade;
import es.sdweb.application.model.facade.IMantenimientoFacade;
import es.sdweb.application.util.ExceptionFaltaParametro;
import es.sdweb.application.util.GestorParametrosConfiguracion;

public class GestorInformacionWeb {
	public static String ATRIBUTO_SESION_USUARIO = "usuario";
	public static String ATRIBUTO_SESION_CREATED = "session_created";
	public static String GLOBAL_FORWARD_LOGIN_KEY = "global.forward.login";
	public static String GLOBAL_FORWARD_HOME_KEY = "global.forward.home";

	public static List<String> scripts = new ArrayList<String>();
	public static List<String> styles = new ArrayList<String>();

	// Usuario actual
	private static UsrDTO user = null;

	/**
	 * Borra una sesión de la aplicación
	 *
	 * @param request
	 * 			Objeto HttpServletRequest
	 * @param response
	 * 			Objeto HttpServletResponse
	 */
	public static void doLogOut(HttpServletRequest request, HttpServletResponse response) {
		// Coge la sesión si existe, si no existe no la crea
		HttpSession session = request.getSession(false);

		// Si existe la sesión
		if (session != null) {
			// La eliminamos
			session.invalidate();
		}
	}

	/**
	 * Borra una sesión de la aplicación
	 *
	 * @deprecated Reemplazada por {@link #doLogOut(HttpServletRequest request, HttpServletResponse response)}
	 *
	 * @param request
	 * 			Objeto HttpServletRequest
	 * @param response
	 * 			Objeto HttpServletResponse
	 */
	@Deprecated
	public static void salir(HttpServletRequest request, HttpServletResponse response) {
		GestorInformacionWeb.doLogOut(request, response);
	}

	/**
	 * Indica si el usuario se ha autenticado en la aplicacion y tiene una
	 * sesion activa. Este metodo tambien está disponible en la clase
	 * BaseDispatchAction.
	 *
	 * @param request
	 *            Objeto HttpServletRequest
	 * @return True si el usuario está autenticado, false en caso contrario.
	 */
	public static boolean isLoggedIn(HttpServletRequest request) {
		// Coge la sesión si existe
		HttpSession session = request.getSession(false);

		// Si hay sesion activa
		if (session != null) {
			// Obtenemos el SessionContainer
			SessionContainer sessionContainer = (SessionContainer) session.getAttribute(IConstantes.SES_SESSION_CONTAINER);

			// Si el sessionContainer esta almacenado en la sesion implica que el usuario se ha autenticado
			return (sessionContainer != null && sessionContainer.getUserContainer() != null);
		} else {
			return false;
		}
	}

	/**
	 * Obtiene la URL a la que debemos hacer forward desde una JSP en caso de
	 * que la sesion haya expirado.
	 *
	 * @return URL a la que realizar forward.
	 */
	public static String getForwardURLlogin() {
		return GestorParametrosConfiguracion.getParametro(GLOBAL_FORWARD_LOGIN_KEY);
	}

	/**
	 * Obtiene la URL a la que debemos hacer forward desde una JSP en caso de
	 * que la sesion haya expirado.
	 *
	 * @return URL a la que realizar forward.
	 */
	public static String getForwardURLHome() {
		return GestorParametrosConfiguracion.getParametro(GLOBAL_FORWARD_HOME_KEY);
	}

	/**
	 * Obtiene el parametro String que se encuentra almacenado en la request, ya
	 * sea un parametro o un atributo.
	 *
	 * @param request
	 *            Request.
	 * @param attribute
	 *            Atributo o parametro buscado.
	 * @return Valor del atributo String.
	 */
	public static String getAttribute(HttpServletRequest request, String attribute) {
		// Intentamos obtenerlo como parámetro
		String result = request.getParameter(attribute);

		// Si no se ha obtenido el valor
		if (StringUtil.isEmpty(result)) {
			// Intentamos obtenerlo como parámetro
			result = (String) request.getAttribute(attribute);
		}

		// Si no se ha obtenido nada de ninguna de las dos forms
		if (StringUtil.isEmpty(result)) {
			// Asignamos una cadena vacía al resultado
			// TODO: Sería mejor devolver NULL si no se encuentra
			// (¿Cómo sabemos si no se ha encontrado o está vacío?)
			result = "";
		}

		return result;
	}

	/**
	 * Obtiene el parametro Object que se encuentra almacenado en la request, ya
	 * sea un parametro o un atributo.
	 *
	 * @param request
	 *            Request.
	 * @param attribute
	 *            Atributo o parametro buscado.
	 * @return Valor del atributo Object o NULL si no se ha encontrado nada.
	 */
	public static Object getAttributeObj(HttpServletRequest request, String attribute) {
		// Intentamos obtenerlo como parámetro
		Object result = request.getParameter(attribute);

		// Si no se ha obtenido el valor
		if (result == null) {
			// Intentamos obtenerlo como parámetro
			result = request.getAttribute(attribute);
		}

		return result;
	}

	/**
	 * Obtiene el parametro Object que se encuentra almacenado en la request, ya
	 * sea un parametro o un atributo.
	 *
	 * @deprecated Esta función solo llama a {@link #getAttributeObj(HttpServletRequest request, String attribute)}
	 *
	 * @param request
	 *            Request.
	 * @param attribute
	 *            Atributo o parametro buscado.
	 * @return Valor del atributo Object o NULL si no se ha encontrado nada.
	 */
	@Deprecated
	public static Object getParameterObj(HttpServletRequest request, String attribute) {
		return GestorInformacionWeb.getAttributeObj(request, attribute);
	}

	/**
	 * Obtiene un Select HTML vacío
	 *
	 * @deprecated No se utiliza en ningún sitio y el HTML debería estar apartado del Java
	 * (o crear una Clase específica para ello)
	 *
	 * @param htmlId
	 * 			ID HTML que debe tener el select
	 * @param elementName
	 * 			Nombre del elemento que se incluirá en el Select (Seleccione una "elemento"...)
	 *
	 * @return El código HTML del Select generado
	 */
	@Deprecated
	public static String getComboVacio(String htmlId, String elementName) {
		String result = "<select id='" + htmlId + "' name='" + htmlId + "'>";
			result += "<option value='0'>" + Translator.getTranslation("txt.select.vacio.seleccione.una", elementName) + "</option>";
		result += "</select>";

		return result;
	}

	/**
	 * Obtiene el parametro String que se encuentra almacenado en la request, ya
	 * sea un parametro o un atributo.
	 *
	 * @param request
	 *            Request
	 * @param param
	 *            Atributo o parametro buscado
	 * @return Valor del atributo String.
	 */
	public static String getParameter(HttpServletRequest request, String param) {
		return GestorInformacionWeb.getAttribute(request, param);
	}

	/**
	 * Establece el valor de un atributo guardado en la request
	 *
	 * @param request
	 * 			Request
	 * @param paramName
	 * 			Nombre del parámetro a almacenar
	 * @param paramValue
	 * 			Valor a almacenar para dicho parámetro
	 */
	public static void setParameter(HttpServletRequest request, String paramName, String paramValue) {
		if (!StringUtil.isEmpty(paramValue) && !StringUtil.isEmpty(paramName) && request != null) {
			request.setAttribute(paramName, paramValue);
		}
	}

	/**
	 * Codificar una cadena en HTML (añadir entidades HTML)
	 *
	 * @deprecated Se debería usar {@link org.apache.commons.lang.StringEscapeUtils.escapeHtml(String str)}
	 *
	 * @param str
	 * 			La cadena a codificar
	 *
	 * @return La cadena con las entidades HTML reemplazadas
	 */
	@Deprecated
	public static String htmlEncode(String str) {
		return StringEscapeUtils.escapeHtml(str);
	}

	/**
	 * Obtiene el número de sesiones abiertas actualmente
	 * (Creo, ¿Estamos seguros de que esto funciona?)
	 *
	 * TODO: Si es un número se debería devolver como tal
	 *
	 * @param session
	 * 			La sesión actual (?)
	 * @return El número de sesiones como String
	 */
	public static String getSessionCount(HttpSession session) {
		int result = 0;

		SessionCounter counter = (SessionCounter) session.getAttribute("counter");
		result = counter.getActiveSessionNumber();

		return String.valueOf(result);
	}

	/**
	 * Resalta una parte de un texto metiéndolo dentro de un span
	 *
	 * @deprecated Se debería hacer mediante una clase, no directamente meter el color (Además no se usa en ningún) sitio.
	 * También debería hacerse usando expresiones regulares.
	 *
	 * @param haystack
	 * 			El texto completo
	 * @param needle
	 * 			La cadena que se debe resaltar
	 *
	 * @return El texto con la cadena resaltada
	 */
	@Deprecated
	public static String resalta(String haystack, String needle) {
		String result = "";
		boolean insideHtmlTags = false;
		int i = 0;

		while (i < haystack.length()) {
			char c = haystack.charAt(i);

			// Si estamos procesando codigo html copiamos tal cual
			if (insideHtmlTags) {
				// Si llegamos a un final de corchete hemos salido de un tag html
				if (c == '>') {
					insideHtmlTags = false;
				}

				// En cualquier caso todo caracter de html lo copiamos
				result = StringUtil.cat(result, String.valueOf(c));
			// Si estamos en texto puro y duro
			} else {
				// Si encontramos un inicio de corchete significa que estamos entrando en un tag html
				if (c == '<') {
					insideHtmlTags = true;
					// Copiamos la letra
					result = StringUtil.cat(result, String.valueOf(c));
				// Si estamos buscando en el texto fuera de tags html
				} else {
					int longFragmento = (haystack.length() < (i + needle.length()) ? haystack.length() : (i + needle.length()));
					// Vemos la palabra donde estamos
					String aux = haystack.substring(i, longFragmento);

					// Si coincide con la buscada
					if (haystack.toUpperCase().equals(aux.toUpperCase())) {
						// Hacemos el replace y seguimos
						result = StringUtil.cat(result, "<span style='background: yellow;'>" + aux + "</span>");
						// Saltamos la palabra, ya que ha sido procesada (restamos 1 porque al final del bucle siempre se suma 1)
						i = i + haystack.length() - 1;
					// Si la palabra no coincide con la buscada
					} else {
						// Copiamos la letra
						result = StringUtil.cat(result, "" + c);
					}
				}
			}

			// Sumamos 1
			i++;
		}

		return result;
	}

	/**
	 * Descodifica una URL
	 *
	 * @param url
	 * 			La URL que se desea descodificar
	 * @return La URL descodificada
	 */
	public static String urlDecode(String url) {
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.logException("GestorInformacionWeb.urlDecode()", Log.TIPO_ALERTAS, Log.CRITICIDAD_MINOR, -1, e);
			return null;
		}
	}

	/**
	 * Codifica un URL
	 *
	 * @param url
	 * 			La URL que se desea codificar
	 * @return La URL codificada
	 */
	public static String urlEncode(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.logException("GestorInformacionWeb.urlDecode()", Log.TIPO_ALERTAS, Log.CRITICIDAD_MINOR, -1, e);
			return null;
		}
	}

	/**
	 * @deprecated Esta función no se usa (y no hace nada?)
	 */
	@Deprecated
	public static void incrementaUsuarioUnico(HttpServletRequest request) {
		/*String full_header = "";
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			String value = request.getHeader(headerName);
			full_header = full_header + headerName + " = " + value + "; ";
		}// while

		String host = request.getRemoteHost();
		String host_header = request.getHeader("Host");
		String ip = request.getRemoteAddr();
		String port = "" + request.getRemoteAddr();
		String browser = request.getHeader("User-Agent");
		String language = request.getHeader("Accept-Language");*/

		// Coge la sesion si existe, si no la crea
		HttpSession session = request.getSession(true);
		// Vemos si el usuario está contabilizado
		// En toda la aplicación solo se hace referencia a esto aquí
		String aux = (String) session.getAttribute("usr_contabilizado");
		if (StringUtil.isEmpty(aux) || (!aux.equals("false"))) {
			// Guardamos en la sesion la informacion del usuario nuevo unico
			session.setAttribute("usr_contabilizado", "true");
			/*
			 * IUsuarioOperations op=new UsuarioOperations(); //guardamos en
			 * BBDD el usuario y todos sus datos de navegacion try {
			 * op.incrementaUsuarioUnico(host, host_header, ip, port, browser,
			 * language, full_header); } catch (ExcepcionErrorInterno ex) {
			 * Util.logError(ex.getMessage(), 1); }
			 */
		}
	}

	/**
	 * Recibe un ancho, p.e. "100px", y devuelve el atributo HMTL:
	 * " width='100px' ". Si el parametro de entrada es la cadena vacia o nulo,
	 * devuelve la cadena vacia.
	 *
	 * @param width
	 *            Un ancho.
	 * @return Atributo HTML width
	 */
	public static String getWidth(String width) {
		String result = "";

		if (!StringUtil.isEmpty(width)) {
			result = StringUtil.cat(" width='", width, "' ");
		}

		return result;
	}

	/**
	 * Recibe un ancho, p.e. "100", y devuelve el atributo HMTL:
	 * " width='100px' ".
	 *
	 * @param width
	 *            Un ancho.
	 * @return Atributo HTML width
	 */
	public static String getWidth(int width) {
		return (width > 0 ? getWidth(width + "px") : "");
	}

	/**
	 * Recibe un ancho en pixeles, p.e. "100", y devuelve el size aproximado
	 * para llenar con un input esa longitud.
	 *
	 * @param width
	 *            Un ancho.
	 * @return Atributo HTML width
	 */
	public static String getInputSize(int width) {
		int result = width / 8;
		return String.valueOf(result);
	}

	/**
	 * Obtiene un objeto guardado en sesión
	 *
	 * @param request
	 * 			La request
	 * @param attrName
	 * 			El nombre del objeto que se desea obtener
	 * @return El objeto recuperado de la sesión o NULL
	 */
	private static Object getSessionObject(HttpServletRequest request, String attrName) {
		Object sessionObj = null;

		// Obtenemos la sesión (si no existe no se crea)
		HttpSession session = request.getSession(false);

		if (session != null) {
			sessionObj = session.getAttribute(attrName);
		}

		return sessionObj;
	}

	/**
	 * Devuelve el objeto SessionContainer. Lo busca en la sesión y si lo
	 * encuentra lo devuelve. Si no lo encuentra en la sesión, es que no hay
	 * sesión, en cuyo caso se crea y se guarda en ella el objeto
	 * SessionContainer. El objeto SessionContainer contendrá todos aquellos
	 * datos que deban ser almacenados en sesion. El objeto SessionContainer se
	 * guarda con la clave definida por IConstantes.SES_SESSION_CONTAINER
	 *
	 * @param request
	 * 			La Request
	 * @return El SessionContainer de la sesión actual
	 */
	private static SessionContainer getSessionContainer(HttpServletRequest request) {
		// Obtenemos el SessionContainer almacenado en sesión
		SessionContainer sessionContainer = (SessionContainer) getSessionObject(request, IConstantes.SES_SESSION_CONTAINER);

		// Si no existe creamos uno nuevo y lo guardamos
		if (sessionContainer == null) {
			sessionContainer = new SessionContainer();

			HttpSession session = request.getSession(true);
			session.setAttribute(IConstantes.SES_SESSION_CONTAINER, sessionContainer);
		}

		return sessionContainer;
	}

	/**
	 * Devuelve el usuario almacenado en sesión
	 *
	 * @param request
	 *			La Request
	 *
	 * @return El UsrDTO correspondiente al usuario actual o NULL si no existe
	 */
	public static UsrDTO getSessionUsr(HttpServletRequest request) {
		// Obtenemos el sessionContainer
		SessionContainer sessionContainer = getSessionContainer(request);

		// Si hay un usuario lo devolvemos
		if(sessionContainer != null && sessionContainer.getUserContainer() != null) {
			return sessionContainer.getUserContainer().getUsuario();
		// En caso contrario, devolvemos null
		} else {
			return null;
		}
	}

	/**
	 * Función para obtener el usuario actual
	 * Sin el parámetro request usa el que se guarda anteriormente
	 *
	 * @return  El UsrDTO correspondiente al usuario actual o NULL si no existe
	 */
	public static UsrDTO getSessionUsr() {
		return user;
	}


	/**
	 * Devuelve los datos en BD de una constante de nombre dado. Devuelve null
	 * si no se encuentra.
	 *
	 * @deprecated Reemplazada por {@link #getConstant(String constantName)}
	 *
	 * @param request
	 *            Request
	 * @param nombreConstante
	 *            Nombre de la constante buscada
	 * @return Valor de la constante.
	 * @throws ExceptionErrorInterno
	 */
	@Deprecated
	public static String getConstante(HttpServletRequest request, String nombreConstante) throws ExceptionErrorInterno {
		return GestorInformacionWeb.getConstant(nombreConstante);
	}

	/**
	 * Devuelve los datos en BD de una constante de nombre dado. Devuelve null
	 * si no se encuentra.
	 *
	 * @param constantName
	 *            Nombre de la constante buscada
	 * @return Valor de la constante o null si no se encuentra.
	 * @throws ExceptionErrorInterno
	 */
	public static String getConstant(String constantName) throws ExceptionErrorInterno {
		String result = null;

		if (!StringUtil.isEmpty(constantName)) {
			IMantenimientoFacade iMantenimientoFacade = FabricaFacades.createMantenimientoFacade();
			result = iMantenimientoFacade.getConstante(constantName).getValor();
		}

		return result;
	}

	public static void clearScriptsAndStyles() {
		scripts = new ArrayList<String>();
		styles = new ArrayList<String>();
	}

	public static void enqueueScript(String url) {
		scripts.add(url);
	}

	public static void enqueueStyle(String url) {
		styles.add(url);
	}

	public static String getScripts() {
		String result = "";

		for(String script: scripts) {
			result = StringUtil.cat(result, "<script type=\"text/javascript\" src=\"", script, "\"></script>\n");
		}

		return result;
	}

	public static String getStyles() {
		String result = "";

		for(String style: styles) {
			result = StringUtil.cat(result, "<link rel=\"stylesheet\" type=\"text/css\" href=\"", style, "\" />\n");
		}

		return result;
	}

	/**
	 * Función para establecer el usuario actual a partir de la Request
	 *
	 * @param request
	 */
	public static void setUser(HttpServletRequest request) {
		user = getSessionUsr(request);
	}


	/**
	 * Obtiene todos los usuarios registrados en la base de datos.
	 *
	 * @param request
	 *     Petición realizada por el cliente.
	 * @return
	 *     Lista de UsuarioDTO con la información de los usuarios.
	 *
	 * @throws ExceptionErrorInterno
	 */
	public static List<UsuarioDTO> getAllUsuarios(HttpServletRequest request) throws ExceptionErrorInterno {
	    UsrDTO usrDTO = getSessionContainer(request).getUserContainer().getUsuario();
        IUsuarioDAO usuarioDAO = FabricaDAOs.createUsuarioDAO();

        return usuarioDAO.findAll(usrDTO);
    }

	/**
	 * Devuelve todos los datos del usuario logueado.
	 * @param request
	 *     Petición realizada por el cliente.
	 * @param login
	 *     Login del usuario del que se quiere obtener la información.
	 * @return UsuarioDTO
	 *     Datos del usuario logueado.
	 * @throws ExceptionErrorInterno
	 * @throws ExceptionInstanciaNoHallada
	 * @throws ExceptionFaltaParametro
	 * @throws ExceptionFormatoDeDatos
	 */
	public static UsuarioDTO getUsuarioByLogin(HttpServletRequest request, String login) throws ExceptionErrorInterno, ExceptionInstanciaNoHallada, ExceptionFaltaParametro, ExceptionFormatoDeDatos {
	    UsrDTO usrDTO = getSessionContainer(request).getUserContainer().getUsuario();
	    IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();

        UsuarioDTO usuarioDTO = iGestionUsuarioFacade.buscarUsuario(usrDTO, login);

        return usuarioDTO;

	}

} // class
