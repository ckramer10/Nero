package com.sffilps.waterlocater.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sffilps.waterlocater.R;
import com.sffilps.waterlocater.model.PurityReport;
import com.sffilps.waterlocater.model.WaterReport;

import java.util.ArrayList;
import java.util.List;

import static android.R.color.white;
import static com.github.mikephil.charting.animation.Easing.EasingOption.EaseInQuad;

/**
 * Created by ckramer on 4/2/17.
 */

public class HistoricalReportGraph extends AppCompatActivity {

    public static String xAxis;
    public static String yAxis;
    public static WaterReport report;
    private LineChart chart;
    private ArrayList<PurityReport> purityReports;
    private double janAVG = 0, febAVG = 0, marAVG = 0, aprAVG = 0, mayAVG = 0, junAVG = 0, julAVG = 0, augAVG = 0, sepAVG = 0, octAVG = 0, novAVG = 0, decAVG = 0;
    private double janCount= 0, febCount= 0, marCount = 0, aprCount = 0, mayCount = 0, junCount = 0, julCount = 0, augCount = 0, sepCount = 0, octCount = 0, novCount = 0, decCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_report_graph);

        chart = (LineChart) findViewById(R.id.chart);

        List<String> months = new ArrayList<>();
        months.add("February " + xAxis);
        months.add("March " + xAxis);
        months.add("April " + xAxis);
        months.add("May " + xAxis);
        months.add("June " + xAxis);
        months.add("July " + xAxis);
        months.add("August " + xAxis);
        months.add("September " + xAxis);
        months.add("October " + xAxis);
        months.add("November " + xAxis);
        months.add("December " + xAxis);

        List<Entry> entries = new ArrayList<Entry>();

        getDataFromReport();

        entries.add(new Entry(1,(float) janAVG));
        entries.add(new Entry(2,(float) febAVG));
        entries.add(new Entry(3,(float) marAVG));
        entries.add(new Entry(4,(float) aprAVG));
        entries.add(new Entry(5,(float) mayAVG));
        entries.add(new Entry(6,(float) junAVG));
        entries.add(new Entry(7,(float) julAVG));
        entries.add(new Entry(8,(float) augAVG));
        entries.add(new Entry(9,(float) sepAVG));
        entries.add(new Entry(10,(float) octAVG));
        entries.add(new Entry(11,(float) novAVG));
        entries.add(new Entry(12,(float) decAVG));

        LineDataSet dataSet = new LineDataSet(entries,"Report Type: " + yAxis);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setTouchEnabled(true);
        chart.invalidate();
        XAxis xAx = chart.getXAxis();
        xAx.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.animateX(2500, Easing.EasingOption.EaseInQuad);

    }

    /**
     * Get Data from Firebase
     */
    public void getDataFromReport() {
        purityReports = report.getPurityList();

        for (int i = 0; i < purityReports.size(); i++) {

            String submitDate = purityReports.get(i).dateTime;
            String[] explode = submitDate.split(" ");
            String month = explode[0];

            if (month.equals("Jan")) {
                janCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    janAVG = janAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    janAVG = janAVG + Double.parseDouble(contaminantPPMStr);
                }
            } else if (month.equals("Feb")) {
                febCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    febAVG = febAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    febAVG = febAVG + Double.parseDouble(contaminantPPMStr);
                }
            } else if (month.equals("Mar")) {
                marCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    marAVG = marAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    marAVG = marAVG + Double.parseDouble(contaminantPPMStr);
                }
            }  else if (month.equals("Apr")) {
                aprCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    aprAVG = aprAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    aprAVG = aprAVG + Double.parseDouble(contaminantPPMStr);
                }

            }   else if (month.equals("May")) {
                mayCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    mayAVG = mayAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    mayAVG = mayAVG + Double.parseDouble(contaminantPPMStr);
                }

            }  else if (month.equals("Jun")) {
                aprCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    junAVG = junAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    junAVG = junAVG + Double.parseDouble(contaminantPPMStr);
                }

            }  else if (month.equals("Jul")) {
                aprCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    julAVG = julAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    julAVG = julAVG + Double.parseDouble(contaminantPPMStr);
                }

            } else if (month.equals("Aug")) {
                augCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    augAVG = augAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    augAVG = augAVG + Double.parseDouble(contaminantPPMStr);
                }

            } else if (month.equals("Sep")) {
                sepCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    sepAVG = sepAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    sepAVG = sepAVG + Double.parseDouble(contaminantPPMStr);
                }

            } else if (month.equals("Oct")) {
                octCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    octAVG = octAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    octAVG = octAVG + Double.parseDouble(contaminantPPMStr);
                }

            } else if (month.equals("Nov")) {
                novCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    novAVG = novAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    novAVG = novAVG + Double.parseDouble(contaminantPPMStr);
                }

            } else if (month.equals("Dec")) {
                decCount++;
                if (yAxis.equals("Virus")) {
                    String virusStr = purityReports.get(i).virusPPM;
                    decAVG = decAVG + Double.parseDouble(virusStr);
                } else {
                    String contaminantPPMStr = purityReports.get(i).contaminantPPM;
                    decAVG = decAVG + Double.parseDouble(contaminantPPMStr);
                }

            }

        }

        if (janCount != 0) {
            janAVG = janAVG / janCount;
        }

        if (febCount != 0) {
            febAVG = febAVG / febCount;
        }

        if (marCount != 0) {
            marAVG = marAVG / marCount;
        }

        if (aprCount != 0) {
            aprAVG = aprAVG / aprCount;
        }

        if (mayCount != 0) {
            mayAVG = mayAVG / mayCount;
        }

        if (junCount != 0) {
            junAVG = junAVG / junCount;
        }

        if (julCount != 0) {
            julAVG = julAVG / julCount;
        }

        if (augCount != 0) {
            augAVG = augAVG / augCount;
        }

        if (sepCount != 0) {
            sepAVG = sepAVG / sepCount;
        }

        if (octCount != 0) {
            octAVG = octAVG / octCount;
        }

        if (novCount != 0) {
            novAVG = novAVG / novCount;
        }

        if (decCount != 0) {
            decAVG = decAVG / decCount;
        }
    }
}
