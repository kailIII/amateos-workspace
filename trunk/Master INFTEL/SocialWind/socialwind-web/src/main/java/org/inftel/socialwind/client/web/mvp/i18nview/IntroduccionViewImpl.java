package org.inftel.socialwind.client.web.mvp.i18nview;

import org.inftel.socialwind.client.web.mvp.presenter.IntroduccionPresenter;
import org.inftel.socialwind.client.web.mvp.view.IntroduccionView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Clase que contiene el panel de introduccion de la aplicacion
 * 
 * @author aljiru
 * 
 */
public class IntroduccionViewImpl extends Composite implements IntroduccionView {
    private static IntroPanelUiBinder uiBinder = GWT.create(IntroPanelUiBinder.class);

    interface IntroPanelUiBinder extends UiBinder<Widget, IntroduccionViewImpl> {
    }

    interface Resources extends ClientBundle {
        @Source("socialwind.png")
        public ImageResource logo();
    }

//    @UiField
//    ScrollPanel scrollPanel;

    /** Presenter asociado a esta vista */
    IntroduccionPresenter presenter;

    public IntroduccionViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        Window.addResizeHandler(resizeHandler);
    }

    private ResizeHandler resizeHandler = new ResizeHandler() {

        public void onResize(ResizeEvent event) {
            //scrollPanel.setHeight((event.getHeight() - 20) + "px");
        }
    };

    public void setPresenter(IntroduccionPresenter presenter) {
        this.presenter = presenter;
    }

}
