/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sdweb.application.model.dto;

import java.util.List;

/**
 *
 * @author Antonio Carro Mariño
 */
public abstract class ActualizableEnBloqueDTOList {

    public abstract List getLista();
    public abstract void setLista(List lista);
    
    /**
     * Guarda el estado especificado en cada uno de los items de la lista.
     * @param estado Estado a guardar (estan definidos en IConstantes)
     */
    public void setEstado(String estado) {
      List lista=getLista();
      for (int i=0;i<lista.size();i++){
        ActualizableEnBloqueDTO dto=(ActualizableEnBloqueDTO)lista.get(i);
        dto.setEstado(estado);
      }//for
    }
        
}// Class
