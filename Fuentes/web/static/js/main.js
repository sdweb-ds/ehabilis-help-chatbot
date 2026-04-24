$(document).on('ready', function() {
	// Si se carga el JS para el eModal
	if (typeof eModal != 'undefined') {
		eModal.setEModalOptions({
			// TODO: Traducir esto
			loadingHtml: '<h5>Cargando...</h5><div class=progress><div class="progress-bar progress-bar-striped active" style="width: 100%"></div></div>'
		});
	}
});

/**
 * Función para obtener una fecha en formato "d/m/Y H:i:s"
 */
function getDateString(timeOnly) {
	if (typeof(timeOnly) === 'undefined') {
		timeOnly = false;
	}

	// Obtenemos la fecha actual
	var date = new Date();

	// Obtenemos todas las partes de la fecha
	var hours = (date.getHours() >= 10) ? date.getHours(): '0' + date.getHours();
	var minutes = (date.getMinutes() >= 10) ? date.getMinutes(): '0' + date.getMinutes();
	var seconds = (date.getSeconds() >= 10) ? date.getSeconds(): '0' + date.getSeconds();

	// Montamos la cadena final de la fecha
	if (!timeOnly) {
		var day = (date.getDate() >= 10) ? date.getDate(): '0' + date.getDate();
		var month = (date.getMonth() + 1 >= 10) ? (date.getMonth() + 1): '0' + (date.getMonth() + 1);

		return day + '/' + month + '/' + date.getFullYear() + ' ' + hours + ':' + minutes + ':' + seconds;
	} else {
		return hours + ':' + minutes + ':' + seconds;
	}
}