package org.inftel.socialwind.client.web.mvp.presenter;

import org.inftel.socialwind.shared.domain.SpotProxy;

/**
 * Clase interfaz que debe implementar el Presenter para controlar el listado de playas
 * 
 * @author aljiru
 * 
 */
public interface PlayasListPresenter extends Presenter {
    public void onCargarListadoPlayas();
    public void onCargarListadoHotspots();
    public void onPlayaSeleccionada(SpotProxy id);
}
