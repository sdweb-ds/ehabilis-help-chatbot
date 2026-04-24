/*
 * Clase general de utilidades
 */
package es.sdweb.application.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
/**
 * @author Antonio Carro Mariño
 *
 */
public abstract class Util {
	
	public static boolean isEmpty(String str){
		return ((str==null)||(str.equals("")));
	}
	
	
	public static String arrayToString(byte[] b){
		String result="{";
		for (int i=0; i<b.length;i++){
			result+=""+b[i]+(i<b.length-1?",":"");
		};
		return result+"}";
	}
	
	
	public static String generateFileName(String extension){
		String result="";
		Calendar calendario=Calendar.getInstance();		
		String yyyy=(""+ calendario.get(Calendar.YEAR));
		String yy=yyyy.substring(2);
		String mm=Util.pon2Digitos(calendario.get(Calendar.MONTH)); //de 00 a 11
		String dd=Util.pon2Digitos(calendario.get(Calendar.DAY_OF_MONTH));
		String hh=Util.pon2Digitos(calendario.get(Calendar.HOUR_OF_DAY)); //de 00 a 24 
		String mi=Util.pon2Digitos(calendario.get(Calendar.MINUTE)); 
		String ss=Util.pon2Digitos(calendario.get(Calendar.SECOND)); 
		String cadenaAleatoria="";
		for (int x=13;x<=20;x++){
			cadenaAleatoria+=""+(char)(65+Math.round(Math.random()*25));
		};
		result= "img_"+yy+mm+dd+hh+mi+ss+cadenaAleatoria+"."+extension;
		return result;
	}
	
	
	public static String ponBarra(String pathDirectorio){
		String result="";
		if (!isEmpty(pathDirectorio)){
		  char lastChar=pathDirectorio.charAt(pathDirectorio.length()-1);
		  String barra=(pathDirectorio.indexOf("\\")>0?"\\":"/");
		  result=((lastChar=='/')||(lastChar=='\\')?pathDirectorio:pathDirectorio+barra);
		};
		return result;
	}

	public static String getExtension(String nombreFichero){
		String result="";
		if ((nombreFichero!=null)&&(!nombreFichero.equals(""))){
			int pos=nombreFichero.lastIndexOf("."); //buscamos la posici�n del �ltimo punto
			result=nombreFichero.substring(pos+1); //nos quedamos con los chars desde pos+1 en adelante
		};
		return result;
	}
	
	
	public static String getNombreFichero(String pathCompleto){
		String result="";
		if ((pathCompleto!=null)&&(!pathCompleto.equals(""))){
			int pos1=pathCompleto.lastIndexOf("/"); //buscamos la posici�n de la �ltima barra
			int pos2=pathCompleto.lastIndexOf("\\"); //buscamos la posici�n de la �ltima barra
			int pos=(pos1>pos2?pos1:pos2); //nos quedamos con la mayor pos (-1 si no se hall� ninguna)			
			result=pathCompleto.substring(pos+1); //nos quedamos con los chars desde pos+1 en adelante
		};
		return result;
	}
	
	
	public static void saveFile(String pathCompleto,byte[] fileBytes){
		try {
			FileOutputStream fos=new FileOutputStream(pathCompleto);
			//ByteArrayOutputStream baos=new ByteArrayOutputStream()
			fos.write(fileBytes);
			fos.close();
		} catch (FileNotFoundException e) {
			Util.logTraza("ERROR: "+e.getMessage(),0);
		} catch (IOException e) {
			Util.logTraza("ERROR: "+e.getMessage(),0);
		};
	}
	
	
	
	public static String pon2Digitos(int entero){
		String result=((entero<=9)?"0"+entero:""+entero);
		return result;
	}
	
	
	public static Object createInstance(String nombreClase){
		Object result=null;
		if (nombreClase == null) {
		   System.err.println(nombreClase +
					 " no especificado.");
		};
		Class clase = null;
		try { // busca la clase
			clase = Class.forName(nombreClase);
		} catch (Exception e) {
		  e.printStackTrace(System.err);
		};
		try { // instancia un objeto de la clase buscada
		  result = clase.newInstance();
		} catch (Exception e) {
		  e.printStackTrace(System.err);
		};
  	
		return result;
	}


	public static String getFecha(){
		Calendar c=Calendar.getInstance();
		Date d=c.getTime();
		
		return pon2Digitos(c.get(Calendar.DAY_OF_MONTH))+"/"+
		pon2Digitos(c.get(Calendar.MONTH))+"/"+c.get(Calendar.YEAR)+"  "+
		pon2Digitos(c.get(Calendar.HOUR))+":"+pon2Digitos(c.get(Calendar.MINUTE))+":"+
		pon2Digitos(c.get(Calendar.SECOND));
		
	}
	
	
	public static String ToString(Object obj){
		return (obj!=null?obj.toString():"null");
	}


	/**
	 * Esta funcion imprime en la salida de error estandar una linea de error con la identacion especificada
	 * por nivel y con el mensaje indicado. Si logTraza es false no se imprimira la linea.
	 * @param logTraza Booleano que indica si se debe imprimir (true) o no (false) el mensaje.
	 * @param mensaje Mensaje a imprimir.
	 * @param nivel Grado de identacion para pretty printing.
	 */
	public static void logTraza(boolean logTraza, String mensaje, int nivel){
		String espacios="";
		nivel++;
		for (int x=1;x<=nivel;x++){
			espacios+="  "; //incremento de 2 espacios
		};
		if (logTraza){
		  System.err.println(getFecha()+""+espacios + mensaje);	
		};
	}


	/**
	 * Esta funcion imprime en la salida de error estandar una linea de error con la identacion especificada
	 * por nivel y con el mensaje indicado. Para que la linea se imprima el parametro "es.sdweb.util.hazLog"
	 * ubicado en el fichero de configuraci�n hgcConfiguration.properties debe valer "S".
	 * @param mensaje
	 * @param nivel
	 */
	public static void logTraza(String mensaje, int nivel){
		logTraza(GestorParametrosConfiguracion.getParametro("es.sdweb.util.hazLog").equals("S"),
			mensaje,nivel);
	}


        /**
         * Genera una tira de 20 caracteres unica en formato "20151022183059XYZKXYZK" (fecha y hora actual + 8 caracteres aleatorios)
         * @return Tira de caracteres unica en formato "20151022183059XYZKXYZK"
         */
	public static String generateId(){
		String result="";
		Calendar calendario=Calendar.getInstance();
		String yyyy=(""+ calendario.get(Calendar.YEAR));
		String yy=yyyy.substring(2);
		String mm=Util.pon2Digitos(calendario.get(Calendar.MONTH)); //de 00 a 11
		String dd=Util.pon2Digitos(calendario.get(Calendar.DAY_OF_MONTH));
		String hh=Util.pon2Digitos(calendario.get(Calendar.HOUR_OF_DAY)); //de 00 a 24
		String mi=Util.pon2Digitos(calendario.get(Calendar.MINUTE));
		String ss=Util.pon2Digitos(calendario.get(Calendar.SECOND));
		String cadenaAleatoria="";
		for (int x=13;x<=20;x++){
			cadenaAleatoria+=""+(char)(65+Math.round(Math.random()*25));
		}
		result= yyyy+mm+dd+hh+mi+ss+cadenaAleatoria;
		return result;
	}        
        
}//class
