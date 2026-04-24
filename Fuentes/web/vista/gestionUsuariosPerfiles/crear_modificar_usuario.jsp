<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>
<%@ page import="es.sdweb.application.controller.actionforms.gestionUsuariosPerfiles.CrearModificarUsuarioForm" %>

<% if (!GestorInformacionWeb.isLoggedIn(request)) { %>
	<jsp:forward page="<%= GestorInformacionWeb.getForwardURLlogin() %>" />
<% } %>

<% CrearModificarUsuarioForm frm = (CrearModificarUsuarioForm) GestorInformacionWeb.getAttributeObj(request, "crearModificarUsuarioForm"); %>

<!DOCTYPE html>
<html>
	<head>
		<meta content="no-cache" http-equiv="Pragma" />
		<meta content="no-cache" http-equiv="Cache-Control" />
		<meta content="no-store" http-equiv="Cache-Control" />
		<meta content="max-age=0" http-equiv="Cache-Control" />
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
		<meta charset="utf-8" />
		
		<title><fmt:message key="alta.usuario" /></title>
		
		<link rel="stylesheet" href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/main.css" type="text/css" />
	</head>
	<body class="table-body crear-modificar-usuarios" onLoad="onLoad();">
		<div class="container-fluid">
	    	<logic:notEqual parameter="accion" value="cargarModificar">
	    		<div class="row row-30">
	    			<div class="row menugrande">
						<logic:equal property="alta" name="crearModificarUsuarioForm" value="true">
							<fmt:message key="alta.usuario" />
						</logic:equal>
						<logic:notEqual property="alta" name="crearModificarUsuarioForm" value="true">
							<fmt:message key="modificacion.usuario" />
						</logic:notEqual>
					</div>
				</div>
	    	</logic:notEqual>
	
			<div class="row">
				<html:form styleId="crearModificarUsuarioForm" action="/CrearModificarUsuario.do?accion=insertar" method="post">
						<logic:equal property="alta" name="crearModificarUsuarioForm" value="true">
							<div class="filter-form">
						</logic:equal>
						<logic:notEqual property="alta" name="crearModificarUsuarioForm" value="true">
							<div class="filter-form gray-border-top">
						</logic:notEqual>
						<div class="row">
							<div class="subtitle">
								<label class="menugrande"><fmt:message key="datos.personales" /></label>
							</div>
						
							<div class="row">
								<div class="col-md-3">
									<div class="equal-form">
										<label class="menugrande" for="TxtNome"><fmt:message key="txt.nombre" />:</label>
										<html:text property="nombre" name="crearModificarUsuarioForm" size="20" maxlength="30" styleClass="inputObligatorio" styleId="TxtNome"></html:text>
				                    	<!-- Este campo no se muestra, pero se necesita para que el codUsuario se siga manteniendo en la request -->
				                    	<html:hidden property="codUsuario" name="crearModificarUsuarioForm" styleId="TxtCodUsuario"></html:hidden>
				                    	<html:hidden property="alta" name="crearModificarUsuarioForm" styleId="TxtAlta"></html:hidden>
				                    </div>
								</div>
								
								<div class="col-md-3">
									<div class="equal-form">
										<label class="menugrande" for="TxtApelido1"><fmt:message key="txt.apellido1" />:</label>
										<html:text property="apellido1" name="crearModificarUsuarioForm" size="20" maxlength="30" styleClass="inputObligatorio" styleId="TxtApelido1"></html:text>
									</div>
								</div>
								
								<div class="col-md-3">
									<div class="equal-form">
										<label class="menugrande" for="TxtApelido2"><fmt:message key="txt.apellido2" />:</label>
										<html:text property="apellido2" name="crearModificarUsuarioForm" size="20" maxlength="30" styleClass="inputObligatorio" styleId="TxtApelido2"></html:text>
									</div>
								</div>
								
								<div class="col-md-3">
									<div class="equal-form">
										<label class="menugrande" for="TxtNif"><fmt:message key="txt.nif" />:</label>
										<html:text property="nif" name="crearModificarUsuarioForm" size="11" maxlength="10" styleClass="menugrande" styleId="TxtNif"></html:text>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-md-3">
									<div class="equal-form">
										<label class="menugrande" for="TxtTelefono"><fmt:message key="txt.telefono" />:</label>
										<html:text property="telefono" name="crearModificarUsuarioForm" size="17" maxlength="15" styleClass="menugrande" styleId="TxtTelefono"></html:text>
									</div>
								</div>
								
								<div class="col-md-3">
									<div class="equal-form">
										<label class="menugrande" for="TxtCorreo"><fmt:message key="txt.correoElec" />:</label>
										<html:text property="correo" name="crearModificarUsuarioForm" size="32" maxlength="50" styleClass="menugrande" styleId="TxtCorreo"></html:text>
									</div>
								</div>
							</div>
							
							<div class="subtitle">
								<label class="menugrande"><fmt:message key="datos.aplicacion" /></label>
							</div>
							
							<div class="row">
								<div class="col-md-3">
									<div class="equal-form">
										<label class="menugrande" for="login"><fmt:message key="txt.login" />:</label>
										<html:text property="login" name="crearModificarUsuarioForm" size="20" maxlength="30" styleClass="inputObligatorio" styleId="login"></html:text>
									</div>
								</div>
								
								<div class="col-md-3">
									<div class="equal-form">
										<label class="menugrande" for="clave"><fmt:message key="txt.clave" />:</label>
										<html:password property="password" name="crearModificarUsuarioForm" size="20" maxlength="30" styleClass="inputObligatorio" styleId="clave"></html:password>
									</div>
								</div>
								
								<div class="col-md-3">
									<div class="equal-form">
										<label class="menugrande" for="cclave"><fmt:message key="txt.confirmar.clave" /></label>
										<html:password property="confirmarPassword" name="crearModificarUsuarioForm" size="20" maxlength="30" styleClass="inputObligatorio" styleId="cclave"></html:password>
									</div>
								</div>
							
								<div class="col-md-3">
									<div class="equal-form">
										<label class="menugrande" for="TxtIdioma"><fmt:message key="txt.idioma" />:</label>
										<html:select styleClass="menugrande" size="0" name="crearModificarUsuarioForm" styleId="TxtIdioma" property="idioma">
											<html:optionsCollection name="crearModificarUsuarioForm" property="idiomasDisponibles" label="key" value="value" />
										</html:select>
									</div>
								</div>															
							</div>
							
							<!-- Es colaborador -->
							<div class="row">
								<div class="col-md-3">
									<div class="equal-form">
										<label class="menugrande"><fmt:message key="txt.es.colaborador" />:</label>
										<input type="checkbox" id="esColaborador" name="esColaborador" value="1" onchange="cambiarColaborador();"/>
										<html:hidden property="es_colaborador" name="crearModificarUsuarioForm" styleId="es_colaborador" ></html:hidden>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-md-6">
								
									<label class="menugrande"><fmt:message key="txt.perfil" />:</label>
									<div>
										<html:select styleId="noasociado" styleClass="inputObligatorio" size="7" name="crearModificarUsuarioForm"
											property="codPerfilesNoAsociados" multiple="true">
											<html:optionsCollection name="crearModificarUsuarioForm" property="perfilesNoAsociados" value="codPerfil" label="nomPerfil" styleClass="input" />
										</html:select>
										<div class="flechas-select">
											<a href="#">
												<html:img page="/images/flecha.gif" titleKey="boton.asociar" altKey="boton.asociar" border="0" styleId="bot_asociar" onclick="asociar()"></html:img>
											</a>
											<a href="#">						                
												<html:img page="/images/flechaInv.gif" titleKey="boton.desasociar" altKey="boton.desasociar" border="0" styleId="bot_des_asociar" onclick="desAsociar()"></html:img>
											</a>
										</div>
										<html:select styleId="asociado" styleClass="inputObligatorio" size="7" name="crearModificarUsuarioForm" property="codPerfilesAsociados" multiple="true">
											<html:optionsCollection name="crearModificarUsuarioForm" property="perfilesAsociados" value="codPerfil" label="nomPerfil" styleClass="input" />
										</html:select>
									</div>
								</div>
							</div>
						</div>
						
						<div class="row row-50">
							<div class="col-md-6">
								<div class="alert-container">
									<!-- Alertas -->
				                    <!-- Por defecto oculto y vacio, se carga y se muestra con JavaScript -->
				                    <!-- Modificado comportamiento por defecto del boton cerrar para que oculte el div en lugar de eliminarlo --> 
				                    <div id="alertas" role="alert" class="alert alert-dismissible fade in" style="margin-bottom:5px;margin-top:10px;display:none;">
				                        <button aria-label="Close" class="close" type="button" onclick="$('#alertas').hide()">
				                            <span aria-hidden="true">&times;</span>
				                        </button>
										<!-- Contenido -->
										<div id="alertas-content"></div>
									</div>
								</div>
							</div>
		                    	<div class="col-md-6 button-row">
			                    <!-- Si no estamos en la ventana modal de modificar perfil mostrar el botón limpiar -->
			                    <logic:notEqual parameter="accion" value="cargarModificar">
			                       <button type="button" name="bot_limpiar" class="botones_estandar" onClick="limpiar();"><fmt:message key="boton.limpiar"/></button>
			                       <button type="button" name="bot_aceptar" class="botones_estandar" onClick="enviar('crear');"><fmt:message key="boton.aceptar" /></button>
			                    </logic:notEqual> <!-- Si estamos en la ventana modal de modificar perfil mostrar el botón cancelar en vez de limpiar -->
			
			                    <logic:equal parameter="accion" value="cargarModificar">
			                        <button type="button" name="bot_cancelar" class="botones_estandar" onClick="cancelar();"><fmt:message key="boton.cancelar"/></button>
			                        <button type="button" name="bot_aceptar" class="botones_estandar" onClick="enviar('modificar');"><fmt:message key="boton.aceptar" /></button>
			                    </logic:equal>
							</div>
						</div>
					</div>
				</html:form>
			</div>
		</div>

		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/jquery/jquery.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/validator.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/global.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/calendario3.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/rolloverImg.js"></script>
		<script type="text/javascript">
		
			$(document).on('ready', function() {
				if($('#es_colaborador').val() == "1") {
		            $('#esColaborador').prop('checked', true);
		        }
			});
			
			function cambiarColaborador() {
				var esColab = $('#esColaborador');
				valor = esColab.prop("checked")? '1': '0';
				$('#es_colaborador').val(valor);
			}
		
		
			// En función del valor de un parámetro del beanForm abre o no una ventana de confirmación de alta
			function confirmar() {
				<logic:equal name="crearModificarUsuarioForm" property="confirmar" value="true">
					mostrarExitoCrear('<fmt:message key="confirmacion.alta.usuario"/>');
				</logic:equal>
			}

			function limpiar() {
				document.forms.crearModificarUsuarioForm.TxtNome.value = "";
				document.forms.crearModificarUsuarioForm.TxtApelido1.value = "";
				document.forms.crearModificarUsuarioForm.TxtApelido2.value = "";
				document.forms.crearModificarUsuarioForm.TxtNif.value = "";
				document.forms.crearModificarUsuarioForm.TxtTelefono.value = "";
				document.forms.crearModificarUsuarioForm.TxtCorreo.value = "";

				if (opener==null) {
					parent.principal.location = "<%= request.getContextPath() %>/CrearModificarUsuario.do?accion=cargar";
				}

				document.forms.crearModificarUsuarioForm.clave.value = "";
				document.forms.crearModificarUsuarioForm.cclave.value = "";
				resetearAdvertencia();
			}

			// Funcion que se ejecuta al pulsar el boton cancelar en la venta modal de modificacion de Usuario, lanza un clic del boton cerrar de la ventana modal
			function cancelar() {
				$("#buscarUsuariosModalClose").trigger("click");
			}

			//Comprueba si los datos son válidos y llama a la funcion crearModificarUsuario para insertar en la BD
			function enviar(tipoLlamada) {
				var fecha;
				var correcto = [];
				var aux = true;

				if (camposObligatorios()) {
			
					if (crearModificarUsuarioForm.TxtNif.value != "") { // Si se ha introducido NIF hay que validarlo
						if (isNif(crearModificarUsuarioForm.TxtNif.value)) {
							correcto.push(true);				
						} else {
							correcto.push(false);
							mostrarError('<fmt:message key="error.nif.formato"/>');
						}
					}

					if(crearModificarUsuarioForm.TxtCorreo.value != "") { // Si se ha introducido un correo hay que validarlo
						if (validateEmail(crearModificarUsuarioForm.TxtCorreo.value)) {
							correcto.push(true);
						} else {
							correcto.push(false);
							mostrarError('<fmt:message key="error.correo.formato"/>');
						}
					}
					
					if(crearModificarUsuarioForm.TxtTelefono.value != "") { // Si se ha introducido un teléfono hay que validarlo
						if (validateNumTel(crearModificarUsuarioForm.TxtTelefono.value)) {
							correcto.push(true);
						} else {
							correcto.push(false);
							mostrarError('<fmt:message key="error.telefono.formato"/>');
						}
					}
	
					//Comprobamos que no haya habido un error en ningún campo opcional(NIF,Correo)
					for(i =0; i < correcto.length;i++) {
						if(!correcto[i]) {
							aux = false;
						}
					}
	
					if(aux) {
						crearModificarUsuario(tipoLlamada);
					}
				} else {
					mostrarError('<fmt:message key="advertencia.campos.obligatorios"/>');
				}
			}
	
			// Envia el formulario para que se realice la alta de los datos. Para ello invoca el método insertar de CrearModificarUsuarioAction
			function crearModificarUsuario(tipoLlamada) {
				if ((crearModificarUsuarioForm.cclave.value == crearModificarUsuarioForm.clave.value)) {
					// Si se esta modificando el usuario se hace la llamada por Ajax para mantener la pagina de busqueda de usuarios
					if (tipoLlamada == 'modificar') {
						// Peticion Ajax
						var url = '<%= request.getContextPath() %>/CrearModificarUsuario.do?accion=insertar';
		
						// Enviar los datos actuales del formulario usando post
						var posting = $.post(url, $("#crearModificarUsuarioForm").serialize());
					
						// Tareas posteriores a la llamada Ajax
						posting.done(function(data) {
		
		                	// Cerrar ventana modal
		                	$("#buscarUsuariosModalClose").trigger("click");
		
		                	// Si los datos de busqueda siguen estando en el formulario
		                	if (!formBuscarVacio()) {
		                		// Mostrar mensaje de exito despues de recargar el formulario de búsqueda
		                    	document.getElementById('formBusquedaUsuario').exito.value = 'confirmacion.modificacion.usuario';
		
		                    	// Relanzar busqueda para que se muestren los datos del usuario actualizados en vez de los antiguos
		                    	enviarBuscar();
		                	} else {
		                		// Mostrar mensaje modificación correcta
		                		resetearAdvertencia();
		                		mostrarExitoCrear('<fmt:message key="confirmacion.modificacion.usuario"/>');
		                	}
		                });
					} else {
						// Peticion Ajax
						var url = '<%= request.getContextPath() %>/CrearModificarUsuario.do?accion=insertar';
						// Enviar los datos actuales del formulario usando post
						var posting = $.post(url, $("#crearModificarUsuarioForm").serialize());
	
						posting.done(function(data) {
		            		resetearAdvertencia();
		            		mostrarExitoCrear('<fmt:message key="confirmacion.alta.usuario"/>');
		            	});
					}
				} else {
					mostrarError('<fmt:message key="advertencia.claves.non.coinciden"/>');
				}
			}
	
			// Funcion que se ejecuta al pulsar el boton de asociar un perfil al usuario
			function asociar() {
				<%-- document.getElementById('crearModificarUsuarioForm').action='<%=request.getContextPath()%>/CrearModificarUsuario.do?accion=asociar'; --%>
				// document.getElementById('crearModificarUsuarioForm').submit();
				// resetearAdvertencia();
	
				// Si se ha seleccionado al menos un perfil
				if (crearModificarUsuarioForm.noasociado.value != "") {
					// Peticion Ajax
					var url = '<%= request.getContextPath() %>/CrearModificarUsuario.do?accion=asociar';
	
					// Enviar los datos actuales del formulario usando post
					var posting = $.post(url, $("#crearModificarUsuarioForm").serialize());
	    
					// Añadir los resultados a los select de perfiles asociados y no asociados
					posting.done(function(data) {
						var obj = jQuery.parseJSON(data);
	
						var noAsociados = $("#noasociado");
						noAsociados.empty();
						noAsociados.append(obj.no_asociados);
	
						var asociados = $("#asociado");
						asociados.empty();
						asociados.append(obj.asociados);
					});
				}
			}
	
			// Funcion que se ejecuta al pulsar el boton de desasociar un perfil del usuario
			function desAsociar() {
				<%-- document.getElementById('crearModificarUsuarioForm').action='<%=request.getContextPath()%>/CrearModificarUsuario.do?accion=desAsociar'; --%>
				// document.getElementById('crearModificarUsuarioForm').submit();
				// resetearAdvertencia();
	
				// Si se ha seleccionado al menos un perfil
				if (crearModificarUsuarioForm.asociado.value != "") {
	            	// Peticion Ajax
	            	var url = '<%= request.getContextPath() %>/CrearModificarUsuario.do?accion=desAsociar';
	
	            	// Enviar los datos actuales del formulario usando post
	            	var posting = $.post(url, $("#crearModificarUsuarioForm").serialize());
	    
	            	// Añadir los resultados a los select de perfiles asociados y no asociados
	            	posting.done(function(data) {
	            		var obj = jQuery.parseJSON(data);
	
						var noAsociados = $("#noasociado");
						noAsociados.empty();
						noAsociados.append(obj.no_asociados);
	
						var asociados = $("#asociado");
						asociados.empty();
						asociados.append(obj.asociados);
					});
	            }
			}
	
			// 	function actualizar() {
			// 		limpiar();
			// 	}
	  		//  var emailValido = validateEmail(crearModificarUsuarioForm.TxtCorreo.value);
	
			// Comprueba si el formulario de búsqueda está o no vacío
			function camposObligatorios() {
				if ((crearModificarUsuarioForm.login.value != "") &&
						(crearModificarUsuarioForm.clave.value != "") &&
						(crearModificarUsuarioForm.cclave.value != "") &&
						(crearModificarUsuarioForm.TxtNome.value != "") &&
						(crearModificarUsuarioForm.TxtApelido1.value != "") &&
						(crearModificarUsuarioForm.TxtApelido2.value != "") &&
						(crearModificarUsuarioForm.asociado.length > 0)) {
					// <logic:empty name="crearModificarUsuarioForm" property="perfilesAsociados">
						// return (false);
					// </logic:empty>

					return true;
				} else {
					return false;
				}
			}
	
			// Muestra un mensaje de informacion
			function mostrarExitoCrear(mensaje) {
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
	
			// Muestra un mensaje de error
			function mostrarError(error) {
				<%-- document.getElementById('TD_IMG_Error').innerHTML ='<img src="<%=request.getContextPath()%>/images/advertencia.gif" border="0">'; --%>
				// document.getElementById('TD_Error').innerHTML=error;
				
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
	
			function resetearAdvertencia() {
				<%-- document.getElementById('TD_IMG_Error').innerHTML ='<img src="<%=request.getContextPath()%>/images/spacer.gif" width="1" height="1">'; --%>
	 			// document.getElementById('alertas').innerHTML = '';
	 			$('#alertas').removeClass('alert-danger'); 
	 		}
	
			// Muestra un mensaje que advierte de alguna situación especial 
			function advertencia() {
				<logic:equal name="crearModificarUsuarioForm" property="advertencia" value="advertencia.fecha.formato.buscar">
					mostrarError('<fmt:message key="advertencia.fecha.formato.buscar" />');
				</logic:equal>
				<logic:equal name="crearModificarUsuarioForm" property="advertencia" value="advertencia.fecha.formato.insercion">
					mostrarError('<fmt:message key="advertencia.fecha.formato.insercion" />');
				</logic:equal>
				<logic:equal name="crearModificarUsuarioForm" property="advertencia" value="advertencia.unique.login">
					mostrarError('<fmt:message key="advertencia.unique.login" />');
				</logic:equal>
				<logic:equal name="crearModificarUsuarioForm" property="advertencia" value="advertencia.password.longitud.incorrecta">
					mostrarError('<fmt:message key="advertencia.password.longitud.incorrecta" />');
				</logic:equal>
			}
	
		    function onLoad() {
		    	advertencia();
		        confirmar();
		    }
		</script>
	</body>
</html>
