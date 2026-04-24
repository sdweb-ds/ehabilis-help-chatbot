package es.sdweb.application.model.exceptions;

public class ExceptionXML extends ExceptionErrorInterno{

	private static int codigo = 200;
	//Codigos de los errores en la creacion,transformacion... de los xml
	public static int XML_TRANSFORMER_CONFIG = 1;
	public static int XML_TRANSFORMER = 2;
	public static int XML_TRANSFORMER_IO = 3;
	public static int XML_PARSER_CONFIGURATION = 4;
	public static int XML_PARSER = 5;
	public static int XML_PARSER_IO = 6;
	public static int XML_JDOM = 7;
	public static int XML_XML2TEXT = 8;
	
	
	public ExceptionXML( Exception e ) {
	  super(e);
	}
	
	public ExceptionXML( Exception excepcion, int codigo, String mensaje ) {
	  super( excepcion, ExceptionXML.codigo + codigo, mensaje );
	}
}//class
