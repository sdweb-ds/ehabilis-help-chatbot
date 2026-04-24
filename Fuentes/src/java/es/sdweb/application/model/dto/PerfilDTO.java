package es.sdweb.application.model.dto;

import java.io.Serializable;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import es.sdweb.application.componentes.util.DateUtil;

public class PerfilDTO implements Serializable {
    private String codPerfil;
    private String nomPerfil;
    private String desPerfil;

    public PerfilDTO(String codPerfil, String nomPerfil, String desPerfil) {
        super();
        // TODO Auto-generated constructor stub
        this.codPerfil = codPerfil;
        this.nomPerfil = nomPerfil;
        this.desPerfil = desPerfil;
    }

    public PerfilDTO(String nomPerfil, String desPerfil) {
        super();
        this.nomPerfil = nomPerfil;
        this.desPerfil = desPerfil;

        // El codigo de perfil se genera concantenando la fecha actual mas 4 caracteres aleatorios
        this.codPerfil = DateUtil.getTime("yyyyMMddHHmmss") + RandomStringUtils.randomAlphabetic(4);
    }

    public String getCodPerfil() {
        return codPerfil;
    }

    public void setCodPerfil(String codPerfil) {
        this.codPerfil = codPerfil;
    }

    public String getNomPerfil() {
        return nomPerfil;
    }

    public void setNomPerfil(String nomPerfil) {
        this.nomPerfil = nomPerfil;
    }

    public String getDesPerfil() {
        return desPerfil;
    }

    public void setDesPerfil(String desPerfil) {
        this.desPerfil = desPerfil;
    }

    /*
    public boolean compararSinEspacios(PerfilDTO perfilDTO) {
    	  if (StringUtil.trim(codPerfil).compareToIgnoreCase(StringUtil.trim(perfilDTO.codPerfil))==0
    			  && desPerfil.compareTo(perfilDTO.desPerfil)==0)
    		  return true;
    	  else
    		  return false;
    }
    */

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public int compareTo(Object remoteObject) {
        return CompareToBuilder.reflectionCompare(this, remoteObject);
    }

    @Override
    public boolean equals(Object remoteObject) {
        return EqualsBuilder.reflectionEquals(this, remoteObject);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
