/**
 * @author Javier Medina Quero && Mª Dolores Ruiz Lozano
 *  Code generated GNU GENERAL PUBLIC LICENSE
 */

package MessageBt;

import javax.bluetooth.*;
import javax.microedition.io.*;

//Implementa el Servidore de Mensajes Bt
public class ReceiverMessageBt implements Runnable{
    
    MessageEvent handler;

        public ReceiverMessageBt(){
            this(null);
        }

        public ReceiverMessageBt(MessageEvent handler){
            this.handler=handler;
            t=new Thread(this);
            t.start();
        }
        
        public void setMessageEvent(MessageEvent handler){
            this.handler=handler;
        }
        
        static int type=DiscoveryAgent.GIAC;
        static String UUID="00000000000010008000006d6167656e";
        private String getBtURL(){
            return "btl2cap://localhost:"+UUID+";name=ServerMessageBt";
        }
        
        boolean end=false;
        Thread t;
        public void stop(){
            end=true;
            t.interrupt();
        }
        //A la escucha y espera de mensajes
        public void run(){
            try {

                LocalDevice local=LocalDevice.getLocalDevice();
                local.setDiscoverable(type);

                //si.BtTest.midlet.println("Waiting conexions...");
                L2CAPConnectionNotifier notifier = (L2CAPConnectionNotifier) Connector.open(this.getBtURL());
                do{
                    L2CAPConnection connection = notifier.acceptAndOpen();

                    RemoteDevice rd = RemoteDevice.getRemoteDevice(connection);
                    rd.getFriendlyName(false);
                    //si.BtTest.midlet.println("New client connection... " +   rd.getFriendlyName(false));

                    byte buffer[] = null;
                    try{
                        buffer=new byte[connection.getReceiveMTU()];
                    } catch(Exception e) {buffer=new byte[256];}

                    
                    String message="";
                    boolean good=true;
                    do{
                  //      si.BtTest.midlet.println("Vamos a leer");
                        if(connection.ready()){
                            int tam=connection.receive(buffer);
                            message+=new String(buffer, 0, tam);
                    //        si.BtTest.midlet.println("\t The partial message is @"+message+"@");
                        }else
                            Thread.sleep(100);
                        if(!message.startsWith(Message.cab_Message)){
                    //        si.BtTest.midlet.println("The message is not correct");
                            good=false;
                            break;
                        }
                        if(message.endsWith(Message.end_Message)){
                            break;
                        }
                    } while(true);
                    connection.close();
                    Message msg=Message.processXML(message);
                    if(!msg.getSender().equals(rd.getBluetoothAddress())){
                        //si.BtTest.midlet.println("Sender is a liar!!");
                        good=false;
                    }
                    //si.BtTest.midlet.println("The total message is "+message);
                    connection.close();
                    if(good && this.handler!=null)
                        this.handler.arrivesMesagge(msg,rd);
                }while(!end);        
            } catch(Exception e) {
                e.printStackTrace();
            }
        }//run

}
