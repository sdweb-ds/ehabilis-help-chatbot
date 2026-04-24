/*
 * Created on 07-dic-2003
 *
 */
package es.sdweb.application.vista.util;

import java.util.List;
import java.util.ResourceBundle;

import es.sdweb.application.model.dto.ElementoDetallesDTO;
import es.sdweb.application.model.dto.ElementoDetallesDTOArbol;

/**
 * @author Antonio Carro Mariño
 *
 */
public class ElementosPerfil {

	private static ElementosPerfil gestFeudoWebElementos;

	static {
		gestFeudoWebElementos = new ElementosPerfil(); // creamos una unica instancia
	}

	private ElementosPerfil() {
	}

	public static ElementosPerfil getInstance() {
		return gestFeudoWebElementos;
	}

	private String contenidoTablaArbol(ElementoDetallesDTOArbol elementoDetallesDTOArbol, int nivel, String pathBase, ResourceBundle properties) {
		ElementoDetallesDTO feudo = (elementoDetallesDTOArbol.getElementoDetallesDTO());
		String codPadre = feudo.getCodPadre();
		String chequeado = elementoDetallesDTOArbol.getSeleccionado();
		List hijos = elementoDetallesDTOArbol.getSubArbol();
		String result;

		if (!hijos.isEmpty()) {
			result = "<li id='" + feudo.getUbicacion() + "' class='menugrande abrir-elemento'>";
		} else {
			result = "<li id='" + feudo.getUbicacion() + "' class='menugrande abrir-elemento no-childs'>";
		}

		if (chequeado.compareTo("true") == 0) {
			result += "<input type=\"checkbox\" id=\"" + feudo.getCodElemento() + "\" name=\"" + feudo.getCodElemento() + "\" value=\"ON\" checked />  ";
		} else {
			result += "<input type=\"checkbox\" id=\"" + feudo.getCodElemento() + "\" name=\"" + feudo.getCodElemento() + "\" value=\"ON\" />  ";
		}

		result += "<input type='hidden' id='codigo_" + feudo.getUbicacion() + "' value='" + feudo.getUbicacion() + "' />"
				+ "<input type='hidden' id='padre_" + feudo.getUbicacion() + "' value='" + codPadre + "' />"
				+ "<input type='hidden' id='tiene_hijos_" + feudo.getUbicacion() + "' value='" + (hijos.size() > 0 ? "S" : "N") + "' />";

		result += "<a href='#'>" + properties.getString(feudo.getNombre()) + "</a>";

		if(hijos.size() > 0) {
			result += "<ul class='menu3 elementos-perfil-child' style='padding-left: 25px'>";

			for (int i = 0; i < hijos.size(); i++) {
				result += contenidoTablaArbol((ElementoDetallesDTOArbol) (hijos.get(i)), nivel + 1, pathBase, properties);
			}

			result += "</ul>";
		}

		result += "</li>";

		return result;
	}

	public String getArbolFeudos(String pathBase, List elementosArbol, ResourceBundle properties) {
		String result = "<ul class='menu elementos-perfil-main'>";

		for (int i = 0; i < elementosArbol.size(); i++) {
			result += contenidoTablaArbol((ElementoDetallesDTOArbol) (elementosArbol.get(i)), 0, pathBase, properties); // Componemos el HTML
		}

		result += "</ul>";

		return result;
	}

	private String contenidoTablaArbolOculto(ElementoDetallesDTOArbol elementoDetallesDTOArbol, int nivel) {
		// El nombre del feudo y sus datos iran en una tabla con un padding dependiendo del nivel
		ElementoDetallesDTO feudo = (elementoDetallesDTOArbol.getElementoDetallesDTO());

		String chequeado = elementoDetallesDTOArbol.getSeleccionado();
		List hijos = elementoDetallesDTOArbol.getSubArbol();
		String result;
		if (!hijos.isEmpty()) {
			result = "<input type=\"hidden\" id=" + feudo.getCodElemento() + " name=" + feudo.getCodElemento() + " value=\"" + chequeado + "\" />";
		} else {
			result = "<input type=\"hidden\" id=" + feudo.getCodElemento() + " name=" + feudo.getCodElemento() + " value=\"" + chequeado + "\" />";
		}

		for (int i = 0; i < hijos.size(); i++) {
			result += contenidoTablaArbolOculto((ElementoDetallesDTOArbol) (hijos.get(i)), nivel + 1);
		}

		return result;
	}

	public String getArbolFeudosOcultos(List elementosArbol) {
		String result = "";

		for (int i = 0; i < elementosArbol.size(); i++) {
			result += contenidoTablaArbolOculto((ElementoDetallesDTOArbol) (elementosArbol.get(i)), 0); // Componemos el HTML
		}

		return result;
	}

}// class
