package es.sdweb.application.model.dal.exceptions;

import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;

/**
 * @author Antonio Carro Mariño
 *
 */
public class ExceptionErrorConexion extends ExceptionAccesoDatos{
	
	String descripcion=null;
	private Exception encapsulatedException;
	
	public ExceptionErrorConexion(){
		
	}
	
	public ExceptionErrorConexion(Exception e){
	  super(e);	
	}
	
	public ExceptionErrorConexion(String mensaje,String nombreClase){
		super(nombreClase,mensaje);
		descripcion=mensaje;
	}

	public ExceptionErrorConexion(String mensaje,Exception e,String nombreClase){
		super(nombreClase,mensaje);
		descripcion=mensaje;
		encapsulatedException=e;
	}

	/**
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param string
	 */
	public void setDescripcion(String string) {
		descripcion = string;
	}

	/**
	 * @return
	 */
	public Exception getEncapsulatedException() {
		return encapsulatedException;
	}

	/**
	 * @param exception
	 */
	public void setEncapsulatedException(Exception exception) {
		encapsulatedException = exception;
	}

}//class