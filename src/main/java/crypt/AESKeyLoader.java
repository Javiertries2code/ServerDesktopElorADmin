package crypt;

import java.io.*;
import java.util.Properties;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class AESKeyLoader {
    
    private static SecretKey secretKey = null; 
    
    public static SecretKey loadAESKey() throws IOException {
        
        if (secretKey != null) {
            System.out.println("Already loaded, delivering instance of key");
            return secretKey; 
        }
        System.out.println("Not loaded yet");

        byte[] bytes = null;
        Properties properties = new Properties();

        try (InputStream input = AESKeyLoader.class.getClassLoader().getResourceAsStream("serverConfig.properties")) {
            if (input == null) {
                throw new FileNotFoundException("serverConfig.properties NOT FOUND");
            }
            System.out.println("serverConfig.properties ENCONTRADO");
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Error al cargar serverConfig.properties");
            e.printStackTrace();
        }

        String keyPath = properties.getProperty("aes.key.path");
        
        System.out.println("keyPath encontrado para claves: " + keyPath);
        if (keyPath == null || keyPath.isEmpty()) {
            throw new IllegalArgumentException(" Smthg went wrong in serverConfig.properties");
        }

        Path path = Paths.get(keyPath);
        System.out.println("Ruta absoluta del archivo de clave: " + path.toAbsolutePath());

   

        try {
            bytes = Files.readAllBytes(path);
        } catch (Exception e) {
            System.out.println("Error al leer el archivo de clave.");
            e.printStackTrace();
        }

     
      

     
      
        secretKey = new SecretKeySpec(bytes, "AES");
        System.out.println("Clave encontrada y cargada");

        return secretKey;
    }

  
    public static SecretKey getAESKey() throws IOException {
        if (secretKey == null) {
            return loadAESKey();
        }
        return secretKey;
    }
}
