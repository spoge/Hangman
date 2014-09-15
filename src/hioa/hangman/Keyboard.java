package hioa.hangman;

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

	public void update(String letter, int state) {
		for (Letter l : keyboard) {
			if (l.toString().equals(letter))
				l.setState(state);
		}
	}

	public void update(int[] keys) {
		int i = 0;
		for (Letter l : keyboard) {
			l.setState(keys[i]);
			i++;
		}
	}
	
	public int[] getState(){
		int[] keystate = new int[keyboard.size()];
		for(int i = 0; i < keystate.length; i++){
			keystate[i] = keyboard.get(i).getState();
		}
		return keystate;
	}
	
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
