package org.inftel.socialwind.client.web.mvp.view;

import org.inftel.socialwind.client.web.mvp.presenter.PerfilPresenter;
import org.inftel.socialwind.shared.domain.SurferProxy;

import com.google.gwt.user.client.ui.IsWidget;

public interface PerfilView extends IsWidget {
    void setPresenter(PerfilPresenter presenter);
    void setPerfil(SurferProxy surfero);
}
