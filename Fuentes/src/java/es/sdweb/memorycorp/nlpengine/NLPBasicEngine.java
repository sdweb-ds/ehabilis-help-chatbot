package es.sdweb.memorycorp.nlpengine;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import es.sdweb.application.componentes.util.Objeto;
import java.util.ArrayList;

/**
 * La lematizacion de la libería básica (Stanford) no funciona en Español, solo en Inglés.
 * https://stanfordnlp.github.io/CoreNLP/human-languages.html
 * Se ha implementado una lematización propia del Español (soy->ser; fue->ir; etc).
 * Igualmente se han corregido múltiples errores de interpretación de la librería.
 * @author Antonio Carro Mariño
 */
public class NLPBasicEngine {
    private StanfordCoreNLP pipeline=pipeline= new StanfordCoreNLP("spanish");

    /**
     * Permite identificar los Objetos Directos e Indirectos de la oración, y marcar los Principales, es decir, los de la oración global.
     * Recorre el árbol en profundidad hasta dar con el OD y el OI, si existen.
     * Los Objetos Principales son los que tienen menor nivel en el árbol.
     * AVISO: No tiene por que existir objeto/complemento Directo o Indirecto.
     * @param arbol Arbol sintactico de la oracion.
     * @param principalOI Encapsula el Objeto Indirecto principal. Encapsula NULO si no hay Objeto Indirecto
     * @param principalOD Encapsula el Objeto Directo principal. Encapsula NULO si no hay Objeto Directo
     */
    private void identificaObjetos(LTermino arbol, Objeto principalOI, Objeto principalOD){
        
        if (arbol.isObject()){ //Si es un objeto (directo o indirecto)
            boolean indirecto=false;
            for (LTermino hijo:arbol.getHijos()){ //Miramos para cada hijo (siguiente nivel), si esta presente la preposicion "a" o "para"
                String text=hijo.getTexto().toLowerCase();
                if (text.equals("a")||text.equals("para")){
                    indirecto=true;
                    arbol.setObjectIndirecto(true); //Lo marcamos como OI
                    LTermino cursor=(LTermino)principalOI.getObj();
                    if (cursor==null){
                      principalOI.setObj(arbol); //Si no hay Objeto Indirecto, lo guardamos
                    }else{
                        if (cursor.getNivel()>arbol.getNivel()){ //Nos quedamos con el objeto indirecto con menor nivel
                            principalOI.setObj(arbol);
                        }   
                    }
                }
            }//for
            
            if ((!indirecto)&&(!arbol.getPos().equals("PRON"))){ //Si es un Objeto, pero no es indirecto ni pronombre (sino nombre NOUN), implica que es directo
                LTermino cursor=(LTermino)principalOD.getObj();
                if (cursor==null){
                  principalOD.setObj(arbol); //Si no hay Objeto Directo, lo guardamos
                }else{
                    if (cursor.getNivel()>arbol.getNivel()){ //Nos quedamos con el objeto directo con menor nivel
                        principalOD.setObj(arbol);
                    }   
                }
            }
            
        }//si es un objeto
        
        //*** En cualquier caso seguimos analizando cada hijo    
        for (LTermino hijo:arbol.getHijos()){
            identificaObjetos(hijo,principalOI,principalOD); //Analizamos cada hijo
        }//for
    }
    
    /**
     * Este método permitirá identificar y marcar los elementos principales de la oración.
     * @param arbol 
     */
    private void identificaElementos(LTermino arbol, Objeto principalOI, Objeto principalOD){
        //*** primero buscamos y marcamos los Objetos Directos e Indirectos ****
        identificaObjetos(arbol, principalOI, principalOD);
        
        //Los punteros apuntan al OD y OI (o son nulos si no hay OD u OI)
        LTermino termOI=(LTermino)principalOI.getObj();
        LTermino termOD=(LTermino)principalOD.getObj();
        if (termOI!=null){ //Si hay Objeto Indirecto
            termOI.setPrincipal(true); //Establecemos el flag dentro del elemento del árbol
        }
        if (termOD!=null){ //Si hay Objeto Directo
            termOD.setPrincipal(true); //Establecemos el flag dentro del elemento del árbol
        }
            
    }
    
    /**
     * Crea el árbol sintáctico de la oración, y marca el verbo y el sujeto principal de la misma.
     * @param dependencyParse Cadena con el árbol de dependencias.
     * @return Raiz del árbol sintáctico de la oración.
     */
    private LTermino createTree(String dependencyParse){
        LTermino result=null;
        LTermino cursor=null; //Puntero al nodo donde se deben añadir los Terminos en el árbol
        
        LTermino accion=null; //Accion principal de la oracion (en principio consideramos solo una accion principal)
        LTermino sujeto=null; //Sujeto principal que realiza la acción principal
        
        String lineas[]= dependencyParse.split("\n");
        for (String linea : lineas) {
            //******** Leemos una linea ************
            int pospad=linea.indexOf("-");
            String pad=linea.substring(0, pospad);
            int nivel=(pad.length()>0?pad.length()/2:0); //La anchura del padding (va de 2 en 2 espacios) marca la profundidad en el arbol: pading->0,2,4,6,8,... -> nivel->0,1,2,3,...
            linea=linea.trim(); //A continuacion nos cargamos los espacios iniciales
            
            //******** Extraemos sus elementos ******
            String words[]=linea.split(" "); //Cada linea se rompe en 3 partes: "->", "azul/ADJ", "(root)"
            String textPOS= words[1]; //"azul/ADJ"
            int ibar=textPOS.indexOf("/");
            String text=textPOS.substring(0,ibar);
            String pos=textPOS.substring(ibar+1);
            String ud= words[2].substring(1, words[2].length()-1); //"(root)" elimando los paréntesis 
            String ref="";
            int iref=ud.lastIndexOf(":"); //Pues puede haber UDs del tipo "csubj:pass:__"
            if (iref>=0){ //Si la UD esta formada por dos elementos separados por ":", p.e. "conj:y"
                String aux=ud.substring(0,iref);
                ref=ud.substring(iref+1);
                ud=aux;
            }
            
            //****** Creamos un nuevo termino ********
            LTermino nuevo=new LTermino(); //Creamos el termino
            nuevo.setTexto(text); //Lemma
            nuevo.setPos(pos);
            nuevo.setUd(ud);
            nuevo.setReferencia(ref);
            nuevo.setNivel(nivel); //Profundidad en que debe ubicarse este nodo dentro del arbol: 0,1,2,3,...
            
            //****** Miramos si se trata de una accion *****
            if (pos.equals("VERB")||pos.equals("AUX")){
                if (accion==null){
                   accion=nuevo; //Si no hay acción, la guardamos
                   nuevo.setTexto(nuevo.getTexto().toLowerCase()); //pasamos el verbo a minúscula
                }else{
                   if (accion.getNivel()>nivel){ //Nos quedamos con la accion con menor nivel
                       accion=nuevo;
                   }   
                }
            }
            
            //****** Miramos si se trata de un sujeto *****
            if (ud.equals("nsubj")){
                if (sujeto==null){
                   sujeto=nuevo; //Si no hay sujeto, lo guardamos
                }else{
                   if (sujeto.getNivel()>nivel){ //Nos quedamos con el sujeto con menor nivel
                       sujeto=nuevo;
                   }   
                }
            }
            
            //****** Lo colgamos del nodo del árbol que corresponda *******
            if (cursor==null){
                cursor=nuevo; //El cursor pasa a apuntar al nodo raiz
                result=nuevo; //El resultado siempre devuelve el nodo raiz, del que cuelgan todos los demás
            }else{
                while (cursor.getNivel()>nivel-1){ //Si hay que ir hacia la raiz, vamos
                    cursor=cursor.getPadre();
                }
                cursor.addChild(nuevo); //Una vez ubicados en el nodo padre, lo colgamos
                nuevo.setPadre(cursor); //Y enlazamos el padre
                cursor=nuevo; //Y colocamos el cursor en el ultimo termino añadido
            }
            
        }//for
        
        //**** Por ultimo marcamos los elementos principales ****
        if (accion!=null){
           accion.setPrincipal(true); //Marcamos el término como principal (ya sea verbo, sujeto, objeto, etc)
        }
        if (sujeto!=null){
           sujeto.setPrincipal(true); //Marcamos el término como principal (ya sea verbo, sujeto, objeto, etc)
        }
        
        return result;        
    }
    
    /**
     * Obtiene el árbol de descomposición sintáctica de una oración.
     * @param oracion
     * @return 
     */
    public LTermino getSentenceStructure(String oracion){
        LTermino result=null;
        CoreDocument document= new CoreDocument(oracion);
        pipeline.annotate(document);
        
        for (CoreSentence sentence : document.sentences()){ //Para cada oracion, la analizamos y la guardamos
           SemanticGraph dependencyParse=sentence.dependencyParse();
           result=this.createTree(dependencyParse.toString()); //Establecemos la estructura de la oración
        }//for
        return result;
    }
    
    
    /**
     * Analiza un párrafo que se recibe en formato texto, descomponiendo su estructura, y devolviendola.
     * Identifica el OD y el OI. También identifica si los verbos están negados.
     * @param text Texto del párrafo a analizar
     * @return Estructura del párrafo analizado
     */
    LParrafo analizaParrafo(String text){
        LParrafo result=new LParrafo();
        result.setText(text); //Guardamos el texto original
        ArrayList<LOracion> oraciones=result.getOraciones(); //Un parrafo contiene una secuencia de oraciones
        CoreDocument document= new CoreDocument(text);
        pipeline.annotate(document);
        
        for (CoreSentence sentence : document.sentences()){ //Para cada oracion, la analizamos y la guardamos
           String txtOracion= sentence.toString();
           SemanticGraph dependencyParse=sentence.dependencyParse();
           LOracion oracion=new LOracion();
           oracion.setOracion(txtOracion); //Establecemos el texto literal de la oración
           oracion.setEstructura(this.createTree(dependencyParse.toString())); //Establecemos la estructura de la oración
           oracion.setParrafo(result); //Enlazamos en sentido contrario, cada oración con el párrafo al que pertenece
           
           //*** Identificamos los Objetos Directo e Indirecto
           Objeto resultOI=new Objeto(); //Variable auxiliar
           Objeto resultOD=new Objeto(); //Variable auxiliar
           identificaElementos(oracion.getEstructura(),resultOI,resultOD);
           oracion.setObjetoIndirecto((LTermino)resultOI.getObj()); //Los establecemos
           oracion.setObjetoDirecto((LTermino)resultOD.getObj());
           
           //*** Identificamos los verbos que están negados ******
           analizaNegacionesVerbales(oracion.getEstructura());
           //*** Identificamos los adjetivos que están negados ******
           analizaNegacionesAdjetivales(oracion.getEstructura());
           
           oraciones.add(oracion); //Añadimos la oracion al párrafo
        }//for
        return result;
    }
    
    
    /**
     * Analiza si los verbos contenidos en la oración están negados, para marcarlos como tales.
     * @param arbol Árbol de descomposición sintáctica de la oración.
     */
    void analizaNegacionesVerbales(LTermino arbol){
        if (arbol.isVerb()&&arbol.getPos().equals(DWord.POS_VERB)){ //si es un verbo no AUX (verbo SER), la negación la hallaremos en el siguiente nivel
            for (LTermino t:arbol.getHijos()){ //Vemos si hay presente un "no" ADV bajo él.
                if (t.getTexto().equals("no")&&(t.getPos().equals(DWord.POS_ADV))){ //Si está presente la negación
                    arbol.setNegado(true); //El verbo está negado
                }else{
                   if (t.isVerb()){ //Si el término examinado es un verbo de una oración subordinada o coordinada
                       analizaNegacionesVerbales(t); //Repetimos el proceso para este verbo.
                   }  
                }
            }//for
        }else{ //Si no es un verbo, analizamos el siguiente nivel
            boolean no=false;
            for (LTermino t:arbol.getHijos()){ //Analizamos el siguiente nivel buscando verbos
                if (t.getTexto().equals("no")&&(t.getPos().equals(DWord.POS_ADV))){ //Si está presente la negación
                    no=true;
                }
                if (t.isVerb()&&t.getPos().equals(DWord.POS_AUX)){ //Cuando es un verbo AUXiliar (verbo SER), la negación aparece en su mismo nivel y antes.
                    t.setNegado(no); //Si se halló "no", marcamos la negación
                }
            }//for
        }
    }


    
    /**
     * Analiza si los adjetivos contenidos en la oración están negados, para marcarlos como tales.
     * Busca elementos con UD "conj" (conjunción) y observa si tienen bajo ellos una negación "no" de UD "advmod".
     * @param arbol Árbol de descomposición sintáctica de la oración.
     */
    void analizaNegacionesAdjetivales(LTermino arbol){
        if (!arbol.isVerb()&&arbol.getUd().equals(LTermino.UD_CONJ)){ //si es un elemento (no un verbo), la negación la hallaremos en el siguiente nivel
            for (LTermino t:arbol.getHijos()){ //Vemos si hay presente un "no" ADV bajo él.
                if (t.getTexto().equals("no")&&(t.getPos().equals(DWord.POS_ADV))){ //Si está presente la negación
                    arbol.setNegado(true); //El elemento está negado
                }else{
                   if (!arbol.isVerb()&&arbol.getUd().equals(LTermino.UD_CONJ)){ //Si el término examinado también debe ser examinado
                       analizaNegacionesAdjetivales(t); //Repetimos el proceso para este elemento.
                   }  
                }
            }//for
        }else{ //Si es cualquier otro elemento, analizamos el siguiente nivel
            for (LTermino t:arbol.getHijos()){ //Analizamos el siguiente nivel buscando negaciones adjetivales
                analizaNegacionesAdjetivales(t); //Repetimos el proceso para este elemento.
            }//for
        }
    }

    
}//class
