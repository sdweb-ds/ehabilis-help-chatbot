/*
 * Created on 14-sep-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package es.sdweb.application.model.dal.exceptions;

import es.sdweb.application.model.dal.exceptions.ExceptionAccesoDatos;

/**
 * @author Antonio Carro Mariño
 *
 */
public class ExceptionEjecucionSQL extends ExceptionAccesoDatos {

	private Exception encapsulatedException;
	private String sql;
	private String nombreClase;
	private String nombreFuncion;
	private int codigoError;

	public ExceptionEjecucionSQL(Exception exception,String nombreClase) {
		super(nombreClase,exception.getMessage());
		encapsulatedException = exception;
	}

	public ExceptionEjecucionSQL(Exception exception,String nombreClase,String sql) {
	super(nombreClase,exception.getMessage());
	encapsulatedException = exception;
	this.sql=sql;
	}

	public ExceptionEjecucionSQL(Exception exception,String sql,String nombreClase,
	                                                    String nombreFuncion, int codigoError) {
	super(nombreClase,exception.getMessage());
	encapsulatedException = exception;
	this.sql=sql;
	this.nombreClase=nombreClase;
	this.nombreFuncion=nombreFuncion;
	this.codigoError=codigoError;
	}

	public Exception getEncapsulatedException() {
	return encapsulatedException;
	}
	
	/**
	 * @return
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @return
	 */
	public int getCodigoError() {
		return codigoError;
	}

	/**
	 * @return
	 */
	public String getNombreClase() {
		return nombreClase;
	}

	/**
	 * @return
	 */
	public String getNombreFuncion() {
		return nombreFuncion;
	}

}
