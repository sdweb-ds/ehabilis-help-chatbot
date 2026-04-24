<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>
<%@ page import="es.sdweb.application.controller.util.SessionContainer" %>

<!DOCTYPE html>
<html>
	<head>
		<meta content="no-cache" http-equiv="Pragma" />
		<meta content="no-cache" http-equiv="Cache-Control" />
		<meta content="no-store" http-equiv="Cache-Control" />
		<meta content="max-age=0" http-equiv="Cache-Control" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta charset="utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/main.css" />
	</head>
	<body class="frametop_jsp frametop1_jsp">
		<div class="header-container">
			<div class="header-title">
				<fmt:message key="txt.cabecera.gestion.entradas" />
				<span class="small-text"><fmt:message key="txt.cabecera.nombre.cliente" /></span>
			</div>
			<img src="<%= request.getContextPath() %>/images/logo_fondo2.png" alt="<fmt:message key="txt.cabecera.logo" />" class="logo" />
			
			<div id="td_idiomas" class="tituloblanco center-text">
				<html:link page="/Locale.do?idioma=es_ES"><img src="<%= request.getContextPath() %>/images/idiomas/es_ES.png" alt="es_ES" title="Español" /> ES</html:link>
				<html:link page="/Locale.do?idioma=gl_ES"><img src="<%= request.getContextPath() %>/images/idiomas/gl_ES.png" alt="gl_ES" title="Galego" /> GL</html:link>
			</div>
			
			<div class="header-login-info">
				<span class="login-text"><fmt:message key="txt.bienvenido" />, <%= ((SessionContainer)session.getAttribute("sessCon")).getUserContainer().getUsuario().getUsr() %></span>
				<button type="button" name="bot_salir" id="bot_salir" class="botones_estandar" onclick="logout();"><fmt:message key="boton.salir"/></button>
			</div>
		</div>
		<div id="td_fecha" class="tituloblanco">
			<div class="inner">00/00/00 - 00:00:00</div>
		</div>
		
		<div id="td_ayuda" class="tituloblanco">
			<div class="inner">
				<a href="#"><fmt:message key="txt.guia.taquillamanager" /></a>
				<a href="<%= request.getContextPath() %>/ayuda.jsp" target="principal"><fmt:message key="txt.ayuda" /></a>
			</div>
		</div>
		
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/jquery/jquery.min.js"></script>
		<script type="text/javascript">
			function recarga() {
				window.top.recargar();
			}

			function actualizaFecha() {
				var fecha = new Date();
				var anio = fecha.getFullYear();
				var mes = fecha.getMonth() + 1;
				var dia = fecha.getDate();
				var hora = fecha.getHours();
				var minutos = fecha.getMinutes();
				var segundos = fecha.getSeconds();
		
				var ddMMyyyy = dia + "/" + mes + "/" + anio;
				var hhmmss = hora + ":"
						+ ("00" + minutos).substring(("" + minutos).length) + ":"
						+ ("00" + segundos).substring(("" + segundos).length);
		
				document.getElementById("td_fecha").getElementsByClassName('inner')[0].innerHTML = ddMMyyyy + ' - ' + hhmmss;
			}
			
			function logout() {
				parent.principal.location = "<%= request.getContextPath() %>/logout.jsp";
			}
			
			$(document).on('ready', function() {
				<% if(GestorInformacionWeb.getParameter(request, "recargar").equals("true")) { %>
					recarga();
				<% } %>
				
				actualizaFecha();
				setInterval('actualizaFecha()',1000);
			});
		</script>
	</body>
</html>
