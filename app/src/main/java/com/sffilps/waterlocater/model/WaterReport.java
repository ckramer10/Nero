package com.sffilps.waterlocater.model;

import com.google.firebase.database.IgnoreExtraProperties;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by ckramer on 2/23/17.
 */

@IgnoreExtraProperties
public class WaterReport {

    public String dateTime;
    public String location;
    public String condition;
    public String type;
    public String submittedBy;

    public WaterReport() {

    }

    public WaterReport(String date, String loc, String cond, String typ, String subBy) {
        this.dateTime = date;
        this.location = loc;
        this.condition = cond;
        this.type = typ;
        this.submittedBy = subBy;
    }

    public WaterReport(String cond, String typ, String subBy, String loc) {
        this.dateTime = DateFormat.getDateTimeInstance().format(new Date());
        this.location = loc;
        this.condition = cond;
        this.type = typ;
        this.submittedBy = subBy;

    }
}
