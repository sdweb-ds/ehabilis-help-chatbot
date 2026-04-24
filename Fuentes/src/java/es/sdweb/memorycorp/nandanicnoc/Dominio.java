package es.sdweb.memorycorp.nandanicnoc;

import java.util.ArrayList;

/**
 * Lista de dominios NANDA (pe "DOMINIO 1: Promoción de la salud",...
 * @author Antonio Carro Mariño
 */
public class Dominio {
    private String name="";
    private ArrayList<Clase> clases=new ArrayList();
    
    public Dominio(String name){
        this.name=name;
    }
    
    /**
     * Añade una clase (pe "CLASE 1: TOMA DE CONCIENCIA DE LA SALUD") al dominio.
     * @param clase Clase a añadir
     */
    public void addClase(Clase clase){
        this.clases.add(clase);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param aName the name to set
     */
    public void setName(String aName) {
        name = aName;
    }

    /**
     * @return the clases
     */
    public ArrayList<Clase> getClases() {
        return clases;
    }

    /**
     * @param aClases the clases to set
     */
    public void setClases(ArrayList<Clase> aClases) {
        clases = aClases;
    }
    
    @Override
    public String toString(){
        return this.getName();
    }
    
}//class
