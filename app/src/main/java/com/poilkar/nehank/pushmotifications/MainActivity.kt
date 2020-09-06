package com.poilkar.nehank.pushmotifications

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.poilkar.nehank.pushmotifications.api.RetrofitInstance
import com.poilkar.nehank.pushmotifications.model.NotificationData
import com.poilkar.nehank.pushmotifications.model.PushNotification
import com.poilkar.nehank.pushmotifications.service.FirebaseService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        FirebaseService.sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {

            FirebaseService.token = it.token
            Log.d("Nehankkkkk", "onNewToken:::  ${it.token} ")
        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        btnSend.setOnClickListener {
            val title = etTitle.text.trim().toString()
            val message = etMessage.text.trim().toString()

            if(title.isNotEmpty() && message.isNotEmpty()){
                PushNotification(
                    NotificationData(title, message),
                    etToken.text.trim().toString()
                ).also {
                    sendNotification(it)
                }
            }
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {

        try{
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d("TAG", "sendNotificationnn: Response SuccessFul")
            }else{
                Log.d("TAG", "sendNotificationnn: Response UnSuccessful")
            }
        }catch (ex: Exception){
            Log.d("TAG", "sendNotificationnn: Exception")
        }

    }
}