package com.ta.messenger.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;

public class ProcesadorMensajesEntrada extends Thread{
	
    PrintWriter out;
    BufferedReader in;
    Socket socket;
    Conversacion conversacion;
    Principal ventanaPrincipal;
    
    public ProcesadorMensajesEntrada(Socket s, Conversacion _conversacion, Principal _ventanaPrincipal)  {
        socket=s;
        conversacion = _conversacion;
        ventanaPrincipal = _ventanaPrincipal;
        try {
        
            // Obtenemos el flujo de lectura para  recibir mensajes:  
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException ex) {
          System.err.println("Error al abrir el canal de lectura.");
        }
            
    }//Fin constructor
    
    
    public void run(){
        
        boolean salir=false;
        String textoRecibido;
        
        try {
	        // Leemos li'neas enviadas por el servidor, y las mostramos:
	        do{
	          	textoRecibido=in.readLine();
	            Log.d("TCP","Texto recibido -> "+textoRecibido);
	            conversacion.agregarTextoAConversacion(conversacion.apodo, textoRecibido);
	            ventanaPrincipal.mostrarConversacion();
	            yield();
	            
	        } while(!salir);
        } catch (IOException ex) {
        	System.err.println("Error al leer del canal.");
        }
        
   }//Fin run()

}
