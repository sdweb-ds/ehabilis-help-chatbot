package es.sdweb.memorycorp.core;

import es.sdweb.memorycorp.nlpengine.DVerb;
import es.sdweb.memorycorp.nlpengine.DWord;
import es.sdweb.memorycorp.nlpengine.LOracion;
import es.sdweb.memorycorp.nlpengine.LTermino;
import es.sdweb.memorycorp.nlpengine.NLPUtil;
import es.sdweb.memorycorp.nlpengine.SpanishDictionary;
import java.util.ArrayList;

/**
 * Utilidades para operar con los datos de la Red Semántica.
 * @author Antonio Carro Mariño
 */
public class NetUtil {
    
    /**
     * Indica si una palabra está presente en el array de términos.
     * @param word Palabra buscada.
     * @param terminos Lista de términos en la que se busca la palabra. 
     * @return True si la palabra está en la lista; false en caso contrario.
     */
    public static boolean isPresent(String word,ArrayList<LTermino> terminos){
        boolean result=false;
        for (LTermino term:terminos){
            if (term.getTexto().equals(word)){
                result=true;
            }
        }//for
        return result;
    }
    
    
    /**
     * Indica si una palabra está presente en el array de nodos.
     * @param word Palabra buscada.
     * @param nodos Lista de nodos en los que se busca la palabra. 
     * @return True si la palabra está en la lista; false en caso contrario.
     */
    public static boolean isPresentNode(String word,ArrayList<Node> nodos){
        boolean result=false;
        for (Node nod:nodos){
            if (nod.getWord().getText().equals(word)){
                result=true;
            }
        }//for
        return result;
    }
    
    
    /**
     * Purga de la lista de nodos, aquellos nodos cuyo sentido de negación no coincida con el flag especificado.
     * @param negado Flag booleano. Se conservarán los nodos cuyo sentido de negación sea el mismo que el de este flag.
     * @param nodos Lista de nodos sobre el que se realizará la criba.
     * @return Lista de nodos cribados.
     */
    public static ArrayList<Node> purgeArrayN(boolean negado, ArrayList<Node> nodos){
        ArrayList<Node> result=new ArrayList();
        for (Node nod:nodos){
            if (negado==nod.isNegado()){ //Si el sentido de la negación es el mismo, lo guardamos
                result.add(nod);
            }
        }//for
        return result;
    }
    
    
    /**
     * Crea una lista de palabras con el texto de todos los nodos.
     * @param nodos Lista de nodos a partir de la cual se crea la lista de cadenas.
     * @return Lista de palabras que se corresponden con los nodos.
     */
    public static ArrayList<String> toStringArrayN(ArrayList<Node> nodos){
        ArrayList<String> result=new ArrayList();
        for (Node nod:nodos){
            result.add(nod.getWord().getText());
        }//for
        return result;
    }
    
    
    /**
     * Crea una lista de palabras con el texto de todos los Términos ligüísticos.
     * @param terminos Lista de términos a partir de los cuales se crea la lista de cadenas.
     * @return Lista de palabras que se corresponden con los términos.
     */
    public static ArrayList<String> toStringArrayT(ArrayList<LTermino> terminos){
        ArrayList<String> result=new ArrayList();
        for (LTermino term:terminos){
            result.add(term.getTexto());
        }//for
        return result;
    }
    
    
    /**
     * Indica si todas las cadenas de una lista, están presentes en una lista de cadenas más amplia.
     * @param searchList Lista de cadenas que se buscan en la lista amplia.
     * @param completeList Lista completa de cadenas donde se buscan las palabras.
     * @return True si se encuentran todas, false si falta alguna.
     */
    public static boolean isAllPresent(ArrayList<String> searchList, ArrayList<String> completeList){
        boolean result=true;
        for (String cad:searchList){
            boolean cadPresent=false;
            for (String cadAll:completeList){
                if (cadAll.equals(cad)){ //Si está la marcamos como presente
                    cadPresent=true;
                }
            }//for
            result=result&&cadPresent; //En cuando una cadena no esté, result pasa a ser falso
        }//for
        return result;
    }
    

    /**
     * Función única para generar los LinkKey (claves) para almacenar los Links en los HashMap de la red semántica.
     * Esta función es utilizada por las oraciones copulativas: asignan propiedades a los objetos (herencia, adjetivos, agregación).
     * P.e. "Juan es muy alto", "La mano está formada por la palma y los dedos".
     * @param origen Texto que referencia al Node origen (sujeto).
     * @param enlace Texto que referencia al enlace (verbo/acción).
     * @param destino Texto que referencia al Node destino (Objeto Directo).
     * @return Cadena con la clave de almacenamiento en la HashMap
     */
    public static String generateLinkMapId(String origen, String enlace, String destino){
        String linkKey=origen+"-"+enlace+"-"+destino+"--";
        return linkKey;
    }
    

    /**
     * Función única para generar los LinkKey (claves) para almacenar los Links en los HashMap de la red semántica.
     * Esta función es utilizada por las oraciones copulativas: asignan propiedades a los objetos (herencia, adjetivos, agregación).
     * P.e. "Juan es muy alto", "La mano está formada por la palma y los dedos".
     * @param origen Nodo que referencia al Node origen (sujeto).
     * @param enlace Link que referencia al enlace (verbo/acción).
     * @param destino Nodo que referencia al Node destino (Objeto Directo).
     * @return Cadena con la clave de almacenamiento en la HashMap
     */
    public static String generateLinkMapId(Node origen, Link enlace, Node destino){
        String linkKey=getNodeText(origen)+"-"+getLinkText(enlace)+"-"+getNodeText(destino)+"--";
        return linkKey;
    }
    
    /**
     * Función para generar los LinkKey (claves) para almacenar los Links en los HashMap de la red semántica.
     * Esta función es utilizada por el resto de las oraciones (las que no son copulativas: asignan propiedades a los objetos (herencia, adjetivos, agregación)).
     * P.e. "Juan robó un coche a Marco en la peluquería".
     * @param sujeto Sujeto de la acción.
     * @param accion Acción/verbo que sucede.
     * @param objetoDirecto Objeto al que se aplica la acción (OD)
     * @param objetoIndirecto Objeto beneficiario de la acción (OI)
     * @param ccLugar Lugar donde sucede la acción (CC de lugar).
     * @return 
     */
    public static String generateLinkMapId(String sujeto, String accion, String objetoDirecto, String objetoIndirecto, String ccLugar){
        String linkKey=sujeto+"-"+accion+"-"+objetoDirecto+"-"+objetoIndirecto+"-"+ccLugar;
        return linkKey;
    }
    
    /**
     * Función para generar los LinkKey (claves) para almacenar los Links en los HashMap de la red semántica.
     * Esta función es utilizada por el resto de las oraciones (las que no son copulativas: asignan propiedades a los objetos (herencia, adjetivos, agregación)).
     * P.e. "Juan robó un coche a Marco en la peluquería".
     * @param sujeto Sujeto de la acción.
     * @param accion Acción/verbo que sucede.
     * @param objetoDirecto Objeto al que se aplica la acción (OD)
     * @param objetoIndirecto Objeto beneficiario de la acción (OI)
     * @param ccLugar Lugar donde sucede la acción (CC de lugar).
     * @return 
     */
    public static String generateLinkMapId(Node sujeto, Link accion, Node objetoDirecto, Node objetoIndirecto, Node ccLugar){
        String linkKey=getNodeText(sujeto)+"-"+getLinkText(accion)+"-"+getNodeText(objetoDirecto)+"-"+getNodeText(objetoIndirecto)+"-"+getNodeText(ccLugar);
        return linkKey;
    }
    
    /**
     * Devuelve la palabra (String) de un nodo (del DWord contenido en él). Si el nodo es nulo, devuelve la cadena vacía.
     * @param n Nodo del que se desea obtener la palabra.
     * @return Devuelve la palabra (String) de un nodo. Si el nodo es nulo, devuelve la cadena vacía.
     */
    public static String getNodeText(Node n){
        String result="";
        if (n!=null){
            result=n.getWord().getText();
        }
        return result;
    }
    
    
    /**
     * Devuelve la palabra (String) de un Link (del DVerb contenido en él). Si el Link es nulo, devuelve la cadena vacía.
     * @param lnk Nodo del que se desea obtener la palabra.
     * @return Devuelve la palabra (String) de un Link. Si el Link es nulo, devuelve la cadena vacía.
     */
    public static String getLinkText(Link lnk){
        String result="";
        if (lnk!=null){
            result=lnk.getVerb().getText();
        }
        return result;
    }
    
    
    /**
     * Crea un nuevo nodo (Node) a partir de un término.
     * @param term Término especificado.
     * @return Nodo creado.
     */
    public static Node createNode(LTermino term){
        Node result=null;
        if (term!=null){
           DWord word=SpanishDictionary.getDictionary().getWord(term.getTexto()); //Obtenemos su palabra del diccionario
           result=new Node(word,term.getPos()); //Creamos el nodo
        }
        return result;
    }
    
    
    /**
     * Crea un hecho (Fact) a partir de una oración.
     * @param or Oración a analizar.
     * @return Hecho que representa la oración.
     */
    public static Fact createFact(LOracion or){
        //** Analizamos la oración
        LTermino accionT=or.getAccion();
        DVerb verbD=NLPUtil.getVerbUnambiguous(or);
        Link accionL=new Link(verbD,accionT.isNegado());
        
        LTermino sujetoT=or.getSujeto(); //Sujeto
        Node sujetoN=NetUtil.createNode(sujetoT);
        LTermino odT=or.getObjetoDirecto(); //Objeto Directo
        Node odN=NetUtil.createNode(odT);
        LTermino oiT=or.getObjetoIndirecto(); //Objeto Indirecto
        Node oiN=NetUtil.createNode(oiT); 
        LTermino ccLugarT=or.getCcLugar(); //CC Lugar
        Node ccLugarN=NetUtil.createNode(ccLugarT);

        Fact result=new Fact(accionL,sujetoN,odN,oiN,ccLugarN);
        return result;
    }
    
    
}//class
