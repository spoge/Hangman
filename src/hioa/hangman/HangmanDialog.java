package hioa.hangman;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class HangmanDialog {
    /*public static void exitDialog(final Activity a) {
    	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        switch (which){
    	        case DialogInterface.BUTTON_POSITIVE:
    	        	Intent intent = new Intent(Intent.ACTION_MAIN);
    	        	intent.addCategory(Intent.CATEGORY_HOME);
    	        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	        	a.startActivity(intent);
    	        	a.finish();
    	            break;
    	        case DialogInterface.BUTTON_NEGATIVE:
    	            //No button clicked
    	            break;
    	        }
    	    }
    	};
    	AlertDialog.Builder builder = new AlertDialog.Builder(a);
    	builder.setMessage(a.getResources().getString(R.string.exit_question)).setPositiveButton(a.getResources().getString(R.string.yes), dialogClickListener)
    	    .setNegativeButton(a.getResources().getString(R.string.no), dialogClickListener).show();
    }
    
    public static void gameoverDialog(final Hangman h) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(h);
    	builder.setMessage(h.getResources().getString(R.string.finished_message) + "\n" + h.getResources().getString(R.string.display_wins) + " " + h.getWins() + ", " + h.getResources().getString(R.string.display_losses) + " " + h.getLosses())
    	       .setCancelable(false)
    	       .setPositiveButton(h.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                h.resetGame();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
    
    public static void wordDialog(final Hangman h, final boolean win) {
    	String s = "";
    	if(win) s = h.getResources().getString(R.string.correct_message);
    	else s = h.getResources().getString(R.string.wrong_message) + "\n" + h.getResources().getString(R.string.correct_word) + " " + h.printWord();
    	AlertDialog.Builder builder = new AlertDialog.Builder(h);
    	builder.setMessage(s)
    	       .setCancelable(false)
    	       .setPositiveButton(h.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   h.reset(win);
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }*/
    
    /*private void backDialog() { // for when back-button is pressed
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	        switch (which){
	        case DialogInterface.BUTTON_POSITIVE:
	            //Yes button clicked
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	            //No button clicked
	            break;
	        }
	    }
	};

	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
	    .setNegativeButton("No", dialogClickListener).show();
}*/
}
