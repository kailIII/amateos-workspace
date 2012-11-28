package org.inftel.socialwind.client.desktop.model;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

import org.inftel.socialwind.client.desktop.ApplicationWindow;
import org.inftel.socialwind.shared.domain.SurferProxy;
import org.inftel.socialwind.shared.services.SocialwindRequestFactory;
import org.inftel.socialwind.shared.services.SurferRequest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import javax.swing.SwingWorker;

public class SurferPreferences extends AbstractModelObject {
    private String userName;
    private String password;
    private boolean savePassword;
    private String displayName;
    private String fullName;
    private boolean connected;
    private String status;
    private SocialwindRequestFactory requestFactory;

    Preferences preferences = Preferences.userNodeForPackage(ApplicationWindow.class);

    public SurferPreferences() {
        // cargar preferencias locales
        userName = preferences.get("user_name", "");
        password = preferences.get("password", "");
        savePassword = preferences.getBoolean("save_password", false);

        // Cuando se pase a conectado se salva
        this.addPropertyChangeListener("connected", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent arg0) {
                if (arg0.getNewValue().equals(Boolean.TRUE)) {
                    preferences.put("user_name", getUserName());
                    preferences.putBoolean("save_password", isSavePassword());
                    if (isSavePassword()) {
                        preferences.put("password", getPassword());
                    }
                }

            }
        });
    }

    public String getAppUrl() {
        return preferences.get("app_url", "https://socialwindgwt.appspot.com");
    }

    public String getRequestFactoryMethod() {
        return preferences.get("rf_method", "/gwtRequest");
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        boolean oldValue = this.connected;
        this.connected = connected;
        firePropertyChange("connected", oldValue, connected);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        String old = this.userName;
        this.userName = userName;
        firePropertyChange("userName", old, userName);
        if (isConnected()) {
            preferences.put("user_name", userName);
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String oldValue = this.password;
        this.password = password;
        firePropertyChange("password", oldValue, password);
        if (isConnected() && isSavePassword()) {
            preferences.put("password", password);
        }
    }

    public boolean isSavePassword() {
        return savePassword;
    }

    public void setSavePassword(boolean savePassword) {
        boolean oldValue = this.savePassword;
        this.savePassword = savePassword;
        firePropertyChange("savePassword", oldValue, savePassword);
        if (isConnected()) {
            if (savePassword) {
                preferences.putBoolean("save_password", savePassword);
            } else {
                preferences.put("password", "");
            }
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        String oldValue = this.displayName;
        this.displayName = displayName;
        firePropertyChange("displayName", oldValue, displayName);
        queueSaveSurfer();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        String oldValue = this.fullName;
        this.fullName = fullName;
        firePropertyChange("fullName", oldValue, fullName);
        queueSaveSurfer();
    }

    public String getAppId() {
        return preferences.get("app_id", "socialwind-v1.0");
    }

    public void setStatus(String status) {
        String oldValue = this.status;
        this.status = status;
        firePropertyChange("status", oldValue, status);
    }

    public String getStatus() {
        return status;
    }

    public SocialwindRequestFactory getRequestFactory() {
        return requestFactory;
    }

    public void setRequestFactory(SocialwindRequestFactory requestFactory) {
        SocialwindRequestFactory oldValue = this.requestFactory;
        this.requestFactory = requestFactory;
        requestSurfer();
        firePropertyChange("requestFactory", oldValue, requestFactory);
    }

    // Se guarda la referencia del surfer
    private SurferProxy surfer;

    private void requestSurfer() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                SurferRequest request = requestFactory.surferRequest();
                request.currentSurfer().fire(new Receiver<SurferProxy>() {
                    @Override
                    public void onSuccess(SurferProxy surfer) {
                        SurferPreferences.this.surfer = surfer;
                        setDisplayName(surfer.getDisplayName());
                        setFullName(surfer.getFullName());
                    }
                });
                return null;
            }
        }.execute();
    }

    private void saveSurfer() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    System.out.println("enviando peticion guardar cambios del surfer");
                    SurferRequest request = requestFactory.surferRequest();
                    surfer = request.edit(surfer);
                    surfer.setDisplayName(getDisplayName());
                    surfer.setFullName(getFullName());
                    request.fire(new Receiver<Void>() {
                        @Override
                        public void onSuccess(Void arg0) {
                            System.out.println("cambios guardados con exito");
                        }

                        @Override
                        public void onFailure(ServerFailure error) {
                            System.out.println("fallo al guardar cambios: " + error.getMessage());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                setStatus("cambios guardados");
            };
        }.execute();
    }

    private Timer saveTimer = new Timer();
    private TimerTask saveTask;

    /**
     * Encola las peticiones de guardar cambios en servidor. Si pasan 5 segundos se realiza la
     * peticion al servidor, pero si se encola una nueva petición, se descartan las anteriores.
     */
    private synchronized void queueSaveSurfer() {
        if (saveTask != null) {
            saveTask.cancel();
            saveTimer.purge();
        }
        saveTask = new TimerTask() {
            @Override
            public void run() {
                saveTask.cancel();
                saveSurfer();
            }
        };
        saveTimer.schedule(saveTask, 5 * 1000);
    }

}
