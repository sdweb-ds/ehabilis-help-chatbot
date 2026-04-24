<%@ page import="es.sdweb.application.controller.config.IConstantes "%>
<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>

		<%= GestorInformacionWeb.getStyles() %>
		<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/jquery.min.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/app.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/tendina.min.js"></script>
		<%= GestorInformacionWeb.getScripts() %>
		
	</body>
</html>