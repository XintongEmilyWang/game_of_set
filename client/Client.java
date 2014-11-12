package lab5.client;

import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lab5.view.SetGameFrame;

/**
 *  Client
 *  Player program for game of Set.
 *
 *   Author: Anqi Wang, Jiayi Zhou, Xin Liu, Xintong Wang
 *   Email:  anqi@wustl.edu zhou.jiayi@wustl.edu, liuxin@wustl.edu, xintong.wang@wustl.edu
 *   Course: CSE 132
 *   Lab:    5
 *   File:   Client.java
 */

public class Client {

	public String[][] cardOnTable;
	public Map<Integer,Integer> players;
	public Set<Byte> set;
	public Socket socket;
	public SetMsgInputStream input;
	public SetMsgOutputStream output;
	public PropertyChangeListener pcs;
	public SetGameFrame GUI;
	
	public byte gameVersion;
	public byte playerID;
	public String playerName;
	public short score;
	public byte cardLeft;
	public byte fieldSize;
	public byte numberOfPlayer;
	public byte winningNum;
	public String reason;
	public String thisGUISay;
	
	/**
	 * constructor
	 * @param args unused
	 */
	public Client(){
		GUI = new SetGameFrame();
		GUI.setVisible(true);
		
		cardOnTable=new String[3][4];
		players=new HashMap<Integer,Integer>();
		set = new HashSet<Byte>();
		
		gameVersion = -1;
		playerID = (byte)-1;
		playerName = new String();
		score = -1;
		cardLeft = -1;
		fieldSize = -1;
		numberOfPlayer= -1;
		winningNum = -1;
		reason = new String();
		thisGUISay = "";
	}
	
	/**
	 * Run the client
	 */
	public void run(){
		try {
			socket = new Socket("localhost",10501);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out= new DataOutputStream(socket.getOutputStream());
			input = new SetMsgInputStream(in);
			output = new SetMsgOutputStream(out);
			
			GUI.allowConnect(this, output);
			while (true) {	
				input.readServerMSG(this);
			}

		} catch (IOException e) {
		}
	}
	
	/**
	 * main thread, open a new client
	 * @param args
	 */
	public static void main(String[] args) {
		Client c = new Client();
		c.run();
	}
}
