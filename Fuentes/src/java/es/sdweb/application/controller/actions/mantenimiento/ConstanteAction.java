package es.sdweb.application.controller.actions.mantenimiento;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.mantenimiento.ConstanteForm;
import es.sdweb.application.controller.actions.util.BaseDispatchAction;
import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.model.dto.ConstanteDTO;
import es.sdweb.application.model.dto.ConstanteDTOList;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.model.facade.FabricaFacades;
import es.sdweb.application.model.facade.IMantenimientoFacade;

/**
 * @author Antonio Carro Mariño
 *
 */

public class ConstanteAction extends BaseDispatchAction {

	public ActionForward cargar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;
		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		ConstanteForm form = (ConstanteForm) actionForm;

		Log.logRT(usrDTO.getUsr(), "BEGIN ConstanteAction.cargar()");

		try {
			// Comprobamos si el usuario tiene permisos, y si es asi ejecutamos la funcionalidad
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_MANTENIMIENTO_CONSTANTES)) {
				IMantenimientoFacade iMantenimientoFacade = FabricaFacades.createMantenimientoFacade();
				List constantesList = iMantenimientoFacade.getAllConstantes(usrDTO);

				form.setConstantes(constantesList); // Guardamos la lista de constantes en el FormBean

				result = actionMapping.findForward("FrameConstantes"); // Redirigimos a la vista para pintar los resultados
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END ConstanteAction.cargar()");

		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "ConstanteAction.cargar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Se ha producido un error interno.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
		}

		return result;
	}

	public ActionForward actualizar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;
		// Si el usuario no esta validado retorna la pagina de no validado
		ConstanteForm form = (ConstanteForm) actionForm;
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

		Log.logRT(usrDTO.getUsr(), "BEGIN ConstanteAction.actualizar()");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_MANTENIMIENTO_CONSTANTES)) {
				result = actionMapping.findForward("FrameConstantesConfirmar");
				IMantenimientoFacade iMantenimientoFacade = FabricaFacades.createMantenimientoFacade();

				String[] idConstante = httpServletRequest.getParameterValues("idConstante");
				String[] nombre = httpServletRequest.getParameterValues("nombre");
				String[] valor = httpServletRequest.getParameterValues("valor");
				String[] descripcion = httpServletRequest.getParameterValues("descripcion");
				String[] modificado = httpServletRequest.getParameterValues("modificado");

				if (idConstante != null) { // Si no hay una sola linea en la tabla de constantes, podria ser null el array
					ConstanteDTOList ctel = new ConstanteDTOList();
					for (int i = 0; i < idConstante.length; i++) {
						ConstanteDTO constante = new ConstanteDTO(idConstante[i], nombre[i], valor[i], descripcion[i]);
						constante.setEstado(modificado[i]);
						if (!constante.getEstado().equals(IConstantes.CFG_JAVASCRIPT_DELETE_ITEM_KEY)
								&& !constante.getEstado().equals(IConstantes.CFG_JAVASCRIPT_IGNORE_ITEM_KEY)) {
							ctel.addConstante(constante); // Añadimos la nueva constante a la lista si es nueva o modificada
						}
					} // for
					iMantenimientoFacade.actualizarConstantes(usrDTO, ctel); // Actualizamos las constantes en BBDD
				}
				List constantes = iMantenimientoFacade.getAllConstantes(usrDTO); // Recargamos las constantes de BBDD

				// Guardamos las constantes actualizadas en el formulario
				form.setConstantes(constantes);
				result = actionMapping.findForward("FrameConstantes"); // Redirigimos a la vista para pintar los resultados
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END ConstanteAction.actualizar()");

		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "ConstanteAction.actualizar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Se ha producido un error interno.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
		}

		return result;
	}

	public ActionForward eliminar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;
		// Si el usuario no esta validado retorna la pagina de no validado
		ConstanteForm form = (ConstanteForm) actionForm;
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

		Log.logRT(usrDTO.getUsr(), "BEGIN ConstanteAction.eliminar()");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_MANTENIMIENTO_CONSTANTES)) {
				result = actionMapping.findForward("FrameConstantesConfirmar");
				IMantenimientoFacade iMantenimientoFacade = FabricaFacades.createMantenimientoFacade();

				String[] idConstante = httpServletRequest.getParameterValues("idConstante");
				String[] nombre = httpServletRequest.getParameterValues("nombre");
				String[] valor = httpServletRequest.getParameterValues("valor");
				String[] descripcion = httpServletRequest.getParameterValues("descripcion");
				String[] modificado = httpServletRequest.getParameterValues("modificado");

				if (idConstante != null) { // Si no hay una sola linea en la tabla de constantes, podria ser null el array
					ConstanteDTOList ctel = new ConstanteDTOList();
					for (int i = 0; i < idConstante.length; i++) {
						ConstanteDTO constante = new ConstanteDTO(idConstante[i], nombre[i], valor[i], descripcion[i]);
						constante.setEstado(modificado[i]);
						if (constante.getEstado().equals(IConstantes.CFG_JAVASCRIPT_DELETE_ITEM_KEY)) {
							ctel.addConstante(constante); // Añadimos la nueva constante a la lista si esta marcada para eliminar
						}
					} // for
					iMantenimientoFacade.actualizarConstantes(usrDTO, ctel); // Actualizamos las constantes en BBDD
				}
				List constantes = iMantenimientoFacade.getAllConstantes(usrDTO); // Recargamos las constantes de BBDD

				// Guardamos las constantes actualizadas en el formulario
				form.setConstantes(constantes);
				result = actionMapping.findForward("FrameConstantes"); // Redirigimos a la vista para pintar los resultados
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END ConstanteAction.eliminar()");
		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "ConstanteAction.eliminar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Se ha producido un error interno.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
		}

		return result;
	}
} // class