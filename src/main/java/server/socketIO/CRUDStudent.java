package server.socketIO;

//import org.hibernate.mapping.List;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import DBEntities.Student;
import DBManagers.ManagerStudent;
import DTO.StudentDTO;
import server.socketIO.config.Events;
import server.socketIO.model.MessageInput;
import server.socketIO.model.MessageOutput;

public class CRUDStudent {

	
	private SocketIOServer server = null;

    public CRUDStudent(SocketIOServer server) {
		this.server = server;
		// TODO Auto-generated constructor stub
	}

	public DataListener<MessageInput> index() {
        return (client, data, ackSender) -> {
            System.out.println("Client from " + client.getRemoteAddress() + " wants to getAll");
            System.out.println("From extended CRUDSTUDENT");
            // Acceso a la base de datos usando ManagerStudent
            ManagerStudent manager = new ManagerStudent();
            List<Student> students = manager.getAllStudents();

            // Mostrar los resultados (solo para depuración)
            for (Student student : students) {
                System.out.println("ID: " + student.getIdStudent());
                System.out.println("Name: " + student.getName() + " \n" + student.getLastName());
                System.out.println("NEXT\n");
      
            }
          

            //DTO para evitar problemas de carga lazy
         // Convierte entidades a DTOs
            List<StudentDTO> studentDTOs = students.stream()
                .map(StudentDTO::new) // Convierte cada Student en StudentDTO
                .collect(Collectors.toList());

            // Convertir los estudiantes a JSON y enviarlos al cliente
            String answerMessage =null;
            try {
				answerMessage = new Gson().toJson(studentDTOs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (answerMessage == null) {
                System.out.println("no hace el json");

            }
            System.out.println("After Gson");

            MessageOutput messageOutput = new MessageOutput(answerMessage);
            System.out.println("responding to client...hopefully");
            client.sendEvent(Events.ON_GET_ALL_STUDENTS_ANSWER.value, messageOutput);
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

	    public DataListener<MessageInput> delete() {
	        return (client, data, ackSender) -> {
	            // TODO: Implement delete logic
	        };
	    }
	
	
	
	
}
