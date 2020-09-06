package com.poilkar.nehank.pushmotifications.api

import com.poilkar.nehank.pushmotifications.model.PushNotification
import com.poilkar.nehank.pushmotifications.util.Constants.Companion.CONTENT_TYPEE
import com.poilkar.nehank.pushmotifications.util.Constants.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPEE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}