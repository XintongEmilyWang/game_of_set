package lab5.model;

import java.util.HashSet;
import java.util.Set;

import lab5.model.Player;

public class SetGame {
	private Set<Player> players;	// set of players in the game
	private int numPlayers; 		// number of players since game started
	
	public SetGame() {
		players = new HashSet<Player>();
		numPlayers = 0;
	}
	
	/**
	 * Adds a player to the set of Players playing set.
	 * @param p player to be added to set of Players
	 */
	public synchronized void addPlayer(String player) {
		numPlayers++;
		Player p = new Player(player,numPlayers);
		if (players.contains(p)) {
			numPlayers--;
			return;
		}
		players.add(p);
	}
	
	/**
	 * Accessor for set of players in game.
	 * @return set of players in game
	 */
	public synchronized Set<Player> getPlayers() {
		return players;
	}
	
}
