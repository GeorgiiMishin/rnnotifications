package com.rnnotifcations.notifications

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.rnnotifications.notifications.RemoteMessagingServiceImpl

class RemoteMessagingModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext), RemoteMessagingSubscriber, ActivityEventListener {
    private val remoteMessaging = RemoteMessagingServiceImpl(reactContext)
    override fun getName(): String = "RemoteMessagingModule"

    @ReactMethod
    fun getToken(promise: Promise) {
        remoteMessaging.receiveToken { result ->
            when (result) {
                is TokenResult.Success -> {
                    promise.resolve(result.token)
                }

                is TokenResult.Failure -> {
                    promise.reject(result.exception ?: Exception("Token not received"))
                }
            }
        }
    }

    @ReactMethod
    fun getInitialNotification(promise: Promise) {
        currentActivity?.intent?.let {
            remoteMessaging.tryReadRemoteMessage(it)
        }?.let {
            promise.resolve(it.toWritableMap())
        }
    }

    private fun sendEvent(
        eventName: String,
        params: WritableMap?
    ) {
        if (!reactApplicationContext.hasActiveReactInstance()) {
            return
        }

        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    override fun onEvent(event: RemoteMessagingEvent) {
        when (event) {
            is RemoteMessagingEvent.NewToken -> {
                sendEvent("NewToken", Arguments.createMap().apply {
                    putString("token", event.token)
                })
            }

            is RemoteMessagingEvent.NewMessage -> {
                sendEvent("NewMessage", event.message.toWritableMap())
            }
        }
    }

    @ReactMethod
    fun addListener(eventName: String?) {
    }

    @ReactMethod
    fun removeListeners(count: Int?) {
    }

    override fun onNewIntent(intent: Intent?) {
        intent ?: return

        remoteMessaging.tryReadRemoteMessage(intent)?.let { remoteMessage ->
            sendEvent("MessageOpened", remoteMessage.toWritableMap())
        }
    }

    override fun initialize() {
        super.initialize()
        RemoteMessagingEventEmitter.shared.subscribe(this)
    }

    override fun invalidate() {
        super.invalidate()
        RemoteMessagingEventEmitter.shared.unsubscribe(this)
    }

    override fun onActivityResult(p0: Activity?, p1: Int, p2: Int, p3: Intent?) {

    }

}