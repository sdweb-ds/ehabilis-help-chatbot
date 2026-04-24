/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sdweb.application.componentes.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antonio Carro Mariño
 */
public abstract class NumberUtil {

	/**
	 * Convierte el Double a un String con dos decimales y con el separador de
	 * decimal que corresponda al Locale ("," en España). Ejemplo: convierte
	 * "123,3891" en "123,39"
	 *
	 * @param d
	 *            Double
	 * @param loc
	 *            Locale del usuario
	 * @return String con la representacion del double.
	 */
	// Esta función se mantiene por compatibilidad con el código anterior
	// Aunque ya no se usa el Locale por que da problemas con el gallego
	public static String formatDouble(double d, Locale loc) {
		return formatDouble(d);
	}

	public static String formatDouble(double d) {
		DecimalFormat df = new DecimalFormat("0.00");

		String result = "";

		DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator(',');

		df.setDecimalFormatSymbols(decimalFormatSymbols);
		df.setGroupingUsed(false);

		result = df.format(d);

		return result.trim();
	}

	/**
	 * Convierte el Double a un String con dos decimales y con el separador de
	 * decimal que corresponda al Locale ("," en España). Ejemplo: convierte
	 * "123,3891" en "123,39"
	 *
	 * @param d
	 *            Double
	 * @param patron
	 *            Patron a aplicar, por ejemplo "#.##" (Patrones de
	 *            DecimalFormat)
	 * @param loc
	 *            Locale del usuario
	 * @return String con la representacion del double.
	 */
	public static String formatDouble(double d, String patron, Locale loc) {
		String result = "";
		NumberFormat nf = NumberFormat.getNumberInstance(loc);
		DecimalFormat df = (DecimalFormat) nf;
		df.applyPattern(patron);
		result = df.format(d);
		return result;
	}

	/**
	 * Convierte un string a un double considerando el Locale
	 *
	 * @param d
	 *            Numero double en formato cadena.
	 * @param loc
	 *            Locale del usuario.
	 * @return Double con el valor.
	 */
	public static double formatToDouble(String d, Locale loc) {
		double result = 0;
		DecimalFormat df = new DecimalFormat("0.00");

		DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator(',');

		df.setDecimalFormatSymbols(decimalFormatSymbols);
		df.setGroupingUsed(false);

		try {
			result = df.parse(d.replace(".", ",")).doubleValue();
		} catch (ParseException ex) {
			Logger.getLogger(NumberUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return result;
	}

	/**
	 * Parsea un string a entero. Si la cadena no es un entero, lo convierte en
	 * cero.
	 *
	 * @param entero
	 *            Entero a parsear.
	 * @return Entero equivalente a la cadena.
	 */
	public static int parseInt(String entero) {
		int result = 0;
		try {
			Integer aux = new Integer(entero);
			result = (aux == null ? 0 : aux.intValue());
		} catch (NumberFormatException e) {
		}
		return result;
	}

	/**
	 * Convierte un numero en formato String en un double, p.e. convierte el
	 * numero 1.512,30 en un double con ese mismo valor.
	 *
	 * @param numeroDecimal
	 *            Numero en formato String.
	 * @return Valor double de ese numero.
	 * @throws NumberFormatException
	 *             En caso de que el numero no sea valido.
	 */
	public static double parseDouble(String numeroDecimal) throws NumberFormatException {
		double result = 0;

		numeroDecimal = numeroDecimal.replaceAll(",", "."); // Sustituimos los puntos por comas

		result = Double.parseDouble(numeroDecimal); // Finalmente parseamos el double

		return result;
	}

	public static double roundDouble(double num, int places) {
		if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(num);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static String formatInteger(int d) {
		DecimalFormat df = new DecimalFormat("00");

		String result = "";

		DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator(',');

		df.setDecimalFormatSymbols(decimalFormatSymbols);
		df.setGroupingUsed(false);

		result = df.format(d);

		return result.trim();
	}
        
        /**
         * Indica si un String es un número.
         * @param word Palabra con el número
         * @return True si es un número, false en caso contrario.
         */
        public static boolean isNumber(String word){
            boolean result=true;
            try{
               Integer.parseInt(word); //Intentamos convertirlo
            }catch (NumberFormatException e){
               result=false; //Si no es posible, es que no es un numero
            }
            return result;
        }

}// Class
