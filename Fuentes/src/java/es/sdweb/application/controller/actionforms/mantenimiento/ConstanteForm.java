package es.sdweb.application.controller.actionforms.mantenimiento;


import org.apache.struts.action.ActionForm;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import java.util.Collection;


/**
 * 
 * @author Antonio Carro Mariño
 *
 */


public class ConstanteForm extends ActionForm {
	
	

	private String advertencia="";
	private Collection constantes = null;
	



public String getAdvertencia() {
		return advertencia;
	}

public void setAdvertencia(String advertencia) {
		this.advertencia = advertencia;
	}

public Collection getConstantes() {
		return constantes;
	}

	public void setConstantes(Collection constantes) {
		this.constantes = constantes;
	}

public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

  public int compareTo(Object remoteObject){
    return CompareToBuilder.reflectionCompare(this,remoteObject);
  }

  public boolean equals(Object remoteObject){
    return EqualsBuilder.reflectionEquals(this, remoteObject);
  }

  public int hashCode(){
    return HashCodeBuilder.reflectionHashCode(this);
  }

}//class
