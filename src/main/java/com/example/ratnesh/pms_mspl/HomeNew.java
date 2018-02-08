package com.example.ratnesh.pms_mspl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeNew extends Fragment {
    View rootView;

    LinearLayout project_layout, progress_layout, report_layout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_home_new, container, false);

        project_layout = (LinearLayout) rootView.findViewById(R.id.projects_item);
        progress_layout = (LinearLayout) rootView.findViewById(R.id.progress_item);
        report_layout = (LinearLayout) rootView.findViewById(R.id.reports_item);

        project_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent project_details_intent = new Intent(getActivity().getApplicationContext(), Projects.class);
                startActivity(project_details_intent);
            }
        });
        project_layout.setVisibility(View.GONE);

        progress_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent progress_status_intent = new Intent(getActivity().getApplicationContext(), ProgressStatus.class);
                startActivity(progress_status_intent);
            }
        });

        report_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reports_intent = new Intent(getActivity().getApplicationContext(), Reports.class);
                startActivity(reports_intent);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("PMS MSPL");
    }

}
