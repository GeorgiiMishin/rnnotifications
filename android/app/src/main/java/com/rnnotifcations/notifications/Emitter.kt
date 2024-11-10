package com.rnnotifcations.notifications

sealed class RemoteMessagingEvent {
    class NewToken(val token: String) : RemoteMessagingEvent()
    class NewMessage(val message: RemoteMessage) : RemoteMessagingEvent()
}

interface RemoteMessagingSubscriber {
    fun onEvent(event: RemoteMessagingEvent)
}

class RemoteMessagingEventEmitter {
    companion object {
        val shared = RemoteMessagingEventEmitter()
    }

    private val subscribers = mutableListOf<RemoteMessagingSubscriber>()

    fun subscribe(subscriber: RemoteMessagingSubscriber) {
        subscribers.add(subscriber)
    }

    fun unsubscribe(subscriber: RemoteMessagingSubscriber) {
        subscribers.remove(subscriber)
    }

    fun notify(event: RemoteMessagingEvent) {
        subscribers.forEach {
            it.onEvent(event)
        }
    }
}

