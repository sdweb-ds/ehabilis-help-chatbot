package es.sdweb.application.componentes.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * @author Antonio Carro Mariño
 *
 *         Clase que implementa funciones de utilidad sobre cadenas de texto.
 */
public class StringUtil {
        
        /**
         * Pasa a cadena el array de Strings que se recibe.
         * @param array Lista de palabras a pasar a String.
         * @param comillas Si es true, las palabras se entrecomillan. P.e. "perro", "gato" y "arbol"
         * @return Cadena con todas las palabras del array.
         */
        public static String toString(ArrayList<String> array, boolean comillas){
            String result="";
            String com=(comillas?"\"":"");
            int i=0;
            for (String word:array){
                i++;
                result+=com+word+com+", ";
            }//for
            if (i>0){
                result=result.substring(0,result.length()-2);
            }
            return result;
        }
    
        /**
         * Crea una cadena de espacios tan larga como se indique en "longitud".
         * @param longitud Número de espacios que contendrá la cadena.
         * @return Cadena con los espacios indicados.
         */
        public static String padding(int longitud){
            String result="";
            for (int i=0;i<longitud;i++){
                result+=" "; //Sumamos un espacio
            }
            return result;
        }

	public static String minusculas(String cadena) {
		String result = (cadena != null ? cadena.toLowerCase() : "");
		return result;
	}

	public static String mayusculas(String cadena) {
		String result = (cadena != null ? cadena.toUpperCase() : "");
		return result;
	}

	public static String mayusculas(String cadena, String locale) {
		String result = (cadena != null ? cadena.toUpperCase(new Locale(locale)) : "");
		return result;
	}

        /**
         * Elimina el signo de puntuación final de la cadena (si existe), ya sea un punto, una coma o un punto y coma.
         * Y pasa la cadena a minúscula, y añade un espacio en blanco al final de la misma.
         * @param cadena Cadena a procesar.
         * @return Devuelve la cadena en minúscula, sin puntuación final, y con un espacio final.
         */
        public static String eliminaPuncFinalSpaceLower(String cadena){
            String result="";
            if (!StringUtil.isEmpty(cadena)){
                if (cadena.endsWith(".")||cadena.endsWith(",")||cadena.endsWith(";")){ //Si termina en un signo de puntuación
                    cadena=cadena.substring(0, cadena.length()-1); //lo recortamos
                }
                cadena=cadena+" ";//Por ultimo añadimos un espacio en blanco
                result=cadena.toLowerCase(); //y la pasamos a minúsculas
            }
            return result;
        }
        
        
        /**
         * Devuelve el texto "no " si el valor es false. Cadena vacía si es true.
         * @param valor Valor booleano
         * @return "no " si el valor es false. Cadena vacía si es true.
         */
        public static String negado(boolean valor){
            return (valor?"":"no ");
        }
        
        /**
         * Pone la primera letra de la cadena en mayúscula; las restantes letras puede ponerlas todas en minúsculas o respetar su situación actual, en función del parámetro restLowerCase.
         * @param cadena Cadena de texto de entrada.
         * @param restLowerCase Si recibe True pone en minúsculas toda la cadena menos la primera letra. Si recibe False, deja los caracteres en su situación origina.
         * @return Cadena de entrada con la primera letra en mayúscula.
         */
	public static String primeraMay(String cadena, boolean restLowerCase) {
            String resultado = cadena;
            if (!StringUtil.isEmpty(cadena)) {
                if (restLowerCase){
                    resultado = resultado.toLowerCase();
                } //If queremos el resto en minusculas
                char letra = cadena.charAt(0);
                resultado = resultado.substring(1);
                resultado = cat(new Character(letra).toString().toUpperCase(), resultado);
	    } else {
		resultado=""; //Si la cadena está vacía, devolvemos cadena vacía
	    }
            return resultado;

	}

	public static String todasMay(String cadena) {

		String resultado = "";
		List<String> cadenas = new ArrayList<String>();
		char[] charCadena = cadena.toCharArray();

		if (cadena != null && cadena != "") {
			int o = 0;
			for (int i = 0; i < cadena.length(); i++) {
				if (charCadena[i] == ' ') {
					cadenas.add(cadena.substring(o, i));
					o = i + 1;
				}
			}
			cadenas.add(cadena.substring(o, cadena.length()));

			Iterator<String> iterator = cadenas.iterator();
			resultado = primeraMay(iterator.next(),true);
			while (iterator.hasNext()) {
				resultado = resultado + " " + primeraMay(iterator.next(),true);
			}
			return resultado;
		} else {
			return "";
		}
	}

        /**
         * Reemplaza en la cadena especificada el oldChar por el newChar.
         * @param cadena Cadena sobre la que se realiza el reemplazo.
         * @param oldChar Caracter a sustituir.
         * @param newChar Nuevo caracter.
         * @return Cadena con el viejo caracter reemplazado.
         */
	public static String Rep(String cadena, char oldChar, char newChar) {
		String result = "";
		if (cadena != null) {
			result = cadena.replace(oldChar, newChar);
		}

		return result;
	}

	public static String fillWith(String str1, String relleno, int longitud) {
		String result = "";
		result = (isEmpty(str1) ? "" : str1);
		for (int i = str1.length(); i < longitud; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append(relleno);
			sb.append(result);
			result = sb.toString();
		}
		;
		return result;
	}

	public static String fillWithZero(String str1, int longitud) {
		return fillWith(str1, "0", longitud);
	}

	public static String cat(String... strs) {
		StringBuffer sb = new StringBuffer();

		for (String str : strs) {
			sb.append(str);
		}

		return sb.toString();
	}
        
        
        /**
         * Obtiene la primera palabra de la cadena, eliminando espacios superfluos y retornos de línea y tabuladores, y considerando signos de puntuación.
         * Por ejemplo si la cadena es "Jorge, plasta." devuelve "Jorge".
         * @param cadena Cadena de la que se quiere extraer la primera palabra.
         * @return Primera palabra de la cadena.
         */
        public static String getFirstWord(String cadena){
            String result=cadena;
            cadena=StringUtil.trim(cadena); //Limpiamos espacios laterales y superfluos, tabuladores y retornos de línea
            ArrayList<String> words=new ArrayList();
            int i=0;
            if (!StringUtil.isEmpty(cadena)){ //Si la cadena tiene algo, iremos comprobando con cada signo de puntuacion (si existe)
               i=cadena.indexOf(" ");
               if (i>0) words.add(cadena.substring(0,i));
               i=cadena.indexOf(".");
               if (i>0) words.add(cadena.substring(0,i));
               i=cadena.indexOf(",");
               if (i>0) words.add(cadena.substring(0,i));
               i=cadena.indexOf("?");
               if (i>0) words.add(cadena.substring(0,i));
               i=cadena.indexOf("!");
               if (i>0) words.add(cadena.substring(0,i));
               words.add(cadena); //por último añadimos la cadena completa
               
               for (String w:words){
                   if(w.length()<result.length()){
                       result=w; //Si hemos encontrado una palabra más corta, nos quedamos con ella
                   }
               }//for
            }//if
            return result;
        }
        
        
        /**
         * Elimina espacios laterales de la cadena, y sustituye tabuladores y retornos por espacios.
         * También elimina todos los espacios interiores de la cadena superfluos (es decir, nunca deja dos espacios seguidos).
         * @param cadena Cadena a convertir
         * @return Cadena tratada
         */
	public static String trim(String cadena) {
            String result = null;
            if (cadena != null) {
                    result = cadena.trim();
                    result = result.replace("\t", " "); //Sustituimos tabuladores por espacios
                    result = result.replace("\n", " "); //Sustituimos retornos por espacios
                    result=result.replaceAll("\\s{2,}", " "); //Eliminamos espacios superfluos en el interior de la cadena
            }
            return result;
	}
        
        /**
         * Limpia la cadena de todo caracter extraño que no esté recogido en cualquier palabra presente en un diccionario.
         * @param cadena Cadena a normalizar.
         * @return Cadena normalizada.
         */
	public static String trimNormalizado(String cadena) {
            String result = null;
            if (cadena != null) {
                    result = cadena.trim();
                    result = result.replaceAll("[^a-z^A-Z^0-9^_^-^\\s^á^Á^é^É^í^Í^ó^Ó^ú^Ú^ñ^Ñ^ü^Ü]*", ""); //Eliminamos cualquier cosa que no sea una letra, un número o los guiones "_" y "-"
            }
            return result;
	}
        
        /**
         * Indica si la cadena es un nombre. Para ello se comprueba únicamente si empieza con una letra mayúscula, acentuada o sin acentuar.
         * @param cadena Cadena a analizar
         * @return True si emplieza por una letra mayúscula acentuada o sin acentuar.
         */
	public static boolean isName(String cadena) {
            boolean result = false;
            if (!isEmpty(cadena)) {
                   String aux = cadena.trim().substring(0, 1);
                   aux = aux.replaceAll("[^A-Z^Á^É^Í^Ó^Ú^Ñ]*", ""); //Eliminamos cualquier cosa que no sea una letra mayúscula
                   result=!isEmpty(aux); //Si quedó una letra es que es mayúscula
            }
            return result;
	}
        
        /**
         * Limpia la cadena de todo caracter extraño que no esté recogido en cualquier palabra presente en un diccionario.
         * @param cadena Cadena a normalizar.
         * @param eliminaEspacios Indica si se deben eliminar o no los espacios.
         * @return Cadena normalizada.
         */
	public static String trimNormalizado(String cadena,boolean eliminaEspacios) {
            String result = null;
            if (cadena != null) {
                    result = cadena.trim();
                    String regexp=(eliminaEspacios?"[^a-z^A-Z^0-9^_^-^á^Á^é^É^í^Í^ó^Ó^ú^Ú^ñ^Ñ^ü^Ü]*":"[^a-z^A-Z^0-9^_^-^á^Á^é^É^í^Í^ó^Ó^ú^Ú^^ñ^Ñ^ü^Ü\\s]*");
                    result = result.replaceAll(regexp, ""); //Eliminamos cualquier cosa que no sea una letra, un número o los guiones "_" y "-"
            }
            return result;
	}
        
        /**
         * Replaces the first subsequence of the <tt>source</tt> string that matches
         * the literal target string with the specified literal replacement string.
         * 
         * @param source source string on which the replacement is made
         * @param target the string to be replaced
         * @param replacement the replacement string
         * @return the Resulting string.
         */
        public static String replaceFirst(String source, String target, String replacement) {
            int index = source.indexOf(target);
            if (index == -1) {
                return source; //Si la cadena no existe, no hay nada que reemplazar y devolvemos la cadena original
            }
            String result=source.substring(0, index).concat(replacement).concat(source.substring(index+target.length()));

            return result;
        }    
        
        /**
         * Reemplaza todas las ocurrencias en la cadena "source", de la cadena "target" con la cadena "replacement".
         * 
         * @param source source string on which the replacement is made
         * @param target the string to be replaced
         * @param replacement the replacement string
         * @return the Resulting string.
         */
        public static String replaceAll(String source, String target, String replacement) {
            String result=source;
            while (result.contains(target)){
                result=replaceFirst(source,target,replacement);
            }//mientras haya ocurrencias

            return result;
        }    
        
        
        /**
         * Limpia la cadena de todo caracter extraño que no esté recogido en cualquier palabra presente en un diccionario.
         * @param cadena Cadena a normalizar.
         * @param eliminaEspaciosComas Indica si se deben eliminar o no los espacios y las comas.
         * @param eliminaNumerosGuiones Indica si se deben eliminar o no los numeros y guiones "_" y "-".
         * @return Cadena normalizada.
         */
	public static String trimNormalizado(String cadena,boolean eliminaEspaciosComas,boolean eliminaNumerosGuiones) {
            String result = null;
            if (cadena != null) {
                    result = cadena.trim();
                    String filtro=(eliminaEspaciosComas?"":"^,\\s"); //Primer filtro
                    filtro+=(eliminaNumerosGuiones?"":"^0-9^_^-"); //Ampliamos el filtro
                    String exp="[^a-z^A-Z^á^Á^é^É^í^Í^ó^Ó^ú^Ú^ñ^Ñ^ü^Ü"+filtro+"]*";
                    result = result.replaceAll(exp, ""); //Aplicamos el filtro
            }
            return result;
	}
        
        /**
         * Indica si una letra es vocal (acentuada o no).
         * @param letra Letra de la que se desea averiguar si es vocal.
         * @return True si es una vocal.
         */
        public static boolean isVocal(String letra){
            boolean result=false;
            if (!isEmpty(letra)){
                letra=letra.substring(0, 1).toLowerCase(); //Cogemos la primera letra y la pasamos a minuscula
                result=(letra.equals("a")||letra.equals("e")||letra.equals("i")||letra.equals("o")||letra.equals("u")||
                        letra.equals("á")||letra.equals("é")||letra.equals("í")||letra.equals("ó")||letra.equals("ú"));
            }
            return result;
        }

        /**
         * Indica si una palabra tiene alguna vocal acentuada.
         * @param palabra Palabra a analizar.
         * @return True si la palabra tiene alguna vocal acentuada.
         */
        public static boolean isAcentuada(String palabra){
            boolean result=false;
            for (int i=0;i<palabra.length();i++){
                String letra=palabra.substring(i,i+1).toLowerCase(); //Cogemos una letra
                if (letra.equals("á")||letra.equals("é")||letra.equals("í")||letra.equals("ó")||letra.equals("ú")){
                   result=true; //Si está acentuada
                }                
            }//for
            return result;
        }

        /**
         * Devuelve la terminacion de la palabra, que tendrá el número de letras que se indica. Si la palabra es más corta
         * que el número de letras indicado, devolverá la totalidad de la palabra.
         * @param palabra Palabra a recortar
         * @param numeroLetras Tamaño de la terminación deseada
         * @return Terminación de la palabra
         */
        public static String terminacion(String palabra, int numeroLetras){
            String result=palabra;
            if (!isEmpty(palabra)){
                if (palabra.length()>=numeroLetras){
                    result=palabra.substring(palabra.length()-numeroLetras, palabra.length()); //Devolvemos el recorte
                }
            }
            return result;
        }
        
        /**
         * Elimina los acentos del texto que se recibe como parámetro.
         * @param texto Texto del que se desea sustituir las vocales acentuadas por vocales no acentuadas
         * @return Texto original pero sin acentos. No se alteran las mayusculas o minusculas.
         */
        public static String eliminarAcentos(String texto){
            texto=texto.replace('Á', 'A');
            texto=texto.replace('á', 'a');
            texto=texto.replace('É', 'E');
            texto=texto.replace('é', 'e');
            texto=texto.replace('Í', 'I');
            texto=texto.replace('í', 'i');
            texto=texto.replace('Ó', 'o');
            texto=texto.replace('ó', 'o');
            texto=texto.replace('Ú', 'U');
            texto=texto.replace('ú', 'u');
            return texto;
        }
        

	public static boolean isEmpty(String str) {
            boolean result=((str == null) || (str.trim().equals(""))|| (str.trim().equals("\n"))  || (str.trim().toLowerCase().equals("null")));
	    return result; 
	}

	public static String arrayToString(byte[] b) {
		String result = "{";
		for (int i = 0; i < b.length; i++) {
			result += "" + b[i] + (i < b.length - 1 ? "," : "");
		}

		return result + "}";
	}

	/**
	 * Devuelve true si la cadena es alguno de los valores: S,SI,YES,Y,YE.
	 * Devuelve false en caso de que la cadena sea null u otro valor.
	 *
	 * @param cadena
	 *            cadena de texto
	 * @return indica si la cadena indica SI
	 */
	public static boolean isYes(String cadena) {
		boolean result = false;
		cadena = cadena.toUpperCase();
		if (cadena != null) {
			result = (cadena.toLowerCase().equals("true") || cadena.equals("S") || cadena.equals("SI") || cadena.equals("Y") || cadena.equals("YES") || cadena.equals("YE"));
		}

		return result;
	}

	/**
	 * Se utiliza para la clase PropertyTag, etiqueta property, para poner la
	 * fecha en los pdf's generados.
	 */

	public static Object string2Object(String pValue, String pType) throws Exception {
		return string2Object(pValue, pType, null);
	}

	public static Object string2Object(String pValue, String pType, String pFormat) {
		if (pValue == null || pValue.length() == 0 || pType == null)
			return pValue;

		if (pType.equals(java.lang.String.class.getName())) {
			return pValue;
		} else if (pType.equals(java.lang.Integer.class.getName())) {
			return new Integer(pValue);
		} else if (pType.equals(java.math.BigDecimal.class.getName())) {
			return new java.math.BigDecimal(pValue);
		} else if (pType.equals(java.lang.Boolean.class.getName())) {
			return new Boolean(pValue);
		} else
			return null;
	}

	public static String objectToString(Object obj) {
		if(obj != null) {
			return obj.toString();
		}
		return null;
	}

	public static String toString(Object obj) {
		return objectToString(obj);
	}

	public static Object object2String(Object pValue, String pFormat) {
		if (pValue == null || pFormat == null)
			return pValue;
		if (pValue instanceof java.util.Date) {
			java.text.DateFormat df = new java.text.SimpleDateFormat(pFormat);
			return df.format(pValue);
		} else if (pValue instanceof Number) {
			java.text.DecimalFormat df = new java.text.DecimalFormat(pFormat);
			return df.format(pValue);
		}
		return pValue;
	}

	public static String eliminarSaltosLinea(String text) {
		String result = "";
		if (text != null) {
			text = text.replaceAll("\r", "");
			result = text.replaceAll("\n", "");
		}
		return result;
	}

        /**
         * Elimina puntos, comas y guiones bajos, y los sustituye por espacios en blanco.
         * @param text Texto de entrada
         * @return Texto sin puntos, comas y guiones bajos.
         */
	public static String eliminarPuntosYComas(String text) {
		String result = text;
		if (text != null) {
			result = result.replace('.', ' '); 
			result = result.replaceAll("_", " ");
			result = result.replaceAll(",", " ");
		}
		return result;
	}
	
	/**
	 * Recorre un String reemplazando las comillas simples por dos comillas simples
	 * 
	 * @param value
	 * @return El String con dobles comillas simples
	 */
	public static String pon2Comillas(String value) {
		String result = "";
		
		for(char c: value.toCharArray()) {
			result += (c == '\'' ? "''" : String.valueOf(c));
		}
		
		return result;
	}

    /**
     * Esta función resalta en amarillo todas las ocurrencias de la cadenaBuscada que se localicen
     * en un texto codificado en HTML.
     * @param textoHTML Texto codificado en HTML donde se desea resaltar todas las ocurrencias de la cadenaBuscada.
     * @param cadenaBuscada Texto a buscar dentro del textoHTML.
     * @return Texto en HTML modificado con los resaltes. Este texto parte del textoHTML que se recibe como parámetro.
     */
    public static String resalta(String textoHTML, String cadenaBuscada){
        String result="";
        boolean insideHtmlTags=false;
        int i=0;
        while (i<textoHTML.length()){
          char c=textoHTML.charAt(i);
          if (insideHtmlTags){ //Si estamos procesando codigo html copiamos tal cual
            if (c=='>'){  //Si llegamos a un finalde corchete hemos salido de un tag html
               insideHtmlTags=false;
            }
            result=StringUtil.cat(result, ""+c); //En cualquier caso todo caracter de html lo copiamos
          }else{ //Si estamos en texto puro y duro
            if (c=='<'){  //Si encontramos un inicio de corchete significa que estamos entrando en un tag html
               insideHtmlTags=true;
               result=StringUtil.cat(result, ""+c); //copiamos la letra
            }else{ //Si estamos buscando en el texto fuera de tags html
               int longFragmento=( textoHTML.length()< (i+cadenaBuscada.length())?textoHTML.length():(i+cadenaBuscada.length()));
               String aux=textoHTML.substring(i, longFragmento); //vemos la palabra donde estamos
               if (cadenaBuscada.toUpperCase().equals(aux.toUpperCase())){ //si coincide con la buscada
                  //result=StringUtil.cat(result, "<span class='rotulado'>"+aux+"</span>"); //hacemos el replace y seguimos 
                  result=StringUtil.cat(result, "<span style='background: yellow;'>"+aux+"</span>"); //hacemos el replace y seguimos 
                  i=i+cadenaBuscada.length()-1; //saltamos la palabra, ya que ha sido procesada (restamos 1 porque al final del bucle siempre se suma 1)
               }else{ //si la palabra no coincide con la buscada
                  result=StringUtil.cat(result, ""+c); //copiamos la letra
               }
            }  
          }
          i++;  //sumamos 1
        }//while
        return result;
    }
    
    /**
     * Llama al método toString() tomando la precaucion de si el objeto es nulo. En ese caso devuelve cadena vacía.
     * @param obj Objeto a pasar a cadena.
     * @return Cadena vacía o cadena resultado de obj.toString()
     */
    public static String printObject(Object obj){
        String result=(obj!=null?obj.toString():"");
        return result;
    }
    
    /**
     * Esta función extrae el texto plano de un texto HTML que se recibe como parámetro. Para ello elimina todos
     * los tags que se declaran usando corchetes.
     * @param textoHTML Texto HTML del que se desea extraer texto plano.
     * @return Texto plano contenido en el textoHTML que se recibe.
     */
    public static String getTxtFromHTML(String textoHTML){
        String result="";
        boolean insideHtmlTags=false;
        int i=0;
        int longTexto=textoHTML.length();
        while (i<longTexto){
          char c=textoHTML.charAt(i);
          if (insideHtmlTags){ //Si estamos procesando codigo HTML omitimos las letras
            if (c=='>'){  //Si llegamos a un finalde corchete hemos salido de un tag html
               insideHtmlTags=false;
            }
            result=StringUtil.cat(result, ""); //Omitimos la letra ya que estamos en un tag
          }else{ //Si estamos en texto puro y duro
            if (c=='<'){  //Si encontramos un inicio de corchete significa que estamos entrando en un tag html
               insideHtmlTags=true;
               result=StringUtil.cat(result, " "); //No copiamos el corchete, ya que pertenece a un tag HTML, ponemos un espacio en su lugar
            }else{ //Si estamos buscando en el texto fuera de tags HTML
               result=StringUtil.cat(result, ""+c); //copiamos la letra
            }  
          }
          i++;  //sumamos 1
        }//while
        return result;
    }
    
	/**
         * Pasa a mayusculas la cadena de texto.
         * @param text Cadena de texto (puede ser nula).
         * @return Devuelve la cadena en mayúsculas, o null si la cadena era null.
         */
    public static String upper(String text){
        String result=(text!=null?text.toUpperCase():null);
        return result;
    }
    
    
    /**
     * Devuelve un ArrayList de subcadenas que forman parte de la cadena indicada, pero están separadas por el delimitador.
     * Las cadenas se devuelven libres de espacios superfluos.
     * @param cadena Cadena de entrada a trocear.
     * @param delimitador Delimitador que separa las subcadenas (no formará parte de ninguna subcadena).
     * @return Devuelve un ArrayList de subcadenas. Si la cadena de entrada está vacía, el ArrayList se devuelve vacío.
     */
    public static ArrayList<String> tokeniza(String cadena, String delimitador){
        ArrayList<String> result=new ArrayList();
        if (!StringUtil.isEmpty(cadena)){
            StringTokenizer st = new StringTokenizer(cadena, delimitador);
            while (st.hasMoreTokens()) {
              String subcadena=StringUtil.trim(st.nextToken());
              result.add(subcadena);
            } //While hay mas tokens
        }// If cadena no vacía
        return result;
    }
        
        
}// class
