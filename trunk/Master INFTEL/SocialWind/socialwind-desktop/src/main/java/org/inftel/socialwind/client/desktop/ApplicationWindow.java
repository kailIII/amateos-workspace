package org.inftel.socialwind.client.desktop;

import javax.swing.Action;
import javax.swing.UIManager;

import org.inftel.socialwind.client.desktop.model.SurferPreferences;
import org.inftel.socialwind.client.desktop.requestfactory.SocialWindServer;
import org.inftel.socialwind.client.desktop.view.SocialWindTrayIcon;
import org.inftel.socialwind.client.desktop.view.SurferPreferencesWindow;

public class ApplicationWindow {

    public static void main(String[] args) throws Exception {
        configureSystemLookAndFeelStyle();

        // Instacia las preferencias y el controlador del server
        final SurferPreferences preferences = new SurferPreferences();
        final SocialWindServer server = new SocialWindServer(preferences);
        final SocialWindTrayIcon trayIcon = new SocialWindTrayIcon();
        final SurferPreferencesWindow preferencesWindow = new SurferPreferencesWindow(preferences);
        final EvenewsPuller puller = new EvenewsPuller();

        // Create Actions
        final Action exit = new ExitAction();
        final Action connect = new ConnectAction(server, preferences);
        final Action show = new PreferencesAction(preferencesWindow);

        // Add Actions
        trayIcon.setTrayIconActionListener(show);
        trayIcon.setPreferencesActionListener(show);
        trayIcon.setExitActionListener(exit);
        
        preferencesWindow.getBtnConnect().setAction(connect);
        preferencesWindow.getBtnExit().setAction(exit);
        
        // El puller se activa cuando cambia estado a conectado
        preferences.addPropertyChangeListener(puller);
        
        // Intentar conectar el servidor
        preferencesWindow.getBtnConnect().doClick();

        // ImageIcon test = new ImageIcon(new
        // URL("http://docs.oracle.com/javase/tutorial/images/jws-launch-button.png"));
        // preferencesWindow.getLblNewLabel().setIcon(test);
    }

    static void configureSystemLookAndFeelStyle() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
    }

}
