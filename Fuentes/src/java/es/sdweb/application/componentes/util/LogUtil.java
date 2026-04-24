package es.sdweb.application.componentes.util;

/**
 * Utilidad para imprimir lineas de Log.
 * @author Antonio Carro Mariño
 */
public class LogUtil {
    
    private static boolean LOG_TRAZA=false;
    private static boolean LOG_ERROR=false;
    private static int NIVEL_TRAZA=0; //Nivel por defecto.
    
    /**
     * Imprime una línea de depuración en la salida estándar.
     * @param text Línea de traza a imprimir.
     * @param nivel Nivel de la línea de traza. Se hará un padding de espacios del tamaño de "nivel".
     */
    public static void logTraza(Object text, int nivel){
        nivel=(nivel<0?0:nivel); //El nivel nunca puede ser menor de cero.
        if (nivel<=NIVEL_TRAZA){
            text=(text!=null?text:StringUtil.padding(nivel));
            if (LOG_TRAZA){
                System.out.println(text.toString());
            }
        }
    }
    
    /**
     * Imprime una línea de error en la salida de error estándar.
     * @param text Línea de error a imprimir.
     */
    public static void logError(Object text){
        text=(text!=null?text:"");
        if (LOG_ERROR){
            System.err.println(text.toString());
        }
    }
    
    /**
     * Imprime una línea de texto en la salida estándar.
     * @param text Línea de texto a imprimir.
     */
    public static void logConsole(Object text){
        text=(text!=null?text:"");
        System.out.println(text.toString());
    }
    
    /**
     * Indica si está activado el log de trazas.
     * @return True si está activado, false en caso contrario.
     */
    public static boolean isLogTraza(){
        return LOG_TRAZA;
    }
    
    /**
     * Indica si está activado el log de errores.
     * @return True si está activado, false en caso contrario.
     */
    public static boolean isLogError(){
        return LOG_ERROR;
    }
    
    /**
     * Establece el nivel a partir del cual las trazas dejan de imprimirse. P.e. si indicamos "3", se imprimirán las trazas
     * de niveles 0, 1, 2 y 3. Se imprimen las trazas siempre que esté activado el log de trazas.
     * @param nivel Nivel a partir del cual las trazas no se imprimen. Si es negativo, se pone cero.
     */
    public static void setNivelTraza(int nivel){
        nivel=(nivel<0?0:nivel); //El nivel nunca puede ser menor de cero.
        NIVEL_TRAZA=nivel;
    }
    
    /**
     * Devuelve el nivel de visibilidad de traza.
     * @return Entero que indica el nivel a partir del cual las trazas dejan de verse.
     */
    public static int getNivelTraza(){
        return NIVEL_TRAZA;
    }
    
    /**
     * Activa o desactiva el Log de trazas.
     * @param activo True para activarlo, false para desactivarlo.
     */
    public static void setLogTrazas(boolean activo){
        LOG_TRAZA=activo;
    }

    /**
     * Activa o desactiva el Log de errores.
     * @param activo True para activarlo, false para desactivarlo.
     */
    public static void setLogErrores(boolean activo){
        LOG_ERROR=activo;
    }

}//class
