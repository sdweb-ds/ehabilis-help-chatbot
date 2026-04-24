package es.sdweb.memorycorp.rulesengine;

import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.memorycorp.core.LearningEngine;
import es.sdweb.memorycorp.dialogue.DialogueEngine;
import es.sdweb.memorycorp.dialogue.DialogueUtil;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Juego de Reglas con las que opera el sistema para generar el lenguaje (NLG)
 * @author Antonio Carro Mariño
 */
public class RuleSet {
    
    public static int MATCH_LITERAL=1;  //Peso si encaja un literal, p.e. "el hombre" o "[edema]". Encaso de que el literal esté entre "[]" cuenta doble.
    public static int MATCH_SAME_ROOT=2;  //Peso a sumar si la regla activada pertenece a la rama activada en la última interacción

    private ArrayList<Rule> allRules=new ArrayList<>(); //Listado de todas las reglas de este Conjunto de Reglas
    private ArrayList<Rule> rules=new ArrayList<>(); //Listado de reglas de Primer Nivel de este Conjunto de Reglas
    private HashMap rulesMap=new HashMap(); //Para localizarlas a partir de su ID
    //HashMap(String nombreVariable, String valor) de variables, para mover datos entre las preguntas y las respuestas
    private HashMap variable=new HashMap(); 
    
    /**
     * Resetea (elimina) las reglas del RuleSet, y su memoria de variables.
     * Ello permite recargar las reglas y partir de una memoria de variables en blanco.
     */
    public void resetRuleSet(){
        allRules=new ArrayList<>();
        rules=new ArrayList<>();
        rulesMap=new HashMap();
        variable=new HashMap(); 
    }
    
    /**
     * Elimina las variables del map de la regla especificada.
     * @param rule Regla especificada
     */
    public void resetVariables(Rule rule){
        for (String var:rule.getVariables()){ //Eliminamos las variables del map
            getVariable().remove(var);
        }//for
    }
    
   
    /**
     * Obtiene el valor de una variable, de la tabla hash.
     * El variableName se pasa a minúsculas antes de consultar la tabla de variables.
     * @param variableName Nombre de la variable.
     * @return String con el valor de la variable.
     */
    public String getVariable(String variableName){
        String result=(String)this.getVariable().get(variableName.toLowerCase());
        if (result==null) result="";
        return result;
    }
    
    /**
     * Establece una variable y su valor asociado; y los guarda en la tabla hash.
     * Tanto el nombre de la variable, como su valor, se pasan a minúsculas antes de almacenarse.
     * @param variableName Nombre de la variable.
     * @param variableValue Valor de la variable.
     */
    public void setVariable(String variableName, String variableValue){
        this.getVariable().put(variableName.toLowerCase(), variableValue);
    }
    
    
    
    /**
     * Obtiene una regla del Conjunto de Reglas a partir de su ID.
     * @param id ID de la regla buscada.
     * @return Rule buscada a partir de su ID.
     */
    public Rule getRule(String id){
        Rule result=(Rule)rulesMap.get(id);
        return result;
    }
    
    /**
     * Añade la regla al HashMap general donde están todas las reglas, independientemente del nivel de anidamiento que tengan.
     * @param id ID de la regla a guardar. El ID debe ser único.
     * @param rule Regla a guardar.
     */
    private void putRule(String id, Rule rule){
        this.rulesMap.put(id, rule);
    }
    
    /**
     * Añade la regla en el nivel que le corresponda, y la añade también al al HashMap general donde están todas las reglas, independientemente del nivel de anidamiento que tengan.
     * @param id ID de la regla a guardar. El ID debe ser único.
     * @param rule Regla raíz a guardar.
     */
    public void addRule(String id, Rule rule){
        this.allRules.add(rule); //Añadimos la regla al listado completo de reglas
        this.rulesMap.put(id, rule); //La añadimos al HashMap en cualquier caso
        if (StringUtil.isEmpty(rule.getIdPadre())){ //Si no tiene padre
          this.rules.add(rule); //Añade la Regla a la lista de reglas raíz.
        }else{ //Si tiene padre
          for (Rule r:this.rules){ //Añadimos la regla como regla-hija de la regla-raiz que corresponsa
             addToRulesHijas(rule, r);
          }
        }
    }
    
    /**
     * Añade la regla como regla-hija del nivel que corresponda.
     * @param ruleToAdd Regla a añadir.
     * @param rulePadre Regla que se está explorando como posible regla-padre de la regla a añadir.
     */
    private void addToRulesHijas(Rule ruleToAdd, Rule rulePadre){
        if (ruleToAdd.getIdPadre().equals(rulePadre.getId())){ //Si rulePadre es el padre de ruleToAdd
            rulePadre.addReglaHija(ruleToAdd); //La añadimos como hija
        }else{
            for (Rule r:rulePadre.getReglasHijas()){ //En otro caso seguimos profundizando
                addToRulesHijas(ruleToAdd,r); //Llamada recursiva
            }
        }
    }

    /**
     * Genera un String con todo el Conjunto de reglas (RuleSet)
     * @return String con el árbol de reglas.
     */
    public String printRuleSet(){
        String result=printRuleSetLevels(this.rules,0);
        return result;
    }
    
    @Override
    public String toString(){
        return printRuleSet();
    }
    
    /**
     * Genera un String con todo el Conjunto de reglas (RuleSet)
     * @param list Lista de niveles a pintar.
     * @param level Nivel en el que estamos.
     * @return String con el árbol de reglas.
     */
    private String printRuleSetLevels(ArrayList<Rule> list, int level){
        String result="";
        for (Rule r:list){
            result=result+StringUtil.padding(level)+"Rule "+r.getId()+"(padre "+r.getIdPadre()+"): "+r.getInput()+" --> "+r.getOutput()+"\n"+
                   printRuleSetLevels(r.getReglasHijas(), (level+3));
        } //For
        return result;
    }
    
    
    /**
     * Obtiene el ID de la Regla raíz de la rama a la que pertenece el idRule indicado.
     * Si el idRule ya pertenece a una regla raiz, devuelve el mismo idRule.
     * @param idRule Identificador de la regla de la que se desea obtener la regla raiz.
     * @return Identificador de la regla raiz de la rama a la que pertenece idRule.
     */
    public String getIdRootRule(String idRule){
       String result="";
       if (!StringUtil.isEmpty(idRule)){
           Rule r=this.getRule(idRule); //Recuperamos la regla
           if (r==null){
               LogUtil.logError("ERROR en RuleSet.getIdRuleRaiz(String idRule): el idRule ["+idRule+"] no existe.");
           }else{
               String idRulePadre=r.getIdPadre();
               result=(StringUtil.isEmpty(idRulePadre)?idRule:getIdRootRule(idRulePadre));
           }
       }
       return result;
    }    
    
    /**
     * Obtiene la Regla raiz de la rama a la que pertenece la regla con el idRule indicado.
     * Si la regla ya es raíz, se devuelve ella misma.
     * @param idRule Identificador de la regla de la que se desea obtener la regla raíz.
     * @return Regla raíz de la rama.
     */
    public Rule getRootRule(String idRule){
        Rule result=this.getRule(getIdRootRule(idRule)); 
        return result;
    }
    
    
    /**
     * Devuelve un ArrayList con las reglas raiz.
     * @return ArrayList con las reglas raiz.
     */
    public ArrayList<Rule> getRootRules(){
        return this.rules;
    }
    
    /**
     * Devuelve un ArrayList con todas las reglas.
     * @return ArrayList con todas las reglas
     */
    public ArrayList<Rule> getAllRules(){
        return this.allRules;
    }
    
    /**
     * Devuelve un ArrayList con todas las reglas po defecto (aquellas cuyo <input> es "[default]").
     * @return ArrayList con todas las reglas
     */
    public ArrayList<Rule> getDefaultRules(){
        ArrayList<Rule> result=new ArrayList();
        for (Rule rule:getAllRules()){
            if (rule.getInput().equals("[default]")){
                result.add(rule);
            }//Si es una regla por defecto
        } //Para cada regla
        return result;
    }
     
    /**
     * Obtiene la respuesta de la regla, sustituyendo sus variables por los valores asociados a las mismas.
     * @param rule Regla a procesar.
     * @param de DialogueEngine que estamos utilizando.
     * @return Cadena de texto completa de salida.
     */
    public String getCompleteOutput(Rule rule, DialogueEngine de){
        String salida=(rule.isErrorFlag()?rule.getOutputError():rule.getOutput()); //Si hubo un error de tipos, cogemos como salida outputerror en lugar de output
        
        ArrayList<String> partesOutput=StringUtil.tokeniza(salida, "|"); //Vemos si el output de la regla tiene varias partes. Si no hay separador dentro de la cadena, devuelve la propia cadena en un solo chunk.
        ArrayList<String> outputPattersThatFits=new ArrayList(); //En esta lista guardaremos los patrones de salida que encajen
        
        for (String outPat:partesOutput){ //Para cada patrón de salida, comprobamos si es coherente con el patrón de entrada activado
            //Verificamos que el patrón de salida tiene todas las variables pertinentes
            if (DialogueUtil.fitsOutput(de, rule.getId(), rule.getInputPatternActivated(), outPat, rule.isCheckVariables())){
                outputPattersThatFits.add(outPat); //Lo añadimos a las salidas posibles
            }//Si es coherente
        }//for
        
        if (!outputPattersThatFits.isEmpty()){ //Si hay varias alternativas separadas por "|", escogeremos una aleatoriamente
            int totalRespuestas=outputPattersThatFits.size();
            int pos=(int)Math.floor(Math.random()*totalRespuestas); //Escogemos/activamos una respuesta al azar (de entre las que encajan) para ser generada
            salida=outputPattersThatFits.get(pos);
        }else{ //Si hay varias partes
            LogUtil.logError("ERROR: La regla "+rule.getId()+" no tiene una salida que encaje con el patrón de entrada: "+rule.getInputPatternActivated());
        } 
        
        boolean aunQuedanVariables=true;
        while (aunQuedanVariables){ //Mientras queden variables dentro del rule output, las sustituímos
           int posAbre=salida.indexOf("["); //Comprobaremos si hay un comando y los corchetes están en posición correcta
           int posCierra=salida.indexOf("]");
           if (((posAbre>-1)||(posCierra>-1))&&(posAbre>=posCierra)){ //Si falta un corchete o están al revés, mostramos error
               LogUtil.logError("ERROR: error en regla ["+rule.getId()+"], en el OUTPUT falta un corchete o están en posición incorrecta. ");
               return salida; //Salimos de la funcion directamente con lo que haya
           }
           if (!hayComandosPresentes(salida)){ //Si no hay más variables/comandos presentes, salimos
               return salida;
           }
           String nombreVariable=salida.substring(posAbre, posCierra+1); //Cortamos el fragmente de comando (que es el nombre de la variable).
           String valor=this.getVariable(nombreVariable); //Obtenemos el valor de la variable           
           if (de.getLearningEngine().getDictionary().isNameOrLocation(valor))
               valor=StringUtil.primeraMay(valor, false); //Si es un nombre, ponemos la primera en mayuscula
           salida=StringUtil.replaceAll(salida,nombreVariable, valor); //Reemplazamos esa varible por su valor en todas las ocurrencias
        }//while aún quedan variables
        
        return salida;
    }
    
    /**
     * Indica si hay algún comando presente en la cadena de texto. Esto se verifica buscando un "[" y un "]" posterior al primero.
     * @param inputChunk Cadena de texto a analizar.
     * @return True si hay algún comando presente, por ejemplo "[*]". False en caso contrario.
     */
    public static boolean hayComandosPresentes(String inputChunk){
        int posAbre=inputChunk.indexOf("["); //Comprobaremos si hay un comando y los corchetes están en posición correcta
        int posCierra=inputChunk.indexOf("]");
        boolean result=(posAbre>-1)&&(posCierra>-1)&&(posAbre<posCierra);
        return result;
    }
    
    
    /**
     * Obtiene la lista de fragmentos presentes en el <input> de la regla, texto y comandos (que van siempre entre corchetes [comando]).
     * Por ejemplo, si el <input> es "Hola [*]", devolverá un ArrayList con dos elementos: "Hola", y "[*]".
     * Ningún fragmento del array de respuesta estará vacío.
     * @param ruleID ID de la regla a la que pertenece el Patron de entrada (input) de la Regla a analizar
     * @param ruleInput Patron de entrada (input) de la Regla a analizar
     * @return Lista de comandos encontrados, con su texto interior, sin los corchetes. 
     */
    public ArrayList<String> getChunks(String ruleID, String ruleInput){
        ArrayList<String> result=new ArrayList(); //En principio no hay comandos presentes
        String entrada=ruleInput.toLowerCase(); //Pasaremos el texto del <input> de la regla a minúsculas
        boolean aunQuedanComandos=true;
        while (aunQuedanComandos){ //Mientras queden comandos dentro del userInput, los extraemos
           int posAbre=entrada.indexOf("["); //Comprobaremos si hay un comando y los corchetes están en posición correcta
           int posCierra=entrada.indexOf("]");
           if (((posAbre>-1)||(posCierra>-1))&&(posAbre>=posCierra)){ //Si falta un corchete o están al revés, mostramos error
               LogUtil.logError("ERROR: error en regla ["+ruleID+"], en el INPUT falta un corchete o están en posición incorrecta. ");
               return result; //Salimos de la funcion directamente con lo que haya
           }
           aunQuedanComandos=hayComandosPresentes(entrada);
           if (aunQuedanComandos){
                String comando=entrada.substring(posAbre, posCierra+1); //Cortamos el fragmente de comando.
                comando=comando.replaceAll(" ", ""); //Eliminamos todos los espacios en blanco del interior del comando
                String chunkIzdo=entrada.substring(0, posAbre).trim(); 
                String chunkDcho=entrada.substring(posCierra+1).trim();
                if (!StringUtil.isEmpty(chunkIzdo))
                    result.add(chunkIzdo); //Añadimos el fragmento izquierdo si tiene algo
                //LogUtil.logTraza("Chunk izdo: "+chunkIzdo+"  LEN: "+chunkIzdo.length(),1);
                result.add(comando); //Añadimos el comando
                //LogUtil.logTraza("Chunk comando: "+comando+"  LEN: "+comando.length(),1);
                entrada=chunkDcho; //Procesamos lo que queda a la derecha
                //LogUtil.logTraza("Chunk dcho: "+chunkDcho+"  LEN: "+chunkDcho.length(),1);
           }else{
               if (!StringUtil.isEmpty(entrada)){
                  result.add(entrada); //Como ya no quedan comandos añadimos el resto de la cadena (si queda algo)
                  //LogUtil.logTraza("Chunk resto: "+entrada+"  LEN: "+entrada.length(),1);
               }
           }
            
        }
        return result;
    }
    
    /**
     * Verifica si el tipo de variable esperada (Nombre de Persona/Lugar, etc) encaja en el texto de entrada encontrado.
     * @param comando Literal del comando (p.e. "[?NM:R02-01]").
     * @param varValue Texto de entrada encontrado.
     * @param lengine Objeto LearningEngine que aporta funcionalidades NLP.
     * @return True si el tipo de variable esperado encaja en el texto. False en caso contrario.
     */
    private boolean matchVariable(String comando, String varValue, Rule rule, LearningEngine lengine){
        boolean result=false;

           int posAbre=comando.indexOf("?"); //Analizamos el tipo de variable
           int posCierra=comando.indexOf(":");
           if (((posAbre>-1)||(posCierra>-1))&&(posAbre>=posCierra)){ //Si falta alguno de estos caracteres o están al revés, mostramos error
               LogUtil.logError("ERROR: error en regla ["+rule.getId()+"], falta un \"?\" o un \":\" o están en posición incorrecta. ");
               return result; //Salimos de la funcion directamente con lo que haya
           }

           String tipo=comando.substring(posAbre+1, posCierra).toUpperCase(); //Cortamos el fragmente de comando, que indica el tipo.
           if (tipo.length()!=2){
               LogUtil.logError("ERROR: error en regla ["+rule.getId()+"], el tipo especificado en una variable no tiene 2 letras. ");
           }
           switch (tipo){
               case "NM": //Si el tipo de variable esperada es un nombre/lugar, lo comprobamos
                   result=lengine.getDictionary().isNameOrLocation(varValue);
                   break;
                   
               default:
                   result=true; //Por defecto el texto encaja.
           }
        
        return result;
    }
    
    
    /**
     * Indica si el userInput encaja con el patrón complejo definido en el <input> de la Regla.
     * Para ello irá cotejando los diferentes fragmentos del <input> de la Rule, contra la cadena userInput.
     * Cada vez que se ejecuta el método actualiza la lista de variables de la regla.
     * Esta función considera los sinónimos tipo "[un/una]", en el procesamiento.
     * Esta función define las variables y las carga, sólo si la regla encaja. 
     * OJO: Son ilegales y carecen de sentido y utilidad las siguientes ocurrencias: dos comodines seguidos [*][*], dos variables seguidas [?R001-01][?R001-02]
     * En esta tarea de cotejo se consideran los siguientes tipos de chuncks del <input> de la Rule:
     *     1. Chuncks que empiezan por "[" y terminan por "]" son comandos, por ejemplo "[*]". Tipos de comandos:
     *         a)[*]: es un comodín que representa a cualquier extensión de texto.
     *     2. Cualquier otro chunk es un texto literal, por ejemplo "hola".
     * @param userInput Cadena de entrada del usuario.
     * @param rule Regla a cotejar.
     * @param lengine Objeto LearningEngine que aporta funcionalidades NLP.
     * @return True si el userInput encaja en la regla.
     */
    private boolean matchChunks(String userInput, Rule rule, LearningEngine lengine){
        boolean result=true;
        HashMap varTempValues=new HashMap(); //Lugar donde almacenaremos temporalmente el valor de las variables
        ArrayList<String> varTempKeys=new ArrayList(); //Lugar donde almacenaremos temporalmente el nombre de las variables detectadas
        
        ArrayList<String> subPartes=getChunks(rule.getId(), rule.getInput()); //Primero troceamos el patrón de la Regla y lo pasamos a minúsculas
        String entrada=StringUtil.eliminaPuncFinalSpaceLower(userInput); //Preparamos el userInput para el procesamiento
        
        boolean comodinEstrella=false; //Flag que indica si hemos encontrado un comodín estrella
        boolean comodinVariable=false; //Flag que indica si hemos encontrado un comodín de variable
        boolean comodinSinonimos=false; //Flag que indica si hemos encontrado un comodín de sinónimos
        String lastChunk="";
        boolean salir=false;
        int i=0;
        while (!salir){
            String chunk=subPartes.get(i); //El algoritmo va recorriendo los chuncks del INPUT de la regla y tratando de encajarlos en el userInput
            if (hayComandosPresentes(chunk)){ //Si estamos procesando un comando
               //Ejecutamos el comando 
               if (chunk.equals("[*]")){
                   comodinEstrella=true;
                   i++; //Nos movemos al siguiente chunk
                   chunk=(i<subPartes.size()?subPartes.get(i):chunk);
                   salir=(i>=subPartes.size());  //Vemos si hay que salir
               }
               if(chunk.indexOf("[?")==0){ //Comodín de variable, se comporta como un comodín estrella, solo que además almacena la cadena recortada en una variable, de nombre "[?R001-01]" por ejemplo.
                   comodinEstrella=true;
                   comodinVariable=true;
                   lastChunk=chunk; //Nos quedamos con el nombre de la variable
                   rule.addVariable(lastChunk); //Y la guardamos en la lista de variables de la regla
                   i++; //Nos movemos al siguiente chunk
                   chunk=(i<subPartes.size()?subPartes.get(i):chunk);
                   salir=(i>=subPartes.size());  //Vemos si hay que salir
               }
               if (!chunk.equals("[*]") && chunk.indexOf("[?")!=0 && chunk.indexOf("[")==0){ //Asuminos que después de un comodín no puede haber otro, y que después de una variable no puede haber otra. Ambos casos carecen de sentido alguno.
                   lastChunk=chunk; //Nos quedamos con el corchete con los sinonimos (p.e. "[un/una]")
                   comodinSinonimos=true;
               }
            }//Si hay un comando
            
            //Si después de un comodín viene otro comodín, es un ERROR
            if ((chunk.equals("[*]") || chunk.indexOf("[?")>=0) && !salir){
                LogUtil.logError("ERROR en la regla +"+rule.getId()+": encontrada en el <input> una disposición ilegal del tipo \"[*][*]\", \"[*][?var]\", \"[?var][*]\" o \"[?var1][?var2]\"");
            }
                   
            chunk=chunk+" "; //Añadimos un espacio al chunk para que lo trate como palabra separada (la cadena de entrada siempre termina en espacio).

            //Si estamos ante un comodín de sinónimos, probamos a encajar cada uno de ellos
            if (comodinSinonimos){
               comodinSinonimos=false; //gastamos el comodin
               lastChunk=lastChunk.substring(1, lastChunk.length()-1); //Eliminamos los corchetes laterales (p.e. "[un/una]" -> "un/una"
               String[] sinonimos=lastChunk.split("/"); //Si no hay barra, devuelve las palabras que haya entre los corchetes (elimina todos los espacios interiores)
               for (String sin:sinonimos){ //probamos con cada sinónimo
                   sin=(sin.equals("·")?"":sin+" "); //El punto central representa "nada", es decir, que el sinónimo puede faltar, como si fuera opcional
                   sin=sin.replaceAll("·", " "); 
                    //Añadimos un espacio al chunk para que lo trate como palabra separada (la cadena de entrada siempre termina en espacio).
                   int pos=entrada.indexOf(sin);
                   if (pos>=0){ //Comprobamos si el literal del chunk está presente (puede haber un comodín o variable previa)
                       chunk=sin; //El chunk pasa a ser el sinónimo
                       if (!StringUtil.isEmpty(sin)) //Solo suma prioridad si el sinónimo no es "·", es decir, nada
                          rule.setPriority(rule.getPriority()+RuleSet.MATCH_LITERAL); //Como es una palabra coincidente, incrementamos su prioridad
                       break; //Salimos del for
                   } //If sinonimo en primera posicion
               }//for cada sinónimo
            }//Si se detectó un corchete de sinónimos               


           int pos=entrada.indexOf(chunk);
           if (pos==0){ //Comprobamos si el literal del chunk está presente en primera posición
               entrada=entrada.substring(chunk.length()).trim()+" "; //Eliminamos ese fragmento de la entrada y pasamos al siguiente                   
               rule.setPriority(rule.getPriority()+RuleSet.MATCH_LITERAL); //Como es un fragmento coincidente, incrementamos su prioridad
               if (comodinVariable){ //Si había que guardar una variable
                  this.setVariable(lastChunk, ""); //Como no hay nada a la izquierda, el valor de la variable es cadena vacía
                  comodinVariable=false;
               }
               result=StringUtil.isEmpty(entrada); //Si ya no queda más entrada que procesar, el patrón encaja
           }else{ //Si es un literal que está más allá de la posición 0 => (no encaja o es el valor de una variable o un comodín)
               if ((pos>0 && comodinEstrella)||salir){ //si está mas adelante pero teníamos un comodín anterior, ignoramos el trozo a la izquierda de la coincidencia
                  if (comodinVariable){ //Si había que guardar una variable
                     String varValue= (salir?entrada:entrada.substring(0,pos).trim()); //Cortamos el texto que será el valor de la variable. 
                     if (!matchVariable(lastChunk,varValue,rule,lengine)){ //Si el texto no es del tipo esperado
                         rule.setErrorFlag(true); //La salida será un mensaje de error o aviso al usuario
                     }
                     varTempValues.put(lastChunk, varValue); //Lo que queda a la izquierda es el valor de la variable. Guardamos la variable
                     comodinVariable=false;
                     if (salir){
                         rule.setInputPatternActivatedVariables(varTempValues); //Guardamos las variables de este patrón, ya que se activó
                         return true;
                     } //Si hay que salir, no seguimos procesando nada más de la entrada.
                  }
                  
                  entrada=entrada.substring(pos+chunk.length()).trim(); //recortamos lo que haya antes, incluyendo el chunck que estamos analizando
                  comodinEstrella=false; //Gastamos el comodín
                  result=StringUtil.isEmpty(entrada); //Si ya no queda más entrada que procesar, el patrón encaja
               }else{ //En cuanto algo no encaja y no hay comodín, salimos
                  return false; //Si el chunk (que no es un comando) no está presente, no encaja
               }
           }//si está presente           
            
            i++; //Nos movemos al siguiente chunk
            salir=(i>=subPartes.size());  //Vemos si hay que salir
        }//While cada chunk de la regla
       
        rule.setInputPatternActivatedVariables(varTempValues); //Guardamos las variables de este patrón, ya que se activó
        return result;
    }


    
    /**
     * Función que analiza en detalle si el input encaja en la regla, con la complejidad que se desee.
     * Para cada regla, el sistema identifica el patrón de entrada con más especificidad/prioridad, y lo guarda junto a su prioridad calculada.
     * Estos valores guardados se almacenan en los campos lastInputPatternActivated y priority, del objeto Rule.
     * @param userInput Cadena de texto tecleada por el usuario.
     * @param regla Regla en la que se busca encaje.
     * @param lengine Objeto LearningEngine que aporta funcionalidades NLP.
     * @return True si el userInput encaja en la regla. False en caso contrario.
     */
    private boolean evaluateMatch(String userInput, Rule rule, LearningEngine lengine){
        boolean result=false;
        if (rule.getInput().equals("[default]")) return false; //Las reglas por defecto no se valora su encaje
        
        ArrayList<String> partesInput=StringUtil.tokeniza(rule.getInput(), "|"); //Vemos si el input de la regla tiene varias partes. Si no hay separador dentro de la cadena, devuelve la propia cadena en un solo chunk.
        if (!partesInput.isEmpty()){ //Si hay varias alternativas separadas por "|", analizamos el encaje con cada una de ellas
            
            rule.setPriority(0); //Inicializamos la prioridad de esta regla a cero
            int maxPriority=0; //variante de patrón de más prioridad
            for (String chunk:partesInput){ //Para cada chunk/fragmento (un fragmento es similar (no igual) a una regla nueva)
                
              Rule ruleClon=rule.clone(); //Clonamos la regla y establecemos el chunk como input
              ruleClon.setInput(chunk);

              boolean fits=matchChunks(userInput, ruleClon, lengine); //Vemos si encaja de forma compleja
              
              //Si el patrón de entrada encajó, y tiene más prioridad (es decir, especificidad), guardamos este patrón como último activado de esta regla, y la prioridad calculada
              if (fits){
                 result=true; //Si encajó una variante => encajó la regla
                 if (ruleClon.getPriority()>=maxPriority){ //Solo guardamos la variante, si supera la prioridad de variantes previas
                    maxPriority=ruleClon.getPriority(); //Es la nueva prioridad máxima
                    rule.setInputPatternActivated(chunk); //Patron activado con más prioridad
                    rule.setPriority(ruleClon.getPriority()); //Nueva prioridad calculada
                    rule.setInputPatternActivatedVariables(ruleClon.getInputPatternActivatedVariables()); //Variables asociadas a este patrón activado
                    rule.setErrorFlag(ruleClon.isErrorFlag()); //Si se detectó un error en la regla Clon, lo pasamos a la original
                 }//Si la variante tiene más prioridad
              } //si encajó el patrón
              
           }// For cada patrón de entrada del input
        } //Si hay varias partes
        return result;
    }
    
    
    /**
     * Indica si un texto de entrada (metido por el usuario) encaja en una regla.
     * Esta funcionalidad puede tener toda la complejidad que se quiera.
     * @param userInput Texto de entrada introducido por el usuario.
     * @param regla Regla sobre la que se quiere comprobar.
     * @param lengine LearningEngine objeto LearningEngine que aporta funcionalidades NLP.
     * @return True si el texto de entrada encaja. False en caso contrario.
     */
    public boolean match(String userInput, Rule regla, LearningEngine lengine){
        boolean result=evaluateMatch(userInput, regla, lengine);
        return result;
    }

    /**
     * @return the variable
     */
    public HashMap getVariable() {
        return variable;
    }
    
    
    
}//class
