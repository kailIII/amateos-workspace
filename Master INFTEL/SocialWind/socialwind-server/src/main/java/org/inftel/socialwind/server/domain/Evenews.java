package org.inftel.socialwind.server.domain;

import java.util.Date;

import org.inftel.socialwind.shared.services.EvenewsRequest;

import javax.persistence.Basic;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Los <code>evenews</code> son mensajes que se usan para comunicar eventos, noticias, etc. entre
 * los surferos. Los <code>evenews</code> suelen estar localizados.
 * 
 * Algunos ejemplos de <code>evenews</code> son; playa cambia estado a hotspot, nueva playa cercana,
 * sesion iniciada por un amigo, etc.
 * 
 * Los evenews estan directamente relacionados con las notificaciones C2DM. Este servicio usado para
 * los dispositivos notificara que el surfero dispone de nuevos evenews, y luego a traves de
 * {@link EvenewsRequest} solicitara la lista.
 * 
 * @author ibaca
 * 
 */
@Entity
@Table(name = "evenews")
public class Evenews extends BaseEntity {

    private String message;

    @Version
    private Long version;

    @Embedded
    private Location location;

    private String sender;

    @Basic(optional = false)
    private Long surferId;

    /**
     * Contenido del mensaje del evenews.
     * 
     * @return cadena que contiene el mensaje que se notifica en el evenews
     */
    public String getMessage() {
        return message;
    }

    /**
     * Contenido del mensaje del evenews. Es recomendable que sea corto, ya que el funcionamiento es
     * similar al de los tweets.
     * 
     * @param message
     *            cadena con el contenido del mensaje que se quiere transmitir
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Uso interno
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Uso interno
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Posicion que representa el origen del evenews. En caso de no estrablecer posición no debe
     * llamarse al metodo o llamarse pasando el parametro <code>null</code>. Un objeto
     * {@link Location} con posiciones nulas o 0 puede provocar comportamientos inesperados.
     * 
     * @param location
     *            objeto location que representa la posicion del origen del evenews
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Posicion que represetna el origen del evenews. Puede ser nulo cuando el evewnews es global,
     * por ejemplo la creacion de un nuevo usuario, o una noticia de nueva version para app android.
     * 
     * @return {@link Location} que representa el origen del evenews.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Surfer al que se quiere dirigir el evenews.
     * 
     * @param surferId
     */
    public void setSurferId(Long surferId) {
        this.surferId = surferId;
    }

    /**
     * Surfero al que va dirigido el evenews.
     * 
     * @return el id del surfero
     */
    public Long getSurferId() {
        return surferId;
    }

    /**
     * Descripcion de la entidad que origino la noticia.
     * 
     * @param sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    /**
     * Fecha asociada al evenews
     * @return devuelve la fecha de creacion del evenews
     */
    public Date getDate() {
        return getCreated();
    }

    /**
     * Descripción de la entidad que originó la noticia. En caso de no haberse establecido ninguna
     * entidad, se devolvera el nombre de la aplicación.
     * 
     * @return
     */
    public String getSender() {
        // FIXME esto debe ir en algun sitio de propiedades
        return (sender == null) ? "SocialWind" : sender;
    }

}
