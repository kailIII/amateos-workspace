import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

class Servicio extends Thread {
  // Atributos de la clase
  Socket socketServicio;
  PrintWriter out;
  BufferedReader in;
  Vector <Usuario> listaClientes;

  // El constructor recibirá como argumentos el socket (abierto) que debe
  // utilizar (se lo pasa la hebra principal del servidor).
  public Servicio (Socket socketServicio_, Vector <Usuario> listaClientes_) {
    
	  this.listaClientes = listaClientes_;
	  socketServicio = socketServicio_;

    // Se obtienen los flujos de lectura y de escritura para enviar y recibir
    // mensajes.
    try {
      out = new PrintWriter (socketServicio.getOutputStream(), true);
      in = new BufferedReader (new InputStreamReader (socketServicio.getInputStream()));
    } catch (IOException e) {
      System.err.println(this.getName() + " Error: no se pudo obtener un canal para los flujos.");
    }
  }

  
  //************** RUN ******************************************************************
  public void run() {
	  
	//System.out.println ("Entra a método RUN");  
    String mensajeSolicitud = "";
    String mensajeRespuesta = "";

    try {
      // 3 - Código del servicio ofrecido.

      // 3a - Se lee el mensaje del cliente.
      mensajeSolicitud = in.readLine();
      System.out.println ("Leido mensaje de entrada ->" +mensajeSolicitud);
    } catch (IOException e) {
      System.err.println(this.getName() + " Error: no se pudo leer el mensaje.");
    }

    // 3b - Se aplica el servicio.
    mensajeRespuesta = procesaServicio (mensajeSolicitud);

    // 3c - Se envía la respuesta.
    out.println(mensajeRespuesta);
    System.out.println("Enviado mensaje -> "+mensajeRespuesta);

    
    /************ GESTIÓN DEL MENSAJE DE LLEGADA ********/
	  String [] palabrasMensaje = mensajeSolicitud.split(";"); // Se extraen las palabras del mensaje (están separadas por ;)
	  int tipoMensaje = obtenerTipoMensaje (palabrasMensaje [0]);
	  
	  if(tipoMensaje == 1 || tipoMensaje == 2){ //Mensajes son del tipo bienvenida o registro
		  System.out.println("SE VA A CERRAR EL SOCKET");
	  	try {
	      in.close();
	      out.close();
	      socketServicio.close();
	    } catch (IOException e) {
	      System.err.println(this.getName() + " Error: no se pudo cerrar la conexión.");
	    }
	  } //Fin if
  } // FIN RUN ******************************************************************************

   /**************************************************************************************************/  	
   /************PROCESADO DE MENSAJES ENTRANTES ********************************/
  // Método encargado de procesar las peticiones que se reciben en el servidor
  String procesaServicio (String mensaje) {
    
	  System.out.println ("Entra a procesado de servicio");
	  String respuesta = null;
	  
	  /************ GESTIÓN DEL MENSAJE DE LLEGADA ********/
	  String [] palabrasMensaje = mensaje.split(";"); // Se extraen las palabras del mensaje (están separadas por ;)
	  int tipoMensaje = obtenerTipoMensaje (palabrasMensaje [0]);
	  
	  System.out.println ("Tipo de mensaje obtenido ->" +tipoMensaje);
	 
	  switch (tipoMensaje){
	  
		  case 1: //Bienvenida de cliente
			  	respuesta = TiposMensaje.mBienvenidaServidor;
			  break;
			  
		  case 2: //Petición de registro
			  respuesta = gestionRegistro (palabrasMensaje);		  	  
			  break;
			  
		  case 3: //Petición de localización de contacto
			  respuesta = gestionLocalizacionContacto (palabrasMensaje);
			  break;
		  
		  case 4: //Petición de agregar contacto
			  respuesta = gestionAgregarContacto (palabrasMensaje);
			  break;
			  
		  case 5: //Petición de eliminar contacto
			  respuesta = gestionEliminarContacto (palabrasMensaje);
			  break;
			  
		  case 6: //Petición de lista de contactos
			  respuesta = gestionListaContacto (palabrasMensaje);
			  break;

		  case 7: //Petición de lista de contactos conectados
			  respuesta = gestionListaContactosConectados (palabrasMensaje);
			  break;
			  
		  case 8: //Petición de cierre de sesión
			  respuesta = gestionCierreConexion (palabrasMensaje);
			  break;
		  case 9: //Petición de lista de apodos
			  respuesta = gestionListaApodos (palabrasMensaje);
			  break;
		  case 10: //Petición de cambio de apodo
			  respuesta = gestionCambioApodo (palabrasMensaje);
			  break;
	  }
	  
	  
     return respuesta;
  }//Fin procesaServicio
	
/**************************************************************************************************/
/////////////////// METODOS DE GESTION DE PETICIONES ///////////////////////////////////////////////
/**************************************************************************************************/

	/************ GESTION DE REGISTRO ********************************/
	// Gestiona las peticiones de registro del cliente
	private String gestionRegistro (String [] palabrasMensaje){
	      
		System.out.println("Entrando a gestión de registro. Mensaje recibido: " + palabrasMensaje[1] + " " + palabrasMensaje[2]);

		// Formato del mensaje -> REGISTRAR <nombre> <contraseña> <apodo> <direccion>
		String respuesta=null;
		
		  if (usuarioConectado(palabrasMensaje [1])) { // El usuario ya está conectado
			  System.out.println("Entra a usuario conectado");
			  respuesta = TiposMensaje.mRegistroIncorrecto + ";" + "El usuario ya está conectado.";
		  
		  }else{ // El usuario no está conectado
			  
			  if (usuarioTieneCuenta(palabrasMensaje [1])){ // El usuario tiene cuenta de usuario en el servidor.
				  
				  if (contrasenaCorrecta(palabrasMensaje[1],palabrasMensaje[2])){ // La contraseña introducida es correcta
					  Usuario cliente = obtenerCliente(palabrasMensaje[1]);
					  cliente.setConectado(true);
					  cliente.IP = socketServicio.getInetAddress().toString();
					  respuesta = TiposMensaje.mRegistroCorrecto + ";" + "El registro se ha realizado correctamente.";
				  
				  } else { // La contraseña introducida no es correcta
					  respuesta = TiposMensaje.mRegistroIncorrecto + ";" + "La contraseña introducida no es correcta.";
				  }
				  
			  }else { // El usuario no tiene cuenta
				  System.out.println ("El cliente no tiene cuenta");
				  Usuario cliente = new Usuario (palabrasMensaje[1],palabrasMensaje[2],palabrasMensaje[3],socketServicio.getInetAddress().toString());
				  System.out.println ("Creado objeto cliente con params-> "+palabrasMensaje[1]+palabrasMensaje[2]+palabrasMensaje[3]);
				  cliente.setConectado(true);
				  System.out.println ("Conectado");
				  //cliente.IP = obtenerClientePorIP(socketServicio.getInetAddress().toString()).IP;
				  System.out.println ("Cliente creado con ip ->"+cliente.IP);
				  anadirClienteLista(cliente);
				  System.out.println ("Añadido cliente a la lista");
				  respuesta = TiposMensaje.mRegistroCorrecto + ";" + "Se ha creado una cuenta nueva para el cliente. El registro se ha realizado correctamente.";
				  System.out.println ("Enviado mensaje respuesta -> "+respuesta);
			  }
			  
		  }
		  return respuesta;
	}// fin gestionRegistro
	
	/************ GESTION DE LOCALIZACION DE CONTACTOS ********************************/
	// Gestiona las peticiones de localizacion de un contacto del cliente
	private String gestionLocalizacionContacto (String [] palabrasMensaje){
		String respuesta = null;
		
		// Formato del mensaje -> LOCALIZAR <nombre>
		
		Usuario contacto = obtenerCliente(palabrasMensaje[1]);
		
		if (contacto.conectado){ // El contacto está conectado
			String ip = contacto.IP;
			
			if (ip != null && ip != ""){ // La IP del contacto no está vacía
				//Mensaje a enviar -> 200 LOCALIZACION <IP>
				respuesta = TiposMensaje.mRespuestaOK + ";" + TiposMensaje.mRespuestaLocalizacion + ";" + ip;
			}else{
				respuesta = TiposMensaje.mRespuestaFallo + ";" + "No se ha podido localizar la dirección del contacto " + palabrasMensaje[1];
			}
		}else { // El contacto no está conectado
			respuesta = TiposMensaje.mRespuestaFallo + ";" + "El contacto solicitado no está conectado";
		}
			
		return respuesta;
	}// Fin gestionLocalizacion
	
	/************ GESTION DE AGREGAR CONTACTOS ********************************/
	// Gestiona las peticiones de agregar un contacto del cliente
	private String gestionAgregarContacto (String [] palabrasMensaje){
		String respuesta = null;

		// Formato del mensaje -> AÑADIRCONTACTO <nombre>

		if (existeCliente(palabrasMensaje[1])){ //El contacto solicitado existe
			
			Usuario clienteAlQueAnadir = obtenerClientePorIP(socketServicio.getInetAddress().toString());
			// Se elimina el cliente de la lista para proceder a su actualización
			Boolean eliminado = listaClientes.remove(clienteAlQueAnadir);		
			// Se añade el contacto
			clienteAlQueAnadir.agregarContacto(palabrasMensaje[1]);
			//Se vuelve a agregar el cliente (actualizado) a la lista de clientes
			Boolean agregado = listaClientes.add(clienteAlQueAnadir);
			
			respuesta = TiposMensaje.mRespuestaOK + ";" + "El contacto " + palabrasMensaje[1] + " ha sido añadido a la lista de contactos de " + clienteAlQueAnadir.nombre;
			
		}else{ // El contacto solicitado no existe
			respuesta = TiposMensaje.mRespuestaFallo + ";" + "El contacto solicitado no existe.";
		}
		
		return respuesta;
	}// Fin gestionAgregarContacto
	
	/************ GESTION DE ELIMINAR CONTACTOS ********************************/
	// Gestiona las peticiones de eliminar un contacto del cliente
	private String gestionEliminarContacto (String [] palabrasMensaje){
		String respuesta = null;

		// Formato del mensaje -> ELIMINAR <nombre>

		if (existeCliente(palabrasMensaje[1])){ //El contacto solicitado existe
			
			// Se obtiene el cliente del q se desea eliminar el contacto
			Usuario clienteDelQueEliminar = obtenerClientePorIP(socketServicio.getInetAddress().toString());
			// Se elimina el cliente de la lista para proceder a su actualización
			Boolean eliminado = listaClientes.remove(clienteDelQueEliminar);

			String [] listaContactos = clienteDelQueEliminar.contactos.split(";"); //Se obtiene la lista actual de contactos
			for(int i=1; i<listaContactos.length; i++){//El primer elemento de la lista de contactos está vacío
				if (listaContactos[i].equals(palabrasMensaje[1])){ //Si es el contacto que se desea eliminar
					
					// Se crea una nueva lista de contactos
					String nuevaLista="";
					for (int j=0; j<i; j++){
						nuevaLista += listaContactos[j]+";";
					}
					for (int j=i+1; j<listaContactos.length; j++){
						nuevaLista += listaContactos[j]+";";
					}
					//Se asigna la nueva lista al cliente
					clienteDelQueEliminar.contactos=nuevaLista; 
					break;
				}
			}
			
			//Se vuelve a agregar el cliente (actualizado) a la lista de clientes
			Boolean agregado = listaClientes.add(clienteDelQueEliminar);
			respuesta = TiposMensaje.mRespuestaOK + ";" + "El contacto" + palabrasMensaje[1] + "ha sido eliminado de la lista de contactos de " + clienteDelQueEliminar.nombre;
			
		}else{ // El contacto solicitado no existe
			respuesta = TiposMensaje.mRespuestaFallo + ";" + "El contacto solicitado no existe.";
		}
		
		return respuesta;
	}// Fin gestionEliminarContacto
	
	/************ GESTION DE PETICIÓN DE LISTA DE CONTACTOS ********************************/
	// Gestiona las peticiones de la lista de contactos del cliente
	private String gestionListaContacto (String [] palabrasMensaje){
		String respuesta = null;

		// Formato del mensaje -> LISTARCONTACTOS
			Usuario clientePeticion = obtenerClientePorIP(socketServicio.getInetAddress().toString());			
			respuesta = TiposMensaje.mRespuestaOK + ";" + TiposMensaje.mRespuestaListaContactos + ";" + clientePeticion.contactos;
		
		return respuesta;
	}// Fin gestionEliminarContacto

	/************ GESTION DE PETICIÓN DE LISTA DE CONTACTOS ********************************/
	// Gestiona las peticiones de la lista de contactos del cliente
	private String gestionCambioApodo (String [] palabrasMensaje){
		String respuesta = null;

		// Formato del mensaje -> CAMBIOAPODO <nuevoApodo>
			Usuario clientePeticion = obtenerClientePorIP(socketServicio.getInetAddress().toString());
			if(palabrasMensaje[1]!=null){
				listaClientes.remove(clientePeticion);
				clientePeticion.apodo = palabrasMensaje[1];
				System.out.println("Cambiado apodo de "+clientePeticion.nombre+" a "+clientePeticion.apodo);
				listaClientes.add(clientePeticion);
				respuesta = TiposMensaje.mRespuestaOK + ";" + TiposMensaje.mRespuestaCambioApodo + ";";
			}else{
				respuesta = TiposMensaje.mRespuestaFallo + ";" + TiposMensaje.mRespuestaCambioApodo + ";";
			}

		
		return respuesta;
	}// Fin gestionEliminarContacto
	/************ GESTION DE PETICIÓN DE LISTA DE CONTACTOS CONECTADOS *********************/
	// Gestiona las peticiones de la lista de contactos del cliente
	private String gestionListaContactosConectados (String [] palabrasMensaje){
		String respuesta = null;

		// Formato del mensaje -> LISTARCONTACTOSCONECTADOS <usuario>
			Usuario clientePeticion = obtenerClientePorIP(socketServicio.getInetAddress().toString());
			String[] listaContactos =  clientePeticion.contactos.split(";");
			
			String listaContactosConectados = ";";
			
			for (int i=0; i<listaContactos.length; i++){
				if(usuarioConectado(listaContactos[i])){
					listaContactosConectados+=listaContactos[i] + ";";
				}
			}
			respuesta = TiposMensaje.mRespuestaOK + ";" + TiposMensaje.mRespuestaListaContactosConectados + ";" + listaContactosConectados;
		
		return respuesta;
	}// Fin gestionListaContactosConectados
	
	/************ GESTION DE PETICIÓN DE LISTA DE APODOS *********************/
	// Gestiona las peticiones de obtención de apodos de usuarios
	private String gestionListaApodos (String [] palabrasMensaje){
		String respuesta = null;
		String apodos = ";";
		// Formato del mensaje -> APODO <nombres>
		for (int j=2; j<palabrasMensaje.length; j++){
				for (int i=0; i<listaClientes.size(); i++){
					String nombre = listaClientes.elementAt(i).nombre;
					if(nombre.equals(palabrasMensaje[j])){
						apodos+=listaClientes.elementAt(i).apodo + ";";
						break;
					}
				}
		}
			respuesta = TiposMensaje.mRespuestaOK + ";" + TiposMensaje.mRespuestaApodo + ";" + apodos;
		
		return respuesta;
	}// Fin gestionListaContactosConectados

	
	/************ GESTION DE CIERRE DE CONEXIÓN ********************************/
	// Gestiona las peticiones de la lista de contactos del cliente
	private String gestionCierreConexion (String [] palabrasMensaje){
		String respuesta = null;

		// Formato del mensaje -> CERRAR
			
			Usuario clientePeticion = obtenerClientePorIP(socketServicio.getInetAddress().toString());
			clientePeticion.conectado = false;
			respuesta = TiposMensaje.mRespuestaOK + ";" + "Desconectado del servidor";
		
		return respuesta;
	}// Fin gestionEliminarContacto
	

	
	
	/**************************************************************************************************/  		
	/************ OTROS MÉTODOS ***********************************************************************/
	/**************************************************************************************************/  	
  	// Devuelve un número en función del tipo de mensaje recibido
	private int obtenerTipoMensaje(String mensaje) {
		// TODO Auto-generated method stub
		
		int result = -1; //Por defecto se pone a -1 (error)
		
		if (mensaje.equals(TiposMensaje.mBienvenidaCliente)){result = 1;}		
		if (mensaje.equals(TiposMensaje.mRegistrar)){result = 2;}
		if (mensaje.equals(TiposMensaje.mSolicitarLocalizacion)){result = 3;}
		if (mensaje.equals(TiposMensaje.mAnadirContacto)){result = 4;}
		if (mensaje.equals(TiposMensaje.mEliminarContacto)){result = 5;}
		if (mensaje.equals(TiposMensaje.mSolicitudListaContactos)){result = 6;}
		if (mensaje.equals(TiposMensaje.mSolicitudListaContactosConectados)){result = 7;}
		if (mensaje.equals(TiposMensaje.mCierreSesion)){result = 8;}
		if (mensaje.equals(TiposMensaje.mSolicitudApodo)){result = 9;}
		if (mensaje.equals(TiposMensaje.mSolicitudCambioApodo)){result = 10;}		
		
		return result;
	}// Fin obtenerTipoMensaje
	
	/**************************************************************************************************/	
	// Comprueba si un usuario está conectado
	private Boolean usuarioConectado (String nombreUsuario){
		
		Usuario cliente = obtenerCliente(nombreUsuario);
		Boolean resultado = false;
		
		for (int i=0; i< listaClientes.size();i++){
			if (cliente!=null){
				if (cliente.conectado){
					resultado = true;
					break;
				}
			}
		}// Fin for
		
		return resultado;
	}// Fin usuarioConectado
	
	/**************************************************************************************************/
	// Comprueba si un usuario tiene cuenta, es decir, se ha registrado en otra ocasión anterior.
	private Boolean usuarioTieneCuenta (String nombreUsuario){
		
		Boolean resultado = false;
		
		for (int i=0; i< listaClientes.size();i++){
			
			if (nombreUsuario.equals(listaClientes.get(i).nombre)){
				resultado = true;
				break;
			}
			
		}// Fin for
		
		return resultado;
	}// Fin usuarioTieneCuenta
	
	/**************************************************************************************************/
	// Comprueba si la contraseña introducida por un usuario se corresponde con la asignada a su cuenta
	private Boolean contrasenaCorrecta (String nombreUsuario, String contrasenaUsuario){
		Boolean resultado = false;
		
		for (int i=0; i< listaClientes.size();i++){
			
			if (nombreUsuario.equals(listaClientes.get(i).nombre) && contrasenaUsuario.equals(listaClientes.get(i).password)){
				resultado = true;
				break;
			}
		}// Fin for
		
		return resultado;
	}// Fin contrasenaCorrecta
	
	/**************************************************************************************************/
	// Devuelve un objeto Usuario de la lista de clientes que tienen cuenta
	private Usuario obtenerCliente (String nombreUsuario){
		Usuario resultado = null;
		
		for (int i=0; i< listaClientes.size();i++){
			
			if (nombreUsuario.equals(listaClientes.get(i).nombre)){
				resultado = listaClientes.get(i);
				break;
			}
			
		}// Fin for
		
		return resultado;
	}// obtenerCliente
	
	
	/**************************************************************************************************/
	// Devuelve un objeto Usuario de la lista de clientes que tienen cuenta
	private Usuario obtenerClientePorIP (String ipUsuario){
		Usuario resultado = null;
		
		for (int i=0; i< listaClientes.size();i++){
			
			if (ipUsuario.equals(listaClientes.get(i).IP)){
				resultado = listaClientes.get(i);
				break;
			}
			
		}// Fin for
		
		return resultado;
	}// obtenerClientePorIP
	
	/**************************************************************************************************/
	// Comprueba si existe un cliente en la lista de clientes
	private Boolean existeCliente (String nombreUsuario){
		Boolean resultado = false;
		
		for (int i=0; i< listaClientes.size();i++){
			
			if (nombreUsuario.equals(listaClientes.get(i).nombre)){
				resultado = true;
				break;
			}
			
		}// Fin for
		
		return resultado;
	}// obtenerCliente
	
	/**************************************************************************************************/
	// Añade un cliente a la lista de clientes
	private void anadirClienteLista (Usuario cliente){
		listaClientes.add(cliente);
	}
	
	/**************************************************************************************************/
	// Devuelve la lista de contactos de un cliente en formato string
	/*private String listaContactosToString (Usuario cliente){
		String resultado = null;
		
		for (int i=0; i<cliente.contactos.size();i++){
			resultado += cliente.contactos.get(i)+"/"; //Se añade el caracter / como escape para separar cada contacto
		}
		return resultado;
	}*/
	



}// ************* FIN DE LA CLASE ************************************************************************************************