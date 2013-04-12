import java.net.*;
import java.util.Vector;
import java.io.*;

public class Servidor {
  // Atributos de la clase
  static ServerSocket socketAtencion;
  static Socket socketServicio;
  static final int PUERTO=9090;
  static Servicio servicio; // Hebras para que el servicio sea concurrente
  static Vector <Usuario> listaClientes; // Lista de todos los clientes que pueden conectarse
  static Vector <Usuario> listaClientesConectados; // Lista de clientes conectados
  
  public Servidor() {
  }

  static public void main (String args[]) {
    boolean salir = false;
    
    listaClientes = new Vector <Usuario> ();
    
    ///////////////////////////////////////////////
    // USUARIOS DE PRUEBAAAAAAAAAAAAAAAA
	Usuario emp = new Usuario ("emp@andromessenger","pass","emp");
	Usuario user1 = new Usuario ("mati@andromessenger","pass","matildilla","10.0.2.2");
	Usuario user2 = new Usuario ("ignacio@andromessenger","pass","nacho","192.168.1.2");
	Usuario user3 = new Usuario ("pablo@andromessenger","pass","pabloskic","192.168.1.3");
	Usuario user4 = new Usuario ("sanchez@andromessenger","pass","san","192.168.1.4");
	
	user1.conectado = true;
	user2.conectado = true;
	
	emp.agregarContacto(user1.nombre);
	emp.agregarContacto(user2.nombre);
	emp.agregarContacto(user3.nombre);
	emp.agregarContacto(user4.nombre);

	listaClientes.add(emp);
	listaClientes.add(user1);
	listaClientes.add(user2);
	listaClientes.add(user3);
	listaClientes.add(user4);

	///////////////////////////////////////////
    
    // 1 - Se abre el socket en modo "escucha".
    try {
      socketAtencion = new ServerSocket (PUERTO);
      System.out.println("Abierto socket. Escuchando...");
    } catch (IOException e) {
      System.err.println ("Error: no se puede abrir el puerto indicado.");
      System.exit(-2);
    }

    // Bucle para aceptar conexiones. Por cada conexión aceptada se creará una
    // hebra a la que se le pasará el socket para cursar el servicio.
    do {

      // 2 - Se bloquea la hebra actual en "accept" esperando una solicitud de
      //     conexión. Se devuelve un nuevo socket con la conexión establecida.
      try {
        socketServicio = socketAtencion.accept();
        System.out.println("Cliente conectado. Se va a crear la hebra de atención al cliente.");
        // Se lanza una hebra para que sirva a este cliente por "socketServicio".
        new Servicio(socketServicio, listaClientes).start();
      } catch (IOException e) {
        System.err.println("Error: no se pudo aceptar la solicitud de una conexión.");
      }

    } while (!salir);
  } // Fin MAIN*******************************************

} // Fin CLASE********************************************