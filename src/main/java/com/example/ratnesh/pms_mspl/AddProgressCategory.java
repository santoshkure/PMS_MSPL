package com.example.ratnesh.pms_mspl;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
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
import com.nileshp.multiphotopicker.photopicker.activity.PickImageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddProgressCategory extends AppCompatActivity implements View.OnClickListener {

    private String progress_category_id, projectId, locationId, progressCategoryName, projectName, locationName;
    private TextView messageText, projectNameTextView, locationNameTextView, categoryNameTextView;
    private Button uploadButton, btnselectpic;
    private EditText etxtUpload, progressDateEditText, remarkEditText;
    private TextInputLayout progressDateTextInput, remarkTextInput, statusInput;
    private ImageView imageview;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;
    private JSONArray dateJsonArray;
    private String siteId, siteName, progressDate, progressStatus, progressRemark;
    int i = 0;
    private RadioGroup statusRadioGroup;
    private RadioButton statusRadioButton, yesRadioButton, noRadioButton, partiallyRadioButton;
    private ArrayList<String> pathList;
    private User user;
    private AlertDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_progress_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        pathList = new ArrayList<>();

        Intent getIntent = getIntent();
        progress_category_id = getIntent.getStringExtra("progress_category_id");
        projectId = getIntent.getStringExtra("project_id");
        locationId = getIntent.getStringExtra("location_id");
        progressCategoryName = getIntent.getStringExtra("progress_category_name");
        projectName = getIntent.getStringExtra("project_name");
        locationName = getIntent.getStringExtra("location_name");

        projectNameTextView = (TextView) findViewById(R.id.project_name);
        locationNameTextView = (TextView) findViewById(R.id.location_name);
        categoryNameTextView = (TextView) findViewById(R.id.category_name);
        projectNameTextView.setText(projectName);
        locationNameTextView.setText(locationName);
        categoryNameTextView.setText(progressCategoryName);

        statusRadioGroup = (RadioGroup) findViewById(R.id.status);
        yesRadioButton = (RadioButton) findViewById(R.id.yes_radio_button);
        noRadioButton = (RadioButton) findViewById(R.id.no_radio_button);
        partiallyRadioButton = (RadioButton) findViewById(R.id.partially_radio_button);

        progressDateEditText = (EditText) findViewById(R.id.progress_date);
        progressDateTextInput = findViewById(R.id.progress_date_input);
        remarkEditText = (EditText) findViewById(R.id.remark);
        remarkTextInput = findViewById(R.id.remark_input);
        statusInput = findViewById(R.id.status_input);
        uploadButton = (Button) findViewById(R.id.submit);
        btnselectpic = (Button) findViewById(R.id.button_selectpic);
        messageText = (TextView) findViewById(R.id.messageText);
        imageview = (ImageView) findViewById(R.id.imageView_pic);
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
                final Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddProgressCategory.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        mcurrentDate.set(Calendar.YEAR, selectedyear);
                        mcurrentDate.set(Calendar.MONTH, selectedmonth);
                        mcurrentDate.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        progressDateEditText.setText(sdf.format(mcurrentDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        setProgressCategoryDetails();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(AddProgressCategory.this, ProgressStatus.class);
        startActivity(intent);
        finish();
    }

    private void setProgressCategoryDetails() {
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

                                progressDate = actor.getString("progress_date");
                                progressStatus = actor.getString("progress_status");

                                if (progressStatus.equals("Yes")) {
                                    yesRadioButton.setChecked(true);
                                    uploadButton.setVisibility(View.GONE);
                                    btnselectpic.setVisibility(View.GONE);
                                    noRadioButton.setEnabled(false);
                                    partiallyRadioButton.setEnabled(false);
                                } else if (progressStatus.equals("No")) {
                                    noRadioButton.setChecked(true);
                                } else if (progressStatus.equals("Partially")) {
                                    partiallyRadioButton.setChecked(true);
                                }
                                progressRemark = actor.getString("progress_remark");
                                String imagePath = actor.getString("image_path");
                                if (!imagePath.equals("null")) {
                                    pathList = new ArrayList<String>();
                                    if (imagePath.contains(",")) {
                                        String[] str = imagePath.split(",");
                                        for (int j = 0; j < str.length; j++) {
                                            pathList.add(str[j]);
                                        }
                                    } else {
                                        pathList.add(imagePath);
                                    }
                                }

                                if (pathList != null && !pathList.isEmpty()) {
                                    SetToGallary();
                                }
                                uploadButton.setText("Update");

                                progressDateEditText.setText(progressDate);
                                remarkEditText.setText(progressRemark);
                            }
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

        switch (v.getId()) {
            case R.id.button_selectpic:
                CreateDialog();

//                Intent mIntent = new Intent(getApplicationContext(), PickImageActivity.class);
//                mIntent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 60);
//                mIntent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 1);
//                startActivityForResult(mIntent, PickImageActivity.PICKER_REQUEST_CODE);

                break;
            case R.id.submit:
                statusRadioButton = (RadioButton) findViewById(statusRadioGroup.getCheckedRadioButtonId());
                if (Validate()) {
                    try {
                        ArrayList<String> str = new ArrayList<String>();
                        JSONArray jsonArray = new JSONArray();

                        dialog.show();
                        for (int i = 0; i < pathList.size(); i++) {
                            try {
                                Bitmap myBitmap;
                                if (pathList.get(i).contains("http")) {
                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);

                                    URL url = new URL(pathList.get(i));
                                    myBitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
                                } else if (pathList.get(i).contains("CameraDemo")) {
                                    Uri file = Uri.parse(pathList.get(i));
                                    InputStream is = getContentResolver().openInputStream(file);
                                    Bitmap rotatedImg = BitmapFactory.decodeStream(is);

                                    Matrix matrix = new Matrix();
                                    matrix.postRotate(90);
                                    myBitmap = Bitmap.createBitmap(rotatedImg, 0, 0, rotatedImg.getWidth(), rotatedImg.getHeight(), matrix, true);
                                } else {
                                    File imgFile = new File(pathList.get(i));
                                    myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                }
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                String a = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                                jsonArray.put(a);
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }

                        jsonObject.put("project_id", projectId);
                        jsonObject.put("location_id", locationId);
                        jsonObject.put("progress_category_id", progress_category_id);
                        jsonObject.put("progress_status", statusRadioButton.getText());
                        jsonObject.put("progress_date", progressDateEditText.getText().toString());
                        jsonObject.put("remark", remarkEditText.getText().toString());
                        jsonObject.put("registered_by", user.getUserId());
                        jsonObject.put("image", jsonArray);
                        jsonObject.put("request_type", uploadButton.getText().toString());
                    } catch (JSONException e) {
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
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    Volley.newRequestQueue(this).add(jsonObjectRequest);
                }
                break;
        }
    }

    private boolean Validate() {
        if(!TextUtils.isEmpty(progressDateTextInput.getError())) {
            progressDateTextInput.setErrorEnabled(false);
        }
        if(!TextUtils.isEmpty(statusInput.getError())) {
            statusInput.setErrorEnabled(false);
        }
        if(!TextUtils.isEmpty(remarkTextInput.getError())) {
            remarkTextInput.setErrorEnabled(false);
        }

        boolean reply = true;

        //validating inputs
        if (TextUtils.isEmpty(progressDateEditText.getText().toString()) ||
                progressDateEditText.getText().toString().equals("null")) {
            progressDateTextInput.setError("Please select Date");
            progressDateTextInput.requestFocus();
            reply = false;
        } else if (statusRadioButton == null) {
            statusInput.setError("Please select status");
            statusInput.requestFocus();
            reply = false;
        } else if (TextUtils.isEmpty(remarkEditText.getText().toString()) ||
                remarkEditText.getText().toString().equals("null")) {
            remarkTextInput.setError("Please enter remark");
            remarkTextInput.requestFocus();
            reply = false;
        } else if (pathList.size() == 0) {
            Toast.makeText(this, "Atleast one image is require", Toast.LENGTH_SHORT).show();
            reply = false;
        }

        return reply;
    }

    private void CreateDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.attachment_options, null);
        final TextView Gallery = alertLayout.findViewById(R.id.gallery_attach);
        final TextView Capture = alertLayout.findViewById(R.id.capture);

        AlertDialog.Builder alert = new AlertDialog.Builder(AddProgressCategory.this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog1 = alert.create();
        dialog1.show();
        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
                dialog1.dismiss();
            }
        });

        Capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddProgressCategory.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                } else {
                    OpenCamera();
                }

                dialog1.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                OpenCamera();
            }
        }
    }

    private void OpenGallery() {
                Intent mIntent = new Intent(getApplicationContext(), PickImageActivity.class);
                mIntent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 60);
                mIntent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 1);
                startActivityForResult(mIntent, PickImageActivity.PICKER_REQUEST_CODE);
    }

    Uri file;
    private void OpenCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = FileProvider.getUriForFile(AddProgressCategory.this,
                    BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile());

            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
            startActivityForResult(intent, 100);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == PickImageActivity.PICKER_REQUEST_CODE) {
            if (pathList == null) {
                this.pathList = data.getExtras().getStringArrayList(PickImageActivity.KEY_DATA_RESULT);
            } else {
                ArrayList<String> str = data.getExtras().getStringArrayList(PickImageActivity.KEY_DATA_RESULT);
                for (int i = 0; i < str.size(); i++) {
                    this.pathList.add(str.get(i));
                }
            }
        } else if (resultCode == -1 && requestCode == 100) {
            try {
                this.pathList.add(file.toString());
            } catch (Exception e) {
                e.getMessage();
            }
        }

        if (this.pathList != null && !this.pathList.isEmpty()) {
            SetToGallary();
        }
    }

    public void SetToGallary() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Gallery gallery = (Gallery) findViewById(R.id.gallery);
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gallery.getLayoutParams();
        mlp.setMargins((int) -(metrics.widthPixels / 1.7), mlp.topMargin, mlp.rightMargin, mlp.bottomMargin);

        gallery.setVisibility(View.VISIBLE);
        gallery.setSpacing(10);
        final GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(this, pathList, "ProgressCat");
        gallery.setAdapter(galleryImageAdapter);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ZoomActivity.class);
                i.putExtra("image", pathList.get(position));
                startActivity(i);
            }
        });
    }

}
