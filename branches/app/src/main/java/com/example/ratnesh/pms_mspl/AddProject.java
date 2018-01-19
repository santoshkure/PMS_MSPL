package com.example.ratnesh.pms_mspl;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddProject extends AppCompatActivity {

    private String partyId, projectName, projectType, clientName, contactNo, description, address, contactPerson, emailId ;
    private EditText projectNameEditText, clientNameEditText, contactPersonEditText,
            contactNoEditText, descriptionEditText, addressEditText, emailIdEditText;
    User user;
    private Spinner projectTypeSpinner;
    private ConstraintLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = SharedPrefManager.getInstance(this).getUser();
        partyId = user.getPartyId();

        projectTypeSpinner = (Spinner)findViewById(R.id.project_type);

        projectNameEditText = (EditText)findViewById(R.id.project_name);
        clientNameEditText = (EditText)findViewById(R.id.client_name);
        contactNoEditText = (EditText)findViewById(R.id.contact_no);
        descriptionEditText = (EditText)findViewById(R.id.description);
        addressEditText = (EditText)findViewById(R.id.address);
        contactPersonEditText = (EditText)findViewById(R.id.contact_person);
        emailIdEditText = (EditText)findViewById(R.id.email_id);

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProject();
            }
        });
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(AddProject.this, Projects.class);
        startActivity(intent);
    }


    private void addProject() {
        //first getting the values
        projectName = projectNameEditText.getText().toString();
        clientName = clientNameEditText.getText().toString();
        contactNo = contactNoEditText.getText().toString();
        description = descriptionEditText.getText().toString();
        address = addressEditText.getText().toString();
        contactPerson = contactPersonEditText.getText().toString();
        emailId = emailIdEditText.getText().toString();

        projectType = projectTypeSpinner.getSelectedItem().toString();


        //validating inputs
        if (TextUtils.isEmpty(projectName)) {
            projectNameEditText.setError("Please enter project name");
            projectNameEditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(clientName)) {
            clientNameEditText.setError("Please enter your username");
            clientNameEditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(clientName)) {
            contactPersonEditText.setError("Please enter your username");
            contactPersonEditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contactNo)) {
            contactNoEditText.setError("Please enter your username");
            contactNoEditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(emailId)) {
            emailIdEditText.setError("Please enter your username");
            emailIdEditText.requestFocus();
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADDPROJET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();

                                startActivity(new Intent(getApplicationContext(), Projects.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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
                params.put("project_name", projectName);
                params.put("project_type", projectType);
                params.put("client_name", clientName);
                params.put("contact_no", contactNo);
                params.put("description", description);
                params.put("address", address);
                params.put("contact_person", contactPerson);
                params.put("email_id", emailId);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
