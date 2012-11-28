package org.inftel.socialwind.client.web;

import org.inftel.socialwind.client.web.mvp.ClientFactory;
import org.inftel.socialwind.client.web.mvp.SocialWindApp;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Clase principal que contiene el punto de arranque de la aplicacion. Delega en la clase
 * PanelPrincipal la creacion del Widget que se muestra en pantalla.<br>
 * 
 * @author aljiru
 * 
 */

public class SocialWind implements EntryPoint {

    /** Instancia de la clase para obtener los objetos mas pesados */
    ClientFactory clientFactory = GWT.create(ClientFactory.class);

    /** Contenedor de los distintos widgets */
    SimplePanel container;
    
    public void onModuleLoad() {
        EventBus eventBus = clientFactory.getEventBus();
        SocialwindRequestFactory swrf = clientFactory.getSWRF();
        PanelPrincipal pp = new PanelPrincipal();
        container = pp.getPanelCentral().getPanelContainer();
        new SocialWindApp(swrf, eventBus, container);
        container.setSize("100%", "100%");
        RootPanel.get().add(pp, 0, 0);
//        Utilidades.creaSesion(new Location(36.836, -2.403));
    }
}
