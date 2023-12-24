package com.example.healthcareapp;

import android.app.Application;

//import com.google.android.libraries.texttospeech.bard.Bard;

import com.google.firebase.FirebaseApp;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        String apiKey = "AIzaSyA4ZFQnloi8m9X0lAGnl22Rmm3g5mDjXO4";
//        Bard.initialize(this, apiKey);
    }
}
