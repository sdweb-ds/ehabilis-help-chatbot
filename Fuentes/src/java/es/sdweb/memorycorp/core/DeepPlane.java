package es.sdweb.memorycorp.core;

import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.memorycorp.nlpengine.DVerb;
import es.sdweb.memorycorp.nlpengine.DWord;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Antonio Carro Mariño
 */
public class DeepPlane {
    
    private HashMap nodosMapID=new HashMap(); //HashMap(ID, Node) de todos los componentes "Node" de la Red Semántica Deep de conocimiento
    private HashMap nodosMap=new HashMap(); //HashMap(texto, ArrayList<Node>) de todos los componentes "Node" de la Red Semántica Deep de conocimiento
    //Un mismo texto puede tener múltiples acepciones (palabras polisémicas) en la mayor parte de los casos.
    private HashMap linksMapID=new HashMap(); //HashMap(ID, Link) de todos los componentes "Link" de la Red Semántica Deep de conocimiento. 
    private HashMap linksMap=new HashMap(); //HashMap(texto, Link) de todos los componentes "Link" de la Red Semántica Deep de conocimiento. 
    //El texto usado como clave es origen-link-destino. En el nivel DeepPlane habrá solo una ocurrencia de esta terna.
    //Los enlaces son todos diferentes, nunca se reutilizan.
    
    
    public void deleteNode(Node nod){
        //Borramos el nodo de los MAPs de Nodes
        nodosMapID.remove(nod.getId());
        ArrayList<Node> acepciones=(ArrayList<Node>)nodosMap.get(nod.getWord().getText());
        if (acepciones!=null){
            acepciones.remove(nod); //Eliminamos el nodo de las acepciones
        }
        
        //Desenganchamos el nodo de todos los Nodos que apuntan a él. Que él apunte a otros nos da igual.
        ArrayList<Node> nodosOrigen=nod.getNodesOrigen(null);
        ArrayList<Link> linksOrigen=nod.getLinksEntrantes(); //Acciones que enlazan a este nodo
        for (Link lnk:linksOrigen){ //Borramos todos los kinks de los MAPs
            linksMapID.remove(lnk.getId());
            linksMap.remove(lnk.getId());
        }
        for (Node orig:nodosOrigen){ //Borramos los links de todos los nodos origen
            orig.getLinksSalientes().removeAll(linksOrigen); //Cortamos todos los enlaces desde los nodos origen
        }
                
    }
    
    /**
     * Localiza un nodo a partir de su nombre (si existe) y devuelve sus acepciones.
     * @param text Texto del nodo a buscar.
     * @return Lista con las correspondencias halladas; o lista vacía si no se halló nada.
     */
    public ArrayList<Node> getNode(String text){
        ArrayList<Node> result=new ArrayList();
        ArrayList<Node> aux=(ArrayList)getNodosMap().get(text);
        if (aux!=null){ //Si está en el Map, devolvemos la lista. En otro caso se devuelve la lista vacía.
            result=aux;
        }
        return result;
    }
    
    /**
     * Localiza un nodo destino a partir de su nombre (si existe) y devuelve aquellos cuyos enlaces cumplan su pertenencia al tipo primitivo especificado.
     * Esta función permite, por tanto, recuperar enlaces a: subclases (las superclases estarían en los nodos entrantes), elementos agregados, etc.
     * @param nodo Nodo a partir del cual buscar.
     * @param primitiveType Tipo primitivo (Link.LINK_) de la relación que debe tener cada nodo devuelto (además del texto).
     * @return Lista con las correspondencias halladas.
     */
    public ArrayList<Node> getNodesDestino(Node nodo, String primitiveType){
        ArrayList<Node> result=new ArrayList();
        if (nodo!=null){ //Solo devolvemos los tipos con algún Link concordante con el Tipo Primitivo especificado
            ArrayList<Node> destinos=nodo.getNodesDestino(primitiveType); //Obtenemos la lista de Nodos destino alcanzables por Links del tipo primitivo especificado
            //Estos enlaces llevan a Nodos que pueden ser: subclases, agregados, adjetivos, etc.
            result.addAll(destinos); //Añadimos al resultado todos los nodos hallados
        }
        return result;
    }
    
    /**
     * Localiza un nodo origen a partir de su nombre (si existe) y devuelve aquellos cuyos enlaces cumplan su pertenencia al tipo primitivo especificado.
     * Esta función permite, por tanto, recuperar enlaces a: superclases (las subclases estarían en los nodos salientes), elementos a los que pertenece, etc.
     * @param nodo Nodo a partir del cual se va a buscar.
     * @param primitiveType Tipo primitivo (Link.LINK_) de la relación que debe tener cada nodo devuelto (además del texto).
     * @return Lista con las correspondencias halladas.
     */
    public ArrayList<Node> getNodesOrigen(Node nodo, String primitiveType){
        ArrayList<Node> result=new ArrayList();
        if (nodo!=null){ //Solo devolvemos los tipos con algún Link concordante con el Tipo Primitivo especificado
           ArrayList<Node> origenes=nodo.getNodesOrigen(primitiveType); //Obtenemos la lista de Nodos origen alcanzables por Links del tipo primitivo especificado
           //Estos enlaces llevan a Nodos que pueden ser: superclases, objetos del que éste es un componente, etc.
           result.addAll(origenes); //Añadimos al resultado todos los nodos hallados
        }
        return result;
    }
    
    /**
     * Busca un nodo a partir de su nombre y tipo, y devuelve una acepción concreta. 
     * @param text Texto del nodo a buscar.
     * @param tipo Tipo del nodo. Es un valor del tipo "DWord.POS_"; si se fija a nulo este parámetro, no se considera el tipo.
     * @return Nodo localizado o null si no se encuentra.
     */
    public Node getNode(String text, String tipo){
        Node result=null;
        ArrayList<Node> aux=(ArrayList)getNodosMap().get(text);
        if (aux!=null){ //Puede que no esté ni definido en la tabla hash
            for (Node n:aux){
                DWord p=n.getWord();
                if (p.getText().equals(text)&&(n.getTipo().equals(tipo)||StringUtil.isEmpty(tipo))){ //Si coinciden texto y tipo
                    result=n; //Es el resultado buscado
                }
            }//for
        }//if
        return result;
    }
    
    /**
     * Busca un nodo a partir de su nombre y tipo, y devuelve la lista de acepciones coincidentes. 
     * @param text Texto del nodo a buscar.
     * @param tipo Tipo del nodo. Es un valor del tipo "DWord.POS_"; si se fija a nulo este parámetro, no se considera el tipo.
     * @return Nodo localizado o null si no se encuentra.
     */
    public ArrayList<Node> getNodes(String text, String tipo){
        ArrayList<Node> result=new ArrayList();
        ArrayList<Node> aux=(ArrayList)getNodosMap().get(text);
        if (aux!=null){ //Puede que no esté ni definido en la tabla hash
            for (Node n:aux){
                DWord p=n.getWord();
                if (p.getText().equals(text)&&(n.getTipo().equals(tipo)||StringUtil.isEmpty(tipo))){ //Si coinciden texto y tipo
                    result.add(n); //Es un nodo coincidente, y lo añadimos al resultado
                }
            }//for
        }//if
        return result;
    }
    
    /**
     * Almacena un Node en las HashTables del DeepPlane.
     * @param node Nodo a guardar.
     * @return ArrayList<Node> del nodosMap con el nodo ya añadido.
     */
    private ArrayList<Node> putNode(Node node){
        ArrayList<Node> newNodeList=new ArrayList();
        newNodeList.add(node); //Añadimos el nodo
        this.nodosMap.put(node.getWord().getText(), newNodeList); //Lo almacenados por su texto literal
        this.nodosMapID.put(node.getId(), node); //Y lo almacenamos por su ID
        return newNodeList;
    }
    
    /**
     * Almacena un Link en las HashTables del DeepPlane.
     * @param link Enlace/Accion a guardar.
     * @param key Clave bajo la que se guardará el link en el linksMap. En linksMapID se guarda con su ID como clave.
     */
    private void putLink(Link link, String key){
        this.linksMap.put(key, link); //Lo almacenados por su texto literal
        this.linksMapID.put(link.getId(), link); //Y lo almacenamos por su ID
    }
    
    /**
     * Almacena un Link en las HashTables del DeepPlane.
     * @param link Enlace/Accion a guardar.
     * @param key Clave bajo la que se guardará el link en el linksMap. En linksMapID se guarda con su ID como clave.
     */
    public void addLink(Link link, String key){
        putLink(link,key);
    }
    
    /**
     * Obtiene un Link por su ID.
     * @param id Identificador único del Link.
     * @return Devuelve el Link si existe. Nulo en caso contrario.
     */
    public Link getLinkByID(String id){
        Link result=(Link)this.linksMapID.get(id);
        return result;
    }
    
    /**
     * Obtiene un Link por su ID.
     * @param id Identificador único del Nodo.
     * @return Devuelve el Nodo si existe. Nulo en caso contrario.
     */
    public Node getNodeByID(String id){
        Node result=(Node)this.nodosMapID.get(id);
        return result;
    }
    
    /**
     * Añade un nodo al Map evitando duplicidades.
     * @param nodo Nodo a añadir al Map
     */
    public void addNode(Node nodo){
        ArrayList<Node> aux=(ArrayList)getNodosMap().get(nodo.getWord().getText()); //Buscamos el nodo por su texto. Obtenemos la lista de nodos con ese texto.
        if (aux==null){ //Si el término no está en el Map en absoluto.
           aux=putNode(nodo); //Creamos el nodo en el DeepPlane, y recuperamos su lista con él dentro.
        }else{ //ya existía un nodo con el mismo texto
           aux.add(nodo); //Lo añadimos al Map. Aux apunta a la lista que está ya en el Map y corresponde a la lista de palabras con el mismo texto del nodo
           this.nodosMapID.put(nodo.getId(), nodo); //Y lo almacenamos por su ID   
        }
    }
    

    /**
     * Busca un nodo con el mismo texto y tipo (PROPN, PROPNL NOUN, etc) que el del nodo especificado, y devuelve una acepción concreta si lo encuentra.Si el nodo es un Adjetivo (ADJ), es decir, un valor asignado a un objeto, debe ser creado siempre.
     * @param node Nodo a buscar/crear.
     * @param createIfNotExist Si es True, crea el nodo a partir del especificado en caso de que no exista y lo devuelve.
     * @param createAlways Fuerza la creación siempre, aunque exista un nodo igual en la Red Semántica
     * @return Nodo localizado o null si no se encuentra.
     */
    public Node getNode(Node node, boolean createIfNotExist, boolean createAlways){
        Node result=null;
        if (node!=null){
            ArrayList<Node> aux=(ArrayList)getNodosMap().get(node.getWord().getText());
            if (aux!=null){ //Si está en el Map
                for (Node n:aux){
                    if (n.equals(node)){ //Si coinciden en sus características
                        result=n; //Es el resultado buscado
                    }
                }//for
            }
            
            if (node.getTipo().equals(DWord.POS_ADJ)){ //Si en nodo es un adjetivo (ADJ), se crea siempre, porque es un valor asignado a un objeto.
                result=null; //Por ello, ponemos esta variable a nulo.
            }
            
            if ((result==null&&createIfNotExist)||createAlways){ //Si no se encontró y se desea crear; o se ha indicado la creación forzada
                result=node;
                addNode(node); //Añadimos el nodo al Map
            }
        }//If nodo existe
        return result;
    }
    

    /**
     * Busca un nodo con el mismo texto y tipo (PROPN, PROPNL NOUN, etc) que el del nodo especificado, y devuelve una acepción concreta si lo encuentra.
     * Si el nodo es un Adjetivo (ADJ) NO DEBE USARSE ESTE MÉTODO, es decir, un valor asignado a un objeto, debe ser creado siempre.
     * @param node Nodo a buscar/crear.
     * @param nodeOrigin (Opcional nulable) Si se especifica, indica el nodo origen que deberá tener el node especificado, para considerar que un nodo existente es el buscado. AVISO: Si este parámetro es nulo, el node se creará siempre.
     * @param createIfNotExist Si es True, crea el nodo a partir del especificado en caso de que no exista y lo devuelve.
     * @return Nodo localizado o null si no se encuentra.
     */
    public Node getNode(Node node, Node nodeOrigin, boolean createIfNotExist){
        //Tratamos de recuperar el nodo buscado
        Node result=null;
        ArrayList<Node> nodesList=getNodes(node.getWord().getText(), node.getTipo()); //Buscamos la lista de nodos que cuelgan de la palabra textual (p.e. "zorro")
        if (nodesList.isEmpty()){ //Si no hay nodos coincidentes en la Red
            result=getNode(node,true,true); //Lo creamos y lo devolvemos
        }else{
            //Si hubo coincidencias con palabra y tipo
            if (nodeOrigin!=null){ //Si se indicó un nodo origen
                for (Node acepcion:nodesList){ //Para cada acepción
                    ArrayList<Node> origenes=acepcion.getNodesOrigen(null); //Cogemos todos sus nodos origen
                    for (Node ori:origenes){
                        if (ori.equals(nodeOrigin)){ //Si el origen coincide, tenemos la acepción buscada
                            result=acepcion;
                        }
                    }//For origenes
                }//for acepcion
                
                //Si había acepciones pero encajan por sus origenes
                if (result==null && createIfNotExist){
                    result=getNode(node,true,true); //Lo creamos y lo devolvemos
                }
            }//Si se especificó origen
        }//If hay nodos coincidentes
        
        return result;
    }

    
    /**
     * Enlaza en nodo origen con el nodo destino, mediante un enlace (si ya existe ese enlace, lo reutiliza).
     * El enlace se produce en los dos sentidos: origen -> destino ; destino -> origen
     * @param nodeOr Nodo origen.
     * @param nodeDest Nodo destino.
     * @param link Enlace que los une y/o debe unirlos.
     */
    public void linkNodes(Node nodeOr, Node nodeDest, Link link){
        nodeOr.addLinkSaliente(link); //Enlazamos en los dos sentidos
        nodeDest.addLinkEntrante(link); //Enlazamos en los dos sentidos
    }
    
    
    /**
     * Enlaza de forma coherente un Nodo origen y un Nodo destino, mediante un Link, y evitando duplicidades.
     * Es decir, genera los enlaces: NODO_ORIGEN <-> ENLACE <-> NODO_DESTINO
     * @param hecho Hecho que se quiere cargar en la red semántica.
     */
    public void loadAndLink(Fact hecho){
        Node sujetoN=getNode(hecho.getSujeto(),true, false); //Buscamos el nodo, y si no está lo creamos y añadimos al Map
        Node objetoDirectoN=getNode(hecho.getObjetoDirecto(),true, false); //Buscamos el nodo, y si no está lo creamos y añadimos al Map
        Node objetoIndirectoN=getNode(hecho.getObjetoIndirecto(),true, false); //Buscamos el nodo, y si no está lo creamos y añadimos al Map
        Node ccLugarN=getNode(hecho.getCcLugar(),true, false); //Buscamos el nodo, y si no está lo creamos y añadimos al Map
        
        //Tratamos de recuperar el enlace de este hecho (para ver si existe y no duplicarlo)
        String linkKey=hecho.getLinkKey();
        
        Link link=getLink(linkKey);
        if (link!=null){ //Si el enlace ya existe 
            link.incPeso(); //Incrementamos su peso
        }
        //Creamos el enlace (siempre que no exista una contradicción)
        //*** enlaces del Link ****
        hecho.getAccion().setNodeSaliente(objetoDirectoN); //Enlazamos el nodo OD
        hecho.getAccion().setNodeEntrante(sujetoN); //Enlazamos el nodo sujeto
        hecho.getAccion().setNodeOI(objetoIndirectoN); //Enlazamos el nodo OI
        hecho.getAccion().setNodeCcLugar(ccLugarN); //Enlazamos el nodo ccLugar

        //*** enlaces de los nodos ***
        if (sujetoN!=null) //No tiene porque haber sujeto. P.e. "Hace frío"
            sujetoN.addLinkSaliente(hecho.getAccion()); //Enlazamos el origen (suj) con el link y viceversa
        if (objetoDirectoN!=null) //No tiene porque haber OD. P.e. "El perro de Juan mordió a Luisstruct el perro de Juan mordió a Luis
            objetoDirectoN.addLinkEntrante(hecho.getAccion()); //Enlazamos el destino (OD) con el link y viceversa

        putLink(hecho.getAccion(),linkKey); //Guardamos el link en los Map
    }

    /**
     * Carga un hecho en la Red Semántica.Es decir, genera los enlaces: NODO_ORIGEN <-> ENLACE <-> NODO_DESTINO
     * @param hecho Hecho que se quiere cargar en la red semántica.
     * @param createIfNotExists True si queremos que los nodos se creen en caso de no existir; false en caso contrario.
     */
    public void loadFact(Fact hecho,boolean createIfNotExists){
        Node sujetoN=getNode(hecho.getSujeto(),createIfNotExists, false); //Buscamos el nodo, y si no está lo creamos y añadimos al Map
        Node objetoDirectoN=getNode(hecho.getObjetoDirecto(),createIfNotExists, false); //Buscamos el nodo, y si no está lo creamos y añadimos al Map
        Node objetoIndirectoN=getNode(hecho.getObjetoIndirecto(),createIfNotExists, false); //Buscamos el nodo, y si no está lo creamos y añadimos al Map
        Node ccLugarN=getNode(hecho.getCcLugar(),createIfNotExists, false); //Buscamos el nodo, y si no está lo creamos y añadimos al Map
        
        //Tratamos de recuperar el enlace de este hecho (para ver si existe y no duplicarlo)
        String linkKey=hecho.getLinkKey();
        
        Link link=getLink(linkKey); //Esto de incPeso no funciona bien, pues se repiten las claves sujeto-verbo-objeto, y se incrementa el peso de la primera ocurrencia, no del enlace que debiera ser
        if (link!=null){ //Si el enlace ya existe 
            link.incPeso(); //Incrementamos su peso
        }
        //Creamos el enlace (siempre que no exista una contradicción)
        //*** enlaces del Link ****
        hecho.getAccion().setNodeSaliente(objetoDirectoN); //Enlazamos el nodo OD
        hecho.getAccion().setNodeEntrante(sujetoN); //Enlazamos el nodo sujeto
        hecho.getAccion().setNodeOI(objetoIndirectoN); //Enlazamos el nodo OI
        hecho.getAccion().setNodeCcLugar(ccLugarN); //Enlazamos el nodo ccLugar

        //*** enlaces de los nodos ***
        if (sujetoN!=null) //No tiene porque haber sujeto. P.e. "Hace frío"
            sujetoN.addLinkSaliente(hecho.getAccion()); //Enlazamos el origen (suj) con el link y viceversa
        if (objetoDirectoN!=null) //No tiene porque haber OD. P.e. "El perro de Juan mordió a Luisstruct el perro de Juan mordió a Luis
            objetoDirectoN.addLinkEntrante(hecho.getAccion()); //Enlazamos el destino (OD) con el link y viceversa

        putLink(hecho.getAccion(),hecho.getAccion().getId()); //Guardamos el link en los Map
    }
    
    
    /**
     * Obtiene un Link a partir de su clave: sujeto-link-OD-OI-CCL. 
     * AVISO: No pueden existir varios enlaces bajo la misma clave: P.e. no puede haber: "gorrión-ser-ave--"(LINK_SUBCLASE_DE), "gorrión-ser-ave"([LINK_SUPERCLASE_DE]
     * Por tanto los enlaces deben ser siempre ÚNICOS. Si se duplican es porque estamos tratando de introducir 
     * una INCONSISTENCIA (o información duplicada).
     * @param linkKey Clave del enlace buscado: sujeto-link-OD-OI-CCL. P.e. "gorrión-ser-ave--"
     * @return Enlace buscado o null si no se encuentra.
     */
    public Link getLink(String linkKey){
        Link result=(Link)getLinksMap().get(linkKey);
        return result;
    }
    
    /**
     * Obtiene y/o crea un nodo del origen al destino. Si el nodo origen tiene un enlace con el verbo especificado hacia
     * el nodo destino (se comprueba igualdad de puntero con el nodo destino), se recupera y devuelve ese enlace. 
     * Si el enlace no existe, se crea y se devuelve (enlazado ya a la Red Semántica con un Link Key que es su ID único).
     * @param origen Nodo origen.
     * @param destino Nodo destino.
     * @param verbo Verbo buscado (verbo perteneciente al enlace buscado).
     * @param tipoLink Tipo primitivo del link (Link.LINK_)
     * @return Link encontrado y/o creado en caso de que no existiera (y añadido a la Red Semántica por su ID único)
     */
    public Link getLink(Node origen, Node destino, DVerb verbo, String tipoLink){
        Link result=null;
        for (Link lnk:origen.getLinksSalientes()){ //Vemos si el origen enlaza con un verbo de ese tipo
            DVerb verb=lnk.getVerb();
            if (verb.getText().equals(verbo.getText())){ //Si el verbo coincide
                if (lnk.getNodeSaliente()==destino){ //Y apunta al nodo destino (igualdad de punteros)
                    result=lnk; //Devolvemos el resultado
                }//if apunta a nodo destino
            }
        }//For links salientes del origen
        if (result==null){ //Si el enlace no existe
            result=new Link(verbo,false); //Creamos el Link
            result.setTipoPrimitivo(tipoLink); //Indicamos su tipo
            addLink(result, result.getId()); //Añadimos el nuevo Link a la Red Semántica, indicando como clave su ID único.
        }
        return result;
    }

    /**
     * @return the nodosMap
     */
    public HashMap getNodosMap() {
        return nodosMap;
    }

    /**
     * @return the linksMap
     */
    public HashMap getLinksMap() {
        return linksMap;
    }
    
    
    /**
     * Busca un nodo en la Red Semántica de tipo (en este orden): DWord.POS_PROPN, DWord.POS_PROPNL, DWord.POS_NOUN. Y lo recupera.
     * @param word Palabra buscada en la Red Semántica
     * @return El nodo, si existe; en otro caso, devuelve null.
     */
    public Node getNodeSustantivo(String word){
        Node nod=getNode(word,DWord.POS_PROPN); //Miramos si es un nombre propio
        nod=(nod==null?getNode(word,DWord.POS_PROPNL):nod); //Si no, miramos si es un lugar
        nod=(nod==null?getNode(word,DWord.POS_NOUN):nod); //Si no, miramos si es un sustantivo
        return nod;
    }
    
    /**
     * Devuelve las cualidades de un sustantivo/nombre/lugar, si las tiene. Los adjetivos, etc, no tienen propiedades.
     * @param word Sustantivo/nombre/lugar especificado
     * @return Listado de cualidades del sustantivo/nombre/lugar. Lista vacía si no se encuentra ninguna.
     */
    public ArrayList<Node> getCualidades(String word){
        ArrayList<Node> result=new ArrayList();
        Node nod=getNodeSustantivo(word); //Miramos si es un nombre propio, lugar o sustantivo/NOUN
        
        if (nod!=null){
            //Recolectamos todos los hijos que son cualidad
            for (Link lnk:nod.getLinksSalientes(Link.LINK_ES_CUALIDAD)){
                Node cualidad=lnk.getNodeSaliente(); //Nodo al que apunta el enlace
                cualidad.setNegado(lnk.isNegado()); //Indicamos si la cualidad está o no negada.
                result.add(cualidad); //Lo añadimos
            }//for
        }
        return result;
    }
 
    
    /**
     * Devuelve las cualidades de un sustantivo/nombre/lugar, si las tiene. Los adjetivos, etc, no tienen propiedades.
     * @param nodeID Identificador concreto del Nodo que contiene el Sustantivo/nombre/lugar especificado
     * @return Listado de cualidades del sustantivo/nombre/lugar. Lista vacía si no se encuentra ninguna.
     */
    public ArrayList<Node> getCualidadesByID(String nodeID){
        ArrayList<Node> result=new ArrayList();
        Node nod=getNodeByID(nodeID); //Recuperamos el nodo por su ID
        
        if (nod!=null){
            //Recolectamos todos los hijos que son cualidad
            for (Link lnk:nod.getLinksSalientes(Link.LINK_ES_CUALIDAD)){
                Node cualidad=lnk.getNodeSaliente(); //Nodo al que apunta el enlace
                cualidad.setNegado(lnk.isNegado()); //Indicamos si la cualidad está o no negada.
                result.add(cualidad); //Lo añadimos
            }//for
        }
        return result;
    }
    
    
    
}//class
