package es.sdweb.application.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class DigesterWebParams
{
  private Properties result=new Properties();
  
 
  public void run(InputStream inputStream) throws IOException, SAXException
  {    
 	
	 Digester digester = new Digester();

	 // This method pushes this class to the Digesters
	 // object stack making its methods available to processing rules.
	 digester.push(this);

	 // This set of rules calls the add method and passes
	 // in dos parameters to the method.
	 digester.addCallMethod("web-app/context-param", "add", 2);
	 digester.addCallParam("web-app/context-param/param-name", 0);
	 digester.addCallParam("web-app/context-param/param-value", 1);

   
	 // This method starts the parsing of the document.
	 digester.parse(inputStream);
  }  

  // method called by Digester.
  public void add(String name,String value)
  {
    result.put(name,value);
  }
  
  public Properties getResult(){
  	return result;
  }
  
}//class