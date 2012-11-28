package org.inftel.socialwind.server.domain;

import javax.persistence.Embeddable;

/**
 * Representa una posicion en el espacion. Es usada principalmente para facilitar el traspaso de
 * informacion a la capa de presentacion. No es {@link Embeddable} debido a limitaciones de geocell.
 * 
 * @author ibaca
 * 
 */
public class Location {

    private double latitude;

    private double longitude;
    
    public Location() {
    }
    
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
