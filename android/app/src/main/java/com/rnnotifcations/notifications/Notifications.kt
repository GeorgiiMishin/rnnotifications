package com.rnnotifcations.notifications

import android.content.Intent
import java.lang.Exception

data class Notification(
    val title: String,
    val body: String,
    val channelId: String
)

data class RemoteMessage(
    val id: String,
    val data: Map<String, String>,
    val notification: Notification? = null
)

sealed class TokenResult {
    data class Success(val token: String) : TokenResult()
    data class Failure(val exception: Exception?) : TokenResult()
}

interface RemoteMessagingService {
    fun receiveToken(onComplete: (result: TokenResult) -> Unit)
    fun tryReadRemoteMessage(intent: Intent): RemoteMessage?
}

