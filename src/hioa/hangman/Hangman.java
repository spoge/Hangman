package hioa.hangman;

import hioa.hangman.logic.ArrayListAdapter;
import hioa.hangman.logic.GameLogic;
import hioa.hangman.logic.ViewHandler;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
    private GameLogic gl;
    private TextView wins, losses;
    
    private Keyboard keyboard;
    
    public static int FAULTS = 0;
    public static int LEFT = -1; // how many letters left til victory

    private final int WON = 1, LOST = -1, PLAYING = 0; // int values represesnting which state the game is in
    private static int STATE = 0; // current state of the game
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);
        
        //creates our database with words from the selected language-option
        wdb = new WordDatabase(fetchWords());
        
        //starts our gamelogic at 0 wins 0 losses, can be modified to store in sharedpreferences
        gl = new GameLogic(0,0);
        
        // creating hangman-image
        hangedMan = (ImageView) findViewById(R.id.imageView);
        ViewHandler.hang(this, hangedMan, FAULTS);

        //creation of the underlined boxes for the letters on screen
        letters = new ArrayList<Letter>();

        letterHolder = (LinearLayout) findViewById(R.id.llHorizontal);
        letters = getRandomWord(letters);

        //we refernce this adapter later when we want to make changes to the textviews
        ArrayListAdapter.addViews(this, letters, letterHolder);
        
        // the keyboard itself, easier to configure
        keyboard = new Keyboard(getResources().getString(R.string.keyboard));
        
        // generates keyboard buttons
        buttonGenerator();
        
        wins = (TextView) findViewById(R.id.tvWins);
        losses = (TextView) findViewById(R.id.tvLosses);
        
        FAULTS = 0;
    	ViewHandler.hang(this, hangedMan, FAULTS);
    }
    
    protected void onSaveInstanceState (Bundle outState) {
    	outState.putInt("faults", FAULTS);
    	outState.putInt("left", LEFT);
    	outState.putInt("wins", gl.getWins());
    	outState.putInt("losses", gl.getLosses());

    	char[] c = new char[letters.size()];
    	for(int i = 0; i < c.length; i++) c[i] = letters.get(i).getCharLetter();
    	outState.putCharArray("letters", c);
    	
    	boolean[] visible = new boolean[letters.size()];
    	for(int i = 0; i < visible.length; i++) visible[i] = letters.get(i).isVisible();
    	outState.putBooleanArray("visible", visible);
    	
    	outState.putIntArray("keyboard", keyboard.getState());
    	
        super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        
        FAULTS = savedInstanceState.getInt("faults");
        LEFT = savedInstanceState.getInt("left");
        int w = savedInstanceState.getInt("wins");
        int l = savedInstanceState.getInt("losses");
        char[] c = savedInstanceState.getCharArray("letters");
        boolean[] visible = savedInstanceState.getBooleanArray("visible");
        int[] keys = savedInstanceState.getIntArray("keyboard");
        
        gl = new GameLogic(w, l);

        letters = new ArrayList<Letter>();
        for(int i = 0; i < c.length; i++) letters.add(new Letter(c[i]+"", visible[i]));
        
        //keyboard = new Keyboard(getResources().getString(R.string.keyboard));
        keyboard.update(keys);
        
        updateUI();
    }
    
    public void updateUI() {
    	String langWin = getResources().getString(R.string.display_wins);
    	wins.setText(langWin + " " + gl.getWins());
    	String langLoss = getResources().getString(R.string.display_losses);
    	losses.setText(langLoss + " " + gl.getLosses());
    	
    	hangedMan = (ImageView) findViewById(R.id.imageView);
    	ViewHandler.hang(this, hangedMan, FAULTS);
        
        updateWordView(letters);
        
        keyboard.update(this);
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
        else if(id == R.id.action_exit){
        	Intent intent = new Intent(Intent.ACTION_MAIN);
        	intent.addCategory(Intent.CATEGORY_HOME);
        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(intent);
        	finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // method executed in onClick of keyboard-buttons
    public void executeButtonClick(View view) {
        if(STATE != 0) {
            //Toast.makeText(this, getResources().getString(R.string.reset_message), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, getResources().getString(R.string.correct_word) + " " + printWord(), Toast.LENGTH_SHORT).show();
            return;
        }

        Button button = (Button) view;
        view.setEnabled(false);
        updateTextViews(button.getText().toString(), button);
        ViewHandler.hang(this, hangedMan, FAULTS);
        
        STATE = checkState();
        
        boolean win;
        
        if(STATE == WON){
        	win = true;
        	Toast.makeText(this, getResources().getString(R.string.win_message), Toast.LENGTH_SHORT).show();
        	//gets language-spesific text and updates number of wins
        	String langWin = getResources().getString(R.string.display_wins);
        	wins.setText(langWin + " " + gl.updateWinLoss(win));
        }
        else if(STATE == LOST){ 
        	win = false;
        	Toast.makeText(this, getResources().getString(R.string.lose_message), Toast.LENGTH_SHORT).show();
        	//gets language-spesific text and updates number of losses
        	String langLoss = getResources().getString(R.string.display_losses);
        	losses.setText(langLoss + " " + gl.updateWinLoss(win));
        }
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
            keyboard.update(inputLetter, LOST);
        }
        else {
        	LEFT = ArrayListAdapter.getLettersLeft(letters);
        	button.setTextColor(getResources().getColor(R.color.correct));
        	keyboard.update(inputLetter, WON);
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
            if(letter.toString().equalsIgnoreCase(inputLetter)){
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
        //finds new words until we run out.
        try{
        letters = getRandomWord(letters);
        }catch(NoSuchElementException e){
        	e.printStackTrace();
        	Toast.makeText(this, "GAME OVER WORDS OUT", Toast.LENGTH_LONG).show();
        }
        //ViewHandler.resetKeyboard(this, keyboard.getState());
        keyboard.reset(this);
        ViewHandler.hang(this, hangedMan, FAULTS);
        updateWordView(letters);
    }

    // dynamically generates our keyboard based on language/input
    @SuppressLint("InflateParams") 
    private void buttonGenerator(){
        String keyboardString = getResources().getString(R.string.keyboard);
        //when buttoncount passes each 10, we switch to another layout
        //this is to avoid having to implement a heavy method of analyzing the size of the screen
        int buttonCount = 0;
        int rowlength = 8;
        //keyboard.buttons.clear();
        
        for(int i = 0; i < keyboardString.length(); i++){
            Button letterButton = ViewHandler.generateButton(this, Character.toString(keyboardString.charAt(i)));
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
            keyboard.addButton(letterButton);

            //inflates empty space between buttons
            TextView space = (TextView) LayoutInflater.from(this).inflate(R.layout.keyboard_space, null);
            addToLayout.addView(space);

            buttonCount++;
        }
    }
    
    public void updateKeyboard() {
    	keyboard.update(this);
    }

    private String printWord() {
    	String out = "";
    	for(Letter l : letters)
    		out += l.toString();
    	return out;
    }
    
    //gets words from language.array.words
    private String[] fetchWords(){
    	return getResources().getStringArray(R.array.words);
    }
    
    //connects to our word-db and gets a random word, updates variables.
    private ArrayList<Letter> getRandomWord(ArrayList<Letter> al) {
        al = new ArrayList<Letter>();
        String s = wdb.getRandomWord();
        //convert to Letter, and add to arraylist that shows up on screen.
        char[] c = s.toCharArray();
        for(int i = 0; i < c.length; i++)
            al.add(new Letter(c[i]+"", false));

        LEFT = ArrayListAdapter.getLettersLeft(al);
        return al;
    }


}
