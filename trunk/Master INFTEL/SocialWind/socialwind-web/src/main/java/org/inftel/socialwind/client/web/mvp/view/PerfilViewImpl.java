package org.inftel.socialwind.client.web.mvp.view;

import org.inftel.socialwind.client.web.mvp.presenter.PerfilPresenter;
import org.inftel.socialwind.shared.domain.SurferProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PerfilViewImpl extends Composite implements PerfilView {

    private static PerfilViewImplUiBinder uiBinder = GWT.create(PerfilViewImplUiBinder.class);

    interface PerfilViewImplUiBinder extends UiBinder<Widget, PerfilViewImpl> {
    }

    /** Presenter asociado a esta vista */
    PerfilPresenter presenter;

    public PerfilViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    private SurferProxy surfero;

    @UiField
    TextBox cuenta;

    @UiField
    TextBox mail;

    @UiField
    Image avatar;

    @UiField
    TextBox fullname;

    public PerfilViewImpl(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("save")
    void guardarCambios(ClickEvent event) {
        presenter.saveSurfer(cuenta.getText(), fullname.getText());
    }

    @Override
    public void setPresenter(PerfilPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setPerfil(SurferProxy surfero) {
        this.surfero = surfero;
        actualizaUI();
    }

    private void actualizaUI() {
        fullname.setText(surfero.getFullName());
        cuenta.setText(surfero.getDisplayName());
        mail.setText(surfero.getEmail());
        mail.setEnabled(false);
        avatar.setUrl("http://www.gravatar.com/avatar/" + surfero.getGravatarHash());
    }

}
