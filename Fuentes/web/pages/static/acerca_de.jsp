<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%-- @ include file="../includes/header.jsp" --%>
		<div class="container-fluid help-page">
			<div class="row">
				<div class="col-md-12 center-text">
					<h2 class="menugrande">Acerca de eHabilis Help ChatBot</h2>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12">
					<p class="center-text">
						SDWEB - SOLUCIÓNS DIXITAIS, S.L.N.E. está llevando a cabo el proyecto de I+D+i denominado "eHabilis Help Chatbot" (Expediente: IG408M-2025-000-000106).
					</p>
                                        <p class="center-text">
						Objetivo del proyecto: El objetivo principal es el diseño, entrenamiento e implementación de un núcleo tecnológico avanzado basado en Inteligencia Artificial (NLU/ML) para el ecosistema eHabilis KMS. Este sistema de asistencia conversacional intelirgente permitirá un razonamiento semántico complejo y la resolución autónoma de consultas, elevando el nivel de madurez tecnológica de nuestras soluciones a un entorno operativo real.
					</p>
                                        <p class="center-text">
						Financiación: Este proyecto ha sido financiado por la Unión Europea – NextGenerationEU, a través del Plan de Recuperación, Transformación y Resiliencia (PRTR) y apoyado por el Instituto Galego de Promoción Económica (IGAPE) dentro de la convocatoria de Ayudas IA360 para el desarrollo tecnológico y la innovación mediante el uso de la Inteligencia Artificial.
					</p>
                                        <p class="center-text">
						<img src="https://www.sdweb.es/sites/default/files/imagen.jpg" alt="Logos IGAPE Ministerio NextGeneration Recuperacion">
					</p>
					<br/>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12 center-text">
                                        <a class="botones_estandar" id="volver" href="<%= request.getContextPath() %>" onclick="volver();"><fmt:message key="txt.volver" /></a>
				</div>
			</div>
		</div>
<%@ include file="../includes/footer.jsp" %>


<script>
        function volver() {
                var volver = document.getElementById('volver');
		window.parent.location = volver.href;
        }
</script>
