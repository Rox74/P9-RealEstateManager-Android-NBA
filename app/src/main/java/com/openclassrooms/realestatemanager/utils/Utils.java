package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class providing various helper methods for real estate application.
 * Created by Philippe on 21/02/2018.
 */
public class Utils {

    /**
     * Converts a price from US Dollars to Euros.
     * NOTE: DO NOT REMOVE, MUST BE PRESENTED DURING THE FINAL REVIEW.
     *
     * @param dollars The amount in USD.
     * @return The converted amount in EUR.
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.812); // Approximate conversion rate
    }

    /**
     * Converts a price from Euros to US Dollars.
     * NOTE: NEW METHOD, MUST BE PRESENTED DURING THE FINAL REVIEW.
     *
     * @param euros The amount in EUR.
     * @return The converted amount in USD.
     */
    public static int convertEuroToDollar(int euros) {
        return (int) Math.round(euros * 1.23); // Approximate conversion rate
    }

    /**
     * Gets today's date formatted in "dd/MM/yyyy".
     * NOTE: UPDATED METHOD, MUST BE PRESENTED DURING THE FINAL REVIEW.
     *
     * @return The formatted date as a String.
     */
    public static String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * Checks if the device has an active internet connection.
     * Supports Android 5.0+ (API 21 - Lollipop) and newer versions.
     * NOTE: UPDATED METHOD, MUST BE PRESENTED DURING THE FINAL REVIEW.
     *
     * @param context The application context.
     * @return True if the device is connected to the internet, false otherwise.
     */
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            // API 23+ (Marshmallow and above): Use NetworkCapabilities
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connectivityManager.getActiveNetwork();
                if (network == null) return false; // No active network

                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            } else {
                // API 21-22 (Lollipop): Use deprecated NetworkInfo
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                return activeNetwork != null && activeNetwork.isConnected();
            }
        }
        return false;
    }
}