package com.example.pets.handler;

import android.app.Activity;
import android.content.Context;

import com.example.pets.Classes.User;

public class PasswordUpdateHandler {

    Context context;
    Activity activity;

    User user;

    public PasswordUpdateHandler(Context context, Activity activity, User user) {
        this.context = context;
        this.activity = activity;
        this.user = user;
    }

    void setUpDialog(){

    }

}
