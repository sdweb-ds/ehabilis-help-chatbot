package es.sdweb.application.util;


import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.parsers.*;

// Parsers
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
//JDOM
import org.jdom.*; 
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
//import org.jdom.output.Format.TextMode;


import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import es.sdweb.application.model.exceptions.ExceptionXML;
import es.sdweb.application.util.HerramientasXML;
import es.sdweb.application.util.XmlOutputter;


public class HerramientasXML {

	public HerramientasXML() {
	}

	/** Convierte el contenido de un documento XML en otro utilizando una pagina de estilo XSL */
	public static String transformaXml( String xmlIn, File ficheroXslIn) throws ExceptionXML{
			//Establecemos las propiedades del documento XML de salida
			Properties xmlProperties = new Properties();
			xmlProperties.setProperty("omit-xml-declaration","no");
			xmlProperties.setProperty("method","text");
			xmlProperties.setProperty("indent","no");
			return motorTransformacionXML( xmlIn, ficheroXslIn, xmlProperties );
	}

	/** Convierte el contenido de un documento XML en otro utilizando una p�gina de estilo XSL */
	public static String transformaXml( String xmlIn, String xslIn )
	throws ExceptionXML{
			//Establecemos las propiedades del documento XML de salida
			Properties xmlProperties = new Properties();
			xmlProperties.setProperty("omit-xml-declaration","no");
			xmlProperties.setProperty("method","text");
			xmlProperties.setProperty("indent","no");
			return motorTransformacionXML( xmlIn, xslIn, xmlProperties );
	}

	/** Convierte el contenido de un documento XML en otro utilizando una p�gina de estilo XSL */
	public static String transformaXml( String xmlIn, File ficheroXslIn, boolean declaracion_xml )
	throws ExceptionXML{
			//Establecemos las propiedades del documento XML de salida
			Properties xmlProperties = new Properties();
			if( declaracion_xml == true ) xmlProperties.setProperty("omit-xml-declaration","no");
			else xmlProperties.setProperty("omit-xml-declaration","yes");
			xmlProperties.setProperty("method","text");
			xmlProperties.setProperty("indent","no");
			return motorTransformacionXML( xmlIn, ficheroXslIn, xmlProperties );
	}

	/** Convierte el contenido de un documento XML en otro utilizando una p�gina de estilo XSL */
	public static String transformaXml( String xmlIn, String xslIn, boolean declaracion_xml )
	throws ExceptionXML{
			//Establecemos las propiedades del documento XML de salida
			Properties xmlProperties = new Properties();
			if( declaracion_xml == true ) xmlProperties.setProperty("omit-xml-declaration","no");
			else xmlProperties.setProperty("omit-xml-declaration","yes");
			xmlProperties.setProperty("method","text");
			xmlProperties.setProperty("indent","no");
			return motorTransformacionXML( xmlIn, xslIn, xmlProperties );
	}

	/** Convierte el contenido de un documento XML en otro utilizando una p�gina de estilo XSL */
	public static String transformaXml( String xmlIn, File ficheroXslIn, String tipo_salida, boolean declaracion_xml )
	throws ExceptionXML{
		//Establecemos las propiedades del documento XML de salida
		Properties xmlProperties = new Properties();
		if( declaracion_xml == true ) xmlProperties.setProperty("omit-xml-declaration","no");
		else xmlProperties.setProperty("omit-xml-declaration","yes");
		xmlProperties.setProperty("method",tipo_salida);
		xmlProperties.setProperty("indent","no");
		return motorTransformacionXML( xmlIn, ficheroXslIn, xmlProperties );
	}

	/** Convierte el contenido de un documento XML en otro utilizando una p�gina de estilo XSL */
	public static String transformaXml( String xmlIn, String xslIn, String tipo_salida, boolean declaracion_xml )
	throws ExceptionXML{
		//Establecemos las propiedades del documento XML de salida
		Properties xmlProperties = new Properties();
		if( declaracion_xml == true ) xmlProperties.setProperty("omit-xml-declaration","no");
		else xmlProperties.setProperty("omit-xml-declaration","yes");
		xmlProperties.setProperty("method",tipo_salida);
		xmlProperties.setProperty("indent","no");
		return motorTransformacionXML( xmlIn, xslIn, xmlProperties );
	}

	private static String motorTransformacionXML( String xmlIn, String xslIn, Properties xmlProperties )
		throws ExceptionXML{
		//Salida
		StringWriter xmlOut = new StringWriter();
		try {
			 //Transforma el documento XML de entrada en uno de salida en funci�n del documento XSL
			 TransformerFactory tFactory = TransformerFactory.newInstance();
			 Transformer transformer = tFactory.newTransformer(new StreamSource( new StringReader( xslIn ) ));
			 transformer.setOutputProperties( xmlProperties );
			 transformer.transform(new StreamSource( new StringReader( xmlIn ) ), new StreamResult( xmlOut ) );
		}catch (TransformerConfigurationException e) {
			throw new ExceptionXML(e,ExceptionXML.XML_TRANSFORMER_CONFIG,"Error en la configuraci�n del transformador XSL");
		}catch (TransformerException e) {
			throw new ExceptionXML(e,ExceptionXML.XML_TRANSFORMER,"Error en la transformaci�n XSL");
		}
		return xmlOut.toString();
	}

	private static String motorTransformacionXML( String xmlIn, File ficXslIn, Properties xmlProperties )
	throws ExceptionXML{
		//Salida
		StringWriter xmlOut = new StringWriter();
		try {
			 //Transforma el documento XML de entrada en uno de salida en funci�n del documento XSL
			 TransformerFactory tFactory = TransformerFactory.newInstance();
			 Transformer transformer = tFactory.newTransformer(new StreamSource( ficXslIn ) );
			 transformer.setOutputProperties( xmlProperties );
			 transformer.transform(new StreamSource( new StringReader( xmlIn ) ), new StreamResult( xmlOut ) );
		}catch (TransformerConfigurationException e) {
			throw new ExceptionXML(e,ExceptionXML.XML_TRANSFORMER_CONFIG,"Error en la configuraci�n del transformador XSL");
		}catch (TransformerException e) {
			throw new ExceptionXML(e,ExceptionXML.XML_TRANSFORMER,"Error en la transformaci�n XSL");
		}
		return xmlOut.toString();
	}

	/** Cambia la codificacion de un documento XML */
	public static String setEncoding(String xmlIn, String encoding)
	throws ExceptionXML{
		//Convertimos el documento XML a otro formato
		ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
		String out=null;
		try{
			xmlOutputStream.write( xmlIn.getBytes() );
			out = xmlOutputStream.toString(encoding);
		}catch(IOException e){
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_TRANSFORMER_IO,"Error de entrada/salida al modificar la codificaci�n( " + encoding + " ) del XML");
			throw err;
		}
		return out;
	}
	public static InputStream setEncoding(InputStream xmlIn, String encoding)
	throws ExceptionXML{
		//Convertimos el documento XML a otro formato
		InputStream is = null;
		try{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int l=0;
			while ((l = xmlIn.read(buffer)) > 0) {
				os.write(buffer, 0, l);
			}
			is = new ByteArrayInputStream(os.toString(encoding).getBytes());
		}
		catch(IOException e){
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_TRANSFORMER_IO,"Error de entrada/salida al modificar la codificaci�n( " + encoding + " ) del XML");			
			throw err;
		}
		return is;
	}

	/** Cambia la codificacion de un documento XML a UTF-8 */
	public static String setUTFEncoding( String xmlIn )
	throws ExceptionXML{
		return setEncoding(xmlIn, "UTF-8");
	}
	public static InputStream setUTFEncoding(InputStream xmlIn)
	throws ExceptionXML{
		return setEncoding(xmlIn, "UTF-8");
	}

	/** Cambia la codificacion de un documento XML a ISO-8859-1 */
	public static String setISOEncoding( String xmlIn )
	throws ExceptionXML{
		return setEncoding(xmlIn, "ISO-8859-1");
	}
	public static InputStream setISOEncoding(InputStream xmlIn)
	throws ExceptionXML{
		return setEncoding(xmlIn, "ISO-8859-1");
	}
	
	/** Obtiene un �rbol DOM a partir de un texto en XML */
	public static org.w3c.dom.Document crearArbolDOM( String xmlIn )
	throws ExceptionXML{
		org.w3c.dom.Document docXml = null;
		if (xmlIn != null && xmlIn.trim().length() > 0) {
			try{
				DocumentBuilder docBuildXml = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				StringReader sr = new StringReader( xmlIn );
				docXml = docBuildXml.parse( new InputSource( sr ) );
			}catch(ParserConfigurationException e){
				ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_PARSER_CONFIGURATION,"Error de la configuraci�n del parser XML DOM");				
				throw err;
			}catch(SAXException e){
				ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_PARSER,"Error SAX del parser XML DOM");				
				throw err;
			}catch(java.io.IOException e){
				ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_PARSER_IO,"Error de entrada/salida al parsear el XML");
				throw err;
			}
		}
		return docXml;
	}

	/** Obtiene un �rbol DOM a partir de un elemento XML */
	public static org.w3c.dom.Document crearArbolDOM( org.w3c.dom.Element elemento )
	throws ExceptionXML{
		org.w3c.dom.Document docXml = null;
		if(elemento!=null){
		try{
			DocumentBuilder docBuildXml = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			org.w3c.dom.Document doc = docBuildXml.newDocument();
			doc.appendChild( elemento );
			}catch(ParserConfigurationException e){
				ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_PARSER_CONFIGURATION,"Error de la configuraci�n del parser XML DOM");			
			throw err;
			}
		}
		return docXml;
	}

	/** Obtiene un �rbol DOM a partir de un stream */
	public static org.w3c.dom.Document crearArbolDOM( InputStream xmlIS )
	throws ExceptionXML{
		org.w3c.dom.Document docXml = null;
		if(xmlIS!=null){
		try{
			DocumentBuilder docBuildXml = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			docXml = docBuildXml.parse( xmlIS );
		}catch(ParserConfigurationException e){
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_PARSER_CONFIGURATION,"Error de la configuraci�n del parser XML DOM");			
			throw err;
		}catch(SAXException e){
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_PARSER,"Error SAX del parser XML DOM");			
			throw err;
		}catch(java.io.IOException e){
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_PARSER_IO,"Error de entrada/salida al parsear el XML");			
			throw err;
		}
		}
		return docXml;
	}
	
	/** Proporciona un manejador cargado con los datos a partir de un texto en XML */
	public static void cargarXML( String xmlIn, DefaultHandler handler ) throws ExceptionXML{
		SAXParserFactory factoriaParsers = SAXParserFactory.newInstance();
		SAXParser parserXml = null;
		try{
			 parserXml = factoriaParsers.newSAXParser();
			 parserXml.parse( new InputSource( new StringReader( xmlIn ) ), handler );
		}catch(ParserConfigurationException e){
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_PARSER_CONFIGURATION,"Error de la configuraci�n del parser XML DOM");			
			throw err;
		}catch(SAXException e){
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_PARSER,"Error SAX del parser XML DOM");			
			throw err;
		}catch(java.io.IOException e){
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_PARSER_IO,"Error de entrada/salida al parsear el XML");			
			throw err;
		}
		
	}

	//Devuelve un vector con tablas hash, cada una de ellas es una fila
	//con la ACCION y los nombres de los campos como claves
	// ACCION,col1,col2...
	public static Vector obtenerFilas( String xmlIn )
	throws ExceptionXML{

		org.w3c.dom.Document docXml = HerramientasXML.crearArbolDOM( xmlIn );
		Vector salida = new Vector();

		NodeList filas = docXml.getElementsByTagName("datos").item(0).getChildNodes();
		for( int i=0; i<filas.getLength(); i++ ){
			if( filas.item(i).getNodeType() == Node.ELEMENT_NODE ){
				String accion = filas.item(i).getAttributes().getNamedItem("accion").getNodeValue();
				NodeList columnas = filas.item(i).getChildNodes();
				//Creamos una fila, que es una tabla hash de columnas incluidas la acci�n
				Hashtable ht = new Hashtable();
				if (accion != null) ht.put( "ACCION", accion );
				for( int j=0; j<columnas.getLength(); j++ ){
					Node columna = columnas.item(j);
					String nombreColumna = columna.getNodeName();
					Node nodo = columna.getChildNodes().item(0);
					if (nodo != null) {
						String valorColumna = null;
						if (nodo.getNodeValue() != null) {
							valorColumna = nodo.getNodeValue();
						}
						else {
							valorColumna = obtenerXML((org.w3c.dom.Element)columna);
						}
						if (valorColumna != null) ht.put( nombreColumna, valorColumna );
					}
				}
				//A�adimos la nueva fila
				salida.add( ht );
			}
		}
		return salida;
	}

	//Devuelve un vector con tablas hash, cada una de ellas es una fila
	//con su NOMBRE y los nombres de los campos como claves
	// NOMBRE,col1,col2...
	public static Vector obtenerVector( String nodoRaiz, String xmlIn )
	throws ExceptionXML{

		org.w3c.dom.Document docXml = HerramientasXML.crearArbolDOM( xmlIn );
		Vector salida = new Vector();

		NodeList filas = docXml.getElementsByTagName( nodoRaiz ).item(0).getChildNodes();
		for( int i=0; i<filas.getLength(); i++ ){ //Recogemos las propiedades de nivel 1
			if( filas.item(i).getNodeType() == Node.ELEMENT_NODE ){
				String nombre = filas.item(i).getNodeName();
				NodeList columnas = filas.item(i).getChildNodes();
				//Creamos una fila, que es una tabla hash de columnas incluidas la acci�n
				Hashtable ht = new Hashtable();
				ht.put( "NOMBRE", nombre );
				for( int j=0; j<columnas.getLength(); j++ ){ //Recogemos las propiedades de nivel 2
					if( columnas.item(j).getNodeType() == Node.ELEMENT_NODE ){
						Node columna = columnas.item(j);
						String nombreColumna = columna.getNodeName();
						Node nodo = columna.getChildNodes().item(0); //Recogemos el valor
						if (nodo != null) {
							String valorColumna = null;
							if (nodo.getNodeValue() != null) {
								valorColumna = nodo.getNodeValue();
							}
							else {
								valorColumna = obtenerXML((org.w3c.dom.Element)columna);
							}
							if (valorColumna != null) ht.put( nombreColumna, valorColumna );
						}
					}
				}
				//A�adimos la nueva fila
				salida.add( ht );
			}
		}
		return salida;
	}

	public static String getXml( Vector al )
	throws ExceptionXML{
		String docXml = "";

		Iterator it = al.iterator();
		int id = 0;
		while( it.hasNext() ){
			docXml += "<item id=\"" + id + "\">";
			Hashtable ht = (Hashtable)it.next();
			Enumeration enu = ht.keys();
			while( enu.hasMoreElements() ){
				String clave = (String)enu.nextElement();
				String valor = ht.get( clave ).toString();
				docXml += "<property nombre=\"" + clave + "\">" + valor + "</property>";
			}
			docXml += "</item>";
			id++;
		}

		docXml += "";

		return setUTFEncoding( docXml );
	}


	// Obtiene un �rbol JDOM a partir de un input stream de XML 
	public static org.jdom.Document crearArbolJDOM(InputStream xmlIS) throws ExceptionXML{
		try{
			byte[] buffer = new byte[1024];
			int l=0;
			String str = "";
			while ((l = xmlIS.read(buffer)) > 0) {
				str += new String(buffer, 0, l);
			}			
			ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
		
			Charset cs = Charset.forName("ISO-8859-1");
			Writer writer = new OutputStreamWriter(xmlOutputStream, cs);
			writer.write(str);
			writer.flush();
	
			java.io.DataInputStream xmlIso88591 = new java.io.DataInputStream(new ByteArrayInputStream(xmlOutputStream.toByteArray()));
			
			SAXBuilder b = new SAXBuilder();
			b.setIgnoringElementContentWhitespace(false);
			org.jdom.Document docXml = b.build( xmlIso88591 );
			
			return docXml;
		}catch(JDOMException e){
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_JDOM,"Error del creador de documentos JDOM");			
			throw err;
		}catch(Exception e){
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_JDOM,"Error del creador de documentos JDOM");			
			throw err;
		}
	}
	
	private static XmlOutputter obtenerXescurXmlOutputter( String encoding ) {
		XmlOutputter salida = new XmlOutputter();
		salida.setIndent(false);
		salida.setNewlines(false);
		salida.setExpandEmptyElements(true);
		//salida.setTrimText(true);
		salida.setEncoding(encoding);
		return salida;	
	}

	public static String obtenerXescurXml(org.jdom.Element elemento) throws ExceptionXML{

		XmlOutputter salida = obtenerXescurXmlOutputter("ISO-8859-1");
		try {
			return salida.outputString(elemento);
		} catch (Exception e) {
			throw new ExceptionXML(e,ExceptionXML.XML_TRANSFORMER_CONFIG,"Error de entrada/salida al convertir el documento XML a texto");
		}
	}
	
	public static String obtenerXescurXml(org.jdom.Document documento) throws ExceptionXML{

		XmlOutputter salida = obtenerXescurXmlOutputter("ISO-8859-1");
		try {
			return salida.outputString(documento);
		} catch (Exception e) {
			throw new ExceptionXML(e,ExceptionXML.XML_TRANSFORMER_CONFIG,"Error de entrada/salida al convertir el documento XML a texto");
		}
	}

	private static XMLOutputter obtenerXMLOutputter( String encoding ) {
		//Format format = Format.getRawFormat();
		Format format = Format.getCompactFormat();
		format.setEncoding(encoding);
		format.setExpandEmptyElements(true);
		return new XMLOutputter( format );		
	}
	

	public static String obtenerXML(org.jdom.Element elemento) throws ExceptionXML{

		XMLOutputter outputter = obtenerXMLOutputter("ISO-8859-1");
		try {
			return outputter.outputString(elemento);
		} catch (Exception e) {
			throw new ExceptionXML(e,ExceptionXML.XML_TRANSFORMER_CONFIG,"Error de entrada/salida al convertir el documento XML a texto");
		}
	}

	public static String obtenerXML( org.jdom.Document docXml ) throws ExceptionXML{
		
		XMLOutputter outputter = obtenerXMLOutputter("ISO-8859-1");
		try {
			return outputter.outputString(docXml);
		} catch (Exception e) {
			throw new ExceptionXML(e,ExceptionXML.XML_TRANSFORMER_CONFIG,"Error de entrada/salida al convertir el documento XML a texto");
		}
	}


	public static String obtenerXML(org.w3c.dom.Element docXml ) throws ExceptionXML{
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
			Transformer transformer = transformerFactory.newTransformer(); 
			Source src = new DOMSource(docXml);
	
			ByteArrayOutputStream bytearrayxml = new ByteArrayOutputStream();
			Result dest = new StreamResult(bytearrayxml); 
			transformer.transform(src, dest);
	
			Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytearrayxml.toByteArray()), "UTF8"));
			char[] buffer = new char[1024];
			int longo = 0;
			String xml = "";
			while ((longo = reader.read(buffer)) > 0) {
				xml += new String(buffer, 0, longo);
			}
	
			return xml;
	
		} catch (TransformerConfigurationException e) {
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_XML2TEXT,"Error de entrada/salida al convertir el documento XML a texto");			
			throw err;
		} catch (TransformerException e) {
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_XML2TEXT,"Error de entrada/salida al convertir el documento XML a texto");			
			throw err;
		} catch (UnsupportedEncodingException e) {
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_XML2TEXT,"Error de entrada/salida al convertir el documento XML a texto");			
			throw err;
		} catch (IOException e) {
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_XML2TEXT,"Error de entrada/salida al convertir el documento XML a texto");			
			throw err;
		}
	}

	public static String obtenerXML(org.w3c.dom.Document docXml ) throws ExceptionXML{
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
			Transformer transformer = transformerFactory.newTransformer(); 
			Source src = new DOMSource(docXml);
	
			ByteArrayOutputStream bytearrayxml = new ByteArrayOutputStream();
			Result dest = new StreamResult(bytearrayxml); 
			transformer.transform(src, dest);
	
			Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytearrayxml.toByteArray()), "UTF8"));
			char[] buffer = new char[1024];
			int longo = 0;
			String xml = "";
			while ((longo = reader.read(buffer)) > 0) {
				xml += new String(buffer, 0, longo);
			}
	
				return xml;
	
		} catch (TransformerConfigurationException e) {
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_XML2TEXT,"Error de entrada/salida al convertir el documento XML a texto");			
			throw err;
		} catch (TransformerException e) {
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_XML2TEXT,"Error de entrada/salida al convertir el documento XML a texto");			
			throw err;
		} catch (UnsupportedEncodingException e) {
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_XML2TEXT,"Error de entrada/salida al convertir el documento XML a texto");			
			throw err;
		} catch (IOException e) {
			ExceptionXML err = new ExceptionXML(e,ExceptionXML.XML_XML2TEXT,"Error de entrada/salida al convertir el documento XML a texto");			
			throw err;
		}
	}


}