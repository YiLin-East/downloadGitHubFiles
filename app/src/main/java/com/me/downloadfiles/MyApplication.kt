package com.me.downloadfiles

import android.app.Application

class MyApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationHelp.setApp(this)
    }
}