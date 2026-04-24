package es.sdweb.memorycorp.nlpengine;

import es.sdweb.application.componentes.util.ObjectUtil;
import java.util.ArrayList;

/**
 *
 * @author Antonio Carro Mariño
 */
public class LConversacion {
    private String id=ObjectUtil.generateId(); //Generamos un Id unico para la conversacion
    private ArrayList<LParrafo> parrafos=new ArrayList<>(); //Una conversacion es una secuencia de parrafos

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the parrafos
     */
    public ArrayList<LParrafo> getParrafos() {
        return parrafos;
    }

    /**
     * @param parrafos the parrafos to set
     */
    public void setParrafos(ArrayList<LParrafo> parrafos) {
        this.parrafos = parrafos;
    }
    
    public void addParrafo(LParrafo parrafo){
        parrafos.add(parrafo);
    }
    
}//class
