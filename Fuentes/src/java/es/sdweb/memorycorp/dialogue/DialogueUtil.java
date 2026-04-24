package es.sdweb.memorycorp.dialogue;

import es.sdweb.memorycorp.rulesengine.Rule;
import es.sdweb.memorycorp.rulesengine.RuleComparator;
import es.sdweb.memorycorp.rulesengine.RuleSet;
import java.util.ArrayList;

/**
 * A
 * @author Antonio Carro mariño
 */
public class DialogueUtil {
    
    
    /**
     * Añade una variable a la lista de variables, sólo si ésta no está ya en la lista.
     * @param variable Nombre de variable a añadir.
     * @param variables Lista de variables.
     */
    public static void addVariable(String variable, ArrayList<String> variables){
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
     * Comprueba si existe la variable en la lista de variables especificada.
     * @param variable Variable de la que se desea comprobar su existencia.
     * @param variables Lista de variables en la que se buscará la varible especificada.
     * @return True si la variable está presente en la lista, false en caso contrario.
     */
    public static boolean existsVariable(String variable, ArrayList<String> variables){
        boolean existe=false;
        for (String var:variables){ //Miramos si existe la variable
            if (var.equals(variable)){
                existe=true;
                break;
            }
        }//for
        return existe;
    }    
    
    
    /**
     * Obtiene el listado de variables del patrón de i/o recibido como parámetro.
     * @param de Objeto DialogueEngine que estamos utilizando.
     * @param ioPattern String con el patrón de entrada/salida (NLP/NLG).
     * @param idRule String con el ID de la regla a la que pertenece el patrón de entrada/salida (NLP/NLG).
     * @return Lista de variables detectada en el patrón.
     */
    public static ArrayList<String> getVariables(DialogueEngine de, String idRule, String ioPattern){
        ArrayList<String> result=new ArrayList();
        ArrayList<String> chunks=de.getRuleSet().getChunks(idRule, ioPattern);
        
        for (String chunk:chunks){ //El algoritmo va recorriendo los chuncks del INPUT/OUTPUT de la regla buscando variables
            if (RuleSet.hayComandosPresentes(chunk)){ //Si estamos procesando un comando
              if(chunk.indexOf("[?")==0){ //Si es una variable de nombre "[?R001-01]" por ejemplo.
                   addVariable(chunk,result); //Nos quedamos con el nombre de la variable, si no está ya en la lista
              }//Si es una variable
            }//Si hay comandos presentes
        }//for cada chunk del input/output
        
        return result;
    }
    
    /**
     * Indica si el patrón de salida NLG encaja a nivel de variables con el patrón de entrada NLP.Un patrón de salida no encaja
     * @param de Objeto DialogueEngine que se está utilizando.
     * @param idRuleSelected Identificador de la regla seleccionada para ejecutarse.
     * @param inputPatternSelected Patrón de entrada seleccionado para ejecutarse.
     * @param outputPatternSelected Patrón de salida seleccionado para ejecutarse.
     * @param checkVariables True si queremos checkear que todas las variables de salida están presentes en la entrada.
     * @return True si están presentes, false en caso contrario
     */
    public static boolean fitsOutput(DialogueEngine de, String idRuleSelected, String inputPatternSelected, String outputPatternSelected, boolean checkVariables){
        boolean result=true;
        if (checkVariables){ //Si tenemos que verificar la coherencia entre input y output seleccionados
           ArrayList<String> varOutputList=getVariables(de, idRuleSelected, outputPatternSelected); //Vemos las variables presentes en la salida
           ArrayList<String> varInputputList=getVariables(de, idRuleSelected, inputPatternSelected); //Vemos las variables presentes en la entrada
           
           //Para cada variable del output vemos si está presente en la entrada
           for (String var:varOutputList){
               if (!existsVariable(var, varInputputList)){
                   result=false; //Si no está presente, devolvemos false y salimos.
                   break; //salimos del bucle
               }
           }//for
        } //Si hay que checkear la coherencia de las variables de salida
        return result;
    }
    
    
    /**
     * Coge la regla más prioritaria de todas la lista de reglas recibida. Si la más prioritaria comparte prioridad con otras, escoge una al azar de ese grupo de "más prioritarias".
     * Esto es así porque una regla más prioritaria significa que tiene más coincidencias de literales con el input escrito por el usuario.
     * @param activableRules Lista de reglas activables.
     * @return Regla más prioritaria. Si activableRules es vacío, se devuelve null.
     */
    public static Rule getPrioritaryRule(ArrayList<Rule> activableRules){
        Rule result=null;
        if (!activableRules.isEmpty()){
            activableRules.sort(new RuleComparator()); //Ordenamos las regla por orden ascendente

            ArrayList<Rule> grupoPrioritario=new ArrayList(); //Inicializamos el grupo prioritario
            Rule reglaMaximaPrioridad=activableRules.get(activableRules.size()-1); //La ultima regla es la más prioritaria
            for (Rule rul:activableRules){ //Para todas las reglas con la máxima prioridad
               if (rul.getPriority()==reglaMaximaPrioridad.getPriority()){
                  grupoPrioritario.add(rul); //Las añadimos al grupo prioritario
               }
            }//for
            
            int totalReglas=grupoPrioritario.size();
            int pos=(int)Math.floor(Math.random()*totalReglas); //Escogemos/activamos una regla al azar, de las más prioritarias, para ser ejecutada
            result=grupoPrioritario.get(pos);
            
        } //Si reglas activables no es vacío
        
        return result;
    }
    
}//class
