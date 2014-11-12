package lab5.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import lab5.client.Client;
import lab5.client.SetMsgOutputStream;

/**
 *   SetGameFrame
 *   class to visualize the game
 *
 *   Author: Anqi Wang, Jiayi Zhou, Xin Liu, Xintong Wang
 *   Email:  anqi@wustl.edu zhou.jiayi@wustl.edu, liuxin@wustl.edu, xintong.wang6@wustl.edu
 *   Course: CSE 132
 *   Lab:    5
 *   File: SetGameFrame.java
 */

public class SetGameFrame extends JFrame {

	private JPanel contentPane;
	private JLabel[] players;
	private JTextField[] scores;
	public JButton[][] cards;
	private JList chat;
	private JScrollPane scrollp;
	private JTextField input;

	private JButton enter;
	public JButton call;
	public JTextField name;
	private String nameToJoin;
	public JButton join;
	public JButton leave;
	public JButton start;
	public Set<String> selected;
	private String[] chatHistory;
	private int trackChat;

	private int PANEL_WIDTH = 1400;
	private int PANEL_HEIGHT = 780;
	private int EDGE_WIDTH = 50;
	private int EDGE_HEIGHT = 30;
	private int LABEL_WIDTH = 100;
	private int LABEL_HEIGHT = 50;
	private int CARD_WIDTH = 140;
	private int CARD_HEIGHT = 200;
	private int CHAT_WIDTH = 290;
	private int CHAT_HEIGHT = 320;
	private int CHAT_X = 960;

	public boolean sendCall;
	public int count = 0;
	public boolean connect = false;
	public int startTime=0; 

	/**
	 * Create the frame.
	 */
	public SetGameFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		players = new JLabel[4];
		scores = new JTextField[4];
		cards = new JButton[3][4];
		setCards();
		setPlayer((byte)1);
		setPlayer((byte)2);
		setPlayer((byte)3);
		setPlayer((byte)4);
		
		input = new JTextField();
		input.setBounds(CHAT_X, EDGE_HEIGHT * 2 + CHAT_HEIGHT, LABEL_WIDTH,LABEL_HEIGHT);
		contentPane.add(input);

		enter = new JButton("Enter");
		enter.setBounds(CHAT_X + LABEL_WIDTH + EDGE_WIDTH, EDGE_HEIGHT * 2 + CHAT_HEIGHT, LABEL_WIDTH, LABEL_HEIGHT);
		contentPane.add(enter);

		call = new JButton("CALL");
		call.setBounds(CHAT_X, EDGE_HEIGHT * 3 + CHAT_HEIGHT + LABEL_HEIGHT, CHAT_WIDTH - EDGE_WIDTH, LABEL_HEIGHT);
		contentPane.add(call);
		
		name = new JTextField();
		name.setBounds(CHAT_X, EDGE_HEIGHT * 4 + CHAT_HEIGHT + LABEL_HEIGHT * 2, LABEL_WIDTH, LABEL_HEIGHT);
		contentPane.add(name);
		
		join = new JButton("Join!");
		join.setBounds(CHAT_X + LABEL_WIDTH + EDGE_WIDTH, EDGE_HEIGHT * 4 + CHAT_HEIGHT + LABEL_HEIGHT * 2, LABEL_WIDTH, LABEL_HEIGHT);
		contentPane.add(join);

		start = new JButton("Start");
		start.setBounds(CHAT_X + LABEL_WIDTH + EDGE_WIDTH, EDGE_HEIGHT * 5 + CHAT_HEIGHT + LABEL_HEIGHT * 3, LABEL_WIDTH, LABEL_HEIGHT);
		contentPane.add(start);
		
		leave = new JButton("Quit");
		leave.setBounds(CHAT_X, EDGE_HEIGHT * 5 + CHAT_HEIGHT + LABEL_HEIGHT * 3, LABEL_WIDTH, LABEL_HEIGHT);
		contentPane.add(leave);

		chat = new JList();
		scrollp = new JScrollPane(chat);
		chat.setBounds(CHAT_X, EDGE_HEIGHT, CHAT_WIDTH, CHAT_HEIGHT);
		scrollp.setBounds(CHAT_X, EDGE_HEIGHT, CHAT_WIDTH, CHAT_HEIGHT);
		contentPane.add(scrollp);
		
		selected = new HashSet<String>();
		chatHistory = new String[100];
		trackChat = -1;
		nameToJoin = new String();

		this.setBounds(0, 0, 1300, 800);
		this.setVisible(true);
	}

	/**
	 * Add a Player
	 * @param p: player number
	 */
	public void setPlayer(byte p) {
		int x = EDGE_WIDTH;
		int y1 = (EDGE_HEIGHT + LABEL_HEIGHT) * 2 * (p - 1) + EDGE_HEIGHT;
		int y2 = y1 + EDGE_HEIGHT + LABEL_HEIGHT;
		players[p-1] = new JLabel("Player " + p);
		players[p-1].setBounds(x, y1, LABEL_WIDTH, LABEL_HEIGHT);
		contentPane.add(players[p-1]);
		scores[p-1] = new JTextField();
		scores[p-1].setBounds(x, y2, LABEL_WIDTH, LABEL_HEIGHT);
		contentPane.add(scores[p-1]);
		players[p-1].setVisible(false);
		scores[p-1].setVisible(false);
	}

	/**
	 * Add action listener to Button join and start to connect the server
	 */
	public void allowConnect(Client client, SetMsgOutputStream out) {
		final SetMsgOutputStream output = out;
		final Client c = client;
		join.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect = true;
				try {
					if (name.getText().length() == 0)
						name.setText("Anonymous");
					c.playerName = name.getText();
					output.writeJoin(name.getText());
				} 
					catch (IOException e1) {
				}
				System.out.println(name.getText()+" joined the game");
				avoidConnect();
			}
		});
	}

	/**
	 * Method to remove actionListener of join
	 */
	public void avoidConnect() {
		ActionListener[] al=  join.getActionListeners();
		for (int i = 0; i<al.length; i++) {
			join.removeActionListener(al[i]);
		}
	}
	
	/**
	 * Add a Player
	 * @param p: player number
	 */
	public void addPlayer(byte p) {
		players[p-1].setVisible(true);
		scores[p-1].setVisible(true);
	}
	
	/**
	 * delete a Player
	 * @param p: player number 
	 */
	public void removePlayer(byte p) {
		players[p-1].setVisible(false);
		scores[p-1].setVisible(false);
	}

	/**
	 * Add action listener to Button leave and start to disconnect the server
	 */
	public void allowDisconnect(Client client, SetMsgOutputStream out) {
		final SetMsgOutputStream output = out;
		final Client c = client;
		leave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					output.writeQuit(c.playerID);
				} catch (IOException e1) {
				}
			}
		});
	}
	
	/**
	 * Add action listener to Button start and start the game
	 */
	public void allowStart(Client client, SetMsgOutputStream out) {
		final SetMsgOutputStream output = out;
		final Client c = client;
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					output.writeStart(c.playerID);
				} catch (IOException e1) {
					throw new Error("bad start in client side");
				}
			}
		});
	}
	
	/**
	 * Remove action listener to Button start
	 * After one successfully click start button, all others' action listener should be removed
	 */
	public void avoidStart() {
		ActionListener[] al=  start.getActionListeners();
		for (int i = 0; i<al.length; i++) {
			start.removeActionListener(al[i]);
		}
	}
	
	/**
	 * set Layout of 12 Cards as JButtons listen for mouse click count up o
	 * three clicks and then remove all actionListener
	 */
	public void setCards() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				int x = 200 + (CARD_WIDTH + EDGE_WIDTH) * (j);
				int y = (EDGE_HEIGHT + CARD_HEIGHT) * (i) + EDGE_HEIGHT;
				cards[i][j] = new JButton();
				cards[i][j].setBounds(x, y, CARD_WIDTH, CARD_HEIGHT);
				contentPane.add(cards[i][j]);
			}
		}
	}

	/**
	 * Update cards' image
	 */
	public void setCardsImage(String[][] cardInflow) {
		try {
			for (int i=0; i<3;i++) {
				for (int j=0; j<4; j++) {
					String thisName = "/image/" + cardInflow[i][j] + ".jpg";
					cards[i][j].setIcon(new ImageIcon(ImageIO.read(getClass().getResource(thisName))));
				}
			}
		} catch (IOException e) {
		}
	}
	
	/**
	 * Add actionListen to call button
	 * @param Client c
	 * @param SetMsgOutputStream out
	 */
	public void allowCall(Client c, SetMsgOutputStream out) {
		final Client client = c;
		final SetMsgOutputStream output = out;
		call.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					output.sendCall(client.playerID);
				} catch (IOException e1) {
					throw new Error("bad call in client side");
				}
			}
		});
	}
	
	/**
	 * Method to remove actionListener for call
	 */
	public void avoidCallPress() {
		ActionListener[] al=  call.getActionListeners();
		for (int i = 0; i<al.length; i++) {
			call.removeActionListener(al[i]);
		}
	}
	
	/**
	 * call timeout method
	 * @return true or false
	 */
	public boolean timeout(long timeStart){
		long timerS = timeStart;
		long timerEnd = System.currentTimeMillis();
		 if ((timerEnd-timerS)>10000){
			 return false;
		 }
		 else return true;
	}
	
	/**
	 * Method to add card-buttons' actionListeners
	 * @throws IOException 
	 */
	public void allowCardActions(Client client, SetMsgOutputStream out) throws IOException {
		final Client c = client;
		final SetMsgOutputStream output = out;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				final int ii=i;
				final int jj=j;
				cards[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						count = count + 1;
						selected.add(c.cardOnTable[ii][jj]);
						if (count == 3) {
							try {
								output.sendCardSelected((byte) c.playerID, selected);
								count = 0;
								
							} catch (IOException e) {
							}
							avoidCardActions();
							selected.clear();
						}
					}
				});
			}
		}
	}
	
	/**
	 * Method to remove all of the actionListener
	 */
	public void avoidCardActions() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				ActionListener[] al=  cards[i][j].getActionListeners();
				for (int k = 0; k<al.length; k++) {
					cards[i][j].removeActionListener(al[k]);
				}
			}
		}
	}
	

	/**
	 * Update one player's Scores
	 */
	public void updateScore(byte numP, Map<Integer,Integer> map) {
		for (Integer p: map.keySet()) {
			scores[p-1].setText("score: " + map.get(p));
		}
	}
	
	/**
	 * add actionListener to text box of input and enter button
	 * @param client
	 */
	public void allowInput(Client client, SetMsgOutputStream out) {
		final Client c = client;
		final SetMsgOutputStream output = out;
		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.thisGUISay = "" + input.getText();
				try {
					output.sendMessage(c.playerID, c.thisGUISay);
				} catch (IOException e) {
				}
			}
		});
	}
	
	/**
	 * update the content of chat the chat history at maximum tracks 100 Strings
	 * if more things happen, the history box will clear history and restart
	 */
	public void updateChat(byte p, String playername, String sayings) {
		trackChat = trackChat + 1;
		String thisTime = null;
		if (p == 0) {
			if (playername.length() != 0) {
				thisTime = "Server" + " says: " + playername + " " + sayings;
			} else
				thisTime = "Server" + " says: " + sayings;
		} else {
			thisTime = "Player " + playername + " says: " + sayings;
		}
		if (trackChat < 100) {
			chatHistory[trackChat] = thisTime;
			chat.setListData(chatHistory);
		} else {
			chatHistory = new String[100];
			trackChat = 0;
			chatHistory[trackChat] = sayings;
			chat.setListData(chatHistory);
		}
	}
	
	/**
	 * sleep for several secs
	 * @param secs: second to sleep
	 */
	public void snooze(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (Throwable t) {
			throw new Error("Bad sleep: " + t);
		}
	}

}