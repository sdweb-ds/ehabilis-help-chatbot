/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sdweb.application.model.dto;

/**
 *
 * @author Antonio Carro Mariño
 */
public abstract class ActualizableEnBloqueDTO {
    private String estado=null;

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
        
}// Class
