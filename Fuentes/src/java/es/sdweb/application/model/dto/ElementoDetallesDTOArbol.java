package es.sdweb.application.model.dto;

import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;



/**
 * @author Antonio Carro Mariño
 *
 *  La presencia de dos constructores distintos se debe a que este Custom Value Object es utilizado
 *  en dos situaciones distintas:
 *  	- Por una lado es utilizado para generar los botones del menu a los que el usuario en sesion
 *  tiene acceso, en este caso el atributo "seleccionado" no es de utilidad ya que solo se forma en
 *  arbol con aquellos elementos a los que el usuario tiene acceso.
 *  	- En cambio cuando se genera el arbol entero de los elementos que hay en la B.D., usado para
 *  asociar/desasociar elementos a un perfil en la creacion o modificacion de este ultimo, gana importancia
 *  ya que sera indicativo de la asociacion entre los dos objetos mencionados.
 *
 */
public class ElementoDetallesDTOArbol {

	private ElementoDetallesDTO elementoDetallesDTO;
	private String seleccionado="off";
	private List subArbol;



	public ElementoDetallesDTOArbol(ElementoDetallesDTO elementoDetallesDTO, List subArbol) {
		super();
		// TODO Auto-generated constructor stub
		this.elementoDetallesDTO = elementoDetallesDTO;
		this.subArbol = subArbol;
	}



	public ElementoDetallesDTOArbol(ElementoDetallesDTO elementoDetallesDTO, String seleccionado, List subArbol) {
		super();
		// TODO Auto-generated constructor stub
		this.elementoDetallesDTO = elementoDetallesDTO;
		this.seleccionado = seleccionado;
		this.subArbol = subArbol;
	}



	public String getSeleccionado() {
		return seleccionado;
	}



	public void setSeleccionado(String seleccionado) {
		this.seleccionado = seleccionado;
	}



	public ElementoDetallesDTO getElementoDetallesDTO() {
		return elementoDetallesDTO;
	}

	public void setElementoDetallesDTO(ElementoDetallesDTO elementoDetallesDTO) {
		this.elementoDetallesDTO = elementoDetallesDTO;
	}

	public List getSubArbol() {
		return subArbol;
	}

	public void setSubArbol(List subArbol) {
		this.subArbol = subArbol;
	}

	@Override
	public String toString(){
	    return ReflectionToStringBuilder.toString(this);
	  }

	  public int compareTo(Object remoteObject){
	    return CompareToBuilder.reflectionCompare(this,remoteObject);
	  }

	  @Override
	public boolean equals(Object remoteObject){
	    return EqualsBuilder.reflectionEquals(this, remoteObject);
	  }

	  @Override
	public int hashCode(){
	    return HashCodeBuilder.reflectionHashCode(this);
	  }
}
