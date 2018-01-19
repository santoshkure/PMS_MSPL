package com.example.ratnesh.pms_mspl;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditLoctionDetails extends AppCompatActivity {
    private String locationId, locationName;
    private TextView locationNameTextView, itemNameTextView, quantityTextView, remarkTextView;
    private EditText locationNameEditText, addressEditText, remarkEditText, itemRemarkEditText;
    private TableLayout displayTableLayout;
    private Button saveButton;
    private TableRow row1, row2;
    Map<String,String> paramsItemRemark;
    private List<EditText> itemArrayList;
    int indexRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_loction_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent getlocationIntent = getIntent();
        locationId = getlocationIntent.getStringExtra("location_id");
        locationName = getlocationIntent.getStringExtra("location_name");

        locationNameTextView = (TextView)findViewById(R.id.location_name);
        locationNameTextView.setText(locationName);
        displayTableLayout = (TableLayout) findViewById(R.id.display_table_layout);
        saveButton = (Button) findViewById(R.id.buttonSubmit);

        addLocation();
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(EditLoctionDetails.this, Projects.class);
        startActivity(intent);
    }

    private void addLocation() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SETSPINNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            final JSONObject obj = new JSONObject(response);

                            itemArrayList = new ArrayList<EditText>();
                            JSONArray itemJSONArray = obj.getJSONArray("item_location_list");

                            row1 = new TableRow(EditLoctionDetails.this);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row1.setBackground(getResources().getDrawable(R.drawable.border));
                            row1.setLayoutParams(lp);

                            itemNameTextView = new TextView(EditLoctionDetails.this);
                            itemNameTextView.setText("Item Name");
                            itemNameTextView.setPadding(10, 10, 10, 10);
                            itemNameTextView.setTypeface(null, Typeface.BOLD);
                            row1.addView(itemNameTextView);


                            quantityTextView = new TextView(EditLoctionDetails.this);
                            quantityTextView.setText("Quantity");
                            quantityTextView.setPadding(10, 10, 10, 10);
                            quantityTextView.setTypeface(null, Typeface.BOLD);
                            row1.addView(quantityTextView);

                            remarkTextView = new TextView(EditLoctionDetails.this);
                            remarkTextView.setText("Remarks");
                            remarkTextView.setPadding(10, 10, 10, 10);
                            remarkTextView.setTypeface(null, Typeface.BOLD);
                            row1.addView(remarkTextView);

                            displayTableLayout.addView(row1, 0);

                            for (int i = 0; i < itemJSONArray.length(); i++) {
                                JSONObject actor = itemJSONArray.getJSONObject(i);

                                final String itemId = actor.getString("locationitem_id");
                                int itemIdInt = Integer.parseInt(itemId);

                                final String itemName = actor.getString("item_name");
                                final String quantity = actor.getString("quantity");
                                final String remarks = actor.getString("remarks");

                                row2 = new TableRow(EditLoctionDetails.this);
                                row2.setBackground(getResources().getDrawable(R.drawable.border));
                                row2.setLayoutParams(lp);

                                itemNameTextView = new TextView(EditLoctionDetails.this);
                                itemNameTextView.setText(itemName);
                                itemNameTextView.setPadding(10, 10, 10, 10);
                                row2.addView(itemNameTextView);

                                quantityTextView = new TextView(EditLoctionDetails.this);
                                quantityTextView.setText(quantity);
                                quantityTextView.setPadding(10, 10, 10, 10);
                                row2.addView(quantityTextView);

                                itemRemarkEditText = new EditText(EditLoctionDetails.this);
                                itemRemarkEditText.setTextSize(14);
                                itemRemarkEditText.setText(remarks);
                                itemArrayList.add(itemRemarkEditText);
                                itemRemarkEditText.setId(itemIdInt);
                                itemRemarkEditText.setPadding(10, 10, 10, 10);
                                row2.addView(itemRemarkEditText);

                                indexRow = i;
                                displayTableLayout.addView(row2, ++indexRow);

                            }

                            final String[] itemsRemark = new String[itemArrayList.size()];

                            saveButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View arg0) {

                                    paramsItemRemark = new HashMap<String, String>();

                                    for (int i = 0; i < itemArrayList.size(); i++) {

                                        itemsRemark[i] = itemArrayList.get(i).getText().toString();
                                        if (!itemsRemark[i].isEmpty()) {
                                            int itemId = itemArrayList.get(i).getId();
                                            String itemIdString = String.valueOf(itemId);
                                            Log.d("dsff", itemsRemark[i] + "   " + itemId);
                                            paramsItemRemark.put(itemIdString, itemsRemark[i]);
                                        } else {
                                            Log.d("dsff", "ds");
                                        }

                                    }
                                    sendLocationData();

                                }
                            });

                        } catch (JSONException e) {
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("location_id", locationId);
                params.put("msg", "ss");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void sendLocationData(){

        paramsItemRemark.put("location_id", locationId);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_UPDATELOCATIONDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(EditLoctionDetails.this, Projects.class);
                        startActivity(intent);
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
                return paramsItemRemark;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}