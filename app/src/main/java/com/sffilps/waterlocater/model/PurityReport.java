package com.sffilps.waterlocater.model;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by pauld on 3/15/2017.
 */

public class PurityReport {

    public String dateTime;
    public int purityReportNumber;
    public String name;
    public String address;
    public double longitude;
    public double latitude;
    public String condition;
    public String virusPPM;
    public String contaminantPPM;

    /**
     * default constructor
     */
    public PurityReport() {

    }

    /**
     * constructor to initalize all the data
     * @param purityReportNumber
     * @param name
     * @param address
     * @param longitude
     * @param latitude
     * @param condition
     * @param virusPPM
     * @param contaminantPPM
     */
    public PurityReport(int purityReportNumber, String name, String address, double longitude,
                            double latitude, String condition, String virusPPM, String contaminantPPM) {
        this.dateTime = DateFormat.getDateTimeInstance().format(new Date());
        this.purityReportNumber = purityReportNumber;
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.condition = condition;
        this.virusPPM = virusPPM;
        this.contaminantPPM = contaminantPPM;
    }

    /**
     * write the data to be put into the list
     * @return the correct string for list
     */
    public String toString() {
        return "\nReport Number: " + purityReportNumber + "    Condition: " + condition
                + "\n\nVirus PPM: " + virusPPM +  "   Contaminant PPM: " + contaminantPPM
                + "\n\nLocation: " + address
                + "\n\nSubmitted by: " + name + "\n\nTime: " + dateTime + "\n";
    }

    public String getSubmitDate() {
        return dateTime;
    }

}
