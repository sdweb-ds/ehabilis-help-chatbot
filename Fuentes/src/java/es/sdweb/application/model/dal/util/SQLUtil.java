package es.sdweb.application.model.dal.util;

import es.sdweb.application.componentes.util.Booleano;
import es.sdweb.application.componentes.util.NumberUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;

/**
 * Utilidades para operar con elementos de acceso a datos como
 * ResultSets, Statements, etc.
 */
abstract public class SQLUtil {
	/**
	 * Obtener un String de un ResultSet a partir del nombre del campo
	 * 
	 * @param resultSet
	 * 			El ResultSet del que se desea extraer el String
	 * @param fieldName
	 * 			El nombre del campo que se desea obtener
	 * @return El valor que se desea obtener o una cadena vacía
	 * @throws SQLException
	 */
	public static String getString(ResultSet resultSet, String fieldName) throws SQLException {
		String value = "";
		
		value = resultSet.getString(fieldName);
		
		// Si el valor obtenido fué NULL
		if (resultSet.wasNull()) {
			// Devolvemos una cadena vacía
			value = "";
		} else {
			value = StringUtil.trim(value);
		}

		return value;
	}
	
	/**
	 * Obtener un String de un ResultSet a partir de su posición
	 * 
	 * @param resultSet
	 * 			El ResultSet del que se desea extraer el String
	 * @param fieldPos
	 * 			El nombre del campo que se desea obtener
	 * @return El valor que se desea obtener o una cadena vacía
	 * @throws SQLException
	 */
	public static String getString(ResultSet resultSet, int fieldPos) throws SQLException {
		String value = "";
		
		value = resultSet.getString(fieldPos);
		
		// Si el valor obtenido fué NULL
		if (resultSet.wasNull()) {
			// Devolvemos una cadena vacía
			value = "";
		} else {
			value = StringUtil.trim(value);
		}
		
		return value;
	}

	/**
	 * Obtener un String en mayúsculas de un ResultSet a partir de su posición
	 * 
	 * @param resultSet
	 * 			El ResultSet del que se desea extraer el String
	 * @param fieldPos
	 * 			El nombre del campo que se desea obtener
	 * @return El valor que se desea obtener o una cadena vacía
	 * @throws SQLException
	 */
	public static String getStringUpper(ResultSet resultSet, int fieldPos) throws SQLException {
		String valor = SQLUtil.getString(resultSet, fieldPos);
		
		return StringUtil.mayusculas(valor);
	}

	/**
	 * Obtiene un Double de un ResultSet redondeado a 2 posiciones decimales a partir de su posición
	 * 
	 * @param resultSet
	 * 			El ResultSet del que se desea extraer el Double
	 * @param fieldPos
	 * 			La posición que ocupa el campo dentro del ResultSet
	 * @return El valor que se desea obtener o 0
	 * @throws SQLException
	 */
	public static double getDouble(ResultSet resultSet, int fieldPos) throws SQLException {
		double value = 0.0;

		value = resultSet.getDouble(fieldPos);

		// Si el valor obtenido fué NULL
		if (resultSet.wasNull()) {
			// Devolvemos 0.0
			value = 0.0;
		}

		// Redondeamos a 2 decimales
		value = NumberUtil.roundDouble(value, 2);

		return value;
	}
	
	/**
	 * Obtiene un Integer de un ResultSet a partir del nombre del campo
	 * 
	 * @param resultSet
	 * 			El ResultSet del que se desea extraer el Integer
	 * @param fieldName
	 * 			En nombre del campo que se desea obtener
	 * @return El valor que se desea obtener o 0
	 * @throws SQLException
	 */
	public static int getInteger(ResultSet resultSet, String fieldName) throws SQLException {
		int value = 0;
		
		// Comprobamos que no sea BOF ni EOF
		if (!resultSet.isAfterLast() && !resultSet.isBeforeFirst()) {
			value = resultSet.getInt(fieldName);
		}
		
		return value;
	}

	/**
	 * Obtiene un Integer de un ResultSet a partir de su posición
	 * 
	 * @param resultSet
	 * 			El ResultSet del que se desea extraer el Integer
	 * @param fieldPos
	 * 			La posición que ocupa el campo dentro del ResultSet
	 * @return El valor que se desea obtener o 0
	 * @throws SQLException
	 */
	public static int getInteger(ResultSet resultSet, int fieldPos) throws SQLException {
		int value = 0;
		
		value = resultSet.getInt(fieldPos);
		
		// Si el valor obtenido fué NULL
		if (resultSet.wasNull()) {
			// Devolvemos 0
			value = 0;
		}
		
		return value;
	}

	/**
	 * Obtiene un Integer en forma de String de un ResultSet a partir de su posición
	 * 
	 * @param resultSet
	 * 			El ResultSet del que se desea extraer el Integer
	 * @param fieldPos
	 * 			La posición que ocupa el campo dentro del ResultSet
	 * @return El valor que se desea obtener o 0
	 * @throws SQLException
	 */
	public static String getIntegerToString(ResultSet resultSet, int fieldPos) throws SQLException {
		int value = SQLUtil.getInteger(resultSet, fieldPos);
		
		return (new Integer(value).toString());
	}

	/**
	 * Pasa un String a Integer y lo almacena como tal, o como nulo.
	 * Si el número es negativo, se almacenará como NULL también.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el Integer
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param str
	 * 			La cadena que se almacenará como Integer
	 * @throws SQLException
	 */
	public static void setStringToInteger(PreparedStatement statement, int fieldPos, String str) throws SQLException {
		Integer value = null;
		
		if ((str != null) && (!StringUtil.trim(str).equals("")) && (!str.trim().toLowerCase().equals("null"))) {
			value = new Integer(str);
		}
		
		SQLUtil.setInteger(statement, fieldPos, value);
	}

	/**
	 * Recibe un Integer y lo almacena como tal, o como nulo.
	 * Si el número es negativo, se almacenará como NULL también.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el Integer
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param intVal
	 * 			El Integer que se desea insertar
	 * @throws SQLException
	 */
	public static void setInteger(PreparedStatement statement, int fieldPos, Integer intVal) throws SQLException {
		// Si el valor es mayor o igual que cero lo metemos
		if (intVal != null) {
			statement.setInt(fieldPos, intVal);
		} else {
			statement.setNull(fieldPos, Types.INTEGER);
		}
	}
	
	/**
	 * Recibe un String y lo almacena como Integer.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el array de bytes
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param strInt
	 * 			El String que se desea insertar como Integer
	 * @throws SQLException
	 */
	public static void setInteger(PreparedStatement statement, int fieldPos, String strInt) throws SQLException {
		SQLUtil.setStringToInteger(statement, fieldPos, strInt);
	}

	/**
	 * Recibe un double y lo almacena.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el Integer
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param value
	 * 			El Integer que se desea insertar
	 * @throws SQLException
	 */
	public static void setDouble(PreparedStatement statement, int fieldPos, double value) throws SQLException {
		statement.setDouble(fieldPos, value);
	}
	
	/**
	 * Recibe un string y lo almacena.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el String
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param value
	 * 			El String que se desea insertar
	 * @throws SQLException
	 */
	public static void setString(PreparedStatement statement, int fieldPos, String value) throws SQLException {
		if ((value != null) && (!StringUtil.trim(value).equals("")) && (!StringUtil.trim(value).toLowerCase().equals("null"))) {
			statement.setString(fieldPos, StringUtil.trim(value));
		} else {
			statement.setNull(fieldPos, Types.CHAR);
		}
	}

	/**
	 * Recibe un string y lo almacena en mayúsculas.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el String
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param value
	 * 			El String que se desea insertar
	 * @throws SQLException
	 */
	public static void setStringUpper(PreparedStatement statement, int fieldPos, String value) throws SQLException {
		SQLUtil.setString(statement, fieldPos, StringUtil.mayusculas(value));
	}

	/**
	 * Recibe un string y lo almacena en mayúsculas con una longitud máxima.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el String
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param value
	 * 			El String que se desea insertar
	 * @param maxLength
	 * 			El número máximo de caracteres que se permiten en el campo
	 * @throws SQLException
	 */
	public static void setStringUpper(PreparedStatement statement, int fieldPos, String value, int maxLength) throws SQLException {
		if ((value != null) && (!StringUtil.trim(value).equals("")) && (!StringUtil.trim(value).toLowerCase().equals("null"))) {
			value = value.substring(0, maxLength);
		}
		
		SQLUtil.setStringUpper(statement, fieldPos, value);
	}

	/**
	 * Recibe un string y lo almacena tanto en el PreparedStatement como en los Vectores de campos y valores.
	 * No creo que se use esto para nada.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el String
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param value
	 * 			El String que se desea insertar
	 * @param fields
	 * 			Vector con los campos añadidos
	 * @param values
	 * 			Vector con los valores añadidos
	 * @throws SQLException
	 */
	@Deprecated
	public static void setString(PreparedStatement statement, int fieldPos, String value, Vector<Integer> fields, Vector<String> values) throws SQLException {
		SQLUtil.setString(statement, fieldPos, value);
		fields.add(new Integer(fieldPos));
		values.add(value);
	}

	/**
	 * Recibe un string y lo almacena.
	 * Si el String está vacío lo almacena tal cual y no como NULL.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el String
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param value
	 * 			El String que se desea insertar
	 * @throws SQLException
	 */
	public static void setStringVacio(PreparedStatement statement, int fieldPos, String value) throws SQLException {
		if ((value != null) && (!StringUtil.trim(value).equals("")) && (!StringUtil.trim(value).toLowerCase().equals("null"))) {
			statement.setString(fieldPos, value);
		} else {
			statement.setString(fieldPos, "");
		}
	}

	/**
	 * Recibe un string y lo almacena tanto en el PreparedStatement como en los Vectores de campos y valores.
	 * Si el String está vacío lo almacena tal cual y no como NULL.
	 * No creo que se use esto para nada.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el String
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param value
	 * 			El String que se desea insertar
	 * @param fields
	 * 			Vector con los campos añadidos
	 * @param values
	 * 			Vector con los valores añadidos
	 * @throws SQLException
	 */
	@Deprecated
	public static void setStringVacio(PreparedStatement statement, int fieldPos, String value, Vector<Integer> fields, Vector<String> values) throws SQLException {
		fields.add(new Integer(fieldPos));
		
		
		if ((value != null) && (!StringUtil.trim(value).equals("")) && (!StringUtil.trim(value).toLowerCase().equals("null"))) {
			statement.setString(fieldPos, value);
			values.add(value);
		} else {
			statement.setString(fieldPos, "");
			values.add("");
		}
	}

	/**
	 * Recibe un array de bytes y lo almacena (Normalmente un fichero).
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el array de bytes
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param value
	 * 			El array de bytes que se desea insertar
	 * @throws SQLException
	 */
	public static void setBinary(PreparedStatement statement, int fieldPos, byte[] value) throws SQLException {
		if (value != null) {
			// Creamos un nuevo InpuStream
			InputStream inputStream = new ByteArrayInputStream(value);
			
			// Si el fichero tiene bytes dentro
			if (value.length > 0) {
				// Metemos los datos binarios del fichero
				statement.setBinaryStream(fieldPos, inputStream, value.length);
			} else {
				statement.setNull(fieldPos, Types.BINARY);
			}
		} else {
			statement.setNull(fieldPos, Types.BINARY);
		}
	}

	/**
	 * Recibe un String y lo almacena, devolviendo el número de valores almacenados.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el array de bytes
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param value
	 * 			El String que se desea insertar
	 * @param setNull
	 * 			Flag que indica si se debe guardar cuando el campo es NULL
	 * @return El número de valores almacenados 
	 * @throws SQLException
	 */
	public static int setStringAcumulado(PreparedStatement statement, int fieldPos, String value, boolean setNull) throws SQLException {
		if ((value != null) && (!StringUtil.trim(value).equals("")) && (!StringUtil.trim(value).toLowerCase().equals("null"))) {
			SQLUtil.setString(statement, fieldPos, value);
			
			return fieldPos + 1;
		} else {
			if (setNull) {
				statement.setNull(fieldPos, Types.CHAR);
			}
			// TODO: Si se añade no se debería incrementar el contador?
			return fieldPos;
		}
	}

	/**
	 * Recibe un Long y lo almacena.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el array de bytes
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param strLong
	 * 			El Long que se desea insertar
	 * @throws SQLException
	 */
	public static void setLong(PreparedStatement statement, int fieldPos, String strLong) throws SQLException {
		if ((strLong != null) && (!StringUtil.trim(strLong).equals("")) && (!StringUtil.trim(strLong).toLowerCase().equals("null"))) {
			statement.setLong(fieldPos, Long.parseLong(strLong));
		} else {
			statement.setNull(fieldPos, Types.INTEGER);
		}
	}

	/**
	 * Recibe un Long y lo almacena, devolviendo el número de valores almacenados.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el array de bytes
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param strLong
	 * 			El Long que se desea insertar
	 * @param setNull
	 * 			Flag que indica si se debe guardar cuando el campo es NULL
	 * @throws SQLException
	 */
	public static int setLongAcumulado(PreparedStatement statement, int fieldPos, Object strLong, boolean setNull) throws SQLException {
		if ((strLong != null) && (!strLong.toString().trim().equals("")) && (!strLong.toString().trim().toLowerCase().equals("null"))) {
			statement.setLong(fieldPos, Long.parseLong(strLong.toString()));
			return fieldPos + 1;
		} else {
			if (setNull) {
				statement.setNull(fieldPos, Types.INTEGER);
			}
			
			// TODO: Si se añade no se debería incrementar el contador?
			return fieldPos;
		}
	}

	/**
	 * Recibe un Long y lo almacena, devolviendo el número de valores almacenados.
	 * 
	 * @param statement
	 * 			El PreparedStatement en el que se desea insertar el array de bytes
	 * @param fieldPos
	 * 			La posición que debe ocupar el campo dentro del PreparedStatement
	 * @param date
	 * 			El Date que se desea insertar
	 * @param setNull
	 * 			Flag que indica si se debe guardar cuando el campo es NULL
	 * @throws SQLException
	 */
	public static int setDateAcumulado(PreparedStatement statement, int fieldPos, Object date, boolean setNull) throws SQLException {
		if ((date != null) && (!date.toString().trim().equals("")) && (!date.toString().trim().toLowerCase().equals("null"))) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			try {
				Date fec = sdf.parse(date.toString());
				statement.setDate(fieldPos, new java.sql.Date(fec.getTime()));
			} catch (ParseException e) {
				e.printStackTrace(System.err);
			}

			return fieldPos + 1;
		} else {
			if (setNull) {
				statement.setNull(fieldPos, Types.DATE);
			}
			
			return fieldPos;
		}
	}

	/**
	 * Cierra la conexión a la base de datos
	 * 
	 * @param connection
	 * 			La conexión que se desea cerrar
	 * @return True si la conexión se ha cerrado con éxito, False en caso contrario
	 */
	public static boolean cierraConexion(Connection connection) {
		boolean result = false;
		
		if (connection != null) {
			try {
				connection.close();
				result = true;
			} catch (SQLException e) {
				Log.logRE("SQLUtil.cierraConexion()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, 4201,
						"Se ha producido un error al intentar cerrar la conexión a la base de datos: " + e.toString());
			}
		}

		return result;
	}

	/**
	 * Realiza un rollback o un commit según el parámetro rollback y después cierra la conexión
	 * 
	 * @param connection
	 * 			La conexión que se desea usar
	 * @param doRollback
	 * 			Flag que indica si se debe realizar un rollback o no
	 */
	public static void rollbackOrCommitAndCloseConnection(Connection connection, boolean doRollback) throws SQLException {
		if (connection != null) {
			if (doRollback) {
				connection.rollback();
			} else {
				connection.commit();
			}
			SQLUtil.cierraConexion(connection);
		}
	}

	/**
	 * Cierra un PreparedStatement
	 * 
	 * @param statement
	 * 			La PreparedStatement que se desea cerrar
	 * @return True si se ha cerrado con éxito, False en caso contrario
	 */
	public static boolean cierraPreparedStatement(PreparedStatement statement) {
		boolean result = false;
		if (statement != null) {
			try {
				statement.close();
				result = true;
			} catch (Exception e) {
				Log.logRE("SQLUtil.cierraPreparedStatement()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, 4201,
						"Se ha producido un error al intentar cerrar el PreparedStatement: " + e.toString());
			}
		}

		return result;
	}
	
	/**
	 * Convierte un String a SQL Date 
	 * 
	 * @param date
	 * 			La fecha que se desea convertir
	 * @return La fecha en formato SQL Date
	 */
	public static java.sql.Date toDate(String date) {
		java.sql.Date result = null;

		if (!StringUtil.isEmpty(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date dateS = null;
			try {
				dateS = sdf.parse(date);
			} catch (ParseException e) {
				Log.logRE("SQLUtil.toDate()", Log.TIPO_BASE_DATOS, Log.CRITICIDAD_NORMAL, 4201,
						"Se ha producido un error al parsear la fecha: " + e.toString());
			}

			result = new java.sql.Date(dateS.getTime());
		}

		return result;
	}

	/**
	 * Devuelve un filtro SQL
	 * 
	 * @param fieldName
	 * 			Nombre del campo que se quiere comparar
	 * @param operator
	 * 			Operador que se desea usar (=, <>, <, >...)
	 * @param value
	 * 			El valor con el que se va a comparar, si es null o un string vacío
	 * 			se devuelve una cadena vacía.
	 * @param logicOperator
	 * 			Operador lógico (AND, OR, ...)
	 * @param addLogicOperator
	 * 			Flag para indicar si se debe añadir o no el operador lógico
	 * @return El filtro SQL
	 */
	public static String filtroLogico(String fieldName, String operator, Object value, String logicOperator, Booleano addLogicOperator) {
		String result = "";
		if ((value != null) && (!value.toString().equals(""))) {
			if (addLogicOperator.isBool()) {
				result = StringUtil.cat(" ", logicOperator);
			} else {
				addLogicOperator.setBool(true);
			}

			result += " (" + fieldName + " " + operator + " ?)";
		}

		return result;
	}

	/**
	 * Genera una expresion del tipo "AND (nombreCampo=valor)" cuando
	 * noParameter es true y la relacion es "="; o
	 * "AND (nombreCampo=?)" cuando es falso. Si ponOpLogico es false se genera
	 * una expresion del tipo "(nombreCampo=valor)" o "(nombreCampo=?)".
	 *
	 * @param fieldName
	 *            Nombre del campo sobre el que se desea operar.
	 * @param operator
	 *            Operador logico que operara sobre el campo.
	 * @param value
	 *            Valor que se relacionara con el campo.
	 * @param logicOperator
	 *            Operador logico que se antepone, en caso de ser necesario, a
	 *            la operacion logica.
	 * @param addLogicOperator
	 *            Flag que indica si hay que anteponer un operador logico.
	 * @param addParameter
	 *            Flag que indica si el valor hay que indicarlo o dejarlo como "?".
	 * @return La expresión generada
	 */
	public static String filtroLogico(String fieldName, String operator, Object value, String logicOperator, Booleano addLogicOperator, boolean addParameter) {
		String result = "";
		if ((value != null) && (!value.toString().equals(""))) {
			if (addLogicOperator.isBool()) {
				result = StringUtil.cat(" ", logicOperator);
			} else {
				addLogicOperator.setBool(true);
			}

			result = (addParameter ? StringUtil.cat(result, " (", fieldName, " ", operator, " ", value.toString(), " ) ") : StringUtil.cat(result, " (",
					fieldName, " ", operator, " ?) "));
		}
		
		return result;
	}

	/**
	 * Devuelve una expresion del tipo " AND (nombreCampo LIKE '%texto%')", si
	 * likeIzdo es true, y operadorLogico es "AND".
	 *
	 * @param fieldName
	 *            Campo sobre el que se opera.
	 * @param text
	 *            Texto a buscar con el Like.
	 * @param leftWildcard
	 *            True si tambien se desea incluir el comodin "%" a la izquierda
	 *            del texto.
	 * @param logicOperator
	 *            Operador logico que se antepone, en caso de ser necesario, a
	 *            la operacion logica.
	 * @param addLogicOperator
	 *            Flag que indica si hay que anteponer un operador logico.
	 * @return La expresión generada
	 */
	public static String filtroLike(String fieldName, String text, boolean leftWildcard, String logicOperator, Booleano addLogicOperator) {
		String result = "";
		if ((text != null) && (!StringUtil.trim(text).equals(""))) {
			if (addLogicOperator.isBool()) {
				result = StringUtil.cat(" ", logicOperator);
			} else {
				addLogicOperator.setBool(true);
			}

			String li = (leftWildcard ? "%" : "");
			result += " (" + fieldName + " like '" + li + StringUtil.pon2Comillas(text) + "%')";
		}
		
		return result;
	}

	public static String filtroLikeOR(String nombreCampo1, String nombreCampo2, String texto, boolean likeIzdo, String operadorLogico, Booleano ponOpLogico) {
		String result = "";
		if ((texto != null) && (!texto.trim().equals(""))) {
			if (ponOpLogico.isBool()) {
				result = StringUtil.cat(" ", operadorLogico); // AND, OR, etc
			} else {
				ponOpLogico.setBool(true);
			}
			;
			texto = texto.replaceAll(" ", "%").toUpperCase(); // Hacemos un like
																// por ambos
																// lados y por
																// el medio

			String li = (likeIzdo ? "%" : "");
			result += " ((UPPER(" + nombreCampo1 + ") like '" + li + StringUtil.pon2Comillas(texto) + "%')";
			result += " OR (UPPER(" + nombreCampo2 + ") like '" + li + StringUtil.pon2Comillas(texto) + "%'))";
		}
		;
		return result;
	}

	public static String filtroLikeOR(String nombreCampo1, String nombreCampo2, String nombreCampo3, String texto, boolean likeIzdo, String operadorLogico,
			Booleano ponOpLogico) {
		String result = "";
		if ((texto != null) && (!texto.trim().equals(""))) {
			if (ponOpLogico.isBool()) {
				result = StringUtil.cat(" ", operadorLogico); // AND, OR, etc
			} else {
				ponOpLogico.setBool(true);
			}
			;
			texto = texto.replaceAll(" ", "%").toUpperCase(); // Hacemos un like
																// por ambos
																// lados y por
																// el medio

			String li = (likeIzdo ? "%" : "");
			result += " ((UPPER(" + nombreCampo1 + ") like '" + li + StringUtil.pon2Comillas(texto) + "%')";
			result += " OR (UPPER(" + nombreCampo2 + ") like '" + li + StringUtil.pon2Comillas(texto) + "%')";
			result += " OR (UPPER(" + nombreCampo3 + ") like '" + li + StringUtil.pon2Comillas(texto) + "%'))";
		}
		;
		return result;
	}

	/**
	 * Hace un Like OR no solo con cada campo implicado, sino con todas las
	 * concatenaciones posibles de los campos (separados por un espacio)
	 *
	 * @param nombreCampo1
	 *            Campo de la BD 1
	 * @param nombreCampo2
	 *            Campo de la BD 2
	 * @param nombreCampo3
	 *            Campo de la BD 3
	 * @param texto
	 *            Texto a buscar.
	 * @param likeIzdo
	 *            Si se debe anteponer el simbolo % al texto.
	 * @param operadorLogico
	 *            Operador (AND, OR, NOT) que se antepondra a la clausula logica
	 *            LIKE resultante
	 * @param ponOpLogico
	 *            Si debe ponerse el Operador Logico
	 * @return Clausula logica LIKE
	 */
	public static String filtroLikeORFull(String nombreCampo1, String nombreCampo2, String nombreCampo3, String texto, boolean likeIzdo, String operadorLogico,
			Booleano ponOpLogico) {
		String result = "";
		if ((texto != null) && (!texto.trim().equals(""))) {
			if (ponOpLogico.isBool()) {
				result = StringUtil.cat(" ", operadorLogico); // AND, OR, etc
			} else {
				ponOpLogico.setBool(true);
			}
			;
			texto = texto.replaceAll(" ", "%").toUpperCase(); // Hacemos un like
																// por ambos
																// lados y por
																// el medio

			String li = (likeIzdo ? "%" : "");
			result += " ( UPPER(" + nombreCampo1 + ") LIKE '" + li + StringUtil.pon2Comillas(texto) + "%' ";
			result += " OR UPPER(" + nombreCampo2 + ") LIKE '" + li + StringUtil.pon2Comillas(texto) + "%' ";
			result += " OR UPPER(" + nombreCampo3 + ") LIKE '" + li + StringUtil.pon2Comillas(texto) + "%' ";

			result += " OR CONCAT(UPPER(" + nombreCampo1 + "),' ',UPPER(" + nombreCampo2 + ")) LIKE '" + li + StringUtil.pon2Comillas(texto) + "%' ";
			result += " OR CONCAT(UPPER(" + nombreCampo1 + "),' ',UPPER(" + nombreCampo3 + ")) LIKE '" + li + StringUtil.pon2Comillas(texto) + "%' ";

			result += " OR CONCAT(UPPER(" + nombreCampo2 + "),' ',UPPER(" + nombreCampo1 + ")) LIKE '" + li + StringUtil.pon2Comillas(texto) + "%' ";
			result += " OR CONCAT(UPPER(" + nombreCampo2 + "),' ',UPPER(" + nombreCampo3 + ")) LIKE '" + li + StringUtil.pon2Comillas(texto) + "%' ";

			result += " OR CONCAT(UPPER(" + nombreCampo3 + "),' ',UPPER(" + nombreCampo1 + ")) LIKE '" + li + StringUtil.pon2Comillas(texto) + "%' ";
			result += " OR CONCAT(UPPER(" + nombreCampo3 + "),' ',UPPER(" + nombreCampo2 + ")) LIKE '" + li + StringUtil.pon2Comillas(texto) + "%' ";

			result += " OR CONCAT(UPPER(" + nombreCampo1 + "),' ',UPPER(" + nombreCampo2 + "),' ',UPPER(" + nombreCampo3 + ")) LIKE '" + li
					+ StringUtil.pon2Comillas(texto) + "%' ";
			result += " OR CONCAT(UPPER(" + nombreCampo1 + "),' ',UPPER(" + nombreCampo3 + "),' ',UPPER(" + nombreCampo2 + ")) LIKE '" + li
					+ StringUtil.pon2Comillas(texto) + "%' ";
			result += " OR CONCAT(UPPER(" + nombreCampo2 + "),' ',UPPER(" + nombreCampo1 + "),' ',UPPER(" + nombreCampo3 + ")) LIKE '" + li
					+ StringUtil.pon2Comillas(texto) + "%' ";
			result += " OR CONCAT(UPPER(" + nombreCampo2 + "),' ',UPPER(" + nombreCampo3 + "),' ',UPPER(" + nombreCampo1 + ")) LIKE '" + li
					+ StringUtil.pon2Comillas(texto) + "%' ";
			result += " OR CONCAT(UPPER(" + nombreCampo3 + "),' ',UPPER(" + nombreCampo1 + "),' ',UPPER(" + nombreCampo2 + ")) LIKE '" + li
					+ StringUtil.pon2Comillas(texto) + "%' ";
			result += " OR CONCAT(UPPER(" + nombreCampo3 + "),' ',UPPER(" + nombreCampo2 + "),' ',UPPER(" + nombreCampo1 + ")) LIKE '" + li
					+ StringUtil.pon2Comillas(texto) + "%' )";
		}
		;
		return result;
	}

	/**
	 * Comprueba que cada una de las palabras del texto, en el orden en que
	 * aparecen en el mismo, se encuentre en la concatenacion de los campos
	 * indicados.
	 *
	 * @param nombreCampo1
	 * @param nombreCampo2
	 * @param nombreCampo3
	 * @param texto
	 * @param likeIzdo
	 * @param operadorLogico
	 * @param ponOpLogico
	 * @return
	 */
	public static String filtroLikeORAllText(String nombreCampo1, String nombreCampo2, String nombreCampo3, String texto, boolean likeIzdo,
			String operadorLogico, Booleano ponOpLogico) {
		String result = "";
		if ((texto != null) && (!texto.trim().equals(""))) {
			if (ponOpLogico.isBool()) {
				result = StringUtil.cat(" ", operadorLogico); // AND, OR, etc
			} else {
				ponOpLogico.setBool(true);
			}
			;
			texto = texto.replaceAll(" ", "%").toUpperCase(); // Hacemos un like
																// por ambos
																// lados y por
																// el medio

			String li = (likeIzdo ? "%" : "");
			result += " CONCAT(UPPER(" + nombreCampo1 + "),' ',UPPER(" + nombreCampo2 + "),' ',UPPER(" + nombreCampo3 + ")) LIKE '" + li + StringUtil.pon2Comillas(texto)
					+ "%' ";
		}
		return result;
	}

	public static String filtroIN(String nombreCampo, String conjunto, String operadorLogico, Booleano ponOpLogico) {
		String result = "";
		if ((conjunto != null) && (!conjunto.trim().equals(""))) {
			if (ponOpLogico.isBool()) {
				result = StringUtil.cat(" ", operadorLogico); // AND, OR, etc
			} else {
				ponOpLogico.setBool(true);
			}
			;
			result += " (" + nombreCampo + " IN (" + conjunto + "))";
		}
		;
		return result;
	}

}// class
