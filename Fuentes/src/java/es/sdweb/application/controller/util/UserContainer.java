package es.sdweb.application.controller.util;

import java.util.HashMap;
import java.io.Serializable;
import es.sdweb.application.model.dto.UsrDTO;

public class UserContainer implements Serializable{

	private UsrDTO usuario = null; //Datos basicos del usuario
	private HashMap acceso = null; //Permisos que tiene el usuario en base a sus perfiles
	
	public HashMap getAcceso() {
		return acceso;
	}
	public void setAcceso(HashMap acceso) {
		this.acceso = acceso;
	}
	public UsrDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsrDTO usuario) {
		this.usuario = usuario;
	}
	
}//class
