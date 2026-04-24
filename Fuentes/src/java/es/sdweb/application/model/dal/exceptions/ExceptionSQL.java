package es.sdweb.application.model.dal.exceptions;

import es.sdweb.application.model.dal.exceptions.ExceptionErrorConexion;

/**
 * @author Antonio Carro Mariño
 *
 */
public class ExceptionSQL extends ExceptionErrorConexion{
	
  private Exception encapsulatedException;
  
  public ExceptionSQL(Exception e){
    super(e);
    encapsulatedException=e;
  }


    /**
     * Returns the encapsulatedException.
     * @return Exception
     */
    public Exception getEncapsulatedException() {
            return encapsulatedException;
    }

}//class
