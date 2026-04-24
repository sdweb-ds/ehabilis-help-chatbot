package es.sdweb.application.vista.util;

import java.util.List;
import java.util.Locale;

import es.sdweb.application.controller.util.Translator;
import es.sdweb.application.model.dto.ElementoDetallesDTO;
import es.sdweb.application.model.dto.ElementoDetallesDTOArbol;

/**
 * @author Antonio Carro Mariño
 *
 */

public class ElementosMenu {

    private static ElementosMenu elementos;

    static {
        elementos = new ElementosMenu(); // creamos una unica instancia
    }

    private ElementosMenu() {
    }

    public static ElementosMenu getInstance() {
        return elementos;
    }

    /**
     *
     * @param feudo
     * @param nivel
     * @return
     */

    private String contenidoTablaArbol(ElementoDetallesDTOArbol elementoDetallesDTOArbol, int nivel, String pathBase, Locale locale) {
        ElementoDetallesDTO feudo = (elementoDetallesDTOArbol.getElementoDetallesDTO());
        List hijos = elementoDetallesDTOArbol.getSubArbol();
        String result = "";

        result += "<li>";

        if(hijos.isEmpty()) {
        	result += "<a href='" + pathBase + feudo.getUrl() + "' target='principal' class='menu-link'>";
        } else {
        	result += "<a href='#'>";
        }

        if(locale != null)
        	result +=  Translator.getTranslation(locale, feudo.getNombre()) + "</a>";
        else
        	result +=  Translator.getTranslation(feudo.getNombre()) + "</a>";

        if (!hijos.isEmpty()) {
        	result += "<ul class='menu3'>";
            for (int i = 0; i < hijos.size(); i++) {
                result += contenidoTablaArbol((ElementoDetallesDTOArbol) (hijos.get(i)), nivel + 1, pathBase, locale);
            }
            result += "</ul>";
        }

        result += "</li>";

        return result;
    }

    public String getArbolMenu(String pathBase, List elementosArbol) {
        String result = "<ul id='main-menu' class='menu'>";

        for (int i = 0; i < elementosArbol.size(); i++) {
            result += contenidoTablaArbol((ElementoDetallesDTOArbol) (elementosArbol.get(i)), 0, pathBase, null); // componemos el HTML
        }

        result += "</ul>";

        return result;
    }


    public String getArbolMenu(String pathBase, List elementosArbol, Locale locale) {
    	String result = "<ul id='main-menu' class='menu'>";

        for (int i = 0; i < elementosArbol.size(); i++) {
            result += contenidoTablaArbol((ElementoDetallesDTOArbol) (elementosArbol.get(i)), 0, pathBase, locale); // componemos el HTML
        }

        result += "</ul>";

        return result;
    }
}// class
