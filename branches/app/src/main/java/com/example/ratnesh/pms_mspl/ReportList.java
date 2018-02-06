package com.example.ratnesh.pms_mspl;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
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
import com.example.ratnesh.pms_mspl.Models.ReportModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportList extends AppCompatActivity {

    private TextView projectNameTextView, complectedTextView, pendingTextView, locationNameTextView, categoryTextView, TempTextView;
    private String projectId, projectName;
    TableLayout displayTableLayout;
    private TableRow row1, row2, row3;
    int indexRow;
    ArrayList<String> complectedStatusArray, pendingStatusArray;
    int TotalLoc=0, TotalCompleted=0, TotalPending=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent getProjectId = getIntent();
        projectId = getProjectId.getStringExtra("project_id");
        projectName = getProjectId.getStringExtra("project_name");

        projectNameTextView = (TextView) findViewById(R.id.project_name);
        projectNameTextView.setText(projectName);

        displayTableLayout = (TableLayout) findViewById(R.id.display_table_layout);
        viewReport();
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ReportList.this, Reports.class);
        startActivity(intent);
    }

    ArrayList<ReportModel> reports = new ArrayList<>();
    ArrayList<LocationDetails> reportSatus = new ArrayList<LocationDetails>();
    JSONArray locationCountJSONArray;

    private void viewReport() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REPORTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int count = 0, partially = 0, completed = 0;

                            final JSONObject obj = new JSONObject(response);

                            locationCountJSONArray = obj.getJSONArray("location_count");
                            JSONArray locationListJSONArray = obj.getJSONArray("location_list");
                            JSONArray locationJSONArray = obj.getJSONArray("report_list_status");

                            Gson gson = new Gson();
                            for (int i = 0; i < locationJSONArray.length(); i++) {
                                LocationDetails founderArray = gson.fromJson(String.valueOf(locationJSONArray.get(i)), LocationDetails.class);
                                reportSatus.add(founderArray);
                            }

                            Boolean isComplected = false;
                            Boolean isMatched = false;
                            complectedStatusArray = new ArrayList<String>();
                            pendingStatusArray = new ArrayList<String>();
                            for (int i = 0; i < locationCountJSONArray.length(); i++) {
                                String category_id = locationCountJSONArray.getJSONObject(i).getString("category_id");
                                for (int j = 0; j < locationListJSONArray.length(); j++) {
                                    String location_id = locationListJSONArray.getJSONObject(j).getString("location_id");
                                    isComplected = true;
                                    isMatched = false;
                                    for (int k = 0; k < reportSatus.size(); k++) {
                                        if (reportSatus.get(k).getCategory_id().equals(category_id) && reportSatus.get(k).getLocation_id().equals(location_id)) {
                                            isMatched = true;
                                            if (reportSatus.get(k).getProgress_status().equals("Yes")) {

                                            } else {
                                                isComplected = false;
                                                break;
                                            }
                                        }
                                    }
                                    if (isMatched) {
                                        if (isComplected) {
                                            completed = completed + 1;
                                        } else {
                                            partially = partially + 1;
                                        }
                                    }

                                }
                                complectedStatusArray.add(String.valueOf(completed));
                                pendingStatusArray.add(String.valueOf(partially));
                                completed = 0;
                                partially = 0;
                            }

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
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void createRowData(final ArrayList<ReportModel> reports) throws JSONException {
        row1 = new TableRow(ReportList.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row1.setBackground(getResources().getDrawable(R.drawable.border));
        row1.setLayoutParams(lp);

        locationNameTextView = new TextView(ReportList.this);
        locationNameTextView.setText("Category");
        locationNameTextView.setPadding(10, 10, 10, 10);
        locationNameTextView.setTypeface(null, Typeface.BOLD);
        row1.addView(locationNameTextView);

        categoryTextView = new TextView(ReportList.this);
        categoryTextView.setText("Total Locations");
        categoryTextView.setGravity(Gravity.CENTER);
        categoryTextView.setPadding(10, 10, 10, 10);
        categoryTextView.setTypeface(null, Typeface.BOLD);
        row1.addView(categoryTextView);

        categoryTextView = new TextView(ReportList.this);
        categoryTextView.setText("Complected");
        categoryTextView.setPadding(10, 10, 10, 10);
        categoryTextView.setTypeface(null, Typeface.BOLD);
        row1.addView(categoryTextView);

        categoryTextView = new TextView(ReportList.this);
        categoryTextView.setText("Pending");
        categoryTextView.setPadding(10, 10, 10, 10);
        categoryTextView.setTypeface(null, Typeface.BOLD);
        row1.addView(categoryTextView);

        displayTableLayout.addView(row1, 0);

        for (int i = 0; i < locationCountJSONArray.length(); i++) {
            JSONObject actor = locationCountJSONArray.getJSONObject(i);

            ReportModel report = new ReportModel();
            report.setCat_id(actor.getString("category_id"));
            report.setCat_name(actor.getString("category_location"));
            report.setLocation_name(actor.getString("category_location"));
            report.setCompleted(complectedStatusArray.get(i));
            report.setPending(pendingStatusArray.get(i));

            reports.add(report);

            final String locationCount = actor.getString("location_count");
            final String category = actor.getString("category_location");

            row2 = new TableRow(ReportList.this);
            row2.setBackground(getResources().getDrawable(R.drawable.border));
            row2.setLayoutParams(lp);

            locationNameTextView = new TextView(ReportList.this);
            locationNameTextView.setText(category);
            locationNameTextView.setGravity(Gravity.CENTER);
            locationNameTextView.setPadding(10, 10, 10, 10);
            row2.addView(locationNameTextView);

            categoryTextView = new TextView(ReportList.this);
            categoryTextView.setText(locationCount);
            categoryTextView.setGravity(Gravity.CENTER);
            categoryTextView.setPadding(10, 10, 10, 10);
            row2.addView(categoryTextView);

            complectedTextView = new TextView(ReportList.this);
            complectedTextView.setText(complectedStatusArray.get(i));
            complectedTextView.setGravity(Gravity.CENTER);
            complectedTextView.setPadding(10, 10, 10, 10);
            complectedTextView.setId(i);

            if (!(Integer.parseInt(complectedStatusArray.get(i)) == 0)) {
                complectedTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                complectedTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent complectedIntent = new Intent(getApplicationContext(), ReportDetails.class);
                        complectedIntent.putExtra("category_id", reports.get(view.getId()).getCat_id());
                        complectedIntent.putExtra("project_id", projectId);
                        complectedIntent.putExtra("status", "complected");
                        startActivity(complectedIntent);
                    }
                });
            }

            row2.addView(complectedTextView);

            pendingTextView = new TextView(ReportList.this);
            pendingTextView.setText(pendingStatusArray.get(i));
            pendingTextView.setGravity(Gravity.CENTER);
            pendingTextView.setPadding(10, 10, 10, 10);
            pendingTextView.setId(i);

            if (!(Integer.parseInt(pendingStatusArray.get(i)) == 0)) {
                pendingTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                pendingTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pendingIntent = new Intent(getApplicationContext(), ReportDetails.class);
                        pendingIntent.putExtra("category_id", reports.get(view.getId()).getCat_id());
                        pendingIntent.putExtra("project_id", projectId);
                        pendingIntent.putExtra("status", "pending");
                        startActivity(pendingIntent);

                    }
                });
            }
            row2.addView(pendingTextView);

            TotalLoc = TotalLoc + Integer.parseInt(locationCount);
            TotalCompleted = TotalCompleted + Integer.parseInt(complectedStatusArray.get(i));
            TotalPending = TotalPending + Integer.parseInt(pendingStatusArray.get(i));

            indexRow = i;
            displayTableLayout.addView(row2, ++indexRow);
        }

        row3 = new TableRow(ReportList.this);
        row3.setBackground(getResources().getDrawable(R.drawable.border));
        row3.setLayoutParams(lp);

        TempTextView = new TextView(ReportList.this);
        TempTextView.setText("Total");
        TempTextView.setGravity(Gravity.CENTER);
        TempTextView.setPadding(10, 10, 10, 10);
        TempTextView.setTypeface(null, Typeface.BOLD);
        row3.addView(TempTextView);

        TempTextView = new TextView(ReportList.this);
        TempTextView.setText(String.valueOf(TotalLoc));
        TempTextView.setGravity(Gravity.CENTER);
        TempTextView.setPadding(10, 10, 10, 10);
        TempTextView.setTypeface(null, Typeface.BOLD);
        row3.addView(TempTextView);

        TempTextView = new TextView(ReportList.this);
        TempTextView.setText(String.valueOf(TotalCompleted));
        TempTextView.setGravity(Gravity.CENTER);
        TempTextView.setPadding(10, 10, 10, 10);
        TempTextView.setTypeface(null, Typeface.BOLD);
        row3.addView(TempTextView);

        TempTextView = new TextView(ReportList.this);
        TempTextView.setText(String.valueOf(TotalPending));
        TempTextView.setGravity(Gravity.CENTER);
        TempTextView.setPadding(10, 10, 10, 10);
        TempTextView.setTypeface(null, Typeface.BOLD);
        row3.addView(TempTextView);

        displayTableLayout.addView(row3, ++indexRow);


    }

}