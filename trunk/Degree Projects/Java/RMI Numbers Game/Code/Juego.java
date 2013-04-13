import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Juego extends Remote {
    String iniciarJuego() throws RemoteException;
    String comprobarNumero(int numeroCliente) throws RemoteException;
}
