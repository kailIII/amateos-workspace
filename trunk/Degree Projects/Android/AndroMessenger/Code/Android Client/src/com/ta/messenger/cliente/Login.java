package com.ta.messenger.cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.os.Bundle;

public class Login extends Activity {
    
	private final int PETICION_ACTIVITY_TABSMAIN=0;
	EditText entradaUsuario;
	EditText entradaContrasena;
	EditText entradaApodo;
	Button conectar;
	
    InetAddress direccionServidor;
    Socket socket;
        
    /*********************************************************************/
    /**************** METODO ON CREATE ***********************************/
    /*********************************************************************/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        entradaUsuario = (EditText) findViewById(R.id.user);
        entradaContrasena = (EditText) findViewById(R.id.pass);
        entradaApodo = (EditText) findViewById(R.id.apodoEntrada);
        conectar = (Button) findViewById(R.id.botonEnviar);
        conectar.setOnClickListener(conectarListener);
               
        
    } // FIN ONCREATE ***********
    
    
    /*********************************************************************/
    /**************** LISTENER BOTÓN CONECTAR ****************************/
    /*********************************************************************/
    private OnClickListener conectarListener = new OnClickListener() {
        public void onClick(View v) {
        	
        	// Crear conexión con el servidor
            try{
            	direccionServidor = InetAddress.getByName("10.0.2.2");
            	socket = new Socket(direccionServidor, 9090);
            } catch (Exception e) {
            	Log.e("TCP", "C: Error al crear el socket de conexión con el servidor.", e);
            }
            
            String respuestaServidor = login (entradaUsuario.getText().toString(),entradaContrasena.getText().toString(),entradaApodo.getText().toString());
            Log.e("TCP", "S: La al login del servidor es " + respuestaServidor);
            
            //Peticion apertura activity de TABS (contactos y conversaciones)
            Intent abrirTabs = new Intent(getApplicationContext(), Principal.class);
            Bundle bundle = new Bundle();
    		bundle.putString("Usuario", entradaUsuario.getText().toString());
    		bundle.putString("Apodo", entradaApodo.getText().toString());
            abrirTabs.putExtras(bundle);
            
            try{
            	socket.close();//Se cierra la conexión antes de hacer la llamada a la nueva activity
            } catch (Exception e) {
            	Log.e("TCP", "C: No se puede cerrar el socket.", e);
            }
            
            startActivityForResult(abrirTabs,PETICION_ACTIVITY_TABSMAIN);
        }
    };

    
    /*********************************************************************/
    /**************** Método LOGIN ***************************************/
    /*********************************************************************/
    private String login (String usuario, String contrasena, String apodo){
    	
    	String resultado = null;
    	
    	
    	//Envio de mensaje de login
    	
    	try {
	    	 PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
	 		// Formato del mensaje -> REGISTRAR <nombre> <contraseña> <apodo>
	    	 out.println(TiposMensaje.mRegistrar + ";" + usuario + ";" + contrasena + ";" + apodo );
	    	 Log.d("TCP", "C: Mensaje de login enviado.");
            		    	 
         } catch(Exception e) {
           Log.e("TCP", "S: Error al enviar mensaje.", e);
	      } 
    	
    	try {

       	 BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));

       	 Log.d("TCP", "C: Esperando respuesta del servidor.");
            String mensajeRespuesta = in.readLine();
            
            Log.d("TCP", "C: Respuesta obtenida : "+mensajeRespuesta);
	    	 resultado = mensajeRespuesta;
                             
          } catch (IOException e) {
       	   Log.d("TCP", "Error: no se pudo leer la respuesta.");
          }
    	
          return resultado;
    }// Fin LOGIN
}