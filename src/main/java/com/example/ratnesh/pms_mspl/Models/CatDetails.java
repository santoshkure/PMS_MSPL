package com.example.ratnesh.pms_mspl.Models;

import java.util.ArrayList;

/**
 * Created by mspl-01 on 12-Feb-18.
 */

public class CatDetails {
    String CatName;
    ArrayList<LocationDetail> locationDetail;

    public String getCatName() {
        return CatName;
    }

    public void setCatName(String catName) {
        CatName = catName;
    }

    public ArrayList<LocationDetail> getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(ArrayList<LocationDetail> locationDetail) {
        this.locationDetail = locationDetail;
    }
}