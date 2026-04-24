package es.sdweb.memorycorp.nlpengine;

import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.memorycorp.ui.console.UserConsole;
import java.util.ArrayList;

/**
 * Clase encargada de hacer el Análisis del Lenguaje Natural (NPL) del Castellano/Español.
 * AVISO: "El pollo comió un melón. El pollo comio un melon."; Ojo: el arbol sintáctico varía con y sin acentos.
 * @author Antonio Carro Mariño
 */
public class NLPEngine {
    
    private NLPBasicEngine basicEngine=new NLPBasicEngine(); //Procesamiento básico del lenguaje
    private LConversacion conversacion=new LConversacion(); //Conversación con el input/usuario
    
    /**
     * Analiza y añade un párrafo que se recibe en formato texto a la conversación, analizando su estructura, y devolviendola.
     * @param text Parrafo de texto que puede contener varias oraciones.
     * @return La estructura del parrafo completo añadida a la conversación.
     */
    public LParrafo addParrafo(String text){
        LParrafo result=basicEngine.analizaParrafo(text); //Analizamos el párrafo
        this.conversacion.addParrafo(result); //Lo añadimos a la conversación.
        result.setConversacion(this.conversacion); //Enlazamos en sentido contrario también
        return result;
    }

    /**
     * Analiza un párrafo que se recibe en formato texto, generando su estructura, y devolviendola.
     * @param text Parrafo de texto que puede contener varias oraciones.
     * @return La estructura del parrafo completo.
     */
    public LParrafo analizaParrafo(String text){
        LParrafo result=basicEngine.analizaParrafo(text); //Analizamos el párrafo
        return result;
    }

    /**
     * Analiza una palabra que se recibe en formato texto, generando sus metadatos, y devolviendola.
     * @param text Palabra a analizar (debe introducirse una sola palabra)
     * @return El término correspondiente a la palabra analizada.
     */
    public LTermino analizaPalabra(String text){
        LParrafo par=basicEngine.analizaParrafo(text); //Analizamos el párrafo
        LTermino result=null;
        if (!par.getOraciones().isEmpty()){ //Si hay algún texto analizado
            result=par.getFirstTerm(); //Devolvemos el primer término encontrado
        }
        return result;
    }
    
    /**
     * @return Devuelve la conversacion (colección de párrafos de entrada) vinculada a esta instancia.
     */
    public LConversacion getConversacion() {
        return conversacion;
    }
    
    
    /**
     * Devuelve el parrafo más reciente de la conversación.
     * @return Párrafo más reciente de la conversacion.
     */
    public LParrafo getLastInput(){
        LParrafo result=null;
        ArrayList parrafos=conversacion.getParrafos();
        if (!parrafos.isEmpty()){
            result=(LParrafo)parrafos.get(parrafos.size()-1);
        }
        return result;
    }
    
    /**
     * Para cada palabra del diccionario, establece el tipo de palabra que es (POS): ADJ, ADV, ADP, etc.
     * Solo establece el POS cuando éste es null, es decir, no está establecido de antemano.
     * @param dictionary Diccionario
     */
    public void initializePOS (Dictionary dictionary){
        //****** Completamos la inicialización del diccionario ********
        String letra=""; //Variable para notificar progreso
        for (DWord word:dictionary.getAllPalabras()){  //Completamos la inicialización POS de las palabras (no verbos, salvo infinitivos)
            LogUtil.logTraza("[INICIALIZACION FASE_2] Analizando POS de ->"+word.getText(),7);
            LTermino term=this.analizaPalabra(word.getText()); //Analizamos la palabra
            if (word.getAllPOS().isEmpty()){ //Si el POS no viene prefijado
                word.addPOS(term.getPos()); //Guardamos el POS de esta palabra del diccionario
            }
            
            //Feedback de inicialización
            String aux=word.getText().substring(0,1).toLowerCase(); //Letra que inicia la palabra
            if (!letra.equals(aux)&&(letra.compareTo(aux)<0)){ //Si saltamos de letra del alfabeto
                letra=aux;
                switch (letra){
                    case "a": LogUtil.logTraza("[INICIALIZACION FASE_2] Analizando POS: 0%",5); break;
                    case "c": LogUtil.logTraza("[INICIALIZACION FASE_2] Analizando POS: 25%",5); break;
                    case "m": LogUtil.logTraza("[INICIALIZACION FASE_2] Analizando POS: 50%",5); break;
                    case "r": LogUtil.logTraza("[INICIALIZACION FASE_2] Analizando POS: 75%",5); break;
                    case "y": LogUtil.logTraza("[INICIALIZACION FASE_2] Analizando POS: 100%",5); break;
                }//switch
            }
        }//for
    }
    
    
    /**
     * Obtiene un listado de todas las palabras relevantes que contiene la oración, excluyendo nombres, determinantes, adverbios, etc.
     * @param sentence Oración de entrada
     * @return Lista de palabras clave contenidas en la oración.
     */
    public ArrayList<String> getKeyWords(LOracion sentence){
        ArrayList result=new ArrayList();
        NLPUtil.getKeyWords(sentence.getEstructura(), result); //Cargamos en "result" la lista de palabras relevante
        return result;
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //String text="El loro es un pájaro.";
        NLPEngine engine=new NLPEngine();
        //String text="El ladrón robó un martillo a Laura después de golpear a Jorge.";
        //String text="El ladrón es tonto. El ladrón es alto.";
        //String text="El ladrón robó un martillo que le gustaba mucho.";
        String text="Jorge ha amado a Laura.";
        
        SpanishDictionary.initializeDictionary();
        UserConsole.execConsole();

        /*
        // Lista los POS tags ("part-of-speech") de la frase por orden de ocurrencia: [DET, NOUN, AUX, ADJ, ...]
        List<String> posTags = sentence.posTags();
        System.out.println("Example: pos tags");
        System.out.println(posTags);
        System.out.println();
        // list of the NER (Named Entity Recognition) tags for the sentence. Similar a POS, pero para nombres de personas, ciudades, etc.
        List<String> nerTags = sentence.nerTags();
        System.out.println("Example: ner tags");
        System.out.println(nerTags);
        System.out.println();
        // Entity (entidades NER) mencionadas in the sentence
        List<CoreEntityMention> entityMentions = sentence.entityMentions();
        System.out.println("Example: entity mentions");
        System.out.println(entityMentions);
        System.out.println(); */
        
        // this is the parse tree of the current sentence
        LParrafo par=engine.addParrafo(text);
        System.out.println(par.toStringEstructura());
        
        System.out.println("Oracion 1: "+ par.getOraciones().get(0).toString());
        System.out.println("Verbo principal: "+ par.getOraciones().get(0).getAccion().toString());
        System.out.println("Sujeto principal: "+ StringUtil.printObject(par.getOraciones().get(0).getSujeto()));
        System.out.println("Objeto Directo: "+ StringUtil.printObject(par.getOraciones().get(0).getObjetoDirecto()));
        System.out.println("Objeto Indirecto: "+ StringUtil.printObject(par.getOraciones().get(0).getObjetoIndirecto()));
   
    }//main
    
}//class
