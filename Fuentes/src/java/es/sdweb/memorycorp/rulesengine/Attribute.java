package es.sdweb.memorycorp.rulesengine;

/**
 * Atributo vinculado a una regla NLP/NLG.
 * @author Antonio
 */
public class Attribute {
    private String name=""; //Nombre del atributo, p.e. "color"
    private String object=""; //Nombre del objeto al que pertenece el atributo, p.e. "pájaro"
    private String value=""; //Nombre del valor del atributo, p.e. "azul"
    
    public Attribute(String name, String value, String object){
        this.name=name;
        this.value=value;
        this.object=object;
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
     * @return the object
     */
    public String getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    
}//Class
