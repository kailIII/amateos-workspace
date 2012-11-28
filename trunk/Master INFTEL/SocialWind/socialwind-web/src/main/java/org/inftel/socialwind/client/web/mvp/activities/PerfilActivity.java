package org.inftel.socialwind.client.web.mvp.activities;

import org.inftel.socialwind.client.web.mvp.ClientFactory;
import org.inftel.socialwind.client.web.mvp.event.AppBusyEvent;
import org.inftel.socialwind.client.web.mvp.event.AppFreeEvent;
import org.inftel.socialwind.client.web.mvp.places.PerfilPlace;
import org.inftel.socialwind.client.web.mvp.presenter.PerfilPresenter;
import org.inftel.socialwind.client.web.mvp.view.PerfilView;
import org.inftel.socialwind.shared.domain.SurferProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;
import org.inftel.socialwind.shared.services.SurferRequest;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class PerfilActivity extends AbstractActivity implements PerfilPresenter {

    private ClientFactory clientFactory = GWT.create(ClientFactory.class);
    private PerfilView perfilView;
    private SocialwindRequestFactory swrf;
    private EventBus eventBus;

    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.perfilView = clientFactory.getPerfilView();
        this.swrf = clientFactory.getSWRF();
        this.eventBus = clientFactory.getEventBus();
        this.onCargarPerfil();
        bind();
        panel.setWidget(perfilView.asWidget());
    }

    public PerfilActivity(PerfilPlace place, ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    /**
     * Le pasa a la vista un objeto Presenter
     */
    public void bind() {
        perfilView.setPresenter(this);
    }

    @Override
    public void onCargarPerfil() {
        eventBus.fireEvent(new AppBusyEvent());
        perfilView.setPerfil(clientFactory.getSurfer());
        eventBus.fireEvent(new AppFreeEvent());
    }

    @Override
    public void saveSurfer(String displayName, String fullName) {
        eventBus.fireEvent(new AppBusyEvent());
        SurferRequest request = swrf.surferRequest();        
        SurferProxy surfer = clientFactory.getSurfer();
        surfer = request.edit(surfer);
        surfer.setDisplayName(displayName);
        surfer.setFullName(fullName);
        request.fire(new Receiver<Void>() {
            @Override
            public void onSuccess(Void response) {
                System.out.println("Success");
            }

            @Override
            public void onFailure(ServerFailure error) {
                System.out.println(error.getMessage());
                eventBus.fireEvent(new AppFreeEvent());
            }
        });
        clientFactory.setSurfer(surfer);
        eventBus.fireEvent(new AppFreeEvent());
    }

}
