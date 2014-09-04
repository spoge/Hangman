package hioa.hangman;

/**
 * Created by NegatioN on 19.08.2014.
 * A class representing a single letter in a word which gives us the ability to check if
 * it should be made visible to the user.
 */
public class Letter {
    private String letter;
    private boolean visible;

    public String getLetter() {
        return letter;
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

}
