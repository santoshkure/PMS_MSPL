package com.example.ratnesh.pms_mspl;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Reports extends AppCompatActivity {
    private TableLayout displayTableLayout;
    private TableRow row2;
    private TextView projectNameTextView;
    private int indexRow;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayTableLayout = (TableLayout)findViewById(R.id.display_table_layout);

        viewProjects();
    }

    public void onBackPressed() {
        Intent intent = new Intent(Reports.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void viewProjects(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_PROJECTNAMES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            final JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray projectList = obj.getJSONArray("project_list");

                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

                                for (int i = 0; i <= projectList.length(); i++) {
                                    JSONObject actor = projectList.getJSONObject(i);
                                    final String projectId = actor.getString("project_id");
                                    final String projectName = actor.getString("project_name");

                                    row2 = new TableRow(Reports.this);
                                    row2.setBackground(getResources().getDrawable(R.drawable.border));
                                    row2.setLayoutParams(lp);


                                    projectNameTextView = new TextView(Reports.this);
                                    projectNameTextView.setText(projectName);
                                    projectNameTextView.setGravity(Gravity.LEFT);
                                    projectNameTextView.setTextSize(16);
                                    projectNameTextView.setPadding(22, 22, 60, 22);
                                    projectNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                                    row2.addView(projectNameTextView);

                                    indexRow = i;
                                    displayTableLayout.addView(row2, indexRow++);

                                    row2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                            Intent sendProjectId = new Intent(Reports.this, ReportList.class);
                                            sendProjectId.putExtra("project_id", projectId);
                                            sendProjectId.putExtra("project_name", projectName);
                                            startActivity(sendProjectId);
                                            finish();
                                        }
                                    });
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "No Project Available..", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (JSONException e) {
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
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("party_id", "10000");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

