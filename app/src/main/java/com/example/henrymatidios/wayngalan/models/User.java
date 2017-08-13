package com.example.henrymatidios.wayngalan.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author Henry Matidios
 * @since 26/07/2017.
 */

@IgnoreExtraProperties
public class User {
    public String email;
    public String type;

    public User() {}

    public User(String email, String type) {
        this.email = email;
        this.type = type;
    }
}
