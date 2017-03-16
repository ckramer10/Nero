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

    public PurityReport() {

    }
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
}
