package UI;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class MidletSI extends MIDlet{

    static public MidletSI midlet;
    static public MessageBt.ReceiverMessageBt receptor;
    Intro intro;

    public void startMIDlet() {
        midlet=this;
        intro=new Intro();
        switchDisplayable(null, intro);
    }
    
    private void initialize(){}
    public void resumeMIDlet() {}
    public void pauseApp() {midletPaused = true;}
    public void destroyApp(boolean unconditional) {}


    
    public Display getDisplay () {
        return Display.getDisplay(this);
    }

    public void switchDisplayable(Displayable nextDisplayable) {
        this.switchDisplayable(null, nextDisplayable);
    }
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
        Display display = this.getDisplay();
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }
    }

    
    
    public void exitMIDlet() {
        switchDisplayable (null, null);
        destroyApp(true);
        notifyDestroyed();
    }

   public void startApp() {
        if (midletPaused) {
            resumeMIDlet ();
        } else {
            initialize ();
            startMIDlet ();
        }
        midletPaused = false;
    }

    boolean midletPaused;
}
