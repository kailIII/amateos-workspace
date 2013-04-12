/**
 * @author Javier Medina Quero && Mª Dolores Ruiz Lozano
 *  Code generated GNU GENERAL PUBLIC LICENSE
 */

package MessageBt;

//Interfaz que recivira los mensajes de Bt ya procesados
public interface MessageEvent {
        public void arrivesMesagge(MessageBt.Message m, javax.bluetooth.RemoteDevice rd);
}
