package com.openclassrooms.realestatemanager.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.openclassrooms.realestatemanager.R;

/**
 * Utility class for managing property-related notifications.
 * This class creates and displays notifications to the user.
 */
public class NotificationHelper {
    private static final String CHANNEL_ID = "property_notifications"; // Notification channel ID

    /**
     * Displays a notification with the given title and message.
     *
     * @param context The application context.
     * @param title   The title of the notification.
     * @param message The message/body of the notification.
     */
    public static void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for Android 8.0+ (Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Property Notifications", // Channel name displayed in settings
                    NotificationManager.IMPORTANCE_HIGH // High importance for priority notifications
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Replace with your notification icon
                .setContentTitle(title) // Set the notification title
                .setContentText(message) // Set the notification content
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Ensure high-priority notifications
                .setAutoCancel(true); // Dismiss notification when clicked

        // Show the notification with a fixed ID (replace 1 with a unique ID if needed)
        notificationManager.notify(1, builder.build());
    }
}