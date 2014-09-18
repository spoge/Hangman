package hioa.hangman.logic;

import hioa.hangman.GameLetter;
import hioa.hangman.Letter;
import hioa.hangman.R;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by NegatioN on 19.08.2014.
 * Class takes in a letter and adds it to the viewgroup
 * this is the way we generate the letterboxes under the stick-figure
 */


public class ArrayListAdapter {

    public static ArrayAdapter<GameLetter> addViews(Context c, ArrayList<GameLetter> letters, LinearLayout letterHolder){
        ArrayAdapter<GameLetter> adapter = new ArrayAdapter<GameLetter>(c, R.layout.tv_layout, letters);
        final int adapterCount = adapter.getCount();

        // makes every single textview here
        for(int i = 0; i < adapterCount;i++){
            TextView textView = (TextView) adapter.getView(i, null, null);
            GameLetter letter = letters.get(i);
            
            //if the character is a whitepace, automatically set to visible and removes underline
            if(letter.toString().matches(" ")){
            	letters.get(i).setVisible(true);
            	textView.setBackgroundColor(Color.TRANSPARENT);
            }
            
            textView.setText(letter.toString());
            //hides the letter if it has still not been pressed
            //highlights the ones that has been correctly been pressed
            if(letter.isVisible()){
                textView.setTextColor(Color.WHITE);
            }else {
                textView.setTextColor(Color.TRANSPARENT);
            }

            letterHolder.addView(textView);
        }
        return adapter;
    }

    // returns letters left to be guessed
    public static int getLettersLeft(ArrayList<GameLetter> letters) {
        int count = 0;
        for(GameLetter letter : letters)
            if(!letter.isVisible()) count++;
        return count;
    }
}
