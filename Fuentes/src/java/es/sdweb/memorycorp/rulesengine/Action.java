package es.sdweb.memorycorp.rulesengine;

/**
 * Clase que define una acción asociada a una regla NLP/NLG
 * @author Antonio
 */
public class Action {
    private String name=""; //Nombre de la acción (literal), p.e. "obtén las subclases"
    private String input=""; //Valor de entrada que recibe (suele ser un literal o una variable), p.e. "pájaro"
    private String output=""; //Variable donde se guarda la salida , p.e. "gorrión"
    
    /**
     * Crea una acción vinculada a una regla NLP/NLG
     * @param name Nombre de la acción (literal), p.e. "obtén las subclases"
     * @param input Valor de entrada que recibe (suele ser un literal o una variable), p.e. "pájaro"
     * @param output Variable donde se guarda la salida , p.e. "gorrión"
     */
    public Action(String name, String input, String output){
        this.name=name;
        this.input=input;
        this.output=output;
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
     * @return the input
     */
    public String getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * @return the output
     */
    public String getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(String output) {
        this.output = output;
    }
    
}//class
