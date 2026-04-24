package es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles;

import java.util.Collection;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author Antonio Carro Mariño
 *
 */

public class BusquedaUsuarioForm extends ActionForm {

    private String codPerfil;
    private Collection perfiles = null;

    private String login;
    private String nif;
    private String nombre;
    private String apellido1;
    private String apellido2;

    private String codUsuario;
    private Collection usuarios = null;

    private String order = "login";
    private String orderType = "ASC";

    private String advertencia = "";
    private String exito = ""; // Variable que indica si hay que mostrar un mensaje despues de realizar la busqueda. Usada para la recarga de la página después de modificar un usuario en ventana modal

    //Cadena JSON que guarda el listado de usuarios
    private String lookup = null;

    public String getAdvertencia() {
        return advertencia;
    }

    public void setAdvertencia(String advertencia) {
        this.advertencia = advertencia;
    }

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

    public String getExito() {
        return exito;
    }

    public void setExito(String exito) {
        this.exito = exito;
    }

    public String getLookup() {
        return lookup;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public String getOrder() {
    	return this.order;
    }

    public void setOrder(String order) {
    	this.order = order;
    }

    public String getOrderType() {
    	return this.orderType;
    }

    public void setOrderType(String orderType) {
    	this.orderType = orderType;
    }

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
