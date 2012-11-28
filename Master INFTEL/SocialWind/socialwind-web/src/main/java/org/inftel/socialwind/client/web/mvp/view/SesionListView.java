package org.inftel.socialwind.client.web.mvp.view;

import java.util.List;

import org.inftel.socialwind.client.web.mvp.presenter.SesionListPresenter;
import org.inftel.socialwind.shared.domain.SessionProxy;

import com.google.gwt.user.client.ui.IsWidget;

public interface SesionListView extends IsWidget {
    void setPresenter(SesionListPresenter presenter);
    void addSesiones(List<SessionProxy> sesiones);
}
