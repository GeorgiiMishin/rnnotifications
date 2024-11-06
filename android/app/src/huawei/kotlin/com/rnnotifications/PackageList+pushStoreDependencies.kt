package com.rnnotifications

import com.facebook.react.ReactPackage
import com.huawei.hms.rn.push.HmsPushPackage

fun ArrayList<ReactPackage>.pushStoreDependencies() {
    add(HmsPushPackage())
}