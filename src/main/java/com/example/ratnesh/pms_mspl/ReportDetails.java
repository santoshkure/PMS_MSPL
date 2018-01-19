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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportDetails extends AppCompatActivity {

    private String projectId, categoryId, status;
    LinearLayout mainLinearLayout;
    ListView reportDetailsDataListView;

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

        mainLinearLayout = (LinearLayout) findViewById(R.id.main_layout);
        reportDetailsDataListView = (ListView) findViewById(R.id.report_data);
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

                            createRowData(reports);

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

    private void createRowData(final ArrayList<ReportDetailsModel> reports) throws JSONException {

        for (int i = 0; i < reoprtDetailsJSONArray.length(); i++) {
            JSONObject actor = reoprtDetailsJSONArray.getJSONObject(i);

            ReportDetailsModel report = new ReportDetailsModel();
            report.setLocationName(actor.getString("location_name"));
            report.setProjectCategory(actor.getString("progress_category"));
            report.setProgressDate(actor.getString("progress_date"));
            report.setProgressRemark(actor.getString("progress_remark"));
            report.setProgressStatus(actor.getString("progress_status"));

            reports.add(report);
        }

        ArrayList<ReportDetailsModel> reportsComplected = new ArrayList<>();
        ArrayList<ReportDetailsModel> reportsPending = new ArrayList<>();

        Map<String, Integer> locationNamesMap = new HashMap();

        int count = 0;
        for (int i=0; i<reports.size(); i++) {

            if (!locationNamesMap.containsKey(reports.get(i).getLocationName())) {
                locationNamesMap.put(reports.get(i).getLocationName(), 1);
                // hm1.put(x, 1);
            } else {
                count++;
                locationNamesMap.put(reports.get(i).getLocationName(), locationNamesMap.get(reports.get(i).getLocationName())+1);

            }
        }

        Log.d("dsfasa", String.valueOf(locationNamesMap));

        for (Map.Entry<String,Integer> entry : locationNamesMap.entrySet()) {

            for (int j = 0; j < reports.size(); j++) {

                if(reports.get(j).getLocationName().equals(entry.getKey())){

                    for (int k = 0; k < entry.getValue(); k++) {
                        if (reports.get(j).getProgressStatus().equals("Yes")) {
                            reportsComplected.add(reports.get(j));
                        }
                        else {

                            reportsPending.add(reports.get(j));
                            Log.d("dsfadsf", reports.get(j).getLocationName());
                        }
                    }
                }
            }

        }


        /*String location = "";
        for (int i=0; i<reports.size()-1; i++) {
            if (!reports.get(i).getLocationName().equals(location)) {
                if (reports.get(i).getProgressStatus().equals("Yes") &&
                        reports.get(i+1).getProgressStatus().equals("Yes")) {
                    reportsComplected.add(reports.get(i));
                    location = reports.get(i).getLocationName();
                } else {
                    location = reports.get(i).getLocationName();
                    reportsPending.add(reports.get(i));
                }
            }
        }

        ArrayList<ReportDetailsModel> reportsComplected1 = new ArrayList<>();
        ArrayList<ReportDetailsModel> reportsPending1 = new ArrayList<>();
        for(int i=0; i<reportsComplected.size(); i++) {
            for(int j=0; j<reports.size(); j++) {
                if(reportsComplected.get(i).getLocationName().equals(reports.get(j).getLocationName())){
                    reportsComplected1.add(reports.get(j));
                }
            }
        }

        for(int i=0; i<reportsPending.size(); i++) {
            for(int j=0; j<reports.size(); j++) {
                if(reportsPending.get(i).getLocationName().equals(reports.get(j).getLocationName())){
                    reportsPending1.add(reports.get(j));
                }
            }
        }*/

//        if(status.equals("pending")){
//            CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), reportsPending1);
//            reportDetailsDataListView.setAdapter(adapter );
//        }
//        else if (status.equals("complected")) {
//            CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), reportsComplected1);
//            reportDetailsDataListView.setAdapter(adapter);
//        }


    }
}