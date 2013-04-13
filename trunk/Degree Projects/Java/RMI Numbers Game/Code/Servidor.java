import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.Random;
	
public class Servidor implements Juego {
	
    int numero;
    int intentos=0;
    int maximoIntentos=5;

    public Servidor() {}

    public String iniciarJuego() {
	intentos = 0;
	Random aleatorio = new Random();
	numero = aleatorio.nextInt(10);
	System.out.println("El número a acertar es: "+numero);
	return "Número aleatorio listo!";
    }

    public String comprobarNumero(int numeroCliente){
	String respuesta = null;
	intentos++;

	if(intentos<=maximoIntentos){
	    if(numero<numeroCliente){
	        respuesta = "El número que buscas es más BAJO. Intentos restantes-> "+(maximoIntentos-intentos);
            }else if(numero>numeroCliente){
	        respuesta = "El número que buscas es más ALTO. Intentos restantes-> "+(maximoIntentos-intentos);
	    }else if(numero==numeroCliente){
	        respuesta = "HAS ACERTADO!";
	    }
	}else{
	    respuesta = "Has superado el límite de intentos";
	}
	return respuesta;
    }

    public static void main(String args[]) {
	
	try {
	    Servidor obj = new Servidor();
	    Juego stub = (Juego) UnicastRemoteObject.exportObject(obj, 0);

	    Registry registry = LocateRegistry.getRegistry();
	    registry.bind("Juego", stub);

	    System.err.println("Servidor listo");
	} catch (Exception e) {
	    System.err.println("Excepción en el servidor: " + e.toString());
	    e.printStackTrace();
	}
    }
}
