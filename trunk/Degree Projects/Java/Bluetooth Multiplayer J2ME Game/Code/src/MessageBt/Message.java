/**
 * @author Javier Medina Quero && Mª Dolores Ruiz Lozano
 *  Code generated GNU GENERAL PUBLIC LICENSE
 */

package MessageBt;

//Mensaje que se envian los dispositivos 
public class Message {
    
    String content;
    String BtSender;
    
    public Message(String content){
            this.content=content;
            this.BtSender=myLocalDevice.getLocalDevice().getBluetoothAddress();
    }
    
    private Message(String content, String BtSender){
            this.content=content;
            this.BtSender=BtSender;
    }
        
    public String getContent(){
        return content;
    }
    public String getSender(){
        return BtSender;
    }

    //Formato en XML del mensaje
    static String cab_Message="<Message>";
    static String end_Message="</Message>";
    static String cab_Content="<content>";
    static String end_Content="</content>";
    static String cab_Sender="<sender>";
    static String end_Sender="</sender>";
    
    //Codificacion
    public static String getXML(Message m){
        return Message.cab_Message+Message.cab_Sender+m.BtSender+Message.end_Sender+Message.cab_Content+m.content+Message.end_Content+Message.end_Message;
    }
            
    //Traduccion
    public static Message processXML(String m){
        int iniM=m.indexOf(cab_Content);
        int endM=m.indexOf(end_Content);
        if(iniM<0||endM<0)
            return null;
        
        int iniS=m.indexOf(cab_Sender);
        int endS=m.indexOf(end_Sender);
        if(iniS<0||endS<0)
            return null;

        return new Message(m.substring(iniM+(cab_Content).length(), endM),m.substring(iniS+(cab_Sender).length(), endS));
    }
    
}
