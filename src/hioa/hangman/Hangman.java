package hioa.hangman;

import hioa.hangman.logic.ArrayListAdapter;
import hioa.hangman.logic.ViewHandler;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Hangman extends Activity {

    private ArrayList<Letter> letters;
    private LinearLayout letterHolder;
    private ImageView hangedMan;
    private WordDatabase wdb;

    public static int FAULTS = 0;
    public static int LEFT = -1; // how many letters left til victory

    private final int WON = 1, LOST = -1, PLAYING = 0; // int values represesnting which state the game is in
    private static int STATE = 0; // current state of the game
    
    private static Context mContext;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);

        mContext = this;
        
        //creates our database with words from the selected language-option
        wdb = new WordDatabase(fetchWords());
        
        // creating hangman-image
        hangedMan = (ImageView) findViewById(R.id.imageView);
        ViewHandler.hang(this, hangedMan, FAULTS);

        //creation of the underlined boxes for the letters on screen
        letters = new ArrayList<Letter>();

        letterHolder = (LinearLayout) findViewById(R.id.llHorizontal);
        letters = getRandomWord(letters);

        //we refernce this adapter later when we want to make changes to the textviews
        ArrayListAdapter.addViews(this, letters, letterHolder);



        // generates keyboard buttons
        buttonGenerator();
    }
    
    // used in ViewHandler to reset textcolor on keyboard-buttons
    public static Context getContext(){
    	return mContext;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //gets us a new word for the game, accessible through actionbar.
        if (id == R.id.action_refresh) {
        	reset();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // method executed in onClick of keyboard-buttons
    public void executeButtonClick(View view) {
        if(STATE != 0) {
            Toast.makeText(this, "The game is over, press \"Reset\"", Toast.LENGTH_SHORT).show();
            return;
        }

        Button button = (Button) view;
        view.setEnabled(false);
        updateTextViews(button.getText().toString(), button);
        ViewHandler.hang(this, hangedMan, FAULTS);

        
        
        STATE = checkState();
        if(STATE == WON) Toast.makeText(this, "YOU WON!", Toast.LENGTH_SHORT).show();
        else if(STATE == LOST) Toast.makeText(this, "YOU LOST!", Toast.LENGTH_SHORT).show();
    }

    // checks the game-state (win/lose)
    public int checkState(){
        if(FAULTS >= 10) return LOST;
        if(LEFT == 0) return WON;
        return PLAYING;
    }

    // takes user input-letter and checks our textviews to see if they are matching
    public void updateTextViews(String inputLetter, Button button){
    	
        boolean found = checkInputLetter(inputLetter);
        //if the letter is not found, user has made a wrong guess, and the man inches closer to death.
        if(!found) {
            FAULTS++;
            button.setTextColor(getResources().getColor(R.color.wrong));
        }
        else {
        	LEFT = ArrayListAdapter.getLettersLeft(letters);
        	button.setTextColor(getResources().getColor(R.color.correct));
        }

        //after this we remove and reload all guessed letter-views
        updateWordView(letters);
    }

    //updates all our guessed letters on screen
    private void updateWordView(ArrayList<Letter> letters){
        //after this we remove and reload all guessed letter-views
        letterHolder = (LinearLayout) findViewById(R.id.llHorizontal);
        if(letterHolder.getChildCount() > 0)
            letterHolder.removeAllViews();

        ArrayListAdapter.addViews(this, letters, letterHolder);
    }

    // was our guessed letter part of the word?
    private boolean checkInputLetter(String inputLetter){
        boolean found = false;
        for(Letter letter: letters){
            if(letter.getLetter().equalsIgnoreCase(inputLetter)){
                letter.setVisible(true);
                found = true;
            }
        }
        return found;
    }

    //resets the screen after a game has been completed by the user, or reset-button has been pressed
    private void reset(){
        FAULTS = 0;
        STATE = PLAYING;
        letters = getRandomWord(letters);
        ViewHandler.resetKeyboard(this);
        ViewHandler.hang(this,hangedMan, FAULTS);
        updateWordView(letters);
    }

    // dynamically generates our keyboard based on language/input
    @SuppressLint("InflateParams") 
    private void buttonGenerator(){
        String keyboard = getResources().getString(R.string.keyboard);
        //when buttoncount passes each 10, we switch to another layout
        //this is to avoid having to implement a heavy method of analyzing the size of the screen
        int buttonCount = 0;
        int rowlength = 8;

        for(int i = 0; i < keyboard.length(); i++){
            Button letterButton = ViewHandler.generateButton(this, Character.toString(keyboard.charAt(i)));

            letterButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    executeButtonClick(v);
                }
            });
            LinearLayout addToLayout;

            if(buttonCount < rowlength) addToLayout = (LinearLayout) findViewById(R.id.llTopKeyboard);
            else if(buttonCount < rowlength * 2) addToLayout = (LinearLayout) findViewById(R.id.llMidKeyboard);
            else if(buttonCount < rowlength * 3) addToLayout = (LinearLayout) findViewById(R.id.llmBotKeyboard);
            else addToLayout = (LinearLayout) findViewById(R.id.llBotKeyboard);

            Log.d("ButtonGenerator", buttonCount + " ");
            addToLayout.addView(letterButton);

            //space between buttons, little hack method...
            TextView space = (TextView) LayoutInflater.from(this).inflate(R.layout.keyboard_space, null);
            addToLayout.addView(space);

            buttonCount++;
        }
    }

    //gets words from language.array.words
    private String[] fetchWords(){
    	return getResources().getStringArray(R.array.words);
    }

    /**
     *
     * Legg all test-kode på bunnen
     */

    private ArrayList<Letter> getRandomWord(ArrayList<Letter> al) {
        al = new ArrayList<Letter>();
        String s = wdb.getRandomWord();
        char[] c = s.toCharArray();
        for(int i = 0; i < c.length; i++)
            al.add(new Letter(c[i]+"", false));

        LEFT = ArrayListAdapter.getLettersLeft(al);
        return al;
    }


}
