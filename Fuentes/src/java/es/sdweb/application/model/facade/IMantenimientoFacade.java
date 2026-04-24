/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sdweb.application.model.facade;

import java.util.List;

import es.sdweb.application.model.dto.ConstanteDTO;
import es.sdweb.application.model.dto.ConstanteDTOList;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;

/**
 * Expone una fachada a la capa superior (Controlador-Vista). Implementacion del patron Facade.
 * @author Antonio Carro Mariño
 */
public interface IMantenimientoFacade {

    //OPERACIONES DE MANTENIMIENTO DE CONSTANTES

    /**
     * Actualiza la lista de constantes en Base de Datos, creando, modificando o eliminando las constantes que fuera menester.
     * @param usrDTO Usuario que realiza la operacion.
     * @param listaConstantes Lista de las constantes a actualizar.
     * @throws ExceptionErrorInterno en caso de que se produzca un error interno.
     */
    public void actualizarConstantes (UsrDTO usrDTO,ConstanteDTOList listaConstantes)
            throws ExceptionErrorInterno;


    /**
     * Obtiene la lista completa de constantes.
     * @param usrDTO Usuario que realiza la operacion.
     * @return Lista completa de constantes.
     * @throws ExceptionErrorInterno
     */
    public List getAllConstantes (UsrDTO usrDTO) throws ExceptionErrorInterno;

    /**
     * Obtiene una constante concreta a partir de su nombre.
     * @param usrDTO Usuario que invoca la operacion.
     * @param nombreConstante Nombre de la constante que se desea recuperar.
     * @return Constante recuperada.
     * @throws ExceptionErrorInterno
     */
    public ConstanteDTO getConstante (UsrDTO usrDTO, String nombreConstante) throws ExceptionErrorInterno;

    /**
     * Obtiene una constante concreta a partir de su nombre.
     * @param nombreConstante Nombre de la constante que se desea recuperar.
     * @return Constante recuperada.
     * @throws ExceptionErrorInterno
     */
    public ConstanteDTO getConstante (String nombreConstante) throws ExceptionErrorInterno;


}//Interface
