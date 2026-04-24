package es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles;


import org.apache.struts.action.*;
import javax.servlet.http.*;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * 
 * @author Antonio Carro Mariño
 *
 */


public class LoginForm extends ActionForm {
  private String login;
  private String password;

  private String advertencia;
  
  

public String getAdvertencia() {
	return advertencia;
}
public void setAdvertencia(String advertencia) {
	this.advertencia = advertencia;
}
public String getLogin() {
	return login;
}
public void setLogin(String login) {
	this.login = login;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
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
