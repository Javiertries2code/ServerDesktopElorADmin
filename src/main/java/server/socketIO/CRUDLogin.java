package server.socketIO;

import java.util.Map;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import DBManagers.ManagerLogin;
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
				Map<String, Object> userStatus = mL.validUser("teacher1@email.com", "123");

				// Extract and convert the user object
				Object user = userStatus.get("user");
				//the objects goes as a json string, this is why
				String answerMessage;
				if (user != null) {
				    answerMessage = gson.toJson(user);
				} else {
				    answerMessage = gson.toJson("User not found or invalid credentials or I fucked");
				}
				//At least i send the not found messge, to avoid null problems, if any
				MessageOutput messageOutput = new MessageOutput(answerMessage);

				// Handle user status with a switch case
				String status = (String) userStatus.get("status"); 
				
				System.out.println("status devuelto de ManagerLogin->" + status);
				
				// Extract status from the map
				switch (status) {
				case "registered":
					client.sendEvent(Events.ON_LOGIN_SUCCESS_ANSWER.value, messageOutput);
					break;
				case "not registered":
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

	  
	  
	  
}
