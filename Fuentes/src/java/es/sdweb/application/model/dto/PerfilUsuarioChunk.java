package es.sdweb.application.model.dto;


import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author Antonio Carro Mariño
 *
 * Clase que almacena los datos de Usuario.
 */
public class PerfilUsuarioChunk implements Serializable{

	PerfilDTO perfilDTO = null;
	List usuarioVOs=null;

	public PerfilUsuarioChunk(PerfilDTO perfilDTO, List usuarioVOs) {
		super();
		// TODO Auto-generated constructor stub
		this.perfilDTO = perfilDTO;
		this.usuarioVOs = usuarioVOs;
	}

	public PerfilDTO getPerfilDTO() {
		return perfilDTO;
	}

	public void setPerfilDTO(PerfilDTO perfilDTO) {
		this.perfilDTO = perfilDTO;
	}

	public List getUsuarioVOs() {
		return usuarioVOs;
	}

	public void setUsuarioVOs(List usuarioVOs) {
		this.usuarioVOs = usuarioVOs;
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
