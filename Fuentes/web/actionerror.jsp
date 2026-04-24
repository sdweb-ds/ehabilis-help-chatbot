<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %> 

<%@ page isErrorPage="true" %>
<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.apache.commons.lang.exception.ExceptionUtils" %>

<%@ page import="es.sdweb.application.controller.config.IConstantes" %>
<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>

<!DOCTYPE html>
<html>
	<head>
		<meta content="no-cache" http-equiv="Pragma" />
		<meta content="no-cache" http-equiv="Cache-Control" />
		<meta content="no-store" http-equiv="Cache-Control" />
		<meta content="max-age=0" http-equiv="Cache-Control" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta charset="utf-8" />

		<title><fmt:message key="action.error.page"/></title>
		
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/fontawesome/css/font-awesome.min.css" />
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/main.css" />
	</head>
	<body class="actionerror_jsp">
		<div id="mensajePrincipal">
			<img src="<%= request.getContextPath() %>/images/iconoError.gif" />
			<span id="error"><fmt:message key="txt.error"/></span>
			<span id="queError"><fmt:message key="action.error.page"/><fmt:message key="action.error.page.principal"/></span>
		</div>
		
		<div id="masDetalles">
			<i class="fa fa-plus"></i>
		</div>
		
		<div id="detalles" style="display:none;">
			<div id='textoDetalles'>
				<logic:present name="mensajeError">
					<bean:write name='mensajeError' />
				</logic:present>
				<logic:notPresent name="mensajeError">
					<% if(exception == null || !"1".equals(GestorInformacionWeb.getConstante(request, IConstantes.CONSTANTE_MODO_DEBUG))) { %>
						<fmt:message key="action.error.page.generico" />
					<% } else { %>
						<pre><%= StringEscapeUtils.escapeHtml(ExceptionUtils.getStackTrace(exception)) %></pre>
					<% } %>
				</logic:notPresent>
			</div>
		</div>
		
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/jquery/jquery.min.js"></script>
		<script type="text/javascript">
			$(document).on('ready', function() {
				$('#masDetalles').on('click', function(e) {
					$('#detalles').slideToggle();
				})
				
				try {
					window.top.loadingStop();
				} catch(e) {}
			});
		</script>
	</body>
</html>