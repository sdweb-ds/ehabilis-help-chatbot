package es.sdweb.application.controller.config;

public interface IConstantes {

	// Configuraciones generales
	public final String URL_FICHERO_PROPIEDADES = "i18n.properties";

	public final String CFG_LOG4J_KEY = "log4jconfig";

	public final String CFG_LOG_ACCESODATOS = "loggerAccesoDatos";
	public final String CFG_LOG_LOGICANEGOCIO = "loggerLogicaNegocio";
	public final String CFG_LOG_ERRORES = "loggerErrores";

	// Constantes que se utilizaran en JavaScript para señalizar elementos
	// nuevos, borrados o actualizados.
	// Tambien son usadas en la capa vista.
	public final String CFG_JAVASCRIPT_NEW_ITEM_KEY = "new";
	public final String CFG_JAVASCRIPT_UPDATE_ITEM_KEY = "update";
	public final String CFG_JAVASCRIPT_DELETE_ITEM_KEY = "delete";
	public final String CFG_JAVASCRIPT_IGNORE_ITEM_KEY = "ignore";

	public final String CFG_JAVASCRIPT_ESPACIO_EMPTY_ITEM_FIELD = "?"; // Los campos vacios del Mapa de un Espacio se indicaran con "?"
	public final String CFG_JAVASCRIPT_ESPACIO_SEPARADOR_ITEM_FIELD = "_"; // Los separadores de campos del Mapa de un Espacio se indicaran con "_".
																				  // Ejemplo: "0_0_1_2_0_?_5/0_1_1_2_0_?_?/..."

	public final int CFG_JAVASCRIPT_TIEMPO_INTERVALO_ACTUALIZACION = 10000;

	// Contenedores
	public final String APP_APPLICATION_CONTAINER = "appCon";
	public final String SES_SESSION_CONTAINER = "sessCon";// dispatchaction

	// Action Forwards
	public final String FWD_GLOBAL_ERROR = "error"; // si
	public final String FWD_GLOBAL_ERROR_BD = "errorBD"; // si
	public final String FWD_GLOBAL_NOLOGIN = "nologin";// SI USADO
	public final String FWD_GLOBAL_ACCESODENEGADO = "accesoDenegado";
	public final String FWD_GLOBAL_ERROR_WEB = "errorWeb";

	// Codigos de los elementos a los que se limita el acceso
	public final String ACCESO_MENU_GESTION_USUARIOS = "M.0";
	public final String ACCESO_MENU_USUARIO = "M.0.1";
	public final String ACCESO_MENU_USUARIO_BUSCAR = "M.0.1.1";
	public final String ACCESO_MENU_USUARIO_CREAR = "M.0.1.2";
	public final String ACCESO_MENU_PERFIL = "M.0.2";
	public final String ACCESO_MENU_PERFIL_BUSCAR = "M.0.2.1";
	public final String ACCESO_MENU_PERFIL_CREAR = "M.0.2.2";

	public final String ACCESO_MENU_MANTENIMIENTO = "M.1";
	public final String ACCESO_MENU_MANTENIMIENTO_MAESTROS = "M.1.11";
	public final String ACCESO_MENU_MANTENIMIENTO_CONSTANTES = "M.1.2";


	// Expresiones regulares de: D.N.I, N.I.E, e-mails,...
	public final String EXPRESION_EMAIL = "^([0-9a-zA-Z]([-.a-zA-Z_0-9])*[0-9a-zA-Z])*@([0-9a-zA-Z][-a-zA-Z_0-9]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,4}$";
	public final String EXPRESION_NIF_NIE = "(X[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9][a-zA-Z])|(0?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[0-9]?[a-zA-Z])";

	// Constantes que se utilizaran para recuperar constantes de la Base de Datos
	public final String CONSTANTE_BUTACA = "IDENTIFICADOR.TIPO.BUTACA"; // Utilizado para conocer que Item es una Butaca
	public final String CONSTANTE_EDITAR = "IDENTIFICADOR.TIPO.EDITAR"; // Utilizado para conocer que Item es una Label/Editar
	public final String CONSTANTE_PASILLO = "IDENTIFICADOR.TIPO.PASILLO"; // Utilizado para conocer que Item es un Pasillo Vertical
	public final String CONSTANTE_ESPACIO_ANCHURA_DEFECTO = "NUEVO.ESPACIO.ANCHURA.DEFECTO"; // Anchura de un nuevo espacio por defecto
	public final String CONSTANTE_ESPACIO_ALTURA_DEFECTO = "NUEVO.ESPACIO.ALTURA.DEFECTO"; // Altura de un nuevo espacio por defecto
	public final String CONSTANTE_IMPUESTOS_DEFECTO = "IMPUESTOS.DEFECTO"; // IVA aplicable a los precios
	public final String CONSTANTE_FORMA_PAGO_TARJETA_ABONADO = "IDENTIFICADOR.FORMA.PAGO.TARJETA.ABONADO"; // ID de la forma de pago "Tarjeta de abonado"
	public final String CONSTANTE_FORMA_PAGO_ABONO = "IDENTIFICADOR.FORMA.PAGO.ABONO"; //ID de la forma de pago "Abono"
	public final String CONSTANTE_NUMERO_DIAS_LIMITE_RESERVA_ABONADOS = "LIMITE.RESERVA.ABONADOS.NUMERO.DIAS"; // Numero de dias limite antes de la funcion,
																													   //a partir del cual las butacas de los abonados pasan a la venta.
	public final String CONSTANTE_NUMERO_DIAS_LIMITE_RESERVA = "LIMITE.RESERVA.NUMERO.DIAS.GENERAL"; // Numero de dias limite antes de la funcion, a partir del cual no se admitiran reservas
																										   // a todos los usuarios en general.
	public final String CONSTANTE_NUMERO_HORAS_LIMITE_RESERVA = "LIMITE.RESERVA.NUMERO.HORAS"; // Numero de horas que se bloqueara una reserva antes de ser liberada por el sistema.
	public final String CONSTANTE_ESPACIO_COLOR_FONDO_BUTACAS_VACIAS = "COLOR.FONDO.BUTACAS.VACIAS"; // COLOR DE FONDO DE LAS BUTACAS SIN OCUPAR.
	public final String CONSTANTE_ESPACIO_COLOR_CELDA_VACIA = "COLOR.CELDA.VACIA"; // COLOR DE FONDO DE LAS CELDAS VACIAS DE UN ESPACIO (CELDAS SIN ITEM).
	public final String CONSTANTE_PUERTO_LECTOR_RFID = "PUERTO.LECTOR.RFID"; // PUERTO PARA EL LECTOR RFID
	public final String CONSTANTE_MODO_LECTOR_RFID = "MODO.LECTOR.RFID"; // PUERTO PARA EL LECTOR RFID
	public final String CONSTANTE_COLOR_BUTACA_BLOQUEADA_ABONADOS = "COLOR.BUTACA.BLOQUEADA.ABONADOS"; // COLOR DE FONDO DE LAS BUTACAS BLOQUEADAS PARA ABONADOS EN UNA FUNCION.
	public final String CONSTANTE_COMISION_BANCARIA = "COMISION.BANCARIA";
	public final String CONSTANTE_TIEMPO_BLOQUEO_BUTACAS = "TIEMPO.BLOQUEO.BUTACAS";
	public final String CONSTANTE_DURACION_SESION_COMPRA = "DURACION.SESION.COMPRA";
	public final String CONSTANTE_MODO_DEBUG = "MODO.DEBUG";
	public final String CONSTANTE_CAJON_RUTA_JPOS_XML = "CAJON.RUTA.JPOS.XML";
	public final String CONSTANTE_CAJON_ENTRY_NAME = "CAJON.ENTRY.NAME";
	public final String CONSTANTE_ESPACIO_ASIGNACION_ABONADOS = "ESPACIO.ASIGNACION.ABONADOS";
	public final String CONSTANTE_HORAS_VENTA_LIBRE_ABONADOS = "HORAS.VENTA.LIBRE.ABONADOS";
	public final String CONSTANTE_COLOR_VENTA_LIBRE_ABONADOS = "COLOR.VENTA.LIBRE.ABONADOS";

	//App tiqueo.
	public final String CONSTANTE_APP_TIQUEO_DIAS = "APP.TIQUEO.DIAS";

	// Nombres de impresoras
	public final String CONSTANTE_NOMBRE_IMPRESORA_ENTRADAS = "NOMBRE.IMPRESORA.ENTRADAS";
	public final String CONSTANTE_NOMBRE_IMPRESORA_RECIBOS = "NOMBRE.IMPRESORA.RECIBOS";
	public final String CONSTANTE_NOMBRE_IMPRESORA_CAJA = "NOMBRE.IMPRESORA.CAJA";

	public final String LOGIN_USUARIO_PORTAL = "Servicio Web";

	public final String USUARIO_BUSQUEDA_OPERACIONES_PORTAL = "PORTAL WEB";
	public final String USUARIO_BUSQUEDA_OPERACIONES_TODOS = "TODOS";

	public final String CONSTANTE_ESPACIO_COLOR_VENDIDA_PORTAL = "COLOR.VENDIDA.PORTAL"; // COLOR DE FONDO DE LAS CELDAS VENDIDAS EN EL MAPA DEL PORTAL

	public final String CLAVE_SECRETA_SEGURIDAD = "i*Uf#a]&ONnm";

	public final int PERIODO_RESERVA_ABONADOS = 1;
	public final int PERIODO_VENTA_LIBRE_ABONADOS = 2;
	public final int PERIODO_NORMAL = 3;

	public final String TIPO_OPERACION_CAJA_DEVOLUCION_TARJETA = "1";
	public final String TIPO_OPERACION_CAJA_VENTA = "2";
	public final String TIPO_OPERACION_CAJA_CANCELACION = "3";
	public final String TIPO_OPERACION_CAJA_RECARGA = "4";
	public final String TIPO_OPERACION_CAJA_COPIA_TARJETA = "6";
	public final String TIPO_OPERACION_CAJA_VENTA_ABONO = "7";
	public final String TIPO_OPERACION_CAJA_CANCELACION_ABONO = "8";

	public final String FORMA_PAGO_TARJETA_ABONADO = "1";
	public final String FORMA_PAGO_ABONO = "5";

	public final String ID_TIPO_LABEL = "14";

	// Max 'nombre_apellidos_reserva' column length
	public final int MAX_LENGTH_LOCALIDAD_RESERVA_APELLIDOS_NOMBRE = 250;
	public final int MAX_LENGTH_LOCALIDAD_RESERVA_TELEFONO = 250;

	// Max 'operacion de caja description' column length
	public final int MAX_OP_CAJA_DESCRIPTION = 250;


}
