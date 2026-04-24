package es.sdweb.memorycorp.nlpengine;

import es.sdweb.application.componentes.util.ObjectUtil;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Antonio Carro Mariño
 */
public class LParrafo {
    
    private String id=ObjectUtil.generateId(); //Generamos un Id unico para este parrafo
    private ArrayList<LOracion> oraciones=new ArrayList<>(); //Un parrafo es una secuencia de oraciones
    private String text=""; //Texto original vinculado a este párrafo, a efectos de depuración
    private LConversacion conversacion=null; //Conversacion a la que pertenece este párrafo. Dependencia circular necesaria (pero sorteable a través de Object en caso necesario).
    private HashMap context=new HashMap(); //HashMap(String,String) para registrar valores contextuales de: pronombres. P.e. él->Juan

    /**
     * Devuelve el texto original del párrafo, con todas sus oraciones. Entre oraciones no hay saltos de línea ya que es un párrafo.
     * @return Devuelve el texto original del párrafo.
     */
    @Override
    public String toString(){
       String result="";
       for (LOracion oracion : getOraciones()){
           result+=oracion.getOracion()+" ";
       }
       return result.trim();
    }
    
    /**
     * Pasa a texto toda la estructura del párrafo.
     * @return Cadena de texto con la estructura del párrafo.
     */
    public String toStringEstructura(){
       String result="";
       for (LOracion oracion : getOraciones()){
           result+=oracion.toStringEstructura()+"\n";
       }
       return result.trim();
    }

    /**
     * @return Devuelve el ID del párrafo.
     */
    public String getId() {
        return id;
    }

    /**
     * @return Devuelve la lista de oraciones que componen el párrafo.
     */
    public ArrayList<LOracion> getOraciones() {
        return oraciones;
    }

    /**
     * @param oraciones Establece un nueva nueva lista de oraciones en el párrafo (las anteriores quedan eliminadas).
     */
    public void setOraciones(ArrayList<LOracion> oraciones) {
        this.oraciones = oraciones;
    }

    /**
     * @return Devuelve el texto original del párrafo.
     */
    public String getText() {
        return text;
    }

    /**
     * @param text Establece el texto plano del párrafo (el anterior queda eliminado).
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * Devuelve el primer nodo de la estructura del párrafo, es decir, el primer nodo de la primera oración.
     * @return Devuelve el primer término de la primera oración (no es un clon).
     */
    public LTermino getFirstTerm(){
        LTermino result=null;
        if (!this.oraciones.isEmpty()){ //Si hay al menos una oración
            result=this.oraciones.get(0).getEstructura(); //Devolvemos la raiz de la primera, que es el primer nodo.
        }
        return result;
    }

    /**
     * @return Devuelve el hash map de Contexto, que contiene los valores contextuales de pronombres, etc. P.e. él->Juan
     */
    public HashMap getContext() {
        return context;
    }

    /**
     * @param context Establece un nuevo contexto, machando el previamente existente.
     */
    public void setContext(HashMap context) {
        this.context = context;
    }
    
    /**
     * Añade una clave-valor al Contexto, machacando cualquier referencia previa anterior.
     * @param key Palabra que actúa de clave.
     * @param value Valor asignado a la palabra.
     */
    public void addContextValue(String key,String value){
        this.context.put(key, value);
    }
    
    /**
     * Devuelve un valor del contexto.
     * @param key Palabra clave del valor buscado. P.e. "él"
     * @return Valor String encontrado. Null si no se encontró nada.
     */
    public String getContextValue(String key){
        String result=(String)this.context.get(key);
        return result;
    }

    /**
     * @return Conversación a la que pertenece este párrafo.
     */
    public LConversacion getConversacion() {
        return conversacion;
    }

    /**
     * @param conversacion Conversación a la que pertenece este párrafo.
     */
    public void setConversacion(LConversacion conversacion) {
        this.conversacion = conversacion;
    }
    
}//class
