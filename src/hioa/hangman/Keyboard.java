package hioa.hangman;

/**
 * Created by Sondre on 15.09.2014.
 * Class represents the virtual keyboard on the Hangman-screen,
 * and is used when state of the keyboard.
 * 
 * list<Letter> represents the state of each keyboard-button.
 * list<Button> references the actual keyboard-buttons, and is used
 * when we change the state of the actual buttons.
 */

import java.util.ArrayList;

import android.content.Context;
import android.widget.Button;

public class Keyboard {
	private ArrayList<Letter> keyboard = new ArrayList<Letter>();
	private ArrayList<Button> buttons = new ArrayList<Button>();
	
	public Keyboard(String keys) {
		char[] c = keys.toCharArray();
		for (int i = 0; i < c.length; i++)
			keyboard.add(new Letter(c[i] + "", 0));
	}

	// updates state of a keyboard-button, for instance if change text-color
	public void update(String letter, int state) {
		for (Letter l : keyboard) {
			if (l.toString().equals(letter))
				l.setState(state);
		}
	}

	// updates the state of each keyboard-letter, based on input
	public void update(int[] keys) {
		int i = 0;
		for (Letter l : keyboard) {
			l.setState(keys[i]);
			i++;
		}
	}
	
	// return state of keyboard-buttons
	public int[] getState(){
		int[] keystate = new int[keyboard.size()];
		for(int i = 0; i < keystate.length; i++){
			keystate[i] = keyboard.get(i).getState();
		}
		return keystate;
	}
	
	// updates and changes state on the whole keyboard
	public void update(Context context) {
		for(int i = 0; i < keyboard.size(); i++) {
			int state = keyboard.get(i).getState();
			Button button = buttons.get(i);
			if(state == 0) {
				button.setTextColor(context.getResources().getColor(R.color.black));
				button.setEnabled(true);
			}
			else if(state == 1) {
				button.setTextColor(context.getResources().getColor(R.color.correct));
				button.setEnabled(false);
			}
			else {
				button.setTextColor(context.getResources().getColor(R.color.wrong));
				button.setEnabled(false);
			}
		}
	}
	
	// resets keyboard to default state
	public void reset(Context context) {
		for(int i = 0; i < keyboard.size(); i++) {
			keyboard.get(i).setState(0);
			Button button = buttons.get(i);
			button.setTextColor(context.getResources().getColor(R.color.black));
			button.setEnabled(true);
		}
	}

	public Letter getLetter(int i) {
		return keyboard.get(i);
	}
	
	public void addButton(Button button) {
		buttons.add(button);
	}
}
