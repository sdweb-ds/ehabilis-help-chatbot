package es.sdweb.application.model.dal.facade;

import es.sdweb.application.componentes.util.ObjectUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.util.ExceptionFaltaParametro;
import es.sdweb.application.util.GestorParametrosConfiguracion;

/**
 * Clase encargada de crear los DAOs a partir de los parametros de configuracion.
 *
 * TODO: Si todos los daos compartieran una interfaz padre se podrái simplificar toda esta clase a una sola función
 */
public abstract class FabricaDAOs {

	private final static String PREFIJO_PARAMETRO_CONFIGURACION_DAO = FabricaDAOs.class.getPackage().getName() + ".";
	private final static String SUBFIJO_PARAMETRO_CONFIGURACION_DAO = ".className";

	// Gestion de usuarios:
	private final static String USUARIO_DAO = "UsuarioDAO";
	private final static String ELEMENTO_DAO = "ElementoDAO";
	private final static String PERFIL_DAO = "PerfilDAO";
	private final static String PERFIL_USUARIO_DAO = "PerfilUsuarioDAO";

	// Gestion de maestros:
	private final static String CONSTANTE_DAO = "ConstanteDAO";
	private final static String TIPO_OPERACION_CAJA_DAO = "TipoOperacionCajaDAO";
	private final static String FORMA_PAGO_DAO = "FormaPagoDAO";
	private final static String UBICACION_DAO = "UbicacionDAO";
	private final static String ESPACIO_DAO = "EspacioDAO";
	private final static String TARIFA_DAO = "TarifaDAO";
	private final static String TARIFA_RANGO_TEMPORAL_DAO = "TarifaRangoTemporalDAO";
	private final static String EVENTO_DAO = "EventoDAO";
	private final static String CIUDADANO_DAO = "CiudadanoDAO";
	private final static String LOCALIDAD_DAO = "LocalidadDAO";
	private final static String CONJUNTO_TARIFAS_DAO = "ConjuntoTarifasDAO";
	private final static String ITEM_DAO = "ItemDAO";
	private final static String FUNCION_DAO = "FuncionDAO";
	private final static String TIPO_ITEM_DAO = "TipoItemDAO";
	private final static String DIRIGIDO_A_DAO = "DirigidoADAO";
	private final static String GENERO_DAO = "GeneroDAO";
	private final static String OPERACION_CAJA_DAO = "OperacionCajaDAO";
	private final static String PROGRAMA_DAO = "ProgramaDAO";

	private final static String TIPO_ABONO_DAO = "TipoAbonoDAO";
	private final static String ABONO_DAO = "AbonoDAO";
	private final static String ABONO_ITEM_DAO = "AbonoItemDAO";
	private final static String TARIFA_TIPO_ABONO_DAO = "TarifaTipoAbonoDAO";
	private final static String ABONO_OPERACION_CAJA_DAO = "AbonoOperacionCajaDAO";

	// Bloqueo de localidades
	private final static String BLOQUEO_LOCALIDADES_DAO = "BloqueoLocalidadesDAO";

	// Log
	private final static String LOG_DAO = "LogDAO";

	/**
	 * Esta funcion crea un objeto DAO a partir de su nombre simple, p.e.
	 * "ElementoDAO", sin calificativos de paquete, etc. Las clases DAO deberan
	 * estar ubicadas en un subpaquete ".dao" respecto a la ubicacion de esta
	 * clase.
	 *
	 * @param daoName
	 *            Nombre del DAO sin calificativos de paquete, p.e. "ElementoDAO".
	 *
	 * @return El objeto DAO creado.
	 * @throws ExceptionFaltaParametro
	 */
	private static Object createDao(String daoName) throws ExceptionErrorInterno {
		Object result = null;

		// Obtenemos el nombre del parámetro
		String paramName = PREFIJO_PARAMETRO_CONFIGURACION_DAO + daoName + SUBFIJO_PARAMETRO_CONFIGURACION_DAO;

		try {
			// Obtenemos la clase
			String daoClassName = GestorParametrosConfiguracion.getParametro(paramName, true);
			// Obtenemos una instancia de la clase
			result = ObjectUtil.createInstance(daoClassName);
		} catch (ExceptionFaltaParametro ex) {
			Log.logException("FabricaDAOs.createDao()", Log.TIPO_APLICACION, Log.CRITICIDAD_CRITICAL, -1, ex);
			throw new ExceptionErrorInterno(ex, -1, "ERROR: En la creacion del DAO de nombre " + paramName);
		}

		return result;
	}

	/**
	 * Devuelve una instancia del DAO de la relación Usuarios - Perfil de Usuarios
	 *
	 * @return Una instancia del DAO de la relación Usuarios - Perfil de Usuarios
	 * @throws ExceptionErrorInterno
	 */
	public static IPerfilUsuarioDAO createPerfilUsuarioDAO() throws ExceptionErrorInterno {
		return (IPerfilUsuarioDAO) createDao(PERFIL_USUARIO_DAO);
	}

	/**
	 * Devuelve una instancia del DAO de Usuarios
	 *
	 * @return Una instancia del DAO de Usuarios
	 * @throws ExceptionErrorInterno
	 */
	public static IUsuarioDAO createUsuarioDAO() throws ExceptionErrorInterno {
		return (IUsuarioDAO) createDao(USUARIO_DAO);
	}

	/**
	 * Devuelve una instancia del DAO de Perfil de Usuarios
	 *
	 * @return Una instancia del DAO de Perfil de Usuarios
	 * @throws ExceptionErrorInterno
	 */
	public static IPerfilDAO createPerfilDAO() throws ExceptionErrorInterno {
		return (IPerfilDAO) createDao(PERFIL_DAO);
	}

	/**
	 * Devuelve una instancia del DAO de Elementos (de menú)
	 *
	 * @return Una instancia del DAO de Elementos
	 * @throws ExceptionErrorInterno
	 */
	public static IElementoDAO createElementoDAO() throws ExceptionErrorInterno {
		return (IElementoDAO) createDao(ELEMENTO_DAO);
	}

	/**
	 * Devuelve una instancia del DAO de Constantes
	 *
	 * @return Una instancia del DAO de Constantes
	 * @throws ExceptionErrorInterno
	 */
	public static IConstanteDAO createConstanteDAO() throws ExceptionErrorInterno {
		return (IConstanteDAO) createDao(CONSTANTE_DAO);
	}

	/**
	 * Devuelve una instancia del DAO de Log
	 *
	 * @return Una instancia del DAO de Log
	 * @throws ExceptionErrorInterno
	 */
	public static ILogDAO createLogDAO() throws ExceptionErrorInterno {
		return (ILogDAO) createDao(LOG_DAO);
	}
}

