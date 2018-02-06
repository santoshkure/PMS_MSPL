package com.example.ratnesh.pms_mspl.Models;

import java.util.ArrayList;

/**
 * Created by mspl-01 on 1/29/2018.
 */

public class TempModel {
    String LocName;
    String Proj_cat;
    int Count;
    int Completed;
    int Pending;
    ArrayList<ProjectDetailModel> Details;

    public String getLocName() {
        return LocName;
    }

    public void setLocName(String locName) {
        LocName = locName;
    }

    public String getProj_cat() {
        return Proj_cat;
    }

    public void setProj_cat(String proj_cat) {
        Proj_cat = proj_cat;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getCompleted() {
        return Completed;
    }

    public void setCompleted(int completed) {
        Completed = completed;
    }

    public int getPending() {
        return Pending;
    }

    public void setPending(int pending) {
        Pending = pending;
    }

    public ArrayList<ProjectDetailModel> getDetails() {
        return Details;
    }

    public void setDetails(ArrayList<ProjectDetailModel> details) {
        Details = details;
    }
}
