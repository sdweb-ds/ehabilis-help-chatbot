package es.sdweb.application.model.dal.facade;

import org.json.JSONArray;

import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;

public interface ILogDAO {
	/**
	 * Guarda una línea de log en la base de datos
	 * 
	 * @param line La línea que se debe guardar
	 * @param system El sistema donde ocurre la acción
	 * @param ip La IP del ordenador cliente
	 * 
	 * @throws ExceptionAccesoDatos
	 */
	public void saveLogLine(String line, String system, String ip) throws ExceptionAccesoDatos;
	
	/**
	 * Guarda una línea de log en la base de datos después de procesar un array JSON
	 * 
	 * @param line La línea que se debe guardar
	 * @param json El array JSON que se incluirá junto con la línea
	 * @param system El sistema donde ocurre la acción
	 * @param ip La IP del ordenador cliente
	 * 
	 * @throws ExceptionAccesoDatos
	 */
	public void saveLogLine(String line, JSONArray json, String system, String ip) throws ExceptionAccesoDatos;
}
