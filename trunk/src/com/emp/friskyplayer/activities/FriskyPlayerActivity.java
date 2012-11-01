package com.emp.friskyplayer.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.emp.friskyplayer.R;
import com.emp.friskyplayer.activities.listeners.PlayButtonOnClickListener;
import com.emp.friskyplayer.activities.listeners.PlayerStateListener;
import com.emp.friskyplayer.application.FriskyPlayerApplication;
import com.emp.friskyplayer.receivers.ServiceToGuiCommunicationReceiver;
import com.emp.friskyplayer.services.FriskyService;
import com.emp.friskyplayer.utils.PlayerUtils;

public class FriskyPlayerActivity extends SherlockActivity {
    
	
    private ImageButton playPauseButton;
    private TextView streamTitleTextView;
    private ActionBar ab;

	private ShareActionProvider mShareActionProvider;


    
    private final ArrayList<PlayerStateListener> listeners = new ArrayList<PlayerStateListener>();
    
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
		
    	// Sets button image based on player state
		if(playerState == PlayerUtils.STATE_STOPPED){
			playPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
		}else{
			playPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
		}
    	
    	playPauseButton.setOnClickListener(new PlayButtonOnClickListener(this, playPauseButton));
    	
    	streamTitleTextView = (TextView) findViewById(R.id.bottom_bar_stream_title_textview);
    	streamTitleTextView.setMovementMethod(new ScrollingMovementMethod());
    	streamTitleTextView.setSelected(true);
    	streamTitleTextView.setText(streamTitle);
        
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

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        return shareIntent;
    }
    
    /**
     * GETTERS AND SETTERS
     */
    
    public void setStreamTitle(String streamTitle) {
    	streamTitleTextView.setText(streamTitle);
	}
    
	/*public int getPlayerState(){
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
	}*/
	
}