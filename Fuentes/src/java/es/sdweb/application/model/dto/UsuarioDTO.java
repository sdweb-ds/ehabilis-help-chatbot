package es.sdweb.application.model.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import es.sdweb.application.componentes.util.StringUtil;

/**
 * @author Antonio Carro Mariño
 *
 *         Clase que almacena los datos de Usuario.
 */
public class UsuarioDTO implements Serializable {

    private String codUsuario;
    private String idioma; // El idioma se guarda como idioma_PAIS (Ejemplos: gl_ES, es_ES)
    private String login;
    private String password;
    private String apellido1;
    private String apellido2;
    private String nombre;
    private String telefono;
    private String correo;
    private String nif;

    public UsuarioDTO(String codUsuario, String idioma, String login, String password, String apellido1, String apellido2, String nombre,
            String telefono, String correo, String nif) {
        super();
        // TODO Auto-generated constructor stub
        this.codUsuario = codUsuario;
        this.idioma = idioma;
        this.login = login;
        this.password = password;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.nif = nif;
    }

    public UsuarioDTO(String login, String nombre, String apellido1, String apellido2) {
        super();
        this.login = login;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.nombre = nombre;
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

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean compararSinEspacios(UsuarioDTO usuarioDTO) {
        if (StringUtil.trim(codUsuario).compareToIgnoreCase(StringUtil.trim(usuarioDTO.codUsuario)) == 0
                && StringUtil.trim(login).compareToIgnoreCase(StringUtil.trim(usuarioDTO.login)) == 0 && idioma == usuarioDTO.idioma
                && StringUtil.trim(apellido1).compareToIgnoreCase(StringUtil.trim(usuarioDTO.apellido1)) == 0
                && StringUtil.trim(apellido2).compareToIgnoreCase(StringUtil.trim(usuarioDTO.apellido2)) == 0
                && StringUtil.trim(nombre).compareToIgnoreCase(StringUtil.trim(usuarioDTO.nombre)) == 0
                && StringUtil.trim(telefono).compareToIgnoreCase(StringUtil.trim(usuarioDTO.telefono)) == 0
                && StringUtil.trim(nif).compareToIgnoreCase(StringUtil.trim(usuarioDTO.nif)) == 0
                && StringUtil.trim(password).compareToIgnoreCase(StringUtil.trim(usuarioDTO.password)) == 0)
            return true;
        else
            return false;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
