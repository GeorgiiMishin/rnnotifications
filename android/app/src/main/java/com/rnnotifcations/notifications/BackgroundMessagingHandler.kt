package com.rnnotifcations.notifications

class BackgroundMessagingHandler {
    fun onMessageReceived(message: RemoteMessage) {
        RemoteMessagingEventEmitter.shared.notify(RemoteMessagingEvent.NewMessage(message))
    }

    fun onNewToken(token: String) {
        RemoteMessagingEventEmitter.shared.notify(RemoteMessagingEvent.NewToken(token))
    }
}

