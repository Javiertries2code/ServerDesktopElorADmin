package server.socketIO;

import java.util.ArrayList;
import java.util.List;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import DBEntities.Teacher;
import DBManagers.ManagerStudent;
import DBManagers.ManagerTeacher;
import server.socketIO.config.Events;
import server.socketIO.model.MessageInput;
import server.socketIO.model.MessageOutput;

public class CRUDTeacher {
	

	private SocketIOServer server = null;

    public CRUDTeacher(SocketIOServer server) {
		this.server = server;
		// TODO Auto-generated constructor stub
	}
	
	
	

	private DataListener<MessageInput> index() {
		return ((client, data, ackSender) -> {
			// This time, we simply write the message in data
			System.out.println("Client from " + client.getRemoteAddress() + " wants to getAll Teachers");

			List<Teacher> teachers = new ArrayList<Teacher>();

			ManagerTeacher mS = new ManagerTeacher();
			teachers = mS.getAllTeachers();

		
			// Thread.sleep(1000);
			// We parse the answer into JSON
			String answerMessage = new Gson().toJson(teachers);

			// ... and we send it back to the client inside a MessageOutput
			MessageOutput messageOutput = new MessageOutput(answerMessage);
			client.sendEvent(Events.ON_GET_ALL_TEACHERS_ANSWER.value, messageOutput);
		});
	}

	 public DataListener<MessageInput> update() {
	        return (client, data, ackSender) -> {


	        	// Extracting the JSON message
				String message = data.getMessage();
				//System.out.println(message   + " the mensaje ");

				Gson gson = new Gson();
				JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
				
				
				
				
				String email = null;
				String lastName = null;
				String name = null;
				String address = null;
				String phone1 = null;
				String phone2 = null;
				String dni = null;
				String userType = null;
				String passwordHashed = null;
				String passwordNotHashed = null;

				// This is to get all values passed in the json, didnt find how to
				if (jsonObject.has("nameValuePairs")) {
				    JsonObject nameValuePairs = jsonObject.getAsJsonObject("nameValuePairs");
				    email = nameValuePairs.has("email") ? nameValuePairs.get("email").getAsString() : null;
				    lastName = nameValuePairs.has("lastName") ? nameValuePairs.get("lastName").getAsString() : null;
				    name = nameValuePairs.has("name") ? nameValuePairs.get("name").getAsString() : null;
				    address = nameValuePairs.has("address") ? nameValuePairs.get("address").getAsString() : null;
				    phone1 = nameValuePairs.has("phone1") ? nameValuePairs.get("phone1").getAsString() : null;
				    phone2 = nameValuePairs.has("phone2") ? nameValuePairs.get("phone2").getAsString() : null;
				    dni = nameValuePairs.has("dni") ? nameValuePairs.get("dni").getAsString() : null;
				    userType = nameValuePairs.has("user_type") ? nameValuePairs.get("user_type").getAsString() : null;
				    passwordHashed = nameValuePairs.has("passwordHashed") ? nameValuePairs.get("passwordHashed").getAsString() : null;
				    passwordNotHashed = nameValuePairs.has("passwordNotHashed") ? nameValuePairs.get("passwordNotHashed").getAsString() : null;

				System.out.println("Received-" + email + "--- " + lastName + "--- " + name + "--- " + address + "--- " + phone1 + "--- " + phone2 + "--- " + dni + "--- " + userType + "--- " + passwordHashed + "--- " + passwordNotHashed);
				} else {
				    System.out.println("El JSON no contiene 'nameValuePairs'");
				}

				System.out.println("calling to managerStudent-" + email + "--- " + lastName + "--- " + name + "--- " + address + "--- " + phone1 + "--- " + phone2 + "--- " + dni + "--- " + userType + "--- " + passwordHashed + "--- " + passwordNotHashed);

				
				ManagerStudent mS = new ManagerStudent();
				
//				Map<String, Object> userStatus = mL.validUser(email, password);
			String statusCode  = mS.updateStudent(email, name,lastName,address, phone1, phone2,dni,userType,passwordHashed,passwordNotHashed  );
	        
	        };
	    }
	

	    public DataListener<MessageInput> create() {
	        return (client, data, ackSender) -> {
	            // TODO: Implement create logic
	        };
	    }

	    public DataListener<MessageInput> show() {
	        return (client, data, ackSender) -> {
	            // TODO: Implement show logic
	        };
	    }



	    public DataListener<MessageInput> delete() {
	        return (client, data, ackSender) -> {
	            // TODO: Implement delete logic
	        };
	    }
}
