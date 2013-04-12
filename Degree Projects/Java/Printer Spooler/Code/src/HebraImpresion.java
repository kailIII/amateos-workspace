import java.util.concurrent.locks.*;
import java.util.ArrayList;
public class HebraImpresion extends Thread {

        public Lock mutex;          //exclusion mutua para la cola de trabajos

        public Lock numTrabajos;    //semaforo para controlar si no hay trabajos en cola

        public ArrayList<Trabajo> colaTrabajos;

        public HebraImpresion () {
        }

        public void run(ArrayList<Trabajo> colaTrabajos) {
            do {
            numTrabajos.lock();     //esperamos a que halla trabajos en cola
            mutex.lock();           //exclusion mutua para acceder a la cola de trabajos
            //seleccionar el trabajo con menor índice y cambia el estado a imprimiendo
            int menor=obtenerMenorIndice(colaTrabajos);
            Trabajo t = (Trabajo) colaTrabajos.get(menor);
            t.estado=1; //cambiar estado a imprimiendo
            mutex.unlock();

            //imprime el trabajo segun indique el dato miembro 'copias'
            imprimir(t);

            mutex.lock();
            colaTrabajos.remove(menor);     //eliminar trabajo de la cola
            mutex.unlock();

            }while (true);
        }

        public int obtenerMenorIndice(ArrayList<Trabajo> colaTrabajos){
            return 0;
        }

        public boolean imprimir(Trabajo trabajo) {
            
            //realizar impresión de archivo
            return true;
        }
    }