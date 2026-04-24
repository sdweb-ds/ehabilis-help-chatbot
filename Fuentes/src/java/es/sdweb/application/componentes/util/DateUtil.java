package es.sdweb.application.componentes.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author Antonio Carro Mariño
 *
 *         Utilidades relacionadas con Fechas
 */
public abstract class DateUtil {

	/**
	 * @author Antonio Carro Mariño Recibe como parametro una fecha en el
	 *         formato 22/08/2006 y devuelve la un string con la fecha con el
	 *         siguiente formato: "1 de enero de 2006" Si el parametro viene
	 *         vacio devuelve la cadena vacia. Sino cumple el formato de entrada
	 *         devuelve null.
	 * @param fecha
	 *            Fecha a convertir de formato.
	 * @param properties
	 *            Parametros de configuracion que corresponden al Locale en uso.
	 * @return La fecha en el formato descrito o null (si el formato de la fecha
	 *         no es correcto).
	 */
	public static String fechaEscrita(String fecha, Properties properties) {
		String result = null;

		if ((fecha != null) && (!fecha.equals(""))) {
			if (fecha.length() == 10) {
				try {
					String anho = fecha.substring(6, 10);
					String mes = fecha.substring(3, 5);
					String dia = fecha.substring(0, 2);

					int day = new Integer(dia).intValue();
					int month = new Integer(mes).intValue();
					int year = new Integer(anho).intValue();
					if ((year) < 1900)
						return null;
					if ((month < 1) || (month > 12))
						return null;
					if ((day < 1) || (day > 31))
						return null;

					switch (month) {
					case 1:
						mes = properties.getProperty("DateUtil.mes.enero");
						break;
					case 2:
						mes = properties.getProperty("DateUtil.mes.febrero");
						break;
					case 3:
						mes = properties.getProperty("DateUtil.mes.marzo");
						break;
					case 4:
						mes = properties.getProperty("DateUtil.mes.abril");
						break;
					case 5:
						mes = properties.getProperty("DateUtil.mes.mayo");
						break;
					case 6:
						mes = properties.getProperty("DateUtil.mes.junio");
						break;
					case 7:
						mes = properties.getProperty("DateUtil.mes.julio");
						break;
					case 8:
						mes = properties.getProperty("DateUtil.mes.agosto");
						break;
					case 9:
						mes = properties.getProperty("DateUtil.mes.septiembre");
						break;
					case 10:
						mes = properties.getProperty("DateUtil.mes.octubre");
						break;
					case 11:
						mes = properties.getProperty("DateUtil.mes.noviembre");
						break;
					case 12:
						mes = properties.getProperty("DateUtil.mes.diciembre");
						break;
					}

					result = (new Integer(day)).toString() + " " + properties.getProperty("DateUtil.preposicion.de") + " " + mes + " "
							+ properties.getProperty("DateUtil.preposicion.de") + " " + anho;

				} catch (NumberFormatException e) {
					return null;
				}
			}

		}
		;
		if (fecha.equals("")) {
			result = "";
		}
		return result;
	}

	/**
	 * Recibe como par�metro unha data en formato dd/mm/aaaa e devolve un String
	 * da data escrita traduccida ao castel�n.
	 *
	 * @param fecha
	 * @param properties
	 * @return
	 */
	public static String fechaEscritaCastelan(String fecha, Properties properties) {
		String result = null;

		if ((fecha != null) && (!fecha.equals(""))) {
			if (fecha.length() == 10) {
				try {
					String anho = fecha.substring(6, 10);
					String mes = fecha.substring(3, 5);
					String dia = fecha.substring(0, 2);

					int day = new Integer(dia).intValue();
					int month = new Integer(mes).intValue();
					int year = new Integer(anho).intValue();
					if ((year) < 1900)
						return null;
					if ((month < 1) || (month > 12))
						return null;
					if ((day < 1) || (day > 31))
						return null;

					switch (month) {
					case 1:
						mes = properties.getProperty("DateUtil.mes.enero.castelan");
						break;
					case 2:
						mes = properties.getProperty("DateUtil.mes.febrero.castelan");
						break;
					case 3:
						mes = properties.getProperty("DateUtil.mes.marzo");
						break;
					case 4:
						mes = properties.getProperty("DateUtil.mes.abril");
						break;
					case 5:
						mes = properties.getProperty("DateUtil.mes.mayo.castelan");
						break;
					case 6:
						mes = properties.getProperty("DateUtil.mes.junio.castelan");
						break;
					case 7:
						mes = properties.getProperty("DateUtil.mes.julio.castelan");
						break;
					case 8:
						mes = properties.getProperty("DateUtil.mes.agosto");
						break;
					case 9:
						mes = properties.getProperty("DateUtil.mes.septiembre.castelan");
						break;
					case 10:
						mes = properties.getProperty("DateUtil.mes.octubre.castelan");
						break;
					case 11:
						mes = properties.getProperty("DateUtil.mes.noviembre.castelan");
						break;
					case 12:
						mes = properties.getProperty("DateUtil.mes.diciembre.castelan");
						break;
					}

					result = (new Integer(day)).toString() + " " + properties.getProperty("DateUtil.preposicion.de") + " " + mes + " "
							+ properties.getProperty("DateUtil.preposicion.de") + " " + anho;

				} catch (NumberFormatException e) {
					return null;
				}
			}

		}
		;
		if (fecha.equals("")) {
			result = "";
		}
		return result;
	}

	/**
	 * Obtiene la fecha actual como objeto Date
	 *
	 * @return Fecha actual como objeto Date
	 */
	public static java.util.Date getTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	public static java.util.Date getTime(int seconds) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + seconds);
		return cal.getTime();
	}

	/**
	 * Devuelve un objeto Date con la fecha de hace numDays dias.
	 *
	 * @param locale
	 *            Locale a considerar para gestionar el calculo de fechas.
	 * @param fechaReferencia
	 *            Fecha que se toma de referencia a partir de la cual se
	 *            restaran los dias.
	 * @param numDays
	 *            Numero de dias a restar a la fecha actual.
	 * @return Objeto Date con la fecha de hace numDays dias.
	 */
	public static java.util.Date getDateBefore(Locale locale, Date fechaReferencia, int numDays, int numHours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fechaReferencia);
		cal.add(Calendar.HOUR_OF_DAY, -numHours); // Volvemos atras en la fecha
		return cal.getTime();
	}

	/**
	 * Devuelve un objeto Date con la fecha dentro de numDays dias.
	 *
	 * @param locale
	 *            Locale a considerar para gestionar el calculo de fechas.
	 * @param fechaReferencia
	 *            Fecha que se toma de referencia a partir de la cual se sumaran
	 *            los dias.
	 * @param numDays
	 *            Numero de dias a sumar a la fecha actual.
	 * @return Objeto Date con la fecha de dentro de numDays dias.
	 */
	public static java.util.Date getDateAfter(Locale locale, Date fechaReferencia, int numDays, int numHours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fechaReferencia);
		cal.add(Calendar.HOUR_OF_DAY, numHours);
		return cal.getTime();
	}

	public static String getTime(String formato) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		String result = sdf.format(getTime());
		return result;
	}

	/**
	 * Obtiene la fecha y hora actuales en formato "2015-07-25 14:32:58"
	 *
	 * @return La fecha y hora actuales
	 */
	public static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = sdf.format(getTime());
		return result;
	}

	public static String getDateTime(int seconds) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = sdf.format(getTime(seconds));
		return result;
	}
	
	public static String getDateTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = sdf.format(date);
		return result;
	}

	/**
	 * Obtiene la fecha y hora actuales en formato "2015-07-25"
	 *
	 * @return La fecha y hora actuales
	 */
	public static String getDateNow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String result = sdf.format(getTime());
		return result;
	}

	/**
	 * Obtiene la fecha y hora de hace numDays dias, respecto de la
	 * fechaReferencia, en formato "2015-07-25 14:32:58"
	 *
	 * @param locale
	 *            Locale a considerar para gestionar el calculo de fechas.
	 * @return La fecha y hora de hace numDays dias
	 */
	public static String getDateTimeBefore(Locale locale, Date fechaReferencia, int numDays, int numHours) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = sdf.format(getDateBefore(locale, fechaReferencia, numDays, numHours));
		return result;
	}

	/**
	 * Obtiene la fecha y hora de dentro de numDays dias, respecto de la
	 * fechaReferencia, en formato "2015-07-25 14:32:58"
	 *
	 * @param locale
	 *            Locale a considerar para gestionar el calculo de fechas.
	 * @return La fecha y hora de dentro de numDays
	 */
	public static String getDateTimeAfter(Locale locale, Date fechaReferencia, int numDays, int numHours) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = sdf.format(getDateAfter(locale, fechaReferencia, numDays, numHours));
		return result;
	}

	/**
	 * Recibe una fecha en formato "2015-07-25 14:32:58" y devuelve una fecha en
	 * formato "25/07/2015".
	 *
	 * @param fecha
	 *            Fecha en formato "2015-07-25 14:32:58"
	 * @return
	 */
	public static String getFechaFormatted(String fecha) {
		String result = "";
		if ((fecha != null) && (!fecha.equals(""))) {
			String anho = fecha.substring(0, 4);
			String mes = fecha.substring(5, 7);
			String dia = fecha.substring(8, 10);
			result = dia + "/" + mes + "/" + anho;
		}
		;
		return result;
	}

	/**
	 * Recibe una fecha en formato "2015-07-25 14:32:58" y devuelve una hora en
	 * formato "14:32".
	 *
	 * @param fecha
	 *            Fecha en formato "2015-07-25 14:32:58"
	 * @return
	 */
	public static String getHoraFormatted(String fecha) {
		String result = "";
		if ((fecha != null) && (!fecha.equals(""))) {
			String hora = fecha.substring(11, 16);
			result = hora;
		}
		;
		return result;
	}

	/**
	 * Compara dos fechas pasadas como String, y en el formato dd/mm/yyyy
	 * Devuelve correcto si la primera es menor que la segunda
	 *
	 * @param fecha1
	 * @param fecha2
	 * @return
	 * @throws GeneralException
	 */
	public static boolean menorFecha(String fecha1, String fecha2) throws GeneralException {
		GeneralException excepcion = new GeneralException("Fecha", "compararFechas", "Formato de Fecha Incorrecto");
		try {
			boolean correcta = true;
			String diaFecha1 = fecha1.substring(0, 2);
			String diaFecha2 = fecha2.substring(0, 2);
			String mesFecha1 = fecha1.substring(3, 5);
			String mesFecha2 = fecha2.substring(3, 5);
			String anoFecha1 = fecha1.substring(6, 10);
			String anoFecha2 = fecha2.substring(6, 10);
			if (Integer.valueOf(anoFecha1).intValue() > Integer.valueOf(anoFecha2).intValue()) {
				correcta = false;
			}
			if (anoFecha1.equals(anoFecha2)) {
				if (Integer.valueOf(mesFecha1).intValue() > Integer.valueOf(mesFecha2).intValue()) {
					correcta = false;
				}
				if (mesFecha1.equals(mesFecha2)) {
					if (Integer.valueOf(diaFecha1).intValue() > Integer.valueOf(diaFecha2).intValue()) {
						correcta = false;
					}
				}
			}
			return correcta;
		} catch (Exception exc) {
			System.out.println(exc);
			throw (excepcion);
		}
	}

	/**
	 * Devuelve un String con la fecha actual con el formato yyyyMMdd
	 *
	 * @return String
	 */
	public static String getFechaActualyyyyMMdd() {

		Calendar c = Calendar.getInstance();

		String dia = Integer.toString(c.get(Calendar.DATE));
		String mes = Integer.toString(c.get(Calendar.MONTH) + 1);
		String ano = Integer.toString(c.get(Calendar.YEAR));

		if (mes.length() == 1) {
			mes = "0" + mes;
		}
		if (dia.length() == 1) {
			dia = "0" + dia;
		}

		String fecha = ano.trim() + mes.trim() + dia.trim();

		return fecha;
	}

	/**
	 * Devuelve la fecha actual en un String con el formato dd/MM/yyyy
	 *
	 * @return String
	 */
	public static String getFechaActual() {
		Calendar c = Calendar.getInstance();

		String dia = Integer.toString(c.get(Calendar.DATE));
		String mes = Integer.toString(c.get(Calendar.MONTH) + 1);
		String ano = Integer.toString(c.get(Calendar.YEAR));

		if (mes.length() == 1) {
			mes = "0" + mes;
		}
		if (dia.length() == 1) {
			dia = "0" + dia;
		}

		String fecha = dia.trim() + "/" + mes.trim() + "/" + ano.trim();

		return fecha;
	}

	/**
	 * Convierte una cadena en un objeto Date.
	 *
	 * @param fecha
	 *            Cadena fecha.
	 * @return Objeto Date.
	 * @throws GeneralException
	 */
	public static Date toDate(String fecha) throws GeneralException {
		GeneralException excepcion = new GeneralException("Fecha", "toDate", "Formato de Fecha Incorrecto");
		try {
			int ano;
			int mes;
			int dia;
			String aux = fecha;

			aux = to_dmy(fecha);

			if (!chequearFecha(aux))
				throw (excepcion);
			else {
				ano = new Integer(aux.substring(6, 10)).intValue();
				mes = new Integer(aux.substring(3, 5)).intValue();
				dia = new Integer(aux.substring(0, 2)).intValue();
			}
			Calendar calendario = Calendar.getInstance();
			calendario.clear();
			calendario.set(Calendar.YEAR, ano);
			calendario.set(Calendar.MONTH, mes - 1); // Los meses se numeran de
														// 0 a 11
			calendario.set(Calendar.DATE, dia);
			java.util.Date t = calendario.getTime();
			Date result = new Date(t.getTime());
			return (result);
		} catch (Exception exc) {
			System.out.println(exc);
			throw (excepcion);
		}
	}

	public static String to_dmy(String s) throws GeneralException {
		GeneralException excepcion = new GeneralException("Fecha", "to_dmy", "Formato de Fecha Incorrecto");

		try {
			s = s.trim();
			String t = "";

			if (s.length() == 10 && s.charAt(4) == '/')
				t = s.substring(8, 10) + "/" + s.substring(5, 7) + "/" + s.substring(0, 4);
			else
				t = expandirFecha_dmy4(s);

			if (chequearFecha(t))
				return (t);
			else
				throw (excepcion);
		} catch (GeneralException exc) {
			throw (exc);

		} catch (Exception exc) {
			throw (excepcion);
		}
	}

	// Convierte una Fecha pasada en un String en un formato corto != dd/mm/yyyy
	// y lo convierte a este
	// formato o devuelve una excepcion.
	private static String expandirFecha_dmy4(String fecha) throws GeneralException {

		String resultado = fecha;
		try {
			if ("".equals(fecha)) {
				return fecha;
			}

			StringTokenizer tokens = new StringTokenizer(fecha, "/");
			if (tokens.countTokens() == 3 && fecha.length() != 10) // La fecha
																	// llega con
																	// barras,
																	// pero
																	// incorrecto.
			{
				String dato = tokens.nextToken().trim();

				if (dato.length() <= 2 && dato.length() > 0) {
					dato = "0" + dato;
				} else {
					throw (new GeneralException("Fecha", "expandirFecha_dmy4", "Formato de Fecha Incorrecto"));
				}
				resultado = dato.substring(dato.length() - 2) + "/";

				dato = tokens.nextToken().trim();
				if (dato.length() <= 2 && dato.length() > 0) {
					dato = "0" + dato;
				} else {
					throw (new GeneralException("Fecha", "expandirFecha_dmy4", "Formato de Fecha Incorrecto"));
				}
				resultado += dato.substring(dato.length() - 2) + "/";
				resultado += tokens.nextToken().trim();
			}
			while (resultado.length() != 10) {
				if (resultado.charAt(2) != '/') {
					resultado = resultado.substring(0, 2) + "/" + resultado.substring(2, resultado.length());
					continue;
				}
				if (resultado.charAt(5) != '/') {
					resultado = resultado.substring(0, 5) + "/" + resultado.substring(5, resultado.length());
					continue;
				}

				String aux = resultado.substring(6, resultado.length());

				if (aux.length() == 2) {
					if (Integer.parseInt(aux) > 80)
						resultado = resultado.substring(0, 6) + "19" + aux;
					else
						resultado = resultado.substring(0, 6) + "20" + aux;
				}

				if (resultado.length() == 10)
					break;
				else
					throw (new GeneralException("Fecha", "expandirFecha_dmy4", "Formato de Fecha Incorrecto"));
			}
		} catch (Exception exc) {
			/**/exc.printStackTrace();
			throw (new GeneralException("Fecha", "expandirFecha_dmy4", "Formato de Fecha Incorrecto" + exc.getMessage()));
		}
		return (resultado);
	}

	/**
	 * Chequea si una Fecha es Correcta. La fecha se debe pasar en Formato
	 * dd/mm/yyyy
	 *
	 * @param pFecha
	 *            Fecha.
	 * @return true -> Fecha correcta, false --> fecha incorrecta.
	 */
	private static boolean chequearFecha(String pFecha) {
		boolean resultado = false;
		int ano = new Integer(pFecha.substring(6, 10)).intValue();
		int mes = new Integer(pFecha.substring(3, 5)).intValue();
		int dia = new Integer(pFecha.substring(0, 2)).intValue();
		switch (mes) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			if (dia >= 1 && dia <= 31)
				resultado = true;
			else
				resultado = false;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			if (dia >= 1 && dia <= 30)
				resultado = true;
			else
				resultado = false;
			break;
		case 2:
			if (isBisiesto(ano))
				if (dia >= 1 && dia <= 29)
					resultado = true;
				else
					resultado = false;
			else if (dia >= 1 && dia <= 28)
				resultado = true;
			else
				resultado = false;
			break;
		default:
			resultado = false;
			break;
		}

		return (resultado);
	}

	// Chequea si un a�o es bisiesto.
	private static boolean isBisiesto(int ano) {
		int dias = 365;
		if (ano % 4 == 0)
			dias++;
		if (ano % 100 == 0)
			dias--;
		if (ano % 400 == 0)
			dias++;
		return (dias == 366);
	}

	public static String pon2Digitos(int numero) {
		String result = (numero <= 9 ? "0" + numero : "" + numero);
		return result;
	}

	/**
	 * Devuelve la hora actual del sistema en formato hhMMss
	 *
	 * @return String con la hora del sistema en formato hhMMss
	 */
	public static String getHoraActual() {
		String result = null;
		Calendar calendario = Calendar.getInstance();
		calendario.clear();
		calendario.setTime(new java.sql.Date(System.currentTimeMillis()));
		result = (pon2Digitos(calendario.get(Calendar.HOUR_OF_DAY)) + pon2Digitos(calendario.get(Calendar.MINUTE)) + pon2Digitos(calendario
				.get(Calendar.SECOND)));
		return result;
	}

	/**
	 * Devuelve la hora actual del sistema en formato entero de 0 a 23
	 *
	 * @return String con la hora del sistema en formato 0-23
	 */
	public static int getHoraActualInt() {
		int result;
		Calendar calendario = Calendar.getInstance();
		calendario.clear();
		calendario.setTime(new java.sql.Date(System.currentTimeMillis()));
		result = calendario.get(Calendar.HOUR_OF_DAY);
		return result;
	}

	/**
	 * Convierte un string de fecha en formato Calendar
	 *
	 * @param date
	 * 				La fecha en formato string
	 * @param format
	 * 				El formato al que se quiere pasar
	 * @return Calendar
	 */
	public static Calendar parseToCalendar(String date, String format) {
		try {
			Calendar cal = Calendar.getInstance();

			SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
			cal.setTime(dateFormatter.parse(date));

			return cal;
		} catch(ParseException e) {
			return null;
		}
	}

	/**
	 * Convierte un string de fecha en formato Calendar
	 *
	 * @param date
	 * 				La fecha en formato string
	 * @return Calendar
	 */
	public static Calendar parseToCalendar(String date) {
		// Por defecto cogemos el formato "2016-06-22 13:40:42"
		return parseToCalendar(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String formatFecha(String fecha) {
		String result = null;

		if ((fecha != null) && (!fecha.equals(""))) {
			if (fecha.length() == 8) {
				String anho = fecha.substring(0, 4);
				String mes = fecha.substring(4, 6);
				String dia = fecha.substring(6, 8);
				try {
					int day = new Integer(dia).intValue();
					int month = new Integer(mes).intValue();
					int year = new Integer(anho).intValue();
					if ((year) < 1900)
						return null;
					if ((month < 1) || (month > 12))
						return null;
					if ((day < 1) || (day > 31))
						return null;

				} catch (NumberFormatException e) {
					return null;
				}
				result = dia + "/" + mes + "/" + anho;
			}
		}
		;
		if (fecha.equals("")) {
			result = "";
		}
		return result;
	}
	
	// Comprueba el formato de la fecha el cual debe ser 11/11/2006
		// No comprueba el separador.
		// Si devuelve un null entonces el formato de la fecha no es el correcto.
		// En caso contrario devuelve un String con el siguiente formato 20061111
		public static String checkFormatFecha(String fecha) {

			String date = fecha.trim();

			if (date.length() == 10) {
				String day = date.substring(0, 2);
				String month = date.substring(3, 5);
				String year = date.substring(6, 10);
				try {
					int dia = new Integer(day).intValue();
					int mes = new Integer(month).intValue();
					int anho = new Integer(year).intValue();
					if ((anho) < 1900)
						return null;
					if ((mes < 1) || (mes > 12))
						return null;
					if ((dia < 1) || (dia > 31))
						return null;

				} catch (NumberFormatException e) {
					return null;
				}

				date = year + month + day;
			} else {
				if (date.length() == 9) {
					if (date.substring(1, 1).equals("")) {
						String day = date.substring(0, 1);
						String month = date.substring(2, 4);
						String year = date.substring(5, 9);
						try {
							int dia = new Integer(day).intValue();
							int mes = new Integer(month).intValue();
							int anho = new Integer(year).intValue();
							if ((anho) < 1900)
								return null;
							if ((mes < 1) || (mes > 12))
								return null;
							if ((dia < 1) || (dia > 31))
								return null;

						} catch (NumberFormatException e) {
							return null;
						}

						date = year + month + "0" + day;
					} else {
						String day = date.substring(0, 2);
						String month = date.substring(3, 4);
						String year = date.substring(5, 9);
						try {
							int dia = new Integer(day).intValue();
							int mes = new Integer(month).intValue();
							int anho = new Integer(year).intValue();
							if ((anho) < 1900)
								return null;
							if ((mes < 1) || (mes > 12))
								return null;
							if ((dia < 1) || (dia > 31))
								return null;

						} catch (NumberFormatException e) {
							return null;
						}

						date = year + "0" + month + day;
					}
				} else {
					if (date.length() == 8) {

						String day = date.substring(0, 1);
						String month = date.substring(2, 3);
						String year = date.substring(4, 8);
						try {
							int dia = new Integer(day).intValue();
							int mes = new Integer(month).intValue();
							int anho = new Integer(year).intValue();
							if ((anho) < 1900)
								return null;
							if ((mes < 1) || (mes > 12))
								return null;
							if ((dia < 1) || (dia > 31))
								return null;

						} catch (NumberFormatException e) {
							return null;
						}

						date = year + month + "0" + day;
					} else {
						if (fecha.compareTo("") == 0) {
							return "";

						} else {
							return null;
						}
					}
				}
			}
			return date;
		}

}// class
