package org.inftel.socialwind.client.web.mvp.view;


import java.util.List;

import org.inftel.socialwind.client.web.mvp.presenter.PlayasListPresenter;
import org.inftel.socialwind.client.web.mvp.ui.SpotUI;
import org.inftel.socialwind.shared.domain.SpotProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Clase encargado de controlar los eventos relacionados con la parte visual del listado de playas
 * @author aljiru
 *
 */
public class PlayasListViewImpl extends Composite implements PlayasListView {

    private static PlayasListViewImplUiBinder uiBinder = GWT
            .create(PlayasListViewImplUiBinder.class);

    interface PlayasListViewImplUiBinder extends UiBinder<Widget, PlayasListViewImpl> {
    }
    
    /** Presenter asociado a esta vista */
    PlayasListPresenter presenter;

    /**
     * Metodo constructor para crear la vista
     */
    public PlayasListViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    FlowPanel fpPlayas;

    public PlayasListViewImpl(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(PlayasListPresenter presenter) {
        this.presenter = presenter;        
    }

    @Override
    public void addPlayas(List<SpotProxy> lplayas) {
        fpPlayas.clear();
        for (final SpotProxy playa : lplayas) {
            SpotUI spot = new SpotUI(playa);
            spot.addClickHandler(new ClickHandler() {
                
                @Override
                public void onClick(ClickEvent arg0) {
                    // TODO Auto-generated method stub
                    presenter.onPlayaSeleccionada(playa);
                }
            });
            fpPlayas.add(spot);
        }     
    }

}
