package UI;


import java.io.IOException;
import javax.microedition.lcdui.*;
import MessageBt.*;

public class ServerMessageBt extends Form  implements CommandListener, MessageEvent{

    Command atras = new Command("Atras", Command.BACK, 0);

    //Formulario anterior
    Intro anterior;
    
    public ServerMessageBt(Intro anterior){
        super("Servidor Mensajes Bluetooth", null);
        
        this.anterior=anterior;

        this.addCommand(atras);
        
        this.setCommandListener(this);

        //IMPORTANTE PONEMOS EL RECEPTOR DE MENSAJES
        MidletSI.receptor=new ReceiverMessageBt();
        
        //IMPORTANTE ESTO ES PARA QUE AVISE A ESTA CLASE CUANDO LLEGUE UN MENSAJE
        MidletSI.receptor.setMessageEvent(this);
    }
    
    public void commandAction(Command command, Displayable displayable) {
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
