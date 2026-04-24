package es.sdweb.memorycorp.nlpengine;

import es.sdweb.application.componentes.util.NumberUtil;
import es.sdweb.application.componentes.util.StringUtil;
import java.util.ArrayList;

/**
 * Utilidades para manipular términos lingüísticos.
 * @author Antonio Carro Mariño
 */
public class NLPUtil {
    
    /**
     * Indica si una palabra es un nombre, en función de si empieza por mayúscula o su POS es POS_PROPN o POS_PROPNL.
     * @param word Palabra a analizar.
     * @param pos POS de la palabra (si se conoce). Null si no se conoce.
     * @return La palabra (con la primera letra en mayúscula) si es un nombre. Null en caso contrario.
     */
    public static String getName(String word, String pos){
        String result=null;
        if (!StringUtil.isEmpty(word)){
            String letra=word.substring(0,1);
            boolean primMay=((letra.compareTo("A")>=0&&letra.compareTo("Z")<=0)||(letra.equals("Ñ"))); //Si la primera letra es mayúscula
            if (primMay||(!StringUtil.isEmpty(pos)&&(pos.equals(DWord.POS_PROPN)||pos.equals(DWord.POS_PROPNL)))){ //Si empieza por mayúscula o ha sido evaluado como nombre previamente
               result=word;
            }
        }
        return result;
    }
    
    
    /**
     * Indica si una palabra, por su posición en la frase y su forma, es un Nombre (de persona, lugar, etc).
     * Se actualiza el texto del término con la primera letra en mayúscula, si resulta ser un nombre de lugar.
     * @param termino Término de la palabra a analizar.
     * @return True si es un nombre de lugar (y se actualiza el texto del término con la primera mayúscula). False en caso de que no sea un nombre.
     */
    public static boolean isName(LTermino termino){
        boolean result=false;
        String nombre=getName(termino.getTexto(), termino.getPos());
        if (nombre!=null){ //Si es un nombre
            termino.setTexto(nombre); //Corregimos el nombre (ponemos la primera en mayúscula)
            /*
            for (LTermino hijo:termino.getHijos()){ //Miramos en el siguiente nivel si es un lugar
                if (hijo.getTexto().equals("en")){ //Indicativo de lugar (aquí no puede ser tiempo)
                    result=true;
                }
            }//for
            */
            result=true; //Es un nombre/apellido de Persona
        }//if es nombre
        return result;
    }
    
    /**
     * Indica si una palabra es un nombre, por ejemplo, "Pedro", o "Madrid".
     * @param word Palabra a analizar.
     * @param engineNLP Para analizar esto, se utilizará un motor NLP.
     * @return True si la palabra es un nombre. False en caso contrario.
     */
    public static boolean isNameNLP(String word, NLPEngine engineNLP){
        boolean result=false;
        if (!StringUtil.isEmpty(StringUtil.trim(word))){
            LParrafo par=engineNLP.addParrafo(word);
            LOracion or=par.getOraciones().get(0); //Cogemos la primera oración
            LTermino termino=or.getEstructura(); //Cogemos la raiz de la estructura, que contiene la palabra analizada.
            result=isName(termino); //Analizamos si es un nombre
        } //Si la entrada tiene algo
        return result;
    }
    
    
    /**
     * Indica si una palabra es un objeto al que se refiere una posición. P.e. "mesa" en: El gato está debajo de la mesa.
     * Esta función determina lugares comunes, que no tienen nombre propio (mesa, silla, parque, jardín, etc).
     * Si analizamos "mesa", la función devolverá el término "debajo".
     * AVISO: Puede referenciar también tiempos. P.e. "Galileo nació sobre 1564", "Galileo nació en 1564" ("en" podría expresar lugar, pero expresa tiempo).
     * @param object Término que representa al objeto a analizar ("mesa").
     * @return LTermino que indica la posición respecto al objeto analizado (debajo, encima, etc). Null si el objeto (por ejemplo "mesa") no es referenciado como posicionamiento.
     */
    public static LTermino isPositionObject(LTermino object){
        LTermino result=null;
        //**** Buscamos bajo el término, señalizadores de lugar *******
        for (LTermino hijo:object.getHijos()){ //Miramos en el siguiente nivel
            if (SpanishDictionary.isPositionFlag(hijo.getTexto())){ //Vemos si bajo la palabra hay indicadores de posicionamiento de lugar
                result=hijo;
            }
        }//for
        //**** Buscamos sobre el término, señalizadores de lugar ******
        LTermino padre=object.getPadre(); //Vemos si el padre indica posicionamiento físico
        if (padre!=null){ //Si hay
            if (SpanishDictionary.isPositionFlag(padre.getTexto())){
                result=padre;
            }
        }
        
        //**** Por último comprobamos si es un número. Si lo es, esta palabra no indica un lugar. P.e. "en 1516"
        if (NumberUtil.isNumber(object.getTexto())){
            result=null;
        }
            
        return result;
    }
    
    
    /**
     * Indica si una palabra, por su posición en la frase y su forma, es un Nombre de lugar con nombre propio (p.e. "NuevaYork").
     * Se actualiza el texto del término con la primera letra en mayúscula, si resulta ser un nombre de lugar.
     * @param terminoLugar Término de la palabra a analizar.
     * @return True si es un nombre de lugar (y se actualiza el texto del término con la primera mayúscula). False en caso de que no sea un nombre.
     */
    public static boolean isPlaceName(LTermino terminoLugar){
        boolean result=false;
        String nombre=getName(terminoLugar.getTexto(), terminoLugar.getPos());
        if (nombre!=null){ //Si es un nombre
            terminoLugar.setTexto(nombre); //Corregimos el nombre (ponemos la primera en mayúscula)
            for (LTermino hijo:terminoLugar.getHijos()){ //Miramos en el siguiente nivel
                if (SpanishDictionary.isPositionFlag(hijo.getTexto())){ //Vemos si bajo la palabra hay indicadores de posicionamiento de lugar, p.e. "en Vigo"
                    result=true;
                }
            }//for
            
        }//if es nombre
        return result;
    }
    
    
    /**
     * Trasforma una lista de conjugaciones en una lista de los infinitivos a los que pertenecen.
     * Exactamente en el mismo orden en que figuran.
     * @param conjugaciones Conjugaciones a transformar.
     * @return Listado de infinitivos que están relacionados con cada conjugación.
     */
    public static ArrayList<DVerb> getInfinitivos(ArrayList<DVerb> conjugaciones){
        ArrayList<DVerb> result=new ArrayList();
        for (DVerb v:conjugaciones){
            result.add(v.getVerboInfinitivo()); //Añadimos los infinitivos
        }//for
        return result;
    }
    
    /**
     * Devuelve la conjugación verbal desambiguada. Ello dado que hay conjugaciones homógrafas.
     * Puede haber conjugaciones homógrafas de verbos distintos: "fue" (de "ser"), "fue" (de "ir")
     * Puede haber dos conjugaciones iguales del mismo verbo: "era" (de "ser")-> primera y tercera persona singular (yo era, él era)
     * AVISO: aun falla en este último caso, por no contemplar la concordancia.
     * @param or Oración a analizar.
     * @return Verbo desambiguado.
     */
    public static DVerb getVerbUnambiguous(LOracion or){
        DVerb result=null;
        String conjTxt=or.getAccion().getTexto(); //Texto del input, p.e. "fue"
        ArrayList<DVerb> conjugaciones=SpanishDictionary.getDictionary().getVerb(conjTxt); //P.e. "fue" (de "ser"), "fue" (de "ir")
        ArrayList<DVerb> infinitivos=getInfinitivos(conjugaciones); //Obtenemos la lista de infinitivos: "ser", "ir"
        
        //**** Desambiguamos ser-ir
        String infTxt=infinitivos.get(0).getText(); //Nos llega con ver uno solo de la lista
        if (infTxt.equals("ser")||infTxt.equals("ir")){
            //Una vez visto que es "ser" o "ir", analizamos cual de los dos es por su contexto.
            String verboCtx=or.desambiguaSerIrEstar(SpanishDictionary.getDictionary()); //AVISO: puede haber dos conjugaciones iguales del mismo verbo: "era" ("ser")-> primera y tercera persona singular
            for (int i=0; i<infinitivos.size(); i++){
                String aux=infinitivos.get(i).getText();
                if (aux.equals(verboCtx)){ //Si el infinitivo coincide con el infinitivo deducido
                    result=conjugaciones.get(i); //Nos quedamos con una de las conjugaciones posibles (a falta de concordancia)
                }
            }//for
        }
        
        //*** Por defecto devolvemos la primera ocurrencia.
        if (result==null){
            result=SpanishDictionary.getDictionary().getVerb(conjTxt).get(0);
        }
        
        return result;
    }
    

    /**
     * Indica si una palabra tiene género opuesto, lo que implica que es un ser vivo, y posible beneficiario (complemento indirecto).
     * @param palabra Palabra a analizar si tiene género.
     * @return True si tiene género, false si no lo tiene.
     */
    public static boolean tieneGeneroOpuesto(String palabra){
        DWord word=SpanishDictionary.getDictionary().getWordOnly(palabra);
        if (word==null) return false; //Si la palabra no está en el diccionario => no tiene género opuesto 
        boolean result=word.getPalabraGeneroOpuesto()!=null;
        return result;
    }
    
    
    /**
     * Indica si una palabra es indicativa de pregunta (oración interrogativa): qué, cómo, cuándo, cuánto, "?", "¿", etc
     * @param word Palabra a analizar.
     * @return True si es indicativa de pregunta, false en caso contrario.
     */
    public static boolean isQuestionWord(String word){
        boolean result=SpanishDictionary.isQuestionFlag(word);
        return result;
    }
    
     /**
     * Obtiene un listado de todas las palabras relevantes que contiene la oración, excluyendo nombres, determinantes, adverbios, etc.
     * @param te Término raiz de la oración
     * @param lista Lista de palabras clave de la oración
     */
    public static void getKeyWords(LTermino te, ArrayList<String> lista){
        switch (te.getPos()){ //Si la palabra es de estos tipos, la guardamos
            case "ADJ":
            case "NOUN":
                if (!te.getUd().equals("nsubj")){ //El sujeto lo ignoramos
                   lista.add(te.getTexto().toLowerCase()); //Lo pasamos a minúsculas, ya que no es nombre propio
                }
        }
        for (LTermino h:te.getHijos()){ //Examinamos cada uno de sus hijos
            getKeyWords(h,lista);
        }//for
    }
    
    
    /**
     * Número de términos coincidentes entre las dos listas de palabras que se reciben como parámetros.
     * Si todas las palabras de la lista origen se encuentran en la lista destino, la coincidencia es del 100%.
     * En otro caso el porcentaje de coincidencia es igual al porcentaje de coincidencias de la lista más pequeña.
     * P.e. Si ListaOr={perro, gato, casa} y ListaDes={perro, gato, coche, avión}, entonces resultado=2/3=0.67
     * @param listaOr Lista origen a comparar con la lista destino.
     * @param listaDes Lista destino con la que se compara.
     * @param dic Diccionario.
     * @return Porcentaje de coincidencia (100% conincidencia total, 0% ninguna coincidencia);
     */
    public static double getCoincidenceRatio(ArrayList<String> listaOr, ArrayList<String> listaDes, Dictionary dic){
        if (listaOr.isEmpty()||listaDes.isEmpty()) return 0; //Si alguna lista es vacía no hay coincidencia (par que no se imprima nada) y salimos
        int coincidencias=0;
        for (String w:listaOr){
            if (listaDes.contains(w)){ //Comprobamos si el término literal existe
                coincidencias++;
            }else{ //En otro caso comprobamos sus sinónimos
                DWord p=dic.getWord(w);
                if (p!=null){ //Si la palabra está en el diccionario la analizamos, en otro caso no hacemos nada 
                    ArrayList<DWord> sinonimos=p.getSinonimos();
                    boolean existeSin=false;
                    for (DWord dw:sinonimos){ //Si hay alguna coincidencia con sus sinónimos, sumamos una coincidencia
                        if (listaDes.contains(dw.getText())){
                            existeSin=true;
                        }
                    }//for
                    if (existeSin) coincidencias++; //Sumamos una coincidencia
                }//if existe en diccionario
            }
        }//for
        double result=((double)coincidencias / (double)listaOr.size()); //Porcentaje de coincidencia
        return result;
    }
    
    
}//class
