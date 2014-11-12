package lab5.server;

/**
 *   GameServerOnly
 *   class that should be run as a server
 *
 *   Author: Someone/ Given code
 *   Email:  
 *   Course: CSE 132
 *   Lab:    5
 *   File: 	 GameServerOnly.java
 */

public class GameServerOnly {

	/**
	 * Main method to run server
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
			System.out.println("Hostname of local machine: " + localMachine.getHostName());
		}
		catch(java.net.UnknownHostException ex) {
		} 

		GameController game = new GameController();
		game.start();
	}

}
