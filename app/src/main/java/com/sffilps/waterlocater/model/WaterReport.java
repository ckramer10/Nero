package com.sffilps.waterlocater.model;

import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;
import java.text.DateFormat;
import java.util.ArrayList;
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
    public ArrayList<PurityReport> purityList;
    public String key;


    /**
     * Default Constructor
     */
    public WaterReport() {
        this.purityList = new ArrayList<>();
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
        this.purityList = new ArrayList<>();
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
        this.purityList = new ArrayList<>();
        this.address = loc;
        this.condition = cond;
        this.type = typ;
        this.submittedBy = subBy;
        this.longitude = lng;
        this.latitude = lat;

    }

    /**
     * adds report to list
     * @param newReport new purity report
     */
    public void addPurityReport(PurityReport newReport) {
        if(newReport == null) {
            throw new IllegalArgumentException("Do not input a null purity report");
        } else if(purityList.contains(newReport)) {
            throw new IllegalArgumentException("That purity report already exists");
        } else {
            purityList.add(newReport);
        }
    }


    /**
     * gets list
     * @return purity list
     */
    public ArrayList<PurityReport> getPurityList() {
        return purityList;
    }

    /**
     * sets key
     * @param key string
     */
    public void setKey (String key) {
        this.key = key;
    }

    /**
     * gets key
     * @return current key
     */
    public String getKey () {
        return key;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return longitude
     */
    public double getLongitude(){
        return longitude;
    }

    /**
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return address
     */
    public String getTitle() {
        return address;
    }

    /**
     * @return returns type and condition in string
     */
    public String getSnippet() {
        return "Type: " + type + "    " + "Condition: " + condition;
    }

    @Override
    public String toString(){
        return "\nType: " + type + "    Condition: " + condition + "\n\nLocation: " + address
                + "\n\nSubmitted by: " + submittedBy + "\n\nTime: " + dateTime + "\n";
    }
}
