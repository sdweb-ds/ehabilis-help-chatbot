<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html; charset=utf-8" %>

<!DOCTYPE html> 
<html>
	<head>
		<meta content="no-cache" http-equiv="Pragma" />
		<meta content="no-cache" http-equiv="Cache-Control" />
		<meta content="no-store" http-equiv="Cache-Control" />
		<meta content="max-age=0" http-equiv="Cache-Control" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta charset="utf-8" />
		
		<title><fmt:message key="frame.derecho" /></title>
		
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link href="<%= request.getContextPath() %>/styles/main.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<div class="login-page">
			<div id="main-login-container">
				<div class="login-description">
					<fmt:message key="txt.login.bienvenida" />
				</div>
				
				<form id="formLogin" name="formLogin" action="<%= request.getContextPath() %>/Autenticacion.do" method="post" target="_top">
					<div id="login-container">
						<div id="login-form-container">
							<div class="control-group">
								<label class="titulo" for="usuario"><i><fmt:message key="txt.usuario"/>:</i></label>
								<span class="input-control">
									<input type="text" id="usuario" name="login" class="input" size="15" maxlength="30" autofocus="autofocus" />
								</span>
							</div>
							<div class="control-group">
								<label class="titulo" for="contrasinal"><i><fmt:message key="txt.contrasinal"/>:</i></label>
								<span class="input-control">
									<input type="password" id="contrasinal"  name="password" class="input" value="" size="15" maxlength="30" />
								</span>
							</div>
							<div class="control-group">
								<button type="submit" class="login-button-2 botones_estandar" title="<fmt:message key="boton.entrar"/>"><fmt:message key="boton.entrar"/></button>
							</div>
							<div class="control-group standar cambiar-clave">
								<fmt:message key="txt.trocar.clave"/>: <a href="<%= request.getContextPath() %>/vista/gestionUsuariosPerfiles/cambiar_clave.jsp" target="_self" class=""><img src="<%=request.getContextPath()%>/images/llave.gif" width="22" height="18" name="llave" title="<fmt:message key="txt.trocar.clave"/>"></a>
							</div>
							<div class="standarRojoLetra" id="TD_Error"></div>
						</div>
					</div>					
				</form>
			</div>
		</div>
		
		<script type="text/javascript">
			function mostrarError(error) {
				document.getElementById('TD_Error').innerHTML = error;
			}
		</script>
	</body>
</html>
