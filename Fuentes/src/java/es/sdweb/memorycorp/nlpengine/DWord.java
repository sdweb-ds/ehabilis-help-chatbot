package es.sdweb.memorycorp.nlpengine;

import es.sdweb.application.componentes.util.Booleano;
import es.sdweb.application.componentes.util.StringUtil;
import java.util.ArrayList;

/**
 * Representa una palabra del diccionario y cierta metainformación sobre la misma.
 * @author Antonio Carro Mariño
 */
public class DWord {
    private int index=0; //Posicion de la palabra dentro de un Dictionary
    
    private String raiz=""; //Raiz (lexema): habl- (hablo, hablas, habla, ...)
    private String text=""; //Palabra: hablar

    private ArrayList<String> POS=new ArrayList(); //Tipo de palabra que es: ADJ, ADV, ADP, etc. Deberán utilizarse las constantes estáticas POS de esta misma clase.
    //Una palabra polisémica puede ser de varios tipos a la vez. P.e. sierra (conjugación del verbo serrar), sierra (coordillera), sierra (herramienta)
    
    private Booleano genero=null; //MASCULINO=true; FEMENINO=false; NULL=no definido
    private Booleano numero=null; //SINGULAR=true; PLURAL=false; NULL=no definido
    
    private Booleano contable=null; //CONTABLE=true; NO_CONTABLE=false; NULL=desconocido
    private Booleano colectivo=null; //COLECTIVO=true; NO_COLECTIVO=false; NULL=desconocido
    
    private DWord palabraGeneroOpuesto=null; //enlaza la palabra del género opuesto (si existe y se conoce). Si esta palabra es masculina, esta variable apunta a la palabra femenina, y viceversa.
    //palabraNumeroOpuesto enlaza la palabra del género opuesto (si existe y se conoce). Si esta palabra es singular, esta variable apunta a la palabra en plural, y viceversa.
    //Puede haber más de un plural, pero solo existe un singular. Hombre-Mujer se modelaria como antónimos, ya que no sigue la regla para generar el género.
    private ArrayList<DWord> palabrasNumeroOpuesto=new ArrayList(); 
    
    private ArrayList<DWord> sinonimos=new ArrayList(); //Lista de sinóminos de esta palabra. Deben coincidir en número (siempre) y género (normalmente) (p.e. si esta palabra es plural, enlazará una lista de plurales).
    private ArrayList<DWord> antonimos=new ArrayList(); //Lista de antónimos de esta palabra. Deben coincidir en número al menos.
    private ArrayList<DWord> derivadas=new ArrayList(); //Lista de palabras relacionadas. P.e. belleza -> bello, bella, bellísimo, bellísima, bellamente, etc. P.e. coche -> cochazo, cochecito, etc.
    private DWord derivaDe=null; //Palabra de la que deriva ésta. Es null si es la palabra raiz.
    
    //********* ETIQUETAS POS ESTANDAR**************
    public final static String POS_ADJ="ADJ"; //Adjetivos
    public final static String POS_ADV="ADV"; //Adverbio
    public final static String POS_ADP="ADP"; //Preposición
    public final static String POS_AUX="AUX"; //Verbo auxuliar: ser, poder, deber, etc. 
    public final static String POS_CCONJ="CCONJ"; //Conjunción coordinativa: y, o, etc.
    public final static String POS_DET="DET"; //Determinante
    public final static String POS_NUM="NUM"; //Numeral
    public final static String POS_PRON="PRON"; //Pronombre
    public final static String POS_NOUN="NOUN"; //Nombre
    public final static String POS_PROPN="PROPN"; //Nombre propio
    public final static String POS_PUNCT="PUNCT"; //Signo de puntuación
    public final static String POS_SCONJ="SCONJ"; //Conjunción subordinativa que precede a una frase subordinada
    public final static String POS_SYM="SYM"; //Símbolos: $, %, €, etc.
    public final static String POS_VERB="VERB"; //Verbo

    //********* ETIQUETAS POS AÑADIDAS *************
    public final static String POS_PROPNL="PROPNL"; //Nombre propio de lugar.
    public final static String POS_QUAN="QUAN"; //Cuantificador. P.e. "Muy", "Poco", etc.    

    /**
     * Crea una palabra de Diccionario.
     * @param text Palabra
     * @param raiz Lexema de la palabra si se conoce. En caso contrario, es la misma palabra.
     */
    public DWord (String text, String raiz){
        this.raiz=(raiz==null?"":raiz);
        this.text=(text==null?"":text);
    }

    /**
     * Crea una palabra de Diccionario.
     * @param text Palabra. El lexema en este caso se establecerá igual a la palabra.
     */
    public DWord (String text){
        this.raiz=(text==null?"":text);
        this.text=(text==null?"":text);
    }

    /**
     * Crea una palabra de Diccionario.
     * @param text Palabra
     * @param raiz Lexema de la palabra si se conoce. En caso contrario, es la misma palabra.
     * @param genero Para masculino true, para femenino false, null para indefinido
     * @param numero Para singular true, para plural false, null para indefinido
     */
    public DWord (String text, String raiz, Booleano genero, Booleano numero){
        this.raiz=(raiz==null?"":raiz);
        this.text=(text==null?"":text);
        this.genero=genero;
        this.numero=numero;
    }
    
    /**
     * Indica si una palabra que representa un objeto/concepto (NOUN), es contable (coche) o no lo es (azúcar).
     * AVISO: esta función no puede detectar si está sin definir. Para ello usar getContable()
     * @return True si es contable.
     */
    public boolean isContable(){
        boolean result=(this.contable!=null?this.contable.isBool():false);
        return result;
    }
    
    /**
     * Indica si una palabra que representa un objeto/concepto (NOUN), es un colectivo de objetos contables.
     * AVISO: esta función no puede detectar si está sin definir. Para ello usar getColectivo()
     * @return True si es contable.
     */
    public boolean isColectivo(){
        boolean result=(this.colectivo!=null?this.colectivo.isBool():false);
        return result;
    }
    
    /**
     * Indica si una palabra especificada es sinónima de esta.
     * @param word Palabra de la que se quiere saber si es sinónima.
     * @return True si la palabra es sinónima.
     */
    public boolean isSinonimo(String word){
        word=word.toLowerCase();
        boolean result=false;
        for (DWord w:sinonimos){ //Buscamos secuencialmente la palabra
           if (w.getText().equals(word)){
               result=true;
           }
        }//for
        return result;
    }
    
    /**
     * Indica si una palabra especificada es antónima de esta.
     * @param word Palabra de la que se quiere saber si es antónima.
     * @return True si la palabra es antónima.
     */
    public boolean isAntonimo(String word){
        word=word.toLowerCase();
        boolean result=false;
        for (DWord w:antonimos){ //Buscamos secuencialmente la palabra
           if (w.getText().equals(word)){
               result=true;
           }
        }//for
        return result;
    }
    
    /**
     * Indica si una palabra especificada es derivada de esta.
     * @param word Palabra de la que se quiere saber si es derivada.
     * @return True si la palabra es derivada.
     */
    public boolean isDerivada(String word){
        word=word.toLowerCase();
        boolean result=false;
        for (DWord w:getDerivadas()){ //Buscamos secuencialmente la palabra
           if (w.getText().equals(word)){
               result=true;
           }
        }//for
        return result;
    }


    /**
     * Añade una palabra como sinónimo de esta (si no existe). Y enlaza ambas palabras.
     * @param word Palabra a añadir a la lista de sinónimos.
     */
    public void addSinonimo(DWord word){
        if (!isSinonimo(word.getText())){ //Si ya está como sinónimo, no lo duplicamos. Este "if" evita un bucle infinito
            sinonimos.add(word); //Lo añadimos a la lista
            word.addSinonimo(this); //Y a la propia palabra también le indicamos que es sinónima de esta
        }
    }

    /**
     * Añade una palabra como antónimo de esta (si no existe). Y enlaza ambas palabras.
     * @param word Palabra a añadir a la lista de antónimos.
     */
    public void addAntonimo(DWord word){
        if (!isAntonimo(word.getText())){ //Si ya está como antónimos, no lo duplicamos. Este "if" evita un bucle infinito
            antonimos.add(word); //La añadimos a la lista
            word.addAntonimo(this); //Y a la propia palabra también le indicamos que es antónima de esta
        }
    }

    /**
     * Añade una palabra como derivada de esta (si no existe). Y enlaza ambas palabras.
     * @param word Palabra a añadir a la lista de derivadas.
     */
    public void addDerivada(DWord word){
        if (!isDerivada(word.getText())){ //Si ya está como derivada, no la duplicamos. 
            getDerivadas().add(word); //La añadimos a la lista
            word.setDerivaDe(this); //E indicamos que esta palabra es de la que deriva la añadida
        }
    }

    /**
     * Establece el género masculino.
     */
    public void setGeneroMasculino(){
        this.genero=new Booleano(true);
    }
    
    /**
     * Establece el género femenino.
     */
    public void setGeneroFemenino(){
        this.genero=new Booleano(false);
    }
    
    /**
     * Indica si es género masculino.
     * @return Si es femenino o indefinido devuelve falso. Si es masculino devuelve verdadero.
     */
    public boolean isGeneroMasculino(){
        boolean result=(getGenero()!=null?this.getGeneroValue().isBool():false); //Si es femenino o indefinido es falso
        return result;
    } 
    
    /**
     * Indica si es género femenino.
     * @return Si es masculino o indefinido devuelve falso. Si es femenino devuelve verdadero.
     */
    public boolean isGeneroFemenino(){
        boolean result=(getGenero()!=null?!this.genero.isBool():false); //Si es masculino o indefinido es falso
        return result;
    } 
    
    /**
     * Indica si el género no está definido.
     * @return Devuelve true si el género no está definido, false en caso contrario.
     */
    public boolean isGeneroUndefined(){
        return (getGenero()==null);
    } 

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the raiz
     */
    public String getRaiz() {
        return raiz;
    }

    /**
     * @param raiz the raiz to set
     */
    public void setRaiz(String raiz) {
        this.raiz = raiz;
    }

    /**
     * @return Devuelve el genero: true=masculino, false=femenino, null=neutro o no definido.
     */
    public Booleano getGeneroValue() {
        return genero;
    }

    /**
     * @param genero Establece el genero: true=masculino, false=femenino, null=neutro o no definido.
     */
    public void setGeneroValue(Booleano genero) {
        this.genero = genero;
    }

    /**
     * @return Devuelve el numero: true=singular, false=plural, null=neutro o no definido.
     */
    public Booleano getNumeroValue() {
        return numero;
    }

    /**
     * @param numero Establece el numero: true=singular, false=plural, null=neutro o no definido.
     */
    public void setNumeroValue(Booleano numero) {
        this.numero = numero;
    }
    
    /**
     * Establece la palabra como singular (numero).
     */
    public void setNumeroSingular(){
        this.numero = new Booleano(true);
    }
    
    /**
     * Establece la palabra como plural (numero).
     */
    public void setNumeroPlural(){
        this.numero = new Booleano(false);
    }
    
    /**
     * Establece el número de la palabra como no definido.
     */
    public void setNumeroNoDefinido(){
        this.numero = null;
    }

    /**
     * @return the palabraGeneroOpuesto
     */
    public DWord getPalabraGeneroOpuesto() {
        return palabraGeneroOpuesto;
    }

    /**
     * Establece la palabra de género opuesto, y enlaza ambas.
     * @param word Palabra de género opuesto
     */
    public void setPalabraGeneroOpuesto(DWord word) {
        this.palabraGeneroOpuesto = word;
    }

    /**
     * @return Devuelve el listado de palabras con número opuesto. Puede haber solo un singular, pero dos plurales para una palabra.
     */
    public ArrayList<DWord> getPalabrasNumeroOpuesto() {
        return palabrasNumeroOpuesto;
    }
    
    /**
     * Indica si una palabra dada es singular o plural (según sea el caso) de la presente palabra.
     * @param word Palabra a analizar.
     * @return True si la palabra es de número opuesto a la presente. False en caso contrario.
     */
    public boolean isPalabraNumeroOpuesto(DWord word){
        boolean result=false;
        for (DWord w: this.palabrasNumeroOpuesto){
            if (w.getText().equals(word.getText())){ //Si está presente la palabra
                result=true;
            }
        }
        return result;
    }

    /**
     * Establece las palabras de número opuesto, y las enlaza.
     * @param lword Lista de palabras de número opuesto a esta. Puede contener una (p.e. perros) o dos (p.e. jabalís, jabalíes).
     */
    public void setPalabrasNumeroOpuesto(ArrayList<DWord> lword) {
        if (!lword.isEmpty()){ //Si el parámetro de entrada no está vacío
            for (DWord w:lword){
                if (!this.isPalabraNumeroOpuesto(w)){ //Si no está repetida
                    this.palabrasNumeroOpuesto.add(w); //La añadimos
                    w.getPalabrasNumeroOpuesto().add(this); //Y enlazamos también la palabra añadida w. Pues si no está en esta lista, tampoco puede estar en la de "w".
                }
            }//for
        }//if
    }

    /**
     * @return Array de palabras que son sinónimas de esta
     */
    public ArrayList<DWord> getSinonimos() {
        return sinonimos;
    }
    
    /**
     * Obtiene un ArrayList de Strings con los sinónimos de esta palabra.
     * @return ArrayList de Strings de sinónimos
     */
    public ArrayList<String> getSinonimosStr() {
        ArrayList<String> result=new ArrayList();
        for (DWord w:this.sinonimos){
            result.add(w.getText());
        }//for
        return result;
    }
    

    /**
     * @return the antonimos
     */
    public ArrayList<DWord> getAntonimos() {
        return antonimos;
    }
    
    /**
     * Obtiene un ArrayList de Strings con los antónimos de esta palabra.
     * @return ArrayList de Strings de antónimos
     */
    public ArrayList<String> getAntonimosStr() {
        ArrayList<String> result=new ArrayList();
        for (DWord w:this.antonimos){
            result.add(w.getText());
        }//for
        return result;
    }
    

    /**
     * @return Devuelve los tipos de esta palabra. 
     */
    public ArrayList<String> getAllPOS() {
        return POS;
    }
    
    /**
     * Devuelve el POS de la palabra, la primera ocurrencia en la lista de POS asociada a la misma.
     * @return el POS de la palabra. Devuelve nulo si no tiene POS.
     */
    public String getPOS(){
        String result=null;
        if (!this.POS.isEmpty()){
            result=this.POS.get(0); //Cogemos el primer POS que hay.
        }
        return result;
    }

    /**
     * Indica si una palabra (alguna de sus acepciones/significados) es del tipo POS especificado.
     * @param tipoPOS Tipo POS. Este valor debe especificarse a partir de las variables estáticas de esta clase.
     * @return True si la palabra está asiciada al POS indicado. False en caso contratrio.
     */
    public boolean isPOS(String tipoPOS){
       boolean result=false;
       for (String pos:this.POS){
           if (pos.equals(tipoPOS)){
               result=true;
           }
       }//for
       return result;
    }
    
    /**
     * @param tipoPOS Asocia esta palabra con un nuevo tipo POS. Si ya existe, no se añade.
     */
    public void addPOS(String tipoPOS) {
        if (!StringUtil.isEmpty(tipoPOS)){
            if (!isPOS(tipoPOS)){ //Si no está asociada a este tipo POS
                this.POS.add(tipoPOS); //Lo añadimos
            }
        }//Si no es vacio
    }
    
    /**
     * Devuelve los principales valores de esta palabra.
     * @return Valores de la palabra.
     */
    @Override
    public String toString(){
        //Sinónimos
        String lsinonimos="";
        for (DWord w:this.sinonimos){
            lsinonimos+="["+w.getText()+"] ";
        }//for

        //Antónimos
        String lantonimos="";
        for (DWord w:this.antonimos){
            lantonimos+="["+w.getText()+"] ";
        }//for

        //Lista de palabras de número opuesto
        String lnumero="";
        for (DWord w:this.palabrasNumeroOpuesto){
            lnumero+="["+w.getText()+"] ";
        }//for
        
        //Genero opuesto
        String genop=(this.palabraGeneroOpuesto!=null?this.palabraGeneroOpuesto.getText():"");

        String result="TXT: "+this.text+"; RAIZ: "+this.raiz+"; POS: "+this.POS+"; GEN: "+this.getGenero()+"; NUM: "+this.getNumero()+
                "; NUM.OPUESTO: "+lnumero+"; GEN.OPUESTO: "+genop+ "; SINONIMOS: "+lsinonimos+"; ANTONIMOS: "+lantonimos;
        return result;
    }
    
    /**
     * Devuelve un string con el género de la palabra: MASCULINO, FEMENINO, NO_DEFINIDO
     * @return Cadena de texto con el género.
     */
    public String getGenero(){
        String result="NO_DEFINIDO";
        if (this.genero!=null){
           result=(this.genero.isBool()?"MASCULINO":"FEMENINO");
        }
        return result;
    }
    
    
    /**
     * Devuelve un string con el numero de la palabra: SINGULAR, PLURAL, NO_DEFINIDO
     * @return Cadena de texto con el género.
     */
    public String getNumero(){
        String result="NO_DEFINIDO";
        if (this.numero!=null){
           result=(this.numero.isBool()?"SINGULAR":"PLURAL");
        }
        return result;
    }

    /**
     * @return the contable
     */
    public Booleano getContable() {
        return contable;
    }

    /**
     * @param contable the contable to set
     */
    public void setContable(Booleano contable) {
        this.contable = contable;
    }

    /**
     * @return the colectivo
     */
    public Booleano getColectivo() {
        return colectivo;
    }

    /**
     * @param colectivo the colectivo to set
     */
    public void setColectivo(Booleano colectivo) {
        this.colectivo = colectivo;
    }

    /**
     * @return Posición de la palabra dentro de un Dictionary. Atributo de uso interno de nlpengine.
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index Posicion dentro de un Dictionary. Atributo de uso interno de nlpengine.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @param sinonimos Lista de sinónimos de esta palabra. Aquellos que ya existan, no se duplican.
     */
    public void setSinonimos(ArrayList<DWord> sinonimos) {
        for (DWord w:sinonimos){
            this.addSinonimo(w);
        }
    }

    /**
     * @param derivadas Lista de derivadas de esta palabra. Aquellas que ya existan, no se duplican.
     */
    public void setDerivadas(ArrayList<DWord> derivadas) {
        for (DWord w:derivadas){
            this.addDerivada(w);
        }
    }

    /**
     * @param antonimos Lista de antónimos de esta palabra. Aquellos que ya existan, no se duplican.
     */
    public void setAntonimos(ArrayList<DWord> antonimos) {
        for (DWord w:antonimos){
            this.addAntonimo(w);
        }
    }

    /**
     * @return the derivadas
     */
    public ArrayList<DWord> getDerivadas() {
        return derivadas;
    }

    /**
     * @return Palabra de la que deriva ésta.
     */
    public DWord getDerivaDe() {
        return derivaDe;
    }

    /**
     * @param derivaDe Establece la palabra de la que deriva esta. P.e. Bello deriva de Belleza. Cochazo deriva de coche.
     */
    public void setDerivaDe(DWord derivaDe) {
        this.derivaDe = derivaDe;
    }
}//class
