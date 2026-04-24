/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sdweb.application.model.dal.facade;

import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dto.ConstanteDTO;
import es.sdweb.application.model.dto.ConstanteDTOList;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaReferenciada;
import java.util.List;

/**
 *
 * @author Antonio Carro Mariño
 */
public interface IConstanteDAO {
    
    public ConstanteDTO create(UsrDTO usrDTO,ConstanteDTO constante) throws ExceptionAccesoDatos,ExceptionInstanciaDuplicada;

    public void update(UsrDTO usrDTO,ConstanteDTO constante) throws ExceptionAccesoDatos,ExceptionInstanciaNoHallada,ExceptionInstanciaDuplicada;

    public void updateAll (UsrDTO usrDTO,ConstanteDTOList constantes) throws ExceptionAccesoDatos,ExceptionInstanciaReferenciada,ExceptionInstanciaDuplicada;

    public List getAllConstantes(UsrDTO usrDTO) throws ExceptionAccesoDatos;

    public ConstanteDTO getConstante(UsrDTO usrDTO, String nombreConstante) throws ExceptionAccesoDatos;

    public void remove(UsrDTO usrDTO,String idConstante)throws ExceptionAccesoDatos,ExceptionInstanciaNoHallada, ExceptionInstanciaReferenciada;

}//interface
