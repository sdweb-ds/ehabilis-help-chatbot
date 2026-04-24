
package es.sdweb.application.model.exceptions;

import es.sdweb.application.model.exceptions.ExceptionLogica;


/**
 * @author Antonio Carro Mariño
 *
 */
public class ExceptionUniqueLogin extends ExceptionLogica {
	private String key;
	
	
	public ExceptionUniqueLogin(String nombreClase, String descripcion, String key) {
		super(nombreClase, descripcion);
		// TODO Auto-generated constructor stub
		this.key = key;
	}



	public String getKey() {
		return key;
	}

}//class
