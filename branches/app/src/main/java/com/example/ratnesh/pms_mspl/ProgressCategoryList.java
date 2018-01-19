package com.example.ratnesh.pms_mspl;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProgressCategoryList extends AppCompatActivity {

    private TableLayout displayTableLayout;
    private TableRow row2;
    private TextView progressItemNameTextView;
    private int indexRow;
    private String projectId, locationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_item_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent getIntent = getIntent();
        projectId = getIntent.getStringExtra("project_id");
        locationId = getIntent.getStringExtra("location_id");

        displayTableLayout = (TableLayout)findViewById(R.id.display_table_layout);
        setProgressItemList();
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ProgressCategoryList.this, MainActivity.class);
        startActivity(intent);
    }

    private void setProgressItemList(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_PROGRESSCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            final JSONObject obj = new JSONObject(response);
                                JSONArray projectList = obj.getJSONArray("progress_cate_list");

                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

                                for (int i = 0; i <= projectList.length(); i++) {
                                    JSONObject actor = projectList.getJSONObject(i);
                                    final String progress_category_id = actor.getString("progress_category_id");
                                    final String progress_category = actor.getString("progress_category");

                                    row2 = new TableRow(ProgressCategoryList.this);
                                    row2.setBackground(getResources().getDrawable(R.drawable.border));
                                    row2.setLayoutParams(lp);


                                    progressItemNameTextView = new TextView(ProgressCategoryList.this);
                                    progressItemNameTextView.setText(progress_category);
                                    progressItemNameTextView.setGravity(Gravity.LEFT);
                                    progressItemNameTextView.setTextSize(16);
                                    progressItemNameTextView.setPadding(22, 22, 60, 22);
                                    progressItemNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
                                    row2.addView(progressItemNameTextView);


                                    indexRow = i;
                                    displayTableLayout.addView(row2, indexRow++);

                                    row2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                            Intent sendProjectId = new Intent(ProgressCategoryList.this, AddProgressCategory.class);
                                            sendProjectId.putExtra("progress_category_id", progress_category_id);
                                            sendProjectId.putExtra("progress_category", progress_category);
                                            startActivity(sendProjectId);
                                        }
                                    });
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
                params.put("location_id", locationId);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
