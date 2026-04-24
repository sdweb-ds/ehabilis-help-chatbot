<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import=" es.sdweb.application.controller.util.SessionContainer" %>
<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>

<% if (!GestorInformacionWeb.isLoggedIn(request)) { %>
	<jsp:forward page="<%= GestorInformacionWeb.getForwardURLlogin() %>" />
<% } %>

<!DOCTYPE html>
<html>
	<head>
		<meta content="no-cache" http-equiv="Pragma" />
		<meta content="no-cache" http-equiv="Cache-Control" />
		<meta content="no-store" http-equiv="Cache-Control" />
		<meta content="max-age=0" http-equiv="Cache-Control" />
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
		<meta charset="utf-8" />

		<title><fmt:message key="crear.perfil" /></title>

		<link rel="stylesheet" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/main.css" type="text/css" />
	</head>
	<body class="crear_modificar_perfil table-body" onLoad="confirmar();">
		<div class="container-fluid">
			<logic:notEqual parameter="accion" value="cargarPopUpModificar">
				<div class="row row-30">
					<div class="row menugrande">
						<fmt:message key="crear.perfil" />
					</div>
				</div>
			</logic:notEqual>
			
			<div class="row">
				<div class="filter-form">
					<div class="row row-30">
						<div class="col-md-6">
							<div class="equal-form big-input">
								<label class="menugrande" for="TxtNomPerfil"><fmt:message key="txt.nombre" />:</label>
								<html:text property="nomPerfil" name="perfilForm" size="30" maxlength="50" styleClass="inputObligatorio" styleId="TxtNomPerfil"></html:text>
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="equal-form big-input">
								<label class="menugrande" for="TxtDesPerfil"><fmt:message key="txt.descripcion" />:</label>
								<html:text property="desPerfil" name="perfilForm" size="50" maxlength="55" styleClass="inputObligatorio" styleId="TxtDesPerfil"></html:text>
							</div>						
						</div>
					</div>
				
					<div class="row row-30">				
						<div class="subtitle">
								<label class="menugrande"><fmt:message key="listado.elementos" /></label>
						</div>
					</div>
					
					<div class="row">
						<iframe name="ifPerfiles" src="<%= request.getContextPath() %>/vista/gestionUsuariosPerfiles/lista_elementos.jsp" width="100%" height="100%" class="iframe-lista-elementos"></iframe>
					</div>
					
					<div class="row row-50">
						<div class="col-md-6">
							<div class="alert-container">
								<input type="checkbox" value="true" name="TxtIncluirSubMenus" onclick="" id="TxtIncluirSubMenus" class="cursor-pointer" checked="checked" />
								<label for="TxtIncluirSubMenus" class="cursor-pointer"><fmt:message key="txt.incluir.submenus" /></label>
							</div>
						</div>
						
						<div class="col-md-6 button-row">
							<!-- Si no estamos en la ventana modal de modificar perfil mostrar el botón limpiar -->
							<logic:notEqual parameter="accion" value="cargarPopUpModificar">
								<button type="button" name="bot_limpiar" class="botones_estandar" onClick="limpiar();"><fmt:message key="boton.limpiar"/></button>
							</logic:notEqual> <!-- Si estamos en la ventana modal de modificar perfil mostrar el botón cancelar en vez de limpiar -->
							<logic:equal parameter="accion" value="cargarPopUpModificar">
								<button type="button" name="bot_cancelar" class="botones_estandar" onClick="cancelar();"><fmt:message key="boton.cancelar"/></button>
							</logic:equal>
							<button type="button" name="bot_aceptar" class="botones_estandar" onClick="aceptar();"><fmt:message key="boton.aceptar"/></button>
						</div>
					</div>

					<!-- Alertas -->
		            <!-- Por defecto oculto y vacio, se carga y se muestra con JavaScript -->
		            <!-- Modificado comportamiento por defecto del boton cerrar para que oculte el div en lugar de eliminarlo --> 
		            <div id="alertas" role="alert" class="alert alert-dismissible fade in" style="display:none;">
		                <button aria-label="Close" class="close" type="button" onclick="$('#alertas').hide()">
		                    <span aria-hidden="true">&times;</span>
		                </button>
		                <!-- Contenido -->
		                <div id="alertas-content"></div>
		            </div>
		            
					<div style="display: none">
						<html:form styleId="perfilForm" action="/CrearModificarPerfil.do?" method="post">
							<input type="hidden" id="accion" name="accion" value="insertar" />
							<input type="hidden" id="subseleccionarTodos" name="seleccionarTodos" value="" />
							<input type="hidden" id="subcodPerfil" name="codPerfil" value="<bean:write name="perfilForm" property="codPerfil"/>" />
							<input type="hidden" id="subnomPerfil" name="nomPerfil" value="" />
							<input type="hidden" id="subdesPerfil" name="desPerfil" value="" />
							<input type="hidden" id="tipo" name="tipo" value="<bean:write name="perfilForm" property="tipo"/>" />
							<%= ((SessionContainer) session.getAttribute("sessCon")).getUtil2() %>
						</html:form>
					</div>
				</div>
			</div>
		</div>

		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/jquery/jquery.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/global.js"></script>
		<script type="text/javascript">
			function aceptar() {
				if ((document.getElementById("TxtNomPerfil").value != "") && (document.getElementById("TxtDesPerfil").value != "")) {
					document.getElementById("subnomPerfil").value = document.getElementById("TxtNomPerfil").value;
					document.getElementById("subdesPerfil").value = document.getElementById("TxtDesPerfil").value;
					document.getElementById("perfilForm").submit();
					//limpiar(); // Si se llama a limpiar aqui no se muestran los mensajes de exito y advertencia
				} else {
					mostrarAdvertencia('<fmt:message key="advertencia.campos.obligatorios"/>');
				}
		 	}

			function actualizar() {
				this.location = "<%= request.getContextPath() %>/CrearModificarPerfil.do?accion=cargar";
			}

			//Esta función se encarga de mostrar el pop-up de confirmación de alta o modificación de datos.
			//Lo hace en función del valor de una parámetro que llega en la request.			
			function confirmar() {
				<logic:equal name="perfilForm" property="confirmar" value="true">
					<logic:equal name="perfilForm" property="tipo" value="principal">
						<%-- window.open("<%=request.getContextPath()%>/vista/util/up_confirmar_modificar_crear.jsp?mensaje=O perfil foi gardado con éxito","",windowParams(300,150)); --%>
                    	mostrarExito('<fmt:message key="mensaje.perfil.creado.correctamente"/>');
					</logic:equal>
				</logic:equal>
 				<logic:equal name="perfilForm" property="confirmar" value="advertencia.perfil.duplicado">
					mostrarAdvertencia('<fmt:message key="advertencia.perfil.duplicado"/>');
 				</logic:equal>
 				<logic:equal name="perfilForm" property="confirmar" value="advertencia.perfil.sin.elementos">
					mostrarAdvertencia('<fmt:message key="advertencia.perfil.sin.elementos"/>');
				</logic:equal>
			}

			// Muestra un mensaje de informacion
			function mostrarExito(mensaje) {      
				// Añadir mensaje
				$('#alertas-content').empty();
				$('#alertas-content').append(mensaje);

				// Aplicar estilo (Clases posibles: alert-info(Azul); alert-danger(Rojo); alert-warning(Amarillo); alert-success(Verde))
				//$('#alertas').addClass('alert-info'); // Añadir la clase alert-info (Azul) asignada por defecto
				//$('#alertas').addClass('alert-danger'); // Añadir la clase alert-danger (Rojo)
				//$('#alertas').addClass('alert-warning'); // Añadir la clase alert-warning (Amarillo)
				$('#alertas').addClass('alert-success'); // Añadir la clase alert-success (Verde)

				// Mostrar mensaje 
				$('#alertas').show();
			}

			// Muestra un mensaje de advertencia 
			function mostrarAdvertencia(advertencia) {
				// Añadir mensaje
				$('#alertas-content').empty();
				$('#alertas-content').append(advertencia);

				// Aplicar estilo (Clases posibles: alert-info(Azul); alert-danger(Rojo); alert-warning(Amarillo); alert-success(Verde))
				//$('#alertas').addClass('alert-info'); // Añadir la clase alert-info (Azul) asignada por defecto
				$('#alertas').addClass('alert-danger'); // Añadir la clase alert-danger (Rojo)
				//$('#alertas').addClass('alert-warning'); // Añadir la clase alert-warning (Amarillo)
				//$('#alertas').addClass('alert-success'); // Añadir la clase alert-success (Verde)

				// Mostrar mensaje
				$('#alertas').show();
			}

			function resetearAdvertencia() {}

			// Funcion que se ejecuta al pulsar el boton limpiar en la pantalla de creacion de Perfil, vuelve a la pantalla Crear Perfil
			function limpiar() {
				this.location = "<%= request.getContextPath() %>/CrearModificarPerfil.do?accion=limpiar";			
			}

			// Funcion que se ejecuta al pulsar el boton cancelar en la venta modal de modificacion de Perfil, lanza un clic del boton cerrar de la ventana modal
			function cancelar() {
				$("#buscarPerfilesModalClose").trigger("click");
			}

			function seleccionarTodos() {
				document.getElementById("seleccionarTodos").value = document.getElementById("TxtSeleccionTodos").value;
				document.getElementById("nomPerfil").value = document.getElementById("TxtNomPerfil").value;
				document.getElementById("desPerfil").value = document.getElementById("TxtDesPerfil").value;
				document.getElementById("accion").value = "seleccionarTodos";
				document.getElementById("perfilForm").submit();
			}
		</script>
	</body>
</html>
