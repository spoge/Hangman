package hioa.hangman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		//find all buttons for menu, and add actionlisteners
		Button start = (Button) findViewById(R.id.buttonStart);
		Button rules = (Button) findViewById(R.id.buttonRules);
		Button languages = (Button) findViewById(R.id.buttonLanguages);
		Button exit = (Button) findViewById(R.id.buttonExit);
		setupButton(start);
		setupButton(rules);
		setupButton(languages);
		setupButton(exit);
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, (android.view.Menu) menu);
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
	
    //starts our new activities. add proper intent to switch-case
    private void startNewActivity(View v){
        Intent i = null;
        switch(v.getId()){
            case R.id.buttonStart:
                i = new Intent(getApplicationContext(), Hangman.class);
                break;
            case R.id.buttonExit:
            	finish();
            	return;
            case R.id.buttonLanguages:
            	i = new Intent(getApplicationContext(), Language.class);
            	break;
            default:
                i = new Intent(getApplicationContext(), Hangman.class);
                break;
        }
        startActivity(i);
    }
    
    //sets the onclick-listener for our button.
    private void setupButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(v);
            }
        });
    }
}
