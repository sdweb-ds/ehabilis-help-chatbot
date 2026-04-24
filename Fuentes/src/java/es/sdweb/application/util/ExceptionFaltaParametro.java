/*
 * Created on 28-nov-05
 *
 */
package es.sdweb.application.util;

import es.sdweb.application.model.exceptions.ExceptionLogica;

/**
 * @author Antonio Carro Mariño
 *
 */
public class ExceptionFaltaParametro extends ExceptionLogica {

	public ExceptionFaltaParametro(String parametro){
		super (parametro);
	}

}//class
