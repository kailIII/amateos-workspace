package UI;

import java.io.IOException;
import javax.microedition.lcdui.*;
import MessageBt.*;

public class PrintMessageBt extends Form  implements CommandListener, MessageEvent{

    Command atras = new Command("Atras", Command.BACK, 0);
    Command hola = new Command("Hola", Command.OK, 0);
    //Servidor al que nos hemos conectado 
    SenderMessageBt server;
    //Formulario anterior
    ListExplorerBt anterior;
    
    public PrintMessageBt(SenderMessageBt server, ListExplorerBt anterior){
        super("Mensajes Bluetooth", null);
        
        this.server=server;
        this.anterior=anterior;

        this.addCommand(atras);
        this.addCommand(hola);
        
        this.setCommandListener(this);
        
        //IMPORTANTE ESTO ES PARA QUE AVISE A ESTA CLASE CUANDO LLEGUE UN MENSAJE
        MidletSI.receptor.setMessageEvent(this);
    }
    
    public void commandAction(Command command, Displayable displayable) {
        if(command==hola){
            
        }
        if(command==atras){
            //Volvemos a tras
            MidletSI.midlet.switchDisplayable(anterior);
        }
    }

    //Aqui llegan los mensajes automaticamente
    public void arrivesMesagge(MessageBt.Message m, javax.bluetooth.RemoteDevice rd){
        try {
            
            
            this.append("Ha llegado el mensaje " + m.getContent() + " de " + rd.getFriendlyName(false));
            
            
        } catch (IOException ex) {ex.printStackTrace();}
    }//arrivesMessage
}
