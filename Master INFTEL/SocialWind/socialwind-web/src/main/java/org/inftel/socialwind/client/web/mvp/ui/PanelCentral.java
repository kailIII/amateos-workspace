package org.inftel.socialwind.client.web.mvp.ui;

import org.inftel.socialwind.client.web.mvp.ClientFactory;
import org.inftel.socialwind.client.web.mvp.places.HotspotListPlace;
import org.inftel.socialwind.client.web.mvp.places.IntroduccionPlace;
import org.inftel.socialwind.client.web.mvp.places.PerfilPlace;
import org.inftel.socialwind.client.web.mvp.places.PlayasListPlace;
import org.inftel.socialwind.client.web.mvp.places.SesionListPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Esta clase contiene la parte central de la pagina, compuesta por un menu lateral derecho y la
 * parte principal para la informacion en el lado izquierdo. Ademas controla los eventos que ocurren
 * en el.
 * 
 * @author aljiru
 * 
 */
public class PanelCentral extends Composite {
    private static PanelCentralUiBinder uiBinder = GWT.create(PanelCentralUiBinder.class);

    interface PanelCentralUiBinder extends UiBinder<Widget, PanelCentral> {
    }

    @UiField
    SimplePanel exampleArea;

    /** Instancia de la clase para obtener los objetos mas pesados */
    ClientFactory clientFactory = GWT.create(ClientFactory.class);

    /**
     * Metodo constructor. Inicializa el widget y establece el panel de Introduccion como panel
     * inicial
     */
    public PanelCentral() {
        initWidget(uiBinder.createAndBindUi(this));
        setWidgetToMaxWidthAndHeight();
        Window.addResizeHandler(resizeHandler);
    }

    /**
     * Maneja los eventos del boton marcado como opIntro
     * 
     * @param event
     *            Evento que refleja la pulsacion del boton de introduccion
     */
    @UiHandler("opIntro")
    void showPanelIntroduccion(ClickEvent event) {
        clientFactory.getPlaceController().goTo(new IntroduccionPlace());
    }

    /**
     * Maneja los eventos del boton Playas
     * 
     * @param event
     *            Evento que refleja la pulsacion del boton de playas
     */
    @UiHandler("opPlayas")
    void showPanelPlayas(ClickEvent event) {
        clientFactory.getPlaceController().goTo(new PlayasListPlace());
    }

    @UiHandler("opHotspots")
    void showPanelHotspots(ClickEvent event) {
        clientFactory.getPlaceController().goTo(new HotspotListPlace());
    }

    @UiHandler("opHistorico")
    void showPanelHistorico(ClickEvent event) {
        clientFactory.getPlaceController().goTo(new SesionListPlace());
    }

    /**
     * Maneja los eventos del boton Perfil
     * 
     * @param event
     *            Evento que refleja la pulsacion del boton de perfil
     */
    @UiHandler("opPerfil")
    void showPanelPerfil(ClickEvent event) {
        clientFactory.getPlaceController().goTo(new PerfilPlace());
    }

    /* ************* WIDGET CENTERING CODE *************** */
    private ResizeHandler resizeHandler = new ResizeHandler() {
        public void onResize(ResizeEvent event) {
            setWidgetToMaxWidthAndHeight();
        }
    };

    /**
     * Metodo que establece el 100% del ancho y el 70% del alto de la pantalla
     */
    private void setWidgetToMaxWidthAndHeight() {
        setWidth("100%");
        setHeight((Window.getClientHeight() * 0.8) + "px");
    }

    public SimplePanel getPanelContainer() {
        return exampleArea;
    }
}
