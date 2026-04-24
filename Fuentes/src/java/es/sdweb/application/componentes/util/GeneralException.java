package es.sdweb.application.componentes.util;



public class GeneralException extends Exception
 {

    //VARIABLES PRIVADAS
    /**
     * Clase que lanza esta excepcion.
     */
    private String clase;
    /**
    * Metodo que lanza a esta excepcion.
    */
    private String metodo;

    /**
    * Descripcion del error.
    */
    private String descripcion;

    private int tipoError = 0; // Un Error negativo Indica un Error de Base de Datos.

  //CONSTRUCTORES

  /**
   * El metodo y la clase que llama al error son imprescindibles.
   *@param Clase  Nombre de la clase que invoco esta excepcion.
   *@param Metodo Nombre del metodo que invoco esta excepcion.
   */
  public GeneralException(String Clase,
  	                String Metodo)
   {
    clase = Clase;
    metodo = Metodo;
    descripcion = "";
    try {
     }
     catch (Exception ex1) {
       ex1.printStackTrace();
     }
   }//FIN errorFatal

  /**
   * Para mas informaci�n permitimos una descripci�n.
   *@param Clase       Nombre de la clase que invoc� esta excepci�n.
   *@param Metodo      Nombre del m�todo que invoc� esta excepci�n.
   *@param Descripcion Descripci�n de por que se produjo el error.
   */
  public GeneralException(String Clase,
  	                String Metodo,
  	                String Descripcion)
   {
    this(Clase, Metodo);
    descripcion = Descripcion;
    try {
      //LogErroresDTO log = new LogErroresDTO("Usuario", "Sistema");
      //log.setDatos("Error", clase + " - " + metodo + " => " + descripcion, Constantes.ERROR_TIPO_ALERTA,
      //             Constantes.ERROR_CRITICIDAD_NORMAL, "accion", "N");
      //LogServiceCiWEB.log(log);
    }
    catch (Exception ex1) {
      ex1.printStackTrace();
    }
   }


  //M�TODOS P�BLICOS

  /**
   * Devuelve informaci�n sobre la clase que llamo a esta excepci�n.
   *@return El nombre de la clase que lanz� esta excepci�n.
   */
  public String getClase()
   {
    return clase;
   }//FIN getClase

  /**
   * Devuelve informaci�n sobre el m�todo que llamo a esta excepci�n.
   *@return El nombre del m�todo que lanz� esta excepci�n.
   */
  public String getMetodo()
   {
    return metodo;
   }//FIN getMetodo

  /**
   * Devuelve informaci�n mas detallada sobre las causas de la excepci�n.
   *@return Descripci�n sobre las causas de la excepci�n.
   */
  public String getDescripcion()
   {
    return descripcion;
   }//FIN getDescripcion


  /**
   * Establece la descripci�n de la excepcion.
   *param descripc String que contien la descripci�n.
   */
  public void setDescripcion(String descripc)
  {
   descripcion = descripc;
  }

  /**
   * Devuelve el tipo de error
   *@return tipo_error
   */
  public int getTipoError()
   {
    return tipoError;
   }


  /**
   * Establece el tipo de error de la excepcion.
   *param tipo_error int que contiene el error
   */
  public void setTipoError(int tipo_error)
  {
   tipoError = tipo_error;
  }


  /**
   * Convierte la excepci�n en un String.
   *@return El String que representa la excepci�n (con todos sus datos).
   */
  public String toString()
   {
    String respuesta;
    String descrip;

    descrip = getDescripcion();

    respuesta = getClase() + ".\n ";
    respuesta += "M�todo: " + getMetodo()+".\n";
    if (descrip.length() != 0)
     {//Hay descripci�n
      respuesta += " Descripci�n: " + descrip;
     }

   return (respuesta);
   }//FIN toString


 }//class
