package es.sdweb.application.exceptions;

/**
 * Esta es la excepción padre de todas las excepciones lógicas del programa (excepciones de negocio).
 * @author Antonio Carro Mariño
 */
public class LogicalException {
    private String description=null;
    
    public LogicalException(String description){
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
