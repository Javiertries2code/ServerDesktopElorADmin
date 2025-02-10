package errorCodes;


import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {
	
    // Errores generales
    INVALID_INPUT(100, "Entrada inválida"),
    USER_NOT_FOUND(101, "Usuario no encontrado"),
    DATABASE_ERROR(102, "Error en la base de datos"),
    UNKNOWN_ERROR(999, "Error desconocido"),
	   // Errores de conexión por sockets
    SOCKET_CONNECTION_FAILED(200, "Error al conectar con el socket"),
    SOCKET_TIMEOUT(201, "Tiempo de espera de la conexión agotado"),
    SOCKET_DISCONNECTED(202, "Conexión de socket cerrada inesperadamente"),
    SOCKET_IO_ERROR(203, "Error de entrada/salida en el socket"),
    SOCKET_INVALID_DATA(204, "Datos recibidos inválidos o corruptos"),
    SOCKET_AUTHENTICATION_FAILED(205, "Fallo en la autenticación del socket"),
    
    // Errores de conexión y acceso a MySQL
    DATABASE_CONNECTION_FAILED(300, "No se pudo conectar a la base de datos"),
    DATABASE_QUERY_ERROR(301, "Error en la ejecución de la consulta SQL"),
    DATABASE_DUPLICATE_ENTRY(302, "Entrada duplicada en la base de datos"),
    DATABASE_FOREIGN_KEY_CONSTRAINT(303, "Violación de restricción de clave foránea"),
    DATABASE_TIMEOUT(304, "Tiempo de espera agotado en la base de datos"),
    DATABASE_ACCESS_DENIED(305, "Acceso a la base de datos denegado"),
    DATABASE_TRANSACTION_FAILED(306, "Error en la transacción de la base de datos"),
    DATABASE_INVALID_CREDENTIALS(307, "Credenciales de base de datos inválidas"),
    
    
    // Errores de envío de correo electrónico
    EMAIL_SEND_FAILED(400, "Error al enviar el correo electrónico"),
    EMAIL_INVALID_ADDRESS(401, "Dirección de correo inválida"),
    EMAIL_SERVER_UNAVAILABLE(402, "Servidor de correo no disponible"),
    EMAIL_AUTHENTICATION_FAILED(403, "Fallo en la autenticación del servidor de correo"),
    EMAIL_TIMEOUT(404, "Tiempo de espera agotado al enviar correo"),


    ENCRYPTION_ERROR(404, "sE HA PRODUCIDO UN ERROR EN LA ENCRIPTACION");


    private final int code;
    private final String message;

    private static final Map<Integer, String> errorMap = new HashMap<>();

    // Cargar el HashMap
    static {
        for (ErrorCode error : values()) {
            errorMap.put(error.code, error.message);
        }
    }

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // So to get Error enum or HashMap
    public static String getErrorMessage(int code) {
    	
    	//this is a good one, if we call with unknown errors, is gottt hit with a generic error
        return errorMap.getOrDefault(code, "Código de error desconocido");
    }

    // So to add one beforehand, for later recover, as DB errors are  way too many
    public static void addCustomError(int code, String message) {
        errorMap.put(code, message);
    }
}


/*
how is printed----
System.out.println("Error " + ErrorCode.INVALID_INPUT.getCode() + ": " + ErrorCode.INVALID_INPUT.getMessage());


    System.err.println("Error 101: " + ErrorCode.getErrorMessage(101));
 -------->:: Error 101: Usuario no encontrado


//adding dinamic error, aint sure how to handle number

 ErrorCode.addCustomError(200, "Error de autenticación");
System.err.println("Error 200: " + ErrorCode.getErrorMessage(200));
// -------->:: Error 200: Error de autenticación

System.err.println("Error 300: " + ErrorCode.getErrorMessage(300));
// -------->: Error 300: Código de error desconocido
*/
