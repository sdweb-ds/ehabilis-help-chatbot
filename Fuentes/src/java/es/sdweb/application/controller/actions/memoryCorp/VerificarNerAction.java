package es.sdweb.application.controller.actions.memoryCorp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.memoryCorp.VerificarNerForm;
import es.sdweb.application.controller.actions.util.BaseAction;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.vista.util.GestorInformacionWeb;
import es.sdweb.memorycorp.core.LearningEngine;
import es.sdweb.memorycorp.nlpengine.DWord;
import es.sdweb.memorycorp.nlpengine.NLPEngine;
import es.sdweb.memorycorp.nlpengine.SpanishDictionary;

/**
 * Implementa la funcionalidad Verificar Ner (comando ner).
 */
public class VerificarNerAction extends BaseAction {

	@Override
	public ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward("Login");
		}

		ActionForward result = null;

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "---------------------------------------------------------------------------------------------------------------------");
		Log.logRT(usrDTO.getUsr(), "BEGIN VerificarNerAction.execute()");


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




		VerificarNerForm verificarNerForm = (VerificarNerForm) actionForm;

		String accion = GestorInformacionWeb.getParameter(httpServletRequest, "accion");



		if ( (accion != null) && (accion.equals("procesar")) ) {

			if ( (verificarNerForm != null) && (verificarNerForm.getTextoEntrada() != null) && (!verificarNerForm.getTextoEntrada().isEmpty()) ) {

				String userInput = StringUtil.trim(verificarNerForm.getTextoEntrada()); //Leemos una entrada del usuario


				DWord palabra=learningEngine.getDictionary().getNameOrLocation(userInput, null, null, false);
	                        String salida = "Es un nombre de persona/lugar: "+!StringUtil.isEmpty(StringUtil.toString(palabra));

	            verificarNerForm.setTextoSalida(salida);

			} else {
				verificarNerForm.setTextoSalida("[Error] Debe especificar una palabra a analizar.");
			}

		} else {
			verificarNerForm.setTextoSalida(null);
		}

		result = actionMapping.findForward("PantallaVerificarNer");
		Log.logRT(usrDTO.getUsr(), "END VerificarNerAction.execute()");

		return result;
	}

} // class
