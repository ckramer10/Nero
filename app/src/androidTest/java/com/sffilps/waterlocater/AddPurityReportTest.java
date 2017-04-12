package com.sffilps.waterlocater;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.sffilps.waterlocater.controllers.SubmitReport;
import com.sffilps.waterlocater.model.PurityReport;
import com.sffilps.waterlocater.model.WaterReport;
import static org.hamcrest.CoreMatchers.is;


import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device. Runs junit tests on addPurity Report
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AddPurityReportTest {
    /**
     * makes sure the purity report adds tot he waterReport's purityList
     */
    @Test
    public void addPurityTest() {
        WaterReport waterReport = new WaterReport();
        PurityReport p = new PurityReport();
        ArrayList<PurityReport> purityList = new ArrayList<>();

        purityList.add(p);
        waterReport.addPurityReport(p);

        assertThat(purityList.equals(waterReport.getPurityList()), is(true));
        assertThat(waterReport.getPurityList().size(), is(1));
    }

    /**
     * makes sure the purity reports add to end of purity list and dont delete anything
     */
    @Test
    public void addPurityTestToListWithExistingReports() {
        WaterReport waterReport = new WaterReport();
        PurityReport p = new PurityReport();
        ArrayList<PurityReport> purityList = new ArrayList<>();

        purityList.add(p);
        waterReport.addPurityReport(p);
        PurityReport p2 = new PurityReport();
        PurityReport p3 = new PurityReport();

        purityList.add(p2);
        waterReport.addPurityReport(p2);

        purityList.add(p3);
        waterReport.addPurityReport(p3);
        assertThat(purityList.equals(waterReport.getPurityList()), is(true));
        assertThat(waterReport.getPurityList().size(), is(3));
    }

    /**
     * tests to make sure the purity report is not null
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullArguement() {
        PurityReport p = null;
        WaterReport w = new WaterReport();
        w.addPurityReport(p);
    }

    /**
     * tests to make sure the purity report is not already in the water report list
     */
    @Test(expected=IllegalArgumentException.class)
    public void testDuplicateArguement() {
        PurityReport p = new PurityReport();
        WaterReport w = new WaterReport();
        w.addPurityReport(p);
        w.addPurityReport(p);
        String s = "1";
        SubmitReport.setCount(s);
        System.out.print(s);
    }
}
