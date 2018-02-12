package com.example.ratnesh.pms_mspl.Models;

/**
 * Created by mspl-01 on 12-Feb-18.
 */

public class LocationDetail {
    String locationId;
    String CategoryId;
    String ProjectId;
    String ProgressStatus;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getProgressStatus() {
        return ProgressStatus;
    }

    public void setProgressStatus(String progressStatus) {
        ProgressStatus = progressStatus;
    }
}
