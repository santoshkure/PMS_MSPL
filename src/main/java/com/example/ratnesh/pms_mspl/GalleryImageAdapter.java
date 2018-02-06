package com.example.ratnesh.pms_mspl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by mspl-01 on 1/22/2018.
 */
public class GalleryImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> ImgPaths;
    private String Acti;

    public GalleryImageAdapter(Context context, ArrayList<String> ImgPaths, String Acti) {
        mContext = context;
        this.ImgPaths = ImgPaths;
        this.Acti = Acti;
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
                Glide.with(mContext)
                        .load(ImgPaths.get(index))
                        .placeholder(R.drawable.loader)
                        .error(R.mipmap.ic_launcher)
                        .into(i);

            } else {
                if (ImgPaths.get(index).contains("CameraDemo")) {
                    Uri file = Uri.parse(ImgPaths.get(index));
                    Glide.with(mContext)
                            .load(file)
                            .into(i);
//                    InputStream is = mContext.getContentResolver().openInputStream(file);
//                    Bitmap bitmap = BitmapFactory.decodeStream(is);
//                    i.setImageBitmap(bitmap);
                } else {
                    File imgFile = new  File(ImgPaths.get(index));
                    if(imgFile.exists()) {
                        Picasso.with(mContext)
                                .load(imgFile)
                                .placeholder(R.drawable.loader)
                                .error(R.mipmap.ic_launcher)
                                .into(i);
//                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                        i.setImageBitmap(myBitmap);
                    }
                }
            }
            if (Acti.equals("Expandable")) {
                i.setLayoutParams(new Gallery.LayoutParams(200, 100));
            } else {
                i.setLayoutParams(new Gallery.LayoutParams(400, 400));
            }
            i.setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (Exception e) {
            e.getMessage();
        }

        return i;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
}