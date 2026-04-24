package es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles;

import org.apache.struts.action.*;
import javax.servlet.http.*;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Clase de definición del formulario de cambio de contraseña.
 *
 * @author sdweb
 * @see ActionForm
 */
public class CambiarClaveForm extends ActionForm {
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6959592375678303760L;

	/**
	 * Variable utilizada para almacenar el nombre de usuario
	 */
	private String login;
	/**
	 * Variable utilizada para almacenar la contraseña actual del usuario
	 */
	private String claveAntigua;
	/**
	 * Variable utilizada para almacenar la nueva contraseña del usuario
	 */
	private String claveNueva;
	/**
	 * Variable utilizada para almacenar la confirmación de nueva contraseña del usuario
	 */
	private String claveNuevaConf;
	/**
	 * Variable utilizada para almacenar los mensajes de advertencia del formulario
	 */
	private String advertencia;

	public String getAdvertencia() {
		return advertencia;
	}

	public void setAdvertencia(String advertencia) {
		this.advertencia = advertencia;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getClaveAntigua() {
		return claveAntigua;
	}

	public void setClaveAntigua(String claveAntigua) {
		this.claveAntigua = claveAntigua;
	}

	public String getClaveNueva() {
		return claveNueva;
	}

	public void setClaveNueva(String claveNueva) {
		this.claveNueva = claveNueva;
	}

	public String getClaveNuevaConf() {
		return claveNuevaConf;
	}

	public void setClaveNuevaConf(String claveNuevaConf) {
		this.claveNuevaConf = claveNuevaConf;
	}

	public String validar(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
		String advertencia = "";

		if (login == null || login.trim().length() == 0) {
			advertencia = "advertencia.campos.vacios";
		}
		if (claveAntigua == null || claveAntigua.trim().length() == 0) {
			advertencia = "advertencia.campos.vacios";
		}
		if (claveNueva == null || claveNueva.trim().length() == 0) {
			advertencia = "advertencia.campos.vacios";
		}
		if (claveNuevaConf == null || claveNuevaConf.trim().length() == 0) {
			advertencia = "advertencia.campos.vacios";
		}

		return advertencia;
	}

	@Override
	public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
		login = null;
		claveAntigua = null;
		claveNueva = null;
		claveNuevaConf = null;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public int compareTo(Object remoteObject) {
		return CompareToBuilder.reflectionCompare(this, remoteObject);
	}

	@Override
	public boolean equals(Object remoteObject) {
		return EqualsBuilder.reflectionEquals(this, remoteObject);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}
