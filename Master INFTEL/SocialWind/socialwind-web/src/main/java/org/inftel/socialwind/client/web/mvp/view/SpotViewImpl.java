package org.inftel.socialwind.client.web.mvp.view;

import org.inftel.socialwind.client.web.Location;
import org.inftel.socialwind.client.web.Utilidades;
import org.inftel.socialwind.client.web.mvp.presenter.SpotPresenter;
import org.inftel.socialwind.shared.domain.SpotProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SpotViewImpl extends Composite implements SpotView {

    private static HotspotViewImplUiBinder uiBinder = GWT.create(HotspotViewImplUiBinder.class);

    interface HotspotViewImplUiBinder extends UiBinder<Widget, SpotViewImpl> {
    }

    SpotPresenter presenter;

    public SpotViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    Image avatar;

    @UiField
    Label name;

    @UiField
    Label surfers;

    @UiField
    Label loc;

    @UiField
    Label locLabel;

    @UiField
    Label dist;

    @UiField
    Label description;

    public void cargaInterfaz(SpotProxy data) {
        if (data.getImgUrl() != null) {
            avatar.setUrl(data.getImgUrl());
            avatar.setSize("90px", "90px");
        }

        name.setText(data.getName());
        surfers.setText(data.getSurferCount().toString());
        if (data.getLocation() != null) {
            Location l = new Location(data.getLocation().getLatitude(), data.getLocation()
                    .getLongitude());
            loc.setText(l.getLatitud() + " - " + l.getLongitud());
            dist.setText(Utilidades.calculaDistancia(l, Utilidades.DEFAULT_LOCATION) + "km");
        } else {
            locLabel.setText("");
        }

        description.setText(data.getDescription());
    }

    @Override
    public void setPresenter(SpotPresenter presenter) {
        this.presenter = presenter;
    }

}
