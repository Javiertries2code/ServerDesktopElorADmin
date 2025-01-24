package server.socketIO;

//import org.hibernate.mapping.List;

import java.util.List;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;

import DBEntities.Student;
import DBManagers.ManagerStudent;
import server.socketIO.config.Events;
import server.socketIO.model.MessageInput;
import server.socketIO.model.MessageOutput;

public class CRUDStudent extends SocketIOModule {

    public CRUDStudent(SocketIOServer server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

	public DataListener<MessageInput> index() {
        return (client, data, ackSender) -> {
            System.out.println("Client from " + client.getRemoteAddress() + " wants to getAll");

            // Acceso a la base de datos usando ManagerStudent
            ManagerStudent manager = new ManagerStudent();
            List<Student> students = manager.getAllStudents();

            // Mostrar los resultados (solo para depuración)
            for (Student student : students) {
                System.out.println("ID: " + student.getIdStudent());
                System.out.println("Name: " + student.getName() + " " + student.getLastName());
            }

            // Convertir los estudiantes a JSON y enviarlos al cliente
            String answerMessage = new Gson().toJson(students);
            MessageOutput messageOutput = new MessageOutput(answerMessage);
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
