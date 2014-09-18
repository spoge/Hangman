package hioa.hangman;

/**
 * Created by Sondre 
 * Handles all the AlertDialogs in the game
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class HangmanDialog {

	// yes/no on exit
    public static void exitDialog(Context context, OnClickListener dialoginterface) {
    	DialogInterface.OnClickListener dialogClickListener = dialoginterface;
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setMessage(context.getResources().getString(R.string.exit_question)).setPositiveButton(context.getResources().getString(R.string.yes), dialogClickListener)
    	    .setNegativeButton(context.getResources().getString(R.string.no), dialogClickListener).show();
    }
    
    // after all words have been used
    public static void gameoverDialog(Context context, OnClickListener dialoginterface, int wins, int losses) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setMessage(context.getResources().getString(R.string.finished_message) + "\n" + context.getResources().getString(R.string.display_wins) + " " + wins + ", " + context.getResources().getString(R.string.display_losses) + " " + losses)
    	       .setCancelable(false)
    	       .setPositiveButton(context.getResources().getString(R.string.ok), dialoginterface);
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    // when a word is finished, or man is hanged
    public static void wordDialog(Context context, OnClickListener dialoginterface, String msg) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setMessage(msg)
    	       .setCancelable(false)
    	       .setPositiveButton(context.getResources().getString(R.string.ok), dialoginterface);
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    // when the back-button is pressed
    public static void backDialog(Context context, OnClickListener dialoginterface) {
		DialogInterface.OnClickListener dialogClickListener = dialoginterface;
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setMessage(context.getResources().getString(R.string.back_question)).setPositiveButton(context.getResources().getString(R.string.yes), dialogClickListener)
    	    .setNegativeButton(context.getResources().getString(R.string.no), dialogClickListener).show();
    }
	
}
