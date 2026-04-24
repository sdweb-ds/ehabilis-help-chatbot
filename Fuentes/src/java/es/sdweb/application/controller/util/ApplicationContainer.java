package es.sdweb.application.controller.util;

import java.util.Locale;


/**
 * Empleada por el Contenedor Web para almacenar la informacion comun
 * a todos los usuarios.
 */
public class ApplicationContainer {
  
	/**
	 * Locale por defecto para la aplicacion.
	 */
	private Locale systemLocale = null;


	/**
	 * Obten o Locale para o sistema.
	 */
	public Locale getSystemLocale() {
		return systemLocale;
	}
	  
	/**
	 * Introduce o Locale para o sistema.
	 */
	public void setSystemLocale(Locale aLocale) {
		systemLocale = aLocale;
	}


}
