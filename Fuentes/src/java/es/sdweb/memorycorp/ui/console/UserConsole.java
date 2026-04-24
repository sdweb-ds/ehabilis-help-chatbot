package es.sdweb.memorycorp.ui.console;

import es.sdweb.application.componentes.util.Booleano;
import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.memorycorp.core.LearningEngine;
import es.sdweb.memorycorp.nandanicnoc.NandaDefinitions;
import es.sdweb.memorycorp.nandanicnoc.TextAnalizer;
import es.sdweb.memorycorp.nlpengine.DVerb;
import es.sdweb.memorycorp.nlpengine.DWord;
import es.sdweb.memorycorp.nlpengine.Dictionary;
import es.sdweb.memorycorp.nlpengine.LOracion;
import es.sdweb.memorycorp.nlpengine.LParrafo;
import es.sdweb.memorycorp.nlpengine.NLPEngine;
import es.sdweb.memorycorp.nlpengine.SpanishDictionary;
import es.sdweb.memorycorp.dialogue.DialogueEngine;
import java.util.ArrayList;
import java.util.Scanner;
import org.jdom2.Document;

/**
 * Consola de usuario en modo texto para operar y entrenar el motor semántico.
 * @author Antonio Carro Mariño
 */
public class UserConsole {
    
    static LearningEngine learningEngine=null;    
    static DialogueEngine dialogueEngine=null;
    static Scanner sc = new Scanner(System.in); //Se crea el lector;
    
    /**
     * Imprime en pantalla el prompt para esperar la entrada del usuario.
     */
    private static void printPrompt(){
       System.out.print(">> ");
    }
    
    /**
     * Imprime por consola (normalmente la salida estándar) la salida del sistema.
     * @param response Respuesta del sistema.
     */
    private static void printConsole(String response){
       LogUtil.logConsole(response);
    }
    
    /**
     * Obtiene la instancia de DialogueEngine, y si no existe la crea y la devuelve.
     * @return Instancia de DialogueEngine
     */
    public static DialogueEngine getDialogueEngine(){
        if (UserConsole.dialogueEngine==null){
            UserConsole.dialogueEngine=new DialogueEngine(learningEngine);
        }
        return UserConsole.dialogueEngine;
    }
    
    /**
     * Ventana de diálogo que interacciona a alto nivel, en lenguaje natural,  con el motor de razonamiento semántico.
     */
    public static void execInterprete(){

        //NLPEngine engineNLP=learningEngine.getEngine();
        DialogueEngine dialogueEngine=getDialogueEngine();
        
        printConsole("\n[MemoryCorp Engine 1.0 started and listening][MemoryCorp ChatBot]");

        boolean exit=false; //Flag de salida del programa
        
        while (!exit){

            printPrompt(); //Muestra en prompt en pantalla esperando la entrada del usuario
            String userInput = StringUtil.trim(sc.nextLine()); //Leemos una entrada del usuario
            
            //Comprobamos la condición de salida
            String p=StringUtil.trimNormalizado(userInput); //Eliminamos todo caracter extraño
            exit=(p.toLowerCase().equals("exit")||p.toLowerCase().equals("bye")||
                    p.toLowerCase().equals("salir")||p.toLowerCase().equals("adios")
                    ||p.toLowerCase().equals("quit")||p.toLowerCase().equals("end"));
            
            if(!exit){
                /* CODIGO ANTERIOR BRAINLEARNING
                // this is the parse tree of the current sentence
                LParrafo par=engineNLP.addParrafo(userInput);
                
                Dictionary dict=learningEngine.getDictionary();
                System.out.println(StringUtil.objectToString(dict.getWord(userInput)));

                
                System.out.println("Oracion 1: "+ par.getOraciones().get(0).toString());
                System.out.println("Verbo principal: "+ StringUtil.printObject(par.getOraciones().get(0).getAccion()));
                System.out.println("Sujeto principal: "+ StringUtil.printObject(par.getOraciones().get(0).getSujeto()));
                System.out.println("Objeto Directo: "+ StringUtil.printObject(par.getOraciones().get(0).getObjetoDirecto()));
                System.out.println("Objeto Indirecto: "+ StringUtil.printObject(par.getOraciones().get(0).getObjetoIndirecto()));

                printConsole("\n"); //Salto de línea
                */
                String answer=dialogueEngine.getAnswer(userInput);
                LogUtil.logConsole("MemoryCorp Chatbot v.1.0: "+answer);
                
            }//if not exit
            
        }//while
    }//execConsole
    
    
    /**
     * Recibe el texto de entrada del usuario (en lenguaje natural), lo procesa, y devuelve el texto de salida del ChatBot (en lenguaje Natural).
     * @param userInput Texto de entrada del usuario (en lenguaje natural)
     * @return Texto de salida del ChatBot (en lenguaje Natural).
     */
    public static String getAnswer(String userInput){
       String answer=UserConsole.dialogueEngine.getAnswer(userInput);
       return answer;
    }
    
    /**
     * Ventana de diálogo que interacciona con el motor semántico a bajo nivel (comandos).
     */
    public static void execConsole(){
        
        NLPEngine engineNLP=learningEngine.getEngine();
        UserConsole.getDialogueEngine(); //Inicializa el DialogueEngine
        
        boolean exit=false; //Flag de salida del programa
        
        while (!exit){

            printConsole("\n[MemoryCorp Engine 1.0 started and listening][Command Terminal]");

            printPrompt(); //Muestra en prompt en pantalla esperando la entrada del usuario
            String userInput = StringUtil.trim(sc.nextLine()); //Leemos una entrada del usuario
            
            //Comprobamos la condición de salida
            String p=StringUtil.trimNormalizado(userInput); //Eliminamos todo caracter extraño
            exit=(p.toLowerCase().equals("exit")||p.toLowerCase().equals("bye")||
                    p.toLowerCase().equals("salir")||p.toLowerCase().equals("adios")
                    ||p.toLowerCase().equals("quit")||p.toLowerCase().equals("end"));
            
            if(!exit){
                boolean varias=(p.indexOf(" ")>0);
                String comando=(varias?p.split(" ")[0]:p); //Nos quedamos con la primera palabra: el comando
                comando=comando.toLowerCase();
                userInput=userInput.substring(userInput.indexOf(" ")+1, userInput.length()); //Recortamos el comnando
                
                switch (comando){
                    case "?":
                    case "ayuda":
                    case "help": printConsole("bot / load / struc / sin / ant / eval / comp / agre / sub / sup / reset / resetall / net / chkv / props / loadrs / printrs");
                        break;
                    case "bot": execInterprete(); //Intérprete de frases en lenguaje natural
                        break;
                    case "load": //Procesa y carga una sentencia/párrafo en la red.
                        if (varias){
                            // this is the parse tree of the current sentence
                            LParrafo par=engineNLP.addParrafo(userInput);
                            printConsole("Frase procesada."); //Analiza y carga la oración/párrafo en el sistema.
                            ArrayList unkWords=learningEngine.processParrafo(par); //Carga el párrafo en la Red Semántica (genera un fragmento de red) y obtiene las palabras desconocidas (si existen)
                            printConsole("Palabras desconocidas: "+StringUtil.toString(unkWords, true));
                        }else{
                            printConsole("[Error] No se ha especificado una oración a cargar.");
                        }
                        break;
                    case "struc": //Análisis y carga de estructura sintáctica en la red, imprimiendo su árbol en Consola.
                    case "struct": 
                    case "structure": 
                        if (varias){
                            // this is the parse tree of the current sentence
                            LParrafo par=engineNLP.addParrafo(userInput);
                            LOracion or=par.getOraciones().get(0); //Cogemos la primera oración
                            printConsole(par.toStringEstructura()); //Imprimimos la estructura de la oración

                            ArrayList unkWords=learningEngine.processParrafo(par); //Palabras desconocidas
                            printConsole("Palabras desconocidas: "+(unkWords.isEmpty()?"NO HAY":StringUtil.toString(unkWords, true)));
                            printConsole("1. Verbo principal: "+(or.getAccion()!=null?or.getAccion().getTexto():""));
                            printConsole("2. Sujeto: "+(or.getSujeto()!=null?or.getSujeto().getTexto():""));
                            printConsole("3. Objeto Directo: "+(or.getObjetoDirecto()!=null?or.getObjetoDirecto().getTexto():""));
                            printConsole("4. Objeto Indirecto: "+(or.getObjetoIndirecto()!=null?or.getObjetoIndirecto().getTexto():""));
                            printConsole("5. C.C. de Lugar: "+(or.getCcLugar()!=null?or.getCcLugar().getTexto():""));
                            printConsole("6. C.C. de Modo: ");
                            printConsole("7. C.C. de Finalidad: ");
                            printConsole("8. C.C. de Causa: ");
                            printConsole("9. C.C. de Tiempo: ");
                            printConsole("10. C.C. de Coparticipación: ");
                            
                        }else{
                            printConsole("[Error] No se ha especificado una oración a analizar.");
                        }
                        break;
                    case "sin": //Obtiene los sinónimos de una palabra
                    case "sinonim":
                    case "sinonimos":
                        if (varias){
                            Dictionary dic=learningEngine.getDictionary();
                            DWord w=dic.getWord(userInput);
                            if (w!=null){
                                printConsole("Sinónimos de \""+userInput+"\": "+StringUtil.toString(w.getSinonimosStr(),true));
                            }else{
                                printConsole("La palabra \""+userInput+"\" no está en el Diccionario");
                            }
                        }else{
                            printConsole("[Error] No se ha especificado una palabra a analizar.");
                        }
                        break;
                    case "ant": //Obtiene los antónimos de una palabra
                    case "antonim":
                    case "antonimos":
                        if (varias){
                            Dictionary dic=learningEngine.getDictionary();
                            DWord w=dic.getWord(userInput);
                            if (w!=null){
                                printConsole("Antonimos de \""+userInput+"\": "+StringUtil.toString(w.getAntonimosStr(),true));
                            }else{
                                printConsole("La palabra \""+userInput+"\" no está en el Diccionario");
                            }
                        }else{
                            printConsole("[Error] No se ha especificado una palabra a analizar.");
                        }
                        break;
                    case "eval": //Evalúa una aserción, p.e. "Juan es guapo"
                    case "evaluate":
                        LParrafo par=engineNLP.addParrafo(userInput);
                        Booleano result=learningEngine.evaluateSentence(par);
                        printConsole("Resultado de la evaluación: "+(result!=null?result.isBool():"NO CONCLUYENTE"));
                        break;
                    case "comp": //Imprime en pantalla el árbol de componentes de una palabra dada
                    case "components":
                    case "componentes":
                        printConsole("Componentes de \""+userInput+"\": ");
                        String arbolComp=learningEngine.printComponentes(userInput);
                        printConsole(arbolComp);
                        break;
                    case "agre": //Imprime en pantalla el árbol de agregadores de una palabra dada
                    case "agregators":
                    case "agregadores":
                        printConsole("Agregadores de \""+userInput+"\": ");
                        String arbolAgreg=learningEngine.printAgregadores(userInput);
                        printConsole(arbolAgreg);
                        break;
                    case "sub": //Imprime en pantalla el árbol de subclases de una palabra dada
                    case "subclass":
                    case "subclasses":
                        printConsole("Subclases de \""+userInput+"\": ");
                        String arbolDesc=learningEngine.printJerarquiaDescendente(userInput);
                        printConsole(arbolDesc);
                        break;
                    case "sup": //Imprime en pantalla el árbol de subclases de una palabra dada
                    case "super":
                    case "superclass":
                    case "superclasses":
                        printConsole("Superclases de \""+userInput+"\": ");
                        String arbolAsc=learningEngine.printJerarquiaAscendente(userInput);
                        printConsole(arbolAsc);
                        break;
                    case "reset": //Borra toda la memoria que contiene la red semántica
                        learningEngine.resetAll();
                        break;
                    case "resetall": //Borra toda la memoria que contiene la Red Semántica, la Memoria de Variables, y recarga el RuleSet
                        learningEngine.resetAll(); //Borra la Red Semántica
                        DialogueEngine de=new DialogueEngine(learningEngine); //Borra la Memoria de Variables, y recarga el RuleSet
                        break;
                    case "printnet": //Imprime toda la red por pantalla: todos los nodos y enlaces de la red
                    case "net": 
                        String red=learningEngine.printAllSemanticNet();
                        printConsole(red);
                        break;
                    case "chkv": //Checkeo de una palabra del diccionario
                    case "conj": 
                    case "checkv": 
                    case "checkverb": 
                        if (varias){
                            Dictionary dic=learningEngine.getDictionary();
                            ArrayList<DVerb> verbs=dic.getVerb(userInput);
                            if (!verbs.isEmpty()){
                                DVerb verb=verbs.get(0); //Cogemos el primer elemento
                                printConsole("Conjugaciones de \""+userInput+"\": "+verb.getConjugacionesStr());
                            }else{
                                printConsole("El verbo \""+userInput+"\" no está en el Diccionario");
                            }
                        }else{
                            printConsole("[Error] No se ha especificado un verbo a analizar.");
                        }
                        break;
                    case "props": //Cualidades (adjetivos) de un objeto
                    case "cual": 
                    case "cualidades": 
                        if (varias){
                            String cualidades=learningEngine.getCualidades(userInput);
                            printConsole("Cualidades del sustantivo: "+cualidades);
                        }else{
                            printConsole("[Error] No se ha especificado un sustantivo a analizar.");
                        }
                        break;
                    case "nanda": //Imprime el listado completo de Nandas
                    case "nandas": 
                        if (varias){
                           printConsole(NandaDefinitions.getNandaString(userInput));
                        }else{
                           printConsole(NandaDefinitions.getNandasString());
                        }
                        break;
                    case "nic": //Imprime el listado completo de Nics
                    case "nics": 
                        if (varias){
                           printConsole(NandaDefinitions.getNicString(userInput));
                        }else{
                           printConsole(NandaDefinitions.getNicsString());
                        }
                        break;
                    case "noc": //Imprime el listado completo de Nocs
                    case "nocs": 
                        if (varias){
                           printConsole(NandaDefinitions.getNocString(userInput));
                        }else{
                           printConsole(NandaDefinitions.getNocsString());
                        }                        
                        break;
                    case "process": //Procesa un texto de enfermería y extrae sus Nanda, Nic y Noc.
                    case "procesa": 
                    case "procesar": 
                    case "analize": 
                        if (varias){
                           printConsole(TextAnalizer.processText(userInput, learningEngine).toString());
                        }else{
                           printConsole("[Error] No se ha especificado un texto del dominio de enfermería a analizar.");
                        }                        
                        break;
                    case "resetr": //Reseteamos las reglas (no la memoria de variables) dejando que se ejecute el comando "loadruleset" que va a continuación de este "case"
                        varias=true;
                        userInput="rules.txt";
                    case "loadruleset":
                    case "loadrule":
                    case "loadrs":
                    case "loadr": //Carga archivo que define el Conjunto de Reglas en memoria (Si se indica el archivo NO BORRA LA MEMORIA DE VARIABLES; si no se indica archivo BORRA LA MEMORIA)
                        if (varias){ //Si hemos indicado nombre de archivo
                           printConsole("Cargando archivo: "+userInput);
                           UserConsole.dialogueEngine.loadRuleSet(userInput);//Recargamos las reglas
                           printConsole((UserConsole.dialogueEngine.getRuleSet()!=null?"Archivo cargado con Éxito.":"Error al cargar el archivo."));
                        }else{  //Si no indicamos nombre de archivo, cogemos el archivo por defecto "rules.txt"
                           new DialogueEngine(learningEngine); 
                        }
                        break;
                    case "printruleset":
                    case "printrule":
                    case "printrs":
                    case "printr": //Imprime en pantalla el Conjunto de Reglas cargadas en memoria
                        DialogueEngine dien=new DialogueEngine(learningEngine); 
                        printConsole("\nArbol de Reglas: \n"+dien.getRuleSet().printRuleSet());
                        break;
                    case "ner":
                    case "nombre":
                    case "nameloc":
                    case "name": //Indica si una palabra dada es un nombre de persona/lugar o no.
                        if (varias){ //Si hemos indicado un nombre de persona o lugar a buscar
                           DWord palabra=learningEngine.getDictionary().getNameOrLocation(userInput, null, null, false); 
                           printConsole("\nEs un nombre de persona/lugar: "+!StringUtil.isEmpty(StringUtil.toString(palabra)));
                        }else{
                           printConsole("[Error] Debe especificar una palabra a analizar.");                            
                        }
                        break;
                    case "loadbot": //Procesa y carga una sentencia/párrafo mediante las reglas NLP/NLG.
                    case "loadb":    
                        if (varias){
                            String answer=UserConsole.dialogueEngine.getAnswer(userInput);
                            printConsole("MemoryCorp Chatbot v.1.0: "+answer);
                        }else{
                            printConsole("[Error] No se ha especificado una oración a cargar.");
                        }
                        break;
                    case "snomed":
                        printConsole("Taxonomía de SNOMED-CT: ");
                        String arbolSnomedCT=learningEngine.printJerarquiaDescendenteSNOMED("concepto de snomed ct");
                        printConsole(arbolSnomedCT);
                        break;


                        
                    default:
                        printConsole("[Error] Comando desconocido: "+comando);
                        break;
                }//switch

                printConsole("\n"); //Salto de línea
                
            }//if not exit
            
        }//while
    }//execConsole



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        LogUtil.setLogTrazas(true); //Activamos el log de trazas
        LogUtil.setNivelTraza(0); //Establecemos el nivel de la visibilidad de trazas
        LogUtil.logTraza("[INICIALIZACION FASE_0] Logs activados.",0);

        LogUtil.logTraza("[INICIALIZACION FASE_1] Inicializando diccionario de Español.",5);
        SpanishDictionary.initializeDictionary(); //Cargamos el diccionario de Castellano
        LogUtil.logTraza("[INICIALIZACION FASE_1] Inicializando LearningEngine.",5);
        learningEngine=new LearningEngine(SpanishDictionary.getDictionary()); //Creamos el Engine de IA
        
        LogUtil.logTraza("[INICIALIZACION FASE_3] Inicializando estándar NANDA-NIC-NOC.",5);
        NandaDefinitions.inicializaNandaNicNoc();

        LogUtil.logTraza("[INICIALIZACION FASE_4] Inicializando consola.",5);
        UserConsole.execConsole(); //Ejecutamos la consola de interacción con el usuario
        
    }//main


    
}//class
