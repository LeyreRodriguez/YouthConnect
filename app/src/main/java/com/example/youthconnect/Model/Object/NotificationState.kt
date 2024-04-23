package com.example.youthconnect.Model.Object

data class NotificationState(
    val isEnteringToken: Boolean = true,
    val remoteToken: String = "",
    val messageText: String = ""
)