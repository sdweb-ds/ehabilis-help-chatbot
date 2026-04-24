package es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles;

import java.util.HashMap;
import java.util.List;

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

public class CrearModificarUsuarioForm extends ActionForm {

    private String[] codPerfilesAsociados = null;

    private List perfilesAsociados = null;

    private String[] codPerfilesNoAsociados = null;

    private List perfilesNoAsociados = null;

    private String codUsuario;

    // Tabla hash para guardar el listado de idiomas disponibles para mostrar el desplegable en las pantallas
    private HashMap<String, String> idiomasDisponibles;
    private String idioma;

    private String login;
    private String nif;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String telefono;
    private String correo;
    private String password;
    private String confirmarPassword;

    private String confirmar;
    private String alta;

    private String es_colaborador;

    private String advertencia;


    public String getEs_colaborador() {
        return es_colaborador;
    }

    public void setEs_colaborador(String es_colaborador) {
        this.es_colaborador = es_colaborador;
    }

    public String getAdvertencia() {
        return this.advertencia;
    }

    public void setAdvertencia(String advertencia) {
        this.advertencia = advertencia;
    }

    public String getAlta() {
        return alta;
    }

    public void setAlta(String alta) {
        this.alta = alta;
    }

    public String getConfirmar() {
        return confirmar;
    }

    public void setConfirmar(String confirmar) {
        this.confirmar = confirmar;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
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

    public String getIdioma() {
        return idioma;
    }

    public HashMap<String, String> getIdiomasDisponibles() {
        return idiomasDisponibles;
    }

    public void setIdiomasDisponibles(HashMap<String, String> idiomasDisponibles) {
        this.idiomasDisponibles = idiomasDisponibles;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List getPerfilesAsociados() {
        return perfilesAsociados;
    }

    public void setPerfilesAsociados(List perfilesAsociados) {
        this.perfilesAsociados = perfilesAsociados;
    }

    public List getPerfilesNoAsociados() {
        return perfilesNoAsociados;
    }

    public void setPerfilesNoAsociados(List perfilesNoAsociados) {
        this.perfilesNoAsociados = perfilesNoAsociados;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String[] getCodPerfilesAsociados() {
        return codPerfilesAsociados;
    }

    public void setCodPerfilesAsociados(String[] codPerfilesAsociados) {
        this.codPerfilesAsociados = codPerfilesAsociados;
    }

    public String[] getCodPerfilesNoAsociados() {
        return codPerfilesNoAsociados;
    }

    public void setCodPerfilesNoAsociados(String[] codPerfilesNoAsociados) {
        this.codPerfilesNoAsociados = codPerfilesNoAsociados;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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
