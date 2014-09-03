package hioa.hangman.logic;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import hioa.hangman.R;

/**
 * Created by NegatioN on 20.08.2014.
 */
public class ViewHandler {

    public static Button generateButton(Context context, String textValue){
        Button a = (Button) LayoutInflater.from(context).inflate(R.layout.keyboard_button, null);
        a.setText(textValue);
        return a;
    }

    // updates image of hangman according to number of errors commited
    public static void hang(Context context, ImageView hangman, int faults){
        int id = context.getResources().getIdentifier("hangman_"+faults, "drawable", context.getPackageName());
        if(faults < 10) hangman.setImageResource(id);
        else hangman.setImageResource(R.drawable.hangman_10);
    }

    //eneable all buttons contained in the keyboard-layouts
    public static void resetKeyboard(Activity activity){
        LinearLayout layout = (LinearLayout) activity.findViewById(R.id.llTopKeyboard);
        resetLayoutChildViews(layout);
        layout = (LinearLayout) activity.findViewById(R.id.llMidKeyboard);
        resetLayoutChildViews(layout);
        layout = (LinearLayout) activity.findViewById(R.id.llmBotKeyboard);
        resetLayoutChildViews(layout);
        layout = (LinearLayout) activity.findViewById(R.id.llBotKeyboard);
        resetLayoutChildViews(layout);
    }
    //enable buttons for one layout
    private static void resetLayoutChildViews(LinearLayout layout){
        int childCount = layout.getChildCount();

        for(int i = 0; i < childCount; i++){
            View v = layout.getChildAt(i);
            v.setEnabled(true);
        }
    }
}
