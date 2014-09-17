package hioa.hangman;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Language extends Activity {

	public final static String LOCALEKEY = "localeKey";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language);
		
		Button nor = (Button) findViewById(R.id.buttonNorwegian);
		Button eng = (Button) findViewById(R.id.buttonEnglish);
		setupButton(nor);
		setupButton(eng);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.language, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_exit) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    //starts our new activities. add proper intent to switch-case
    private void changeLanguage(String lang){
    	Locale locale = new Locale(lang);
    	Locale.setDefault(locale);
    	Configuration config = new Configuration();
    	config.locale = locale;
    	getResources().updateConfiguration(config, null);
    	
    	//saves our locale-preference for future use.
    	SharedPreferences prefs = this.getSharedPreferences("hioa.hangman", Context.MODE_PRIVATE);
    	prefs.edit().putString(LOCALEKEY, lang).apply();
    }
    
    private void languageClick(View v) {  	
        switch(v.getId()){
        case R.id.buttonNorwegian:
        	changeLanguage("no");
        	startActivity(new Intent(this, Language.class));
        	finish();
        	break;
        case R.id.buttonEnglish:
        	changeLanguage("en");
        	startActivity(new Intent(this, Language.class));
        	finish();
        	break;
        }
    }
	
    //sets the onclick-listener for our button.
    private void setupButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	languageClick(v);
            }
        });
    }
}
