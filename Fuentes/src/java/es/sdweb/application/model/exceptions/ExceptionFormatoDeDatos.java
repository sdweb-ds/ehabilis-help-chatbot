/*
 * Created on 14-sep-2003
 *
 */
package es.sdweb.application.model.exceptions;

import es.sdweb.application.model.exceptions.ExceptionLogica;


/**
 * @author Antonio Carro Mariño
 *
 */
public class ExceptionFormatoDeDatos extends ExceptionLogica {
	
	public ExceptionFormatoDeDatos(String className,String descripcion ) {
	  super(className,descripcion);
	}

}//class
