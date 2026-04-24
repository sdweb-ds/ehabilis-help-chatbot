package es.sdweb.memorycorp.core;

import es.sdweb.application.componentes.util.Booleano;
import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.memorycorp.nlpengine.DVerb;
import es.sdweb.memorycorp.nlpengine.DWord;
import es.sdweb.memorycorp.nlpengine.Dictionary;
import es.sdweb.memorycorp.nlpengine.LOracion;
import es.sdweb.memorycorp.nlpengine.LParrafo;
import es.sdweb.memorycorp.nlpengine.LTermino;
import es.sdweb.memorycorp.nlpengine.NLPEngine;
import es.sdweb.memorycorp.nlpengine.NLPUtil;
import es.sdweb.memorycorp.rulesengine.Attribute;
import java.util.ArrayList;

/**
 * Esta clase constituye el núcleo del motor de aprendizaje.
 * @author Antonio Carro Mariño
 */
public class LearningEngine {
    
    private NLPEngine engine=new NLPEngine();
    private Dictionary dictionary=null; //El diccionario del Español se compone aproximadamente de 1 millón de palabras.
    
    //********** PLANOS DE ABSTRACCIÓN **************
    private DeepPlane deepPlane=new DeepPlane(); //Plano de abstracción profundo
    
    /**
     * Es el constructor que debe usarse para crear el motor de IA. Recibe el diccionario ya inicializado con lo básico, y completa su inicialización.
     * @param dictionary Diccionario cargado e inicializado en parte.
     */
    public LearningEngine(Dictionary dictionary){
        
        this.dictionary=dictionary; //Guardamos el diccionario

        LogUtil.logTraza("[INICIALIZACION FASE_2] Analizando POS de cada palabra.",5);
        //engine.initializePOS (this.dictionary);
        
    }//LearningEngine

    /**
     * @return devuelve el NLPEngine
     */
    public NLPEngine getEngine() {
        return engine;
    }
    
    /**
     * Procesa el párrafo de entrada para rastrear e insertar en la Red Semática relaciones de asignación de propiedades, herencia y agregación.
     * Estas relaciones son estructuras primitivas fundamentales subyacentes a la operativa general de la Red.
     * Como segundo paso se analiza si semánticamente, se está planteando una relación de Herencia (H) entre objetos, o de asignación de propiedades.
     * Como tercer paso se analiza si semánticamente, se está planteando una relación de agregación (A) entre objetos.
     * @param or Oración a analizar.
     * @return True si la oración ha sido procesada como de herencia o agregación.
     */
    private boolean processPrimitiveStructuresHA(LOracion or){

        //** 1. Analizamos si la oración es copulativa: asigna cualidades a un sujeto o establece una relación de herencia.
        boolean result=PrimitiveStructAnalizer.analizeOrCopulativa(getDeepPlane(), dictionary, or);
        //** 2. En caso de que la oración (que no es de tipos anteriores) exprese agregación, la procesamos
        if (!result) result=PrimitiveStructAnalizer.analizeOrAgregacion(getDeepPlane(), dictionary, or);

        return result;
    }
    
        /**
     * Genera un fragmento de red correspondiente al Atributo que recibe como parámetro.
     * El atributo contiene el nombre de la propiedad (p.e. "color") y su valor (p.e. "azul"), y el objeto al que pertenece (p.e. "pájaro").
     * El fragmento de red que se creará tiene dos formas:
     *    1. Si el nombre de la propiedad es vacío, el valor se enlaza directamente al objeto.
     *       p.e.: pájaro -es-> azul
     *    2. Si tiene nombre la propiedad, el nombre se enlaza al objeto, y el valor se enlaza al nombre. 
     *       p.e.: pájaro -tiene-> color -es-> azul
     * @param or Oración a analizar.
     * @param atr El atributo contiene el nombre de la propiedad (p.e. "color") y su valor (p.e. "azul"), y el objeto al que pertenece (p.e. "pájaro")
     */
    public void generateAttributeNetChunk(LOracion or, Attribute atr){
        PrimitiveStructAnalizer.generateAttributeNetChunk(getDeepPlane(), dictionary, or, atr);
    }

    
    
    /**
     * Procesa una oración "normal", es decir, que no es copulativa ni expresa agregación.
     * P.e. Marco nació en Roma.
     * @param or Oración a analizar.
     * @return True si la frase fue procesada, false en caso contraria.
     */
    private boolean processRegularSentence(LOracion or){
        boolean result=false;
        
        //**** Leemos la información de entrada *******
        //** 1. Verbo
        DVerb verboD=NLPUtil.getVerbUnambiguous(or); //Obtenemos el verbo desambiguado
        Link verbN=new Link(verboD,or.getAccion().isNegado()); //Creamos el enlace
        
        //** 2. Sujeto
        LTermino sujetoT=or.getSujeto(); //Cogemos el sujeto de la acción.
        Node sujetoN=NetUtil.createNode(sujetoT); //Creamos el nodo
        if (sujetoN!=null)
            sujetoN.getWord().addPOS(sujetoT.getPos()); //Añadimos el POS (sin duplicar). Esto es para ir completando los POS de las palabras genéricas del diccionario (que están incompletos).
        
        //** 3. Objeto Directo
        LTermino objetoDirectoT=or.getObjetoDirecto(); //Obtenemos el Objeto Directo
        Node objetoDirectoN=NetUtil.createNode(objetoDirectoT); //Creamos el nodo
        
        //** 4. Objeto Indirecto
        LTermino objetoIndirectoT=or.getObjetoIndirecto(); //Obtenemos el Objeto Indirecto
        Node objetoIndirectoN=NetUtil.createNode(objetoIndirectoT); //Creamos el nodo
        
        //** 5. Complemento Circunstancial de Lugar
        LTermino ccLugarT=or.getCcLugar(); //Obtenemos el complemento circunstancial de lugar
        Node ccLugarN=NetUtil.createNode(ccLugarT); //Creamos el nodo
        
        //** 6. Creamos el hecho
        Fact hecho=new Fact(verbN,sujetoN,objetoDirectoN,objetoIndirectoN,ccLugarN);
        
        //** 7. Lo cargamos en la red semántica
        this.getDeepPlane().loadAndLink(hecho);
        
        return result;
    }
    
    /**
     * Procesa el párrafo de entrada para generar los fragmentos de la Red Semática que sea menester.
     * En este paso del proceso se analiza que las palabras (W) estén en el diccionario (es decir, sean conocidas por el sistema).
     * Como segundo paso se analiza si semánticamente, se está planteando una relación de Herencia (H) entre objetos.
     * Como tercer paso se analiza si semánticamente, se está planteando una relación de agregación (A) entre objetos.
     * Como cuarto paso se analiza la oración como ordinaria, para ser insertada en la red.
     * AVISO: Sólo se interpretarán/procesarán aquellas oraciones cuyas palabras se entiendan todas.
     * @param input Párrafo introducido por el usuario.
     * @return Lista de palabras desconocidas para el sistema.
     */
    public ArrayList processParrafo(LParrafo input){
        ArrayList<String> result=new ArrayList();
        
        for (LOracion or:input.getOraciones()){ //Procesamos cada oración.
            //*** 0. Primero vemos si todas las palabras de la oración son conocidas ***
            ArrayList aux=PrimitiveStructAnalizer.analizeWords(getDictionary(), or);
            result.addAll(aux); //Añadimos todas las palabras desconocidas
            if (aux.isEmpty()){ //Solo si se han comprendido todas las palabras de la oracion, puede interpretarse la frase
               //** 1. Analizamos si la oración asigna cualidades a un sujeto, es de herencia o de agregación.
               boolean analizada=processPrimitiveStructuresHA(or);
               //** 2. Procesamos la oración como ordinaria (si no ha sido analizada)
               if (!analizada){
                    analizada=processRegularSentence(or);
               }
            }
        }//for
        return result;
    }

    /**
     * @return Diccionario utilizado por este LearningEngine.
     */
    public Dictionary getDictionary() {
        return dictionary;
    }
    
    /**
     * Evalúa una oración ordinaria.
     * @param dp DeepPlane con la red semántica.
     * @param dic Diccionario utilizado.
     * @param or Oración a evaluar.
     * @return True si la sentencia es cierta. False si no lo es. Null si la evaluación no es concluyente.
     */
    private Booleano evalRegularSentence(DeepPlane dp, Dictionary dic, LOracion or){
        Booleano result=null;
        
        Fact hecho=NetUtil.createFact(or); //Creamos el hecho que representa a la oración
        String linkKey=hecho.getLinkKey();
        if (dp.getLink(linkKey)!=null){ //Comprobamos si existe esa estructura
            result=new Booleano(true);
            if (hecho.getAccion().isNegado()){ //Si la pregunta fue negada, invertimos el resultado
                result=new Booleano(!result.isBool());
            }
        }else{
            result=new Booleano(false);
            if (hecho.getAccion().isNegado()){ //Si la pregunta fue negada, invertimos el resultado
                result=new Booleano(!result.isBool());
            }
        }
        
        return result;
    }
    
    /**
     * De acuerdo a estado de la Red Semántica DeepPlane, evalúa la certeza/falsedad de cada frase del párrafo.
     * @param input Párrafo introducido por el usuario
     * @return True si las sentencias del párrafo son ciertas. False en caso contrario.
     */
    public Booleano evaluateSentence(LParrafo input){
        Booleano result=new Booleano(false);
        boolean procesada=false;
        for (LOracion or:input.getOraciones()){ //Procesamos cada oración.
            ArrayList aux=PrimitiveStructAnalizer.analizeWords(getDictionary(), or);
            if (aux.isEmpty()){ //Solo si se han comprendido todas las palabras de la oracion, puede interpretarse la frase
               //** 1. Analizamos si la oración es copulativa: asigna cualidades a un sujeto
               procesada=(or.isOracionCopulativaSer(dictionary)&&!or.isOracionAgregacion(dictionary));
               result=PrimitiveStructAnalizer.evalOrCopulativa(getDeepPlane(), dictionary, or); //True si es cierta, false si no lo es, null si no puede evaluar
               //** 2. Analizamos si la oración expresa agregación: define la composición de un objeto
               if (!procesada){ 
                   procesada=or.isOracionAgregacion(dictionary);
                   result=PrimitiveStructAnalizer.evalOrAgregacion(getDeepPlane(), dictionary, or); //True si es cierta, false si no lo es, null si no puede evaluar
               }
               //** 3. Analizamos la oración como ordinaria
               if (!procesada){
                    result=evalRegularSentence(getDeepPlane(), dictionary, or);
               }
            }
        }//for
        return result;
    }
    
     /**
     * Genera una cadena de texto reflejando la jerarquía descendente de los componentes de un objeto/concepto.
     * Si el objeto/concepto no tiene componentes, se devuelve la propia palabra.
     * @param word Parabra de la que se desea obtener la jerarquía descendente de componentes (si la tiene).
     * @return String con una representación del arbol descendente de la jerarquía de componentes la palabra indicada.
     */
    public String printComponentes(String word){
        String result="";
        if (!StringUtil.isEmpty(word)){
            word=(word.indexOf(" ")>0?word.substring(0,word.indexOf(" ")):word.trim()); //Nos quedamos con la primera palabra
            result=PrimitiveStructAnalizer.printComponentes(word, getDeepPlane()); //Obtenemos todos los nodos concordantes
        }
        return result;
    }
    
     /**
     * Genera una cadena de texto reflejando la jerarquía ascendente de los objeto/concepto que tienen a esta palabra como componente.
     * Si el objeto/concepto no pertenece a ningún objeto, se devuelve la propia palabra.
     * @param word Parabra de la que se desea obtener la jerarquía de objetos a los que pertenece (si la tiene).
     * @return String con una representación del arbol de la jerarquía de objetos a los que pertenece.
     */
    public String printAgregadores(String word){
        String result="";
        if (!StringUtil.isEmpty(word)){
            word=(word.indexOf(" ")>0?word.substring(0,word.indexOf(" ")):word.trim()); //Nos quedamos con la primera palabra
            result=PrimitiveStructAnalizer.printAgregadores(word, getDeepPlane()); //Obtenemos todos los nodos concordantes
        }
        return result;
    }

    
     /**
     * Genera una cadena de texto reflejando la jerarquía descendente de un objeto/concepto (y/o tipo de objeto/concepto).
     * Si el objeto/concepto no tiene jerarquía, se devuelve la propia palabra.
     * @param word Parabra de la que se desea obtener la jerarquía descendente (si la tiene).
     * @return String con una representación del arbol descendente de la jerarquía de la palabra indicada.
     */
    public String printJerarquiaDescendente(String word){
        String result="\n";
        if (!StringUtil.isEmpty(word)){
            //word=(word.indexOf(" ")>0?word.substring(0,word.indexOf(" ")):word.trim()); //Nos quedamos con la primera palabra
            result=PrimitiveStructAnalizer.printJerarquiaDescendente(word, getDeepPlane()); //Obtenemos todos los nodos concordantes
        }
        return result;
    }
    
     /**
     * Genera la jerarquía descendente del estándar SNOMED-CT.
     * @param word Parabra de la que se desea obtener la jerarquía descendente (si la tiene).
     * @return String con una representación del arbol descendente de la jerarquía de la palabra indicada.
     */
    public String printJerarquiaDescendenteSNOMED(String word){
        String result="\n";
        if (!StringUtil.isEmpty(word)){
            //word=(word.indexOf(" ")>0?word.substring(0,word.indexOf(" ")):word.trim()); //Nos quedamos con la primera palabra
            result=PrimitiveStructAnalizer.printJerarquiaDescendenteSNOMED(word, getDeepPlane()); //Obtenemos todos los nodos concordantes
        }
        return result;
    }
    
     /**
     * Genera una cadena de texto reflejando la jerarquía ascendente de un objeto/concepto (y/o tipo de objeto/concepto).
     * Si el objeto/concepto no tiene jerarquía, se devuelve la propia palabra.
     * @param word Parabra de la que se desea obtener la jerarquía ascendente (si la tiene).
     * @return String con una representación del arbol descendente de la jerarquía de la palabra indicada.
     */
    public String printJerarquiaAscendente(String word){
        String result="";
        if (!StringUtil.isEmpty(word)){
            //word=(word.indexOf(" ")>0?word.substring(0,word.indexOf(" ")):word.trim()); //Nos quedamos con la primera palabra
            result=PrimitiveStructAnalizer.printJerarquiaAscendente(word.trim(), getDeepPlane()); //Obtenemos todos los nodos concordantes
        }
        return result;
    }
    
    /**
     * Resetea por completo todo lo aprendido (elimina toda la red semántica).
     */
    public void resetAll(){
        this.deepPlane=new DeepPlane(); //Plano de abstracción profundo nuevo, desechando el anterior
    }
    
    
    /**
     * Obtiene un listado de todos los nodos y todos los enlaces de la red.
     * @return Cadena de texto con todos los nodos y enlaces de la red.
     */
    public String printAllSemanticNet(){
        ArrayList<ArrayList<Node>> nodos=new ArrayList(this.getDeepPlane().getNodosMap().values()); //Lista de listas de nodos
        ArrayList<Link> enlaces=new ArrayList(this.getDeepPlane().getLinksMap().values());
        String result="NODOS:\n";
        for (ArrayList<Node> ln:nodos){
            for (Node n:ln){
                result+=n.toString()+"\n";
            }//for
        }//for
        result+="ENLACES:\n";
        for (Link l:enlaces){
            result+=l.toString()+"\n";
        }//for
        return result;
    }
    
    
    /**
     * Obtiene una cadena de texto con todas las cualidades de un sustantivo.
     * @param sustantivo Sustantivo/nombre/lugar especificado, del que se desean obtener las cualidades.
     * @return Cadena con las cualidades del sustantivo especificado
     */
    public String getCualidades(String sustantivo){
        String result="\n";
        ArrayList<Node> cualidades= this.getDeepPlane().getCualidades(sustantivo); //Siempre se parte de un nombre textual, p.e. "coche" (Dame las cualidades de un coche)
        //La variable "cualidades" tiene ahora la lista de nodos directamente enlazados al sustantivo que son atributos del mismo (color, peso, etc) o adjetivos directamente (azul, grande, listo, etc).
        int i=1;
        for (Node n:cualidades){ //Para cada una de las cualidades encontradas
            if (n.getTipo().equals(DWord.POS_NOUN)){ //Si la cualidad es del tipo: pájaro -tiene-> color -es-> azul
                result+=StringUtil.padding(32)+i+") "+n.getWord().getText()+": ";
                String valores="";
                ArrayList<Node> subCualidades= this.getDeepPlane().getCualidadesByID(n.getId()); //Recuperamos las cualidades de ese Nodo concreto
                for (Node nsub:subCualidades){
                    valores+=StringUtil.negado(!nsub.isNegado())+nsub.getWord().getText()+", ";
                }//for subcualidades
                if (!valores.isEmpty()) valores=valores.substring(0,valores.length()-2)+"."; //Quitamos el ", " sobrante
                result+=valores+"\n";
            }else{ //Este caso es del tipo: pájaro -es-> azul
                result+=StringUtil.padding(32)+i+") "+StringUtil.negado(!n.isNegado())+n.getWord().getText()+".\n";
            }    
            i++;
        }//for

        return result;
    }

    /**
     * @return the deepPlane
     */
    public DeepPlane getDeepPlane() {
        return deepPlane;
    }
    
    /**
     * Busca en la Red Semántica y recupera todos los elementos que tengan el atributo con el valor especificado.
     * @param attributeName Nombre del atributo buscado.
     * @param attributeValue Valor que debe tener el atributo buscado.
     * @param considerSinonims True si queremos considerar los sinónimos del attributeName; false en caso de que queramos una comparación literal con el attributeName
     * @return Listado en modo texto de todos los elementos encontrados.
     */
    public String getElementsbyAttribute(String attributeName, String attributeValue, boolean considerSinonims){
        String result="";
        ArrayList<Node> elements=PrimitiveStructAnalizer.getNodesbyAttribute(deepPlane, attributeName, attributeValue); //Recuperamos los nodos que coinciden exactamente con el nombre de atributo

        if (considerSinonims){ //Si consideramos sinónimos
            DWord attributeWord=this.dictionary.getWord(attributeName);
            if (attributeWord!=null){ //Si el sinonimo existe (pues puede ser una palabra inventada no presente en el diccionario)
                for (DWord sin:attributeWord.getSinonimos()){
                   ArrayList<Node> moreElements=PrimitiveStructAnalizer.getNodesbyAttribute(deepPlane, sin.getText(), attributeValue); //Elementos vinculados a este sinónimo
                   elements.addAll(moreElements); //Añadimos todos los elementos a la lista
                }//For sinonimos
            }
        }//If sinónimos
        
        for (Node nod:elements){
            result+=nod.getWord().getText()+", ";
        }//for
        result=(StringUtil.isEmpty(result)?"":result.substring(0, result.length()-2)); //Quitamos el ", " sobrante
                
        return result;
    }
    
    
    /**
     * Crea (si es necesario) una relación clase -> subclase en la Red Semántica.
     * @param userInput Entrada del usuario (p.e. "el cuervo es un tipo de ave".
     * @param subclase Texto de la subclase (p.e. "cuervo").
     * @param superclase Texto de la superclase (p.e. "ave").
     */
    public void loadSubclaseSuperclase(LParrafo userInput, String subclase, String superclase){
        //Obtenemos y/o creamos las palabras/términos
        DWord subclaseW=getDictionary().getWord(subclase,DWord.POS_NOUN,true); //Obtenemos la palabra y/o la creamos si es necesario
        DWord superclaseW=getDictionary().getWord(superclase,DWord.POS_NOUN,true); //Obtenemos la palabra y/o la creamos si es necesario
        
        //Obtenemos y/o creamos sus nodos en la Red Semántica
        Node nodo=new Node(subclaseW); //Subclase
        Node nodeSub=getDeepPlane().getNode(nodo, true, false); //Recupera el nodo y/o lo crea
        nodo=new Node(superclaseW); //Superclase
        Node nodeSuper=getDeepPlane().getNode(nodo, true, false); //Recupera el nodo y/o lo crea
        
        //Una relación clase-subclase usa siempre el verbo "ser"
        DVerb ser=getDictionary().getVerb("ser").get(0);
        Link enlaceSub=getDeepPlane().getLink(nodeSub, nodeSuper, ser, Link.LINK_ES_SUBCLASE); //Recuperamos y/o creamos el enlace
        Link enlaceSuper=getDeepPlane().getLink(nodeSuper, nodeSub, ser, Link.LINK_ES_SUPERCLASE); //Recuperamos y/o creamos el enlace

        //Enlazamos los nodos con enlace de SUBCLASE
        getDeepPlane().linkNodes(nodeSub, nodeSuper, enlaceSub); //Enlazamos subclase -> clase
        getDeepPlane().linkNodes(nodeSuper, nodeSub, enlaceSuper); //Enlazamos clase -> subclase

        //Añadimos la trazabilidad que causa este enlace
        LOracion ora=userInput.getOraciones().get(0); //Cogemos la primera oración que es la descomposición de una frase tipo "el cuervo es un tipo de ave"
        enlaceSub.addOrigen(ora);
        enlaceSuper.addOrigen(ora);      
    }
    
        
    
    
}//class
