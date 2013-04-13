import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Scanner;

public class Cliente {

    private Cliente() {}



    public static void main(String[] args) {

    	Scanner in = new Scanner(System.in);
    	Boolean acierto = false;

	try {
	    Registry registry = LocateRegistry.getRegistry("127.0.0.1");
	    Juego stub = (Juego) registry.lookup("Juego");
	    String respuesta = stub.iniciarJuego();
	    System.out.println("Respuesta servidor: "+respuesta);

	    do{
	        System.out.println("Introduce un número:\n");
	        int num = in.nextInt();
	        respuesta = stub.comprobarNumero(num);
	        System.out.println("Respuesta servidor: " + respuesta);
		if(respuesta.equals("HAS ACERTADO!")){
		    acierto = true;
		}
		if(respuesta.equals("Has superado el límite de intentos")){
		    System.out.println("Eres un burro, no mereces jugar a este pedazo de juego");
		    acierto = true;
		}
	   }while(!acierto);
	} catch (Exception e) {
	    System.err.println("Excepción en cliente: " + e.toString());
	    e.printStackTrace();
	}
	

 	
    }
}
