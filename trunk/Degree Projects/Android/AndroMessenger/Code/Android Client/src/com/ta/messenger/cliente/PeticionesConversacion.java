package com.ta.messenger.cliente;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;
import android.widget.EditText;

public class PeticionesConversacion extends Thread{

	String ipContacto;
    Socket socket;
    ProcesadorMensajesEntrada lector;
    ProcesadorMensajesSalida escritor;
    EditText textoIntroducido;
    Conversacion conversacion;
    Principal ventanaPrincipal;
	
	public PeticionesConversacion(String _ip, Conversacion _conversacion, Principal _ventanaPrincipal, EditText _textoIntroducido){
		ipContacto = _ip;
		textoIntroducido = _textoIntroducido;
		conversacion = _conversacion;
		ventanaPrincipal = _ventanaPrincipal;
	}
	
    
    // METODO RUN
    public void run(){
    	
        try {
			socket=new Socket(ipContacto,8888);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Log.d("TCP", "Error al crear socket: No se conoce el host");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("TCP", "Error al crear socket: excepcion IO");
		}
        
        lector=new ProcesadorMensajesEntrada(socket, conversacion, ventanaPrincipal);
        escritor=new ProcesadorMensajesSalida(socket,textoIntroducido);
        
        // Lanzamos las hebras para leer y escribir:
        lector.start();
        escritor.start();
    	
    }// FIN METODO RUN
	
} //FIN CLASE







