package org.inftel.socialwind.client.desktop.view;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.net.URL;

public class SocialWindTrayIcon {

    TrayIcon trayIcon;

    URL sucessImageUri = SocialWindTrayIcon.class.getResource("socialwind-success.png");

    URL newsImageUri = SocialWindTrayIcon.class.getResource("socialwind-news.png");

    URL problemImageUri = SocialWindTrayIcon.class.getResource("socialwind-problem.png");

    MenuItem preferencesItem;
    MenuItem exitItem;
    MenuItem aboutItem;

    public SocialWindTrayIcon() {
        if (SystemTray.isSupported()) {

            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(sucessImageUri);
            PopupMenu popup = new PopupMenu();

            preferencesItem = new MenuItem(Messages.getString("preferences"));
            exitItem = new MenuItem(Messages.getString("exit"));
            aboutItem = new MenuItem(Messages.getString("about"));

            popup.add(preferencesItem);
            popup.addSeparator();
            popup.add(aboutItem);
            popup.add(exitItem);

            trayIcon = new TrayIcon(image, Messages.getString("appTitle"), popup);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        } else {
            // TODO deberia mostrarse la ventana de preferencias o algo así
        }
    }

    public void setPreferencesActionListener(ActionListener action) {
        preferencesItem.addActionListener(action);
    }

    public void setTrayIconActionListener(ActionListener action) {
        trayIcon.addActionListener(action);
    }

    public void setAboutActionListener(ActionListener action) {
        aboutItem.addActionListener(action);
    }

    public void setExitActionListener(ActionListener action) {
        exitItem.addActionListener(action);
    }

}
