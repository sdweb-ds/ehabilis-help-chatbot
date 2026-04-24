package es.sdweb.application.util;

import java.io.InputStream;
import java.util.Properties;


/**
 * @author Antonio Carro Mariño
 *
 * Clase encargada de cargar los parametros de configuracion a partir del fichero de configuracion.
 * La clase será inicializada desde el metodo init() perteneciente a la clase GesculActionServlet
 */
public class GestorParametrosConfiguracion {

    //private static final String FICHERO_CONFIGURACION = "configuration.properties"; //Nombre del archivo de configuracion
    private static Properties parametrosConfiguracion;


    /**
     * Este metodo carga los parametros de configuracion a partir de un fichero XML
     * @param fileName Nombre del fichero XML con los parametros de configuracion.
     */
    public static void loadFromXML(String fileName){
	try {
          Class gestorParametrosConfiguracionClass = GestorParametrosConfiguracion.class;
          ClassLoader classLoader = gestorParametrosConfiguracionClass.getClassLoader();
          InputStream inputStream =
                classLoader.getResourceAsStream(fileName);
          // Parseamos el xml para obtener los parametros de configuracion y cerramos el fichero
	  DigesterWebParams dwp=new DigesterWebParams();
	  dwp.run(inputStream);
	  parametrosConfiguracion=dwp.getResult();
          inputStream.close();
	} catch (Exception e) {
	  System.err.println("ERROR: No se han podido cargar los parametros de configuración. "+
	     		   "Verifique la existencia del inputStream: "+ fileName);
	  e.printStackTrace(System.err);
        }
    }

    /**
     * Este metodo carga los parametros de configuracion a partir de un stream vinculado a una fuente XML
     * @param inputStream Nombre del stream que apunta a los parametros de configuracion.
     */
    public static void loadFromXML(InputStream inputStream){
	try {
          // Parseamos el xml para obtener los parametros de configuracion y cerramos el input stream
	  DigesterWebParams dwp=new DigesterWebParams();
	  dwp.run(inputStream);
	  parametrosConfiguracion=dwp.getResult();
          inputStream.close();
	} catch (Exception e) {
	  System.err.println("ERROR: No se han podido cargar los parametros de configuración. "+
	     		   "Verifique la existencia del inputStream: "+ inputStream.toString());
	  e.printStackTrace(System.err);
	}
    }

    /**
     * Este metodo carga los parametros de configuracion a partir de un stream vinculado a una fuente de tipo Properties
     * @param fileName Nombre del fichero Properties con los parametros de configuracion.
     */
    public static void loadFromProperties(String fileName){
        try {
            Class gestorParametrosConfiguracionClass = GestorParametrosConfiguracion.class;
            ClassLoader classLoader = gestorParametrosConfiguracionClass.getClassLoader();
            InputStream inputStream =
                classLoader.getResourceAsStream(fileName);
            parametrosConfiguracion = new Properties();
            parametrosConfiguracion.load(inputStream);
            inputStream.close();

        } catch (Exception e) {
            System.err.println("No se han podido cargar los parametros de configuración.\n"+
                               "Verifique la existencia del fichero \""+ fileName+"\"");
            e.printStackTrace(System.err);
        }         
    }

    
    /**
     * En este bloque estatico de codigo se cargan los parametros desde un fichero de tipo Properties.
     * Si se deseara cargar desde XML, se debera cambiar este codigo de forma apropiada.
     */
    /*
    static { 
        try {
            Class gestorParametrosConfiguracionClass = GestorParametrosConfiguracion.class;
            ClassLoader classLoader = gestorParametrosConfiguracionClass.getClassLoader();
            InputStream inputStream =
                classLoader.getResourceAsStream(FICHERO_CONFIGURACION);
            loadFromProperties(inputStream); //Cargamos los parametros desde un fichero de tipo properties

        } catch (Exception e) {
            System.err.println("No se han podido cargar los parametros de configuración.\n"+
                               "Verifique la existencia del fichero "+ GestorParametrosConfiguracion.FICHERO_CONFIGURACION);
            e.printStackTrace(System.err);
        } 
    }
    */


    /**
    * Declaramos privado el constructor para que esta clase no pueda ser instanciada.
    */
    private GestorParametrosConfiguracion() {
    }
    

    /**
     * Obtiene el valor asociado al parametro cuyo nombre se recibe como argumento.
     * @param nombre Nombre del parametro del que se quiere obtener el valor.
     * @return Valor del parametro en formato String
     */
    public static String getParametro(String nombre) {
    	String result=parametrosConfiguracion.getProperty(nombre);
    	if (result==null){
           //Si no se cargan los parametros de configuracion es posible que no funcione el log, por tanto sacamos el error por salida estandar de error.     
           System.err.println(Util.getFecha()+" Parametro: \""+ nombre +"\" no especificado.");
    	};
        return result;
    } 


    /**
     * Obtiene el valor asociado al parametro cuyo nombre se recibe como argumento.
     * @param nombre Nombre del parametro del que se quiere obtener el valor.
     * @param obligatorio Booleano que indica si el parametro es obligatorio o no.
     * @return Valor del parametro en formato String
     * @throws ExceptionFaltaParametro Esta excepcion es lanzada si el parametro es obligatorio y no esta definido.
     */
    public static String getParametro(String nombre,boolean obligatorio) throws ExceptionFaltaParametro {
	String result=getParametro(nombre);

	if ((result==null)&&(obligatorio)){
		throw new ExceptionFaltaParametro(nombre);
	};
	return result;
    }


    /**
     * Obtiene el valor entero asociado al parametro cuyo nombre se recibe como argumento.
     * @param nombre Nombre del parametro del que se quiere obtener el valor.
     * @return Valor del parametro en formato entero
     */
    public static int getParametroEntero(String nombre) {
        String valor=getParametro(nombre);
        int result=0;
        try{
          result=Integer.parseInt(valor);
        }catch (NumberFormatException ex){
          System.err.println(Util.getFecha()+" El parametro \""+ nombre +"\" no es numerico cuando deberia serlo.");
        }
        return result;
    }

}//class
