/**
 * Inicializaciones básicas
 */
$(document).on('ready', function() {
	// Iniciar la actualización del reloj de la cabecera
	actualizaFecha();
	setInterval(actualizaFecha,1000);
	
	// Inicializar el menú principal
	$('#main-menu').tendina({
		speed: 200
	});
});

/**
 * Función para actualizar el reloj con la fecha y hora actuales
 * (Se ejecuta cada segundo)
 */
function actualizaFecha() {
	var fecha = new Date();
	var anio = fecha.getFullYear();
	var mes = fecha.getMonth() + 1;
	var dia = fecha.getDate();
	var hora = fecha.getHours();
	var minutos = fecha.getMinutes();
	var segundos = fecha.getSeconds();

	var ddMMyyyy = dia + "/" + mes + "/" + anio;
	var hhmmss = hora + ":" + ("00" + minutos).substring(("" + minutos).length) + ":" + ("00" + segundos).substring(("" + segundos).length);

	$("#td_fecha .inner").first().html(ddMMyyyy + ' - ' + hhmmss);
}

/**
 * Función para mostrar la pantalla de carga
 */
function loadingStart() {
	$("#loader").fadeIn();
}

/**
 * Función para ocultar la pantalla de carga
 */
function loadingStop() {
	$("#loader").fadeOut();
}

/**
 * Función para comprobar el estado del Applet RFID
 */
function checkRfid() {
	// Esto no se puede hacer por jQuery
	var applet = top.document.getElementById('appletRfid');
	
	try {
		if(navigator.javaEnabled() && typeof applet != 'undefined' && typeof applet.isActive === 'function' && applet.isActive()) {
			return true;
		} else {
			return false;
		}
	} catch(e) {
		console.log('Ha ocurrido una excepción al intentar detectar el estado del Applet RFID:');
		console.log(e);
		
		return false;
	}
}