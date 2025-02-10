package crypt;






import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import app.App;

//import server.config.ServerConfig;

public class AESEncoder {


	public static String encrypt(String dataToEncrypt) throws Exception {
		System.out.println("Enterinen encrypts");
		
		
		
		SecretKey key = AESKeyLoader.loadAESKey();

		String encryptedString = null;
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		
		byte[] encryptedData = cipher.doFinal(dataToEncrypt.getBytes());
		encryptedString = Base64.getEncoder().encodeToString(encryptedData);
		
		return encryptedString;
	}
	public static String decrypt(String encryptedData) throws Exception {
	    System.out.println("Entering decrypt");
	   System.out.println("object received --- "+encryptedData);
	   
	   
	   encryptedData = encryptedData.replaceAll("[^A-Za-z0-9+/=]", "");

	    byte[] decodedData = null;
		try {
		    System.out.println("En el try");

			decodedData = Base64.getDecoder().decode(encryptedData);
		    System.out.println("atascado");

			  System.out.println(App.PURPLE+ " got a decodedData"+App.RESET);

		} catch (Exception e) {
			  System.out.println(App.PURPLE+ "Didnt get decodedData"+App.RESET);
			e.printStackTrace();
		}
	    
	    SecretKey key = AESKeyLoader.loadAESKey();
	    
	 
		  System.out.println(App.PURPLE+ "about to decifer"+App.RESET);

	   
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, key);
	    

	    byte[] decryptedData = cipher.doFinal(decodedData);
	    String decryptedString = new String(decryptedData);
		  System.out.println(App.PURPLE+ "RETURNING "+decryptedString+App.RESET);

	    return decryptedString;
	}

	
/*
	
	public static String decrypt(String encryptedData) throws Exception {
		System.out.println("Enterinen decrypt");

	byte[] decodedData = Base64.getDecoder().decode(encryptedData);

		SecretKey key = AESKeyLoader.loadAESKey();

		String decryptedString = null;
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		
		byte[] decryptedData = cipher.doFinal(decodedData);
		decryptedString = new String(decryptedData);
		
		return decryptedString;
	}
*/
}


