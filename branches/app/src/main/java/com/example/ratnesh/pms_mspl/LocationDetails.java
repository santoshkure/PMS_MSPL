package com.example.ratnesh.pms_mspl;

/**
 * Created by Ratnesh on 13/01/2018.
 */

public class LocationDetails {
    String location_id;
    String category_id;
    String project_id;
    String category;
    String progress_status;

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProgress_status() {
        return progress_status;
    }

    public void setProgress_status(String progress_status) {
        this.progress_status = progress_status;
    }
}
