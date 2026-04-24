package es.sdweb.application.util;

import java.io.InputStream;
import java.util.Properties;

import org.jdom.Document;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.exceptions.ExceptionXML;

public class GeneradorJDOM {

	//CONSTANTES QUE SE UTILIZAN EN LA CREACI�N DE LOS XML'S
	public static final String ENVIOS_XML_FILENAME = "es/sdweb/application/envioMailSMS.xml";
	public static final String ENVIOS_XML_CONFIG_FILENAME = "es/sdweb/application/envioMailSMS.properties";

	public static final String IDENTIFICADOR_MENSAJE = "mensaje.id";
	public static final String TIMESTAMP_MENSAJE = "mensaje.ts";
	public static final String TIPO_MENSAJE = "cabecera.tipo.ref";
	public static final String FORMATO_MENSAJE = "cabecera.tipo.formato";
	public static final String DESTINO_MENSAJE = "cabecera.info.destino";
	public static final String LOGIN_EMPRESA_EN_PLATAFORMA = "cabecera.info.loginEmpresa";
	public static final String REFERENCIA_CONTRATO = "cabecera.info.refContrato";
	public static final String CONTENIDO_MENSAJE = "cuerpo.contenido";


	public GeneradorJDOM() {
	}


	public Document obtenerDocumentoMail( String textoMail,String correoElec,String loginEmpresa,String refContrato ) throws ExceptionXML {

		Log.logRT("BEGIN GeneradorJDOM.obtenerDocumentoMail()");

		Document xmlDocument = null;
		try {
			//Obtenemos el documento XML
			InputStream is = getClass().getClassLoader().getResourceAsStream(ENVIOS_XML_FILENAME);
			xmlDocument = HerramientasXML.crearArbolJDOM(is);

			//Obtenemos el fichero de propiedades
			Properties properties = new Properties();
			properties.load(getClass().getClassLoader().getResourceAsStream(ENVIOS_XML_CONFIG_FILENAME));

			//Rellenamos los datos del xml del env�o
			String id = "AQs93KvkWK3G8MCoMiIBfIyh";
			String timestamp = "1149150544907";
			String ref = "sms";
			String formato = "text";

			XmlUtils.setElementValue(xmlDocument, properties.getProperty(IDENTIFICADOR_MENSAJE), id);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(TIMESTAMP_MENSAJE), timestamp);

			XmlUtils.setElementValue(xmlDocument, properties.getProperty(TIPO_MENSAJE), ref);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(FORMATO_MENSAJE), formato);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(DESTINO_MENSAJE), correoElec);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(LOGIN_EMPRESA_EN_PLATAFORMA), loginEmpresa);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(REFERENCIA_CONTRATO), refContrato);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(CONTENIDO_MENSAJE), textoMail);
		}
		catch (Exception e)
		{
			throw new ExceptionXML(e);
		}

		Log.logRT("END GeneradorJDOM.obtenerDocumentoMail()");

		return xmlDocument;
	}

	public Document obtenerDocumentoSMS( String textoSMS,String telefono,String loginEmpresa,String refContrato) throws ExceptionXML {

		Log.logRT("BEGIN GeneradorJDOM.obtenerDocumentoSMS()");

		Document xmlDocument = null;
		try {
			//Obtenemos el documento XML
			InputStream is = getClass().getClassLoader().getResourceAsStream(ENVIOS_XML_FILENAME);
			xmlDocument = HerramientasXML.crearArbolJDOM(is);

			//Obtenemos el fichero de propiedades
			Properties properties = new Properties();
			properties.load(getClass().getClassLoader().getResourceAsStream(ENVIOS_XML_CONFIG_FILENAME));

			//Rellenamos los datos del xml del env�o
			String id = "AQs93KvkWK3G8MCoMiIBfIyh";
			String timestamp = "1149150544907";
			String ref = "email";
			String formato = "text";

			XmlUtils.setElementValue(xmlDocument, properties.getProperty(IDENTIFICADOR_MENSAJE), id);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(TIMESTAMP_MENSAJE), timestamp);

			XmlUtils.setElementValue(xmlDocument, properties.getProperty(TIPO_MENSAJE), ref);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(FORMATO_MENSAJE), formato);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(DESTINO_MENSAJE), telefono);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(LOGIN_EMPRESA_EN_PLATAFORMA), loginEmpresa);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(REFERENCIA_CONTRATO), refContrato);
			XmlUtils.setElementValue(xmlDocument, properties.getProperty(CONTENIDO_MENSAJE), textoSMS);
		}
		catch (Exception e)
		{
			throw new ExceptionXML(e);
		}

		Log.logRT("END GeneradorJDOM.obtenerDocumentoSMS()");

		return xmlDocument;
	}
}
