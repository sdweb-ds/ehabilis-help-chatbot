package es.sdweb.memorycorp.core;

import es.sdweb.application.componentes.util.Booleano;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.memorycorp.nlpengine.DVerb;
import es.sdweb.memorycorp.nlpengine.DWord;
import es.sdweb.memorycorp.nlpengine.Dictionary;
import es.sdweb.memorycorp.nlpengine.LOracion;
import es.sdweb.memorycorp.nlpengine.LTermino;
import es.sdweb.memorycorp.nlpengine.SpanishDictionary;
import es.sdweb.memorycorp.rulesengine.Attribute;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Esta clase aglutina todas las operaciones de manipulación de la Red Semántica relacionadas con las estructuras primitivas.
 * Las estructuras primitivas son: herencia, agregación, sinonimia, antonimia, posesión, ubicación, secuencia, etc.
 * @author Antonio Carro Mariño
 */
public class PrimitiveStructAnalizer {
    
    /**
     * Analiza si las palabras contenidas en una oración son conocidas por el sistema (diccionario).
     * @param dict Diccionario del sistema.
     * @param or Oración a analizar.
     * @return Listado de palabras desconocidas por el sistema.
     */
    public static ArrayList<String> analizeWords(Dictionary dict, LOracion or){
        ArrayList<String> result=new ArrayList();
        String text=StringUtil.trim(or.getOracion()); //deben respetarse las mayúsculas que haya escrito el usuario
        String[] words=text.split(" "); //Obtenemos las palabras
        for (String palabra:words){
           boolean existe=dict.existePalabra(palabra); //Miramos si existe la palabra
           if (!existe){
               palabra=StringUtil.trimNormalizado(palabra); //Le quitamos las comas y demás caracteres raros
               result.add(palabra); //Si no existe, la guardamos
           }
        }//for
        return result;
    }
    
    
    /**
     * Indica si el término (NOUN) que se recibe como parámetro es indicativo de la presencia de una clasificación,
     * y en caso de que así lo sea, devuelve la clase en la que se enmarca el término recibido.
     * P.e. "El gorrión es un tipo de ave". El término "tipo" indica subclasificación, en este caso de ave (otro NOUN).
     * En este caso la clasificación es: "gorrión" -> "ave" (no "tipo").
     * Si el texto dice: "El gorrión es un ave". Se devolverá "ave".
     * @param termino Término a analizar (debe ser un NOUN raíz de la oración, ya que se trata de una oración copulativa).
     * @param enlace Enlace vinculado al verbo de la oración copulativa. Se ajustará su tipo primitivo: subclase/superclase/nada
     * @return El término bajo el que se clasifica (p.e. "ave" NOUN) si el término es subclasificatorio. Null si no es un término clasificatorio.
     */
    public static LTermino esTerminoClasificatorio(LTermino termino, Link enlace){
        LTermino result=null;
        LTermino destino=null; //En caso de que sea un termino clasificatorio, debemos buscar la propiedad
        for (LTermino clase:termino.getHijos()){ //Buscamos la clase entre los nodos hijos
            String ud=clase.getUd(); //La clase será aquella que tenga una UD "nmod", es decir, modificador de un nombre
            if (ud.equals(LTermino.UD_NMOD)){ //Si hallamos un modificador al nombre (en este caso "tipo",etc)
                destino=clase; //Implica que esta palabra es la clase
            }
        }//for
        
        if (termino.getPos().equals(DWord.POS_NOUN)){ //Si es un nombre, no un adjetivo
            String text=termino.getTexto().toLowerCase();
            if (text.equals("tipo")||text.equals("tipología")||text.equals("clase")||text.equals("subclase")||
                text.equals("subtipo")||text.equals("especie")||text.equals("subespecie")||text.equals("categoría")||
                text.equals("subcategoría")||text.equals("casta")||text.equals("estirpe")||text.equals("estamento")||
                text.equals("orden")){
                result=destino; //La propiedad a asignar es la subclase
                enlace.setTipoPrimitivo(Link.LINK_ES_SUBCLASE); //Además es un enlace de subclasificación
            }else if (text.equals("superclase")||text.equals("supertipo")||text.equals("supercategoría")){
                result=destino; //La propiedad a asignar es la superclase
                enlace.setTipoPrimitivo(Link.LINK_ES_SUPERCLASE); //Además es un enlace de superclasificación
            }else{
                result=termino; //Devolvemos el nombre raíz.
                enlace.setTipoPrimitivo(Link.LINK_ES_SUBCLASE); //Es un enlace de subclasificación, en cualquier caso
            }
        }//Si es un nombre
        return result;
    }
    
    
    /**
     * Genera un fragmento de red correspondiente al Atributo que recibe como parámetro.
     * Esta función opera en el caso de que el atributo no se haya especificado.
     * El fragmento de red que se creará tiene la forma:
     *    1. Si el nombre de la propiedad es vacío, el valor se enlaza directamente al objeto.
     *       p.e.: pájaro -es-> azul
     * @param dp Plano profundo de comprensión semántica.
     * @param dict Diccionario en uso.
     * @param or Oración a analizar.
     * @param atr El atributo contiene el nombre de la propiedad (p.e. "color") y su valor (p.e. "azul"), y el objeto al que pertenece (p.e. "pájaro")
     */
    private static void linkAttributeObjectValue(DeepPlane dp, Dictionary dict, LOracion or, Attribute atr){
        //Obtenemos y/o creamos las palabras/términos
        DWord objectW=dict.getWord(atr.getObject(),DWord.POS_NOUN,true); //Obtenemos la palabra y/o la creamos si es necesario
        DWord valueW=dict.getWord(atr.getValue(),DWord.POS_NOUN,true); //Obtenemos la palabra y/o la creamos si es necesario
        
        //Obtenemos y/o creamos sus nodos en la Red Semántica
        Node nodo=new Node(objectW); //Objeto
        Node nodeObj=dp.getNode(nodo, true, false); //Recupera el nodo y/o lo crea
        
        //Eliminamos el viejo valor (Nodo) asignado al atributo (si existe)
        ArrayList<Node> oldValues=nodo.getNodesDestino(null);
        for (Node dest:oldValues){
            if (dest.getWord().getText().equals(valueW.getText())){ //Borramos solo si coincide, ya que el objeto puede estar enlazado a muchos otros nodos destino
               dp.deleteNode(dest); //Eliminamos el valor asociado (se entiende que solo hay uno)
            }
        }

        //Creamos el nuevo valor (Nodo)
        nodo=new Node(valueW); //Valor
        Node nodeValue=dp.getNode(nodo, true, true); //Fuerza la creación de un nodo nuevo
        
        //Creamos el enlace: pájaro -ser-> azul
        DVerb ser=dict.getVerb("ser").get(0);
        Link enlaceSer=dp.getLink(nodeObj, nodeValue, ser, Link.LINK_ES_CUALIDAD); //Recuperamos y/o creamos el enlace

        //Enlazamos los nodos
        dp.linkNodes(nodeObj, nodeValue, enlaceSer); //Enlazamos atributo -> valor

        //Añadimos la trazabilidad que causa este enlace
        enlaceSer.addOrigen(or);      
    }
    
    
    /**
     * Genera un fragmento de red correspondiente al Atributo que recibe como parámetro.
     * El atributo contiene el nombre de la propiedad (p.e. "color") pero NO se especificó valor (p.e. "azul"), y el objeto al que pertenece (p.e. "pájaro").
     * El fragmento de red que se creará tiene la forma:
     *    3. Si el valor es vacio, el objeto enlaza con su atributo.
     *       p.e.: pájaro -tiene-> color
     * @param dp Plano profundo de comprensión semántica.
     * @param dict Diccionario en uso.
     * @param or Oración a analizar.
     * @param atr El atributo contiene el nombre de la propiedad (p.e. "color") y su valor (p.e. "azul"), y el objeto al que pertenece (p.e. "pájaro")
     */
    public static void generateAttributeObjectChunk(DeepPlane dp, Dictionary dict, LOracion or, Attribute atr){
        
        //Obtenemos y/o creamos las palabras/términos
        DWord objectW=dict.getWord(atr.getObject(),DWord.POS_NOUN,true); //Obtenemos la palabra y/o la creamos si es necesario
        DWord attributeW=dict.getWord(atr.getName(),DWord.POS_NOUN,true); //Obtenemos la palabra y/o la creamos si es necesario
        
        //Obtenemos y/o creamos sus nodos en la Red Semántica
        Node nodo=new Node(objectW); //Objeto
        Node nodeObj=dp.getNode(nodo, true, false); //Recupera el nodo y/o lo crea
        nodo=new Node(attributeW); //Atributo
        Node nodeAtr=dp.getNode(nodo, nodeObj, true); //Crea la cualidad sólo si el objeto no la tiene; si la tiene, recupera la existente
        
        //Creamos los dos enlaces: pájaro -tener-> color
        DVerb tener=dict.getVerb("tener").get(0);
        Link enlaceTener=dp.getLink(nodeObj, nodeAtr, tener, Link.LINK_ES_CUALIDAD); //Recuperamos y/o creamos el enlace

        //Enlazamos los nodos
        dp.linkNodes(nodeObj, nodeAtr, enlaceTener); //Enlazamos objeto -> atributo

        //Añadimos la trazabilidad que causa este enlace
        enlaceTener.addOrigen(or);
    }//END generateAttributeNetChunk
            
    
    /**
     * Genera un fragmento de red correspondiente al Atributo que recibe como parámetro.
     * El atributo contiene el nombre de la propiedad (p.e. "color") y su valor (p.e. "azul"), y el objeto al que pertenece (p.e. "pájaro").
     * El fragmento de red que se creará tiene 3 formas:
     *    1. Si el nombre de la propiedad es vacío, el valor se enlaza directamente al objeto.
     *       p.e.: pájaro -es-> azul
     *    2. Si tiene nombre la propiedad, el nombre se enlaza al objeto, y el valor se enlaza al nombre. 
     *       p.e.: pájaro -tiene-> color -es-> azul
     *    3. Si el valor es vacio, el objeto enlaza con su atributo.
     *       p.e.: pájaro -tiene-> color
     *    4. Si el atributo y el valor son nulos es un error.
     * @param dp Plano profundo de comprensión semántica.
     * @param dict Diccionario en uso.
     * @param or Oración a analizar.
     * @param atr El atributo contiene el nombre de la propiedad (p.e. "color") y su valor (p.e. "azul"), y el objeto al que pertenece (p.e. "pájaro")
     */
    public static void generateAttributeNetChunk(DeepPlane dp, Dictionary dict, LOracion or, Attribute atr){
        
        //Vemos qué caso es de los 3 posibles:
        if (StringUtil.isEmpty(atr.getName())){ //Caso: pájaro -es-> azul
            linkAttributeObjectValue(dp, dict, or, atr); 
            return; //Salimos
        }else{
            if (StringUtil.isEmpty(atr.getValue())){ //Caso: pájaro -tiene-> color
                generateAttributeObjectChunk(dp, dict, or, atr);
                return;  //Salimos  
            }
        }
        
        //Obtenemos y/o creamos las palabras/términos
        DWord objectW=dict.getWord(atr.getObject(),DWord.POS_NOUN,true); //Obtenemos la palabra y/o la creamos si es necesario
        DWord attributeW=dict.getWord(atr.getName(),DWord.POS_NOUN,true); //Obtenemos la palabra y/o la creamos si es necesario
        DWord valueW=dict.getWord(atr.getValue(),DWord.POS_NOUN,true); //Obtenemos la palabra y/o la creamos si es necesario. El valor suele ser un ADJ o un NOUN
        
        //Obtenemos y/o creamos sus nodos en la Red Semántica
        Node nodo=new Node(objectW); //Objeto
        Node nodeObj=dp.getNode(nodo, true, false); //Recupera el nodo y/o lo crea
        nodo=new Node(attributeW); //Atributo
        Node nodeAtr=dp.getNode(nodo, nodeObj, true); //Crea la cualidad sólo si el objeto no la tiene; si la tiene, recupera la existente
        
        //Eliminamos el viejo valor (Nodo) asignado al atributo (si existe)
        ArrayList<Node> oldValues=nodeAtr.getNodesDestino(null);
        for (Node dest:oldValues){
            dp.deleteNode(dest); //Eliminamos el valor asociado (se entiende que solo hay uno)
        }

        //Creamos el nuevo valor (Nodo)
        nodo=new Node(valueW); //Valor
        Node nodeValue=dp.getNode(nodo, true, true); //Fuerza la creación de un nodo nuevo
        
        //Creamos los dos enlaces: pájaro -tener-> color -ser-> azul
        DVerb tener=dict.getVerb("tener").get(0);
        DVerb ser=dict.getVerb("ser").get(0);
        Link enlaceTener=dp.getLink(nodeObj, nodeAtr, tener, Link.LINK_ES_CUALIDAD); //Recuperamos y/o creamos el enlace
        Link enlaceSer=dp.getLink(nodeAtr, nodeValue, ser, Link.LINK_ES_CUALIDAD); //Recuperamos y/o creamos el enlace

        //Enlazamos los nodos
        dp.linkNodes(nodeObj, nodeAtr, enlaceTener); //Enlazamos objeto -> atributo
        dp.linkNodes(nodeAtr, nodeValue, enlaceSer); //Enlazamos atributo -> valor

        //Añadimos la trazabilidad que causa este enlace
        enlaceTener.addOrigen(or);
        enlaceSer.addOrigen(or);      
        
    }//END generateAttributeNetChunk
            
    
    
    /**
     * Analiza la presencia de una estructura copulativa en la frase y genera la estructura correspondiente dentro del Plano Profundo.
     * Asigna propiedades a un objeto (El coche es azul) y/o lo ubica en una jerarquía (El gorrión es un pájaro).
     * La agregación del tipo "La mano es parte del brazo)", no se aborda aquí, sino en la función de análisis de agregaciones.
     * AVISO: No se considera el tiempo (se considera todo como presente/vigente).
     * AVISO: Hay casos en que los adjetivos se clasifican erróneamente como sustantivos, y por ello se generan clases en lugar de atributos.
     * @param dp Plano profundo de comprensión semántica.
     * @param dict Diccionario en uso.
     * @param or Oración a analizar.
     * @return True si se procesó la oración como copulativa.
     */
    public static boolean analizeOrCopulativa(DeepPlane dp, Dictionary dict, LOracion or){
        boolean result=false;
        if (or.isOracionCopulativaSer(dict)&&!or.isOracionAgregacion(dict)){ //Si la oración es copulativa SER y no es una oración del tipo "La mano es parte del brazo" (agregación)
            result=true;
            
            LTermino raizT=or.getEstructura(); //La raiz de la oración es el atributo ADJ o nombre NOUN/PROPN que se le asigna al sujeto
            DWord raizW=dict.getWord(raizT.getTexto()); //Obtenemos el ADJ o NOUN atribuído
            raizW.addPOS(raizT.getPos()); //Añadimos el POS (sin duplicar)
            LTermino verbT=or.getAccion(); //Cogemos la acción principal (su POS está clara)
            DVerb verbD=dict.getVerb(verbT.getTexto()).get(0); //Obtenemos la primera ocurrencia de una conjugación que se corresponda con este verbo
            LTermino sujetoT=or.getSujeto(); //Cogemos el sujeto de la acción.
            DWord sujetoW=dict.getWord(sujetoT.getTexto()); //Obtenemos su palabra del diccioanrio
            sujetoW.addPOS(sujetoT.getPos()); //Añadimos el POS (sin duplicar)
            
            //Miramos si hay adjetivos adicionales que asignar
            ArrayList<LTermino> adjAdicionales=or.getElementosAdicionales(); //Obtenemos adjetivos adicionales a asignar si existen (P.e. "Juan es un tonto, un memo y un idiota").
            
            if (raizT.getPos().equals(DWord.POS_ADJ)||!adjAdicionales.isEmpty()){ //Si se está asignando un adjetivo (ADJ) al sujeto, sencillamente lo asignamos. Si hay más elementos suponemos que estos son adjetivos, no clases
                Link enlace=new Link(verbD,verbT.isNegado()); //Verbo
                enlace.addOrigen(or); //Enlazamos el origen de este conocimiento
                Node suj=new Node(sujetoW,DWord.POS_NOUN);  //Sujeto
                Node adj=new Node(raizW,DWord.POS_ADJ);   //Atributo ADJ (en este caso)
                enlace.setTipoPrimitivo(Link.LINK_ES_CUALIDAD); //Es una asignación de cualidad al sujeto
                
                Fact hecho=new Fact(enlace,suj,adj); //Creamos el hecho
                dp.loadAndLink(hecho); //Cargamos el hecho en el DeepPlane
                
                //Miramos si hay adjetivos adicionales que asignar
                for (LTermino aux:adjAdicionales){
                    DWord adjW=dict.getWordOnly(aux.getTexto()); //Vemos si está en el diccionario
                    Node adjAdd=(adjW!=null?new Node(adjW,DWord.POS_ADJ):null); //Creamos un Nodo si lo está
                    if (adjAdd!=null){
                        Link enlaceClon=enlace.clone(); //Debemos usar un nuevo enlace para no modificar el anterior.
                        enlaceClon.setNegado(aux.isNegado()); //Vemos si está negado el adjetivo para transmitirlo al verbo
                        hecho=new Fact(enlaceClon,suj,adjAdd); //Creamos el hecho
                        dp.loadAndLink(hecho); //Creamos un nuevo enlace
                    }
                }//for
                
            }else if (raizT.getPos().equals(DWord.POS_NOUN)||raizT.getPos().equals(DWord.POS_PRON)|| //Si se está indicando una relación de jerarquía
                    raizT.getPos().equals(DWord.POS_PROPNL)||raizT.getPos().equals(DWord.POS_PRON)){ 
                //En otro caso, es un NOUN (por fuerza) con un (opcional) complemento al nombre (nmod).P.e. El gorrión es un tipo(NOUN) de(CASE) pájaro(NOUN)
                Link enlace=new Link(verbD,verbT.isNegado()); //Verbo
                enlace.addOrigen(or); //Enlazamos el origen de este conocimiento
                Node suj=new Node(sujetoW,raizT.getPos());  //Sujeto, puede ser un Nombre de persona o lugar, o un sustantivo
                Node noun=new Node(raizW,DWord.POS_NOUN);   //Una clase siempre es una palabra de tipo NOUN (AVISO: puede ser un ADJ erróneamente marcado como NOUN)
                
                LTermino claseT=esTerminoClasificatorio(raizT,enlace); //Miramos si el raiz es un término clasificatorio en lugar de un ADJ
                if (claseT!=null){ //Si es una oración clasificatoria
                    DWord claseW=dict.getWord(claseT.getTexto()); //Obtenemos su palabra del diccionario
                    noun=new Node(claseW,DWord.POS_NOUN); //El nodo destino será la clase. P.e. en "El gorrión es un tipo de ave", el destino es "ave"
                    if (enlace.getTipoPrimitivo().equals(Link.LINK_ES_SUPERCLASE)){ //Si se indicó una superclase
                        Node aux=noun; //Intercambiamos origen y destino del enlace, y el tipo primitivo de la conexión
                        noun=suj;
                        suj=aux;
                        enlace.setTipoPrimitivo(Link.LINK_ES_SUBCLASE);
                    }//Si es superclase
                }
                
                Fact hecho=new Fact(enlace,suj,noun); //Creamos el hecho
                dp.loadAndLink(hecho); //Cargamos el hecho en el DeepPlane (enlace CLASE -> SUBCLASE)
                
                Link enlaceInv=enlace.clone(); //Creamos el enlace inverso
                enlaceInv.setTipoPrimitivo(Link.LINK_ES_SUPERCLASE);
                enlaceInv.addOrigen(or); //La oración causante de este enlace es la misma
                hecho=new Fact(enlaceInv,noun,suj); //Creamos el hecho
                dp.loadAndLink(hecho); //Cargamos el hecho en el DeepPlane (enlace SUBCLASE -> CLASE)
                
            }
        }//If copulativa SER
        return result;
    }
    
    
    /**
     * Cuando no existe Objeto Directo, esta funcion localiza un candidato alternativo al mismo (si existe).
     * @param arbol Termino a partir del cual se bsucará el término candidato alternativo.
     * @return Término candidato alternativo a OD, o null si no se encuentra.
     */
    private static LTermino searchAlternateOD(LTermino arbol){
        LTermino result=null;
        for (LTermino t:arbol.getHijos()){
           //El OD "útil" puede ser un complemento a otro sustantivo (parte, sección, etc) que no es el verdadero objeto, sino éste
           if (t.getUd().equals(LTermino.UD_NMOD)||t.getUd().equals(LTermino.UD_OBL)){ 
              result=t; //Guardamos el resultado    
           } 
        }//for
        return result;
    }
    
    /**
     * Cuando no existe Objeto Directo, esta funcion localiza un candidato alternativo al mismo (si existe).
     * @param or Oración que se desea analizar.
     * @return Término candidato alternativo a OD, o null si no se encuentra.
     */
    private static LTermino searchAlternateOD(LOracion or){
        return searchAlternateOD(or.getEstructura());
    }
    
    /**
     * Analiza la presencia de una estructura de agregación en la frase y genera la estructura correspondiente dentro del Plano Profundo.
     * Relaciona componentes con un objeto (El coche tiene motor) y/o lo ubica en una relación de agregación (La mano pertenece al brazo).
     * Se utilizan los verbos "pertenecer" e "incluir" para establecer estas relaciones de agregación.
     * AVISO: No se considera el tiempo (se considera todo como presente/vigente).
     * @param dp Plano profundo de comprensión semántica.
     * @param dict Diccionario en uso.
     * @param or Oración a analizar.
     * @return True si se procesó la oración como si fuera una estructura de agregación.
     */
    public static boolean analizeOrAgregacion(DeepPlane dp, Dictionary dict, LOracion or){
        boolean result=false;
        boolean esAgregado=true; //Con este flag indicaremos si el sujeto la oración es subparte/subconjunto (La mano es parte del brazo) o conjunto/agregador (El brazo incluye la mano)
        if (or.isOracionAgregacion(dict)){ //Si la oración es de agregación
            result=true; //Sabiendo que la oración expresa agregación, la respuesta es True
            //**** Verbos que usaremos en todas las relaciones de agregación ********
            DVerb pertenece=dict.getVerb("pertenece").get(0); //Cogemos el presente de pertenecer.
            DVerb incluye=dict.getVerb("incluye").get(0); //Cogemos el presente de incluir.
            
            //**** Leemos la información de entrada *******
            LTermino verbT=or.getAccion(); //VERBO: Cogemos la acción principal (su POS está clara)
            String verbo=verbT.getTexto(); //Vemos el verbo (en texto) de esta accion
            String root=or.getEstructura().getTexto(); //Vemos la raiz
            
            LTermino sujetoT=or.getSujeto(); //SUJETO: Cogemos el sujeto de la acción.
            DWord sujetoW=null;
            if (sujetoT!=null){ 
               sujetoW=dict.getWord(sujetoT.getTexto()); //Obtenemos su palabra del diccioanrio
               sujetoW.addPOS(sujetoT.getPos()); //Añadimos el POS (sin duplicar). Esto es para ir completando los POS de las palabras genéricas del diccionario (que están incompletos).
            }
            
            LTermino objetoDirectoT=or.getObjetoDirecto(); //OBJETO DIRECTO: Obtenemos el Objeto Directo, que es el primer objeto agregado del sujeto, o al que se agrega el sujeto
            //Cuando el verbo es AUX el OD es null, por tanto buscamos un OD alternativo bajo el raiz.
            objetoDirectoT=(objetoDirectoT==null?searchAlternateOD(or.getEstructura()):objetoDirectoT);
            DWord objetoDirectoW=(objetoDirectoT!=null?dict.getWord(objetoDirectoT.getTexto()):null); //Obtenemos su palabra del diccionario (si existe OD, que no tiene por qué)

            //**** Vemos que tipo de agregación es *********
            if (!dict.getVerb(verbo).isEmpty()){
                DVerb conj=dict.getVerb(verbo).get(0); //Cogemos la primera conjugacion coincidente. AVISO: no tiene en cuenta la concordancia (y habría que contemplarla)
                DVerb inf=conj.getVerboInfinitivo(); //Cogemos el infinitivo de la conjugación

                if (inf.getText().equals("ser")||inf.getText().equals("estar")){
                    if (SpanishDictionary.isAgregatorWord(root)){ //Frase tipo "El brazo es una agregación de mano y antebrazo", en lugar de "La mano es una parte del brazo". Aquí root="parte" o root="agregación".)
                       esAgregado=false; //Pues la oración es de tipo "agregador". P.e. "El brazo está formado por el antebrazo y la mano"
                    }
                }else

                if (inf.getText().equals("componer")){ //Si la frase el del tipo "El brazo se compone de mano y codo".
                    for (LTermino term:or.getAccion().getHijos()){
                        if (term.getTexto().equals("se")){
                            esAgregado=false; //Pues la oración es de tipo "agregador".
                        }
                    }//for
                }else

                //Frases del tipo "La mano tiene dedos", o "Juan tiene piernas". 
                if (inf.getText().equals("tener")){
                    //Comprobamos si el sujeto es un nombre de persona, lugar o pronombre personal (ojo con el PRON "ello") y el verbo sea "tener"
                    if (sujetoT!=null&&((sujetoT.getPos().equals(DWord.POS_PROPN)|| 
                        (sujetoT.getPos().equals(DWord.POS_PRON)&&!sujetoT.getTexto().equals("ello"))||
                         sujetoT.getPos().equals(DWord.POS_PROPNL)))){
                        //En el caso de personas, sólo se podría determinar si algo es una parte del cuerpo mediante ontología. Igualmente hay que diferenciar posesión ("Juan tiene una moto") de adjetivo ("Juan tiene frío").
                    }else{
                        //Si es un objeto, es claramente una composición.
                        esAgregado=false; //Pues la oración es de tipo "agregador".
                    }
                }
                    
                //**** En este punto ya sabemos si el sujeto es agregador o agregado, y el OD es el elemento agregado o agregador, respectivamente *****
                //**** Por tanto, creamos los fragmentos de red *******
            
                //Miramos si hay elementos adicionales que asignar
                ArrayList<LTermino> adjAdicionales=or.getElementosAdicionales(); //Obtenemos elementos adicionales a asignar si existen (P.e. "El brazo está formado por antebrazo, muñeca y mano.").

                Link enlace=null;
                Link enlaceInv=null;
                Node suj=null;
                
                if (!esAgregado){ //Creamos Sujeto->Es_agregación_de->Objeto. "El brazo se compone de antebrazo y codo."
                    enlace=new Link(incluye,verbT.isNegado()); //Creamos el enlace con el Verbo "incluir"
                    enlaceInv=new Link(pertenece,verbT.isNegado()); //Creamos el enlace inverso con el Verbo "pertenecer"
                    enlace.setTipoPrimitivo(Link.LINK_ES_AGREGACION); //Suj->incluye->OD
                    enlaceInv.setTipoPrimitivo(Link.LINK_ES_PARTE); //OD->pertenece->Suj
                    enlace.addOrigen(or); //Enlazamos el texto origen de este conocimiento
                    enlaceInv.addOrigen(or); //Enlazamos el texto origen de este conocimiento
                    suj=new Node(sujetoW,(sujetoT!=null?sujetoT.getPos():null));  //Sujeto (sustantivo, nombre de localización, etc)
                    Node odn=new Node(objetoDirectoW,objetoDirectoT.getPos());   //OD (sustantivo, nombre de localización, etc)

                    Fact hecho=new Fact(enlace,suj,odn); //Creamos el hecho
                    dp.loadAndLink(hecho); //Cargamos el hecho Suj->incluye->OD en el DeepPlane
                    hecho=new Fact(enlaceInv,odn,suj); //Creamos el hecho
                    dp.loadAndLink(hecho); //Cargamos el hecho OD->pertenece->Suj en el DeepPlane
                    
                }else{ //Creamos Sujeto->Es_parte_de->Objeto. "La mano forma parte del brazo".
                    enlace=new Link(pertenece,verbT.isNegado()); //Creamos el enlace con el Verbo "pertenecer"
                    enlaceInv=new Link(incluye,verbT.isNegado()); //Creamos el enlace inverso con el Verbo "incluir"
                    enlace.setTipoPrimitivo(Link.LINK_ES_PARTE); //Suj->pertenece->OD
                    enlaceInv.setTipoPrimitivo(Link.LINK_ES_AGREGACION); //OD->incluye->Suj
                    enlace.addOrigen(or); //Enlazamos el texto origen de este conocimiento
                    enlaceInv.addOrigen(or); //Enlazamos el texto origen de este conocimiento
                    suj=new Node(sujetoW,(sujetoT!=null?sujetoT.getPos():null));  //Sujeto (sustantivo, nombre de localización, etc)
                    Node odn=new Node(objetoDirectoW,objetoDirectoT.getPos());   //OD (sustantivo, nombre de localización, etc)

                    Fact hecho=new Fact(enlace,suj,odn); //Creamos el hecho
                    dp.loadAndLink(hecho); //Cargamos la estructura Suj->pertenece->OD en el DeepPlane
                    hecho=new Fact(enlaceInv,odn,suj); //Creamos el hecho
                    dp.loadAndLink(hecho); //Cargamos la estructura OD->incluye->Suj en el DeepPlane
                }

                //El tratamiento de los elementos adicionales es el mismo en los dos casos anteriores.
                //Miramos si hay elementos adicionales que agregar. En caso de que los haya, realizamos la misma operación.
                for (LTermino aux:adjAdicionales){
                    DWord elemW=dict.getWordOnly(aux.getTexto()); //Vemos si está en el diccionario
                    Node elemAdd=(elemW!=null?new Node(elemW,aux.getPos()):null); //Creamos un Nodo si lo está
                    if (elemAdd!=null){
                        Link enlaceClon=enlace.clone(); //Debemos usar un nuevo enlace para no modificar el anterior.
                        enlaceClon.setNegado(aux.isNegado()); //Vemos si está negado el adjetivo para transmitirlo al verbo
                        Fact hecho=new Fact(enlaceClon,suj,elemAdd); //Creamos el hecho
                        dp.loadAndLink(hecho); //Cargamos la estructura Suj->incluye->OD en el DeepPlane

                        Link enlaceInvClon=enlaceInv.clone(); //Debemos usar un nuevo enlace para no modificar el anterior.
                        enlaceInvClon.setNegado(aux.isNegado()); //Vemos si está negado el adjetivo para transmitirlo al verbo
                        hecho=new Fact(enlaceInvClon,elemAdd,suj); //Creamos el hecho
                        dp.loadAndLink(hecho); //Cargamos la estructura OD->pertenece->Suj en el DeepPlane
                    }
                }//for elementos adicionales
                
            }//If existe el verbo
        } //If is oracion agregante
        return result;
    }
    
    
    /**
     * Pregunta (no aseción, cuidado) sobre la veracidad de una aserción (oración agregación). P.e. "La mano se compone de palma, dedos y uñas.".
     * AVISO: En caso de ser necesario depurar esta funcionalidad, es conveniente apoyarse en tablas de verdad para no perderse.
     * @param dp Plano profundo de comprensión semántica.
     * @param dict Diccionario en uso.
     * @param or Oración a analizar.
     * @return True si la aserción es cierta, false si es falsa, null si no hay datos suficientes.
     */
    public static Booleano evalOrAgregacion(DeepPlane dp, Dictionary dict, LOracion or){
        Booleano result=new Booleano(false);
        boolean esAgregado=true; //Con este flag indicaremos si el sujeto la oración es subparte/subconjunto (La mano es parte del brazo) o conjunto/agregador (El brazo incluye la mano)
        if (or.isOracionAgregacion(dict)){ //Si la oración es de agregación
            
            //**** Leemos la información de entrada *******
            LTermino verbT=or.getAccion(); //Cogemos la acción principal (su POS está clara)
            String verbo=verbT.getTexto(); //Vemos el verbo (en texto) de esta accion
            String root=or.getEstructura().getTexto(); //Vemos la raiz
            LTermino sujetoT=or.getSujeto(); //Cogemos el sujeto de la acción.
            DWord sujetoW=dict.getWord(sujetoT.getTexto()); //Obtenemos su palabra del diccioanrio
            sujetoW.addPOS(sujetoT.getPos()); //Añadimos el POS (sin duplicar). Esto es para ir completando los POS de las palabras genéricas del diccionario (que están incompletos).
            LTermino objetoDirectoT=or.getObjetoDirecto(); //Obtenemos el Objeto Directo, que es el primer objeto agregado del sujeto, o al que se agrega el sujeto
            //Cuando el verbo es AUX el OD es null, por tanto buscamos un OD alternativo bajo el raiz.
            objetoDirectoT=(objetoDirectoT==null?searchAlternateOD(or.getEstructura()):objetoDirectoT);

            //**** Vemos que tipo de agregación es *********
            if (!dict.getVerb(verbo).isEmpty()){
                DVerb conj=dict.getVerb(verbo).get(0); //Cogemos la primera conjugacion coincidente. AVISO: no tiene en cuenta la concordancia (y habría que contemplarla)
                DVerb inf=conj.getVerboInfinitivo(); //Cogemos el infinitivo de la conjugación

                if (inf.getText().equals("ser")||inf.getText().equals("estar")){
                    if (SpanishDictionary.isAgregatorWord(root)){ //Frase tipo "El brazo es una agregación de mano y antebrazo", en lugar de "La mano es una parte del brazo". Aquí root="parte" o root="agregación".)
                       esAgregado=false; //Pues la oración es de tipo "agregador". P.e. "El brazo está formado por el antebrazo y la mano"
                    }
                }else

                if (inf.getText().equals("componer")){ //Si la frase el del tipo "El brazo se compone de mano y codo".
                    for (LTermino term:or.getAccion().getHijos()){
                        if (term.getTexto().equals("se")){
                            esAgregado=false; //Pues la oración es de tipo "agregador".
                        }
                    }//for
                }else
                    
                //Frases del tipo "La mano tiene dedos", o "Juan tiene piernas". 
                if (inf.getText().equals("tener")){
                    //Comprobamos si el sujeto es un nombre de persona, lugar o pronombre personal (ojo con el PRON "ello") y el verbo sea "tener"
                    if ((sujetoT.getPos().equals(DWord.POS_PROPN)|| 
                        (sujetoT.getPos().equals(DWord.POS_PRON)&&!sujetoT.getTexto().equals("ello"))||
                         sujetoT.getPos().equals(DWord.POS_PROPNL))){
                        //En el caso de personas, sólo se podría determinar si algo es una parte del cuerpo mediante ontología. Igualmente hay que diferenciar posesión de un objeto ("Juan tiene una moto") de adjetivo/atributo ("Juan tiene frío" o "Juan tiene ansiedad").
                    }else{
                        //Si es un objeto, es claramente una composición.
                        esAgregado=false; //La oración es de tipo "agregador".
                    }
                }
                    

                //Miramos si se están considerando varios elementos en el Sintagma Verbal (SV)
                ArrayList<String> componentes=NetUtil.toStringArrayT(or.getElementosAdicionales()); //Obtenemos elementos adicionales a asignar si existen (P.e. "El brazo está formado por antebrazo, muñeca y mano.").
                componentes.add(objetoDirectoT.getTexto()); //Añadimos el OD para tenerlos todos en la lista de componentes
                
                
                ArrayList<Node> comps=null; //Componentes o agregadores
                //**** En este punto ya sabemos si el sujeto es agregador o agregado, y el OD es el elemento agregado o agregador, respectivamente *****
                if (!esAgregado){ //Buscamos Sujeto->Es_agregación_de->Objeto. "El brazo se compone de antebrazo, mano y codo."
                    comps=getComponentes(sujetoT.getTexto(),dp); //Listado de componentes en el DP de este término
                }else{  //Buscamos Sujeto->Es_parte_de->Objeto. "Los dedos forman parte de la mano."
                    comps=getAgregadores(sujetoT.getTexto(),dp); //Listado de agregadores en el DP de este término
                }
                ArrayList<Node> compPurgado=NetUtil.purgeArrayN(verbT.isNegado(),comps); //Eliminamos de la lista los elementos/agregadores que correspondan, en base a la negación (o afirmación) de cada uno de ellos
                ArrayList<String> componentesDP=NetUtil.toStringArrayN(compPurgado); //Listado de componentes/agregadores purgados considerando el sentido de la negación de la pregunta
                boolean presentes=NetUtil.isAllPresent(componentes, componentesDP); //Vemos si están todos presentes en el DP
                result=new Booleano(presentes);
                
                
            }//Si hay verbo
            
        }//Si es agregación

        return result;
    }
    
    
    /**
     * Pregunta (no aseción, cuidado) sobre la veracidad de una aserción (oración copulativa). P.e. "Juan es guapo".
     * AVISO: En caso de ser necesario depurar esta funcionalidad, es conveniente apoyarse en tablas de verdad para no perderse.
     * @param dp Plano profundo de comprensión semántica.
     * @param dict Diccionario en uso.
     * @param or Oración a analizar.
     * @return True si la aserción es cierta, false si es falsa, null si no hay datos suficientes.
     */
    public static Booleano evalOrCopulativa(DeepPlane dp, Dictionary dict, LOracion or){
        Booleano result=new Booleano(false);
        if (or.isOracionCopulativaSer(dict)&&!or.isOracionAgregacion(dict)){ //Si la oración es copulativa SER y no es una oración del tipo "La mano es parte del brazo" (agregación)
            
            LTermino raizT=or.getEstructura(); //La raiz de la oración es el atributo ADJ o nombre NOUN/PROPN que se le asigna al sujeto
            DWord raizW=dict.getWord(raizT.getTexto()); //Obtenemos el ADJ o NOUN atribuído
            LTermino verbT=or.getAccion(); //Cogemos la acción principal
            DVerb verbD=dict.getVerb(verbT.getTexto()).get(0); //Obtenemos el verbo
            LTermino sujetoT=or.getSujeto(); //Cogemos el sujeto de la acción.
            DWord sujetoW=dict.getWord(sujetoT.getTexto()); //Obtenemos su palabra del diccioanrio
            
            //*** 1. En primer lugar evaluamos si es una relación clase-subclase o clase-superclase
            if (isSubclass(sujetoW.getText(),raizW.getText(),dp)){
                result=new Booleano(true); //P.e. "El gorrión(SUJ-subclase) es un ave(RAIZ-superclase)"
                if (verbT.isNegado()){ //Si la pregunta fue negada, invertimos el resultado
                    result=new Booleano(!result.isBool());
                }
                return result; //Salimos
            }else{
                if (isSuperclass(sujetoW.getText(),raizW.getText(),dp)){
                    result=new Booleano(false); //P.e. "El ave(SUJ-subclase) es un gorrión(RAIZ-superclase)"
                    if (verbT.isNegado()){ //Si la pregunta fue negada, invertimos el resultado
                        result=new Booleano(!result.isBool());
                    }
                    return result; //Salimos
                }
                //En otro caso ambas palabras no forman parte de la jerarquía
            }
            
            
            //*** 2. Buscamos ese patrón exacto en la red semántica
            String question=NetUtil.generateLinkMapId(sujetoW.getText(),verbD.getText(),raizW.getText());
            Link enlace=dp.getLink(question); //Obtenemos el enlace
            if (enlace!=null){ //Si se encuentra 
               if (enlace.isNegado()==verbT.isNegado()){ //y tiene el mismo sentido en relación a la negación
                    result=new Booleano(true); //Si se encuentra => la respuesta es verdadera, en otro caso es desconocida
               }else{ //Si el sentido de la negación no coincide, es que las declaraciones son opuestas
                    result=new Booleano(false); //Por tanto es falso
               }//if negacion
            }else{
               result=null; //No concluyente
            }
            
            //*** 3. En caso de que no se encuentre, buscamos la veracidad a través de los sinónimos
            if (result==null){
                for (DWord sin:raizW.getSinonimos()){ //Para cada sinónimo
                    question=NetUtil.generateLinkMapId(sujetoW.getText(),verbD.getText(),sin.getText());  //Buscamos ese patrón en la red
                    enlace=dp.getLink(question);
                    if (enlace!=null){ //Si hay una coincidencia la respuesta es True
                        if (enlace.isNegado()==verbT.isNegado()){ //y tiene el mismo sentido en relación a la negación
                             result=new Booleano(true); //Si se encuentra => la respuesta es verdadera, en otro caso es desconocida
                        }else{ //Si el sentido de la negación no coincide, es que las declaraciones son opuestas
                             result=new Booleano(false); //Por tanto es falso
                        }//if negacion
                    }
                }//for
            }
            
            //*** 4. En caso de que no se encuentre, buscamos la falsedad a través de los antónimos
            if (result==null){
                for (DWord ant:raizW.getAntonimos()){ //Para cada antónimo
                    question=NetUtil.generateLinkMapId(sujetoW.getText(),verbD.getText(),ant.getText());  //Buscamos ese patrón en la red
                    enlace=dp.getLink(question);
                    if (enlace!=null){ //Si hay una coincidencia: la respuesta es False porque se afirma lo contrario a lo establecido en la red
                        if (enlace.isNegado()!=verbT.isNegado()){ //Como hemos hallado la afirmación contaria: si la pregunta es opuesta, ambas afirmaciones están de acuerdo.
                            result=new Booleano(true); //Entonces ambas afirmaciones coinciden => la respuesta es afirmativa
                        }else{ //Si el sentido de la negación coincide, es que las declaraciones son opuestas
                             result=new Booleano(false); //Por tanto es falso
                        }//if negacion
                    }
                }//for
            }
            
        }//If copulativa SER
        return result;
    }
    

    /**
     * Genera una cadena de texto reflejando el árbol de componentes en jerarquía descendente, a partir del nodo especificado.
     * @param nod Nodo de partida, a partir del cual se exploran los enlaces salientes (componentes).
     * @param dp DeepPlane con la red semántica.
     * @param nivel Nivel de profundidad en que estamos.
     * @param idsVisitados Lista de IDs de Nodes ya visitados, para evitar bucles infinitos.
     * @return Cadena con la representación textual del árbol.
     */
    private static String strComponentes(Node nod, DeepPlane dp, int nivel, HashMap idsVisitados){
        String result="";
        String id=(String)idsVisitados.get(nod.getId());
        if (id==null){ //Si no hemos visitado este nodo aún (si ya fue visitado, devolvemos cadena vacía)
            idsVisitados.put(nod.getId(),nod.getId()); //Guardamos el ID como visitado
            result=StringUtil.padding(nivel)+"["+nivel+"] "+nod.getWord().getText()+"\n";
            ArrayList<Node> nodos=dp.getNodesDestino(nod,Link.LINK_ES_AGREGACION); //Obtenemos todas sus elementos agregados
            for (Node n:nodos){
                result+=strComponentes(n,dp,(nivel+1),idsVisitados); //Buceamos en los nodos destino
            }//for
        }//if no fue visitado
        return result;
    }
    

    /**
     * Genera una cadena de texto reflejando el árbol de agregadores, a partir del nodo especificado.
     * @param nod Nodo de partida, a partir del cual se exploran los enlaces salientes (componentes).
     * @param dp DeepPlane con la red semántica.
     * @param nivel Nivel de profundidad en que estamos.
     * @param idsVisitados Lista de IDs de Nodes ya visitados, para evitar bucles infinitos.
     * @return Cadena con la representación textual del árbol.
     */
    private static String strAgregadores(Node nod, DeepPlane dp, int nivel, HashMap idsVisitados){
        String result="";
        String id=(String)idsVisitados.get(nod.getId());
        if (id==null){ //Si no hemos visitado este nodo aún (si ya fue visitado, devolvemos cadena vacía)
            idsVisitados.put(nod.getId(),nod.getId()); //Guardamos el ID como visitado
            result=StringUtil.padding(nivel)+"["+nivel+"] "+nod.getWord().getText()+"\n";
            ArrayList<Node> nodos=dp.getNodesDestino(nod,Link.LINK_ES_PARTE); //Obtenemos todas sus elementos agregadores
            for (Node n:nodos){
                result+=strAgregadores(n,dp,(nivel+1),idsVisitados); //Buceamos en los nodos destino
            }//for
        }//if no fue visitado
        return result;
    }
    
    
    /**
     * Genera una cadena de texto reflejando el árbol de jerarquía descendente, a partir del nodo especificado.
     * @param nod Nodo de partida, a partir del cual se exploran los enlaces salientes (subclase).
     * @param dp DeepPlane con la red semántica.
     * @param nivel Nivel de profundidad en que estamos.
     * @param idsVisitados Lista de IDs de Nodes ya visitados, para evitar bucles infinitos.
     * @return Cadena con la representación textual del árbol.
     */
    private static String strJerarquiaDescendente(Node nod, DeepPlane dp, int nivel, HashMap idsVisitados){
        String result="";
        String id=(String)idsVisitados.get(nod.getId());
        if (id==null){ //Si no hemos visitado este nodo aún (si ya fue visitado, devolvemos cadena vacía)
            idsVisitados.put(nod.getId(),nod.getId()); //Guardamos el ID como visitado
            result=StringUtil.padding(nivel)+"("+nivel+") "+nod.getWord().getText()+"\n";
            ArrayList<Node> nodos=dp.getNodesDestino(nod,Link.LINK_ES_SUPERCLASE); //Obtenemos todas sus subclases: dame todos los nodos de los que este es superclase
            for (Node n:nodos){
                result+=strJerarquiaDescendente(n,dp,(nivel+1),idsVisitados); //Buceamos en los nodos destino
            }//for
        }//if no fue visitado
        return result;
    }


    /**
     * Obtiene una cadena de texto con el valor de la cualidad cuyo nombre se especifica, y que pertenece al objeto especificado.La cualidad debe ser del tipo: pájaro -tiene-> color -es-> azul; porque no es resoluble el caso del tipo: pájaro -es-> azul
     * @param objeto Sustantivo/nombre/lugar especificado, del que se desea obtener el valor de una cualidad.
     * @param nombreCualidad Nombre de la cualidad/propiedad de la que se desea obtener el valor.
     * @param dp Red Semántica
     * @return Texto con el valor de la cualidad
     */
    public static String getCualidad(String objeto, String nombreCualidad, DeepPlane dp){
        String result="";
        ArrayList<Node> cualidades= dp.getCualidades(objeto); //Siempre se parte de un nombre textual, p.e. "coche" (Dame las cualidades de un coche)
        //La variable "cualidades" tiene ahora la lista de nodos directamente enlazados al sustantivo que son atributos del mismo (color, peso, etc) o adjetivos directamente (azul, grande, listo, etc).
        int i=1;
        for (Node n:cualidades){ //Para cada una de las cualidades encontradas
            if (n.getTipo().equals(DWord.POS_NOUN) && n.getWord().getText().equals(nombreCualidad)){ //Si la cualidad tiene el nombre especificado y es del tipo: pájaro -tiene-> color -es-> azul
                ArrayList<Node> subCualidades= dp.getCualidadesByID(n.getId()); //Recuperamos las cualidades de ese Nodo concreto
                for (Node nsub:subCualidades){
                    result=StringUtil.negado(!nsub.isNegado())+nsub.getWord().getText();
                }//for subcualidades
            }  
            i++;
        }//for

        return result;
    }



    
    /**
     * Genera una cadena de texto reflejando el árbol de jerarquía descendente SNOMED, a partir del nodo especificado.
     * @param nod Nodo de partida, a partir del cual se exploran los enlaces salientes (subclase).
     * @param dp DeepPlane con la red semántica.
     * @param nivel Nivel de profundidad en que estamos.
     * @param idsVisitados Lista de IDs de Nodes ya visitados, para evitar bucles infinitos.
     * @return Cadena con la representación textual del árbol.
     */
    private static String strJerarquiaDescendenteSNOMED(Node nod, DeepPlane dp, int nivel, HashMap idsVisitados){
        String result="";
        String id=(String)idsVisitados.get(nod.getId());
        if (id==null){ //Si no hemos visitado este nodo aún (si ya fue visitado, devolvemos cadena vacía)
            idsVisitados.put(nod.getId(),nod.getId()); //Guardamos el ID como visitado
            String sctid=getCualidad(nod.getWord().getText(), "sctid", dp);
            result=StringUtil.padding(nivel)+"("+nivel+") [SCTID: "+sctid+"] "+nod.getWord().getText()+"\n";
            ArrayList<Node> nodos=dp.getNodesDestino(nod,Link.LINK_ES_SUPERCLASE); //Obtenemos todas sus subclases: dame todos los nodos de los que este es superclase
            for (Node n:nodos){
                result+=strJerarquiaDescendenteSNOMED(n,dp,(nivel+1),idsVisitados); //Buceamos en los nodos destino
            }//for
        }//if no fue visitado
        return result;
    }
    
    /**
     * Genera una cadena de texto reflejando el árbol de jerarquía ascendente, a partir del nodo especificado.
     * @param nod Nodo de partida, a partir del cual se exploran los enlaces salientes (superclase).
     * @param dp DeepPlane con la red semántica.
     * @param nivel Nivel de profundidad en que estamos.
     * @param idsVisitados Lista de IDs de Nodes ya visitados, para evitar bucles infinitos.
     * @return Cadena con la representación textual del árbol.
     */
    private static String strJerarquiaAscendente(Node nod, DeepPlane dp, int nivel, HashMap idsVisitados){
        String result="";
        String id=(String)idsVisitados.get(nod.getId());
        if (id==null){ //Si no hemos visitado este nodo aún (si ya fue visitado, devolvemos cadena vacía)
            idsVisitados.put(nod.getId(),nod.getId()); //Guardamos el ID como visitado
            result=StringUtil.padding(nivel)+"("+nivel+") "+nod.getWord().getText()+"\n";
            ArrayList<Node> nodos=dp.getNodesDestino(nod,Link.LINK_ES_SUBCLASE); //Obtenemos todas sus superclases: dame todos los nodos de los que este es subclase
            for (Node n:nodos){
                result+=strJerarquiaAscendente(n,dp,(nivel+1),idsVisitados); //Buceamos en los nodos destino
            }//for
        }//if no fue visitado
        return result;
    }
    
    
    /**
     * Genera una cadena de texto reflejando la jerarquía descendente de los componentes de un objeto/concepto.
     * Si el objeto/concepto no tiene componentes, se devuelve la propia palabra.
     * @param word Parabra de la que se desea obtener la jerarquía descendente de componentes (si la tiene).
     * @param dp DeepPlane donde se buscará la jerarquía de la palabra especificada.
     * @return String con una representación del arbol descendente de la jerarquía de componentes de la palabra indicada.
     */
    public static String printComponentes(String word, DeepPlane dp){
        String result="";
        HashMap idsVisitados=new HashMap(); //Para registrar los ids de los nodos que se van visitando y detectar bucles
        ArrayList<Node> objs=dp.getNode(word); //Obtenemos todos los nodos concordantes
        for (Node nod:objs){ //Para cada uno de los nodos, obtenemos su árbol de componentes, y los vamos concatenando
            result+=strComponentes(nod,dp,0,idsVisitados);
        }//for
        return result;
    }
    
    
    /**
     * Genera una cadena de texto reflejando la jerarquía los objetos que tienen al esecificado como componente.
     * Si el objeto/concepto no tiene elementos agregadores, se devuelve la propia palabra.
     * @param word Parabra de la que se desea obtener la jerarquía de agregadores (si la tiene).
     * @param dp DeepPlane donde se buscará la jerarquía de la palabra especificada.
     * @return String con una representación del arbol de la jerarquía de agregadores de la palabra indicada.
     */
    public static String printAgregadores(String word, DeepPlane dp){
        String result="";
        HashMap idsVisitados=new HashMap(); //Para registrar los ids de los nodos que se van visitando y detectar bucles
        ArrayList<Node> objs=dp.getNode(word); //Obtenemos todos los nodos concordantes
        for (Node nod:objs){ //Para cada uno de los nodos, obtenemos su árbol de agregadores, y los vamos concatenando
            result+=strAgregadores(nod,dp,0,idsVisitados);
        }//for
        return result;
    }


    /**
     * Genera una cadena de texto reflejando la jerarquía descendente de un objeto/concepto (y/o tipo de objeto/concepto).
     * Si el objeto/concepto no tiene jerarquía, se devuelve la propia palabra.
     * @param word Parabra de la que se desea obtener la jerarquía descendente (si la tiene).
     * @param dp DeepPlane donde se buscará la jerarquía de la palabra especificada.
     * @return String con una representación del arbol descendente de la jerarquía de la palabra indicada.
     */
    public static String printJerarquiaDescendente(String word, DeepPlane dp){
        String result="";
        HashMap idsVisitados=new HashMap(); //Para registrar los ids de los nodos que se van visitando y detectar bucles
        ArrayList<Node> objs=dp.getNode(word); //Obtenemos todos los nodos concordantes
        for (Node nod:objs){ //Para cada uno de los nodos, obtenemos su árbol de subclases, y los vamos concatenando
            result+=strJerarquiaDescendente(nod,dp,0,idsVisitados);
        }//for
        return result;
    }
    

    /**
     * Genera una cadena de texto reflejando la jerarquía descendente de SNOMED CT.
     * @param word Parabra de la que se desea obtener la jerarquía descendente (si la tiene).
     * @param dp DeepPlane donde se buscará la jerarquía de la palabra especificada.
     * @return String con una representación del arbol descendente de la jerarquía de la palabra indicada.
     */
    public static String printJerarquiaDescendenteSNOMED(String word, DeepPlane dp){
        String result="";
        HashMap idsVisitados=new HashMap(); //Para registrar los ids de los nodos que se van visitando y detectar bucles
        ArrayList<Node> objs=dp.getNode(word); //Obtenemos todos los nodos concordantes
        for (Node nod:objs){ //Para cada uno de los nodos, obtenemos su árbol de subclases, y los vamos concatenando
            result+=strJerarquiaDescendenteSNOMED(nod,dp,0,idsVisitados);
        }//for
        return result;
    }
    

    /**
     * Genera una cadena de texto reflejando la jerarquía ascendente de un objeto/concepto (y/o tipo de objeto/concepto).
     * Si el objeto/concepto no tiene jerarquía, se devuelve la propia palabra.
     * @param word Parabra de la que se desea obtener la jerarquía ascendente (si la tiene).
     * @param dp DeepPlane donde se buscará la jerarquía de la palabra especificada.
     * @return String con una representación del arbol ascendente de la jerarquía de la palabra indicada.
     */
    public static String printJerarquiaAscendente(String word, DeepPlane dp){
        String result="";
        HashMap idsVisitados=new HashMap(); //Para registrar los ids de los nodos que se van visitando y detectar bucles
        ArrayList<Node> objs=dp.getNode(word); //Obtenemos todos los nodos concordantes
        for (Node nod:objs){ //Para cada uno de los nodos, obtenemos su árbol de superclases, y los vamos concatenando
            result+=strJerarquiaAscendente(nod,dp,0,idsVisitados);
        }//for
        return result;
    }

    /**
     * Genera un listado con los nodos que forman parte de la jerarquía ascendente, a partir del nodo especificado.
     * @param nod Nodo de partida, a partir del cual se exploran los enlaces salientes (superclase).
     * @param dp DeepPlane con la red semántica.
     * @param nivel Nivel de profundidad en que estamos.
     * @param idsVisitados Lista de IDs de Nodes ya visitados, para evitar bucles infinitos.
     * @return Listado con los Nodos superclase del especificado.
     */
    private static ArrayList<Node> jerarquiaAscendente(Node nod, DeepPlane dp, int nivel, HashMap idsVisitados){
        ArrayList<Node> result=new ArrayList();
        String id=(String)idsVisitados.get(nod.getId());
        if (id==null){ //Si no hemos visitado este nodo aún (si ya fue visitado, devolvemos cadena vacía)
            idsVisitados.put(nod.getId(),nod.getId()); //Guardamos el ID como visitado
            if (!nod.isNegado()){ //Si el nodo está negado => camino cortado (P.e. caso de "El mono no es un hombre")
                result.add(nod); //Y añadimos el nodo a la lista
                ArrayList<Node> nodos=dp.getNodesDestino(nod,Link.LINK_ES_SUBCLASE); //Obtenemos todas sus superclases: dame todos los nodos de los que este es subclase
                for (Node n:nodos){
                    result.addAll(jerarquiaAscendente(n,dp,nivel+1,idsVisitados)); //Buceamos en los nodos destino
                }//for
            }//If nodo negado
        }//if no fue visitado
        return result;
    }
    

    /**
     * Genera un listado con los nodos que forman parte de la jerarquía descendente, a partir del nodo especificado.
     * Los nodos pueden estar negados o no (en función de la negación el link).
     * @param nod Nodo de partida, a partir del cual se exploran los enlaces salientes (subclase).
     * @param dp DeepPlane con la red semántica.
     * @param nivel Nivel de profundidad en que estamos.
     * @param idsVisitados Lista de IDs de Nodes ya visitados, para evitar bucles infinitos.
     * @return Listado con los Nodos subclase del especificado.
     */
    private static ArrayList<Node> jerarquiaDescendente(Node nod, DeepPlane dp, int nivel, HashMap idsVisitados){
        ArrayList<Node> result=new ArrayList();
        String id=(String)idsVisitados.get(nod.getId());
        if (id==null){ //Si no hemos visitado este nodo aún (si ya fue visitado, devolvemos cadena vacía)
            idsVisitados.put(nod.getId(),nod.getId()); //Guardamos el ID como visitado
            if (!nod.isNegado()){ //Si el nodo está negado => camino cortado (P.e. caso de "El mono no es un hombre")
                result.add(nod); //Y añadimos el nodo a la lista
                ArrayList<Node> nodos=dp.getNodesDestino(nod,Link.LINK_ES_SUPERCLASE); //Obtenemos todas sus subclases: dame todos los nodos de los que este es superclase
                for (Node n:nodos){
                    result.addAll(jerarquiaDescendente(n,dp,nivel+1,idsVisitados)); //Buceamos en los nodos destino
                }//for
            }//If no negado
        }//if no fue visitado
        return result;
    }
    

    /**
     * Obtiene el listado de las superclases de este objeto/concepto. AVISO: Se analiza solo uno de los nodos
     * coincidentes (acepciones) con la palabra especificada (el último).
     * @param word Palabra de la que se desea obtener la jerarquía ascendente.
     * @param dp DeepPlane donde se buscará la jerarquía de la palabra especificada.
     * @return Lista de nodos pertenecientes a la jerarquía ascendente de la palabra indicada.
     */
    public static ArrayList<Node> getJerarquiaAscendente(String word, DeepPlane dp){
        ArrayList<Node> result=new ArrayList();
        HashMap idsVisitados=new HashMap(); //Para registrar los ids de los nodos que se van visitando y detectar bucles
        ArrayList<Node> objs=dp.getNode(word); //Obtenemos todos los nodos concordantes
        for (Node nod:objs){ //Para cada uno de los nodos, obtenemos su árbol de superclases
            result=jerarquiaAscendente(nod,dp,0,idsVisitados); //Machacamos la variable (solo se analizará la última acepción)
        }//for
        return result;
    }
    
    /**
     * Indica si una clase especificada, es superclase del objeto indicado.
     * @param clase Clase de objeto.
     * @param objeto Objeto del que se desea conocer si es superclase.
     * @param dp DeepPlane
     * @return True si la clase es superclase del objeto, false en caso contrario.
     */
    public static boolean isSuperclass(String clase, String objeto, DeepPlane dp){
        ArrayList<Node> superclases=getJerarquiaAscendente(objeto,dp); //Obtenemos las superclases
        boolean result=NetUtil.isPresentNode(clase, superclases); //Buscamos si la clase es superclase del objeto
        return result;
    }
    
    /**
     * Indica si una clase especificada, es subclase del objeto indicado.
     * @param clase Clase de objeto.
     * @param objeto Objeto del que se desea conocer si es subclase.
     * @param dp DeepPlane
     * @return True si la clase es subclase del objeto, false en caso contrario.
     */
    public static boolean isSubclass(String clase, String objeto, DeepPlane dp){
        ArrayList<Node> subclases=getJerarquiaDescendente(objeto,dp); //Obtenemos las superclases
        boolean result=NetUtil.isPresentNode(clase, subclases); //Buscamos si la clase es superclase del objeto
        return result;
    }
    

    /**
     * Obtiene el listado de las subclases de este objeto/concepto. AVISO: Se analiza solo uno de los nodos
     * coincidentes (acepciones) con la palabra especificada (el último).
     * @param word Palabra de la que se desea obtener la jerarquía descendente.
     * @param dp DeepPlane donde se buscará la jerarquía de la palabra especificada.
     * @return Lista de nodos pertenecientes a la jerarquía descendente de la palabra indicada.
     */
    public static ArrayList<Node> getJerarquiaDescendente(String word, DeepPlane dp){
        ArrayList<Node> result=new ArrayList();
        HashMap idsVisitados=new HashMap(); //Para registrar los ids de los nodos que se van visitando y detectar bucles
        ArrayList<Node> objs=dp.getNode(word); //Obtenemos todos los nodos concordantes (AVISO: aqui solo debieramos coger la acepción correcta, no la última como se está haciendo)
        for (Node nod:objs){ //Para cada uno de los nodos, obtenemos su árbol de subclases
            result=jerarquiaDescendente(nod,dp,0,idsVisitados); //Machacamos la variable (solo se analizará la última acepción)
        }//for
        return result;
    }
    
    
    /**
     * Genera un listado con los nodos/objetos en que se descompone un objeto/nodo especificado (agregación).
     * Explora Objeto->Está_compuesto_de->Componentes.
     * @param nod Nodo de partida, a partir del cual se exploran sus componentes.
     * @param dp DeepPlane con la red semántica.
     * @param nivel Nivel de profundidad en que estamos.
     * @param idsVisitados Lista de IDs de Nodes ya visitados, para evitar bucles infinitos.
     * @return Listado con los Nodos/Objetos que componen el nodo especificado.
     */
    private static ArrayList<Node> getComponentes(Node nod, DeepPlane dp, int nivel, HashMap idsVisitados){
        ArrayList<Node> result=new ArrayList();
        String id=(String)idsVisitados.get(nod.getId());
        if (id==null){ //Si no hemos visitado este nodo aún (si ya fue visitado, devolvemos cadena vacía)
            idsVisitados.put(nod.getId(),nod.getId()); //Guardamos el ID como visitado
            ArrayList<Node> nodos=dp.getNodesDestino(nod,Link.LINK_ES_AGREGACION); //Rastreamos todos los enlaces que son componentes
            for (Node n:nodos){ //Obtenemos todos sus componentes
                result.add(n); //Y añadimos el nodo a la lista
                result.addAll(getComponentes(n,dp,nivel+1,idsVisitados)); //Buceamos en los nodos destino
            }//for
        }//if no fue visitado
        return result;
    }
    

    /**
     * Genera un listado con los nodos/objetos en que se descompone un objeto/nodo especificado (agregación).
     * Explora Objeto->Está_compuesto_de->Componentes.
     * AVISO: Se analiza solo uno de los nodos coincidentes (acepciones) con la palabra especificada (el último).
     * @param word Palabra de la que se desea obtener los elementos en que se descompone.
     * @param dp DeepPlane donde se buscará la estructura de agregación de la palabra especificada.
     * @return Listado con los Nodos/Objetos que componen la palabra especificada.
     */
    public static ArrayList<Node> getComponentes(String word, DeepPlane dp){
        ArrayList<Node> result=new ArrayList();
        HashMap idsVisitados=new HashMap(); //Para registrar los ids de los nodos que se van visitando y detectar bucles
        ArrayList<Node> objs=dp.getNode(word); //Obtenemos todos los nodos concordantes
        for (Node nod:objs){ //Para cada uno de los nodos, obtenemos su árbol de componentes
            result=getComponentes(nod,dp,0,idsVisitados); //Machacamos la variable (solo se analizará la última acepción)
        }//for
        return result;
    }
    
    
    /**
     * Genera un listado con los nodos/objetos que tienen como componente el objeto/nodo especificado (agregación).
     * Explora Objeto->Es_parte_de->Agregador.
     * @param nod Nodo de partida, a partir del cual se exploran los elementos que lo tienen como componente.
     * @param dp DeepPlane con la red semántica.
     * @param nivel Nivel de profundidad en que estamos.
     * @param idsVisitados Lista de IDs de Nodes ya visitados, para evitar bucles infinitos.
     * @return Listado con los Nodos/Objetos que tienen como componente el nodo especificado.
     */
    private static ArrayList<Node> getAgregadores(Node nod, DeepPlane dp, int nivel, HashMap idsVisitados){
        ArrayList<Node> result=new ArrayList();
        String id=(String)idsVisitados.get(nod.getId());
        if (id==null){ //Si no hemos visitado este nodo aún (si ya fue visitado, devolvemos cadena vacía)
            idsVisitados.put(nod.getId(),nod.getId()); //Guardamos el ID como visitado
            result.add(nod); //Y añadimos el nodo a la lista
            ArrayList<Node> nodos=dp.getNodesDestino(nod,Link.LINK_ES_PARTE); //Obtenemos todos sus componentes
            for (Node n:nodos){
                result.addAll(getAgregadores(n,dp,nivel+1,idsVisitados)); //Buceamos en los nodos destino
            }//for
        }//if no fue visitado
        return result;
    }
    

    /**
     * Genera un listado con los nodos/objetos de los que forma parte el objeto/nodo especificado (agregación).
     * Explora Objeto->Es_parte_de->Agregador.
     * AVISO: Se analiza solo uno de los nodos coincidentes (acepciones) con la palabra especificada (el último).
     * @param word Palabra de la que se desea obtener aquellos objetos de los que forma parte.
     * @param dp DeepPlane donde se buscará la estructura de agregación de la palabra especificada.
     * @return Listado con los Nodos/Objetos que tienen la palabra especificada como componente.
     */
    public static ArrayList<Node> getAgregadores(String word, DeepPlane dp){
        ArrayList<Node> result=new ArrayList();
        HashMap idsVisitados=new HashMap(); //Para registrar los ids de los nodos que se van visitando y detectar bucles
        ArrayList<Node> objs=dp.getNode(word); //Obtenemos todos los nodos concordantes
        for (Node nod:objs){ //Para cada uno de los nodos, obtenemos su árbol de componentes
            result=getAgregadores(nod,dp,0,idsVisitados); //Machacamos la variable (solo se analizará la última acepción)
        }//for
        return result;
    }
    
    
    /**
     * Busca en la Red Semántica y recupera todos los objetos que tengan el atributo (p.e. "color") con el valor especificado (p.e. "azul").
     * @param dp DeepPlane que contiene la Red Semántica
     * @param attributeName Nombre del atributo que debe tener el objeto (p.e. "color").
     * @param attributeValue Valor que debe "estar contenido" en el valor del atributo buscado. P.e. "azul" está contenido en "azul oscuro".
     * @return Lista de todos los elementos encontrados.
     */
    public static ArrayList<Node> getNodesbyAttribute(DeepPlane dp, String attributeName, String attributeValue){
        ArrayList<Node> result=new ArrayList();
        ArrayList<Node> nodos=dp.getNode(attributeName); //Nodos coincidentes exactamente
        for (Node nod:nodos){ //Para cada nodo coincidente
            
            ArrayList<Node> objetosOrigen=nod.getNodesOrigen(Link.LINK_ES_CUALIDAD); //Objetos origen con los que está relacionado el atributo
            Node objetoOrigen=(objetosOrigen.isEmpty()?null:objetosOrigen.get(0)); //Cogemos el primero y descartamos el resto, pues un atributo solo va a tener un origen.
            ArrayList<Node> objetosDestino=nod.getNodesDestino(Link.LINK_ES_CUALIDAD); //Objetos destino (que realmente son los valores del atributo) con los que está relacionado el atributo
                    
            if (objetoOrigen!=null){ //Si hay objeto origen, continuamos buscando; si no lo hay no tiene sentido seguir buscando en él
                for (Node atValue:objetosDestino){
                    if (atValue.getWord().getText().contains(attributeValue)){ //Si el valor coincide
                       result.add(objetoOrigen); //Guardamos el objeto que tiene el atributo
                    }
                }//for valores atributos
            }//If hay objeto origen
        }//for
        
        return result;
    }
    
    
}//class
