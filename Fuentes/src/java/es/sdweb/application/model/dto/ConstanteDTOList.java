/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sdweb.application.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Antonio Carro Mariño
 */
public class ConstanteDTOList {
    private List lista= new ArrayList();

    /**
     * @return the lista
     */
    public List getLista() {
        return lista;
    }

    /**
     * @param lista the lista to set
     */
    public void setLista(List lista) {
        this.lista = lista;
    }
    
    public void addConstante(ConstanteDTO constante){
        lista.add(constante);
    }
    
}//class
