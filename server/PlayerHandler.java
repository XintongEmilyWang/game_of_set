package lab5.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import lab5.client.SetMsgInputStream;
import lab5.client.SetMsgOutputStream;

/**
 *   PlayerHandler
 *   class handles the output from a particular client
 *
 *   Author: Anqi Wang, Jiayi Zhou, Xin Liu, Xintong Wang
 *   Email:  anqi@wustl.edu zhou.jiayi@wustl.edu, liuxin@wustl.edu, xintong.wang@wustl.edu
 *   Course: CSE 132
 *   Lab:    5
 *   File: 	 PlayerHandler.java
 */

public class PlayerHandler extends Thread {

	private Socket socket;
	private GameController game;
	public byte playerID;
	public static int num = 0;
	private boolean shouldDie;
	public String playerName;
	public String message;
	DataOutputStream output;
	SetMsgOutputStream out;

	public PlayerHandler(Socket s, GameController g) {
		this.socket = s;
		this.game = g;
		this.playerID = (byte) ++num;
		this.shouldDie = false;
	}
	
	/**
	 * Tell the game a player has been added
	 * Then repeatedly get a String from the player and
	 * pass it to the game using sendMessage(string);
	 * 
	 * The protocol here is that the Client sends a String
	 * and this class sends it to the game.  You could just as easily
	 * accept integers, by doing in.readInt() instead of in.readUTF()
	 * 
	 * The protocol for your game is up to you, but it has to be
	 * consistent from the Server and Client points of view.
	 */
	public void run() {
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			SetMsgInputStream in = new SetMsgInputStream(input);
			output = new DataOutputStream(socket.getOutputStream());
			out = new SetMsgOutputStream(output);
			while (true) {
				if (shouldDie) {
					System.out.println("Player should die.");
				}
				in.readClientMSG(this, game);
			}
		}
		catch (Throwable t) {
			System.out.println("Noting exception from playerHandler " + t);
		} finally {
			game.removePlayer(this);
		}
	}
	
	/**
	 * tell client what to do according to message type
	 * @param s
	 * @param type:mssg type
	 */
	public void tellClient(int type) {
		try{
			switch (type) {
			case 1: out.writeNewGame(game.gameVersion, playerID);
					game.IDandScore.put(playerID, (byte)0);
					break;
			case 3: out.writePlayerLeave(game.leftPlayer);
					break;
			case 5: out.updateField(game.cardLeft, game.fieldSize, game.cards);
					break;
			case 6: out.updatePlayer((byte)game.numOfPlayer, game.IDandScore);
					break;
			case 7: out.updateScore((byte)game.numOfPlayer, game.IDandScore);
					break;
			case 8: out.updateRoomFull((byte)game.numOfPlayer, (byte)playerID);
					break;
			case 10: out.updateChatBox((byte)game.talkingPlayer, game.message);
			        break;
			case 14: out.select(game.calledPlayer);	
					 break;
			case 17: out.validSet(game.winPlayerNum);
					 break;
			case 18: out.invalidSet(game.calledPlayer, game.reason);
					 break;
			case 60: out.gameover(game.winPlayerNum);
					 break;
			}
		} catch(IOException e){
			System.out.println("IOException during playerHandler tellClient");
		}
	}
	
	/**
	 * boolean indicate die of a player
	 */
	public void die() {
		shouldDie = true;
	}
	
	/**
	 * getter method for player ID
	 * @return playerID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * To string method
	 */
	public String toString() {
		return "Player " + playerID;
	}

}
