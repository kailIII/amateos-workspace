package org.inftel.socialwind.client.web.mvp.view;


import java.util.List;

import org.inftel.socialwind.client.web.mvp.presenter.PlayasListPresenter;
import org.inftel.socialwind.shared.domain.SpotProxy;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Clase interfaz que debe implementar la Vista para controlar el listado de playas
 * 
 * @author aljiru
 * 
 */
public interface PlayasListView extends IsWidget {
    void setPresenter(PlayasListPresenter presenter);
    void addPlayas(List<SpotProxy> playa);
}
