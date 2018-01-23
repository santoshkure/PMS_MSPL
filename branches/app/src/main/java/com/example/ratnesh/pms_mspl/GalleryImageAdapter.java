package com.example.ratnesh.pms_mspl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mspl-01 on 1/22/2018.
 */
public class GalleryImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> ImgPaths;


    public GalleryImageAdapter(Context context, ArrayList<String> ImgPaths) {
        mContext = context;
        this.ImgPaths = ImgPaths;
    }

    public int getCount() {
        return ImgPaths.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext);

        try {
            if (ImgPaths.get(index).contains("http")) {
                Picasso.with(mContext)
                        .load(ImgPaths.get(index))
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(i);
            } else {
                File imgFile = new  File(ImgPaths.get(index));

                if(imgFile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    i.setImageBitmap(myBitmap);
                }
            }
            i.setLayoutParams(new Gallery.LayoutParams(400, 400));
            i.setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (Exception e) {
            e.getMessage();
        }

        return i;
    }

}