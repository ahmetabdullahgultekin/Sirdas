package com.gultekinahmetabdullah.sirdas.database

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.gultekinahmetabdullah.sirdas.MainActivity
import com.gultekinahmetabdullah.sirdas.R


class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle the message received from FCM
        try {
            remoteMessage.notification?.let {
                sendNotification(it.title, it.body)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
        }

    }

    private fun sendNotification(title: String?, message: String?) {
        val channelId = "default_channel_id"
        val channelName = "Default Channel"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create an intent for opening the app when the notification is tapped
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create the notification sound
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.sirdas)
            .setContentTitle(title)
            .setShowWhen(true)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent).build()

        // Create the notification channel if needed (for Android Oreo and above)
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(channel)

        // Show the notification
        notificationManager.notify(0, notificationBuilder)
        /*val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(0, notificationBuilder.build())*/
    }

    override fun onNewToken(token: String) {
        // Handle the token update
        // Send the token to your server or save it locally
    }


    /*
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            // Assuming the message contains a "title" and "body" in the data payload
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]

            // Display a notification
            showNotification(title, body)
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "DailyNotifications",
                "Daily Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, "DailyNotifications")
            .setSmallIcon(R.drawable.sirdas)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("fcmTokens")
            .whereEqualTo("token", token)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // If the query finds a document with the same token, it means the token already exists in the database
                    if (task.result?.isEmpty == true) {
                        // The token does not exist in the database, so we can add it
                        val data = hashMapOf(
                            "token" to token
                        )

                        db.collection("fcmTokens")
                            .add(data)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot added with ID: ${documentReference.id}"
                                )
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                    } else {
                        Log.d(TAG, "Token already exists in the database")
                    }
                } else {
                    Log.w(TAG, "Error checking for token", task.exception)
                }
            }
    }


    companion object {
        fun getMessageToken(context: Context) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                val msg = context.getString(R.string.msg_token_fmt, token)
                Log.d(TAG, msg)
                sendRegistrationToServer(token)
            })
        }


        private fun sendRegistrationToServer(token: String) {
            val db = FirebaseFirestore.getInstance()

            db.collection("fcmTokens")
                .whereEqualTo("token", token)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // If the query finds a document with the same token, it means the token already exists in the database
                        if (task.result?.isEmpty == true) {
                            // The token does not exist in the database, so we can add it
                            val data = hashMapOf(
                                "token" to token
                            )

                            db.collection("fcmTokens")
                                .add(data)
                                .addOnSuccessListener { documentReference ->
                                    Log.d(
                                        TAG,
                                        "DocumentSnapshot added with ID: ${documentReference.id}"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }
                        } else {
                            Log.d(TAG, "Token already exists in the database")
                        }
                    } else {
                        Log.w(TAG, "Error checking for token", task.exception)
                    }
                }
        }
    }*/
}