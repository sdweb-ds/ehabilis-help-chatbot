package es.sdweb.memorycorp.core;

/**
 * Estructura que aglutina todos los elementos implicados en la definición de un hecho.
 * @author Antonio Carro Mariño
 */
public class Fact {
    private Link accion=null;
    
    private Node sujeto=null;
    private Node objetoDirecto=null;
    private Node objetoIndirecto=null;
    private Node ccLugar=null;
    
    public Fact(Link accion, Node sujeto, Node objetoDirecto){
        this.accion=accion;
        this.sujeto=sujeto;
        this.objetoDirecto=objetoDirecto;
    } 
    
    public Fact(Link accion, Node sujeto, Node objetoDirecto, Node objetoIndirecto, Node ccLugar){
        this.accion=accion;
        this.sujeto=sujeto;
        this.objetoDirecto=objetoDirecto;
        this.objetoIndirecto=objetoIndirecto;
        this.ccLugar=ccLugar;
    } 
    
    public String getLinkKey(){
        String result=NetUtil.generateLinkMapId(sujeto, accion, objetoDirecto, objetoIndirecto, ccLugar);
        return result;
    }

    /**
     * @return Devuelve la acción (Link) de hecho.
     */
    public Link getAccion() {
        return accion;
    }

    /**
     * @param accion Establece la acción (Link) de hecho.
     */
    public void setAccion(Link accion) {
        this.accion = accion;
    }

    /**
     * @return Devuelve el sujeto del hecho.
     */
    public Node getSujeto() {
        return sujeto;
    }

    /**
     * @param sujeto Establece el sujeto del hecho.
     */
    public void setSujeto(Node sujeto) {
        this.sujeto = sujeto;
    }

    /**
     * @return Devuelve el objeto directo.
     */
    public Node getObjetoDirecto() {
        return objetoDirecto;
    }

    /**
     * @param objetoDirecto Establece el objeto directo.
     */
    public void setObjetoDirecto(Node objetoDirecto) {
        this.objetoDirecto = objetoDirecto;
    }

    /**
     * @return Devuelve el objeto indirecto.
     */
    public Node getObjetoIndirecto() {
        return objetoIndirecto;
    }

    /**
     * @param objetoIndirecto Establece el objeto indirecto.
     */
    public void setObjetoIndirecto(Node objetoIndirecto) {
        this.objetoIndirecto = objetoIndirecto;
    }

    /**
     * @return Devuelve el lugar de la acción.
     */
    public Node getCcLugar() {
        return ccLugar;
    }

    /**
     * @param ccLugar Establece el lugar de la acción.
     */
    public void setCcLugar(Node ccLugar) {
        this.ccLugar = ccLugar;
    }
    
}//class
