package es.sdweb.application.model.dto;

import java.io.Serializable;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import es.sdweb.application.componentes.util.StringUtil;

public class ElementoPerfilDTO implements Serializable
{
	private String codPerfil;
	private String codElemento;


	public ElementoPerfilDTO( String codElemento,String codPerfil) {
		super();
		// TODO Auto-generated constructor stub
		this.codPerfil = codPerfil;
		this.codElemento = codElemento;
	}

	public String getCodElemento() {
		return codElemento;
	}

	public void setCodElemento(String codElemento) {
		this.codElemento = codElemento;
	}

	public String getCodPerfil() {
		return codPerfil;
	}

	public void setCodPerfil(String codPerfil) {
		this.codPerfil = codPerfil;
	}

	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}

  public boolean compararSinEspacios(ElementoPerfilDTO elementoPerfilDTO) {
	  
	  if (StringUtil.trim(codPerfil).compareTo(StringUtil.trim(elementoPerfilDTO.codPerfil))==0
			  && StringUtil.trim(codElemento).compareTo(StringUtil.trim(elementoPerfilDTO.codElemento))==0)
		  return true;
	  else 
		  return false;
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
