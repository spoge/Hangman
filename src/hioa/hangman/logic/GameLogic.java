package hioa.hangman.logic;

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
	
	public void updateWinLoss(boolean win){
		if(win)
			wins++;
		else
			losses++;
	}
	
}
