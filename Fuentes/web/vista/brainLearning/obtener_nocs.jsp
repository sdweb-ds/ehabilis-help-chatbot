<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>
<%@ page import="es.sdweb.application.controller.actionforms.brainLearning.ObtenerNocsForm" %>

<% if (!GestorInformacionWeb.isLoggedIn(request)) { %>
	<jsp:forward page="<%= GestorInformacionWeb.getForwardURLlogin() %>" />
<% } %>


<% ObtenerNocsForm frm = (ObtenerNocsForm) GestorInformacionWeb.getAttributeObj(request, "obtenerNocsForm"); %>


<!DOCTYPE html>
<html>
	<head>
		<meta content="no-cache" http-equiv="Pragma" />
		<meta content="no-cache" http-equiv="Cache-Control" />
		<meta content="no-store" http-equiv="Cache-Control" />
		<meta content="max-age=0" http-equiv="Cache-Control" />
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
		<meta charset="utf-8" />

		<title><fmt:message key="obtener.nocs" /></title>

		<link rel="stylesheet" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/main.css" type="text/css" />
	</head>
	<body onload="inicializa();" class="obtener_nocs table-body">
		<div class="container-fluid">
			<div class="row row-30">
				<div class="row menugrande">
					<fmt:message key="obtener.nocs" />
				</div>
			</div>

			<div class="row">
				<div class="filter-form">
					<div class="row">
						<html:form styleId="obtenerNocsForm" action="/ObtenerNocs.do?accion=procesar" method="post">

							<div class="row">
								<div class="col-md-6">
									<div class="equal-form big-input">
										<label class="menugrande" for="textoEntrada"><fmt:message key="txt.texto.entrada" />:</label>
									</div>
								</div>

								<div class="col-md-6">
									<div class="equal-form big-input">
										<label class="menugrande" for="textoSalida"><fmt:message key="txt.texto.salida" />:</label>
									</div>
								</div>
							</div>


							<div class="row">
								<div class="col-md-6">
									<div class="equal-form big-input">
										<html:textarea property="textoEntrada" name="obtenerNocsForm" rows="10" styleClass="inputObligatorio" styleId="textoEntrada"></html:textarea>
									</div>
								</div>

								<div class="col-md-6">
									<div class="equal-form big-input">
										<html:textarea property="textoSalida" name="obtenerNocsForm" rows="10" styleClass="" disabled="true" styleId="textoSalida"></html:textarea>
									</div>
								</div>
							</div>


						</html:form>
					</div>

					<div class="row row-50">
						<div class="col-md-6">
							<div class="alert-container">
								<!-- Alertas -->
								<!-- Por defecto oculto y vacio, se carga y se muestra con JavaScript -->
								<!-- Margen inferior forzado a 5px, por defecto 20px -->
								<!-- El comportamiento por defecto del boton cerrar es eliminar el div -->
								<div id="alertas" role="alert" class="alert alert-dismissible fade in" style="margin-bottom:5px;display:none;">
								    <button aria-label="Close" data-dismiss="alert" class="close" type="button">
									<span aria-hidden="true">&times;</span>
								    </button>
								    <!-- Contenido -->
								    <div id="alertas-content"></div>
								</div>
							</div>
						</div>

						<div class="col-md-6 button-row">
				        		<button type="button" id="obtenerNocsLimpiar" class="boton_secundario" onClick="limpiarTexto();"><fmt:message key="boton.limpiar"/></button>
				        		<button type="button" id="obtenerNocsLink" class="botones_estandar" onClick="obtenerNocs();"><fmt:message key="boton.obtener.nocs"/></button>
						</div>
					</div>
				</div>
			</div>

		</div>

		<!-- Ventana modal para Modificar Perfil y mostrar alertas/confirmaciones -->
		<div class="modal fade" id="buscarPerfilesModal" tabindex="-1" role="dialog" aria-labelledby="buscarPerfilesModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button id="buscarPerfilesModalClose" type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="buscarPerfilesModalTitle"></h4>
					</div>
					<div class="modal-body" id="buscarPerfilesModalBody"></div>

					<!-- Los botones se muestran u ocultan cuando se muestra la ventana en funcion del tipo de uso que se le vaya a dar -->
					<!-- Si es una ventana de aviso solamente tiene el boton cerrar -->
					<div id="id_modal_aviso" class="modal-footer" style="display:none">
					    <button id="id_modal_cerrar" name="id_modal_cerrar" type="button" class="btn btn-primary" data-dismiss="modal"><fmt:message key="boton.cerrar"/></button>
					</div>

					<!-- Si es una ventana de pregunta tiene los botones cancelar y aceptar -->
					<!-- La funcionalidad del boton aceptar se asigna al mostrar la ventana -->
					<div id="id_modal_pregunta" class="modal-footer" style="display:none">
					    <button id="id_modal_cancelar" type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="boton.cancelar"/></button>
					    <button id="id_modal_aceptar" name="id_modal_aceptar" type="button" class="btn btn-primary"><fmt:message key="boton.aceptar"/></button>
					</div>
				</div>
			</div>
		</div>

		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/global.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/jquery/jquery.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/bootstrap/js/bootstrap.min.js"></script>

		<script type="text/javascript">
		  	function habilitaBotones() {}

			// onClick del botón limpiarTexto
		  	function limpiarTexto() {
				$("#textoEntrada").val("");
				$("#textoSalida").val("");
			}

			// onClick del botón obtenerNocs
		  	function obtenerNocs() {
				document.getElementById('obtenerNocsForm').submit();
			}

			// Muestra un mensaje de error
			function mostrarError(error) {
				// Añadir mensaje
				$('#alertas-content').empty();
				$('#alertas-content').append(error);

				// Aplicar estilo (Clases posibles: alert-info(Azul); alert-danger(Rojo); alert-warning(Amarillo); alert-success(Verde))
				//$('#alertas').addClass('alert-info'); // Añadir la clase alert-info (Azul) asignada por defecto
				$('#alertas').addClass('alert-danger'); // Añadir la clase alert-danger (Rojo)
				//$('#alertas').addClass('alert-warning'); // Añadir la clase alert-warning (Amarillo)
				//$('#alertas').addClass('alert-success'); // Añadir la clase alert-success (Verde)

				// Mostrar mensaje 
				$('#alertas').show();
			}

			function resetearAdvertencia() {}

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

			function inicializa() {
				<logic:equal name="advertencia" value="advertencia.perfil.con.usuarios">
			    		mostrarError('<fmt:message key="advertencia.perfil.con.usuarios" />');
				</logic:equal>
				<logic:equal name="mensaje" value="mensaje.perfil.modificado.correctamente">
				    mostrarExito('<fmt:message key="mensaje.perfil.modificado.correctamente" />');
				</logic:equal>
				<logic:equal name="mensaje" value="mensaje.perfil.eliminado.correctamente">
		    			mostrarExito('<fmt:message key="mensaje.perfil.eliminado.correctamente" />');
				</logic:equal>
			}
		</script>
	</body>
</html>
