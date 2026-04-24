<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/sdweb-comun.tld" prefix="comun" %>

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
	<body>
    	<logic:notEmpty name="sessCon" property="lista4">
        	<table class="table-content table-7-columns">
        		<logic:iterate id="usuario" name="sessCon" property="lista4" scope="session" indexId="i">
        			<comun:isImpar name="i">
        				<tr id="TR<bean:write name="i"/>" class="colorLineaImpar" onclick="seleccionar(<bean:write name="i"/>);">
        			</comun:isImpar>
        			<comun:isPar name="i">
        				<tr id="TR<bean:write name="i"/>" class="colorLineaPar" onclick="seleccionar(<bean:write name="i"/>);">
        			</comun:isPar>
	                    <td id="TD_NIF_<bean:write name="i"/>" class="standar cell-85 radio-button" align="left"><bean:write name="usuario" property="nif" /></td>
	                    <td id="TD_LOG_<bean:write name="i"/>" class="standar cell-135 radio-button" align="left">
	                    	<bean:write name="usuario" property="login" />
	                    	<input type="hidden" id="input_login_<bean:write name="i"/>" value="<bean:write name="usuario" property="login" />" />
	                    </td>
	
	                    <td id="TD_NOM_<bean:write name="i"/>" class="standar cell-135 radio-button"><bean:write name="usuario" property="nombre" /></td>
	                    <td id="TD_AP1_<bean:write name="i"/>" class="standar cell-135 radio-button"><bean:write name="usuario" property="apellido1" /></td>
	                    <td id="TD_AP2_<bean:write name="i"/>" class="standar cell-135 radio-button"><bean:write name="usuario" property="apellido2" /></td>
	                    <td id="TD_TEL_<bean:write name="i"/>" class="standar cell-85 radio-button"><bean:write name="usuario" property="telefono" /></td>
	                    <td class="standar cell-40 radio-button"><input type="radio" name="sel" class="radio-button" /></td>
	                </tr>
	            </logic:iterate>
	        </table>
	    </logic:notEmpty>
	    
	    <script type="text/javascript" src="<%=request.getContextPath()%>/Scripts/jquery/jquery.min.js"></script>
		<script>
			var linSel = -1;
			
			function seleccionar( linea ) {
				if (linea == linSel) {	
					linSel = -1;
					restaurar( linea );
				} else {
					var oldLin = linSel;
					linSel = linea;
					document.getElementById( 'TR' + linea ).className = 'colorLineaResaltada radio-button';
					$('#TR' + linea).find('input[type=radio]').first().prop('checked', true);
					restaurar( oldLin );
					window.parent.document.getElementById('parLogin').value = document.getElementById( 'input_login_' + linea ).value;
				}
			}
			
			function restaurar(linea) {
				$('#TR' + linea).find('input[type=radio]').first().prop('checked', false);
				
				if ((linea != linSel) && (linea != -1)) {
					if (linea % 2 == 0) {
						document.getElementById('TR' + linea).className = 'colorLineaPar';
					} else {
						document.getElementById('TR' + linea).className = 'colorLineaImpar';
					}
				}
			}
		</script>
	</body>
</html>