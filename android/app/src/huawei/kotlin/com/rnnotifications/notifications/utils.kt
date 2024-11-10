package com.rnnotifications.notifications

import com.rnnotifcations.notifications.Notification
import com.rnnotifcations.notifications.RemoteMessage
import org.json.JSONObject
import java.util.UUID
import com.huawei.hms.push.RemoteMessage as HmsRemoteMessage

private fun JSONObject.getStringSafe(key: String): String? {
    return try {
        getString(key)
    } catch (ex: Throwable) {
        null
    }
}

private fun mapFromString(string: String): Map<String, String> {
    try {
        val jsonObject = JSONObject(string)
        val result = mutableMapOf<String, String>()

        jsonObject.keys().forEach { key ->
            val value = jsonObject.getStringSafe(key) ?: return@forEach

            result[key] = value
        }

        return result
    } catch (ex: Throwable) {
        return mapOf()
    }
}


fun HmsRemoteMessage.Notification.toNotification(): Notification =
    Notification(title = title ?: "", body = body ?: "", channelId = channelId ?: "default")

fun HmsRemoteMessage.toRemoteMessage(): RemoteMessage {
    val data = mapFromString(data)
    val id = messageId ?: data["id"] ?: UUID.randomUUID().toString()
    val notification = notification?.toNotification()

    return RemoteMessage(id, data, notification)
}