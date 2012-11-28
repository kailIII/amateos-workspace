package org.inftel.socialwind.client.web.mvp.presenter;

/**
 * @author aljiru
 *
 */
public interface PerfilPresenter extends Presenter {
    public void onCargarPerfil();
    public void saveSurfer(String displayName, String fullName);
}
