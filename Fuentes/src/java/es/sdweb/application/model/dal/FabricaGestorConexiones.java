package es.sdweb.application.model.dal;


import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.util.GestorParametrosConfiguracion;
import java.sql.Connection;

/**
 * @author Antonio Carro Mariño
 *
 * Esta clase tiene como objetivo crear una fabrica de conexiones concreta a partir de los datos del fichero
 * de configuracion.
 */
public abstract class FabricaGestorConexiones {

  private final static String PREFIJO_PARAMETRO_CONFIGURACION = FabricaGestorConexiones.class.getName() + ".";
  private final static String PARAMETRO_CONFIGURACION_FABRICA_CLASSNAME = PREFIJO_PARAMETRO_CONFIGURACION + "fabricaConcretaClassName";  
  
  private static FabricaGestorConexiones instancia;

  static {
	  String fabricaConcretaClassName = GestorParametrosConfiguracion.getParametro(PARAMETRO_CONFIGURACION_FABRICA_CLASSNAME);
    
	  if (fabricaConcretaClassName == null) {
	       System.err.println(PARAMETRO_CONFIGURACION_FABRICA_CLASSNAME +
				     " no especificada.");
	  }
	  Class fabricaConcretaClass = null;
	  try { // busca la clase
		  fabricaConcretaClass = Class.forName(fabricaConcretaClassName);
	  } catch (Exception e) {
		  e.printStackTrace(System.err);
	  }
	  try { // instancia un objeto de la clase buscada
		  instancia = (FabricaGestorConexiones) fabricaConcretaClass.newInstance();
	  } catch (Exception e) {
		  e.printStackTrace(System.err);
	  }
  }


  public static FabricaGestorConexiones getInstance() {
    return instancia;
  }


  /* Los metodos que vienen a continuacion
   * no deben ser estaticos para que no puedan ser usados sin haber hecho el getInstance(),
   * se invocaran sobre el unico objeto FabricaDAO.
   */

  public abstract Connection getConexion() throws ExceptionAccesoDatos;

}
