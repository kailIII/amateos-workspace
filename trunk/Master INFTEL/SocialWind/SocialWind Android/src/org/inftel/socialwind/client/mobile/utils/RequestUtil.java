package org.inftel.socialwind.client.mobile.utils;

import java.util.ArrayList;
import java.util.List;

import org.inftel.socialwind.client.mobile.models.HotspotModel;
import org.inftel.socialwind.client.mobile.models.SessionModel;
import org.inftel.socialwind.client.mobile.models.SurferModel;
import org.inftel.socialwind.client.mobile.vos.Session;
import org.inftel.socialwind.client.mobile.vos.Spot;
import org.inftel.socialwind.shared.domain.SessionProxy;
import org.inftel.socialwind.shared.domain.SpotProxy;
import org.inftel.socialwind.shared.domain.SurferProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;
import org.inftel.socialwind.shared.services.SpotRequest;
import org.inftel.socialwind.shared.services.SurferRequest;

import android.location.Location;
import android.util.Log;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class RequestUtil {

    private static final String TAG = "Request";
    private SocialwindRequestFactory requestFactory;

    /**
     * Constructor
     * 
     * @param requestFactory
     */
    public RequestUtil(SocialwindRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    /**
     * Envía la localización del surfer al servidor
     * 
     * @param location
     */
    public void updateLocation(Location location) {

        // Envío de la localización al servidor
        final SurferRequest updateLocationRequest = requestFactory.surferRequest();
        Log.i(TAG,
                "Enviando petición updateLocation ->" + location.getLatitude() + ", "
                        + location.getLongitude());
        updateLocationRequest.updateSurferLocation(location.getLatitude(), location.getLongitude())
                .fire(new Receiver<SessionProxy>() {

                    @Override
                    public void onFailure(ServerFailure error) {
                        Log.e(TAG, "Fallo al enviar la localización: " + error.getMessage());
                    }

                    @Override
                    public void onSuccess(SessionProxy arg0) {
                        Log.d(TAG, "Localización enviada con éxito");
                    }
                });
    }

    /**
     * Obtiene el SurferModel a partir de la información del servidor
     * 
     * @param surfer
     * @return Devuelve el modelo del surfer
     */
    public SurferModel getSurfer(final SurferModel surfer) {

        SurferRequest request = requestFactory.surferRequest();
        Log.i(TAG, "Enviando petición currentSurfer");

        request.currentSurfer().fire(new Receiver<SurferProxy>() {
            @Override
            public void onFailure(ServerFailure error) {
                Log.e(TAG, "Fallo al obtener el surfer: " + error.getMessage());
            }

            @Override
            public void onSuccess(SurferProxy surferProxy) {
                Log.d(TAG, "Surfer obtenido con éxito");

                surfer.setDisplayName(surferProxy.getDisplayName());
                surfer.setFullName(surferProxy.getFullName());
                surfer.setGravatarHash(surferProxy.getGravatarHash());

            }
        });

        return surfer;
    }

    /**
     * Obtiene el HotspotModel a partir de la información del servidor
     * 
     * @param hotspots
     * @param location
     * @return Devuelve el modelo de los hotspots
     */
    public HotspotModel getHotspots(final HotspotModel hotspots, Location location) {

        SpotRequest request = requestFactory.spotRequest();
        Log.i(TAG, "Enviando petición find hotspots");

        // si la localización ha sido obtenida -> obtener hotspots
        if (location != null) {
            request.findNearbyHotSpots(location.getLatitude(), location.getLongitude())
                    .with("location").fire(new Receiver<List<SpotProxy>>() {

                        @Override
                        public void onFailure(ServerFailure error) {
                            Log.e(TAG, "Fallo al obtener los hotspots: " + error.getMessage());
                        }

                        @Override
                        public void onSuccess(List<SpotProxy> spotProxyList) {
                            Log.d(TAG, "Hotspots obtenidos con éxito");

                            // se agregan los datos obtenidos al modelo
                            if (spotProxyList.size() > 0) {
                                ArrayList<Spot> spots = new ArrayList<Spot>();
                                Spot s;

                                for (SpotProxy spotProxy : spotProxyList) {

                                    // Se crea el objeto Spot
                                    s = new Spot();
                                    s.setName(spotProxy.getName());
                                    s.setDescription(spotProxy.getDescription());
                                    s.setLatitude(spotProxy.getLocation().getLatitude());
                                    s.setLongitude(spotProxy.getLocation().getLongitude());
                                    s.setHot(spotProxy.getHot());
                                    s.setImgUrl(spotProxy.getImgUrl());
                                    s.setSurferCount(spotProxy.getSurferCount());
                                    s.setSurferCurrentCount(spotProxy.getSurferCurrentCount());

                                    // Se agrega el spot a la lista de spots
                                    spots.add(s);
                                }

                                hotspots.setHotspots(spots);
                            }
                        }

                    });
        } else { // si no se puede obtener la localización -> obtener todos los spots
            request.findAllSpots().with("location").fire(new Receiver<List<SpotProxy>>() {

                @Override
                public void onFailure(ServerFailure error) {
                    Log.e(TAG, "Fallo al obtener los hotspots: " + error.getMessage());
                }

                @Override
                public void onSuccess(List<SpotProxy> spotProxyList) {
                    Log.d(TAG, "Hotspots obtenidos con éxito");

                    // se agregan los datos obtenidos al modelo
                    if (spotProxyList.size() > 0) {
                        ArrayList<Spot> spots = new ArrayList<Spot>();
                        Spot s;

                        for (SpotProxy spotProxy : spotProxyList) {

                            // Se crea el objeto Spot
                            s = new Spot();
                            s.setName(spotProxy.getName());
                            s.setDescription(spotProxy.getDescription());

                            try {
                                double latitude = spotProxy.getLocation().getLatitude();
                                double longitude = spotProxy.getLocation().getLongitude();
                                if (!Double.isNaN(latitude)) {
                                    s.setLatitude(latitude);
                                }

                                if (!Double.isNaN(longitude)) {
                                    s.setLongitude(longitude);
                                }
                            } catch (NullPointerException e) {
                                Log.d(TAG, "No existen coordenadas de localización para el usuario");
                            }

                            s.setHot(spotProxy.getHot());
                            s.setImgUrl(spotProxy.getImgUrl());
                            s.setSurferCount(spotProxy.getSurferCount());
                            s.setSurferCurrentCount(spotProxy.getSurferCurrentCount());

                            // Se agrega el spot a la lista de spots
                            spots.add(s);
                        }

                        hotspots.setHotspots(spots);
                    }
                }

            });
        }

        return hotspots;
    }

    /**
     * Obtiene el SessionModel a partir de la información del servidor
     * 
     * @param sessions
     * @return Devuelve el modelo de las sesiones
     */
    public SessionModel getSessions(final SessionModel sessions) {

        SurferRequest request = requestFactory.surferRequest();
        Log.i(TAG, "Enviando petición getSessions");

        request.findSessions().with("spot.location").fire(new Receiver<List<SessionProxy>>() {

            @Override
            public void onFailure(ServerFailure error) {
                Log.e(TAG, "Fallo al obtener las sesiones: " + error.getMessage());
            }

            @Override
            public void onSuccess(List<SessionProxy> sessionProxyList) {
                Log.d(TAG, "Sesiones obtenidas con éxito");

                // se agregan los datos obtenidos al modelo
                if (sessionProxyList.size() > 0) {
                    ArrayList<Session> sessionsList = new ArrayList<Session>();
                    Session s;
                    Spot sp;

                    for (SessionProxy sessionProxy : sessionProxyList) {

                        // Se crean los objetos Session y Spot
                        s = new Session();
                        sp = new Spot();
                        SpotProxy proxy = sessionProxy.getSpot();

                        sp.setName(proxy.getName());
                        sp.setDescription(proxy.getDescription());
                        sp.setLatitude(proxy.getLocation().getLatitude());
                        sp.setLongitude(proxy.getLocation().getLongitude());
                        sp.setHot(proxy.getHot());
                        sp.setImgUrl(proxy.getImgUrl());
                        sp.setSurferCount(proxy.getSurferCount());
                        sp.setSurferCurrentCount(proxy.getSurferCurrentCount());

                        s.setSpot(sp);
                        s.setStart(sessionProxy.getStart());
                        s.setEnd(sessionProxy.getEnd());

                        // Se agrega el spot a la lista de spots
                        sessionsList.add(s);
                    }

                    // se agrega la lista de sesiones al modelo
                    sessions.setSessions(sessionsList);
                }

            }

        });

        return sessions;
    }

    /**
     * Modifica los nombres del surfer en el servidor
     * 
     * @param displayName
     * @param fullName
     */
    public void editSurfer(final String displayName, final String fullName) {

        // Se obtiene el surfer que se quiere editar
        SurferRequest request = requestFactory.surferRequest();

        request.currentSurfer().fire(new Receiver<SurferProxy>() {
            @Override
            public void onFailure(ServerFailure error) {
                Log.e(TAG, "Fallo al obtener el surfer: " + error.getMessage());
            }

            @Override
            public void onSuccess(SurferProxy surferProxy) {
                Log.d(TAG, "Surfer obtenido con éxito");

                SurferRequest editRequest = requestFactory.surferRequest();
                SurferProxy surferEdit = editRequest.edit(surferProxy);
                surferEdit.setDisplayName(displayName);
                surferEdit.setFullName(fullName);
                editRequest.fire(new Receiver<Void>() {

                    @Override
                    public void onFailure(ServerFailure error) {
                        Log.e(TAG, "Fallo al editar el surfer: " + error.getMessage());
                    }

                    @Override
                    public void onSuccess(Void arg0) {
                        Log.e(TAG, "Surfer editado con éxito");
                    }
                });
            }
        });

    }
}
