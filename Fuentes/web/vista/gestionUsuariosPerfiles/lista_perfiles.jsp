<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
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

		<title><fmt:message key="listado.niveis" /></title>

		<link rel="stylesheet" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" rel="stylesheet">
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/main.css" type="text/css" />
	</head>
	<body class="lista_perfiles_jsp">
		<div class="container-fluid">
	    	<logic:notEmpty name="sessCon" property="lista1">
		        <table id="hijos_null">
	            	<logic:iterate id="perfil" name="sessCon" property="lista1" scope="session" indexId="i">
	                	<tr>
		                    <td valign="top">
	    	                    <table id="TA_ELE_USU_<bean:write name="i"/>" class="menugrande">
	        	                    <tr>
	            	                    <td id="TD_ELE_<bean:write name="i"/>" class="menugrande">
	                    	                <logic:notEmpty name="perfil" property="usuarioVOs">
	                        	                <img id="img_<bean:write name="i"/>" onClick="ocultaUsuarios( <bean:write name="i"/> )" src="<%= request.getContextPath() %>/images/menos.gif" alt="" />
	                            	            <input type="radio" name="chkUsuario" onclick="habilita( <bean:write name="i"/> );" class="radio-button" />
	                                	        <a href="javascript:ocultaUsuarios( <bean:write name="i"/> )">
	                                        	    <bean:write name="perfil" property="perfilDTO.nomPerfil" />:&nbsp;
	                                    	        <bean:write name="perfil" property="perfilDTO.desPerfil" />
	                                            	<input type="hidden" id="codPerfil_<bean:write name="i"/>" name="codPerfil_<bean:write name="i"/>" value="<bean:write name="perfil" property="perfilDTO.codPerfil" />">
	                                            	<input type="hidden" id="nomPerfil_<bean:write name="i"/>" name="nomPerfil_<bean:write name="i"/>" value="<bean:write name="perfil" property="perfilDTO.nomPerfil" />">
	                                            	<input type="hidden" id="desPerfil_<bean:write name="i"/>" name="desPerfil_<bean:write name="i"/>" value="<bean:write name="perfil" property="perfilDTO.desPerfil" />">
	                                        	</a>
	                                    	</logic:notEmpty>
	                                    	<logic:empty name="perfil" property="usuarioVOs">
	                                        	<img id="img_<bean:write name="i"/>" onClick="muestraUsuarios( <bean:write name="i"/> )" src="<%= request.getContextPath() %>/images/mas.gif" alt="" />
	                                        	<input type="radio" name="chkUsuario" onclick="habilita( <bean:write name="i"/> );" class="radio-button" />
	                                        	<a href="javascript:muestraUsuarios( <bean:write name="i"/> )">
	                                            	<bean:write name="perfil" property="perfilDTO.nomPerfil" />:&nbsp;
	                                            	<bean:write name="perfil" property="perfilDTO.desPerfil" />
	                                            	<input type="hidden" id="codPerfil_<bean:write name="i"/>" name="codPerfil_<bean:write name="i"/>" value="<bean:write name="perfil" property="perfilDTO.codPerfil" />">
	                                            	<input type="hidden" id="nomPerfil_<bean:write name="i"/>" name="nomPerfil_<bean:write name="i"/>" value="<bean:write name="perfil" property="perfilDTO.nomPerfil" />">
	                                            	<input type="hidden" id="desPerfil_<bean:write name="i"/>" name="desPerfil_<bean:write name="i"/>" value="<bean:write name="perfil" property="perfilDTO.desPerfil" />">
	                                        	</a>
	                                    	</logic:empty>
	                                    	<input type="hidden" id="input_codPerfil_<bean:write name="i"/>" value="<bean:write name="i"/>" />
	                                	</td>
	                            	</tr>
	                        	</table>
	                    	</td>
	                	</tr>
	                	<logic:notEmpty name="perfil" property="usuarioVOs">
	                    	<logic:iterate id="usuarios_perfil" name="perfil" property="usuarioVOs" scope="page" indexId="j">
	                        	<tr>	
	        	                	<td valign="top">
	                                	<table id='hijos_<bean:write name="i"/>'>
	                                    	<tr>
	                                        	<td valign="top">
	                                            	<table id='t_<bean:write name="i"/><bean:write name="j"/>' class="menu-up">
	                                                	<tr>
	                                                    	<td>
	                                                        	<img src="<%=request.getContextPath()%>/images/shim.gif" alt="">
	                                                        	<img id='img_<bean:write name="i"/><bean:write name="j"/>' src="<%=request.getContextPath()%>/images/punto.gif" onClick='flechaClick('<bean:write name="i"/><bean:write name="j"/>')' alt="">
	    	                                                    <bean:write name="usuarios_perfil" property="nombre" />
	        	                                                <bean:write name="usuarios_perfil" property="apellido1" />
	            	                                            <bean:write name="usuarios_perfil" property="apellido2" />
	                	                                        (<bean:write name="usuarios_perfil" property="login" />)
	                        	                            </td>
	                    	                            </tr>
	                            	                </table>
	                                	        </td>
	                                    	</tr>
		                                </table>
	    	                        </td>
								</tr>
	            	        </logic:iterate>
	                	</logic:notEmpty>
	            	</logic:iterate>
	        	</table>
	    	</logic:notEmpty>
	
	    	<form id="formUsuarios" action="<%= request.getContextPath() %>/BusquedaPerfil.do?" onclick="crear();">
	        	<input type="hidden" id="idAccion" name="accion" value="mostrarUsuarios" />
	        	<input type="hidden" id="parIndice" name="indice" />
	    	</form>
    	</div>

    	<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/global.js"></script>
		<script type="text/javascript">
    		var srcFlechaDer="<%= request.getContextPath() %>/images/mas.gif";
    		var srcFlechaAba="<%= request.getContextPath() %>/images/menos.gif";

    		// La siguiente funcion unicamente visibiliza e invisibiliza los hijos de un nodo
    		function flechaClick(codFeudo) {
        		if (document.getElementById("hijos_" + codFeudo) != null) {
            		var hijosVisibles = (((document.getElementById("hijos_" + codFeudo)).style.display) == "");
            		var estilo = "";

            		if (hijosVisibles) { // Si los hijos eran visibles los hacemos invisibles y viceversa
                		estilo = "none";
                		(document.getElementById("img_" + codFeudo)).src = srcFlechaDer;
            		} else {
                		var tieneHijos = (document.getElementById("tiene_hijos_" + codFeudo)).value == "S";

                		if (tieneHijos) { // si tiene hijos giramos la flecha
                    		(document.getElementById("img_" + codFeudo)).src = srcFlechaAba;
            	    	}
            		}
            		
            		(document.getElementById("hijos_" + codFeudo)).style.display = estilo; 
        		}
        	}

    		function muestraUsuarios(linea) {
        		document.getElementById('parIndice').value = document.getElementById( 'input_codPerfil_' + linea ).value;
        		document.getElementById('formUsuarios').submit();
    		}

		    function ocultaUsuarios(linea) {    		
		        document.getElementById('idAccion').value = "ocultarUsuarios";
        		document.getElementById('parIndice').value = document.getElementById( 'input_codPerfil_' + linea ).value;
        		document.getElementById('formUsuarios').submit();
    		}

    		function habilita(linea) {
        		parent.habilitaBotones();
        		window.parent.document.getElementById('codPerfil').value = document.getElementById( 'codPerfil_' + linea ).value;
        		window.parent.document.getElementById('nomPerfil').value = document.getElementById( 'nomPerfil_' + linea ).value;
        		window.parent.document.getElementById('desPerfil').value = document.getElementById( 'desPerfil_' + linea ).value;
    		}
		</script>
	</body>
</html>