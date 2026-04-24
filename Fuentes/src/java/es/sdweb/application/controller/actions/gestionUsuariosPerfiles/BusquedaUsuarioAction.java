package es.sdweb.application.controller.actions.gestionUsuariosPerfiles;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles.BusquedaUsuarioForm;
import es.sdweb.application.controller.actions.util.BaseDispatchAction;
import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.model.dto.PerfilDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.model.exceptions.ExceptionFormatoDeDatos;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.facade.FabricaFacades;
import es.sdweb.application.model.facade.IGestionUsuarioFacade;
import es.sdweb.application.util.ExceptionFaltaParametro;

/**
 * @author Antonio Carro Mariño
 *
 */

public class BusquedaUsuarioAction extends BaseDispatchAction {
	public ActionForward cargar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		ActionForward result = null;
		BusquedaUsuarioForm busquedaUsuarioForm = (BusquedaUsuarioForm) actionForm;

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

		Log.logRT(usrDTO.getUsr(), "BEGIN BusquedaUsuarioAction.cargar()");
		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_USUARIO_BUSCAR)) {
				IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();

				List perfilDTOs = iGestionUsuarioFacade.buscarPerfiles(usrDTO);
				PerfilDTO vacio = new PerfilDTO("", "", "");
				List perfiles = new ArrayList();

				perfiles.add(vacio);
				perfiles.addAll(perfilDTOs);

				List usuarios = new ArrayList();
				List lookup = new ArrayList();
				List usariosDTOs;

				usariosDTOs = iGestionUsuarioFacade.buscarTodos(usrDTO);
				lookup.addAll(usariosDTOs);

				JSONArray arrayJSON = new JSONArray(lookup);

				this.getSessionContainer(httpServletRequest).setLista3(perfiles);
				this.getSessionContainer(httpServletRequest).setLista4(usuarios);
				this.getSessionContainer(httpServletRequest).setLista5(arrayJSON.toString());
				busquedaUsuarioForm.setPerfiles(perfiles);
				busquedaUsuarioForm.setUsuarios(usuarios);
				busquedaUsuarioForm.setLookup(arrayJSON.toString());

				result = actionMapping.findForward("PantallaBusquedaUsuario");
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END BusquedaUsuarioAction.cargar()");
		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaUsuarioAction.cargar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
		} catch (ExceptionFaltaParametro e) {
			httpServletRequest.setAttribute("mensajeError", "BusquedaUsuarioAction: " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaUsuarioAction.cargar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado la carga de parámetros de configuración");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return result;
	}

	public ActionForward buscar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		ActionForward result = null;
		BusquedaUsuarioForm busquedaUsuarioForm = (BusquedaUsuarioForm) actionForm;

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

		Log.logRT(usrDTO.getUsr(), "BEGIN BusquedaUsuarioAction.buscar()");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_USUARIO_BUSCAR)) {
				IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();

				List usuarios;

				busquedaUsuarioForm.setPerfiles(this.getSessionContainer(httpServletRequest).getLista3());

				// Si no se ha seleccionado perfil
				if (busquedaUsuarioForm.getCodPerfil().compareTo("") == 0) {
					usuarios = iGestionUsuarioFacade.buscarUsuarios(usrDTO, null, busquedaUsuarioForm.getLogin(), busquedaUsuarioForm.getApellido1(),
							busquedaUsuarioForm.getApellido2(), busquedaUsuarioForm.getNombre(), busquedaUsuarioForm.getNif(), null,
							busquedaUsuarioForm.getOrder(), busquedaUsuarioForm.getOrderType(), false);
				} else {
					usuarios = iGestionUsuarioFacade.buscarUsuarios(usrDTO, null, busquedaUsuarioForm.getLogin(), busquedaUsuarioForm.getApellido1(),
							busquedaUsuarioForm.getApellido2(), busquedaUsuarioForm.getNombre(), busquedaUsuarioForm.getNif(),
							busquedaUsuarioForm.getCodPerfil(), busquedaUsuarioForm.getOrder(), busquedaUsuarioForm.getOrderType(), false);
				}

				busquedaUsuarioForm.setUsuarios(usuarios);
				this.getSessionContainer(httpServletRequest).setLista4(usuarios);

				List lookup = new ArrayList();
				List usariosDTOs;

				usariosDTOs = iGestionUsuarioFacade.buscarTodos(usrDTO);
				lookup.addAll(usariosDTOs);

				JSONArray arrayJSON = new JSONArray(lookup);

				this.getSessionContainer(httpServletRequest).setLista5(arrayJSON.toString());
				busquedaUsuarioForm.setLookup(arrayJSON.toString());
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			result = actionMapping.findForward("PantallaBusquedaUsuario");
			Log.logRT(usrDTO.getUsr(), "END BusquedaUsuarioAction.buscar()");
		} catch (ExceptionInstanciaNoHallada e) {
			busquedaUsuarioForm.setAdvertencia("advertencia.usuario.inexistente");
			this.getSessionContainer(httpServletRequest).setLista4(new ArrayList());
			return result = actionMapping.findForward("PantallaBusquedaUsuario");

		} catch (ExceptionFormatoDeDatos e) {
			busquedaUsuarioForm.setAdvertencia("advertencia.fecha.formato.buscar");
			Log.logRE(usrDTO.getUsr(), "BusquedaUsuarioAction.buscar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			this.getSessionContainer(httpServletRequest).setLista4(new ArrayList());
			return result = actionMapping.findForward("PantallaBusquedaUsuario");
		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaUsuarioAction.buscar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);

		} catch (ExceptionFaltaParametro e) {
			httpServletRequest.setAttribute("mensajeError", "BusquedaUsuarioAction: " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaUsuarioAction.buscar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado la carga de parámetros de configuración");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return result;
	}

	/**
	 * Este action implementa la funcionalidad de eliminar un determinado
	 * usuario y seguidamente llama al action de buscar usuarios para actualizar
	 * el listado de busqueda
	 *
	 * @param actionMapping
	 * @param actionForm
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return
	 */
	public ActionForward eliminar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		ActionForward result = null;

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

		Log.logRT(usrDTO.getUsr(), "BEGIN BusquedaUsuarioAction.eliminar()");

		String login = httpServletRequest.getParameter("parLogin");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_USUARIO_BUSCAR)) {
				IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();
				iGestionUsuarioFacade.eliminarUsuarioporLogin(usrDTO, login);
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			result = actionMapping.findForward("NuevaBusqueda");
			Log.logRT(usrDTO.getUsr(), "END BusquedaUsuarioAction.eliminar()");
		} catch (ExceptionInstanciaNoHallada e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaUsuarioAction.eliminar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"ExceptionInstanciaNoHallada.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);

		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaUsuarioAction.eliminar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);

		} catch (ExceptionFaltaParametro e) {
			httpServletRequest.setAttribute("mensajeError", ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaUsuarioAction.eliminar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado la carga de parámetros de configuración");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return result;
	}
} // class
