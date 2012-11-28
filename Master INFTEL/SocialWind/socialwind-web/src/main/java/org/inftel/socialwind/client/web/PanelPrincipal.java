package org.inftel.socialwind.client.web;

import org.inftel.socialwind.client.web.mvp.ClientFactory;
import org.inftel.socialwind.client.web.mvp.event.AppBusyEvent;
import org.inftel.socialwind.client.web.mvp.event.AppFreeEvent;
import org.inftel.socialwind.client.web.mvp.places.PerfilPlace;
import org.inftel.socialwind.client.web.mvp.ui.PanelCentral;
import org.inftel.socialwind.shared.domain.SurferProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;
import org.inftel.socialwind.shared.services.SurferRequest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * Clase que contiene la estructura principal del panel que se muestra por pantalla. Se divide en 3
 * zonas: - La parte superior que contiene una cabecera con un menu desplegable. - La parte central
 * que contiene el panel donde se muestra toda la informacion - La parte inferior donde reside el
 * pie y la opcion para el cambio de idioma. <br>
 * 
 * @author aljiru
 * 
 */
public class PanelPrincipal extends Composite {

    /**
     * Variable que permite al compilador generar una definicion para el widget a partir de un
     * archivo xml.
     */
    private static PanelPrincipalUiBinder uiBinder = GWT.create(PanelPrincipalUiBinder.class);

    /** Interfaz necesaria para la creacion del widget */
    interface PanelPrincipalUiBinder extends UiBinder<Widget, PanelPrincipal> {
    }

    /** Instancia de la clase para obtener los objetos mas pesados */
    ClientFactory clientFactory = GWT.create(ClientFactory.class);

    private EventBus eventBus = clientFactory.getEventBus();
    private SocialwindRequestFactory swrf;

    @UiField
    Label locEn;

    @UiField
    Label locEs;

    @UiField
    Anchor name;
    
    @UiField
    Image avatar;

    /**
     * Metodo constructor. Se encarga de construir un widget a partir de la definicion del archivo
     * PanelPrincipal.ui.xml. Ademas configura el tamaño del Widget y le permite responder a cambios
     * del tamaño de la pantalla
     */
    public PanelPrincipal() {
        this.swrf = clientFactory.getSWRF();
        initWidget(uiBinder.createAndBindUi(this));
        setWidgetToMaxWidthAndHeight();
        Window.addResizeHandler(resizeHandler);
        bind();
    }

    private void bind() {
        locEn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                UrlBuilder newUrl = Window.Location.createUrlBuilder();
                newUrl.setParameter("locale", "en_US");
                Window.Location.assign(newUrl.buildString());
            }
        });

        locEs.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                UrlBuilder newUrl = Window.Location.createUrlBuilder();
                newUrl.setParameter("locale", "es");
                Window.Location.assign(newUrl.buildString());
            }
        });

        avatar.setSize("20px", "20px");
        eventBus.fireEvent(new AppBusyEvent());
        SurferRequest request = swrf.surferRequest();
        request.currentSurfer().fire(new Receiver<SurferProxy>() {
            @Override
            public void onSuccess(SurferProxy response) {
                clientFactory.setSurfer(response);
                avatar.setUrl("http://www.gravatar.com/avatar/" + response.getGravatarHash());
                name.setText(response.getDisplayName());
                eventBus.fireEvent(new AppFreeEvent());
            }

            @Override
            public void onFailure(ServerFailure error) {
                System.out.println(error.getMessage());
                eventBus.fireEvent(new AppFreeEvent());
            }
        });
    }

    /* ************* WIDGET CENTERING CODE *************** */
    /**
     * Metodo que permite al widget adecuarse a cambios en el tamaño de la pantalla
     */
    private ResizeHandler resizeHandler = new ResizeHandler() {
        public void onResize(ResizeEvent event) {
            setWidgetToMaxWidthAndHeight();
        }
    };

    @UiField
    PanelCentral pc;

    /**
     * Metodo que establece el ancho y alto del widget para que ocupe el 100% de la pantalla
     */
    private void setWidgetToMaxWidthAndHeight() {
        setWidth("100%");
        setHeight("100%");
    }

    public PanelCentral getPanelCentral() {
        return pc;
    }
}
