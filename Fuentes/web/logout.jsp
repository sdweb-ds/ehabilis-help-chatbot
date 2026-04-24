<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<!DOCTYPE html> 
<html>
	<head>
		<meta content="no-cache" http-equiv="Pragma" />
		<meta content="no-cache" http-equiv="Cache-Control" />
		<meta content="no-store" http-equiv="Cache-Control" />
		<meta content="max-age=0" http-equiv="Cache-Control" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta charset="utf-8" />
		
		<title><fmt:message key="txt.logout"/></title>
		
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link href="<%= request.getContextPath() %>/styles/main.css" rel="stylesheet" type="text/css">
	</head>
	<body class="logout_jsp">
		<div class="background-principal">
			<div class="logout-inner">
				<div class="row">
					<div class="col-md-12 TituloEntrada">
						<fmt:message key="confirmacion.sair"/>
					</div>
				</div>
				
				<div class="row logout-buttons">
					<div class="col-md-6 right-text">
						<button type="button" name="bot_aceptar" id="bot_aceptar" class="botones_estandar" onclick="aceptar();"><fmt:message key="boton.aceptar"/></button>
					</div>
					
					<div class="col-md-6">
						<button type="button" name="bot_cancelar" id="bot_cancelar" class="boton_secundario" onclick="cancelar();"><fmt:message key="boton.cancelar"/></button>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			function aceptar() {
				window.parent.location = "<%= request.getContextPath() %>/Salir.do?";
			}
			
			function cancelar() {
				window.parent.location = "index_principal.jsp";
			}
		</script>
	</body>
</html>
