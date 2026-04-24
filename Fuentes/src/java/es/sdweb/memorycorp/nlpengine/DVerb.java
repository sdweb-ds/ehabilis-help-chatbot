package es.sdweb.memorycorp.nlpengine;

import java.util.ArrayList;

/**
 * Especializa una palabra del diccionario, para ampliarlo con las propiedades vinculadas a un verbo.
 * AVISO: Se debe prestar especial atención a los 50 verbos más utilizados: ser estar haber hacer ir tener 
 * poder abrir aprender beber buscar caminar cerrar cocinar comenzar comer conocer creer dar decir encontrar 
 * entender escribir hablar jugar leer llamar llegar mirar necesitar pagar pedir pensar poner preferir quedar 
 * querer saber salir sentarse sentir tomar trabajar traer usar venir ver viajar vivir volver
 * @author Antonio Carro Mariño
 */
public class DVerb extends DWord{
    
    //****** VALORES MAESTROS DE TIEMPO Y MODO ****************
    
    //Formas impersonales
    public final static String TIEMPO_INFINITIVO="INFINITIVO";
    public final static String TIEMPO_PARTICIPIO="PARTICIPIO";
    public final static String TIEMPO_GERUNDIO="GERUNDIO";
    
    //Tiempo: en qué momento se desarrolla la acción (únicamente atendiendo al tiempo verbal en sí, no al significado global de la oración).
    public final static String TIEMPO_PASADO="PASADO"; //Puede tener influencia en el presente
    public final static String TIEMPO_PRESENTE="PRESENTE"; //Son acciones vigentes o frecuentes.
    public final static String TIEMPO_FUTURO="FUTURO"; //No tiene influencia en el presente.

    
    //********* TIEMPOS DEL INDICATIVO ************
    /* El presente de indicativo en español se utiliza para situar una acción en el momento del habla 
       o en un futuro muy próximo, para describir una rutina o acciones que se repiten, o para aludir a 
       situaciones estables o permanentes.  ES EL TIEMPO MÁS IMPORTANTE.   
       En indicativo, el o la hablante hace una declaración a través de un enunciado afirmativo, 
       negativo o interrogativo con la que afirma, niega o cuestiona de forma directa algo.
        Ejemplo: ¿Lograré dominar el subjuntivo? 
    */
    public final static String MODO_PRESENTE="MODO_PRESENTE"; //P.e.: aprendo
    //PRETÉRITO PERFECTO:
    // *Acción realizada en un marco temporal que se extiende hasta el presente (este …, hoy)
    // *Acción realizada en el pasado que tiene influencia en el presente
    public final static String MODO_PRETERITO_PERFECTO_COMPUESTO="MODO_PRETERITO_PERFECTO_COMPUESTO"; //P.e.: he aprendido
    //PRETÉRITO PERFECTO SIMPLE:
    // *Acción puntual sucedida en el pasado sin influencia en el presente
    // *Acción pasada que interrumpe un curso de acción pasado
    // *Sucesión de acciones en el pasado
    public final static String MODO_PRETERITO_PERFECTO_SIMPLE="MODO_PRETERITO_PERFECTO_SIMPLE"; //P.e.: aprendí
    //PRETÉRITO IMPERFECTO:
    // *Descripción de una situación pasada.
    // *Acción habitual o rutinaria del pasado.
    // *Enfatizar la continuidad de una acción
    public final static String MODO_PRETERITO_IMPERFECTO="MODO_PRETERITO_IMPERFECTO"; //P.e.: aprendía
    //PRETÉRITO PLUSCUAMPERFECTO:
    // *Acción pasada que tiene lugar antes que otra acción también pasada.
    public final static String MODO_PRETERITO_PLUSCUAMPERFECTO="MODO_PRETERITO_PLUSCUAMPERFECTO"; //P.e.: había aprendido
    //PRETÉRITO ANTERIOR:
    // *Acción inmediatamente anterior a otra acción también pasada. Similar al PLUSCUAMPERFECTO.
    // *Actualmente solo se utiliza en textos literarios.
    public final static String MODO_PRETERITO_ANTERIOR="MODO_PRETERITO_ANTERIOR"; //P.e.: hube aprendido
    
    //FUTURO:
    public final static String MODO_FUTURO_SIMPLE="MODO_FUTURO_SIMPLE"; //P.e.: amaré
    public final static String MODO_FUTURO_SIMPLE_A="MODO_FUTURO_SIMPLE_A"; //P.e.: voy a hablar. Es un modo alternativo al futuro simple (perífrasis verbal ir a + infinitivo).
    public final static String MODO_FUTURO_COMPUESTO="MODO_FUTURO_COMPUESTO"; //P.e.: habré amado
    
    //CONDICIONAL:
    public final static String MODO_CONDICIONAL_SIMPLE="MODO_CONDICIONAL_SIMPLE"; //P.e.: amaría
    public final static String MODO_CONDICIONAL_COMPUESTO="MODO_CONDICIONAL_COMPUESTO"; //P.e.: habría amado
    
    //********* TIEMPOS DEL SUBJUNTIVO ************
    // El subjuntivo es el modo verbal de la irrealidad o de la conjetura. La palabra «subjuntivo» quiere decir 
    // que el sentido de los tiempos verbales de este modo DEPENDE del verbo de la oración principal.
    /* Ejemplo del subjuntivo en español… ¿lograré dominarlo?
        Creo que tengo que practicarlo más. ¡Dudo que pueda aprenderlo todo en una sola sesión!
        Es posible que haya un par de cosas que aún no tengo del todo claras.
        Si hubiera prestado más atención en clase, habría entendido mejor la explicación.
        Al final, el profesor nos pidió que resolviéramos unos ejercicios dificilísimos.
        Espero que la próxima semana sea más fácil.

        En subjuntivo, el o la hablante expone su percepción acerca de algo. Las oraciones en subjuntivo
        van encabezadas por una expresión u oración que hace expresa esa subjetividad.
            Ejemplo: Dudo que pueda aprenderlo todo en una sesión.     
    */
    public final static String MODO_SUBJUNTIVO_PRESENTE_SIMPLE="MODO_SUBJUNTIVO_PRESENTE_SIMPLE"; //P.e.: ame
    public final static String MODO_SUBJUNTIVO_PRESENTE_COMPUESTO="MODO_SUBJUNTIVO_PRESENTE_COMPUESTO"; //P.e.: haya amado
    public final static String MODO_SUBJUNTIVO_PRETERITO_SIMPLE="MODO_SUBJUNTIVO_PRETERITO_SIMPLE"; //P.e.: amara o amase
    public final static String MODO_SUBJUNTIVO_PRETERITO_COMPUESTO="MODO_SUBJUNTIVO_PRETERITO_COMPUESTO"; //P.e.: hubiera o hubiese amado
    public final static String MODO_SUBJUNTIVO_FUTURO_SIMPLE="MODO_SUBJUNTIVO_FUTURO_SIMPLE"; //P.e.: amare
    public final static String MODO_SUBJUNTIVO_FUTURO_COMPUESTO="MODO_SUBJUNTIVO_FUTURO_COMPUESTO"; //P.e.: hubiere amado

    //************** IMPERATIVO *******************
    // Modo para dar órdenes o consejos
    public final static String MODO_IMPERATIVO_AFIRMATIVO="MODO_IMPERATIVO_AFIRMATIVO"; //P.e.: ama
    public final static String MODO_IMPERATIVO_NEGATIVO="MODO_IMPERATIVO_NEGATIVO"; //P.e.: no ames
    
    //************** PERSONA *******************
    public final static String PERSONA_PRIMERA="PERSONA_PRIMERA";
    public final static String PERSONA_SEGUNDA="PERSONA_SEGUNDA";
    public final static String PERSONA_TERCERA="PERSONA_TERCERA";

    //************** NUMERO *******************
    public final static String NUMERO_SINGULAR="NUMERO_SINGULAR";
    public final static String NUMERO_PLURAL="NUMRO_PLURAL";

    
    //***** VARIABLES *********
    private String tiempo="";
    private String modo="";
    private String persona="";
    private String numero="";
    private DVerb verboInfinitivo=null; //Verbo al que pertenece este tiempo/modo verbal
    private ArrayList<DVerb> conjugaciones=new ArrayList(); //Si es un infinitivo, este es un enlace a todas sus conjugaciones. Las conjugaciones tendrán esta lista vacía. 
    private boolean negado=false; //Flag que indica si es una acción negada. P.e. Juan no es guapo", "No vayais al cine"

    /**
     * Crea un tiempo verbal del diccionario.
     * @param text Tiempo verbal.
     * @param raiz Lexema (raiz) del verbo. La raiz del infinitivo es común a todas sus formas verbales (en los verbos regulares). Si el verbo es irregular, el lexema es el infinitivo del verbo.
     * @param tiempo Tiempo en que se desarrolla la acción.
     * @param modo Modo específico del tiempo verbal, que implica el cómo se desarrolla la acción.
     * @param infinitivo Si no es un infinitivo, indicad en este parámetro el infinitivo de este verbo.
     * @param persona Persona (Primera, Segunda, Tercera).
     * @param numero Numero (singular, plural).
     * @param negado True si queremos indicar que el verbo está negado. P.e. "Juan no es guapo", "No vayais al cine".
     */
    public DVerb (String text, String raiz, String tiempo, String modo, DVerb infinitivo, String persona, String numero, boolean negado){
        super(text, raiz, null, null); //El verbo no tiene genero
        this.tiempo=(tiempo==null?"":tiempo);
        this.modo=(modo==null?"":modo);
        this.verboInfinitivo=infinitivo;
        this.persona=(persona==null?"":persona);
        this.numero=(numero==null?"":numero);
        this.addPOS(DWord.POS_VERB); //Es siempre un verbo.
        this.negado=negado;
    }
    
    public boolean isInfinitivo(){
        boolean result=(this.verboInfinitivo==null); //Si esta variable es null, es que es infinitivo. También puede comprobarse su tiempo.
        return result;
    }
    
    /**
     * Si este verbo es infinitivo, añade la conjugación indicada a su lista. Si no es infinitivo, no hace nada.
     * @param conjugacion Conjugación a añadir a la lista
     */
    public void addConjugacion(DVerb conjugacion){
        if (this.isInfinitivo()){
            this.conjugaciones.add(conjugacion); //No hace falta comprobar si existe, ya que las conjugaciones se añaden sólo una vez y son invariables.
        }
    }
    
    /**
     * Si este verbo es un infinitivo, devuelve el listado de todas las conjugaciones de este verbo.
     * @return Lista de conjugaciones de este verbo.
     */
    public ArrayList<DVerb> getAllConjugaciones(){
        return this.conjugaciones;
    }
    
    /**
     * Indica si la palabra especificada es una conjugación de este verbo (this).
     * @param conjugacion Palabra de la que se revisará si es una conjugación de este verbo.
     * @return True si es una conjugación, false en caso contrario.
     */
    public boolean isConjugacion(String conjugacion){
        boolean result=false;
        for (DVerb v:this.conjugaciones){
            if (v.getText().equals(conjugacion)){
                result=true;
            }
        }//for
        return result;
    }
    
    /**
     * Genera una cadena de texto con todas las conjugaciones de este verbo (conjugación).
     * Solo tiene sentido invocar a esta función si estamos en un infinitivo.
     * @return Listado de conjugaciones asociados a este verbo.
     */
    public String getConjugacionesStr(){
        String result="";
        for (DVerb v:this.conjugaciones){
            result+=v.getText()+", ";
        }//for
        if (!this.conjugaciones.isEmpty()){
            result=result.substring(0,result.length()-2);
        }
        return result;
    }
    
    /**
     * @return Devuelve el tiempo en el que transcurre la acción. Son valores de DVerb.TIEMPO_
     */
    public String getTiempo() {
        return tiempo;
    }

    /**
     * @param tiempo Establece el tiempo verbal de esta conjugación/verbo. Son valores de DVerb.TIEMPO_
     */
    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * @return Obtiene el modo de esta conjugación/verbo. Son valores de DVerb.MODO_
     */
    public String getModo() {
        return modo;
    }

    /**
     * @param modo Establece el modo de esta conjugación/verbo. Son valores de DVerb.MODO_
     */
    public void setModo(String modo) {
        this.modo = modo;
    }

    /**
     * @return Devuelve el verbo infinitivo (DVerb) asociado a esta conjugación. Si ya es infinitivo, devuelve null.
     */
    public DVerb getVerboInfinitivo() {
        return verboInfinitivo;
    }

    /**
     * @param verboInfinitivo El verbo Infinitivo al que pertenece este tiempor/modo
     */
    public void setVerboInfinitivo(DVerb verboInfinitivo) {
        this.verboInfinitivo = verboInfinitivo;
    }
    

    @Override
    public String toString(){
        String result=super.toString()+" ; CONJUGACIONES: [ ";
        for (DVerb verb:this.conjugaciones){
           result+=verb.getText()+", ";
        }//for
        result=(conjugaciones.isEmpty()?result+" ]":result.substring(0, result.length()-2)+" ]");
        return result;
    }

    /**
     * @return True si el verbo está negado. P.e. "Juan no es guapo", "No vayais al cine".
     */
    public boolean isNegado() {
        return negado;
    }

    /**
     * @param negado True si queremos indicar que el verbo está negado. P.e. "Juan no es guapo", "No vayais al cine".
     */
    public void setNegado(boolean negado) {
        this.negado = negado;
    }
    
}//Class
