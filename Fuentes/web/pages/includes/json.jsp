<%@ page pageEncoding="utf-8" %>
<%@ page contentType="text/html;charset=utf-8" %>

<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>
<%@ page import="es.sdweb.application.controller.actionforms.BaseForm" %>

<% BaseForm actionForm = (BaseForm) GestorInformacionWeb.getAttributeObj(request, "actionForm"); %>

<%= actionForm.toJson().toString() %>