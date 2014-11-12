package lab5.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *   GameController
 *   model/controller of this game
 *
 *   Author: Anqi Wang, Jiayi Zhou, Xin Liu, Xintong Wang
 *   Email:  anqi@wustl.edu zhou.jiayi@wustl.edu, liuxin@wustl.edu, xintong.wang@wustl.edu
 *   Course: CSE 132
 *   Lab:    5
 *   File: 	 GameController.java
 */

public class GameController extends Thread {

	public BlockingQueue<Runnable> runqueue; // queue of runnable
	public Set<PlayerHandler> players;
	public Map<Byte, Byte> IDandScore;
	public byte gameVersion;
	public byte calledPlayer;
	public byte leftPlayer;
	public byte cardLeft;
	public byte fieldSize;
	public String[][] cards;
	public int numOfPlayer;
	public byte winPlayerNum;
	public byte reason;
	public String message;
	public String name;
	public byte talkingPlayer;
	public boolean gameStarted = false;
	public int startTime = 0;
	public int currentTime = 0;
	public boolean cardSent = false;

	/**
	 * constructor of gameController
	 */
	public GameController() {
		runqueue = new BlockingQueue<Runnable>(10);
		players = new HashSet<PlayerHandler>();
		IDandScore = new HashMap<Byte,Byte>();
		gameVersion = (byte) -1;
		cardLeft = (byte) 81;
		fieldSize = (byte) 12;
		cards = new String[3][4];
		numOfPlayer = 0;
		winPlayerNum = (byte) -1;
		reason = (byte) 0;
		message= new String();
		talkingPlayer=(byte)0;
	}
	

	/**
	 * Run the server queue keep dequeue from all item in the queue
	 */
	public void run() {
		new Server(10501, this).start();
		while (true) {
			if (startTime != 0 && cardSent == false) {
				getTime();
				if (currentTime-startTime == 10) {
					reason = 1;
					refuseSet();
					startTime= 0;
				}
			}
			if (cardSent) {
				startTime= 0;
			}
			Runnable r = runqueue.dequeue();
			r.run();
		}
	}
	
	/**
	 * Get current time
	 */
	public void getTime() {
		addRunnable(new Runnable() {
			public void run() {
				int temp =(int) ((System.currentTimeMillis()/1000) % (24 * 60 * 60));
				currentTime = temp;
			}
		});
	}
	
	/**
	 * Add new task to queue
	 * @param r: new task in form of runnable
	 */
	public void addRunnable(Runnable r) {
		runqueue.enqueue(r);
	}

	/**
	 * Add player
	 * @param h: new player handler
	 */
	public void addPlayer(final PlayerHandler h) {
		addRunnable(new Runnable() {
			public void run() {
				players.add(h);
				numOfPlayer = numOfPlayer + 1;
				if (numOfPlayer <= 4 && gameStarted ==false) {
					tellClient(h, 1);
					for (PlayerHandler p : players) {
						tellClient(p, 6);
					}
				} else {
					killPlayer(h);
				}
			}
		});
	}

	/**
	 * Remove player when the client requires a quit
	 * @param h: new player handler
	 */
	public void removePlayer(final PlayerHandler h) {
		addRunnable(new Runnable() {
			public void run() {
				for (PlayerHandler p : players) {
					leftPlayer = h.playerID;
					tellClient(p, 3);
					if (p.playerID == h.playerID) {
						numOfPlayer = numOfPlayer - 1;
					}
				}
				players.remove(h);
				IDandScore.remove(h.playerID);
			}
		});
	}

	/**
	 * kill extra player when the room full 
	 * @param h: new player handler
	 */
	public void killPlayer(final PlayerHandler h) {
		addRunnable(new Runnable() {
			public void run() {
				for (PlayerHandler p : players) {
					if (p.getPlayerID() == h.playerID) {
						tellClient(p, 8);
						numOfPlayer = numOfPlayer - 1;
					}
				}
				players.remove(h);
			}
		});
	}

	/**
	 * Randomly generate 12 cards without replacement
	 */
	public String[][] cardAssignment() {
		Set<String> tempCards = new HashSet<String>();
		Set<String> threeYes = new HashSet<String>();
		String aYes = cardAssignmentHelper();
		tempCards.add(aYes);
		threeYes.add(aYes);
		String bYes = cardAssignmentHelper();
		while (tempCards.contains(bYes)) {
			bYes = cardAssignmentHelper();
		}
		tempCards.add(bYes);
		threeYes.add(bYes);
		String cYes = findAnswer(aYes, bYes);
		tempCards.add(cYes);
		threeYes.add(cYes);
		for (int k=0; k<9; k++) {
			String temp = cardAssignmentHelper();
			while (tempCards.contains(temp)) {
				temp = cardAssignmentHelper();
			}
			tempCards.add(temp);
		}
		String[][] newCards = new String[3][4];
		Iterator it= tempCards.iterator();
		String[] tempCardsArr = new String[12];
		Numbers indexes= new Numbers(12);
		for (int m=0; m<12; m++) {
			tempCardsArr[indexes.nums[m]-1]=""+it.next();
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				newCards[i][j] = tempCardsArr[i*4+j];
			}
		}
		return newCards;
	}


	/**
	 * Generate new 4-digit integer randomly
	 */
	public String cardAssignmentHelper() {
		Random generator = new Random();
		int color = generator.nextInt(3)+1;
		int shape = generator.nextInt(3)+1;
		int shade = generator.nextInt(3)+1;
		int number = generator.nextInt(3)+1;
		String cardToCheck = ""+ color + shape + shade + number;
		return cardToCheck;
	}

	/**
	 * find the third card, given two cards
	 */
	public String findAnswer(String a, String b) {
		int temp;
		if (a.charAt(0) == b.charAt(0)) {
			temp = ((int) a.charAt(0) - 48) * 1000;
		} else {
			temp = (6 - ((int) a.charAt(0) - 48) - ((int) b.charAt(0) - 48)) * 1000;
		}
		if (a.charAt(1) == b.charAt(1)) {
			temp += ((int) a.charAt(1) - 48) * 100;
		} else {
			temp += (6 - (a.charAt(1) - 48) - (b.charAt(1) - 48)) * 100;
		}
		if (a.charAt(2) == b.charAt(2)) {
			temp += ((int) a.charAt(2) - 48) * 10;
		} else {
			temp += (6 - ((int) a.charAt(2) - 48) - (int) (b.charAt(2) - 48)) * 10;
		}
		if (a.charAt(3) == b.charAt(3)) {
			temp += ((int) a.charAt(3) - 48) * 1;
		} else {
			temp += (6 - ((int) a.charAt(3) - 48) - ((int) b.charAt(3) - 48)) * 1;
		}
		return ""+temp;
	}

	/**
	 * start a game Send update game field msg to client
	 */
	public void startGame() {
		addRunnable(new Runnable() {
			public void run() {
				if (cardLeft >= 12) {
					gameVersion = gameVersion++;
					fieldSize = 12;
					cardLeft = (byte) (cardLeft-12);
					cards=cardAssignment();
					gameStarted = true;
					for (PlayerHandler p : players) {
						tellClient(p, 5);
					}
				} else {
					shutDown();
				}
			}
		});
	}

	/**
	 * server record the player who pressed call
	 */
	public void recordCall() {
		addRunnable(new Runnable() {
			public void run() {
				for (PlayerHandler p : players) {
					tellClient(p, 14);
				}
			}
		});
	}

	/**
	 * check answer submitted from client
	 *@param a: the name of the first card
	 *@param b: the name of the second card
	 *@param c: the name of the third card
	 *@return boolean: true when the three cards are in a Set!
	 */
	public static boolean checkAnswer(String a, String b, String c) {
		int temp;
		if (a.charAt(0) == b.charAt(0)) {
			temp = ((int)a.charAt(0)-48)*1000;
		}else {
			temp = (6 - ((int)a.charAt(0)-48) - ((int) b.charAt(0)-48)) * 1000;
		}
		if (a.charAt(1) == b.charAt(1)) {
			temp += ((int) a.charAt(1)-48) * 100;
		}else {
			temp += (6 - (a.charAt(1)-48) - (b.charAt(1)-48)) * 100;
		}
		if (a.charAt(2) == b.charAt(2)) {
			temp += ((int) a.charAt(2)-48) * 10;
		}else {
			temp += (6 - ((int) a.charAt(2)-48) - (int)(b.charAt(2)-48)) * 10;
		}
		if (a.charAt(3) == b.charAt(3)) {
			temp += ((int) a.charAt(3)-48) * 1;
		}else {
			temp += (6 - ((int) a.charAt(3)-48) - ((int)b.charAt(3)-48)) * 1;
		}

		if (temp == Integer.parseInt(c)) {
			return true;
		}
			return false;
	}
	
	/**
	 * start a game Send update game field msg to client
	 * @param h: the player who successfully catch a set
	 */
	public void updateScore(PlayerHandler h) {
		addRunnable(new Runnable() {
			public void run() {
				cardSent = false;
				for (PlayerHandler p : players) {
					tellClient(p, 7);
				}
			}
		});
	}

	/**
	 * tell the client refuse reason
	 */
	public void refuseSet() {
		addRunnable(new Runnable() {
			public void run() {
				cardSent = false;
				for (PlayerHandler p : players) {
					tellClient(p, 18);
				}
			}
		});
	}
	
	/**
	 * tell all clients to update chat
	 */
	public void updateMessage(){
		addRunnable(new Runnable() {
			public void run() {
				for (PlayerHandler p : players) {
					tellClient(p, 10);
				}
			}
		});
		
	}
	

	/**
	 * server ends the game
	 */
	public void shutDown() {
		addRunnable(new Runnable() {
			public void run() {
				int max=0;
				for (PlayerHandler p : players) {
					if (IDandScore.get(p.playerID)>max) {
						max=IDandScore.get(p.playerID);
						winPlayerNum=p.playerID;
					}
				}
				for (PlayerHandler p: players) {
					tellClient(p, 60);
				}
				gameStarted = false;
			}
		});
	}
	
	/**
	 * Write to client by calling the method in playerHandller
	 * 
	 * @param p: playerHandller
	 * @param s: message to tell
	 */
	public void tellClient(final PlayerHandler p, final int type) {
		addRunnable(new Runnable() {
			public void run() {
				p.tellClient(type);
			}
		});
	}

}
