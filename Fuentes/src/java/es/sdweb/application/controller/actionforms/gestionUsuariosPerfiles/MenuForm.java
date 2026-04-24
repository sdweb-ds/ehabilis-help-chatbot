package es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles;


import org.apache.struts.action.*;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * 
 * @author Antonio Carro Mariño
 *
 */


public class MenuForm extends ActionForm {
  private String menu;
 

public MenuForm(String menu) {
	super();
	// TODO Auto-generated constructor stub
	this.menu = menu;
}
public String getMenu() {
	return menu;
}
public void setMenu(String menu) {
	this.menu = menu;
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
