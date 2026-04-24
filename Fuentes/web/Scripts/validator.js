// Utilidad para validar formularios de entrada

// Esta función valida si la cadena que se le pasa es un dni
function isNif(cadena) {
	if (cadena.length != 9) {
		return false;
	}

	var dni = cadena.substring(0, cadena.length - 1)
	var letra_introducida = cadena.charAt(cadena.length - 1)

	if (!isNaN(letra_introducida)) {
		return false
	} else {
		var letras = "TRWAGMYFPDXBNJZSQVHLCKET"
		var posicion = dni % 23
		var letra_calculada = letras.substring(posicion, posicion + 1)

		if (letra_calculada != letra_introducida.toUpperCase()) {
			return false
		}
	}
	return true;
}

// Esta función valida si la cadena que se le pasa es un email
function validateEmail(cadena) {
	var EXPRESION_EMAIL = /^([0-9a-zA-Z]([\-\.a-zA-Z\_0-9])*[0-9a-zA-Z])*@([0-9a-zA-Z][\-a-zA-Z\_0-9]*[0-9a-zA-Z]\.)+[a-zA-Z]{2,4}$/;

	return(EXPRESION_EMAIL.test(cadena));
}

// Esta función valida si la cadena que se le pasa es un número de teléfono
function validateNumTel(cadena) {
	var primeraPos = cadena.substring(0,1);
	var numeros = cadena.substring(1,cadena.length);
	
	if (!isNaN(cadena)) return true;
	else {
		if(isNaN(primeraPos) && primeraPos == '+'){	
			if (!isNaN(numeros)) return true;
			else return false;
		} else return false;
	}
}

// Elimina los espacios al inicio de una cadena
function leftTrim(sString) {
	while (sString.substring(0, 1) == ' ') {
		sString = sString.substring(1, sString.length);
	}
	return sString;
}

// Elimina los espacios al final de una cadena
function rightTrim(sString) {
	while (sString.substring(sString.length - 1, sString.length) == ' ') {
		sString = sString.substring(0, sString.length - 1);
	}
	return sString;
}

// Elimina los espacios al inicio y al final de una cadena
function trim(sString) {
	sString = leftTrim(sString);
	sString = rightTrim(sString);
	return sString;
}

// Formateo de enteros
function formatearEntero(sDato) {
	if ((trim(sDato) == "") || (trim(sDato) == null)) {
		return null;
	} else {
		sDato = trim(sDato);
		var iDato = null;

		if (!isNaN(sDato)) {
			iDato = parseInt(sDato);
		}
		return iDato;
	}
}

// Para comprobar si un campo está vacío, util en los campos obligatorios.
function esVacio(cadena) {
	if (noVacio(cadena)) {
		return false
	} else {
		return true
	}

}

// Busca caracteres que no sean espacio en blanco en una cadena
function noVacio(q) {
	for (i = 0; i < q.length; i++) {
		if (q.charAt(i) != " ") {
			return true
		}
	}
	return false
}

// Formateo de fechas
function formatearFecha(fecha) {
	if (trim(fecha) == '') {
		return '';
	}
	var ddmmyyyy = trim(fecha).split('/');
	var d = 0;
	var m = 0;
	var y = 0;
	var anho;

	if (ddmmyyyy.length == 1) {
		// dmmyy o dmmyyy
		if ((ddmmyyyy[0].length == 5) || (ddmmyyyy[0].length == 7)) {
			d = (ddmmyyyy[0].substring(0, 1)) * 1;
			m = (ddmmyyyy[0].substring(1, 3)) * 1;
			anho = ddmmyyyy[0].substring(3);

			if (anho.length == 2) {
				anho = '20' + anho;
			}

			if (anho.length != 4) {
				return null;
			}

			y = (anho) * 1;

		} else { // ddmmyy o ddmmyyyy
			d = (ddmmyyyy[0].substring(0, 2)) * 1;
			m = (ddmmyyyy[0].substring(2, 4)) * 1;
			anho = ddmmyyyy[0].substring(4);

			if (anho.length == 2) {
				anho = '20' + anho;
			}

			if (anho.length != 4) {
				return null;
			}

			y = (anho) * 1;

		}
	} else if (ddmmyyyy.length == 3) {
		d = (ddmmyyyy[0]) * 1;
		m = (ddmmyyyy[1]) * 1;
		anho = ddmmyyyy[2];

		if (anho.length == 2) {
			anho = '20' + anho;
		}

		if (anho.length != 4) {
			return null;
		}

		y = (anho) * 1;

	} else {
		return null;
	}

	var dateValue = fecha;
	if (d > 0 && m > 0 && y > 0) {
		var date = new Date(y, m - 1, d);

		if ((d == date.getDate()) && (m - 1 == date.getMonth()) && (y == date.getFullYear())) {
			dateValue = ('00' + d).substring(('' + d).length) + '/'
					+ ('00' + m).substring(('' + m).length) + '/'
					+ ('0000' + y).substring(('' + y).length);
			return dateValue;

		} else {
			return null;
		}
	} else {
		return null;
	}
}

// Función que compara fechas. Devuelve true si fecha>fecha2 y false en caso contrario
function mayor(fecha, fecha2) {
	if ((fecha2 == "") || (fecha2 == null)) {
		return false;
	}

	var xDia = fecha.substring(0, 2);
	var xMes = fecha.substring(3, 5);
	var xAnio = fecha.substring(6, 10);
	var yDia = fecha2.substring(0, 2);
	var yMes = fecha2.substring(3, 5);
	var yAnio = fecha2.substring(6, 10);

	if (xAnio > yAnio) {
		return (true);
	} else if (xAnio == yAnio) {
		if (xMes > yMes) {
			return (true);
		} else {
			if (xMes == yMes) {
				if (xDia > yDia) {
					return (true);
				} else {
					return (false);
				}
			} else {
				return (false);
			}
		}
	} else {
		return (false);
	}
}
