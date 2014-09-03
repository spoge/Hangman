package hioa.hangman;

/**
 * Created by NegatioN on 19.08.2014.
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
