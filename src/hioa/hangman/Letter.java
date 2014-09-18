package hioa.hangman;


/**
 * Created by NegatioN on 19.08.2014.
 * A class representing a single letter. The reason a string is used instead of char
 * is because we would have to spend CPU-time on converting a lot of these to chars. Especially information from views.
 */
public abstract class Letter {
    private String letter;

    public Letter(String letter){
    	this.letter = letter;
    }
    
    //used for storing the data onDestroy. Ex: screen flip
    public char getCharLetter() {
        return letter.charAt(0);
    }
    
   
    public void setLetter(String letter) {
        this.letter = letter;
    }
   
    public String toString() {
    	return letter;
    }

}
