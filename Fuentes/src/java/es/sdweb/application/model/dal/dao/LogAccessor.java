package es.sdweb.application.model.dal.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.json.JSONArray;
import es.sdweb.application.componentes.util.StringUtil;
import es.sdweb.application.componentes.util.logging.Log;
import es.sdweb.application.model.dal.FabricaGestorConexiones;
import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;
import es.sdweb.application.model.dal.exceptions.ExceptionSQL;
import es.sdweb.application.model.dal.facade.ILogDAO;
import es.sdweb.application.model.dal.util.SQLUtil;

public class LogAccessor implements ILogDAO {
	@Override
	public void saveLogLine(String line, String system, String ip) throws ExceptionAccesoDatos {
		Log.logRT("BEGIN LogAccessor.saveLogLine()");
		
		FabricaGestorConexiones connectionManager = FabricaGestorConexiones.getInstance();
		
		boolean rollback = false;
		Connection connection = null;
		
		try {
			Log.logRT("Obteniendo una conexión...");
			connection = connectionManager.getConexion();
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			// Iniciamos una transacción
			connection.setAutoCommit(false);
			
			Log.logRT("Llamando al DAO...");
			LogDAO logDao = new LogDAO();
			logDao.addLogLine(connection, line, system, ip);
			
		} catch (ExceptionAccesoDatos e) {
			rollback = true;
			throw e;
		} catch(SQLException e) {
			rollback = true;
			throw new ExceptionSQL(e);
		} finally {
			try {
				SQLUtil.rollbackOrCommitAndCloseConnection(connection, rollback);
			} catch (SQLException e) {
				throw new ExceptionAccesoDatos(LogAccessor.class.getName(), "Error al cerrar la conexion.");
			}
		}
		
		Log.logRT("END LogAccessor.saveLogLine()");
	}

	@Override
	public void saveLogLine(String line, JSONArray json, String system, String ip) throws ExceptionAccesoDatos {
		Log.logRT("BEGIN LogAccessor.saveLogLine()");
		
		String finalLine = line;
		
		for(int i = 0; i < json.length(); i++) {
			@SuppressWarnings("unchecked")
			Map<String, String> jsonElement = (Map<String, String>) json.get(i);
			
			finalLine = StringUtil.cat(finalLine, "|");
			
			for(String key: jsonElement.keySet()) {
				finalLine = StringUtil.cat(finalLine, key, ": ", jsonElement.get(key), "/"); 
			}
		}
		
		this.saveLogLine(finalLine, system, ip);
		
		Log.logRT("END LogAccessor.saveLogLine()");
	}
}
