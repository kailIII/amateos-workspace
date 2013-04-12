/**
 * @author Javier Medina Quero && Mª Dolores Ruiz Lozano
 *  Code generated GNU GENERAL PUBLIC LICENSE
 */

package MessageBt;

import javax.bluetooth.*;

//Dispositivo Bt desde el que se ejecuta el programa
public class myLocalDevice {

    //Dispositivo Local
        private static LocalDevice local;
        static LocalDevice getLocalDevice(){
            if(local==null){
                try {
                    local = LocalDevice.getLocalDevice();
                    local.setDiscoverable(ReceiverMessageBt.type);
                } catch (BluetoothStateException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
            return local;
        }//local

        static public String getMyName(){
            return getLocalDevice().getFriendlyName();
        }
}
