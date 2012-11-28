package org.inftel.socialwind.client.desktop;

import org.inftel.socialwind.client.desktop.model.SurferPreferences;
import org.inftel.socialwind.client.desktop.requestfactory.SocialWindServer;
import org.inftel.socialwind.client.desktop.view.Messages;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public class ConnectAction extends AbstractAction {

    SocialWindServer server;
    SurferPreferences preferences;

    public ConnectAction(SocialWindServer server, SurferPreferences preferences) {
        putValue(NAME, Messages.getString("connect"));
        putValue(SHORT_DESCRIPTION, Messages.getString("connectDescription"));
        this.server = server;
        this.preferences = preferences;
        preferences.setStatus(Messages.getString("disconnected"));
        preferences.setConnected(false);
    }

    public void actionPerformed(ActionEvent event) {
        final Object button = event.getSource();

        disable(button);
        new SwingWorker<SocialwindRequestFactory, Void>() {
            @Override
            protected SocialwindRequestFactory doInBackground() throws Exception {
                return server.connect();
            }

            @Override
            protected void done() {
                try {
                    preferences.setRequestFactory(get());
                    preferences.setStatus(Messages.getString("connected"));
                    preferences.setConnected(true);
                    PopupHelper.getPopupHelper().queuePopup("Conectado al servidor SocialWind",
                            "Pronto empezará a recibir notifiaciones de los diferentes enventos.");
                } catch (Exception e) {
                    e.printStackTrace();
                    preferences.setStatus("desconectado (" + safeCause(e) + ")");
                    preferences.setConnected(false);
                    enable(button);
                }
            }
        }.execute();

    }

    private String safeCause(Exception e) {
        try {
            return e.getCause().getMessage();
        } catch (Exception ignored) {
            return Messages.getString("fail");
        }
    }

    public void enable(Object object) {
        if (object instanceof Component) {
            Component component = (Component) object;
            component.setEnabled(true);
        }
    }

    public void disable(Object object) {
        if (object instanceof Component) {
            Component component = (Component) object;
            component.setEnabled(false);
        }
    }

}
