package org.hodroist.app;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


//Allow user to Login on system
public class Login extends Activity {
	
	private final String directoryServerIP = "192.168.1.66";
	EditText userIntroName;
	EditText userIntroPass;
	Button connect;
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        //Get GUI items
        userIntroName = (EditText) findViewById(R.id.usernameEditText);
        userIntroPass = (EditText) findViewById(R.id.passwordEditText);
        connect = (Button) findViewById(R.id.connectButton);
        connect.setOnClickListener(connectListener);        
    }
    
    
    /*********************************************************************/
    /**************** CONNECT BUTTON LISTENER ****************************/
    /*********************************************************************/
    private OnClickListener connectListener = new OnClickListener() {
        
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String jsonResponse = null;
			
			//Get IP from directory server using an SSL connection
	        try {
				SSLConnection conn = new SSLConnection();
				jsonResponse = conn.connect(directoryServerIP, userIntroName.getText().toString(), userIntroPass.getText().toString());
	        } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Get IP from Json response
			String ip = getIPfromJson(jsonResponse);
	
			String pass = userIntroPass.getText().toString();
			
			//Intent to open next Activity -> Tracklist.class
			Intent i = new Intent(Login.this, Tracklist.class);
			//Put variables into Intent
            Bundle bundle = new Bundle();
    		bundle.putString("ip",ip);
    		bundle.putString("password", pass);
            i.putExtras(bundle);
            
            //Open Tracklist Activity
            Login.this.startActivity(i);
			
		}
    };
      
    
    //get IP on a String from a JSON response
    public String getIPfromJson(String jsonText){

    	String ip = null;
        Object obj = null;   	
    	JSONParser parser=new JSONParser();
        
		try {
			obj = parser.parse(jsonText);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("json","Cant parse Json response.");
		}
        JSONArray array=(JSONArray)obj;                      
        JSONObject obj2=(JSONObject)array.get(0);
		ip = (String) obj2.get("ip");    	

		return ip; 	    
    }
}