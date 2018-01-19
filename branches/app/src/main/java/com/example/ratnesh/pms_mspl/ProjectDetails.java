package com.example.ratnesh.pms_mspl;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ProjectDetails extends AppCompatActivity {

    private TextView projectNameTextView, projectTypeTextView, clientNameTextView, contactPersonTextView,
            contactNoTextView, statusTextView, locationNameTextView, categoryTextView;
    private String projectId, projectName;
    Button addLocationButton;
    TableLayout displayTableLayout;
    private TableRow row1, row2;
    int indexRow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent getProjectId = getIntent();
        projectId = getProjectId.getStringExtra("project_id");
        projectName = getProjectId.getStringExtra("project_name");

        projectNameTextView = (TextView)findViewById(R.id.project_name);
        projectTypeTextView = (TextView)findViewById(R.id.project_type);
        clientNameTextView = (TextView)findViewById(R.id.client_name);
        contactPersonTextView = (TextView)findViewById(R.id.contact_person);
        contactNoTextView = (TextView)findViewById(R.id.contact_no);
        statusTextView = (TextView)findViewById(R.id.status);

        displayTableLayout = (TableLayout)findViewById(R.id.display_table_layout);
        addLocationButton = (Button)findViewById(R.id.button_add);

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationIntent = new Intent(ProjectDetails.this, AddLocation.class);
                locationIntent.putExtra("project_id", projectId);
                locationIntent.putExtra("project_name", projectName);
                startActivity(locationIntent);
            }
        });
        viewProjectDetails();
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ProjectDetails.this, Projects.class);
        startActivity(intent);
    }

    private void viewProjectDetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_PROJECTDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray projectDetailsJSONArray = obj.getJSONArray("project_details");

                                for (int i = 0; i < projectDetailsJSONArray.length(); i++) {
                                    JSONObject actor = projectDetailsJSONArray.getJSONObject(i);
                                    final String projectId = actor.getString("project_id");
                                    final String projectName = actor.getString("project_name");
                                    final String projectType = actor.getString("project_type");
                                    final String clientName = actor.getString("client_name");
                                    final String contactPerson = actor.getString("contact_person");
                                    final String contactNo = actor.getString("contact_no");
                                    final String status = actor.getString("status");

                                    projectNameTextView.setText(projectName);
                                    projectTypeTextView.setText(projectType);
                                    clientNameTextView.setText(clientName);
                                    contactPersonTextView.setText(contactPerson);
                                    contactNoTextView.setText(contactNo);
                                    statusTextView.setText(status);
                                }

                                JSONArray locationDetailsJSONArray = obj.getJSONArray("location_details");

                                row1 = new TableRow(ProjectDetails.this);
                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                row1.setBackground(getResources().getDrawable(R.drawable.border));
                                row1.setLayoutParams(lp);

                                locationNameTextView = new TextView(ProjectDetails.this);
                                locationNameTextView.setText("Item Name");
                                locationNameTextView.setPadding(10, 10, 10, 10);
                                locationNameTextView.setTypeface(null, Typeface.BOLD);
                                row1.addView(locationNameTextView);


                                categoryTextView = new TextView(ProjectDetails.this);
                                categoryTextView.setText("Quantity");
                                categoryTextView.setPadding(10, 10, 10, 10);
                                categoryTextView.setTypeface(null, Typeface.BOLD);
                                row1.addView(categoryTextView);

                                displayTableLayout.addView(row1, 0);

                                for (int i = 0; i < locationDetailsJSONArray.length(); i++) {
                                    JSONObject actor = locationDetailsJSONArray.getJSONObject(i);

                                    final String locationId = actor.getString("location_id");

                                    final String locationName = actor.getString("location_name");
                                    final String category = actor.getString("description");

                                    row2 = new TableRow(ProjectDetails.this);
                                    row2.setBackground(getResources().getDrawable(R.drawable.border));
                                    row2.setLayoutParams(lp);

                                    locationNameTextView = new TextView(ProjectDetails.this);
                                    locationNameTextView.setText(locationName);
                                    locationNameTextView.setPadding(10, 10, 10, 10);
                                    row2.addView(locationNameTextView);

                                    categoryTextView = new TextView(ProjectDetails.this);
                                    categoryTextView.setText(category);
                                    categoryTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    categoryTextView.setPadding(10, 10, 10, 10);
                                    row2.addView(categoryTextView);

                                    indexRow = i;
                                    displayTableLayout.addView(row2, ++indexRow);

                                    row2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent locationIntent = new Intent(ProjectDetails.this, EditLoctionDetails.class);
                                            locationIntent.putExtra("location_id", locationId);
                                            locationIntent.putExtra("location_name", locationName);
                                            startActivity(locationIntent);
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
                params.put("project_id", projectId);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
