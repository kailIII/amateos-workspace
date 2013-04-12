/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hodroist.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author albertomateos
 */

//Subclass offers service to serve Android client requests through TCP socket
public class Service extends Thread {

        //Connection variables
        Socket socketService;
        PrintWriter out;
        BufferedReader in;
        ObjectOutputStream outObject;

        static String password;
        static String listOfTracks;
        static ArrayList<Track> mp3Collection;

        
        public Service(Socket _socketService, String _password, ArrayList _mp3Collection){

            socketService=_socketService;
            password=_password;
            mp3Collection = _mp3Collection;

             try {
                 out= new PrintWriter(socketService.getOutputStream(), true);
                 outObject = new ObjectOutputStream(socketService.getOutputStream());
                 in= new BufferedReader(new InputStreamReader(socketService.getInputStream()));
              } catch(IOException e){
                 System.err.println(this.getName()+" Error: cannot stablish in/out");
              }

        }

        public void run(){
            String messageFromClient=null;

            //Read message from client
            try {
                messageFromClient=in.readLine();
                System.out.println("Received message from client -> "+messageFromClient);
            } catch (IOException ex) {
                 System.err.println(this.getName()+" Error: can not read message from client");
            }


            //Get words from message
            String [] messageWords = messageFromClient.split(";");

            //Response to CONNECT MESSAGE
            if (messageWords[0].equals("CONNECT") && messageWords[1].equals(password)) {
                System.out.println("Received CONNECT message");
                out.println(getListOfTracks());
            }

            //Response to TRACK MESSAGE
            if (messageWords[0].equals("TRACK") && messageWords[1].equals(password)) {

                int index = Integer.parseInt(messageWords[2]);
                Track trackToSend = (Track) mp3Collection.get(index);
                try {
                    outObject.writeObject(trackToSend);
                    outObject.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            // Close connection
            try {
                socketService.close();
            } catch (IOException ex) {
                System.err.println(this.getName()+" Can not close connection");
            }
        }


    //Get a String compose by all tracks of the collection
    public static String getListOfTracks(){
        String result="##";

        for (int i=0; i<mp3Collection.size(); i++){
            Track track = (Track) mp3Collection.get(i);
            result += track.artist+ " - "+track.title+"##";  //Separator -> ##
        }
        return result;
    }

}