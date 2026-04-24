/*
 * Created on 24-ago-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package es.sdweb.application.componentes.util;

/**
 * @author infacm00
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Booleano {
	private boolean bool;
	
	public Booleano(boolean b){
		bool=b;
	}

	/**
	 * @return
	 */
	public boolean isBool() {
		return bool;
	}

	/**
	 * @param b
	 */
	public void setBool(boolean b) {
		bool = b;
	}

        @Override
        public String toString(){
            return ""+bool;
        }
}//class
