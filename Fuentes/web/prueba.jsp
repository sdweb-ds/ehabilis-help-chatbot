<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>

<% if (GestorInformacionWeb.isLoggedIn(request)) { %>
	<jsp:forward page="<%= GestorInformacionWeb.getForwardURLHome() %>" />
<% } %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ include file="pages/includes/header.jsp" %>

  	<div class="login-page">
			<div id="main-login-container">
				<div class="login-description">
					<fmt:message key="txt.login.bienvenida" />
				</div>
				
				<form id="formLogin" name="formLogin" action="<%= request.getContextPath() %>/Autenticacion.do" method="post">
					<div id="login-container">
						<div id="login-form-container">
							<div class="control-group">
								<label class="titulo" for="usuario"><i><fmt:message key="txt.usuario"/>:</i></label>
								<span class="input-control">
									<input type="text" id="usuario" name="login" class="input" size="15" autofocus="autofocus" />
								</span>
							</div>
							<div class="control-group">
								<label class="titulo" for="contrasinal"><i><fmt:message key="txt.contrasinal"/>:</i></label>
								<span class="input-control">
									<input type="password" id="contrasinal"  name="password" class="input" value="" size="15" />
								</span>
							</div>
							<div class="control-group">
								<button type="submit" class="login-button-2 botones_estandar" title="<fmt:message key="boton.entrar"/>"><fmt:message key="boton.entrar"/></button>
							</div>
							<div class="control-group standar cambiar-clave">
								<fmt:message key="txt.trocar.clave"/>: <a href="<%= request.getContextPath() %>/password.do" target="_self" class=""><img src="<%=request.getContextPath()%>/images/llave.gif" width="22" height="18" name="llave" title="<fmt:message key="txt.trocar.clave"/>"></a>
							</div>
							<logic:present name="loginForm" property="advertencia">			
								<bean:define id="advertencia" name="loginForm" property="advertencia" type="java.lang.String" />
                                                                <div class="standarRojoLetra" id="TD_Error"></div>
							</logic:present>
						</div>
					</div>					
				</form>
			</div>
		</div>

<%@ include file="pages/includes/footer.jsp" %>