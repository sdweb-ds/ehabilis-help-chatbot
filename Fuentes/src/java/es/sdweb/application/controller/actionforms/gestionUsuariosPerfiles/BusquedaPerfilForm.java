package es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles;


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


public class BusquedaPerfilForm extends ActionForm {
	
	
	private String codPerfil;
	private Collection perfiles = null;
	
	
	private String login;
	private String nif;
	private String nombre;
	private String apellido1;
	private String apellido2; 

	private String codUsuario;
	private Collection usuarios = null;
	
	
  public String getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}

	public Collection getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Collection usuarios) {
		this.usuarios = usuarios;
	}

public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public String getCodPerfil() {
		return codPerfil;
	}

	public void setCodPerfil(String codPerfil) {
		this.codPerfil = codPerfil;
	}

	public Collection getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(Collection perfiles) {
		this.perfiles = perfiles;
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


}
