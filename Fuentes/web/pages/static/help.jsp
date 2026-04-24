<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ include file="../includes/header.jsp" %>
		<div class="container-fluid help-page">
			<div class="row">
				<div class="col-md-12 center-text">
					<h2 class="menugrande"><fmt:message key="txt.ayuda.taquilla.manager" /></h2>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12">
					<p class="center-text">
						<fmt:message key="txt.ayuda.descripcion" />
					</p>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12 center-text">
					<a class="botones_estandar" id="volver" href="<%= request.getContextPath() %>"><fmt:message key="txt.volver" /></a>
				</div>
			</div>
		</div>
<%@ include file="../includes/footer.jsp" %>