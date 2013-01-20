package com.emp.friskyplayer.utils;

public class PlayerConstants {

	
	// Player states
	public static final int STATE_STOPPED = 0; 
	public static final int STATE_LOADING = 1;
	public static final int STATE_PLAYING = 2;
	
	// Pause reasons
	public static final int USER_REQUEST = 0;
	public static final int FOCUS_LOSS = 1;
	
	// Audio focus
	public static final int NO_FOCUS_NO_DUCK = 0;
	public static final int NO_FOCUS_CAN_DUCK = 1;
	public static final int FOCUSED = 2;
}
