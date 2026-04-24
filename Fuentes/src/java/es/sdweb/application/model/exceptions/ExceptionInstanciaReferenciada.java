package es.sdweb.application.model.exceptions;

import es.sdweb.application.model.exceptions.ExceptionLogica;


/**
 * Esta excepcion indica que una instancia es referenciada por otros datos, y que por tanto no se pueden realizar operaciones
 * sobre ella como la eliminacion, para no dejar los datos inconsistentes.
 * @author Antonio Carro
 */
public class ExceptionInstanciaReferenciada extends ExceptionLogica {
	private String key;
	
	public ExceptionInstanciaReferenciada(String key, String className) {
	super(className,"Instancia de la clase " + className + " con codigo = " +
		  key + " esta siendo referenciada por otros datos existentes.");
	this.key = key;
	}

	public String getKey() {
		return key;
	}

}//class
