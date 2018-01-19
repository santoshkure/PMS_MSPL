package com.example.ratnesh.pms_mspl;

/**
 * Created by Belal on 9/5/2017.
 */


//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
public class User {

    private String user_login_id, party_id, user_id;

    public User(String user_login_id, String user_id, String party_id) {
        this.user_login_id = user_login_id;
        this.user_id = user_id;
        this.party_id = party_id;
    }

    public String getUserLoginId() {
        return user_login_id;
    }

    public String getUserId() {
        return user_id;
    }

    public String getPartyId() {
        return party_id;
    }
}
