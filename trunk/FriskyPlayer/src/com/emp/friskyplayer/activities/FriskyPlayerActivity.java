package com.emp.friskyplayer.activities;

import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.emp.friskyplayer.R;
import com.emp.friskyplayer.activities.listeners.PlayButtonOnClickListener;
import com.emp.friskyplayer.application.FriskyPlayerApplication;
import com.emp.friskyplayer.receivers.ServiceToGuiCommunicationReceiver;
import com.emp.friskyplayer.utils.PlayerUtils;

public class FriskyPlayerActivity extends SherlockActivity {
    
	
    private ImageButton playPauseButton;
    private ImageButton shareButton;
    private TextView streamTitleTextView;
    private ActionBar ab;
    private ProgressBar bufferProgressBar;

    private ServiceToGuiCommunicationReceiver serviceToGuiReceiver;
    
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d("App","onCreate ACTIVITY");
    }
    
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.d("App","onResume ACTIVITY");
		
		setContentView(R.layout.main);
        
        initActionBar();
        initGuiElements();
        
        serviceToGuiReceiver = new ServiceToGuiCommunicationReceiver(this);
	}




    @Override
	protected void onPause() {
		super.onPause();
		
		// Unregister receiver and release resources
		serviceToGuiReceiver.unregisterReceiver();
		serviceToGuiReceiver = null;
	}

	/**
     * Initiates ActionBar and sets title
     */
	private void initActionBar(){
    	ab = getSupportActionBar();
        String text = "<font color='#d84395'>frisky</font><font color='#cfcdce'>Player</font>";
        ab.setTitle(Html.fromHtml(text));
    }
    
    /**
     * Initiates elements of GUI and sets listeners
     */
    private void initGuiElements(){ 

    	// Gets state and stream title from Application object
    	int playerState = ((FriskyPlayerApplication) getApplication()).getInstance().getPlayerState();
    	String streamTitle = ((FriskyPlayerApplication) getApplication()).getInstance().getStreamTitle();
    	
    	playPauseButton = (ImageButton) findViewById(R.id.bottom_bar_play_button);
    	shareButton = (ImageButton) findViewById(R.id.bottom_bar_share_button);
    	
    	// Sets button image based on player state
		if(playerState == PlayerUtils.STATE_STOPPED){
			playPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
		}else{
			playPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
		}
    	
    	playPauseButton.setOnClickListener(new PlayButtonOnClickListener(this, playPauseButton));
    	
    	shareButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent i = createShareIntent();
				startActivity(Intent.createChooser(i, getResources().getString(R.string.share_question)));
			}
		});
    	
    	
    	streamTitleTextView = (TextView) findViewById(R.id.bottom_bar_stream_title_textview);
    	streamTitleTextView.setMovementMethod(new ScrollingMovementMethod());
    	streamTitleTextView.setSelected(true);
    	streamTitleTextView.setText(streamTitle);
    	
    	bufferProgressBar = (ProgressBar) findViewById(R.id.bottom_bar_buffer_progressbar);
    	bufferProgressBar.setMax(100);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	getSupportMenuInflater().inflate(R.menu.menu_share, menu);

        // Set file with share history to the provider and set the share intent.
        MenuItem actionItem = menu.findItem(R.id.menu_share);
      
        /*ShareActionProvider actionProvider = (ShareActionProvider) actionItem.getActionProvider();
        actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        actionProvider.setShareIntent(createShareIntent());
*/
        return true;
    	
        //Used to put dark icons on light action bar

       /* menu.add("Save")
            .setIcon(R.drawable.ic_compose)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menu.add("Refresh")
            .setIcon(R.drawable.ic_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		*/
    }

    @Override
   	public void onConfigurationChanged(Configuration newConfig) {
   		super.onConfigurationChanged(newConfig);
   	}
    
    
    /**
     * Creates an Intent to share the streaming using an external app such as: Twitter, Facebook, Gmail...
     */
    private Intent createShareIntent() {
    	
    	String streamTitle = ((FriskyPlayerApplication) getApplication()).getInstance().getStreamTitle();
    	
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide what to do with it.
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_subject));
        
     // Gets state and stream title from Application object
    	int playerState = ((FriskyPlayerApplication) getApplication()).getInstance().getPlayerState();
        if(playerState == PlayerUtils.STATE_PLAYING){
        	shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_text_start) + " "+ streamTitle + " " + getResources().getString(R.string.share_text_end));
        }else{
        	shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_text_start) + " FriskyRadio " + getResources().getString(R.string.share_text_end));
        }
        return shareIntent;
    }
    
    /**
     * Sets StreamTitleTextView text
     * @param streamTitle
     */
    public void setStreamTitle(String streamTitle) {
    	streamTitleTextView.setText(streamTitle);
	}
    
}