package com.newvisiondz.sayara.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonObject
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.utils.getUserToken
import com.newvisiondz.sayara.screens.brands.Brands
import retrofit2.Callback
import retrofit2.Response

class SayaraFirebaseMessagingService : FirebaseMessagingService() {

    private var userInfo: SharedPreferences? = applicationContext?.getSharedPreferences("userinfo", Context.MODE_PRIVATE)


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        remoteMessage?.notification?.let {
            Log.d(Brands.TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.body.toString())
        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        //Here's is where we send the new token to back end
        val jsonObject = JsonObject()
        jsonObject.addProperty("token", token)
        val call = RetrofitClient(applicationContext).serverDataApi
            .updateUser(getUserToken(userInfo!!)!!, jsonObject)

        Log.i("FirebaseServiceRes","sending Token")
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.i("FirebaseServiceRes", response.body().toString())
                }
            }

        })

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

