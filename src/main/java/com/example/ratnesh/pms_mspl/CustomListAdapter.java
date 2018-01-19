package com.example.ratnesh.pms_mspl;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ratnesh on 18/01/2018.
 */

public class CustomListAdapter extends BaseAdapter {

    private Context activity;
    private ArrayList<ReportDetailsModel> data;
    private static LayoutInflater inflater = null;
    public Resources res;
    ReportDetailsModel tempValues = null;
    int i = 0;

    /*************  CustomAdapter Constructor *****************/
    public CustomListAdapter(Context a, ArrayList<ReportDetailsModel> data) {
        /********** Take passed values **********/
        activity = a;
        this.data = data;
    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {
        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder {

        public TextView locationNameTextView, projectProgressTextView, progressDateTextView,
                statusTextView, remarkTextView;
        ReportDetailsModel data;

    }

    ViewHolder holder;
    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            rowView = inflater.inflate(R.layout.report_list_item, null);

            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.locationNameTextView = (TextView) rowView.findViewById(R.id.location_name);
            viewHolder.projectProgressTextView = (TextView) rowView.findViewById(R.id.project_progress);
            viewHolder.progressDateTextView = (TextView) rowView.findViewById(R.id.date);
            viewHolder.statusTextView = (TextView) rowView.findViewById(R.id.status);
            viewHolder.remarkTextView = (TextView) rowView.findViewById(R.id.remark);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        ReportDetailsModel dataModel = data.get(position);

        holder.locationNameTextView.setText(dataModel.getLocationName());
        holder.projectProgressTextView.setText(dataModel.getProjectCategory());
        holder.progressDateTextView.setText(dataModel.getProgressDate());
        holder.statusTextView.setText(dataModel.getProgressStatus());
        holder.remarkTextView.setText(dataModel.getProgressRemark());

        return rowView;

//
//        // Get the data item for this position
//        // Check if an existing view is being reused, otherwise inflate the view
//        ViewHolder viewHolder; // view lookup cache stored in tag
//        final View result;
//
//        if (convertView == null) {
//
//            viewHolder = new ViewHolder();
//
//            holder.locationNameTextView = (TextView) convertView.findViewById(R.id.location_name);
//            holder.projectProgressTextView = (TextView) convertView.findViewById(R.id.project_progress);
//            holder.progressDateTextView = (TextView) convertView.findViewById(R.id.date);
//            holder.statusTextView = (TextView) convertView.findViewById(R.id.status);
//            holder.remarkTextView = (TextView) convertView.findViewById(R.id.remark);
//
//            result=convertView;
//
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//            result=convertView;
//        }
//
////        tempValues = (ReportDetailsModel) data.get(position);
//
//        /************  Set Model values in Holder elements ***********/
//        ReportDetailsModel dataModel = data.get(position);
//        holder.locationNameTextView.setText(dataModel.getLocationName());
//        holder.projectProgressTextView.setText(dataModel.getProjectCategory());
//        holder.progressDateTextView.setText(dataModel.getProgressDate());
//        holder.statusTextView.setText(dataModel.getProgressStatus());
//        holder.remarkTextView.setText(dataModel.getProgressRemark());
//
//
////        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
////        result.startAnimation(animation);
////        lastPosition = position;
////
////        viewHolder.txtName.setText(dataModel.getName());
////        viewHolder.txtType.setText(dataModel.getType());
////        viewHolder.txtVersion.setText(dataModel.getVersion_number());
////        viewHolder.info.setOnClickListener(this);
////        viewHolder.info.setTag(position);
//        // Return the completed view to render on screen
//        return convertView;
//
//
////        View vi = convertView;
////
////        holder = new ViewHolder();
////        if (vi == null) {
////            /***********  Layout inflator to call external xml layout () ***********/
////
////            holder.locationNameTextView = (TextView) vi.findViewById(R.id.location_name);
////            holder.projectProgressTextView = (TextView) vi.findViewById(R.id.project_progress);
////            holder.progressDateTextView = (TextView) vi.findViewById(R.id.date);
////            holder.statusTextView = (TextView) vi.findViewById(R.id.status);
////            holder.remarkTextView = (TextView) vi.findViewById(R.id.remark);
////            vi.setTag(holder);
////        } else
////            holder = (ViewHolder) vi.getTag();
////        if (data.size() <= 0) {
////            holder.locationNameTextView.setText("No Data");
////        } else {
////            /***** Get each Model object from Arraylist ********/
////            tempValues = null;
////        }
////        return vi;
    }

}
