package es.sdweb.application.vista.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class ImparTag extends TagSupport {
	private String name;
	private String scope;
	public String getName(){
		return name;
	}
	public void setName( String name ){
		this.name = name;
	}

	public Object buscarBean( String name, String scope ){
		//Localizamos el objeto a comprobar
		if( scope == null ){
			if (pageContext.getAttribute(name) != null) return pageContext.getAttribute(name);
			if (pageContext.getRequest().getParameter(name) != null) return pageContext.getRequest().getParameter(name);
			if (pageContext.getSession().getAttribute(name) != null) return pageContext.getSession().getAttribute(name);
			if (pageContext.getServletContext().getAttribute(name) != null) return pageContext.getServletContext().getAttribute(name);
		}else{
			if( scope.equals("page") ) return pageContext.getAttribute(name);
			else if( scope.equals("request") ) return pageContext.getRequest().getParameter(name);
			else if( scope.equals("session") ) return pageContext.getSession().getAttribute( name );
			else if( scope.equals("application") ) return pageContext.getServletContext().getAttribute(name);
		}
		return null;
	}

	public int doStartTag() throws JspException {

		//Obtenemos el valor
		Integer valor = (Integer)buscarBean(name,scope);

		//Si el contenido es par, entonces evaluamos el contenido
		if( valor.intValue() % 2 != 0 ) return EVAL_BODY_INCLUDE;
		else return SKIP_BODY;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getScope() {
		return scope;
	}	
}
