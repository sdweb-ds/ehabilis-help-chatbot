
import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
//import java.util.Base64; //Sustituye a sun.misc.BASE64Decoder y sun.misc.BASE64Encoder a partir de Java 11

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;


public class Cifrar {

	private static final String ALGORITMO = "DES";
	private static int longoRijndael = 64;

	public static byte[] chave2bytes(String chave) {
		ByteArrayOutputStream chaveBytes = new ByteArrayOutputStream();
		int numbytes = (longoRijndael / 8);
		chaveBytes.write(chave.getBytes(), 0, (numbytes>chave.getBytes().length?chave.getBytes().length:numbytes));
		while (chaveBytes.size() < (longoRijndael / 8)) {
			numbytes = (longoRijndael / 8) - chaveBytes.size();
			chaveBytes.write(chave.getBytes(), 0, (numbytes>chave.getBytes().length?chave.getBytes().length:numbytes));
		}
		return chaveBytes.toByteArray();
	}

	public static byte[] cifrar(byte[] chave, String textoOrixe) {
		Cipher cifrador;
		try {
			cifrador = Cipher.getInstance(ALGORITMO);
			SecretKeySpec claveSecreta = new SecretKeySpec(chave, ALGORITMO);
			cifrador.init(javax.crypto.Cipher.ENCRYPT_MODE,claveSecreta);
			return cifrador.doFinal(textoOrixe.getBytes());
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Algoritmo no encontrado. Excepcion: " +e.getMessage());
			return null;
		} catch (NoSuchPaddingException e) {
			System.out.println("Padding no encontrado. Excepcion: " + e.getMessage());
			return null;
		} catch (InvalidKeyException e) {
			System.out.println("Clave inv�lida. Excepcion: " + e.getMessage());
			return null;
		} catch (IllegalStateException e) {
			System.out.println("Estado ilegal. Excepcion: " + e.getMessage());
			return null;
		} catch (IllegalBlockSizeException e) {
			System.out.println("Tama�o de bloque ilegal. Excepcion: " + e.getMessage());
			return null;
		} catch (BadPaddingException e) {
			System.out.println("Padding incorrecto. Excepcion: " + e.getMessage());
			return null;
		}
	}

	public static String cifrar(String pwd, String textoOrixe) {

		byte[] chavebytes = Cifrar.chave2bytes(pwd);
		byte[] textoCifrado = Cifrar.cifrar(chavebytes, textoOrixe);

		String valorCifrado = (new BASE64Encoder().encode(textoCifrado)); // Obsoleto desde Java 11
		return valorCifrado;
	}


	public static void main(String[] args){
		if (args.length<2){
			System.out.println("Cifrar [password_cifrado] [palabra_a_cifrar]");
		}
		else{
			String passwdEncriptada=Cifrar.cifrar(args[0],args[1]); //encriptamos la palabra
			System.out.println("Palabra en limpio: " + args[1]);
			System.out.println("Palabra encriptada: " + passwdEncriptada);
		}
	};
}
