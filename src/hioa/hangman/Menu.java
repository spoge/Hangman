package hioa.hangman;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Menu extends Activity {
	
	private Button start, rules, languages, exit;
	public final static String LOCALEKEY = "localeKey";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		//find all buttons for menu, and add actionlisteners
		start = (Button) findViewById(R.id.buttonStart);
		rules = (Button) findViewById(R.id.buttonRules);
		languages = (Button) findViewById(R.id.buttonLanguages);
		exit = (Button) findViewById(R.id.buttonExit);
		setupButton(start);
		setupButton(rules);
		setupButton(languages);
		setupButton(exit);
		getLocale();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, (android.view.Menu) menu);
		return true;
	}
	
	@Override
	public void onRestart(){
		super.onRestart();
		start.setText(R.string.play_button);
		rules.setText(R.string.rules_button);
		languages.setText(R.string.language_button);
		exit.setText(R.string.end_game_button);
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
                i = new Intent(getApplicationContext(), Rules.class);
                break;
        }
        startActivity(i);
    }
    private void getLocale(){
    	SharedPreferences prefs = this.getSharedPreferences("hioa.hangman", Context.MODE_PRIVATE);
    	String languageSetting = prefs.getString(LOCALEKEY, "en");
    	
    	Log.d("Menu.getLocale()", languageSetting);
    	
    	Locale locale = new Locale(languageSetting);
    	Locale.setDefault(locale);
    	Configuration config = new Configuration();
    	config.locale = locale;
    	getResources().updateConfiguration(config, null);
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
