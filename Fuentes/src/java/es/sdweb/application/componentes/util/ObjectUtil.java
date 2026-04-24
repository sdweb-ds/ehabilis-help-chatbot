package es.sdweb.application.componentes.util;

import java.util.Calendar;

/**
 * @author Antonio Carro Mariño
 *
 * Utilidades relacionadas con objetos
 */
public abstract class ObjectUtil {

	/**
	 * Crea un objeto de una clase determinada a partir del nombre de clase.
	 * @param nombreClase Nombre de la clase del objeto que se desea crear.
	 * @return Instancia del objeto creado.
	 */
	public static Object createInstance(String nombreClase){
		Object result=null;
		if (nombreClase == null) {
		   System.err.println("ERROR: "+nombreClase + " no especificado.");
		};
		Class clase = null;
		try { // busca la clase
			clase = Class.forName(nombreClase);
		} catch (Exception e) {
			System.err.println("[ERROR] No se ha podido localizar la clase "+nombreClase+": "+e.getMessage());
		} catch (Error e){
			System.err.println("[ERROR] No se ha podido localizar la clase "+nombreClase+": "+e.getMessage());
                }
		try { // instancia un objeto de la clase buscada
		  result = clase.newInstance();
		} catch (Exception e) {
			System.err.println("[ERROR] No se ha podido instanciar la clase "+nombreClase+": "+e.getMessage());
		} catch (Error e){
			System.err.println("[ERROR] No se ha podido instanciar la clase "+nombreClase+": "+e.getMessage());
                }
  	
		return result;
	}
	

	/**
	 * Imprime un objeto en formato string.
	 * @param obj objeto que se desea imprimir
	 * @return string resultado de ejecutar obj.toString()
	 */	
	public static String toString(Object obj){
		String result=(obj!=null?obj.toString():"");
		return result;
	}

	public static String pon2Digitos(int entero){
		String result=((entero<=9)?"0"+entero:""+entero);
		return result;
	}
        
        /**
         * Genera una tira de 22 caracteres unica en formato "20151022183059XYZKXYZK" (fecha, hora y segundos actuales + 6 caracteres aleatorios)
         * @return Tira de caracteres unica en formato "20151022183059XYZKXYZK"
         */
	public static String generateId(){
		String result="";
		Calendar calendario=Calendar.getInstance();
		String yyyy=(""+ calendario.get(Calendar.YEAR));
		String yy=yyyy.substring(2);
		String mm=ObjectUtil.pon2Digitos(calendario.get(Calendar.MONTH)); //de 00 a 11
		String dd=ObjectUtil.pon2Digitos(calendario.get(Calendar.DAY_OF_MONTH));
		String hh=ObjectUtil.pon2Digitos(calendario.get(Calendar.HOUR_OF_DAY)); //de 00 a 24
		String mi=ObjectUtil.pon2Digitos(calendario.get(Calendar.MINUTE));
		String ss=ObjectUtil.pon2Digitos(calendario.get(Calendar.SECOND));
		String cadenaAleatoria="";
		for (int x=0;x<8;x++){
			cadenaAleatoria+=""+(char)(65+Math.round(Math.random()*25));
		}
		result= yyyy+mm+dd+hh+mi+ss+cadenaAleatoria;
		return result;
	}        

}//class
