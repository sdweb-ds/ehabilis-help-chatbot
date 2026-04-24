package es.sdweb.memorycorp.nlpengine;

import es.sdweb.application.componentes.util.ObjectUtil;
import es.sdweb.application.componentes.util.StringUtil;
import java.util.ArrayList;

/**
 *
 * @author Antonio Carro Mariño
 */
public class LOracion {
    
    private final String id=ObjectUtil.generateId(); //Generamos un Id unico para esta oración
    private String oracion=""; //Almacena el texto literal de la oracion
    private LTermino estructura=null; //Almacena la estructura en árbol de la oración
    private LParrafo parrafo=null; //Almacena un enlace al párrafo al que pertenece. Es una dependencia circular, necesaria, pero sorteable a través de Object (en su caso).
    
    private LTermino accion=null; //Almacena la accion principal (verbo principal) objeto de esta oración
    private LTermino sujeto=null; //Almacena el sujeto principal que realiza la acción principal
    private LTermino objetoDirecto=null; //Almacena el objeto (directo) principal implicado en la acción
    private LTermino objetoIndirecto=null; //Almacena el objeto (indirecto) principal beneficiario de la acción
    private LTermino ccCausa=null; //Almacena el Complemento Circustancial de Causa principal implicado en la acción: Por qué (causa) sucede.
    private LTermino ccTiempo=null; //Almacena el Complemento Circustancial de Tiempo principal implicado en la acción: Cuándo sucede.
    private LTermino ccLugar=null; //Almacena el Complemento Circustancial de Lugar principal implicado en la acción: Dónde sucede.
    private LTermino ccLugarEsp=null; //Almacena el Complemento Circustancial de Lugar específico implicado en la acción: Por dónde sucede.
    private LTermino ccFinalidad=null; //Almacena el Complemento Circustancial de Finalidad implicado en la acción: Para qué sucede.
    private LTermino ccModo=null; //Almacena el Complemento Circustancial de Modo implicado en la acción: Cómo sucede.
    private LTermino cSujeto=null; //Almacena el complemento del sujeto que indica más partícipes en la acción: Con quién sucede.
    
    /**
     * @return Devuelve el ID de la oración.
     */
    public String getId() {
        return id;
    }

    /**
     * @return Devuelve el texto original de la oración.
     */
    public String getOracion() {
        return oracion;
    }

    /**
     * @param text the oracion to set
     */
    public void setOracion(String text) {
        this.oracion = text;
    }

    /**
     * @return devuelve la estructura de la oración.
     */
    public LTermino getEstructura() {
        return estructura;
    }

    /**
     * @param estructura the estructura to set
     */
    public void setEstructura(LTermino estructura) {
        this.estructura = estructura;
    }
    
    public String toStringEstructura(){
        String result=estructura.toStringTree(); //Pasamos todo el árbol a texto estructurado
        return result;
    }
    
    @Override
    public String toString(){
        return oracion;
    }

    
    /**
     * Recorre el árbol buscando la accion (verbo) marcado como principal. Fue marcado ya durante el 
     * análisis sintáctico de la oración.
     * @param t Término a partir del cual se realizará la búsqueda. Si es el raiz, se busca en todo el árbol.
     * @return  Término que contiene la acción principal de la oración
     */
    private LTermino getAccionPrincipal(LTermino t){
      LTermino result=null;
      //****** Miramos si se trata de la acción principal *****
      if (t.isVerb()&&t.isPrincipal()){
          result= t;
      }else{
          for (LTermino hijo:t.getHijos()){ //Para cada uno de los hijos
              result=getAccionPrincipal(hijo); //Vemos si es la accion principal
              if (result!=null){ //Si no es nulo, es que es la acción principal
                  break; //salimos del For
              }
          }
          
      }
      return result;
    }
    
    /**
     * Devuelve el sujeto principal que realiza la acción principal.
     * @param t Término de la estructura a partir del que se realiza la búsqueda
     * @return Término con el sujeto principal
     */
    private LTermino getSujetoPrincipal(LTermino t){
      LTermino result=null;
      //****** Miramos si se trata del sujeto principal *****
      if (t.isSubject()&&t.isPrincipal()){
          result= t;
      }else{
          for (LTermino hijo:t.getHijos()){ //Para cada uno de los hijos
              result=getSujetoPrincipal(hijo); //Vemos si es el sujeto principal
              if (result!=null){ //Si no es nulo, es que es el sujeto principal
                  break; //salimos del For
              }
          }
          
      }
      return result;
    }
    
    /**
     * [NO USADA]
     * Devuelve el objeto (directo) principal que sufre la acción principal.
     * Recorre el árbol en su búsqueda.
     * @param t Término de la estructura a partir del que se realiza la búsqueda
     * @return Término con el objeto (directo) principal
     */
    private LTermino getObjetoDirectoPrincipal(LTermino t){
      LTermino result=null;
      //****** Miramos si se trata del objeto (directo) principal *****
      if (t.isObjectDirectoPrincipal()){
          result= t;
      }else{
          for (LTermino hijo:t.getHijos()){ //Para cada uno de los hijos
              result=getObjetoDirectoPrincipal(hijo); //Vemos si es el objeto principal
              if (result!=null){ //Si no es nulo, es que es el objeto principal
                  break; //salimos del For
              }
          }
          
      }
      return result;
    }
    
    /**
     * [NO USADA]
     * Devuelve el objeto (Indirecto) principal que sufre la acción principal.
     * Recorre el árbol en su búsqueda.
     * @param t Término de la estructura a partir del que se realiza la búsqueda
     * @return Término con el objeto (indirecto) principal
     */
    private LTermino getObjetoIndirectoPrincipal(LTermino t){
      LTermino result=null;
      //****** Miramos si se trata del objeto (indirecto) principal *****
      if (t.isObjectIndirectoPrincipal()){
          result= t;
      }else{
          for (LTermino hijo:t.getHijos()){ //Para cada uno de los hijos
              result=getObjetoIndirectoPrincipal(hijo); //Vemos si es el objeto principal
              if (result!=null){ //Si no es nulo, es que es el objeto principal
                  break; //salimos del For
              }
          }
          
      }
      return result;
    }
    
    /**
     * @return La accion principal de la oracion. Puede haber más acciones (en oraciones subordinadas,etc), pero solo esta es la principal.
     */
    public LTermino getAccion() {
        if (this.accion==null){
            this.accion=getAccionPrincipal(this.estructura);
        }
        return accion;
    }
    


    /**
     * Corrige la confusión de tiempos verbales de SER e IR: Pedro fue(SER) sargento; Pedro fue(IR) a la panadería.
     * También descarta usos de "estar" que no asigna cualidades: "Pedro está cerca(ADV) de la panadería".
     * Ambos verbos son homógrafos en el: pretérito indefinido, futuro del subjuntivo, y en las dos formas del pretérito imperfecto del subjuntivo
     * @param dict Diccionario utilizado
     * @return "ser"/"estar"/"parecer" con sentido de "ser", "ir" o "estar" (en usos que no asignan propiedades, sino que indican lugar).
     */
    public String desambiguaSerIrEstar(Dictionary dict){
        String result="ser";
        //Buscaremos el patrón "IR A". Analizaremos los hijos del Raiz (por ser copulativa).
        boolean verboAux=false;
        if (!this.estructura.getPos().equals(DWord.POS_ADV)||
             SpanishDictionary.isPositionFlag(this.estructura.getTexto())){ //Si el verbo se usa para localización "Pedro está cerca(root-ADV) de la panadería", no es copulativa
            for (LTermino term:this.getEstructura().getHijos()){
                if (term.getPos().equals(DWord.POS_AUX)){
                    verboAux=true;
                }else if (term.getTexto().equals("a")||term.getTexto().equals("al")){ //Detectamos el enlace "a". P.e. "Pedro fue al cine"
                    result="ir";
                }else if (term.getTexto().equals("en")){ //Detectamos el enlace "en". P.e. "Pedro esta en el cine"
                    result="estar";
                }
            }//for
        }
        return result;
    }

    /**
     * Evalúa si esta oración (this) es de tipo copulativa con el verbo "ser" (o "estar" y "parecer" en el sentido de "ser").
     * P.e. "Juan es gordo"/"Juan está gordo"/"Juan parece gordo".
     * @param dict Diccionario utilizado.
     * @return True si es copulativa, false en caso contrario.
     */
    public boolean isOracionCopulativaSer(Dictionary dict){
        boolean result=false; //Vemos si el verbo es "ser"/"estar" y no "ir"
        String verbo=this.getAccion().getTexto(); //Veremos si el verbo de esta accion es SER (o "estar" en el sentido de "ser")
        String ud=this.getAccion().getUd();
        if (!dict.getVerb(verbo).isEmpty()&&ud.equals("cop")){ //Si existe el verbo y es copulativo
            String esSer=desambiguaSerIrEstar(dict); //Hacemos una última comprobación por la confusión de conjugaciones entre "ser" e "ir" (fue, fui, etc), y de usos de "estar" no copulativos
            result=esSer.equals("ser");
        }
        return result;
    }

    
    /**
     * Evalúa si esta oración (this) declara una agregación de elementos.
     * @param dict Diccionario utilizado.
     * @return True si indica una agregación, false en caso contrario.
     */
    public boolean isOracionAgregacion(Dictionary dict){
        boolean result=false;
        String verbo=this.getAccion().getTexto(); //Vemos el verbo principal de esta accion
        String root=this.getEstructura().getTexto(); //Vemos la raiz
        LTermino sujetoT=this.getSujeto(); //Vemos el sujeto
     
        if (!dict.getVerb(verbo).isEmpty()){
            DVerb conj=dict.getVerb(verbo).get(0); //Cogemos la conjugacion
            DVerb inf=conj.getVerboInfinitivo(); //Cogemos el infinitivo de la conjugación
            
            //Frases como: "El brazo está formado por codo y mano" o "La mano es parte del brazo".
            if ((inf.getText().equals("ser")||inf.getText().equals("estar"))&&
                (SpanishDictionary.isAgregationWord(root)||
                 SpanishDictionary.isAgregatorWord(root))){ //Frase tipo "La mano es una parte del brazo". Aquí root="parte". 
                result=true;
            }else
            
            if (inf.getText().equals("componer")){ //Si la frase el del tipo "El brazo se compone de mano y codo".
                for (LTermino term:this.getAccion().getHijos()){
                    if (term.getTexto().equals("se")){
                        result=true; //Entonces se habla de agregación en la oracion
                    }
                }//for
            }else

            if (inf.getText().equals("formar")){ //Si la frase el del tipo "La mano forma parte del brazo".
                //Ahora miraremos si aparece la palabra "parte" (y sus sinónimos) en los hijos directos
                for (LTermino term:this.getAccion().getHijos()){
                    if (term.getUd().equals(LTermino.UD_COMPOUND)&&
                        SpanishDictionary.isAgregationWord(term.getTexto())){ //Si el verbo es compuesto y la partícula compuesta es algo como "parte", "componente", etc
                        result=true; //Entonces se habla de agregación en la oracion
                    }
                }//for
            }else
                
            //Frases del tipo "La mano tiene dedos", o "Juan tiene piernas". 
            if (inf.getText().equals("tener")){
                //Comprobamos si hay sujeto y el sujeto es un nombre de persona, lugar o pronombre personal (ojo con el PRON "ello") y el verbo sea "tener"
                if (sujetoT!=null&&((sujetoT.getPos().equals(DWord.POS_PROPN)|| 
                    (sujetoT.getPos().equals(DWord.POS_PRON)&&!sujetoT.getTexto().equals("ello"))||
                     sujetoT.getPos().equals(DWord.POS_PROPNL)))){
                    //En el caso de personas, sólo se podría determinar si algo es una parte del cuerpo mediante ontología. Igualmente hay que diferenciar posesión ("Juan tiene una moto") de adjetivo ("Juan tiene frío").
                }else{
                    //Si es un objeto, es claramente una composición.
                    result=true;
                }
            }else
                
            //Frases del tipo "La mano pertenece al brazo", "La mano está incluída en el brazo"
            if (inf.getText().equals("pertenecer")||inf.getText().equals("incluir")){
                result=true;
            }
            
        }
        return result;
    }
    

    /**
     * @return El sujeto principal de la oracion (AVISO: puede no existir y ser nulo). Puede haber más sujetos (en oraciones subordinadas,etc), pero este es el principal.
     */
    public LTermino getSujeto() {
        if (this.sujeto==null){
            this.sujeto=getSujetoPrincipal(this.estructura);
        }
        return sujeto;
    }

    /**
     * El Objeto Directo se establece inicialmente al analizar la oración por NLPBasicEngine.
     * Si no existe Odjeto Directo, buscamos uno alternativo, p.e. un sintagma adjetival, en "Marco era leal a César" el OD será "leal"
     * @return El objeto (directo) principal de la oracion (AVISO: puede no existir y ser nulo). Puede haber más objetos (en oraciones subordinadas,etc), pero este es el principal.
     */
    public LTermino getObjetoDirecto() {
        if (objetoDirecto==null){ //Si no existe Odjeto Directo, buscamos uno alternativo, p.e. un sintagma adjetival, en "Marco era leal a César" el OD será "leal"
            if (this.estructura.getPos().equals(DWord.POS_ADJ)||
                this.estructura.getPos().equals(DWord.POS_NOUN)||
                this.estructura.getPos().equals(DWord.POS_PROPN)){
                if (NLPUtil.tieneGeneroOpuesto(this.estructura.getTexto())){ //Vemos si el raiz es un objeto con genero
                    objetoDirecto=this.estructura; //Si lo es, es un posible sustituto del Objeto Directo, si no lo es, lo más probable es que sea un lugar, etc
                }
            }
        }
        return objetoDirecto;
    }

    /**
     * El objeto directo se establece al analizar la oración por NLPBasicEngine
     * @return El objeto (indirecto) principal de la oracion (AVISO: puede no existir y ser nulo). Puede haber más objetos (en oraciones subordinadas,etc), pero este es el principal.
     */
    public LTermino getObjetoIndirecto() {
        return objetoIndirecto;
    }

    /**
     * @param accion Guardamos la acción y pasamos su texto a minúscula.
     */
    public void setAccion(LTermino accion) {
        accion.setTexto(accion.getTexto().toLowerCase());
        this.accion = accion;
        this.accion.setPrincipal(true); //La marcamos como principal (si no lo esta ya)
    }

    /**
     * @param objetoDirecto the objetoDirecto to set
     */
    public void setObjetoDirecto(LTermino objetoDirecto) {
        this.objetoDirecto = objetoDirecto;
    }

    /**
     * @param objetoIndirecto the objetoIndirecto to set
     */
    public void setObjetoIndirecto(LTermino objetoIndirecto) {
        this.objetoIndirecto = objetoIndirecto;
    }

    /**
     * @return Párrafo al que pertenece esta oración.
     */
    public LParrafo getParrafo() {
        return parrafo;
    }

    /**
     * @param parrafo Párrafo al que pertenece esta oración.
     */
    public void setParrafo(LParrafo parrafo) {
        this.parrafo = parrafo;
    }
    
    
    private ArrayList<LTermino> getElementosAdicionales(LTermino arbol){
        ArrayList<LTermino> result=new ArrayList();

        if (!arbol.isVerb()&&arbol.getUd().equals(LTermino.UD_CONJ)){ //si es un elemento (no un verbo), la negación la hallaremos en el siguiente nivel
            result.add(arbol); //Lo añadimos al resultado
            for (LTermino t:arbol.getHijos()){ //Vemos si hay presente un "no" ADV bajo él.
                if (t.getTexto().equals("no")&&(t.getPos().equals(DWord.POS_ADV))){ //Si está presente la negación
                    arbol.setNegado(true); //El elemento padre está negado
                }else{
                   if (!arbol.isVerb()&&arbol.getUd().equals(LTermino.UD_CONJ)){ //Si el término examinado también debe ser examinado
                       ArrayList<LTermino> aux=getElementosAdicionales(t); //Repetimos el proceso para este elemento.
                       result.addAll(aux); //Añadimos los encontrados
                   }  
                }
            }//for
        }else{ //Si es cualquier otro elemento, analizamos el siguiente nivel
            for (LTermino t:arbol.getHijos()){ //Analizamos el siguiente nivel buscando negaciones adjetivales
                ArrayList<LTermino> aux=getElementosAdicionales(t); //Repetimos el proceso para este elemento.
                result.addAll(aux); //Añadimos los encontrados
            }//for
        }
        return result;
    }
            
    /**
     * Busca los adjetivos adicionales o nombres adicionales asignados al sujeto, en el primer caso en una oración copulativa
     * ("Juan es un tonto, un memo y un idiota"), y en el segundo caso en una oración de agregación (El brazo está formado por antebrazo, muñeca y mano).
     * @return Lista de adjetivos/nombres adicionales, enlazados por conjunciones.
     */
    public ArrayList<LTermino> getElementosAdicionales(){
        ArrayList<LTermino> result=getElementosAdicionales(this.getEstructura());
        return result;
    }

    /**
     * Rastrea el árbol sintáctico para identificar complementos circunstanciales de Tiempo.
     * @param term Término del árbol a partir del que se realiza la búsqueda.
     * @return Término que especifica lugar.
     */
    public LTermino getCcTiempo(LTermino term){
        LTermino result=null;
        if (NLPUtil.isPlaceName(term)){ //Casos tipo "en Madrid"
            result=term;
        }else{
            LTermino adv=NLPUtil.isPositionObject(term);
            if (adv!=null){ //Casos tipo "detrás de la mesa"
                result=term; //P.e. "mesa"
            }
        }
        if (result==null){ //Si este término no es un lugar, buscamos en sus hijos
            for (LTermino t:term.getHijos()){
                result=getCcLugar(t);
            }//for
        }
        return result;
    }
    
    /**
     * Rastrea el árbol sintáctico para identificar complementos circunstanciales de lugar.
     * @param term Término del árbol a partir del que se realiza la búsqueda.
     * @return Término que especifica lugar.
     */
    public LTermino getCcLugar(LTermino term){
        LTermino result=null;
        if (NLPUtil.isPlaceName(term)){ //Casos tipo "en Madrid"
            result=term;
        }else{
            LTermino adv=NLPUtil.isPositionObject(term);
            if (adv!=null){ //Casos tipo "detrás de la mesa"
                result=term; //P.e. "mesa"
            }
        }
        if (result==null){ //Si este término no es un lugar, buscamos en sus hijos
            for (LTermino t:term.getHijos()){
                result=getCcLugar(t);
            }//for
        }
        return result;
    }
    
    
    /**
     * Indica si la oración es Interrogativa.
     * @return True si la oración es interrogativa, false en caso contrario.
     */
    public boolean isQuestion(){
        boolean result=false;
        LTermino tsujeto =this.getSujeto(); //Obtenemos el sujeto principal de la oración
        if (tsujeto!=null&&!StringUtil.isEmpty(tsujeto.getTexto())){ //Si existe y no está vacío
            result=NLPUtil.isQuestionWord(tsujeto.getTexto()); //Vemos si el sujeto es un pronombre interrogativo
            if (!result){ //Si no hay pronombre interrogativo (qué, dónde, quién, etc), miramos si la frase es una pregunta (¿Juan se murió?)
              for (LTermino t:this.getEstructura().getHijos()){ //miramos en el nivel 1 si está presente alguno de los signos de interrogación
                if (!result){ 
                   result=t.getTexto().equals("¿")||t.getTexto().equals("?");
                }
              }//for
            }
        }
        return result;
    }
    
    /**
     * @return Lugar en el que se desarrolla la acción.
     */
    public LTermino getCcLugar() {
        if (ccLugar==null){
            ccLugar= getCcLugar(this.estructura);
        }
        return ccLugar;
    }

    /**
     * @param ccLugar Lugar más específico que ccLugar donde se desarrolla la acción
     */
    public void setCcLugar(LTermino ccLugar) {
        this.ccLugar = ccLugar;
    }

    /**
     * @return 
     */
    public LTermino getCcLugarEsp() {
        return ccLugarEsp;
    }

    /**
     * @param ccLugarEsp the ccLugarEsp to set
     */
    public void setCcLugarEsp(LTermino ccLugarEsp) {
        this.ccLugarEsp = ccLugarEsp;
    }
    
    
}//class
