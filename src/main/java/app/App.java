package app;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

import server.socketIO.SocketIOModule;

public class App {
	
	//	private static final String HOST_NAME =  "172.19.0.1" ;
 //  private static final String HOST_NAME =  "10.0.22.43" ;

	private static final String HOST_NAME = "192.168.1.40";
	private static final int PORT = 5008;
	
	public static final String PURPLE = "\u001B[35m";
	public static final String GREEN = "\u001B[32m";
	public static final String RESET = "\u001B[0m";
	
	public static void main(String[] args) {
		
		// Server configuration 
		Configuration config = new Configuration ();
		config.setHostname(HOST_NAME);
		config.setPort(PORT);
		
		// We start the server
		SocketIOServer server = new SocketIOServer(config);
		SocketIOModule module = new SocketIOModule(server);
		module.start();
	}
}