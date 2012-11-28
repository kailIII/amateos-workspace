package org.inftel.socialwind.client.web.mvp.view;

import java.util.List;

import org.inftel.socialwind.client.web.mvp.presenter.SesionListPresenter;
import org.inftel.socialwind.client.web.mvp.ui.SesionUI;
import org.inftel.socialwind.shared.domain.SessionProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class SesionListViewImpl extends Composite implements SesionListView {

    private static SesionListImplUiBinder uiBinder = GWT.create(SesionListImplUiBinder.class);

    interface SesionListImplUiBinder extends UiBinder<Widget, SesionListViewImpl> {
    }
    
    SesionListPresenter presenter;
    
    @UiField FlowPanel fpsesiones;

    public SesionListViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(SesionListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addSesiones(List<SessionProxy> sesiones) {
        fpsesiones.clear();
        for (final SessionProxy sesion : sesiones) {
            SesionUI sesionUI = new SesionUI(sesion);
            fpsesiones.add(sesionUI);
        }
    }

}
