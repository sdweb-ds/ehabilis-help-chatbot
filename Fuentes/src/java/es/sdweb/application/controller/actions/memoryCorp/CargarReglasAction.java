package es.sdweb.application.controller.actions.memoryCorp;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.LogUtil;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.memoryCorp.CargarReglasForm;
import es.sdweb.application.controller.actions.util.BaseAction;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.vista.util.GestorInformacionWeb;
import es.sdweb.memorycorp.core.LearningEngine;
import es.sdweb.memorycorp.nlpengine.LParrafo;
import es.sdweb.memorycorp.nlpengine.NLPEngine;
import es.sdweb.memorycorp.nlpengine.SpanishDictionary;

/**
 * Implementa la funcionalidad Cargar Reglas (comando loadruleset).
 */
public class CargarReglasAction extends BaseAction {

	@Override
	public ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward("Login");
		}

		ActionForward result = null;

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "---------------------------------------------------------------------------------------------------------------------");
		Log.logRT(usrDTO.getUsr(), "BEGIN CargarReglasAction.execute()");


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




		CargarReglasForm cargarReglasForm = (CargarReglasForm) actionForm;

		String accion = GestorInformacionWeb.getParameter(httpServletRequest, "accion");



		if ( (accion != null) && (accion.equals("procesar")) ) {

			if ( (cargarReglasForm != null) && (cargarReglasForm.getTextoEntrada() != null) && (!cargarReglasForm.getTextoEntrada().isEmpty()) ) {

				String userInput = StringUtil.trim(cargarReglasForm.getTextoEntrada()); //Leemos una entrada del usuario
/*
				LParrafo par=engineNLP.addParrafo(userInput);
*/
				String salida = "";
				salida += "Reglas procesadas.";
/*
				ArrayList unkWords=learningEngine.processParrafo(par); //Palabras desconocidas
	            salida += "Palabras desconocidas: " + StringUtil.toString(unkWords, true);
*/
	            cargarReglasForm.setTextoSalida(salida);

			} else {
				cargarReglasForm.setTextoSalida("[Error] No se ha especificado unas reglas a cargar.");
			}

		} else {
			cargarReglasForm.setTextoSalida(null);
		}

		result = actionMapping.findForward("PantallaCargarReglas");
		Log.logRT(usrDTO.getUsr(), "END CargarReglasAction.execute()");

		return result;
	}

} // class
