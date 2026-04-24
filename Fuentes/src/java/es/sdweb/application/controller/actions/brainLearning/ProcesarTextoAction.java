package es.sdweb.application.controller.actions.brainLearning;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.controller.actionforms.brainLearning.ProcesarTextoForm;
import es.sdweb.application.controller.actions.util.BaseAction;
import es.sdweb.application.model.dto.UsrDTO;
import es.sdweb.application.vista.util.GestorInformacionWeb;
import es.sdweb.memorycorp.nandanicnoc.TextAnalizer;

/**
 * Implementa la funcionalidad Procesar Texto.
 */
public class ProcesarTextoAction extends BaseAction {

	public final String SALIDA_NO_CONCLUYENTE = "No concluyente";

	public final String TEXTO_ENTRADA_INICIO = "Mujer, 67 años";

	public final String TEXTO_ENTRADA = "Mujer, 67 años acude a urgencias de CAP por subida de tensión.\r\n"
			+ "\r\n"
			+ "Exploración física: FC= 109, Respiraciones= 36, TA=155/99, Dolor de cabeza, Sat02= 98%. Buen estado de hidratación, buena coloración de piel y mucosas.\r\n"
			+ "\r\n"
			+ "Antecedentes personales: Fumadora de 20 cigarros/día desde los 22 años, bebedora escasa de alcohol (un vaso de vino al día). Consumo alto de café.\r\n"
			+ "\r\n"
			+ "Peso: 102,5 Kg. Talla: 1,57. I.M.C.: 46%. Dieta poco equilibrada con abuso de grasas y procesados. \r\n"
			+ "Hace años fue intervenida de varices en MMI. Actualmente la integridad de la superficie cutánea es completa.\r\n"
			+ "\r\n"
			+ "Presenta problemas de eliminación: Estreñimiento, eliminación urinaria OK.\r\n"
			+ "\r\n"
			+ "A pesar de aconsejarle ejercicios (pasear es lo más habitual) al menos 3-4 veces por semana durante 1h, la paciente refiere llevar un estilo de vida en el que la movilidad es mínima. Realiza las tareas de casa diariamente, va de compras y el dÍa que más se aleja de su hogar es el día que viene a consulta.\r\n"
			+ "\r\n"
			+ "La paciente dice tener problemas de conciliación del sueño pudiendo ser debida a la situación familiar, a la ingesta excesiva de café, a la inactividad diurna, etc. No toma fármacos.\r\n"
			+ "\r\n"
			+ "La paciente refiere sensación de ahogo y dolor en el pecho que aumenta cuando su marido se va a trabajar (trabaja todo el día). \r\n"
			+ "\r\n"
			+ "Se deduce un afrontamiento familiar con el marido y con el hijo, lo que le provoca un aumento del grado de ansiedad. Además, muestra una autovaloración negativa seguida de negación a la realización de dieta, ejercicio, abandono del tabaco...\r\n"
			+ "\r\n"
			+ "Se realiza toma de constantes y un electro. EKG sin alteraciones. Se administra medicación para la bajar la tensión y queda en sala para disminuir estímulos. Se toma nuevamente la tensión habiendo bajado 4 puntos, se dan pautas higiénico dietéticas y alta.";

	public final String TEXTO_SALIDA = "NANDA:\r\n"
			+ "\r\n"
			+ "[00188] Tendencia a adoptar conductas de riesgo para la salud\r\n"
			+ "   *Características definitorias:\r\n"
			+ "      ->Tabaquismo: Fumadora de 20 cigarros/día desde los 22 años, bebedora escasa de alcohol (un vaso de vino al día).\r\n"
			+ "      ->Uso inadecuado de sustancias: Fumadora de 20 cigarros/día desde los 22 años, bebedora escasa de alcohol (un vaso de vino al día). Consumo alto de café.\r\n"
			+ "   *Factores relacionados:\r\n"
			+ "      ->Estresores: Se deduce un afrontamiento familiar con el marido y con el hijo, lo que le provoca un aumento del grado de ansiedad.\r\n"
			+ "\r\n"
			+ "[00232] Obesidad\r\n"
			+ "   *Características definitorias:\r\n"
			+ "      ->I.M.C.: 46%.\r\n"
			+ "   *Factores relacionados:\r\n"
			+ "      ->Consumo de bebidas azucaradas: Consumo alto de café.\r\n"
			+ "\r\n"
			+ "[00228] Riesgo de perfusión tisular periférica ineficaz\r\n"
			+ "   *Factor de riesgo:\r\n"
			+ "      ->Tabaquismo: Fumadora de 20 cigarros/día desde los 22 años, bebedora escasa de alcohol (un vaso de vino al día).\r\n"
			+ "   *Factores relacionados:\r\n"
			+ "      ->Hipertensión: Exploración física: FC= 109, Respiraciones= 36, TA=155/99, Dolor de cabeza, Sat02= 98%.\r\n"
			+ "\r\n"
			+ "[00011] Estreñimiento\r\n"
			+ "   *Características definitorias:\r\n"
			+ "      ->Estreñimiento: Presenta problemas de eliminación: Estreñimiento, eliminación urinaria OK.\r\n"
			+ "   *Factor de riesgo:\r\n"
			+ "\r\n"
			+ "[00095] Insomnio\r\n"
			+ "   *Características definitorias:\r\n"
			+ "      -> Insomnio: La paciente dice tener problemas de conciliación del sueño pudiendo ser debida a la situación familiar, a la ingesta excesiva de café, a la inactividad diurna, etc. \r\n"
			+ "   *Factores relacionados:\r\n"
			+ "      ->Ansiedad: Se deduce un afrontamiento familiar con el marido y con el hijo, lo que le provoca un aumento del grado de ansiedad.\r\n"
			+ "      ->Consumo de cafeína: Consumo alto de café.\r\n"
			+ "      ->Consumo de bebidas azucaradas: Consumo alto de café.\r\n"
			+ "      ->Síntomas depresivos: Además, muestra una autovaloración negativa seguida de negación a la realización de dieta, ejercicio, abandono del tabaco...\r\n"
			+ "      ->Obesidad: I.M.C.: 46%.\r\n"
			+ "      ->Estresores: Se deduce un afrontamiento familiar con el marido y con el hijo, lo que le provoca un aumento del grado de ansiedad.\r\n"
			+ "      ->Uso inadecuado de sustancias: Fumadora de 20 cigarros/día desde los 22 años, bebedora escasa de alcohol (un vaso de vino al día). Consumo alto de café.\r\n"
			+ "\r\n"
			+ "[00146] Ansiedad\r\n"
			+ "   *Características definitorias:\r\n"
			+ "      ->Nerviosismo: Se deduce un afrontamiento familiar con el marido y con el hijo, lo que le provoca un aumento del grado de ansiedad.\r\n"
			+ "      ->Opresión en el pecho: La paciente refiere sensación de ahogo y dolor en el pecho que aumenta cuando su marido se va a trabajar (trabaja todo el día). \r\n"
			+ "   *Factores relacionados:\r\n"
			+ "      ->Estresares familiares: Se deduce un afrontamiento familiar con el marido y con el hijo, lo que le provoca un aumento del grado de ansiedad.\r\n"
			+ "\r\n"
			+ "[00063] Procesos familiares disfuncionales\r\n"
			+ "   *Características definitorias:\r\n"
			+ "      ->Ansiedad: Se deduce un afrontamiento familiar con el marido y con el hijo, lo que le provoca un aumento del grado de ansiedad.\r\n"
			+ "      ->Síntomas depresivos: Además, muestra una autovaloración negativa seguida de negación a la realización de dieta, ejercicio, abandono del tabaco...\r\n"
			+ "      ->Baja autoestima: Además, muestra una autovaloración negativa seguida de negación a la realización de dieta, ejercicio, abandono del tabaco...\r\n"
			+ "\r\n"
			+ "\r\n"
			+ "--------------------------------------------------------------------------------------------------\r\n"
			+ "NIC:\r\n"
			+ "[4162] Manejo de la hipertensión mediante Identificación de las posibles causas de la hipertensión\r\n"
			+ "      ->Se realiza toma de constantes y un electro. EKG sin alteraciones. Se administra medicación para la bajar la tensión y queda en sala para disminuir estímulos. Se toma nuevamente la tensión habiendo bajado 4 puntos, se dan pautas higiénico dietéticas y alta. \r\n"
			+ "\r\n"
			+ "[2304] Administración de medicación\r\n"
			+ "      ->Se administra medicación para la bajar la tensión y queda en sala para disminuir estímulos.\r\n"
			+ "\r\n"
			+ "[5510] Educación para la salud\r\n"
			+ "      ->Se toma nuevamente la tensión habiendo bajado 4 puntos, se dan pautas higiénico dietéticas y alta.\r\n"
			+ "\r\n"
			+ "\r\n"
			+ "--------------------------------------------------------------------------------------------------\r\n"
			+ "NOC:\r\n"
			+ "[2112] Severidad de la hipertensión\r\n"
			+ "\r\n"
			+ "[211216] Aumento de la presión arterial sistólica\r\n"
			+ "      ->Se realiza toma de constantes y un electro. EKG sin alteraciones. Se toma nuevamente la tensión habiendo bajado 4 puntos, se dan pautas higiénico dietéticas y alta. \r\n"
			+ "\r\n"
			+ "[211217] Aumento de la presión arterial diastólica\r\n"
			+ "      ->Se realiza toma de constantes y un electro. EKG sin alteraciones. Se toma nuevamente la tensión habiendo bajado 4 puntos, se dan pautas higiénico dietéticas y alta.\r\n"
			+ "\r\n"
			+ "[1837] Conocimiento: control de la hipertensión\r\n"
			+ "      ->Se toma nuevamente la tensión habiendo bajado 4 puntos, se dan pautas higiénico dietéticas y alta.";

	@Override
	public ActionForward executeAction(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		if (!isLoggedIn(httpServletRequest)) {
			return actionMapping.findForward("Login");
		}

		ActionForward result = null;

		UsrDTO usrDTO = getSessionContainer(httpServletRequest).getUserContainer().getUsuario();
		Log.logRT(usrDTO.getUsr(), "---------------------------------------------------------------------------------------------------------------------");
		Log.logRT(usrDTO.getUsr(), "BEGIN ProcesarTextoAction.execute()");

		ProcesarTextoForm procesarTextoForm = (ProcesarTextoForm) actionForm;

		String accion = GestorInformacionWeb.getParameter(httpServletRequest, "accion");

		if ( (accion != null) && (accion.equals("procesar")) && (procesarTextoForm != null) && (procesarTextoForm.getTextoEntrada() != null) && (!procesarTextoForm.getTextoEntrada().isEmpty()) ) {
			if (procesarTextoForm.getTextoEntrada().toUpperCase().startsWith(TEXTO_ENTRADA_INICIO.toUpperCase())) {
				procesarTextoForm.setTextoSalida(TEXTO_SALIDA);
			} else {
				//procesarTextoForm.setTextoSalida(SALIDA_NO_CONCLUYENTE);

				procesarTextoForm.setTextoSalida(TextAnalizer.processText(procesarTextoForm.getTextoEntrada(), learningEngine).toString());
			}
		} else if ((accion != null) && (accion.equals("procesar"))) {
			procesarTextoForm.setTextoSalida("[Error] No se ha especificado un texto del dominio de enfermería a analizar.");
		} else {
			procesarTextoForm.setTextoSalida(null);
		}

		result = actionMapping.findForward("PantallaProcesarTexto");
		Log.logRT(usrDTO.getUsr(), "END ProcesarTextoAction.execute()");

		return result;
	}

} // class
