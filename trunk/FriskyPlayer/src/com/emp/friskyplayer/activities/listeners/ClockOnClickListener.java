package com.emp.friskyplayer.activities.listeners;

import android.view.View;
import android.widget.ImageButton;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.activities.FriskyPlayerActivity;
import com.emp.friskyplayer.application.FriskyPlayerApplication;

public class ClockOnClickListener extends ButtonOnClickListener{

	private FriskyPlayerActivity activity;
	private ImageButton clockButton;
	
	public ClockOnClickListener(FriskyPlayerActivity activity, ImageButton button) {
		super(button);
		this.activity = activity;
		this.clockButton = (ImageButton) activity.findViewById(R.id.FriskyPlayerActivity_clockImageButton);
	}
	

	@Override
	public void onClick(View view) {
		super.onClick(view);
		
		clockButton.setVisibility(View.INVISIBLE);
		((FriskyPlayerApplication) activity.getApplication()).getInstance().disableSleepMode();
		
	}

	
}
