package es.sdweb.application.componentes.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
//import java.util.Base64; //Sustituye a sun.misc.BASE64Decoder y sun.misc.BASE64Encoder a partir de Java 11
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import es.sdweb.application.componentes.util.logging.Log;

//import Rijndael;

import es.sdweb.application.util.GestorParametrosConfiguracion;
import sun.misc.BASE64Decoder; // No soportado desde Java 11
import sun.misc.BASE64Encoder; // No soportado desde Java 11


/**
 * @author Antonio Carro Mariño
 *
 * Clase de utilidad para cifrar y descifrar textos.
 *
 */
public class CifradorRijndael {

	private static final String ALGORITMO = "DES";

	private String usuario="UNKNOWN";
	private String ip="UNKNOWN";
	//Nombre del fichero que contiene la clave para el cifrado de contraseñas
	private static final String FICHERO_CLAVE_PARA_CIFRADO = "key.properties";

	private int longoRijndael = 64;

	public CifradorRijndael() {
	}

	public CifradorRijndael(int longoRijndael) {
		this.longoRijndael = longoRijndael;
	}

    public void setUSER_IP(String usuario, String ip){
    	this.usuario=usuario;
    	this.ip=ip;
    }

	private byte[] chave2bytes(String chave) {
		ByteArrayOutputStream chaveBytes = new ByteArrayOutputStream();
		int numbytes = (longoRijndael / 8);
		chaveBytes.write(chave.getBytes(), 0, (numbytes>chave.getBytes().length?chave.getBytes().length:numbytes));
		while (chaveBytes.size() < (longoRijndael / 8)) {
			numbytes = (longoRijndael / 8) - chaveBytes.size();
			chaveBytes.write(chave.getBytes(), 0, (numbytes>chave.getBytes().length?chave.getBytes().length:numbytes));
		}
		return chaveBytes.toByteArray();
	}

	public byte[] cifrar(byte[] chave, String textoOrixe) {
		Cipher cifrador;
		try {
			Log.logRT(usuario,"Ciframos el texto con la clave rijndael...");
			cifrador = Cipher.getInstance(ALGORITMO);
			SecretKeySpec claveSecreta = new SecretKeySpec(chave, ALGORITMO);
			cifrador.init(javax.crypto.Cipher.ENCRYPT_MODE,claveSecreta);
			return cifrador.doFinal(textoOrixe.getBytes());
		} catch (NoSuchAlgorithmException e) {
			Log.logRE(usuario,"cifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Algoritmo no encontrado.");
			return null;
		} catch (NoSuchPaddingException e) {
			Log.logRE(usuario,"cifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Padding no encontrado.");
			return null;
		} catch (InvalidKeyException e) {
			Log.logRE(usuario,"cifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Clave invalida.");
			return null;
		} catch (IllegalStateException e) {
			Log.logRE(usuario,"cifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Estado ilegal.");
			return null;
		} catch (IllegalBlockSizeException e) {
			Log.logRE(usuario,"cifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Tamaño de bloque ilegal.");
			return null;
		} catch (BadPaddingException e) {
			Log.logRE(usuario,"cifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Padding incorrecto.");
			return null;
		}
	}

	public String descifrar(byte[] chave, byte[] cifrado) {
		Cipher cifrador;
		try {
			Log.logRT(usuario,"Desciframos el texto cifrado con la clave rijndael...");
			cifrador = Cipher.getInstance(ALGORITMO);
			cifrador.init(javax.crypto.Cipher.DECRYPT_MODE, new SecretKeySpec(chave, ALGORITMO));
			return new String(cifrador.doFinal(cifrado));
		} catch (NoSuchAlgorithmException e) {
			Log.logRE(usuario,"descifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Algoritmo no encontrado.");
			return null;
		} catch (NoSuchPaddingException e) {
			Log.logRE(usuario,"descifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Padding no encontrado.");
			return null;
		} catch (InvalidKeyException e) {
			Log.logRE(usuario,"descifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Clave invalida.");
			return null;
		} catch (IllegalStateException e) {
			Log.logRE(usuario,"descifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Estado ilegal.");
			return null;
		} catch (IllegalBlockSizeException e) {
			Log.logRE(usuario,"descifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Tamaño de bloque ilegal.");
			return null;
		} catch (BadPaddingException e) {
			Log.logRE(usuario,"descifrar(String)", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Padding incorrecto.");
			return null;
		}
	}

	public static String rijndaelPassword() {
		String claveRijndael = null;
		try {
			//Log.logRT(null,"Leemos parametro de file path del fichero de properties donde se encuentra la clave de encriptado de rijndael...");
			String rijndaelPasswordFilePath = GestorParametrosConfiguracion.getParametro("rijndael.password.properties.file.path");
			//Log.logRT(null,"Leemos parametro de clave de la entrada del fichero de properties donde se encuentra la clave de encriptado de rijndael...");
			String rijndaelPasswordEntryKey = GestorParametrosConfiguracion.getParametro("rijndael.password.properties.entry.key");

			//Log.logRT(null,"Abrimos el fichero de properties " + rijndaelPasswordFilePath + " para lectura...");
			InputStream claveRijndaelFileIs = new FileInputStream(new File(rijndaelPasswordFilePath));
			Properties claveRijndaelProperties = new Properties();
			claveRijndaelProperties.load(claveRijndaelFileIs);

			//Log.logRT(null,"Leemos la entrada " + rijndaelPasswordEntryKey + "...");
			claveRijndael = claveRijndaelProperties.getProperty(rijndaelPasswordEntryKey);
		} catch(IOException e) {
			Log.logRE(null,"rijndaelPassword()", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Error al intentar leer el fichero de properties donde se encuentra la clave rijndael.");
			return null;
		}
		return claveRijndael;
	}

	public static String cifrar(String textoOrixe) {
		Log.logRT(null,"BEGIN CifradorRijndael.cifrar(String)");

		CifradorRijndael cifrador = new CifradorRijndael();
		byte[] chavebytes = cifrador.chave2bytes(rijndaelPassword());
		byte[] textoCifrado = cifrador.cifrar(chavebytes, textoOrixe);

		String valorCifrado = (new BASE64Encoder()).encode(textoCifrado); // Obsoleto desde Java 11
		//String valorCifrado = new String(Base64.getEncoder().encode(textoCifrado));
		Log.logRT(null,"END CifradorRijndael.cifrar(String)");

		return valorCifrado;
	}

	public static String descifrar(String textoCifrado) {
		try {
			//Log.logRT(null,"BEGIN CifradorRijndael.descifrar(String)");
			CifradorRijndael cifrador = new CifradorRijndael();
			byte[] chavebytes = cifrador.chave2bytes(rijndaelPassword());
			byte[] cifrado = (new BASE64Decoder()).decodeBuffer(textoCifrado); // Obsoleto desde Java 11
			//byte[] cifrado = Base64De.decode(textoCifrado);
			String textoOrixe = cifrador.descifrar(chavebytes, cifrado);
			//Log.logRT(null,"END CifradorRijndael.descifrar(String)");

			return textoOrixe;
		} catch (Exception e) {
			Log.logRE(null,"CifradorRijndael.descifrar()", Log.TIPO_SISTEMA, Log.CRITICIDAD_NORMAL, 1001, "Error al intentar convertir el texto en base64 a byte[].");
			return null;
		}

	}



}
