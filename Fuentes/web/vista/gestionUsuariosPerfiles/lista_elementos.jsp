<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="es.sdweb.application.controller.util.SessionContainer" %>

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
	<body class="iframe-body">
		<div id="limpiarCB">
			<%= ((SessionContainer) session.getAttribute("sessCon")).getUtil() %>
		</div>
		
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/global.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/jquery/jquery.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/Scripts/tendina.min.js"></script>
		<script type="text/javascript">
			$(document).on('ready', function() {
				$('.elementos-perfil-main').tendina({
					speed: 200,
					onlyOneOpen: false
				});
				
				$('.elementos-perfil-main input[type=checkbox]').on('change', changeCheckedState);
			});
			
			function changeCheckedState(e) {
				checkedState($(this));
			}
			
			function checkedState($node) {
				if($node.is(':checked')) {
					window.parent.document.getElementById($node.attr("id")).value = 'true';
					
					if (window.parent.document.getElementById('TxtIncluirSubMenus').checked) {
						$node.parent().children('ul').first().children('li').children('input[type="checkbox"]').prop('checked', true).trigger('change');
					}
				} else {
					window.parent.document.getElementById($node.attr("id")).value = 'false';
					
					if (window.parent.document.getElementById('TxtIncluirSubMenus').checked) {
						$node.parent().children('ul').first().children('li').children('input[type="checkbox"]').prop('checked', false).trigger('change');
					}
				}
			}

			// Se usa?
			function habilita(linea) {
				parent.habilitaBotones();
				window.parent.document.getElementById('parIndice').value = document.getElementById( 'input_codPerfil_' + linea ).value;	    
			}
		</script>
	</body>
</html>     	