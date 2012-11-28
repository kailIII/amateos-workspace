package org.inftel.socialwind.client.web.mvp.view;


import org.inftel.socialwind.client.web.mvp.presenter.IntroduccionPresenter;

import com.google.gwt.user.client.ui.IsWidget;

public interface IntroduccionView extends IsWidget {
    void setPresenter(IntroduccionPresenter presenter);
}
