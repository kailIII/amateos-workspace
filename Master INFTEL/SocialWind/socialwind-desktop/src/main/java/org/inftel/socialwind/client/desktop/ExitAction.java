package org.inftel.socialwind.client.desktop;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.inftel.socialwind.client.desktop.view.Messages;

@SuppressWarnings("serial")
public class ExitAction extends AbstractAction {

    public ExitAction() {
        putValue(NAME, Messages.getString("exit"));
        putValue(SHORT_DESCRIPTION, Messages.getString("exitDescription"));
    }

    public void actionPerformed(ActionEvent e) {
        for (Frame frame : Frame.getFrames()) {
            frame.dispose();
        }
        System.exit(0);
    }

}
