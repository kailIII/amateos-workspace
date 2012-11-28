package org.inftel.socialwind.client.web.mvp;

import org.inftel.socialwind.client.web.mvp.event.AppBusyEvent;
import org.inftel.socialwind.client.web.mvp.event.AppBusyHandler;
import org.inftel.socialwind.client.web.mvp.event.AppFreeEvent;
import org.inftel.socialwind.client.web.mvp.event.AppFreeHandler;
import org.inftel.socialwind.client.web.mvp.places.IntroduccionPlace;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Clase encargada de manejar los eventos de cambio en el historico de la pagina. Cada vez que
 * cambia el historico, esta clase se encarga de cargar el Presenter correspondiente. A parte, cada
 * vez que esten cambiando los datos pone una etiqueta de ocupado.
 * 
 * @author aljiru
 * 
 */
public class SocialWindApp {

    EventBus eventBus;
    ClientFactory clientFactory = GWT.create(ClientFactory.class);
    SocialwindRequestFactory swrf;
    Label busyLabel = new Label("BUSY");

    private Place defaultPlace = new IntroduccionPlace();

    /**
     * Constructor de la clase. Prepara la aplicacion para manejar los eventos.
     * 
     * @param swrf
     *            Objeto necesario para realizar la comunicacion con el servidor
     * @param eventBus
     *            Bus encargado de gestionar los distintos eventos de la aplicacion
     */
    public SocialWindApp(SocialwindRequestFactory swrf, EventBus eventBus,
            AcceptsOneWidget appWidget) {
        PlaceController placeController = clientFactory.getPlaceController();

        // Start ActivityManager for the main widget with our ActivityMapper
        ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(appWidget);

        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        AppPlacesHistoryMapper historyMapper = GWT.create(AppPlacesHistoryMapper.class);
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, defaultPlace);

        // Goes to the place represented on URL else default place
        historyHandler.handleCurrentHistory();

        this.eventBus = eventBus;
        this.swrf = swrf;
        bind();

        // Implementa la etiqueta "Busy" que debe ser mostrada cuando la aplicacion esta ocupada
        RootPanel.get().add(busyLabel, 200, 20);
        busyLabel.setVisible(false);
        busyLabel.getElement().getStyle().setBackgroundColor("#ff5555");
        busyLabel.getElement().getStyle().setColor("#ffffff");
    }

    /**
     * Metodo para manejar los eventos del historico
     */
    private void bind() {
        // Escucha eventos de tipo AppBusy en el bus de eventos
        eventBus.addHandler(AppBusyEvent.getType(), new AppBusyHandler() {
            public void onAppBusyEvent(AppBusyEvent event) {
                busyLabel.setVisible(true);
            }
        });

        // Escucha eventos de tipo AppFree en el bus de eventos
        eventBus.addHandler(AppFreeEvent.getType(), new AppFreeHandler() {
            public void onAppFreeEvent(AppFreeEvent event) {
                busyLabel.setVisible(false);
            }
        });
    }

}
