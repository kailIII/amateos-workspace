package org.hodroist.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.net.InetAddress;
import java.net.Socket;
import android.util.Log;

public class ServerConnection extends Thread{

	String password;
	String ipMediaServer;
	
	ServerConnection(String _ipMediaServer, String _password){
		password = _password;
		ipMediaServer =_ipMediaServer;
		
	}

	//Get String representing all files of collection from server
	public String getMp3List(){

		String result = null;
		Socket socket = null;
		PrintWriter out = null;
		
		//Create TCP connection
		try{
			InetAddress serverAddress = InetAddress.getByName(ipMediaServer);
			socket = new Socket(serverAddress, 4444);
			out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
		} catch (Exception e) {
			Log.d("TCP", "Error trying to create a connection", e);
		}

	
		//Sends CONNECT message
		try {
			String messageToServer = "CONNECT;" + password;
			out.println(messageToServer);
			Log.d("TCP", "C: Message sent -> "+messageToServer);
				    	 
		} catch(Exception e) {
			Log.d("TCP", "S: Error sending message.", e);
		} 
		
		
		//Read response from server
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
			result = in.readLine();		               
		} catch (IOException e) {
			Log.d("TCP", "Error: can not read response from server.");
		}

		
		//Close connection
		try{
			socket.close();
		} catch (Exception e) {
			Log.d("TCP", "Error: can not close connection", e);
		}
		
	return result;

	}
	
	
	//Get a track object from server
	public Track getTrack(int trackNumber){
		Track result = null;
		Socket socket = null;
		PrintWriter out = null;
		
		// Create TCP connection
		try{
			InetAddress serverAddress = InetAddress.getByName(ipMediaServer);
			socket = new Socket(serverAddress, 4444);
			out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
		} catch (Exception e) {
			Log.d("TCP", "Error trying to create a connection", e);
		}
		
		//Send TRACK message
		try {
			String messageToServer = "TRACK;" + password +";"+trackNumber;
			out.println(messageToServer);
			Log.d("TCP", "C: Message sent -> "+messageToServer);
				    	 
		} catch(Exception e) {
			Log.d("TCP", "S: Error sending message.", e);
		} 
		
		//Read object response from server
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			result = (Track) in.readObject();			
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Close connection
		try{
			socket.close();
		} catch (Exception e) {
			Log.d("TCP", "Error: can not close connection", e);
		}
		
		return result;
	}
	
}
