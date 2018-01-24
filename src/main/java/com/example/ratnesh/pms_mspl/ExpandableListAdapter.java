package com.example.ratnesh.pms_mspl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ratnesh.pms_mspl.Models.ProjectDetailModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mspl-01 on 1/23/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<String, List<ProjectDetailModel>> _listDataChild;
    private ArrayList<String> pathList;
    private LinearLayout linLay;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<ProjectDetailModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ProjectDetailModel childText = (ProjectDetailModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView proj_progress = (TextView) convertView
                .findViewById(R.id.project_progress);
        TextView date = (TextView) convertView
                .findViewById(R.id.date);
        TextView status = (TextView) convertView
                .findViewById(R.id.status);
        TextView remark = (TextView) convertView
                .findViewById(R.id.remark);
        linLay = convertView.findViewById(R.id.linLay);

        proj_progress.setText(childText.getProjectCategory());
        date.setText(childText.getProgressDate());
        status.setText(childText.getProgressStatus());
        remark.setText(childText.getProgressRemark());
        String imgPath = childText.getImagePath();

        if (!imgPath.equals("null") && !imgPath.isEmpty()) {
            pathList = new ArrayList<>();
            if (imgPath.contains(",")) {
                String[] str = imgPath.split(",");
                for (int i=0; i<str.length; i++) {
                    pathList.add(str[i]);
                }
            } else {
                pathList.add(imgPath);
            }
            SetToGallary(convertView, pathList);
        } else {
            linLay.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void SetToGallary(View convertView, final ArrayList<String> path_List) {
        Gallery gallery = (Gallery) convertView.findViewById(R.id.gallery);
        linLay.setVisibility(View.VISIBLE);
        gallery.setSpacing(10);
        final GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(_context, pathList, "Expandable");
        gallery.setAdapter(galleryImageAdapter);
        gallery.setSelection(1);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(_context, ZoomActivity.class);
                i.putExtra("image", path_List.get(position));
                _context.startActivity(i);
            }
        });
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}