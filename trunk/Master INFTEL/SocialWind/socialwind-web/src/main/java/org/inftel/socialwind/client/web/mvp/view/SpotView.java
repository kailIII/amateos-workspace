package org.inftel.socialwind.client.web.mvp.view;

import org.inftel.socialwind.client.web.mvp.presenter.SpotPresenter;
import org.inftel.socialwind.shared.domain.SpotProxy;

import com.google.gwt.user.client.ui.IsWidget;

public interface SpotView extends IsWidget {
    void setPresenter(SpotPresenter presenter);
    void cargaInterfaz(SpotProxy spot);
}
