package es.sdweb.memorycorp.nlpengine;

import es.sdweb.application.componentes.util.ObjectUtil;
import es.sdweb.application.componentes.util.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Representa a una palabra o signo lingüístico dentro de una oración. Esta palabra o signo
 * puede estar enlazado con otros términos dentro de una oración, formando una
 * estructura de dependencias en árbol, que refleja la estructura de la oración.
 * @author Antonio Carro Mariño
 */
public class LTermino {
    
    public static String[] SIGNOS_PUNTUACION={",",".",";",":","¿","?","!"};
    
    private String id=ObjectUtil.generateId(); //Generamos un Id unico para este término
    private String texto=""; //Palabra
    private String pos=""; //Part of Speech: ADJ, ADV, ADP, ...
    private String ud=""; //Universal Dependencies: ACL, ADVCL, ADVMOD, ...
    private String referencia=""; //Texto adicional al que se vincula este termino. P.e. la conjuncion "y", "pero", etc.
    private List<LTermino> hijos=new ArrayList<>();
    private LTermino padre=null;
    private boolean negado=false; //En el caso de que el término sea un verbo, indica si está negado (true) o no (false)
    private int nivel=0; //nivel del arbol sintactico en donde se ubica este termino: 0(raiz), 1, 2, 3,...
    
    private boolean principal=false; //Indica si es un elemento principal, p.e. el verbo más importante, etc 
    private boolean OI=false; //Indica si es un Objeto Indirecto 

    //********* ETIQUETAS UI ESTANDAR**************
    public final static String UD_ACL="acl"; //Clausula modificadora de nombre
    public final static String UD_ACLREL="acl:relcl"; //Clausula modificadora de nombre relativa
    public final static String UD_ADVMOD="advmod"; //Modificador advervial
    public final static String UD_AMOD="amod"; //Modificador adjetival
    public final static String UD_APPOS="appos"; //Modificador aposicional
    public final static String UD_AUX="aux"; //Partícula auxiliar de un verbo
    public final static String UD_CASE="case"; //Preposición de un complemento
    public final static String UD_CC="cc"; //Conjunción coordinativa
    public final static String UD_CCOMP="ccomp"; //Complemento clausal
    public final static String UD_COMPOUND="compound"; //Composición
    public final static String UD_CONJ="conj"; //Conjunción
    public final static String UD_COP="cop"; //Cópula (verbo copulativo: ser, estar, parecer)
    public final static String UD_CSUBJ="csubj"; //Sujeto clausal
    public final static String UD_CSUBJPASS="csubj:pass"; //Sujeto clausal pasivo
    public final static String UD_DEP="dep"; //Dependencia
    public final static String UD_DET="det"; //Determinante
    public final static String UD_EXPLPASS="expl:pass"; //Pronombre reflexivo usado en pasivo reflexivo
    public final static String UD_EXPLPV="expl:pv"; //Pronombre reflexivo para verbos reflexivos
    public final static String UD_FIXED="fixed"; //Expresión multipalabra
    public final static String UD_FLAT="flat"; //Expresión multipalabra para expresiones sin estructura
    public final static String UD_LIST="list"; //Lista de items comparables
    public final static String UD_MARK="mark"; //Marcador de una oración subordinada
    public final static String UD_NMOD="nmod"; //Modificador de nombre. P.e. hijo(nsubj) de(case) María(nmod)
    public final static String UD_NSUBJ="nsubj"; //Sujeto nominal
    public final static String UD_NUMMOD="nummod"; //Modificador numérico
    public final static String UD_OBJ="obj"; //Objeto Directo/Indirecto
    public final static String UD_OBL="obl"; //Complemento nominal indirecto
    public final static String UD_PARATAXIS="parataxis"; //Parataxis
    public final static String UD_PUNCT="punct"; //Signo de puntuación
    public final static String UD_ROOT="root"; //Raíz de la oración
    public final static String UD_XCOMP="xcomp"; //Complemento clausal abierto
    
    
    
    public boolean isWord(){
        ArrayList<String> signos=(ArrayList<String>) Arrays.asList(SIGNOS_PUNTUACION);
        boolean result=!signos.contains(this.texto);
        return result;
    }
    
    public boolean isVerb(){
        boolean result=pos.equals("VERB")||pos.equals("AUX");
        return result;
    }
    
    public boolean isSubject(){
        boolean result=ud.equals("nsubj");
        return result;
    }
    
    /**
     * Indica si es el Objeto (Directo o Indirecto) de la oración global, o de una subordinada, o de una coordinada.
     * @return Verdadero si lo es
     */
    public boolean isObject(){
        boolean result=ud.equals("obj");
        return result;
    }
    
    /**
     * Indica si es el Objeto Directo de la oración global, o de una subordinada, o de una coordinada.
     * @return Verdadero si lo es
     */
    public boolean isObjectDirecto(){
        boolean result=ud.equals("obj")&&!OI;
        return result;
    }
    
    /**
     * Indica si es el Objeto Directo principal de la oración global.
     * @return Verdadero si lo es
     */
    public boolean isObjectDirectoPrincipal(){
        boolean result=ud.equals("obj")&&this.principal&&!OI;
        return result;
    }
    
    /**
     * Indica si es el Objeto InDirecto de la oración global, o de una subordinada, o de una coordinada.
     * @return Verdadero si lo es
     */
    public boolean isObjectIndirecto(){
        boolean result=ud.equals("obj")&&OI;
        return result;
    }
    
    /**
     * Indica si es el Objeto InDirecto principal de la oración global.
     * @return Verdadero si lo es
     */
    public boolean isObjectIndirectoPrincipal(){
        boolean result=ud.equals("obj")&&this.principal&&OI;
        return result;
    }
    
    public boolean isPunc(){
        boolean result=!isWord();
        return result;
    }
    
    public void addChild(LTermino term){
        hijos.add(term);
    }
    
    /**
     * @return the texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * @param texto the texto to set
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * @return the pos
     */
    public String getPos() {
        return pos;
    }

    /**
     * @param pos the pos to set
     */
    public void setPos(String pos) {
        this.pos = pos;
    }

    /**
     * @return the ud
     */
    public String getUd() {
        return ud;
    }

    /**
     * @param ud the ud to set
     */
    public void setUd(String ud) {
        this.ud = ud;
    }

    /**
     * @return the hijos
     */
    public List<LTermino> getHijos() {
        return hijos;
    }

    /**
     * @param hijos the hijos to set
     */
    public void setHijos(List<LTermino> hijos) {
        this.hijos = hijos;
    }

    /**
     * @return the padre
     */
    public LTermino getPadre() {
        return padre;
    }

    /**
     * @param padre the padre to set
     */
    public void setPadre(LTermino padre) {
        this.padre = padre;
    }

    /**
     * @return the referencia
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * @param referencia the referencia to set
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * @return the nivel
     */
    public int getNivel() {
        return nivel;
    }

    /**
     * @param nivel the nivel to set
     */
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    
    @Override
    public String toString(){
        String aux=(StringUtil.isEmpty(referencia)?")":":"+referencia+")");
        String result=StringUtil.padding(nivel*2)+"["+nivel+"]> "+texto+"/"+pos+" ("+ud+aux;
        return result;
    }
    
    /**
     * Devuelve una cadena con el contenido de todo el arbol a partir de este nodo
     * @return 
     */
    public String toStringTree(){
        String result=toString()+"\n";
        for (LTermino t : hijos){
            result+=t.toStringTree();
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
     * @return the principal
     */
    public boolean isPrincipal() {
        return principal;
    }

    /**
     * @param principal the principal to set
     */
    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    /**
     * @param isOI Indica si es un objeto indirecto.
     */
    public void setObjectIndirecto(boolean isOI) {
        this.OI = isOI;
    }

    /**
     * @return En el caso de que el término sea un verbo, indica si está negado (true) o no (false)
     */
    public boolean isNegado() {
        return negado;
    }

    /**
     * @param negado En el caso de que el término sea un verbo, indica si está negado (true) o no (false)
     */
    public void setNegado(boolean negado) {
        this.negado = negado;
    }
    
}//class
