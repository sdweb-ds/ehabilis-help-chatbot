package es.sdweb.application.model.exceptions;

import es.sdweb.application.componentes.util.logging.Log;

/**
 * @author Antonio Carro Mariño
 *
 */
public class ExceptionErrorInterno extends Exception {

	public final static int TIPO_ERROR_ACCESO = 1;
	public final static int TIPO_ERROR_SISTEMA = 2;
	public final static int TIPO_ERROR_BASE_DATOS = 3;
	public final static int TIPO_ERROR_APLICACION = 4;
	public final static int TIPO_ERROR_ALERTAS = 5;
	
	public final static int CRITICIDAD_AVISO = 1;
	public final static int CRITICIDAD_POCA = 2;
	public final static int CRITICIDAD_NORMAL = 3;
	public final static int CRITICIDAD_MUCHA = 4;
	public final static int CRITICIDAD_CRITICO = 5;
	
	private int codigo = 0;
	private String descripcion;
	private String nombreClase;
	
	public ExceptionErrorInterno(){
		
	}
	
	public ExceptionErrorInterno(String nombreClase,String descripcion){
		super(descripcion);
		this.descripcion = descripcion;
		this.nombreClase= nombreClase;	
	}
		
	public ExceptionErrorInterno(String descripcion, Exception excepcion){
		super(descripcion, excepcion);
		this.descripcion = descripcion;
	}
		
	public ExceptionErrorInterno(String nombreClase,String descripcion, Exception excepcion){
		super(descripcion, excepcion);
		this.descripcion = descripcion;
		this.nombreClase= nombreClase;	
	}
		
	public ExceptionErrorInterno( Exception excepcion, int criticidad ) {
		super(excepcion);		 	
		//En el mensaje de error incluimos el mensaje fuente
		if( excepcion != null ) this.descripcion = " Error origen: ( " + excepcion.getMessage() + " ) ";
		
		//Trazamos el error
		Log.logRE("","",TIPO_ERROR_SISTEMA,criticidad,this.codigo,descripcion);
	}
	
	public ExceptionErrorInterno(Exception excepcion ) {
		super(excepcion);		 	
		//En el mensaje de error incluimos el mensaje fuente
		if( excepcion != null ) this.descripcion = " Error origen: ( " + excepcion.getMessage() + " ) ";
		
		//Trazamos el error
		Log.logRE("","",TIPO_ERROR_SISTEMA,CRITICIDAD_NORMAL,this.codigo,descripcion);		
	}
	
	public ExceptionErrorInterno(Exception excepcion, int codigo, String descripcion) {		
		super(descripcion, excepcion);
		this.codigo = codigo;
		this.descripcion = descripcion;
		//En el mensaje de error incluimos el mensaje fuente
		if( excepcion != null ) this.descripcion += " Error origen: ( " + excepcion.getMessage() + " ) ";

		//Trazamos el error
		Log.logRE("","",TIPO_ERROR_SISTEMA,CRITICIDAD_NORMAL,this.codigo,this.descripcion);
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public String getNombreClase() {
		return nombreClase;
	}
	
}//class
