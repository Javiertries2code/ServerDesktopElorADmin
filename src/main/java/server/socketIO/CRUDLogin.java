package server.socketIO;

import java.util.Map;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import DBEntities.Student;
import DBEntities.Teacher;
import DBManagers.ManagerLogin;
import DTO.StudentDTO;
import DTO.TeacherDTO;
import app.App;
import crypt.AESEncoder;
import server.socketIO.config.Events;
import server.socketIO.model.MessageInput;
import server.socketIO.model.MessageOutput;

public class CRUDLogin {

	private SocketIOServer server = null;

	  public CRUDLogin(SocketIOServer server) {
			this.server = server;
			// TODO Auto-generated constructor stub
		}
	
	  
	  
	  public DataListener<MessageInput> userLogin() {
			return ((client, data, ackSender) -> {
				
				 AESEncoder codeEncode = new AESEncoder();
				System.out.println("Client from " + client.getRemoteAddress() + " wants to log in userLogin");

				
			
				// Extracting the JSON message
				String message = data.getMessage();
				//System.out.println(message   + " the mensaje ");
				
				
				/*	System.out.println("recibo mensaje encriptadoxxxxxxxxx");
				System.out.println(message);
				//Here i decode the message  just got
				message = codeEncode.decrypt(message);
				
				System.out.println("desencriptadoxxxxxxxxxxxxxx");
				System.out.println(message);
				*/
				
				
				Gson gson = new Gson();
				JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
				
				
				String email = null;
				 String password  =null;
				// This is to get all values passed in the json, didnt find how to"
				if (jsonObject.has("nameValuePairs")) {
				    JsonObject nameValuePairs = jsonObject.getAsJsonObject("nameValuePairs");
				     email = nameValuePairs.has("email") ? nameValuePairs.get("email").getAsString() : null;
				     password = nameValuePairs.has("password") ? nameValuePairs.get("password").getAsString() : null;

				    System.out.println("Email: " + email + " Password: " + password);
				} else {
				    System.out.println("El JSON no contiene 'nameValuePairs'");
				}
				
				System.out.println("calling to managerLogin.validUSer ---CRUDLOGIN-- with" + email +"--- "+password);
				

				// Validate user with extracted credentials
				ManagerLogin mL = new ManagerLogin();
				
//				Map<String, Object> userStatus = mL.validUser(email, password);
				Map<String, Object> userStatus = mL.validUser(email, password);
				
				
				String answerMessage;

              if (userStatus== null) {
            	  System.out.println("GOT NULL IN VALID USER");
		            answerMessage = gson.toJson("User not found or invalid credentials");
              }else {
            	  System.out.println("GOT a VALID USER");

				// Extract and convert the user object
				Object user = userStatus.get("user");
				System.out.println("returned user-- user name--->"+ user.getClass());

				//the objects goes as a json string, this is why
				 if (user instanceof Teacher) {
						System.out.println("user instanceof Teacher)");

			            Teacher teacher = (Teacher) user; 
			            TeacherDTO teacherDTO = new TeacherDTO(teacher); 
			            answerMessage = gson.toJson(teacherDTO); 
			        } else if (user instanceof Student) {
						System.out.println("user instanceof Student)");

			            Student student = (Student) user; 
			            StudentDTO studentDTO = new StudentDTO(student); 
			            answerMessage = gson.toJson(studentDTO); // Convert DTO to JSON
			            
			        } else if (user != null) {
			            answerMessage = gson.toJson("User is not a Teacher or Student");
			        } else {
			            answerMessage = gson.toJson("What the fucking ever");

			        }}
              
              
              System.out.println("Genero mensaje sin encriptar");
				System.out.println(message);
            //  Here i Sould encrypt the message
				//At least i send the not found messge, to avoid null problems, if any
           
				
				//answerMessage =codeEncode.encrypt(answerMessage);
              
				MessageOutput messageOutput = new MessageOutput(answerMessage);
				
				System.out.println("Envioencriptado");
			//	System.out.println(message);
				
				String status;
				// Handle user status with a switch case
				if(userStatus != null) {
					status	= (String) userStatus.get("status"); 
				}
				else {
					status = "not existant";
				}
				 
				
			//	System.out.println("status devuelto de ManagerLogin->" + status + user.getClass());
				
				// Extract status from the map
				switch (status) {
				case "registered":
					System.out.println("YES REGISTERED");
					
					try {
						client.sendEvent(Events.ON_LOGIN_SUCCESS_ANSWER.value, messageOutput);
						
						 answerMessage =codeEncode.encrypt(answerMessage);
			              
					MessageOutput cryptedMessageOutput = new MessageOutput(answerMessage);
					client.sendEvent(Events.ON_SERVER_SENDING_ENCRYPTED.value, cryptedMessageOutput);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
					break;
				case "not registered":
					System.out.println("NOT REGISTERED");
					client.sendEvent(Events.ON_NOT_REGISTERED.value, messageOutput);
					break;
				case "not existant":
					System.out.println("Not existant " + status);

					client.sendEvent(Events.ON_LOGIN_USER_NOT_FOUND_ANSWER.value, messageOutput);
					break;
				default:
					System.out.println("Unknown user status: " + status);
					client.sendEvent(Events.ON_LOGIN_USER_NOT_FOUND_ANSWER.value, messageOutput);

				}
			});
		}
	  
	  
	  public DataListener<MessageInput> resetPassword() {
			return ((client, data, ackSender) -> {
				
				System.out.println("Client from " + client.getRemoteAddress() + " requested password reset");

				String message = data.getMessage();
				Gson gson = new Gson();
				JsonObject jsonObject = gson.fromJson(message, JsonObject.class);


				String email = null;
				 String password  =null;
				
				if (jsonObject.has("nameValuePairs")) {
				    JsonObject nameValuePairs = jsonObject.getAsJsonObject("nameValuePairs" );
				     email = nameValuePairs.has("email") ? nameValuePairs.get("email").getAsString() : null;

				    System.out.println("Email:    " + email);
				} else {
				    System.out.println("El JSON no contiene 'nameValuePairs' enresetPassword");
					client.sendEvent(Events.ON_RESET_PASSWORD_FAILER.value, new MessageOutput("Invalid request: email is missing"));
				}
				
			
				System.out.println( "CRUD login got a request for a new password with email--- ");
				// Validate user with extracted email
				
				ManagerLogin mL = new ManagerLogin();
				int result;
				String responseMessage;

				try {
					result = mL.resetPassword(email);
					if (result == 1) {
						
						//mL.setNewPassword(email);
						responseMessage = "Si el usuario existe, se habra enviado un email con su nueva conrasena";
						MessageOutput messageOutput = new MessageOutput(gson.toJson(Map.of("message", responseMessage)));
						client.sendEvent(Events.ON_RESET_PASSWORD_SUCCESSFULL.value, messageOutput);
					} if (result == 2){
						responseMessage = "Se ha completado la accion de cambio de contrasena y No se envio de email";
						MessageOutput messageOutput = new MessageOutput(gson.toJson(Map.of("message", responseMessage)));
						client.sendEvent(Events.ON_CHANGE_NO_MAIL.value, messageOutput);
					}if (result == 0){
						responseMessage = "No se ha completado la accion de cambio de contrasena y envio de email";
						MessageOutput messageOutput = new MessageOutput(gson.toJson(Map.of("message", responseMessage)));
						client.sendEvent(Events.ON_LOGIN_USER_NOT_FOUND_ANSWER.value, messageOutput);
					}
					
				} catch (Exception e) {
					System.err.println("Error during password reset: " + e.getMessage());
					responseMessage = "An error occurred while processing your request";
				}

				// Send response back to the client, i guess, showing the response was sent and delivering it to login, should do enough
			
			});
		}
////////////////////////////////////////////////////////////////////
	  public DataListener<MessageInput>  testingEncription() {
		  /*
		   * OOOki doki, if everything happens as i wish ut does... it will bounce back the encrypted message, that as it 
		   * is a valid user should contain this data :-P
		   * 
		   */
		  return (client, data, ackSender) -> {
			  System.out.println(App.GREEN+ "se ha recibido ON_CLIENT_SENDING_ENCRYPTED"+App.RESET);
			// Extracting the JSON message
			  Object receivedData = data.getMessage();
			  String message;

			  if (receivedData instanceof Object[] && ((Object[]) receivedData).length > 0) {
			      message = String.valueOf(((Object[]) receivedData)[0]);
			  } else {
			      message = String.valueOf(receivedData);
			  }
				// System.out.println(message + " the mensaje ");
				 AESEncoder codeEncode = new AESEncoder();
//Code me... code youuuu!!! coding forever...
			//	message = codeEncode.decrypt(message);
				 
				 //it seems i had to cast, fuck me...
		            message = codeEncode.decrypt(message);

				

				Gson gson = new Gson();
				JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

				  System.out.println(App.PURPLE+ "after json"+message+App.RESET);

			  
			 
			  
		
	  };
}
	  

	  /////////////////////////////////////////////////
	/// ENDOFCLASSS
	 }

	  

