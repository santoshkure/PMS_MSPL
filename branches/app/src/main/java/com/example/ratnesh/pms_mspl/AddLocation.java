package com.example.ratnesh.pms_mspl;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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

public class AddLocation extends AppCompatActivity {

    private String partyId, projectId, projectName, categoryId, categoryName, locationName, category,
            address, remark ;
    private TextView projectNameTextView, itemNameTextView, quantityTextView, remarkTextView;
    private EditText locationNameEditText, addressEditText, remarkEditText, itemRemarkEditText;
    private User user;
    private Spinner categorySpinner;
    private TableLayout displayTableLayout;
    private Button saveButton;
    private String[] categoryIdArray, categoryNameArray;
    public ArrayAdapter<String> SpinerAdapter;
    private List<EditText> itemArrayList;
    private TableRow row1, row2;
    Map<String,String> paramsItemRemark;
    int indexRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent getProjectIdIntent = getIntent();
        projectId = getProjectIdIntent.getStringExtra("project_id");
        projectName = getProjectIdIntent.getStringExtra("project_name");

        categorySpinner = (Spinner)findViewById(R.id.category);
        projectNameTextView = (TextView)findViewById(R.id.project_name);
        projectNameTextView.setText(projectName);

        locationNameEditText = (EditText)findViewById(R.id.location_name);
        addressEditText = (EditText)findViewById(R.id.address);
        remarkEditText = (EditText)findViewById(R.id.remark);

        displayTableLayout = (TableLayout)findViewById(R.id.display_table_layout);
        saveButton = (Button)findViewById(R.id.buttonSubmit);

        addLocation();
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(AddLocation.this, Projects.class);
        startActivity(intent);
    }

    private void addLocation(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SETSPINNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            final JSONObject obj = new JSONObject(response);

                            JSONArray siteList = obj.getJSONArray("category_list");

                            categoryIdArray = new String[siteList.length()];
                            categoryNameArray = new String[siteList.length()];

                            for (int i = 0; i < siteList.length(); i++) {
                                JSONObject actor = siteList.getJSONObject(i);
                                final String categoryId = actor.getString("category_id");
                                final String description = actor.getString("description");
                                categoryIdArray[i] = categoryId;
                                categoryNameArray[i] = description;

                            }
                            SpinerAdapter = new ArrayAdapter<String>(AddLocation.this, android.R.layout.simple_spinner_dropdown_item);
                            SpinerAdapter.add("Select Category");
                            SpinerAdapter.addAll(categoryNameArray);
                            categorySpinner.setAdapter(SpinerAdapter);


                            Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                                    Toolbar.LayoutParams.WRAP_CONTENT);

                            itemArrayList = new ArrayList<EditText>();


                            JSONArray itemJSONArray = obj.getJSONArray("item_list");

                            row1 = new TableRow(AddLocation.this);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row1.setBackground(getResources().getDrawable(R.drawable.border));
                            row1.setLayoutParams(lp);

                            itemNameTextView = new TextView(AddLocation.this);
                            itemNameTextView.setText("Item Name");
                            itemNameTextView.setPadding(10, 10, 10, 10);
                            itemNameTextView.setTypeface(null, Typeface.BOLD);
                            row1.addView(itemNameTextView);


                            quantityTextView = new TextView(AddLocation.this);
                            quantityTextView.setText("Quantity");
                            quantityTextView.setPadding(10, 10, 10, 10);
                            quantityTextView.setTypeface(null, Typeface.BOLD);
                            row1.addView(quantityTextView);

                            remarkTextView = new TextView(AddLocation.this);
                            remarkTextView.setText("Remarks");
                            remarkTextView.setPadding(10, 10, 10, 10);
                            remarkTextView.setTypeface(null, Typeface.BOLD);
                            row1.addView(remarkTextView);

                            displayTableLayout.addView(row1, 0);

                            for (int i = 0; i < itemJSONArray.length(); i++) {
                                JSONObject actor = itemJSONArray.getJSONObject(i);

                                final String itemId = actor.getString("item_id");
                                int itemIdInt = Integer.parseInt(itemId);

                                final String itemName = actor.getString("item_name");
                                final String quantity = actor.getString("quantity");

                                row2 = new TableRow(AddLocation.this);
                                row2.setBackground(getResources().getDrawable(R.drawable.border));
                                row2.setLayoutParams(lp);

                                itemNameTextView = new TextView(AddLocation.this);
                                itemNameTextView.setText(itemName);
                                itemNameTextView.setPadding(10, 10, 10, 10);
                                row2.addView(itemNameTextView);

                                quantityTextView = new TextView(AddLocation.this);
                                quantityTextView.setText(quantity);
                                quantityTextView.setPadding(10, 10, 10, 10);
                                row2.addView(quantityTextView);

                                itemRemarkEditText = new EditText(AddLocation.this);
                                itemRemarkEditText.setTextSize(14);
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
                                        if(!itemsRemark[i].isEmpty()) {
                                            int itemId = itemArrayList.get(i).getId();
                                            String itemIdString = String.valueOf(itemId);
                                            Log.d("dsff", itemsRemark[i] + "   " + itemId);
                                            paramsItemRemark.put(itemIdString, itemsRemark[i]);
                                        }
                                        else{
                                            int itemId = itemArrayList.get(i).getId();
                                            String itemIdString = String.valueOf(itemId);
                                            Log.d("dsff", itemsRemark[i] + "   " + itemId);
                                            paramsItemRemark.put(itemIdString, "nil");
                                        }

                                    }

                                    if(categorySpinner.getSelectedItem() == "Select Category")
                                    {
                                        Toast.makeText(getApplicationContext(), "Please Select Project", Toast.LENGTH_LONG).show();

                                    }
                                    else {
                                        sendLocationData();
                                    }


                                }
                            });

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

   private void sendLocationData(){

       int pos = categorySpinner.getSelectedItemPosition();

       categoryId = categoryIdArray[pos];

       paramsItemRemark.put("category_id", categoryId);
       paramsItemRemark.put("project_id", projectId);
       paramsItemRemark.put("location_name", locationNameEditText.getText().toString().trim());
       paramsItemRemark.put("address", addressEditText.getText().toString().trim());
       paramsItemRemark.put("remark", remarkEditText.getText().toString());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_INSERTLOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(AddLocation.this, Projects.class);
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
