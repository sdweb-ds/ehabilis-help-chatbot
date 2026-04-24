package es.sdweb.memorycorp.dialogue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.memorycorp.core.LearningEngine;
import es.sdweb.memorycorp.core.Node;
import es.sdweb.memorycorp.core.PrimitiveStructAnalizer;
import es.sdweb.memorycorp.nlpengine.DWord;
import es.sdweb.memorycorp.nlpengine.Dictionary;
import es.sdweb.memorycorp.nlpengine.LOracion;
import es.sdweb.memorycorp.nlpengine.LParrafo;
import es.sdweb.memorycorp.rulesengine.Action;
import es.sdweb.memorycorp.rulesengine.Rule;
import es.sdweb.memorycorp.rulesengine.RuleSet;


/**
 * Clase que agrupa la lógica principal de gestión de los diálogos. Incluye la generación de lenguaje natural (NLG).
 * @author Antonio Carro Mariño
 */
public class DialogueEngine {

    private final static String PATH_BASE_ARCHIVO_REGLAS="/opt/desarrollo/memorycorp/";
    private final static String NOMBRE_ARCHIVO_REGLAS="rules.txt"; //Archivo con reglas NLP/NLG a ser cargado en memoria
    //private final static String NOMBRE_ARCHIVO_REGLAS="rule_prueba.txt"; //Archivo con reglas NLP/NLG a ser cargado en memoria
    private Document xmlDocument=null; //Objeto que contiene el reflejo JDom del fichero XML de reglas.

    private RuleSet ruleSet=new RuleSet(); // Conjunto de Reglas donde se cargará el fichero de definición de las reglas

    //HashMap(String idRule, Rule regla). Solo estarán las reglas raiz en este HashMap, ya que son realmente las ramas.
    //Para cada regla raiz identifica la última regla activada de su rama. Si no se activó ninguna, el valor será null.
    //Al llegar al final de la ejecución de una rama se reseteará a null el valor, para volver a empezar. Se llega al final de una rama, cuando la regla ejecutada es nodo hoja.
    private HashMap dialogueLevelTable=new HashMap();
    private Rule lastRootRuleActivated=null; //Indica la útima rama activada para, de entre las reglas seleccionadas, dar continuidad al diálogo mantenido en una rama

    //El LearningEngine se le pasa en la creación, y contiene todas las utilidades NLP y el Diccionario de Español.
    private LearningEngine learningEngine=null;


    /**
     * Inicializa el Motor de Diálogo del ChatBot, a partir del fichero "rules.txt". Genera el RuleSet (Conjunto de Reglas).
     * Todo el procesamiento y gestión del Diálogo se apoyará en el LearningEngine, con capacidades NLP y el Diccionario de Español.
     */
    public DialogueEngine(LearningEngine learningEngine){
        this.learningEngine=learningEngine;
        loadRuleSet(NOMBRE_ARCHIVO_REGLAS); //Genera el Conjunto de Reglas a partir del XML cargado del archivo por defecto.
    }

    /**
     * Carga del fichero XML de reglas de disco, y crea una imagen JDom del Document xml en memoria.
     * A partir de esta imagen (que se recorre) se genera el RuleSet (Conjunto de Reglas).
     * @param nombreArchivo Nombre del archivo XML de reglas.
     * @return Objeto Document (de JDom) con la imagen del archivo de reglas XML cargado.
     */
    public static Document loadRulesFile(String nombreArchivo){
        Document result=null;
        try {
            SAXBuilder builder = new SAXBuilder();
            nombreArchivo=PATH_BASE_ARCHIVO_REGLAS+nombreArchivo; //Obtenemos la ruta completa al archivo
            result = builder.build(nombreArchivo);
        } catch(JDOMException e) {
            LogUtil.logError(e);
        } catch(IOException e) {
            LogUtil.logError(e);
        }
        return result;
    }

    /**
     * Obtiene la última regla activada en la rama del idRootRule especificado.
     * @param idRootRule Identificador de una regla raíz.
     * @return Ultima regla activada en esa rama.
     */
    private Rule getLastActivatedRule(String idRootRule){
        Rule result=(Rule)this.dialogueLevelTable.get(idRootRule);
        return result;
    }

    /**
     * Obtiene el listado de reglas que se activan ante la entrada del usuario.
     * Para ello, vamos recorriendo las reglas raiz, miramos la situación de cada rama y vamos anotando las reglas activadas.
     * En cada rama, se busca a partir de la última regla ejecutada. Por tanto, esta búsqueda deja subramas sin explorar, para
     * tratar de dar continuidad lógica al diálogo.
     * @param userInput Entrada de texto tecleada por un usuario.
     * @return Lista de reglas activadas.
     */
    public ArrayList<Rule> getRulesActivables(String userInput){
        ArrayList<Rule> result=new ArrayList();
        ArrayList<Rule> reglasRaiz=this.getRuleSet().getRootRules(); //Cogemos las reglas raiz
        for (Rule r:reglasRaiz){ //Vamos cotejando cada regla raiz
            LogUtil.logTraza("DialogueEngine: Evaluando MATCH de la regla de ID:"+r.getId()+" de prioridad "+r.getPriority(), 6);
            Rule lastRuleActivated=getLastActivatedRule(r.getId()); //Obtenemos la última regla ejecutada en esta rama
            if (lastRuleActivated==null && this.ruleSet.match(userInput, r, this.getLearningEngine())){ //Si la regla raiz es la seleccionada y encaja
                LogUtil.logTraza("DialogueEngine: La regla de ID:"+r.getId()+" y prioridad "+r.getPriority()+" se ha ACTIVADO.", 6);
                result.add(r); //Añadimos la regla raiz a la lista de reglas activables
            }else{ //En otro caso significa que se ha avanzado en esta rama
                for (Rule rHija:r.getReglasHijas()){ //Analizamos todas las reglas hijas del siguiente nivel (únicamente) a partir de la última regla ejecutada
                    LogUtil.logTraza("DialogueEngine: Evaluando MATCH de la regla de ID:"+rHija.getId()+" de prioridad "+r.getPriority(), 2);
                    if (this.ruleSet.match(userInput, rHija, this.getLearningEngine())){ //Si alguna hija encaja
                       LogUtil.logTraza("DialogueEngine: La regla de ID:"+rHija.getId()+" y prioridad "+r.getPriority()+" se ha ACTIVADO.", 6);
                       result.add(rHija); //Añadimos la hija a la lista de reglas activables
                    } //If hija encaja
                }//For reglas hijas
            }//if raiz o hijos
        }//for reglas raiz
        return result;
    }

    /**
     * Recorre el árbol de reglas recursivamente, buscando todas las reglas que encajen.
     * Rastrea exhaustivamente todas las reglas del árbol, empezando por el propio nodo/regla recibido como parámetro.
     * @param userInput Entrada textual del usuario que se analiza
     * @param nodo Regla/Nodo en el que iniciamos el análisis del match.
     * @param lista Lista de reglas halladas que encajan con el userInput
     * @param noEjecutadas Indicaremos True si queremos captar sólo reglas no ejecutadas. False si nos da igual este estado.
     */
    private void fillAllRulesActivablesTree(String userInput, Rule nodo, ArrayList<Rule> lista, boolean noEjecutadas){
        if (nodo!=null){
            if (this.ruleSet.match(userInput,nodo, this.getLearningEngine()) && (!noEjecutadas || (!nodo.isEjecutada() && noEjecutadas))){ //Si la regla analizada encaja
                lista.add(nodo); //Añadimos la regla raiz a la lista de reglas activables, y no profundizamos más.
            }else{
                for (Rule rHija:nodo.getReglasHijas()){ //Analizamos todas las reglas hijas de esta regla
                    fillAllRulesActivablesTree(userInput, rHija, lista, noEjecutadas); //Para completar la lista
                }//for reglas hijas
            }//Si este nodo encaja
        }//Si nodo no es nulo
    }

    /**
     * Obtiene el listado de reglas todas que se activan ante la entrada del usuario, independientemente de la progresión en cada rama, y de la rama prioritaria.
     * Se trata de un recorrido exhaustivo sobre todas las ramas del árbol de reglas.
     * Para ello, recorremos todo el árbol de reglas. Este método es útil cuando no hay ninguna regla activable si consideramos el avance sobre cada rama.
     * @param userInput Entrada de texto tecleada por un usuario.
     * @param noEjecutadas Indicaremos True si queremos captar sólo reglas no ejecutadas. False si nos da igual que la regla esté ejecutada o no.
     * @return Lista de reglas activadas.
     */
    public ArrayList<Rule> getAllRulesActivables(String userInput, boolean noEjecutadas){
        ArrayList<Rule> result=new ArrayList();
        ArrayList<Rule> reglasRaiz=this.getRuleSet().getRootRules(); //Cogemos las reglas raiz
        for (Rule r:reglasRaiz){ //Vamos cotejando cada regla raiz
            fillAllRulesActivablesTree(userInput, r, result, noEjecutadas); //Llenamos la lista con todos los hallazgos en esta rama
        }//for reglas raiz
        return result;
    }


    /**
     * De la lista de reglas activables, selecciona/activa una de ellas para ser ejecutada (No la ejecuta, solo la activa).
     * El mecanismo de selección de la regla a ajecutar puede ser lo complejo que se quiera. Si se debe reseterar las variables de la regla, se resetean.
     * En el caso más sencillo será una regla seleccionada al azar.
     * @param activableRules Lista de reglas activables (es decir, que hacer match con la entrada del usuario).
     * @return Regla seleccionada para activarse.
     */
    public Rule activateRule(ArrayList<Rule> activableRules){
        Rule result=null;
        if (this.lastRootRuleActivated!=null){ //Si hay preferencia por una rama (para continuar ese hilo)
            for (Rule rule:activableRules){ //Recorremos las reglas activables para localizar la primera ejecutable de esa rama
                String idLastRootRuleActivated=lastRootRuleActivated.getId();
                String rootRule=this.ruleSet.getIdRootRule(rule.getId());
                if (rootRule.equals(idLastRootRuleActivated)){
                    result=rule;
                    result.setPriority(result.getPriority()+RuleSet.MATCH_SAME_ROOT); //Incrementamos su prioridad para tratar de seguir por la misma rama
                    break; //Salimos del bucle pues ya tenemos una regla localizada
                }//Si la regla activada es de la misma rama que la última ejecutada
            }//for rules activables
        } //Si se activó una regla justo antes

        if (result==null){ //Si no tenemos ninguna regla activada en la rama prioritaria, cogemos una regla al azar
            //int totalReglas=activableRules.size();
            //int pos=(int)Math.floor(Math.random()*totalReglas); //Escogemos/activamos una regla al azar para ser ejecutada
            //result=activableRules.get(pos);
            result=DialogueUtil.getPrioritaryRule(activableRules); //Cogemos una de las reglas prioritarias (es decir, más específicas)
        }

        //Marcamos su activacion en su rama, es decir, la marcamos como última regla ejecutada de esa rama
        Rule raiz=this.ruleSet.getRootRule(result.getId()); //Obtenemos la regla raiz de la seleccionada
        this.dialogueLevelTable.put(raiz.getId(), result); //Indicamos la regla activada
        this.lastRootRuleActivated=raiz; //Marcamos la rama seleccionada, de forma que tendrá preferencia en siguientes iteraciones

        return result; //La devolvemos
    }


    /**
     * Procesa los atributos de la regla ejecutada (si los hubiera), para crear los correspondientes fragmentos de Red Semántica.
     * En este proceso intervienen el objeto (que debe ser creado si no existe, p.e. "coche"), el atributo (que es un nombre p.e. "color"), el valor del atributo (suele ser un ADJ, p.e. "azul").
     * @param userInput Párrafo con el texto de entrada ya analizado y con su árbol de descomposición sintáctica.
     * @param ruleSelected Regla ejecutada.
     */
    private void processAttributes(LParrafo userInput, Rule ruleSelected){
        for (es.sdweb.memorycorp.rulesengine.Attribute atr:ruleSelected.getAttributes()){ //Para cada atributo, generamos el correspondiente fragmento de red
            String name=(atr.getName().indexOf("[?")>=0?this.ruleSet.getVariable(atr.getName()):atr.getName()); //Si es una variable, obtenemos su valor; en otro caso cogemos el literal
            String value=(atr.getValue().indexOf("[?")>=0?this.ruleSet.getVariable(atr.getValue()):atr.getValue()); //Si es una variable, obtenemos su valor; en otro caso cogemos el literal
            String object=(atr.getObject().indexOf("[?")>=0?this.ruleSet.getVariable(atr.getObject()):atr.getObject()); //Si es una variable, obtenemos su valor; en otro caso cogemos el literal

            //LogUtil.logConsole("Atributo ["+name+"] con valor ["+value+"] del objeto ["+object+"].");
            //Creamos la propiedad con los valores correctos
            es.sdweb.memorycorp.rulesengine.Attribute propiedad=new es.sdweb.memorycorp.rulesengine.Attribute(name,value,object);
            LOracion or=userInput.getOraciones().get(0); //Procesamos solo la primera oracion.
            this.getLearningEngine().generateAttributeNetChunk(or, propiedad); //Generamos a partir de ella el correspondiente fragmento de Red Semática
        }//for

    }


    /**
     * Procesa las acciones de la regla ejecutada (si las hubiera), para calcular el resultado en base a la Red Semántica.
     * El resultado de una acción se almacena siempre en una variable, que puede estar presente en el <output> de la regla.
     * @param userInput Párrafo con el texto de entrada ya analizado y con su árbol de descomposición sintáctica.
     * @param ruleSelected Regla ejecutada.
     */
    private void processActions(LParrafo userInput, Rule ruleSelected){
        for (Action act:ruleSelected.getActions()){ //Para cada acción, obtnemos la correspondiente respuesta que almacenamos en la correspondiente variable
            String name=StringUtil.trim(act.getName().indexOf("[?")>=0?this.ruleSet.getVariable(act.getName()):act.getName()); //Si es una variable, obtenemos su valor; en otro caso cogemos el literal
            String input=StringUtil.trim((act.getInput().indexOf("[?")>=0?this.ruleSet.getVariable(act.getInput()):act.getInput())); //Si es una variable, obtenemos su valor; en otro caso cogemos el literal
            String output=StringUtil.trim(act.getOutput()); //El output siempre debe ser una variable de la que no hay que obtener su valor, sino almacenarlo en ella.

            //Creamos la acción con los valores correctos
            Action action=new Action(name,input,output);
            String[] variables=null; //Variable auxiliar para almacenar variables

            switch (action.getName()){
                //Accion para recuperar los atributos de un objeto de la Red Semántica; p.e. "Cuáles son las propiedades de amputado"
                case "attributes": //Esta accion tiene como campos obligatorios: name, input, output
                    String result=this.getLearningEngine().getCualidades(input); //Obtenemos las cualidades del objeto
                    this.ruleSet.setVariable(output, result); //Las almacenamos en la variable de salida, para que se puedan imprimir en la respuesta del Bot.
                    this.getLearningEngine().getDictionary().addWord(new DWord(name));//Añadimos el nombre de atributo al diccionario (por si no existiera, si existe no hace nada)
                    break;
                //Acción para guardar una relación de clase->subclase o clase->superclase
                case "loadsuperclass": //Esta accion tiene como campos obligatorios: name, input
                    variables=act.getInput().split(","); //Nos llegarán los nombres de dos variables en este formato: [?OT:R104-01],[?OT:R103-02]
                    if (variables.length<=1){
                        LogUtil.logError("ERROR: Error en <action> de la regla ["+ruleSelected.getId()+"], las variables del input no son correctas. El formato debe ser: [?OT:R104-01],[?OT:R103-02]");
                    }
                    String subclase=StringUtil.trim(this.ruleSet.getVariable(variables[0].trim()));
                    String superclase=StringUtil.trim(this.ruleSet.getVariable(variables[1].trim()));

                    this.getLearningEngine().loadSubclaseSuperclase(userInput,subclase,superclase); //Cargamos en la Red Semántica la relación de herencia
                    break;
                //Acción para mostrar las superclases de un término
                case "superclasses": //Esta accion tiene como campos obligatorios: name, input, output
                    String arbolAsc=getLearningEngine().printJerarquiaAscendente(input); //El input recibe el objeto del que se quiere conocer sus superclases
                    this.ruleSet.setVariable(output, arbolAsc); //La almacenamo en la variable de salida, para que se pueda imprimir en la respuesta del Bot.
                    break;
                //Acción para mostrar las subclases de un término
                case "subclasses": //Esta accion tiene como campos obligatorios: name, input, output
                    String arbolDesc=getLearningEngine().printJerarquiaDescendente(input); //El input recibe el objeto del que se quiere conocer sus subclases
                    this.ruleSet.setVariable(output, arbolDesc); //La almacenamo en la variable de salida, para que se pueda imprimir en la respuesta del Bot.
                    break;
                //Acción para mostrar los elementos con una propiedad y valor determinados: "Dame los elementos cuyo atributo color es azul".
                case "searchbyattribute": //Esta accion tiene como campos obligatorios: name, input, output
                    variables=act.getInput().split(","); //Nos llegarán los nombres de dos variables en este formato: [?OT:R104-01],[?OT:R103-02]
                    if (variables.length<=1){
                        LogUtil.logError("ERROR: Error en <action> de la regla ["+ruleSelected.getId()+"], las variables del input no son correctas. El formato debe ser: [?OT:R104-01],[?OT:R103-02]");
                    }
                    String atName=StringUtil.trim(this.ruleSet.getVariable(variables[0].trim()));
                    String atValue=StringUtil.trim(this.ruleSet.getVariable(variables[1].trim()));

                    String atts=this.getLearningEngine().getElementsbyAttribute(atName, atValue, true);
                    this.ruleSet.setVariable(output, atts); //La almacenamo en la variable de salida, para que se pueda imprimir en la respuesta del Bot.
                    break;
                //Acción para indicar que dos elementos son sinónimos, p.e. "propiedad morfológica" y "característica morfológica"
                case "synonyms": //Esta accion tiene como campos obligatorios: name, input
                    variables=act.getInput().split(","); //Nos llegarán los nombres de dos variables en este formato: [?OT:R104-01],[?OT:R103-02]
                    if (variables.length<=1){
                        LogUtil.logError("ERROR: Error en <action> de la regla ["+ruleSelected.getId()+"], las variables del input no son correctas. El formato debe ser: [?OT:R104-01],[?OT:R103-02]");
                    }
                    String sin1=StringUtil.trim(this.ruleSet.getVariable(variables[0].trim()));
                    String sin2=StringUtil.trim(this.ruleSet.getVariable(variables[1].trim()));

                    Dictionary dic=getLearningEngine().getDictionary();
                    DWord w1=dic.getWord(sin1);
                    DWord w2=dic.getWord(sin2);
                    if (w1==null) { //Si la palabra no existe en el diccionario
                        w1=new DWord(sin1); //La creamos
                        dic.addWord(w1); //Y la guardamos en el diccionario
                    }
                    if (w2==null) { //Si la palabra no existe en el diccionario
                        w2=new DWord(sin2); //La creamos
                        dic.addWord(w2); //Y la guardamos en el diccionario
                    }
                    w1.addSinonimo(w2); //Ahora que ya existen ambas palabras en el diccionario, las hacemos sinónimas
                    break;
                //Acción para mostrar el valor de la propiedad de un término
                case "getattributevalue": //Esta accion tiene como campos obligatorios: name, input, output
                    variables=act.getInput().split(","); //Nos llegarán los nombres de dos variables en este formato: [?OT:R104-01],[?OT:R103-02]
                    if (variables.length<=1){
                        LogUtil.logError("ERROR: Error en <action> de la regla ["+ruleSelected.getId()+"], las variables del input no son correctas. El formato debe ser: [?OT:R104-01],[?OT:R103-02]");
                    }
                    String obj=StringUtil.trim(this.ruleSet.getVariable(variables[0].trim())); //Objeto
                    String att=StringUtil.trim(this.ruleSet.getVariable(variables[1].trim())); //Nombre del atributo

                    //Recuperamos el valor del atributo
                    String valor=PrimitiveStructAnalizer.getCualidad(obj,att, getLearningEngine().getDeepPlane());
                    this.ruleSet.setVariable(output, valor); //La almacenamo en la variable de salida, para que se pueda imprimir en la respuesta del Bot.

                    //Tratamos los errores: el objeto no existe, o no tiene esa cualidad
                    if (StringUtil.isEmpty(valor)){ //Si no se encontró un valor asociado a la cualidad, o el objeto no existe, mostramos mensaje de error
                        ruleSelected.setErrorFlag(true); //Implica que hay un error
                        Node aux=getLearningEngine().getDeepPlane().getNodeSustantivo(obj);
                        if (aux==null){ //Si no existe el objeto
                           this.ruleSet.setVariable(output, "Lo siento, no conozco nada sobre el concepto/objeto \""+obj+"\".");
                        }else{
                           this.ruleSet.setVariable(output, "El concepto/objeto \""+obj+"\" no tiene ninguna propiedad con ese nombre.");
                        }
                    }
                    break;
                default:
                    break;
            }//switch

        }//for
    }

    /**
     * Genera la frase de respuesta del Chatbot a partir de la regla seleccionada y del texto de entrada del usuario.
     * @param userInput Texto introducido por el usuario. Puede ser necesario para componer una respuesta.
     * @param ruleSelected Regla NLG escogida para su aplicación.
     * @return Cadena de texto de respuesta del Chatbot.
     */
    private String executeRule(String userInput, Rule ruleSelected){
        ruleSelected.setEjecutada(true); //Marcamos la regla como ejectutada (si no lo estaba ya)
        ruleSelected.setErrorFlag(false); //Una vez ejecutada la regla, la reseteamos este flag a su estado inicial

        if (ruleSelected.getReglasHijas().isEmpty()){ //Si hemos ejecutado un nodo hoja, volvemos al principio en esa rama
            String idRootRule=this.ruleSet.getIdRootRule(ruleSelected.getId());
            this.dialogueLevelTable.put(idRootRule, null); //Reseteamos esa rama
            this.lastRootRuleActivated=null; //Esa rama deja de tener prioridad
        }//If nodo hoja ejecutado

        //PROCESAMOS LA REGLA A NIVEL DE RED SEMÁNTICA
        // 1. Guardamos las variables de la regla activada (si existen)
        this.ruleSet.getVariable().putAll(ruleSelected.getInputPatternActivatedVariables());

        // 2. Procesamos los atributos si existen
        if (!ruleSelected.getAttributes().isEmpty()){ //Si hay algún atributo de la regla que procesar, lo procesamos
            LParrafo par=this.getLearningEngine().getEngine().addParrafo(userInput); //Analizamos sintácticamente la entrada
            processAttributes(par, ruleSelected); //Y procesamos los atributos identificados en la misma para insertarlos en la Red Semántica
        }//atributos

        // 3. Procesamos las acciones si existen
        if (!ruleSelected.getActions().isEmpty()){ //Si hay alguna acción de la regla que procesar, la procesamos
            LParrafo par=this.getLearningEngine().getEngine().addParrafo(userInput); //Analizamos sintácticamente la entrada
            processActions(par, ruleSelected); //Y procesamos las acciones identificadas en la misma para insertarlos en variables de respuesta
        }//acciones

        //GENERAMOS LA SALIDA (NLG)
        String result=this.ruleSet.getCompleteOutput(ruleSelected, this); //Completamos la salida (sus variables, etc)
        return result;
    }

    /**
     * Preprocesa la entrada del usuario para normalizarla y limar problemas de interpretación.
     * Devuelve una cadena de entrada normalizada. Esta función está pensada para tratar una sola oración.
     * @param userInput Cadena de entrada tecleada por el usuario.
     * @return Cadena de entrada normalizada.
     */
    public String preprocessUserInput(String userInput){
        String result=StringUtil.trim(userInput); //Eliminamos espacios superfluos
        if (!StringUtil.isEmpty(userInput)){ //Si el usuario escribió algo, anaizaremos varias casuísticas para normalizar el texto de entrada
            ArrayList<String> palabras=StringUtil.tokeniza(result, " "); //Separamos todas las palabras

            //Analizamos si la primera letra mayuscula está solo para respetar que se empieza una frase en mayuscula
            String primeraPalabra=palabras.get(0); //Cogemos la primera palabra. Siempre hay una.
            String minuscula=primeraPalabra.toLowerCase();
            DWord nombre=this.getLearningEngine().getDictionary().getNameOrLocation(primeraPalabra,null,null,false); //Vemos si el nombre existe en el diccionario
            if (nombre==null){ //Vemos si la palabra No es un Nombre propio (de persona o lugar)
               //Como no es un nombre, pasamos la primera palabra a minusculas.
               result=StringUtil.replaceFirst(result,primeraPalabra,minuscula);
            }//Si no es un nombre
        }
        return result;
    }

    /**
     * PUNTO DE ENTRADA A ESTA CLASE.
     * Procesa la entrada textual del usuario y genera una respuesta en base, entre otras cosas, a las reglas NLG del sistema.
     * @param userInput Entrada textual introducida por el usuario en el Chatbot.
     * @return Cadena de respuesta del Chatbot.
     */
    public String getAnswer(String userInput){
        String result="";
        if (StringUtil.isEmpty(StringUtil.trim(userInput))) return result; //Si el usuario no escribió nada, salimos directamente al prompt
        userInput=preprocessUserInput(userInput); //Preparamos/normalizamos el texto de entrada para su procesamiento

        ArrayList<Rule> activableRules=getRulesActivables(userInput); //Obtenemos las reglas activables respetando el progreso previo en las ramas

        if (activableRules.isEmpty()){ //Si no hay reglas disponibles respetando los progresos en cada rama
           //activableRules=getAllRulesActivables(userInput, true); //Buscamos cualquier regla no ejecutada previamente, pero que encaje
           activableRules=getAllRulesActivables(userInput, false); //Buscamos cualquier regla que encaje, se haya ejecutado o no previamente
           //if (activableRules.isEmpty()){ //Si aún así no encontramos reglas activables
              //activableRules=getAllRulesActivables(userInput, false); //Buscamos cualquier regla que encaje, se haya ejecutado o no previamente
           //}
        }//IF no hay activables

        if (!activableRules.isEmpty()){ //Si al menos hay una activable
           Rule reglaSeleccionada=activateRule(activableRules); //Activamos una regla NLG
           result=executeRule(userInput,reglaSeleccionada); //Elaboramos/Ejecutamos la respuesta en base a esa regla y al userInput.
        }else{ //Si no hay ninguna regla activable
           ArrayList<Rule> defaultRules=this.ruleSet.getDefaultRules(); //Obtenemos el listado de reglas por defecto
           if (!defaultRules.isEmpty()) { //Si hay alguna regla [default]
               Rule reglaSeleccionada=activateRule(defaultRules); //Activamos una regla NLG de las por defecto
               result=executeRule(userInput,reglaSeleccionada); //Elaboramos/Ejecutamos la respuesta en base a esa regla.
           }else{ //Si no hay reglas [default] definidas
              result="Lo siento, no tengo suficiente información para responder a eso";
           }//Si no hay [default]
        } // If hay una o más activables

        return result;
    }


    /**
     * Genera el RuleSet a partir del contenido XML del archivo de reglas, almacenado en this.xmlDocument.No borra la memoria de variables del RuleSet.
     * Únicamnete regenera las reglas.
     * @param fileName Especifica el nombre de archivo de reglas (p.e. "rule.txt"). Es opcional, si es null coge el filename por defecto.
     */
    public void loadRuleSet(String fileName){
        this.ruleSet.resetRuleSet(); //Reseteamos el RuleSet, ello incluye la memoria de variables

        if (!StringUtil.isEmpty(fileName)){ //Si se indicó nombre de archivo, lo cargamos
            xmlDocument=loadRulesFile(fileName);
        }

        for (Content caiml:xmlDocument.getContent()){ //Nivel Raiz <aiml>
          Element elaiml=(Element)caiml;
          String tag=elaiml.getName();  //Leemos el nombre de etiqueta
          if (!StringUtil.isEmpty(tag) && tag.toLowerCase().equals("xaiml")){ //Vemos si es un elemento <xaiml>. De los saltos de línea pasamos.
             for (Content crule:elaiml.getContent()){  //Recorremos el nivel de reglas
                if (crule instanceof Element){ //Puede ser una instancia de Element o de Text (si es un salto de línea)
                    Element elrule=(Element)crule;
                    tag=elrule.getName();  //Leemos el nombre de etiqueta
                    if (!StringUtil.isEmpty(tag) && tag.toLowerCase().equals("rule")){ //Si es un elemento <rule>
                        Attribute atId=elrule.getAttribute("idr"); //Leemos su id
                        if (atId==null){ //Si no tiene ID
                           LogUtil.logError("ERROR: Una de las reglas no dispone de idr.");
                        }
                        String idRule=atId.getValue();
                        if (StringUtil.isEmpty(idRule)){ //El ID es un dato obligatorio
                           LogUtil.logError("ERROR: Se ha detectado una regla sin ID.");
                        }
                        idRule=idRule.trim();

                        //Regla padre asociada a esta
                        Attribute atIdPadre=elrule.getAttribute("idrp"); //Leemos su ID de la regla padre. Este ID es opcional, si no existe => no tiene padre.
                        String idRulePadre=(atIdPadre!=null?atIdPadre.getValue():"");
                        Rule reglaPadre=null;
                        if (!StringUtil.isEmpty(idRulePadre)){
                           idRulePadre=idRulePadre.trim();
                           reglaPadre=this.getRuleSet().getRule(idRulePadre); //Obtenemos la regla padre, que deberá estar cargada previamente
                        }

                        //Reseteo de variables cada vez que se ejecuta
                        Attribute resetVariablesAtt=elrule.getAttribute("checkvariables"); //Leemos el flag.
                        String resetVariables=(resetVariablesAtt!=null?resetVariablesAtt.getValue():"");

                        //Creamos la Regla
                        Rule regla=new Rule();
                        regla.setId(idRule); //ID de la regla
                        regla.setReglaPadre(reglaPadre); //Regla padre
                        regla.setCheckVariables(StringUtil.isYes(resetVariables)); //Flag para verificar la coherencia de variables del output

                        //Cargamos su contenido
                        for (Content cInputOutput:elrule.getContent()){  //Recorremos los subelementos de <rule>
                            if (cInputOutput instanceof Element){ //Puede ser una instancia de Element o de Text (si es un salto de línea)
                                Element elIO=(Element)cInputOutput;
                                tag=elIO.getName();  //Leemos el nombre de etiqueta
                                if (!StringUtil.isEmpty(tag) && tag.toLowerCase().equals("input")){ //Si es un elemento <input>
                                    String textoNormalizado=preprocessUserInput(elIO.getValue()); //Lo normalizamos, pues puede contener errores, espacios superfluos, etc.
                                    regla.setInput(textoNormalizado);
                                }// If <input>
                                if (!StringUtil.isEmpty(tag) && tag.toLowerCase().equals("output")){ //Si es un elemento <output>
                                    String textoNormalizado=StringUtil.primeraMay(preprocessUserInput(elIO.getValue()),false); //Lo normalizamos, pues puede contener errores, espacios superfluos, etc.
                                    regla.setOutput(textoNormalizado);
                                } // If <output>
                                if (!StringUtil.isEmpty(tag) && tag.toLowerCase().equals("outputerror")){ //Si es un elemento <outputerror>
                                    String textoNormalizado=StringUtil.primeraMay(preprocessUserInput(elIO.getValue()),false); //Lo normalizamos, pues puede contener errores, espacios superfluos, etc.
                                    regla.setOutputError(textoNormalizado);
                                } // If <outputerror>
                                if (!StringUtil.isEmpty(tag) && tag.toLowerCase().equals("attributte")){ //Si es un elemento <attributte>
                                    Attribute at=elIO.getAttribute("name"); //Leemos el nombre del atributo. Este nombre es opcional, si no existe => el valor se asocia directamente al objeto.
                                    String name=(at!=null?preprocessUserInput(at.getValue()):"");
                                    at=elIO.getAttribute("value"); //Leemos el valor del atributo.
                                    String value=(at!=null?preprocessUserInput(at.getValue()):"");
                                    if (StringUtil.isEmpty(value)){
                                        LogUtil.logError("WARNING: El aributo "+name+" no tiene value.");
                                    }
                                    at=elIO.getAttribute("object"); //Leemos el objeto al que pertenece el atributo (siempre tiene que haber uno).
                                    String object=(at!=null?preprocessUserInput(at.getValue()):"");
                                    if (StringUtil.isEmpty(object)){
                                        LogUtil.logError("ERROR: El aributo "+name+" no tiene object.");
                                    }

                                    regla.addAttribute(name, value, object); //Añadimos el atributo a la regla
                                } // If <attribute>
                                if (!StringUtil.isEmpty(tag) && tag.toLowerCase().equals("action")){ //Si es un elemento <action>
                                    Attribute at=elIO.getAttribute("name"); //Leemos el nombre de la acción.
                                    String name=(at!=null?preprocessUserInput(at.getValue()):"");
                                    at=elIO.getAttribute("input"); //Leemos la entrada de la acción. Es un campo opcional
                                    String input=(at!=null?preprocessUserInput(at.getValue()):"");
                                    at=elIO.getAttribute("output"); //Leemos la variable de salida donde almacenar el resultado de la acción. Es un campo opcional.
                                    String output=(at!=null?preprocessUserInput(at.getValue()):"");

                                    regla.addAction(name, input, output); //Añadimos la acción a la regla
                                } // If <action>

                            } //If Element
                        }// For subelementos de <rule>

                        //La añadimos al RuleSet (Conjunto de Reglas)
                        this.getRuleSet().addRule(regla.getId(), regla);
                        LogUtil.logConsole("Added Rule "+regla.getId()+"(padre "+regla.getIdPadre()+"): "+regla.getInput()+" --> "+regla.getOutput());

                    }//If Rule
                }//If Element
             } //for rules
          } //if aiml
        }//for aiml

    } //End loadRuleSet()

    /**
     * @return the ruleSet
     */
    public RuleSet getRuleSet() {
        return ruleSet;
    }

    /**
     * @return the learningEngine
     */
    public LearningEngine getLearningEngine() {
        return learningEngine;
    }

}//End Class
