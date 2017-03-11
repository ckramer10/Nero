package com.sffilps.waterlocater.model;

import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by ckramer on 2/23/17.
 */

@IgnoreExtraProperties
public class WaterReport {

    public String dateTime;
    public String address;
    public double longitude;
    public double latitude;
    public String condition;
    public String type;
    public String submittedBy;


    /**
     * Default Constructor
     */
    public WaterReport() {

    }

    /**
     * Full Constructor
     * @param date
     * @param loc
     * @param cond
     * @param typ
     * @param subBy
     */
    public WaterReport(String date, String loc, String cond, String typ, String subBy, double lat, double lng) {
        this.dateTime = date;
        this.address = loc;
        this.condition = cond;
        this.type = typ;
        this.submittedBy = subBy;
        this.latitude = lat;
        this.longitude = lng;
    }

    /**
     *  Auto Generate Date Constructor
     * @param cond
     * @param typ
     * @param subBy
     * @param loc
     */
    public WaterReport(String cond, String typ, String subBy, String loc, double lat, double lng) {
        this.dateTime = DateFormat.getDateTimeInstance().format(new Date());
        this.address = loc;
        this.condition = cond;
        this.type = typ;
        this.submittedBy = subBy;
        this.longitude = lng;
        this.latitude = lat;

    }

    public String getAddress() {
        return address;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getTitle() {
        return address;
    }

    public String getSnippet() {
        return "Type: " + type + "    " + "Condition: " + condition;
    }

    @Override
    public String toString(){
        return "\nType: " + type + "    Condition: " + condition + "\n\nLocation: " + address
                + "\n\nSubmitted by: " + submittedBy + "\n\nTime: " + dateTime + "\n";
    }
}
