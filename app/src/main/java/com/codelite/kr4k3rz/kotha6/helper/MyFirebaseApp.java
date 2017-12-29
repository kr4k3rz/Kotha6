package com.codelite.kr4k3rz.kotha6.helper;

import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;


public class MyFirebaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        Paper.init(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}