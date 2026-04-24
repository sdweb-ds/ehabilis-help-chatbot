<%@ page pageEncoding="utf-8" %>
<%@ page contentType="application/json;charset=utf-8" %>

<%@ page import="es.sdweb.application.vista.util.GestorInformacionWeb" %>
<%@ page import="es.sdweb.application.controller.actionforms.memoryCorp.ChatForm" %>

<% ChatForm frm = (ChatForm) GestorInformacionWeb.getAttributeObj(request, "chatForm"); %>

<%= frm.getTextoSalida() %>
