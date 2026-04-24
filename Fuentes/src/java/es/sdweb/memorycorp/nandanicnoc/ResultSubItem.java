
package es.sdweb.memorycorp.nandanicnoc;

/**
 * 
 * @author Antonio Carro Mariño
 */
public class ResultSubItem {
    private SubItem subItem=null; //SubItem (Nanda, Nic o Noc) presente en la oración.
    private String oracion=""; //Oración original

    /**
     * Asocia un SubItem al resultado, junto a la oración que motiva tal asociación. P.e. "Insomnio" (SubItem), y "La paciente presenta problemas de conciliación del sueño".
     * @param sub SubItem detectado en la información de entrada.
     * @param oracion Oración que motiva esta asociación.
     */
    public ResultSubItem(SubItem sub, String oracion){
        this.subItem=sub;
        this.oracion=oracion;
    }
    
    
    /**
     * @return the subItem
     */
    public SubItem getSubItem() {
        return subItem;
    }

    /**
     * @param subItem the subItem to set
     */
    public void setSubItem(SubItem subItem) {
        this.subItem = subItem;
    }

    /**
     * @return the oracion
     */
    public String getOracion() {
        return oracion;
    }

    /**
     * @param oracion the oracion to set
     */
    public void setOracion(String oracion) {
        this.oracion = oracion;
    }
    
}//class
