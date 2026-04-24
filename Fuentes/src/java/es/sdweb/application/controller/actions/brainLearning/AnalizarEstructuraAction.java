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
import es.sdweb.application.controller.actionforms.brainLearning.AnalizarEstructuraForm;
import es.sdweb.application.controller.actions.util.BaseAction;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.vista.util.GestorInformacionWeb;
import es.sdweb.memorycorp.core.LearningEngine;
import es.sdweb.memorycorp.nlpengine.LOracion;
import es.sdweb.memorycorp.nlpengine.LParrafo;
import es.sdweb.memorycorp.nlpengine.NLPEngine;
import es.sdweb.memorycorp.nlpengine.SpanishDictionary;

/**
 * Implementa la funcionalidad Analizar Estructura (comando struc).
 */
public class AnalizarEstructuraAction extends BaseAction {

	@Override
	public ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward("Login");
		}

		ActionForward result = null;

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "---------------------------------------------------------------------------------------------------------------------");
		Log.logRT(usrDTO.getUsr(), "BEGIN AnalizarEstructuraAction.execute()");


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




		AnalizarEstructuraForm analizarEstructuraForm = (AnalizarEstructuraForm) actionForm;

		String accion = GestorInformacionWeb.getParameter(httpServletRequest, "accion");



		if ( (accion != null) && (accion.equals("procesar")) ) {

			if ( (analizarEstructuraForm != null) && (analizarEstructuraForm.getTextoEntrada() != null) && (!analizarEstructuraForm.getTextoEntrada().isEmpty()) ) {

				String userInput = StringUtil.trim(analizarEstructuraForm.getTextoEntrada()); //Leemos una entrada del usuario

				LParrafo par=engineNLP.addParrafo(userInput);

			    LOracion or=par.getOraciones().get(0); //Cogemos la primera oración

			    String salida = "";

			    salida += par.toStringEstructura();
			    salida += "\n";
			    salida += "\n";

			    ArrayList unkWords=learningEngine.processParrafo(par); //Palabras desconocidas

			    salida += "Palabras desconocidas: "+(unkWords.isEmpty()?"NO HAY":StringUtil.toString(unkWords, true));
			    salida += "\n";
			    salida += "\n";
			    salida += "1. Verbo principal: "+(or.getAccion()!=null?or.getAccion().getTexto():"");
			    salida += "\n";
			    salida += "2. Sujeto: "+(or.getSujeto()!=null?or.getSujeto().getTexto():"");
			    salida += "\n";
			    salida += "3. Objeto Directo: "+(or.getObjetoDirecto()!=null?or.getObjetoDirecto().getTexto():"");
			    salida += "\n";
			    salida += "4. Objeto Indirecto: "+(or.getObjetoIndirecto()!=null?or.getObjetoIndirecto().getTexto():"");
			    salida += "\n";
			    salida += "5. C.C. de Lugar: "+(or.getCcLugar()!=null?or.getCcLugar().getTexto():"");
			    salida += "\n";

			    salida += "6. C.C. de Modo: ";
			    salida += "\n";
			    salida += "7. C.C. de Finalidad: ";
                salida += "\n";
                salida += "8. C.C. de Causa: ";
                salida += "\n";
                salida += "9. C.C. de Tiempo: ";
                salida += "\n";
                salida += "10. C.C. de Coparticipación: ";
                salida += "\n";

        		analizarEstructuraForm.setTextoSalida(salida);

			} else {
				analizarEstructuraForm.setTextoSalida("[Error] No se ha especificado una oración a analizar.");
			}

		} else {
			analizarEstructuraForm.setTextoSalida(null);
		}

		result = actionMapping.findForward("PantallaAnalizarEstructura");
		Log.logRT(usrDTO.getUsr(), "END analizarEstructuraFormAction.execute()");

		return result;
	}

} // class
