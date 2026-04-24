package es.sdweb.application.controller.actions.gestionUsuariosPerfiles;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actions.util.BaseDispatchAction;
import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.model.dto.PerfilDTO;
import es.sdweb.application.model.dto.PerfilUsuarioChunk;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.dto.UsuarioDTO;
import es.sdweb.application.model.dto.UsuarioDTOList;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.model.exceptions.ExceptionFormatoDeDatos;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaReferenciada;
import es.sdweb.application.model.facade.FabricaFacades;
import es.sdweb.application.model.facade.IGestionUsuarioFacade;
import es.sdweb.application.util.ExceptionFaltaParametro;

/**
 * @author Antonio Carro Mariño
 *
 */
public class BusquedaPerfilAction extends BaseDispatchAction {
	public ActionForward cargar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;

		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

		Log.logRT(usrDTO.getUsr(), "BEGIN BusquedaPerfilAction.cargar()");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_PERFIL_BUSCAR)) {
				IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();
				List perfilDTOs = iGestionUsuarioFacade.buscarPerfiles(usrDTO);

				List perfileDTOs = new ArrayList();
				List perfiles = new ArrayList();
				perfileDTOs.addAll(perfilDTOs);

				for (int i = 0; i < perfileDTOs.size(); i++) {
					perfiles.add(new PerfilUsuarioChunk((PerfilDTO) perfileDTOs.get(i), new ArrayList()));
				}

				this.getSessionContainer(httpServletRequest).setLista1(perfiles);
				// Enviamos el atributo advertencia en la request
				httpServletRequest.setAttribute("advertencia", httpServletRequest.getAttribute("advertencia"));
				result = actionMapping.findForward("PantallaBusquedaPerfil");
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END BusquedaPerfilAction.cargar()");
		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "AutenticacionAction.cargar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
		} catch (ExceptionFaltaParametro e) {
			httpServletRequest.setAttribute("mensajeError", "BusquedaPerfilAction: " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "AutenticacionAction.cargar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado la carga de parámetros de configuración");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return result;
	}

	public ActionForward mostrarUsuarios(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;

		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "BEGIN BusquedaPerfilAction.mostrarUsuarios()");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_PERFIL_BUSCAR)) {
				IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();

				int indice = (new Integer(httpServletRequest.getParameter("indice"))).intValue();
				List perfiles = this.getSessionContainer(httpServletRequest).getLista1();

				PerfilUsuarioChunk perfilUsuarioChunk = (PerfilUsuarioChunk) perfiles.get(indice);
				UsuarioDTOList usuarioDTOList = iGestionUsuarioFacade.buscarUsuariosPorPerfil(usrDTO, perfilUsuarioChunk.getPerfilDTO().getCodPerfil(), 1, 0,
						true);

				List usuarioVOs = new ArrayList();
				List usuarioDTOS = usuarioDTOList.getUsuarioDTOs();

				for (int i = 0; i < usuarioDTOS.size(); i++) {
					UsuarioDTO usuarioDTO = (UsuarioDTO) usuarioDTOS.get(i);
					usuarioVOs.add(new UsuarioDTO(usuarioDTO.getLogin(), usuarioDTO.getNombre(), usuarioDTO.getApellido1(), usuarioDTO.getApellido2()));
				}

				perfilUsuarioChunk.setUsuarioVOs(usuarioVOs);
				perfiles.set(indice, perfilUsuarioChunk);
				this.getSessionContainer(httpServletRequest).setLista1(perfiles);

				result = actionMapping.findForward("FrameBusquedaPerfil");
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END BusquedaPerfilAction.mostrarUsuarios()");
		} catch (ExceptionFormatoDeDatos e) {
			Log.logRE(usrDTO.getUsr(), "BusquedaPerfilAction.mostrarUsuarios()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return result = actionMapping.findForward("FrameBusquedaPerfil");
		} catch (ExceptionInstanciaNoHallada e) {
			Log.logRE(usrDTO.getUsr(), "BusquedaPerfilAction.mostrarUsuarios()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return result = actionMapping.findForward("FrameBusquedaPerfil");
		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaPerfilAction.mostrarUsuarios()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
		} catch (ExceptionFaltaParametro e) {
			httpServletRequest.setAttribute("mensajeError", "BusquedaPerfilAction: " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaPerfilAction.mostrarUsuarios()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado la carga de parámetros de configuración");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return result;
	}

	public ActionForward ocultarUsuarios(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;

		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

		Log.logRT(usrDTO.getUsr(), "BEGIN BusquedaPerfilAction.ocultarUsuarios()");

		if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_PERFIL_BUSCAR)) {
			int indice = (new Integer(httpServletRequest.getParameter("indice"))).intValue();
			List perfiles = this.getSessionContainer(httpServletRequest).getLista1();

			PerfilUsuarioChunk perfilUsuarioChunk = (PerfilUsuarioChunk) perfiles.get(indice);
			perfilUsuarioChunk.setUsuarioVOs(new ArrayList());

			perfiles.set(indice, perfilUsuarioChunk);
			this.getSessionContainer(httpServletRequest).setLista1(perfiles);

			result = actionMapping.findForward("FrameBusquedaPerfil");
		} else {
			result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
		}

		Log.logRT(usrDTO.getUsr(), "END BusquedaPerfilAction.ocultarUsuarios()");

		return result;
	}

	public ActionForward eliminar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;

		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

		Log.logRT(usrDTO.getUsr(), "BEGIN BusquedaPerfilAction.eliminar()");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_PERFIL_BUSCAR)) {
				IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();

				String codPerfil = (httpServletRequest.getParameter("codPerfil"));
				try {
					iGestionUsuarioFacade.eliminarPerfil(usrDTO, codPerfil);
				} catch (ExceptionInstanciaReferenciada e) {
					Log.logRT("El perfil no será eliminado mientras tenga usuarios asociados.");
					httpServletRequest.setAttribute("advertencia", "advertencia.perfil.con.usuarios");
					return actionMapping.findForward("NuevaBusqueda");
				}

				httpServletRequest.setAttribute("mensaje", "mensaje.perfil.eliminado.correctamente");

				result = actionMapping.findForward("NuevaBusqueda");
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END BusquedaPerfilAction.cargar()");

		} catch (ExceptionInstanciaNoHallada e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaPerfilAction.eliminar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"ExceptionInstanciaNoHallada.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaPerfilAction.eliminar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
		} catch (ExceptionFaltaParametro e) {
			httpServletRequest.setAttribute("mensajeError", "BusquedaPerfilAction: " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "BusquedaPerfilAction.eliminar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado la carga de parámetros de configuración");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return result;
	}
} // class