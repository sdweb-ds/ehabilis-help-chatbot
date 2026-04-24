package es.sdweb.application.model.dal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.dal.exceptions.ExceptionEjecucionSQL;
import es.sdweb.application.model.dal.util.SQLUtil;
import es.sdweb.application.vista.util.GestorInformacionWeb;

public class LogDAO {
	public void addLogLine(Connection connection, String line, String system, String ip) throws ExceptionEjecucionSQL {
		Log.logRT("BEGIN LogDAO.addLogLine()");

		PreparedStatement preparedStatement = null;

		// Generamos la QUERY
		String queryString = "INSERT INTO log VALUES(NULL, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

		try {
			preparedStatement = connection.prepareStatement(queryString);

			int i = 1;
			SQLUtil.setString(preparedStatement, i++, system);
			SQLUtil.setString(preparedStatement, i++, line);
			if (GestorInformacionWeb.getSessionUsr() != null) {
			    SQLUtil.setString(preparedStatement, i++, GestorInformacionWeb.getSessionUsr().getUsr());
			} else {
			    SQLUtil.setString(preparedStatement, i++, "App tiqueo");
			}

			SQLUtil.setString(preparedStatement, i++, ip);

			Log.logRT("SQL: " + queryString);

			Log.logRT("Ejecutando la SQL...");
			preparedStatement.executeUpdate();
			Log.logRT("END LogDAO.addLogLine()");
		} catch (SQLException e) {
			throw new ExceptionEjecucionSQL(e, queryString, LogDAO.class.getName(), "addLogLine()", e.getErrorCode());
		} finally {
			SQLUtil.cierraPreparedStatement(preparedStatement);
		}
	}
}
