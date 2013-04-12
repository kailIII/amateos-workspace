package UI;

import java.io.IOException;
import javax.microedition.lcdui.*;

public class Intro extends Form  implements CommandListener{
    
    Command salir = new Command("Salir", Command.EXIT, 0);
    Command client = new Command("Cliente!", Command.OK, 0);
    Command servidor = new Command("Servidor!", Command.OK, 0);
    
    ListExplorerBt lista;
    ServerMessageBt formServer;
    
    public Intro(){
        super("Sistemas Inteligentes", null);
        
        try {
            this.append(Image.createImage("/res/ia.PNG"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.append("\nPrácticas\n");
        
        this.addCommand(salir);
        this.addCommand(client);
        this.addCommand(servidor);
        this.setCommandListener(this);
        
    }
    
    public void commandAction(Command command, Displayable displayable) {
        if(command==salir){
            MidletSI.midlet.exitMIDlet();
        }
        else if(command==client){
            if(lista==null)
                lista=new ListExplorerBt(this);
            MidletSI.midlet.switchDisplayable(lista);
        }
        else if(command==servidor){
            if(formServer==null)
                formServer=new ServerMessageBt(this);
            MidletSI.midlet.switchDisplayable(formServer);
        }
    }
}
