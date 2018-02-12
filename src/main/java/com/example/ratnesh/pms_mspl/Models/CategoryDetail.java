package com.example.ratnesh.pms_mspl.Models;

/**
 * Created by mspl-01 on 12-Feb-18.
 */

public class CategoryDetail {
    String category;
    String categoryId;
    int loc_count;
    int complete_count;
    int pending_count;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getLoc_count() {
        return loc_count;
    }

    public void setLoc_count(int loc_count) {
        this.loc_count = loc_count;
    }

    public int getComplete_count() {
        return complete_count;
    }

    public void setComplete_count(int complete_count) {
        this.complete_count = complete_count;
    }

    public int getPending_count() {
        return pending_count;
    }

    public void setPending_count(int pending_count) {
        this.pending_count = pending_count;
    }
}
