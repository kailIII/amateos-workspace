package com.emp.friskyplayer.activities.listeners;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.activities.FriskyPlayerActivity;
import com.emp.friskyplayer.application.FriskyPlayerApplication;
import com.emp.friskyplayer.services.FriskyService;
import com.emp.friskyplayer.utils.PlayerUtils;

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
		
		//int playerState = activity.getPlayerState();
		
		int playerState = ((FriskyPlayerApplication) activity.getApplication()).getInstance().getPlayerState();
		
		if(playerState == PlayerUtils.STATE_STOPPED){
			
			activity.setSupportProgressBarIndeterminateVisibility(true);
			
			activity.startService(new Intent(FriskyService.ACTION_PLAY));
			//activity.setPlayerState(PlayerUtils.STATE_PLAYING);
			playPauseButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_pause));
			
			//ProgressDialog progressDialog = ((FriskyPlayerApplication) activity.getApplication()).getProgressDialogInstance();
			//ProgressDialog.show(activity.getBaseContext(), "Titulo", "Mensaje", true, true);
			/*, new OnCancelListener() {
				public void onCancel(DialogInterface dialogInterface) {
					activity.startService(new Intent(FriskyService.ACTION_STOP));
					playPauseButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_play));
					((TextView) activity.findViewById(R.id.bottom_bar_stream_title_textview)).setText("");
					((FriskyPlayerApplication) activity.getApplication()).getInstance().setStreamTitle("");
					((ProgressBar) activity.findViewById(R.id.bottom_bar_buffer_progressbar)).setProgress(0);
				}
			});
			*/
		}else{
			activity.startService(new Intent(FriskyService.ACTION_STOP));
			playPauseButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_play));
			((TextView) activity.findViewById(R.id.bottom_bar_stream_title_textview)).setText("");
			((FriskyPlayerApplication) activity.getApplication()).getInstance().setStreamTitle("");
			((ProgressBar) activity.findViewById(R.id.bottom_bar_buffer_progressbar)).setProgress(0);
			activity.setSupportProgressBarIndeterminateVisibility(false);
		}
		
	}

	
}
