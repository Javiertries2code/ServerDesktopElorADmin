package server.socketIO;

//import org.hibernate.mapping.List;

import java.util.List;
import java.util.stream.Collectors;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;

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
	            // TODO: Implement update logic
	        };
	    }

	    public DataListener<MessageInput> delete() {
	        return (client, data, ackSender) -> {
	            // TODO: Implement delete logic
	        };
	    }
	
	
	
	
}
