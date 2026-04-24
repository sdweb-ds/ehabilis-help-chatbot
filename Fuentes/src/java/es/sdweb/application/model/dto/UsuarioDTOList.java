package es.sdweb.application.model.dto;

import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;



/**
 * @author Antonio Carro Mariño
 *  
 */
public class UsuarioDTOList {

	private List usuarioDTOs;
	private boolean existMoreUsuarioDTO;
	
	public UsuarioDTOList(List usuarioDTOs, boolean existMoreUsuarioDTO) {
		super();
		// TODO Auto-generated constructor stub
		this.usuarioDTOs = usuarioDTOs;
		this.existMoreUsuarioDTO = existMoreUsuarioDTO;
	}

	public List getUsuarioDTOs() {
		return usuarioDTOs;
	}

	public boolean isExistMoreUsuarioDTO() {
		return existMoreUsuarioDTO;
	}
	

	public String toString(){
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
}
