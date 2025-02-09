package crypt;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Encoder {
	
	private final  String CLAVE_DEFECTO = "88888888"; 

	public Map<String, byte[]> encoder(String mensaje, String miclave) throws GeneralSecurityException, NoSuchPaddingException {
		String claveUsuario = null;
		
		Map<String, byte[]> mapaMensaje = new HashMap<>();
		
	if(null == miclave)
	{
		claveUsuario = CLAVE_DEFECTO;

	}
	SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");

	DESKeySpec clavEspec = new DESKeySpec(claveUsuario.getBytes());

	SecretKey claveSecreta = skf.generateSecret(clavEspec);
	

	Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

	cipher.init(Cipher.ENCRYPT_MODE, claveSecreta);

	byte[] mensajeCodificado = cipher.doFinal(mensaje.getBytes());

	byte[] iv = cipher.getIV();
	
	mapaMensaje.put("mensajeCofificado", mensajeCodificado);
	mapaMensaje.put("iv", iv);

	
	
	
	return mapaMensaje;
	}
	
}
