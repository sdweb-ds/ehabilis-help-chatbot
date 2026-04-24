/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sdweb.application.model.dto;

/**
 * Objeto de transporte de constantes.
 * @author Antonio Carro Mariño
 */
public class ConstanteDTO extends ActualizableEnBloqueDTO{
    private String idConstante="";
    private String nombre="";
    private String valor="";
    private String descripcion="";

    public ConstanteDTO(String idConstante, String nombre, String valor, String descripcion) {
        //throw new UnsupportedOperationException("Not yet implemented");
        this.idConstante=idConstante;
        this.nombre=nombre;
        this.valor=valor;
        this.descripcion=descripcion;
    }

    /**
     * @return the idConstante
     */
    public String getIdConstante() {
        return idConstante;
    }

    /**
     * @param idConstante the idConstante to set
     */
    public void setIdConstante(String idConstante) {
        this.idConstante = idConstante;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}//class
