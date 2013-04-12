package org.hodroist.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//Allow to play mp3 streaming from server
public class Player extends Activity implements
MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {

String TAG = getClass().getSimpleName();
MediaPlayer mp = null;

	Button play;
	Button tracklist;
	Button stop;
	Button next;
	Button prev;

	String mediaServerIP;
	String password;
	Track selectedTrack;
	int trackIndex;
	String[] mp3Collection;
	TextView playingText;
	TextView loadedText;
	TextView trackOrderText;
	ImageView artwork;
	ProgressBar seekBar;
	Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.player);

		//Get variables from Intent of previous activity
		Bundle bundle = this.getIntent().getExtras();
		mediaServerIP = bundle.getString("ip");
		password = bundle.getString("password");
		selectedTrack = (Track) bundle.getSerializable("track");
		trackIndex = bundle.getInt("trackIndex");
		mp3Collection = bundle.getStringArray("collection");

		//Obtain GUI layout items
		play = (Button) findViewById(R.id.playButton);
		stop = (Button) findViewById(R.id.stopButton);
		next = (Button) findViewById(R.id.nextButton);
		prev = (Button) findViewById(R.id.prevButton);
		tracklist = (Button) findViewById(R.id.tracklistButton);
		playingText = (TextView) findViewById(R.id.playingText);
		loadedText = (TextView) findViewById(R.id.loadedText);
		trackOrderText = (TextView) findViewById(R.id.trackOrderText);
		artwork = (ImageView) findViewById(R.id.artworkImageView);
		seekBar = (ProgressBar) findViewById(R.id.seekBar);

		
		//Set configuration of GUI
		resetProgressBar();
		setArtwork();
		setPlayingText();
		setTrackOrderText();

		// Set Buttons listeners
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				next();
			}
		});

		tracklist.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				//Intent to open next Activity -> Tracklist.class
				Intent i = new Intent(Player.this, Tracklist.class);
				Bundle bundle = new Bundle();
				bundle.putString("ip",mediaServerIP);
				bundle.putString("password", password);
				i.putExtras(bundle);

				//Open Tracklist Activity
				Player.this.startActivity(i);
			}
		});

		prev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				prev();
			}
		});
		play.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				play();
			}
		});

		stop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				stop();
			}
		});
	}

	//Reset position of progress bar
	 void resetProgressBar(){
		mHandler.post(new Runnable() {
			public void run() {
				seekBar.setProgress(0);
			}
		});
	}

	 //Show information about playing track on GUI
	 void setPlayingText(){
		// When clicked, show a toast with the track text
		Toast.makeText(getApplicationContext(), "Loading: "+selectedTrack.artist+" - "+selectedTrack.title,Toast.LENGTH_SHORT).show();
		playingText.setText(selectedTrack.artist+" - "+selectedTrack.title + " ("+selectedTrack.year+")"+" ["+selectedTrack.album+"]");
	}

	 //Show order of playing track
	 void setTrackOrderText(){
		trackOrderText.setText("Track "+(trackIndex+1)+" of "+mp3Collection.length);
	}

	 //Show artwork image of playing track
	 void setArtwork(){

		Bitmap bitmap = BitmapFactory.decodeByteArray(selectedTrack.artwork, 0, selectedTrack.artwork.length);
		artwork.setImageBitmap(bitmap);

	}

	 
	 //Play a track
	 void play() {

		//Change color of play and stop button
		play.setBackgroundResource(R.drawable.greenplay);
		stop.setBackgroundResource(R.drawable.bluestop);
		
		Uri myUri = Uri.parse("http://"+mediaServerIP+":8088/"+Uri.encode(selectedTrack.filename));
		Log.d(TAG, "Url -> "+myUri.toString());
		
		//Starts a Media Player
		try {
			if (mp == null) {
				this.mp = new MediaPlayer();
			} else {
				mp.stop();
				mp.reset();
			}
			mp.setDataSource(this, myUri); // Go to Initialized state
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.setOnPreparedListener(this);
			mp.setOnBufferingUpdateListener(this);

			mp.setOnErrorListener(this);
			mp.prepareAsync();

			Log.d(TAG, "LoadClip Done");
		} catch (Throwable t) {
			Log.d(TAG, t.toString());
		}

	}

	//Overrided method
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "Stream is prepared");
		mp.start();
	}

	//Stop playing a track
	 void stop() {
		if(mp.isPlaying()){
			
			//Change color of play and stop buttons
			play.setBackgroundResource(R.drawable.blueplay);
			stop.setBackgroundResource(R.drawable.greenstop);
			mp.stop();
		}
	}


	 //Get a track object from server, set GUI parameters and call play() method
	 void loadAndPlayTrack(){
		ServerConnection conn = new ServerConnection(mediaServerIP,password);
		selectedTrack = (Track) conn.getTrack(trackIndex);
		resetProgressBar();
		setArtwork();
		setPlayingText();
		setTrackOrderText();
		play();	
	}

	 //Play next track
	 void next() { 
		if(trackIndex<mp3Collection.length-1){
			trackIndex++;
			loadAndPlayTrack();

		}else{
			trackIndex=0;
			loadAndPlayTrack();	
		}
	}

	 //Play previous track
	 void prev(){
		if(trackIndex>0){
			trackIndex--;
			loadAndPlayTrack();

		}else{
			trackIndex=mp3Collection.length-1;
			loadAndPlayTrack();	
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stop();

	}

	//Overrided method
	public void onCompletion(MediaPlayer mp) {
		stop();
	}

	//Overrided method
	public boolean onError(MediaPlayer mp, int what, int extra) {
		StringBuilder sb = new StringBuilder();
		sb.append("Media Player Error: ");
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			sb.append("Not Valid for Progressive Playback");
			break;
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			sb.append("Server Died");
			break;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			sb.append("Unknown");
			break;
		default:
			sb.append(" Non standard (");
			sb.append(what);
			sb.append(")");
		}
		sb.append(" (" + what + ") ");
		sb.append(extra);
		Log.e(TAG, sb.toString());
		return true;
	}

	//Overrided method
	public void onBufferingUpdate(MediaPlayer mp, int percent) {

		//Update seekbar & loadedTextView
		seekBar.setSecondaryProgress(percent);
		loadedText.setText("Loaded: "+percent+" %");
	}

}
