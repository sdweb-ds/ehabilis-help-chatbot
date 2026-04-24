package es.sdweb.memorycorp.rulesengine;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Antonio
 */
public class Rule {
    
    private String id="";
    private String idPadre="";
    private Rule reglaPadre=null;
    private ArrayList<Rule> reglasHijas=new ArrayList<>();
    private ArrayList<Attribute> attributes=new ArrayList<>();
    private ArrayList<Action> actions=new ArrayList<>();
    private ArrayList<String> variables=new ArrayList<>();
    private String input="";
    private String output="";
    private String outputError="";
    private boolean errorFlag=false; //Cuando se activa este Flag, el sistema debe generar la salida a partir de outputError, no de output
    private RuleSet ruleSet=null; //Juego de Reglas al que pertenece esta regla
    private boolean ejecutada=false; //Flag que Indica si esta regla ha sido ejecutada al menos una vez.
    private boolean checkVariables=false; //Flag que indica si debe verificarse que las variables que se usan en las salidas (output) están presentes en la entrada seleccionada (input).
    private String inputPatternActivated=""; //Patrón activado de esta regla.
    private int priority=0; //Prioridad en la ejecución, cuanto más alto es este valor (empieza en 0) mayor es la prioridad entre las reglas seleccionadas 
    private HashMap inputPatternActivatedVariables=new HashMap(); //Map donde guardaremos las variables del patrón activado de esta regla
    
    public Rule(){
    }
    
    /**
     * Clona la Regla, pero solo su estado, no toda la estructura/punteros a otros objetos con los que enlaza.
     * Es un clonado "light".
     * @return Clon de la regla.
     */
    @Override
    public Rule clone(){
        Rule result=new Rule();
        result.setId(id);
        result.setIdPadre(idPadre);
        result.setInput(input);
        result.setOutput(output);
        result.setReglaPadre(reglaPadre);
        result.setEjecutada(ejecutada);
        result.setReglasHijas(reglasHijas);
        result.setRuleSet(ruleSet);
        result.setAttributes(attributes);
        result.setActions(actions);
        result.setVariables(variables);
        result.setCheckVariables(checkVariables);
        return result;
    }

    /**
     * Establece el Juego de Reglas al que pertenece esta regla.
     * @param ruleSet Juego de Reglas al que pertenece esta.
     */
    public void setRuleSet(RuleSet ruleSet){
        this.ruleSet=ruleSet;
    }

    /**
     * Devuelve el Conjunto de Reglas (RuleSet) al que pertenece esta regla.
     * @return RuleSet al que pertenece esta regla.
     */
    public RuleSet getRuleSet(){
        return this.ruleSet;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the reglaPadre
     */
    public Rule getReglaPadre() {
        return reglaPadre;
    }
    
    public String getIdPadre(){
        return this.idPadre;
    }

    /**
     * @param reglaPadre the reglaPadre to set
     */
    public void setReglaPadre(Rule reglaPadre) {
        this.reglaPadre = reglaPadre;
        this.setIdPadre(reglaPadre!=null?reglaPadre.getId():"");
    }
    
    /**
     * Añade una regla como regla hija. OJO!: No comprueba si está repetida o su padre es otro.
     * @param regla Regla hija a añadir a la presente regla.
     */
    public void addReglaHija(Rule regla){
       regla.setReglaPadre(this); //Enlazamos su padre
       this.reglasHijas.add(regla); //La añadimos a la lista
    }
    
    /**
     * Añade un atributo a la regla NLP/NLG.
     * @param attribute Atributo a añadir.
     */
    public void addAttribute(Attribute attribute){
        this.getAttributes().add(attribute); //La añadimos a la lista
    }
    
    /**
     * Añade un atributo a la regla NLP/NLG.
     * @param name Nombre del atributo, p.e. "color"
     * @param value Valor del atributo, p.e. "azul"
     * @param object Objeto al que pertenece el atributo, p.e. "pájaro"
     */
    public void addAttribute(String name, String value, String object){
       Attribute attribute=new Attribute(name, value, object);
       this.getAttributes().add(attribute); //La añadimos a la lista
    }
    
    /**
     * Añade una acción a la regla NLP/NLG.
     * @param action Atributo a añadir.
     */
    public void addAction(Action action){
        this.getActions().add(action); //La añadimos a la lista
    }
    
    public void addVariable(String variable){
        boolean existe=false;
        for (String var:variables){ //Miramos que no se repita la variable
            if (var.equals(variable)){
                existe=true;
                break;
            }
        }//for
        if (!existe){
            variables.add(variable); //la añadimos
        }
    }
    
    /**
     * Añade una acción  a la regla NLP/NLG.
     * @param name Nombre del atributo, p.e. "color"
     * @param input Valor del atributo, p.e. "azul"
     * @param output Objeto al que pertenece el atributo, p.e. "pájaro"
     */
    public void addAction(String name, String input, String output){
       Action action=new Action(name, input, output);
       this.getActions().add(action); //La añadimos a la lista
    }
    
    /**
     * Obtiene una regla HIJA a partir de su ID.
     * @param id ID de la regla hija buscada.
     * @return Regla hija (Rule) buscada. Null si no existe.
     */
    public Rule gerReglaHija(String id){
        Rule result=(Rule)this.ruleSet.getRule(id);
        return result;
    }

    /**
     * @return the reglasHijas
     */
    public ArrayList<Rule> getReglasHijas() {
        return reglasHijas;
    }

    /**
     * @param reglasHijas the reglasHijas to set
     */
    public void setReglasHijas(ArrayList<Rule> reglasHijas) {
        this.reglasHijas = reglasHijas;
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

    /**
     * @return the ejecutada
     */
    public boolean isEjecutada() {
        return ejecutada;
    }

    /**
     * @param ejecutada the ejecutada to set
     */
    public void setEjecutada(boolean ejecutada) {
        this.ejecutada = ejecutada;
    }
    
    
    @Override
    public String toString(){
        String result="ID: "+this.getId()+"    Input: "+this.getInput();
        return result;
    }

    /**
     * @param idPadre the idPadre to set
     */
    public void setIdPadre(String idPadre) {
        this.idPadre = idPadre;
    }

    /**
     * @return the outputError
     */
    public String getOutputError() {
        return outputError;
    }

    /**
     * @param outputError the outputError to set
     */
    public void setOutputError(String outputError) {
        this.outputError = outputError;
    }

    /**
     * @return the errorFlag
     */
    public boolean isErrorFlag() {
        return errorFlag;
    }

    /**
     * @param errorFlag the errorFlag to set
     */
    public void setErrorFlag(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }

    /**
     * @return the attributes
     */
    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the actions
     */
    public ArrayList<Action> getActions() {
        return actions;
    }

    /**
     * @param actions the actions to set
     */
    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    /**
     * @return the variables
     */
    public ArrayList<String> getVariables() {
        return variables;
    }

    /**
     * @param variables the variables to set
     */
    public void setVariables(ArrayList<String> variables) {
        this.variables = variables;
    }

    /**
     * @return the checkVariables
     */
    public boolean isCheckVariables() {
        return checkVariables;
    }

    /**
     * @param checkVariables the checkVariables to set
     */
    public void setCheckVariables(boolean checkVariables) {
        this.checkVariables = checkVariables;
    }

    /**
     * @return the inputPatternActivated
     */
    public String getInputPatternActivated() {
        return inputPatternActivated;
    }

    /**
     * @param inputPatternActivated the inputPatternActivated to set
     */
    public void setInputPatternActivated(String inputPatternActivated) {
        this.inputPatternActivated = inputPatternActivated;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @return the inputPatternActivatedVariables
     */
    public HashMap getInputPatternActivatedVariables() {
        return inputPatternActivatedVariables;
    }

    /**
     * @param inputPatternActivatedVariables the inputPatternActivatedVariables to set
     */
    public void setInputPatternActivatedVariables(HashMap inputPatternActivatedVariables) {
        this.inputPatternActivatedVariables = inputPatternActivatedVariables;
    }
    
    
}//class
