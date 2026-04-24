package es.sdweb.memorycorp.nandanicnoc;

/**
 * Subitem (pe "Ausencia de tos" de tipo "Característica definitoria), perteneciente a un item (pe "NANDA [00031]: Limpieza ineficaz de las vías aéreas"),
 * perteneciente a una clase (pe "Clase 2: Lesión física"), perteneciente a un dominio de NANDA (pe "Dominio 11: Seguridad/Protección")
 * @author Antonio Carro mariño
 */
public class SubItem {

    private String description=""; //Característica y/o factor
    private String tipo="";

    public SubItem(String subItem, String tipo){
        this.description=subItem;
        this.tipo=tipo;
    }

    /**
     * @return the subItem
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param subItemDesc The subItem description to set
     */
    public void setDescription(String subItemDesc) {
        this.description = subItemDesc;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}//Class
