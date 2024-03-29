package org.inftel.socialwind.server.domain;

import org.inftel.socialwind.server.services.SurferService;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * Informacion de registro de dispositivos para ser notificados.
 * 
 * Un usuario puede tener varios dispositivos. El dispositivo tiene un identificador único que se
 * almacena en {@link Device#deviceId}.
 * 
 * @author ibaca
 * 
 */
@Entity
@Table(name = "devices")
public class Device extends BaseEntity {

    /**
     * Hash del id de dispositivo.
     */
    @Basic(optional = false)
    private String deviceId;

    /**
     * El ID usado como destinatario de los mensajes.
     */
    @Basic
    private String deviceRegistrationId;

    @Transient
    private Surfer surfer;

    /**
     * Surfero asociado a este dispositivo.
     */
    @Basic
    private Long surferId;

    /**
     * Tipos soportados actualmente:
     * <ul>
     * <li>(default) - ac2dm, regular froyo+ devices using C2DM protocol</li>>
     * </ul>
     * Nuevos tipos podran añadirse - por ejemplo para enviar a chrome.
     */
    @Basic
    private String type;

    /**
     * Correo de la cuenta asociada al dispositivo.
     */
    @Basic(optional = false)
    private String userEmail;

    @Version
    private Long version;

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceRegistrationId() {
        return deviceRegistrationId;
    }

    public Surfer getSurfer() {
        if (surferId == null) {
            return null;
        }
        if (surfer == null) {
            surfer = SurferService.findSurfer(getSurferId());
        }
        return surfer;
    }

    public Long getSurferId() {
        return surferId;
    }

    public String getType() {
        return type != null ? type : "";
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getVersion() {
        return version;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceRegistrationId(String deviceRegistrationId) {
        this.deviceRegistrationId = deviceRegistrationId;
    }

    public void setSurfer(Surfer surfer) {
        if (surfer == null) {
            this.surfer = null;
            this.surferId = null;
        } else {
            this.surfer = surfer;
            this.surferId = surfer.getId();
        }
    }

    public void setSurferId(Long surferId) {
        this.surferId = surferId;
        this.surfer = null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Fecha de la ultima vez que se registro este dispositivo.
     * 
     * @return
     */
    public Date getRegistrationDate() {
        return getUpdated();
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer(super.toString());
        buff.subSequence(0, buff.length());
        buff.append(", deviceId=").append(getDeviceId());
        buff.append(", deviceRegistrationId=").append(getDeviceRegistrationId());
        buff.append("]");
        return buff.toString();
    }
}
