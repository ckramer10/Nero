package com.sffilps.waterlocater.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sffilps.waterlocater.R;

public class SubmitQualityReportOptions extends AppCompatActivity {

    public Button viewList;
    public Button viewMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_quality_report_options);

        viewList = (Button) findViewById(R.id.list_option);
        viewMap = (Button) findViewById(R.id.map_option);

        viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, SubmitQualityList.class);
                context.startActivity(intent);
            }
        });

        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, SubmitQualityMap.class);
                context.startActivity(intent);
            }
        });
    }

    /**
     * changes back button so it goes to quality report home
     */
    public void onBackPressed() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, QualityReportHome.class);
        context.startActivity(intent);
    }
}
