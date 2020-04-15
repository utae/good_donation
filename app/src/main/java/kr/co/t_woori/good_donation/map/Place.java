package kr.co.t_woori.good_donation.map;

import java.io.Serializable;

/**
 * Created by rladn on 2017-08-18.
 */

public class Place implements Serializable {

    private String idNum;
    private String name;
    private double longitude;
    private double latitude;
    private String imgAmount;

    public Place(String idNum, String name, String longitude, String latitude, String imgAmount) {
        this.idNum = idNum;
        this.name = name;
        this.longitude = Double.parseDouble(longitude);
        this.latitude = Double.parseDouble(latitude);
        this.imgAmount = imgAmount;
    }

    public String getIdNum() {
        return idNum;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getImgAmount() {
        return imgAmount;
    }
}
