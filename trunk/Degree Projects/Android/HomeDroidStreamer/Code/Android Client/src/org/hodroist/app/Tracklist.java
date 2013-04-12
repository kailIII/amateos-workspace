package org.hodroist.app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


//Show information about all tracks available to be played
public class Tracklist extends ListActivity{
	
	String mediaServerIP;
	String password;
	String[] mp3Collection;
	ServerConnection conn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Get variables from Intent of Login Activity
        Bundle bundle = this.getIntent().getExtras();
        mediaServerIP = bundle.getString("ip");
        password = bundle.getString("password");
        
        //Create a connection to TCP socket of media server
        conn = new ServerConnection(mediaServerIP,password);
        
        //Get mp3 collection on String format
        mp3Collection = getCollection(conn.getMp3List());
       
        //Show collection on ListView
        setListAdapter(new ArrayAdapter<String>(this, R.layout.tracklist_item, mp3Collection));
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        //ListView Listener
        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
            
        	//Get selected track from server            
            Track trackToPlay = conn.getTrack(position);
            
            //Intent to open next Activity -> Player
            Intent i = new Intent(Tracklist.this, Player.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("track", trackToPlay);
            bundle.putInt("trackIndex", position);
            bundle.putStringArray("collection", mp3Collection);
            bundle.putString("ip",mediaServerIP);
            bundle.putString("password",password);
            i.putExtras(bundle);
            
            //Open Tracklist Activity
            Tracklist.this.startActivity(i);
            
          }

        });  
    }
    
    
    //Get String[] from String representation of collection coming from server
    public String[] getCollection (String mp3List){
    	
    	String[] aux = mp3List.split("##");
    	String[] result = new String[aux.length-1];
    	
    	//Remove first element of aux
    	for (int i=1;i<aux.length;i++){
    		result[i-1]=aux[i];
    	}

    	return result;
    }

}
