package com.rnnotifcations.notifications

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap

fun Map<String, String>.toWritableMap(): WritableMap = Arguments.createMap().apply {
    forEach { entry ->
        putString(entry.key, entry.value)
    }
}

fun Notification.toWritableMap(): WritableMap = Arguments.createMap().apply {
    putString("title", title)
    putString("body", body)
    putString("channelId", channelId)
}

fun RemoteMessage.toWritableMap(): WritableMap = Arguments.createMap().apply {
    putString("id", id)
    putMap("data", data.toWritableMap())
    putMap("notification", notification?.toWritableMap())
}