package org.inftel.socialwind.client.web;

public class Location {

    private double latitud;
    private double longitud;

    public Location(double latitud, double longitud) {
        this.setLatitud(latitud);
        this.setLongitud(longitud);
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLongitud() {
        return longitud;
    }
}
