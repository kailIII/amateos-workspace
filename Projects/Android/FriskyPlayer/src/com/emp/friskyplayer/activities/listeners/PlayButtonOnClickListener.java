package com.emp.friskyplayer.activities.listeners;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.activities.FriskyPlayerActivity;
import com.emp.friskyplayer.application.FriskyPlayerApplication;
import com.emp.friskyplayer.utils.ConnectionUtils;
import com.emp.friskyplayer.utils.PlayerConstants;

public class PlayButtonOnClickListener extends ButtonOnClickListener{

	private FriskyPlayerActivity activity;
	private ImageButton playPauseButton;
	
	public PlayButtonOnClickListener(FriskyPlayerActivity activity, ImageButton button) {
		super(button);
		this.activity = activity;
		this.playPauseButton = (ImageButton) activity.findViewById(R.id.bottom_bar_play_button);
	}
	

	@Override
	public void onClick(View view) {
		super.onClick(view);
		
		int playerState = ((FriskyPlayerApplication) activity.getApplication()).getInstance().getPlayerState();
		
		if(playerState == PlayerConstants.STATE_STOPPED){
			
			//Check Internet connection
			if(ConnectionUtils.haveNetworkConnection(activity.getBaseContext())){
				activity.setSupportProgressBarIndeterminateVisibility(true);
				activity.getFriskyService().processPlayRequest();
				playPauseButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_pause));
			}else{
				Toast.makeText(activity.getApplicationContext(),
	                    activity.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
			}
			
			
		}else{
			activity.getFriskyService().processStopRequest();
			playPauseButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_play));
			((TextView) activity.findViewById(R.id.bottom_bar_stream_title_textview)).setText("");
			((FriskyPlayerApplication) activity.getApplication()).getInstance().setStreamTitle("");
			((ProgressBar) activity.findViewById(R.id.bottom_bar_buffer_progressbar)).setProgress(0);
			activity.setSupportProgressBarIndeterminateVisibility(false);
		}
		
	}

	
}
