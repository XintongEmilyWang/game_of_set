package lab5.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lab5.server.GameController;
import lab5.server.PlayerHandler;

/**
 *   SetMsgInputStream
 *   class handles the input from server or client
 *
 *   Author: Anqi Wang, Jiayi Zhou, Xin Liu, Xintong Wang
 *   Email:  anqi@wustl.edu zhou.jiayi@wustl.edu, liuxin@wustl.edu, xintong.wang6@wustl.edu
 *   Course: CSE 132
 *   Lab:    5
 *   File: 	 SetMsgInputStream.java
 */

public class SetMsgInputStream {
	
	DataInputStream dis;
	public int type;
	
	/**
	 * Class reads all the communicating messages
	 * @param DataInputStream dis
	 */
	public SetMsgInputStream(DataInputStream dis) {
		this.dis = dis;
		type= -1;
	}

	/**
	 * To check the "!" at first
	 * @return boolean judge: check if it is a right formated message
	 */
	public boolean correctStart() {
		boolean judge = false;
		try {
			byte temp = dis.readByte();
			judge = temp == ((byte) '!');
		} catch (IOException e) {
		}
		return judge;
	}

	/**
	 * To get message type
	 * @return int type: indicates message type
	 */
	public int readType() {
		int type = -1;
		try {
			type = (int) dis.readByte();
		} catch (IOException e) {
		}
		return type;
	}

	/**
	 * To get length of message content
	 * @return byte length: length of message content
	 */
	public byte readLength() {
		byte length = -1;
		try {
			length = (byte) dis.readShort();
		} catch (IOException e) {
		}
		return length;
	}

	/**
	 * To get name of player
	 * @return String name: name of player
	 */
	public String readName() {
		String name = null;
		try {
			name = dis.readUTF();
		} catch (IOException e) {
		}
		return name;
	}

	/**
	 * To get game version
	 * @return byte gv: game version
	 */
	public byte readGameVersion() {
		byte gv = -1;
		try {
			gv = dis.readByte();
		} catch (IOException e) {
		}
		return gv;
	}

	/**
	 * To get number of player
	 * @return byte pNum: the player's number;
	 */
	public byte readPlayerNum() {
		byte pNum = -1;
		try {
			pNum = dis.readByte();
			
		} catch (IOException e) {
		}
		return pNum;
	}

	/**
	 * To get number of card left
	 * @return byte cardLeft: number of card left;
	 */
	public byte readCardLeft() {
		byte cardLeft = -1;
		try {
			cardLeft = dis.readByte();
		} catch (IOException e) {
		}
		return cardLeft;
	}

	/**
	 * To get game field size
	 * @return byte fieldSize: the size of game field;
	 */
	public byte readFieldSize() {
		byte fieldSize = -1;
		try {
			fieldSize = dis.readByte();
		} catch (IOException e) {
		}
		return fieldSize;
	}

	/**
	 * To get cards setting
	 * the card setting is represented by an array of string.
	 * Each card's string starts with its row number and column number to guide position
	 * and followed by its file name to be found in resources to add them into the GUI
	 * @return String[][] cardNames: string of 12 cards;
	 */
	public String[][] readCards() {
		String[][] cardName = new String[3][4];
		try {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 4; j++) {
					String newAttri = dis.readUTF();
					cardName[i][j] = newAttri;
				}
			}
		} catch (IOException e) {
		}
		return cardName;
	}

	/**
	 * To get replaced cards setting
	 * the card setting is represented by an set of string.
	 * Each card's string starts with its index number to guide position
	 * and followed by its file name to be found in resources to add them into the GUI
	 * the index number should be the same as the one any client previously selected
	 * @return Set<String> cardName: string of 3 cards;
	 */
	public Set<String> readTriCards() {
		Set<String> cardName = new HashSet<String>();
		try {
			String aCard = dis.readUTF();
			cardName.add(aCard); 
			String bCard = dis.readUTF();
			cardName.add(bCard);
			String cCard = dis.readUTF();
			cardName.add(cCard); 
		} catch (IOException e) {
		}
		return cardName;
	}
	
	/**
	 * For server to get the position of the cards chosen
	 * @return byte num: number of players;
	 */
	public byte[] readCardCall() {
		byte[] temp = new byte[3];
		for (int i= 0; i<3; i++) {
			try {
				temp[i] = dis.readByte();
			} catch (IOException e) {
			}
		}
		return temp; 
	}
	
	/**
	 * To get number of total players
	 * @return byte num: number of players;
	 */
	public byte readNumOfPlayer() {
		byte num = -1;
		try {
			num = dis.readByte();
		} catch (IOException e) {
		}
		return num;
	}

	/**
	 * To read timeout information froms server
	 */
	public byte readTimeout() {
		byte reason =(byte) 0;
		try {
			 reason = dis.readByte();
		} catch (IOException e) {
		}
		return reason;
	}
	
	/**
	 * To get a player's score
	 * @return byte score: a player's score;
	 */
	public short readPlayerScore() {
		short score = -1;
		try {
			score = dis.readByte();
		} catch (IOException e) {
		}
		return score;
	}
	
	/**
	 * To get a player's message
	 * @return string: a player's message;
	 */
	public String readMessage(){
		String message = "";
		try {
			message = dis.readUTF();
		} catch (IOException e) {
		}
		return message;
	}

	/**
	 * To get selected cad index
	 * the cards positions are recored as a 2 digit byte
	 * Each card's byte starts with its row number and column number to guide position
	 * @return Set<Byte> index: index of three cards;
	 */
	public Set<String> readSelectedCard() {
		Set<String> index = new HashSet<String>();
		try {
			int k = 0;
			while (k < 3) {
				index.add(dis.readUTF());
				k++;
			}
		} catch (IOException e) {
		}
		return index;
	}

	/**
	 * To read winning player's number
	 * @return byte winningP: the winning player's number;
	 */
	public byte readWinningPlayerNum() {
		byte winningP = -1;
		try {
			winningP = dis.readByte();
		} catch (IOException e) {
		}
		return winningP;
	}
	

	/**
	 * To get reason for invalid set
	 * @return String reason: reason to fail;
	 */
	public String readReason() {
		String reason = new String();
		try {
			byte b = dis.readByte();
			if (b == 1) {
				reason = "TIMEOUT";
			} else if (b == 2) {
				reason = "NOTASET";
			} else {
				throw new Error("wrong reason");
			}
		} catch (IOException e) {
		}
		return reason;
	}
	
	/**
	 * Method to read server message according to msg type
	 * @param Client c, the reading will change client class's instance variable values
	 * @return int the type number
	 */
	public int readServerMSG(Client c) {
		if (this.correctStart()) {
			type = readType();
			readLength();
			switch (type) {
			case 1: c.gameVersion= readGameVersion();
					c.playerID = readPlayerNum();
					c.GUI.updateChat((byte)0, "Player "+c.playerID, "is your new ID");
					c.GUI.updateChat((byte)0, "", "Enjoy your new journey in this game!");
					c.GUI.updateChat((byte)0, "", "You have six trials in this round");
					c.GUI.updateChat((byte)0, "", "The highest possible score per round is 18 points");
					c.GUI.allowStart(c,c.output);
					c.GUI.allowDisconnect(c, c.output);
					c.GUI.allowInput(c, c.output);
					c.GUI.addPlayer((byte)(c.playerID));
					break;
			case 3: int left = readPlayerNum();
					if (c.players.containsKey(left)) {
						c.players.remove(left);
						c.GUI.updateChat((byte)0, "Player "+left, "left game");
						c.GUI.removePlayer((byte)left);
						if (c.playerID==left) {
							System.exit(0);
						}
					}
					break;
			case 5: c.cardLeft = readCardLeft();
					c.fieldSize = readFieldSize();
					c.cardOnTable = readCards();
					c.GUI.setCardsImage(c.cardOnTable);
					c.GUI.updateChat((byte)0, "", "Please find a set from the 12 cards");
					c.GUI.avoidStart();
					c.GUI.allowCall(c, c.output);
					break;
			case 6: c.numberOfPlayer = readNumOfPlayer();
					c.players.clear();
					for (int p = 1; p<=c.numberOfPlayer; p++) {
						c.players.put((int) readPlayerNum(), 0);
						c.GUI.addPlayer((byte)(p));
					}
					break;
			case 7: c.numberOfPlayer = readNumOfPlayer();
			        c.players.clear();
			        for (int p = 1; p<=c.numberOfPlayer; p++) {
						c.players.put((int) readPlayerNum(), (int) readPlayerScore());
						c.GUI.updateScore((byte)c.numberOfPlayer, c.players);
					}
					break;
			case 8: readNumOfPlayer();
			        readPlayerNum();
					if (c.playerID == -1) {
						c.GUI.updateChat((byte)0, "", "Room full");
					}
					break;
			case 10: byte talkedPlayer=readPlayerNum();
					 String msg=readMessage();
	                 c.GUI.updateChat((byte)talkedPlayer, ""+talkedPlayer, msg);
	                 break;	
			case 14: int numToSelect = readPlayerNum();
					 c.GUI.avoidCallPress();
					 c.GUI.updateChat((byte) 0, "Player " + numToSelect, "called");
					 if (c.playerID == numToSelect) {
						 try {
						    c.GUI.allowCardActions(c, c.output);
						 } catch (IOException e) {
						 }
					 }
					 break;
			case 17: c.winningNum = readWinningPlayerNum();
					 c.GUI.updateChat((byte)0, "Player "+ c.winningNum, "got a Set!");
					 break;
			case 18: int numFail = readPlayerNum();
			         c.reason = readReason();
					 if (c.playerID == numFail) {
						 c.GUI.avoidCardActions();
					 }
					 c.GUI.updateChat((byte)0, "Player "+ numFail,"got an invalid set"+ "because " + c.reason);
					 c.GUI.updateChat((byte)0, "", "Everyone try again!");
					 c.GUI.allowCall(c, c.output);
					 break;
			case 60: c.winningNum = readWinningPlayerNum();
					 c.GUI.updateChat((byte)0, "Player " + c.winningNum, "Win!");
					 c.GUI.updateChat((byte)0, "", "Game over! What a sad story!");
					 c.GUI.snooze(5);
					 System.exit(0);
					 break;
			}
			
		}
		return type;
	}
	
	/**
	 * Method to read all client's msg according the type
	 * @param h the reading player handler
	 * @param g the only game controller
	 * @return it type 
	 */
	public int readClientMSG(PlayerHandler h, GameController g) {
		if (this.correctStart()) {
			type = readType();
			readLength();
			switch (type) {
			case 0: h.playerName= readName();
					g.addPlayer(h);
					break;
			case 2: g.leftPlayer = readPlayerNum();
					if (h.playerID == g.leftPlayer) {
						g.removePlayer(h);
					}
					break;
			case 4: readPlayerNum();
					g.startGame();
					break;
			case 9: g.talkingPlayer=readPlayerNum();
				    g.message=readMessage();
			        g.updateMessage();
			        break;			
			case 13: g.calledPlayer = readPlayerNum();
					 g.recordCall();
					 g.startTime = (int) ((System.currentTimeMillis()/1000) % (24 * 60 * 60));
					 break;
			case 15: byte selectedP= readPlayerNum();
					 g.cardSent = true;
				     if (h.playerID == selectedP) {
						 Set<String> aSet = readSelectedCard();
						 String[] checkMe = new String[3];
						 int b = 0;
						 for (String a: aSet) {
							 checkMe[b]=a;
							 b++;
						 }
						 boolean check = g.checkAnswer(checkMe[0], checkMe[1], checkMe[2]);
						 if(check){
							 byte s = g.IDandScore.get(h.playerID);
							 g.IDandScore.remove(h.playerID);
							 g.IDandScore.put(h.playerID, (byte) (s+3));
							 g.updateScore(h);
							 g.startGame();
						 } else{
							 g.reason=2;
							 g.refuseSet();
						 } 
					 } else {
						 throw new Error("Number transition problem");
					 }
					 break;
			case 16: byte timeoutP= readPlayerNum();
					 if (h.playerID == timeoutP) {
						 g.reason=readTimeout();
						 g.refuseSet();
					 } else {
						 throw new Error("bad time out information");
					 }
			}
			
		}
		return type;
	}
}
