<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="java.util.List" %>

<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>
<%@ page import="es.sdweb.application.model.GestionUsuarioFacade" %>
<%@ page import="es.sdweb.application.model.dto.UsrDTO" %>
<%@ page import="es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles.BusquedaUsuarioForm" %>

<% if (!GestorInformacionWeb.isLoggedIn(request)) { %>
	<jsp:forward page="<%= GestorInformacionWeb.getForwardURLlogin() %>" />
<% } %>

<% BusquedaUsuarioForm frm = (BusquedaUsuarioForm) GestorInformacionWeb.getAttributeObj(request, "busquedaUsuarioForm"); %>

<!DOCTYPE html>
<html>
	<head>
		<meta content="no-cache" http-equiv="Pragma" />
		<meta content="no-cache" http-equiv="Cache-Control" />
		<meta content="no-store" http-equiv="Cache-Control" />
		<meta content="max-age=0" http-equiv="Cache-Control" />
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
		<meta charset="utf-8" />

		<title><fmt:message key="cabecera.buscar.usuarios" /></title>
		
		<link rel="stylesheet" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/fontawesome/css/font-awesome.min.css" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/style_autocompletado.css" />
		<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/main.css" />
	</head>
	<body onload="onLoadBuscar();" class="table-body buscar-usuarios">
		<div class="container-fluid">
			<div class="row row-30">
				<div class="row menugrande">
					<fmt:message key="buscar.usuarios" />
				</div>
			</div>
			
			<div class="row row-150">
				<html:form styleId="formBusquedaUsuario" action="/BusquedaUsuario.do?accion=buscar" method="post">
					<div class="filter-form">
						<div class="subtitle">
							<fmt:message key="datos.usuario" />
						</div>
					
						<div class="row">
							<div class="col-md-3">
								<div class="equal-form">
									<label class="menugrande" for="TxtNif"><fmt:message key="txt.nif" />:</label>
									<html:text property="nif" name="busquedaUsuarioForm" maxlength="10" styleClass="input" styleId="TxtNif" disabled="false"></html:text>
								</div>
								
							</div>
							
							<div class="col-md-3">
								<div class="equal-form big-input">
									<label class="menugrande" for="TxtNombre"><fmt:message key="txt.nombre" />:</label>
									<html:text property="nombre" name="busquedaUsuarioForm" maxlength="30" styleClass="input" styleId="TxtNombre" disabled="false"></html:text>
								</div>
							</div>
							
							<div class="col-md-3">
								<div class="equal-form">
									<label class="menugrande" for="TxtApellido1"><fmt:message key="txt.apellido1" />:</label>
									<html:text property="apellido1" name="busquedaUsuarioForm" size="20" maxlength="30" styleClass="input" styleId="TxtApellido1" disabled="false"></html:text>
								</div>
							</div>
							
							<div class="col-md-3">
								<div class="equal-form">
									<label class="menugrande" for="TxtApellido2"><fmt:message key="txt.apellido2" />:</label>
									<html:text property="apellido2" name="busquedaUsuarioForm" size="20" maxlength="30" styleClass="input" styleId="TxtApellido2" disabled="false"></html:text>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-3">
								<div class="equal-form">
									<label class="menugrande" for="TxtLogin"><fmt:message key="txt.login" />:</label>
									<html:text property="login" name="busquedaUsuarioForm" maxlength="20" styleClass="input" styleId="TxtLogin" disabled="false"></html:text>
								</div>
							</div>
							
							<div class="col-md-3">
								<div class="equal-form big-input">
									<label class="menugrande" for="TxtPerfil"><fmt:message key="txt.perfil" />:</label>
									<html:select styleClass="menugrande" name="busquedaUsuarioForm" styleId="TxtPerfil" property="codPerfil">
										<html:optionsCollection name="busquedaUsuarioForm" property="perfiles" value="codPerfil" label="nomPerfil" styleClass="input" />
									</html:select>
								</div>
							</div>
						</div>
					
						<div class="row">
							<div class="col-md-12 button-group">
								<button type="button" name="bot_limpiar" id="bot_limpiar" class="botones_estandar" onClick="limpiarBuscar();"><fmt:message key="boton.limpiar" /></button>
								<button type="button" name="bot_buscar" id="bot_buscar" class="botones_estandar" onClick="enviarBuscar();"><fmt:message key="boton.buscar" /></button>
							</div>
						</div>
						
						<html:hidden styleId="exito" name="busquedaUsuarioForm" property="exito" />
						<html:hidden styleId="order" name="busquedaUsuarioForm" property="order" />
						<html:hidden styleId="orderType" name="busquedaUsuarioForm" property="orderType" />
					</div>
		    	</html:form>
	    	</div>
	    	
	    	<div class="row row-30">
	    		<table class="table-header table-7-columns">
					<thead>
						<tr>
							<th class="standarblanco cell-85 can-order"
								<% if(!frm.getOrder().equals("nif")) { %>
									onclick="setOrder('nif', 'ASC');"><fmt:message key="txt.nif" />&nbsp;<i class="fa fa-sort"></i>
								<% } else { %>
									<% if(frm.getOrderType().equalsIgnoreCase("DESC")) { %>
										onclick="setOrder('nif', 'ASC');"><fmt:message key="txt.nif" />&nbsp;<i class="fa fa-sort-desc"></i>
									<% } else { %>
										onclick="setOrder('nif', 'DESC');"><fmt:message key="txt.nif" />&nbsp;<i class="fa fa-sort-asc"></i>
									<% } %>
								<% } %>
							</th>
							<th class="standarblanco cell-135 can-order"
								<% if(!frm.getOrder().equals("login")) { %>
									onclick="setOrder('login', 'ASC');"><fmt:message key="txt.login" />&nbsp;<i class="fa fa-sort"></i>
								<% } else { %>
									<% if(frm.getOrderType().equalsIgnoreCase("DESC")) { %>
										onclick="setOrder('login', 'ASC');"><fmt:message key="txt.login" />&nbsp;<i class="fa fa-sort-desc"></i>
									<% } else { %>
										onclick="setOrder('login', 'DESC');"><fmt:message key="txt.login" />&nbsp;<i class="fa fa-sort-asc"></i>
									<% } %>
								<% } %>
							</th>
							<th class="standarblanco cell-135 can-order"
								<% if(!frm.getOrder().equals("nombre")) { %> 
									onclick="setOrder('nombre', 'ASC');"><fmt:message key="txt.nombre" />&nbsp;<i class="fa fa-sort"></i>
								<% } else { %>
									<% if(frm.getOrderType().equalsIgnoreCase("DESC")) { %>
										onclick="setOrder('nombre', 'ASC');"><fmt:message key="txt.nombre" />&nbsp;<i class="fa fa-sort-desc"></i>
									<% } else { %>
										onclick="setOrder('nombre', 'DESC');"><fmt:message key="txt.nombre" />&nbsp;<i class="fa fa-sort-asc"></i>
									<% } %>
								<% } %>
							</th>
							<th class="standarblanco cell-135 can-order"
								<% if(!frm.getOrder().equals("apellido1")) { %> 
									onclick="setOrder('apellido1', 'ASC');"><fmt:message key="txt.apellido1" />&nbsp;<i class="fa fa-sort"></i>
								<% } else { %>
									<% if(frm.getOrderType().equalsIgnoreCase("DESC")) { %>
										onclick="setOrder('apellido1', 'ASC');"><fmt:message key="txt.apellido1" />&nbsp;<i class="fa fa-sort-desc"></i>
									<% } else { %>
										onclick="setOrder('apellido1', 'DESC');"><fmt:message key="txt.apellido1" />&nbsp;<i class="fa fa-sort-asc"></i>
									<% } %>
								<% } %>
							</th>
							<th class="standarblanco cell-135 can-order"
								<% if(!frm.getOrder().equals("apellido2")) { %>
									onclick="setOrder('apellido2', 'ASC');"><fmt:message key="txt.apellido2" />&nbsp;<i class="fa fa-sort"></i>
								<% } else { %>
									<% if(frm.getOrderType().equalsIgnoreCase("DESC")) { %>
										onclick="setOrder('apellido2', 'ASC');"><fmt:message key="txt.apellido2" />&nbsp;<i class="fa fa-sort-desc"></i>
									<% } else { %>
										onclick="setOrder('apellido2', 'DESC');"><fmt:message key="txt.apellido2" />&nbsp;<i class="fa fa-sort-asc"></i>
									<% } %>
								<% } %>
							</th>
							<th class="standarblanco cell-85">
								<fmt:message key="txt.telefono" />
							</th>
							<th class="cell-40"></th>
						</tr>
					</thead>
				</table>
	    	</div>
	    	
	    	<div class="row">
	    		<iframe id="ifUsuarios" name="ifUsuarios" src="<%= request.getContextPath() %>/vista/gestionUsuariosPerfiles/lista_usuarios.jsp" width="100%" height="100%" scrolling="yes"></iframe>
	    	</div>
	    	
	    	<div class="row row-50">
				<div class="col-md-12 button-row botonera-inferior">
					<button type="button" name="bot_crear" id="bot_crear" class="botones_estandar" onClick="crear();"><fmt:message key="boton.crear" /></button>
					<button type="button" name="bot_modificar" id="bot_modificar" class="botones_estandar" onClick="modificar();"><fmt:message key="boton.modificar" /></button>
					<button type="button" name="bot_eliminar" id="bot_eliminar" class="botones_estandar" onClick="eliminar();"><fmt:message key="boton.eliminar" /></button>
				</div>
			</div>
	    	
	    	<!-- En este input el frame que lista los usuarios guarda el login del usuario que se encuentre seleccionado -->
	    	<input type="hidden" id="parLogin" name="parLogin" value="" />
	    </div>

    	<!-- Ventana modal para Modificar Usuario y mostrar alertas/confirmaciones -->
	    <div class="modal fade" id="buscarUsuariosModal" tabindex="-1" role="dialog" aria-labelledby="buscarUsuariosModalLabel">
        	<div class="modal-dialog" role="document">
	            <div class="modal-content">
    	            <div class="modal-header">
	                    <button id="buscarUsuariosModalClose" type="button" class="close" data-dismiss="modal" aria-label="<fmt:message key="boton.cerrar" />">
	                        <span aria-hidden="true">&times;</span>
	                    </button>
	                    <h4 class="modal-title" id="buscarUsuariosModalTitle"></h4>
	                </div>

	                <!-- div inner para que en caso de que haya scroll mantenga los margenes -->
	                <div class="modal-body" id="buscarUsuariosModalBody"><div class="modal-body-inner" id="buscarUsuariosModalBodyInner"></div></div>

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
	    
	    <script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/validator.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/global.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/calendario3.js"></script>

		<script type="text/javascript" src="<%=request.getContextPath()%>/Scripts/jquery/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/Scripts/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/Scripts/jquery/jquery.autocomplete.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/Scripts/autocompletadoUtil.js"></script>
		
		<script type="text/javascript">
			$(function () {
				'use strict';

				var usuarios = <%= frm.getLookup() %>;
				
				usuarios = parse(usuarios);
				var logins = usuarios['login'];
				var nif = usuarios['nif'];

				var opciones = {lookup: 2, minChars: 1, lookupLimit: 5, showNoSuggestionNotice: false, noSuggestionNotice: 'No hay resultados.', 
					lookupFilter: function (suggestion, query, queryLowerCase) {
						return suggestion.value.toLowerCase().indexOf(queryLowerCase) === 0;
					}
				};
				
				inicializarAutocompletado('#TxtLogin', logins, opciones);
				inicializarAutocompletado('#TxtNif', nif, opciones);
			});

			function modificar() {
			  	// Establecer titulo para la ventana modal
			  	$('#buscarUsuariosModalTitle').text("<fmt:message key="modificar.usuario" />");

			  	$('#id_modal_aviso').hide();
			  	$('#id_modal_pregunta').hide();

			  	// Mostrar la pantalla de edición de Perfiles como ventana modal Bootstrap en lugar de como PopUp
			  	if (document.getElementById('parLogin').value != '') {
			  		// Establecer contenido de la ventana modal (resultado de la peticion Ajax)
			  		$("#buscarUsuariosModalBodyInner").load("<%= request.getContextPath() %>/CrearModificarUsuario.do", {
			  			"accion": "cargarModificar",
			  			"login": document.getElementById('parLogin').value
			  		});
			  		
			  		// Ajustar ancho de la ventana modal a la pagina de modificacion
			  		$(".modal-dialog").css({
			  			'width': '90%'
			  		});

			  		// Ajustar overflow para que aparezca el scroll si la pagina de modificacion no cabe de ancho
			  		$("#buscarUsuariosModalBodyInner").css({
			  			'overflow-x': 'auto'
			  		});
			  	} else {
			  		// Establecer contenido de la ventana modal
			  		$("#buscarUsuariosModalBodyInner").text("<fmt:message key="advertencia.usuario.seleccionar"/>");

			  		$('#id_modal_pregunta').hide();
			  		$('#id_modal_aviso').show();

			  		// Deshacer ajustar ancho de la ventana modal a la pagina de modificacion
			  		$(".modal-dialog").css({
			  			'width': ''
			  		});
			  	}

				// Mostrar ventana modal
				$('#buscarUsuariosModal').modal();
			}

			function crear() {
				// El boton crear lleva a la pantalla de crear usuario en vez de abrirla en un pop up
				this.location="<%= request.getContextPath() %>/CrearModificarUsuario.do?accion=cargar";
			}

			function eliminar() {
				if (document.getElementById('parLogin').value != '') {
            		// Establecer titulo para la ventana modal
            		$('#buscarUsuariosModalTitle').text("<fmt:message key="ventana.confirmacion.titulo"/>");

            		// Establecer contenido de la ventana modal
            		$("#buscarUsuariosModalBodyInner").text("<fmt:message key="confirmacion.eliminar.usuario"/>");

            		// Eliminar los eventos onClic que pueda haber para evitar que se acumulen
            		$('#id_modal_aceptar').off('click');

            		// Asignar funcionalidad al boton aceptar
            		// Se hace submit del formulario de busqueda para mantener la busqueda 
            		var funcion = function aceptarEliminar() {
            			document.forms.formBusquedaUsuario.action = "<%= request.getContextPath() %>/BusquedaUsuario.do?accion=eliminar&parLogin=" + document.getElementById('parLogin').value;
						document.forms.formBusquedaUsuario.submit();
					}
            		
            		$('#id_modal_aceptar').on('click',funcion);

            		$('#id_modal_aviso').hide();
            		$('#id_modal_pregunta').show();

            		// Deshacer ajustar ancho de la ventana modal a la pagina de modificacion
            		$(".modal-dialog").css({
            			'width': ''
            		});
				} else {
					// Establecer titulo para la ventana modal
					$('#buscarUsuariosModalTitle').text("<fmt:message key="eliminar.usuario"/>");

					// Establecer contenido de la ventana modal
					$("#buscarUsuariosModalBodyInner").text("<fmt:message key="advertencia.usuario.seleccionar"/>");

					$('#id_modal_pregunta').hide();
					$('#id_modal_aviso').show();

					// Deshacer ajustar ancho de la ventana modal a la pagina de modificacion
					$(".modal-dialog").css({
						'width': ''
					});
				}

				// Mostrar ventana modal
				$('#buscarUsuariosModal').modal();
			}

			function limpiarBuscar() {
				document.forms.busquedaUsuarioForm.TxtLogin.value = "";
				document.forms.busquedaUsuarioForm.TxtNif.value = "";
				document.forms.busquedaUsuarioForm.TxtNombre.value = "";
				document.forms.busquedaUsuarioForm.TxtApellido1.value = "";
				document.forms.busquedaUsuarioForm.TxtApellido2.value = "";
				document.getElementById("TxtPerfil").value = "";
				document.getElementById("ifUsuarios").src = "";
				resetearAdvertenciaBuscar();
			}

			// Envia el formulario con los campos de búsqueda a la BusquedaUsuarioAction metodo buscar
			function enviarBuscar() {
				if (!formBuscarVacio()) {
					if (formBusquedaUsuario.TxtNif.value != "") {
						if (isNif(formBusquedaUsuario.TxtNif.value)) {
							window.top.loadingStart();
							document.getElementById('formBusquedaUsuario').submit();
							resetearAdvertenciaBuscar();
						} else {
							mostrarErrorBuscar('<fmt:message key="error.nif.formato" />');
						}
					} else {
						window.top.loadingStart();
						document.getElementById('formBusquedaUsuario').submit();
						resetearAdvertenciaBuscar();
					}
				} else {
    					mostrarErrorBuscar('<fmt:message key="error.vacio" />');
				}
			}

			// Comprueba si el formulario de búsqueda está o no vacío
			function formBuscarVacio() {
				if ((formBusquedaUsuario.TxtLogin.value == "") && 
						(formBusquedaUsuario.TxtNif.value == "") &&
						(formBusquedaUsuario.TxtNombre.value == "") &&
						(formBusquedaUsuario.TxtApellido1.value == "") &&
						(formBusquedaUsuario.TxtApellido2.value == "") &&
						(formBusquedaUsuario.TxtPerfil.value == "")) {
					return true;
				} else {
					return false;
				}
			}

			// Muestra un mensaje de informacion
			function mostrarExito(mensaje) {
				// Añadir mensaje
				$('#alertas-buscar-usuarios-content').empty();
				$('#alertas-buscar-usuarios-content').append(mensaje);

				// Aplicar estilo (Clases posibles: alert-info(Azul); alert-danger(Rojo); alert-warning(Amarillo); alert-success(Verde))
				//$('#alertas').addClass('alert-info'); // Añadir la clase alert-info (Azul) asignada por defecto
				//$('#alertas').addClass('alert-danger'); // Añadir la clase alert-danger (Rojo)
				//$('#alertas').addClass('alert-warning'); // Añadir la clase alert-warning (Amarillo)
				$('#alertas-buscar-usuarios').addClass('alert-success'); // Añadir la clase alert-success (Verde)
				$('#alertas-buscar-usuarios').removeClass("alert-info alert-danger alert-warning");

				// Mostrar mensaje
				$('#alertas-buscar-usuarios').show();
			}

			// Muestra un mensaje de error
			function mostrarErrorBuscar(error) {
			  	// Establecer titulo para la ventana modal
			  	$('#buscarUsuariosModalTitle').text("<fmt:message key="ventana.informacion.titulo" />");

			  	$('#id_modal_aviso').hide();
			  	$('#id_modal_pregunta').hide();

			  	// Establecer contenido de la ventana modal
			  	$("#buscarUsuariosModalBodyInner").text(error);

			  	$('#id_modal_aviso').show();

			  	// Deshacer ajustar ancho de la ventana modal a la pagina de modificacion
			  	$(".modal-dialog").css({
			  			'width': ''
                                                });
				// Mostrar ventana modal
				$('#buscarUsuariosModal').modal();
			}

			// Muestra un mensaje que advierte de alguna situación especial
			function advertenciaBuscar() {
				<logic:equal name="busquedaUsuarioForm" property="advertencia" value="advertencia.fecha.formato.buscar">
					mostrarErrorBuscar('<fmt:message key="advertencia.fecha.formato.buscar" />');
				</logic:equal>
				<logic:equal name="busquedaUsuarioForm" property="advertencia" value="advertencia.fecha.formato.insercion">
					mostrarErrorBuscar('<fmt:message key="advertencia.fecha.formato.insercion" />');	
				</logic:equal>
				<logic:equal name="busquedaUsuarioForm" property="advertencia" value="advertencia.usuario.inexistente">
					mostrarErrorBuscar('<fmt:message key="advertencia.usuario.inexistente" />');
				</logic:equal>
			}

			function resetearAdvertenciaBuscar() {}

			function onLoadBuscar() {
				window.top.loadingStop();

				advertenciaBuscar();

				// Si hay mensaje de exito mostrarlo y resetearlo
				<logic:equal name="busquedaUsuarioForm" property="exito" value="confirmacion.modificacion.usuario">
					mostrarExito('<fmt:message key="confirmacion.modificacion.usuario"/>');
					document.getElementById('formBusquedaUsuario').exito.value = '';
				</logic:equal>

				$('#formBusquedaUsuario input').on('keypress', function(e) {
					if(e.keyCode == 13) {
						enviarBuscar();
					}
				});
			}

			function setOrder(order, orderType) {
				document.getElementById('order').value = order;
				document.getElementById('orderType').value = orderType;

				enviarBuscar();
			}
		</script>
	</body>
</html>