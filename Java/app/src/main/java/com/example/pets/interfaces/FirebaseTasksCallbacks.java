package com.example.pets.interfaces;

import android.net.Uri;

public interface FirebaseTasksCallbacks {
    void onSuccess(Uri result);
    void onFailure(Exception e);
}
