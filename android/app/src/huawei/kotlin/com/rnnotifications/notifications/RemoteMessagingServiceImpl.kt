package com.rnnotifications.notifications

import android.content.Context
import android.content.Intent
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.hms.aaid.HmsInstanceId
import com.rnnotifcations.notifications.RemoteMessage
import com.rnnotifcations.notifications.RemoteMessagingService
import com.rnnotifcations.notifications.TokenResult
import com.huawei.hms.push.RemoteMessage as HmsRemoteMessage

class RemoteMessagingServiceImpl(private val context: Context) : RemoteMessagingService {
    override fun receiveToken(onComplete: (result: TokenResult) -> Unit) {
        try {
            val appId =
                AGConnectOptionsBuilder().build(context).getString("client/app_id")

            val token = HmsInstanceId.getInstance(context).getToken(appId, "HSM")
            onComplete(TokenResult.Success(token))
        } catch (ex: Exception) {
            onComplete(TokenResult.Failure(ex))
        }
    }

    override fun tryReadRemoteMessage(intent: Intent): RemoteMessage? =
        intent.extras?.let {
            HmsRemoteMessage(it).toRemoteMessage()
        }
}

