package es.sdweb.application.exceptions;

/**
 * Esta clase representa la excepción padre de todas las que en el programa suponen un error.
 * Entre estas excepciones se encontrarán: excepciones de acceso a datos, etc.
 * @author Antonio Carro Mariño
 */
public class InternalErrorException {
    private String description=null;
    
    public InternalErrorException(String description){
        this.description=description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}//class
