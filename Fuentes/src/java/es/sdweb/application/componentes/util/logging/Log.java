package es.sdweb.application.componentes.util.logging;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;
import es.sdweb.application.componentes.util.FileUtil;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.LogException;
import es.sdweb.application.componentes.util.logging.SdwebRollingFileAppender;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.vista.util.GestorInformacionWeb;

/**
 * Clase que se encarga del logging de eventos
 */
public class Log {
	// Nombres de los loggers
	public static final String LOGGER_ACCESO_DATOS_PROTEGIDOS = "es.sdweb.gescul.componentes.util.logging.AccesoDatos";
	public static final String LOGGER_TRAZAS_LOGICA_NEGOCIO = "es.sdweb.gescul.componentes.util.logging.LogicaNegocio";
	public static final String LOGGER_TRAZAS_ERRORES = "es.sdweb.gescul.componentes.util.logging.Errores";

	// Constantes de criticidad de los errores
	public static final int CRITICIDAD_WARNING = 1;
	public static final int CRITICIDAD_MINOR = 2;
	public static final int CRITICIDAD_NORMAL = 3;
	public static final int CRITICIDAD_MAYOR = 4;
	public static final int CRITICIDAD_CRITICAL = 5;

	// Constantes de tipo de error
	public static final int TIPO_ACCESO_DISPONIBILIDAD = 1;
	public static final int TIPO_SISTEMA = 2;
	public static final int TIPO_BASE_DATOS = 3;
	public static final int TIPO_APLICACION = 4;
	public static final int TIPO_ALERTAS = 5;

	// Constates de tipo de acceso a datos
	public static final String TIPO_SQL_SELECT = "SELECT";
	public static final String TIPO_SQL_UPDATE = "UPDATE";
	public static final String TIPO_SQL_DELETE = "DELETE";
	public static final String TIPO_SQL_INSERT = "INSERT";

	// Loggers
	private static Logger logAccesoDatos = null;
	private static Logger logLogicaNegocio = null;
	private static Logger logError = null;

	// Variables estáticas con información sobre la aplicación
	private static String _appName = null;
	private static String _appVersion = null;

	// Propiedades de configuración
	private static Properties props = new Properties();

	/**
	 * Función de inicialización de logging
	 * 
	 * @param configLogs
	 * @param appName
	 * @param appVersion
	 */
	public static void init(InputStream configLogs, String appName, String appVersion) {
		try {
			// Si no se recibe un InputStream con le fichero
			if (configLogs == null) {
				// Lanzamos una excepción
				throw new IOException("Log.init(): [ERROR] No se encuentra el fichero de propiedades log4j.properties.");
			}

			// Cargamos los datos del fichero de properties
			props.load(configLogs);
			// Cerramos el fichero de configuracion
			FileUtil.closeFile(configLogs);

			// Configuramos log4j
			PropertyConfigurator.configure(props);
			
			// Guardamos los loggers en las variables estáticas
			logAccesoDatos = Logger.getLogger(LOGGER_ACCESO_DATOS_PROTEGIDOS);
			logLogicaNegocio = Logger.getLogger(LOGGER_TRAZAS_LOGICA_NEGOCIO);
			logError = Logger.getLogger(LOGGER_TRAZAS_ERRORES);

			// Guardamos los datos de la aplicación
			_appName = appName;
			_appVersion = appVersion;
		} catch (IOException e) {
			// Ante una excepción de lectura del fichero
			// Sacamos la excepción por la salida del sistema
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Constructor privado para impedir que se pueda instanciar esta clase
	 */
	private Log() { }

	/**
	 * Función para loguear acceso a datos de alto nivel
	 * 
	 * @param user
	 * @param baseDatos
	 * @param sgbdHostName
	 * @param ipCliente
	 * @param fechaInicio
	 * @param fechaFin
	 * @param coderror
	 * @param filasDevueltas
	 * @param tipoSQL
	 * @param sentenciaSQL
	 * @throws LogException
	 */
	public static void logRA(String user, String baseDatos, String sgbdHostName, String ipCliente, Date fechaInicio, Date fechaFin, int coderror,
			int filasDevueltas, String tipoSQL, String sentenciaSQL) throws LogException {
		if (logAccesoDatos != null) {
			Appender app = logAccesoDatos.getAppender("AccesoDatos");
			String logFileName = ((SdwebRollingFileAppender) app).getFileName();

			StringBuffer filaLog = new StringBuffer();
			filaLog.append(logFileName);
			filaLog.append("|" + new SimpleDateFormat("yyyyMMdd-HHmmssSS").format(fechaInicio).substring(0, 17));
			// La fecha fin se introduce automáticamente
			filaLog.append("|" + new SimpleDateFormat("yyyyMMdd-HHmmssSS").format(fechaFin).substring(0, 17));
			filaLog.append("|" + (StringUtil.isEmpty(sgbdHostName) ? "UNKNOWN" : sgbdHostName));
			filaLog.append("|" + (StringUtil.isEmpty(_appName) ? "UNKNOWN" : _appName));
			filaLog.append("|" + (StringUtil.isEmpty(_appVersion) ? "UNKNOWN" : _appVersion));
			filaLog.append("|" + (!StringUtil.isEmpty(user) ? user : "UNKNOWN"));
			filaLog.append("|" + (ipCliente != null ? ipCliente : ""));
			filaLog.append("|" + (baseDatos != null ? baseDatos : ""));
			filaLog.append("|" + coderror);
			filaLog.append("|" + filasDevueltas);
			filaLog.append("|" + (tipoSQL != null ? tipoSQL : ""));
			filaLog.append("|" + (sentenciaSQL != null ? sentenciaSQL : "") + "\n");

			// Independientemente del nivel de criticidad, queremos grabar la
			// linea de registro de acceso. Es por ello por
			// lo que utilizamos criticidad FATAL => siempre se grabara la linea
			// de log.
			logAccesoDatos.fatal(filaLog);
		}
	}

	/**
	 * Log de trazas
	 *
	 * @param message
	 */
	public static void logRT(String message) {
		// Si no se envía prioridad, por defecto se entiende que es prioridad DEBUG
		logRT(message, Priority.DEBUG);
	}

	/**
	 * Log de trazas con múltiples mensajes
	 *
	 * @param messages
	 */
	public static void logRT(String[] messages) {
		if (messages != null) {
			for (int i = 0; i < messages.length; i++) {
				logRT(messages[i]);
			}
		}
	}

	/**
	 * Log de trazas
	 *
	 * @param message
	 * @param priority
	 */
	public static void logRT(String message, Priority priority) {
		// Obtenemos el usuario actual
		String user = getUserName();

		// Llamamos al logger con el usuario actual
		logRT(user, message, priority);
	}

	/**
	 * Log de trazas
	 *
	 * @param user
	 * @param message
	 */
	public static void logRT(String user, String message) {
		// Llamamos al logger con prioridad DEBUG
		logRT(user, message, Priority.DEBUG);
	}

	/**
	 * Log de trazas
	 *
	 * @param user
	 * @param message
	 * @param priority
	 */
	public static void logRT(String user, String message, Priority priority) {
		// Si existe el Logger de Lógica de Negocio
		if (logLogicaNegocio != null) {
			// Escribir el mensaje en él
			logLogicaNegocio.log(priority, StringUtil.cat(user, ": ", message));
		}
	}

	/**
	 * Log de errores
	 * 
	 * @param system
	 * @param errorType
	 * @param criticality
	 * @param errorCode
	 * @param message
	 */
	public static void logRE(String system, int errorType, int criticality, int errorCode, String message) {
		// Obtenemos el usuario actual
		String user = getUserName();
		
		// Llamamos al logger sin observaciones
		logRE(user, system, errorType, criticality, errorCode, message, null);
	}

	/**
	 * Log de errores
	 * 
	 * @param user
	 * @param sistema
	 * @param tipoError
	 * @param criticidad
	 * @param codigo
	 * @param mensaje
	 */
	public static void logRE(String user, String system, int errorType, int criticality, int errorCode, String message) {
		// Llamamos al logger sin observaciones
		logRE(user, system, errorType, criticality, errorCode, message, null);
	}

	/**
	 * Log de errores
	 * 
	 * @param user
	 * @param system
	 * @param errorType
	 * @param criticality
	 * @param errorCode
	 * @param message
	 * @param observations
	 */
	public static void logRE(String user, String system, int errorType, int criticality, int errorCode, String message, String observations) {
		// Obtenemos una instancia de Calendar
		Calendar calendar = GregorianCalendar.getInstance();
		
		// Inicializamos un StringBuffer con la línea de Log
		StringBuffer filaLog = new StringBuffer();

		// Añadimos el nombre de la App
		filaLog.append(_appName);
		// Añadimos el sistema en el que se ha producido el error
		filaLog.append("|" + system);
		
		// Añadimos el usuario (Si no se recibe un usuario se escribe UNKNOWN)
		filaLog.append("|" + (user != null ? user : "UNKNOWN"));
		
		// Escribimos la fecha actual
		filaLog.append("|" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(calendar.getTime()));
		
		// Escribimos la criticidad del error
		filaLog.append("|" + criticality);
		
		// Escribimos el tipo de error
		filaLog.append("|" + errorType);
		
		// Escribimos el código de error
		filaLog.append("|" + errorCode);
		
		// Añadimos el propio mensaje de error
		filaLog.append("|" + message);
		
		// Añadimos las observaciones (Si las hubiera)
		filaLog.append("|" + (observations != null ? observations : "") + "||\n");
		
		// Log en disco
		if (logError != null) {
			logError.error(filaLog);
		}

		// Tambien escribimos una linea de traza
		logRT(user, "ERROR: " + message);
	}
	
	/**
	 * Añadir al log de errores una excepción
	 * 
	 * @param system
	 * @param errorType
	 * @param criticality
	 * @param errorCode
	 * @param exception
	 */
	public static void logException(String system, int errorType, int criticality, int errorCode, Throwable exception) {
		Log.logRE(system, errorType, criticality, errorCode, "Exception: " + exception.getClass().getName() + "\nMessage: " + exception.getMessage() + "\nStackTrace: " + ExceptionUtils.getStackTrace(exception));
	}
	
	/**
	 * Obtiene el usuario actual desde el contenedor de la sesión
	 * 
	 * @return Nombre del usuario actual
	 */
	private static String getUserName() {
		UsrDTO userDto = GestorInformacionWeb.getSessionUsr();
		if(userDto != null) {
			return userDto.getUsr();
		} else {
			return "UNKNOWN";
		}
	}
}