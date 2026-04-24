
package es.sdweb.application.model.exceptions;

import es.sdweb.application.model.exceptions.ExceptionLogica;


/**
 * @author Antonio Carro Mariño
 *
 */
public class ExceptionInstanciaDuplicada extends ExceptionLogica {
	private String key;
	
	public ExceptionInstanciaDuplicada(String key, String className) {
	super(className,"Instancia de la clase " + className + " con codigo = " +
		  key + " ya existe en la base de datos.");
	this.key = key;
	}

	public String getKey() {
		return key;
	}

}//class
