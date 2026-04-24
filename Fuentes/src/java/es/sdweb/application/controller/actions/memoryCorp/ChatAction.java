package es.sdweb.application.controller.actions.memoryCorp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.memoryCorp.ChatForm;
import es.sdweb.application.controller.actions.util.BaseAction;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.vista.util.GestorInformacionWeb;
import es.sdweb.memorycorp.core.LearningEngine;
import es.sdweb.memorycorp.nlpengine.SpanishDictionary;

/**
 * Implementa la funcionalidad chat.
 */
public class ChatAction extends BaseAction {

	@Override
	public ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward("Login");
		}

		ActionForward result = null;

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "---------------------------------------------------------------------------------------------------------------------");
		Log.logRT(usrDTO.getUsr(), "BEGIN ChatAction.execute()");


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



		ChatForm chatForm = (ChatForm) actionForm;

		String accion = GestorInformacionWeb.getParameter(httpServletRequest, "accion");


		if ( (accion != null) && (accion.equals("procesar")) ) {

System.out.println("executeAction + procesar");

String userInput = httpServletRequest.getParameter("userInput");
System.out.println("Requested Values ::"+ userInput);


			// Obtener respuesta
			String answer = dialogueEngine.getAnswer(userInput);

			// Escapar resultado
			String escaped = answer;
		    escaped = escaped.replace("\\", "\\\\");
		    escaped = escaped.replace("\"", "\\\"");
		    escaped = escaped.replace("\b", "\\b");
		    escaped = escaped.replace("\f", "\\f");
		    escaped = escaped.replace("\n", "\\n");
		    escaped = escaped.replace("\r", "\\r");
		    escaped = escaped.replace("\t", "\\t");
		    // TODO: escape other non-printing characters using uXXXX notation
		    ;


			//chatForm.setTextoSalida("{\"responseMessage\": \"Texto de salida del chat es: "+ userInput +"\"}");
			chatForm.setTextoSalida("{\"responseMessage\": \""+ escaped +"\"}");

			result = actionMapping.findForward("JsonChat");

		} else {
			result = actionMapping.findForward("PantallaChat");
		}

		Log.logRT(usrDTO.getUsr(), "END ChatAction.execute()");

		return result;
	}

} // class
