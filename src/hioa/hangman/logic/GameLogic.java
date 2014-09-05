package hioa.hangman.logic;

import android.widget.TextView;

public class GameLogic {
	private int wins, losses;
	
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
