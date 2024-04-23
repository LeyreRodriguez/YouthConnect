package com.example.youthconnect.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Firebase.PushNotifications.FcmApi
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Object.NotificationState
import com.example.youthconnect.Model.Object.NotificationBody
import com.example.youthconnect.Model.Object.SendNotificationDto
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.IOException

class NotificationViewModel : ViewModel() {
    var state by mutableStateOf(NotificationState())
        private set

    private val api: FcmApi = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()

    init {
        viewModelScope.launch {
            Firebase.messaging.subscribeToTopic("notification").await()
        }
    }

    fun sendMessage(isBroadcast: Boolean, news : News) {
        viewModelScope.launch {
            val messageDto = SendNotificationDto(
                to = if(isBroadcast) "/topics/notification" else state.remoteToken,
                notification = NotificationBody(
                    title = news.title,
                    body = news.description
                )
            )

            try {
                api.sendMessage(messageDto)

                state = state.copy(
                    messageText = ""
                )
            } catch(e: HttpException) {
                e.printStackTrace()
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }
}