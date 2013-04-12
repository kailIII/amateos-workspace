package UI;

import java.io.IOException;
import javax.microedition.lcdui.*;

public class ListExplorerBt extends List implements CommandListener{
    
    Command explorar = new Command("Explorar", Command.OK, 0);
    Command elegir = new Command("Elegir", Command.OK, 0);
    Command atras = new Command("Atras", Command.BACK, 0);

    Intro intro;

    public ListExplorerBt(Intro intro){
        super("Lista de Servidores", Choice.IMPLICIT);

        this.addCommand(explorar);
        this.addCommand(elegir);
        this.addCommand(atras);
        this.setCommandListener(this);
        
        this.intro=intro;
    }
    public void commandAction(Command command, Displayable displayable) {
        if(command==explorar){

            //Avisamos que vamos a explorar y que va a tardar
            Alert a=new Alert("Exploramos el entorno ");
            a.setString("Esto va a tardar un rato...");
            a.setTimeout(Alert.FOREVER);
            MidletSI.midlet.switchDisplayable(a, this);

            //exploramos
            (new Thread(new explorer())).start();
            
        }//explorar
        else if(command==elegir){
            //Decimos el tipo de dispositivo
            Alert a=new Alert("Informacion");
            a.setString("Has elegido el movil "+this.getSelectedIndex());
            a.setTimeout(1500);
            MidletSI.midlet.switchDisplayable(a, this);
        }
        else if(command==atras){
            MidletSI.midlet.switchDisplayable(intro);
        }
    }
    
    class explorer implements Runnable{
        public void run(){

            //Simulamos explorar!!!
            
            //simulamos tardar 10 segundos
            try {

                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            
            //y que los moviles son "Movil 0", "Movil 1", etc...
            deleteAll();
            try {
                append("Movil 0", Image.createImage("/res/movil.PNG"));
                append("Movil 1", Image.createImage("/res/movil.PNG"));
                append("Movil 2", Image.createImage("/res/movil.PNG"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}
