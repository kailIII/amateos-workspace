package org.inftel.socialwind.server.services;

import com.google.android.c2dm.server.C2DMessaging;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.Locator;

import static org.inftel.socialwind.server.services.DeviceService.ANDROID_DEVICE_TYPE;

import org.inftel.socialwind.server.domain.Device;
import org.inftel.socialwind.server.domain.Evenews;
import org.inftel.socialwind.server.domain.Surfer;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

/**
 * Este servicio es principalmente la fachada del servicio {@link C2DMessaging} y permite enviar
 * mensajes C2DM a los dispositivos registrados en el sistema. Los dispositivos se registran por
 * medio del servicio {@link DeviceService}.
 * 
 * @author ibaca
 * 
 * @see DeviceService
 * 
 */
public class CloudToDeviceService {

    private static final Logger log = Logger.getLogger(CloudToDeviceService.class.getName());

    /**
     * Devuelve el {@link ServletContext} asociado al request actual.
     * 
     * TODO esto es totalmente dependiente de RequestFactory, y por tanto deberia inyectarse a los
     * servicios en alguna etapa previa, por ejemplo a traves de un filtro servlet, a traves de
     * inyeccion como Guice o a traves del ciclo de vida requestFactory, como en {@link Locator}.
     * 
     * @return contexto actual del servlet en ejecucion
     */
    private static ServletContext requestContext() {
        return RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext();
    }

    /**
     * Envia el mensaje a partir de la informacion del evenews. El mensaje será enviado a todos los
     * dispositivos que esten registrados en el surfero que tenga establecido el evenews. Por tanto,
     * si el surfero no tiene registrados dispositivos, no se enviara ningun mensaje.
     * 
     * @param evenews
     */
    public static void sendMessage(Evenews evenews) {
        Surfer surfer = SurferService.findSurfer(evenews.getSurferId());
        String sender = evenews.getSender();
        String message = evenews.getMessage();
        String collapseKey = "" + message.hashCode();

        C2DMessaging push = C2DMessaging.get(requestContext());

        List<Device> registrations = DeviceService.findDevicesBySurfer(surfer);

        // Un mensaje para cada dispositivo
        for (Device device : registrations) {
            if (!ANDROID_DEVICE_TYPE.equals(device.getType())) {
                continue; // user-specified device type
            }

            // Se envia el mensaje
            String registrationId = device.getDeviceRegistrationId();
            if (doSendViaC2dm(message, sender, push, collapseKey, registrationId)) {
                log.info("evenews (" + evenews + ") notificado en el dispositivo (" + device + ")");
            } else {
                // TODO investigar porque esto puede fallar, y registrar mejor la causa
                log.warning("evenews (" + evenews + ") fallido en el dispositivo (" + device + ")");
            }
        }
    }

    private static boolean doSendViaC2dm(String message, String sender, C2DMessaging push,
            String collapseKey, String registationId) {
        // Trim message if needed.
        if (message.length() > 1024) {
            log.warning("se ha intentado enviar un mensaje de mas de 1024 caracteres");
            message = message.substring(0, 1000) + "[...]";
        }

        return push.sendNoRetry(registationId, collapseKey, "sender", sender, "message", message);
    }
}
