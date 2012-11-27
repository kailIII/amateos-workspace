package org.inftel.socialwind.shared.services;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import org.inftel.socialwind.shared.domain.DeviceInfoProxy;

@ServiceName("org.inftel.socialwind.services.DeviceService")
public interface DeviceRequest extends RequestContext {

    /**
     * Registra un dispositivo para el servicio de mensajes. El dispositivo se registra asociado a
     * la cuenta del surfero que esta atuenticado.
     */
    Request<Void> register(DeviceInfoProxy device);

    /**
     * Elimina un dispositivo del servicio de mensajes. El dispositivo se elimina de la lista de
     * dispositivos de la cuenta del surfero que esta atuenticado.
     */
    Request<Void> unregister(DeviceInfoProxy device);
}