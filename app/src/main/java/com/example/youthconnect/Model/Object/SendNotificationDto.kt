package com.example.youthconnect.Model.Object

import kotlinx.serialization.Serializable

@Serializable
data class SendNotificationDto(
    val to: String?,
    val notification: NotificationBody
)
@Serializable

data class NotificationBody(
    val title: String,
    val body: String
)