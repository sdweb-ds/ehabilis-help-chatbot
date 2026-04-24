<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %> 
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="/WEB-INF/sdweb-comun.tld" prefix="comun" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="es.sdweb.application.controller.config.IConstantes" %>
<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>
<%@ page import="es.sdweb.application.controller.actionforms.mantenimiento.ConstanteForm" %>

<%
	ConstanteForm frm = (ConstanteForm) GestorInformacionWeb.getAttributeObj(request, "constanteForm");

	int ANCHO_CAMPO1 = 100;
	int ANCHO_CAMPO2 = 200;
	int ANCHO_CAMPO3 = 250;
	int ANCHO_CAMPO4 = 0;
	int ANCHO_CAMPO5 = 30;
%>
<!DOCTYPE html>
<html>
	<head>
		<meta content="no-cache" http-equiv="Pragma" />
		<meta content="no-cache" http-equiv="Cache-Control" />
		<meta content="no-store" http-equiv="Cache-Control" />
		<meta content="max-age=0" http-equiv="Cache-Control" />
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
		<meta charset="utf-8" />

		<link href="<%= request.getContextPath() %>/Scripts/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<link href='<%= request.getContextPath() %>/styles/fonts/roboto.css' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/main.css" type="text/css" />
	</head>
	<body class="constantes_lista table-body">
		<div class="container-fluid">
			<div class="row row-30">
				<div class="row menugrande">
					<div class="col-md-6">
						<fmt:message key="menu.mantenimiento.constantes.pantalla.titulo"/>
					</div>
					<div class="col-md-6 add-cell">
						<a href="javascript:addRow();"><img src="<%= request.getContextPath() %>/images/plus_add_blue.png" class="image-12-12" /> <fmt:message key="boton.novo.rexistro" /></a>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12">
					<form id="actualizar" name="actualizar" action="<%= request.getContextPath() %>/Constante.do" method="post">
						<input type="hidden" id="accion" name="accion" value="actualizar" />
						
						<table id="tablaMaestro" class="table-list">
							<thead>
								<tr>
									<th class="menu" width="<%= ANCHO_CAMPO1 %>"><fmt:message key="menu.mantenimiento.constantes.pantalla.campo1"/></th>
									<th class="menu table-required" width="<%= ANCHO_CAMPO2 %>"><fmt:message key="menu.mantenimiento.constantes.pantalla.campo2"/></th>
									<th class="menu table-required" width="<%= ANCHO_CAMPO3 %>"><fmt:message key="menu.mantenimiento.constantes.pantalla.campo3"/></th>
									<th class="menu" width="<%= ANCHO_CAMPO4 %>"><fmt:message key="menu.mantenimiento.constantes.pantalla.campo4"/></th>
									<th class="menu"></th>
								</tr>
							</thead>
							<tbody>
								<logic:notEmpty name="constanteForm" property="constantes"> <!-- Si hay constantes que visualizar -->
									<!-- Filas con los Datos  -->
									<logic:iterate id="constante" name="constanteForm" property="constantes" indexId="i">			
										<comun:isImpar name="i">
											<tr id="TR<bean:write name="i"/>" class="colorLineaImpar" onmouseover="destacar(<bean:write name="i"/>)" onmouseout="mitigar(<bean:write name="i"/>)">
										</comun:isImpar>
										<comun:isPar name="i">
											<tr id="TR<bean:write name="i"/>" class="colorLineaPar" onmouseover="destacar(<bean:write name="i"/>)" onmouseout="mitigar(<bean:write name="i"/>)">
										</comun:isPar>
											<td id="td_campo1_<bean:write name="i"/>" class="standar" style="width: <%= ANCHO_CAMPO1 %>px;" onclick="editar(1,<bean:write name="i"/>);">
												<span id="span_campo1_<bean:write name="i"/>"><bean:write name="constante" property="idConstante" /></span>
												<input id="OcultoCampo1_<bean:write name="i"/>" name="idConstante" type="text" class="input" size="<%= GestorInformacionWeb.getInputSize(ANCHO_CAMPO1) %>" style="display:none"  value="<bean:write name="constante" property="idConstante" />">
											</td>
											<td id="td_campo2_<bean:write name="i"/>" class="standar" align="left" <%=GestorInformacionWeb.getWidth(ANCHO_CAMPO2)%> onclick="editar(2,<bean:write name="i"/>);">
												<span id="span_campo2_<bean:write name="i"/>"><bean:write name="constante" property="nombre" /></span>
												<input id="OcultoCampo2_<bean:write name="i"/>" name="nombre" type="text" class="input inputObligatorio" size="<%=GestorInformacionWeb.getInputSize(ANCHO_CAMPO2)%>" style="display:none" value="<bean:write name="constante" property="nombre" />">
											</td>
											<td id="td_campo3_<bean:write name="i"/>" class="standar" align="left" <%=GestorInformacionWeb.getWidth(ANCHO_CAMPO3)%> onclick="editar(3,<bean:write name="i"/>);">
												<span id="span_campo3_<bean:write name="i"/>"><bean:write name="constante" property="valor" /></span>
												<input id="OcultoCampo3_<bean:write name="i"/>" name="valor" type="text" class="input inputObligatorio" size="<%=GestorInformacionWeb.getInputSize(ANCHO_CAMPO3)%>" style="display:none"  value="<bean:write name="constante" property="valor" />">
											</td>
											<td id="td_campo4_<bean:write name="i"/>" class="standar" align="left" <%=GestorInformacionWeb.getWidth(ANCHO_CAMPO4)%> onclick="editar(4,<bean:write name="i"/>);">
												<span id="span_campo4_<bean:write name="i"/>"><bean:write name="constante" property="descripcion" /></span>
												<input id="OcultoCampo4_<bean:write name="i"/>" name="descripcion" type="text" class="input" size="65" style="display:none"  value="<bean:write name="constante" property="descripcion" />">
											</td>
											<td width="30" class="standar" align="center" <%=GestorInformacionWeb.getWidth(ANCHO_CAMPO5)%>>
												<input type="checkbox" id="sel_<bean:write name="i"/>" name="sel" style="cursor:pointer"  onclick="seleccionar(<bean:write name="i"/>)"/>
												<!-- Campos ocultos guardaran los datos de las filas existentes, que podran ser modificadas o no -->
												<input id="OcultoModificado_<bean:write name="i"/>" name="modificado" type="hidden" class="input" value="<%=IConstantes.CFG_JAVASCRIPT_IGNORE_ITEM_KEY%>">
											</td>
										</tr>
									</logic:iterate>
								</logic:notEmpty>
							</tbody>
						</table>
					</form>
				</div>
			</div>
			
			<div class="row row-50">
				<div class="col-md-12 button-row botonera-inferior">
					<input type="hidden" id="mensajeEliminar" name="mensajeEliminar" value="<fmt:message key="confirmacion.eliminar.registros"/>">
					
					<input type="button" name="accion" id="BtnEngadir" class="botones_estandar" value="<fmt:message key="boton.insertar_modificar"/>" onclick="aceptar();" />
					<input type="button" id="BtnBorrar" name="BtnBorrar" class="botones_estandar" value="<fmt:message key="boton.eliminar"/>" onClick="eliminar();" />
					<input type="button" id="BtnCancelar" name="BtnCancelar" class="botones_estandar" value="<fmt:message key="boton.cancelar"/>" onClick="cancelar();" />
				</div>
			</div>
		</div>

		<!-- Ventana modal para mostrar alertas -->
		<div class="modal fade" id="modal_alerta" tabindex="-1" role="dialog" aria-labelledby="modal_alerta_label">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						
						<h4 class="modal-title" id="modal_alerta_title"></h4>
					</div>
					
					<div class="modal-body" id="modal_alerta_body"></div>
					
					<div id="id_modal_aviso" class="modal-footer" style="display:none">
						<button id="id_modal_cerrar" name="id_modal_cerrar" type="button" class="btn btn-primary" data-dismiss="modal"><fmt:message key="boton.cerrar"/></button>
					</div>
					
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
			$(function() {
				window.top.loadingStop();
				
				$('#actualizar').on('submit', function(e) {
					window.top.loadingStart();
				});
			});
		
			var maxLineas = <%= frm.getConstantes().size() %>; //Numero de filas maestras que se mostraran en la tabla

			// Esta funcion añade una nueva fila de datos maestros a la tabla
			function addRow() {
				var table = document.getElementById("tablaMaestro");
				var maxfilas = table.rows.length; // Ojo: aqui estamos contando el encabezado de la tabla, para efectos graficos deberemos considerar una fila menos
				var rowCount = maxfilas -1;
				var row = table.insertRow(maxfilas);
				
				row.setAttribute("id","TR" + rowCount);
				
				if ((rowCount % 2) == 0) { //Coloreamos la nueva linea como corresponda
					row.className = "colorLineaPar";
				} else {
					row.className = "colorLineaImpar";
				}
				
				row.onmouseover = new Function("destacar(" + rowCount + ");");
				row.onmouseout = new Function("mitigar(" + rowCount + ");");
				row.style.cursor = "pointer";

				var cell1 = row.insertCell(0);
				cell1.setAttribute("id", "td_campo1_" + rowCount);
				cell1.onclick = new Function('editar(1,' + rowCount + ');');
				
				if ("" != "<%= ANCHO_CAMPO1 %>") {
					cell1.setAttribute("width", "<%= ANCHO_CAMPO1 %>");
				}
				
				cell1.setAttribute("align", "left");

				var cell2 = row.insertCell(1);
				cell2.setAttribute("id", "td_campo2_" + rowCount);
				cell2.onclick = new Function('editar(2, ' + rowCount + ');');
				
				if ("" != "<%= ANCHO_CAMPO2 %>") {
					cell2.setAttribute("width", "<%= ANCHO_CAMPO2 %>");
				}
				cell2.setAttribute("align", "left");

				var cell3 = row.insertCell(2);
				cell3.setAttribute("id", "td_campo3_" + rowCount);
				cell3.onclick = new Function('editar(3, ' + rowCount+');');
				
				if ("" != "<%= ANCHO_CAMPO3 %>") {
					cell3.setAttribute("width", "<%= ANCHO_CAMPO3 %>");
				}
				cell3.setAttribute("align", "left");

				var cell4 = row.insertCell(3);
				cell4.setAttribute("id", "td_campo4_" + rowCount);
				cell4.onclick = new Function('editar(4, ' + rowCount + ');');
				
				if ("" != "<%= ANCHO_CAMPO4 %>") {
					cell4.setAttribute("width", "<%= ANCHO_CAMPO4 %>");
				}
				cell4.setAttribute("align", "left");

				var cell5 = row.insertCell(4); // Radio button selector
				cell5.setAttribute("id", "td_radio_" + rowCount);
				
				if ("" != "<%= ANCHO_CAMPO5 %>") {
					cell5.setAttribute("width", "<%= ANCHO_CAMPO5 %>");
				}
				cell5.setAttribute("align", "center");

				// Rellenamos los TD con el HTML correspondiente
				cell1.innerHTML = "<span id='span_campo1_" + rowCount + "' class='standar'></span>" +
					"<input id='OcultoCampo1_" + rowCount + "' name='idConstante' size='<%= GestorInformacionWeb.getInputSize(ANCHO_CAMPO1) %>' type='text' class='input' style='display:none;' value=''>";
				
				cell2.innerHTML = "<span id='span_campo2_" + rowCount + "' class='standar'></span>" +
					"<input id='OcultoCampo2_" + rowCount + "' name='nombre' size='<%= GestorInformacionWeb.getInputSize(ANCHO_CAMPO2) %>' type='text' class='input inputObligatorio' style='display:none;' value=''>";
				
				cell3.innerHTML = "<span id='span_campo3_" + rowCount + "' class='standar'></span>" +
					"<input id='OcultoCampo3_" + rowCount + "' name='valor' size='<%= GestorInformacionWeb.getInputSize(ANCHO_CAMPO3) %>' type='text' class='input inputObligatorio' style='display:none' value=''>";
				
				cell4.innerHTML = "<span id='span_campo4_" + rowCount + "' class='standar'></span>" +
					"<input id='OcultoCampo4_" + rowCount + "' name='descripcion' size ='65' type='text' class='input inputObligatorio' style='display:none;' value=''>";
				
				cell5.innerHTML="<input type='checkbox' id='sel_" + rowCount + "' name='sel' class='radio-button' onclick='seleccionar(" + rowCount + ")'/>" +
					"<input id='OcultoModificado_" + rowCount + "' name='modificado' type='text' class='input' style='display:none;' value='<%= IConstantes.CFG_JAVASCRIPT_NEW_ITEM_KEY %>'>";

				maxLineas++; // Contabilizamos una linea mas
				
				editar(2, rowCount);
			}

			// Funcion que se ejecuta al hacer click en una fila. En ese momento se visibilizaran los inputs de esa fila para que pueda editarse el texto.
			function seleccionar(linea) {
				var checked = document.getElementById("sel_" + linea).checked;
				
				if (checked) { // Si la linea no estaba seleccionada, la seleccionamos. Ojo que puede haber varias lineas seleccionadas.
					destacar(linea); // La destacamos graficamente
					
					// El campo 1 es el ID y no se puede modificar
					//document.getElementById("span_campo1_"+linea).style.display="none"; 
					//document.getElementById("OcultoCampo1_"+linea).style.display="";
					
					document.getElementById("span_campo2_" + linea).style.display = "none"; 
					document.getElementById("OcultoCampo2_" + linea).style.display = ""; 
					
					document.getElementById("span_campo3_" + linea).style.display = "none"; 
					document.getElementById("OcultoCampo3_" + linea).style.display = "";

					document.getElementById("span_campo4_" + linea).style.display = "none"; 
					document.getElementById("OcultoCampo4_" + linea).style.display = ""; 

					// Miramos como debemos marcar el flag
					var flag = document.getElementById("OcultoModificado_" + linea).value;
					
					// Si el item no era nuevo, lo marcamos como modificado. Si era nuevo sigue siendo nuevo.
					if (flag != "<%= IConstantes.CFG_JAVASCRIPT_NEW_ITEM_KEY %>") { 
						document.getElementById("OcultoModificado_" + linea).value = "<%= IConstantes.CFG_JAVASCRIPT_UPDATE_ITEM_KEY %>";
					}
				} else { // Si la linea estaba seleccionada, la deseleccionamos
					mitigar(linea); // La mitigamos graficamente
					
					document.getElementById("span_campo1_" + linea).innerHTML = document.getElementById("OcultoCampo1_" + linea).value;
					document.getElementById("span_campo1_" + linea).style.display = ""; 
					document.getElementById("OcultoCampo1_" + linea).style.display = "none"; 

					document.getElementById("span_campo2_" + linea).innerHTML = document.getElementById("OcultoCampo2_" + linea).value;
					document.getElementById("span_campo2_" + linea).style.display = ""; 
					document.getElementById("OcultoCampo2_" + linea).style.display = "none"; 

					document.getElementById("span_campo3_" + linea).innerHTML = document.getElementById("OcultoCampo3_" + linea).value;
					document.getElementById("span_campo3_" + linea).style.display = ""; 
					document.getElementById("OcultoCampo3_" + linea).style.display = "none"; 

					document.getElementById("span_campo4_" + linea).innerHTML = document.getElementById("OcultoCampo4_" + linea).value;
					document.getElementById("span_campo4_" + linea).style.display = "";
					document.getElementById("OcultoCampo4_" + linea).style.display = "none"; 
				}
			}
        
			function todosVacios(campo1, campo2, campo3, campo4) {
				result = false;

				if ((campo1 == "") && (campo2 == "") && (campo3 == "") && (campo4 == "")) {
					result = true;
				}
				
				return result;
			}

			function aceptar() {
				// Recorremos todos los items para ver si todos los campos estan cubiertos
				var table = document.getElementById("tablaMaestro");
				var maxfilas = table.rows.length; // Ojo: aqui estamos contando el encabezado de la tabla, para efectos graficos deberemos considerar una fila menos
				var faltanDatos = false;
				
				for (x = 0; x < maxfilas - 1; x++) { // Restamos 1 porque la cabecera cuenta como fila
					// Comprobamos solo si estan cubiertos los campos de nombre y valor de cada constante.
					try { // Usamos try-catch dada la posibilidad de que alguna fila de la tabla no exista, es decir que la tabla no tenga una secuencia completa
						// Solo permitiremos campos vacios obligatorios en aquellas filas que sean nuevas, y que seran ignoradas
						var nueva = (document.getElementById('OcultoModificado_' + x).value == "<%= IConstantes.CFG_JAVASCRIPT_NEW_ITEM_KEY %>");
						var campo1 = document.getElementById('OcultoCampo1_' + x).value;
						var campo2 = document.getElementById('OcultoCampo2_' + x).value;
						var campo3 = document.getElementById('OcultoCampo3_' + x).value;
						var campo4 = document.getElementById('OcultoCampo4_' + x).value;
						
						if(todosVacios(campo1, campo2, campo3, campo4)) { // Las filas totalmente vacias son ignoradas
							document.getElementById('OcultoModificado_' + x).value = "<%= IConstantes.CFG_JAVASCRIPT_IGNORE_ITEM_KEY %>";                      
						} else {
							if ((campo2 == "" || campo3 == "")) { //Si falta alguno de los campos obligatorios
								faltanDatos = true;
							}
						}
					}catch (e){}
				} //for
				
				if (faltanDatos) {
					modal('<fmt:message key="ventana.informacion.titulo"/>', '<fmt:message key="advertencia.campos.obligatorios.vacios"/>', false, false, false);        
				} else {
					$("#actualizar").trigger('submit');
				}
			} //function

	        /**
	         * Muestra una venta modal mostrando un titulo, una informacion textual (puede ser html) y un boton de cerrar.
	         * titulo: titulo de la ventana.
	         * texto: texto que forma el cuerpo de la ventana. Puede ser codigo HTML.
	         * aceptar_cancelar: true para visualizar botones de "Aceptar" y "Cancelar", false para mostrar solo un boton "Cerrar".
	         * fclick_aceptar_cerrar: funcion javascript que se invocara al pulsar en aceptar o cerrar segun sea el caso. Es un parametro obligatorio si aceptar_cancelar es true.
	         * fclick_cancelar: funcion javascript que se ejecutara al pulsar cancelar, si aceptar_cancelar es true.
	         */
	         function modal(titulo, texto, aceptar_cancelar, fclick_aceptar_cerrar, fclick_cancelar) {
	        	 // Ponemos el titulo
	        	 $('#modal_alerta_title').text(titulo);
	        	 // Establecemos contenido de la ventana modal
	        	 $("#modal_alerta_body").text(texto);

				if (aceptar_cancelar) { // Si es una ventana de pregunta
					document.getElementById('id_modal_pregunta').style.display = "";
					document.getElementById('id_modal_aviso').style.display = "none";
					
					$('#id_modal_aceptar').on('click', fclick_aceptar_cerrar);
				} else { //Si es una ventana de aviso
					document.getElementById('id_modal_pregunta').style.display = "none";
					document.getElementById('id_modal_aviso').style.display = "";
				}

				// Mostrar ventana modal
				$('#modal_alerta').modal();
			}

			function eliminar() {
				var mensaje = document.getElementById("mensajeEliminar").value;
			
				var table = document.getElementById("tablaMaestro");
				var maxfilas = table.rows.length; // Ojo: aqui estamos contando el encabezado de la tabla, para efectos graficos deberemos considerar una fila menos

				var hasAny = false;
				// Vemos si hay alguna fila marcada
				for (x = 0; x < maxfilas - 1; x++) { // Restamos 1 porque la cabecera cuenta como fila
					try { // Usamos try-catch dada la posibilidad de que alguna fila de la tabla no exista, es decir que la tabla no tenga una secuencia completa
						if (document.getElementById("sel_" + x).checked) { // Si la fila esta seleccionada
							hasAny = true;
							break;
						}
					} catch (e){}
				} //for
				
				if(hasAny) {
					var fe = function submitEliminar() {
						document.getElementById("accion").value = "eliminar";
						
						var table = document.getElementById("tablaMaestro");
						var maxfilas = table.rows.length; // Ojo: aqui estamos contando el encabezado de la tabla, para efectos graficos deberemos considerar una fila menos
						
						// Marcamos las filas checkeadas con el estado "eliminar"
						for (x = 0; x < maxfilas - 1; x++) { // Restamos 1 porque la cabecera cuenta como fila
							try { // Usamos try-catch dada la posibilidad de que alguna fila de la tabla no exista, es decir que la tabla no tenga una secuencia completa
								var nueva = (document.getElementById('OcultoModificado_' + x).value == "<%= IConstantes.CFG_JAVASCRIPT_NEW_ITEM_KEY %>");
								var checked = document.getElementById("sel_" + x).checked;
								
								if (checked) { // Si la fila esta seleccionada
									if (nueva) { // Si es nueva, la ignoramos
										document.getElementById('OcultoModificado_' + x).value = "<%= IConstantes.CFG_JAVASCRIPT_IGNORE_ITEM_KEY %>";
									} else { // En otro caso la marcamos para eliminar
										document.getElementById('OcultoModificado_' + x).value = "<%= IConstantes.CFG_JAVASCRIPT_DELETE_ITEM_KEY %>";
									}
								}
							}catch (e){}
						} //for
						
						$('#actualizar').trigger('submit'); 
					}

					modal('<fmt:message key="ventana.confirmacion.titulo"/>', '<fmt:message key="confirmacion.eliminar.registros"/>', true, fe, false);        
				}
			}

			function cancelar() {
				this.location = "<%= request.getContextPath() %>/Constante.do?accion=cargar";
			}

			//Muestra un mensaje que advierte de alguna situación especial 
			function advertencia() { 
				<logic:equal name="constanteForm" property="advertencia" value="advertencia.clave.referenciada">
					parent.mostrarAdvertencia( '<fmt:message key="advertencia.clave.referenciada" />');
				</logic:equal>
			}			

			// Destacar una linea cuando se pasa el raton por encima o cuando se selecciona.
			function destacar(linea) {
				document.getElementById('TR' + linea).className = 'colorLineaResaltada';			
			}

			function mitigar(linea) {
				var checked = document.getElementById("sel_" + linea).checked;
				if (!checked) { // Si la linea no esta seleccionada, mitigamos sus colores. Ojo que puede haber varias lineas seleccionadas.
					if (linea % 2 == 0) {
						document.getElementById('TR' + linea).className = 'colorLineaPar';
					} else {
						document.getElementById('TR' + linea).className = 'colorLineaImpar';
					}
				}
			} //function

	        /**
	         * Permite editar la fila en la que se ha hecho clic, y le pasa el foco al campo correspondiente.
	         * Selecciona el check tambien.
	         */
	         function editar(campo, linea) {
				document.getElementById("sel_" + linea).checked = true; //Seleccionamos el check
				seleccionar(linea); //Seleccionamos la linea
				document.getElementById("OcultoCampo" + campo + "_" + linea).focus(); //Le pasamos el foco al elemento donde se hizo click
			}
		</script>
	</body>
</html>