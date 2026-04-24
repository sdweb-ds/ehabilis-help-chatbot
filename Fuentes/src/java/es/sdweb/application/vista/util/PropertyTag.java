package es.sdweb.application.vista.util;

import java.net.URLEncoder;

import es.sdweb.application.componentes.util.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import java.io.IOException;

import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.vista.util.PropertyUtils;


/**
 * Tag que lee o guarda valores en propiedades.
 * @author Antonio Carro Mariño
 * @version 1.0
 */

public class PropertyTag extends BodyTagSupport {

	public static final String SPECIAL_NAME_NOW = "NOW";

	public static final String FORMAT_URL_ENCODED = "URLEncoded";

	private String mDo = null;
	public void setDo (String pDo) {mDo = pDo;}
	public String getDo () {return mDo;}

	private String mName = null;
	public void setName (String pName) {mName = pName;}
	public String getName () {return mName;}

	private String mProperty = null;
	public void setProperty (String pProperty) {mProperty = pProperty;}
	public String getProperty () {return mProperty;}

	private String mType = null;
	public void setType (String pType) {mType = pType;}
	public String getType () {return mType;}

	private String mFormat = null;
	public void setFormat (String pFormat) {mFormat = pFormat;}
	public String getFormat () {return mFormat;}

	private Object mValue = null;
	public void setValue (Object pValue) {mValue = pValue;}
	public Object getValue () {return mValue;}

	private String mDefault = null;
	public void setDefault (String pDefault) {mDefault = pDefault;}
	public String getDefault () {return mDefault;}

	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}


	public int doEndTag(){
		boolean formatUrlEncoded = false;
		if (getFormat() != null && getFormat().equalsIgnoreCase(FORMAT_URL_ENCODED)) {
			formatUrlEncoded = true;
			setFormat(null);
		}



		if (getDo().toUpperCase().equals("SET")) {
			try {
				if (getType() != null && getValue() instanceof String) {
					setValue(StringUtil.string2Object((String)getValue(), getType(), getFormat()));
				}
			}
			catch (Exception e) {}

			if (formatUrlEncoded) setValue(URLEncoder.encode(getValue().toString()));

			if (getProperty() != null && getProperty().length() > 0) {
				PropertyUtils.setObjectValue(PropertyUtils.getAttribute(pageContext, getName()), getProperty(), getValue(), getType());
			}
			else pageContext.setAttribute(getName(), getValue());
		}
		else {
			try {
				Object value = null;
				if (getProperty() != null && getProperty().length() > 0) {
					value = PropertyUtils.getObjectValue(PropertyUtils.getAttribute(pageContext, getName()), getProperty());
				}
				else {
					if (getName().equals(SPECIAL_NAME_NOW)) {
						value = new java.util.Date();
					}
					else value = PropertyUtils.getAttribute(pageContext, getName());
				}
				if (value instanceof String && getType() != null) {
					try {
						value = StringUtil.string2Object((String)value, getType());
					}
					catch (Exception e) {}
				}
				value = StringUtil.object2String(value, getFormat());
				if (formatUrlEncoded) value = ((value==null)?null:URLEncoder.encode(value.toString()));

				pageContext.getOut().print((value==null)?getDefault():value);
			}
			catch (IOException e) {
				
				ExceptionErrorInterno error = new ExceptionErrorInterno("PropertyTag",e.getMessage()); 
			}
		}
		return EVAL_PAGE;
	}
}