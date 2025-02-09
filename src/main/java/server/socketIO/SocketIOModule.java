package server.socketIO;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import DBManagers.ManagerLogin;
import server.socketIO.config.Events;
import server.socketIO.model.MessageInput;

/**
 * Server control main configuration class
 */
public class SocketIOModule {

	// The server
	private SocketIOServer server = null;
	private CRUDStudent crudStudent = new CRUDStudent(server);
	private CRUDLogin crudLogin = new CRUDLogin(server);

	private CRUDTeacher crudTeacher = new CRUDTeacher(server);
	
//	private CRUDMeeting crudMeeting = new CRUDMeeting(server);
//	private CRUDSchedule crudSchedule = new CRUDSchedule(server);
//	private CRUDCourse crudCourse = new CRUDCourse(server);
//	private CRUDSubject crudSubject = new CRUDSubject(server);
//	private CRUDUser crudUser = newCRUDLogin CRUDUser(server);
//	CRUDMeetingRequest crudMeetingRequest = new CRUDMeetingRequest(server);
//	CRUDRegistration crudRegistration = new CRUDRegistration(server);

	
	public SocketIOModule(SocketIOServer server) {
		super();
		this.server = server;

		
		ManagerLogin managerLogin= new ManagerLogin();

		

		// Default events (for control the connection of clients)
		server.addConnectListener(onConnect());
		server.addDisconnectListener(onDisconnect());

		// Custom events
		server.addEventListener(Events.ON_LOGIN.value, MessageInput.class, crudLogin.userLogin());
		server.addEventListener(Events.ON_CLIENT_SENDING_ENCRYPTED.value, MessageInput.class, crudLogin.testingEncription());

		server.addEventListener(Events.ON_GET_ALL_STUDENTS.value, MessageInput.class, crudStudent.index());
		server.addEventListener(Events.ON_LOGOUT.value, MessageInput.class, this.logout());
		server.addEventListener(Events.ON_RESET_PASSWORD.value, MessageInput.class, crudLogin.resetPassword());
		
		
		server.addEventListener(Events.ON_REGISTER_TEACHER.value, MessageInput.class, crudTeacher.update());
		server.addEventListener(Events.ON_REGISTER_STUDENT.value, MessageInput.class, crudStudent.update());

		//server.addEventListener(Events.ON_STOP_SERVER.value, MessageInput.class, this.stop());


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


			System.out.println(userName + " loged out");
		});
	}

	// Server control

	public void start() {
		server.start();
		System.out.println("Server started...");
	}

	public boolean stop() {
		server.stop();
		System.out.println("Server stopped");
		return true;
	}
}
