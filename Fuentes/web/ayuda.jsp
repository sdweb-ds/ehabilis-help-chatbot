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
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
		<meta charset="utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/main.css" />
	</head>
	<body class="ayuda_jsp">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12 center-text">
					<h2 class="menugrande"><fmt:message key="txt.ayuda.taquilla.manager" /></h2>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12">
					<p class="center-text">
						<fmt:message key="txt.ayuda.descripcion" />
					</p>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12 center-text">
					<button class="botones_estandar" type="button" id="volver" onclick="volver();"><fmt:message key="txt.volver" /></button>
				</div>
			</div>
		</div>
		
		<script type="text/javascript">
			function volver() {
				window.top.location = "index_principal.jsp";
			}
		</script>
	</body>
</html>