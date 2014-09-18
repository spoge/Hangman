package hioa.hangman.logic;

/**
 * 
 * Object that keeps track of wins/losses for each game.
 *
 */

public class GameLogic {
	private int wins = 0, losses = 0;
	
	public GameLogic(int wins, int losses){
		this.wins = wins;
		this.losses = losses;
	}

	public int getWins() {
		return wins;
	}

	public int getLosses() {
		return losses;
	}
	
	//returns win or loss depending on what happened.
	public int updateWinLoss(boolean win){
		if(win)
			return ++wins;
		else
			return ++losses;
	}
	
}
