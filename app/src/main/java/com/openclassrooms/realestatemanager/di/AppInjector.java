package com.openclassrooms.realestatemanager.di;

import android.app.Application;

/**
 * AppInjector is responsible for initializing dependency injection components.
 * It ensures that the ViewModelFactory instance is created at the application startup.
 */
public class AppInjector {

    /**
     * Initializes the dependency injection system.
     * This method should be called once at the application startup.
     *
     * @param application The Application instance used to initialize ViewModelFactory.
     */
    public static void init(Application application) {
        // Ensures the ViewModelFactory is instantiated with the application context
        ViewModelFactory.getInstance(application);
    }
}