package com.openclassrooms.realestatemanager.di;

import android.app.Application;

public class AppInjector {
    public static void init(Application application) {
        ViewModelFactory.getInstance(application);
    }
}