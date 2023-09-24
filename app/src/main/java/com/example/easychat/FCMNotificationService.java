package com.example.easychat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // This method is called when a new FCM message is received.

        // You can extract the notification payload and handle it here.
        if (remoteMessage.getData().size() > 0) {
            // Handle the data payload here.
            // For example, you can extract the title and body of the notification.
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");

            // You can display the notification or perform custom actions here.
            // You can use NotificationCompat.Builder to build and display a notification.

            // Example:
             sendNotification(title, body);
        }

        // You can also check if the message contains a notification payload (message sent from Firebase Console).
        if (remoteMessage.getNotification() != null) {
            // Handle the notification payload here.
            // For example, you can access the notification title and body as follows:
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Display the notification or perform custom actions here.
            // Example:
            sendNotification(title, body);
        }
    }


    private void sendNotification(String title, String messageBody) {
        // Use NotificationCompat.Builder to build a notification.
        // Configure the notification's title, text, icon, and other properties.
        // Create an Intent to open a specific activity when the notification is tapped.

        // Example:
//         Intent intent = new Intent(this, Context.class);
//         PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//         //Create and show the notification.
//         NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                 .setSmallIcon(R.drawable.icon_back)
//                 .setContentTitle(title)
//                 .setContentText(messageBody)
//                 .setAutoCancel(true)
//                 .setContentIntent(pendingIntent);
//
//         NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//         notificationManager.notify(notificationId, notificationBuilder.build());
    }

}
