package es.sdweb.application.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.sdweb.application.vista.util.GestorInformacionWeb;

/**
 * Filtro para control de acceso
 *
 * @author Sdweb
 *
 */
public class LoginFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// Obtener el objeto HttpServletRequest
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		// Guardamos la request (TODO: Hacer esto en otro filtro o cambiarle el nombre a este [Mejor lo segundo])
		GestorInformacionWeb.setUser(httpRequest);

		// Si está logueado o esta es la página principal o el action de logueo
		// TODO: Debe haber una forma mejor de hacer la comprobación de la página actual
		// TODO: Meter las excepciones en el fichero de configuración
		if(LoginHelper.isLoggedIn(httpRequest) || httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()).equals("/") ||
				httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()).equals("/Autenticacion.do") ||
				httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()).equals("/password.do") ||
				httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()).equals("/pages/static/help.jsp") ||
				httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()).equals("/Portal.do") ||
				httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()).equals("/vista/taquilla/actualiza_localidades.jsp") ||
				httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()).equals("/prueba.jsp") ||
				httpRequest.getRequestURI().contains("/vista/portal/") ||
				httpRequest.getRequestURI().contains("/images/") ||
				httpRequest.getRequestURI().contains("/Scripts/") ||
				httpRequest.getRequestURI().contains("/styles/")) {

			// Seguimos la ejecución sin problemas
			chain.doFilter(request, response);
		} else {
			// En caso contrario redirigimos a la página principal
			// Local
			((HttpServletResponse) response).sendRedirect("/" + httpRequest.getContextPath());
			//DES24
			//((HttpServletResponse) response).sendRedirect("https://chatbot.ehabilis.es" + httpRequest.getContextPath());
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}
}
