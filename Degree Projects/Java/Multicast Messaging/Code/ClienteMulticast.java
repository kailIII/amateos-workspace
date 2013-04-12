import java.io.*;
import java.net.*;
import java.util.*;

public class ClienteMulticast {
    public static void main(String[] args) throws IOException {
	
	
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        MulticastSocket socket = new MulticastSocket(4445); //Crea un socket multicast con el mismo puerto que el servidor

        socket.joinGroup(InetAddress.getByName("230.0.0.1")); // El cliente se une al grupo multicast

        socket.receive(packet);
        String recibido = new String(packet.getData(),0);

        System.out.println("Recibido->" + recibido);
	
	socket.leaveGroup(InetAddress.getByName("230.0.0.1")); //El cliente deja el grupo
        socket.close();
	
	while(true){} // El programa sigue ejecutándose y no se debería de recibir ningún paquete
    }
}