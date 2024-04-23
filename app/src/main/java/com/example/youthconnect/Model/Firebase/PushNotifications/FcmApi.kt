package com.example.youthconnect.Model.Firebase.PushNotifications

import com.example.youthconnect.Model.Object.SendNotificationDto
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {

    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendNotificationDto
    )

    @POST("/broadcast")
    suspend fun broadcast(
        @Body body: SendNotificationDto
    )
}