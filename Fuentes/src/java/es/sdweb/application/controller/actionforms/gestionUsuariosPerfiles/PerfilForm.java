package es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles;

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

public class PerfilForm extends ActionForm {

    private String codPerfil = "";
    private String nomPerfil = "";
    private String desPerfil = "";

    private String confirmar = "false";

    //Atributo que indicara si nos encontramos en la pantalla o en el popup, y en el popup de modificacion o en el de creacion
    private String tipo = "";

    private String seleccionarTodos = "false";

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getConfirmar() {
        return confirmar;
    }

    public void setConfirmar(String confirmar) {
        this.confirmar = confirmar;
    }

    public String getDesPerfil() {
        return desPerfil;
    }

    public void setDesPerfil(String desPerfil) {
        this.desPerfil = desPerfil;
    }

    public String getSeleccionarTodos() {
        return seleccionarTodos;
    }

    public void setSeleccionarTodos(String seleccionarTodos) {
        this.seleccionarTodos = seleccionarTodos;
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
