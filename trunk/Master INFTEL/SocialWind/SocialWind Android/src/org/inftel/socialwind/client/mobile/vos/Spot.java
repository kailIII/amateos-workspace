package org.inftel.socialwind.client.mobile.vos;

import java.io.Serializable;

public class Spot implements Serializable {

    private static final long serialVersionUID = 5764734520583293311L;

    private String description;

    /** Si es true indica que la playa es hotspot */
    private Boolean hot;

    private String imgUrl;

    private double latitude;

    private double longitude;

    private String name;

    /** Contador de todos los surfer que han pasado por la playa */
    private Integer surferCount;

    /** Contador de todos los surfer que estan en la playa */
    private Integer surferCurrentCount;

    /**
     * Constructor
     */
    public Spot() {

    }

    /**
     * Constructor
     * 
     * @param name
     * @param description
     * @param latitude
     * @param longitude
     */
    public Spot(String name, String description, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getHot() {
        return (hot == null) ? false : hot;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return (name == null) ? "" : name;
    }

    public Integer getSurferCount() {
        return (surferCount == null) ? 0 : surferCount;
    }

    public Integer getSurferCurrentCount() {
        return (surferCurrentCount == null) ? 0 : surferCurrentCount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * Modifica la latitude
     * 
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Modifica la latitude
     * 
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurferCount(Integer surferCount) {
        this.surferCount = surferCount;
    }

    public void setSurferCurrentCount(Integer surferCurrentCount) {
        this.surferCurrentCount = surferCurrentCount;
    }

}
