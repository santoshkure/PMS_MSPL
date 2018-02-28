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
import com.example.ratnesh.pms_mspl.Models.CatDetails;
import com.example.ratnesh.pms_mspl.Models.LocationDetail;
import com.example.ratnesh.pms_mspl.Models.ReportModel;
import com.example.ratnesh.pms_mspl.Models.CategoryDetail;

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
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent getProjectId = getIntent();
        projectId = getProjectId.getStringExtra("project_id");
        projectName = getProjectId.getStringExtra("project_name");

        projectNameTextView = (TextView) findViewById(R.id.project_name);
        projectNameTextView.setText(projectName);

        displayTableLayout = (TableLayout) findViewById(R.id.display_table_layout);
        viewReport();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ReportList.this, Reports.class);
        startActivity(intent);
    }

    private void viewReport() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REPORTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject obj = new JSONObject(response);

                            JSONArray locationJSONArray = obj.getJSONArray("report_list_status");
                            ArrayList<LocationDetail> location = new ArrayList<LocationDetail>();
                            ArrayList<CatDetails> categoryList = new ArrayList<CatDetails>();
                            CatDetails category = new CatDetails();
                            String CatName = "";

                            for (int i = 0; i < locationJSONArray.length(); i++) {
                                JSONObject item = locationJSONArray.getJSONObject(i);

                                if (!CatName.equals(item.getString("category"))) {
                                    CatName = item.getString("category");
                                    category.setLocationDetail(location);
                                    if (i != 0) {
                                        categoryList.add(category);
                                    }

                                    category = new CatDetails();
                                    location = new ArrayList<LocationDetail>();
                                    category.setCatName(CatName);

                                    LocationDetail locationDetail = new LocationDetail();
                                    locationDetail.setLocationId(item.getString("location_id"));
                                    locationDetail.setCategoryId(item.getString("category_id"));
                                    locationDetail.setProjectId(item.getString("project_id"));
                                    locationDetail.setProgressStatus(item.getString("progress_status"));
                                    location.add(locationDetail);
                                } else {
                                    LocationDetail locationDetail = new LocationDetail();
                                    locationDetail.setLocationId(item.getString("location_id"));
                                    locationDetail.setCategoryId(item.getString("category_id"));
                                    locationDetail.setProjectId(item.getString("project_id"));
                                    locationDetail.setProgressStatus(item.getString("progress_status"));
                                    location.add(locationDetail);
                                }

                                if (i == locationJSONArray.length() - 1) {
                                    category.setLocationDetail(location);
                                    categoryList.add(category);
                                }
                            }

                            CountComplete_Pending(categoryList);
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


    private void CountComplete_Pending(ArrayList<CatDetails> categoryList) {
        try {
            ArrayList<CategoryDetail> data = new ArrayList<CategoryDetail>();
            String locId = "";
            boolean flag = false;

            for (int i = 0; i < categoryList.size(); i++) {
                CategoryDetail t = new CategoryDetail();
                t.setCategory(categoryList.get(i).getCatName());

                for (int j = 0; j < categoryList.get(i).getLocationDetail().size(); j++) {
                    if (!locId.equals(categoryList.get(i).getLocationDetail().get(j).getLocationId())) {
                        if (j != 0) {
                            t.setCategoryId(categoryList.get(i).getLocationDetail().get(j).getCategoryId());
                            if (flag) {
                                int pend = t.getPending_count();
                                t.setPending_count(pend + 1);
                            } else {
                                int comp = t.getComplete_count();
                                t.setComplete_count(comp + 1);
                            }
                            flag = false;
                        }

                        locId = categoryList.get(i).getLocationDetail().get(j).getLocationId();
                        int count = t.getLoc_count();
                        t.setLoc_count(count + 1);

                        if (!categoryList.get(i).getLocationDetail().get(j).getProgressStatus().equals("Yes")) {
                            flag = true;
                        }
                    } else {
                        if (!flag) {
                            if (!categoryList.get(i).getLocationDetail().get(j).getProgressStatus().equals("Yes") ||
                                    categoryList.get(i).getLocationDetail().get(j).getProgressStatus().equals("null")) {
                                flag = true;
                            }
                        }
                    }

                    if (j == categoryList.get(i).getLocationDetail().size() -1) {
                        t.setCategoryId(categoryList.get(i).getLocationDetail().get(j).getCategoryId());
                        if (flag) {
                            int pend = t.getPending_count();
                            t.setPending_count(pend + 1);
                        } else {
                            int comp = t.getComplete_count();
                            t.setComplete_count(comp + 1);
                        }
                        flag = false;
                    }
                }
                data.add(t);
            }

            createRowData(data);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void createRowData(ArrayList<CategoryDetail> data) throws JSONException {
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

        for (int i = 0; i < data.size(); i++) {
            final CategoryDetail actor = data.get(i);

            row2 = new TableRow(ReportList.this);
            row2.setBackground(getResources().getDrawable(R.drawable.border));
            row2.setLayoutParams(lp);

            locationNameTextView = new TextView(ReportList.this);
            locationNameTextView.setText(actor.getCategory());
            locationNameTextView.setGravity(Gravity.CENTER);
            locationNameTextView.setPadding(10, 10, 10, 10);
            row2.addView(locationNameTextView);

            categoryTextView = new TextView(ReportList.this);
            categoryTextView.setText(String.valueOf(actor.getLoc_count()));
            categoryTextView.setGravity(Gravity.CENTER);
            categoryTextView.setPadding(10, 10, 10, 10);
            row2.addView(categoryTextView);

            complectedTextView = new TextView(ReportList.this);
            complectedTextView.setText(String.valueOf(actor.getComplete_count()));
            complectedTextView.setGravity(Gravity.CENTER);
            complectedTextView.setPadding(10, 10, 10, 10);
            complectedTextView.setId(i);

            if (actor.getComplete_count() != 0) {
                complectedTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                complectedTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent complectedIntent = new Intent(getApplicationContext(), ReportDetails.class);
                        complectedIntent.putExtra("category_id", actor.getCategoryId());
                        complectedIntent.putExtra("project_id", projectId);
                        complectedIntent.putExtra("project_name", projectName);
                        complectedIntent.putExtra("status", "complected");
                        startActivity(complectedIntent);
                    }
                });
            }

            row2.addView(complectedTextView);

            pendingTextView = new TextView(ReportList.this);
            pendingTextView.setText(String.valueOf(actor.getPending_count()));
            pendingTextView.setGravity(Gravity.CENTER);
            pendingTextView.setPadding(10, 10, 10, 10);
            pendingTextView.setId(i);

            if (actor.getPending_count() != 0) {
                pendingTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                pendingTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pendingIntent = new Intent(getApplicationContext(), ReportDetails.class);
                        pendingIntent.putExtra("category_id", actor.getCategoryId());
                        pendingIntent.putExtra("project_id", projectId);
                        pendingIntent.putExtra("status", "pending");
                        startActivity(pendingIntent);

                    }
                });
            }
            row2.addView(pendingTextView);

            TotalLoc = TotalLoc + actor.getLoc_count();
            TotalCompleted = TotalCompleted + actor.getComplete_count();
            TotalPending = TotalPending + actor.getPending_count();

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