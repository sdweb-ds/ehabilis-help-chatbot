<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>
<%@ page import="es.sdweb.application.controller.util.SessionContainer" %>

<%
	// Limpiar las listas de estilos y scripts de la página actual (TODO: Buscar una solución mejor)
	GestorInformacionWeb.clearScriptsAndStyles();
%>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta charset="utf-8" />
		<%-- TODO: Buscar una mejor forma de controlar esto --%>
		<meta http-equiv="refresh" content="<%= session.getMaxInactiveInterval() %>;url=index.jsp" />
		
		<title><fmt:message key="titulo"/></title>
		
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/static/css/bootstrap/css/bootstrap.min.css" />
		<link rel='stylesheet' type='text/css' href='<%= request.getContextPath() %>/static/css/fonts/roboto.css' />
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/static/css/fontawesome/css/font-awesome.min.css" />
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/static/css/main.css" />
	</head>
	<body>
		<header>
			<div class="header-container">
				<div class="logo-cabecera">
					<img src="<%= request.getContextPath() %>/static/images/logo.png" alt="<fmt:message key="txt.cabecera.logo" />" class="logo" />
				</div>

				<div class="header-title">

					<fmt:message key="txt.cabecera.gestion.titulo_aplicacion" />
					<!--<fmt:message key="txt.cabecera.locale_depuracion" />-->
					<span class="small-text"><fmt:message key="txt.cabecera.nombre.titulo_descripcion" /></span>
				</div>
				
				
				<%  if (GestorInformacionWeb.isLoggedIn(request)) { %>
					<div id="td_idiomas" class="tituloblanco center-text">
						<html:link page="/Locale.do?idioma=es_ES"><img src="<%= request.getContextPath() %>/static/images/locales/es_ES.png" alt="es_ES" title="Español" /> ES</html:link>
						<html:link page="/Locale.do?idioma=gl_ES"><img src="<%= request.getContextPath() %>/static/images/locales/gl_ES.png" alt="gl_ES" title="Galego" /> GL</html:link>
					</div>
					
					<div class="header-login-info">
						<span class="login-text"><fmt:message key="txt.bienvenido" />, <%= ((SessionContainer)session.getAttribute("sessCon")).getUserContainer().getUsuario().getUsr() %></span>
						<a id="bot_salir" class="boton_salir" href="<%= request.getContextPath() %>/logout.jsp" target="principal"><fmt:message key="boton.salir"/></a>
					</div>
				<% } %>
			</div>
			<div id="td_fecha" class="tituloblanco">
				<div class="inner">00/00/00 - 00:00:00</div>
			</div>
			
			<div id="td_ayuda" class="tituloblanco">
				<div class="inner">
					<a href="#"><fmt:message key="txt.guia.taquillamanager" /></a>
					<a href="<%= request.getContextPath() %>/pages/static/help.jsp"><fmt:message key="txt.ayuda" /></a>
				</div>
			</div>
		</header>