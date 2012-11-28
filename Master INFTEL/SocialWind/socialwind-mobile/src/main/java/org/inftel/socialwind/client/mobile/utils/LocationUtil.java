package org.inftel.socialwind.client.mobile.utils;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.FloatMath;

public class LocationUtil {

    /**
     * Obtiene la localización del móvil a partir de la conexión a internet
     * 
     * @param context
     * @return
     */
    static public Location getLocation(Context context) {
        // Se intenta obtener el LocationManager
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return null;
        }

        // Se comprueba la lista completa de proveedores de localizacion
        List<String> matchingProviders = locationManager.getAllProviders();
        for (String provider : matchingProviders) {
            Location location = locationManager.getLastKnownLocation(provider);
            // Si se obtiene localizacion, se devuelve
            if (location != null) {
                return location;
            }
        }
        // Si ningun proveedor da localizacion se devuelve null
        return null;
    }

    /**
     * Calcula la distancia en kilómentros hasta un hotspot a partir de las coordenadas GPS
     * 
     * @param lat_a
     * @param lng_a
     * @param lat_b
     * @param lng_b
     * @return
     */
    static public double getDistanceToSpot(double lat_a, double lng_a, double lat_b, double lng_b) {
        float pk = (float) (180 / 3.14169);

        float a1 = (float) (lat_a / pk);
        float a2 = (float) (lng_a / pk);
        float b1 = (float) (lat_b / pk);
        float b2 = (float) (lng_b / pk);

        float t1 = FloatMath.cos(a1) * FloatMath.cos(a2) * FloatMath.cos(b1) * FloatMath.cos(b2);
        float t2 = FloatMath.cos(a1) * FloatMath.sin(a2) * FloatMath.cos(b1) * FloatMath.sin(b2);
        float t3 = FloatMath.sin(a1) * FloatMath.sin(b1);
        double distance = (double) Math.acos(t1 + t2 + t3) * 6366;

        // se redondea la distancia para obtener 2 decimales
        double distanceResult = distance * 100;
        distanceResult = Math.round(distanceResult);
        distanceResult /= 100;
        return distanceResult;
    }
}
