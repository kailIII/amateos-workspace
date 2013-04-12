/**
 * @author Javier Medina Quero && Mª Dolores Ruiz Lozano
 *  Code generated GNU GENERAL PUBLIC LICENSE
 */


package MessageBt;

import javax.bluetooth.*;
import java.util.*;

//Clase que analiza las propiedades de un dispositivo Bt
//Si es capaz de recibir mensajes, lo almacena en una 
//cache para poder enviarle mensajes rapidamente.
public class CacheReceiverMessageBt {

    Vector addresses;
    Vector BtUrl;
    
    public CacheReceiverMessageBt(){
        this.addresses=new Vector();
        this.BtUrl=new Vector();
    }
    Object lock=new Object();
    
    /**
     * Analiza un dispositovo Bt para comprobar que podemos mandarle mensajes.
     * @param rd Dispositivo remoto Bt que vamos a analizar
     * @return true si podemos mandarle mensajes, false si no. Ademas en caso
     * afirmativo lo almacenamos en la cache.
     */
    public boolean addIfServerMessageBt(RemoteDevice rd){
        
        if(getClienteFromAddress(rd)!=null)
            return true; //ya estaba en la lista
        
        DiscoveryAgent agent=myLocalDevice.getLocalDevice().getDiscoveryAgent();
        ExplorerReceiverMessageBt.SearcherServiceMessageServerBt services = new ExplorerReceiverMessageBt.SearcherServiceMessageServerBt();
        UUID[] u = new UUID[]{new UUID( ReceiverMessageBt.UUID, false )};
        int attrbs[] = { 0x0100 };
        
         synchronized(services)	{
             try {
                    agent.searchServices(attrbs, u, rd, services);
                    services.wait();
             } catch(Exception e){System.out.println("\n"+e.toString());}
         }//syncronizes
        
        if(services.devices.size()==0)
            return false;
        
        ServiceRecord device=(ServiceRecord)(services.devices.elementAt(0));
        
        synchronized(lock){
            this.addresses.addElement(rd.getBluetoothAddress());
            this.BtUrl.addElement(device.getConnectionURL(0, false));
        }
        return true;
    }
    
    /**
     * Busca en la cache el dispositivo, si lo encuentra crea un enviador de mensajes
     * @param rd Dispositivo remoto que buscamos en la cache
     * @return clase para enviar mensajes
     */
    public SenderMessageBt getClienteFromAddress(RemoteDevice rd){
        SenderMessageBt ret=null;
        synchronized(lock){
        for(Enumeration addressesE = addresses.elements(), BtUrlE=BtUrl.elements();
        addressesE.hasMoreElements()&&BtUrlE.hasMoreElements()&&ret==null;){
            String address2=(String)(addressesE.nextElement());
            String conexion=(String)(BtUrlE.nextElement());
            if(address2.equals(rd.getBluetoothAddress()))
                ret=new SenderMessageBt(conexion);
            }//for
        }//synchronized
        return ret;
    }
    
}
