package es.sdweb.application.componentes.util.logging;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 
 * Esta clase es un FileAppender que permite crear archivos de log que roten o no por cualquier formato de 
 * fecha disponible para la clase <B>java.text.SimpleDateFormat</B> y/o por numero de indice al superar un tamaño 
 * maximo de archivo.
 * 
 * @author Antonio Carro Mariño
 * 
 */
public class SdwebRollingFileAppender extends FileAppender {

	private String originalFileNameNoExt = null;
	private String originalFileExtension = null;


	/**
	 * 
	 * <P>Recibe del archivo properties de configuraci�n de los logs el nombre del archivo de log, incluyendo 
	 * el path, sin indicaci�n de fecha ni n�mero de volumen.</P>
	 *  
	 * @param filename nombre del archivo de log, incluyendo el path, sin indicaci�n de fecha ni n�mero 
	 * de volumen.
	 * 
	 */
	public void setFile(String filename) {
		originalFileNameNoExt = new String(filename);
		if (originalFileNameNoExt.indexOf(".") > 0) {
			originalFileNameNoExt = originalFileNameNoExt.substring(0, originalFileNameNoExt.lastIndexOf("."));
		}
		originalFileExtension = "";
		if (filename != null && filename.indexOf(".") > 0) {
			originalFileExtension = filename.substring(filename.lastIndexOf(".")+1);
		}
		super.setFile(getFileName(new Date(), 1));
	}


	private int findNextFileNum(Date currentDate) {
		if (maxBackupIndex > 0) {
			return findNextFileNum(currentDate, 1, maxBackupIndex-1);
		}
		else {
			int ind = 1;
			while (new File(getFileName(currentDate, ind)).exists()) {
				ind++;
			}
			return ind;
		}
	}
	private int findNextFileNum(Date currentDate, int fst, int lst) {
		if (fst >= (lst-1)) { 
			if ((new File(getFileName(currentDate, fst))).exists()) {
				if ((new File(getFileName(currentDate, lst))).exists()) {
					return (lst + 1);
				}
				else return lst;
			}
			else return fst;
		}
		else {
			int mid = (fst+lst)/2;
			if ((new File(getFileName(currentDate, mid))).exists()) {
				return findNextFileNum(currentDate, mid+1, lst);
			}
			else {
				return findNextFileNum(currentDate, fst, mid);
			}
		}
	}


	private Date fileNameDate = null;
	private int fileNameNumber = 1;

	private long maximumFileSize = 0;
	private int maxBackupIndex = 0;
	private String datePattern = null;

	/**
	 * 
	 * <P>Recibe del archivo properties de configuraci�n de los logs el tama�o m�ximo del archivo de log, en 
	 * bytes, a partir del cual se debe crear un nuevo archivo de log con un nuevo n�mero de �ndice.</P>
	 * 
	 * <P>Tambi�n se puede definir utilizando los sufijos <I>KB</I>, <I>MB</I> o <I>GB</I>, que ser�n 
	 * interpretados como <I>KiloBytes</I>, <I>MegaBytes</I> o <I>GigaBytes</I>, respectivamente.</P>
	 * 
	 * <P>Si no se especifica este par�metro, el fichero de log no rotar� por tama�o m�ximo de archivo.</P>
	 * 
	 * @param value tama�o m�ximo de un archivo de log.
	 * 
	 */
	public void setMaxFileSize(String value) {
		maximumFileSize = OptionConverter.toFileSize(value, maximumFileSize + 1);
	}

	
	/**
	 * 
	 * <P>Recibe del archivo properties de configuraci�n de los logs el �ndice m�ximo del archivo log dentro
	 * de una fecha</P>
	 * 
	 * <P>Este par�metro es irrelevante si el par�metro <I>maxFileSize</I> no fue definido.</P>
	 * 
	 * <P>En caso de que este par�metro no se especifique, el n�mero de �ndice se incrementar� 
	 * indefinidamente. Si este par�metro se espefica, el n�mero indicado ser� el m�ximo �ndice que pueda 
	 * tener un fichero de log, y todos los �ndices de los ficheros de log estar�n formateados, completados 
	 * con ceros a la izquierda, con la misma cantidad de cifras que el �ndice m�ximo.</P>
	 * 
	 * <TABLE BORDER="1" CELLSPACING="0" CELLPADDING="2">
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TH>file</TH>
	 * <TH>maxBackupIndex</TH>
	 * <TH>�ndice</TH>
	 * <TH>nombre de fichero log</TH>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD ROWSPAN="4">FILENAME1.log</TD>
	 * <TD ROWSPAN="4">99</TD>
	 * <TD>1</TD>
	 * <TD>FILENAME1_01.log</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>12</TD>
	 * <TD>FILENAME1_12.log</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>123</TD>
	 * <TD> - </TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>1234</TD>
	 * <TD> - </TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD ROWSPAN="4">FILENAME2.log</TD>
	 * <TD ROWSPAN="4">999</TD>
	 * <TD>1</TD>
	 * <TD>FILENAME2_001.log</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>12</TD>
	 * <TD>FILENAME2_012.log</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>123</TD>
	 * <TD>FILENAME2_123.log</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>1234</TD>
	 * <TD> - </TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD ROWSPAN="4">FILENAME3.log</TD>
	 * <TD ROWSPAN="4"> - </TD>
	 * <TD>1</TD>
	 * <TD>FILENAME3_1.log</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>12</TD>
	 * <TD>FILENAME3_12.log</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>123</TD>
	 * <TD>FILENAME3_123.log</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>1234</TD>
	 * <TD>FILENAME3_1234.log</TD>
	 * </TR>
	 * </TABLE>
	 * 
	 * @param maxBackupIndex �ndice m�ximo del archivo log dentro de una fecha.
	 */
	public void setMaxBackupIndex(int maxBackupIndex) {this.maxBackupIndex = maxBackupIndex;}
	/**
	 * 
	 * <P>Devuelve el �ndice m�ximo del archivo log dentro de una fecha</P>
	 * 
	 * @return �ndice m�ximo del archivo log dentro de una fecha.
	 */
	public int getMaxBackupIndex() {return maxBackupIndex;}

	/**
	 * 
	 * <P>Recibe del archivo properties de configuraci�n de los logs el patr�n seg�n el cual se debe 
	 * formatear la fecha actual para a�adirla al nombre de archivo.</P>
	 * 
	 * <P>La clase LogFileAppender utiliza la clase <B>java.text.SimpleDateFormat</B> para formatear las fechas, por esta raz�n.</P>
	 * 
	 * <P>Si no se especifica este par�metro, el fichero de log no rotar� por fecha.</P>
	 * 
	 * <P>Este par�metro tambi�n define el momento de la rotaci�n, ya que el archivo rotar� cuando la fecha
	 * formateada seg�n el patr�n definido por este par�metro cambie.</P>
	 * 
	 * <TABLE BORDER="1" CELLSPACING="0" CELLPADDING="2">
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TH>file</TH>
	 * <TH>datePattern</TH>
	 * <TH>fecha</TH>
	 * <TH>nombre de fichero log</TH>
	 * <TH>rotaci�n</TH>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>FILENAME.log</TD>
	 * <TD>'_'MMM.yyyy</TD>
	 * <TD>30/1/2006 17:48:25</TD>
	 * <TD>FILENAME_Jan.2006.log</TD>
	 * <TD>mensual</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>12</TD>
	 * <TD>'_'HH'h_'mm'min'</TD>
	 * <TD>30/1/2006 17:48:25</TD>
	 * <TD>FILENAME_17h_48min.log</TD>
	 * <TD>por minuto</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>FILENAME.log</TD>
	 * <TD>'-'yyyy</TD>
	 * <TD>30/1/2006 17:48:25</TD>
	 * <TD>FILENAME-2006.log</TD>
	 * <TD>anual</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>FILENAME.log</TD>
	 * <TD>yyyyMMdd</TD>
	 * <TD>30/1/2006 17:48:25</TD>
	 * <TD>FILENAME20060130.log</TD>
	 * <TD>diaria</TD>
	 * </TR>
	 * <TR ALIGN="LEFT" VALIGN="TOP">
	 * <TD>FILENAME.log</TD>
	 * <TD>'_'hha</TD>
	 * <TD>30/1/2006 17:48:25</TD>
	 * <TD>FILENAME_05PM.log</TD>
	 * <TD>por hora</TD>
	 * </TR>
	 * </TABLE>
	 * 
	 * @param datePattern patr�n seg�n el cual se debe formatear la fecha actual para a�adirla al nombre de 
	 * archivo.
	 */
	public void setDatePattern(String datePattern) {this.datePattern = datePattern;}
	/**
	 * 
	 * <P>Devuelve el patr�n seg�n el cual se debe formatear la fecha actual para a�adirla al nombre de 
	 * archivo.</P>
	 * 
	 * @return patr�n seg�n el cual se debe formatear la fecha actual para a�adirla al nombre de archivo.
	 */
	public String getDatePattern() {return datePattern;}

	/**
	 * 
	 * <P>Devuelve en nombre del fichero actual de log, sin incluir el path, dependiendo del formato definido 
	 * para el Logger y la fecha actual, y el tama�o m�ximo de archivo y el �ndice y tama�o del archivo actual.</P>
	 * 
	 * @return Nombre del fichero actual de log, sin incluir el path.
	 */
	public String getNoPathFileName() {
		return new File(getFileName()).getName();
	}

	/**
	 * 
	 * <P>Devuelve en nombre del fichero actual de log, dependiendo del formato definido para el Logger y la 
	 * fecha actual, y el tama�o m�ximo de archivo y el �ndice y tama�o del archivo actual.</P>
	 * 
	 * @return Nombre del fichero actual de log. 
	 */
	public String getFileName() {
		if (datePattern != null && (fileNameDate == null || !(new SimpleDateFormat(datePattern)).format(new Date()).equals((new SimpleDateFormat(datePattern)).format(fileNameDate)))) {
			fileNameDate = new Date();
			fileNameNumber = 1;
		}
		else if (maximumFileSize > 0 && (new File(fileName)).length() > maximumFileSize) {
			fileNameNumber = findNextFileNum(fileNameDate);
		}
		return getFileName(fileNameDate, fileNameNumber);
	}
	private String getFileName(Date date, int number) {
		String formattedDate = "";
		if (datePattern != null) {
			formattedDate = (new SimpleDateFormat(datePattern)).format(date);
		}

		String formattedNumber = "";
		if (maximumFileSize > 0) {
			if (maxBackupIndex > 0) {
				String numPattern = "0";
				int tmp = maxBackupIndex;
				while ((tmp = tmp/10) > 0) {
					numPattern += "0";
				}
				formattedNumber = "_" + (new DecimalFormat(numPattern)).format(number);
			}
			else formattedNumber = "_" + Integer.toString(number);
		}

		return originalFileNameNoExt.trim() + formattedDate + formattedNumber + "." + originalFileExtension.trim();
	}


	private void rollOver() {
		String newFilename = getFileName();
		if (!newFilename.equals(fileName) || !(new File(newFilename).exists())) {
			try {
				this.closeFile();
				super.fileName = newFilename;
				this.setFile(super.fileName, true, bufferedIO, bufferSize);
			}
			catch (IOException e) {
				LogLog.error("rollOver() failed.", e);
			}
		}
	}

	protected void subAppend(LoggingEvent event) {
		rollOver();
		super.subAppend(event);
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.spi.OptionHandler#activateOptions()
	 */
	public void activateOptions() {
		super.activateOptions();
		if (fileName != null) {
			File file = new File(fileName);
			this.closeFile();
			if (file.exists()) {
				file.delete();
			}
		}
	}

}//class
