package com.emp.friskyplayer.activities;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.services.FriskyService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FriskyPlayerActivity extends Activity {
    
    Button mPlayButton;
    Button mStopButton;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mPlayButton = (Button) findViewById(R.id.playbutton);
        mPlayButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
	            startService(new Intent(FriskyService.ACTION_PLAY));
			}
		});
        
        mStopButton = (Button) findViewById(R.id.stopbutton);
        mStopButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
	            startService(new Intent(FriskyService.ACTION_STOP));
			}
		});
    }
}