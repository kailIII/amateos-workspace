package org.inftel.socialwind.client.web.mvp.ui;

import org.inftel.socialwind.client.web.Location;
import org.inftel.socialwind.client.web.Utilidades;
import org.inftel.socialwind.shared.domain.SpotProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SpotUI extends Composite {

    private static SpotUIUiBinder uiBinder = GWT.create(SpotUIUiBinder.class);

    interface SpotUIUiBinder extends UiBinder<Widget, SpotUI> {
    }

    @UiField
    Image imagen;

    @UiField
    Label name;

    @UiField
    Label surfercount;

    @UiField
    Label loc;

    @UiField
    Label locLabel;

    @UiField
    Label dist;

    @UiField
    FocusPanel clickable;

    private SpotProxy spot;

    public @UiConstructor
    SpotUI(SpotProxy spot) {
        this.spot = spot;
        initWidget(uiBinder.createAndBindUi(this));
        initializeUI();
    }

    private void initializeUI() {
        if (spot.getImgUrl() != null) {
            imagen.setUrl(spot.getImgUrl());
            imagen.setSize("40px", "40px");
        }

        name.setText(spot.getName());
        surfercount.setText(spot.getSurferCount().toString());
        if (spot.getLocation() != null) {
            Location l = new Location(spot.getLocation().getLatitude(), spot.getLocation()
                    .getLongitude());
            loc.setText(l.getLatitud() + " - " + l.getLongitud());
            dist.setText(Utilidades.calculaDistancia(l, Utilidades.DEFAULT_LOCATION) + "km");
        } else {
            locLabel.setText("");
        }
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return clickable.addClickHandler(handler);
    }

}
