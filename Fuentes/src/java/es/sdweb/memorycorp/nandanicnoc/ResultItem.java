package es.sdweb.memorycorp.nandanicnoc;

import java.util.ArrayList;

/**
 * Clase DTO que contiene: el IItem Nanda/Nic/Noc implicado, array de subItems implicados.
 * @author Antonio Carro Mariño
 */
public class ResultItem {
    private IItem item=null; //Nanda, Nic o Noc cuya presencia se ha detectado
    private ArrayList<ResultSubItem> subItemList=new ArrayList(); //Listado de subItems implicados
    
    /**
     * Crea un ResultItem en el resultado a partir de un Item cuya presencia se ha detectado (NANDA, NIC o NOC).
     * @param item Item (NANDA, NIC o NOC) detectado.
     */
    public ResultItem(IItem item){
        this.item=item;
        this.subItemList=new ArrayList(); //Creamos una lista vacia de subItemns
    }
    
    /**
     * Añade un subItem a la lista (pe "Tabaquismo"-"La paciente fuma mucho").
     * @param sub SubItem a añadir a la lista de resultados.
     */
    public void addResultSubItem(ResultSubItem sub){
        this.subItemList.add(sub);
    }
    

    /**
     * @return the item
     */
    public IItem getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(IItem item) {
        this.item = item;
    }

    /**
     * @return the subItemList
     */
    public ArrayList<ResultSubItem> getSubItemList() {
        return subItemList;
    }

    /**
     * @param subItemList the subItemList to set
     */
    public void setSubItemList(ArrayList<ResultSubItem> subItemList) {
        this.subItemList = subItemList;
    }
    
}//class
