<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta charset="utf-8" />
		
		<meta name="robots" content="noindex,nofollow" />
		
		<title><fmt:message key="control.entrada.titulo" /></title>

		<link href="<%= request.getContextPath() %>/static/css/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<link href="<%= request.getContextPath() %>/static/css/fontawesome/css/font-awesome.css" rel="stylesheet" type="text/css" />
		<link href="<%= request.getContextPath() %>/static/js/datetimepicker/jquery.datetimepicker.css" rel="stylesheet" />
		<link href="<%= request.getContextPath() %>/static/css/fonts/roboto.css" rel="stylesheet" type="text/css" />
		<link href="<%= request.getContextPath() %>/static/css/main.css" rel="stylesheet" type="text/css" />
	</head>
	<body class="entry-control">
		<div class="container-fluid">
			<div class="row top-bar" style="display: flex;">
				<div class="title col-md-2 col-lg-2"><fmt:message key="control.entrada.titulo" /></div>
				<div class="filter col-md-2 col-lg-7 filter-entrycontrol">
					<input type="text" placeholder="<fmt:message key="txt.evento" />" id="event-name" name="event-name" class="event-filter" autofocus />
					<input type="text" placeholder="<fmt:message key="txt.funcion" />" id="function-name" name="function-name" class="event-filter" />
					<div class="filter-entrycontrol-div">
						<p style="margin: 0;"><fmt:message key="txt.solo.futuros" />:</p>
						<input type="checkbox" placeholder="<fmt:message key="txt.solo.futuros" />" id="future-function-name" name="future-function-name" class="event-filter" style="margin-left: 5px;" value="1" checked />
					</div>
				</div>
				<div class="buttons col-md-4 col-lg-4">
					<ul class="top-menu ul-entrycontrol">
						<li class="only-when-selected">
							<a href="javascript:void(0);" id="map-selector" class="button">
								<fmt:message key="boton.mapas" />
							</a>
							<ul class="submenu">
								<li><a href="javascript:void(0);" id="show-ocupation-map"><fmt:message key="boton.mapa.ocupacion" /></a></li>
							</ul>
						</li>
						<li class="only-when-selected"><button type="button" id="function-clean" class="button"><fmt:message key="boton.limpiar" /></button></li>
						<li><button type="button" id="function-search" class="button"><fmt:message key="boton.buscar" /></button></li>
					</ul>
				</div>
			</div>
			
			
						<!-- <div class="col-md-6 no-float half-cell frm-abonado-taquilla">
							<div class="subtitle">
								<label class="menugrande"><fmt:message key="txt.gestion.taquilla.filtro.abonado" /></label>
							</div>
							
							<div class="row">
								<div class="col-md-3">
									<label for="subscriber-code" class="menugrande"><fmt:message key="txt.codigo.abonado" />:</label>
									<input type="text" id="subscriber-code" name="subscriber-code" size="15" class="citizen-filter" value="" />
								</div>
								<div class="col-md-3">
									<label for="dni" class="menugrande"><fmt:message key="txt.dni_nie" />:</label>
									<input type="text" id="dni" name="dni" size="15" class="citizen-filter" value="" />
								</div>
								<div class="col-md-6">
									<label for="full-name" class="menugrande"><fmt:message key="txt.nombre.apellidos" />:</label>
									<input type="text" id="full-name" name="full-name" size="40" class="citizen-filter" value="" />
								</div>
							</div>
							
							<div class="row button-row">
								<div class="col-md-12">
									<input type="button" id="BtnLimpiarAbonado" name="BtnLimpiarAbonado" class="botones_estandar" value="<fmt:message key="boton.limpiar" />" />
									<input type="button" id="BtnBuscarAbonado" name="BtnBuscarAbonado" class="botones_estandar" value="<fmt:message key="boton.buscar" />" />
									<input type="button" id="BtnBuscarNumAbonado" name="BtnBuscarNumAbonado" class="botones_estandar" value="<fmt:message key="boton.tiquea.abonado" />" onClick="clickTiquearAbonado();" />
								</div>
							</div>
						</div> -->

			<input type="hidden" id="rfid-code" name="rfid-code" value="" />
			<input type="hidden" id="localizador" name="localizador" value="" />
			
			<div class="row content">
				<div class="col-md-4">
					<div id="rates-container"></div>
					
					<div id="aux-input">
						<div class="subtitle">
							<h4><fmt:message key="control.entrada.estado" /></h4>
						</div>
						<div class="row">
							<div class="col-md-12 col-lg-6">
								<div class="status-icons">
									<div id="code-reader-status">
										<i class="fa fa-times-circle-o"></i>
										<fmt:message key="control.entrada.lectura.entradas" />
									</div>
									<div id="rfid-status" style="display: none;">
										<i class="fa fa-check-circle-o"></i>
										<fmt:message key="control.entrada.lectura.tarjetas" />
									</div>
								</div>
							</div>
							<div class="col-md-12 col-lg-6">
								<!-- Este INPUT se usa como apoyo para asegurarnos de que el foco está donde debe estar -->
								<input type="text" id="reader-aux" value="" />
								<div class="explication-text">
									<fmt:message key="control.entrada.texto.explicacion" />
								</div>
							</div>
						</div>
					</div>
					
					<div id="last-connection-container">
						<div class="subtitle">
							<h4><fmt:message key="control.entrada.ultima.conexion" /></h4>
						</div>
						<div class="last-connection-text">
							<fmt:message key="control.entrada.ultima.conexion.texto" /> <span id="last-connection"></span>
						</div>
					</div>
				</div>
				<div class="col-md-8">
					<div id="history-container"></div>
				</div>
			</div>
		</div>
		
		<textarea id="debug-info" style="display: none;"></textarea>
		<textarea id="debug-ticked-info" style="display: none;"></textarea>

		<!-- Scripts de la página -->
		<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/jquery.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/static/css/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/emodal.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/datetimepicker/jquery.datetimepicker.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/main.js"></script>
		<!-- Variables globales JS -->
		<script type="text/javascript">
			var globals = {
					contextPath: '<%= request.getContextPath() %>',
					soundsPath: '<%= request.getContextPath() %>/static/sound/',
					ocupationMapPath: '<%= request.getContextPath() %>/EntryControl.do?action=ocupationMap&',
					location: {
						boton_cerrar: '<fmt:message key="boton.cerrar" />',
						gestion_taquilla_ventana_seleccion_abonado_titulo: '<fmt:message key="gestion.taquilla.ventana.seleccion.abonado.titulo" />',
						error_ningun_abonado_con_estos_datos: '<fmt:message key="error.ningun.abonado.con.estos.datos" />',
						ventana_informacion_titulo: '<fmt:message key="ventana.informacion.titulo" />',
						action_error_page: '<fmt:message key="action.error.page" />',
						action_error_page_principal: '<fmt:message key="action.error.page.principal" />',
						gestion_taquilla_ventana_seleccion_abonado_titulo: '<fmt:message key="gestion.taquilla.ventana.seleccion.abonado.titulo" />',
						gestion_taquilla_ventana_seleccion_funcion_titulo: '<fmt:message key="gestion.taquilla.ventana.seleccion.funcion.titulo" />',
						txt_num_abonado: '<fmt:message key="txt.num_abonado" />',
						txt_apellido1: '<fmt:message key="txt.apellido1" />',
						txt_apellido2: '<fmt:message key="txt.apellido2" />',
						txt_nombre: '<fmt:message key="txt.nombre" />',
						txt_dni_nie: '<fmt:message key="txt.dni.nie" />',
						txt_telefono1: '<fmt:message key="txt.telefono1" />',
						txt_telefono2: '<fmt:message key="txt.telefono2" />',
						txt_correoElectronico: '<fmt:message key="txt.correoElectronico" />',
						txt_saldo: '<fmt:message key="txt.saldo" />',
						txt_titulo: '<fmt:message key="txt.titulo" />',
						txt_fecha: '<fmt:message key="txt.fecha" />',
						txt_hora: '<fmt:message key="txt.hora" />',
						txt_compania: '<fmt:message key="txt.compania" />',
						txt_duracion: '<fmt:message key="txt.duracion" />',
						txt_libre: '<fmt:message key="txt.libre" />',
						txt_privado: '<fmt:message key="txt.privado" />',
						txt_espacio: '<fmt:message key="mantenimiento.evento.pantalla.crear_editar.funciones.espacio" />',
						txt_no_numerada: '<fmt:message key="mantenimiento.evento.pantalla.crear_editar.funciones.entradas.numeradas" />',
						gestion_taquilla_error_ninguna_funcion: '<fmt:message key="gestion.taquilla.error.ninguna.funcion" />',
						gestion_taquilla_no_evento: '<fmt:message key="gestion.taquilla.no.evento" />',
						tiquear_error_seleccionar_funcion_mapa_ocupacion: '<fmt:message key="tiquear.error.seleccionar.funcion.mapa.ocupacion" />',
						tiquear_error_seleccionar_mapa_ocupacion: '<fmt:message key="tiquear.error.seleccionar.mapa.ocupacion" />',
						ninguna_funcion_encontrada: '<fmt:message key="ninguna.funcion.encontrada" />',
						control_entrada_localidades_tiqueadas: '<fmt:message key="control.entrada.localidades.tiqueadas" />',
						control_entrada_vendidas: '<fmt:message key="control.entrada.vendidas" />',
						control_entrada_tiqueadas: '<fmt:message key="control.entrada.tiqueadas" />',
						txt_totales_titulo_barra_tarifas: '<fmt:message key="txt.totales.titulo.barra.tarifas" />',
						tiqueo_localidad_ya_tiqueada: '<fmt:message key="tiqueo.localidad.ya.tiqueada" />',
						tiqueo_entrada_no_pertenece_funcion: '<fmt:message key="tiqueo.entrada.no.pertenece.funcion" />',
						control_entrada_localidad_correcta: '<fmt:message key="control.entrada.localidad.correcta" />',
						planta: '<fmt:message key="planta" />',
						fila: '<fmt:message key="fila" />',
						butaca: '<fmt:message key="butaca" />',
						tiqueo_abonado_no_localidad: '<fmt:message key="tiqueo.abonado.no.localidad" />',
						control_entrada_abonado_desplazado: '<fmt:message key="control.entrada.abonado.desplazado" />',
						tiqueo_tarjeta_no_pertenece_abonado: '<fmt:message key="tiqueo.tarjeta.no.pertenece.abonado" />',
						control_entrada_historial: '<fmt:message key="control.entrada.historial" />',
						boton_mapa_ocupacion: '<fmt:message key="boton.mapa.ocupacion" />'
					},
					locale: '<%= response.getLocale().getLanguage() %>'
			};
		</script>
		<!-- Scripts específicos de esta página -->
		<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/pages/entrycontrol/main.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/pages/entrycontrol/entrycontrol.js"></script>
	</body>
</html>