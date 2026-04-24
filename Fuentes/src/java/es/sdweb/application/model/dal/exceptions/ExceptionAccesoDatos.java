package es.sdweb.application.model.dal.exceptions;

import es.sdweb.application.model.exceptions.ExceptionErrorInterno;

/**
 * @author Antonio Carro Mariño
 *
 */
public class ExceptionAccesoDatos extends ExceptionErrorInterno{

       
        public ExceptionAccesoDatos() {
		
	}
        
        public ExceptionAccesoDatos(Exception e) {
	  super(e);	
	}
        
        public ExceptionAccesoDatos(String descripcion, Exception e) {
	  super(descripcion,e);	
	}
        
	public ExceptionAccesoDatos(String nombreClase,String descripcion){
	  super(nombreClase,descripcion);
	}
	
	
}//class
