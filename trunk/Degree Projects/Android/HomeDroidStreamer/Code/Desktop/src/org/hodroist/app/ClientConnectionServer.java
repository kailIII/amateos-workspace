package org.hodroist.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author albertomateos
 */

//Provide socket connection to listen Android client requests
public class ClientConnectionServer extends Thread{


    //Connection variables declaration
    static ServerSocket attentionSocket;
    static Socket serviceSocket;
    static int puerto = 4444;


    public String password;
    public ArrayList<Track> mp3Collection;
    static Service Service;

    //Constructor
    public ClientConnectionServer(String _password, ArrayList<Track> _mp3Collection){
        password = _password;
        mp3Collection = _mp3Collection;

        //Open socket and listen at port 4444
        try {
            attentionSocket = new ServerSocket(puerto);
        } catch (IOException e) {
            System.err.println("Error: Can not open the socket at port 4444.");
            System.exit(-2);
        }
    }



    public void run(){
            boolean salir=false;

            //Infinite loop
            do {

                //Everytime the socket accept a petition a new thread Service starts
                try {
                    serviceSocket=attentionSocket.accept();
                    new Service(serviceSocket,password,mp3Collection).start();
                } catch (IOException e){
                    System.err.println("Error: can not accept petetition.");
                }


            } while (!salir);
     }
}
