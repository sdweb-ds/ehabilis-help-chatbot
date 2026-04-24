package es.sdweb.application.controller.actions.gestionUsuariosPerfiles;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles.PerfilForm;
import es.sdweb.application.controller.actions.util.BaseDispatchAction;
import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.model.dto.ElementoDetallesDTO;
import es.sdweb.application.model.dto.ElementoDetallesDTOArbol;
import es.sdweb.application.model.dto.PerfilDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.facade.FabricaFacades;
import es.sdweb.application.model.facade.IGestionUsuarioFacade;
import es.sdweb.application.util.ExceptionFaltaParametro;
import es.sdweb.application.vista.util.ElementosPerfil;

/**
 *
 * Esta clase de encarga de gestionar el funcionamiento de la patalla de
 * creacion y modificacion de perfiles. Para ello almacena dos cadenas en la
 * sesion: - En una de ellas se encuentra el formulario de los elementos que
 * seran chequeados y asociados al perfil. Esto es creado dinamicamente por la
 * clase utilidad GestFeudoWebElementos. - El segundo String contiene el html
 * que muestra el arbol de los elementos y que es usado en el frame
 * elementos.jsp. Tambien creado por la clase utilidad anterior.
 *
 *
 */

public class CrearModificarPerfilAction extends BaseDispatchAction {
	// Esta action carga la pantalla de creacion y modificacion de perfiles
	public ActionForward cargar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;

		PerfilForm perfilForm = (PerfilForm) actionForm;

		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

		Log.logRT(usrDTO.getUsr(), "BEGIN CrearModificarPerfilAction.cargar()");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_PERFIL_CREAR)) {
				IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();

				List elementosArbol = iGestionUsuarioFacade.arbolElementos(usrDTO, "no perfil");

				ResourceBundle properties = ResourceBundle.getBundle("i18n", getSessionLocale(httpServletRequest));

				String elementos = (ElementosPerfil.getInstance()).getArbolFeudos(httpServletRequest.getContextPath(), elementosArbol, properties);
				String elementosOcultos = (ElementosPerfil.getInstance()).getArbolFeudosOcultos(elementosArbol);

				// El primer String contiene el html generado dinamicamente que se cargara en el frame: lista_elementos.jsp
				this.getSessionContainer(httpServletRequest).setUtil(elementos);
				// Este segundo String (util2) contiene el html generado dinamicamente que forma el formulario que se encuentra el crear_modificar_perfil.jsp
				this.getSessionContainer(httpServletRequest).setUtil2(elementosOcultos);

				perfilForm.setTipo("principal");
				perfilForm.setCodPerfil(perfilForm.getCodPerfil());
				perfilForm.setNomPerfil(perfilForm.getNomPerfil());
				perfilForm.setDesPerfil(perfilForm.getDesPerfil());
				perfilForm.setSeleccionarTodos(perfilForm.getSeleccionarTodos());
				// Se carga en la lista3 porque es un pop-up, las listas 1 y 2 podrian estar siendo utilizadas por la pantalla padre que ha abierto el pop-up
				this.getSessionContainer(httpServletRequest).setLista3(elementosArbol);

				result = actionMapping.findForward("PantallaCrearModificarPerfil");
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END CrearModificarPerfilAction.cargar()");
		} catch (ExceptionInstanciaNoHallada e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.cargar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.cargar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
		} catch (ExceptionFaltaParametro e) {
			httpServletRequest.setAttribute("mensajeError", "CrearModificarPerfilAction: " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.cargar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado la carga de parámetros de configuración");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return result;
	}

	public ActionForward cargarPopUpModificar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;
		PerfilForm perfilForm = (PerfilForm) actionForm;

		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "BEGIN CrearModificarPerfilAction.cargarPopUpModificar()");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_PERFIL_CREAR)) {
				IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();

				String codPerfil = httpServletRequest.getParameter("codPerfil");
				String nomPerfil = httpServletRequest.getParameter("nomPerfil");
				String desPerfil = httpServletRequest.getParameter("desPerfil");
				List elementosArbol = iGestionUsuarioFacade.arbolElementos(usrDTO, codPerfil);

				ResourceBundle properties = ResourceBundle.getBundle("i18n", getSessionLocale(httpServletRequest));

				String elementos = (ElementosPerfil.getInstance()).getArbolFeudos(httpServletRequest.getContextPath(), elementosArbol, properties);
				String elementosOcultos = (ElementosPerfil.getInstance()).getArbolFeudosOcultos(elementosArbol);

				// El primer String contiene el html generado dinamicamente que se cargara en el frame: lista_elementos.jsp
				this.getSessionContainer(httpServletRequest).setUtil(elementos);
				// Este segundo String (util2) contiene el html generado dinamicamente que forma el formulario que se encuentra el crear_modificar_perfil.jsp
				this.getSessionContainer(httpServletRequest).setUtil2(elementosOcultos);

				perfilForm.setTipo("popUpModificar");
				perfilForm.setCodPerfil(codPerfil);
				perfilForm.setNomPerfil(nomPerfil);
				perfilForm.setDesPerfil(desPerfil);
				perfilForm.setSeleccionarTodos(perfilForm.getSeleccionarTodos());

				// Se carga en la lista3 porque es un pop-up, las listas 1 y 2 podrian estar siendo utilizadas por la pantalla padre que ha abierto el pop-up
				this.getSessionContainer(httpServletRequest).setLista3(elementosArbol);

				result = actionMapping.findForward("PantallaCrearModificarPerfil");
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END CrearModificarPerfilAction.cargarPopUpModificar()");

		} catch (ExceptionInstanciaNoHallada e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.cargarPopUpModificar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.cargarPopUpModificar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
		} catch (ExceptionFaltaParametro e) {
			httpServletRequest.setAttribute("mensajeError", "CrearModificarPerfilAction: " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.cargarPopUpModificar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado la carga de parámetros de configuración");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return result;
	}

	// Funcion que selecciona todos los elementos del arbol
	private void arbolSeleccionadoEntero(List nivel) {
		for (int i = 0; i < nivel.size(); i++) {
			ElementoDetallesDTOArbol nodo = ((ElementoDetallesDTOArbol) nivel.get(i));
			nodo.setSeleccionado("true");
			List siguienteNivel = nodo.getSubArbol();
			if (!siguienteNivel.isEmpty())
				arbolSeleccionadoEntero(siguienteNivel);
		}
	}

	// Funcion que deselecciona todos los elementos del arbol
	private void arbolNoSeleccionadoEntero(List nivel) {
		for (int i = 0; i < nivel.size(); i++) {
			ElementoDetallesDTOArbol nodo = ((ElementoDetallesDTOArbol) nivel.get(i));
			nodo.setSeleccionado("false");
			List siguienteNivel = nodo.getSubArbol();
			if (!siguienteNivel.isEmpty())
				arbolNoSeleccionadoEntero(siguienteNivel);
		}
	}

	// Action que se invoca cuando se cheque el checkbox de seleccion de todos los elementos
	public ActionForward seleccionarTodos(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;
		PerfilForm perfilForm = (PerfilForm) actionForm;

		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "BEGIN CrearModificarPerfilAction.seleccionarTodos()");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_PERFIL_CREAR)) {
				IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();
				List elementosArbol = iGestionUsuarioFacade.arbolElementos(usrDTO, "no perfil");

				if (new Boolean(perfilForm.getSeleccionarTodos()).booleanValue()) {
					arbolSeleccionadoEntero(elementosArbol);
				} else {
					arbolNoSeleccionadoEntero(elementosArbol);
				}

				perfilForm.setCodPerfil(perfilForm.getCodPerfil());
				perfilForm.setNomPerfil(perfilForm.getNomPerfil());
				perfilForm.setDesPerfil(perfilForm.getDesPerfil());
				perfilForm.setTipo(perfilForm.getTipo());
				perfilForm.setSeleccionarTodos(perfilForm.getSeleccionarTodos());

				ResourceBundle properties = ResourceBundle.getBundle("i18n", getSessionLocale(httpServletRequest));

				String elementos = (ElementosPerfil.getInstance()).getArbolFeudos(httpServletRequest.getContextPath(), elementosArbol, properties);

				String elementosOcultos = (ElementosPerfil.getInstance()).getArbolFeudosOcultos(elementosArbol);

				this.getSessionContainer(httpServletRequest).setUtil(elementos);
				this.getSessionContainer(httpServletRequest).setUtil2(elementosOcultos);

				// Se carga en la lista3 porque es un pop-up, las listas 1 y 2 podrian estar siendo utilizadas por la pantalla padre que ha abierto el pop-up
				this.getSessionContainer(httpServletRequest).setLista3(elementosArbol);

				result = actionMapping.findForward("PantallaCrearModificarPerfil");
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END CrearModificarPerfilAction.seleccionarTodos()");
		} catch (ExceptionInstanciaNoHallada e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.seleccionarTodos()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.seleccionarTodos()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
		} catch (ExceptionFaltaParametro e) {
			httpServletRequest.setAttribute("mensajeError", "CrearModificarPerfilAction: " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.seleccionarTodos()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado la carga de parámetros de configuración");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return result;
	}

	// Funcion que fue utilizada a la hora de hacer debug
	// Muestra por pantalla el arbol
	private void imprimirArbol(List nivel, int contNivel) {
		for (int i = 0; i < nivel.size(); i++) {
			ElementoDetallesDTOArbol nodo = ((ElementoDetallesDTOArbol) nivel.get(i));
			List siguienteNivel = nodo.getSubArbol();
			if (!siguienteNivel.isEmpty())
				imprimirArbol(siguienteNivel, contNivel + 1);
		}
	}

	// Esta funcion recorre el arbol guardado en la sesion y busca en la request
	// si su valor a cambiado en cuyo caso lo actualiza.
	private boolean actualizarArbol(List nivel, HttpServletRequest httpServletRequest) {
		boolean perfilTieneElementos = false;
		for (int i = 0; i < nivel.size(); i++) {
			ElementoDetallesDTOArbol nodo = ((ElementoDetallesDTOArbol) nivel.get(i));
			ElementoDetallesDTO elemento = nodo.getElementoDetallesDTO();
			String codElemento = elemento.getCodElemento();
			String chequeado = httpServletRequest.getParameter(codElemento);

			if (chequeado != null) {
				nodo.setSeleccionado(chequeado);
				if (chequeado.equals("true")) {
					perfilTieneElementos = true;
				}
			}

			List siguienteNivel = nodo.getSubArbol();
			if (!siguienteNivel.isEmpty()) {
				actualizarArbol(siguienteNivel, httpServletRequest);
			}
		}

		return perfilTieneElementos;
	}

	// Action invocada en el formulario de crear_modificar_perfil.jsp
	// Se encarga de modificar el perfil correspondiente o de crear uno nuevo.
	public ActionForward insertar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;
		PerfilForm perfilForm = (PerfilForm) actionForm;

		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "BEGIN CrearModificarPerfilAction.insertar()");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_PERFIL_CREAR)) {
				IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();

				List elementosArbol = this.getSessionContainer(httpServletRequest).getLista3();

				// Arbol de elementos sin perfiles
				List elementosArbolNoPerfil = iGestionUsuarioFacade.arbolElementos(usrDTO, "no perfil");

				String codPerfil = perfilForm.getCodPerfil();
				String nomPerfil = perfilForm.getNomPerfil();
				String desPerfil = perfilForm.getDesPerfil();
				perfilForm.setSeleccionarTodos(perfilForm.getSeleccionarTodos());
				boolean actualizar = actualizarArbol(elementosArbol, httpServletRequest);

				// Si el usuario no selecciono ningun elemento no se podra crear/modificar el perfil
				if (!actualizar) {
					perfilForm.setConfirmar("advertencia.perfil.sin.elementos");
					return result = actionMapping.findForward("PantallaCrearModificarPerfil");
				} else {
					perfilForm.setConfirmar("true");
					imprimirArbol(elementosArbol, 0);

					String forward;

					// Si no tiene cod perfil es un nuevo perfil
					if ((codPerfil == null) || (codPerfil == "")) {
						iGestionUsuarioFacade.crearPerfilElementos(usrDTO, new PerfilDTO(nomPerfil, desPerfil), elementosArbol);
						forward = "PantallaCrearModificarPerfil";
					} else {
						iGestionUsuarioFacade.updatePerfilElementos(usrDTO, codPerfil, new PerfilDTO(codPerfil, nomPerfil, desPerfil), elementosArbol);

						httpServletRequest.setAttribute("mensaje", "mensaje.perfil.modificado.correctamente");
						forward = "NuevaBusqueda";
					}

					this.getSessionContainer(httpServletRequest).setLista3(elementosArbol);

					ResourceBundle properties = ResourceBundle.getBundle("i18n", getSessionLocale(httpServletRequest));

					String elementos = (ElementosPerfil.getInstance()).getArbolFeudos(httpServletRequest.getContextPath(), elementosArbolNoPerfil, properties);

					this.getSessionContainer(httpServletRequest).setUtil(elementos);

					result = actionMapping.findForward(forward);
				}
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END CrearModificarPerfilAction.insertar()");
		} catch (ExceptionInstanciaNoHallada e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.insertar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		} catch (ExceptionInstanciaDuplicada e) {
			perfilForm.setConfirmar("advertencia.perfil.duplicado");
			result = actionMapping.findForward("PantallaCrearModificarPerfil");

		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.insertar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);

		} catch (ExceptionFaltaParametro e) {
			httpServletRequest.setAttribute("mensajeError", "CrearModificarPerfilAction: " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.insertar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado la carga de parámetros de configuración");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return result;
	}

	public ActionForward limpiar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ActionForward result = null;

		PerfilForm perfilForm = (PerfilForm) actionForm;

		// Si el usuario no esta validado retorna la pagina de no validado
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
		}

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

		Log.logRT(usrDTO.getUsr(), "BEGIN CrearModificarPerfilAction.limpiar()");

		try {
			if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_PERFIL_CREAR)) {
				IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();

				List elementosArbol = iGestionUsuarioFacade.arbolElementos(usrDTO, "no perfil");

				ResourceBundle properties = ResourceBundle.getBundle("i18n", getSessionLocale(httpServletRequest));

				String elementos = (ElementosPerfil.getInstance()).getArbolFeudos(httpServletRequest.getContextPath(), elementosArbol, properties);
				String elementosOcultos = (ElementosPerfil.getInstance()).getArbolFeudosOcultos(elementosArbol);

				this.getSessionContainer(httpServletRequest).setUtil(elementos);
				this.getSessionContainer(httpServletRequest).setUtil2(elementosOcultos);

				perfilForm.setTipo("principal");
				perfilForm.setCodPerfil(perfilForm.getCodPerfil());
				perfilForm.setNomPerfil(perfilForm.getNomPerfil());
				perfilForm.setDesPerfil(perfilForm.getDesPerfil());
				perfilForm.setSeleccionarTodos(perfilForm.getSeleccionarTodos());
				this.getSessionContainer(httpServletRequest).setLista3(elementosArbol);

				result = actionMapping.findForward("PantallaCrearModificarPerfil");
			} else {
				result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
			}

			Log.logRT(usrDTO.getUsr(), "END CrearModificarPerfilAction.limpiar()");
		} catch (ExceptionInstanciaNoHallada e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.limpiar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		} catch (ExceptionErrorInterno e) {
			httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.limpiar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado el establecimiento de la conexión a la BD.");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
		} catch (ExceptionFaltaParametro e) {
			httpServletRequest.setAttribute("mensajeError", "CrearModificarPerfilAction: " + e.getMessage());
			Log.logRE(usrDTO.getUsr(), "CrearModificarPerfilAction.limpiar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
					"Ha fallado la carga de parámetros de configuración");
			return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
		}

		return result;
	}

} // class