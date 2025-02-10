package crypt;

import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESEncoder {
	
	private final  String CLAVE_DEFECTO = "88888888"; 

	public String encoder(String mensaje, String miclave) throws GeneralSecurityException, NoSuchPaddingException {
		String claveUsuario = null;
		 Map<String, byte[]> mapaMensajeCodificado = new HashMap<String, byte[]>();
		 
	SecretKey claveSecreta =  getSecretKey(miclave);


	Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

	cipher.init(Cipher.ENCRYPT_MODE, claveSecreta);

	byte[] mensajeCodificado = cipher.doFinal(mensaje.getBytes());

	byte[] iv = cipher.getIV();
	
	mapaMensajeCodificado.put("mensajeCofificado", mensajeCodificado);
	mapaMensajeCodificado.put("iv", iv);

	
	
	
	return new String(mensajeCodificado);
	}
	
	public  String decoder(Map<String, byte[]> mapaMensajeCodificado,  String miclave)   throws GeneralSecurityException, NoSuchPaddingException,NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		String message = null;

		SecretKey claveSecreta =  getSecretKey(miclave);
		
		byte[] iv = mapaMensajeCodificado.get("iv");
		byte[] mensajeCodificado = mapaMensajeCodificado.get("mensajeCofificado");

		
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		IvParameterSpec dps = new IvParameterSpec(iv);

		cipher.init(Cipher.DECRYPT_MODE, claveSecreta, dps);

		byte[] mensajeDecodificado = cipher.doFinal(mensajeCodificado);

		
		
		return new String(mensajeDecodificado);
	}
	
	private SecretKey getSecretKey( String miclave) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
		String claveUsuario = null;
		if(null == miclave)
		{
			claveUsuario = CLAVE_DEFECTO;

		}
			SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");

			DESKeySpec clavEspec = new DESKeySpec(claveUsuario.getBytes());

			SecretKey claveSecreta = skf.generateSecret(clavEspec);
			return claveSecreta;
		
	}
}
