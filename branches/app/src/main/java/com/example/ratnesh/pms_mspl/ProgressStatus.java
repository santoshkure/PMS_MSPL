package com.example.ratnesh.pms_mspl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class ProgressStatus extends AppCompatActivity {

    private Spinner projectNameSpinner, locationNameSpinner, progressCategorySpinner;
    private String projectName, locationName, progressCategoryName, progressCategoryId, projectId, locationId;
    Button searchButton;
    private String[] projectIdArray, projectNameArray, locationIdArray, locationNameArray, progressCategoryIdArray ,progressCategoryNameArray;
    public ArrayAdapter<String> SpinerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        projectNameSpinner = (Spinner)findViewById(R.id.project_name);
        locationNameSpinner = (Spinner)findViewById(R.id.location_name);
        progressCategorySpinner = (Spinner)findViewById(R.id.progress_category);

        setProjectSpinner();

        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(projectNameSpinner.getSelectedItem() == "Select Project")
                {
                    Toast.makeText(getApplicationContext(), "Please Select Project", Toast.LENGTH_LONG).show();

                }
                else if(locationNameSpinner.getSelectedItem() == "Select Location")
                {
                    Toast.makeText(getApplicationContext(), "Please Select Location", Toast.LENGTH_LONG).show();

                }
                else if(progressCategorySpinner.getSelectedItem() == "Select Progress Category")
                {
                    Toast.makeText(getApplicationContext(), "Please Select Location", Toast.LENGTH_LONG).show();

                }
                else {
                    int pos = progressCategorySpinner.getSelectedItemPosition();
                    progressCategoryId = progressCategoryIdArray[pos-1];
                    progressCategoryName = progressCategoryNameArray[pos-1];

                    Intent intent = new Intent(ProgressStatus.this, AddProgressCategory.class);
                    intent.putExtra("progress_category_id", progressCategoryId);
                    intent.putExtra("location_id", locationId);
                    intent.putExtra("project_id", projectId);
                    intent.putExtra("progress_category_name", progressCategoryName);
                    intent.putExtra("location_name", locationName);
                    intent.putExtra("project_name", projectName);
                    startActivity(intent);
                    finish();
                }
            }
        });

        projectNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        break;
                    default:
                        //projectId = projectIdArray[position-1];
                        int pos = projectNameSpinner.getSelectedItemPosition();
                        projectId = projectIdArray[pos-1];
                        projectName = projectNameArray[pos-1];
                        setLocationSpinner();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        locationNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        break;
                    default:
                        //locationId = locationIdArray[position-1];

                        int pos = locationNameSpinner.getSelectedItemPosition();
                        locationId = locationIdArray[pos-1];
                        locationName = locationNameArray[pos-1];
                        setProgressCategorySpinner();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

    }

    public void onBackPressed() {
        Intent intent = new Intent(ProgressStatus.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setProjectSpinner(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SETSPINNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            final JSONObject obj = new JSONObject(response);

                            JSONArray projectList = obj.getJSONArray("project_list");

                            projectIdArray = new String[projectList.length()];
                            projectNameArray = new String[projectList.length()];

                            for (int i = 0; i < projectList.length(); i++) {
                                JSONObject actor = projectList.getJSONObject(i);
                                projectIdArray[i] = actor.getString("project_id");
                                projectNameArray[i] = actor.getString("project_name");

                            }
                            SpinerAdapter = new ArrayAdapter<String>(ProgressStatus.this, android.R.layout.simple_spinner_dropdown_item);
                            SpinerAdapter.add("Select Project");
                            SpinerAdapter.addAll(projectNameArray);
                            projectNameSpinner.setAdapter(SpinerAdapter);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("dsadd", String.valueOf(e));

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
                params.put("msg", "ff");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void setLocationSpinner(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SETLOCATIONSPINNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            final JSONObject obj = new JSONObject(response);

                            JSONArray locationList = obj.getJSONArray("location_list");

                            locationIdArray = new String[locationList.length()];
                            locationNameArray = new String[locationList.length()];

                            for (int i = 0; i < locationList.length(); i++) {
                                JSONObject actor = locationList.getJSONObject(i);
                                locationIdArray[i] = actor.getString("location_id");
                                locationNameArray[i] = actor.getString("location_name");

                            }
                            SpinerAdapter = new ArrayAdapter<String>(ProgressStatus.this, android.R.layout.simple_spinner_dropdown_item);
                            SpinerAdapter.add("Select Location");
                            SpinerAdapter.addAll(locationNameArray);
                            locationNameSpinner.setAdapter(SpinerAdapter);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("dsadd", String.valueOf(e));

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

    private void setProgressCategorySpinner(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_PROGRESSCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            final JSONObject obj = new JSONObject(response);

                            JSONArray progressCategoryList = obj.getJSONArray("progress_cate_list");

                            progressCategoryIdArray = new String[progressCategoryList.length()];
                            progressCategoryNameArray = new String[progressCategoryList.length()];

                            for (int i = 0; i < progressCategoryList.length(); i++) {
                                JSONObject actor = progressCategoryList.getJSONObject(i);
                                progressCategoryIdArray[i] = actor.getString("progress_category_id");
                                progressCategoryNameArray[i] = actor.getString("progress_category");

                            }
                            SpinerAdapter = new ArrayAdapter<String>(ProgressStatus.this, android.R.layout.simple_spinner_dropdown_item);
                            SpinerAdapter.add("Select Progress Category");
                            SpinerAdapter.addAll(progressCategoryNameArray);
                            progressCategorySpinner.setAdapter(SpinerAdapter);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("dsadd", String.valueOf(e));

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
                params.put("location_id", locationId);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
