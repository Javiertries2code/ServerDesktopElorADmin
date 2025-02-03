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
				System.out.println("Client from " + client.getRemoteAddress() + " wants to log in userLogin");

				// Extracting the JSON message
				String message = data.getMessage();
				//System.out.println(message   + " the mensaje ");

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
				
				System.out.println("calling to managerLogin.validUSer ---CRUDLOGIN--" + email +"--- "+password);
				

				// Validate user with extracted credentials
				ManagerLogin mL = new ManagerLogin();
				
//				Map<String, Object> userStatus = mL.validUser(email, password);
				Map<String, Object> userStatus = mL.validUser(email, password);

				// Extract and convert the user object
				Object user = userStatus.get("user");
				System.out.println("returned user-- user name--->"+ user.getClass());

				//the objects goes as a json string, this is why
				String answerMessage;
				 if (user instanceof Teacher) {
						System.out.println("user instanceof Teacher)");

			            Teacher teacher = (Teacher) user; // Cast user to Teacher
			            TeacherDTO teacherDTO = new TeacherDTO(teacher); // Convert to DTO
			            answerMessage = gson.toJson(teacherDTO); // Convert DTO to JSON
			        } else if (user instanceof Student) {
						System.out.println("user instanceof Student)");

			            Student student = (Student) user; // Cast user to Student
			            StudentDTO studentDTO = new StudentDTO(student); // Convert to DTO
			            answerMessage = gson.toJson(studentDTO); // Convert DTO to JSON
			        } else if (user != null) {
			            answerMessage = gson.toJson("User is not a Teacher or Student");
			        } else {
			            answerMessage = gson.toJson("User not found or invalid credentials");

			        }
				//At least i send the not found messge, to avoid null problems, if any
				MessageOutput messageOutput = new MessageOutput(answerMessage);

				// Handle user status with a switch case
				String status = (String) userStatus.get("status"); 
				
				System.out.println("status devuelto de ManagerLogin->" + status + user.getClass());
				
				// Extract status from the map
				switch (status) {
				case "registered":
					System.out.println("YES REGISTERED");
					client.sendEvent(Events.ON_LOGIN_SUCCESS_ANSWER.value, messageOutput);
					break;
				case "not registered":
					System.out.println("NOT REGISTERED");
					client.sendEvent(Events.ON_NOT_REGISTERED.value, messageOutput);
					break;
				case "not existent":
					client.sendEvent(Events.ON_LOGIN_USER_NOT_FOUND_ANSWER.value, messageOutput);
					break;
				default:
					System.out.println("Unknown user status: " + status);
				}
			});
		}

	  
	  public DataListener<MessageInput> resetPassword() {
			return ((client, data, ackSender) -> {
				System.out.println("Client from " + client.getRemoteAddress() + " requested password reset");

				String message = data.getMessage();
				Gson gson = new Gson();
				JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

				if (!jsonObject.has("email")) {
					System.out.println("Invalid request: email is missing");
					client.sendEvent(Events.ON_RESET_PASSWORD_ANSWER.value, new MessageOutput("Invalid request: email is missing"));
					return;
				}

				String email = jsonObject.get("email").getAsString();
				
				
				System.out.println( "CRUD login got a request for a new password with email--- ");
				// Validate user with extracted email
				
				ManagerLogin mL = new ManagerLogin();
				boolean userFound;
				String responseMessage;

				try {
					userFound = mL.findByEmail(email);
					if (userFound) {
						// TODO: Call method to send the reset password email
						//mL.setNewPassword(email);
						responseMessage = "Si el usuario existe, se habra enviado un email con su nueva conrasena";
					} else {
						responseMessage = "Si el usuario existe, se habra enviado un email con su nueva conrasena, para que el hacker no sepa quien esta o no";
					}
				} catch (Exception e) {
					System.err.println("Error during password reset: " + e.getMessage());
					responseMessage = "An error occurred while processing your request";
				}

				// Send response back to the client, i guess, showing the response was sent and delivering it to login, should do enough
//				MessageOutput messageOutput = new MessageOutput(gson.toJson(Map.of("message", responseMessage)));
//				client.sendEvent(Events.ON_RESET_PASSWORD_ANSWER.value, messageOutput);
			});
		}  
	  
}
