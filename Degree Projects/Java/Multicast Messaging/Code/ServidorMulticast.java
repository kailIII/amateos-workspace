import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class ServidorMulticast {
    public static void main(String[] args) throws IOException {

        int j=0; // Número de paquete multicast
	
        while(true){
        // Creación de socket multicast
        MulticastSocket socket = new MulticastSocket();

        
        // Envío de paquete multicast
        
        byte[] buf = new byte[256];
        String envio = new String("Soy el paquete multicast numero -> "+j);
        buf = envio.getBytes();
        InetAddress address = InetAddress.getByName("230.0.0.1"); //La dirección del grupo multicast es 230.0.0.1
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445); //El puerto usado es el 4445
        socket.send(packet);
	System.out.println("Enviado paquete"+j);
        j++;
        
        socket.close();
	try
	{
		Thread.sleep(1000); // Se deja un tiempo de espera de 1 segundo entre cada envío de paquete
	}
	catch(InterruptedException e)
	{
		e.printStackTrace();
	} 
	if(j>10) // Se envían 10 paquetes. Una vez enviados se sale del programa
	break;
        }
        
    }
}