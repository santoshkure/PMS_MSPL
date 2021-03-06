package com.example.ratnesh.pms_mspl.Models;

/**
 * Created by mspl-01 on 1/23/2018.
 */

public class ProjectDetailModel {
    String projectCategory;
    String progressDate;
    String progressRemark;
    String progressStatus;
    String imagePath;
    String locationName;
    String register_by;

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }

    public String getProgressDate() {
        return progressDate;
    }

    public void setProgressDate(String progressDate) {
        this.progressDate = progressDate;
    }

    public String getProgressRemark() {
        return progressRemark;
    }

    public void setProgressRemark(String progressRemark) {
        this.progressRemark = progressRemark;
    }

    public String getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(String progressStatus) {
        this.progressStatus = progressStatus;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getRegister_by() {
        return register_by;
    }

    public void setRegister_by(String register_by) {
        this.register_by = register_by;
    }
}
