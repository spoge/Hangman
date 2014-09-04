package hioa.hangman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hioa.hangman.logic.ArrayListAdapter;
import hioa.hangman.logic.ViewHandler;


public class Hangman extends Activity {

    private ArrayList<Letter> letters;
    private LinearLayout letterHolder;
    private ArrayAdapter<Letter> adapter;
    private ImageView hangedMan;

    public static int FAULTS = 0;
    public static int LEFT = -1; // how many letters left til victory

    private final int WON = 1, LOST = -1, PLAYING = 0; // int values represesnting which state the game is in
    private static int STATE = 0; // current state of the game

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);

        // creating hangman-image
        hangedMan = (ImageView) findViewById(R.id.imageView);
        ViewHandler.hang(this, hangedMan, FAULTS);

        //creation of letterboxes
        letters = new ArrayList<Letter>();

        letterHolder = (LinearLayout) findViewById(R.id.llHorizontal);
        letters = getRandomWord(letters);

        //we refernce this adapter later when we want to make changes to the textviews
        adapter = ArrayListAdapter.addViews(this, letters, letterHolder);

        // generates resetbutton
        Button buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset(v);
            }
        });

        //generates keyboard buttons
        buttonGenerator();
    }

    //llama was here
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
        if (id == R.id.action_settings) {
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
        updateTextViews(button.getText().toString());
        view.setEnabled(false);
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
    public void updateTextViews(String inputLetter ){
        boolean found = checkInputLetter(inputLetter);
        //if the letter is not found, user has made a wrong guess, and the man inches closer to death.
        if(!found) {
            FAULTS++;
            //Toast.makeText(this, inputLetter + " er ikke med i ordet.", Toast.LENGTH_SHORT).show();
        }
        else LEFT = ArrayListAdapter.getLettersLeft(letters);

        //after this we remove and reload all guessed letter-views
        updateWordView(letters);
    }

    //updates all our guessed letters on screen
    private void updateWordView(ArrayList<Letter> letters){
        //after this we remove and reload all guessed letter-views
        letterHolder = (LinearLayout) findViewById(R.id.llHorizontal);
        if(letterHolder.getChildCount() > 0)
            letterHolder.removeAllViews();

        adapter = ArrayListAdapter.addViews(this, letters, letterHolder);
    }

    // was our guessed letter part of the word?
    private boolean checkInputLetter(String inputLetter){
        boolean found = false;
        for(Letter letter: letters){
            if(letter.getLetter().equals(inputLetter)){
                letter.setVisible(true);
                found = true;
            }
        }
        return found;
    }

    //resets the screen after a game has been completed by the user, or reset-button has been pressed
    private void reset(View view){
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
        String keyboard = "ABCDEFGHIJKLMNOPQRSTUVWXYZ���";
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


    private void generateWordToArrayList(ArrayList<Letter> al, SQLiteDatabase dictionary){

    }

    /**
     *
     * Legg all test-kode p� bunnen
     */

    private ArrayList<Letter> getRandomWord(ArrayList<Letter> al) {
        al = new ArrayList<Letter>();
        String s = WordDatabase.getRandomWord();
        char[] c = s.toCharArray();
        for(int i = 0; i < c.length; i++)
            al.add(new Letter(c[i]+"", false));

        LEFT = ArrayListAdapter.getLettersLeft(al);
        return al;
    }

    private ArrayList<Letter> getRandomWord(ArrayList<Letter> al, String except) {
        al = new ArrayList<Letter>();
        String s = WordDatabase.getRandomWord(except);
        char[] c = s.toCharArray();
        for(int i = 0; i < c.length; i++)
            al.add(new Letter(c[i]+"", false));

        return al;
    }

    // method to bind the array of letters to our custom adapter
    private ArrayList<Letter> testArrayList(ArrayList<Letter> al){
        al = new ArrayList<Letter>();
        String s = "MOTHERFUCKER";
        char[] c = s.toCharArray();
        for(int i = 0; i < c.length; i++)
            al.add(new Letter(c[i]+"", false));

        return al;
    }

}