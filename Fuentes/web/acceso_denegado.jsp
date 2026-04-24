<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %> 
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
		
		<html:base />
		
		<title><fmt:message key="acceso.denegado" /></title>
		<style>
			* {
				margin:0px;
				padding:0px;
				font-family:"Verdana",sans-serif;
			}

			body {
				height:100%;
				width:100%;
				position:fixed;
				background-color: #ffffff;
			}
			
			a {
				text-decoration:none;
			}

			#mensajePrincipal {
				position:relative;
				left:50%;
				margin-top:15%;
				margin-left:-300px;
				text-align:center;
				vertical-align:center;
				width:600px;
			}
			 
			#error {
				position:relative;
				top:-1em;
				color:#FF7800;
				font-size:150%;
				font-variant:italic;
				font-weight:bold;
			}
			 
			#queError {
				color:#2B4EA1;
				font-weight:bold;
				font-size:90%;
				width:450px;
			}
			 
			#masDetalles {
				position:relative;
				left:50%;
				cursor:pointer;
				margin-top:10px;
				margin-left:-286px;
				height:18px;
				width:572px;
				background:transparent url(<%= request.getContextPath() %>/images/masDetalles.gif) no-repeat scroll left top;
				color:#FFF;
				padding-left:20px;
				font-weight:bold;
			}
			 
			a:hover div#masDetalles {
				background:transparent url(<%= request.getContextPath() %>/images/masDetallesOver.gif) no-repeat scroll left top;
			}
			 
			#detalles {
				position:relative;
				left:50%;
				margin-left: -255px;		
				height:18px;
				width:510px;
				background-color:#BADBFF;
				border:1px solid #496DC3;
				overflow:auto;
				font-size:70%;
				color:#496DC3;
			}
			 
			#textoDetalles {
				padding:8px 20px;
			}
		</style>
	</head>
	<body>
		<div id="mensajePrincipal">
			<img src="<%= request.getContextPath() %>/images/iconoError.gif" />
			<span id="error"><fmt:message key="txt.error"/></span><span id="queError"><fmt:message key="acceso.denegado"/></span>
		</div>
	</body>
</html>