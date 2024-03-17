package com.me.downloadfiles.retrofit2

import android.app.Application
import com.me.downloadfiles.ApplicationHelp
import java.io.File

object AddressData {

    val imageCacheFileAddress by lazy { File(ApplicationHelp.getApp()?.filesDir,"image_cache") }
    val backgroundFileAddress by lazy { File(ApplicationHelp.getApp()?.filesDir,"image_background") }
}