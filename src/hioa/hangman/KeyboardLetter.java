package hioa.hangman;


/**
 * 
 * The class is used for displaying a letter on the keyboard.
 * It also defines if it has already been pressed, or not. and what the result of the press was.
 *
 */

public class KeyboardLetter extends Letter{

	private int state;
	
	public KeyboardLetter(String letter, int state) {
		super(letter);
		this.state = state;
		// TODO Auto-generated constructor stub
	}

    public void setState(int state) {
    	this.state = state;
    }
    
    public int getState(){
    	return state;
    }
	
	
}
