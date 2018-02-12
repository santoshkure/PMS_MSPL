package com.example.ratnesh.pms_mspl;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
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
import com.example.ratnesh.pms_mspl.Models.ReportDetailsModel;
import com.example.ratnesh.pms_mspl.Models.ReportModel;
import com.example.ratnesh.pms_mspl.Models.TempModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDetails extends AppCompatActivity {

    private String projectId, categoryId, status;
    TableLayout displayTableLayout;
    private TableRow row1, row2;
    private TextView TempTextView, processNameTextView, locCountTextView, complectedTextView, pendingTextView;
    ExpandableListView reportDetailsDataListView;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<ProjectDetailModel>> listDataChild;
    int indexRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        projectId = intent.getStringExtra("project_id");
        categoryId = intent.getStringExtra("category_id");
        status = intent.getStringExtra("status");

        displayTableLayout = (TableLayout) findViewById(R.id.display_table_layout);
        reportDetailsDataListView = (ExpandableListView) findViewById(R.id.lvExp);
        viewReport();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

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

    private void CountComplete_Pending(ArrayList<ReportDetailModel> reports) {
        try {
            ArrayList<String> Progress_Categories = new ArrayList<>();
            ArrayList<TempModel> Progress_Categories1 = new ArrayList<TempModel>();

            for (int i = 0; i < reports.size(); i++) {
                for (int j = 0; j < reports.get(i).getProjectDetail().size(); j++) {
                    String str = reports.get(i).getProjectDetail().get(j).getProjectCategory();
                    if (!Progress_Categories.contains(str)) {
                        Progress_Categories.add(str);
                        TempModel t = new TempModel();
                        t.setLocName(reports.get(i).getLocationName());
                        t.setProj_cat(str);
                        t.setCount(1);

                        if (reports.get(i).getProjectDetail().get(j).getProgressStatus().equals("Yes")) {
                            t.setCompleted(1);
                            t.setPending(0);
                        } else {
                            t.setCompleted(0);
                            t.setPending(1);
                        }
                        ArrayList<ProjectDetailModel> p = new ArrayList<>();
                        p.add(reports.get(i).getProjectDetail().get(j));
                        t.setDetails(p);

                        Progress_Categories1.add(t);
                    } else {
                        for (int k=0; k<Progress_Categories1.size(); k++) {
                            if (Progress_Categories1.get(k).getProj_cat().equals(str)) {
                                int c = Progress_Categories1.get(k).getCount();
                                Progress_Categories1.get(k).setCount(c+1);

                                if (reports.get(i).getProjectDetail().get(j).getProgressStatus().equals("Yes")) {
                                    int C = Progress_Categories1.get(k).getCompleted();
                                    Progress_Categories1.get(k).setCompleted(C+1);
                                } else {
                                    int P = Progress_Categories1.get(k).getPending();
                                    Progress_Categories1.get(k).setPending(P+1);
                                }
                                Progress_Categories1.get(k).getDetails().add(reports.get(i).getProjectDetail().get(j));
                            }
                        }
                    }
                }
            }

            createRowData1(Progress_Categories1);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void createRowData1(final ArrayList<TempModel> Progress_Categories) throws JSONException {
        row1 = new TableRow(ReportDetails.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row1.setBackground(getResources().getDrawable(R.drawable.border));
        row1.setLayoutParams(lp);

        TempTextView = new TextView(ReportDetails.this);
        TempTextView.setText("Progress Categories");
        TempTextView.setPadding(10, 10, 10, 10);
        TempTextView.setTypeface(null, Typeface.BOLD);
        row1.addView(TempTextView);

        TempTextView = new TextView(ReportDetails.this);
        TempTextView.setText("Total Locations");
        TempTextView.setPadding(10, 10, 10, 10);
        TempTextView.setTypeface(null, Typeface.BOLD);
        row1.addView(TempTextView);

        TempTextView = new TextView(ReportDetails.this);
        TempTextView.setText("Complected");
        TempTextView.setPadding(10, 10, 10, 10);
        TempTextView.setTypeface(null, Typeface.BOLD);
        row1.addView(TempTextView);

        TempTextView = new TextView(ReportDetails.this);
        TempTextView.setText("Pending");
        TempTextView.setPadding(10, 10, 10, 10);
        TempTextView.setTypeface(null, Typeface.BOLD);
        row1.addView(TempTextView);

        displayTableLayout.addView(row1, 0);

        for (int i = 0; i < Progress_Categories.size(); i++) {
            final String category = Progress_Categories.get(i).getProj_cat();
            final String total = String.valueOf(Progress_Categories.get(i).getCount());
            final String pending = String.valueOf(Progress_Categories.get(i).getPending());
            final String completed = String.valueOf(Progress_Categories.get(i).getCompleted());

            row2 = new TableRow(ReportDetails.this);
            row2.setBackground(getResources().getDrawable(R.drawable.border));
            row2.setLayoutParams(lp);

            processNameTextView = new TextView(ReportDetails.this);
            processNameTextView.setText(category);
            processNameTextView.setPadding(10, 10, 10, 10);
            row2.addView(processNameTextView);

            locCountTextView = new TextView(ReportDetails.this);
            locCountTextView.setText(total);
            locCountTextView.setGravity(Gravity.CENTER);
            locCountTextView.setPadding(10, 10, 10, 10);
            row2.addView(locCountTextView);

            complectedTextView = new TextView(ReportDetails.this);
            complectedTextView.setText(completed);
            complectedTextView.setGravity(Gravity.CENTER);
            complectedTextView.setPadding(10, 10, 10, 10);
            complectedTextView.setId(i);

            if (!(Integer.parseInt(completed) == 0)) {
                complectedTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                final int finalI = i;
                complectedTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent complectedIntent = new Intent(getApplicationContext(), ProgressDetails.class);

                        ArrayList<ProjectDetailModel> reports = Progress_Categories.get(finalI).getDetails();
                        ArrayList<ProjectDetailModel> Completed = new ArrayList<>();
                        for (int i=0; i<reports.size(); i++) {
                            if (reports.get(i).getProgressStatus().equals("Yes")) {
                                Completed.add(reports.get(i));
                            }
                        }

                        Gson gson = new Gson();
                        complectedIntent.putExtra("details", gson.toJson(Completed));
                        startActivity(complectedIntent);
                    }
                });
            }

            row2.addView(complectedTextView);

            pendingTextView = new TextView(ReportDetails.this);
            pendingTextView.setText(pending);
            pendingTextView.setGravity(Gravity.CENTER);
            pendingTextView.setPadding(10, 10, 10, 10);
            pendingTextView.setId(i);

            if (!(Integer.parseInt(pending) == 0)) {
                pendingTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                final int finalI = i;
                pendingTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pendingIntent = new Intent(getApplicationContext(), ProgressDetails.class);

                        ArrayList<ProjectDetailModel> reports = Progress_Categories.get(finalI).getDetails();
                        ArrayList<ProjectDetailModel> Pending = new ArrayList<>();
                        for (int i=0; i<reports.size(); i++) {
                            if (!reports.get(i).getProgressStatus().equals("Yes")) {
                                Pending.add(reports.get(i));
                            }
                        }

                        Gson gson = new Gson();
                        pendingIntent.putExtra("details", gson.toJson(Pending));
                        startActivity(pendingIntent);
                    }
                });
            }
            row2.addView(pendingTextView);

            indexRow = i;
            displayTableLayout.addView(row2, ++indexRow);
        }
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
                projectDetail.setImagePath(actor.getString("image_path"));
                projectDetail.setLocationName(actor.getString("location_name"));
                projectDetail.setRegister_by(actor.getString("register_by"));
                project.add(projectDetail);
            } else {
                ProjectDetailModel projectDetail = new ProjectDetailModel();
                projectDetail.setProjectCategory(actor.getString("progress_category"));
                projectDetail.setProgressDate(actor.getString("progress_date"));
                projectDetail.setProgressRemark(actor.getString("progress_remark"));
                projectDetail.setProgressStatus(actor.getString("progress_status"));
                projectDetail.setImagePath(actor.getString("image_path"));
                projectDetail.setLocationName(actor.getString("location_name"));
                projectDetail.setRegister_by(actor.getString("register_by"));
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
            CountComplete_Pending(reportsPending);
//            prepareListData(reportsPending);
        } else if (status.equals("complected")) {
            CountComplete_Pending(reportsComplected);
//            prepareListData(reportsComplected);
        }

//        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
//        reportDetailsDataListView.setAdapter(listAdapter);
//        for(int i=0; i < listAdapter.getGroupCount(); i++)
//            reportDetailsDataListView.expandGroup(i);
    }

    private void prepareListData(ArrayList<ReportDetailModel> reports) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ProjectDetailModel>>();

        // Adding parent data
        for (int i = 0; i < reports.size(); i++) {
            listDataHeader.add(reports.get(i).getLocationName());
            List<ProjectDetailModel> item = new ArrayList<ProjectDetailModel>();
            for (int j = 0; j < reports.get(i).getProjectDetail().size(); j++) {
                item.add(reports.get(i).getProjectDetail().get(j));
            }
            listDataChild.put(listDataHeader.get(i), item); // Header, Child data
        }
    }
}