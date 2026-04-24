package es.sdweb.application.model.dto;

import java.io.Serializable;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import es.sdweb.application.componentes.util.StringUtil;

public class PerfilUsuarioDTO implements Serializable {

	private String codPerfil;
	private String codUsuario;
		

	public PerfilUsuarioDTO(String codPerfil, String codUsuario) {
		super();
		// TODO Auto-generated constructor stub
		this.codPerfil = codPerfil;
		this.codUsuario = codUsuario;
	}
	
	
	public String getCodPerfil() {
		return codPerfil;
	}


	public void setCodPerfil(String codPerfil) {
		this.codPerfil = codPerfil;
	}


	public String getCodUsuario() {
		return codUsuario;
	}


	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}


	public boolean compararSinEspacios(PerfilUsuarioDTO perfilUsuarioDTO) {
		  
		  if (StringUtil.trim(codUsuario).compareTo(StringUtil.trim(perfilUsuarioDTO.codUsuario))==0
				  && StringUtil.trim(codPerfil).compareTo(StringUtil.trim(perfilUsuarioDTO.codPerfil))==0)
			  return true;
		  else 
			  return false;
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
