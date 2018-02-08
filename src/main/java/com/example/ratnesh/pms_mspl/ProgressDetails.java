package com.example.ratnesh.pms_mspl;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.ratnesh.pms_mspl.Models.ProjectDetailModel;
import com.example.ratnesh.pms_mspl.Models.ReportDetailModel;
import com.example.ratnesh.pms_mspl.Models.TempModel;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgressDetails extends AppCompatActivity {
    private Toolbar toolbar;
    private List<String> listDataHeader;
    private HashMap<String, List<ProjectDetailModel>> listDataChild;
    private ExpandableListView reportDetailsDataListView;
    private ExpandableListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_details);

        DeclareVariables();

        Gson gson = new Gson();
        String jsonResponse = getIntent().getStringExtra("details");
        ProjectDetailModel[] reports = gson.fromJson(jsonResponse, ProjectDetailModel[].class);

        ArrayList<ProjectDetailModel> reports1 = new ArrayList<>();
        for (int i=0; i<reports.length; i++) {
            reports1.add(reports[i]);
        }
        prepareListData(reports1);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        reportDetailsDataListView.setAdapter(listAdapter);
//        for (int i = 0; i < listAdapter.getGroupCount(); i++)
//            reportDetailsDataListView.expandGroup(i);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void DeclareVariables() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reportDetailsDataListView = (ExpandableListView) findViewById(R.id.lvExp);
    }

    private void prepareListData(List<ProjectDetailModel> reports) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ProjectDetailModel>>();

        // Adding parent data
        for (int i = 0; i < reports.size(); i++) {
            listDataHeader.add(reports.get(i).getLocationName());
            List<ProjectDetailModel> item = new ArrayList<ProjectDetailModel>();
            item.add(reports.get(i));
//            for (int j = 0; j < reports.size(); j++) {
//                item.add(reports.get(i).getProjectDetail().get(j));
//            }
            listDataChild.put(listDataHeader.get(i), item); // Header, Child data
        }
    }

}
