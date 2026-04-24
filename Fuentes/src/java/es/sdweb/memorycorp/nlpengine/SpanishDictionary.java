package es.sdweb.memorycorp.nlpengine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import es.sdweb.application.componentes.util.Booleano;
import es.sdweb.application.componentes.util.FileUtil;
import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.componentes.util.NumberUtil;
import es.sdweb.application.componentes.util.StringUtil;

/**
 * Esta clase genera e inicializa un dictionary del español, recogiendo las particularidades del idioma.
 * @author Antonio Carro Mariño
 */
public class SpanishDictionary {

    //******************* Dicionario Español ***************************
    private static Dictionary dictionary=new Dictionary();

    //********************* FICHEROS DE DATOS LINGÜÍSTICOS *****************
    //Este fichero contiene todos los verbos irregulares y sus conjugaciones
    private final static String PATH_VERBOS_IRREGULARES="/opt/desarrollo/memorycorp/verbos_irregulares_conjugados.txt";
    //Este fichero contiene todos los verbos (regulares e irregulares) en infinitivo. Los verbos regulares se extraen eliminando los irregulares de esta lista.
    private final static String PATH_ALL_VERBOS="/opt/desarrollo/memorycorp/verbos-espanol.txt";
    //Este fichero contiene un repositorio con las palabras del dictionary de la RAE, en singular, e incluyendo infinitivos. AVISO: también incluye nombres en minúscula y las palabras no tienen acento, lo que es un serio problema.
    private final static String PATH_PALABRAS_1="/opt/desarrollo/memorycorp/palabras-espanol.txt";
    //Esta colección de ficheros ("a.txt" a "z.txt") contienen un repositorio de palabras en singular, e incluyendo infinitivos, y femeninos.
    private final static String PATH_PALABRAS_2="/opt/desarrollo/memorycorp/dics/"; //La coleccion de ficheros está en este directorio
    //Este fichero contiene todos los nombres y apellidos del Español. Pueden coincidir con palabras: p.e. "Mar" y "mar", "Dolores" y "dolores"
    private final static String PATH_NAMES_SURNAMES="/opt/desarrollo/memorycorp/nombres-propios-es.txt";
    //Este fichero contiene todas las localizaciones (ciudades, provincias, etc). Pueden coincidir con palabras: p.e. "Africa" (continente) y "Africa" (Nombre propio), "Sacramento" (ciudad) y "sacramento" (palabra)
    private final static String PATH_LOCALIZACIONES="/opt/desarrollo/memorycorp/entidades-territoriales.txt";
    //Este fichero contiene los sinónimos y antónimos.
    private final static String PATH_SINONIMOS_ANTONIMOS="/opt/desarrollo/memorycorp/sinonimos-antonimos.v.01.01.txt";
    //Listado de adjetivos (permite marcar las palabras que son adjetivos)
    private final static String PATH_ADJETIVOS="/opt/desarrollo/memorycorp/adjetivos/adjetivos.txt";

    /**
     * A partir de una línea del fichero de texto, extrae los tiempos verbales que encuentre y los añade al dictionary.
     * @param linea Linea de texto a analizar.
     * @param dicc Diccionario donde se guardarán los valores/verbos encontrados.
     * @param tiempo //Tiempo vincualdo al verbo. A escoger entre los valores estáticos de la clase DVerb.
     * @param modo //Modo vinculado al verbo. A escoger entre los valores estáticos de la clase DVerb.
     * @param infinitivo //Verbo (en infinitivo) al que pertenece este tiempo verbal. P.e. "luchar"
     * @param persona //Persona del tiempo verbal (primera, segunda tercera). A escoger entre los valores estáticos de la clase DVerb.
     * @param numero //Numero del tiempo verbal (singular, plural). A escoger entre los valores estáticos de la clase DVerb.
     * @param negado True si el verbo es negado. P.s. "No vayáis"
     * @return Ultimo verbo/valor encontrado en la línea analizada.
     */
    private static DVerb loadLineIrregularVerb(String linea, Dictionary dicc, String tiempo, String modo, DVerb infinitivo, String persona, String numero, boolean negado){
        DVerb verbo=null;
        try{
            String words[]=linea.split(":"); //Dos partes: persona y numero (yo, tu, él,...), y el verbo/valor propiamente dicho.

            if (words.length<2){ //Si no ha hallado los ":" (es decir, no hay dos palabras), es que hay un error
                  LogUtil.logError("[INICIALIZACION FASE_1] Error en linea: "+linea);
            }

            String verb= StringUtil.trim(words[1]);

            if (verb.indexOf(",")>0){ //Si hay multiples verbos/valores separados por comas, p.e. "hubiera, hubiese"
                String verbs[]=verb.split(","); //Cogemos cada valor/verbo
                for (String aux:verbs){
                    //La raiz de un verbo irregular es el infinitivo de ese verbo.
                    verbo=new DVerb(StringUtil.trim(aux),infinitivo.getText(),tiempo,modo,infinitivo,persona,numero,negado); //Creamos el verbo
                    dicc.addVerb(verbo); //Lo añadimos al diccionario
                    infinitivo.addConjugacion(verbo); //También añadimos la conjugación al infinitivo
                }//for
            }else{ //Si solo hay un valor
                verbo=new DVerb(StringUtil.trim(verb),infinitivo.getText(),tiempo,modo,infinitivo,persona,numero,negado); //Creamos el verbo
                dicc.addVerb(verbo); //Lo añadimos
                infinitivo.addConjugacion(verbo); //También añadimos la conjugación al infinitivo
            }
        }catch (Exception e){
           LogUtil.logError("[INICIALIZACION FASE_1] Excepcion: "+e.toString()+ " en el proceso de-> "+linea);

        }finally{
           LogUtil.logTraza("[INICIALIZACION FASE_1] Procesando Verbo Irregular: "+linea,10);
        }
        return verbo;
    }

    //****** INICIALIZACION: Cargamos en un bloque estático el dictionary del Castellano
    /**
     * En los verbos irregulares, la raiz (lexema) es la propia forma verbal, debido a la variabilidad del lexema.
     * No es posible extraer un patrón común en el lexema en este tipo de verbos.
     */
    private static void cargaVerbosIrregulares(){

       InputStream is=FileUtil.openFile(PATH_VERBOS_IRREGULARES);
       Scanner fich=new Scanner(is);
       while (fich.hasNextLine()){
           //Infinitivo
           String linea=fich.nextLine().trim(); //Leemos una línea del fichero
           String words[]=linea.split(":"); //Dos partes: "Infinitivo" y el verbo.

            if (!words[0].trim().equals("Infinitivo")){ //Si no es Infinitivo, es que se ha desalineado
                  LogUtil.logError("[INICIALIZACION FASE_1] Error en alineacion en Infinitivo ["+words[0].trim()+"]: "+linea);
            }

           String texto= StringUtil.trim(words[1]);
           DVerb infinitivo=new DVerb(texto,texto,DVerb.TIEMPO_INFINITIVO,null,null,null,null,false);
           getDictionary().addVerb(infinitivo); //Lo añadimos a la lista de verbos

           //Gerundio
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_GERUNDIO,null,infinitivo,null,null,false);

           //Participio
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PARTICIPIO,null,infinitivo,null,null,false);

           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Modo Indicativo.
           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Presente.

           // ******* INDICATIVO PRESENTE ******
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);

           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Pretérito imperfecto.

           // ******* INDICATIVO PRETERITO IMPERFECTO ******
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);

           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Pretérito indefinido (pretérito perfecto simple).

           // ******* INDICATIVO PRETERITO PERFECTO SIMPLE ******
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);

           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Futuro.

           // ******* INDICATIVO FUTURO SIMPLE******
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);

           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Modo Subjuntivo.
           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Presente.

           // ******* SUBJUNTIVO PRESENTE ******
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);

           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Futuro (del subjuntivo).

            if (!linea.trim().equals("Futuro")){ //Si se ha desalineado
                  LogUtil.logError("[INICIALIZACION FASE_1] Error en alineacion en Futuro ["+infinitivo.getText()+"]. Encontrado: "+linea);
            }

           // ******* SUBJUNTIVO FUTURO ******
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);

           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Preterito imperfecto (del subjuntivo).

           // ******* SUBJUNTIVO PRETÉRITO IMPERFECTO ******
           //Primera forma del pretérito imperfecto del subjuntivo (amara)
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);

           //Segunda forma del pretérito imperfecto del subjuntivo (amase)
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);

           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Modo condicional.

            if (!linea.trim().equals("Modo condicional")){ //Si se ha desalineado
                  LogUtil.logError("[INICIALIZACION FASE_1] Error en alineacion en Modo condicional ["+infinitivo.getText()+"]. Encontrado: "+linea);
            }

           // ******* MODO CONDICIONAL ******
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);

           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Modo imperativo.

           // ******* MODO IMPERATIVO ****** (no hay primera persona)
           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Afirmativo.
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
           linea=fich.nextLine().trim(); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);

           linea=fich.nextLine(); //Leemos una línea del fichero con el texto: Negativo.
           linea=fich.nextLine().trim().replace("\t", " "); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,true);
           linea=fich.nextLine().trim().replace("\t", " "); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,true);
           linea=fich.nextLine().trim().replace("\t", " "); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,true);
           linea=fich.nextLine().trim().replace("\t", " "); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,true);
           linea=fich.nextLine().trim().replace("\t", " "); //Leemos una línea del fichero
           loadLineIrregularVerb(linea, getDictionary(),DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,true);

       }//while
       FileUtil.closeFile(is); //Cerramos el fichero
    }//Carga verbos irregulares


    /**
     * Obtiene el lexema (raiz) de un verbo regular a partir de su infinitivo (-ar, -er, -ir).
     * P.e. "medir", su lexema es "med" (al eliminar la terminación "-ir").
     * @param infinitivo Cadena con el infinitivo del verbo regular.
     * @return Cadena con el lexema del verbo.
     */
    private static String getRegVerbLexema(String infinitivo){
        String result="";
        if (infinitivo.length()>=2){ //El verbo debe tener al menos dos letras (otra cosa es un error)
           result=infinitivo.substring(0, infinitivo.length()-2); //simplemente quitamos la terminación
        }
        return result;
    }

    /**
     * Obtiene la terminacion de un verbo regular (-ar, -er, -ir)
     * @param infinitivo Cadena con el infinitivo del verbo regular.
     * @return Cadena con el lexema del verbo.
     */
    private static String getRegVerbTerminacion(String infinitivo){
        String result="";
        if (infinitivo.length()>=2){ //El verbo debe tener al menos dos letras (otra cosa es un error)
           result=infinitivo.substring(infinitivo.length()-2); //nos quedamos con la terminación
        }
        return result;
    }


    /**
     * Genera las conjugaciones de un verbo regular terminado en "-AR" a partir de su lexema, y las añade al diccionario.
     * No comprueba si existen previamente. Tampoco se comprobó en los verbos irregulares.
     * @param infinitivoTxt Infinitivo del verbo regular a conjugar (p.e. "amar").
     * @param lexema Raíz del verbo regular a conjugar (p.e. "am".
     */
    private static void generaConjugacionesAr(String infinitivoTxt, String lexema){

        //Infinitivo
        DVerb infinitivo=new DVerb(infinitivoTxt,lexema,DVerb.TIEMPO_INFINITIVO,null,null,null,null,false);
        getDictionary().addVerb(infinitivo); //Lo añadimos a la lista de verbos

        //Gerundio
        DVerb verb=new DVerb(lexema+"ando",infinitivoTxt,DVerb.TIEMPO_GERUNDIO,null,infinitivo,null,null,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        //Participio
        verb=new DVerb(lexema+"ado",infinitivoTxt,DVerb.TIEMPO_PARTICIPIO,null,infinitivo,null,null,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO PRESENTE ******
        verb=new DVerb(lexema+"o",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"as",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"a",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"amos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"áis",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"an",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO PRETERITO IMPERFECTO ******
        verb=new DVerb(lexema+"aba",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"abas",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aba",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ábamos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"abais",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aban",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO PRETERITO PERFECTO SIMPLE ******
        verb=new DVerb(lexema+"é",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aste",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ó",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"amos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"asteis",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aron",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO FUTURO SIMPLE******
        verb=new DVerb(lexema+"aré",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"arás",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ará",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aremos",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aréis",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"arán",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* SUBJUNTIVO PRESENTE ******
        verb=new DVerb(lexema+"e",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"es",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"e",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"emos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"éis",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"en",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* SUBJUNTIVO FUTURO ******
        verb=new DVerb(lexema+"are",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ares",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"are",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"áremos",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"areis",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aren",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* SUBJUNTIVO PRETÉRITO IMPERFECTO ******
        //Primera forma del pretérito imperfecto del subjuntivo (amara)
        verb=new DVerb(lexema+"ara",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aras",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ara",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"áramos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"arais",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aran",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        //Segunda forma del pretérito imperfecto del subjuntivo (amase)
        verb=new DVerb(lexema+"ase",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ases",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ase",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ásemos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aseis",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"asen",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* MODO CONDICIONAL ******
        verb=new DVerb(lexema+"aría",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"arías",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aría",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aríamos",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"aríais",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"arían",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* MODO IMPERATIVO ****** (no hay primera persona)
        //Forma Afirmativa
        verb=new DVerb(lexema+"a",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"e",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"emos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ad",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"en",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        //Forma Negativa
        verb=new DVerb("no "+lexema+"es",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR, true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+"e",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR, true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+"emos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL, true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+"éis",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL, true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+"en",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL, true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
    }//function

    /**
     * Genera las conjugaciones de un verbo regular terminado en "-ER" a partir de su lexema, y las añade al diccionario.
     * No comprueba si existen previamente. Tampoco se comprobó en los verbos irregulares.
     * @param infinitivoTxt Infinitivo del verbo regular a conjugar (p.e. "temer").
     * @param lexema Raíz del verbo regular a conjugar (p.e. "tem".
     */
    private static void generaConjugacionesEr(String infinitivoTxt, String lexema){
        String lastLetra=lexema.substring(lexema.length()-1, lexema.length()); //Miramos la última letra
        String letraY=(lastLetra.equals("e")?"y":"i"); //Le-er -> Le-yendo, etc
        String letraY2=(lastLetra.equals("e")?"í":"i"); //Le-ir -> Le-ído, etc

        //Infinitivo
        DVerb infinitivo=new DVerb(infinitivoTxt,lexema,DVerb.TIEMPO_INFINITIVO,null,null,null,null,false);
        getDictionary().addVerb(infinitivo); //Lo añadimos a la lista de verbos

        //Gerundio
        DVerb verb=new DVerb(lexema+letraY+"endo",infinitivoTxt,DVerb.TIEMPO_GERUNDIO,null,infinitivo,null,null,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        //Participio
        verb=new DVerb(lexema+letraY2+"do",infinitivoTxt,DVerb.TIEMPO_PARTICIPIO,null,infinitivo,null,null,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO PRESENTE ******
        verb=new DVerb(lexema+"o",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"es",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"e",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"emos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"éis",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"en",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO PRETERITO IMPERFECTO ******
        verb=new DVerb(lexema+"ía",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ías",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ía",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"íamos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"íais",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ían",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO PRETERITO PERFECTO SIMPLE/INDEFINIDO ******
        verb=new DVerb(lexema+"í",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"ste",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"ó",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"mos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"steis",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eron",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO FUTURO SIMPLE******
        verb=new DVerb(lexema+"eré",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"erás",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"erá",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"eremos",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"eréis",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"erán",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* SUBJUNTIVO PRESENTE ******
        verb=new DVerb(lexema+"a",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"as",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"a",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"amos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"áis",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"an",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* SUBJUNTIVO FUTURO ******
        verb=new DVerb(lexema+letraY+"ere",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eres",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"ere",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"éremos",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"ereis",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eren",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* SUBJUNTIVO PRETÉRITO IMPERFECTO ******
        //Primera forma del pretérito imperfecto del subjuntivo (amara)
        verb=new DVerb(lexema+letraY+"era",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eras",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"era",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"éramos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"erais",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eran",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        //Segunda forma del pretérito imperfecto del subjuntivo (amase)
        verb=new DVerb(lexema+letraY+"ese",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eses",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"ese",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"ésemos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eseis",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"esen",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* MODO CONDICIONAL ******
        verb=new DVerb(lexema+"ería",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"erías",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ería",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"eríamos",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"eríais",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"erían",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* MODO IMPERATIVO ****** (no hay primera persona)
        //Forma Afirmativa
        verb=new DVerb(lexema+"a",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"a",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"amos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ed",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"an",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        //Forma Negativa
        verb=new DVerb("no "+lexema+"as",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+"a",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+"amos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+"áis",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+"an",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
    }//function

    /**
     * Genera las conjugaciones de un verbo regular terminado en "-IR" a partir de su lexema, y las añade al diccionario.
     * Verbos del tipo: part-ir.
     * AVISO: No comprueba si existen previamente. Tampoco se comprueban en los verbos irregulares.
     * @param infinitivoTxt Infinitivo del verbo regular a conjugar (p.e. "partir", "pedir", "medir", etc).
     * @param lexema Raíz del verbo regular a conjugar (p.e. "part").
     */
    private static void generaConjugacionesIr(String infinitivoTxt, String lexema){
        String lastLetra=lexema.substring(lexema.length()-1, lexema.length()); //Miramos la última letra
        String letraY=(lastLetra.equals("u")?"y":"i"); //Inclu-ir -> Inclu-yendo, Inclu-ye, etc
        String letraY2=(lastLetra.equals("u")?"y":""); //Inclu-ir -> Inclu-ye, etc

        //Infinitivo
        DVerb infinitivo=new DVerb(infinitivoTxt,lexema,DVerb.TIEMPO_INFINITIVO,null,null,null,null,false);
        getDictionary().addVerb(infinitivo); //Lo añadimos a la lista de verbos

        //Gerundio
        DVerb verb=new DVerb(lexema+letraY+"endo",infinitivoTxt,DVerb.TIEMPO_GERUNDIO,null,infinitivo,null,null,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        //Participio
        verb=new DVerb(lexema+"ido",infinitivoTxt,DVerb.TIEMPO_PARTICIPIO,null,infinitivo,null,null,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO PRESENTE ******
        verb=new DVerb(lexema+letraY2+"o",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"es",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"e",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"imos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ís",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"en",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_PRESENTE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO PRETERITO IMPERFECTO ******
        verb=new DVerb(lexema+"ía",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ías",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ía",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"íamos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"íais",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"ían",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_IMPERFECTO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO PRETERITO PERFECTO SIMPLE/INDEFINIDO ******
        verb=new DVerb(lexema+"í",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"iste",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"ó",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"imos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"isteis",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eron",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_PRETERITO_PERFECTO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* INDICATIVO FUTURO SIMPLE******
        verb=new DVerb(lexema+"iré",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"irás",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"irá",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"iremos",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"iréis",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"irán",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* SUBJUNTIVO PRESENTE ******
        verb=new DVerb(lexema+letraY2+"a",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"as",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"a",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"amos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"áis",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"an",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_SUBJUNTIVO_PRESENTE_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* SUBJUNTIVO FUTURO ******
        verb=new DVerb(lexema+letraY+"ere",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eres",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"ere",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"éremos",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"ereis",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eren",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_SUBJUNTIVO_FUTURO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* SUBJUNTIVO PRETÉRITO IMPERFECTO ******
        //Primera forma del pretérito imperfecto del subjuntivo (amara)
        verb=new DVerb(lexema+letraY+"era",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eras",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"era",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"éramos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"erais",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eran",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        //Segunda forma del pretérito imperfecto del subjuntivo (amase)
        verb=new DVerb(lexema+letraY+"ese",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eses",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"ese",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"ésemos",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"eseis",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY+"esen",infinitivoTxt,DVerb.TIEMPO_PASADO,DVerb.MODO_SUBJUNTIVO_PRETERITO_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* MODO CONDICIONAL ******
        verb=new DVerb(lexema+"iría",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"irías",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"iría",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"iríamos",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"iríais",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"irían",infinitivoTxt,DVerb.TIEMPO_FUTURO,DVerb.MODO_CONDICIONAL_SIMPLE,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        // ******* MODO IMPERATIVO ****** (no hay primera persona)
        //Forma Afirmativa
        verb=new DVerb(lexema+letraY2+"e",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"a",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"amos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+"id",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb(lexema+letraY2+"an",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_AFIRMATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,false);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo

        //Forma Negativa
        verb=new DVerb("no "+lexema+letraY2+"as",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_SINGULAR,true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+letraY2+"a",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_SINGULAR,true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+letraY2+"amos",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_PRIMERA, DVerb.NUMERO_PLURAL,true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+letraY2+"áis",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_SEGUNDA, DVerb.NUMERO_PLURAL,true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
        verb=new DVerb("no "+lexema+letraY2+"an",infinitivoTxt,DVerb.TIEMPO_PRESENTE,DVerb.MODO_IMPERATIVO_NEGATIVO,infinitivo,DVerb.PERSONA_TERCERA, DVerb.NUMERO_PLURAL,true);
        getDictionary().addVerb(verb); //Lo añadimos a la lista de verbos
        infinitivo.addConjugacion(verb); //También añadimos la conjugación al infinitivo
    }//function

    /**
     * Esta función carga los verbos regulares. Lee su infinitivo de un archivo, y genera todos sus tiempos verbales.
     * REQUISITO: EN EL DICCIONARIO DEBEN ESTAR PRECARGADOS LOS VERBOS IRREGULARES.
     */
    private static void cargaVerbosRegulares(){

       InputStream is=FileUtil.openFile(PATH_ALL_VERBOS);
       Scanner fich=new Scanner(is);
       while (fich.hasNextLine()){
           //Infinitivo
           String linea=fich.nextLine().trim(); //Leemos una línea del fichero con un Infinitivo

           if (!dictionary.existeVerbo(linea)){ //Si no existe en el dictionary su Infinitivo, implica que se trata de un verbo regular
               String terminacion=getRegVerbTerminacion(linea); //Obtenemos la terminación del verbo regular: ar, er, ir
               String lexema=getRegVerbLexema(linea); //Obtenemos el lexema del verbo regular: ar, er, ir

               LogUtil.logTraza("[INICIALIZACION FASE_1] Procesando Verbo Regular: "+linea,10);

               switch (terminacion) { //En función de la terminación cargamos todas las conjugaciones en el dictionary
                   case "ar": generaConjugacionesAr(linea,lexema);
                       break;
                   case "er": generaConjugacionesEr(linea,lexema);
                       break;
                   case "ir": generaConjugacionesIr(linea,lexema);
                       break;
                   default: //Terminacion "se". Caso de verbos como zurrarse, mojarse, etc. Ignoramos estos verbos.
                       break;
               }//switch
           }//if

       }//while
       FileUtil.closeFile(is); //Cerramos el fichero
    }//Carga verbos irregulares

    /**
     * En base a la gramática española, establece/inicializa el género de la palabra.
     * Reglas de genero: https://espanol.lingolia.com/es/gramatica/sustantivos/genero
     * @param word Palabra de la que no se conoce el género.
     */
    private static void estableceGenero(DWord palabra){
        String word=palabra.getText();
        word=word.toLowerCase();
        Booleano result=null;
        int longitud=word.length();

        if (longitud==1){ //Si es una letra, es femenino
            result=new Booleano(false); //Es femenino
        }

        String term=StringUtil.terminacion(word, 1); //Ultima letra
        if (term.equals("a")){
            result=new Booleano(false); //Es femenino
        }
        if (term.equals("o")||term.equals("n")||term.equals("r")||term.equals("s")||term.equals("e")||term.equals("l")
            ||term.equals("é")||term.equals("i")||term.equals("í")||term.equals("x")||term.equals("y")){
            result=new Booleano(true); //Es masculino
        }

        term=StringUtil.terminacion(word, 2); //Ultimas 2 letras
        if (term.equals("ad")||term.equals("ed")||term.equals("id")||term.equals("ud")||term.equals("úd")
                ||term.equals("ez")){
            result=new Booleano(false); //Es femenino
        }
        if (term.equals("ma")||term.equals("pa")){ //En algunos casos "ta" es masculino, pero en muchos otros no
            result=new Booleano(true); //Es masculino
        }

        term=StringUtil.terminacion(word, 3); //Ultimas 3 letras
        if (term.equals("dad")||term.equals("tad")||term.equals("eza")||term.equals("zón")){
            result=new Booleano(false); //Es femenino
        }
        if (term.equals("aje")||term.equals("che")){
            result=new Booleano(true); //Es masculino
        }

        term=StringUtil.terminacion(word, 4); //Ultimas 4 letras
        if (term.equals("ción")||term.equals("sión")){
            result=new Booleano(false); //Es femenino
        }

        term=StringUtil.terminacion(word, 5); //Ultimas 5 letras
        if (term.equals("umbre")){
            result=new Booleano(false); //Es femenino
        }
        if (term.equals("ambre")){
            result=new Booleano(true); //Es masculino
        }

        term=StringUtil.terminacion(word, 6); //Ultimas 6 letras
        if (term.equals("miento")){
            result=new Booleano(true); //Es masculino
        }

        //EXCEPCIONES
        if (word.equals("foto")||word.equals("mano")||word.equals("flor")||word.equals("noche")||word.equals("piel")
            ||word.equals("cárcel")||word.equals("crema")||word.equals("cama")||word.equals("imagen")||word.equals("capa")
            ||word.equals("miel")){
            result=new Booleano(false); //Es femenino
        }
        if (NumberUtil.isNumber(word)||word.equals("día")||word.equals("ataúd")||word.equals("arcoíris")){
            result=new Booleano(true); //Es masculino
        }

        palabra.setGeneroValue(result); //Establecemos el género
    }

    /**
     * A partir de la gramática el español, genera el plural de la palabra singular que se recibe, y las añade al diccionario.
     * Hay palabras que admiten dos plurales, p.e. jabalí -> jabalís o jabalíes.
     * También enlazamos cada palabra singular con sus plurales, y cada plural con su palabra en singular.
     * La raiz de cada palabra plural generada (jabalíes) es la palabra en singular (jabalí).
     * AVISO: Hay palabras que no tienen/se usan en plural: p.e. la "paciencia", etc. Y otras solo existen en plural: agujetas, celos, abrelatas, etc.
     * @param palabra Palabra singular de la que se desea obtener el plural (puede ser una palabra o dos).
     */
    private static void generaPlurales(DWord palabra){
        String word=palabra.getText();
        ArrayList<DWord> result=new ArrayList();
        word=StringUtil.trimNormalizado(word, true);
        String raiz=word;
        String term=StringUtil.terminacion(word, 1);
        if (term.equals("a")||term.equals("e")||term.equals("i")||term.equals("o")||term.equals("u")){
           word+="s"; //Añadimos una "s"
           result.add(new DWord(word,raiz,palabra.getGeneroValue(),new Booleano(false))); //Añadimos este plural al resultado
        }else if (term.equals("í")||term.equals("ú")){ //este caso admite dos plurales
           String plu1=word+"s"; //Añadimos una "s"
           String plu2=word+"es"; //Añadimos una "es"
           result.add(new DWord(plu1,raiz,palabra.getGeneroValue(),new Booleano(false))); //Añadimos este plural al resultado
           result.add(new DWord(plu2,raiz,palabra.getGeneroValue(),new Booleano(false))); //Añadimos este plural al resultado
        }else if (term.equals("d")||term.equals("j")||term.equals("n")||term.equals("l")||term.equals("r")){
           word+="es"; //Añadimos una "es"
           result.add(new DWord(word,raiz,palabra.getGeneroValue(),new Booleano(false))); //Añadimos este plural al resultado
        }else if (term.equals("z")){
           word=word.substring(0,word.length()-1)+"ces"; //Quitamos la "z" y añadimos una "ces". P.e. cruz -> cruces
           result.add(new DWord(word,raiz,palabra.getGeneroValue(),new Booleano(false))); //Añadimos este plural al resultado
        }else{ //El caso por defecto admite dos plurales
           String plu1=word+"s"; //Añadimos una "s"
           String plu2=word+"es"; //Añadimos una "es"
           result.add(new DWord(plu1,raiz,palabra.getGeneroValue(),new Booleano(false))); //Añadimos este plural al resultado
           result.add(new DWord(plu2,raiz,palabra.getGeneroValue(),new Booleano(false))); //Añadimos este plural al resultado
        }

        //Enlazamos la palabra singular con sus plurales
        palabra.setPalabrasNumeroOpuesto(result);
        // Añadimos los plurales al diccionario y enlazamos cada plural con su singular
        for (DWord plu:result){
            SpanishDictionary.dictionary.addWord(plu); //Añadimos un plural al diccionario
            ArrayList<DWord> lsingular=new ArrayList(); //Creamos una lista para meter el singular y enlazarlo con este plural
            lsingular.add(palabra); //Singular
            plu.setPalabrasNumeroOpuesto(lsingular); //Establecemos cual es el singular de cada palabra plural generada
        }//for
    }

    /**
     * Carga todas las palabras del diccionario (son todas singulares). Incluye los infinitivos de los verbos, que aquí solo son meras palabras.
     * También incluye los plurales (y singulares) de cada palabra, que están enlazados mutuamente.
     * AVISO: Falta enlazar las palabras masculinas con su correspondiente femenino, y viceversa.
     */
    private static void cargaPalabras1(){

       //El fichero de palabras incluye solo palabras en singular (no incluye plurales ni conjugaciones verbales).
       //También incluye infinitivos verbales (ya presentes en la lista de verbos).
       //Incluye también palabras en masculino y femenino, pero no están relacionadas.
       //Incluye también nombres propios, pero al estar en minúsculas resultan indiferenciables.
       //Contiene también cierta cantidad de errores: acentos faltantes, palabras faltantes, palabras erróneas, etc.
       InputStream is=FileUtil.openFile(SpanishDictionary.PATH_PALABRAS_1);
       Scanner fich=new Scanner(is);
       while (fich.hasNextLine()){
           //Palabra singular
           String linea=fich.nextLine().trim(); //Leemos una línea del fichero con una palabra (siempre son singulares)

           DWord palabra=new DWord(linea,linea); //Creamos una palabra
           estableceGenero(palabra); //Establecemos su género de acuerdo a la gramática española.
           palabra.setNumeroSingular(); //En principio todas las palabras que se cargan son de numero singular.
           generaPlurales(palabra); //Generamos sus plurales y los añadimos al diccionario. Puede haber uno o dos plurales. Enlazamos el singular con el plural, y viceversa.
           getDictionary().addWord(palabra); //La añadimos la palabra singular al dictionary.

       }//while
       FileUtil.closeFile(is); //Cerramos el fichero
    }//Carga palabras


    /**
     * Carga en el diccionario (si no lo está ya) un participio y su femenino, en singular y plural.
     * El lexema de todos ellos será el participio especificado.
     * Los participios actúan como adjetivos, por tanto deben figurar entre las palabras. P.e. "cortado, cortada, cortados, cortadas"
     * @param participio Participio a incluir en el diccionario.
     */
    private static void cargaParticipioComoAdjetivo(String participio){
        String fem=participio.substring(0,participio.length()-1)+"a"; //Componemos el femenino singular ("cortad-a")
        String femPlu=fem+"s"; //Componemos el femenino plural ("cortada-s")
        String partPlu=participio+"s"; //Componemos el masculino plural ("cortado-s")

        //Creamos las palabras y establecemos su género y número
        DWord partMasSin=new DWord(participio,participio,new Booleano(true),new Booleano(true));
        DWord partFemSin=new DWord(fem,participio,new Booleano(false),new Booleano(true));
        DWord partMasPlu=new DWord(partPlu,participio,new Booleano(true),new Booleano(false));
        DWord partFemPlu=new DWord(femPlu,participio,new Booleano(false),new Booleano(false));

        //Establecemos su POS
        partMasSin.addPOS(DWord.POS_ADJ); //Añadimos la tipología adjetivo
        partMasPlu.addPOS(DWord.POS_ADJ); //Añadimos la tipología adjetivo
        partFemSin.addPOS(DWord.POS_ADJ); //Añadimos la tipología adjetivo
        partFemPlu.addPOS(DWord.POS_ADJ); //Añadimos la tipología adjetivo

        //Enlazamos las palabras
        partMasSin.setPalabraGeneroOpuesto(partFemSin); //Singular: Masculino->Femenino. "cortado"->"cortada"
        partFemSin.setPalabraGeneroOpuesto(partMasSin); //Singular: Femenino->Masculino. "cortada"->"cortado"
        partMasPlu.setPalabraGeneroOpuesto(partFemPlu); //Plural: Masculino->Femenino. "cortados"->"cortadas"
        partFemPlu.setPalabraGeneroOpuesto(partMasPlu); //Plural: Femenino->Masculino. "cortadas"->"cortados"

        //Solo los singulares tienen plurales: los creamos y enlazamos
        ArrayList<DWord> pluralesMas=new ArrayList(); //Plural del masculino singular
        pluralesMas.add(partMasPlu);
        partMasSin.setPalabrasNumeroOpuesto(pluralesMas); //"cortado"->"cortados"
        pluralesMas=new ArrayList(); //Enlazamos en sentido contrario
        pluralesMas.add(partMasSin);
        partMasPlu.setPalabrasNumeroOpuesto(pluralesMas); //"cortados"->"cortado"

        ArrayList<DWord> pluralesFem=new ArrayList(); //Plural del femenino singular
        pluralesFem.add(partFemPlu);
        partFemSin.setPalabrasNumeroOpuesto(pluralesFem); //"cortada"->"cortadas"
        pluralesFem=new ArrayList(); //Enlazamos en sentido contrario
        pluralesFem.add(partFemSin);
        partFemPlu.setPalabrasNumeroOpuesto(pluralesFem); //"cortadas"->"cortada"

        //Guardamos las palabras en el diccionario
        dictionary.addWord(partMasSin);
        dictionary.addWord(partMasPlu);
        dictionary.addWord(partFemSin);
        dictionary.addWord(partFemPlu);
    }


    /**
     * Genera los femeninos a partir de las terminaciones presentes en el fichero de carga de palabras.
     * @param masculino Palabra en masculino singular. P.e. "abad"
     * @param termFemenino Terminacion de la palabra en femenino singular. P.e. "desa" -> "abadesa"
     * @return Palabra en femenino singular. P.e. "abadesa"
     */
    private static String getFemenino(String masculino, String termFemenino){
        String result="";
        if (termFemenino.length()==1){ //Si la terminación femenina tiene una sola letra
           result=masculino.substring(0, masculino.length()-1)+termFemenino; //Sustituímos la última letra por la terminación
        }else{ //Si la terminación femenino tiene más de una letra
            String letra=termFemenino.substring(0,1); //Primera letra de la terminacion femenina (debe estar en minuscula)
            int idx=masculino.lastIndexOf(letra); //Ultima posicion de la letra terminacion femenina
            int idxAcento=-1; //índice auxiliar para sortear el problema de "él, ella", "melón, melona"

            if (StringUtil.isVocal(letra)){

                if (idx<0){ //Si la vocal no se encontró en el masculino
                    LogUtil.logTraza("[INICIALIZACION FASE_1] Generando: "+masculino+" -> "+termFemenino,10);
                }

                letra=(letra.equals("a")?"á":letra);
                letra=(letra.equals("e")?"é":letra); //él, ella
                letra=(letra.equals("i")?"í":letra);
                letra=(letra.equals("o")?"ó":letra);
                letra=(letra.equals("u")?"ú":letra);
                idxAcento=masculino.lastIndexOf(letra); //Miramos si hay una ocurrencia de letra con acento
            }

            idx=(idx>idxAcento?idx:idxAcento); //Nos quedamos con la posicion más lejana para enganchar la terminación femenina

            if (StringUtil.isAcentuada(termFemenino)){ //Si la terminación femenina tiene acentos
                masculino=StringUtil.eliminarAcentos(masculino); //Le quitamos los acentos al masculino: hér-oe/her-oína
            }

            try{
                result=masculino.substring(0, idx)+termFemenino; //Creamos el femenino
            }catch (Exception e){
                LogUtil.logError("Error generando el femenino: "+masculino+" -> "+termFemenino);
            }
        }//Else (la terminacion femenino tiene mas de una letra
        return result;
    }

    /**
     * Carga las palabras del conjunto de ficheros "a.txt" a "z.txt".
     */
    private static void cargaPalabras2(){
        /*
            0. Son todas palabras singulares.
            1. Hay palabras repetidas, la única diferencia entre ellas es que terminan en un numero.
               P.e. palabra1
                    palabra2
                    palabra3
               Por tanto hay que eliminar esos números y comprobar si la palabra ya existe antes de introducirla en el diccionario.

            2. Hay palabras con guiones que hay que eliminar.
               P.e. a-1
                    a-2
               Por tanto, eliminar estos guiones y números también igual que en paso 1.

            3. Las palabras generalmente son en masculino (hay algunas femeninas), pero tienen su terminación femenina asociada, que se muestra separada por una coma. Las palabras que tienen género son los ADJ y los NOUN sexuados.
               P.e. "abad, desa" (aunque también aparece la palabra "abadesa" directamente en el diccionario)               Para reflejar: abad, abadesa
               P.e. abismado, da
               P.e. A veces aparece un numero por el medio: ablativo2, va
               Por tanto debemos mirar la primera letra de la terminación y engancharla en la última ocurrencia de dicha letra en la palabra en masculino.

            4. Hay palabras con espacios. Son latinajos sobre todo. P.e. "ab initio", "a capela"

            5. Aparecen terminaciones sueltas como "-able". Por tanto las líneas que empiecen por "-" hay que ignorarlas. También aparecen con su femenino, o con un numero. P.e. "-azo, za", "-atorio, ria", "-ato1, ta"

            6. Hay infinitivos de verbos, pero no conjugaciones. P.e. "abochornar"

            7. Hay acrónimos. P.e. "ABS". Hay nombres de personas y lugares (empiezan por mayúscula).

            8. Hay solo algunas palabras acabadas en "-able", "-mente", "-miento", "-ción", "-bilidad", "-or", "-ivo", "-oso", "-ería"
        */
       boolean fin=false;
       char c='a';
       while (!fin){ //Recorremos los ficheros de "a.txt" a "z.txt" sin olvidarnps de "ñ.txt"
           String n=""+c;
           if (c=='{'){ //Si toca leer el fichero con la "ñ" ('{' es el siguiente caracter a la z)
               fin=true; //Procesamos el fichero de la "ñ", y finalizamos.
               n="nn";
           }
           String fileName=SpanishDictionary.PATH_PALABRAS_2+n+".txt";
           InputStream is=FileUtil.openFile(fileName);
           LogUtil.logTraza("[INICIALIZACION FASE_1] Cargando fichero: "+fileName,7);
           if (is==null) return; //Si el fichero no existe salimos

           Scanner fich=new Scanner(is);
           while (fich.hasNextLine()){
               //Palabra singular
               String linea=fich.nextLine(); //Leemos una línea del fichero con una palabra (siempre son singulares)

               if (!StringUtil.isEmpty(linea)&&linea.charAt(0)!='-'){ //Si no es cadena vacía y no empieza por "-"

                linea=StringUtil.trimNormalizado(linea, false,true); //Eliminamos numeros y guiones, pero no espacios

                if (linea.indexOf(",")>0){ //Si tiene una coma ("abad, desa"), implica que tiene femenino definido, y por tanto no es un Nombre Propio PROPN
                   String w[]=linea.split(","); //La primera palabra es el masculino, la segunda la terminación del femenino
                   if (w.length!=2){ //Si hay coma, pero no existe terminación femenina, o existe más de una coma -> error
                            LogUtil.logError("Error procesando línea: "+linea);
                            return;
                   }

                   String wordm=w[0].trim(); //Masculino

                   String wordf=w[1].trim(); //Terminación femenina
                   DWord masculino=new DWord(wordm,wordm); //Creamos una palabra con el singular masculino
                   masculino.setNumeroSingular(); //En principio todas las palabras que se cargan son de numero singular.
                   masculino.setGeneroMasculino(); //Y masculino

                   String strfem=getFemenino(wordm,wordf); //Generamos la palabra en femenino
                   DWord femenino=new DWord(strfem,wordm); //Creamos una palabra con el femenino
                   femenino.setNumeroSingular(); //En principio todas las palabras que se cargan son de numero singular.
                   femenino.setGeneroFemenino();

                   masculino.setPalabraGeneroOpuesto(femenino); //Enlazamos masculino y femenino
                   femenino.setPalabraGeneroOpuesto(masculino);
                   getDictionary().addWord(masculino); //La añadimos la palabra masculina al dictionary.
                   getDictionary().addWord(femenino); //La añadimos la palabra femenina al dictionary.

                   generaPlurales(masculino); //Generamos sus plurales (perros) y los añadimos al diccionario. Puede haber uno o dos plurales. Enlazamos el singular con el plural, y viceversa.
                   generaPlurales(femenino); //Generamos sus plurales (perras) y los añadimos al diccionario. Puede haber uno o dos plurales. Enlazamos el singular con el plural, y viceversa.
                   for (DWord plum:masculino.getPalabrasNumeroOpuesto()){ //El género opuesto de perros es perras
                       plum.setPalabraGeneroOpuesto(femenino.getPalabrasNumeroOpuesto().get(0));
                   }
                   for (DWord pluf:femenino.getPalabrasNumeroOpuesto()){
                       pluf.setPalabraGeneroOpuesto(masculino.getPalabrasNumeroOpuesto().get(0)); //Y el de perras es perros
                   }

                }else{ //Aunque no tenga femenino, la palabra tiene un género (p.e. juez)
                   DWord palabra=new DWord(linea,linea);
                   palabra.setNumeroSingular(); //En principio todas las palabras que se cargan son de numero singular.
                   if (!(linea.indexOf(' ')>0)){ //Si no es una palabra con espacios, p.e. "ab initio"
                        estableceGenero(palabra); //Establecemos su género de acuerdo a la gramática española.

                        if (!StringUtil.isName(linea)){ //Solo generamos el plural si no es un nombre propio
                           generaPlurales(palabra); //Generamos sus plurales y los añadimos al diccionario. Puede haber uno o dos plurales. Enlazamos el singular con el plural, y viceversa.
                        }
                   }
                   getDictionary().addWord(palabra); //La añadimos la palabra al dictionary.
                } //else: no tiene femenino
               }//Si no empieza por guion
           }//while hay siguiente linea
           FileUtil.closeFile(is); //Cerramos el fichero pues ya leimos todo

           c++; //Por último incrementamos la letra, para seguir avanzando
       }//while !fin
    }//Carga palabras 2

    /**
     * Extrae la lista de sinónimos/antónimos de un trozo de texto con términos separados por comas
     * @param lista Trozo de texto a analizar.
     * @return Lista de palabras encontrada
     */
    private static ArrayList<String> extractSinAnt(String lista){
       ArrayList<String> result=new ArrayList(); //Lista para guardar los sinónimos/antónimos sanos

       if (!StringUtil.isEmpty(lista)){ //Si hay algo que analizar (los antónimos a veces no los hay -> cadena vacía)

            int i=lista.indexOf("-"); //Primer guión. abatido-da, decaído, desalentado
            if (i>0){
                int x=lista.indexOf(","); //primera coma ","
                if (x>0){
                   lista=lista.substring(0, i)+lista.substring(x); //eliminamos el fragmento "-da"
                }
            }

            int nerr=0;
            String chunks[]=lista.split(","); //troceamos los sinónimos/antónimos para obtener cada uno de ellos.
            for (String sin:chunks){
                String txt=StringUtil.trimNormalizado(sin); //Limpiamos la palabra
                LogUtil.logTraza("[INICIALIZACION FASE_1] Sinónimo: "+txt,10);
                if (txt.indexOf(" ")>0){ //Si tiene multiples palabras en su interior lo ignoramos: es un fallo del fichero
                    nerr++;
                    LogUtil.logTraza("[INICIALIZACION FASE_1] Posible error ["+nerr+"]: "+txt,7);
                }else{
                    result.add(txt); //Añadimos la palabra a la lista de sinónimos/antónimos
                }
            }//for
       }
       return result;
    }

    /**
     * A partir de una lista de palabras en texto, devuelve el listado de palabras que se encuentren en el diccionario.
     * @param lista Listado de palabras en texto.
     * @return Lista de DWords que se encuentren en el diccionario.
     */
    private static ArrayList<DWord> getDWordList(ArrayList<String> lista){
        ArrayList<DWord> result=new ArrayList();
        for (String palabra:lista){
            DWord word=getDictionary().getWord(palabra); //recuperamos la palabra
            if (word!=null){ //Si existe la añadimos, si no la ignoramos
                result.add(word); //La añadimos
            }
        }//for
        return result;
    }

    /**
     * Enlaza cada sinónimo con el resto, y con sus antónimos. Los antónimos igualmente se enlazan con sus sinónimos.
     * @param sinonimos Lista de sinónimos.
     * @param antonimos Lista de antónimos. Aquí no vamos a establecer que los antónimos son sinónimos entre sí.
     */
    private static void enlazarSinonimosAntonimos(ArrayList<String> sinonimos, ArrayList<String> antonimos){
        ArrayList<DWord> dsinonimos=getDWordList(sinonimos); //Nos quedamos solo con las palabras que están en el diccionario
        ArrayList<DWord> dantonimos=getDWordList(antonimos);

        for (DWord palabra:dsinonimos){ //Establecemos la lista de sinónimos de todas las palabras sinónimas
            palabra.setSinonimos(dsinonimos); //Los que ya existan no se duplican
            palabra.setAntonimos(dantonimos); //Al añadir los antónimos de cada palabra, ya se hace en el mismo paso la operación contraria (añadir como antónimos de los antónimos, los sinónimos)
            //Aquí no vamos a establecer que los antónimos son sinónimos entre sí.
        }//for
    }

    /**
     * Carga en el diccionario los sinónimos y antónimos de cada palabra presente en el diccionario.
     * Todas las palabras cargadas deben encontrarse previamente en el diccionario. Aquellas que no lo estén serán desechadas.
     */
    private static void cargaSinonimosAntonimos(){
       //Hay sinónimos que realmente son taxonomías. P.e. ave, pájaro
       //Hay sinónimos que realmente son acepciones (polisemia): ayuntamiento (Concejo), coito (cópula); o calavera, libertino
       InputStream is=FileUtil.openFile(SpanishDictionary.PATH_SINONIMOS_ANTONIMOS);
       Scanner fich=new Scanner(is);
       while (fich.hasNextLine()){
           //Palabra singular
           String linea=fich.nextLine().trim(); //Leemos una línea del fichero con una ristra e sinónimos y antónimos
           if (!StringUtil.isEmpty(linea)&&linea.charAt(0)!='-'){

               int i=linea.indexOf("/"); //Todo lo que hay de "//" hacia adelante son otras acepciones. Nos las cargamos
               if (i>=0){
                   linea=linea.substring(0, i); //Borramos de la primera barra hasta final de línea
               }
               i=linea.indexOf("Par."); //Todo lo que hay de "Par." hacia adelante son metáforas. Nos las cargamos
               if (i>=0){
                   linea=linea.substring(0, i); //Borramos de la primera barra hasta final de línea
               }

               String lsinonimos=linea; //En principio todo son sinónimos
               String lantonimos=null;
               int a1=linea.indexOf("Ant.");
               int a2=linea.indexOf("Ant ");
               if (a1>0||a2>0){ //Si hay antónimos en la línea
                  String aux=(a1>0?".":" ");
                  String sublin[]=linea.split("Ant"+aux);
                  lsinonimos=sublin[0]; //Separamos los sinónimos, si solo son un chunk de la cadena
                  lantonimos=sublin[1].trim().toLowerCase(); //De los antónimos
               }
               lsinonimos=lsinonimos.trim().toLowerCase(); //Pasamos todo a minuscula

               //********* ENLAZAMOS SINONIMOS Y ANTÓNIMOS **********
               ArrayList<String> sinonimos=extractSinAnt(lsinonimos); //Extraemos la lista de sinónimos sanos
               ArrayList<String> antonimos=extractSinAnt(lantonimos); //Extraemos la lista de antónimos sanos
               enlazarSinonimosAntonimos(sinonimos,antonimos); //Los enlazamos
           } //Not empty linea

       }//while
       FileUtil.closeFile(is); //Cerramos el fichero
    }//Carga sinónimos y antónimos


    /**
     * Carga todos los nombres y apellidos del Español (empezarán con la primera letra en mayúscula).
     * AVISO: Hay nombres que coinciden con palabras. "Dolores", "dolores".
     */
    private static void cargaNamesSurnames(){

       InputStream is=FileUtil.openFile(SpanishDictionary.PATH_NAMES_SURNAMES);
       Scanner fich=new Scanner(is);
       while (fich.hasNextLine()){
           //Nombre o apellido
           String linea=fich.nextLine().trim(); //Leemos una línea del fichero con una palabra

           DWord palabra=new DWord(linea,linea); //Creamos una palabra
           estableceGenero(palabra); //Establecemos su género de acuerdo a la gramática española.
           palabra.setNumeroSingular(); //En principio todas las palabras que se cargan son de numero singular.
           palabra.addPOS(DWord.POS_PROPN); //Es un nombre
           getDictionary().addNameSurname(palabra); //La añadimos la palabra singular al dictionary.

       }//while
       FileUtil.closeFile(is); //Cerramos el fichero
    }//Carga palabras


    /**
     * Carga todos los nombres de localizaciones (empezarán con la primera letra en mayúscula).
     * AVISO: Hay nombres que coinciden con palabras o nombres. "Sacramento" (ciudad), "sacramento" (palabra).
     */
    private static void cargaLocalizaciones(){

       InputStream is=FileUtil.openFile(SpanishDictionary.PATH_LOCALIZACIONES);
       Scanner fich=new Scanner(is);
       while (fich.hasNextLine()){
           //Localizacion
           String linea=fich.nextLine().trim(); //Leemos una línea del fichero con una palabra

           DWord palabra=new DWord(linea,linea); //Creamos una palabra
           estableceGenero(palabra); //Establecemos su género de acuerdo a la gramática española. (Aunque una ciudad es raro que reseñe género)
           palabra.setNumeroSingular(); //En principio todas las palabras que se cargan son de numero singular.
           palabra.addPOS(DWord.POS_PROPNL); //Es un nombre de lugar
           getDictionary().addLocalizacion(palabra); //La añadimos al dictionary.

       }//while
       FileUtil.closeFile(is); //Cerramos el fichero
    }//Carga palabras


    /**
     * Todos los participios de los verbos pueden actuar también como adjetivos, así que lo indicamos así en el diccionario.
     * Cargamos cada participio en el diccionario, junto a sus flexiones de género y número: "cortado", "cortados", "cortada", "cortadas".
     * Quedan marcados como adjetivos y enlazados entre ellos.
     */
    private static void marcaParticipiosComoAdjetivos(){
        for (DVerb v:dictionary.getAllVerbos()){
            if (v.getTiempo().equals(DVerb.TIEMPO_PARTICIPIO)){ //Si es la conjugación perteneciente al participio
                cargaParticipioComoAdjetivo(v.getText()); //Lo cargamos en el diccionario, junto a sus flexiones de género y número: "cortado", "cortados", "cortada", "cortadas"
            }
        }//for
    }

    /**
     * Permite marcar las palabras del diccionario que son adjetivos a partir del contenido de un fichero de "adjetivos.txt".
     * Este archivo contiene singulares, plurales, derivados, etc. P.e. abierto, abierta, abiertos, abiertas, etc.
     */
    private static void marcaAdjetivos(){
       //Hay sinónimos que realmente son taxonomías. P.e. ave, pájaro
       //Hay sinónimos que realmente son acepciones (polisemia): ayuntamiento (Concejo), coito (cópula); o calavera, libertino
       InputStream is=FileUtil.openFile(SpanishDictionary.PATH_ADJETIVOS);
       Scanner fich=new Scanner(is);
       while (fich.hasNextLine()){
           //Palabra singular
           String linea=fich.nextLine().trim(); //Leemos una línea del fichero con un sinónimo
           String word[]=linea.split(" "); //rompemos la linea en dos: adjetivo + metadata
           String adjetivo=StringUtil.trim(word[0]); //adjetivo
           if (!StringUtil.isEmpty(adjetivo)){
               DWord w=dictionary.getWordOnly(adjetivo); //Buscamos el adjetivo en el diccionario
               if (w!=null){ //si está presente lo marcamos. Los adjetivos que no están en el diccionario, los ignoramos.
                   w.addPOS(DWord.POS_ADJ);
               }
           } //Not empty adjetivo

       }//while
       FileUtil.closeFile(is); //Cerramos el fichero
    }//Carga sinónimos y antónimos


    /**
     * Inicializa todo el diccioonario, cargando todo tipo de palabras.
     */
    public static void initializeDictionary(){
        LogUtil.logTraza("[INICIALIZACION FASE_1] Carga de verbos irregulares.",5);
        cargaVerbosIrregulares(); //Cargamos todos los verbos irregulares y sus conjugaciones en el dictionary
        LogUtil.logTraza("[INICIALIZACION FASE_1] Carga de verbos regulares.",5);
        cargaVerbosRegulares(); //Cargamos todos los verbos regulares y sus conjugaciones en el dictionary
        LogUtil.logTraza("[INICIALIZACION FASE_1] Carga de palabras comunes.",5);
        cargaPalabras2(); //Cargamos todas las palabras (singulares, masculino y femenino) en el dictionary
        LogUtil.logTraza("[INICIALIZACION FASE_1] Marcando participios como adjetivos.",5);
        marcaParticipiosComoAdjetivos(); //Marcamos los participios como adjetivos
        LogUtil.logTraza("[INICIALIZACION FASE_1] Marcando adjetivos generales.",5);
        marcaAdjetivos(); //Marcamos más adjetivos en base al fichero de adjetivos
        LogUtil.logTraza("[INICIALIZACION FASE_1] Carga de sinónimos y antónimos.",5);
        cargaSinonimosAntonimos(); //Cargamos los sinónimos y antónimos de las palabras
        LogUtil.logTraza("[INICIALIZACION FASE_1] Carga de nombres y apellidos.",5);
        cargaNamesSurnames(); //Cargamos todos los nombres y apellidos del español
        LogUtil.logTraza("[INICIALIZACION FASE_1] Carga de localizaciones.",5);
        cargaLocalizaciones(); //Cargamos las localizaciones más comunes
        LogUtil.logTraza("[INICIALIZACION FASE_1] Verbos procesados: "+getDictionary().getVerbsSize(),5);
        LogUtil.logTraza("[INICIALIZACION FASE_1] Palabras procesadas: "+getDictionary().getWordsSize(),5);
        LogUtil.logTraza("[INICIALIZACION FASE_1] Nombres procesados: "+getDictionary().getNameSurnameSize(),5);
        LogUtil.logTraza("[INICIALIZACION FASE_1] Localizaciones procesadas: "+getDictionary().getLocalizacionesSize(),5);
        LogUtil.logTraza("[INICIALIZACION FASE_1] Total procesado: "+getDictionary().getTotalSize(),5);
    }

    /**
     * @return Diccionario del español con todas sus palabras (verbos, sustantivos, adjetivos, etc)
     */
    public static Dictionary getDictionary() {
        return dictionary;
    }

    /**
     * @param aDictionary the dictionary to set
     */
    public static void setDictionary(Dictionary aDictionary) {
        dictionary = aDictionary;
    }


    /**
     * Indentifica palabras que señalan agregación, desde el lado del elemento agregado. Los sinónimos de cualquiera de estas palabras son muy amplios,
     * e incluso metafóricos. Por ello conviene concretar, al igual en el caso de la herencia, las palabras precisas
     * para identificar la presencia del concepto de agregación.
     * @param text Palabra a analizar.
     * @return True si señala agregación desde el punto de vista del agregado, false en caso contrario.
     */
    public static boolean isAgregationWord(String text){
       boolean result=false;

           if (text.equals("componente")||text.equals("integrante")||text.equals("elemento")||text.equals("parte")||
                text.equals("pieza")||text.equals("partícula")||text.equals("factor")||text.equals("principio")||
                text.equals("material")||text.equals("fragmento")||text.equals("miembro")||text.equals("sección")||
                text.equals("porción")||text.equals("cacho")||text.equals("extensión")||text.equals("extremidad")||
                text.equals("apéndice")||text.equals("añadido")||text.equals("subconjunto")||text.equals("subparte")||
                text.equals("subelemento")){
               result=true;
           }
       return result;
    }


    /**
     * Palabras que acompañan a una palabra que indica el posicionamiento de un objeto.
     * @param text Palabra que se desea saber si es un flag señalizador de una palabra de posicionamiento (encima, debajo, etc).
     * @return True si la palabra es indicador de posicionamiento.
     */
    public static boolean isPositionFlag(String text){
       boolean result=false;
       text=text.toLowerCase(); //Comparamos en minúsculas

           if (text.equals("encima")||text.equals("debajo")||text.equals("entre")||text.equals("dentro")||
                text.equals("fuera")||text.equals("cerca")||text.equals("lejos")||text.equals("delante")||
                text.equals("detrás")||text.equals("sobre")||text.equals("en")||text.equals("lado")||
                text.equals("derecha")||text.equals("izquierda")){
               result=true;
           }
       return result;
    }


    /**
     * Palabras que son indicio de que la oración es una pregunta.
     * @param text Palabra a analizar
     * @return  True si la palabra indica pregunta, False en caso contrario.
     */
    public static boolean isQuestionFlag(String text){
       boolean result=false;
       text=text.toLowerCase(); //Comparamos en minúsculas

           if (text.equals("¿")||text.equals("?")||
                text.equals("quién")||text.equals("quien")||text.equals("quienes")||text.equals("quiénes")||
                text.equals("cual")||text.equals("cuál")||text.equals("cuales")||text.equals("cuáles")||
                text.equals("cuanto")||text.equals("cuánto")||text.equals("cuantos")||text.equals("cuántos")||
                text.equals("cuanta")||text.equals("cuánta")||text.equals("cuantas")||text.equals("cuántas")||
                text.equals("cuan")||text.equals("cuán")||
                text.equals("cuando")||text.equals("cuándo")||text.equals("donde")||text.equals("dónde")||
                text.equals("que")||text.equals("qué")||
                text.equals("donde")||text.equals("dónde")||
                text.equals("como")||text.equals("cómo")){
               result=true;
           }
       return result;
    }


    /**
     * Indentifica palabras que señalan agregación, desde el lado del agregador. Los sinónimos de cualquiera de estas palabras son muy amplios,
     * e incluso metafóricos. Por ello conviene concretar, al igual en el caso de la herencia, las palabras precisas
     * para identificar la presencia del concepto de agregación.
     * @param text Palabra a analizar.
     * @return True si señala agregación desde el punto de vista del agregador, false en caso contrario.
     */
    public static boolean isAgregatorWord(String text){
       boolean result=false;

           if (text.equals("constituido")||text.equals("constituida")||text.equals("formado")||text.equals("formada")||text.equals("conformado")||text.equals("conformada")||text.equals("unión")||
                text.equals("compuesto")||text.equals("compuesta")||text.equals("composición")||text.equals("mezcla")||text.equals("amalgama")||
                text.equals("conjunto")||text.equals("conjunción")||text.equals("agregación")||text.equals("agregado")){
               result=true;
           }
       return result;
    }




    /**
     * Corrige la confusión de tiempos verbales de SER e IR: Pedro fue(SER) sargento; Pedro fue(IR) a la panadería.
     * Ambos verbos son homógrafos en el: pretérito indefinido, futuro del subjuntivo, y en las dos formas del pretérito imperfecto del subjuntivo
     * @param oracion Oración a desambigüar.
     * @return Conjugación desambiguada (perteneciente a "ser" o "ir"). Null si no hay presencia de ninguno de los dos verbos.
     */
    private DVerb desambiguaSerIR(LOracion oracion){
        DVerb result=null;
        LTermino verbT=oracion.getAccion();
        //Buscaremos el patrón "IR A". Analizaremos los hijos del Raiz (por ser copulativa).
        boolean verboAux=false;
        boolean a=false;
        for (LTermino term:oracion.getEstructura().getHijos()){
            if (term.getPos().equals(DWord.POS_AUX)){
                verboAux=true;
            }else if (term.getTexto().equals("a")){ //Detectamos el enlace "a"
                a=true;
            }
        }//for
        boolean esIr=verboAux&&a; //Ya sabemos si es "ir" o "ser". Ahora hay que buscar el verbo correcto.

        ArrayList<DVerb> conjugaciones=dictionary.getVerb(verbT.getTexto()); //Obtenemos las conjugaciones coincidentes
        for (DVerb v:conjugaciones){
            String infinitivo=v.getVerboInfinitivo().getText();
            if (esIr&&infinitivo.equals("ir")){
                result=v;
            }
        }//for
        return result;
    }



}//class
