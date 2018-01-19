package com.example.ratnesh.pms_mspl;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddProgress extends AppCompatActivity implements View.OnClickListener{

    private TextView messageText;
    private Button uploadButton, btnselectpic;
    private EditText etxtUpload;
    private ImageView imageview;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;
    private JSONArray dateJsonArray;
    private String projectId, locationId, date, progressStatus, progressRemark;
    TextView viewName, viewValue;
    EditText[] editValue, editValue2, editValue3;
    TableRow row1, row2;
    private List<EditText> progressDateList, progressStatusList, progressRemarkList;
    int indexRow;
    TableLayout displayTableLayout;
    private String[] progressDate, progressId;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent getIntent = getIntent();
        projectId = getIntent.getStringExtra("project_id");
        locationId = getIntent.getStringExtra("location_id");

        displayTableLayout = (TableLayout)findViewById(R.id.display_table_layout);
        uploadButton = (Button)findViewById(R.id.uploadButton);
        btnselectpic = (Button)findViewById(R.id.button_selectpic);
        messageText  = (TextView)findViewById(R.id.messageText);
        imageview = (ImageView)findViewById(R.id.imageView_pic);
        etxtUpload = (EditText)findViewById(R.id.etxtUpload);

        getProgressCateg();

        btnselectpic.setOnClickListener(this);
        uploadButton.setOnClickListener(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);

        jsonObject = new JSONObject();
    }



    private void getProgressCateg(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_PROGRESSCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dsaaa", response);
                        try {
                            final JSONObject obj = new JSONObject(response);
                            String exits = obj.getString("exits");

                            Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                                    Toolbar.LayoutParams.WRAP_CONTENT);

                            progressDateList = new ArrayList<EditText>();
                            progressStatusList = new ArrayList<EditText>();
                            progressRemarkList = new ArrayList<EditText>();

                            JSONArray progressResponse = obj.getJSONArray("progress_cate_list");
                            progressId = new String[progressResponse.length()];

                            row1 = new TableRow(AddProgress.this);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row1.setBackground(getResources().getDrawable(R.drawable.border));
                            row1.setLayoutParams(lp);

                            viewName = new TextView(AddProgress.this);
                            viewName.setText("PROJECT PROGRESS");
                            viewName.setPadding(10, 10, 10, 10);
                            viewName.setTypeface(null, Typeface.BOLD);
                            row1.addView(viewName);

                            viewName = new TextView(AddProgress.this);
                            viewName.setText("PROGRESS DATE");
                            viewName.setPadding(10, 10, 10, 10);
                            viewName.setTypeface(null, Typeface.BOLD);
                            row1.addView(viewName);

                            viewName = new TextView(AddProgress.this);
                            viewName.setText("STATUS");
                            viewName.setPadding(10, 10, 10, 10);
                            viewName.setTypeface(null, Typeface.BOLD);
                            row1.addView(viewName);

                            viewName = new TextView(AddProgress.this);
                            viewName.setText("REMARK");
                            viewName.setPadding(10, 10, 10, 10);
                            viewName.setTypeface(null, Typeface.BOLD);
                            row1.addView(viewName);

                            displayTableLayout.addView(row1, 0);

                            editValue = new EditText[10];
                            editValue2 = new EditText[10];
                            editValue3 = new EditText[10];
                            for (i = 0; i < progressResponse.length(); i++) {
                                JSONObject actor = progressResponse.getJSONObject(i);
                                final String progress_category_id = actor.getString("progress_category_id");

                                progressId[i] = progress_category_id;

                                //final String progress_category_id = actor.getString("progress_category_id");
                                final String progress_category = actor.getString("progress_category");
                                if (exits.equals("false")) {
                                    date = "";
                                    progressStatus = "";
                                    progressRemark = "";
                                }
                                else {
                                    date = actor.getString("progress_date");
                                    progressStatus = actor.getString("progress_status");
                                    progressRemark = actor.getString("progress_remark");
                                }

                                row2 = new TableRow(AddProgress.this);
                                row2.setBackground(getResources().getDrawable(R.drawable.border));
                                row2.setLayoutParams(lp);

                                viewValue = new TextView(AddProgress.this);
                                viewValue.setText(progress_category);
                                viewValue.setPadding(10, 10, 10, 10);
                                row2.addView(viewValue);

                                editValue[i] = new EditText(AddProgress.this);
                                editValue[i].setTextSize(14);
                                progressDateList.add(editValue[i]);
                                editValue[i].setId(i);
                                editValue[i].setText(date);
                                editValue[i].setInputType(InputType.TYPE_CLASS_NUMBER);
                                editValue[i].setPadding(10, 10, 10, 10);
                                editValue[i].setClickable(true);
                                editValue[i].setFocusableInTouchMode(false);
                                editValue[i].setFocusable(false);

                                editValue2[i] = new EditText(AddProgress.this);
                                editValue2[i].setTextSize(14);
                                progressStatusList.add(editValue2[i]);
                                editValue2[i].setId(i);
                                editValue2[i].setText(progressStatus);
                                editValue2[i].setPadding(10, 10, 10, 10);

                                editValue3[i] = new EditText(AddProgress.this);
                                editValue3[i].setTextSize(14);
                                progressRemarkList.add(editValue3[i]);
                                editValue3[i].setId(i);
                                editValue3[i].setText(progressRemark);
                                editValue3[i].setPadding(10, 10, 10, 10);

                                Log.d("ddss", String.valueOf(i));
                                Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();

                                editValue[i].setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        switch (v.getId()) {
                                            case 0:
                                                setDate(0);
                                                break;
                                        }
                                        switch (v.getId()) {
                                            case 1:
                                                setDate(1);
                                                break;
                                        }
                                        switch (v.getId()) {
                                            case 2:
                                                setDate(2);
                                                break;
                                        }
                                        switch (v.getId()) {
                                            case 3:
                                                setDate(3);
                                                break;
                                        }
                                        switch (v.getId()) {
                                            case 4:
                                                setDate(4);
                                                break;
                                        }
                                        switch (v.getId()) {
                                            case 5:
                                                setDate(5);
                                                break;
                                        }
                                        switch (v.getId()) {
                                            case 6:
                                                setDate(6);
                                                break;
                                        }
                                        switch (v.getId()) {
                                            case 7:
                                                setDate(7);
                                                break;
                                        }
                                    }
                                });

                                row2.addView(editValue[i]);
                                row2.addView(editValue2[i]);
                                row2.addView(editValue3[i]);
                                indexRow = i;
                                displayTableLayout.addView(row2, ++indexRow);

                            }



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

    private void setDate(final int id) {
        final Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(AddProgress.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                mcurrentDate.set(Calendar.YEAR, selectedyear);
                mcurrentDate.set(Calendar.MONTH, selectedmonth);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editValue[id].setText(sdf.format(mcurrentDate.getTime()));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_selectpic:
                imageview.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, URLs.REQCODE);
                break;
            case R.id.uploadButton:
                try {
                    jsonObject.put("project_id", projectId);
                    jsonObject.put("location_id", locationId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDate = new String[progressDateList.size()];
                dateJsonArray = new JSONArray();
                for (int j = 0; j < progressDateList.size(); j++) {
                    try {

                        dateJsonArray.put(new JSONObject().put("progress_id", progressId[j])
                                .put("progress_date", progressDateList.get(j).getText().toString())
                                .put("progress_statue",progressStatusList.get(j).getText().toString())
                                .put("progress_remakr",progressRemarkList.get(j).getText().toString()));

                        jsonObject.put("data_array", dateJsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Bitmap image = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                dialog.show();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                try {
                    jsonObject.put("image_name", etxtUpload.getText().toString().trim());
                    jsonObject.put("image", encodedImage);
                    Log.d("dd00", String.valueOf(jsonObject.put("data_array", dateJsonArray)));

                }
                catch (JSONException e) {
                    Log.e("JSONObject Here", e.toString());
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLs.URL_INSERTPROGRESS, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.e("Message from server", jsonObject.toString());
                                dialog.dismiss();
                                messageText.setText("Image Uploaded Successfully");
                                Toast.makeText(getApplication(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddProgress.this, ProgressStatus.class);
                                startActivity(intent);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Message from server", volleyError.toString());
                        dialog.dismiss();
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                Volley.newRequestQueue(this).add(jsonObjectRequest);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == URLs.REQCODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);
        }
    }
}