
package es.sdweb.application.model.exceptions;

import es.sdweb.application.model.exceptions.ExceptionLogica;



public class ExceptionInstanciaNoHallada extends ExceptionLogica {

	private String key;
        private String descripcion="";
	
	public ExceptionInstanciaNoHallada(String key, String className) {
	  super(className,"Instancia de la clase " + className + " con codigo = " +
		  key + " no fue hallada en la base de datos.");
          this.key=key;
	}

	public ExceptionInstanciaNoHallada(String key, String descripcion, String className) {
	  super(className,"Instancia de la clase " + className + " con codigo = " +
		  key + " no fue hallada en la base de datos.");
          this.descripcion=descripcion;
          this.key=key;
	}


	public String getKey(){
	  return key;
	}

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
	
}//class