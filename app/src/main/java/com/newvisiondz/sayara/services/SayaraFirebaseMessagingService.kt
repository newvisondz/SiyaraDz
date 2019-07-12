package com.newvisiondz.sayara.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.newvisiondz.sayara.R
import androidx.core.app.NotificationCompat
import com.newvisiondz.sayara.views.fragments.Brands

class SayaraFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        remoteMessage?.data?.isNotEmpty()?.let {
            Log.d(Brands.TAG, "Message data payload: " + remoteMessage.data)

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob()
            } else {
                // Handle message within 10 seconds
//                handleNow()
            }
        }
        remoteMessage?.notification?.let {
            Log.d(Brands.TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.body.toString())
        }
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        //Here's is where we send the new token to back end
    }

    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, SayaraFirebaseMessagingService::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(getString(R.string.msg_token_fmt, messageBody))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

}

