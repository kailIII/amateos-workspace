import java.util.ArrayList;
import java.util.concurrent.locks.*;

public class Spooler implements Servidor {

    public ArrayList<Trabajo> colaTrabajos;

    public Lock mutex;          //exclusion mutua para la cola de trabajos

    public Lock numTrabajos;    //semaforo para controlar si no hay trabajos en cola

    public HebraImpresion hebraImpresion;

    final long idAdministrador=0;

    public Spooler () {
    }

    public ArrayList<Resultado> imprimir (ArrayList<Trabajo> trabajos) {
        ArrayList<Resultado> resultado=new ArrayList();
        mutex.lock();       //Exclusion mutua para el encolado
        for (int i=0; i<trabajos.size(); i++) {
            Trabajo t = (Trabajo) trabajos.get(i);
            colaTrabajos.add(t);
            t.estado=0;     //0->encolado
            t.idTrabajo=colaTrabajos.size();        //asignamos id de trabajo
            resultado.add(new Resultado(t.idTrabajo,true));
            numTrabajos.unlock();       //indicamos que hay trabajos en cola
        }
        mutex.unlock();
        return resultado;
    }

    
    public ArrayList<Trabajo> consultar (int idUsuario) {
        ArrayList<Trabajo> trabajos=new ArrayList();
        mutex.lock();
        //recorremos la cola de trabajos en busca de los trabajos pertenecientes al usuario
        trabajos=buscarTrabajos(idUsuario);
        mutex.unlock();
        return trabajos;
    }

    public ArrayList<Resultado> cancelar (ArrayList<Trabajo> trabajos) {
        ArrayList<Resultado> resultado=new ArrayList();
        mutex.lock();
        for (int i=0; i<trabajos.size(); i++) {
            Trabajo t = (Trabajo) trabajos.get(i);
            if (colaTrabajos.contains(t) && t.estado==0) {
                colaTrabajos.remove(t);
                numTrabajos.unlock();
                resultado.add(new Resultado(t.idTrabajo,true));
                t.estado=2; //2-> cancelado
            }
        }
        mutex.unlock();
        return null;
    }

    public boolean reiniciar (int idUsuario) {
        boolean operacion=false;
        mutex.lock();
        if (idUsuario==idAdministrador) {
            for (int i=0; i<colaTrabajos.size(); i++) {
                colaTrabajos.remove(i);
                numTrabajos.unlock();
            }
           operacion=true;
        }
        mutex.unlock();
        return operacion;
    }

   

    

    

}

