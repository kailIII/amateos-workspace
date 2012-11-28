package org.inftel.socialwind.server.services;

import static org.inftel.socialwind.server.services.SurferService.currentSurfer;

import org.inftel.socialwind.server.domain.Device;
import org.inftel.socialwind.server.domain.DeviceInfo;
import org.inftel.socialwind.server.domain.Surfer;
import org.inftel.socialwind.server.domain.DomainContext;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class DeviceService {
    
    private static final Logger log = Logger.getLogger(DeviceService.class.getName());

    private static final String SELECT_BY_SURFER = "select o from Device o where surferId = :surferId";

    public static final String ANDROID_DEVICE_TYPE = "ac2dm";

    private static final int MAX_DEVICES = 5;

    public static final EntityManager entityManager() {
        return DomainContext.get();
    }

    public static void register(DeviceInfo device) {
        log.info("dando de alta el dispositivo " + device);
        doRegister(device.getRegistrationId(), ANDROID_DEVICE_TYPE, device.getId(), currentSurfer());
    }

    public static void unregister(DeviceInfo device) {
        log.info("dando de baja el dispositivo " + device);
        doUnregister(device.getRegistrationId(), currentSurfer());
    }

    private static void doRegister(String deviceRegistrationId, String deviceType, String deviceId,
            Surfer surfer) {
        List<Device> registrations = findDevicesBySurfer(surfer);

        if (registrations.size() > MAX_DEVICES) {
            log.info("numero de dispositivos de usuario ha superado " + MAX_DEVICES);
            Device oldest = registrations.get(0);
            long oldestTime = oldest.getRegistrationDate().getTime();
            for (int i = 1; i < registrations.size(); i++) {
                if (registrations.get(i).getRegistrationDate().getTime() < oldestTime) {
                    oldest = registrations.get(i);
                    oldestTime = oldest.getRegistrationDate().getTime();
                }
            }
            remove(oldest);
        }

        Device device = null;

        String deviceIdHash = Long.toHexString(Math.abs(deviceId.hashCode()));
        for (Device d : registrations) {
            if (deviceIdHash.equals(d.getDeviceId())) {
                device = d; // ya esta registrado
            }
        }

        if (device == null) {
            // se crea un dispositivo nuevo
            device = new Device();
            device.setDeviceId(deviceIdHash);
            device.setDeviceRegistrationId(deviceRegistrationId);
            device.setType(deviceType);
            persist(device);
            log.info("creado dispositivo " + device);
        } else {
            // se actualiza el registro del dispositivo existente
            device.setDeviceRegistrationId(deviceRegistrationId);
            log.info("actualizado dispositivo " + device);
        }
    }

    private static void doUnregister(String deviceRegistrationId, Surfer surfer) {
        List<Device> registrations = findDevicesBySurfer(surfer);
        for (Device device : registrations) {
            if (device.getDeviceRegistrationId().equals(deviceRegistrationId)) {
                remove(device);
                log.info("borrado dispositivo " + device);
            }
        }
    }

    public static List<Device> findDevicesBySurfer(Surfer surfer) {
        EntityManager em = entityManager();
        Query query = em.createQuery(SELECT_BY_SURFER);
        query.setParameter("surferId", surfer.getId());
        @SuppressWarnings("unchecked")
        List<Device> registrations = query.getResultList();
        return registrations;
    }

    public static void persist(Device instance) {
        EntityManager em = entityManager();
        em.persist(instance);
        em.refresh(instance);
    }

    public static void remove(Device instance) {
        EntityManager em = entityManager();
        Device attached = em.find(Device.class, instance.getId());
        em.remove(attached);
    }
}
