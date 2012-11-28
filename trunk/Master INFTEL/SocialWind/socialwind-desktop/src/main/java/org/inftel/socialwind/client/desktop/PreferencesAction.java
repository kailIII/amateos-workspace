package org.inftel.socialwind.client.desktop;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.inftel.socialwind.client.desktop.view.Messages;

@SuppressWarnings("serial")
public class PreferencesAction extends AbstractAction {

    Window preferencesWindow;

    public PreferencesAction(Window preferencesWindow) {
        putValue(NAME, Messages.getString("preferences"));
        putValue(SHORT_DESCRIPTION, Messages.getString("preferencesDescription"));
        this.preferencesWindow = preferencesWindow;
    }

    public void actionPerformed(ActionEvent arg0) {
        preferencesWindow.setVisible(true);
        preferencesWindow.toFront();
    }

}
