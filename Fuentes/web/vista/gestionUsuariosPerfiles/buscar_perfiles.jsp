<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

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
		
		<title><fmt:message key="alta.perfil" /></title>

		<link rel="stylesheet" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/main.css" type="text/css" />
	</head>
	<body onload="inicializa();" class="buscar_perfiles table-body">
		<div class="container-fluid">
			<div class="row row-30">
				<div class="row menugrande">
					<fmt:message key="listado.perfiles" />
				</div>
			</div>
			
			<div class="row">
				<div class="filter-form">
					<div class="row">
						<iframe name="ifPerfiles" src="<%= request.getContextPath() %>/vista/gestionUsuariosPerfiles/lista_perfiles.jsp" class="profiles-list"></iframe>
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
			                <!-- Si no estamos en la ventana modal de modificar perfil mostrar el botón limpiar -->
			                <button type="button" id="crearlink" class="botones_estandar" onClick="crear();"><fmt:message key="boton.crear"/></button>
							<button type="button" id="modificarlink" class="botones_estandar" onClick="modificar();"><fmt:message key="boton.modificar"/></button>
				            <button type="button" id="eliminarlink" class="botones_estandar" onClick="eliminar();"><fmt:message key="boton.eliminar"/></button>
						</div>
					</div>
				</div>
			</div>
	
			<form id="formSel" action="<%= request.getContextPath() %>/UpCrearModificarPerfil.do?">
				<input type="hidden" id="idAccion" name="accion" value="cargarModificar" />
				<input type="hidden" id="codPerfil" name="codPerfil" value='vacio' />
	    		<input type="hidden" id="nomPerfil" name="nomPerfil" value='vacio' />
				<input type="hidden" id="desPerfil" name="desPerfil" value='vacio' />
			</form>
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

		  	function crear() {
		  		// El boton crear lleva a la pantalla de crear perfil en vez de abrirla en un pop up
				this.location="<%= request.getContextPath() %>/CrearModificarPerfil.do?accion=cargar";
			}

  			function actualizar() {
				this.location="<%= request.getContextPath() %>/BusquedaPerfil.do?accion=cargar";
			}   

			function modificar() {
        		// Establecer titulo para la ventana modal
				$('#buscarPerfilesModalTitle').text("<fmt:message key="modificar.perfil" />");
	
	        	$('#id_modal_aviso').hide();
	        	$('#id_modal_pregunta').hide();
	
				// Mostrar la pantalla de edición de Perfiles como ventana modal Bootstrap en lugar de como PopUp
				if (document.getElementById('codPerfil').value != 'vacio') {
					// Establecer contenido de la ventana modal (resultado de la peticion Ajax)
					$("#buscarPerfilesModalBody").load("<%= request.getContextPath() %>/CrearModificarPerfil.do", {
						"accion": "cargarPopUpModificar",
						"codPerfil": document.getElementById('codPerfil').value,
						"nomPerfil": document.getElementById('nomPerfil').value,
						"desPerfil": document.getElementById('desPerfil').value
					});
				} else {
	            	// Establecer contenido de la ventana modal
	            	$("#buscarPerfilesModalBody").text("<fmt:message key="advertencia.perfil.seleccionar"/>");
	            
	            	$('#id_modal_pregunta').hide();
	            	$('#id_modal_aviso').show();
				}
	
	        	// Mostrar ventana modal
	        	$('#buscarPerfilesModal').modal();
			}
	
			function eliminar() {
		  		/* if (document.getElementById('codPerfil').value!='vacio') {
		  			window.open("*/<%--=request.getContextPath()--%>/*/vista/gestionUsuariosPerfiles/up_confirmacion_eliminar_perfil.jsp","confirmar_eliminar_solicitud",windowParams(300,130));
		 		} */
	
		  		if (document.getElementById('codPerfil').value != 'vacio') {
	            	// Establecer titulo para la ventana modal
	            	$('#buscarPerfilesModalTitle').text("<fmt:message key="ventana.confirmacion.titulo"/>");
	
	            	// Establecer contenido de la ventana modal
	            	$("#buscarPerfilesModalBody").text("<fmt:message key="confirmacion.eliminar.perfil"/>");
	
		            // Eliminar los eventos onClic que pueda haber para evitar que se acumulen
	            	$('#id_modal_aceptar').off('click');
	            
	            	// Asignar funcionalidad al boton aceptar
	            	var funcion = function aceptarEliminar(){ window.location = "<%= request.getContextPath() %>/BusquedaPerfil.do?accion=eliminar&codPerfil=" + window.document.getElementById('codPerfil').value; }
	            	$('#id_modal_aceptar').on('click', funcion);
	
	            	$('#id_modal_aviso').hide();
	            	$('#id_modal_pregunta').show();
		  		} else {
		            // Establecer titulo para la ventana modal
	    	        $('#buscarPerfilesModalTitle').text("<fmt:message key="eliminar.perfil"/>");
	
	            	// Establecer contenido de la ventana modal
	            	$("#buscarPerfilesModalBody").text("<fmt:message key="advertencia.perfil.seleccionar"/>");
	
	            	$('#id_modal_pregunta').hide();
	            	$('#id_modal_aviso').show();
	  			}
	
	        	// Mostrar ventana modal
	        	$('#buscarPerfilesModal').modal();
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
