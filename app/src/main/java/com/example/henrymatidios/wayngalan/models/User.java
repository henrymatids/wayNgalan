package com.example.henrymatidios.wayngalan.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author Henry Matidios
 * @since 26/07/2017.
 */

@IgnoreExtraProperties
public class User {
    private String email;
    private String type;

    public User() {}

    public User(String email, String type) {
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}