package com.rnnotifications.notifications

import com.huawei.hms.push.HmsMessageService
import com.rnnotifcations.notifications.BackgroundMessagingHandler
import com.huawei.hms.push.RemoteMessage as HmsRemoteMessage

class BackgroundMessagingService : HmsMessageService() {
    private val handler = BackgroundMessagingHandler()

    override fun onMessageReceived(message: HmsRemoteMessage?) {
        super.onMessageReceived(message)

        val preparedMessage = message?.toRemoteMessage() ?: return
        handler.onMessageReceived(preparedMessage)
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)

        token ?: return
        handler.onNewToken(token)
    }
}

