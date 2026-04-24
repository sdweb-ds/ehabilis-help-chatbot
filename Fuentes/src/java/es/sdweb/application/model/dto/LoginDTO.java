package es.sdweb.application.model.dto;


import java.io.Serializable;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import es.sdweb.application.componentes.util.StringUtil;


public class LoginDTO implements Serializable{


	private String login;
	private String password;
	

	
	public LoginDTO(String login, String password) {
		super();
		// TODO Auto-generated constructor stub
		this.login = login;
		this.password = password;
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
