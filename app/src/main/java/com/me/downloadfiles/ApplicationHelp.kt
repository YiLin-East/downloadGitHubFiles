package com.me.downloadfiles

import android.app.Application

object ApplicationHelp {
    private var app: Application? = null
    fun setApp(context:Application){
        app = context
    }

    fun getApp() = app
}