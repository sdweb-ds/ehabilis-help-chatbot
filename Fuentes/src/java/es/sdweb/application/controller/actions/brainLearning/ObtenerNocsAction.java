package es.sdweb.application.controller.actions.brainLearning;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.brainLearning.ObtenerNocsForm;
import es.sdweb.application.controller.actions.util.BaseAction;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.vista.util.GestorInformacionWeb;
import es.sdweb.memorycorp.core.LearningEngine;
import es.sdweb.memorycorp.nandanicnoc.NandaDefinitions;
import es.sdweb.memorycorp.nlpengine.NLPEngine;
import es.sdweb.memorycorp.nlpengine.SpanishDictionary;

/**
 * Implementa la funcionalidad Obtener nocs (comando noc).
 */
public class ObtenerNocsAction extends BaseAction {

	@Override
	public ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward("Login");
		}

		ActionForward result = null;

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "---------------------------------------------------------------------------------------------------------------------");
		Log.logRT(usrDTO.getUsr(), "BEGIN ObtenerNocsAction.execute()");


		// Inicializar learningEngine si no está ya inicializado
		if (learningEngine == null) {
			LogUtil.setLogTrazas(true); //Activamos el log de trazas
	        LogUtil.setNivelTraza(8); //Establecemos el nivel de la visibilidad de trazas
	        LogUtil.logTraza("[INICIALIZACION FASE_0] Logs activados.",0);

	        LogUtil.logTraza("[INICIALIZACION FASE_1] Inicializando diccionario de Español.",5);
	        SpanishDictionary.initializeDictionary(); //Cargamos el diccionario de Castellano
	        LogUtil.logTraza("[INICIALIZACION FASE_1] Inicializando LearningEngine.",5);
	        learningEngine = new LearningEngine(SpanishDictionary.getDictionary()); //Creamos el Engine de IA
		}


		NLPEngine engineNLP = learningEngine.getEngine();


		ObtenerNocsForm obtenerNocsForm = (ObtenerNocsForm) actionForm;

		String accion = GestorInformacionWeb.getParameter(httpServletRequest, "accion");


		if ( (accion != null) && (accion.equals("procesar")) ) {

			String nocs=null;

			if ( (obtenerNocsForm != null) && (obtenerNocsForm.getTextoEntrada() != null) && (!obtenerNocsForm.getTextoEntrada().isEmpty()) ) {

				String userInput = StringUtil.trim(obtenerNocsForm.getTextoEntrada()); //Leemos una entrada del usuario

                nocs = NandaDefinitions.getNocString(userInput);

             } else {
                nocs = NandaDefinitions.getNocsString();
             }

			String salida = nocs;
			obtenerNocsForm.setTextoSalida(salida);

		} else {
			obtenerNocsForm.setTextoSalida(null);
		}

		result = actionMapping.findForward("PantallaObtenerNocs");
		Log.logRT(usrDTO.getUsr(), "END ObtenerNocsAction.execute()");

		return result;
	}

} // class
