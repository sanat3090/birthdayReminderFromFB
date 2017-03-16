package com.sanat.birthdayreminder.firendList;

import java.util.ArrayList;

/**
 * Created by healthcaremagic on 3/3/2017.
 */

public class FriendModel {
    public static ArrayList<FriendModel> ITEMS = new ArrayList<FriendModel>();
    String name,displayDateOfBirth,imageUrl;
    String id;
    String actualFacebookDate;
    long dateInMill;
    public String getActualFacebookDate() {
        return actualFacebookDate;
    }

    public void setActualFacebookDate(String actualFacebookDate) {
        this.actualFacebookDate = actualFacebookDate;
    }
    public long getDateInMill() {
        return dateInMill;
    }

    public void setDateInMill(long dateInMill) {
        this.dateInMill = dateInMill;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getdisplayDateOfBirth() {
        return displayDateOfBirth;
    }

    public void setdisplayDateOfBirth(String dateofbirth) {
        this.displayDateOfBirth = dateofbirth;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
