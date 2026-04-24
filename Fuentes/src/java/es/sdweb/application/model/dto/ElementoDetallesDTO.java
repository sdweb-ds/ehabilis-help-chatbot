package es.sdweb.application.model.dto;

import java.io.Serializable;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import es.sdweb.application.componentes.util.StringUtil;

public class ElementoDetallesDTO implements Serializable
{
  private String codElemento;
  private String url;
  private String tipo;
  private String ubicacion;
  private String nombre;
  private String ubicacionPadre;


public ElementoDetallesDTO(String codElemento, String url, String tipo, String ubicacion, String nombre, String codPadre) {
	super();
	// TODO Auto-generated constructor stub
	this.codElemento = codElemento;
	this.url = url;
	this.tipo = tipo;
	this.ubicacion = ubicacion;
	this.nombre = nombre;
	this.ubicacionPadre = codPadre;
}

public String getCodElemento() {
	return codElemento;
}

public void setCodElemento(String codElemento) {
	this.codElemento = codElemento;
}

public String getCodPadre() {
	return ubicacionPadre;
}

public void setCodPadre(String codPadre) {
	this.ubicacionPadre = codPadre;
}

public String getNombre() {
	return nombre;
}

public void setNombre(String nombre) {
	this.nombre = nombre;
}

public String getTipo() {
	return tipo;
}

public void setTipo(String tipo) {
	this.tipo = tipo;
}

public String getUbicacion() {
	return ubicacion;
}

public void setUbicacion(String ubicacion) {
	this.ubicacion = ubicacion;
}

public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
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
