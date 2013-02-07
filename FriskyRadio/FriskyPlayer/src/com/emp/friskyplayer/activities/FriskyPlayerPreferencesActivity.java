package com.emp.friskyplayer.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.text.Html;
import android.util.Log;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.emp.friskyplayer.R;
import com.emp.friskyplayer.utils.PreferencesConstants;

public class FriskyPlayerPreferencesActivity extends SherlockPreferenceActivity
		implements SharedPreferences.OnSharedPreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		
		String text = "<font color='#d84395'>frisky</font><font color='#cfcdce'>Player</font>";
		getSupportActionBar().setTitle(Html.fromHtml(text));
		
		SharedPreferences sharedPrefs = getPreferenceManager()
				.getSharedPreferences();
		sharedPrefs.registerOnSharedPreferenceChangeListener(this);

	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPrefs,
			String key) {
		if (key.equals(PreferencesConstants.SLEEP_MODE)) {

		}
	}
}
