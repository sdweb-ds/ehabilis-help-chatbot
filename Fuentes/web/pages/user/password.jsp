<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %> 

<%@ include file="../includes/header.jsp" %>
		<html:form styleId="formLogin" styleClass="cambiarClaveForm" action="/password.do" method="post">
			<div id="login-container">
				<div id="login-form-container">
					<div class="control-group">
						<label class="titulo" for="id_login"><i><fmt:message key="txt.usuario"/>:</i></label>
						<span class="input-control">
							<html:text styleClass="input" name="cambiarClaveForm" maxlength="15" size="15" styleId="id_login" property="login" />
						</span>
					</div>
					<div class="control-group">
						<label class="titulo" for="id_claveAntigua"><i><fmt:message key="txt.password.anterior"/>:</i></label>
						<span class="input-control">
							<html:password styleClass="input" name="cambiarClaveForm" maxlength="15" size="15" styleId="id_claveAntigua" property="claveAntigua" />
						</span>
					</div>
					<div class="control-group">
						<label class="titulo" for="id_claveNueva"><i><fmt:message key="txt.password.nova"/>:</i></label>
						<span class="input-control">
							<html:password styleClass="input" name="cambiarClaveForm" maxlength="15" size="15" styleId="id_claveNueva" property="claveNueva" />
						</span>
					</div>
					<div class="control-group">
						<label class="titulo" for="id_claveNuevaConf"><i><fmt:message key="txt.password.confirmar"/>:</i></label>
						<span class="input-control">
							<html:password styleClass="input" name="cambiarClaveForm" maxlength="15" size="15" styleId="id_claveNuevaConf" property="claveNuevaConf" />
						</span>
					</div>
					<div class="control-group">
						<a href="<%= request.getContextPath() %>" title="<fmt:message key="boton.cancelar"/>" class="login-button-cancel-2 botones_estandar"><fmt:message key="boton.cancelar"/></a>
						<button type="submit" class="login-button-2 botones_estandar" title="<fmt:message key="txt.trocar.clave"/>"><fmt:message key="txt.trocar.clave"/></button>
					</div>
					<logic:present name="cambiarClaveForm" property="advertencia">
						<bean:define id="advertencia" name="cambiarClaveForm" property="advertencia" type="java.lang.String" />
						<div class="standarRojoLetra" id="TD_Error"><fmt:message key="${advertencia}" /></div>
					</logic:present>
				</div>
			</div>
		</html:form>
<%@ include file="../includes/footer.jsp" %>