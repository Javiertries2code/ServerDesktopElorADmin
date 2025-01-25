package server.socketIO;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

import server.socketIO.model.MessageInput;

public class CRUDMeetingRequest {

	private SocketIOServer server = null;

	
	public CRUDMeetingRequest(SocketIOServer server) {
		this.server = server;
		// TODO Auto-generated constructor stub
	}
	
	 public DataListener<MessageInput> index() {
	        return (client, data, ackSender) -> {
	            // TODO: Implement index logic
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
