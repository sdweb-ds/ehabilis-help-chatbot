package es.sdweb.application.controller.actions.brainLearning;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.brainLearning.ComprobarVerboForm;
import es.sdweb.application.controller.actions.util.BaseAction;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.vista.util.GestorInformacionWeb;
import es.sdweb.memorycorp.core.LearningEngine;
import es.sdweb.memorycorp.nlpengine.DVerb;
import es.sdweb.memorycorp.nlpengine.Dictionary;
import es.sdweb.memorycorp.nlpengine.NLPEngine;
import es.sdweb.memorycorp.nlpengine.SpanishDictionary;

/**
 * Implementa la funcionalidad Comprobar verbo (comando checkverb).
 */
public class ComprobarVerboAction extends BaseAction {

	@Override
	public ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward("Login");
		}

		ActionForward result = null;

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "---------------------------------------------------------------------------------------------------------------------");
		Log.logRT(usrDTO.getUsr(), "BEGIN ComprobarVerboAction.execute()");


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




		ComprobarVerboForm comprobarVerboForm = (ComprobarVerboForm) actionForm;

		String accion = GestorInformacionWeb.getParameter(httpServletRequest, "accion");



		if ( (accion != null) && (accion.equals("procesar")) ) {

			if ( (comprobarVerboForm != null) && (comprobarVerboForm.getTextoEntrada() != null) && (!comprobarVerboForm.getTextoEntrada().isEmpty()) ) {

				String userInput = StringUtil.trim(comprobarVerboForm.getTextoEntrada()); //Leemos una entrada del usuario

				String salida = "";

				Dictionary dic=learningEngine.getDictionary();
				ArrayList<DVerb> verbs=dic.getVerb(userInput);

				if (!verbs.isEmpty()){
					DVerb verb=verbs.get(0); //Cogemos el primer elemento
					salida += "Conjugaciones de \""+userInput+"\": "+verb.getConjugacionesStr();
				}else{
					salida += "El verbo \""+userInput+"\" no está en el Diccionario";
				}


				comprobarVerboForm.setTextoSalida(salida);

			} else {
				comprobarVerboForm.setTextoSalida("[Error] No se ha especificado un verbo a analizar.");
			}

		} else {
			comprobarVerboForm.setTextoSalida(null);
		}

		result = actionMapping.findForward("PantallaComprobarVerbo");
		Log.logRT(usrDTO.getUsr(), "END ComprobarVerboAction.execute()");

		return result;
	}

} // class
