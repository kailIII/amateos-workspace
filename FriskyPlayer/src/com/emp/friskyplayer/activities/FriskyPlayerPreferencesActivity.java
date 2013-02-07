package com.emp.friskyplayer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Html;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.emp.friskyplayer.R;
import com.emp.friskyplayer.application.FriskyPlayerApplication;
import com.emp.friskyplayer.utils.PreferencesConstants;
import com.emp.friskyplayer.utils.ServiceActionConstants;

public class FriskyPlayerPreferencesActivity extends SherlockPreferenceActivity
		implements SharedPreferences.OnSharedPreferenceChangeListener {
	
	private boolean sleepMode;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		
		String text = "<font color='#d84395'>frisky</font><font color='#cfcdce'>Player</font>";
		getSupportActionBar().setTitle(Html.fromHtml(text));
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		SharedPreferences sharedPrefs = getPreferenceManager()
				.getSharedPreferences();
		sharedPrefs.registerOnSharedPreferenceChangeListener(this);
		
		sleepMode = sharedPrefs.getBoolean(PreferencesConstants.SLEEP_MODE, false);

	}

	@Override
	protected void onStop() {
		super.onStop();
		
		if(sleepMode){
			// Enable sleep mode
			((FriskyPlayerApplication) getApplication()).getInstance().enableSleepMode();
		}
		
	}



	public void onSharedPreferenceChanged(SharedPreferences sharedPrefs,
			String key) {
		if (key.equals(PreferencesConstants.SLEEP_MODE)) {

			sleepMode = sharedPrefs.getBoolean(PreferencesConstants.SLEEP_MODE, false);
			
			if(!sleepMode){
				// Stops sleep mode
				((FriskyPlayerApplication) getApplication()).getInstance().disableSleepMode();
			}
		}else if(key.equals(PreferencesConstants.SLEEP_MODE_TIMEOUT)){
			sleepMode = sharedPrefs.getBoolean(PreferencesConstants.SLEEP_MODE, false);
			if(sleepMode){
				// Restarts countdown using new value
				((FriskyPlayerApplication) getApplication()).getInstance().disableSleepMode();
				((FriskyPlayerApplication) getApplication()).getInstance().enableSleepMode();
			}
		}
		else if (key.equals(PreferencesConstants.QUALITY)) {
			
			((FriskyPlayerApplication) getApplication()).getInstance().onQualityPrefChange();
			
			String quality = sharedPrefs.getString(PreferencesConstants.QUALITY, PreferencesConstants.QUALITY_HQ);

			Editor editor = sharedPrefs.edit();
			if(quality.equals(PreferencesConstants.QUALITY_HQ)){
				editor.putString(PreferencesConstants.STREAMING_URL,PreferencesConstants.STREAMING_URL_HQ);
			}if(quality.equals(PreferencesConstants.QUALITY_LQ)){
				editor.putString(PreferencesConstants.STREAMING_URL,PreferencesConstants.STREAMING_URL_LQ);
			}
			editor.commit();
			
			//Notifies service
			getApplicationContext().startService(new Intent(ServiceActionConstants.ACTION_CHANGE_QUALITY));
		}
	}
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
	         finish();
	        break;

	    }

	    return true;
	}
	
	
}
