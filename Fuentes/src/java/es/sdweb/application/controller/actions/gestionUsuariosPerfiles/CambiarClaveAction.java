package es.sdweb.application.controller.actions.gestionUsuariosPerfiles;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles.CambiarClaveForm;
import es.sdweb.application.controller.actions.util.BaseAction;
import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.controller.util.UserContainer;
import es.sdweb.application.model.dto.ElementoDetallesDTO;
import es.sdweb.application.model.dto.LoginDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.facade.FabricaFacades;
import es.sdweb.application.model.facade.IGestionUsuarioFacade;
import es.sdweb.application.util.ExceptionFaltaParametro;
import es.sdweb.application.vista.util.ElementosMenu;

/**
 * Esta clase gestiona el formulario de cambio de contraseña.
 * Si no se recibe ningún dato solo se muestra el formulario, en caso contrario de intenta realizar
 * un cambio de contraseña y se actúa según el resultado.
 *
 * @author Sdweb
 * @see BaseAction
 */
public class CambiarClaveAction extends BaseAction {
	// Usuario para los registros de logs
	UsrDTO usrDTO = new UsrDTO();

	@Override
	public ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		// Inicialización de variables
		ActionForward result = null;
		CambiarClaveForm cambiarClaveForm = (CambiarClaveForm) actionForm;
		HashMap acceso = new HashMap();
		UserContainer user = new UserContainer();

		// Comprobamos si se ha enviado el formulario
		if(cambiarClaveForm.getLogin() == null) {
			// En caso de que no es que simplemente se debe mostrar el formulario de cambio de contraseña
			result = actionMapping.findForward("PasswordChange");
		// Si se ha enviado
		} else {
			try {
				// Escribir en el log donde estamos
				Log.logRT(usrDTO.getUsr(), "BEGIN CambiarClaveAction.executeAction()");
				// Obtener el nombre de usuario
				usrDTO.setUsr(cambiarClaveForm.getLogin());

				// Obtener los datos de acceso del usuario (Usuario y contraseña actual)
				LoginDTO loginDTO = new LoginDTO(cambiarClaveForm.getLogin(), cambiarClaveForm.getClaveAntigua());
				// Obtenemos una instancia de la Facade de gestión de usuarios
				IGestionUsuarioFacade iGestionUsuariosFacade = FabricaFacades.createGestionUsuarioFacade();

				// Validamos que todos los campos esten cubiertos
				String advertencia = cambiarClaveForm.validar(actionMapping, httpServletRequest);
				// Si hubo algún error
				if (!advertencia.equals("")) {
					// Establecemos la advertencia del formulario
					cambiarClaveForm.setAdvertencia(advertencia);
					// Y mostramos el formulario de nuevo
					result = actionMapping.findForward("PasswordChange");
				// Si no hay ningún error en el formulario
				} else {
					// Comprobamos que el usuario y la contraseña sean correctos
					if (iGestionUsuariosFacade.registro(usrDTO, loginDTO)) {
						// Comprobamos que las nuevas contraseñas coincidan
						if (cambiarClaveForm.getClaveNueva().equals(cambiarClaveForm.getClaveNuevaConf())) {
							// Si la nueva contraseña es menor que el tamaño mínimo
							if (cambiarClaveForm.getClaveNueva().length() < 2) {
								// Establecemos la advertencia
								cambiarClaveForm.setAdvertencia("advertencia.password.longitud.incorrecta");
								// Y volvemos a mostrar la página de cambio de contraseña
								result = actionMapping.findForward("PasswordChange");
							// Si todo es correcto
							} else {
								// Obtenemos el menú
								List elementosArbol = iGestionUsuariosFacade.accesoMenu(usrDTO, loginDTO.getLogin());
								// Obtenemos los permisos del usuario
								List permisos = iGestionUsuariosFacade.permisos(usrDTO, loginDTO.getLogin());

								// En esta seccion de codigo se genera el HTML del menu a
								// partir del fichero de traducciones y la ubicacion de los
								// permisos de tipo menu
								String menu = (ElementosMenu.getInstance()).getArbolMenu(httpServletRequest.getContextPath(), elementosArbol);

								// Se engancha a la sesion el login del usuario y los
								// elementos a los que tiene acceso
								accesoPermisos(permisos, acceso);
								user.setUsuario(usrDTO);
								user.setAcceso(acceso);
								this.getSessionContainer(httpServletRequest).setUserContainer(user);
								this.getSessionContainer(httpServletRequest).setMenu(menu);

								// Se guarda la nueva contraseña del usuario
								iGestionUsuariosFacade.modificarClaveUsuario(usrDTO, cambiarClaveForm.getLogin(), cambiarClaveForm.getClaveNueva());

								// Se redirige a la portada
								result = actionMapping.findForward("Principal");
							}
						} else {
							cambiarClaveForm.setAdvertencia("advertencia.confirmacion.clave");
							cambiarClaveForm.setClaveNueva(null);
							cambiarClaveForm.setClaveNuevaConf(null);
							result = actionMapping.findForward("PasswordChange");
						}
					} else {
						cambiarClaveForm.setAdvertencia("advertencia.password.incorrecto");
						cambiarClaveForm.setClaveAntigua(null);
						cambiarClaveForm.setClaveNueva(null);
						cambiarClaveForm.setClaveNuevaConf(null);
						result = actionMapping.findForward("PasswordChange");
					}
				}

				Log.logRT(usrDTO.getUsr(), "END CambiarClaveAction.executeAction()");

			} catch (ExceptionInstanciaNoHallada e) {
				cambiarClaveForm.setAdvertencia("advertencia.login.incorrecto");
				cambiarClaveForm.setClaveAntigua(null);
				cambiarClaveForm.setClaveNueva(null);
				cambiarClaveForm.setClaveNuevaConf(null);

				result = actionMapping.findForward("PasswordChange");

				Log.logRE(usrDTO.getUsr(), "CambiarClaveAction.executeAction()", Log.TIPO_APLICACION, Log.CRITICIDAD_NORMAL, 4202,
						"No se ha encontrado el login buscado en la BD.");
			} catch (ExceptionFaltaParametro e) {
				httpServletRequest.setAttribute("mensajeError", "CambiarClaveAction: " + e.getMessage());
				Log.logRE(usrDTO.getUsr(), "CambiarClaveAction.executeAction()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4203,
						"Ha fallado el establecimiento de la conexion a la BD.");
				return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
			} catch (ExceptionErrorInterno e) {
				httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
				Log.logRE(usrDTO.getUsr(), "CambiarClaveAction.executeAction()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4204,
						"Ha fallado el establecimiento de la conexion a la BD. Revise la configuracion de los Data Sources requeridos por GESCUL.");
				return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);
			}
		}

		return result;
	}

	private void accesoPermisos(List nivel, HashMap acceso) {
		for (int i = 0; i < nivel.size(); i++) {
			ElementoDetallesDTO elemento = ((ElementoDetallesDTO) nivel.get(i));
			acceso.put(elemento.getCodElemento().trim(), "true");
		}
	}
} // class
