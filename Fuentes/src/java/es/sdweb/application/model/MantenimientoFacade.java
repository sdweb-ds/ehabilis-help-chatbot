/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sdweb.application.model;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.facade.FabricaDAOs;
import es.sdweb.application.model.dal.facade.IConstanteDAO;
import es.sdweb.application.model.dto.ConstanteDTO;
import es.sdweb.application.model.dto.ConstanteDTOList;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaReferenciada;
import es.sdweb.application.model.facade.IMantenimientoFacade;
import es.sdweb.application.vista.util.GestorInformacionWeb;

/**
 * Implementa todas las operaciones de negocio relativas al mantenimiento de
 * datos maestros.
 *
 * @author Antonio Carro Mariño
 */
public class MantenimientoFacade implements IMantenimientoFacade {

	// OPERACIONES DE MANTENIMIENTO DE CONSTANTES

	/**
	 * Actualiza la lista de constantes en Base de Datos, creando, modificando o
	 * eliminando las constantes que fuera menester.
	 *
	 * @param usrDTO
	 *            Usuario que realiza la operacion.
	 * @param listaConstantes
	 *            Lista de las constantes a actualizar.
	 */
	@Override
	public void actualizarConstantes(UsrDTO usrDTO, ConstanteDTOList listaConstantes) throws ExceptionErrorInterno {
		try {
			IConstanteDAO dao = FabricaDAOs.createConstanteDAO();
			dao.updateAll(usrDTO, listaConstantes);
		} catch (ExceptionAccesoDatos ex) {
			Logger.getLogger(MantenimientoFacade.class.getName()).log(Level.SEVERE, null, ex);
			throw new ExceptionErrorInterno("ERROR: Error al actualizar las constantes.", ex);
		} catch (ExceptionInstanciaReferenciada ex) { // Si una constante no se
														// puede actualizar
														// porque duplica a otra
														// es un error.
			Logger.getLogger(MantenimientoFacade.class.getName()).log(Level.SEVERE, null, ex);
			throw new ExceptionErrorInterno(ex.getKey(), ex);
		} catch (ExceptionInstanciaDuplicada ex) { // Si una constante no se
													// puede actualizar porque
													// no existe, no pasa nada
			Logger.getLogger(MantenimientoFacade.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Obtniene una constante concreta a partir de su nombre.
	 *
	 * @param usrDTO
	 *            Usuario que invoca la operacion.
	 * @param nombreConstante
	 *            Nombre de la constante que se desea recuperar.
	 * @return Constante recuperada.
	 * @throws ExceptionErrorInterno
	 */
	@Override
	public List getAllConstantes(UsrDTO usrDTO) throws ExceptionErrorInterno {
		IConstanteDAO dao = FabricaDAOs.createConstanteDAO();
		List result = dao.getAllConstantes(usrDTO);
		return result;
	}

	/**
	 * Obtiene una constante concreta a partir de su nombre.
	 *
	 * @param usrDTO
	 *            Usuario que invoca la operacion.
	 * @param nombreConstante
	 *            Nombre de la constante que se desea recuperar.
	 * @return Constante recuperada.
	 * @throws ExceptionErrorInterno
	 */
	@Override
	public ConstanteDTO getConstante(UsrDTO usrDTO, String nombreConstante) throws ExceptionErrorInterno {
		IConstanteDAO dao = FabricaDAOs.createConstanteDAO();
		ConstanteDTO result = dao.getConstante(usrDTO, nombreConstante);
		return result;
	}

	/**
	 * Obtiene una constante concreta a partir de su nombre.
	 *
	 * @param nombreConstante
	 *            Nombre de la constante que se desea recuperar.
	 * @return Constante recuperada.
	 * @throws ExceptionErrorInterno
	 */
	@Override
	public ConstanteDTO getConstante(String nombreConstante) throws ExceptionErrorInterno {
		return this.getConstante(GestorInformacionWeb.getSessionUsr(), nombreConstante);
	}


}// class
