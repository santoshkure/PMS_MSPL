package com.example.ratnesh.pms_mspl;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ZoomActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private SubsamplingScaleImageView imageView;
    private ImageView imgView;
    private PhotoViewAttacher pAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        DeclareVariable();

        String str = getIntent().getStringExtra("image");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (str.contains("http")) {
            imgView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            Picasso.with(getApplicationContext())
                    .load(str)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imgView);
            pAttacher = new PhotoViewAttacher(imgView);
            pAttacher.update();
        } else {
            imageView.setVisibility(View.VISIBLE);
            imgView.setVisibility(View.GONE);
            imageView.setImage(ImageSource.uri(str));
        }
    }

    public void DeclareVariable() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        imageView = findViewById(R.id.imageView);
        imgView = findViewById(R.id.imgView);
    }
}
