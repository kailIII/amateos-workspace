/**
 * @author Javier Medina Quero && Mª Dolores Ruiz Lozano
 *  Code generated GNU GENERAL PUBLIC LICENSE
 */

package MessageBt;

import javax.bluetooth.*;
import javax.microedition.io.*;

//Enviador de mensajes Bt
public class SenderMessageBt{
    
    String url;
    
    SenderMessageBt(String url){
        this.url=url;
        System.out.println("ClientMessageBt for conexion:"+url);
    }
    
    public void sendMessage(Message m){
        Thread t=new Thread(new senderMessageBt(m));
        t.start();
    }
    
    public void sendMessage(String s){
        Thread t=new Thread(new senderMessageBt(new Message(s)));
        t.start();
    }

    class senderMessageBt implements Runnable{
        Message m;
        
        senderMessageBt(Message m){
            this.m=m;
        }
        
        public void run(){
            try{
                
                L2CAPConnection con = (L2CAPConnection) Connector.open(url);
                int size_buf=256;
                try{
                    size_buf=con.getTransmitMTU();
                } catch(Exception e) {size_buf=256;};
                //si.BtTest.midlet.println("Size Transmiter Buffer: "+size_buf);
                String send=Message.getXML(m);
                for(int i=0;i<send.length();i+=size_buf)
                    con.send(send.substring(i, (i+size_buf<send.length())?i+size_buf:send.length()).getBytes());
                con.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }//run
    }//sender
}
