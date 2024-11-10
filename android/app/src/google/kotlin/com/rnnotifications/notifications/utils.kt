package com.rnnotifications.notifications

import com.google.firebase.messaging.RemoteMessage as FBRemoteMessage
import com.rnnotifcations.notifications.Notification
import com.rnnotifcations.notifications.RemoteMessage
import java.util.UUID

fun getDefaultId() = UUID.randomUUID().toString()

fun FBRemoteMessage.Notification.toNotification(): Notification =
    Notification(title = title ?: "", body = body ?: "", channelId = channelId ?: "default")

fun FBRemoteMessage.toRemoteMessage(): RemoteMessage {
    val id = messageId ?: data["id"] ?: getDefaultId()

    return RemoteMessage(
        id = id,
        data = data,
        notification = notification?.toNotification()
    )
}
