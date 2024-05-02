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
import java.util.Properties

class NotificationViewModel : ViewModel() {
    var state by mutableStateOf(NotificationState())
        private set

    private lateinit var baseUrl: String
    private val api: FcmApi by lazy {
        val properties = Properties()
        val inputStream = javaClass.classLoader.getResourceAsStream("app.properties")
        properties.load(inputStream)
        baseUrl = properties.getProperty("BASE_URL") ?: throw IllegalArgumentException("BASE_URL not set")

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    init {
        viewModelScope.launch {
            try {
                Firebase.messaging.subscribeToTopic("notification").await()
            } catch (e: IOException) {
                state = state.copy(errorMessage = "Error subscribing to topic: ${e.message}")
            }
        }
    }

    fun onRemoteTokenChange(newToken: String) {
        state = state.copy(
            remoteToken = newToken
        )
    }

    fun onSubmitRemoteToken() {
        state = state.copy(
            isEnteringToken = false
        )
    }

    fun onMessageChange(message: String) {
        state = state.copy(
            messageText = message
        )
    }

    fun sendMessage(isBroadcast: Boolean, item: News) {
        viewModelScope.launch {
            val messageDto = SendNotificationDto(
                to = if (isBroadcast) "/topics/notification" else state.remoteToken,
                notification = NotificationBody(
                    title = item.title,
                    body = item.description
                )
            )

            try {
                api.sendMessage(messageDto)
                state = state.copy(
                    messageText = ""
                )
            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    401 -> "Unauthorized"
                    404 -> "Not found"
                    else -> "Error: ${e.message()}"
                }
                state = state.copy(errorMessage = errorMessage)
            } catch (e: IOException) {
                state = state.copy(errorMessage = "Network error: ${e.message}")
            }
        }
    }
}
