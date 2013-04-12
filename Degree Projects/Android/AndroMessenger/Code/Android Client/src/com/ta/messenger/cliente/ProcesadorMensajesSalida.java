package com.ta.messenger.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;
import android.widget.EditText;

public class ProcesadorMensajesSalida extends Thread{
	
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	EditText textoIntroducido;
	
	/**
	 * Creates a new instance of ProcesadorSalida
	 */
	public ProcesadorMensajesSalida(Socket s, EditText _textoIntroducido) {
		socket=s;
		textoIntroducido=_textoIntroducido;
		try {

			// Obtenemos el flujo de escritura para enviar mensajes
			out = new PrintWriter(socket.getOutputStream(), true);

			// Para leer desde entrada esta'ndar:'
			//in=new BufferedReader(new InputStreamReader(System.in)); 
		} catch (IOException ex) {
			System.err.println("Error al abrir canal de lectura/escritura.");
		}
	}

	public void run(){

		boolean salir=false;
		String mensaje;
		
		do {

			mensaje=textoIntroducido.getText().toString();

			if(mensaje!=""){
				Log.d("TCP","Enviando "+mensaje);
				out.println(mensaje);
			}
		} while (!salir);
	}

}
