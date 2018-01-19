package com.example.ratnesh.pms_mspl;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddProgressCategory extends AppCompatActivity implements View.OnClickListener{

    private String progress_category_id, projectId, locationId, progressCategoryName, projectName, locationName;
    private TextView messageText, projectNameTextView, locationNameTextView, categoryNameTextView;
    private Button uploadButton, btnselectpic;
    private EditText etxtUpload, progressDateEditText, remarkEditText;
    private ImageView imageview;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;
    private JSONArray dateJsonArray;
    private String siteId, siteName, progressDate, progressStatus, progressRemark;
    int i = 0;
    private RadioGroup statusRadioGroup;
    private RadioButton statusRadioButton, yesRadioButton, noRadioButton, partiallyRadioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_progress_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent getIntent = getIntent();
        progress_category_id = getIntent.getStringExtra("progress_category_id");
        projectId = getIntent.getStringExtra("project_id");
        locationId = getIntent.getStringExtra("location_id");
        progressCategoryName = getIntent.getStringExtra("progress_category_name");
        projectName = getIntent.getStringExtra("project_name");
        locationName = getIntent.getStringExtra("location_name");

        projectNameTextView = (TextView)findViewById(R.id.project_name);
        locationNameTextView = (TextView)findViewById(R.id.location_name);
        categoryNameTextView = (TextView)findViewById(R.id.category_name);
        projectNameTextView.setText(projectName);
        locationNameTextView.setText(locationName);
        categoryNameTextView.setText(progressCategoryName);

        statusRadioGroup = (RadioGroup) findViewById(R.id.status);
        yesRadioButton = (RadioButton) findViewById(R.id.yes_radio_button);
        noRadioButton = (RadioButton) findViewById(R.id.no_radio_button);
        partiallyRadioButton = (RadioButton) findViewById(R.id.partially_radio_button);

        progressDateEditText = (EditText)findViewById(R.id.progress_date);
        remarkEditText = (EditText)findViewById(R.id.remark);
        uploadButton = (Button)findViewById(R.id.submit);
        btnselectpic = (Button)findViewById(R.id.button_selectpic);
        messageText  = (TextView)findViewById(R.id.messageText);
        imageview = (ImageView)findViewById(R.id.imageView_pic);
        //etxtUpload = (EditText)findViewById(R.id.etxtUpload);


        btnselectpic.setOnClickListener(this);
        uploadButton.setOnClickListener(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);

        jsonObject = new JSONObject();

        progressDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                final Calendar mcurrentDate=Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth=mcurrentDate.get(Calendar.MONTH);
                int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(AddProgressCategory.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        mcurrentDate.set(Calendar.YEAR, selectedyear);
                        mcurrentDate.set(Calendar.MONTH, selectedmonth);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        progressDateEditText.setText(sdf.format(mcurrentDate.getTime()));
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });

        setProgressCategoryDetails();
    }

    public void onBackPressed() {
        Intent intent = new Intent(AddProgressCategory.this, ProgressStatus.class);
        startActivity(intent);
        finish();
    }

    private void setProgressCategoryDetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADDPROGRESSCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject obj = new JSONObject(response);
                            String exits = obj.getString("exits");
                            JSONArray progressCategoryDetails = obj.getJSONArray("progress_cate_list");

                            for (int i = 0; i < progressCategoryDetails.length(); i++) {
                                JSONObject actor = progressCategoryDetails.getJSONObject(i);

                                if (exits.equals("false")) {
                                    progressDate = "";
                                    progressStatus = "";
                                    progressRemark = "";
                                }
                                else {
                                    progressDate = actor.getString("progress_date");
                                    progressStatus = actor.getString("progress_status");

                                    if(progressStatus.equals("Yes")){
                                        yesRadioButton.setChecked(true);
                                    }
                                    else if(progressStatus.equals("No")){
                                        noRadioButton.setChecked(true);
                                    }
                                    else{
                                        partiallyRadioButton.setChecked(true);
                                    }
                                    progressRemark = actor.getString("progress_remark");
                                    String imagePath = actor.getString("image_path");
                                    uploadButton.setText("Update");
                                    if(!imagePath.equals("null")) {
                                        imageview.setVisibility(View.VISIBLE);
                                        new DownLoadImageTask(imageview).execute(imagePath);
                                        btnselectpic.setVisibility(View.GONE);
                                    }
                                }
                                progressDateEditText.setText(progressDate);
                                remarkEditText.setText(progressRemark);

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
                params.put("progress_category_id", progress_category_id);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_selectpic:
                imageview.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, URLs.REQCODE);
                break;
            case R.id.submit:
                Toast.makeText(getApplicationContext(), "fsf", Toast.LENGTH_LONG).show();

                statusRadioButton = (RadioButton) findViewById(statusRadioGroup.getCheckedRadioButtonId());

                Bitmap image = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
                dialog.show();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                try {
                    jsonObject.put("project_id", projectId);
                    jsonObject.put("location_id", locationId);
                    jsonObject.put("progress_category_id", progress_category_id);
                    jsonObject.put("progress_status", statusRadioButton.getText());
                    jsonObject.put("progress_date", progressDateEditText.getText().toString());
                    jsonObject.put("remark", remarkEditText.getText().toString());
                    //jsonObject.put("image_name", etxtUpload.getText().toString().trim());
                    jsonObject.put("image", encodedImage);

                }
                catch (JSONException e) {
                    Log.e("JSONObject Here", e.toString());
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLs.URL_INSERTPROGRESSCATEGORY, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.e("Message from server", jsonObject.toString());
                                dialog.dismiss();
                                messageText.setText("Image Uploaded Successfully");
                                Toast.makeText(getApplication(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddProgressCategory.this, ProgressStatus.class);
                                startActivity(intent);
                                finish();

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

    public static class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;
        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
