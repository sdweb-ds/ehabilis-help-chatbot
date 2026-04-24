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

		<title><fmt:message key="action.error.nologin" /></title>

		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/main.css" />
	</head>
	<body class="nologin_jsp">
		<div id="mensajePrincipal">
			<img src="<%= request.getContextPath() %>/images/iconoError.gif" />
			<span id="error"><fmt:message key="txt.error"/></span>
			<span id="queError"><fmt:message key="action.error.nologin"/></span>
		</div>
	</body>
</html>