package hioa.hangman;

import hioa.hangman.logic.ArrayListAdapter;
import hioa.hangman.logic.GameLogic;
import hioa.hangman.logic.ViewHandler;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
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


/**
 * 
 * @author Joakim
 *This is the main game-class.
 * it's a sort of connection-point for everything else in the system, and also contains various methods for UI
 * and handling of user-actions, in addition to some game-logic.
 */

public class Hangman extends Activity {

    private static ArrayList<GameLetter> letters;
    private LinearLayout letterHolder;
    private ImageView hangedMan;
    private static WordDatabase wdb;
    private static GameLogic gl;
    private TextView wins, losses;
    private MediaPlayer mpCorrect, mpWrong;
    
    private Keyboard keyboard;
    
    public static final int LIMIT = 7;
    public static int FAULTS = 0;
    public static int LEFT = -1; // how many letters left til victory

    private final int WON = 1, LOST = -1, PLAYING = 0; // int values represesnting which state the game is in
    private static int STATE = 0; // current state of the game
    private static boolean firstLoad = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);
        
        //a zone of the onCreate which should not be called when the screen is flipped
        if(firstLoad){
        	
	        //creates our database with words from the selected language-option
	        wdb = new WordDatabase(fetchWords());
	        
	        //starts our gamelogic at 0 wins 0 losses, can be modified to store in sharedpreferences
	        gl = new GameLogic(0,0);
	
	        //creation of the underlined boxes for the letters on screen
	        letters = new ArrayList<GameLetter>();
	
	        letters = getRandomWord(letters);
	        FAULTS = 0;
	        firstLoad = false;
        }
        
    	//mediaplayers for onclicklistener
    	mpCorrect = MediaPlayer.create(this, R.raw.correct_guess);
    	mpWrong = MediaPlayer.create(this, R.raw.wrong_guess);
    	mpCorrect.setVolume(0.3f, 0.3f);
    	mpWrong.setVolume(0.3f, 0.3f);
    	
        // creating hangman-image
        hangedMan = (ImageView) findViewById(R.id.imageView);
        ViewHandler.hang(this, hangedMan, FAULTS);
        
        letterHolder = (LinearLayout) findViewById(R.id.llHorizontal);
        //we refernce this adapter later when we want to make changes to the textviews
        ArrayListAdapter.addViews(this, letters, letterHolder);
        
        // the keyboard itself, easier to configure
        keyboard = new Keyboard(getResources().getString(R.string.keyboard));
        
        // generates keyboard buttons
        buttonGenerator();
        
        wins = (TextView) findViewById(R.id.tvWins);
        losses = (TextView) findViewById(R.id.tvLosses);
		wins.setText(getResources().getString(R.string.display_wins) + " " + gl.getWins());
		losses.setText(getResources().getString(R.string.display_losses) + " " + gl.getLosses());
        
    	ViewHandler.hang(this, hangedMan, FAULTS);
    }
    
    
    //if user presses back we want the game to load from scratch next time.
    @Override
	public void onBackPressed() {
		backDialog();
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

        letters = new ArrayList<GameLetter>();
        for(int i = 0; i < c.length; i++) letters.add(new GameLetter(c[i]+"", visible[i]));
        
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
        case R.id.action_refresh:
        	String s = getResources().getString(R.string.wrong_message) + "\n" + getResources().getString(R.string.correct_word) + " " + printWord();
        	wordDialog(s);
        	updateWin(false);
        	break;
        case R.id.action_exit:
        	exitDialog();
        	break;
        case android.R.id.home:
        	backDialog();
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    // method executed in onClick of keyboard-buttons
    public void executeButtonClick(View view) {
        if(STATE != 0) return;

        Button button = (Button) view;
        view.setEnabled(false);
        updateTextViews(button.getText().toString(), button);
        ViewHandler.hang(this, hangedMan, FAULTS);
        
        STATE = checkState();
        
        if(STATE == WON) {
        	String s = getResources().getString(R.string.correct_message);
        	wordDialog(s);
        	updateWin(true);
        }
        else if(STATE == LOST) {
        	String s = getResources().getString(R.string.wrong_message) + "\n" + getResources().getString(R.string.correct_word) + " " + printWord();
        	wordDialog(s);
        	updateWin(false);
        }
    }
    
    // checks the game-state (win/lose)
    public int checkState(){
        if(FAULTS >= LIMIT) return LOST;
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
            mpWrong.start();
        }
        else {
        	LEFT = ArrayListAdapter.getLettersLeft(letters);
        	button.setTextColor(getResources().getColor(R.color.correct));
        	keyboard.update(inputLetter, WON);
        	mpCorrect.start();
        }

        //after this we remove and reload all guessed letter-views
        updateWordView(letters);
    }

    //updates all our guessed letters on screen
    private void updateWordView(ArrayList<GameLetter> letters){
        //after this we remove and reload all guessed letter-views
        letterHolder = (LinearLayout) findViewById(R.id.llHorizontal);
        if(letterHolder.getChildCount() > 0)
            letterHolder.removeAllViews();

        ArrayListAdapter.addViews(this, letters, letterHolder);
    }

    // was our guessed letter part of the word?
    private boolean checkInputLetter(String inputLetter){
        boolean found = false;
        for(GameLetter letter: letters){
            if(letter.toString().equalsIgnoreCase(inputLetter)){
                letter.setVisible(true);
                found = true;
            }
        }
        return found;
    }

	public void resetGame() {
		wdb = new WordDatabase(fetchWords());
		gl = new GameLogic(0, 0);
		wins.setText(getResources().getString(R.string.display_wins) + " " + gl.getWins());
		losses.setText(getResources().getString(R.string.display_losses) + " " + gl.getLosses());
		reset();
	}
    
	public void reset() {
        FAULTS = 0;
        STATE = PLAYING;
        
        //finds new words until we run out.
        try{
        letters = getRandomWord(letters);
        }catch(Exception e){
        	e.printStackTrace();
        	gameoverDialog();
        }
        keyboard.reset(this);
        ViewHandler.hang(this, hangedMan, FAULTS);
        updateWordView(letters);
	}
	
    //resets the screen after a game has been completed by the user, or reset-button has been pressed
    public void reset(boolean win){
        updateWin(win);
        reset();
    }
    
    private void updateWin(boolean win) {
        String winloss;
        if(win){
        	winloss = getResources().getString(R.string.display_wins);
        	wins.setText(winloss + " " + gl.updateWinLoss(win));
        }
        else{
        	winloss = getResources().getString(R.string.display_losses);
        	losses.setText(winloss + " " + gl.updateWinLoss(win));
        }
    }

    
    // dynamically generates our keyboard based on language/input
    @SuppressLint("InflateParams") 
    private void buttonGenerator(){
        String keyboardString = getResources().getString(R.string.keyboard);
        //when buttoncount passes each 8, we switch to another layout
        //this is to avoid having to implement a heavy method of analyzing the size of the screen
        int buttonCount = 0;
        int rowlength = 8;
        //keyboard.buttons.clear();
        
        for(int i = 0; i < keyboardString.length(); i++){
            Button letterButton = ViewHandler.generateButton(this, Character.toString(keyboardString.charAt(i)));
            
            //disable sound, to make room for our own effects on button
            letterButton.setSoundEffectsEnabled(false);
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

    //used to display the current word in dialogs
    String printWord() {
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
    private ArrayList<GameLetter> getRandomWord(ArrayList<GameLetter> al) {
        al = new ArrayList<GameLetter>();
        String s = wdb.getRandomWord();
        //convert to Letter, and add to arraylist that shows up on screen.
        char[] c = s.toCharArray();
        for(int i = 0; i < c.length; i++)
            al.add(new GameLetter(c[i]+"", false));

        LEFT = ArrayListAdapter.getLettersLeft(al);
        return al;
    }

    // asks whether user want to leave an unfinished game
    private void backDialog() {
    	// skip dialog if no stats are recorded and no game have begun
    	if(gl.getWins() == 0 && gl.getLosses() == 0 && FAULTS == 0 && LEFT == letters.size()) {
    		firstLoad = true;
    		finish();
    		return;
    	}
    	
    	OnClickListener dialoginterface = new DialogInterface.OnClickListener() {
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        switch (which){
    	        case DialogInterface.BUTTON_POSITIVE:
    	        	firstLoad = true;
    	            finish();
    	        }
    	    }
    	};
    	HangmanDialog.backDialog(this, dialoginterface);
    }
    
    private void exitDialog() {
    	OnClickListener dialoginterface = new DialogInterface.OnClickListener() {
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        switch (which){
    	        case DialogInterface.BUTTON_POSITIVE:
    	        	Intent intent = new Intent(Intent.ACTION_MAIN);
    	        	intent.addCategory(Intent.CATEGORY_HOME);
    	        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	        	startActivity(intent);
    	        	finish();
    	        }
    	    }
    	};
    	HangmanDialog.exitDialog(this, dialoginterface);
    }
    
    private void wordDialog(String s) {
    	OnClickListener dialoginterface = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				reset();
			}
		};
		HangmanDialog.wordDialog(this, dialoginterface, s);
    }
    
    private void gameoverDialog() {
    	OnClickListener dialoginterface = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				resetGame();
			}
		};
    	HangmanDialog.gameoverDialog(this, dialoginterface, gl.getWins(), gl.getLosses());
    }
}
