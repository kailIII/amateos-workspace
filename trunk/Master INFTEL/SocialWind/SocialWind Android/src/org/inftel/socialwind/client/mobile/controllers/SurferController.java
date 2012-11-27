package org.inftel.socialwind.client.mobile.controllers;

import org.inftel.socialwind.client.mobile.models.SurferModel;

public class SurferController {

    private SurferModel model;

    /**
     * Constructor
     * 
     * @param model
     */
    public SurferController(SurferModel model) {
        this.model = model;
    }

    public String getDisplayName() {
        return model.getDisplayName();
    }

    public String getFullName() {
        return model.getFullName();
    }

    public String getGravatarHash() {
        return model.getGravatarHash();
    }

    public double getLatitude() {
        return model.getLatitude();
    }

    public double getLongitude() {
        return model.getLongitude();
    }

    public void setDisplayName(String displayName) {
        model.setDisplayName(displayName);
    }

    public void setFullName(String fullName) {
        model.setFullName(fullName);
    }

    public void setGravatarHash(String gravatarHash) {
        model.setGravatarHash(gravatarHash);
    }

    public void setLatitude(double latitude) {
        model.setLatitude(latitude);
    }

    public void setLongitude(double longitude) {
        model.setLongitude(longitude);
    }
}
