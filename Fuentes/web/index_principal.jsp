<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>
<%@ page import="es.sdweb.application.controller.util.SessionContainer" %>

<%  if (!GestorInformacionWeb.isLoggedIn(request)) { %>
	<jsp:forward page="<%= GestorInformacionWeb.getForwardURLlogin() %>" />
<% } %>


<%@ include file="pages/includes/header.jsp" %>
	<div class="main-container">
		<table id="main-table">
			<tr>
				<td id="menu-cell">
					<div id="iframe-menu" class="menu-background">
						<div class="menu-header"><fmt:message key="menu"/></div>
			
						<hr class="separator" />
						
						<%= ((SessionContainer) session.getAttribute("sessCon")).getMenu() %>
						
						<%-- <div class="logo-menu">
							<a class="menu-link" href="<%= request.getContextPath() %>/frame_principal.jsp"><img src="<%= request.getContextPath() %>/images/logo-taquilla_manager.png" alt="Logo" /></a>
						</div> --%>
					</div>
				</td>
				<td>
					<iframe name="principal" src="<%= request.getContextPath() %>/frame_principal.jsp" noresize id="iframe-principal"></iframe>
				</td>
			</tr>
		</table>
	</div>
	
	<div id='loader' class='loader'><span class='content'><i class='fa fa-spinner fa-pulse'></i></span></div>
	
	<% 
		GestorInformacionWeb.enqueueScript(request.getContextPath() + "/Scripts/global.js");
	%>
	
<%--@ include file="pages/includes/footer.jsp" --%>
<%@ include file="footer.jsp" %>