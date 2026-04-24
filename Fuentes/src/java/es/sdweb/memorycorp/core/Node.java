package es.sdweb.memorycorp.core;

import es.sdweb.application.componentes.util.ObjectUtil;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.memorycorp.nlpengine.DWord;
import java.util.ArrayList;

/**
 * Esta clase representa un nodo de la red semántica.
 * @author Antonio Carro Mariño
 */
public class Node {
    
    private String id=ObjectUtil.generateId(); //Generamos un Id unico para este concepto/objeto
    private String tipo=""; //Tipo de objeto: se corresponde con los valores de DWord.POS_ sin embargo, el nodo solo puede tener un valor
    private boolean negado=false; //Si es un ADJ indica si está negado. AVISO: No se utiliza para cálculos lógicos. Los Links y su flag "negado" son los que se utilizan para ello.
    
    private DWord word=null; //Palabra del diccionario vinculada a este nodo (siempre tiene que haber una)
    
    //Relaciones de este nodo con otros. La realción siempre se produce a través de un enlace (LINK).
    private ArrayList<Link> linksSalientes=new ArrayList(); //Enlaces salientes (nevegación saliente)
    private ArrayList<Link> linksEntrantes=new ArrayList(); //Enlaces entrantes (desde otros nodos)
    
    //Cuantificador
    private String cuantificador=Quantifier.QUANT_DISCRETO; //Cuantificador de 0 a 100: por defecto una unidad de cantidad/intensidad

    /**
     * Crea un nodo a partir de una palabra del diccionario.
     * @param word Palabra representada por el nodo.
     * @param tipo Tipo de objeto: se corresponde con los valores de DWord.POS_
     */
    public Node(DWord word, String tipo){
        this.word=word;
        this.tipo=tipo;
    }
    
    /**
     * Crea un nodo a partir de una palabra del diccionario. El tipo de la palabra del nodo es el DWord.POS de la word recibida.
     * @param word Palabra representada por el nodo.
     */
    public Node(DWord word){
        this.word=word;
        this.tipo=word.getPOS();
    }
    
    /**
     * Función que indica cuando dos Nodos son iguales. AVISO: aunque el texto sea el mismo, no es igual un ADJ que un NOUN.
     * @param node Nodo a comparar con este.
     * @return True sin son iguales, false en caso contrario.
     */
    public boolean equals(Node node){
        //Dos nodos son iguales si coinciden en su palabra del diccionario, y su tipo (ADJ, NOUN, etc)
        boolean result=(this.word.equals(node.getWord()))&&(this.getTipo().equals(node.getTipo()));
        return result;
    }
    
    /**
     * Indica si existe un enlace saliente de este tipo en este nodo. Esta funcion tiene el objetivo de no duplicar
     * enlaces ante la introducción repetida de una misma oración.
     * @param enlace Enlace saliente a buscar en este nodo.
     * @return El link saliente encontrado, null en caso de que no exista
     */
    public Link existeLinkSaliente(Link enlace){
        Link result=null;
        for (Link link:this.getLinksSalientes()){
            if (link.equals(enlace)){ //Si ambos enlaces son iguales
                result=link;
            }
        }//for
        return result;
    }
        
    
    /**
     * Indica si existe un enlace entrante de este tipo en este nodo. Esta funcion tiene el objetivo de no duplicar
     * enlaces ante la introducción repetida de una misma oración.
     * @param enlace Enlace entrante a buscar en este nodo.
     * @return El link entrante encontrado, null en caso de que no exista
     */
    public Link existeLinkEntrante(Link enlace){
        Link result=null;
        for (Link link:this.getLinksEntrantes()){
            if (link.equals(enlace)){ //Si ambos enlaces son iguales
                result=link;
            }
        }//for
        return result;
    }
    
    /**
     * Busca un enlace saliente de este nodo que coincida con el enlace especificado. En caso de que no exista, puede ser creado.
     * @param enlace Enlace a localizar.
     * @param createIfNotExist True si queremos que cree el enlace en caso de que no exista.
     * @return Enlace localizado o null si no se ha lozalizado ninguna coincidencia.
     */
    public Link getLinkSaliente(Link enlace,boolean createIfNotExist){
        Link result=null;
        for (Link sal:this.getLinksSalientes()){ //Buscamos un enlace coincidente
            if (sal.equals(enlace)){
                result=sal; //Si lo encontramos, lo asignamos
            }
        }//for
        
        if (result==null&&createIfNotExist){ //Si no existe y se quiere crear
            result=enlace;
            addLinkSaliente(enlace); //Esta función evita duplicados y clona el enlace ya que debe ser distinto.
        }
        return result;
    }
    
    
    /**
     * Añade un enlace saliente a este nodo. Si el enlace ya existe, se reutiliza.
     * Si no existe, se crea y se enlaza en los dos sentidos Nodo->Link y Link->Nodo
     * @param enlace Enlace saliente a añadir.
     * @return Enlace añadido
     */
    public Link addLinkSaliente(Link enlace){
        Link result=existeLinkSaliente(enlace);
        if (result==null){ //Si no existe el link saliente
            enlace.setNodeEntrante(this); //Lo enlazamos con su origen Link->Nodo
            this.getLinksSalientes().add(enlace); //Añadimos el enlace. Nodo->Link
            result=enlace; //Lo devolvemos
        }
        return result;
    }
    
    /**
     * Añade un enlace entrante a este nodo. Si el enlace ya existe, se reutiliza.
     * @param enlace Enlace entrante a añadir.
     * @return Enlace añadido
     */
    public Link addLinkEntrante(Link enlace){
        Link result=existeLinkEntrante(enlace);
        if (result==null){ //Si no existe el link saliente
            enlace.setNodeSaliente(this); //Lo enlazamos con su origen Link->Nodo
            this.getLinksEntrantes().add(enlace); //Añadimos el enlace. Nodo->Link
            result=enlace; //Lo devolvemos
        }
        return result;
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return Tipo de objeto/concepto
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo Tipo de objeto/concepto. Es el atributo POS de una palabra (DWord.POS_)
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return Palabra del diccionario asociada con este nodo
     */
    public DWord getWord() {
        return word;
    }

    /**
     * @param word Asocia una palabra del diccionario con este nodo
     */
    public void setWord(DWord word) {
        this.word = word;
    }

    /**
     * @return Lista de enlaces salientes de este nodo.
     */
    public ArrayList<Link> getLinksSalientes() {
        return linksSalientes;
    }

    /**
     * Obtiene la lista de todos los nodos destino a los que apunta este nodo.
     * Si se indica el tipo de enlace primitivo (Link.LINK_), se filtran por ello; si es nulo o vacío, no consideramos el tipo del enlace.
     * @param primitiveType (Opcional) Tipo primitivo (Link.LINK_) de los enlaces salientes de este nodo, que se desea transitar para llegar a los nodos destino. Si este parámetro es nulo, no se considera el tipo.
     * @return Lista de nodos destino, enlazados con este nodo, a través de enlaces que tienen el tipo primitivo especificado (si se indicó alguno).
     */
    public ArrayList<Node> getNodesDestino(String primitiveType) {
        ArrayList<Node> result=new ArrayList();
        for (Link lnk:this.linksSalientes){
            if (!StringUtil.isEmpty(primitiveType)) { //Si se indicó Tipo Primitivo, filtramos por él
                if (lnk.getTipoPrimitivo().equals(primitiveType)){ //Si el tipo primitivo coincide
                    result.add(lnk.getNodeSaliente()); //Añadimos al resultado todos los nodos a los que lleva este enlace
                    lnk.getNodeSaliente().setNegado(lnk.isNegado()); //Establecemos si está negado en base al link
                }//Si coincide el tipo primitivo
            }else{
                    //Si no se indicí tipo primitivo, añadimos el nodo directamente
                    result.add(lnk.getNodeSaliente()); //Añadimos al resultado todos los nodos a los que lleva este enlace
                    lnk.getNodeSaliente().setNegado(lnk.isNegado()); //Establecemos si está negado en base al link
            } //Si se indicó tipo primitivo
        }//for
        return result;
    }

    /**
     * Obtiene la lista de todos los nodos origen que apuntan a este nodo.
     * Si se indica el tipo de enlace primitivo (Link.LINK_), se filtran por ello; si es nulo o vacío, no consideramos el tipo del enlace.
     * @param primitiveType (Opcional) Tipo primitivo (Link.LINK_) de los enlaces entrantes de este nodo, que se desea transitar para llegar a los nodos origen. Si este parámetro es nulo, no se considera el tipo.
     * @return Lista de nodos origen, enlazados con este nodo, a través de enlaces que tienen el tipo primitivo especificado (si se indicó alguno).
     */
    public ArrayList<Node> getNodesOrigen(String primitiveType) {
        ArrayList<Node> result=new ArrayList();
        for (Link lnk:this.linksEntrantes){
            if (!StringUtil.isEmpty(primitiveType)) { //Si se indicó Tipo Primitivo, filtramos por él
                if (lnk.getTipoPrimitivo().equals(primitiveType)){ //Si el tipo primitivo coincide
                    result.add(lnk.getNodeEntrante()); //Añadimos al resultado el origen del enlace concordante
                    lnk.getNodeEntrante().setNegado(lnk.isNegado()); //Establecemos si está negado en base al link
                }//Si coincide el tipo primitivo
            }else{
                    //Si no se indicí tipo primitivo, añadimos el nodo directamente
                    result.add(lnk.getNodeEntrante()); //Añadimos al resultado todos los nodos a los que lleva este enlace
                    lnk.getNodeEntrante().setNegado(lnk.isNegado()); //Establecemos si está negado en base al link
            } //Si se indicó tipo primitivo
         }//for
        return result;
    }


    /**
     * @param primitiveType Tipo primitivo (Link.LINK_) de los enlaces salientes de este nodo que se desea obtener.
     * @return Lista de enlaces salientes de este nodo, que tienen el tipo primitivo especificado.
     */
    public ArrayList<Link> getLinksSalientes(String primitiveType) {
        ArrayList<Link> result=new ArrayList();
        for (Link lnk:this.linksSalientes){
            if (lnk.getTipoPrimitivo().equals(primitiveType)){ //Si el tipo primitivo coincide
                result.add(lnk); //Lo añadimos al resultado
            }
        }//for
        return result;
    }
    /**
     * @param primitiveType Tipo primitivo (Link.LINK_) de los enlaces entrantes de este nodo que se desea obtener.
     * @return Lista de enlaces entrantes de este nodo, que tienen el tipo primitivo especificado.
     */
    public ArrayList<Link> getLinksEntrantes(String primitiveType) {
        ArrayList<Link> result=new ArrayList();
        for (Link lnk:this.linksEntrantes){
            if (lnk.getTipoPrimitivo().equals(primitiveType)){ //Si el tipo primitivo coincide
                result.add(lnk); //Lo añadimos al resultado
            }
        }//for
        return result;
    }

    /**
     * @param linksSalientes Añade cada elemento a la lista de enlaces salientes (sin duplicar, es decir, si ya existe un enlace no lo crea).
     */
    public void setLinksSalientes(ArrayList<Link> linksSalientes) {
        if (linksSalientes!=null&&!linksSalientes.isEmpty()){
            for (Link lnk:linksSalientes){
                addLinkSaliente(lnk);
            }//for
        }
    }

    /**
     * @return Lista de enlaces entrantes de este nodo.
     */
    public ArrayList<Link> getLinksEntrantes() {
        return linksEntrantes;
    }

    /**
     * @param linksEntrantes Añade cada elemento a la lista de enlaces entrantes (sin duplicar, es decir, si ya existe un enlace no lo crea).
     */
    public void setLinksEntrantes(ArrayList<Link> linksEntrantes) {
        if (linksEntrantes!=null&&!linksEntrantes.isEmpty()){
            for (Link lnk:linksEntrantes){
                addLinkEntrante(lnk);
            }//for
        }
    }

    /**
     * Obtiene una descripción textual de este nodo.
     * @return Descripción textual de este nodo.
     */
    @Override
    public String toString(){
        String result="Nodo["+id+"]: "+this.getWord().getText()+"; Links entrantes: ";
        for (Link l:this.linksEntrantes){
            result+=l.getVerb().getText()+"["+l.getTipoPrimitivo()+"]["+l.getId()+"] ";
        }
        result+="; Links salientes: ";
        for (Link l:this.linksSalientes){
            result+=l.getVerb().getText()+"["+l.getTipoPrimitivo()+"]["+l.getId()+"] ";
        }
        return result;
    }

    /**
     * @return Cuantificador (Quantifier.QUANT_) que aplica a este elemento.
     */
    public String getCuantificador() {
        return cuantificador;
    }

    /**
     * @param cuantificador Cuantificador (Quantifier.QUANT_) que aplicaremos a este elemento.
     */
    public void setCuantificador(String cuantificador) {
        this.cuantificador = cuantificador;
    }

    /**
     * @return True si este nodo está negado. AVISO: no se emplea en cálculos lógicos, como sí lo hace el flag del Link.
     */
    public boolean isNegado() {
        return negado;
    }

    /**
     * @param negado Establece el flag "negado" de este Nodo. AVISO: no se emplea en cálculos lógicos, como sí lo hace el flag del Link.
     */
    public void setNegado(boolean negado) {
        this.negado = negado;
    }

}//Class
