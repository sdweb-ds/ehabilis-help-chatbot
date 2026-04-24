package es.sdweb.application.model.exceptions;

/**
 * @author Antonio Carro Mariño
 *
 */
public class ExceptionLogica extends Exception {

	private String descripcion;
	private String nombreClase;
	
	public ExceptionLogica(String descripcion){
		super(descripcion);
		this.descripcion = descripcion;
	}

	public ExceptionLogica(String nombreClase,String descripcion){
		super(descripcion);
		this.descripcion = descripcion;
		this.nombreClase= nombreClase;
		
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getNombreClase() {
		return nombreClase;
	}
}//class
