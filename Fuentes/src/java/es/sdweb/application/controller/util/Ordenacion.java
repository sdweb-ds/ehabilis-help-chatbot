package es.sdweb.application.controller.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio Carro Mariño
 *
 */
public abstract class Ordenacion {


	/**
	 * Recibe una lista ordenada descendentemente o ascendentemente y 
	 * devuelve dicha lista en el orden inverso.
	 * @param ordenada 
	 * @return la lista en orden inverso.
	 */
	public static List cambiarOrden (List ordenada) {
	
		List auxiliar = new ArrayList();
		
		for (int u=ordenada.size()-1;u>-1;u--) {
			auxiliar.add(ordenada.get(u));
			
		}
		return auxiliar;
	}

}//class
