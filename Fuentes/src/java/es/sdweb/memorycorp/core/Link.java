package es.sdweb.memorycorp.core;

import es.sdweb.application.componentes.util.ObjectUtil;
import es.sdweb.memorycorp.nlpengine.DVerb;
import es.sdweb.memorycorp.nlpengine.LOracion;
import java.util.ArrayList;

/**
 * Representa una relación dentro de la red semántica. Una relación es una ACCION (p.e. "ser", "tener", etc).
 * Un enlace siempre representa a un verbo (en sentido activo o pasivo según el sentido de la navegación).
 * @author Antonio Carro Mariño
 */
public class Link {
    
    //********************* ENLACES PRIMITIVOS ************************
    //Se trata de acciones especiales que, por su naturaleza esencial, deben tener un tratamiento muy especializado
    //Enlaces para representar las taxonomías
    public static String LINK_ES_SUBCLASE="ES_SUBCLASE_DE";
    public static String LINK_ES_SUPERCLASE="ES_SUPERCLASE_DE";
    
    //Enlaces para representar la agregación
    public static String LINK_ES_AGREGACION="ES_AGREGACION_DE";
    public static String LINK_ES_PARTE="ES_PARTE_DE";
    
    //Enlace para representar una cualidad "permanente" y propia del objeto. P.e. alto, bajo, gordo, feo, etc. p.e "Juan es hijo de Ana"
    //Esta cualidad será un ADJ. que es propio de del objeto, o un nombre (NOUN), p.e. "color", asociado a un ADJ, p.e. "azul".
    public static String LINK_ES_CUALIDAD="ES_CUALIDAD_DE";
    //Enlace para representar una cualidad "temporal" de un objeto, p.e "Juan es empleado de SDWEB", etc.
    public static String LINK_ES_CUALIDAD_TEMPORAL="ES_CUALIDAD_TEMPORAL";
    
    //Enlace para representar la tenencia de un objeto por parte de un ser vivo. Si el ser es inerte o se habla del propio cuerpo 
    //del ser VIVO (p.e. el perro tiene patas), se trata de agregación, no de tenencia.
    public static String LINK_TIENE_PROPIEDAD="TIENE_PROPIEDAD";
    public static String LINK_TIENE_PROPIEDAD_INV="ES_PROPIEDAD_DE";
    
    //******************************************************************
    
    private String id=ObjectUtil.generateId(); //Generamos un Id unico para este objeto
    private String linkKey="";
    private String tipoPrimitivo=""; //Adopta valores Link.LINK_
    private DVerb verb=null; //Un enlace siempre representa a un verbo (en sentido activo o pasivo según el sentido de la navegación)
    private boolean negado=false; //Indica si el verbo está negado (true) o no (false)
    private int peso=0; //Numero de veces que se ha reforzado este enlace. Es un número entre 0 y 10.000

    //Relaciones de este Link con los Nodos. La realción siempre se produce a través de un enlace (LINK).
    private ArrayList<LOracion> origen=new ArrayList(); //Origen de la información que dió lugar a este conocimiento. El tamaño de este array es indicativo del peso de esta conexión.
    private Node nodeSaliente=null; //Enlaces salientes (nevegación saliente del nodo).
    private Node nodeEntrante=null; //Enlace entrante (navegación entrante desde el nodo origen). Los enlaces se duplican, así que solo hay un nodo origen.
    private Node nodeOI=null; //Almacena el objeto (indirecto) principal beneficiario de la acción
    private Node nodeCcLugar=null; //Almacena el Complemento Circustancial de Lugar principal implicado en la acción: Dónde sucede.
    
    
    public Link(DVerb verb, boolean negado){
        this.verb=verb; //Acción representada por este enlace
        this.negado=negado; //Indicamos si está negada (true) o no (false)
    }
    
    /**
     * Función que compara dos enlaces: es una coparación directa de comparación de igualdad de punteros. 
     * @param enlace Enlace a comparar con éste. 
     * @return True si son iguales, false en caso contrario.
     */
    public boolean equalsPointer(Link enlace){
        boolean result=(this==enlace); //
        return result;
    }
    
    /**
     * Función que compara dos enlaces: compara que el verbo sea el mismo, y que el origen y destino (sus punteros) también lo sean. 
     * @param enlace Enlace a comparar con éste. 
     * @return True si son iguales, false en caso contrario.
     */
    public boolean equals(Link enlace){
        boolean result=(this.getVerb().equals(enlace.getVerb())&&
                        this.getNodeEntrante()==enlace.getNodeEntrante()&&
                        this.getNodeSaliente()==enlace.getNodeSaliente()); //Comparamos con igualdad plena, a través de la comparación de punteros y el verbo en sí.
        return result;
    }
    
    /**
     * @return Devuelve la meta información de "tipo primitivo", que concreta el sentido estructural de esta acción.
     */
    public String getTipoPrimitivo() {
        return tipoPrimitivo;
    }
    
    /**
     * @param tipoPrimitivo Debe especificarse un valor del tipo Link.LINK_
     */
    public void setTipoPrimitivo(String tipoPrimitivo) {
        this.tipoPrimitivo = tipoPrimitivo;
    }

    /**
     * @return the verb
     */
    public DVerb getVerb() {
        return verb;
    }

    /**
     * @param verb the verb to set
     */
    public void setVerb(DVerb verb) {
        this.verb = verb;
    }

    /**
     * Añade una oración que da origen a este conocimiento. No importa que el texto de la oración se repita.
     * Cuantos más orígenes, mayor es el refuerzo de este enlace. El tamaño de la lista de orígenes es indicativo del peso
     * de este enlace.
     * @param oracion  Oración que dió origen a este conocimiento.
     */
    public void addOrigen(LOracion oracion){
        this.origen.add(oracion); //Añadimos un nuevo origen (no importa que se repita la frase textual original)
    }
    
    /**
     * @return Nodo saliente de este enlace.
     */
    public Node getNodeSaliente() {
        return nodeSaliente;
    }
    
    /**
     * Añade el nodo destino del enlace.
     * @param destino Nodo a añadir como destino del enlace.
     */
    public void setNodeSaliente(Node destino){
        this.nodeSaliente=destino;
    }

    /**
     * @param nodeEntrante Nodo origen de este enlace
     */
    public void setNodeEntrante(Node nodeEntrante) {
        this.nodeEntrante = nodeEntrante;
    }
    
    /**
     * Clona un Link, excluyendo sus enlaces entrantes y salientes. AVISO: Tanto en el DeepPlane, como en el RealityPlane
     * los enlaces deben ser clonados antes de ser almacenados en dichos planos.
     * @return Link clonado.
     */
    public Link clone(){
        Link result=new Link(this.verb,this.negado);
        result.tipoPrimitivo=this.tipoPrimitivo;
        return result;
    }

    /**
     * @return the nodeEntrante
     */
    public Node getNodeEntrante() {
        return nodeEntrante;
    }

    /**
     * Oraciones que dieron origen a este conocimiento. El tamaño de esta lista es indicativo del peso de este enlace.
     * @return Lista de las oraciones que dieron origen a este conocimiento.
     */
    public ArrayList<LOracion> getOrigen() {
        return origen;
    }
    
    /**
     * @return Indica si está negado (true) o no (false)
     */
    public boolean isNegado() {
        return negado;
    }

    /**
     * @param negado Indica si está negado (true) o no (false)
     */
    public void setNegado(boolean negado) {
        this.negado = negado;
    }
    
    /**
     * Devuelve una descripción del enlace.
     * @return Descripción textual del enlace.
     */
    @Override
    public String toString(){
        String result="Link["+id+"]: "+this.getVerb().getText()+"; Tiempo: "+this.getVerb().getTiempo()+"; Tipo Primitivo: "+
                      this.getTipoPrimitivo()+"; Sujeto: ";
        if (this.getNodeEntrante()!=null){
            result+=this.getNodeEntrante().getWord().getText()+"["+this.getNodeEntrante().getId()+"] ";
        }
        result+="; Objeto Directo/Cualidad: ";
        if (this.getNodeSaliente()!=null){
            result+=this.getNodeSaliente().getWord().getText()+"["+this.getNodeSaliente().getId()+"] ";
        }
        result+="; Objeto Indirecto: "+(this.nodeOI!=null?this.nodeOI.getWord().getText():"");
        result+="; CC Lugar: "+(this.nodeCcLugar!=null?this.nodeCcLugar.getWord().getText():"");
        return result;
    }

    /**
     * @return Cadena de texto con el ID único de este Enlace.
     */
    public String getId() {
        return id;
    }

    /**
     * @return the peso
     */
    public int getPeso() {
        return peso;
    }

    /**
     * @param peso Establece el peso de este enlace entre 0 y 10.000
     */
    public void setPeso(int peso) {
        peso=(peso<0?0:peso); //Acota a cero
        peso=(peso>10000?10000:peso); //Acota a 10.000
        this.peso = peso;
    }
    
    /**
     * Incrementa el peso de este enlace en 1 cada vez que se menciona. Su valor estará siempre entre 0 y 10.000
     */
    public void incPeso(){
        this.peso++;
    }

    /**
     * @return Devuelve el beneficiario de la acción (puede ser nulo).
     */
    public Node getNodeOI() {
        return nodeOI;
    }

    /**
     * @param nodeOI Establece el beneficiario de la acción (puede ser nulo).
     */
    public void setNodeOI(Node nodeOI) {
        this.nodeOI = nodeOI;
    }

    /**
     * @return Nodo que indica el lugar de la Acción (puede ser nulo)
     */
    public Node getNodeCcLugar() {
        return nodeCcLugar;
    }

    /**
     * @param nodeCcLugar Establece el lugar donde sucede la acción (puede ser nulo)
     */
    public void setNodeCcLugar(Node nodeCcLugar) {
        this.nodeCcLugar = nodeCcLugar;
    }
        
}//Class
