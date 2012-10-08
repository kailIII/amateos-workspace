package com.emp.friskyplayer.services;

/*   
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import com.emp.friskyplayer.R;
import com.emp.friskyplayer.activities.FriskyPlayerActivity;
import com.emp.friskyplayer.audio.AudioFocusHelper;
import com.emp.friskyplayer.audio.MusicFocusable;


import com.spoledge.aacdecoder.AACPlayer;
import com.spoledge.aacdecoder.MultiPlayer;
import com.spoledge.aacdecoder.PlayerCallback;


/**
 * Service that handles media playback. This is the Service through which we perform all the media
 * handling in our application. Upon initialization, it starts a {@link MusicRetriever} to scan
 * the user's media. Then, it waits for Intents (which come from our main activity,
 * {@link MainActivity}, which signal the service to perform specific operations: Play, Pause,
 * Rewind, Skip, etc.
 */
/*public class FriskyService extends Service implements OnCompletionListener, OnPreparedListener,
                OnErrorListener, MusicFocusable, PlayerCallback {*/
public class FriskyService extends Service implements MusicFocusable, PlayerCallback {

    // The tag we put on debug messages
    final static String TAG = "FriskyService";

    // These are the Intent actions that we are prepared to handle. Notice that the fact these
    // constants exist in our class is a mere convenience: what really defines the actions our
    // service can handle are the <action> tags in the <intent-filters> tag for our service in
    // AndroidManifest.xml.
    public static final String ACTION_TOGGLE_PLAYBACK =
            "com.emp.friskyplayer.services.action.TOGGLE_PLAYBACK";
    public static final String ACTION_PLAY = "com.emp.friskyplayer.services.action.PLAY";
    public static final String ACTION_STOP = "com.emp.friskyplayer.services.action.STOP";

    // The volume we set the media player to when we lose audio focus, but are allowed to reduce
    // the volume instead of stopping playback.
    public static final float DUCK_VOLUME = 0.1f;

    // our media player
  //TODO: adaptar a aacplayer
    //MediaPlayer mPlayer = null;
    AACPlayer mPlayer = null;


    // our AudioFocusHelper object, if it's available (it's available on SDK level >= 8)
    // If not available, this will be null. Always check for null before using!
    AudioFocusHelper mAudioFocusHelper = null;

    // indicates the state our service:
    enum State {
        Stopped,    // media player is stopped and not prepared to play
        Preparing,  // media player is preparing...
        Playing,    // playback active
    };

    State mState = State.Stopped;

    enum PauseReason {
        UserRequest,  // paused by user request
        FocusLoss,    // paused because of audio focus loss
    };

    // why did we pause? (only relevant if mState == State.Paused)
    PauseReason mPauseReason = PauseReason.UserRequest;

    // do we have audio focus?
    enum AudioFocus {
        NoFocusNoDuck,    // we don't have audio focus, and can't duck
        NoFocusCanDuck,   // we don't have focus, but can play at a low volume ("ducking")
        Focused           // we have full audio focus
    }
    AudioFocus mAudioFocus = AudioFocus.NoFocusNoDuck;

    // title of the song we are currently playing
    String mSongTitle = "";

    // Wifi lock that we hold when streaming files from the internet, in order to prevent the
    // device from shutting off the Wifi radio
    WifiLock mWifiLock;



    // Dummy album art we will pass to the remote control (if the APIs are available).
    Bitmap mDummyAlbumArt;


    AudioManager mAudioManager;
    
    // The ID we use for the notification (the onscreen alert that appears at the notification
    // area at the top of the screen as an icon -- and as text as well if the user expands the
    // notification area).
    final int NOTIFICATION_ID = 1;
    NotificationManager mNotificationManager;

    Notification mNotification = null;

    /**
     * Makes sure the media player exists and has been reset. This will create the media player
     * if needed, or reset the existing media player if one already exists.
     */
    //TODO: adaptar a aacplayer
    void createMediaPlayerIfNeeded() {
        if (mPlayer == null) {
            //mPlayer = new MediaPlayer();
        	// TODO: agregar al mplayer los valores de los buffers en el constructor
        	mPlayer = new AACPlayer(this);
            
            // Make sure the media player will acquire a wake-lock while playing. If we don't do
            // that, the CPU might go to sleep while the song is playing, causing playback to stop.
            //
            // Remember that to use this, we have to declare the android.permission.WAKE_LOCK
            // permission in AndroidManifest.xml.
            // TODO: comprobar que el procesador no se va a dormir y jode la reproducción
        	//mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            
            // we want the media player to notify us when it's ready preparing, and when it's done
            // playing:
            /*mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);*/
        }
        //else
            //mPlayer.reset();
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "debug: Creating service");

        // Create the Wifi lock (this does not acquire the lock, this just creates it)
        mWifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                        .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // create the Audio Focus Helper, if the Audio Focus feature is available (SDK 8 or above)
        if (android.os.Build.VERSION.SDK_INT >= 8)
            mAudioFocusHelper = new AudioFocusHelper(getApplicationContext(), this);
        else
            mAudioFocus = AudioFocus.Focused; // no focus feature, so we always "have" audio focus

        
        // TODO: cambiar imagen a mostrar!!
        //mDummyAlbumArt = BitmapFactory.decodeResource(getResources(), R.drawable.dummy_album_art);

        // TODO: implementar el media button receiver.
        //mMediaButtonReceiverComponent = new ComponentName(this, MusicIntentReceiver.class);
    }

    /**
     * Called when we receive an Intent. When we receive an intent sent to us via startService(),
     * this is the method that gets called. So here we react appropriately depending on the
     * Intent's action, which specifies what is being requested of us.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action.equals(ACTION_TOGGLE_PLAYBACK)) processTogglePlaybackRequest();
        else if (action.equals(ACTION_PLAY)) processPlayRequest();
        else if (action.equals(ACTION_STOP)) processStopRequest();

        return START_NOT_STICKY; // Means we started the service, but don't want it to
                                 // restart in case it's killed.
    }

    void processTogglePlaybackRequest() {
        if (mState == State.Stopped) {
            processPlayRequest();
        } else {
            processStopRequest();
        }
    }

    void processPlayRequest() {
       
        tryToGetAudioFocus();

        // actually play the song

        if (mState == State.Stopped) {
            // If we're stopped, just go ahead to the next song and start playing
            playSong();
        }

        // TODO: control remoto
        // Tell any remote controls that our playback state is 'playing'.
        /*if (mRemoteControlClientCompat != null) {
            mRemoteControlClientCompat
                    .setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
        }*/
    }

    void processStopRequest() {
        processStopRequest(false);
    }

    void processStopRequest(boolean force) {
        if (mState == State.Playing || mState == State.Preparing || force) {
            mState = State.Stopped;

            // let go of all resources...
            relaxResources(true);
            giveUpAudioFocus();

            // Tell any remote controls that our playback state is 'paused'.
            
            //TODO: control remoto
            /*if (mRemoteControlClientCompat != null) {
                mRemoteControlClientCompat
                        .setPlaybackState(RemoteControlClient.PLAYSTATE_STOPPED);
            }*/

            // service is no longer necessary. Will be started again if needed.
            stopSelf();
        }
    }

    /**
     * Releases resources used by the service for playback. This includes the "foreground service"
     * status and notification, the wake locks and possibly the MediaPlayer.
     *
     * @param releaseMediaPlayer Indicates whether the Media Player should also be released or not
     */
    //TODO: adaptar a aacplayer
    void relaxResources(boolean releaseMediaPlayer) {
        // stop being a foreground service
        stopForeground(true);

        // stop and release the Media Player, if it's available
        if (releaseMediaPlayer && mPlayer != null) {
            //mPlayer.reset();
            //mPlayer.release();
        	mPlayer.stop();
            mPlayer = null;
        }

        // we can also release the Wifi lock, if we're holding it
        if (mWifiLock.isHeld()) mWifiLock.release();
    }

    void giveUpAudioFocus() {
        if (mAudioFocus == AudioFocus.Focused && mAudioFocusHelper != null
                                && mAudioFocusHelper.abandonFocus())
            mAudioFocus = AudioFocus.NoFocusNoDuck;
    }

    /**
     * Reconfigures MediaPlayer according to audio focus settings and starts/restarts it. This
     * method starts/restarts the MediaPlayer respecting the current audio focus state. So if
     * we have focus, it will play normally; if we don't have focus, it will either leave the
     * MediaPlayer paused or set it to a low volume, depending on what is allowed by the
     * current focus settings. This method assumes mPlayer != null, so if you are calling it,
     * you have to do so from a context where you are sure this is the case.
     */
  //TODO: adaptar a aacplayer
    void configAndStartMediaPlayer() {
        /*if (mAudioFocus == AudioFocus.NoFocusNoDuck) {
            // If we don't have audio focus and can't duck, we have to pause, even if mState
            // is State.Playing. But we stay in the Playing state so that we know we have to resume
            // playback once we get the focus back.
            if (mPlayer.isPlaying()) mPlayer.pause();
            
            return;
        }
        else if (mAudioFocus == AudioFocus.NoFocusCanDuck)
            mPlayer.setVolume(DUCK_VOLUME, DUCK_VOLUME);  // we'll be relatively quiet
        	AudioManager.
        else
            mPlayer.setVolume(1.0f, 1.0f); // we can be loud*/

        if (mState != State.Playing) mPlayer.playAsync("http://205.188.215.229:8028");

    }

    void tryToGetAudioFocus() {
        if (mAudioFocus != AudioFocus.Focused && mAudioFocusHelper != null
                        && mAudioFocusHelper.requestFocus())
            mAudioFocus = AudioFocus.Focused;
    }

    /**
     * Starts playing the next song. If manualUrl is null, the next song will be randomly selected
     * from our Media Retriever (that is, it will be a random song in the user's device). If
     * manualUrl is non-null, then it specifies the URL or path to the song that will be played
     * next.
     */
    void playSong() {
        mState = State.Stopped;
        relaxResources(false); // release everything except MediaPlayer

        //TODO: adaptar a aacplayer
                // set the source of the media player to a manual URL or path
                createMediaPlayerIfNeeded();
               // mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //mPlayer.setDataSource(manualUrl);
              

                //playingItem = new MusicRetriever.Item(0, null, manualUrl, null, 0);
            
            //TODO: mostrar título canción en GUI
            //mSongTitle = playingItem.getTitle();
            
                
            mState = State.Preparing;
            
            //TODO: mostrar string de strings.xml
            setUpAsForeground(mSongTitle + " (loading)");

            // Use the media button APIs (if available) to register ourselves for media button
            // events

          //TODO: media button
           /* MediaButtonHelper.registerMediaButtonEventReceiverCompat(
                    mAudioManager, mMediaButtonReceiverComponent);*/

            // Use the remote control APIs (if available) to set the playback state

//            if (mRemoteControlClientCompat == null) {
//                Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
//                intent.setComponent(mMediaButtonReceiverComponent);
//                mRemoteControlClientCompat = new RemoteControlClientCompat(
//                        PendingIntent.getBroadcast(this /*context*/,
//                                0 /*requestCode, ignored*/, intent /*intent*/, 0 /*flags*/));
//                RemoteControlHelper.registerRemoteControlClient(mAudioManager,
//                        mRemoteControlClientCompat);
//            }
//
//            mRemoteControlClientCompat.setPlaybackState(
//                    RemoteControlClient.PLAYSTATE_PLAYING);
//
//            mRemoteControlClientCompat.setTransportControlFlags(
//                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
//                    RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
//                    RemoteControlClient.FLAG_KEY_MEDIA_NEXT |
//                    RemoteControlClient.FLAG_KEY_MEDIA_STOP);
//
//            // Update the remote controls
//            mRemoteControlClientCompat.editMetadata(true)
//                    .putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, playingItem.getArtist())
//                    .putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, playingItem.getAlbum())
//                    .putString(MediaMetadataRetriever.METADATA_KEY_TITLE, playingItem.getTitle())
//                    .putLong(MediaMetadataRetriever.METADATA_KEY_DURATION,
//                            playingItem.getDuration())
//                    // TODO: fetch real item artwork
//                    .putBitmap(
//                            RemoteControlClientCompat.MetadataEditorCompat.METADATA_KEY_ARTWORK,
//                            mDummyAlbumArt)
//                    .apply();

            // starts preparing the media player in the background. When it's done, it will call
            // our OnPreparedListener (that is, the onPrepared() method on this class, since we set
            // the listener to 'this').
            //
            // Until the media player is prepared, we *cannot* call start() on it!
            //mPlayer.prepareAsync();
            
            mPlayer.playAsync("http://205.188.215.229:8028");

            // If we are streaming from the internet, we want to hold a Wifi lock, which prevents
            // the Wifi radio from going to sleep while the song is playing. If, on the other hand,
            // we are *not* streaming, we want to release the lock if we were holding it before.
            mWifiLock.acquire();
   
        
    }

    //////////////////////////////////
    //TODO: adaptar a aacplayer
    //////////////////////////////////
    
    /** Called when media player is done playing current song. */
    /*public void onCompletion(MediaPlayer player) {
        // The media player finished playing the current song, so we go ahead and start the next.
        //playNextSong(null);
    }*/

    /** Called when media player is done preparing. */
    /*public void onPrepared(MediaPlayer player) {
        // The media player is done preparing. That means we can start playing!
        mState = State.Playing;
        // TODO: mostrar string de strings.xml
        updateNotification(mSongTitle + " (playing)");
        configAndStartMediaPlayer();
    }*/

    /** Updates the notification. */
    void updateNotification(String text) {
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), FriskyPlayerActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.setLatestEventInfo(getApplicationContext(), "RandomMusicPlayer", text, pi);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    /**
     * Configures service as a foreground service. A foreground service is a service that's doing
     * something the user is actively aware of (such as playing music), and must appear to the
     * user as a notification. That's why we create the notification here.
     */
    void setUpAsForeground(String text) {
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), FriskyPlayerActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification = new Notification();
        mNotification.tickerText = text;
        mNotification.icon = R.drawable.ic_stat_playing;
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotification.setLatestEventInfo(getApplicationContext(), "FriskyPlayer",
                text, pi);
        startForeground(NOTIFICATION_ID, mNotification);
    }

    /**
     * Called when there's an error playing media. When this happens, the media player goes to
     * the Error state. We warn the user about the error and reset the media player.
     */
    /*public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(getApplicationContext(), "Media player error! Resetting.",
            Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Error: what=" + String.valueOf(what) + ", extra=" + String.valueOf(extra));

        mState = State.Stopped;
        relaxResources(true);
        giveUpAudioFocus();
        return true; // true indicates we handled the error
    }*/

    public void onGainedAudioFocus() {
        Toast.makeText(getApplicationContext(), "gained audio focus.", Toast.LENGTH_SHORT).show();
        mAudioFocus = AudioFocus.Focused;

        // restart media player with new focus settings
        if (mState == State.Playing)
            configAndStartMediaPlayer();
    }

    public void onLostAudioFocus(boolean canDuck) {
        Toast.makeText(getApplicationContext(), "lost audio focus." + (canDuck ? "can duck" :
            "no duck"), Toast.LENGTH_SHORT).show();
        mAudioFocus = canDuck ? AudioFocus.NoFocusCanDuck : AudioFocus.NoFocusNoDuck;

        // start/restart/pause media player with new focus settings
        //if (mPlayer != null && mPlayer.isPlaying())
        if (mPlayer != null && mState == State.Playing)
            configAndStartMediaPlayer();
    }

    @Override
    public void onDestroy() {
        // Service is being killed, so make sure we release our resources
        mState = State.Stopped;
        relaxResources(true);
        giveUpAudioFocus();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    
    
    // TODO: EVENTOS PLAYER AAC
	public void playerException(Throwable arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "EXCEPCION!!",
	            Toast.LENGTH_SHORT).show();
	        Log.e(TAG, "Error: " + arg0.getMessage());

	        mState = State.Stopped;
	        relaxResources(true);
	        giveUpAudioFocus();
	}

	public void playerMetadata(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void playerPCMFeedBuffer(boolean arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	public void playerStarted() {
		// TODO Auto-generated method stub
		mState = State.Playing;
	    // TODO: mostrar string de strings.xml
	    updateNotification(mSongTitle + " (playing)");
	}

	public void playerStopped(int arg0) {
		// TODO Auto-generated method stub
		 mState = State.Stopped;
	     relaxResources(true);
	     giveUpAudioFocus();
	}
}
