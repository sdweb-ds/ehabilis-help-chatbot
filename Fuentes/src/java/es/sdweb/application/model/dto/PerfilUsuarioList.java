package es.sdweb.application.model.dto;

import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import es.sdweb.application.model.dto.PerfilDTO;



/**
 * @author Antonio Carro Mariño
 *
 */
public class PerfilUsuarioList {

	private PerfilDTO perfilDTO;
	List usuarioDTOs;

	public PerfilUsuarioList(PerfilDTO perfilDTO, List usuarioDTOs) {
		super();
		// TODO Auto-generated constructor stub
		this.perfilDTO = perfilDTO;
		this.usuarioDTOs = usuarioDTOs;
	}


	public PerfilDTO getPerfilDTO() {
		return perfilDTO;
	}


	public List getUsuarioDTOs() {
		return usuarioDTOs;
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
