package es.sdweb.application.componentes.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import es.sdweb.application.componentes.util.StringUtil;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;



/**
 * @author Antonio Carro Mariño
 *
 * Funciones de utilidad para tratar con ficheros y directorios.
 */
public abstract class FileUtil {

	public static void closeFile(InputStream is){
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		};
	}
	
	
	/**
         * Abre un fichero a partir de su pathCompleto. Si el fichero no existe devuelve null y se imprime un error por la salida estandar de error.
         * @param pathCompleto Path completo del fichero
         * @return InputStream del fichero, o null si el fichero no existe.
         */
        public static InputStream openFile(String pathCompleto){
		InputStream result=null;
		try {
			result=new FileInputStream(pathCompleto);
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.err);
		}
		return result;
	}

        /**
         * Abre un fichero a partir de su pathCompleto. Si el fichero no existe devuelve null y se imprime un error por la salida estandar de error.
         * @param pathCompleto Path completo del fichero
         * @param log True si queremos que muestre logs en la salida de error estándar
         * @return InputStream del fichero, o null si el fichero no existe.
         */
        public static InputStream openFile(String pathCompleto,boolean log){
		InputStream result=null;
		try {
			result=new FileInputStream(pathCompleto);
		} catch (FileNotFoundException e) {
                    if (log) e.printStackTrace(System.err);
		}
		return result;
	}
        
	
	public static String getExtension(String nombreFichero){
		String result="";
		if ((nombreFichero!=null)&&(!nombreFichero.equals(""))){
			int pos=nombreFichero.lastIndexOf("."); //buscamos la posici�n del �ltimo punto
			result=nombreFichero.substring(pos+1); //nos quedamos con los chars desde pos+1 en adelante
		};
		return result;
	}
	
	
	public static void saveFile(String pathCompleto,byte[] fileBytes){
		try {
			FileOutputStream fos=new FileOutputStream(pathCompleto);
			//ByteArrayOutputStream baos=new ByteArrayOutputStream()
			fos.write(fileBytes);
			fos.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		};
	}
	
	
	public static String getNombreFichero(String pathCompleto){
		String result="";
		if ((pathCompleto!=null)&&(!pathCompleto.equals(""))){
			int pos1=pathCompleto.lastIndexOf("/"); //buscamos la posici�n de la �ltima barra
			int pos2=pathCompleto.lastIndexOf("\\"); //buscamos la posici�n de la �ltima barra
			int pos=(pos1>pos2?pos1:pos2); //nos quedamos con la mayor pos (-1 si no se hall� ninguna)			
			result=pathCompleto.substring(pos+1); //nos quedamos con los chars desde pos+1 en adelante
		};
		return result;
	}
	
	
	public static String getCurrentDirectory(){
		String result="";
		File dir1 = new File (".");
		try {
		   result=dir1.getCanonicalPath();
		}
		catch(Exception e) {
		   e.printStackTrace(System.err);
		};
		return result;
	}
	
	
	public static String ponBarra(String pathDirectorio){
		String result="";
		if (!StringUtil.isEmpty(pathDirectorio)){
		  char lastChar=pathDirectorio.charAt(pathDirectorio.length()-1);
		  String barra=(pathDirectorio.indexOf("\\")>0?"\\":"/");
		  result=((lastChar=='/')||(lastChar=='\\')?pathDirectorio:pathDirectorio+barra);
		};
		return result;
	}

	
	
	public static boolean deleteFile(String pathCompleto){
            boolean result=false;
            File f = new File(pathCompleto);

            // Make sure the file or directory exists and isn't write protected
            if (f.exists()){
              if (f.canWrite()){
                 if (!f.isDirectory()) {
                   result = f.delete();
                 }
              }
            }
	    return result;
	}//method


        /**
         * Mueve un archivo de una ubicación a otra, sobreescribiendo el destino si este existe. Si alguno de los dos
         * paths completos no existe, no hace nada.
         * @param pathCompletoFicheroOrigen Path completo al fichero origen.
         * @param pathCompletoFicheroDestino Path completo al fichero destino.
         */
        public static void move(String pathCompletoFicheroOrigen, String pathCompletoFicheroDestino) {

            Path origenPath = FileSystems.getDefault().getPath(pathCompletoFicheroOrigen);
            Path destinoPath = FileSystems.getDefault().getPath(pathCompletoFicheroDestino);

            try {
                Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.err.println(e);
            }

        }

        
}//class
