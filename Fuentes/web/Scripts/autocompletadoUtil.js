//Convierte un array JSON en un array asociativo  con clave el nombre de campo y valor la lista de todos los valores del mismo
function parse(lista) {
	var listados = {};
    var object = lista[0];

    //Recorremos la lista de campos
    for (var property in object) {
	   	var aux = [];
	   	//Recorremos la lista de objetos y guardamos los valores de cada campo en un array
   		for (var i = 0; i < lista.length; i++) {
   			object = lista[i];
   			var valor = object[property];
   			//Si encontramos un campo no definido lo ponemos como cadena vacía
  	   		if (typeof valor === "undefined") {
	  	   	    valor = "";
	  	   	}
	   		aux.push(valor);		
   		}
	   	//Guardamos en cada campo su lista de valores
   		listados[property] = aux;
    }
    return listados;
}

//Añade un widget de autocompletado al objeto DOM especificado. 
//Lista completa de opciones en https://www.devbridge.com/sourcery/components/jquery-autocomplete/#jquery-autocomplete-api
function inicializarAutocompletado(objDOM, vLookup, opciones) {
	$(objDOM).devbridgeAutocomplete({
        lookup: vLookup,
        minChars: opciones.minChars,
        lookupLimit: opciones.lookupLimit,
        showNoSuggestionNotice: opciones.showNoSuggestionsNotice,
        noSuggestionNotice: opciones.noSuggestionsNotice,
        //Filtramos los resultados de modo que solo salgan los que empiecen por la cadena introducida.
        lookupFilter: opciones.lookupFilter,
    });
}
