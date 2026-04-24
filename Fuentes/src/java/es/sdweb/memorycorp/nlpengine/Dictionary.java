package es.sdweb.memorycorp.nlpengine;

import es.sdweb.application.componentes.util.Booleano;
import es.sdweb.application.componentes.util.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Esta clase contiene el diccionario de palabras y verbos.
 * @author Antonio Carro Mariño
 */
public class Dictionary {
    //Listado de palabras que son localizaciones (ciudades, provincias, etc). IMPORTANTE: Empiezan siempre por mayúscula.
    private ArrayList<DWord> localizaciones=new ArrayList<>();
    private HashMap localizacionesMap=new HashMap(); //HashMap(String,DWord)

    //Listado de palabras que son Nombres y Apellidos personales del Español. IMPORTANTE: Empiezan siempre por mayúscula.
    private ArrayList<DWord> nombresApellidos=new ArrayList<>();
    private HashMap nombresApellidosMap=new HashMap(); //HashMap(String,DWord)
    
    //Listado de palabras que no son verbos (incluye tambien los infinitivos de los verbos, pero no las conjugaciones).
    //Incluye solo formas de singular (no plurales).
    private ArrayList<DWord> palabras=new ArrayList<>();
    private HashMap palabrasMap=new HashMap(); //HashMap(String,DWord)
    
    //Listado de verbos. También los asociamos a un HashMap para localizarlos rápidamente.
    private ArrayList<DVerb> verbos=new ArrayList<>();
    private HashMap verbosMap=new HashMap(); //HashMap(String,ArrayList<DVerb>) Pues un mismo texto coincide con varias conjugaciones
    //P.e. la palabra "ame" se corresponde con 3 conjugaciones diferentes (presente subjuntivo 1ª y 3ª persona singular, imperativo 2ª persona singular
    //Determinar cuál es la buscada depende de la concordancia y del contexto.
    
    /**
     * Obtiene una palabra de cualquier tipo del diccionario de palabras (si existe) y la devuelve.
     * El orden de busqueda es, por orden: 1.nombres y apellidos (DWord.POS_PROPN), 2.localizaciones (DWord.POS_PROPNL), 3.verbos (DWord.POS_NOUN), 4.palabras_generales (DWord.POS_NOUN). 
     * La búsqueda debe empezar siempre por los repositorios más específicos y terminar por los repositorios más generales.
     * AVISO: En caso de que la palabra sea un verbo, se devolverá una conjugación coincidente etiquetada como DWord.POS_NOUN
     * en el diccionario asociadas al mismo texto.
     * @param text Palabra a localizar en el diccionario.
     * @return Palabra encontrada (ya sea nombre, verbo, localización, etc) o null si no se encuentra.
     */
    public DWord getWord(String text){
        DWord result=getNameSurname(text,false); //La busca entre los nombres y apellidos
        if (result!=null)
            result.addPOS(DWord.POS_PROPN); //Lo marcamos como POS_PROPN, por si no lo estuviera ya
        
        if (result==null){  //Si no la encuentra como PROPN, la busca entre los PROPNL
            result=(DWord)localizacionesMap.get(text); //Busca entre las localizaciones
            if (result!=null)
                result.addPOS(DWord.POS_PROPNL); //Lo marcamos como POS_PROPNL, por si no lo estuviera ya
        }

        if (result==null){  //Si no la encuentra, la busca entre los verbos
            result=(!getVerb(text).isEmpty()?getVerb(text).get(0):null); //Buscamos una conjugación verbal coincidente
            if (result!=null)
                result.addPOS(DWord.POS_NOUN); //Un infinitivo, cuando se busca como una palabra, lo marcamos como DWord.POS_NOUN
        }
        
        if (result==null){  //Si no la encuentra en los NER, ni entre los infinitivos, la busca entre las restantes palabras
            result=(DWord)palabrasMap.get(text); //Buscamos en el diccionario general
            if (result!=null)
                result.addPOS(DWord.POS_NOUN); //Lo marcamos como DWord.POS_NOUN, pues en el diccionario solo debe haber palabras de este tipo
        }

        return result;
    }
    
    
    /**
     * Obtiene una palabra de cualquier tipo del diccionario de palabras (si existe) y la devuelve.
     * El orden de busqueda es, por orden: 1.nombres y apellidos (DWord.POS_PROPN), 2.localizaciones (DWord.POS_PROPNL), 3.verbos (DWord.POS_NOUN), 4.palabras_generales (DWord.POS_NOUN). 
     * La búsqueda debe empezar siempre por los repositorios más específicos y terminar por los repositorios más generales.
     * AVISO: En caso de que la palabra sea un verbo, se devolverá una conjugación coincidente etiquetada como DWord.POS_NOUN
     * en el diccionario asociadas al mismo texto.
     * @param text Palabra a localizar en el diccionario.
     * @param tipo Tipo de la palabra/término. Son los valores DWord.POS_ ; este campo puede ser nulo, en este caso se busca la primera ocurrencia en el orden indicado.
     * @return Palabra encontrada (ya sea nombre, verbo, localización, etc) o null si no se encuentra.
     */
    public DWord getWord(String text, String tipo){
        DWord result=null;
        if (tipo!=null){ //Si se especificó tipo
            if (tipo.equals(DWord.POS_PROPN)){
                getNameSurname(text,false); //La busca entre los nombres y apellidos
                if (result!=null)
                    result.addPOS(DWord.POS_PROPN); //Lo marcamos como POS_PROPN, por si no lo estuviera ya
            }

            if (result==null && tipo.equals(DWord.POS_PROPNL)){  //Si no la encuentra como PROPN, la busca entre los PROPNL
                result=(DWord)localizacionesMap.get(text); //Busca entre las localizaciones
                if (result!=null)
                    result.addPOS(DWord.POS_PROPNL); //Lo marcamos como POS_PROPNL, por si no lo estuviera ya
            }

            if (result==null && tipo.equals(DWord.POS_NOUN)){  //Si no la encuentra, la busca entre los infinitivos (que trataremos como Nombres)
                result=(!getVerb(text).isEmpty()?getVerb(text).get(0):null); //Buscamos una conjugación verbal coincidente
                if (result!=null)
                    result.addPOS(DWord.POS_NOUN); //Un infinitivo, cuando se busca como una palabra, lo marcamos como DWord.POS_NOUN
            }

            if (result==null && tipo.equals(DWord.POS_NOUN)){  //Si no la encuentra en los NER, ni entre los infinitivos, la busca entre las restantes palabras
                result=(DWord)palabrasMap.get(text); //Buscamos en el diccionario general
                if (result!=null)
                    result.addPOS(DWord.POS_NOUN); //Lo marcamos como DWord.POS_NOUN, pues en el diccionario solo debe haber palabras de este tipo
            }
        }else{
            result=getWord(text); //Si no se indicó tipo (DWord.POS), buscamos la primera ocurrencia
        }

        return result;
    }
    
    
    /**
     * Recupera una palabra del diccionario (si existe), o la crea en caso necesario y siempre que no exista.
     * Crea la palabra (si no existe) y establece su tipo (DWord.POS) en base al tipo especificado.
     * @param text Palabra o término buscado y/o a crear.
     * @param tipo Tipo de la palabra/término. Son los valores DWord.POS_ ; este campo puede ser nulo, en este caso se toma como DWord.POS_NOUN.
     * @param createIfNotExist True si queremos crear la palabra cuando no exista.
     * @return La palabra buscada y/o creada. Nulo en caso de que nos exista y no se quiera crear.
     */
    public DWord getWord(String text, String tipo, boolean createIfNotExist){
        DWord word=getWord(text,tipo); //Busca la palabra primero entre las palabras NER (Nombres Propios, Localizaciones, etc), entre los infinitivos, y por ultimo en el diccionario general
        
        if (word==null){
            if (createIfNotExist){ //Si no existe la palabra y queremos crearla
                word=new DWord(text); //La creamos
                word.addPOS(tipo); //establecemos su tipo
                addWord(word); //Y la añadimos al diccionario
            }
        }

        return word;
    }
    
    /**
     * Busca una palabra en la lista específica de palabras (que no incluye verbos, localizaciones, nombres y apellidos, etc)
     * @param text Palabra buscada
     * @return DWord con la palabra buscada.
     */
    public DWord getWordOnly(String text){
        DWord result=(DWord)palabrasMap.get(text); //Buscamos la palabra en su lista específica únicamente
        return result;
    }
    
    /**
     * Indica si la forma verbal que se recibe como parámetro ya existe en el diccionario.
     * AVISO: hay muchas conjugaciones que coinciden en su texto. Este método solo dice si hay al menos una coincidencia.
     * @param text Forma verbal a buscar en el diccionario.
     * @return True si se encuentra, False en caso contrario.
     */
    public boolean existeVerbo(String text){
        ArrayList<DVerb> conjugaciones=(ArrayList<DVerb>)verbosMap.get(text);
        boolean result=((conjugaciones!=null)&&(!conjugaciones.isEmpty()));
        return result;
    }

    /**
     * Indica si la palabra (de cualquier tipo) que se recibe como parámetro ya existe en el diccionario.
     * Es una búsqueda case sensitive.
     * @param text Forma verbal a buscar en el diccionario.
     * @return True si se encuentra, False en caso contrario.
     */
    public boolean existePalabra(String text){
        text=StringUtil.trimNormalizado(text);
        ArrayList<DVerb> verbo=(ArrayList)verbosMap.get(text); //Miramos si hay una conjugación que corresponda (AVISO: puede haber varias conjugaciones que respondan al mismo texto)
        DWord palabra=(DWord)palabrasMap.get(text); //Miramos si es otra palabra
        DWord nombre=getNameSurname(text,false); //Miramos si es un nombre o apellido personal
        DWord location=(DWord)localizacionesMap.get(text); //Miramos si es una localización (ciudad, provincia, etc)
        boolean result=(((verbo!=null)&&(!verbo.isEmpty()))||(palabra!=null)||(nombre!=null)||(location!=null));
        return result;
    }
    
    
    /**
     * Añade un verbo a la lista de verbos/acciones. AVISO: No se comprueba si está repetido.
     * @param verb Verbo a añadir.
     */
    public void addVerb(DVerb verb){
        ArrayList<DVerb> lista=(ArrayList)verbosMap.get(verb.getText()); //Miramos si ya está en el diccionario
        if ((lista==null)||(lista.isEmpty())){ //Si no está en la tabla hash o su lista está vacía
            verb.setIndex(verbos.size()); //Se añade en último lugar
            verbos.add(verb); //Lo metemos en la lista
            ArrayList<DVerb> aux=new ArrayList(); //No nos complicamos y creamos una lista con ese elemento
            aux.add(verb); //Lo metemos en la lista 
            verbosMap.put(verb.getText(), aux); //Guardamos la lista en la tabla hash
        }else{
            verbos.add(verb); //Añadimos el verbo a la lista
            lista.add(verb); //Y a la lista presente en la tabla hash
        }
    }

    /**
     * Añade una localización a la lista de localizaciones.
     * @param localizacion Nombre o apellido a añadir.
     */
    public void addLocalizacion(DWord localizacion){
        DWord aux=(DWord)localizacionesMap.get(localizacion.getText()); //Miramos si ya está en el diccionario
        if (aux==null){ //Si no está
            localizacion.setIndex(localizaciones.size()); //Se añade en último lugar
            localizaciones.add(localizacion); //Lo metemos en la lista
            localizacionesMap.put(localizacion.getText(), localizacion); //Y en la tabla hash
        }else{
            localizaciones.set(aux.getIndex(), localizacion); //Machacamos la palabra
            localizacionesMap.put(localizacion.getText(), localizacion); //Y machacamos en la tabla hash
        }
    }

    /**
     * Añade un nombre o apellido a la lista de nombres y apellidos.
     * @param nameSurname Nombre o apellido a añadir.
     */
    public void addNameSurname(DWord nameSurname){
        DWord aux=(DWord)nombresApellidosMap.get(nameSurname.getText()); //Miramos si ya está en el diccionario
        if (aux==null){ //Si no está
            nameSurname.setIndex(nombresApellidos.size()); //Se añade en último lugar
            nombresApellidos.add(nameSurname); //Lo metemos en la lista
            nombresApellidosMap.put(nameSurname.getText(), nameSurname); //Y en la tabla hash
        }else{
            nombresApellidos.set(aux.getIndex(), nameSurname); //Machacamos la palabra
            nombresApellidosMap.put(nameSurname.getText(), nameSurname); //Y machacamos en la tabla hash
        }
    }

    /**
     * Añade una palabra a la lista de palabras (todo aquello que no es un verbo, a excepcion de los infinitivos y participios (que actúan de adjetivos)).
     * Si ya había una palabra igual en la lista, pero menos completa (pe. le falta el género o número opuesto), la machaca con la especificada.
     * AVISO: Los infinitivos de un verbo están presentes en la lista de palabras, pero también en están en la lista de verbos.
     * AVISO: Los participios de un verbo también están presentes, porque actúan de adjetivos, y tienen femenino y plural en este caso (por concordancia).
     * @param palabra Palabra a añadir.
     */
    public void addWord(DWord palabra){
        DWord aux=(DWord)palabrasMap.get(palabra.getText()); //Miramos si ya está en el diccionario
        if (aux==null){ //Si no está
            palabra.setIndex(palabras.size()); //Se añade en último lugar
            palabras.add(palabra); //Lo metemos en la lista
            palabrasMap.put(palabra.getText(), palabra); //Y en la tabla hash
        }else{
            if (aux.getPalabraGeneroOpuesto()==null||aux.getPalabrasNumeroOpuesto().isEmpty()){ //Si no tiene género opuesto o número opuesto creado, la machacamos.
                palabras.set(aux.getIndex(), palabra); //Machacamos la palabra con la esperanza de que la especificada est´más completa
                palabrasMap.put(palabra.getText(), palabra); //Y machacamos en la tabla hash
            }
        }
    }

    /**
     * Obtiene el número total de palabras de todo tipo (verbos, sustantivos, nombres, determinantes, etc) contenidas en el diccionario.
     * @return Número total de palabras
     */
    public int getTotalSize(){
        int result=palabras.size()+verbos.size()+nombresApellidos.size()+localizaciones.size();
        return result;
    }

    /**
     * Obtiene el número total de verbos (incluídas conjugaciones) contenidos en el diccionario.
     * @return Número total de verbos
     */
    public int getVerbsSize(){
        int result=verbos.size();
        return result;
    }


    /**
     * Obtiene el número total de palabras (sin conjugaciones verbales, salvo el infinitivo) contenidas en el diccionario.
     * @return Número total de palabras
     */
    public int getWordsSize(){
        int result=palabras.size();
        return result;
    }

    /**
     * Obtiene el número total de palabras (sin conjugaciones verbales, salvo el infinitivo) contenidas en el diccionario.
     * @return Número total de palabras
     */
    public int getNameSurnameSize(){
        int result=nombresApellidos.size();
        return result;
    }

    /**
     * Obtiene el número total de localizaciones contenidas en el diccionario.
     * @return Número total de localizaciones
     */
    public int getLocalizacionesSize(){
        int result=localizaciones.size();
        return result;
    }
    
    /**
     * Recupera un objeto verbo, con todos sus datos, del diccionario a partir del texto.
     * Dado que puede haber más de una conjugación con el mismo texto, se devuelve un ArrayList.
     * P.e. En el verbo "amar" existen 4 conjugaciones que corresponden con el texto "ame". Por tanto se devolverán 4 objetos DVerb.
     * @param text Texto del verbo a buscar.
     * @return Lista de conjugaciones encontradas (lista vacía si no se encuentran).
     */
    public ArrayList<DVerb> getVerb(String text){
        ArrayList<DVerb> result=(ArrayList)verbosMap.get(text);
        result=(result==null?new ArrayList():result); //Si es nulo, devolvemos la lista vacía
        return result;
    }
    
    /**
     * Recupera un objeto verbo Infinitivo, con todos sus datos, del diccionario a partir del texto, que puede ser cualquier conjugación.
     * Aunque puede haber más de una conjugación con el mismo texto, el Infinitivo solo es uno.
     * P.e. se recibe "ame" y se devuelve "amar"
     * @param text Texto de la conjugación a buscar.
     * @return Infinitivo de la conjugación recibida (null si no se encuentra).
     */
    public DVerb getVerbInfinitivo(String text){
        DVerb result=null;
        ArrayList<DVerb> lista=getVerb(text);
        if (!lista.isEmpty()){
            DVerb conjugacion=lista.get(0);
            if (conjugacion.getTiempo().equals(DVerb.TIEMPO_INFINITIVO)){
                result=conjugacion; //Si ya es el infinitivo, lo devolvemos.
            }else{
                DVerb inf=conjugacion.getVerboInfinitivo(); //Obtenemos el Infinitivo
                result=inf; //y lo devolvemos
            }
        }
        return result;
    }

    
    /**
     * Recupera un nombre o apellido, con todos sus datos, del diccionario.
     * @param text Texto del nombre o apellido a buscar.
     * @param createIfNotExist Texto del nombre o apellido a buscar.
     * @return Nombre o apellido encontrado (null si no existe).
     */
    public DWord getNameSurname(String text, boolean createIfNotExist){
        DWord result=(DWord)nombresApellidosMap.get(text);
        if (result==null&&createIfNotExist){ //Si el nombre no está en el diccionario pero lo queremos crear
            DWord nombre=new DWord(text); //Lo creamos. No conocemos su género y/o número, solo sabemos que es un nombre
            nombre.addPOS(DWord.POS_PROPN); //Puede ser un nombre propio o de lugar. Por defecto, lo ponemos propio.
            nombresApellidosMap.put(text, nombre); //Y lo guardamos en el Map de nombres
            result=nombre; //Y lo devolvemos
        }
        return result;
    }
    
    /**
     * Recupera una localización, con todos sus datos, del diccionario.
     * @param text Texto de la localización a buscar.
     * @return Localización encontrada (null si no existe).
     */
    public DWord getLocalizacion(String text){
        DWord result=(DWord)localizacionesMap.get(text);
        return result;
    }


    /**
     * Indica si una palabra es un nombre de persona o de localización.
     * @param word Palabra a analizar.
     * @return True si es un nombre de persona/localización. False en caso contrario.
     */
    public boolean isNameOrLocation(String word){
        boolean result=!StringUtil.isEmpty(StringUtil.toString(getNameOrLocation(word, null, null, false)));
        return result;
    }

    /**
     * Indica si esta palabra es un nombre (de persona, localización, etc). En caso de que no exista en el diccionario, puede ser creada.
     * @param word Palabra a analizar
     * @param pos POS de la palabra (si se conoce). Null si no se conoce este valor.
     * @param isPlace True si se conoce que es un lugar, false si es otro nombre, null si no se conoce la naturaleza de esta palabra.
     * @param createIfNotExist True si deseamos que se cree la palabra en caso de no existir y ser un nombre.
     * @return Palabra con el nombre, o null si no es un nombre.
     */
    public DWord getNameOrLocation(String word, String pos, Booleano isPlace, boolean createIfNotExist){
        DWord result=getNameSurname(word,false); //Vemos si es un nombre tal como llega respetando las mayusculas/minusculas
        if (result==null) //Si no se halló nada, buscamos en minúsculas
            result=getNameSurname(StringUtil.minusculas(word),false); 
        if (result==null) //Si no se halló nada, buscamos en mayúsculas (todas las letras)
            result=getNameSurname(StringUtil.mayusculas(word),false); 
        if (result==null) //Si no se halló nada, buscamos en minúsculas
            result=getNameSurname(StringUtil.primeraMay(word,true), false); 
        if (result==null) //Si no se halló nada, buscamos sin acentos
            result=getNameSurname(StringUtil.eliminarAcentos(word), false); 

        if (result==null) //Si no es un nombre, vemos si es una localización
            result=getLocalizacion(word); //Si no es un nombre, vemos si es una localización
        if (result==null) //Si no se halló nada, buscamos en minúsculas
            result=getLocalizacion(StringUtil.minusculas(word)); 
        if (result==null) //Si no se halló nada, buscamos en mayúsculas (todas las letras)
            result=getLocalizacion(StringUtil.mayusculas(word)); 
        if (result==null) //Si no se halló nada, buscamos en minúsculas
            result=getLocalizacion(StringUtil.primeraMay(word,true)); 
        if (result==null) //Si no se halló nada, buscamos sin acentos
            result=getLocalizacion(StringUtil.eliminarAcentos(word)); 

        if (result==null&&createIfNotExist){ //Si a pesar de ello no se encontró y se desea crear
           String letra=word.substring(0,1);
           boolean primMay=((letra.compareTo("A")>=0&&letra.compareTo("Z")<=0)||(letra.equals("Ñ"))); //Si la primera letra es mayúscula
           if ((!StringUtil.isEmpty(pos)&&pos.equals(DWord.POS_PROPN))||(primMay)) { //Si la primera letra es mayúscula o el nombre ya ha sido marcado como Nombre Propio
               letra=letra.toUpperCase(); //Pasamos la primera letra a mayúscula, por si nos indicaron un nombre en minúscula
               word=letra+word.substring(1, word.length()); //Sustituimos la primera letra por una mayúscula: marco -> Marco
               result=new DWord(word); //Creamos la palabra
               
               if (isPlace!=null&&isPlace.isBool()){
                   result.addPOS(DWord.POS_PROPNL);
                   this.addLocalizacion(result); //Añadimos la localización
               }else{
                   result.addPOS(DWord.POS_PROPN);
                   this.addNameSurname(result); //En otro caso lo añadimos como nombre o apellido
               }
               
           }//Si es nombre
        }//si crear
        return result;
    }
    
    
    /**
     * @return Colección de nombres y apellidos presentes en el diccionario.
     */
    public ArrayList<DWord> getAllNameSurname() {
        return nombresApellidos;
    }

    /**
     * @return Colección de palabras presentes en el diccionario (sin conjugaciones verbales)
     */
    public ArrayList<DWord> getAllPalabras() {
        return palabras;
    }

    /**
     * Añade una lista de palabras al diccionario. 
     * @param palabras Lista de palabras a añadir al diccionario (no borra las previas, pero tampoco duplica)
     */
    public void setPalabras(ArrayList<DWord> palabras) {
        for (DWord p:palabras){ 
            addWord(p); //Añadimos la palabra
        }//for
    }

    /**
     * Añade una lista de localizaciones al diccionario (la primera letra debe estar en mayúscula).
     * @param localizaciones Lista de localizaciones a añadir al diccionario.
     */
    public void setLocalizaciones(ArrayList<DWord> localizaciones) {
        for (DWord p:localizaciones){ 
            addLocalizacion(p); //Añadimos la localización
        }//for
    }

    /**
     * Añade una lista de nombres y/o apellidos al diccionario (la primera letra debe estar en mayúscula).
     * @param namesSurnames Lista de nombres/apellidos a añadir al diccionario.
     */
    public void setNameSurnames(ArrayList<DWord> namesSurnames) {
        for (DWord p:namesSurnames){ 
            addNameSurname(p); //Añadimos el nombre o apellido
        }//for
    }

    /**
     * @return Lista de verbos del diccionario
     */
    public ArrayList<DVerb> getAllVerbos() {
        return verbos;
    }

    /**
     * @return Lista de localizaciones del diccionario
     */
    public ArrayList<DWord> getAllLocalizaciones() {
        return localizaciones;
    }

    /**
     * Añade una lista de verbos al diccionario. AVISO: No comprueba si están repetidos.
     * @param verbos the verbos to set
     */
    public void setVerbos(ArrayList<DVerb> verbos) {
        for (DVerb verb:verbos){
            addVerb(verb); //Añadimos el verbo
        }//for
    }
    
}//Class
