package lab5.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 *   SetMsgOutputStream
 *   class handles the output from server or client
 *
 *   Author: Anqi Wang, Jiayi Zhou, Xin Liu, Xintong Wang
 *   Email:  anqi@wustl.edu zhou.jiayi@wustl.edu, liuxin@wustl.edu, xintong.wang6@wustl.edu
 *   Course: CSE 132
 *   Lab:    5
 *   File: 	 SetMsgOutputStream.java
 */

public class SetMsgOutputStream{
	DataOutputStream out;
	
	/**
	 * constructor
	 * @param output
	 * get output stream
	 */
	public SetMsgOutputStream(DataOutputStream output) {
		out = output;
	}

	/**
	 * Write Message 0: 
	 * Join message: client to server
	 * initialize a new player
	 * @param name
	 * @throws IOException
	 */
	public void writeJoin(String name) throws IOException {
		byte[] temp = name.getBytes();
		write(0);
		out.writeShort(temp.length);
		out.writeUTF(name);
	}
	
	/**
	 * Write Message 1:
	 * NewGame message: server to client
	 * start a new game, update game version and player number
	 * @param gameversion, playernum
	 * @throws IOException
	 */
	public void writeNewGame(byte gameversion, byte playernum) throws IOException {
		write(1);
		out.writeShort(2);
		out.writeByte(gameversion);
		out.writeByte(playernum);
	}
	
	/**
	 * Write Message 2:
	 * Quit message: client to server
	 * quit from game
	 * @param playernum: my player ID
	 * @throws IOException
	 */
	public void writeQuit(byte playernum) throws IOException {
		write(2);
		out.writeShort(1);
		out.writeByte(playernum);
	}
	
	/**
	 * Write Message 3:
	 * Leave message: server to client
	 * delete a leaving player from all clients
	 * @param playernum: ID of the leaving player
	 * @throws IOException
	 */
	public void writePlayerLeave(byte playernum) throws IOException {
		write(3);
		out.writeShort(1);
		out.writeByte(playernum);
	}
	
	/**
	 * Write Message 4:
	 * Start message: client to server
	 * decide to start the game
	 * @param playernum: my player ID
	 * @throws IOException
	 */
	public void writeStart(byte playernum) throws IOException {
		write(4);
		out.writeShort(1);
		out.writeByte(playernum);
	}
	
	/**
	 * Message 5: 
	 * gamefield update : server to client
	 * @param cardsLeft: card left in deck
	 * @param fieldSize: 12 usually
	 * @param cardInfo: string for one card
	 * @throws IOException
	 */
	public void updateField(byte cardsLeft, byte fieldSize, String[][] cardInfo) throws IOException {
		byte[] temp = cardInfo[0][0].getBytes();
		write(5);
		out.writeShort(2+temp.length* ((int)fieldSize));
		out.writeByte(cardsLeft);
		out.writeByte(fieldSize);
		for (int i=0;i<3;i++) {
			for (int j=0; j<4; j++) {
				out.writeUTF(cardInfo[i][j]);
			}
		}
	}
	
	/**
	 * Message 6:
	 * player update message: server to client
	 * @param numOfPlayers
	 * @param playernum
	 * @param playername
	 * @throws IOException
	 */
	public void updatePlayer(byte numOfPlayers, Map<Byte,Byte> id) throws IOException {
		write(6);
		out.writeShort(1 + numOfPlayers);
		out.writeByte(numOfPlayers);
		for (Byte aID: id.keySet()) {
			out.writeByte(aID);
		}
	}
	
	/**
	 * Message 7:
	 * game score update: server to client
	 * @param numOfPlayers
	 * @param playernum
	 * @param score
	 * @throws IOException
	 */
	public void updateScore(byte numOfPlayers, Map<Byte,Byte> id) throws IOException {
		write(7);
		out.writeShort(1+ numOfPlayers*3);
		out.writeByte(numOfPlayers);
		for (Byte aID: id.keySet()) {
			out.writeByte(aID);
			out.writeByte(id.get(aID));
		}
	}
	
	/**
	 * Message 8:
	 * number of players update: server to client
	 * @param numOfPlayers
	 * @param extraP
	 * @throws IOException
	 */
	public void updateRoomFull(byte numOfPlayers, byte extraP) throws IOException {
		write(8);
		out.writeShort(2);
		out.writeByte(numOfPlayers);
		out.writeByte(extraP);
	}
	
	/**
	 * Message 9:
	 * input message: client to server
	 * @param message
	 * @param playernum
	 * @throws IOException
	 */
	public void sendMessage(byte p, String message) throws IOException {
		byte[] temp = message.getBytes();
		write(9);
		out.writeShort(1+temp.length);
		out.writeByte(p);
		out.writeUTF(message);
	}
	
	/**
	 * Message 10:
	 * chatbox update: server to client
	 * @param message
	 * @param playernum
	 * @throws IOException
	 */
	public void updateChatBox(byte p, String message) throws IOException {
		byte[] temp = message.getBytes();
		write(10);
		out.writeShort(temp.length+1);
		out.writeByte(p);
		out.writeUTF(message);
	}
	
	
	/**
	 * Message 13:
	 * send card: client to server
	 * @param playernum
	 * @throws IOException
	 */
	public void sendCall(byte playernum) throws IOException {
		write(13);
		out.writeShort(1);
		out.writeByte(playernum);
	}
	
	/**
	 * Message 14:
	 * select cards: server to client
	 * @param playernum
	 * @throws IOException
	 */
	public void select(byte playernum)throws IOException {
		write(14);
		out.writeShort(2);
		out.writeByte(playernum);
		
	}
	
	/**
	 * Message 15
	 * send select cards: client to server
	 * @param playernum
	 * @param set<String> cardSelected
	 * @throws IOException
	 */
	public void sendCardSelected(byte playernum, Set<String> cardSelected) throws IOException {
		String count = "" + 1111; 
		write(15);
		out.writeShort(1 + count.getBytes().length*3);
		out.writeByte(playernum);
		for (String aCard: cardSelected) {
			out.writeUTF(aCard);
		}
	}
	
	/**
	 * Message 16
	 * send timeout: client to server
	 * @param playernum
	 * @param indicator: 1 as timeout
	 * @throws IOException
	 */
	public void sendTimeout(byte playernum, byte indicator) throws IOException {
		write(16);
		out.writeShort(2);
		out.writeByte(playernum);
		out.writeByte(indicator);
	}
	
	/**
	 * Message 17:
	 * valid set: server to client
	 * @param winPlayerNum
	 * @throws IOException
	 */
	public void validSet(byte winPlayerNum) throws IOException {
		write(17);
		out.writeShort(1);
		out.writeByte(winPlayerNum);
	}
	
	/**
	 * Message 18
	 * invalid set: server to client
	 * @param playernum
	 * @param reason
	 * @throws IOException
	 */
	public void invalidSet(byte playernum, byte reason) throws IOException {
		write(18);
		out.writeShort(2);
		out.writeByte(playernum);
		out.writeByte(reason);// 1==timeout, 2== not aset
	}
	
	/**
	 * Message 60:
	 * gameover: server to client
	 * @param winPlayerNum
	 * @throws IOException
	 */
	public void gameover(byte winPlayerNum) throws IOException {
		write(60);
		out.writeShort(1);
		out.writeByte(winPlayerNum);
	}
	
	/**
	 * Framing message:
	 * write '!' and message type
	 * @param b
	 * @throws IOException
	 */
	public void write(int b) throws IOException {
		out.writeByte((byte)'!');
		out.writeByte((byte) b); // b = type message;
	}
}