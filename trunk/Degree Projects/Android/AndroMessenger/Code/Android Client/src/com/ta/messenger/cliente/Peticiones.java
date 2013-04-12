package com.ta.messenger.cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

public class Peticiones extends Thread{
	
    InetAddress direccionServidor;
    Socket socket;
    
    
////////////////////////////////////////////////////////////////////////////////
/////////////////// PETICIONES A SERVIDOR /////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/*********************************************************************/
/**************** Método SolicitarListaContactos *********************/
/*********************************************************************/
public String solicitarListaContactos (String usuario){

String resultado = null;

// Crear conexión con el servidor
try{
direccionServidor = InetAddress.getByName("10.0.2.2");
socket = new Socket(direccionServidor, 9090);
} catch (Exception e) {
Log.e("TCP", "C: Error al crear el socket de conexión con el servidor.", e);
}

try {
PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
// Formato del mensaje -> AÑADIRCONTACTO <nombre>
out.println(TiposMensaje.mSolicitudListaContactos + ";" + usuario);
Log.d("TCP", "C: Enviado mensaje solicitud lista -> "+TiposMensaje.mSolicitudListaContactos + ";" + usuario);
		    	 
} catch(Exception e) {
Log.e("TCP", "S: Error al enviar mensaje.", e);
} 

try {

BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));

String mensajeRespuesta = in.readLine();

Log.d("TCP", "C: Respuesta obtenida : "+mensajeRespuesta);
resultado = mensajeRespuesta;
               
} catch (IOException e) {
Log.d("TCP", "Error: no se pudo leer la respuesta.");
}

try{
socket.close();
} catch (Exception e) {
Log.e("TCP", "C: Error al cerrar el socket de conexión con el servidor.", e);
}

return resultado;

}

/*********************************************************************/
/**************** Método SolicitarListaContactos *********************/
/*********************************************************************/
public String solicitarListaContactosConectados (String usuario){

	String resultado = null;
	
	// Crear conexión con el servidor
	try{
		direccionServidor = InetAddress.getByName("10.0.2.2");
		socket = new Socket(direccionServidor, 9090);
	} catch (Exception e) {
		Log.e("TCP", "C: Error al crear el socket de conexión con el servidor.", e);
	}
	
	try {
		PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
		// Formato del mensaje -> LISTARCONTACTOSCONECTADOS
		out.println(TiposMensaje.mSolicitudListaContactosConectados + ";");
		Log.d("TCP", "C: Enviado mensaje solicitud lista -> "+TiposMensaje.mSolicitudListaContactosConectados + ";");
			    	 
	} catch(Exception e) {
		Log.e("TCP", "S: Error al enviar mensaje.", e);
	} 
	
	try {
	
		BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
	
		String mensajeRespuesta = in.readLine();
		
		Log.d("TCP", "C: Respuesta obtenida : "+mensajeRespuesta);
		resultado = mensajeRespuesta;
	               
	} catch (IOException e) {
		Log.d("TCP", "Error: no se pudo leer la respuesta.");
	}
	
	try{
		socket.close();
	} catch (Exception e) {
		Log.e("TCP", "C: Error al cerrar el socket de conexión con el servidor.", e);
	}
	
	return resultado;

}

/*********************************************************************/
/**************** Método SolicitarApodosContactos *********************/
/*********************************************************************/
public String[] solicitarApodosContactos (String[] contactos){

	String[] resultado = null;
	String listaContactos = ";";
	
	for (int i=0; i<contactos.length; i++){
		listaContactos += contactos[i] + ";";
	}

	Log.e("TCP", "La lista de contactos confeccionada es->" + listaContactos);

// Crear conexión con el servidor
	try{
		direccionServidor = InetAddress.getByName("10.0.2.2");
		socket = new Socket(direccionServidor, 9090);
	} catch (Exception e) {
		Log.e("TCP", "C: Error al crear el socket de conexión con el servidor.", e);
	}
	try {
	PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
	// Formato del mensaje -> APODO <nombres>
		out.println(TiposMensaje.mSolicitudApodo + ";" + listaContactos);
		Log.d("TCP", "C: Enviado mensaje solicitud lista -> "+TiposMensaje.mSolicitudApodo + ";" + listaContactos);
			    	 
	} catch(Exception e) {
		Log.e("TCP", "S: Error al enviar mensaje.", e);
	} 
	
	try {
	
		BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
	
		String mensajeRespuesta = in.readLine();
	
		Log.d("TCP", "C: Respuesta obtenida : "+mensajeRespuesta);
		resultado = obtenerListaContactosDeMensaje(mensajeRespuesta);
	               
	} catch (IOException e) {
		Log.d("TCP", "Error: no se pudo leer la respuesta.");
	}

	try{
		socket.close();
	} catch (Exception e) {
		Log.e("TCP", "C: Error al cerrar el socket de conexión con el servidor.", e);
	}

return resultado;

}

/*********************************************************************/
/*************** Método obtenerListaContactosDeMensaje ***************/
/*********************************************************************/
// Devuelve la lista de contactos en un String a partir de un mensaje que llega del servidor
public String[] obtenerListaContactosDeMensaje (String mensaje){

String[] palabrasMensaje = mensaje.split(";");
String[] lista = new String[palabrasMensaje.length-3];

if (palabrasMensaje.length>3){// Si hay contactos en el mensaje
Log.e("TCP", "C: Obtenidos contactos");
for (int i=0; i<palabrasMensaje.length-3; i++){ // Los contactos aparecen a partir de la tercera posicion
	
	lista[i]  = palabrasMensaje[i+3];
	Log.e("TCP", "C: contacto" + (i) + ": " + lista[i]);
}
}else{
lista = null;
}// fin if
return lista;
} //Fin obtenerListaContactosDeMensaje

/*********************************************************************/
/********** Método obtenerListaContactosConectadosDeMensaje **********/
/*********************************************************************/
// Devuelve la lista de contactos conectados en un String a partir de un mensaje que llega del servidor
public String[] obtenerListaContactosConectadosDeMensaje (String mensaje){

String[] palabrasMensaje = mensaje.split(";");
String[] lista = new String[palabrasMensaje.length-3];

if (palabrasMensaje.length>2){// Si hay contactos en el mensaje
Log.e("TCP", "C: Obtenidos contactos");
for (int i=0; i<palabrasMensaje.length-3; i++){ // Los contactos aparecen a partir de la tercera posicion
	
	lista[i]  = palabrasMensaje[i+3];
	Log.e("TCP", "C: contacto" + (i) + ": " + lista[i]);
}
}else{
lista = null;
}// fin if
return lista;
} //Fin obtenerListaContactosConectadosDeMensaje

/*********************************************************************/
/**************** Método eliminarContacto ****************************/
/*********************************************************************/
public String eliminarContacto (String contacto){
String resultado = null;

// Crear conexión con el servidor
try{
direccionServidor = InetAddress.getByName("10.0.2.2");
socket = new Socket(direccionServidor, 9090);
} catch (Exception e) {
Log.e("TCP", "C: Error al crear el socket de conexión con el servidor.", e);
}

try {
PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
// Formato del mensaje -> ELIMINAR <nombre>
out.println(TiposMensaje.mEliminarContacto + ";" + contacto);
Log.d("TCP", "C: Enviado mensaje eliminar contacto -> "+TiposMensaje.mEliminarContacto + ";" + contacto);
		    	 
} catch(Exception e) {
Log.e("TCP", "S: Error al enviar mensaje.", e);
} 

try {

BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));

resultado = in.readLine();

Log.d("TCP", "C: Respuesta obtenida : "+resultado);
               
} catch (IOException e) {
Log.d("TCP", "Error: no se pudo leer la respuesta.");
}

try{
	socket.close();
} catch (Exception e) {
	Log.e("TCP", "C: Error al cerrar el socket de conexión con el servidor.", e);
}

return resultado;
}

/*********************************************************************/
/**************** Método agregarContacto ****************************/
/*********************************************************************/
public String agregarContacto (String contacto){
String resultado = null;

// Crear conexión con el servidor
try{
direccionServidor = InetAddress.getByName("10.0.2.2");
socket = new Socket(direccionServidor, 9090);
} catch (Exception e) {
Log.e("TCP", "C: Error al crear el socket de conexión con el servidor.", e);
}

try {
PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
// Formato del mensaje -> AÑADIRCONTACTO <nombre>
out.println(TiposMensaje.mAnadirContacto + ";" + contacto);
Log.d("TCP", "C: Enviado mensaje agregar contacto -> "+TiposMensaje.mAnadirContacto + ";" + contacto);
		    	 
} catch(Exception e) {
Log.e("TCP", "S: Error al enviar mensaje.", e);
} 

try {

BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));

resultado = in.readLine();

Log.d("TCP", "C: Respuesta obtenida : "+resultado);
               
} catch (IOException e) {
Log.d("TCP", "Error: no se pudo leer la respuesta.");
}

try{
	socket.close();
} catch (Exception e) {
	Log.e("TCP", "C: Error al cerrar el socket de conexión con el servidor.", e);
}

return resultado;
}

/*********************************************************************/
/**************** Método cambiarApodo ****************************/
/*********************************************************************/
public String cambiarApodo (String apodo){
String resultado = null;

// Crear conexión con el servidor
try{
direccionServidor = InetAddress.getByName("10.0.2.2");
socket = new Socket(direccionServidor, 9090);
} catch (Exception e) {
Log.e("TCP", "C: Error al crear el socket de conexión con el servidor.", e);
}

try {
PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
// Formato del mensaje -> CAMBIOAPODO <nuevoApodo>
out.println(TiposMensaje.mSolicitudCambioApodo + ";" + apodo);
Log.d("TCP", "C: Enviado mensaje cambio de apodo -> "+TiposMensaje.mSolicitudCambioApodo + ";" + apodo);
		    	 
} catch(Exception e) {
Log.e("TCP", "S: Error al enviar mensaje.", e);
} 

try {

BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));

resultado = in.readLine();

Log.d("TCP", "C: Respuesta obtenida : "+resultado);
               
} catch (IOException e) {
Log.d("TCP", "Error: no se pudo leer la respuesta.");
}

try{
	socket.close();
} catch (Exception e) {
	Log.e("TCP", "C: Error al cerrar el socket de conexión con el servidor.", e);
}

return resultado;
}

/*********************************************************************/
/**************** Método localizarContacto ****************************/
/*********************************************************************/
// Realiza la petición de localizar contacto al servidor y devuelve la IP
public String localizarContacto (String contacto){
	String resultado = null;
	String mensajeRespuesta = null;
	
	// Crear conexión con el servidor
	try{
		direccionServidor = InetAddress.getByName("10.0.2.2");
		socket = new Socket(direccionServidor, 9090);
	} catch (Exception e) {
		Log.e("TCP", "C: Error al crear el socket de conexión con el servidor.", e);
	}
	
	try {
		PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
	// Formato del mensaje -> LOCALIZAR <nombre>
		out.println(TiposMensaje.mSolicitarLocalizacion + ";" + contacto + ";" );
		Log.d("TCP", "C: Enviado mensaje: "+TiposMensaje.mSolicitarLocalizacion + ";" + contacto + ";" );
			    	 
	} catch(Exception e) {
		Log.e("TCP", "S: Error al enviar mensaje.", e);
	} 
	
	try {
	
		BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
	
		mensajeRespuesta = in.readLine();
	
		Log.d("TCP", "C: Respuesta obtenida : "+resultado);
	               
	} catch (IOException e) {
		Log.d("TCP", "Error: no se pudo leer la respuesta.");
	}
	
	try{
		socket.close();
	} catch (Exception e) {
		Log.e("TCP", "C: Error al cerrar el socket de conexión con el servidor.", e);
	}
	// Obtener IP del mensaje
	if (mensajeRespuesta.split(";")[0].equals("200")){ //Si se obtiene respuesta OK
		resultado = mensajeRespuesta.split(";")[2];
	}else{
		Log.e("TCP", "S: No se puede obtener la IP");
	}
	return resultado;
}

/*********************************************************************/
/**************** Método localizarContacto ****************************/
/*********************************************************************/
public void iniciarConversacion (String IP){

}


/*********************************************************************/
/**************** Método desconectar ****************************/
/*********************************************************************/
public String desconectar (){
String resultado = null;

// Crear conexión con el servidor
try{
direccionServidor = InetAddress.getByName("10.0.2.2");
socket = new Socket(direccionServidor, 9090);
} catch (Exception e) {
Log.e("TCP", "C: Error al crear el socket de conexión con el servidor.", e);
}

try {
PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
// Formato del mensaje -> CERRAR
out.println(TiposMensaje.mCierreSesion + ";");
Log.d("TCP", "C: Enviado mensaje cierre de sesión");
		    	 
} catch(Exception e) {
Log.e("TCP", "S: Error al enviar mensaje.", e);
} 

try {

BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream()));

resultado = in.readLine();

Log.d("TCP", "C: Respuesta obtenida : "+resultado);
               
} catch (IOException e) {
Log.d("TCP", "Error: no se pudo leer la respuesta.");
}

try{
	socket.close();
} catch (Exception e) {
	Log.e("TCP", "C: Error al cerrar el socket de conexión con el servidor.", e);
}

return resultado;
}
} // FIN CLASE
