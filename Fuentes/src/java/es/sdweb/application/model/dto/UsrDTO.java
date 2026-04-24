package es.sdweb.application.model.dto;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author Antonio Carro Mariño
 *
 * Clase destinada a ser usada por el registro de logs. Sera pasada como primer parametro de cada metodo.
 */
public class UsrDTO implements Serializable {
    private String usr = null;
    private String ipCliente = null;
    private String app = null;
    private Locale locale;

    public UsrDTO() {
    }

    public UsrDTO(String login, String application, String ip, Locale locale) {
        this.usr = login;
        this.app = application;
        this.ipCliente = ip;
        this.locale = locale;
    }

    /**
     * @return
     */
    public String getIpCliente() {
        return ipCliente;
    }

    /**
     * @return
     */
    public String getUsr() {
        return usr;
    }

    /**
     * @param string
     */
    public void setIpCliente(String ip) {
        this.ipCliente = ip;
    }

    /**
     * @param string
     */
    public void setUsr(String usr) {
        this.usr = usr;
    }

    /**
     * @return
     */
    public String getApp() {
        return app;
    }

    /**
     * @param string
     */
    public void setApp(String application) {
        this.app = application;
    }

    /**
     * @return
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param string
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}//class
