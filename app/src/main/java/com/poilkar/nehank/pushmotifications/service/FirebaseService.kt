package com.poilkar.nehank.pushmotifications.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.poilkar.nehank.pushmotifications.MainActivity
import com.poilkar.nehank.pushmotifications.R
import kotlin.random.Random


const val CHANNEL_ID = "my_channel"

class FirebaseService: FirebaseMessagingService() {

    companion object{
        var sharedPreferences: SharedPreferences ?= null

        var token : String ?
        get() {
            return sharedPreferences?.getString("token", "token")
        }
        set(value) {
            sharedPreferences?.edit()?.putString("token",value)?.apply()
        }
    }


    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

        token = newToken

        Log.d("Nehankkkkk", "onNewToken:::  $newToken ")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


        showNotification(message)

    }

    private fun showNotification(message: RemoteMessage) {
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_icon)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId,notification)



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){

        val channelName = "MyChannelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "Channel Description"
            enableLights(true)
            lightColor = Color.BLUE
        }
        notificationManager.createNotificationChannel(channel)
    }
}