package server.socketIO;

import java.util.ArrayList;
import java.util.List;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import DBEntities.Student;
import DBManagers.ManagerStudent;
import server.socketIO.config.Events;
import server.socketIO.model.MessageInput;
import server.socketIO.model.MessageOutput;

/**
 * Server control main configuration class
 */
public class SocketIOModule {

	// The server
	private SocketIOServer server = null;

	public SocketIOModule(SocketIOServer server) {
		super();
		this.server = server;
		
		CRUDStudent crudStudent = new CRUDStudent(server);
		CRUDTeacher crudTeacher = new CRUDTeacher(server);
		CRUDMeeting crudMeeting = new CRUDMeeting(server);
		CRUDSchedule crudSchedule = new CRUDSchedule(server);
		CRUDCourse crudCourse = new CRUDCourse(server);
		CRUDSubject crudSubject = new CRUDSubject(server);
		CRUDMeetingRequest crudMeetingRequest = new CRUDMeetingRequest(server);
		CRUDRegistration crudRegistration = new CRUDRegistration(server);
		

		// Default events (for control the connection of clients)
		server.addConnectListener(onConnect());
		server.addDisconnectListener(onDisconnect());

		// Custom events
		server.addEventListener(Events.ON_LOGIN.value, MessageInput.class, this.login());
		server.addEventListener(Events.ON_GET_ALL_STUDENTS .value, MessageInput.class, crudStudent.index());
		server.addEventListener(Events.ON_LOGOUT.value, MessageInput.class, this.logout());
	}

	// Default events

	private ConnectListener onConnect() {
		return (client -> {
			client.joinRoom("default-room");
			System.out.println("New connection, Client: " + client.getRemoteAddress());
		});
	}

	private DisconnectListener onDisconnect() {
		return (client -> {
			client.leaveRoom("default-room");
			System.out.println(client.getRemoteAddress() + " disconected from server");
		});
	}

	// Custom events

	private DataListener<MessageInput> login() {
		return ((client, data, ackSender) -> {
			System.out.println("Client from " + client.getRemoteAddress() + " wants to login");

			// The JSON message from MessageInput
			String message = data.getMessage();

			// We parse the JSON into an JsonObject
			// The JSON should be something like this: {"message": "patata"}
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
			String userName = jsonObject.get("message").getAsString();

			// We access to database and...
			// Let's say it answers with this...
			Student student = new Student();
			student.setName("Student Test");

			// We parse the answer into JSON
			String answerMessage = gson.toJson(student);

			// ... and we send it back to the client inside a MessageOutput
			MessageOutput messageOutput = new MessageOutput(answerMessage);
			client.sendEvent(Events.ON_LOGIN_ANSWER.value, messageOutput);
		});
	}

	private DataListener<MessageInput> getAllStudents() {
		return ((client, data, ackSender) -> {
			// This time, we simply write the message in data
			System.out.println("Client from " + client.getRemoteAddress() + " wants to getAll");

			// We access to database and... we get a bunch of people
			 List<Student> students = new ArrayList<Student>();

//		        students.add(new Student(true, "email1@example.com", "hashedPassword1", 1234, "12345678A", "John", "Doe", "123 Main St", "123456789", "987654321", null, null));
//		        students.add(new Student(false, "email2@example.com", "hashedPassword2", 5678, "87654321B", "Jane", "Smith", "456 Elm St", "123123123", "321321321", null, null));
//		        students.add(new Student(true, "email3@example.com", "hashedPassword3", 9101, "12349876C", "Alice", "Johnson", "789 Oak St", "456456456", "654654654", null, null));
//		        students.add(new Student(true, "email4@example.com", "hashedPassword4", 1121, "56781234D", "Bob", "Williams", "101 Pine St", "789789789", "987987987", null, null));
//		        students.add(new Student(false, "email5@example.com", "hashedPassword5", 3141, "43219876E", "Charlie", "Brown", "202 Cedar St", "321654987", "789123456", null, null));
//
//		        // Print the list
//		        for (Student student : students) {
//		            System.out.println("ID: " + student.getIdStudent());
//		            System.out.println("Name: " + student.getName() + " " + student.getLastName());
//		            System.out.println("Email: " + student.getEmail());
//		            System.out.println("DNI: " + student.getDni());
//		            System.out.println("-----------------------");
//		        }
			 
			 ManagerStudent  mS = new ManagerStudent();
			 students = mS.getUserStudent();
			 
			  for (Student student : students) {
		            System.out.println("ID: " + student.getIdStudent());
		            System.out.println("Name: " + student.getName() + " " + student.getLastName());
		            System.out.println("Email: " + student.getEmail());
		            System.out.println("DNI: " + student.getDni());
		            System.out.println("-----------------------");
		        }
			  Thread.sleep(1000);
			// We parse the answer into JSON
			String answerMessage = new Gson().toJson(students);

			// ... and we send it back to the client inside a MessageOutput
			MessageOutput messageOutput = new MessageOutput(answerMessage);
			client.sendEvent(Events.ON_GET_ALL_STUDENTS_ANSWER.value, messageOutput);
		});
	}

	private DataListener<MessageInput> logout() {
		return ((client, data, ackSender) -> {
			// This time, we simply write the message in data
			System.out.println("Client from " + client.getRemoteAddress() + " wants to logout");

			// The JSON message from MessageInput
			String message = data.getMessage();

			// We parse the JSON into an JsonObject
			// The JSON should be something like this: {"message": "patata"}
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
			String userName = jsonObject.get("message").getAsString();

			// We do something on dataBase? ¯_(ツ)_/¯

			System.out.println(userName + " loged out");
		});
	}
	
	// Server control 

	public void start() {
		server.start();
		System.out.println("Server started...");
	}

	public void stop() {
		server.stop();
		System.out.println("Server stopped");
	}
}
