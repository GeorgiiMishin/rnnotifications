package com.rnnotifications.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage as FBRemoteMessage
import com.rnnotifcations.notifications.BackgroundMessagingHandler

class BackgroundMessagingService : FirebaseMessagingService() {
    private val handler = BackgroundMessagingHandler()

    override fun onMessageReceived(message: FBRemoteMessage) {
        super.onMessageReceived(message)

        handler.onMessageReceived(message.toRemoteMessage())
    }

    override fun onNewToken(token: String) {
        handler.onNewToken(token)
    }
}

