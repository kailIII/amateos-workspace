/**
 * @author Javier Medina Quero && Mª Dolores Ruiz Lozano
 *  Code generated GNU GENERAL PUBLIC LICENSE
 */

package MessageBt;

import java.io.IOException;
import javax.bluetooth.*;
import java.util.*;

//Clase que explorar el ambiente para encontrar dispositivos Bt
// que sean capaces de recibir mensajes.
public class ExplorerReceiverMessageBt implements Runnable{

        public ExplorerReceiverMessageBt(){
            (new Thread(this)).start();
        }

        //Lanza la hebra
        boolean configured=false;
        Object lock=new Object();
        public void run(){
            synchronized(lock){
                this.explorer();
                configured=true;
                lock.notifyAll();
            }
        }

        //Exclusion Mutua
        private void waitConfigured(){
            synchronized(lock){
            try {
                if (!configured) {
                    lock.wait();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            }
        }
        
        /**
         * @return Nombre de los receptores de mensajes bt
         */
        public String [] getRemoteNames(){
            waitConfigured();
            String [] ret=new String [services.devices.size()];
                 int i=0;
                for(Enumeration devicesE = services.devices.elements();devicesE.hasMoreElements();i++){
                            ServiceRecord deviceR = (ServiceRecord)(devicesE.nextElement());
                            try {
                                ret[i] = deviceR.getHostDevice().getFriendlyName(true);
                            } catch (IOException ex) {
                                ret[i] ="unknown";
                                    ex.printStackTrace();
                            }
                }
            return ret;
        }

        /**
         * @return Estructuras para enviarle un mensaje facilemente
         */
        public SenderMessageBt [] getRemoteClient(){
                waitConfigured();
                System.out.println("Return clients # "+services.devices.size());
                SenderMessageBt [] ret=new SenderMessageBt [services.devices.size()];
                int i=0;
                for(Enumeration devicesE = services.devices.elements();devicesE.hasMoreElements();i++){
                            ServiceRecord deviceR = (ServiceRecord)(devicesE.nextElement());
                           ret[i] = new SenderMessageBt(deviceR.getConnectionURL(0, false));
                }
            return ret;
        }

        /**
         * @param pos un determinado cliente
         * @return Estructura para enviarle un mensaje facilemente
         */
        public SenderMessageBt  getRemoteClient(int pos){
                waitConfigured();
                SenderMessageBt [] ret=new SenderMessageBt [services.devices.size()];
                 int i=0;
                for(Enumeration devicesE = services.devices.elements();devicesE.hasMoreElements()&&i<=pos;i++){
                    if(i==pos){
                            ServiceRecord deviceR = (ServiceRecord)(devicesE.nextElement());
                            return new SenderMessageBt(deviceR.getConnectionURL(0, false));
                    }
                }
            return null;
        }
        
        SearcherDeviceMessageServerBt devices;
        SearcherServiceMessageServerBt services;
        // Exploramos y buscamos las propiedades de los receptores de mensajes
        private void explorer(){
            try{
                    DiscoveryAgent agent=myLocalDevice.getLocalDevice().getDiscoveryAgent();

                    
                    System.out.println("Exploring devices...");
                    
                    devices = new SearcherDeviceMessageServerBt();
                        synchronized(devices){
                            agent.startInquiry(ReceiverMessageBt.type, devices);
                            try {devices.wait(); }  catch(InterruptedException e){System.out.println(e.toString());}
                            }
            
                       System.out.println("Finded devices : #"+devices.cached_devices.size());
            
                        services = new SearcherServiceMessageServerBt();
                        UUID[] u = new UUID[]{new UUID( ReceiverMessageBt.UUID, false )};
                        int attrbs[] = { 0x0100 };
        
                        for(Enumeration devicesE= devices.cached_devices.elements();devicesE.hasMoreElements();){
                            RemoteDevice deviceR = (RemoteDevice)(devicesE.nextElement());
                            System.out.println("#");
                           synchronized(services)	{
                             //  out.println("\n"+"Buscando Servicio en Device "+device.getBluetoothAddress());
                                agent.searchServices(attrbs, u, deviceR, services);
                                try {services.wait();} catch(InterruptedException e){System.out.println("\n"+e.toString());}
                                }//syncronizes
                        }//for  

            }catch(Exception ex){
                ex.printStackTrace();
            }
        }//explorer

//Buscador de dispositivos
static class SearcherDeviceMessageServerBt implements DiscoveryListener {
    public Vector cached_devices;

    public SearcherDeviceMessageServerBt() {
        cached_devices = new Vector();
    }
    
    public void deviceDiscovered( RemoteDevice btDevice, DeviceClass cod )	{
        System.out.println("Descovered Device"+btDevice);
        
        int major = cod.getMajorDeviceClass();
        if( ! cached_devices.contains( btDevice ) )	
            cached_devices.addElement( btDevice );
        
    }
    
    public void inquiryCompleted( int discType )	{
        System.out.println("Inquiry complete");
        synchronized(this){	this.notify(); }
    }
    
    public void servicesDiscovered( int transID, ServiceRecord[] servRecord )	{}
    public void serviceSearchCompleted( int transID, int respCode )	{}
}

//Buscador de propiedades del receptor de mensajes
static class SearcherServiceMessageServerBt implements DiscoveryListener	{
    public Vector devices;

    public SearcherServiceMessageServerBt(){
        devices=new Vector();
    }
    
    public void servicesDiscovered( int transID, ServiceRecord[] servRecord )	{
        devices.addElement(servRecord[0]);
        System.out.println("We found Service #"+servRecord[0]);
    }
    
    public void serviceSearchCompleted( int transID, int respCode )	{
        synchronized( this ){	this.notify();}
    }
    
    public void deviceDiscovered( RemoteDevice btDevice, DeviceClass cod ){}
    public void inquiryCompleted( int discType ){}
}

        
}
