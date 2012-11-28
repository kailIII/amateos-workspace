/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Jesus Ruiz Oliva
 */
@MessageDriven(mappedName = "jms/queue", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class NotificacionPolicia implements MessageListener {
    
    public NotificacionPolicia() {
    }
    
    @Override
    public void onMessage(Message message) {
       
    }
}
