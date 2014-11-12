package lab5.server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *   Server
 *   class record the port number for server
 *
 *   Author: Given code
 *   Email:  
 *   Course: CSE 132
 *   Lab:    5
 *   File: 	 Server.java
 */

public class Server extends Thread {

	private int port;
	private GameController game;
	private Socket socket;
	
	public Server(int port, GameController g) {
		this.port = port;
		this.game = g;
	}

	public void run() {
		System.out.println("Server started at port " + port);
		try {
			ServerSocket sSocket = new ServerSocket(port);
			while(true){
				socket = sSocket.accept();
				PlayerHandler playerHandler = new PlayerHandler(socket, game);
				playerHandler.start();
			}
		} catch(Throwable t) {
			throw new Error("Bad server: " + t);
		}
	}

}
