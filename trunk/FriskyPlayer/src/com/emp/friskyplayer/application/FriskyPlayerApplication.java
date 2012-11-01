package com.emp.friskyplayer.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.emp.friskyplayer.utils.PlayerUtils;

public class FriskyPlayerApplication extends Application {
	
	
	private FriskyPlayerApplication application;
	private SharedPreferences mPrefs;
	private int playerState;
	private String streamTitle;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.d("App","onCreate APPLICATION");
		
		application = this;
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		// Sets playerState to STOPPED by default
		playerState = PlayerUtils.STATE_STOPPED;
		
		// Sets streamTitle to "" by default
		streamTitle = "";
	}

	
	public FriskyPlayerApplication getInstance() {
		return application;
	}
	
	
	public int getPlayerState() {
		return playerState;
	}

	public void setPlayerState(int playerState) {
		this.playerState = playerState;
	}

	public String getStreamTitle() {
		return streamTitle;
	}

	public void setStreamTitle(String streamTitle) {
		this.streamTitle = streamTitle;
	}
	
}
