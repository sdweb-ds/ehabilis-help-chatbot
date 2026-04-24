package es.sdweb.application.controller.util;

import es.sdweb.application.componentes.util.DateUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class SessionContainer implements Serializable, HttpSessionBindingListener {
    private UserContainer userContainer;
    private HashMap tablaHash;
    private HashMap tablaHash1;
    private List lista1;
    private List Lista2;
    private List lista3;
    private List Lista4;
    private String Lista5; //Cadena JSON
    private String menu = "menu";
    private String util = "util";
    private String util2 = "util2";
    private String fechaHoraUltimaActualizacion = DateUtil.getDateTime(); //Utilizado para traer solo los datos mas recientes de BD

    public HashMap getTablaHash1() {
        return tablaHash1;
    }

    public void setTablaHash1(HashMap tablaHash1) {
        this.tablaHash1 = tablaHash1;
    }

    public HashMap getTablaHash() {
        return tablaHash;
    }

    public void setTablaHash(HashMap tablaHash) {
        this.tablaHash = tablaHash;
    }

    public String getUtil2() {
        return util2;
    }

    public void setUtil2(String util2) {
        this.util2 = util2;
    }

    /**
     * Default Constructor
     */
    public SessionContainer() {
        super();
        inicializar();
    }

    /**
     * The container calls this method when it is being unbound from the
     * session.
     */
    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        // Realiza la limpieza de recursos
        limpiar();
    }

    /**
     * The container calls this method when it is being bound to the session.
     */
    @Override
    public void valueBound(HttpSessionBindingEvent event) {
    }

    public UserContainer getUserContainer() {
        return userContainer;
    }

    public void setUserContainer(UserContainer personal) {
        this.userContainer = personal;
    }

    /**
     * Inicializa los recursos requeridos
     */
    private void inicializar() {

    }

    /**
     * Libera los recursos abiertos.
     */
    public void limpiar() {
        setUserContainer(null);
    }

    public List getLista1() {
        return lista1;
    }

    public void setLista1(List lista1) {
        this.lista1 = lista1;
    }

    public List getLista2() {
        return Lista2;
    }

    public void setLista2(List lista2) {
        Lista2 = lista2;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public List getLista3() {
        return lista3;
    }

    public void setLista3(List lista3) {
        this.lista3 = lista3;
    }

    public List getLista4() {
        return Lista4;
    }

    public void setLista4(List lista4) {
        Lista4 = lista4;
    }
    
    public String getLista5() {
        return Lista5;
    }

    public void setLista5(String lista5) {
        Lista5 = lista5;
    }

    public String getUtil() {
        return util;
    }

    public void setUtil(String util) {
        this.util = util;
    }

    /**
     * @return the fechaHoraUltimaActualizacion
     */
    public String getFechaHoraUltimaActualizacion() {
        return fechaHoraUltimaActualizacion;
    }

    /**
     * @param fechaHoraUltimaActualizacion the fechaHoraUltimaActualizacion to set
     */
    public void setFechaHoraUltimaActualizacion(String fechaHoraUltimaActualizacion) {
        this.fechaHoraUltimaActualizacion = fechaHoraUltimaActualizacion;
    }

}
