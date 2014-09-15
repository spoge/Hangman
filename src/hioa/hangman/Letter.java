package hioa.hangman;


/**
 * Created by NegatioN on 19.08.2014.
 * A class representing a single letter in a word which gives us the ability to check if
 * it should be made visible to the user.
 * 
 * Also used by the keyboard to keep track of which buttons that have been clicked
 */
public class Letter {
    private String letter;
    private boolean visible;
    private int state = 0; // used by keyboard, 0 = not guessed, 1 = correct, -1 = wrong

    public char getCharLetter() {
        return letter.toCharArray()[0];
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Letter (String letter, boolean visible){
        this.letter = letter;
        this.visible = visible;
    }
    
    public Letter (String letter, int state){
        this.letter = letter;
        this.state = state;
    }

    public void setState(int state) {
    	this.state = state;
    }
    
    public int getState(){
    	return state;
    }
    
    public String toString() {
    	return letter;
    }

}
