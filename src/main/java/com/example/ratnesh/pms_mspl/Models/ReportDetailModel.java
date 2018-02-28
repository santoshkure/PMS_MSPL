package com.example.ratnesh.pms_mspl.Models;

import java.util.ArrayList;

/**
 * Created by mspl-01 on 1/23/2018.
 */

public class ReportDetailModel {
    String LocationName;
    ArrayList<ProjectDetailModel> ProjectDetail;

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public ArrayList<ProjectDetailModel> getProjectDetail() {
        return ProjectDetail;
    }

    public void setProjectDetail(ArrayList<ProjectDetailModel> projectDetail) {
        ProjectDetail = projectDetail;
    }
}
