import java.util.ArrayList; 
import java.rmi.Remote;

public interface Servidor extends Remote {

    public ArrayList<Resultado> imprimir (ArrayList<Trabajo> trabajos);

    public ArrayList<Trabajo> consultar (int idUsuario);

    public ArrayList<Resultado> cancelar (ArrayList<Trabajo> trabajos);

    public boolean reiniciar (int idUsuario);

}

