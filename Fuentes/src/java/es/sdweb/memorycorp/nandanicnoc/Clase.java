package es.sdweb.memorycorp.nandanicnoc;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Listado de clases de un dominio (pe "CLASE 1: TOMA DE CONCIENCIA DE LA SALUD", "CLASE 2: GESTIÓN DE LA SALUD",
 * etc, del dominio "DOMINIO 1: Promoción de la salud".
 * @author Antonio Carro Mariño
 */
public class Clase {
    private String code="";
    private String name="";
    private String description="";
    private ArrayList itemList=new ArrayList(); //Puede contener objetos NandaItem, NicItem, o NocItem
    private Hashtable itemTable=new Hashtable();
    
    public Clase(String name){
        this.name=name;
    }
    
    public void addItem(IItem item){
        this.itemList.add(item);
        this.itemTable.put(item.getCode(), item);
    }
    
    public IItem getItem(String code){
        IItem result=(IItem)this.itemTable.get(code);
        return result;
    }

    /**
     * @return Devuelve la lista de ítems
     */
    public ArrayList<IItem> getItemList() {
        return itemList;
    }

    /**
     * @param itemsList Lista de items
     */
    public void setItemList(ArrayList<IItem> itemsList) {
        this.itemList = itemsList;
    }

    /**
     * @return Devuelve la tabla hash de items
     */
    public Hashtable getItemsTable() {
        return itemTable;
    }

    /**
     * @param itemTable Establece la tabla de items.
     */
    public void setItemsTable(Hashtable itemTable) {
        this.itemTable = itemTable;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    
}//class
