package com.rnnotifications.notifications

import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessaging
import com.rnnotifcations.notifications.RemoteMessage
import com.rnnotifcations.notifications.RemoteMessagingService
import com.rnnotifcations.notifications.TokenResult
import com.google.firebase.messaging.RemoteMessage as FBRemoteMessage

class RemoteMessagingServiceImpl(private val context: Context) : RemoteMessagingService {
    override fun receiveToken(onComplete: (TokenResult) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(TokenResult.Success(task.result))
            } else {
                onComplete(TokenResult.Failure(task.exception))
            }
        }
    }

    override fun tryReadRemoteMessage(intent: Intent): RemoteMessage? = intent.extras?.let {
        if (
            it.getString("google.message_id") != null ||
            it.getString("message_id") != null
        ) {
            return@let FBRemoteMessage(it).toRemoteMessage()
        }

        return null
    }
}

