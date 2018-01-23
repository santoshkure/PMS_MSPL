package com.example.ratnesh.pms_mspl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ratnesh.pms_mspl.Models.ProjectDetailModel;
import com.example.ratnesh.pms_mspl.Models.ReportDetailModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportDetails extends AppCompatActivity {

    private String projectId, categoryId, status;
    ExpandableListView reportDetailsDataListView;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<ProjectDetailModel>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        projectId = intent.getStringExtra("project_id");
        categoryId = intent.getStringExtra("category_id");
        status = intent.getStringExtra("status");

        reportDetailsDataListView = (ExpandableListView) findViewById(R.id.lvExp);
        viewReport();
    }

    ArrayList<ReportDetailsModel> reports = new ArrayList<>();
    JSONArray reoprtDetailsJSONArray;

    private void viewReport() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REPORTDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("sdfasd", response);
                            final JSONObject obj = new JSONObject(response);
                            reoprtDetailsJSONArray = obj.getJSONArray("report_details");
                            createRowData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError) {
                            Toast.makeText(getApplicationContext(), "Session Time out, Please Login Again...", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Server Authorization Failed", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), "Server Error, please try after some time later", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Network not Available", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "JSONArray Problem", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("project_id", projectId);
                params.put("category_id", categoryId);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void createRowData() throws JSONException {
        ArrayList<ReportDetailModel> reports = new ArrayList<ReportDetailModel>();
        ArrayList<ProjectDetailModel> project = new ArrayList<ProjectDetailModel>();
        ReportDetailModel report = new ReportDetailModel();

        String LocName = "";
        for (int i = 0; i < reoprtDetailsJSONArray.length(); i++) {
            JSONObject actor = reoprtDetailsJSONArray.getJSONObject(i);

            if (!LocName.equals(actor.getString("location_name"))) {
                LocName = actor.getString("location_name");
                report.setProjectDetail(project);
                if (i != 0) {
                    reports.add(report);
                }

                report = new ReportDetailModel();
                project = new ArrayList<ProjectDetailModel>();
                report.setLocationName(LocName);

                ProjectDetailModel projectDetail = new ProjectDetailModel();
                projectDetail.setProjectCategory(actor.getString("progress_category"));
                projectDetail.setProgressDate(actor.getString("progress_date"));
                projectDetail.setProgressRemark(actor.getString("progress_remark"));
                projectDetail.setProgressStatus(actor.getString("progress_status"));
                project.add(projectDetail);
            } else {
                ProjectDetailModel projectDetail = new ProjectDetailModel();
                projectDetail.setProjectCategory(actor.getString("progress_category"));
                projectDetail.setProgressDate(actor.getString("progress_date"));
                projectDetail.setProgressRemark(actor.getString("progress_remark"));
                projectDetail.setProgressStatus(actor.getString("progress_status"));
                project.add(projectDetail);
            }

            if (i == reoprtDetailsJSONArray.length() - 1) {
                report.setProjectDetail(project);
                reports.add(report);
            }
        }

        ArrayList<ReportDetailModel> reportsComplected = new ArrayList<>();
        ArrayList<ReportDetailModel> reportsPending = new ArrayList<>();
        boolean flag = false;

        for (int i = 0; i < reports.size(); i++) {
            for (int j = 0; j < reports.get(i).getProjectDetail().size(); j++) {
                if (!reports.get(i).getProjectDetail().get(j)
                        .getProgressStatus().equals("Yes")) {
                    flag = true;
                    break;
                }
            }
            if (flag == true) {
                reportsPending.add(reports.get(i));
                flag = false;
            } else {
                reportsComplected.add(reports.get(i));
            }
        }

        if (status.equals("pending")) {
            prepareListData(reportsPending);
        } else if (status.equals("complected")) {
            prepareListData(reportsComplected);
        }

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        reportDetailsDataListView.setAdapter(listAdapter);
        for(int i=0; i < listAdapter.getGroupCount(); i++)
            reportDetailsDataListView.expandGroup(i);

    }

    private void prepareListData(ArrayList<ReportDetailModel> reports) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ProjectDetailModel>>();

        // Adding parent data
        for (int i=0; i<reports.size(); i++) {
            listDataHeader.add(reports.get(i).getLocationName());
            List<ProjectDetailModel> item = new ArrayList<ProjectDetailModel>();
            for (int j=0; j<reports.get(i).getProjectDetail().size(); j++) {
                item.add(reports.get(i).getProjectDetail().get(j));
            }
            listDataChild.put(listDataHeader.get(i), item); // Header, Child data
        }
    }
}