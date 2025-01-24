package server.socketIO;

import java.util.ArrayList;
import java.util.List;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;

import DBEntities.Teacher;
import DBManagers.ManagerTeacher;
import server.socketIO.config.Events;
import server.socketIO.model.MessageInput;
import server.socketIO.model.MessageOutput;

public class CRUDTeacher extends SocketIOModule{
	
	 private final ManagerTeacher managerTeacher;


	public CRUDTeacher(SocketIOServer server) {
		super(server);
		this.managerTeacher = new ManagerTeacher();
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
