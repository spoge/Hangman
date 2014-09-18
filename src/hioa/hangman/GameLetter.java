package hioa.hangman;

/**
 * 
 * The class is used for displaying a letter on the word that's being guessed.
 *
 */

public class GameLetter extends Letter{

	private boolean visible;
	
	public GameLetter(String letter, boolean visible) {
		super(letter);
		this.visible = visible;

	}
	
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
