package com.rnnotifications

import com.facebook.react.ReactPackage
import io.invertase.firebase.app.ReactNativeFirebaseAppPackage
import io.invertase.firebase.messaging.ReactNativeFirebaseMessagingPackage

fun ArrayList<ReactPackage>.pushStoreDependencies() {
    add(ReactNativeFirebaseAppPackage())
    add(ReactNativeFirebaseMessagingPackage())
}