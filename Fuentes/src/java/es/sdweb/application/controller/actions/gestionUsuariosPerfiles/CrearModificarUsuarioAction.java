package es.sdweb.application.controller.actions.gestionUsuariosPerfiles;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles.CrearModificarUsuarioForm;
import es.sdweb.application.controller.actions.util.BaseDispatchAction;
import es.sdweb.application.controller.config.IConstantes;
import es.sdweb.application.model.dto.PerfilDTO;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.model.dto.UsuarioDTO;
import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.model.exceptions.ExceptionFormatoDeDatos;
import es.sdweb.application.model.exceptions.ExceptionInstanciaDuplicada;
import es.sdweb.application.model.exceptions.ExceptionInstanciaNoHallada;
import es.sdweb.application.model.exceptions.ExceptionUniqueLogin;
import es.sdweb.application.model.facade.FabricaFacades;
import es.sdweb.application.model.facade.IGestionUsuarioFacade;
import es.sdweb.application.util.ExceptionFaltaParametro;

/**
 * @author Antonio Carro Mariño
 *
 */
public class CrearModificarUsuarioAction extends BaseDispatchAction {

    /**
     *
     * @param actionMapping
     * @param actionForm
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    public ActionForward cargar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        ActionForward result = null;

        // Si el usuario no esta validado retorna la pagina de no validado
        if (!isLoggedIn(httpServletRequest)) {
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
        }

        CrearModificarUsuarioForm crearModificarUsuarioForm = (CrearModificarUsuarioForm) actionForm;

        UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

        Log.logRT(usrDTO.getUsr(), "BEGIN CrearModificarUsuarioAction.cargar()");

        try {

            if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_USUARIO_CREAR)) {
                IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();
                List perfilNoAsociadosDTOs = null;

                perfilNoAsociadosDTOs = iGestionUsuarioFacade.buscarPerfiles(usrDTO);

                crearModificarUsuarioForm.setCodUsuario("");
                crearModificarUsuarioForm.setAlta("true");
                crearModificarUsuarioForm.setLogin("");
                crearModificarUsuarioForm.setNif("");
                crearModificarUsuarioForm.setNombre("");
                crearModificarUsuarioForm.setApellido1("");
                crearModificarUsuarioForm.setApellido2("");
                crearModificarUsuarioForm.setPassword("");
                crearModificarUsuarioForm.setConfirmarPassword("");
                crearModificarUsuarioForm.setTelefono("");
                crearModificarUsuarioForm.setEs_colaborador("");

                crearModificarUsuarioForm.setPerfilesNoAsociados(perfilNoAsociadosDTOs);
                List perfilesAsociados = new ArrayList();
                crearModificarUsuarioForm.setPerfilesAsociados(perfilesAsociados);
                this.getSessionContainer(httpServletRequest).setLista1(perfilNoAsociadosDTOs);
                this.getSessionContainer(httpServletRequest).setLista2(perfilesAsociados);

                // Cargar el listado de idiomas diponibles
                crearModificarUsuarioForm.setIdiomasDisponibles(getListadoIdiomasDisponibles(httpServletRequest));

                result = actionMapping.findForward("PantallaCrearModificarUsuario");
            } else {
                result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
            }

            Log.logRT(usrDTO.getUsr(), "END CrearModificarUsuarioAction.cargar()");

        } catch (ExceptionErrorInterno e) {
            httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.cargar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado el establecimiento de la conexión a la BD.");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);

        } catch (ExceptionFaltaParametro e) {
            httpServletRequest.setAttribute("mensajeError", "CrearModificarUsuarioAction: " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.cargar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado la carga de parámetros de configuración");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
        }

        return result;
    }

    /**
     * Carga los valores que se hubiesen registrado para el usuario en la ventana crear_modificar_usuario.jsp
     * @param actionMapping
     * @param actionForm
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    //Usa dos atributos del ambito de sesion, son las dos lista que estan dentro del contenedor de sesion
    public ActionForward cargarModificar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        ActionForward result = null;

        //Si el usuario no esta validado retorna la pagina de no validado
        if (!isLoggedIn(httpServletRequest)) {
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
        }

        CrearModificarUsuarioForm crearModificarUsuarioForm = (CrearModificarUsuarioForm) actionForm;

        UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

        Log.logRT(usrDTO.getUsr(), "BEGIN CrearModificarUsuarioAction.cargarModificar()");

        try {

            if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_USUARIO_CREAR)) {
                IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();
                String login = httpServletRequest.getParameter("login");

                UsuarioDTO usuarioDTO = iGestionUsuarioFacade.buscarUsuario(usrDTO, login);
                crearModificarUsuarioForm.setAlta("false");
                crearModificarUsuarioForm.setCodUsuario(usuarioDTO.getCodUsuario());
                crearModificarUsuarioForm.setLogin(usuarioDTO.getLogin());
                crearModificarUsuarioForm.setNif(usuarioDTO.getNif());
                crearModificarUsuarioForm.setNombre(usuarioDTO.getNombre());
                crearModificarUsuarioForm.setApellido1(usuarioDTO.getApellido1());
                crearModificarUsuarioForm.setApellido2(usuarioDTO.getApellido2());
                crearModificarUsuarioForm.setPassword(usuarioDTO.getPassword());
                crearModificarUsuarioForm.setConfirmarPassword(usuarioDTO.getPassword());
                crearModificarUsuarioForm.setTelefono(usuarioDTO.getTelefono());
                crearModificarUsuarioForm.setCorreo(usuarioDTO.getCorreo());
                crearModificarUsuarioForm.setIdioma(usuarioDTO.getIdioma());

                List perfilAsociadosDTOs = iGestionUsuarioFacade.PerfilesAsociadosAUsuario(usrDTO, usuarioDTO.getCodUsuario());
                List perfilNoAsociadosDTOs = iGestionUsuarioFacade.PerfilesNoAsociadosAUsuario(usrDTO, usuarioDTO.getCodUsuario());
                crearModificarUsuarioForm.setPerfilesAsociados(perfilAsociadosDTOs);
                crearModificarUsuarioForm.setPerfilesNoAsociados(perfilNoAsociadosDTOs);
                this.getSessionContainer(httpServletRequest).setLista1(perfilNoAsociadosDTOs);
                this.getSessionContainer(httpServletRequest).setLista2(perfilAsociadosDTOs);

                // Cargar el listado de idiomas diponibles
                crearModificarUsuarioForm.setIdiomasDisponibles(getListadoIdiomasDisponibles(httpServletRequest));

                result = actionMapping.findForward("PantallaCrearModificarUsuario");
            } else {
                result = actionMapping.findForward(IConstantes.FWD_GLOBAL_ACCESODENEGADO);
            }

            Log.logRT(usrDTO.getUsr(), "END CrearModificarUsuarioAction.cargarModificar()");

        } catch (ExceptionFormatoDeDatos e) {
            crearModificarUsuarioForm.setAdvertencia("advertencia.fecha.formato.buscar");
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.cargarModificar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado el establecimiento de la conexión a la BD.");
            return result = actionMapping.findForward("PantallaCrearModificarUsuario");

        } catch (ExceptionInstanciaNoHallada e) {
            httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.cargarModificar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado el establecimiento de la conexión a la BD.");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);

        } catch (ExceptionErrorInterno e) {
            httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.cargarModificar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado el establecimiento de la conexión a la BD.");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);

        } catch (ExceptionFaltaParametro e) {
            httpServletRequest.setAttribute("mensajeError", "CrearModificarUsuarioAction: " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.cargarModificar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado la carga de parámetros de configuración");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
        }

        return result;
    }

    public ActionForward asociar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        ActionForward result = null;

        // Si el usuario no esta validado retorna la pagina de no validado
        if (!isLoggedIn(httpServletRequest)) {
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
        }

        CrearModificarUsuarioForm crearModificarUsuarioForm = (CrearModificarUsuarioForm) actionForm;

        UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

        Log.logRT(usrDTO.getUsr(), "BEGIN CrearModificarUsuarioAction.asociar()");

        try {
            if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_USUARIO_CREAR)) {

                List perfilesNoAsociados = this.getSessionContainer(httpServletRequest).getLista1();
                List perfilesAsociados = this.getSessionContainer(httpServletRequest).getLista2();

                //String codPerfilNoAsociado = crearModificarUsuarioForm.getCodPerfilNoAsociado();
                String[] codPerfilesNoAsociados = crearModificarUsuarioForm.getCodPerfilesNoAsociados();

                crearModificarUsuarioForm.setAlta(crearModificarUsuarioForm.getAlta());

                if (codPerfilesNoAsociados != null) {
                    for (int j=0; j < codPerfilesNoAsociados.length; j++) {

                        String codPerfilNoAsociado = codPerfilesNoAsociados[j];

                        boolean encontrado = false;
                        int i = 0;

                        do {
                            if (codPerfilNoAsociado.compareTo(((PerfilDTO) perfilesNoAsociados.get(i++)).getCodPerfil()) == 0) {
                                encontrado = true;
                            }

                        } while ((!encontrado) && (i < perfilesNoAsociados.size()));

                        if (encontrado) {
                            PerfilDTO perfilNoAsociado = (PerfilDTO) perfilesNoAsociados.get(--i);
                            perfilesNoAsociados.remove(i);
                            perfilesAsociados.add(perfilNoAsociado);

                        }
                    }
                }
                crearModificarUsuarioForm.setPerfilesNoAsociados(perfilesNoAsociados);
                crearModificarUsuarioForm.setPerfilesAsociados(perfilesAsociados);

                this.getSessionContainer(httpServletRequest).setLista1(perfilesNoAsociados);
                this.getSessionContainer(httpServletRequest).setLista2(perfilesAsociados);

                // Cargar el listado de idiomas diponibles
                crearModificarUsuarioForm.setIdiomasDisponibles(getListadoIdiomasDisponibles(httpServletRequest));


                // Respuesta Ajax
                httpServletResponse.setContentType("text/text;charset=utf-8");
                httpServletResponse.setHeader("cache-control", "no-cache");

                PrintWriter out = httpServletResponse.getWriter();

                // Como resultado se devuelve el listado de option de perfiles seleccionados y no seleccionados en formato json
                String resultado = "";
                resultado += "{\"no_asociados\":\"";

                Iterator<PerfilDTO> iteratorNoAsociados = perfilesNoAsociados.iterator();
                while (iteratorNoAsociados.hasNext()) {
                    PerfilDTO perfilDTOAux = iteratorNoAsociados.next();

                    resultado += "<option class=\\\"input\\\" value=\\\""+perfilDTOAux.getCodPerfil()+"\\\">"+perfilDTOAux.getNomPerfil()+"</option>";
                }

                resultado += "\",\"asociados\":\"";

                Iterator<PerfilDTO> iteratorAsociados = perfilesAsociados.iterator();
                while (iteratorAsociados.hasNext()) {
                    PerfilDTO perfilDTOAux = iteratorAsociados.next();

                    resultado += "<option class=\\\"input\\\" value=\\\""+perfilDTOAux.getCodPerfil()+"\\\">"+perfilDTOAux.getNomPerfil()+"</option>";
                }

                resultado += "\"}";

                out.println(resultado);

                out.flush();
            }
        } catch (Exception e) {
            httpServletRequest.setAttribute("mensajeError", "CrearModificarUsuarioAction: " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.asociar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado el establecimiento de la conexión a la BD.");

            // return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
            return null;
        }

        Log.logRT(usrDTO.getUsr(), "END CrearModificarUsuarioAction.asociar()");

        return result;
    }

    public ActionForward desAsociar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        ActionForward result = null;

        // Si el usuario no esta validado retorna la pagina de no validado
        if (!isLoggedIn(httpServletRequest)) {
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
        }

        CrearModificarUsuarioForm crearModificarUsuarioForm = (CrearModificarUsuarioForm) actionForm;

        UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

        Log.logRT(usrDTO.getUsr(), "BEGIN CrearModificarUsuarioAction.desAsociar()");

        try {

            if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_USUARIO_CREAR)) {

                List perfilesNoAsociados = this.getSessionContainer(httpServletRequest).getLista1();
                List perfilesAsociados = this.getSessionContainer(httpServletRequest).getLista2();

                // String codPerfilAsociado = crearModificarUsuarioForm.getCodPerfilAsociado();
                String[] codPerfilesAsociados = crearModificarUsuarioForm.getCodPerfilesAsociados();

                crearModificarUsuarioForm.setAlta(crearModificarUsuarioForm.getAlta());

                if (codPerfilesAsociados != null) {

                    for (int j=0; j < codPerfilesAsociados.length; j++) {

                        String codPerfilAsociado = codPerfilesAsociados[j];

                        //Busqueda del perfil no asociado
                        boolean encontrado = false;
                        int i = 0;

                        do {
                            if (codPerfilAsociado.compareTo(((PerfilDTO) perfilesAsociados.get(i++)).getCodPerfil()) == 0) {
                                encontrado = true;
                            }

                        } while ((!encontrado) && (i < perfilesAsociados.size()));

                        if (encontrado) {
                            PerfilDTO perfilAsociado = (PerfilDTO) perfilesAsociados.get(--i);
                            perfilesAsociados.remove(i);
                            perfilesNoAsociados.add(perfilAsociado);

                        }
                    }
                }

                crearModificarUsuarioForm.setPerfilesNoAsociados(perfilesNoAsociados);
                crearModificarUsuarioForm.setPerfilesAsociados(perfilesAsociados);

                this.getSessionContainer(httpServletRequest).setLista1(perfilesNoAsociados);
                this.getSessionContainer(httpServletRequest).setLista2(perfilesAsociados);

                // Cargar el listado de idiomas diponibles
                crearModificarUsuarioForm.setIdiomasDisponibles(getListadoIdiomasDisponibles(httpServletRequest));


                // Respuesta Ajax
                httpServletResponse.setContentType("text/text;charset=utf-8");
                httpServletResponse.setHeader("cache-control", "no-cache");

                PrintWriter out = httpServletResponse.getWriter();

                // Como resultado se devuelve el listado de option de perfiles seleccionados y no seleccionados en formato json
                String resultado = "";
                resultado += "{\"no_asociados\":\"";

                Iterator<PerfilDTO> iteratorNoAsociados = perfilesNoAsociados.iterator();
                while (iteratorNoAsociados.hasNext()) {
                    PerfilDTO perfilDTOAux = iteratorNoAsociados.next();

                    resultado += "<option class=\\\"input\\\" value=\\\""+perfilDTOAux.getCodPerfil()+"\\\">"+perfilDTOAux.getNomPerfil()+"</option>";
                }

                resultado += "\",\"asociados\":\"";

                Iterator<PerfilDTO> iteratorAsociados = perfilesAsociados.iterator();
                while (iteratorAsociados.hasNext()) {
                    PerfilDTO perfilDTOAux = iteratorAsociados.next();

                    resultado += "<option class=\\\"input\\\" value=\\\""+perfilDTOAux.getCodPerfil()+"\\\">"+perfilDTOAux.getNomPerfil()+"</option>";
                }

                resultado += "\"}";

                out.println(resultado);

                out.flush();
            }

        } catch (Exception e) {
            httpServletRequest.setAttribute("mensajeError", "CrearModificarUsuarioAction: " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.desAsociar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado el establecimiento de la conexión a la BD.");

            // return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
            return null;
        }

        Log.logRT(usrDTO.getUsr(), "END CrearModificarUsuarioAction.desAsociar()");

        // result = actionMapping.findForward("PantallaCrearModificarUsuario");

        return result;
    }

    public ActionForward insertar(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        ActionForward result = null;

        //Si el usuario no esta validado retorna la pagina de no validado
        if (!isLoggedIn(httpServletRequest)) {
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_NOLOGIN);
        }

        CrearModificarUsuarioForm crearModificarUsuarioForm = (CrearModificarUsuarioForm) actionForm;

        UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();

        Log.logRT(usrDTO.getUsr(), "BEGIN CrearModificarUsuarioAction.insertar()");

        try {

            if (acceso(httpServletRequest, IConstantes.ACCESO_MENU_USUARIO_CREAR)) {

                IGestionUsuarioFacade iGestionUsuarioFacade = FabricaFacades.createGestionUsuarioFacade();
                List perfiles = new ArrayList();
                perfiles = this.getSessionContainer(httpServletRequest).getLista2();

                // Comprobar que el password tenga, al menos, 2 digitos para que se haga el cifrado correctamente
                if (crearModificarUsuarioForm.getPassword().length() < 2) {
                    crearModificarUsuarioForm.setAdvertencia("advertencia.password.longitud.incorrecta");
                    List perfilesNoAsociados = this.getSessionContainer(httpServletRequest).getLista1();
                    List perfilesAsociados = this.getSessionContainer(httpServletRequest).getLista2();
                    crearModificarUsuarioForm.setPerfilesAsociados(perfilesAsociados);
                    crearModificarUsuarioForm.setPerfilesNoAsociados(perfilesNoAsociados);

                    // Cargar el listado de idiomas diponibles
                    crearModificarUsuarioForm.setIdiomasDisponibles(getListadoIdiomasDisponibles(httpServletRequest));

                    result = actionMapping.findForward("PantallaCrearModificarUsuario");
                }

                // Insercion de un nuevo usuario.
                if (StringUtil.isEmpty(crearModificarUsuarioForm.getCodUsuario())) {
                    UsuarioDTO usuarioDTO = new UsuarioDTO(null, crearModificarUsuarioForm.getIdioma(), crearModificarUsuarioForm.getLogin(),
                            crearModificarUsuarioForm.getPassword(), crearModificarUsuarioForm.getApellido1(), crearModificarUsuarioForm.getApellido2(),
                            crearModificarUsuarioForm.getNombre(), crearModificarUsuarioForm.getTelefono(), crearModificarUsuarioForm.getCorreo(),
                            crearModificarUsuarioForm.getNif());

                    iGestionUsuarioFacade.crearUsuarioPerfiles(usrDTO, usuarioDTO, perfiles);

                    result = actionMapping.findForward("PantallaCrearModificarUsuario");
                } else {
                    // Actualizacion de los datos relativos a un usuario

                    UsuarioDTO usuarioDTO = new UsuarioDTO(crearModificarUsuarioForm.getCodUsuario(), crearModificarUsuarioForm.getIdioma(),
                            crearModificarUsuarioForm.getLogin(), crearModificarUsuarioForm.getPassword(), crearModificarUsuarioForm.getApellido1(),
                            crearModificarUsuarioForm.getApellido2(), crearModificarUsuarioForm.getNombre(), crearModificarUsuarioForm.getTelefono(),
                            crearModificarUsuarioForm.getCorreo(), crearModificarUsuarioForm.getNif());

                    iGestionUsuarioFacade.modificarUsuario(usrDTO, usuarioDTO, perfiles);
                }
            }

        } catch (ExceptionUniqueLogin e) {
            crearModificarUsuarioForm.setAdvertencia("advertencia.unique.login");
            
            httpServletRequest.setAttribute("advertencia", crearModificarUsuarioForm.getAdvertencia());
            List perfilesNoAsociados = this.getSessionContainer(httpServletRequest).getLista1();
            List perfilesAsociados = this.getSessionContainer(httpServletRequest).getLista2();
            crearModificarUsuarioForm.setPerfilesAsociados(perfilesAsociados);
            crearModificarUsuarioForm.setPerfilesNoAsociados(perfilesNoAsociados);

            // Cargar el listado de idiomas diponibles
            crearModificarUsuarioForm.setIdiomasDisponibles(getListadoIdiomasDisponibles(httpServletRequest));

            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.insertar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "El login que ha introducido ya está siendo utilizado por otro usuario");
            return result = actionMapping.findForward("PantallaCrearModificarUsuario");

        } catch (ExceptionInstanciaDuplicada e) {
            httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.insertar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado el establecimiento de la conexión a la BD.");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
        } catch (ExceptionInstanciaNoHallada e) {
            httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.insertar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado el establecimiento de la conexión a la BD.");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
        } catch (ExceptionErrorInterno e) {
            httpServletRequest.setAttribute("mensajeError", e.getNombreClase() + ": " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.insertar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado el establecimiento de la conexión a la BD.");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR_BD);

        } catch (ExceptionFaltaParametro e) {
            httpServletRequest.setAttribute("mensajeError", "CrearModificarUsuarioAction: " + e.getMessage());
            Log.logRE(usrDTO.getUsr(), "CrearModificarUsuarioAction.insertar()", Log.TIPO_ACCESO_DISPONIBILIDAD, Log.CRITICIDAD_NORMAL, 4201,
                    "Ha fallado la carga de parámetros de configuración");
            return actionMapping.findForward(IConstantes.FWD_GLOBAL_ERROR);
        }

        //Al chequear este atributo la pagina JSP abrira la ventana con el mensaje de confirmacion de la insercion
        crearModificarUsuarioForm.setConfirmar("true");
        crearModificarUsuarioForm.setPerfilesAsociados(this.getSessionContainer(httpServletRequest).getLista2());
        crearModificarUsuarioForm.setPerfilesNoAsociados(this.getSessionContainer(httpServletRequest).getLista1());

        // Cargar el listado de idiomas diponibles
        crearModificarUsuarioForm.setIdiomasDisponibles(getListadoIdiomasDisponibles(httpServletRequest));


        return result;
    }

    /**
     * @param httpServletRequest
     * @return el listado de idiomas disponibles para generar el select para la creacion y modificacion de usuarios
     */
    private HashMap<String, String> getListadoIdiomasDisponibles (HttpServletRequest httpServletRequest) {
        HashMap<String, String> idiomasDisponibles = new HashMap<String, String>();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n", getSessionLocale(httpServletRequest));
        idiomasDisponibles.put(resourceBundle.getString("txt.idioma."+"es_ES"), "es_ES");
        idiomasDisponibles.put(resourceBundle.getString("txt.idioma."+"gl_ES"), "gl_ES");

        return idiomasDisponibles;
    }

}//class
