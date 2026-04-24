package es.sdweb.memorycorp.core;

import es.sdweb.application.exceptions.LogicalException;

/**
 * Excepción que se lanza cuando el procesamiento que se pretende realizar no corresponde 
 * @author Antonio
 */
public class TypeMissmatchException extends LogicalException{
    public TypeMissmatchException(String mensaje){
        super(mensaje);
    }
    
}//class
